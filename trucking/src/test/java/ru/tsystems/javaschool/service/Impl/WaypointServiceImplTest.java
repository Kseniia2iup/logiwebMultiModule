package ru.tsystems.javaschool.service.Impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Waypoint;
import ru.tsystems.javaschool.repository.WaypointDao;
import ru.tsystems.javaschool.service.WaypointService;

import java.util.ArrayList;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WaypointServiceImplTest {

    @InjectMocks
    private WaypointService waypointService = new WaypointServiceImpl();

    @Mock
    private WaypointDao waypointDao;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllWaypointsByOrderIdTest() throws TruckingDaoException, TruckingServiceException {
        when(waypointDao.findAllWaypointsByOrderId(anyInt())).thenReturn(new ArrayList<Waypoint>());
        waypointService.findAllWaypointsByOrderId(anyInt());
        verify(waypointDao).findAllWaypointsByOrderId(anyInt());
    }
}
