package ru.tsystems.javaschool.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.User;
import ru.tsystems.javaschool.dto.enums.Role;
import ru.tsystems.javaschool.service.UserService;
import ru.tsystems.javaschool.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private static final String USER_LIST_VIEW_PATH = "redirect:/admin/listUsers";
    private static final String ADD_USER_VIEW_PATH = "newuser";

    private UserValidator userValidator;

    private UserService userService;

    @Autowired
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = {"/", "/home", "/welcome", "/index"})
    public String indexPage(Model model)
    {
        return "redirect:/login";
    }

    @GetMapping(path = "/admin")
    public String adminPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return USER_LIST_VIEW_PATH;
    }

    @GetMapping(path = "/manager")
    public String managerPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "redirect:/manager/listOrders";
    }

    @GetMapping(value = "/Access_Denied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "accessDenied";
    }

    @GetMapping(path = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }


    @GetMapping(value = { "/admin/listUsers" })
    public String listUsers(ModelMap model) throws TruckingServiceException {
        List<User> users = userService.findAllUsers();
        model.addAttribute("currentUser", getPrincipal());
        model.addAttribute("users", users);
        return "allusers";
    }

    @GetMapping(path = "/admin/newUser")
    public String newRegistration(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("currentUser", getPrincipal());
        model.addAttribute("edit", false);
        return ADD_USER_VIEW_PATH;
    }

    @PostMapping(path = "/admin/newUser")
    public String saveRegistration(@Valid User user,
                                   BindingResult result, ModelMap model)
            throws TruckingServiceException {

        userValidator.validate(user, result);

        if (result.hasErrors()) {
            LOGGER.debug("There are errors while new user registration");
            return ADD_USER_VIEW_PATH;
        }
        userService.save(user);

        LOGGER.info("From MainController saveRegistration method:\nLogin : {}\nChecking UsrProfiles....",user.getLogin());

        model.addAttribute("success", "User " + user.getLogin() + " has been registered successfully");
        return "registrationsuccess";
    }

    @GetMapping(value = { "/admin/edit-user-{login}" })
    public String editUser(@PathVariable String login, ModelMap model){
        User user = userService.findByLogin(login);
        model.addAttribute("currentUser", getPrincipal());
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        return ADD_USER_VIEW_PATH;
    }

    @PostMapping(value = { "/admin/edit-user-{login}" })
    public String updateUser(@Valid User user, BindingResult result,
                             ModelMap model, @PathVariable String login)
            throws TruckingServiceException {

        userValidator.validate(user, result);

        if (result.hasErrors()) {
            model.addAttribute("currentUser", getPrincipal());
            model.addAttribute("user", user);
            model.addAttribute("edit", true);
            return ADD_USER_VIEW_PATH;
        }
        userService.updateUser(user);

        model.addAttribute("success", "User " + user.getLogin() + " updated successfully");
        return "registrationsuccess";
    }

    @GetMapping(value = { "/admin/delete-user-{login}" })
    public String deleteUser(@PathVariable String login) throws TruckingServiceException {
        userService.delete(userService.findByLogin(login).getId());
        return USER_LIST_VIEW_PATH;
    }

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

    @ModelAttribute("adminRegistrationRoles")
    public Role[] adminRegistrationRoles(){
        Role[] roles = new Role[2];
        roles[0] = Role.ADMIN;
        roles[1] = Role.USER;
        return roles;
    }

    @ModelAttribute("roles")
    public Role[] initializeProfiles() {
        return Role.values();
    }

    @ModelAttribute("date")
    public LocalDate currentDate(){
        LocalDateTime localDate = LocalDateTime.ofInstant(new Date(System.currentTimeMillis()).toInstant(), ZoneId.systemDefault());
        return localDate.toLocalDate();
    }

}
