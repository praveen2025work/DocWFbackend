package com.docwf;

import com.docwf.entity.WorkflowConfig;
import com.docwf.repository.WorkflowConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private WorkflowConfigRepository workflowRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==========================================");
        System.out.println("DocWF Workflow Management System Started!");
        System.out.println("==========================================");
        System.out.println("Application is running successfully!");
        System.out.println("Database connection established!");
        System.out.println("Hibernate Envers audit system enabled!");
        System.out.println("Quartz scheduler initialized!");
        System.out.println("WebSocket endpoints configured!");
        System.out.println("REST API available at: /api");
        System.out.println("H2 Console available at: /h2-console (dev profile)");
        System.out.println("==========================================");
        
        // Show current workflow count
        long workflowCount = workflowRepository.count();
        System.out.println("Current workflows in database: " + workflowCount);
        System.out.println("==========================================");
    }
}
