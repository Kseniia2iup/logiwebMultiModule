package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.City;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.CityDao;

import java.util.List;

@Repository("cityDao")
public class CityDaoImpl extends AbstractDao<Integer, City> implements CityDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityDaoImpl.class);

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public City findCityById(int id) throws TruckingDaoException {
        try {
            return getByKey(id);
        }
        catch (Exception e){
            LOGGER.warn("From CityDaoImpl method findCityById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public City findCityByName(String name) throws TruckingDaoException {
        try{
            Query query = getSession().createQuery("SELECT C FROM City C WHERE C.name = :name");
            query.setParameter("name", name);
            return (City) query.uniqueResult();
        }
        catch (Exception e){
            LOGGER.warn("From CityDaoImpl method findCityByName something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<City> findAllCities() throws TruckingDaoException {
        try{
            Query query = getSession().createQuery("From City");
            return query.list();
        }
        catch (Exception e){
            LOGGER.warn("From CityDaoImpl method findAllCities something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }
}
