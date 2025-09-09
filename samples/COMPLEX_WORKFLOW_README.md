# Complex 5-Step Workflow Configuration

This document describes the implementation of a complex 5-step workflow configuration system that handles file uploads, updates, consolidation, and decision-making with proper dependency management and sequence-based mapping.

## Workflow Overview

The workflow consists of 5 sequential steps:

1. **File Upload** - Upload initial data files
2. **Sales Data Update** - Update and validate sales data
3. **Expense Data Update** - Update and validate expense data (can run in parallel with step 2)
4. **Data Consolidation** - Consolidate data from all previous steps
5. **Management Decision** - Review and make decisions with revision paths

## Key Features

### 1. Sequence-Based Mapping for New Workflows
When creating new workflows, use sequence numbers instead of IDs:
- `taskSequence`: Unique sequence number for each task
- `parentTaskSequences`: List of parent task sequences
- `fileSequence`: Unique sequence number for each file
- `parentFileSequence`: Parent file sequence for dependencies

### 2. File Dependencies
- Simple structure: `file_id` depends on `parent_file_id`
- Composite primary key ensures unique relationships
- Automatic circular dependency prevention
- Cascade cleanup when files are deleted

### 3. Decision Outcomes
- Multiple decision paths with different revision strategies
- Support for single task, cascade, and selective revisions
- Auto-escalation capabilities
- JSON-based revision mapping

### 4. Task Types
- **FILE_UPLOAD**: Upload new files
- **FILE_UPDATE**: Update existing files
- **CONSOLIDATE_FILE**: Consolidate multiple files
- **DECISION**: Make approval/rejection decisions

## API Endpoints

### Create Workflow with Sequence Mapping
```http
POST /api/workflows/create-with-sequences
Content-Type: application/json

{
  "name": "Financial Data Processing Workflow",
  "description": "Complex 5-step workflow...",
  "status": "ACTIVE",
  "createdBy": "admin",
  "tasks": [
    {
      "taskSequence": 1,
      "name": "Step 1: File Upload",
      "taskType": "FILE_UPLOAD",
      "roleId": 1,
      "sequenceOrder": 1,
      "taskFiles": [
        {
          "fileSequence": 1,
          "fileName": "sales_data.xlsx",
          "dependencies": []
        }
      ]
    }
  ]
}
```

### Update Workflow with Sequence Mapping
```http
PUT /api/workflows/{workflowId}/update-with-sequences
Content-Type: application/json

{
  "name": "Updated Workflow Name",
  "tasks": [
    {
      "taskId": 123,
      "name": "Updated Task Name",
      "taskFiles": [
        {
          "fileId": 456,
          "fileName": "updated_file.xlsx"
        }
      ]
    }
  ]
}
```

## Sample JSON Files

### 1. complex-workflow-5-steps.json
Complete backend-compatible JSON for the 5-step workflow with all technical details.

### 2. ui-workflow-config.json
UI-friendly JSON structure with simplified field names and better organization for frontend consumption.

## Database Schema

### WORKFLOW_CONFIG_TASK_FILE_DEPENDENCY
```sql
CREATE TABLE WORKFLOW_CONFIG_TASK_FILE_DEPENDENCY (
    FILE_ID              NUMBER NOT NULL,
    PARENT_FILE_ID       NUMBER NOT NULL,
    CREATED_BY           VARCHAR2(100) NOT NULL,
    CREATED_ON           TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT CHK_FILE_DEPENDENCY_SELF CHECK (FILE_ID != PARENT_FILE_ID),
    PRIMARY KEY (FILE_ID, PARENT_FILE_ID)
);
```

### TASK_DECISION_OUTCOME
```sql
CREATE TABLE TASK_DECISION_OUTCOME (
    OUTCOME_ID           NUMBER PRIMARY KEY,
    TASK_ID              NUMBER NOT NULL,
    OUTCOME_NAME         VARCHAR2(255) NOT NULL,
    TARGET_TASK_ID       NUMBER,
    REVISION_TYPE        VARCHAR2(50) DEFAULT 'SINGLE',
    REVISION_TASK_IDS    VARCHAR2(1000),
    REVISION_STRATEGY    VARCHAR2(50) DEFAULT 'REPLACE',
    REVISION_PRIORITY    NUMBER DEFAULT 1,
    REVISION_CONDITIONS  CLOB,
    AUTO_ESCALATE        VARCHAR2(1) DEFAULT 'N',
    ESCALATION_ROLE_ID   NUMBER,
    CREATED_BY           VARCHAR2(100),
    CREATED_AT           TIMESTAMP DEFAULT SYSTIMESTAMP
);
```

## Usage Examples

### Creating a New Workflow
1. Use `complex-workflow-5-steps.json` as a template
2. Replace sequence numbers with your desired values
3. Set appropriate role IDs
4. Configure file dependencies using sequence mapping
5. Call the create endpoint

### Updating an Existing Workflow
1. Use actual task IDs and file IDs
2. Add new tasks/files with sequence mapping
3. Update existing tasks/files with their IDs
4. Call the update endpoint

### File Dependencies
```json
{
  "dependencies": [
    {
      "fileSequence": 2,
      "parentFileSequence": 1,
      "createdBy": "admin"
    }
  ]
}
```

### Decision Outcomes
```json
{
  "decisionOutcomes": [
    {
      "outcomeName": "APPROVE",
      "targetTaskSequence": null,
      "revisionType": "SINGLE",
      "revisionTaskIds": "",
      "revisionStrategy": "REPLACE",
      "revisionPriority": 1,
      "autoEscalate": "N"
    },
    {
      "outcomeName": "REVISE_SALES",
      "targetTaskSequence": 2,
      "revisionType": "SINGLE",
      "revisionTaskIds": "2",
      "revisionStrategy": "REPLACE",
      "revisionPriority": 2,
      "autoEscalate": "N"
    }
  ]
}
```

## Validation Rules

1. **Circular Dependencies**: Automatically prevented
2. **File Dependencies**: Must reference valid parent files
3. **Task Dependencies**: Must reference valid parent tasks
4. **Decision Outcomes**: Must have valid target tasks or null for approval
5. **Sequence Numbers**: Must be unique within the workflow

## Error Handling

- Invalid sequence numbers return 400 Bad Request
- Circular dependencies return 400 Bad Request
- Missing required fields return 400 Bad Request
- Non-existent role IDs return 404 Not Found
- Database constraints violations return 500 Internal Server Error

## Performance Considerations

- Use batch operations for large workflows
- Implement pagination for workflow lists
- Cache role and user information
- Use database indexes for frequent queries
- Consider async processing for complex consolidations

## Security

- Validate all input data
- Implement role-based access control
- Audit all workflow changes
- Encrypt sensitive file data
- Use HTTPS for all API calls

## Monitoring and Logging

- Log all workflow creation/updates
- Track file dependency resolution
- Monitor decision outcome paths
- Alert on escalation events
- Generate audit reports

## Future Enhancements

1. **Workflow Templates**: Pre-defined workflow configurations
2. **Dynamic Rules**: Runtime rule evaluation
3. **Workflow Versioning**: Track configuration changes
4. **Performance Analytics**: Workflow execution metrics
5. **Integration APIs**: Connect with external systems
