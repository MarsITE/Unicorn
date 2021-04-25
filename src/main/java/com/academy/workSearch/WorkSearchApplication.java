package com.academy.workSearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
@EnableScheduling
public class WorkSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkSearchApplication.class, args);
    }
}
