package com.gymcrm;

import com.gymcrm.config.AppConfig;
import com.gymcrm.facade.GymFacade;
import com.gymcrm.model.Trainee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println("Starting Spring Application Context...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Spring Application Context started successfully!\n");

        GymFacade facade = context.getBean(GymFacade.class);

        Trainee trainee = new Trainee();
        trainee.setId(3L);
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        trainee.setAddress("Bishkek");

        Trainee savedTrainee = facade.createTrainee(trainee);

        System.out.println("\n=================================================");
        System.out.println("Testing Username Generation Logic for Duplicates:");
        System.out.println("Expected username: John.Smith1");
        System.out.println("Calculated Username: " + savedTrainee.getUsername());
        System.out.println("Calculated Password (10 chars): " + savedTrainee.getPassword());
        System.out.println("=================================================\n");

        context.close();
        System.out.println("Spring Application Context closed.");
    }
}