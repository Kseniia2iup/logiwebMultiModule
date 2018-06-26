package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Order;

import java.util.List;

public interface OrderDao {

    Order findOrderById(Integer id) throws TruckingDaoException;

    void deleteOrder(Integer id) throws TruckingDaoException;

    void saveOrder(Order order) throws TruckingDaoException;

    void updateOrder(Order order) throws TruckingDaoException;

    List<Order> findAllOrders() throws TruckingDaoException;

    boolean isTruckHasOrder(Integer truckId) throws TruckingDaoException;

    List<Order> findLastTenOrders() throws TruckingDaoException;
}
