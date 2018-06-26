package ru.tsystems.javaschool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.tsystems.javaschool.dto.Truck;
import ru.tsystems.javaschool.service.TruckService;

@Component
public class TruckConverter implements Converter<Object, Truck>{

    private static final Logger LOG = LoggerFactory.getLogger(CityIdToCityConverter.class);
    private TruckService truckService;

    @Autowired
    public void setTruckService(TruckService truckService) {
        this.truckService = truckService;
    }

    @Override
    public Truck convert(Object source) {
        Truck truck;
        try{
            truck = (Truck) source;
            LOG.debug("From TruckConverter convert method\nProfile : {}", truck);
        }
        catch (Exception e){
            try {
                LOG.debug("From TruckConverter convert method\nProfile : {}", e);
                Integer id = Integer.parseInt((String) source);
                truck = truckService.findTruckById(id);
                LOG.info("From TruckConverter convert method\nProfile : {}", truck);
            }
            catch (Exception ex){
                LOG.warn("From TruckConverter convert method\nProfile : {}", ex);
                truck = null;
            }
        }
        return truck;
    }
}

