package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.*;
import ru.tsystems.javaschool.dto.enums.CargoStatus;
import ru.tsystems.javaschool.dto.enums.DriverStatus;
import ru.tsystems.javaschool.repository.*;
import ru.tsystems.javaschool.service.*;

import java.util.ArrayList;
import java.util.List;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderDao orderDao;

    private CityService cityService;

    private DriverDao driverDao;

    private DriverService driverService;

    private TruckDao truckDao;

    private WaypointService waypointService;

    private InfoBoardService infoBoardService;

    private CargoService cargoService;

    @Autowired
    public void setCargoService(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @Autowired
    public void setInfoBoardService(InfoBoardService infoBoardService) {
        this.infoBoardService = infoBoardService;
    }

    @Autowired
    public void setWaypointService(WaypointService waypointService) {
        this.waypointService = waypointService;
    }

    @Autowired
    public void setTruckDao(TruckDao truckDao) {
        this.truckDao = truckDao;
    }

    @Autowired
    public void setDriverDao(DriverDao driverDao) {
        this.driverDao = driverDao;
    }

    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public Order findOrderById(Integer id) throws TruckingServiceException {
        try {
            return orderDao.findOrderById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void deleteOrder(Integer id) throws TruckingServiceException {
        try {
            removeTruckAndDriversFromOrder(findOrderById(id));
            List<Cargo> cargoes = cargoService.findAllCargoesOfOrder(id);

            if (!cargoes.isEmpty()) {
                for (Cargo cargo: cargoes
                     ) {
                    deleteCargo(cargo.getId());
                }
            }
            orderDao.deleteOrder(id);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void saveOrder(Order order) throws TruckingServiceException {
        try {
            orderDao.saveOrder(order);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void updateOrder(Order order) throws TruckingServiceException {
        try {
            orderDao.updateOrder(order);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Order> findAllOrders() throws TruckingServiceException {
        try {
            return orderDao.findAllOrders();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Order> findLastTenOrders() throws TruckingServiceException {
        try {
            return orderDao.findLastTenOrders();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Calculate approximate summary distance based on Set of waypoints in the order
     * @param order to distance calculate
     * @return approximate summary distance between the order's waypoints
     */
    @Override
    public Double calculateSumDistanceOfOrder(Order order) throws TruckingServiceException {
        try {
            List<Waypoint> waypoints = waypointService.findAllWaypointsByOrderId(order.getId());
            if (waypoints.isEmpty()){
                return 0d;
            }
            List<Waypoint> waypointsWithNotDeliveredCargoes = new ArrayList<>();
            for (Waypoint wp : waypoints
                    ) {
                if (!wp.getCargo().getDelivery_status().equals(CargoStatus.DELIVERED)) {
                    waypointsWithNotDeliveredCargoes.add(wp);
                }
            }
            if(waypointsWithNotDeliveredCargoes.isEmpty()){
                return 0d;
            }
            Double sumDistance = 0d;
            City cityA;

            List<City> cities = new ArrayList<>();
            for (Waypoint waypoint : waypointsWithNotDeliveredCargoes
                    ) {
                cities.add(waypoint.getCityDep());
                cities.add(waypoint.getCityDest());
            }
            cityA = cities.get(0);
            for (int i = 1; i < cities.size(); i++) {
                sumDistance += cityService.distanceBetweenTwoCities(cityA, cities.get(i));
                cityA = cities.get(i);
            }
            return sumDistance;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Calculate average time in hours for order complete based on approximate summary distance
     * between the order's waypoints
     * @param order to calculate average time of the order fulfillment
     * @return Integer number of hours
     */
    @Override
    public Integer averageTimeInHoursForOrder(Order order) throws TruckingServiceException {
        try {
            Double averageTruckSpeedInKmPerHour = 70d;
            Double averageTimeOfDrivingInHours = calculateSumDistanceOfOrder(order) / averageTruckSpeedInKmPerHour;
            int averageTimeOfProcessingInHours = 2;
            List<Waypoint> waypoints = waypointService.findAllWaypointsByOrderId(order.getId());
            List<Waypoint> waypointsWithNotDeliveredCargoes = new ArrayList<>();
            for (Waypoint wp : waypoints
                    ) {
                if (!wp.getCargo().getDelivery_status().equals(CargoStatus.DELIVERED)) {
                    waypointsWithNotDeliveredCargoes.add(wp);
                }
            }
            return waypointsWithNotDeliveredCargoes.size() * averageTimeOfProcessingInHours
                    + (int) Math.round(averageTimeOfDrivingInHours);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }

    }

    @Override
    public boolean isTimeOrderExceedsDriversShiftLimit(Order order) throws TruckingServiceException {
        try {
            Integer timeForOrder = averageTimeInHoursForOrder(order);
            return timeForOrder > 176;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void removeTruckAndDriversFromOrder(Order order) throws TruckingServiceException {
        try {
            Order entityOrder = orderDao.findOrderById(order.getId());
            Truck truck = entityOrder.getTruck();
            if (truck != null) {
                List<Driver> drivers = driverDao.getAllDriversOfTruck(truck);
                if (drivers != null) {
                    for (Driver driver : drivers
                            ) {
                        driver.setCurrentTruck(null);
                        driver.setOrder(null);
                        driverService.setDriverStatus(driver, DriverStatus.REST);
                        driverDao.updateDriver(driver);
                    }
                }
                truck.setOrder(null);
                truckDao.updateTruck(truck);
                entityOrder.setTruck(null);
                orderDao.updateOrder(entityOrder);
                infoBoardService.sendInfoToQueue();
            }
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong in the OrderServiceImpl\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void deleteCargo(Integer cargoId) throws TruckingServiceException {
        try {
            Cargo cargo = cargoService.findCargoById(cargoId);
            Waypoint waypoint = waypointService.findWaypointById(cargo.getWaypoint().getId());
            waypoint.setOrder(null);
            waypoint.setCargo(null);
            cargo.setWaypoint(null);
            cargo.setOrder(null);
            cargoService.updateCargo(cargo);
            waypointService.updateWaypoint(waypoint);
            cargoService.deleteCargo(cargoId);
            waypointService.deleteWaypoint(waypoint.getId());
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }
}
