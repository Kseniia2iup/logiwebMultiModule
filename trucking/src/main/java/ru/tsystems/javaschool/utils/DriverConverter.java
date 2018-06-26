package ru.tsystems.javaschool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.tsystems.javaschool.dto.Driver;
import ru.tsystems.javaschool.service.DriverService;

@Component
public class DriverConverter implements Converter<Object, Driver> {

    private static final Logger LOG = LoggerFactory.getLogger(DriverConverter.class);

    private DriverService driverService;

    @Autowired
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public Driver convert(Object source) {
        Driver driver;
        try {
            driver = (Driver) source;
            LOG.debug("From DriverConverter convert method\nProfile : {}", driver);
        }
        catch (Exception e){
            try {
                LOG.debug("From DriverConverter convert method\nProfile : {}", e);
                Integer id = Integer.parseInt((String) source);
                driver = driverService.findDriverById(id);
                LOG.debug("From DriverConverter convert method\nProfile : {}", driver);
            }
            catch (Exception ex){
                LOG.debug("From DriverConverter convert method\nProfile : {}", ex);
                driver = null;
            }
        }
        return driver;
    }
}
