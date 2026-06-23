package com.gymcrm.dao;

import com.gymcrm.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDao {

    private Map<Long, Trainee> storage;

    // Сеттер-инжекшн (The rest of the injections should be done in a setter-based way)
    @Autowired
    @Qualifier("traineeStorage")
    public void setStorage(Map<Long, Trainee> storage) {
        this.storage = storage;
    }

    public Trainee save(Trainee trainee) {
        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public Collection<Trainee> getAll() {
        return storage.values();
    }

    public Map<Long, Trainee> getStorageMap() {
        return storage;
    }
}