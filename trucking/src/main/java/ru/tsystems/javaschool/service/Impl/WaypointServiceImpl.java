package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Waypoint;
import ru.tsystems.javaschool.repository.WaypointDao;
import ru.tsystems.javaschool.service.WaypointService;

import java.util.List;

@Service("waypointService")
@Transactional
public class WaypointServiceImpl implements WaypointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaypointServiceImpl.class);

    private WaypointDao waypointDao;

    @Autowired
    public void setWaypointDao(WaypointDao waypointDao) throws TruckingServiceException {
        try {
            this.waypointDao = waypointDao;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public Waypoint findWaypointById(Integer id) throws TruckingServiceException {
        try {
            return waypointDao.findWaypointById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void deleteWaypoint(Integer id) throws TruckingServiceException {
        try {
            waypointDao.deleteWaypoint(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void saveWaypoint(Waypoint waypoint) throws TruckingServiceException {
        try {
            waypointDao.saveWaypoint(waypoint);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void updateWaypoint(Waypoint waypoint) throws TruckingServiceException {
        try {
            waypointDao.updateWaypoint(waypoint);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Waypoint> findAllWaypointsByOrderId(Integer orderId) throws TruckingServiceException {
        try {
            return waypointDao.findAllWaypointsByOrderId(orderId);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }
}
