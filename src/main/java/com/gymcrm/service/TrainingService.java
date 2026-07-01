package com.gymcrm.service;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.exception.ValidationException;
import com.gymcrm.model.Training;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingService {

    @Autowired private TrainingDao trainingDao;
    @Autowired private AuthenticationService authenticationService;

    @Transactional
    public Training addTraining(String authUsername, String authPassword, Training training) {
        authenticationService.authenticate(authUsername, authPassword);
        validateTraining(training);

        training.setTrainingType(training.getTrainer().getSpecialization());

        training.getTrainee().getTrainers().add(training.getTrainer());

        Training saved = trainingDao.save(training);
        log.info("Training '{}' added successfully for trainee {} and trainer {}",
                saved.getTrainingName(), saved.getTrainee().getUser().getUsername(), saved.getTrainer().getUser().getUsername());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username, String password, Date from, Date to,
                                              String trainerName, String trainingType) {
        authenticationService.authenticate(username, password);
        return trainingDao.findByTraineeCriteria(username, from, to, trainerName, trainingType);
    }

    private void validateTraining(Training training) {
        if (training.getTrainee() == null || training.getTrainer() == null) {
            throw new ValidationException("Trainee and Trainer must be specified for the training");
        }
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