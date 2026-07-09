package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.exception.ValidationException;
import com.gymcrm.model.Trainer;
import com.gymcrm.utils.UserUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        UserUtils.setupCredentials(trainer.getUser(), userDao.findAllUsernames());
        Trainer saved = trainerDao.save(trainer);
        log.info("Created trainer profile, username={}", saved.getUser().getUsername());
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
        log.info("Updating trainer profile, username={}", username);
        return trainerDao.update(updated);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.authenticate(username, oldPassword);
        Trainer trainer = trainerDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        trainer.getUser().setPassword(newPassword);
        trainerDao.update(trainer);
        log.info("Password changed for trainer username={}", username);
    }

    @Transactional
    public void setActive(String username, String password, boolean active) {
        authenticationService.authenticate(username, password);
        Trainer trainer = trainerDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        trainer.getUser().setActive(active);
        trainerDao.update(trainer);
        log.info("Trainer {} isActive set to {}", username, active);
    }

    private void validateForCreate(Trainer trainer) {
        if (trainer.getUser() == null
                || trainer.getUser().getFirstName() == null || trainer.getUser().getFirstName().isBlank()
                || trainer.getUser().getLastName() == null || trainer.getUser().getLastName().isBlank()) {
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