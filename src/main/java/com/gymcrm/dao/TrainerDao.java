package com.gymcrm.dao;

import com.gymcrm.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDao {

    private Map<Long, Trainer> storage;

    @Autowired
    @Qualifier("trainerStorage")
    public void setStorage(Map<Long, Trainer> storage) {
        this.storage = storage;
    }

    public Trainer save(Trainer trainer) {
        storage.put(trainer.getId(), trainer);
        return trainer;
    }

    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<Trainer> getAll() {
        return storage.values();
    }

    public Map<Long, Trainer> getStorageMap() {
        return storage;
    }
}