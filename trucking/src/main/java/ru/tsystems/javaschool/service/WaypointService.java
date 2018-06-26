package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Waypoint;

import java.util.List;

public interface WaypointService {

    Waypoint findWaypointById(Integer id) throws TruckingServiceException;

    void deleteWaypoint(Integer id) throws TruckingServiceException;

    void saveWaypoint(Waypoint waypoint) throws TruckingServiceException;

    void updateWaypoint(Waypoint waypoint) throws TruckingServiceException;

    List<Waypoint> findAllWaypointsByOrderId(Integer orderId) throws TruckingServiceException;
}
