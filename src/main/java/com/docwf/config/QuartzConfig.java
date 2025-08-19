package com.docwf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class QuartzConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        // This method will be called after the bean is constructed
        // We'll use it to set up the application context in Quartz
    }

    @Bean
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
