package com.docwf.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DocWF Workflow Management System API")
                        .description("""
                                Comprehensive REST API for managing workflows, tasks, and workflow executions.
                                
                                ## Features
                                - **Workflow Management**: Create, update, deploy, and manage workflows
                                - **Task Execution**: Handle task assignments, completions, and status tracking
                                - **Role Management**: Manage user roles and permissions within workflows
                                - **Real-time Communication**: WebSocket support for live updates
                                - **Audit Trail**: Complete change history with Hibernate Envers
                                - **Scheduling**: Automated workflow triggering with Quartz scheduler
                                
                                ## Authentication
                                - Development: No authentication required
                                - Production: Basic authentication with role-based access control
                                
                                ## Database Support
                                - Development: H2 in-memory database
                                - Production: Oracle database with full audit capabilities
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DocWF Development Team")
                                .email("dev@docwf.com")
                                .url("https://docwf.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.docwf.com")
                                .description("Production Server")
                ));
    }
}
