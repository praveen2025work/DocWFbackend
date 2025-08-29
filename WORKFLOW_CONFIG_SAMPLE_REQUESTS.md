# Workflow Configuration API - Sample Requests & Responses

This document provides comprehensive examples of request and response formats for all workflow configuration endpoints.

## Table of Contents
1. [Create Workflow](#create-workflow)
2. [Get Workflow by ID](#get-workflow-by-id)
3. [Get All Workflows](#get-all-workflows)
4. [Update Workflow](#update-workflow)
5. [Toggle Workflow Status](#toggle-workflow-status)
6. [Add Task to Workflow](#add-task-to-workflow)
7. [Update Task](#update-task)
8. [Add Role to Workflow](#add-role-to-workflow)
9. [Search Workflows](#search-workflows)
10. [Delete Workflow](#delete-workflow)

---

## 1. Create Workflow

### Endpoint
```
POST /api/workflows
```

### Request Body
```json
{
  "name": "Monthly Finance Review Workflow",
  "description": "Automated monthly finance review and approval process",
  "reminderBeforeDueMins": 60,
  "minutesAfterDue": 30,
  "escalationAfterMins": 120,
  "dueInMins": 1440,
  "isActive": "Y",
  "calendarId": 1,
  "createdBy": "admin@company.com",
  "tasks": [
    {
      "name": "Data Collection",
      "taskType": "FILE_UPLOAD",
      "roleId": 1,
      "expectedCompletion": 120,
      "sequenceOrder": 1,
      "escalationRules": "Escalate to manager after 2 hours",
      "canBeRevisited": "Y",
      "maxRevisits": 3,
      "fileSelectionMode": "USER_SELECT",
      "taskDescription": "Upload monthly financial data files",
      "taskPriority": "HIGH",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "Y",
      "fileRetentionDays": 90,
      "keepFileVersions": "Y",
      "maxFileVersions": 5
    },
    {
      "name": "Data Validation",
      "taskType": "FILE_UPDATE",
      "roleId": 2,
      "expectedCompletion": 180,
      "sequenceOrder": 2,
      "escalationRules": "Escalate to senior analyst after 3 hours",
      "canBeRevisited": "Y",
      "maxRevisits": 2,
      "fileSelectionMode": "ALL_FILES",
      "taskDescription": "Validate uploaded financial data",
      "taskPriority": "HIGH",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "N",
      "fileSourceTaskIds": "1",
      "fileRetentionDays": 90
    },
    {
      "name": "Manager Review",
      "taskType": "DECISION",
      "roleId": 3,
      "expectedCompletion": 240,
      "sequenceOrder": 3,
      "escalationRules": "Escalate to director after 4 hours",
      "canBeRevisited": "N",
      "maxRevisits": 0,
      "taskDescription": "Review and approve financial data",
      "isDecisionTask": "Y",
      "decisionType": "APPROVAL",
      "taskPriority": "CRITICAL",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "decisionOutcomes": [
        {
          "outcomeName": "APPROVE",
          "nextTaskId": 4
        },
        {
          "outcomeName": "REJECT",
          "nextTaskId": 1
        }
      ]
    },
    {
      "name": "Final Approval",
      "taskType": "CONSOLIDATE_FILE",
      "roleId": 4,
      "expectedCompletion": 120,
      "sequenceOrder": 4,
      "escalationRules": "Escalate to CFO after 2 hours",
      "canBeRevisited": "N",
      "maxRevisits": 0,
      "fileSelectionMode": "AUTO_SELECT",
      "consolidationRulesJson": "{\"includeAllApproved\": true, \"excludeRejected\": true}",
      "taskDescription": "Consolidate approved financial data",
      "taskPriority": "CRITICAL",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "N",
      "fileSourceTaskIds": "1,2,3",
      "fileRetentionDays": 365
    }
  ],
  "workflowRoles": [
    {
      "roleId": 1,
      "userId": 101,
      "isActive": "Y"
    },
    {
      "roleId": 2,
      "userId": 102,
      "isActive": "Y"
    },
    {
      "roleId": 3,
      "userId": 103,
      "isActive": "Y"
    },
    {
      "roleId": 4,
      "userId": 104,
      "isActive": "Y"
    }
  ],
  "parameters": [
    {
      "paramKey": "REVIEW_PERIOD",
      "paramValue": "MONTHLY"
    },
    {
      "paramKey": "DEPARTMENT",
      "paramValue": "FINANCE"
    },
    {
      "paramKey": "REQUIRES_AUDIT",
      "paramValue": "true"
    }
  ]
}
```

### Response (201 Created)
```json
{
  "workflowId": 5,
  "name": "Monthly Finance Review Workflow",
  "description": "Automated monthly finance review and approval process",
  "reminderBeforeDueMins": 60,
  "minutesAfterDue": 30,
  "escalationAfterMins": 120,
  "dueInMins": 1440,
  "isActive": "Y",
  "calendarId": 1,
  "createdBy": "admin@company.com",
  "createdOn": "2025-08-29T11:15:30.123456",
  "updatedBy": null,
  "updatedOn": null,
  "workflowRoles": [
    {
      "id": 15,
      "workflowId": 5,
      "roleId": 1,
      "userId": 101,
      "isActive": "Y",
      "roleName": "Data Analyst",
      "userName": "John Smith"
    },
    {
      "id": 16,
      "workflowId": 5,
      "roleId": 2,
      "userId": 102,
      "isActive": "Y",
      "roleName": "Senior Analyst",
      "userName": "Jane Doe"
    },
    {
      "id": 17,
      "workflowId": 5,
      "roleId": 3,
      "userId": 103,
      "isActive": "Y",
      "roleName": "Manager",
      "userName": "Bob Johnson"
    },
    {
      "id": 18,
      "workflowId": 5,
      "roleId": 4,
      "userId": 104,
      "isActive": "Y",
      "roleName": "Director",
      "userName": "Alice Brown"
    }
  ],
  "tasks": [
    {
      "taskId": 21,
      "workflowId": 5,
      "name": "Data Collection",
      "taskType": "FILE_UPLOAD",
      "roleId": 1,
      "expectedCompletion": 120,
      "sequenceOrder": 1,
      "escalationRules": "Escalate to manager after 2 hours",
      "canBeRevisited": "Y",
      "maxRevisits": 3,
      "fileSelectionMode": "USER_SELECT",
      "taskDescription": "Upload monthly financial data files",
      "taskPriority": "HIGH",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "Y",
      "fileRetentionDays": 90,
      "keepFileVersions": "Y",
      "maxFileVersions": 5,
      "roleName": "Data Analyst",
      "completed": false,
      "decisionTask": false,
      "pending": true,
      "inProgress": false,
      "rejected": false
    },
    {
      "taskId": 22,
      "workflowId": 5,
      "name": "Data Validation",
      "taskType": "FILE_UPDATE",
      "roleId": 2,
      "expectedCompletion": 180,
      "sequenceOrder": 2,
      "escalationRules": "Escalate to senior analyst after 3 hours",
      "canBeRevisited": "Y",
      "maxRevisits": 2,
      "fileSelectionMode": "ALL_FILES",
      "taskDescription": "Validate uploaded financial data",
      "taskPriority": "HIGH",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "N",
      "fileSourceTaskIds": "1",
      "fileRetentionDays": 90,
      "roleName": "Senior Analyst",
      "completed": false,
      "decisionTask": false,
      "pending": true,
      "inProgress": false,
      "rejected": false
    },
    {
      "taskId": 23,
      "workflowId": 5,
      "name": "Manager Review",
      "taskType": "DECISION",
      "roleId": 3,
      "expectedCompletion": 240,
      "sequenceOrder": 3,
      "escalationRules": "Escalate to director after 4 hours",
      "canBeRevisited": "N",
      "maxRevisits": 0,
      "taskDescription": "Review and approve financial data",
      "isDecisionTask": "Y",
      "decisionType": "APPROVAL",
      "taskPriority": "CRITICAL",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "roleName": "Manager",
      "completed": false,
      "decisionTask": true,
      "pending": true,
      "inProgress": false,
      "rejected": false,
      "decisionOutcomes": [
        {
          "outcomeId": 31,
          "taskId": 23,
          "outcomeName": "APPROVE",
          "nextTaskId": 24,
          "nextTaskName": "Final Approval"
        },
        {
          "outcomeId": 32,
          "taskId": 23,
          "outcomeName": "REJECT",
          "nextTaskId": 21,
          "nextTaskName": "Data Collection"
        }
      ]
    },
    {
      "taskId": 24,
      "workflowId": 5,
      "name": "Final Approval",
      "taskType": "CONSOLIDATE_FILE",
      "roleId": 4,
      "expectedCompletion": 120,
      "sequenceOrder": 4,
      "escalationRules": "Escalate to CFO after 2 hours",
      "canBeRevisited": "N",
      "maxRevisits": 0,
      "fileSelectionMode": "AUTO_SELECT",
      "consolidationRulesJson": "{\"includeAllApproved\": true, \"excludeRejected\": true}",
      "taskDescription": "Consolidate approved financial data",
      "taskPriority": "CRITICAL",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "N",
      "fileSourceTaskIds": "1,2,3",
      "fileRetentionDays": 365,
      "roleName": "Director",
      "completed": false,
      "decisionTask": false,
      "pending": true,
      "inProgress": false,
      "rejected": false
    }
  ],
  "parameters": [
    {
      "paramId": 41,
      "workflowId": 5,
      "paramKey": "REVIEW_PERIOD",
      "paramValue": "MONTHLY",
      "createdBy": "admin@company.com",
      "createdOn": "2025-08-29T11:15:30.123456"
    },
    {
      "paramId": 42,
      "workflowId": 5,
      "paramKey": "DEPARTMENT",
      "paramValue": "FINANCE",
      "createdBy": "admin@company.com",
      "createdOn": "2025-08-29T11:15:30.123456"
    },
    {
      "paramId": 43,
      "workflowId": 5,
      "paramKey": "REQUIRES_AUDIT",
      "paramValue": "true",
      "createdBy": "admin@company.com",
      "createdOn": "2025-08-29T11:15:30.123456"
    }
  ]
}
```

---

## 2. Get Workflow by ID

### Endpoint
```
GET /api/workflows/{workflowId}
```

### Request
```
GET /api/workflows/5
```

### Response (200 OK)
```json
{
  "workflowId": 5,
  "name": "Monthly Finance Review Workflow",
  "description": "Automated monthly finance review and approval process",
  "reminderBeforeDueMins": 60,
  "minutesAfterDue": 30,
  "escalationAfterMins": 120,
  "dueInMins": 1440,
  "isActive": "Y",
  "calendarId": 1,
  "createdBy": "admin@company.com",
  "createdOn": "2025-08-29T11:15:30.123456",
  "updatedBy": null,
  "updatedOn": null,
  "workflowRoles": [
    {
      "id": 15,
      "workflowId": 5,
      "roleId": 1,
      "userId": 101,
      "isActive": "Y",
      "roleName": "Data Analyst",
      "userName": "John Smith"
    }
  ],
  "tasks": [
    {
      "taskId": 21,
      "workflowId": 5,
      "name": "Data Collection",
      "taskType": "FILE_UPLOAD",
      "roleId": 1,
      "expectedCompletion": 120,
      "sequenceOrder": 1,
      "escalationRules": "Escalate to manager after 2 hours",
      "canBeRevisited": "Y",
      "maxRevisits": 3,
      "fileSelectionMode": "USER_SELECT",
      "taskDescription": "Upload monthly financial data files",
      "taskPriority": "HIGH",
      "autoEscalationEnabled": "Y",
      "notificationRequired": "Y",
      "allowNewFiles": "Y",
      "fileRetentionDays": 90,
      "keepFileVersions": "Y",
      "maxFileVersions": 5,
      "roleName": "Data Analyst",
      "completed": false,
      "decisionTask": false,
      "pending": true,
      "inProgress": false,
      "rejected": false
    }
  ],
  "parameters": [
    {
      "paramId": 41,
      "workflowId": 5,
      "paramKey": "REVIEW_PERIOD",
      "paramValue": "MONTHLY",
      "createdBy": "admin@company.com",
      "createdOn": "2025-08-29T11:15:30.123456"
    }
  ]
}
```

---

## 3. Get All Workflows

### Endpoint
```
GET /api/workflows?pageable=0,20
```

### Request
```
GET /api/workflows?pageable=0,20
```

### Response (200 OK)
```json
{
  "content": [
    {
      "workflowId": 1,
      "name": "Monthly Finance Workflow",
      "description": "Finance reporting workflow",
      "reminderBeforeDueMins": 15,
      "minutesAfterDue": 10,
      "escalationAfterMins": 30,
      "dueInMins": 120,
      "isActive": "Y",
      "calendarId": null,
      "createdBy": "system",
      "createdOn": "2025-08-29T10:04:04.840121",
      "updatedBy": null,
      "updatedOn": null,
      "workflowRoles": null,
      "tasks": null,
      "parameters": null
    },
    {
      "workflowId": 2,
      "name": "Quarterly Audit Workflow",
      "description": "Audit workflow for quarterly reports",
      "reminderBeforeDueMins": 20,
      "minutesAfterDue": 15,
      "escalationAfterMins": 60,
      "dueInMins": 240,
      "isActive": "Y",
      "calendarId": null,
      "createdBy": "system",
      "createdOn": "2025-08-29T10:04:04.840603",
      "updatedBy": null,
      "updatedOn": null,
      "workflowRoles": null,
      "tasks": null,
      "parameters": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 20,
  "number": 0,
  "numberOfElements": 2,
  "first": true,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "empty": false
}
```

---

## 4. Update Workflow

### Endpoint
```
PUT /api/workflows/{workflowId}
```

### Request
```
PUT /api/workflows/5
```

### Request Body
```json
{
  "name": "Monthly Finance Review Workflow - Updated",
  "description": "Enhanced monthly finance review and approval process with additional validation",
  "reminderBeforeDueMins": 90,
  "minutesAfterDue": 45,
  "escalationAfterMins": 180,
  "dueInMins": 1440,
  "isActive": "Y",
  "calendarId": 1,
  "updatedBy": "admin@company.com"
}
```

### Response (200 OK)
```json
{
  "workflowId": 5,
  "name": "Monthly Finance Review Workflow - Updated",
  "description": "Enhanced monthly finance review and approval process with additional validation",
  "reminderBeforeDueMins": 90,
  "minutesAfterDue": 45,
  "escalationAfterMins": 180,
  "dueInMins": 1440,
  "isActive": "Y",
  "calendarId": 1,
  "createdBy": "admin@company.com",
  "createdOn": "2025-08-29T11:15:30.123456",
  "updatedBy": "admin@company.com",
  "updatedOn": "2025-08-29T11:20:15.654321",
  "workflowRoles": null,
  "tasks": null,
  "parameters": null
}
```

---

## 5. Toggle Workflow Status

### Endpoint
```
PATCH /api/workflows/{workflowId}/status?isActive={status}
```

### Request
```
PATCH /api/workflows/5/status?isActive=N
```

### Response (200 OK)
```json
{
  "workflowId": 5,
  "name": "Monthly Finance Review Workflow - Updated",
  "description": "Enhanced monthly finance review and approval process with additional validation",
  "reminderBeforeDueMins": 90,
  "minutesAfterDue": 45,
  "escalationAfterMins": 180,
  "dueInMins": 1440,
  "isActive": "N",
  "calendarId": 1,
  "createdBy": "admin@company.com",
  "createdOn": "2025-08-29T11:15:30.123456",
  "updatedBy": "admin@company.com",
  "updatedOn": "2025-08-29T11:25:30.987654",
  "workflowRoles": null,
  "tasks": null,
  "parameters": null
}
```

---

## 6. Add Task to Workflow

### Endpoint
```
POST /api/workflows/{workflowId}/tasks
```

### Request
```
POST /api/workflows/5/tasks
```

### Request Body
```json
{
  "name": "Quality Assurance Review",
  "taskType": "FILE_UPDATE",
  "roleId": 5,
  "expectedCompletion": 120,
  "sequenceOrder": 5,
  "escalationRules": "Escalate to quality manager after 2 hours",
  "canBeRevisited": "Y",
  "maxRevisits": 2,
  "fileSelectionMode": "ALL_FILES",
  "taskDescription": "Final quality assurance review of consolidated data",
  "taskPriority": "HIGH",
  "autoEscalationEnabled": "Y",
  "notificationRequired": "Y",
  "allowNewFiles": "N",
  "fileSourceTaskIds": "1,2,3,4",
  "fileRetentionDays": 365
}
```

### Response (201 Created)
```json
{
  "taskId": 25,
  "workflowId": 5,
  "name": "Quality Assurance Review",
  "taskType": "FILE_UPDATE",
  "roleId": 5,
  "expectedCompletion": 120,
  "sequenceOrder": 5,
  "escalationRules": "Escalate to quality manager after 2 hours",
  "canBeRevisited": "Y",
  "maxRevisits": 2,
  "fileSelectionMode": "ALL_FILES",
  "taskDescription": "Final quality assurance review of consolidated data",
  "taskPriority": "HIGH",
  "autoEscalationEnabled": "Y",
  "notificationRequired": "Y",
  "allowNewFiles": "N",
  "fileSourceTaskIds": "1,2,3,4",
  "fileRetentionDays": 365,
  "roleName": "Quality Manager",
  "completed": false,
  "decisionTask": false,
  "pending": true,
  "inProgress": false,
  "rejected": false
}
```

---

## 7. Update Task

### Endpoint
```
PUT /api/workflows/{workflowId}/tasks/{taskId}
```

### Request
```
PUT /api/workflows/5/tasks/25
```

### Request Body
```json
{
  "name": "Quality Assurance Review - Enhanced",
  "expectedCompletion": 180,
  "escalationRules": "Escalate to quality manager after 3 hours",
  "maxRevisits": 3,
  "taskDescription": "Enhanced final quality assurance review with additional checks",
  "taskPriority": "CRITICAL"
}
```

### Response (200 OK)
```json
{
  "taskId": 25,
  "workflowId": 5,
  "name": "Quality Assurance Review - Enhanced",
  "taskType": "FILE_UPDATE",
  "roleId": 5,
  "expectedCompletion": 180,
  "sequenceOrder": 5,
  "escalationRules": "Escalate to quality manager after 3 hours",
  "canBeRevisited": "Y",
  "maxRevisits": 3,
  "fileSelectionMode": "ALL_FILES",
  "taskDescription": "Enhanced final quality assurance review with additional checks",
  "taskPriority": "CRITICAL",
  "autoEscalationEnabled": "Y",
  "notificationRequired": "Y",
  "allowNewFiles": "N",
  "fileSourceTaskIds": "1,2,3,4",
  "fileRetentionDays": 365,
  "roleName": "Quality Manager",
  "completed": false,
  "decisionTask": false,
  "pending": true,
  "inProgress": false,
  "rejected": false
}
```

---

## 8. Add Role to Workflow

### Endpoint
```
POST /api/workflows/{workflowId}/roles
```

### Request
```
POST /api/workflows/5/roles
```

### Request Body
```json
{
  "roleId": 6,
  "userId": 105,
  "isActive": "Y"
}
```

### Response (201 Created)
```json
{
  "id": 19,
  "workflowId": 5,
  "roleId": 6,
  "userId": 105,
  "isActive": "Y",
  "roleName": "Auditor",
  "userName": "Charlie Wilson"
}
```

---

## 9. Search Workflows

### Endpoint
```
GET /api/workflows/search?name=Finance&isActive=Y&pageable=0,10
```

### Request
```
GET /api/workflows/search?name=Finance&isActive=Y&pageable=0,10
```

### Response (200 OK)
```json
{
  "content": [
    {
      "workflowId": 1,
      "name": "Monthly Finance Workflow",
      "description": "Finance reporting workflow",
      "reminderBeforeDueMins": 15,
      "minutesAfterDue": 10,
      "escalationAfterMins": 30,
      "dueInMins": 120,
      "isActive": "Y",
      "calendarId": null,
      "createdBy": "system",
      "createdOn": "2025-08-29T10:04:04.840121",
      "updatedBy": null,
      "updatedOn": null,
      "workflowRoles": null,
      "tasks": null,
      "parameters": null
    },
    {
      "workflowId": 5,
      "name": "Monthly Finance Review Workflow - Updated",
      "description": "Enhanced monthly finance review and approval process with additional validation",
      "reminderBeforeDueMins": 90,
      "minutesAfterDue": 45,
      "escalationAfterMins": 180,
      "dueInMins": 1440,
      "isActive": "Y",
      "calendarId": 1,
      "createdBy": "admin@company.com",
      "createdOn": "2025-08-29T11:15:30.123456",
      "updatedBy": "admin@company.com",
      "updatedOn": "2025-08-29T11:25:30.987654",
      "workflowRoles": null,
      "tasks": null,
      "parameters": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 10,
  "number": 0,
  "numberOfElements": 2,
  "first": true,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "empty": false
}
```

---

## 10. Delete Workflow

### Endpoint
```
DELETE /api/workflows/{workflowId}
```

### Request
```
DELETE /api/workflows/5
```

### Response (200 OK)
```json
{
  "message": "Workflow deleted successfully",
  "workflowId": 5,
  "deletedAt": "2025-08-29T11:30:00.000000"
}
```

---

## Error Response Examples

### 400 Bad Request - Validation Error
```json
{
  "timestamp": "2025-08-29T11:15:30.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "Workflow name is required"
    },
    {
      "field": "isActive",
      "message": "Active status is required"
    }
  ]
}
```

### 404 Not Found - Workflow Not Found
```json
{
  "timestamp": "2025-08-29T11:15:30.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Workflow with ID 999 not found",
  "path": "/api/workflows/999"
}
```

### 409 Conflict - Duplicate Workflow Name
```json
{
  "timestamp": "2025-08-29T11:15:30.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Workflow with name 'Monthly Finance Review Workflow' already exists",
  "path": "/api/workflows"
}
```

---

## Task Type Enum Values

### Available Task Types
- `FILE_UPLOAD` - Users can upload files
- `FILE_UPDATE` - Users can modify existing files
- `CONSOLIDATE_FILE` - System consolidates multiple files
- `DECISION` - Users make decisions that route the workflow

### Task Priority Levels
- `LOW` - Low priority tasks
- `MEDIUM` - Medium priority tasks
- `HIGH` - High priority tasks
- `CRITICAL` - Critical priority tasks

### File Selection Modes
- `USER_SELECT` - Users manually select files
- `ALL_FILES` - All files from source tasks are available
- `AUTO_SELECT` - System automatically selects files based on rules

---

## Notes

1. **Timing Fields**: All timing fields are in minutes
2. **Status Fields**: Use "Y" for Yes and "N" for No
3. **Sequence Order**: Tasks are executed in ascending sequence order
4. **File Retention**: File retention days determine how long files are kept after task completion
5. **Escalation**: Escalation rules are free-text descriptions of escalation logic
6. **Decision Outcomes**: Only required for decision tasks, define the routing logic
7. **Parallel Execution**: Tasks can run in parallel if `canRunInParallel` is "Y"
8. **File Versions**: File versioning can be enabled/disabled per task
