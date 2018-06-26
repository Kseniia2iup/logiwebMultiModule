package ru.tsystems.javaschool.service.Impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.exceptions.NoCargoInTheOrderException;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.CargoStatus;
import ru.tsystems.javaschool.dto.enums.OrderStatus;
import ru.tsystems.javaschool.dto.enums.TruckStatus;
import ru.tsystems.javaschool.repository.CargoDao;
import ru.tsystems.javaschool.repository.OrderDao;
import ru.tsystems.javaschool.repository.TruckDao;
import ru.tsystems.javaschool.service.TruckService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class TruckServiceImplTest {

    private static List<Truck> allTrucksMock;
    private static Order orderForTruck;
    private static List<Cargo> cargoes;

    @Mock
    private CargoDao cargoDao;

    @Mock
    private TruckDao truckDao;

    @Mock
    private OrderDao orderDao;

    @Before
    public void initTrucksList(){
        MockitoAnnotations.initMocks(this);

        allTrucksMock = new ArrayList<>();

        Order order = new Order();

        Truck truck = new Truck();
        truck.setId(1);
        truck.setRegNumber("AA20394");
        truck.setCondition(TruckStatus.FAULTY);
        truck.setShiftPeriod(2);
        truck.setCapacityTon(4);

        Truck truck2 = new Truck();
        truck2.setId(2);
        truck2.setRegNumber("AA20395");
        truck2.setCondition(TruckStatus.OK);
        truck2.setShiftPeriod(2);
        truck2.setCapacityTon(4);
        truck2.setOrder(order);

        Truck truck3 = new Truck();
        truck3.setId(3);
        truck3.setRegNumber("AA20396");
        truck3.setCondition(TruckStatus.OK);
        truck3.setShiftPeriod(2);
        truck3.setCapacityTon(8);

        allTrucksMock.add(truck);
        allTrucksMock.add(truck2);
        allTrucksMock.add(truck3);

        orderForTruck = new Order();
        orderForTruck.setId(1);
        orderForTruck.setOrderStatus(OrderStatus.CREATED);

        cargoes = new ArrayList<>();
        Cargo cargo_1 = new Cargo();
        cargo_1.setId(1);
        cargo_1.setOrder(orderForTruck);
        cargo_1.setName("Furniture");
        cargo_1.setWeight(2000);
        cargo_1.setDelivery_status(CargoStatus.PREPARED);

        Cargo cargo_2 = new Cargo();
        cargo_2.setId(2);
        cargo_2.setOrder(orderForTruck);
        cargo_2.setName("Furniture");
        cargo_2.setWeight(5000);
        cargo_2.setDelivery_status(CargoStatus.PREPARED);

        cargoes.add(cargo_1);
        cargoes.add(cargo_2);
    }

    @InjectMocks
    private TruckService truckService = new TruckServiceImpl();

    @Test
    public void findAllBrokenTrucksTest() throws TruckingServiceException {
        List<Truck> result = truckService.findAllBrokenTrucks(allTrucksMock);
        assertTrue(result.size()==1 && result.get(0).getId()==1);
    }

    @Test
    public void findAllTrucksOnOrderTest() throws TruckingServiceException {
        List<Truck> result = truckService.findAllTrucksOnOrder(allTrucksMock);
        assertTrue(result.size()==1 && result.get(0).getId()==2);
    }

    @Test
    public void isTruckRegNumberIsValidTest() {
        assertTrue(truckService.isTruckRegNumberIsValid(1, "AA33333"));
        assertTrue(truckService.isTruckRegNumberIsValid(1, "ww30333"));
        assertFalse(truckService.isTruckRegNumberIsValid(1, "AA333033"));
        assertFalse(truckService.isTruckRegNumberIsValid(1, "22Afgrl"));
        assertFalse(truckService.isTruckRegNumberIsValid(1, "fwl3c/.[q"));
    }

    @Test
    public void findAllTrucksReadyForOrderTest() throws TruckingDaoException,
            TruckingServiceException, NoCargoInTheOrderException {
        when(cargoDao.findAllCargoesOfOrder(orderForTruck.getId())).thenReturn(cargoes);
        when(truckDao.findAllOKTrucks()).thenReturn(allTrucksMock);
        when(orderDao.isTruckHasOrder(anyInt())).thenReturn(false);
        List<Truck> result = truckService.findAllTrucksReadyForOrder(orderForTruck);
        assertTrue(result.size()==1);
    }

}
