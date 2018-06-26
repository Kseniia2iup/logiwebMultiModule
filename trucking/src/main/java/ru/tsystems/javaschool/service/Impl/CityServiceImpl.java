package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.City;
import ru.tsystems.javaschool.repository.CityDao;
import ru.tsystems.javaschool.service.CityService;

import java.util.List;

@Service("cityService")
@Transactional
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    private static final Double EARTH_RADIUS = 6371d;

    private CityDao cityDao;

    @Autowired
    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public City findCityById(int id) throws TruckingServiceException {
        try {
            return cityDao.findCityById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public City findCityByName(String name) throws TruckingServiceException {
        try {
            return cityDao.findCityByName(name);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<City> findAllCities() throws TruckingServiceException {
        try {
            return cityDao.findAllCities();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public Double distanceBetweenTwoCities(City cityA, City cityB) {

        if(cityA.equals(cityB)){
            return 0d;
        }

        Double latitudeA = cityA.getLatitude();
        Double longitudeA = cityA.getLongitude();
        Double latitudeB = cityB.getLatitude();
        Double longitudeB = cityB.getLongitude();

        return EARTH_RADIUS*2*Math.asin(Math.sqrt(Math.pow(Math.sin((
                latitudeA-Math.abs(latitudeB))*Math.PI/180/2),2)+Math.cos(latitudeA*Math.PI/180)
                *Math.cos(Math.abs(latitudeB)*Math.PI/180)*Math.pow(Math.sin((longitudeA-longitudeB)
                *Math.PI/180/2),2)));
    }
}
