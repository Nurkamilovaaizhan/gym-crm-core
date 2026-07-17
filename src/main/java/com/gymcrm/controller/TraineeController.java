package com.gymcrm.controller;

import com.gymcrm.dto.*;
import com.gymcrm.facade.GymFacade;
import com.gymcrm.mapper.RestMapper;
import com.gymcrm.model.Trainer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final GymFacade gymFacade;

    public TraineeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping
    public CredentialsDto create(@RequestBody TraineeDto request) {
        var saved = gymFacade.createTrainee(RestMapper.toEntity(request));
        CredentialsDto response = new CredentialsDto();
        response.setUsername(saved.getUsername());
        response.setPassword(saved.getPassword());
        return response;
    }

    @GetMapping("/{username}")
    public TraineeDto get(@PathVariable(value = "username") String username,
                          @RequestHeader(value = "password") String password) {
        return RestMapper.toDto(
                gymFacade.getTrainee(username, password)
                        .orElseThrow(() -> new IllegalArgumentException("Trainee not found"))
        );
    }

    @PutMapping("/{username}")
    public TraineeDto update(@PathVariable(value = "username") String username,
                             @RequestHeader(value = "password") String password,
                             @RequestBody TraineeDto request) {
        return RestMapper.toDto(gymFacade.updateTrainee(username, password, RestMapper.toEntity(request)));
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable(value = "username") String username,
                       @RequestHeader(value = "password") String password) {
        gymFacade.deleteTrainee(username, password);
    }

    @PatchMapping("/{username}/active")
    public void setActive(@PathVariable(value = "username") String username,
                          @RequestHeader(value = "password") String password,
                          @RequestParam(value = "active") boolean active) {
        gymFacade.setTraineeActive(username, password, active);
    }

    @GetMapping("/{username}/trainings")
    public List<TrainingDto> getTrainings(@PathVariable(value = "username") String username,
                                          @RequestHeader(value = "password") String password,
                                          @RequestParam(value = "from", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                          @RequestParam(value = "to", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
                                          @RequestParam(value = "trainerName", required = false) String trainerName,
                                          @RequestParam(value = "trainingType", required = false) String trainingType) {
        return gymFacade.getTraineeTrainings(username, password, from, to, trainerName, trainingType)
                .stream()
                .map(RestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{username}/trainers/unassigned")
    public List<TrainerShortDto> getUnassignedTrainers(@PathVariable(value = "username") String username,
                                                       @RequestHeader(value = "password") String password) {
        return gymFacade.getUnassignedTrainers(username, password)
                .stream()
                .map(RestMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{username}/trainers")
    public List<TrainerShortDto> updateTrainers(@PathVariable(value = "username") String username,
                                                @RequestHeader(value = "password") String password,
                                                @RequestBody Set<String> trainerUsernames) {
        return gymFacade.updateTraineeTrainers(username, password, trainerUsernames)
                .stream()
                .map(RestMapper::toShortDto)
                .collect(Collectors.toList());
    }
}