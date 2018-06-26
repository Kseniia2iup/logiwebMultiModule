package ru.tsystems.javaschool.repository;

import ru.tsystems.javaschool.exceptions.TruckingDaoException;
import ru.tsystems.javaschool.dto.User;

import java.util.List;

public interface UserDao {

    void save(User user) throws TruckingDaoException;

    void delete(Integer id) throws TruckingDaoException;

    User findById(int id) throws TruckingDaoException;

    User findByLogin(String login);

    User findByEmail(String email);

    List<User> findAllUsers() throws TruckingDaoException;
}
