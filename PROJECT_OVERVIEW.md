# DocWF - Document Workflow Management System

## 🎯 Project Overview

DocWF is a comprehensive Java Spring Boot application designed for managing complex document workflows with advanced file versioning, calendar-based execution, and role-based access control. The system supports multi-step workflows with file operations, decision points, and automated scheduling.

## 🚀 Key Features

### Core Functionality
- **Advanced Workflow Management**: Define and execute complex multi-step workflows
- **File Versioning System**: Complete file version control with composite primary keys
- **Calendar-Based Execution**: Schedule workflows based on business calendars
- **Role-Based Access Control**: Granular permissions with admin capabilities
- **Task Sequencing**: Maintain proper order and dependencies between tasks
- **Decision Support**: Branch workflows based on task outcomes
- **Escalation & Reminders**: Configurable notifications and reassignments
- **Complete Audit Trail**: Full history tracking with Hibernate Envers

### Recent Schema Enhancements
- **IS_ADMIN Flag**: Added admin capabilities to user management
- **File Versioning**: Composite primary key system (INSTANCE_FILE_ID + VERSION)
- **Enhanced Decision Outcomes**: Added descriptions and next actions
- **REASON Field**: Replaced REJECTION_REASON with more flexible REASON field
- **Parent Task Tracking**: Added PARENT_TASK_IDS for task relationships

## 🏗️ Architecture

### Technology Stack
- **Java 17+** with Spring Boot 3.2.0
- **H2 Database** (Development) / **Oracle Database** (Production)
- **Spring Data JPA** with Hibernate
- **Hibernate Envers** for auditing
- **Quartz Scheduler** for workflow scheduling
- **OpenAPI/Swagger** for API documentation
- **Jakarta Validation** for request validation

### Project Structure
```
src/main/java/com/docwf/
├── config/           # Configuration classes (Security, Quartz, WebSocket)
├── controller/       # REST API controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities with composite keys
├── enums/           # Enumerations
├── exception/       # Custom exceptions
├── job/             # Scheduled jobs (Calendar, Workflow execution)
├── repository/      # Data access layer with custom queries
└── service/         # Business logic layer
```

## 🗄️ Database Schema

### Core Tables

#### User Management
- **WORKFLOW_USER**: System users with escalation hierarchy and admin flag
  - `IS_ADMIN CHAR(1) DEFAULT 'N'` - Admin privileges flag

#### Configuration Layer
- **WORKFLOW_CONFIG**: Workflow definitions with timing parameters
- **WORKFLOW_CONFIG_TASK**: Tasks within workflows (simplified, runtime data moved to instances)
- **WORKFLOW_CONFIG_TASK_FILE**: File configurations for tasks
- **TASK_DECISION_OUTCOME**: Decision outcomes and next task mapping

#### Execution Layer
- **WORKFLOW_INSTANCE**: Running workflow instances
- **WORKFLOW_INSTANCE_TASK**: Task execution instances with runtime data
  - `REASON VARCHAR2(500)` - Flexible reason/commentary field
  - `PARENT_TASK_IDS VARCHAR2(500)` - Parent task relationships
- **WORKFLOW_INSTANCE_TASK_FILE**: File versioning with composite primary key
  - `PRIMARY KEY (INSTANCE_FILE_ID, VERSION)`
  - `VERSION NUMBER NOT NULL DEFAULT 1`
  - Enhanced metadata fields for file management

#### Calendar System
- **WORKFLOW_CALENDAR**: Business calendars for workflow scheduling
- **WORKFLOW_CALENDAR_DAY**: Specific calendar days (holidays, run days)

### Audit Tables
All tables have corresponding audit tables with `_AUD` suffix for complete history tracking.

## 📚 API Documentation

### File Versioning APIs (New)
- `GET /api/files/versions/{instanceFileId}` - Get all versions of a file
- `GET /api/files/versions/{instanceFileId}/latest` - Get latest version
- `GET /api/files/versions/{instanceFileId}/{version}` - Get specific version
- `POST /api/files/versions/{instanceFileId}` - Create new version
- `DELETE /api/files/versions/{instanceFileId}/{version}` - Delete specific version
- `GET /api/files/task/{instanceTaskId}/latest` - Get latest files for task

### Core Endpoints

#### Users (Enhanced with Admin Support)
- `POST /api/users` - Create user (with isAdmin field)
- `GET /api/users` - Get all users with pagination
- `GET /api/users/{userId}` - Get user by ID
- `PUT /api/users/{userId}` - Update user
- `PATCH /api/users/{userId}/status` - Toggle user status

#### Workflows
- `POST /api/workflows` - Create workflow
- `GET /api/workflows` - Get all workflows
- `GET /api/workflows/{workflowId}` - Get workflow by ID
- `PUT /api/workflows/{workflowId}` - Update workflow
- `GET /api/workflows/{workflowId}/tasks` - Get workflow tasks

#### Calendar Management
- `POST /api/calendars` - Create calendar
- `GET /api/calendars` - Get all calendars
- `POST /api/calendars/{calendarId}/days` - Add calendar days
- `GET /api/calendars/{calendarId}/can-execute` - Validate execution date

#### Workflow Execution
- `POST /api/execution/workflows/{workflowId}/start` - Start workflow
- `POST /api/execution/workflows/{workflowId}/start-with-calendar` - Start with calendar validation
- `GET /api/execution/instances/{instanceId}/tasks` - Get instance tasks
- `POST /api/execution/tasks/{instanceTaskId}/complete` - Complete task

#### Dashboard APIs
- `GET /api/dashboard/user` - User dashboard
- `GET /api/dashboard/process-owner` - Process owner dashboard
- `GET /api/dashboard/manager` - Manager dashboard
- `GET /api/dashboard/admin` - Admin dashboard

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- H2 Database (included) or Oracle Database 19c+

### Quick Start

1. **Clone and Setup**
   ```bash
   git clone <repository-url>
   cd DocWF
   ```

2. **Run Development Environment**
   ```bash
   ./run-dev.sh
   ```

3. **Access the Application**
   - **API**: http://localhost:8080/api
   - **Swagger UI**: http://localhost:8080/swagger-ui/index.html
   - **H2 Console**: http://localhost:8080/h2-console

### Sample Data
The application comes with comprehensive sample data including:
- **7 Users** (Alice as admin, Bob, Carol, David, Eve, Frank, Grace)
- **4 Workflow Configurations** with various task types
- **Multiple Workflow Instances** with different statuses
- **File Versions** for testing the versioning system
- **Calendar Data** with holidays and run days

## 🔄 Workflow Lifecycle

1. **Configuration**: Define workflow with tasks, roles, and parameters
2. **Calendar Setup**: Configure business calendars for execution scheduling
3. **Instantiation**: Create workflow instance with calendar validation
4. **Execution**: Execute tasks in sequence order with file operations
5. **File Versioning**: Track all file changes with version history
6. **Decision Making**: Process decision outcomes and route to next tasks
7. **Completion**: Mark workflow as completed when all tasks are done

## 📊 File Versioning System

### Key Features
- **Composite Primary Key**: `(INSTANCE_FILE_ID, VERSION)`
- **Version History**: Complete audit trail of all file changes
- **Latest Version Access**: Easy retrieval of current file versions
- **File Metadata**: Enhanced tracking with descriptions, status, and commentary
- **Action Types**: UPLOAD, UPDATE, CONSOLIDATE operations

### Usage Examples
```bash
# Get all versions of a file
curl http://localhost:8080/api/files/versions/1

# Get latest version
curl http://localhost:8080/api/files/versions/1/latest

# Create new version
curl -X POST http://localhost:8080/api/files/versions/1 \
  -F "file=@updated_document.pdf" \
  -F "actionType=UPDATE" \
  -F "createdBy=alice"
```

## 📅 Calendar Integration

### Business Calendar Features
- **Holiday Management**: Define non-working days
- **Run Day Scheduling**: Specify when workflows can execute
- **Date Validation**: Check if workflows can run on specific dates
- **Next Valid Date**: Find next available execution date

### Calendar Types
- **YEARLY**: Annual calendars with recurring holidays
- **MONTHLY**: Monthly processing calendars
- **CUSTOM**: Project-specific calendars

## 🔐 Security & Access Control

### Admin Capabilities
- **IS_ADMIN Flag**: Special privileges for administrative users
- **Role-Based Access**: Users assigned to roles within workflows
- **Escalation Hierarchy**: Tasks can be escalated to higher-level users
- **Audit Logging**: All changes tracked with user and timestamp

### User Roles
- **Preparer**: Initial data preparation and file uploads
- **Reviewer**: Data review and validation
- **Approver**: Final approval and decision making
- **Auditor**: Audit and compliance checking

## 🧪 Testing

### Available Test Suites
- **Insomnia Collection**: `DocWF_Insomnia_Test_Suite.json`
- **Sample Requests**: `sample_requests.json` (updated with latest endpoints)
- **Unit Tests**: Comprehensive test coverage for services and controllers

### Test Endpoints
```bash
# Test all endpoints
./test_all_endpoints.sh

# Test calendar integration
./test_calendar_integration.sh
```

## 🚀 Deployment

### Development
```bash
./run-dev.sh
```

### Production
```bash
./run-prod.sh
```

### Configuration Files
- `application-dev.yml` - Development configuration
- `application-prod.yml` - Production configuration
- `application.yml` - Base configuration

## 📈 Monitoring & Health

- **Health Check**: Application status monitoring
- **Audit Logs**: Complete change history in database
- **Application Logs**: Configurable logging levels
- **Performance Metrics**: Workflow execution statistics

## 🔧 Customization

### Adding New Task Types
1. Extend the `TaskType` enum
2. Add corresponding logic in services
3. Update controllers and DTOs

### Custom Workflow Parameters
1. Use the `WORKFLOW_CONFIG_PARAM` table
2. Add parameter validation in services
3. Implement parameter-specific logic

### File Versioning Extensions
1. Extend `WorkflowInstanceTaskFile` entity
2. Add custom metadata fields
3. Implement version-specific business logic

## 📋 Sample API Usage

### Create Admin User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@company.com",
    "isActive": "Y",
    "isAdmin": "Y",
    "createdBy": "system"
  }'
```

### Start Workflow with Calendar
```bash
curl -X POST "http://localhost:8080/api/execution/workflows/1/start-with-calendar?startedByUserId=1&calendarId=1"
```

### Upload File with Versioning
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@document.pdf" \
  -F "instanceTaskId=1" \
  -F "actionType=UPLOAD" \
  -F "createdBy=alice"
```

## 🔮 Future Enhancements

- **Real-time Notifications**: WebSocket support for live updates
- **Advanced File Processing**: OCR, PDF processing, image analysis
- **Workflow Templates**: Pre-built workflow configurations
- **Mobile Support**: Mobile-optimized interfaces
- **Integration APIs**: Connect with external systems
- **Advanced Analytics**: Workflow performance metrics and insights
- **Multi-tenant Support**: Organization-level isolation
- **Advanced Scheduling**: Complex scheduling rules and dependencies

## 🆘 Support & Documentation

### Resources
- **API Documentation**: http://localhost:8080/swagger-ui/index.html
- **Sample Requests**: `sample_requests.json`
- **Test Collections**: `DocWF_Insomnia_Test_Suite.json`
- **Database Schema**: `src/main/resources/db/schema.sql`
- **Sample Data**: `src/main/resources/db/data.sql`

### Getting Help
- Check the Swagger UI for interactive API documentation
- Review sample requests for usage examples
- Examine the test suites for integration patterns
- Consult the database schema for data relationships

---

**DocWF** - Empowering organizations with intelligent document workflow management and advanced file versioning capabilities.
