package com.gymcrm.storage;

import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Map;

@Slf4j
@Component
public class StorageInitializerPostProcessor implements BeanPostProcessor {

    // Спринг подставит значение из application.properties
    @Value("${storage.init.data.path}")
    private String dataPath;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean; // Ничего не меняем ДО инициализации
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Перехватываем конфигурацию хранилищ ПОСЛЕ создания бина
        if (bean instanceof StorageConfig storageConfig) {
            log.info("BeanPostProcessor triggered: Starting data initialization from file: {}", dataPath);
            loadData(storageConfig.traineeStorage(), storageConfig.trainerStorage(), storageConfig.trainingStorage());
        }
        return bean;
    }

    private void loadData(Map<Long, Trainee> trainees, Map<Long, Trainer> trainers, Map<Long, Training> trainings) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(dataPath)) {
            if (is == null) {
                log.warn("Initialization data file {} not found in resources!", dataPath);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split("\\|");
                    String type = parts[0];

                    switch (type) {
                        case "TRAINEE" -> {
                            Trainee t = new Trainee();
                            t.setId(Long.parseLong(parts[1]));
                            t.setFirstName(parts[2]);
                            t.setLastName(parts[3]);
                            t.setDateOfBirth(dateFormat.parse(parts[4]));
                            t.setAddress(parts[5]);
                            t.setActive(Boolean.parseBoolean(parts[6]));

                            // Базовые креды для дефолтных данных из файла
                            t.setUsername(t.getFirstName() + "." + t.getLastName());
                            t.setPassword("initPass123");

                            trainees.put(t.getId(), t);
                            log.info("Loaded Trainee from file: {}", t.getUsername());
                        }
                        case "TRAINER" -> {
                            Trainer tr = new Trainer();
                            tr.setId(Long.parseLong(parts[1]));
                            tr.setFirstName(parts[2]);
                            tr.setLastName(parts[3]);
                            tr.setSpecialization(parts[4]);
                            tr.setActive(Boolean.parseBoolean(parts[5]));

                            tr.setUsername(tr.getFirstName() + "." + tr.getLastName());
                            tr.setPassword("initPass123");

                            trainers.put(tr.getId(), tr);
                            log.info("Loaded Trainer from file: {}", tr.getUsername());
                        }
                        case "TRAINING" -> {
                            Training tn = new Training();
                            tn.setId(Long.parseLong(parts[1]));
                            tn.setTraineeId(Long.parseLong(parts[2]));
                            tn.setTrainerId(Long.parseLong(parts[3]));
                            tn.setTrainingName(parts[4]);
                            tn.setTrainingType(parts[5]);
                            tn.setTrainingDate(dateFormat.parse(parts[6]));
                            tn.setTrainingDuration(Integer.parseInt(parts[7]));

                            trainings.put(tn.getId(), tn);
                            log.info("Loaded Training from file: {}", tn.getTrainingName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while parsing initialization data file", e);
        }
    }
}