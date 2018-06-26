package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.TruckStatus;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.TruckDao;

import java.util.List;

@Repository("truckDao")
public class TruckDaoImpl extends AbstractDao<Integer, Truck> implements TruckDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckDaoImpl.class);

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public Truck findTruckById(int id) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select T from Truck T Join Fetch T.city" +
                    " WHERE T.id = :id");
            query.setParameter("id", id);
            return (Truck) query.uniqueResult();
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method findTruckById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public Truck findTruckByRegNumber(String regNumber) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("SELECT T FROM Truck T Join Fetch T.city" +
                    " WHERE T.regNumber = :regNumber");
            query.setParameter("regNumber", regNumber);
            return (Truck) query.uniqueResult();
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method findTruckByRegNumber something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void saveTruck(Truck truck) throws TruckingDaoException {
        try {
            persist(truck);
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method saveTruck something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void updateTruck(Truck truck) throws TruckingDaoException {
        try {
            update(truck);
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method updateTruck something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void deleteTruckByRegNumber(String regNumber) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Delete Truck T WHERE T.regNumber = :regNumber");
            query.setParameter("regNumber", regNumber);
            query.executeUpdate();
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method deleteTruckByRegNumber something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Truck> findAllTrucks() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select T from Truck T Join Fetch T.city " +
                    " Order by T.regNumber");
            return query.list();
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method findAllTrucks something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    /**
     * Finds all trucks with OK condition
     * @return list of suitable trucks
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Truck> findAllOKTrucks() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select T from Truck T Join Fetch T.city " +
                    "Where T.condition = :condition Order by T.regNumber");
            query.setParameter("condition", TruckStatus.OK);
            return query.list();
        } catch (Exception e){
            LOGGER.warn("From TruckDaoImpl method findAllOKTrucks something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }
}
