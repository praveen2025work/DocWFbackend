# Workflow Management System

A comprehensive Java Spring Boot backend application for managing complex workflows with file operations, task sequencing, decision support, and role-based access control.

## 🚀 Features

### Core Functionality
- **Workflow Configuration**: Define workflows with tasks, roles, and parameters
- **Task Sequencing**: Maintain proper order and dependencies between tasks
- **File Management**: Upload, download, update, and consolidate files
- **Decision Support**: Branch workflows based on task outcomes
- **Role-Based Access**: Assign users to roles within workflows
- **Escalation & Reminders**: Configurable notifications and reassignments
- **Audit Trail**: Complete history tracking with Hibernate Envers

### Task Types
- **FILE_UPLOAD**: Upload files to the system
- **FILE_DOWNLOAD**: Download files from the system
- **FILE_UPDATE**: Update existing files
- **CONSOLIDATE_FILES**: Combine multiple files into one
- **DECISION**: Make decisions that affect workflow flow

## 🏗️ Architecture

### Layers
1. **Reference Layer**: Users, Roles
2. **Configuration Layer**: Workflow definitions, tasks, parameters
3. **Execution Layer**: Workflow instances, task execution, file handling

### Technology Stack
- **Java 17+** with Spring Boot 3.x
- **Oracle Database** with Spring Data JPA
- **Hibernate Envers** for auditing
- **Quartz Scheduler** for reminders and escalations
- **OpenAPI/Swagger** for API documentation
- **Jakarta Validation** for request validation

## 📁 Project Structure

```
src/main/java/com/docwf/
├── config/           # Configuration classes
├── controller/       # REST API controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── enums/           # Enumerations
├── exception/       # Custom exceptions
├── job/             # Scheduled jobs
├── repository/      # Data access layer
└── service/         # Business logic layer
```

## 🗄️ Database Schema

### Core Tables
- `WORKFLOW_USER` - System users with escalation hierarchy
- `WORKFLOW_ROLE` - Available roles (Preparer, Reviewer, Approver, Auditor)
- `WORKFLOW_CONFIG` - Workflow definitions with timing parameters
- `WORKFLOW_CONFIG_TASK` - Tasks within workflows with sequence order
- `WORKFLOW_CONFIG_TASK_FILE` - File configurations for tasks
- `TASK_DECISION_OUTCOME` - Decision outcomes and next task mapping
- `WORKFLOW_INSTANCE` - Running workflow instances
- `WORKFLOW_INSTANCE_TASK` - Task execution instances
- `WORKFLOW_INSTANCE_TASK_FILE` - Actual files in workflow execution

### Audit Tables
All tables have corresponding audit tables with `_AUD` suffix for complete history tracking.

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Oracle Database 19c or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd DocWF
   ```

2. **Configure database**
   - Create Oracle database and user
   - Update `application.yml` with database credentials
   - Run DDL scripts from `src/main/resources/sql/ddl.sql`

3. **Load sample data**
   ```bash
   # Run the sample data script
   sqlplus <username>/<password>@<database> @src/main/resources/sql/sample_data.sql
   ```

4. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the application**
   - API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Configuration

Update `application.yml` with your settings:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    username: your_username
    password: your_password
  
app:
  file:
    upload-dir: /data/uploads
    consolidated-dir: /data/consolidated
    max-file-size: 10MB
  
  workflow:
    trigger-interval: 300000  # 5 minutes
    reminder-before-due: 15   # minutes
    escalation-after: 30      # minutes
```

## 📚 API Documentation

### Core Endpoints

#### Users
- `POST /api/users` - Create user
- `GET /api/users/{userId}` - Get user by ID
- `PUT /api/users/{userId}` - Update user
- `DELETE /api/users/{userId}` - Delete user

#### Workflows
- `POST /api/workflows` - Create workflow
- `GET /api/workflows/{workflowId}` - Get workflow by ID
- `PUT /api/workflows/{workflowId}` - Update workflow
- `DELETE /api/workflows/{workflowId}` - Delete workflow

#### Workflow Execution
- `POST /api/execution/workflows/{workflowId}/start` - Start workflow instance
- `GET /api/execution/instances/{instanceId}/tasks` - Get instance tasks
- `POST /api/execution/tasks/{instanceTaskId}/complete` - Complete task

#### File Management
- `POST /api/files/upload` - Upload file
- `GET /api/files/download/{filename}` - Download file
- `POST /api/files/consolidate` - Consolidate files

### Sample API Usage

#### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "isActive": "Y",
    "createdBy": "system"
  }'
```

#### Start a Workflow
```bash
curl -X POST "http://localhost:8080/api/execution/workflows/1/start?startedByUserId=1"
```

#### Upload a File
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@document.pdf" \
  -F "instanceTaskId=1" \
  -F "actionType=FILE_UPLOAD" \
  -F "createdBy=john.doe"
```

## 🔄 Workflow Lifecycle

1. **Configuration**: Define workflow with tasks, roles, and parameters
2. **Instantiation**: Create workflow instance when needed
3. **Execution**: Execute tasks in sequence order
4. **File Operations**: Handle file uploads, updates, and consolidation
5. **Decision Making**: Process decision outcomes and route to next tasks
6. **Completion**: Mark workflow as completed when all tasks are done

## 📊 Task Sequencing

Tasks are executed based on their `sequenceOrder` field. The system ensures:
- Tasks are executed in the correct order
- Dependencies are respected
- Decision outcomes determine the next task
- Failed tasks can be retried or escalated

## 🔐 Security & Access Control

- **Role-Based Access**: Users are assigned roles within workflows
- **Escalation Hierarchy**: Tasks can be escalated to higher-level users
- **Audit Logging**: All changes are tracked with user and timestamp
- **Validation**: Request validation using Jakarta Validation

## 📅 Scheduling & Notifications

- **Reminders**: Configurable reminders before task due dates
- **Escalations**: Automatic escalation after configurable delays
- **Quartz Integration**: Scheduled job execution for notifications
- **Email Support**: Placeholder for email notification system

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Data
Sample data is provided in `src/main/resources/sql/sample_data.sql` including:
- Sample users (Alice, Bob, Carol, David, Eve)
- Sample roles (Preparer, Reviewer, Approver, Auditor)
- Sample workflows (Monthly Finance, Quarterly Audit)
- Sample tasks and decision outcomes

## 🚀 Deployment

### Development
```bash
./run-dev.sh
```

### Production
```bash
./run-prod.sh
```

### Docker (Optional)
```bash
docker build -t workflow-system .
docker run -p 8080:8080 workflow-system
```

## 📈 Monitoring & Health

- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Audit Logs**: Database audit tables
- **Application Logs**: Configurable logging levels

## 🔧 Customization

### Adding New Task Types
1. Extend the `TaskType` enum
2. Add corresponding logic in services
3. Update controllers if needed

### Custom Workflow Parameters
1. Use the `WORKFLOW_CONFIG_PARAM` table
2. Add parameter validation in services
3. Implement parameter-specific logic

### Custom Escalation Rules
1. Extend escalation logic in services
2. Add custom escalation criteria
3. Implement notification mechanisms

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the API documentation at `/swagger-ui.html`
- Review the sample data and configuration

## 🔮 Future Enhancements

- **Real-time Notifications**: WebSocket support for live updates
- **Advanced File Processing**: OCR, PDF processing, image analysis
- **Workflow Templates**: Pre-built workflow configurations
- **Mobile Support**: Mobile-optimized interfaces
- **Integration APIs**: Connect with external systems
- **Advanced Analytics**: Workflow performance metrics and insights
