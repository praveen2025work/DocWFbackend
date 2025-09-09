# Workflow Configuration Samples

This directory contains sample JSON files demonstrating the new workflow configuration system with file dependencies and sequence-based mapping.

## Files

### 1. workflow-create-with-sequences.json
**Purpose**: Demonstrates creating a new workflow with tasks and files using sequence-based mapping.

**Key Features**:
- Uses `taskSequence` and `parentTaskSequences` for task relationships
- Uses `fileSequence` and `parentFileSequence` for file dependencies
- Shows how to create complex file dependency chains
- Includes all workflow components: tasks, files, roles, and parameters

**Usage**:
```bash
POST /api/workflows/create-with-sequences
Content-Type: application/json

# Use the content from workflow-create-with-sequences.json
```

### 2. workflow-update-with-sequences.json
**Purpose**: Demonstrates updating an existing workflow with mixed ID-based and sequence-based mapping.

**Key Features**:
- Uses `taskId` for existing tasks and `taskSequence` for new tasks
- Uses `fileId` for existing files and `fileSequence` for new files
- Shows how to update existing relationships and add new ones
- Demonstrates the hybrid approach for updates

**Usage**:
```bash
PUT /api/workflows/{workflowId}/update-with-sequences
Content-Type: application/json

# Use the content from workflow-update-with-sequences.json
```

### 3. file-dependency-examples.json
**Purpose**: Provides examples of different file dependency scenarios and patterns.

**Key Features**:
- Simple file chains
- Multiple dependencies
- Mixed dependency types (REQUIRED, OPTIONAL, CONDITIONAL)
- Sequence-based creation examples
- Usage notes and best practices

## API Endpoints

### New Workflow Creation
- **POST** `/api/workflows/create-with-sequences` - Create workflow with sequence mapping

### Workflow Updates
- **PUT** `/api/workflows/{workflowId}/update-with-sequences` - Update workflow with sequence mapping

### File Management
- **POST** `/api/workflows/{workflowId}/tasks/{taskId}/files` - Add file to task
- **GET** `/api/workflows/{workflowId}/tasks/{taskId}/files` - Get task files
- **PUT** `/api/workflows/{workflowId}/tasks/{taskId}/files/{fileId}` - Update task file
- **DELETE** `/api/workflows/{workflowId}/tasks/{taskId}/files/{fileId}` - Delete task file

### File Dependencies
- **POST** `/api/workflows/files/{fileId}/dependencies` - Add file dependency
- **GET** `/api/workflows/files/{fileId}/dependencies` - Get file dependencies
- **PUT** `/api/workflows/dependencies/{dependencyId}` - Update file dependency
- **DELETE** `/api/workflows/dependencies/{dependencyId}` - Delete file dependency

## Key Concepts

### Sequence-based Mapping
When creating new workflows, use sequence numbers instead of IDs:
- `taskSequence`: Order of task in the workflow (1, 2, 3, ...)
- `parentTaskSequences`: Array of parent task sequences
- `fileSequence`: Order of file within a task (1, 2, 3, ...)
- `parentFileSequence`: Parent file sequence for dependencies

### ID-based Mapping
When updating existing workflows, use actual IDs:
- `taskId`: Actual task ID from database
- `fileId`: Actual file ID from database
- `parentTaskIds`: Comma-separated list of parent task IDs
- `parentFileId`: Actual parent file ID

### Dependency Types
- **REQUIRED**: File must be present and completed
- **OPTIONAL**: File may be present but not required
- **CONDITIONAL**: File dependency based on conditions

## Best Practices

1. **For New Workflows**: Always use sequence-based mapping
2. **For Updates**: Use ID-based mapping for existing items, sequence-based for new items
3. **File Dependencies**: Define clear dependency chains to avoid circular references
4. **Validation**: The system automatically prevents circular dependencies
5. **Cleanup**: Dependencies are automatically cleaned up when files are deleted

## Example Workflow Structure

```
Workflow: Monthly Financial Reporting
├── Task 1: Data Collection (sequence: 1)
│   ├── File 1: raw_sales_data.xlsx (sequence: 1)
│   └── File 2: raw_expense_data.xlsx (sequence: 2)
├── Task 2: Data Validation (sequence: 2, parent: 1)
│   ├── File 1: validated_sales_data.xlsx (sequence: 1, depends on: Task1.File1)
│   └── File 2: validated_expense_data.xlsx (sequence: 2, depends on: Task1.File2)
├── Task 3: Report Generation (sequence: 3, parent: 2)
│   └── File 1: monthly_report.xlsx (sequence: 1, depends on: Task2.File1, Task2.File2)
└── Task 4: Management Review (sequence: 4, parent: 3)
```

This structure ensures proper data flow and dependency management throughout the workflow execution.
