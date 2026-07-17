package com.gymcrm.controller;

import com.gymcrm.dto.TrainingDto;
import com.gymcrm.facade.GymFacade;
import com.gymcrm.mapper.RestMapper;
import com.gymcrm.model.Training;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    private final GymFacade gymFacade;

    public TrainingController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping
    public TrainingDto create(@RequestHeader(value = "username") String username,
                              @RequestHeader(value = "password") String password,
                              @RequestBody TrainingDto request) {
        Training saved = gymFacade.createTraining(
                username,
                password,
                request.getTraineeUsername(),
                request.getTrainerUsername(),
                RestMapper.toEntity(request)
        );
        return RestMapper.toDto(saved);
    }
}