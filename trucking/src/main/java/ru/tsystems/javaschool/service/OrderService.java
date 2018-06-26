package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Order;

import java.util.List;

public interface OrderService {

    Order findOrderById(Integer id) throws TruckingServiceException;

    void deleteOrder(Integer id) throws TruckingServiceException;

    void saveOrder(Order order) throws TruckingServiceException;

    void updateOrder(Order order) throws TruckingServiceException;

    List<Order> findAllOrders() throws TruckingServiceException;

    List<Order> findLastTenOrders() throws TruckingServiceException;

    Double calculateSumDistanceOfOrder(Order order) throws TruckingServiceException;

    Integer averageTimeInHoursForOrder(Order order) throws TruckingServiceException;

    boolean isTimeOrderExceedsDriversShiftLimit(Order order) throws TruckingServiceException;

    void removeTruckAndDriversFromOrder(Order order) throws TruckingServiceException;

    void deleteCargo(Integer cargoId) throws TruckingServiceException;
}
