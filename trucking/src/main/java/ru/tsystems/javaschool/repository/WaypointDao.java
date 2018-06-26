package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Waypoint;

import java.util.List;

public interface WaypointDao {

    Waypoint findWaypointById(Integer id) throws TruckingDaoException;

    void deleteWaypoint(Integer id) throws TruckingDaoException;

    void saveWaypoint(Waypoint waypoint) throws TruckingDaoException;

    void updateWaypoint(Waypoint waypoint) throws TruckingDaoException;

    List<Waypoint> findAllWaypointsByOrderId(Integer orderId) throws TruckingDaoException;
}
