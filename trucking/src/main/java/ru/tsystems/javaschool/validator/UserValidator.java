package ru.tsystems.javaschool.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.tsystems.javaschool.dto.User;
import ru.tsystems.javaschool.service.UserService;

@Component
public class UserValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "login", "login", "Login is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "password", "password", "Password is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "email", "email", "Email is required.");

        try {
            if(!userService.isEmailValid(user.getEmail())){
                errors.rejectValue("email", "email",
                        "Email is not valid.");
            }

        }
        catch (Exception e){
            LOGGER.warn("From UserValidator method validate\n", e);
        }

        try {
            if(!userService.isEmailUnique(user.getEmail())){
                errors.rejectValue("email", "email",
                        "Email is not unique.");
            }

        }
        catch (Exception e){
            LOGGER.warn("From UserValidator method validate\n", e);
        }

        try {
            if (!userService.isUserValid(user)){
                errors.rejectValue("login", "login",
                        "Incorrect data.");
            }
        } catch (Exception e){
            LOGGER.warn("From UserValidator method validate\n", e);
        }
    }
}
