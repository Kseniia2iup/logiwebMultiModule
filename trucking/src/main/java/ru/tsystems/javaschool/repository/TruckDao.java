package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Truck;

import java.util.List;

public interface TruckDao {

    Truck findTruckById(int id) throws TruckingDaoException;

    Truck findTruckByRegNumber(String regNumber) throws TruckingDaoException;

    void saveTruck(Truck truck) throws TruckingDaoException;

    void updateTruck(Truck truck) throws TruckingDaoException;

    void deleteTruckByRegNumber(String regNumber) throws TruckingDaoException;

    List<Truck> findAllTrucks() throws TruckingDaoException;

    List<Truck> findAllOKTrucks() throws TruckingDaoException;
}
