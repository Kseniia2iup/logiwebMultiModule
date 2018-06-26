package ru.tsystems.javaschool.service.Impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.DriverStatus;
import ru.tsystems.javaschool.dto.enums.TruckStatus;
import ru.tsystems.javaschool.repository.DriverDao;
import ru.tsystems.javaschool.service.DriverService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DriverServiceImplTest {

    @Mock
    private DriverDao driverDao;

    @InjectMocks
    private DriverService driverService = new DriverServiceImpl();

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllDriversTest() throws TruckingDaoException, TruckingServiceException {
        when(driverDao.findAllDrivers()).thenReturn(new ArrayList<Driver>());
        driverService.findAllDrivers();
        verify(driverDao).findAllDrivers();
    }

    @Test
    public void getAllDriversOfTruckTest() throws TruckingDaoException, TruckingServiceException {
        Truck truck = new Truck();
        truck.setId(1);
        truck.setRegNumber("DD30349");
        truck.setCapacityTon(2);
        truck.setShiftPeriod(1);
        truck.setCondition(TruckStatus.OK);
        when(driverDao.getAllDriversOfTruck(truck)).thenReturn(new ArrayList<Driver>());
        driverService.getAllDriversOfTruck(truck);
        verify(driverDao).getAllDriversOfTruck(truck);
    }

    @Test
    public void generateDriverPasswordTest(){
        Pattern pattern = Pattern.compile("[\\w]{15}");
        Matcher matcher = pattern.matcher(driverService.generateDriverPassword());
        assertTrue(matcher.matches());
    }

    @Test
    public void setHoursOfWorkDependsOnStatusChangingTest_01() throws TruckingServiceException {
        Driver driver = new Driver();
        driver.setId(1);
        driver.setName("John");
        driver.setSurname("Doe");
        driver.setWorkedThisMonth(0);
        driver.setStatus(DriverStatus.REST);

        Driver updatedDriver = driverService.setHoursOfWorkDependsOnStatusChanging(driver, DriverStatus.DRIVING);

        assertTrue(updatedDriver.getStatus().equals(DriverStatus.DRIVING)
                && updatedDriver.getWorkedThisMonth() == 0
                && updatedDriver.getShiftBegined().getDate()==(new Date(System.currentTimeMillis())).getDate());
    }


    @Test
    public void setHoursOfWorkDependsOnStatusChangingTest_02() throws TruckingServiceException {
        Driver driver = new Driver();
        driver.setId(1);
        driver.setName("John");
        driver.setSurname("Doe");
        driver.setWorkedThisMonth(0);
        driver.setStatus(DriverStatus.SECOND_DRIVER);
        driver.setShiftBegined(new Date(System.currentTimeMillis()-400000000));

        Driver updatedDriver = driverService.setHoursOfWorkDependsOnStatusChanging(driver, DriverStatus.REST);

        assertTrue(updatedDriver.getStatus().equals(DriverStatus.REST)
                && updatedDriver.getWorkedThisMonth() == 111
                && updatedDriver.getShiftEnded().getDate()==((new Date(System.currentTimeMillis())).getDate())
        );
    }

    @Test
    public void findAllFreeDriversTest() throws TruckingServiceException {
        List<Driver> allDriversMock = new ArrayList<>();

        Truck truck = new Truck();

        Driver driver = new Driver();
        driver.setId(1);
        driver.setName("John");
        driver.setSurname("Doe");
        driver.setWorkedThisMonth(0);
        driver.setStatus(DriverStatus.SECOND_DRIVER);
        driver.setCurrentTruck(truck);

        Driver driver2 = new Driver();
        driver2.setId(2);
        driver2.setName("Johnny");
        driver2.setSurname("Doe");
        driver2.setWorkedThisMonth(0);
        driver2.setStatus(DriverStatus.REST);

        Driver driver3 = new Driver();
        driver3.setId(3);
        driver3.setName("Jonathan");
        driver3.setSurname("Doe");
        driver3.setWorkedThisMonth(0);
        driver3.setStatus(DriverStatus.REST);
        driver3.setCurrentTruck(truck);

        allDriversMock.add(driver);
        allDriversMock.add(driver2);
        allDriversMock.add(driver3);

        assertTrue((driverService.findAllFreeDrivers(allDriversMock)).size()==2);
    }
}
