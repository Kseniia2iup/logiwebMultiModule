package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Waypoint;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.WaypointDao;

import java.util.List;

@Repository
public class WaypointDaoImpl extends AbstractDao<Integer, Waypoint> implements WaypointDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaypointDaoImpl.class);

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public Waypoint findWaypointById(Integer id) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select W from Waypoint W " +
                    "Join Fetch W.order Join Fetch W.cityDep Join Fetch W.cityDest Join Fetch W.cargo " +
                    "WHERE W.id = :id");
            query.setParameter("id", id);
            return (Waypoint) query.uniqueResult();
        } catch (Exception e){
            LOGGER.warn("From WaypointDaoImpl method findWaypointById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void deleteWaypoint(Integer id) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Delete Waypoint W WHERE W.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception e){
            LOGGER.warn("From WaypointDaoImpl method deleteWaypoint something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void saveWaypoint(Waypoint waypoint) throws TruckingDaoException {
        try {
            persist(waypoint);
        } catch (Exception e){
            LOGGER.warn("From WaypointDaoImpl method saveWaypoint something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void updateWaypoint(Waypoint waypoint) throws TruckingDaoException {
        try {
            update(waypoint);
        } catch (Exception e){
            LOGGER.warn("From WaypointDaoImpl method updateWaypoint something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Waypoint> findAllWaypointsByOrderId(Integer orderId) throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("Select W from Waypoint W " +
                    "Join Fetch W.order Join Fetch W.cityDep Join Fetch W.cityDest Join Fetch W.cargo " +
                    "WHERE W.order.id = :id");
            query.setParameter("id", orderId);
            return query.list();
        } catch (Exception e){
            LOGGER.warn("From WaypointDaoImpl method findAllWaypointsByOrderId something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

}
