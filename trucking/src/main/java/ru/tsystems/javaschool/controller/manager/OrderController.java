package ru.tsystems.javaschool.controller.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.tsystems.javaschool.exceptions.NoCargoInTheOrderException;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.*;
import ru.tsystems.javaschool.dto.enums.CargoStatus;
import ru.tsystems.javaschool.dto.enums.OrderStatus;
import ru.tsystems.javaschool.service.*;
import ru.tsystems.javaschool.validator.CargoValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private static final String ORDER_LIST_VIEW_PATH = "redirect:/manager/listOrders";
    private static final String ADD_ORDER_VIEW_PATH = "allcargoes";

    private OrderService orderService;

    private CargoService cargoService;

    private WaypointService waypointService;

    private CityService cityService;

    private TruckService truckService;

    private DriverService driverService;

    private CargoValidator cargoValidator;

    @Autowired
    public void setCargoValidator(CargoValidator cargoValidator) {
        this.cargoValidator = cargoValidator;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setCargoService(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @Autowired
    public void setWaypointService(WaypointService waypointService) {
        this.waypointService = waypointService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setTruckService(TruckService truckService) {
        this.truckService = truckService;
    }

    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping(path = "manager/listOrders")
    public String listOfOrders(Model model) throws TruckingServiceException {
        model.addAttribute("orders", orderService.findAllOrders());
        LOGGER.info("Manager {} looks on the orders list", getPrincipal());
        return "allorders";
    }

    @GetMapping(path = "manager/newOrder")
    public String newOrder(Model model) throws TruckingServiceException{
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        orderService.saveOrder(order);
        model.addAttribute("order", orderService.findOrderById(order.getId()));
        LOGGER.info("Manager {} has created order with id = {}", getPrincipal(), order.getId());
        return "redirect:/manager/"+order.getId()+"/listOrderCargoes";
    }

    @GetMapping(path = "manager/{orderId}/listOrderCargoes")
    public String listCargoes(@PathVariable Integer orderId, Model model) throws TruckingServiceException {
        Order order = orderService.findOrderById(orderId);
        List<Cargo> cargoes = cargoService.findAllCargoesOfOrder(orderId);
        Truck truck = null;
        if(order.getTruck()!=null){
            truck = truckService.findTruckById(order.getTruck().getId());
        }
        if (orderService.isTimeOrderExceedsDriversShiftLimit(order)){
            model.addAttribute("overLimit", true);
        }
        else {
            model.addAttribute("overLimit", false);
        }
        model.addAttribute("order", order);
        model.addAttribute("truck", truck);
        model.addAttribute("cargoes", cargoes);
        return ADD_ORDER_VIEW_PATH;
    }

    @GetMapping(path = "manager/{orderId}/newCargo")
    public String newCargo(@PathVariable Integer orderId, Model model)
            throws TruckingServiceException{
        if(orderService.findOrderById(orderId).getTruck()!=null ||
                orderService.findOrderById(orderId).getOrderStatus() != OrderStatus.CREATED){
            return ORDER_LIST_VIEW_PATH;
        }
        model.addAttribute("order", orderService.findOrderById(orderId));
        model.addAttribute("cargo", new Cargo());
        return "newcargo";
    }

    @PostMapping(path = "manager/{orderId}/newCargo")
    public String saveCargo(@PathVariable Integer orderId, Cargo cargo, BindingResult result, Model model)
            throws TruckingServiceException{

        cargoValidator.validate(cargo, result);

        if(result.hasErrors()){
            model.addAttribute("order", orderService.findOrderById(orderId));
            model.addAttribute("cargo", new Cargo());
            LOGGER.info("Manager {} has tried to create the cargo but entered incorrect data", getPrincipal());
            return "newcargo";
        }

        Order order = orderService.findOrderById(orderId);
        cargo.setOrder(order);
        cargo.setDelivery_status(CargoStatus.PREPARED);
        cargoService.saveCargo(cargo);
        Waypoint waypoint = new Waypoint();
        waypoint.setOrder(order);
        waypoint.setCargo(cargo);
        waypoint.setCityDep(cargo.getWaypoint().getCityDep());
        waypoint.setCityDest(cargo.getWaypoint().getCityDest());
        waypointService.saveWaypoint(waypoint);

        model.addAttribute("order", orderService.findOrderById(orderId));
        LOGGER.info("Manager {} has added the cargo with id = {} to the order with id = {}",
                getPrincipal(), cargo.getId(), order.getId());
        return "redirect:/manager/"+orderId+"/listOrderCargoes";
    }

    @GetMapping(path = "manager/delete-{id}-cargo")
    public String deleteCargo(@PathVariable Integer id, Model model)
            throws TruckingServiceException {
        Order order = orderService.findOrderById(cargoService.findCargoById(id).getOrder().getId());
        if (order.getTruck()!=null ||
                (order.getOrderStatus() != OrderStatus.CREATED)) {
            return ORDER_LIST_VIEW_PATH;
        }
        orderService.deleteCargo(id);
        List<Cargo> cargoes = cargoService.findAllCargoesOfOrder(order.getId());
        model.addAttribute("order", order);
        model.addAttribute("cargoes", cargoes);
        return ADD_ORDER_VIEW_PATH;
    }

    @GetMapping(path = "manager/{id}/setOrderTruck")
    public String setTruckToOrder(@PathVariable Integer id, Model model)
            throws TruckingServiceException, NoCargoInTheOrderException {
        Order order = orderService.findOrderById(id);
        if(cargoService.findAllCargoesOfOrder(id)==null ||
                ((order.getOrderStatus() != OrderStatus.CREATED)
                && (order.getOrderStatus() != OrderStatus.INTERRUPTED))){
            return ORDER_LIST_VIEW_PATH;
        }
        orderService.removeTruckAndDriversFromOrder(order);
        model.addAttribute("order", order);
        model.addAttribute("trucks",
                truckService.findAllTrucksReadyForOrder(order));
        return "setordertruck";
    }

    @PostMapping(path = "manager/{id}/setOrderTruck")
    public String saveTruckToOrder(@PathVariable Integer id, Order order, Model model)
            throws TruckingServiceException, NoCargoInTheOrderException{
        Order entityOrder = orderService.findOrderById(id);
        model.addAttribute("order", entityOrder);
        model.addAttribute("trucks",
                truckService.findAllTrucksReadyForOrder(entityOrder));
        entityOrder.setTruck(order.getTruck());
        orderService.updateOrder(entityOrder);
        LOGGER.info("Manager {} has added the truck with id = {} to the order with id = {}",
                getPrincipal(), order.getTruck().getId(), order.getId());
        return "redirect:/manager/"+id+"/setOrderDrivers";
    }

    @GetMapping(path = "manager/{id}/setOrderDrivers")
    public String setDriversToOrder(@PathVariable Integer id, Model model) throws TruckingServiceException{
        Order order = orderService.findOrderById(id);
        if(cargoService.findAllCargoesOfOrder(id)==null ||
                order.getTruck()==null ||
                ((order.getOrderStatus() != OrderStatus.CREATED)
                        && (order.getOrderStatus() != OrderStatus.INTERRUPTED))){
            return ORDER_LIST_VIEW_PATH;
        }
        List<Driver> orderDrivers = driverService.getAllDriversOfOrder(order);
        model.addAttribute("maxDrivers", order.getTruck().getShiftPeriod());
        model.addAttribute("order", order);
        model.addAttribute("driver", new Driver());
        model.addAttribute("orderDrivers", orderDrivers);
        model.addAttribute("drivers", driverService.findAllDriversSuitableForOrder(order));
        return "setorderdrivers";
    }

    @PostMapping(path = "manager/{id}/setOrderDrivers")
    public String saveDriversToOrder(@PathVariable Integer id, Driver newDriver, Model model)
            throws TruckingServiceException{
        Driver driver = driverService.findDriverById(newDriver.getId());
        Order entityOrder = orderService.findOrderById(id);
        driver.setOrder(entityOrder);
        driver.setCurrentTruck(entityOrder.getTruck());
        driverService.updateDriver(driver);
        LOGGER.info("Manager {} has added the driver with id = {} to the order with id = {}",
                getPrincipal(), newDriver.getId(), id);
        return "redirect:/manager/"+id+"/setOrderDrivers";
    }

    @GetMapping(path = "manager/{id}/complete")
    public String completeOrderCreation(@PathVariable Integer id, Model model) throws TruckingServiceException{
        Order order = orderService.findOrderById(id);
        if(cargoService.findAllCargoesOfOrder(id)==null||
                orderService.findOrderById(id).getTruck()==null ||
                driverService.getAllDriversOfOrder(orderService.findOrderById(id))==null ||
                ((order.getOrderStatus() != OrderStatus.CREATED)
                        && (order.getOrderStatus() != OrderStatus.INTERRUPTED))){
            return ORDER_LIST_VIEW_PATH;
        }
        order.setOrderStatus(OrderStatus.IN_PROCESS);
        orderService.updateOrder(order);
        LOGGER.info("Manager {} has completed order with id = {}", getPrincipal(), order.getId());
        return ORDER_LIST_VIEW_PATH;
    }

    @GetMapping(path = "manager/{id}/deleteOrder")
    public String deleteOrder(@PathVariable Integer id, Model model) throws TruckingServiceException{
        orderService.deleteOrder(id);
        return ORDER_LIST_VIEW_PATH;
    }

    //@ModelAttribute("user")
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
    public List<City> cityList() throws TruckingServiceException{
        return cityService.findAllCities();
    }

    @ModelAttribute("orderStatuses")
    public OrderStatus[] orderStatuses(){
        return OrderStatus.values();
    }

    @ModelAttribute("date")
    public LocalDate currentDate(){
        LocalDateTime localDate = LocalDateTime.ofInstant(new Date(System.currentTimeMillis()).toInstant(), ZoneId.systemDefault());
        return localDate.toLocalDate();
    }
}
