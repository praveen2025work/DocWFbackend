# DocWF API Endpoints Reference

## 🚀 Quick Start
- **Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **H2 Console**: `http://localhost:8080/h2-console`

---

## 👥 User Management Controller (`/api/users`)

### Create User
```bash
POST /api/users
Content-Type: application/json

{
  "username": "john.doe",
  "email": "john.doe@company.com",
  "firstName": "John",
  "lastName": "Doe",
  "isActive": "Y",
  "createdBy": "admin"
}
```

### Get User by ID
```bash
GET /api/users/1
```

### Get User by Username
```bash
GET /api/users/username/john.doe
```

### Get User by Email
```bash
GET /api/users/email/john.doe@company.com
```

### Get All Users
```bash
GET /api/users
```

### Get Active Users Only
```bash
GET /api/users?isActive=Y
```

### Update User
```bash
PUT /api/users/1
Content-Type: application/json

{
  "username": "john.doe.updated",
  "email": "john.updated@company.com",
  "firstName": "John",
  "lastName": "Doe Updated",
  "isActive": "Y",
  "updatedBy": "admin"
}
```

### Delete User
```bash
DELETE /api/users/1
```

### Get Users by Role
```bash
GET /api/users/role/ADMIN
```

### Get Users by Workflow
```bash
GET /api/users/workflow/1
```

### Check Username Availability
```bash
GET /api/users/check/username/john.doe
```

### Check Email Availability
```bash
GET /api/users/check/email/john.doe@company.com
```

### Toggle User Status
```bash
PATCH /api/users/1/status?isActive=N
```

### Set User Escalation
```bash
PATCH /api/users/1/escalation?escalationToUserId=2
```

---

## 🔐 Role Management Controller (`/api/roles`)

### Create Role
```bash
POST /api/roles
Content-Type: application/json

{
  "roleName": "REVIEWER",
  "isActive": "Y",
  "createdBy": "admin"
}
```

### Get Role by ID
```bash
GET /api/roles/1
```

### Get Role by Name
```bash
GET /api/roles/name/ADMIN
```

### Get All Roles
```bash
GET /api/roles
```

### Get Active Roles Only
```bash
GET /api/roles?isActive=Y
```

### Update Role
```bash
PUT /api/roles/1
Content-Type: application/json

{
  "roleName": "SENIOR_ADMIN",
  "isActive": "Y",
  "updatedBy": "admin"
}
```

### Delete Role
```bash
DELETE /api/roles/1
```

### Get Roles by Workflow
```bash
GET /api/roles/workflow/1
```

### Get Roles by User
```bash
GET /api/roles/user/1
```

### Check Role Name Availability
```bash
GET /api/roles/check/name/REVIEWER
```

### Toggle Role Status
```bash
PATCH /api/roles/1/status?isActive=N
```

### Assign Role to User
```bash
POST /api/roles/1/assign/user/2
```

### Unassign Role from User
```bash
DELETE /api/roles/1/unassign/user/2
```

---

## 📅 Calendar Management Controller (`/api/calendar`)

### Get User Calendar
```bash
GET /api/calendar/user/1?startDate=2025-08-20T00:00:00&endDate=2025-08-21T00:00:00
```

### Create Calendar Event
```bash
POST /api/calendar/event
Content-Type: application/json

{
  "eventType": "MEETING",
  "title": "Team Standup",
  "description": "Daily team synchronization",
  "startTime": "2025-08-20T09:00:00",
  "endTime": "2025-08-20T09:30:00",
  "priority": "HIGH",
  "location": "Conference Room A"
}
```

### Get Event by ID
```bash
GET /api/calendar/event/1
```

### Update Event
```bash
PUT /api/calendar/event/1
Content-Type: application/json

{
  "eventType": "MEETING",
  "title": "Team Standup Updated",
  "startTime": "2025-08-20T09:00:00",
  "endTime": "2025-08-20T09:30:00",
  "priority": "MEDIUM"
}
```

### Delete Event
```bash
DELETE /api/calendar/event/1
```

### Get User Events
```bash
GET /api/calendar/user/1/events
GET /api/calendar/user/1/events?eventType=MEETING
GET /api/calendar/user/1/events?startDate=2025-08-20T00:00:00&endDate=2025-08-21T00:00:00
```

### Get User Tasks
```bash
GET /api/calendar/user/1/tasks
GET /api/calendar/user/1/tasks?status=PENDING
GET /api/calendar/user/1/tasks?priority=HIGH
```

### Get User Reminders
```bash
GET /api/calendar/user/1/reminders
GET /api/calendar/user/1/reminders?type=TASK_DUE
```

### Create Reminder
```bash
POST /api/calendar/reminder
Content-Type: application/json

{
  "title": "Document Review Due",
  "message": "Please review the Q3 report",
  "reminderTime": "2025-08-20T16:00:00",
  "type": "TASK_DUE",
  "priority": "HIGH"
}
```

### Update Reminder
```bash
PUT /api/calendar/reminder/1
Content-Type: application/json

{
  "title": "Document Review Due Updated",
  "message": "Please review the Q3 report - URGENT",
  "reminderTime": "2025-08-20T15:00:00",
  "priority": "URGENT"
}
```

### Delete Reminder
```bash
DELETE /api/calendar/reminder/1
```

### Get User Availability
```bash
GET /api/calendar/user/1/availability
```

### Update User Availability
```bash
PUT /api/calendar/user/1/availability
Content-Type: application/json

{
  "startDate": "2025-08-20",
  "endDate": "2025-08-27",
  "availableHours": 40,
  "scheduledHours": 20
}
```

### Check for Conflicts
```bash
GET /api/calendar/conflicts/user/1?startTime=2025-08-20T10:00:00&endTime=2025-08-20T11:00:00
```

### Get Free Time Slots
```bash
GET /api/calendar/free-slots/user/1?date=2025-08-20T00:00:00&durationMinutes=60
```

---

## 🔄 Workflow Configuration Controller (`/api/workflows`)

### Create Workflow
```bash
POST /api/workflows
Content-Type: application/json

{
  "name": "Document Approval Process",
  "description": "Standard document approval workflow",
  "isActive": "Y",
  "createdBy": "admin"
}
```

### Get Workflow by ID
```bash
GET /api/workflows/1
```

### Get Workflow by Name
```bash
GET /api/workflows/name/Document%20Approval%20Process
```

### Get All Workflows
```bash
GET /api/workflows
GET /api/workflows?isActive=Y
```

### Update Workflow
```bash
PUT /api/workflows/1
Content-Type: application/json

{
  "name": "Document Approval Process Updated",
  "description": "Enhanced document approval workflow",
  "isActive": "Y",
  "updatedBy": "admin"
}
```

### Delete Workflow
```bash
DELETE /api/workflows/1
```

### Toggle Workflow Status
```bash
PATCH /api/workflows/1/status?isActive=N
```

### Add Role to Workflow
```bash
POST /api/workflows/1/roles
Content-Type: application/json

{
  "roleId": 1,
  "isActive": "Y",
  "createdBy": "admin"
}
```

### Get Workflow Roles
```bash
GET /api/workflows/1/roles
```

### Update Workflow Role
```bash
PUT /api/workflows/1/roles/1
Content-Type: application/json

{
  "roleId": 1,
  "isActive": "Y",
  "updatedBy": "admin"
}
```

### Remove Role from Workflow
```bash
DELETE /api/workflows/1/roles/1
```

### Create Task in Workflow
```bash
POST /api/workflows/1/tasks
Content-Type: application/json

{
  "name": "Initial Review",
  "sequenceOrder": 1,
  "isActive": "Y",
  "createdBy": "admin"
}
```

### Get Workflow Tasks
```bash
GET /api/workflows/1/tasks
```

### Update Workflow Task
```bash
PUT /api/workflows/1/tasks/1
Content-Type: application/json

{
  "name": "Initial Review Updated",
  "sequenceOrder": 1,
  "isActive": "Y",
  "updatedBy": "admin"
}
```

### Delete Workflow Task
```bash
DELETE /api/workflows/1/tasks/1
```

### Reorder Tasks
```bash
POST /api/workflows/1/tasks/reorder
Content-Type: application/json

[1, 2, 3]
```

### Add Parameter to Workflow
```bash
POST /api/workflows/1/parameters
Content-Type: application/json

{
  "paramKey": "documentType",
  "paramValue": "Policy",
  "description": "Type of document",
  "isActive": "Y",
  "createdBy": "admin"
}
```

### Get Workflow Parameters
```bash
GET /api/workflows/1/parameters
```

### Update Workflow Parameter
```bash
PUT /api/workflows/1/parameters/1
Content-Type: application/json

{
  "paramKey": "documentType",
  "paramValue": "Policy Updated",
  "description": "Updated document type",
  "isActive": "Y",
  "updatedBy": "admin"
}
```

### Delete Workflow Parameter
```bash
DELETE /api/workflows/1/parameters/1
```

### Get Parameter Value
```bash
GET /api/workflows/1/parameters/documentType/value
```

### Get Workflows by User
```bash
GET /api/workflows/user/1
```

### Get Workflows by Role
```bash
GET /api/workflows/role/1
```

### Get Workflows Needing Reminders
```bash
GET /api/workflows/reminders
```

### Get Workflows Needing Escalations
```bash
GET /api/workflows/escalations
```

---

## ⚡ Workflow Execution Controller (`/api/execution`)

### Start Workflow Instance
```bash
POST /api/execution/workflows/1/start?startedByUserId=1
```

### Get Workflow Instance
```bash
GET /api/execution/instances/1
```

### Get Workflow Instances
```bash
GET /api/execution/workflows/1/instances
```

### Get Instances by Status
```bash
GET /api/execution/instances/status/PENDING
```

### Update Instance Status
```bash
PATCH /api/execution/instances/1/status?status=IN_PROGRESS
```

### Complete Workflow Instance
```bash
POST /api/execution/instances/1/complete
```

### Cancel Workflow Instance
```bash
POST /api/execution/instances/1/cancel
```

### Escalate Workflow Instance
```bash
POST /api/execution/instances/1/escalate?escalatedToUserId=2
```

### Get Instance Tasks
```bash
GET /api/execution/instances/1/tasks
```

### Start Task
```bash
POST /api/execution/tasks/1/start
```

### Complete Task
```bash
POST /api/execution/tasks/1/complete
```

### Assign Task to User
```bash
POST /api/execution/tasks/1/assign?userId=2
```

### Escalate Task
```bash
POST /api/execution/tasks/1/escalate?escalatedToUserId=2
```

### Get Next Pending Task
```bash
GET /api/execution/instances/1/next-task
```

### Get Tasks Assigned to User
```bash
GET /api/execution/users/1/tasks
```

### Get Pending Tasks for User
```bash
GET /api/execution/users/1/pending-tasks
```

### Record Decision Outcome
```bash
POST /api/execution/tasks/1/decisions?outcomeName=APPROVED&createdBy=admin
```

### Get Task Decision Outcomes
```bash
GET /api/execution/tasks/1/decisions
```

### Process Decision Outcome
```bash
POST /api/execution/tasks/1/process-decision?outcomeName=APPROVED
```

### Execute Next Task
```bash
POST /api/execution/instances/1/execute-next
```

### Check Workflow Completion
```bash
GET /api/execution/instances/1/complete
```

### Get Overdue Tasks
```bash
GET /api/execution/overdue-tasks
```

### Get Tasks Needing Attention
```bash
GET /api/execution/tasks-needing-attention
```

### Trigger Workflow Reminders
```bash
POST /api/execution/reminders/trigger
```

### Trigger Workflow Escalations
```bash
POST /api/execution/escalations/trigger
```

---

## 📁 File Management Controller (`/api/files`)

### Upload File
```bash
POST /api/files/upload
Content-Type: multipart/form-data

file: [binary file]
workflowId: 1
taskId: 1
uploadedBy: admin
```

### Get File by ID
```bash
GET /api/files/1
```

### Get Files by Workflow
```bash
GET /api/files/workflow/1
```

### Get Files by Task
```bash
GET /api/files/task/1
```

### Get Files by User
```bash
GET /api/files/user/1
```

### Update File Metadata
```bash
PUT /api/files/1
Content-Type: application/json

{
  "fileName": "Updated Document.pdf",
  "description": "Updated document description",
  "updatedBy": "admin"
}
```

### Delete File
```bash
DELETE /api/files/1
```

### Download File
```bash
GET /api/files/1/download
```

---

## 📊 Dashboard Controllers

### User Dashboard
```bash
GET /api/dashboard/user/1
```

### Manager Dashboard
```bash
GET /api/dashboard/manager/1
```

### Admin Dashboard
```bash
GET /api/dashboard/admin
```

### Process Owner Dashboard
```bash
GET /api/dashboard/process-owner/1
```

---

## 🔧 Testing with cURL

### Quick Test Commands
```bash
# Test if application is running
curl -s "http://localhost:8080/api/users" | head -5

# Create a user
curl -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@company.com","firstName":"Test","lastName":"User","isActive":"Y","createdBy":"admin"}'

# Create a role
curl -X POST "http://localhost:8080/api/roles" \
  -H "Content-Type: application/json" \
  -d '{"roleName":"TEST_ROLE","isActive":"Y","createdBy":"admin"}'

# Create a workflow
curl -X POST "http://localhost:8080/api/workflows" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Workflow","description":"Test workflow description","isActive":"Y","createdBy":"admin"}'
```

---

## 📝 Notes

- **Authentication**: Currently disabled in development mode
- **Validation**: All endpoints include input validation
- **Audit**: All changes are tracked with Hibernate Envers
- **Scheduling**: Quartz scheduler runs background jobs for reminders and escalations
- **WebSocket**: Real-time updates available for workflow progress

---

## 🚨 Common HTTP Status Codes

- **200**: Success
- **201**: Created
- **400**: Bad Request (validation error)
- **404**: Not Found
- **500**: Internal Server Error

---

## 🔍 Troubleshooting

1. **Check Application Status**: `curl http://localhost:8080/api/users`
2. **View H2 Database**: `http://localhost:8080/h2-console`
3. **Check Swagger Docs**: `http://localhost:8080/swagger-ui/index.html`
4. **Application Logs**: Check console output for detailed error messages
