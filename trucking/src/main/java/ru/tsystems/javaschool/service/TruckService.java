package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.NoCargoInTheOrderException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;

import java.util.List;

public interface TruckService {

    Truck findTruckById(int id) throws TruckingServiceException;

    Truck findTruckByRegNumber(String regNumber) throws TruckingServiceException;

    void saveTruck(Truck truck) throws TruckingServiceException;

    void deleteTruckByRegNumber(String regNumber) throws TruckingServiceException;

    List<Truck> findAllTrucks() throws TruckingServiceException;

    void updateTruck(Truck truck) throws TruckingServiceException;

    boolean isTruckRegNumberUnique(Integer id, String regNumber) throws TruckingServiceException;

    boolean isTruckRegNumberIsValid(Integer id, String regNumber);

    List<Truck> findAllTrucksReadyForOrder(Order order)
            throws TruckingServiceException, NoCargoInTheOrderException;

    void markTruckAsBrokenWhileOrder(Integer id) throws TruckingServiceException;

    List<Truck> findAllBrokenTrucks(List<Truck> allTrucks) throws TruckingServiceException;

    List<Truck> findAllTrucksOnOrder(List<Truck> allTrucks) throws TruckingServiceException;
}
