package ru.tsystems.javaschool.service.Impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.enums.CargoStatus;
import ru.tsystems.javaschool.dto.enums.OrderStatus;
import ru.tsystems.javaschool.repository.CargoDao;
import ru.tsystems.javaschool.service.CargoService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CargoServiceImplTest {

    @InjectMocks
    private CargoService cargoService = new CargoServiceImpl();

    @Mock
    private CargoDao cargoDao;

    private Order order;
    private List<Cargo> cargoes;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        order = new Order();
        order.setId(1);
        order.setOrderStatus(OrderStatus.CREATED);

        cargoes = new ArrayList<>();
        Cargo cargo_1 = new Cargo();
        cargo_1.setId(1);
        cargo_1.setOrder(order);
        cargo_1.setName("Furniture");
        cargo_1.setWeight(2000);
        cargo_1.setDelivery_status(CargoStatus.PREPARED);

        Cargo cargo_2 = new Cargo();
        cargo_2.setId(2);
        cargo_2.setOrder(order);
        cargo_2.setName("Furniture");
        cargo_2.setWeight(5000);
        cargo_2.setDelivery_status(CargoStatus.PREPARED);

        cargoes.add(cargo_1);
        cargoes.add(cargo_2);

    }

    @Test
    public void findCargoByIdTest() throws TruckingDaoException, TruckingServiceException {
        when(cargoDao.findCargoById(anyInt())).thenReturn(new Cargo());
        cargoService.findCargoById(anyInt());
        verify(cargoDao).findCargoById(anyInt());
    }

    @Test
    public void findAllCargoesOfOrder() throws TruckingDaoException, TruckingServiceException {
        when(cargoDao.findAllCargoesOfOrder(order.getId())).thenReturn(cargoes);
        cargoService.findAllCargoesOfOrder(order.getId());
        verify(cargoDao).findAllCargoesOfOrder(order.getId());
    }
}
