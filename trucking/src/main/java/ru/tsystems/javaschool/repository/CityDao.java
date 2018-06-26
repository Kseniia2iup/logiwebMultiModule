package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.City;

import java.util.List;

public interface CityDao {

    City findCityById(int id) throws TruckingDaoException;

    City findCityByName(String name) throws TruckingDaoException;

    List<City> findAllCities() throws TruckingDaoException;
}
