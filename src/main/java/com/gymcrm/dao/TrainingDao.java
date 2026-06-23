package com.gymcrm.dao;

import com.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDao {

    private Map<Long, Training> storage;

    @Autowired
    @Qualifier("trainingStorage")
    public void setStorage(Map<Long, Training> storage) {
        this.storage = storage;
    }

    public Training save(Training training) {
        storage.put(training.getId(), training);
        return training;
    }

    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<Training> getAll() {
        return storage.values();
    }
}