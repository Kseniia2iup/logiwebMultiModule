package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.Cargo;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.CargoDao;

import java.util.List;

@Repository
public class CargoDaoImpl extends AbstractDao<Integer, Cargo> implements CargoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CargoDaoImpl.class);

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class, readOnly = true,
            propagation = Propagation.SUPPORTS)
    public Cargo findCargoById(Integer id) throws TruckingDaoException {
        try {

            Query query = getSession().createQuery("Select C from Cargo C " +
                    "Join Fetch C.order Join Fetch C.waypoint W Join Fetch W.cityDep Join Fetch W.cityDest WHERE C.id = :id");
            query.setParameter("id", id);
            return (Cargo) query.uniqueResult();
        }
        catch (Exception e){
            LOGGER.warn("From CargoDaoImpl method findCargoById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class, propagation = Propagation.MANDATORY)
    public void deleteCargo(Integer id)  throws TruckingDaoException{
        try {
            Query query = getSession().createQuery("Delete Cargo C WHERE C.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        }
        catch (Exception e){
            LOGGER.warn("From CargoDaoImpl method deleteCargo something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void saveCargo(Cargo cargo) throws TruckingDaoException {
        try {
            persist(cargo);
        } catch (Exception e){
            LOGGER.warn("From CargoDaoImpl method saveCargo something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void updateCargo(Cargo cargo) throws TruckingDaoException {
        try {
            update(cargo);
        } catch (Exception e){
            LOGGER.warn("From CargoDaoImpl method updateCargo something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Cargo> findAllCargoesOfOrder(Integer orderId) throws TruckingDaoException {
        try {
        Query query = getSession().createQuery("Select C from Cargo C " +
                "Where C.order.id = :id");
        query.setParameter("id", orderId);
        return query.list();
        } catch (Exception e){
            LOGGER.warn("From CargoDaoImpl method findAllCargoesOfOrder something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }
}
