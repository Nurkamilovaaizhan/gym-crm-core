package com.gymcrm;

import com.gymcrm.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class MainApplication {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting Spring Application Context...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Spring Application Context started. Press ENTER to exit.");
        System.in.read();
    } }