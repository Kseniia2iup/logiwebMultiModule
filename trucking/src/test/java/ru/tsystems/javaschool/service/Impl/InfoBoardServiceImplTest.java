package ru.tsystems.javaschool.service.Impl;

import org.junit.Test;
import org.mockito.InjectMocks;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.service.InfoBoardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InfoBoardServiceImplTest {

    @InjectMocks
    private InfoBoardService infoBoardService = new InfoBoardServiceImpl();

    @Test
    public void driversOfOrderTest() throws TruckingServiceException {
        Driver driver1 = new Driver();
        driver1.setId(1);
        driver1.setName("Jack");
        driver1.setSurname("Jackson");
        driver1.setWorkedThisMonth(0);

        Driver driver2 = new Driver();
        driver2.setId(2);
        driver2.setName("Bob");
        driver2.setSurname("Smith");
        driver2.setWorkedThisMonth(0);

        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver1);
        drivers.add(driver2);

        assertEquals("Jack Jackson, Bob Smith", infoBoardService.driversOfOrder(drivers));
    }

    @Test
    public void cargoesOfOrderTest() throws TruckingServiceException {
        Cargo cargo1 = new Cargo();
        cargo1.setId(1);
        cargo1.setName("Wood");

        Cargo cargo2 = new Cargo();
        cargo2.setId(2);
        cargo2.setName("Oak wood");

        List<Cargo> cargoes = new ArrayList<>();
        cargoes.add(cargo1);
        cargoes.add(cargo2);

        assertEquals("Wood, Oak wood", infoBoardService.cargoesOfOrder(cargoes));
    }

}
