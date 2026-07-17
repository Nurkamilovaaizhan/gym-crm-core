package com.gymcrm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TrainingDto {
    private String traineeUsername;
    private String trainerUsername;
    private String traineeName;
    private String trainerName;
    private String trainingName;
    private Date trainingDate;
    private int trainingDuration;
    private TrainingTypeDto trainingType;
}