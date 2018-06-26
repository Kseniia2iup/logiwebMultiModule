package ru.tsystems.javaschool.repository.Impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.User;
import ru.tsystems.javaschool.repository.AbstractDao;
import ru.tsystems.javaschool.repository.UserDao;

import java.util.List;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void save(User user) throws TruckingDaoException {
        try {
            persist(user);
        } catch (Exception e){
            LOGGER.warn("From UserDaoImpl method save something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = TruckingDaoException.class,
            propagation = Propagation.MANDATORY)
    public void delete(Integer id) throws TruckingDaoException {
        try {
            delete(findById(id));
        } catch (Exception e){
            LOGGER.warn("From UserDaoImpl method delete something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public User findById(int id) throws TruckingDaoException {
        try {
            return getByKey(id);
        } catch (Exception e){
            LOGGER.warn("From UserDaoImpl method findById something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }

    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public User findByLogin(String login){
        Query query = getSession().createQuery("SELECT U FROM User U WHERE U.login = :login");
        query.setParameter("login", login);
        return (User) query.uniqueResult();
    }


    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public User findByEmail(String email) {
        Query query = getSession().createQuery("SELECT U FROM User U WHERE U.email = :email");
        query.setParameter("email", email);
        return (User) query.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = TruckingDaoException.class,
            readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> findAllUsers() throws TruckingDaoException {
        try {
            Query query = getSession().createQuery("from User");
            return query.list();
        } catch (Exception e){
            LOGGER.warn("From UserDaoImpl method findAllUsers something went wrong:\n", e);
            throw new TruckingDaoException(e);
        }
    }
}