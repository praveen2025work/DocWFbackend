package com.docwf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan("com.docwf.entity")
@EnableJpaRepositories("com.docwf.repository")
@EnableJpaAuditing
@EnableScheduling
@EnableTransactionManagement
public class WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }
}
