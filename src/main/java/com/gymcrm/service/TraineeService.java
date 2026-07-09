package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.exception.ValidationException;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.utils.UserUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class TraineeService {

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final AuthenticationService authenticationService;

    public TraineeService(TraineeDao traineeDao,
                          TrainerDao trainerDao,
                          UserDao userDao,
                          AuthenticationService authenticationService) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.userDao = userDao;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        validateForCreate(trainee);
        UserUtils.setupCredentials(trainee.getUser(), userDao.findAllUsernames());
        Trainee saved = traineeDao.save(trainee);
        log.info("Created trainee profile, username={}", saved.getUser().getUsername());
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Trainee> getTraineeByUsername(String username, String password) {
        authenticationService.authenticate(username, password);
        return traineeDao.findByUsername(username);
    }

    @Transactional
    public Trainee updateTrainee(String username, String password, Trainee updated) {
        authenticationService.authenticate(username, password);
        validateForUpdate(updated);

        Trainee existing = traineeDao.findById(updated.getId())
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setAddress(updated.getAddress());

        if (updated.getUser() != null) {
            existing.getUser().setFirstName(updated.getUser().getFirstName());
            existing.getUser().setLastName(updated.getUser().getLastName());
        }

        log.info("Updating trainee profile, username={}", username);
        return traineeDao.update(existing);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.authenticate(username, oldPassword);
        Trainee trainee = traineeDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        trainee.getUser().setPassword(newPassword);
        traineeDao.update(trainee);
        log.info("Password changed for trainee username={}", username);
    }

    @Transactional
    public void setActive(String username, String password, boolean active) {
        authenticationService.authenticate(username, password);
        Trainee trainee = traineeDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        if (trainee.getUser().isActive() == active) {
            log.warn("Trainee {} already has isActive={}", username, active);
        }
        trainee.getUser().setActive(active);
        traineeDao.update(trainee);
        log.info("Trainee {} isActive set to {}", username, active);
    }

    @Transactional
    public void deleteTraineeByUsername(String username, String password) {
        authenticationService.authenticate(username, password);
        traineeDao.deleteByUsername(username);
        log.info("Deleted trainee (and cascaded trainings) username={}", username);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        authenticationService.authenticate(traineeUsername, password);
        return trainerDao.findUnassignedTrainersForTrainee(traineeUsername);
    }

    @Transactional
    public Set<Trainer> updateTraineeTrainers(String username, String password, Set<Trainer> newTrainers) {
        authenticationService.authenticate(username, password);
        Trainee trainee = traineeDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        trainee.setTrainers(newTrainers);
        traineeDao.update(trainee);
        log.info("Updated trainers list for trainee username={}", username);
        return trainee.getTrainers();
    }

    private void validateForCreate(Trainee trainee) {
        if (trainee.getUser() == null
                || trainee.getUser().getFirstName() == null || trainee.getUser().getFirstName().isBlank()
                || trainee.getUser().getLastName() == null || trainee.getUser().getLastName().isBlank()) {
            throw new ValidationException("First name and last name are required");
        }
    }

    private void validateForUpdate(Trainee trainee) {
        if (trainee.getId() == null) {
            throw new ValidationException("Trainee id is required for update");
        }
    }
}