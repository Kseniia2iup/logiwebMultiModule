package ru.tsystems.javaschool.service;

import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.User;

import java.util.List;

public interface UserService {

    void save(User user) throws TruckingServiceException;

    void updateUser(User user) throws TruckingServiceException;

    void delete(Integer id) throws TruckingServiceException;

    User findById(int id) throws TruckingServiceException;

    User findByLogin(String login);

    boolean isUserValid(User user) throws TruckingServiceException;

    boolean isUserLoginUnique(String login) throws TruckingServiceException;

    List<User> findAllUsers() throws TruckingServiceException;

    boolean isEmailValid(String email) throws TruckingServiceException;

    boolean isEmailUnique(String email) throws TruckingServiceException;
}
