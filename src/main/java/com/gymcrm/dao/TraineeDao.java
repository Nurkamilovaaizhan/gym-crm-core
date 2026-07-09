package com.gymcrm.dao;
import com.gymcrm.model.Trainee;
import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee save(Trainee entity);
    Trainee update(Trainee entity);
    Optional<Trainee> findById(Long id);
    List<Trainee> findAll();
    void delete(Trainee entity);
    Optional<Trainee> findByUsername(String username);
    void deleteByUsername(String username);
}