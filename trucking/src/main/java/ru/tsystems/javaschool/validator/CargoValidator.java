package ru.tsystems.javaschool.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.tsystems.javaschool.dto.Cargo;

@Component
public class CargoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Cargo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Cargo cargo = (Cargo) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "name", "name", "Name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "weight", "weight", "Weight is required.");

        try {
            if(cargo.getWeight()<1){
                errors.rejectValue("weight", "weight",
                        "Please enter a natural number");
            }
            else if(cargo.getWeight()>100000){
                errors.rejectValue("weight", "weight",
                        "Number is too big");
            }
        } catch (Exception e) {
            errors.rejectValue("weight", "weight",
                    "Please enter a natural number");
        }
    }
}
