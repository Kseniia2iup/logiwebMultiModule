package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.City;

import java.util.List;

public interface CityService {

    City findCityById(int id) throws TruckingServiceException;

    City findCityByName(String name) throws TruckingServiceException;

    List<City> findAllCities() throws TruckingServiceException;

    Double distanceBetweenTwoCities(City cityA, City cityB);
}
