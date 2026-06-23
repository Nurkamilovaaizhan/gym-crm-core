package com.gymcrm.service;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TrainingService {

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training createTraining(Training training) {
        log.info("Creating training profile: {}", training.getTrainingName());
        return trainingDao.save(training);
    }

    public Optional<Training> getTraining(Long id) {
        log.info("Selecting training profile with ID: {}", id);
        return trainingDao.findById(id);
    }
}