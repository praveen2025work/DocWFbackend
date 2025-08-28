# Complex Workflow Flow Diagram

## **🔄 Workflow Overview**

This document describes the complex workflow with file dependencies, updates, consolidation, and decision-based routing with revisitation capabilities.

## **📋 Task Flow**

```
Task 1 (Upload) → Task 2 (Update) → Task 4 (Consolidate) → Task 5 (Decision)
     ↓                    ↓                    ↓                    ↓
  File 1,2,3        File 1,2 Updates      All Files +        Final Approval
     ↓                    ↓                Updates            or Revision
  Available for      Available for        User Selects       Routes back to
  Tasks 2 & 3        Consolidation        Files to           Tasks 2,3,4
                     (Task 3 can run      Consolidate        based on outcome
                      in parallel)        (Manual/Auto)
```

## **🎯 Detailed Task Breakdown**

### **Task 1: Initial Document Upload**
- **Type**: `FILE_UPLOAD`
- **Files**: document_1.pdf, document_2.docx, document_3.xlsx
- **Dependencies**: None (starting point)
- **Output**: 3 files available for downstream tasks

### **Task 2: Document 1 and 2 Updates**
- **Type**: `FILE_UPDATE`
- **Files**: document_1_updated.pdf, document_2_updated.docx
- **Dependencies**: Task 1 (source files)
- **Parallel Execution**: Can run with Task 3
- **Output**: 2 updated files + access to original files

### **Task 3: Document 3 Update**
- **Type**: `FILE_UPDATE`
- **Files**: document_3_updated.xlsx
- **Dependencies**: Task 1 (source files)
- **Parallel Execution**: Can run with Task 2
- **Output**: 1 updated file + access to original files

### **Task 4: File Consolidation**
- **Type**: `CONSOLIDATE_FILE`
- **Dependencies**: Tasks 1, 2, 3 (all files)
- **Available Files**: 
  - Original: document_1.pdf, document_2.docx, document_3.xlsx
  - Updates: document_1_updated.pdf, document_2_updated.docx, document_3_updated.xlsx
- **Selection Strategy**: User selects 3-6 files to consolidate
- **Consolidation Mode**: Hybrid (manual selection + auto-processing)
- **Output**: Consolidated file package

### **Task 5: Final Decision and Approval**
- **Type**: `DECISION`
- **Dependencies**: Task 4 (consolidated output)
- **Decision Options**: APPROVED, REJECTED, NEEDS_REVISION
- **Routing Logic**: Based on decision outcome

## **🔄 Decision Routing and Revisitation**

### **Decision Outcomes from Task 4 (Consolidation)**

| Decision | Action | Target Tasks | Strategy |
|-----------|---------|--------------|----------|
| `APPROVED` | Continue | None | Workflow proceeds |
| `REJECTED` | Revise | Tasks 2, 3 | Selective revision |
| `NEEDS_REVISION` | Revise | Tasks 2, 3 | Selective revision |

### **Decision Outcomes from Task 5 (Final Decision)**

| Decision | Action | Target Tasks | Strategy |
|-----------|---------|--------------|----------|
| `APPROVED` | Complete | None | Workflow completed |
| `REJECTED` | Revise | Tasks 2, 3, 4 | Cascade revision |
| `NEEDS_REVISION` | Revise | Tasks 2, 3 | Selective revision |

## **📁 File Flow and Dependencies**

### **File Availability Matrix**

| Task | Original Files | Updated Files | Total Available |
|------|----------------|---------------|-----------------|
| Task 1 | 3 | 0 | 3 |
| Task 2 | 3 | 2 | 5 |
| Task 3 | 3 | 1 | 4 |
| Task 4 | 3 | 3 | 6 |
| Task 5 | 3 | 3 + 1 consolidated | 7 |

### **File Selection Rules**

- **Task 2**: Must select exactly 2 files (original + updated)
- **Task 3**: Must select exactly 1 file (original + updated)
- **Task 4**: Must select 3-6 files from available pool
- **Task 5**: Must select 1 file (consolidated output)

## **⚙️ Consolidation Process**

### **Consolidation Modes**

1. **Manual**: User manually selects files and triggers consolidation
2. **Auto**: System automatically consolidates based on rules
3. **Hybrid**: User selects files, system processes automatically

### **Consolidation Rules**

```json
{
  "strategy": "merge",
  "outputFormat": "pdf",
  "includeMetadata": true,
  "fileOrder": ["original", "updated"],
  "qualityCheck": true
}
```

## **🔄 Revision Strategies**

### **Single Task Revision**
- Only the specified task is reopened
- Other tasks remain in their current state

### **Cascade Revision**
- Specified task and all downstream tasks are reopened
- Creates a new revision branch

### **Selective Revision**
- User chooses which specific tasks to reopen
- Provides granular control over revision scope

## **📊 Workflow States**

### **Task Status Flow**

```
PENDING → IN_PROGRESS → COMPLETED/REJECTED
    ↓           ↓              ↓
  Started    File Work     Decision Made
    ↓           ↓              ↓
  Files      Updates      Route to Next
  Available  Created      Task or Revise
```

### **File Status Flow**

```
PENDING → IN_PROGRESS → COMPLETED/REJECTED
    ↓           ↓              ↓
  Available   Being         Ready for
  for Task    Processed     Next Step
```

## **🔧 Technical Implementation**

### **Key Components**

1. **File Dependency Manager**: Tracks file availability across tasks
2. **Consolidation Engine**: Handles file merging and processing
3. **Decision Router**: Manages workflow routing based on outcomes
4. **Revision Manager**: Handles task reopening and state management
5. **File Version Control**: Manages file history and versions

### **Database Relationships**

- `WORKFLOW_CONFIG_TASK` → `fileSourceTaskIds` (source task dependencies)
- `WORKFLOW_CONFIG_TASK` → `consolidationRules` (consolidation logic)
- `TASK_DECISION_OUTCOME` → `revisionTaskIds` (revision targets)
- `WORKFLOW_INSTANCE_TASK_FILE` → `sourceFileId` (file lineage)

This workflow provides a robust framework for complex document processing with flexible file management, intelligent consolidation, and sophisticated decision routing capabilities.
