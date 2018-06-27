package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.User;
import ru.tsystems.javaschool.repository.UserDao;
import ru.tsystems.javaschool.service.DriverService;
import ru.tsystems.javaschool.service.UserService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao dao;

    private PasswordEncoder passwordEncoder;

    private DriverService driverService;

    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) throws TruckingServiceException {
        try {
            driverService.sendSuccessRegistrationEmail(user.getEmail(), user.getLogin(), user.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            dao.save(user);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void updateUser(User user) throws TruckingServiceException {
        try {
            User entity = dao.findById(user.getId());
            if (entity != null) {
                entity.setLogin(user.getLogin());
                entity.setPassword(user.getPassword());
                entity.setEmail(user.getEmail());
                entity.setRole(user.getRole());
            }
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws TruckingServiceException {
        try {
            dao.delete(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    public User findById(int id) throws TruckingServiceException {
        try {
            return dao.findById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    public User findByLogin(String login) {
        return dao.findByLogin(login);
    }

    @Override
    public boolean isUserValid(User user) throws TruckingServiceException {
        try {
            return (user.getLogin() != null && user.getPassword() != null
                    && user.getLogin().length() >= 3
                    && user.getPassword().length() >= 5
                    && isUserLoginUnique(user.getLogin()));
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public boolean isUserLoginUnique(String login) {
        return findByLogin(login) == null;
    }

    @Override
    public List<User> findAllUsers() throws TruckingServiceException {
        try {
            return dao.findAllUsers();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public boolean isEmailValid(String email) throws TruckingServiceException {
        try {
            Pattern pattern = Pattern.compile("[\\w[\\.[\\_[\\-]]]]+@+[\\w[\\.[\\_[\\-]]]]+.+\\w");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public boolean isEmailUnique(String email) throws TruckingServiceException {
        try {
            return dao.findByEmail(email)==null;
        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }
}
