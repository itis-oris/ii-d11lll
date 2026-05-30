package com.eventoutfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EventOutfitApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventOutfitApplication.class, args);

        System.out.println("Event Outfit приложение запущено");
    }
}