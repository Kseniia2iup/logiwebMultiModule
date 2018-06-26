package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;

import java.util.List;

public interface DriverDao {

    Driver findDriverById(Integer id) throws TruckingDaoException;

    void deleteDriver(Integer id) throws TruckingDaoException;

    void saveDriver(Driver driver) throws TruckingDaoException;

    void updateDriver(Driver driver) throws TruckingDaoException;

    List<Driver> findAllDrivers() throws TruckingDaoException;

    Integer getLastDriverId() throws TruckingDaoException;

    List<Driver> getAllFreeDriversForTruck(Truck truck) throws TruckingDaoException;

    List<Driver> getAllDriversOfTruck(Truck truck) throws TruckingDaoException;

    List<Driver> getAllDriversOfOrder(Order order) throws TruckingDaoException;
}
