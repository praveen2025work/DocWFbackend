# Process Owner Controller Updates

## Overview
The ProcessOwnerController has been updated to provide filtered execution entries for the logged-in user. The controller now automatically extracts the current user's ID from the security context instead of requiring it as a parameter.

## Key Changes Made

### 1. WorkflowExecutionService Interface
Added new methods for process owner functionality:
- `getWorkflowInstancesByProcessOwner(Long processOwnerId, String status, String priority)`
- `getTasksByProcessOwner(Long processOwnerId, String status, String priority)`
- `getWorkflowInstancesNeedingProcessOwnerAttention(Long processOwnerId)`
- `getOverdueWorkflowsForProcessOwner(Long processOwnerId)`
- `getProcessOwnerDashboard(Long processOwnerId)` - **NEW: Dashboard functionality**
- `getEscalationQueueForProcessOwner(Long processOwnerId)` - **NEW: Escalation queue**
- `getProcessOwnerStatistics(Long processOwnerId)` - **NEW: Statistics for dashboard**

### 2. Repository Layer Updates

#### WorkflowInstanceRepository
Added new query methods:
- `findByProcessOwnerRole(Long processOwnerId, InstanceStatus status)` - Finds workflows where user has PROCESS_OWNER role
- `findInstancesNeedingProcessOwnerAttention(Long processOwnerId)` - Finds workflows needing attention
- `findOverdueInstancesForProcessOwner(Long processOwnerId, LocalDateTime overdueThreshold)` - Finds overdue workflows
- **NEW Dashboard Queries:**
  - `countTotalWorkflowsByProcessOwner(Long processOwnerId)` - Count total workflows
  - `countActiveWorkflowsByProcessOwner(Long processOwnerId)` - Count active workflows
  - `countCompletedWorkflowsTodayByProcessOwner(Long processOwnerId)` - Count completed today
  - `countEscalatedWorkflowsByProcessOwner(Long processOwnerId)` - Count escalated workflows

#### WorkflowInstanceTaskRepository
Added new query methods:
- `findTasksByProcessOwnerRole(Long processOwnerId, TaskInstanceStatus status)` - Finds tasks for workflows where user has PROCESS_OWNER role
- `findOverdueTasksForProcessOwner(Long processOwnerId, LocalDateTime overdueThreshold)` - Finds overdue tasks
- **NEW Dashboard Queries:**
  - `countPendingTasksByProcessOwner(Long processOwnerId)` - Count pending tasks
  - `findPendingTasksByProcessOwnerForDashboard(Long processOwnerId)` - Find pending tasks for dashboard

### 3. Service Implementation
Updated `WorkflowExecutionServiceImpl` with implementations for all new process owner methods, including:
- Status filtering (PENDING, IN_PROGRESS, COMPLETED, etc.)
- Process owner role validation through WorkflowInstanceRole relationships
- Proper transaction management with `@Transactional(readOnly = true)`
- **NEW Dashboard Implementation:**
  - Comprehensive dashboard data aggregation
  - Statistics calculation and completion rate computation
  - Escalation queue processing
  - Limited result sets for dashboard performance (10 most recent items)

### 4. Controller Updates
Modified `ProcessOwnerController` to:
- Remove `processOwnerId` parameters from endpoints
- Automatically extract current user ID using Spring Security context
- Implement new endpoints for filtered data:
  - `GET /api/process-owners/workflows` - Get workflows with status/priority filtering
  - `GET /api/process-owners/tasks` - Get tasks with status/priority filtering
  - `GET /api/process-owners/workflows/attention-needed` - Get workflows needing attention
  - `GET /api/process-owners/workflows/overdue` - Get overdue workflows
  - **NEW: `GET /api/process-owners/dashboard`** - **Complete dashboard with workflow details available for execution**
  - **NEW: `GET /api/process-owners/escalation-queue`** - **Escalation queue for process owner**
  - **NEW: `GET /api/process-owners/stats`** - **Process owner statistics**

## Dashboard Functionality

### What the Dashboard Provides:
The `/api/process-owners/dashboard` endpoint now provides comprehensive process owner based workflow details available for execution:

1. **Active Workflows**: Shows the 10 most recent PENDING and IN_PROGRESS workflows
2. **Pending Tasks**: Displays the 10 most recent pending tasks that need attention
3. **Escalation Queue**: Lists all escalated workflows requiring process owner intervention
4. **Dashboard Summary**: Key metrics including:
   - Total workflows managed
   - Active workflows count
   - Pending tasks count
   - Escalated items count
   - Completed workflows today
   - Overall completion rate
5. **Recent Statistics**: Process owner performance metrics

### Dashboard Data Sources:
- **Workflow Instances**: Filtered by PROCESS_OWNER role and status
- **Tasks**: Pending tasks from managed workflows
- **Statistics**: Real-time counts and calculated metrics
- **Escalations**: Workflows escalated to the process owner

## Security Implementation
- Added Spring Security imports for authentication
- Implemented `getCurrentUserId()` helper method
- Controller now automatically uses logged-in user's ID
- TODO: Implement proper user ID extraction from UserDetails

## Data Filtering
The system now provides filtered execution entries based on:
- **User Role**: Only workflows/tasks where the user has PROCESS_OWNER role
- **Status**: Filter by workflow/task status (PENDING, IN_PROGRESS, COMPLETED, etc.)
- **Priority**: Placeholder for future priority filtering (requires adding priority fields to entities)
- **Attention Needed**: Workflows that are escalated, overdue, or stuck
- **Overdue**: Workflows exceeding expected completion time
- **Dashboard**: Comprehensive overview with limited, relevant data for execution

## Database Relationships
The filtering works through the following entity relationships:
```
WorkflowInstance → WorkflowInstanceRole → WorkflowRole → WorkflowUser
WorkflowInstanceTask → WorkflowInstance → WorkflowInstanceRole → WorkflowRole → WorkflowUser
```

## Usage Examples

### Get Complete Dashboard
```http
GET /api/process-owners/dashboard
```

### Get All Workflows for Current Process Owner
```http
GET /api/process-owners/workflows
```

### Get Workflows by Status
```http
GET /api/process-owners/workflows?status=IN_PROGRESS
```

### Get Tasks by Status
```http
GET /api/process-owners/tasks?status=PENDING
```

### Get Workflows Needing Attention
```http
GET /api/process-owners/workflows/attention-needed
```

### Get Overdue Workflows
```http
GET /api/process-owners/workflows/overdue
```

### Get Escalation Queue
```http
GET /api/process-owners/escalation-queue
```

### Get Process Owner Statistics
```http
GET /api/process-owners/stats
```

## Future Enhancements
1. **Priority Filtering**: Add priority fields to WorkflowConfig and WorkflowConfigTask entities
2. **User ID Extraction**: Implement proper user ID extraction from Spring Security UserDetails
3. **Advanced Filtering**: Add date range, workflow type, and other filtering options
4. **Pagination**: Implement pagination for large result sets
5. **Caching**: Add caching for frequently accessed process owner data
6. **Real-time Updates**: Implement WebSocket for real-time dashboard updates
7. **Customizable Dashboard**: Allow process owners to customize their dashboard layout

## Notes
- The system assumes a role named 'PROCESS_OWNER' exists in the WorkflowRole table
- Priority filtering is currently disabled due to missing priority fields in entities
- All new methods use read-only transactions for better performance
- The overdue threshold is currently set to 7 days (configurable)
- Dashboard results are limited to 10 items for performance optimization
- The dashboard provides a complete overview of workflows available for execution by the process owner
