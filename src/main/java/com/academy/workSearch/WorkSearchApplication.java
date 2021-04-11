package com.academy.workSearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class WorkSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkSearchApplication.class, args);
    }
}
