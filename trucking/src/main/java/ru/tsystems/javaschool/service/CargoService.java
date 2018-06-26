package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.CargoAlreadyDeliveredException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.enums.CargoStatus;

import java.util.List;

public interface CargoService {

    Cargo findCargoById(Integer id) throws TruckingServiceException;

    void deleteCargo(Integer id) throws TruckingServiceException;

    void saveCargo(Cargo cargo) throws TruckingServiceException;

    void updateCargo(Cargo cargo) throws TruckingServiceException;

    List<Cargo> findAllCargoesOfOrder(Integer orderId) throws TruckingServiceException;

    String setCargoStatus(Cargo cargo, CargoStatus newStatus)
            throws TruckingServiceException, CargoAlreadyDeliveredException;
}
