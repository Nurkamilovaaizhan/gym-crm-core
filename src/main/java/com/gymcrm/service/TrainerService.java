package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainer;
import com.gymcrm.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TrainerService {

    private TrainerDao trainerDao;
    private TraineeDao traineeDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainer createTrainer(Trainer trainer) {
        log.info("Creating trainer profile for: {} {}", trainer.getFirstName(), trainer.getLastName());

        // Генерируем username и password с проверкой дубликатов по обеим базам
        UserUtils.setupCredentials(trainer, traineeDao.getStorageMap(), trainerDao.getStorageMap());

        return trainerDao.save(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        log.info("Updating trainer profile with ID: {}", trainer.getId());
        return trainerDao.save(trainer);
    }

    public Optional<Trainer> getTrainer(Long id) {
        log.info("Selecting trainer profile with ID: {}", id);
        return trainerDao.findById(id);
    }
}