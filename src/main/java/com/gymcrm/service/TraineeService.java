package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TraineeService {

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    // Setter-based injection
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainee createTrainee(Trainee trainee) {
        log.info("Creating trainee profile for: {} {}", trainee.getFirstName(), trainee.getLastName());

        // Генерируем username и password на основе заполненных Map-хранилищ
        UserUtils.setupCredentials(trainee, traineeDao.getStorageMap(), trainerDao.getStorageMap());

        return traineeDao.save(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        log.info("Updating trainee profile with ID: {}", trainee.getId());
        return traineeDao.save(trainee);
    }

    public void deleteTrainee(Long id) {
        log.info("Deleting trainee profile with ID: {}", id);
        traineeDao.deleteById(id);
    }

    public Optional<Trainee> getTrainee(Long id) {
        log.info("Selecting trainee profile with ID: {}", id);
        return traineeDao.findById(id);
    }
}