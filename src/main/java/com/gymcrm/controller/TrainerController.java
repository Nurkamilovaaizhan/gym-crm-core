package com.gymcrm.controller;

import com.gymcrm.dto.CredentialsDto;
import com.gymcrm.dto.TrainerDto;
import com.gymcrm.dto.TrainingDto;
import com.gymcrm.facade.GymFacade;
import com.gymcrm.mapper.RestMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final GymFacade gymFacade;

    public TrainerController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping
    public CredentialsDto create(@RequestBody TrainerDto request) {
        var saved = gymFacade.createTrainer(RestMapper.toEntity(request));
        CredentialsDto response = new CredentialsDto();
        response.setUsername(saved.getUsername());
        response.setPassword(saved.getPassword());
        return response;
    }

    @GetMapping("/{username}")
    public TrainerDto get(@PathVariable(value = "username") String username,
                          @RequestHeader(value = "password") String password) {
        return RestMapper.toDto(
                gymFacade.getTrainer(username, password)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer not found"))
        );
    }

    @PutMapping("/{username}")
    public TrainerDto update(@PathVariable(value = "username") String username,
                             @RequestHeader(value = "password") String password,
                             @RequestBody TrainerDto request) {
        return RestMapper.toDto(gymFacade.updateTrainer(username, password, RestMapper.toEntity(request)));
    }

    @PatchMapping("/{username}/active")
    public void setActive(@PathVariable(value = "username") String username,
                          @RequestHeader(value = "password") String password,
                          @RequestParam(value = "active") boolean active) {
        gymFacade.setTrainerActive(username, password, active);
    }

    @GetMapping("/{username}/trainings")
    public List<TrainingDto> getTrainings(@PathVariable(value = "username") String username,
                                          @RequestHeader(value = "password") String password,
                                          @RequestParam(value = "from", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                          @RequestParam(value = "to", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
                                          @RequestParam(value = "traineeName", required = false) String traineeName) {
        return gymFacade.getTrainerTrainings(username, password, from, to, traineeName)
                .stream()
                .map(RestMapper::toDto)
                .collect(Collectors.toList());
    }
}