package com.gymcrm.controller;

import com.gymcrm.dto.TrainingTypeDto;
import com.gymcrm.facade.GymFacade;
import com.gymcrm.mapper.RestMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/training-types")
public class TrainingTypeController {

    private final GymFacade gymFacade;

    public TrainingTypeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @GetMapping
    public List<TrainingTypeDto> getAll() {
        return gymFacade.getTrainingTypes()
                .stream()
                .map(RestMapper::toDto)
                .collect(Collectors.toList());
    }
}