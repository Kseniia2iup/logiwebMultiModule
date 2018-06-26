package ru.tsystems.javaschool.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.service.UserService;

import java.util.regex.Pattern;

@Component
public class DriverValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverValidator.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Driver.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Driver driver = (Driver) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "name", "name", "Name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "surname", "surname", "Surname is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "email", "email", "Email is required.");


        Pattern pattern = Pattern.compile("[a-z[A-Z[-[ ]]]]*");
        if(!pattern.matcher(driver.getName()).matches()){
            errors.rejectValue("name", "name",
                    "Name can contains only ' ', '-', a-z and A-Z symbols.");
        }
        if(!pattern.matcher(driver.getSurname()).matches()){
            errors.rejectValue("surname", "surname",
                    "Surname can contains only ' ', '-', a-z and A-Z symbols.");
        }


        try {
            if(!userService.isEmailValid(driver.getEmail())){
                errors.rejectValue("email", "email",
                        "Email is not valid.");
            }

        }
        catch (Exception e){
            LOGGER.warn("From UserValidator method validate\n", e);
        }

        try {
            if(!userService.isEmailUnique(driver.getEmail())){
                errors.rejectValue("email", "email",
                        "Email is not unique.");
            }

        }
        catch (Exception e){
            LOGGER.warn("From UserValidator method validate\n", e);
        }
    }
}
