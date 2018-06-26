package ru.tsystems.javaschool.service.Impl;

import org.junit.Test;
import org.mockito.InjectMocks;
import ru.tsystems.javaschool.dto.City;
import ru.tsystems.javaschool.service.CityService;

import static org.junit.Assert.assertTrue;

public class CityServiceImplTest {

    @InjectMocks
    private CityService cityService = new CityServiceImpl();

    @Test
    public void distanceBetweenTwoCitiesTest(){
        City cityA = new City();
        cityA.setName("Moscow");
        cityA.setLatitude(55.755773);
        cityA.setLongitude(37.617761);

        City cityB = new City();
        cityB.setName("Saint-Petersburg");
        cityB.setLatitude(59.938806);
        cityB.setLongitude(30.314278);

        assertTrue(cityService.distanceBetweenTwoCities(cityA, cityB)>630
                && cityService.distanceBetweenTwoCities(cityA, cityB)<640 );
    }
}
