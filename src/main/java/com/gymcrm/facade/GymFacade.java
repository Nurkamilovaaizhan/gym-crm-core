package com.gymcrm.facade;

import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
import com.gymcrm.service.TraineeService;
import com.gymcrm.service.TrainerService;
import com.gymcrm.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        log.info("GymFacade initialized successfully using constructor injection.");
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    public Optional<Trainee> getTrainee(String username, String password) {
        return traineeService.getTraineeByUsername(username, password);
    }

    public Optional<Trainer> getTrainer(String username, String password) {
        return trainerService.getTrainerByUsername(username, password);
    }

    public Trainee updateTrainee(String username, String password, Trainee trainee) {
        return traineeService.updateTrainee(username, password, trainee);
    }

    public Trainer updateTrainer(String username, String password, Trainer trainer) {
        return trainerService.updateTrainer(username, password, trainer);
    }

    public void deleteTrainee(String username, String password) {
        traineeService.deleteTraineeByUsername(username, password);
    }

    public Training createTraining(String authUsername, String authPassword, Training training) {
        return trainingService.addTraining(authUsername, authPassword, training);
    }

    public List<Training> getTraineeTrainings(String username, String password, Date from, Date to,
                                              String trainerName, String trainingType) {
        return trainingService.getTraineeTrainings(username, password, from, to, trainerName, trainingType);
    }
}