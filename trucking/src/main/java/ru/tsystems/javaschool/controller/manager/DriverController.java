package ru.tsystems.javaschool.controller.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.City;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.dto.User;
import ru.tsystems.javaschool.dto.enums.DriverStatus;
import ru.tsystems.javaschool.dto.enums.Role;
import ru.tsystems.javaschool.service.CityService;
import ru.tsystems.javaschool.service.DriverService;
import ru.tsystems.javaschool.service.UserService;
import ru.tsystems.javaschool.validator.DriverValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class DriverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverController.class);

    private static final String DRIVER_LIST_VIEW_PATH = "redirect:/manager/listDrivers";
    private static final String ADD_DRIVER_VIEW_PATH = "newdriver";

    private DriverValidator driverValidator;

    @Autowired
    public void setDriverValidator(DriverValidator driverValidator) {
        this.driverValidator = driverValidator;
    }

    private DriverService driverService;

    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(path = "manager/listDrivers")
    public String listOfDrivers(Model model) throws TruckingServiceException {
        model.addAttribute("drivers", driverService.findAllDrivers());
        LOGGER.info("Manager {} looks on the drivers list", getPrincipal());
        return "alldrivers";
    }


    @GetMapping(path = { "manager/delete-{id}-driver" })
    public String deleteDriver(@PathVariable Integer id) throws TruckingServiceException {
        if(driverService.findDriverById(id).getOrder()!=null){
            LOGGER.info("Manager {} has tried to delete driver with id = {} but driver doesn't exist", getPrincipal(), id);
            return DRIVER_LIST_VIEW_PATH;
        }
        driverService.deleteDriver(id);
        userService.delete(id);
        LOGGER.info("Manager {} has deleted driver with id = {}", getPrincipal(), id);
        return DRIVER_LIST_VIEW_PATH;
    }

    @GetMapping(path = { "manager/newDriver" })
    public String newDriver(ModelMap model) {
        model.addAttribute("driver", new Driver());
        model.addAttribute("edit", false);
        return ADD_DRIVER_VIEW_PATH;
    }

    @PostMapping(path = { "manager/newDriver" })
    public String saveDriver(@ModelAttribute Driver driver, BindingResult result,
                            ModelMap model) throws TruckingServiceException {

        driverValidator.validate(driver, result);

        if (result.hasErrors()) {
            LOGGER.info("Manager {} has tried to create a driver but entered incorrect data", getPrincipal());
            return ADD_DRIVER_VIEW_PATH;
        }
        User user = new User();
        String login = driverService.generateDriverLogin(driver);
        String password = driverService.generateDriverPassword();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(Role.DRIVER);
        user.setEmail(driver.getEmail());
        userService.save(user);
        driver.setId(user.getId());
        driver.setWorkedThisMonth(0);
        driver.setStatus(DriverStatus.REST);
        driverService.saveDriver(driver);
        driverService.sendSuccessRegistrationEmail(driver.getEmail(), login, password);
        LOGGER.info("Manager {} has created the driver with id = {}", getPrincipal(), driver.getId());
        model.addAttribute("success", "Driver " + driver.getName()
                + " " + driver.getSurname() + " added successfully");
        return DRIVER_LIST_VIEW_PATH;
    }


    @GetMapping(value = { "manager/edit-{id}-driver" })
    public String editDriver(@PathVariable Integer id, ModelMap model) throws TruckingServiceException {
        if(driverService.findDriverById(id).getOrder()!=null){
            return DRIVER_LIST_VIEW_PATH;
        }
        Driver driver = driverService.findDriverById(id);
        model.addAttribute("driver", driver);
        model.addAttribute("edit", true);
        return ADD_DRIVER_VIEW_PATH;
    }

    @PostMapping(value = { "manager/edit-{id}-driver" })
    public String editDriver(@Valid Driver driver, BindingResult result,
                             ModelMap model, @PathVariable Integer id) throws TruckingServiceException {

        driverValidator.validate(driver, result);

        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            LOGGER.info("Manager {} has tried to edit the driver but entered incorrect data", getPrincipal());
            return ADD_DRIVER_VIEW_PATH;
        }
        User user = userService.findById(driver.getId());
        user.setEmail(driver.getEmail());
        userService.updateUser(user);
        Driver entityDriver = driverService.findDriverById(id);
        driver.setStatus(entityDriver.getStatus());
        driver.setWorkedThisMonth(entityDriver.getWorkedThisMonth());
        driverService.updateDriver(driver);

        LOGGER.info("Manager {} has edited the driver with id = {}", getPrincipal(), driver.getId());
        return DRIVER_LIST_VIEW_PATH;
    }

    @ModelAttribute("user")
    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @ModelAttribute("cities")
    public List<City> cityList() throws TruckingServiceException {
        return cityService.findAllCities();
    }

    @ModelAttribute("driverStatuses")
    public DriverStatus[] driverStatuses(){
        return DriverStatus.values();
    }

    @ModelAttribute("date")
    public LocalDate currentDate(){
        LocalDateTime localDate = LocalDateTime.ofInstant(new Date(System.currentTimeMillis()).toInstant(), ZoneId.systemDefault());
        return localDate.toLocalDate();
    }
}
