package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.exception.ValidationException;
import com.gymcrm.model.Trainer;
import com.gymcrm.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class TrainerService {

    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;
    private final UserDao userDao;
    private final AuthenticationService authenticationService;

    public TrainerService(TrainerDao trainerDao,
                          TraineeDao traineeDao,
                          UserDao userDao,
                          AuthenticationService authenticationService) {
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
        this.userDao = userDao;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        validateForCreate(trainer);
        UserUtils.setupCredentials(trainer, userDao.findAllUsernames());
        Trainer saved = trainerDao.save(trainer);
        log.info("Created trainer profile, username={}", saved.getUsername());
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Trainer> getTrainerByUsername(String username, String password) {
        authenticationService.authenticate(username, password);
        return trainerDao.findByUsername(username);
    }

    @Transactional
    public Trainer updateTrainer(String username, String password, Trainer updated) {
        authenticationService.authenticate(username, password);
        validateForUpdate(updated);

        Trainer existing = trainerDao.findById(updated.getId())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setActive(updated.isActive());

        log.info("Updating trainer profile, username={}", username);
        return trainerDao.update(existing);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.authenticate(username, oldPassword);
        Trainer trainer = trainerDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        trainer.setPassword(newPassword);
        trainerDao.update(trainer);
        log.info("Password changed for trainer username={}", username);
    }

    @Transactional
    public void setActive(String username, String password, boolean active) {
        authenticationService.authenticate(username, password);
        Trainer trainer = trainerDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        if (trainer.isActive() == active) {
            throw new ValidationException("Trainer already has this active state");
        }

        trainer.setActive(active);
        trainerDao.update(trainer);
        log.info("Trainer {} isActive set to {}", username, active);
    }

    private void validateForCreate(Trainer trainer) {
        if (trainer.getFirstName() == null || trainer.getFirstName().isBlank()
                || trainer.getLastName() == null || trainer.getLastName().isBlank()) {
            throw new ValidationException("First name and last name are required");
        }
        if (trainer.getSpecialization() == null) {
            throw new ValidationException("Trainer specialization is required");
        }
    }

    private void validateForUpdate(Trainer trainer) {
        if (trainer.getId() == null) {
            throw new ValidationException("Trainer id is required for update");
        }
    }
}