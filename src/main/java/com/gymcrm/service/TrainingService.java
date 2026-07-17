package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.TrainingDao;
import com.gymcrm.exception.ValidationException;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TrainingService {

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final AuthenticationService authenticationService;

    public TrainingService(TrainingDao trainingDao,
                           TraineeDao traineeDao,
                           TrainerDao trainerDao,
                           AuthenticationService authenticationService) {
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Training addTraining(String authUsername, String authPassword, String traineeUsername, String trainerUsername, Training training) {
        authenticationService.authenticate(authUsername, authPassword);

        Trainee trainee = traineeDao.findByUsername(traineeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        Trainer trainer = trainerDao.findByUsername(trainerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        validateTraining(training);

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization());

        trainee.getTrainers().add(trainer);

        Training saved = trainingDao.save(training);
        log.info("Training '{}' added successfully for trainee {} and trainer {}",
                saved.getTrainingName(), trainee.getUsername(), trainer.getUsername());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username, String password, Date from, Date to,
                                              String trainerName, String trainingType) {
        authenticationService.authenticate(username, password);
        return trainingDao.findByTraineeCriteria(username, from, to, trainerName, trainingType);
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username, String password, Date from, Date to, String traineeName) {
        authenticationService.authenticate(username, password);
        return trainingDao.findByTrainerCriteria(username, from, to, traineeName);
    }

    private void validateTraining(Training training) {
        if (training.getTrainingName() == null || training.getTrainingName().isBlank()) {
            throw new ValidationException("Training name cannot be empty");
        }
        if (training.getTrainingDate() == null) {
            throw new ValidationException("Training date is required");
        }
        if (training.getTrainingDuration() <= 0) {
            throw new ValidationException("Training duration must be greater than zero");
        }
    }
}