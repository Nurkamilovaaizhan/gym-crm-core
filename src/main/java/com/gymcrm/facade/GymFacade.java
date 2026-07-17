package com.gymcrm.facade;

import com.gymcrm.dao.UserDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
import com.gymcrm.model.TrainingType;
import com.gymcrm.model.User;
import com.gymcrm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final AuthenticationService authenticationService;
    private final UserDao userDao;

    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService,
                     TrainingTypeService trainingTypeService,
                     AuthenticationService authenticationService,
                     UserDao userDao) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.authenticationService = authenticationService;
        this.userDao = userDao;
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

    public Training createTraining(String authUsername, String authPassword, String traineeUsername, String trainerUsername, Training training) {
        return trainingService.addTraining(authUsername, authPassword, traineeUsername, trainerUsername, training);
    }

    public List<Training> getTraineeTrainings(String username, String password, Date from, Date to,
                                              String trainerName, String trainingType) {
        return trainingService.getTraineeTrainings(username, password, from, to, trainerName, trainingType);
    }

    public void login(String username, String password) {
        authenticationService.authenticate(username, password);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.authenticate(username, oldPassword);
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(newPassword);
        userDao.update(user);
        log.info("Password changed for username={}", username);
    }

    public void setTraineeActive(String username, String password, boolean active) {
        traineeService.setActive(username, password, active);
    }

    public void setTrainerActive(String username, String password, boolean active) {
        trainerService.setActive(username, password, active);
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        return traineeService.getUnassignedTrainers(traineeUsername, password);
    }

    public Set<Trainer> updateTraineeTrainers(String username, String password, Set<String> trainerUsernames) {
        return traineeService.updateTraineeTrainers(username, password, trainerUsernames);
    }

    public List<Training> getTrainerTrainings(String username, String password, Date from, Date to, String traineeName) {
        return trainingService.getTrainerTrainings(username, password, from, to, traineeName);
    }

    public List<TrainingType> getTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}