package ru.tsystems.javaschool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.tsystems.javaschool.dto.City;
import ru.tsystems.javaschool.service.CityService;

@Component
public class CityIdToCityConverter implements Converter<Object, City> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityIdToCityConverter.class);

    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public City convert(Object source) {
        try {
            Integer id = Integer.parseInt((String) source);
            City city = cityService.findCityById(id);
            LOGGER.info("From CityIdToCityConverter convert method\nProfile : {}", city);
            return city;
        }
        catch (Exception e){
            LOGGER.warn("From CityIdToCityConverter convert method\nProfile : {}", e);
            return null;
        }
    }
}
