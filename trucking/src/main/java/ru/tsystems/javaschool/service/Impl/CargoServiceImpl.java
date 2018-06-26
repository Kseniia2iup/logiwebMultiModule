package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.CargoAlreadyDeliveredException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.*;
import ru.tsystems.javaschool.dto.enums.CargoStatus;
import ru.tsystems.javaschool.dto.enums.OrderStatus;
import ru.tsystems.javaschool.repository.CargoDao;
import ru.tsystems.javaschool.service.*;

import java.util.List;

@Service("cargoService")
@Transactional
public class CargoServiceImpl implements CargoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CargoServiceImpl.class);

    private CargoDao cargoDao;

    private OrderService orderService;

    private TruckService truckService;

    private CityService cityService;

    private InfoBoardService infoBoardService;

    private WaypointService waypointService;

    @Autowired
    public void setWaypointService(WaypointService waypointService) {
        this.waypointService = waypointService;
    }

    @Autowired
    public void setInfoBoardService(InfoBoardService infoBoardService) {
        this.infoBoardService = infoBoardService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setTruckService(TruckService truckService) {
        this.truckService = truckService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setCargoDao(CargoDao cargoDao) {
        this.cargoDao = cargoDao;
    }

    @Override
    public Cargo findCargoById(Integer id) throws TruckingServiceException {
        try {
            return cargoDao.findCargoById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void deleteCargo(Integer id) throws TruckingServiceException {
        try {
            cargoDao.deleteCargo(id);
        } catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void saveCargo(Cargo cargo) throws TruckingServiceException {
        try {
            cargoDao.saveCargo(cargo);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void updateCargo(Cargo cargo) throws TruckingServiceException {
        try {
            cargoDao.updateCargo(cargo);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Cargo> findAllCargoesOfOrder(Integer orderId) throws TruckingServiceException {
        try {
            return cargoDao.findAllCargoesOfOrder(orderId);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Update status of Cargo and status of Order if all Cargoes have already delivered
     * @param cargo with old status
     * @param newStatus to change
     * @return String with new Status and Status of Order if it changed
     */
    @Override
    public String setCargoStatus(Cargo cargo, CargoStatus newStatus)
            throws TruckingServiceException, CargoAlreadyDeliveredException {
        try {
            Cargo entityCargo = findCargoById(cargo.getId());
            CargoStatus oldStatus = entityCargo.getDelivery_status();
            Order order = orderService.findOrderById(entityCargo.getOrder().getId());
            Truck truck = truckService.findTruckById(order.getTruck().getId());
            if (oldStatus.equals(CargoStatus.DELIVERED)
                    || cargo.getOrder().getOrderStatus().equals(OrderStatus.DONE)) {
                throw new CargoAlreadyDeliveredException("Cargo ID: " + entityCargo.getId());
            }
            if (newStatus.equals(CargoStatus.SHIPPED)) {
                City currentProcessCity = cityService.findCityById(entityCargo.getWaypoint().getCityDep().getId());
                truck.setCity(currentProcessCity);
                truckService.updateTruck(truck);

                entityCargo.setDelivery_status(newStatus);
                cargoDao.updateCargo(entityCargo);
                return "shipped";
            } else {
                City currentProcessCity = cityService.findCityById(entityCargo.getWaypoint().getCityDest().getId());
                truck.setCity(currentProcessCity);
                truckService.updateTruck(truck);

                entityCargo.setDelivery_status(newStatus);
                cargoDao.updateCargo(entityCargo);
                List<Cargo> cargoes = cargoDao.findAllCargoesOfOrder(cargo.getOrder().getId());
                int count = 0;
                for (Cargo cargoFromList : cargoes
                        ) {
                    if (cargoFromList.getDelivery_status().equals(CargoStatus.DELIVERED)) {
                        count++;
                    }
                }
                if (cargoes.size() == count) {
                    order.setOrderStatus(OrderStatus.DONE);
                    orderService.updateOrder(order);
                    orderService.removeTruckAndDriversFromOrder(order);
                    return "done";
                } else {
                    return "delivered";
                }
            }
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }
}
