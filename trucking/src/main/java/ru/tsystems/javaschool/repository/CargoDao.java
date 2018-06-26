package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Cargo;

import java.util.List;

public interface CargoDao {

    Cargo findCargoById(Integer id) throws TruckingDaoException;

    void deleteCargo(Integer id) throws TruckingDaoException;

    void saveCargo(Cargo cargo) throws TruckingDaoException;

    void updateCargo(Cargo cargo) throws TruckingDaoException;

    List<Cargo> findAllCargoesOfOrder(Integer orderId) throws TruckingDaoException;
}
