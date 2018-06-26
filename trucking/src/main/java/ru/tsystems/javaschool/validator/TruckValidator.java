package ru.tsystems.javaschool.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.service.TruckService;

@Component
public class TruckValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckValidator.class);

    private TruckService truckService;

    @Autowired
    public void setTruckService(TruckService truckService) {
        this.truckService = truckService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Truck.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Truck truck = (Truck) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "regNumber", "regNumber", "Reg Number is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "shiftPeriod", "shiftPeriod", "Shift Period is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "capacityTon", "capacity", "Capacity is required.");


        try {
            if(truck.getShiftPeriod()<1){
                errors.rejectValue("shiftPeriod", "shiftPeriod",
                        "Please enter a natural number");
            }
            else if(truck.getShiftPeriod()>10){
                errors.rejectValue("shiftPeriod", "shiftPeriod",
                        "Number is too big");
            }
        } catch (Exception e) {
            errors.rejectValue("shiftPeriod", "shiftPeriod",
                    "Please enter a natural number");
        }

        try {
            if(truck.getCapacityTon()<1){
                errors.rejectValue("capacityTon", "capacity",
                        "Please enter a natural number");
            }
            else if(truck.getCapacityTon()>100){
                errors.rejectValue("capacityTon", "capacity",
                        "Number is too big");
            }
        } catch (Exception e) {
            errors.rejectValue("capacityTon", "capacity",
                    "Please enter a natural number");
        }


        try {
            if(!truckService.isTruckRegNumberUnique(truck.getId(), truck.getRegNumber())){
                errors.rejectValue("regNumber", "regNumber",
                        "Reg Number is not unique. Try another one");
            }
        } catch (TruckingServiceException e) {
            LOGGER.warn("From TruckValidator method validate\n", e);
        }


        if (!truckService.isTruckRegNumberIsValid(truck.getId(), truck.getRegNumber())){
            errors.rejectValue("regNumber", "regNumber",
                    "Reg Number is not valid. Reg Number starts with two alphabet characters and ends with five digits");
        }

    }
}
