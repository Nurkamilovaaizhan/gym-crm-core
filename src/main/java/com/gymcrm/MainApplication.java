package com.gymcrm;

import com.gymcrm.config.AppConfig;
import com.gymcrm.facade.GymFacade;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println("Starting Spring Application Context...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Spring Application Context started successfully!\n");

        GymFacade facade = context.getBean(GymFacade.class);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("Bishkek");
        trainee.setDateOfBirth(new Date());

        Trainee savedTrainee = facade.createTrainee(trainee);

        System.out.println("\n=================================================");
        System.out.println("Testing Username Generation Logic for Duplicates:");
        System.out.println("Expected username format: John.Smith (or John.Smith1 if duplicate)");
        System.out.println("Calculated Username: " + savedTrainee.getUser().getUsername());
        System.out.println("Calculated Password (10 chars): " + savedTrainee.getUser().getPassword());
        System.out.println("=================================================\n");

        context.close();
        System.out.println("Spring Application Context closed.");
    }
}