package com.gymcrm.model;

import lombok.Data;
import java.util.Date;

@Data
public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private String trainingType;
    private Date trainingDate;
    private int trainingDuration;
}