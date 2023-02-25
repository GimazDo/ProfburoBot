package com.github.gimazdo.profburobot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ProfburoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfburoBotApplication.class, args);
        log.info("Application started");
    }

}
