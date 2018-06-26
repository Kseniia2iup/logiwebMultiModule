package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.OrderDao;

import java.util.List;

@Repository("orderDao")
public class OrderDaoImpl extends AbstractDao<Integer, Order> implements OrderDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public Order findOrderById(Integer id) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select O from Order O" +
                    " Left Join Fetch O.truck T Left Join Fetch T.city " +
                    "WHERE O.id = :id");
            query.setParameter("id", id);
            return (Order) query.uniqueResult();
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method findOrderById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void deleteOrder(Integer id) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Delete Order O WHERE O.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method deleteOrder something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void saveOrder(Order order) throws TruckingDaoException {
        try {
            persist(order);
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method saveOrder something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void updateOrder(Order order) throws TruckingDaoException {
        try {
            update(order);
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method updateOrder something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Order> findAllOrders() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select O from Order O" +
                    " Left Join Fetch O.truck T Left Join Fetch T.city Order by O.id Desc");
            return query.list();
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method findAllOrders something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isTruckHasOrder(Integer truckId) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select O from Order O Left Join Fetch O.truck WHERE O.truck.id = :truck_id");
            query.setParameter("truck_id", truckId);
            return query.uniqueResult() != null;
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method isTruckHasOrder something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Order> findLastTenOrders() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select O from Order O" +
                    " Left Join Fetch O.truck T Left Join Fetch T.city Order by O.id Desc");
            return query.setMaxResults(10).list();
        } catch (Exception e){
            LOGGER.warn("From OrderDaoImpl method findAllOrders something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }
}
