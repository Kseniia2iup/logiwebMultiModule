package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.dto.InfoDto;
import ru.tsystems.javaschool.dto.OrderDto;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.service.*;

import java.util.ArrayList;
import java.util.List;

@Service("infoBoardService")
@Transactional
public class InfoBoardServiceImpl implements InfoBoardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoBoardServiceImpl.class);

    private AmqpTemplate template;

    private OrderService orderService;

    private TruckService truckService;

    private DriverService driverService;

    private CargoService cargoService;

    @Autowired
    public void setTemplate(AmqpTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setTruckService(TruckService truckService) {
        this.truckService = truckService;
    }

    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @Autowired
    public void setCargoService(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @Override
    public void sendInfoToQueue() throws TruckingServiceException {
        try {
            LOGGER.info("Information about changes has successfully sent to the infoBoardQueue");
            template.convertAndSend("infoBoardQueue", "update");
        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public InfoDto getJSONInfoForUpdate() throws TruckingServiceException {
        try {
            InfoDto boardInfo = new InfoDto();

            List<Driver> listDrivers = driverService.findAllDrivers();

            if(listDrivers!=null){
                boardInfo.setAllDrivers(listDrivers.size());
                List<Driver> freeDrivers = driverService.findAllFreeDrivers(listDrivers);
                boardInfo.setFreeDrivers(freeDrivers.size());
                boardInfo.setDriversOnOrder(listDrivers.size()-freeDrivers.size());
            }
            else {
                boardInfo.setAllDrivers(0);
                boardInfo.setDriversOnOrder(0);
                boardInfo.setFreeDrivers(0);
            }

            List<Truck> listTrucks = truckService.findAllTrucks();

            if(listTrucks!=null){
                boardInfo.setAllTrucks(listTrucks.size());
                List<Truck> trucksOnOrder = truckService.findAllTrucksOnOrder(listTrucks);
                List<Truck> brokenTrucks = truckService.findAllBrokenTrucks(listTrucks);
                boardInfo.setTrucksOnOrder(trucksOnOrder.size());
                boardInfo.setBrokenTrucks(brokenTrucks.size());
                boardInfo.setAvailableTrucks(listTrucks.size()-brokenTrucks.size()-trucksOnOrder.size());
            }
            else{
                boardInfo.setAllTrucks(0);
                boardInfo.setTrucksOnOrder(0);
                boardInfo.setBrokenTrucks(0);
                boardInfo.setAvailableTrucks(0);
            }

            boardInfo.setLastTenOrders(prepareOrdersInfo());

            LOGGER.info("JSON Data has successfully sent");
            return boardInfo;

        } catch (TruckingServiceException e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        } catch (AmqpException e) {
            LOGGER.warn("Something went wrong\n", e);
            throw  new AmqpException(e);
        }
    }

    /**
     * Finds last ten orders from DB, prepares each one for sending and puts to the List
     * @return List of OrderDtos
     * @throws TruckingServiceException if something went wrong on the Service level
     */
    private List<OrderDto> prepareOrdersInfo() throws TruckingServiceException{
        try {
            List<Order> orders = orderService.findLastTenOrders();
            if(orders==null){
                return new ArrayList<>();
            }

            List<OrderDto> result = new ArrayList<>();
            for (Order order: orders
                 ) {
                OrderDto orderToSend = new OrderDto();
                orderToSend.setId(order.getId());
                orderToSend.setOrderStatus(order.getOrderStatus().toString());
                List<Cargo> cargoes = cargoService.findAllCargoesOfOrder(order.getId());
                if(cargoes==null){
                    orderToSend.setCargoes("");
                }
                else {
                    orderToSend.setCargoes(cargoesOfOrder(cargoes));
                }
                if(order.getTruck()==null){
                    orderToSend.setTruck("");
                    orderToSend.setDrivers("");
                }
                else{
                    orderToSend.setTruck(order.getTruck().getRegNumber());
                    List<Driver> drivers = driverService.getAllDriversOfOrder(order);
                    if (drivers==null){
                        orderToSend.setDrivers("");
                    }
                    else{
                        orderToSend.setDrivers(driversOfOrder(drivers));
                    }
                }
                result.add(orderToSend);
            }

            return result;
        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }


    /**
     * Transforms List of Drivers to the String of their names and surnames
     * @param driversOfOrder List of all drivers of Order
     * @return String with names and surnames of drivers from the List
     * @throws TruckingServiceException if something went wrong on the Service level
     */
    @Override
    public String driversOfOrder(List<Driver> driversOfOrder) throws TruckingServiceException {
        try {
            if(driversOfOrder.isEmpty()){
                return "";
            }
            StringBuilder driversNames = new StringBuilder();

            for (Driver driver : driversOfOrder
                    ) {
                driversNames.append(driver.getName());
                driversNames.append(" ");
                driversNames.append(driver.getSurname());
                driversNames.append(", ");
            }
            driversNames.deleteCharAt(driversNames.length()-1);
            driversNames.deleteCharAt(driversNames.length()-1);
            return driversNames.toString();

        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Transforms List of Cargoes to the String of their names
     * @param cargoes List of Cargoes of the Order
     * @return String with names of cargoes from the List
     * @throws TruckingServiceException if something went wrong on the Service level
     */
    @Override
    public String cargoesOfOrder(List<Cargo> cargoes) throws TruckingServiceException {
        try {
            if(cargoes.isEmpty()){
                return "";
            }

            StringBuilder cargoesNames = new StringBuilder();

            for (Cargo cargo : cargoes
                    ) {
                cargoesNames.append(cargo.getName());
                cargoesNames.append(", ");
            }
            cargoesNames.deleteCharAt(cargoesNames.length()-1);
            cargoesNames.deleteCharAt(cargoesNames.length()-1);
            return cargoesNames.toString();

        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }
}
