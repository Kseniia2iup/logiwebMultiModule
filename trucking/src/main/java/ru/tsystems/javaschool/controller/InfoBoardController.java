package ru.tsystems.javaschool.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tsystems.javaschool.dto.InfoDto;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.service.InfoBoardService;

@RestController
public class InfoBoardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoBoardController.class);

    private InfoBoardService infoBoardService;

    @Autowired
    public void setInfoBoardService(InfoBoardService infoBoardService) {
        this.infoBoardService = infoBoardService;
    }

    @GetMapping(value = "/emit")
    public @ResponseBody
    InfoDto infoBoardQueue() throws TruckingServiceException {
        LOGGER.info("Emit to infoBoardQueue");
        return infoBoardService.getJSONInfoForUpdate();
    }
}
