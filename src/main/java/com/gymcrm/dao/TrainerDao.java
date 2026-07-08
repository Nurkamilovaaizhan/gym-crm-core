package com.gymcrm.dao;
import com.gymcrm.model.Trainer;
import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer save(Trainer entity);
    Trainer update(Trainer entity);
    Optional<Trainer> findById(Long id);
    List<Trainer> findAll();
    void delete(Trainer entity);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findUnassignedTrainersForTrainee(String traineeUsername);
}