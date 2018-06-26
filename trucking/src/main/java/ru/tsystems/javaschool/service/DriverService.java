package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.DriverStatus;

import java.util.List;

public interface DriverService {

    Driver findDriverById(Integer id) throws TruckingServiceException;

    void deleteDriver(Integer id) throws TruckingServiceException;

    void saveDriver(Driver driver) throws TruckingServiceException;

    void updateDriver(Driver driver) throws TruckingServiceException;

    List<Driver> findAllDrivers() throws TruckingServiceException;

    Integer getMaxDriverId() throws TruckingServiceException;

    String generateDriverLogin(Driver driver) throws TruckingServiceException;

    String generateDriverPassword();

    List<Driver> findAllDriversSuitableForOrder(Order order) throws TruckingServiceException;

    List<Driver> getAllDriversOfTruck(Truck truck) throws TruckingServiceException;

    List<Driver> getAllDriversOfOrder(Order order) throws TruckingServiceException;

    List<Driver> findCoWorkers(Driver driver) throws TruckingServiceException;

    void setDriverStatus(Driver driver, DriverStatus newStatus) throws TruckingServiceException;

    Driver setHoursOfWorkDependsOnStatusChanging(Driver driver, DriverStatus newStatus) throws TruckingServiceException;

    List<Driver> findAllFreeDrivers(List<Driver> allDrivers) throws TruckingServiceException;

    void sendSuccessRegistrationEmail(String email, String login, String password) throws  TruckingServiceException;
}
