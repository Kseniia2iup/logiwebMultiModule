package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.NoCargoInTheOrderException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.CargoStatus;
import ru.tsystems.javaschool.dto.enums.OrderStatus;
import ru.tsystems.javaschool.dto.enums.TruckStatus;
import ru.tsystems.javaschool.repository.CargoDao;
import ru.tsystems.javaschool.repository.OrderDao;
import ru.tsystems.javaschool.repository.TruckDao;
import ru.tsystems.javaschool.service.InfoBoardService;
import ru.tsystems.javaschool.service.OrderService;
import ru.tsystems.javaschool.service.TruckService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("truckService")
@Transactional
public class TruckServiceImpl implements TruckService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckServiceImpl.class);

    private TruckDao truckDao;

    private OrderDao orderDao;

    private OrderService orderService;

    private CargoDao cargoDao;

    private InfoBoardService infoBoardService;

    @Autowired
    public void setInfoBoardService(InfoBoardService infoBoardService) {
        this.infoBoardService = infoBoardService;
    }

    @Autowired
    public void setCargoDao(CargoDao cargoDao) {
        this.cargoDao = cargoDao;
    }

    @Autowired
    public void setTruckDao(TruckDao truckDao) {
        this.truckDao = truckDao;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Truck findTruckById(int id) throws TruckingServiceException {
        try {
            return truckDao.findTruckById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public Truck findTruckByRegNumber(String regNumber) throws TruckingServiceException {
        try {
            return truckDao.findTruckByRegNumber(regNumber);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void saveTruck(Truck truck) throws TruckingServiceException {
        try {
            truckDao.saveTruck(truck);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void deleteTruckByRegNumber(String regNumber) throws TruckingServiceException {
        try {
            truckDao.deleteTruckByRegNumber(regNumber);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Truck> findAllTrucks() throws TruckingServiceException {
        try {
            return truckDao.findAllTrucks();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void updateTruck(Truck truck) throws TruckingServiceException {
        try {
            truckDao.updateTruck(truck);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public boolean isTruckRegNumberUnique(Integer id, String regNumber) throws TruckingServiceException {
        try {
            Truck truck = findTruckByRegNumber(regNumber);
            return (truck == null || ((id != null) && (truck.getId() == id)));
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public boolean isTruckRegNumberIsValid(Integer id, String regNumber) {
        Pattern pattern = Pattern.compile("[a-zA-Z]{2}\\d{5}");
        Matcher matcher = pattern.matcher(regNumber);
        return matcher.matches();
    }

    /**
     * Returns all Trucks that have OK condition and enough capacity to carry order's cargoes
     * @param order that needs a truck
     * @return List of Trucks suitable for the order
     */
    @Override
    public List<Truck> findAllTrucksReadyForOrder(Order order)
            throws TruckingServiceException, NoCargoInTheOrderException {
        try {
            List<Cargo> cargoes = cargoDao.findAllCargoesOfOrder(order.getId());
            if ((cargoes == null)||cargoes.isEmpty()){
                throw new NoCargoInTheOrderException("No cargoes in the order " + order.getId());
            }
            int cargoMaxWeightKg = 0;
            for (Cargo cargo : cargoes
                    ) {
                if (!cargo.getDelivery_status().equals(CargoStatus.DELIVERED) &&
                        (cargo.getWeight() > cargoMaxWeightKg)) {
                    cargoMaxWeightKg = cargo.getWeight();
                }
            }
            double maxWeightTon = cargoMaxWeightKg / 1000d;
            List<Truck> result = new ArrayList<>();
            List<Truck> trucks = truckDao.findAllOKTrucks();
            for (Truck truck : trucks
                    ) {
                if (!orderDao.isTruckHasOrder(truck.getId()) && ((double) truck.getCapacityTon() >= maxWeightTon)) {
                    result.add(truck);
                }
            }
            return result;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Set Truck status as FAULT, remove all Drivers and Truck from Order, set Order Status
     * as INTERRUPTED, mark all SHIPPED Cargoes as PREPARED and change their cities of
     * departure to the Truck's current City
     * @param id of Truck that was broken
     */
    @Override
    public void markTruckAsBrokenWhileOrder(Integer id)  throws TruckingServiceException{
        try {
            Truck entityTruck = truckDao.findTruckById(id);
            Order order = entityTruck.getOrder();
            List<Cargo> cargoes = cargoDao.findAllCargoesOfOrder(order.getId());
            for (Cargo cargo : cargoes
                    ) {
                if (cargo.getDelivery_status().equals(CargoStatus.SHIPPED)) {
                    cargo.setDelivery_status(CargoStatus.PREPARED);
                    cargo.getWaypoint().setCityDep(entityTruck.getCity());
                    cargoDao.updateCargo(cargo);
                }
            }

            orderService.removeTruckAndDriversFromOrder(order);
            order.setOrderStatus(OrderStatus.INTERRUPTED);
            orderService.updateOrder(order);

            entityTruck.setCondition(TruckStatus.FAULTY);
            truckDao.updateTruck(entityTruck);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Truck> findAllBrokenTrucks(List<Truck> allTrucks) throws TruckingServiceException {
        try {
            if(allTrucks==null){
                return new ArrayList<>();
            }

            List<Truck> result = new ArrayList<>();

            for (Truck truck: allTrucks
                 ) {
                if(truck.getCondition().equals(TruckStatus.FAULTY)){
                    result.add(truck);
                }
            }
            return result;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Truck> findAllTrucksOnOrder(List<Truck> allTrucks) throws TruckingServiceException {
        try {
            if(allTrucks==null){
                return new ArrayList<>();
            }

            List<Truck> result = new ArrayList<>();

            for (Truck truck: allTrucks
                    ) {
                if(truck.getOrder()!=null){
                    result.add(truck);
                }
            }
            return result;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }
}
