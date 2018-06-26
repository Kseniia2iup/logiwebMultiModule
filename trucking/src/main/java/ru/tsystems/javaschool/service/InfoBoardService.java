package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.dto.InfoDto;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.Driver;

import java.util.List;

public interface InfoBoardService {

    void sendInfoToQueue() throws TruckingServiceException;

    InfoDto getJSONInfoForUpdate() throws TruckingServiceException;

    String driversOfOrder(List<Driver> driversOfOrder) throws TruckingServiceException;

    String cargoesOfOrder(List<Cargo> cargoes) throws TruckingServiceException;
}
