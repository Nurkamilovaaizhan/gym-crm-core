package com.gymcrm.dao;

import com.gymcrm.model.Trainer;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerDao {
    Trainer save(Trainer entity);
    Trainer update(Trainer entity);
    Optional<Trainer> findById(Long id);
    List<Trainer> findAll();
    void delete(Trainer entity);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findUnassignedTrainersForTrainee(String traineeUsername);
    Set<Trainer> findByUsernames(Set<String> usernames);
}