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
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.dto.enums.TruckStatus;
import ru.tsystems.javaschool.service.CityService;
import ru.tsystems.javaschool.service.TruckService;
import ru.tsystems.javaschool.validator.TruckValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class TruckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckController.class);

    private static final String TRUCK_LIST_VIEW_PATH = "redirect:/manager/listTrucks";
    private static final String ADD_TRUCK_VIEW_PATH = "newtruck";

    private TruckService truckService;

    private CityService cityService;

    private TruckValidator truckValidator;

    @Autowired
    public void setTruckValidator(TruckValidator truckValidator) {
        this.truckValidator = truckValidator;
    }

    @Autowired
    public void setTruckService(TruckService truckService) {
        this.truckService = truckService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(path = {"/manager/listTrucks"})
    public String listOfTrucks(Model model) throws TruckingServiceException {
        List<Truck> trucks = truckService.findAllTrucks();
        model.addAttribute("trucks", trucks);
        LOGGER.info("Manager {} looks on the trucks list page", getPrincipal());
        return "alltrucks";
    }

    @GetMapping(path = { "manager/delete-{reg_number}-truck" })
    public String deleteTruck(@PathVariable String reg_number) throws TruckingServiceException {
        truckService.deleteTruckByRegNumber(reg_number);
        LOGGER.info("Manager {} has deleted truck with regNumber = {}", getPrincipal(), reg_number);
        return TRUCK_LIST_VIEW_PATH;
    }

    @GetMapping(path = { "manager/newTruck" })
    public String newTruck(ModelMap model) {
        model.addAttribute("truck", new Truck());
        model.addAttribute("edit", false);
        return ADD_TRUCK_VIEW_PATH;
    }

    @PostMapping(path = { "manager/newTruck" })
    public String saveTruck(@ModelAttribute Truck truck, BindingResult result,
                               ModelMap model) throws TruckingServiceException {

        truckValidator.validate(truck, result);

        if (result.hasErrors()) {
            LOGGER.info("Manager {} has tried to create a truck but entered incorrect data", getPrincipal());
            return ADD_TRUCK_VIEW_PATH;
        }

        truckService.saveTruck(truck);

        LOGGER.info("Manager {} has created the truck with id = {}", getPrincipal(), truck.getId());
        model.addAttribute("success", "Truck " + truck.getRegNumber() + " added successfully");
        return TRUCK_LIST_VIEW_PATH;
    }

    @GetMapping(path = { "manager/edit-{id}-truck" })
    public String editTruck(@PathVariable Integer id, ModelMap model)
            throws TruckingServiceException {
        model.addAttribute("truck", truckService.findTruckById(id));
        model.addAttribute("edit", true);
        return ADD_TRUCK_VIEW_PATH;
    }

    @PostMapping(path = { "manager/edit-{id}-truck" })
    public String updateTruck(@Valid Truck truck, BindingResult result,
                                 ModelMap model, @PathVariable Integer id)
            throws TruckingServiceException {

        truckValidator.validate(truck, result);

        if (result.hasErrors()) {
            model.addAttribute("truck", truckService.findTruckById(id));
            model.addAttribute("edit", true);
            LOGGER.info("Manager {} has tried to edit the truck but entered incorrect data", getPrincipal());
            return ADD_TRUCK_VIEW_PATH;
        }

        truckService.updateTruck(truck);

        LOGGER.info("Manager {} has edited the truck with id = {}", getPrincipal(), truck.getId());
        model.addAttribute("success", "Truck " + truck.getRegNumber()  + " updated successfully");
        return TRUCK_LIST_VIEW_PATH;
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

    @ModelAttribute("truckConditions")
    public TruckStatus[] truckConditions(){
        return TruckStatus.values();
    }

    @ModelAttribute("date")
    public LocalDate currentDate(){
        LocalDateTime localDate = LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis()).toInstant(), ZoneId.systemDefault());
        return localDate.toLocalDate();
    }
}
