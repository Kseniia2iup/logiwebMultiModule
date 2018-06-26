package ru.tsystems.javaschool.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.Order;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.DriverStatus;
import ru.tsystems.javaschool.repository.DriverDao;
import ru.tsystems.javaschool.service.DriverService;
import ru.tsystems.javaschool.service.InfoBoardService;
import ru.tsystems.javaschool.service.OrderService;
import ru.tsystems.javaschool.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Service("driverService")
@Transactional
@PropertySource(value = {"classpath:email.properties" })
public class DriverServiceImpl implements DriverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverServiceImpl.class);

    private UserService userService;

    private DriverDao driverDao;

    private OrderService orderService;

    private InfoBoardService infoBoardService;

    @Autowired
    private Environment environment;

    @Autowired
    public void setInfoBoardService(InfoBoardService infoBoardService) {
        this.infoBoardService = infoBoardService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDriverDao(DriverDao driverDao) {
        this.driverDao = driverDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Driver findDriverById(Integer id) throws TruckingServiceException {
        try {
            return driverDao.findDriverById(id);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void deleteDriver(Integer id) throws TruckingServiceException {
        try {
            driverDao.deleteDriver(id);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void saveDriver(Driver driver) throws TruckingServiceException {
        try {
            driverDao.saveDriver(driver);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void updateDriver(Driver driver) throws TruckingServiceException {
        try {
            driverDao.updateDriver(driver);
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Driver> findAllDrivers() throws TruckingServiceException {
        try {
            return driverDao.findAllDrivers();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public Integer getMaxDriverId() throws TruckingServiceException {
        try {
            return driverDao.getLastDriverId();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Generate driver login based on driver name, max id form drivers table and random number
     * @param driver current driver
     * @return random login
     */
    @Override
    public String generateDriverLogin(Driver driver) throws TruckingServiceException {
        try {
            Random random = new Random();
            Integer maxDriverId = getMaxDriverId();
            if (maxDriverId == null) {
                maxDriverId = 1;
            }
            String result = driver.getSurname()
                    + '_'
                    + maxDriverId
                    + random.nextInt(1000);
            while (!userService.isUserLoginUnique(result)) {
                StringBuilder stringBuilder = new StringBuilder(result);
                stringBuilder.append(random.nextInt(9));
                result = stringBuilder.toString();
            }

            return result;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Generate driver password that contains numbers, lower and upper case characters
     * @return random password
     */
    @Override
    public String generateDriverPassword() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            result.append(random.nextInt(10));
            result.append((char) (random.nextInt(26) + 65));
            result.append((char) (random.nextInt(26) + 97));
        }

        return result.toString();
    }

    /**
     * Returns all drivers whose aren't working right now, situated in the same city as truck is
     * and will not exceed the time limit of work per month (176 hours) during the execution of the order
     * @param order to complete
     * @return List of Drivers suitable for the order execution
     */
    @Override
    public List<Driver> findAllDriversSuitableForOrder(Order order) throws TruckingServiceException {
        try {
            List<Driver> drivers = driverDao.getAllFreeDriversForTruck(order.getTruck());
            List<Driver> result = new ArrayList<>();
            int hoursForOrderExecution = orderService.averageTimeInHoursForOrder(order);
            for (Driver driver : drivers
                    ) {
                if (driver.getWorkedThisMonth()+hoursForOrderExecution <= 176) {
                    result.add(driver);
                }
            }
            return result;
        }
        catch (Exception e){
            LOGGER.debug("From DriverServiceImpl method findAllDriversSuitableForOrder:\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Driver> getAllDriversOfTruck(Truck truck) throws TruckingServiceException {
        try {
            return driverDao.getAllDriversOfTruck(truck);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public List<Driver> getAllDriversOfOrder(Order order) throws TruckingServiceException {
        try {
            return driverDao.getAllDriversOfOrder(order);
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Find all co-workers of the driver
     * @param driver whose co-workers needs to be gotten
     * @return List of Drivers with the same Order that driver has
     */
    @Override
    public List<Driver> findCoWorkers(Driver driver) throws TruckingServiceException {
        try {
            if (driver.getOrder() != null) {
                List<Driver> drivers = getAllDriversOfOrder(driver.getOrder());
                int index = -1;
                for (Driver dr : drivers
                        ) {
                    if (dr.getId() == driver.getId()) {
                        index = drivers.indexOf(dr);
                    }
                }
                if (index != -1) {
                    drivers.remove(index);
                }
                return drivers;
            }
            return new ArrayList<>();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    /**
     * Set new status of work to the Driver. If Driver had status REST and newStatus not Rest
     * set current Date to shiftBegined field
     * If Driver has newStatus as Rest and old one is not set current Date to shiftEnded
     * field and then calculate approximately hours of work and update
     * Driver field workedThisMonth
     * @param driver that needs status to change
     * @param newStatus new status to the Driver
     */
    @Override
    public void setDriverStatus(Driver driver, DriverStatus newStatus) throws TruckingServiceException {
        try {
            updateDriver(setHoursOfWorkDependsOnStatusChanging(driver, newStatus));
            infoBoardService.sendInfoToQueue();
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public Driver setHoursOfWorkDependsOnStatusChanging(Driver driver, DriverStatus newStatus)
            throws TruckingServiceException{
        try {
        DriverStatus oldStatus = driver.getStatus();

        if (oldStatus.equals(DriverStatus.REST) && !newStatus.equals(DriverStatus.REST)) {
            driver.setShiftBegined(new Date(System.currentTimeMillis()));
            driver.setStatus(newStatus);
            return (driver);
        }

        //If Driver has ended shift add new hours of work to workedThisMonth field
        else if (!oldStatus.equals(DriverStatus.REST) && newStatus.equals(DriverStatus.REST)) {
            driver.setShiftEnded(new Date(System.currentTimeMillis()));

            LocalDateTime shiftBegan = LocalDateTime.ofInstant(
                    driver.getShiftBegined().toInstant(), ZoneId.systemDefault());
            LocalDateTime shiftEnded = LocalDateTime.ofInstant(
                    driver.getShiftEnded().toInstant(), ZoneId.systemDefault());

            if (shiftBegan.getMonthValue() != shiftEnded.getMonthValue()) {
                driver.setWorkedThisMonth(shiftEnded.getDayOfMonth() * 24 + shiftEnded.getHour());
            } else {
                driver.setWorkedThisMonth((shiftEnded.getDayOfMonth() - shiftBegan.getDayOfMonth())
                        * 24 + shiftEnded.getHour() - shiftBegan.getHour());
            }

            driver.setStatus(newStatus);
            return (driver);
        } else {
            driver.setStatus(newStatus);
            return (driver);
        }
    }
        catch (Exception e){
        LOGGER.warn("Something went wrong\n", e);
        throw new TruckingServiceException(e);
    }
    }

    /**
     * Returns all free from work drivers
     * @param allDrivers List of all drivers
     * @return List of free drivers
     * @throws TruckingServiceException if something went wrong
     */
    @Override
    public List<Driver> findAllFreeDrivers(List<Driver> allDrivers) throws TruckingServiceException {
        try {
            if (allDrivers==null){
                return new ArrayList<>();
            }

            List<Driver> result = new ArrayList<>();
            for (Driver driver: allDrivers
                 ) {
                if (driver.getCurrentTruck()!=null){
                    result.add(driver);
                }
            }
            return result;
        }
        catch (Exception e){
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }
    }

    @Override
    public void sendSuccessRegistrationEmail(String email, String login, String password)
            throws TruckingServiceException {

        // Recipient's email ID needs to be mentioned.
        String to = email;

        // Sender's email ID needs to be mentioned
        String from = environment.getProperty("email.email");

        // Assuming you are sending email from localhost
        String host = "localhost";


        final String username = environment.getProperty("email.email");

        final String password_ = environment.getProperty("email.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password_);
                    }
                });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Registration on the LogiWeb");
            message.setText("Dear" + username + ", \n" +
                            "You have been successfully registered on the LogiWeb application \n"+
                    "Your login and password: \n"
                    + login + " " + password +
                    "\n Login page: http://127.0.0.1:8181/trucking/login" +
                    "\n Hope to see you soon!", "utf-8");

            Transport.send(message);
        } catch (Exception e) {
            LOGGER.warn("Something went wrong\n", e);
            throw new TruckingServiceException(e);
        }

    }
}
