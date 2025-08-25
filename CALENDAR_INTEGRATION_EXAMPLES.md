# 🌐 Calendar Integration & Workflow Instance Mapping

This document provides comprehensive examples of how to use the enhanced calendar functionality and workflow instance mapping in the DocWF system.

## 📅 Calendar Management

### 1. Create Calendar with Days in Same Call

**Endpoint:** `POST /api/calendar/with-days`

**Description:** Creates a calendar and all its calendar days in a single API call, ensuring data consistency.

**Request Body:**
```json
{
  "calendarName": "US Business Days 2025",
  "description": "Excludes US holidays and weekends",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "recurrence": "YEARLY",
  "createdBy": "admin@company.com",
  "calendarDays": [
    {
      "dayDate": "2025-01-01",
      "dayType": "HOLIDAY",
      "note": "New Year's Day"
    },
    {
      "dayDate": "2025-01-20",
      "dayType": "HOLIDAY",
      "note": "Martin Luther King Jr. Day"
    },
    {
      "dayDate": "2025-02-17",
      "dayType": "HOLIDAY",
      "note": "Presidents' Day"
    },
    {
      "dayDate": "2025-05-26",
      "dayType": "HOLIDAY",
      "note": "Memorial Day"
    },
    {
      "dayDate": "2025-07-04",
      "dayType": "HOLIDAY",
      "note": "Independence Day"
    },
    {
      "dayDate": "2025-09-01",
      "dayType": "HOLIDAY",
      "note": "Labor Day"
    },
    {
      "dayDate": "2025-10-13",
      "dayType": "HOLIDAY",
      "note": "Columbus Day"
    },
    {
      "dayDate": "2025-11-11",
      "dayType": "HOLIDAY",
      "note": "Veterans Day"
    },
    {
      "dayDate": "2025-11-27",
      "dayType": "HOLIDAY",
      "note": "Thanksgiving Day"
    },
    {
      "dayDate": "2025-12-25",
      "dayType": "HOLIDAY",
      "note": "Christmas Day"
    }
  ]
}
```

**Response:**
```json
{
  "calendarId": 1,
  "calendarName": "US Business Days 2025",
  "description": "Excludes US holidays and weekends",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "recurrence": "YEARLY",
  "createdBy": "admin@company.com",
  "createdAt": "2024-12-20T10:00:00",
  "updatedBy": null,
  "updatedAt": null,
  "calendarDays": [
    {
      "calendarDayId": 1,
      "calendarId": 1,
      "dayDate": "2025-01-01",
      "dayType": "HOLIDAY",
      "note": "New Year's Day"
    },
    {
      "calendarDayId": 2,
      "calendarId": 1,
      "dayDate": "2025-01-20",
      "dayType": "HOLIDAY",
      "note": "Martin Luther King Jr. Day"
    }
    // ... more calendar days
  ]
}
```

### 2. Create Calendar with Specific Run Days

**Request Body:**
```json
{
  "calendarName": "Monthly Financial Close",
  "description": "Only runs on month-end business days",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "recurrence": "MONTHLY",
  "createdBy": "finance@company.com",
  "calendarDays": [
    {
      "dayDate": "2025-01-31",
      "dayType": "RUNDAY",
      "note": "January month-end close"
    },
    {
      "dayDate": "2025-02-28",
      "dayType": "RUNDAY",
      "note": "February month-end close"
    },
    {
      "dayDate": "2025-03-31",
      "dayType": "RUNDAY",
      "note": "March month-end close"
    }
    // ... continue for all months
  ]
}
```

## 🔄 Workflow Instance Creation with Calendar

### 1. Create Workflow Instance with Calendar Mapping

**Endpoint:** `POST /api/execution/workflows/start-with-calendar`

**Description:** Creates a workflow instance and associates it with a specific calendar for date validation.

**Request Body:**
```json
{
  "workflowId": 123,
  "startedBy": 456,
  "calendarId": 1,
  "triggeredBy": "system_scheduler",
  "scheduledStartTime": "2025-01-02T09:00:00"
}
```

**Response:**
```json
{
  "instanceId": 789,
  "workflowId": 123,
  "status": "PENDING",
  "startedBy": 456,
  "startedOn": "2024-12-20T10:00:00",
  "completedOn": null,
  "escalatedTo": null,
  "calendarId": 1,
  "calendarName": "US Business Days 2025",
  "workflowName": "Financial Close Process",
  "startedByUsername": "john.doe",
  "escalatedToUsername": null,
  "instanceTasks": [
    {
      "instanceTaskId": 1001,
      "instanceId": 789,
      "taskId": 2001,
      "status": "PENDING",
      "assignedTo": 456,
      "startedOn": "2024-12-20T10:00:00",
      "completedOn": null,
      "decisionOutcome": null,
      "assignedToUsername": "john.doe",
      "taskName": "Collect Financial Data",
      "taskType": "FILE_UPLOAD"
    }
    // ... more tasks
  ]
}
```

### 2. Create Workflow Instance without Calendar

**Request Body:**
```json
{
  "workflowId": 124,
  "startedBy": 456,
  "triggeredBy": "manual",
  "scheduledStartTime": null
}
```

## 📊 Calendar Validation & Business Rules

### 1. Check if Date is Valid for Workflow Execution

**Endpoint:** `GET /api/calendar/{calendarId}/can-execute?date={date}`

**Example Request:**
```
GET /api/calendar/1/can-execute?date=2025-01-02
```

**Response:** `true` (January 2, 2025 is a valid business day)

**Example Request:**
```
GET /api/calendar/1/can-execute?date=2025-01-01
```

**Response:** `false` (January 1, 2025 is New Year's Day - holiday)

### 2. Get Next Valid Execution Date

**Endpoint:** `GET /api/calendar/{calendarId}/next-valid-date?fromDate={date}`

**Example Request:**
```
GET /api/calendar/1/next-valid-date?fromDate=2025-01-01
```

**Response:** `2025-01-02` (Next business day after New Year's Day)

### 3. Get Valid Dates in Range

**Endpoint:** `GET /api/calendar/{calendarId}/valid-dates?startDate={start}&endDate={end}`

**Example Request:**
```
GET /api/calendar/1/valid-dates?startDate=2025-01-01&endDate=2025-01-31
```

**Response:**
```json
[
  "2025-01-02",
  "2025-01-03",
  "2025-01-06",
  "2025-01-07",
  "2025-01-08",
  "2025-01-09",
  "2025-01-10",
  "2025-01-13",
  "2025-01-14",
  "2025-01-15",
  "2025-01-16",
  "2025-01-17",
  "2025-01-20",
  "2025-01-21",
  "2025-01-22",
  "2025-01-23",
  "2025-01-24",
  "2025-01-27",
  "2025-01-28",
  "2025-01-29",
  "2025-01-30",
  "2025-01-31"
]
```

## 🎯 Business Logic Implementation

### Calendar Validation Rules

The system implements the following business logic:

1. **Holiday Handling:**
   - If a date is marked as `HOLIDAY` → workflow **cannot** execute
   - Holidays are always invalid regardless of other settings

2. **Run Day Handling:**
   - If `RUNDAY` entries exist → workflow **only** runs on those specific dates
   - If no `RUNDAY` entries → workflow runs on all valid business days

3. **Default Behavior (when no RUNDAY entries):**
   - Skip weekends (Saturday/Sunday)
   - Skip holidays
   - Run on all other business days

4. **Recurrence Patterns:**
   - `NONE`: Skip weekends only
   - `DAILY`: Run every day except holidays
   - `WEEKLY`: Skip weekends
   - `MONTHLY`: Skip weekends
   - `YEARLY`: Skip weekends

### Example Scenarios

#### Scenario 1: US Business Calendar
- **Calendar Type:** General business calendar
- **Holidays:** US federal holidays
- **Behavior:** Skip weekends and holidays, run on all other days

#### Scenario 2: Monthly Financial Close
- **Calendar Type:** Specific run dates
- **RUNDAY entries:** Month-end dates
- **Behavior:** Only run on specified month-end dates

#### Scenario 3: Daily Processing
- **Calendar Type:** Daily operations
- **Recurrence:** DAILY
- **Behavior:** Run every day except holidays

## 🔧 API Integration Examples

### 1. Python Client Example

```python
import requests
import json

# Create calendar with days
def create_calendar_with_days():
    url = "http://localhost:8080/api/calendar/with-days"
    
    calendar_data = {
        "calendarName": "Q1 2025 Business Days",
        "description": "Q1 business calendar",
        "startDate": "2025-01-01",
        "endDate": "2025-03-31",
        "recurrence": "QUARTERLY",
        "createdBy": "admin@company.com",
        "calendarDays": [
            {"dayDate": "2025-01-01", "dayType": "HOLIDAY", "note": "New Year"},
            {"dayDate": "2025-01-20", "dayType": "HOLIDAY", "note": "MLK Day"},
            {"dayDate": "2025-02-17", "dayType": "HOLIDAY", "note": "Presidents Day"}
        ]
    }
    
    response = requests.post(url, json=calendar_data)
    return response.json()

# Create workflow instance with calendar
def create_workflow_with_calendar():
    url = "http://localhost:8080/api/execution/workflows/start-with-calendar"
    
    workflow_data = {
        "workflowId": 123,
        "startedBy": 456,
        "calendarId": 1,
        "triggeredBy": "system_scheduler"
    }
    
    response = requests.post(url, json=workflow_data)
    return response.json()

# Check if date is valid
def check_date_validity(calendar_id, date):
    url = f"http://localhost:8080/api/calendar/{calendar_id}/can-execute"
    params = {"date": date}
    
    response = requests.get(url, params=params)
    return response.json()
```

### 2. JavaScript/Node.js Client Example

```javascript
const axios = require('axios');

// Create calendar with days
async function createCalendarWithDays() {
    const url = 'http://localhost:8080/api/calendar/with-days';
    
    const calendarData = {
        calendarName: 'Q1 2025 Business Days',
        description: 'Q1 business calendar',
        startDate: '2025-01-01',
        endDate: '2025-03-31',
        recurrence: 'QUARTERLY',
        createdBy: 'admin@company.com',
        calendarDays: [
            { dayDate: '2025-01-01', dayType: 'HOLIDAY', note: 'New Year' },
            { dayDate: '2025-01-20', dayType: 'HOLIDAY', note: 'MLK Day' },
            { dayDate: '2025-02-17', dayType: 'HOLIDAY', note: 'Presidents Day' }
        ]
    };
    
    try {
        const response = await axios.post(url, calendarData);
        return response.data;
    } catch (error) {
        console.error('Error creating calendar:', error.response.data);
        throw error;
    }
}

// Create workflow instance with calendar
async function createWorkflowWithCalendar() {
    const url = 'http://localhost:8080/api/execution/workflows/start-with-calendar';
    
    const workflowData = {
        workflowId: 123,
        startedBy: 456,
        calendarId: 1,
        triggeredBy: 'system_scheduler'
    };
    
    try {
        const response = await axios.post(url, workflowData);
        return response.data;
    } catch (error) {
        console.error('Error creating workflow:', error.response.data);
        throw error;
    }
}

// Check date validity
async function checkDateValidity(calendarId, date) {
    const url = `http://localhost:8080/api/calendar/${calendarId}/can-execute`;
    
    try {
        const response = await axios.get(url, { params: { date } });
        return response.data;
    } catch (error) {
        console.error('Error checking date validity:', error.response.data);
        throw error;
    }
}
```

## 📋 Error Handling

### Common Error Responses

#### 1. Calendar Validation Errors
```json
{
  "timestamp": "2024-12-20T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Workflow cannot execute on this date according to calendar: 1",
  "path": "/api/execution/workflows/start-with-calendar"
}
```

#### 2. Calendar Not Found
```json
{
  "timestamp": "2024-12-20T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Calendar not found with ID: 999",
  "path": "/api/calendar/999"
}
```

#### 3. Validation Errors
```json
{
  "timestamp": "2024-12-20T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "calendarName",
      "message": "Calendar name is required"
    },
    {
      "field": "startDate",
      "message": "Start date is required"
    }
  ]
}
```

## 🚀 Best Practices

### 1. Calendar Design
- **Use descriptive names** for calendars (e.g., "US Business Days 2025")
- **Include comprehensive notes** for holidays and special dates
- **Plan for future dates** when creating calendars
- **Consider time zones** for global operations

### 2. Workflow Integration
- **Always validate dates** before starting workflows
- **Use calendar IDs** consistently across related workflows
- **Monitor calendar changes** that might affect running workflows
- **Implement fallback logic** for calendar failures

### 3. Performance Considerations
- **Batch calendar day creation** for large calendars
- **Cache calendar validation results** for frequently accessed dates
- **Use pagination** when retrieving large numbers of calendar days
- **Index calendar queries** for optimal performance

### 4. Maintenance
- **Regularly review and update** calendars for accuracy
- **Archive old calendars** to maintain system performance
- **Document calendar changes** for audit purposes
- **Test calendar logic** before deploying to production

## 🔍 Testing Calendar Functionality

### 1. Test Calendar Creation
```bash
# Test calendar creation with days
curl -X POST http://localhost:8080/api/calendar/with-days \
  -H "Content-Type: application/json" \
  -d @calendar_with_days.json
```

### 2. Test Date Validation
```bash
# Test if date is valid
curl "http://localhost:8080/api/calendar/1/can-execute?date=2025-01-02"

# Test next valid date
curl "http://localhost:8080/api/calendar/1/next-valid-date?fromDate=2025-01-01"
```

### 3. Test Workflow Creation
```bash
# Test workflow creation with calendar
curl -X POST http://localhost:8080/api/execution/workflows/start-with-calendar \
  -H "Content-Type: application/json" \
  -d @workflow_with_calendar.json
```

This comprehensive guide covers all aspects of the calendar integration and workflow instance mapping functionality in the DocWF system.
