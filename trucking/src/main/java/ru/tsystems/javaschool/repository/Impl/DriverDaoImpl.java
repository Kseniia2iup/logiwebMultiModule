package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.DriverDao;

import java.util.List;

@Repository("driverDao")
public class DriverDaoImpl extends AbstractDao<Integer, Driver> implements DriverDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverDaoImpl.class);

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public Driver findDriverById(Integer id) throws TruckingDaoException {
        try{
            Query query = getSession().createQuery("Select D from Driver D Join Fetch D.user " +
                    "Left Join Fetch D.order Join Fetch D.city Left Join Fetch D.currentTruck WHERE D.id = :id");
            query.setParameter("id", id);
            return (Driver) query.uniqueResult();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method findDriverById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void deleteDriver(Integer id) throws TruckingDaoException {
        try{
            Query query = getSession().createQuery("Delete Driver D WHERE D.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method deleteDriver something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void saveDriver(Driver driver) throws TruckingDaoException {
        try {
            persist(driver);
        } catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method saveDriver something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void updateDriver(Driver driver) throws TruckingDaoException {
        try {
            update(driver);
        } catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method updateDriver something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Driver> findAllDrivers() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select D from Driver D Join Fetch D.user " +
                    "Left Join Fetch D.order Join Fetch D.city Left Join Fetch D.currentTruck Order by D.name");
            return query.list();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method findAllDrivers something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public Integer getLastDriverId() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select max(D.id) from Driver D");
            return (Integer)query.uniqueResult();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method getMaxDriverId something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    /**
     * Returns all free from work drivers in the same city as truck is
     * @param truck that needs drivers
     * @return list of suitable drivers
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Driver> getAllFreeDriversForTruck(Truck truck) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select D from Driver D Join Fetch D.city " +
                    "Left Join Fetch D.currentTruck Left Join Fetch D.order " +
                    "Where D.currentTruck is null AND D.city = :truck_city Order by D.name");
            query.setParameter("truck_city", truck.getCity());
            return query.list();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method getAllFreeDriversForTruck" +
                    " something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Driver> getAllDriversOfTruck(Truck truck) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select D from Driver D Join Fetch D.city " +
                    "Left Join Fetch D.order Left Join Fetch D.currentTruck Where D.currentTruck = :truck");
            query.setParameter("truck", truck);
            return query.list();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method getAllDriversOfTruck" +
                    " something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Driver> getAllDriversOfOrder(Order order) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select D from Driver D Join Fetch D.city " +
                    "Left Join Fetch D.order Left Join Fetch D.currentTruck Where D.order = :order");
            query.setParameter("order", order);
            return query.list();
        }
        catch (Exception e){
            LOGGER.warn("From DriverDaoImpl method getAllDriversOfOrder" +
                    " something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }
}
