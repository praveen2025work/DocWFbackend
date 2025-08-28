-- =====================================================
-- WORKFLOW MANAGEMENT SYSTEM - DATABASE MIGRATION
-- =====================================================

-- Migration script to update existing database schema
-- Run this script after updating the main schema.sql

------------------------------------------------------------
-- MIGRATION 1: Add calendar mapping to WORKFLOW_CONFIG
------------------------------------------------------------

-- Add calendar mapping column to WORKFLOW_CONFIG table
ALTER TABLE WORKFLOW_CONFIG ADD (
    CALENDAR_ID              NUMBER  -- Reference to the assigned calendar for this workflow
);

------------------------------------------------------------
-- MIGRATION 2: Add new task fields to WORKFLOW_CONFIG_TASK
------------------------------------------------------------

-- Add new task-related columns to WORKFLOW_CONFIG_TASK table
ALTER TABLE WORKFLOW_CONFIG_TASK ADD (
    PARENT_TASK_IDS          VARCHAR2(500),
    CAN_BE_REVISITED        CHAR(1) DEFAULT 'N',
    MAX_REVISITS            NUMBER DEFAULT 0,
    FILE_SELECTION_MODE     VARCHAR2(50),
    SOURCE_TASK_IDS         VARCHAR2(500),
    FILE_SOURCE_TASK_IDS    VARCHAR2(500),
    ALLOW_NEW_FILES         CHAR(1) DEFAULT 'N',
    CAN_RUN_IN_PARALLEL     CHAR(1) DEFAULT 'N',
    PARALLEL_TASK_IDS       VARCHAR2(500),
    TASK_DESCRIPTION        VARCHAR2(1000),
    IS_DECISION_TASK        CHAR(1) DEFAULT 'N',
    DECISION_TYPE           VARCHAR2(50),
    TASK_PRIORITY           VARCHAR2(20) DEFAULT 'MEDIUM',
    AUTO_ESCALATION_ENABLED CHAR(1) DEFAULT 'Y',
    NOTIFICATION_REQUIRED   CHAR(1) DEFAULT 'Y',
    TASK_STATUS             VARCHAR2(50) DEFAULT 'PENDING',
    TASK_STARTED_AT         TIMESTAMP,
    TASK_COMPLETED_AT       TIMESTAMP,
    TASK_REJECTED_AT        TIMESTAMP,
    TASK_REJECTION_REASON   VARCHAR2(500),
    TASK_COMPLETED_BY       VARCHAR2(100),
    TASK_REJECTED_BY        VARCHAR2(100),
    FILE_FILTER_JSON        CLOB,
    CONSOLIDATION_RULES_JSON CLOB,
    DECISION_CRITERIA_JSON  CLOB,
    METADATA_JSON           CLOB
);

-- Update existing records with default values
UPDATE WORKFLOW_CONFIG_TASK SET 
    CAN_BE_REVISITED = 'N',
    MAX_REVISITS = 0,
    ALLOW_NEW_FILES = 'N',
    CAN_RUN_IN_PARALLEL = 'N',
    IS_DECISION_TASK = 'N',
    TASK_PRIORITY = 'MEDIUM',
    AUTO_ESCALATION_ENABLED = 'Y',
    NOTIFICATION_REQUIRED = 'Y',
    TASK_STATUS = 'PENDING'
WHERE CAN_BE_REVISITED IS NULL;

-- Update task types to match new enum values
UPDATE WORKFLOW_CONFIG_TASK SET TASK_TYPE = 'CONSOLIDATE_FILE' 
WHERE TASK_TYPE = 'CONSOLIDATE_FILES';

------------------------------------------------------------
-- MIGRATION 3: Update WORKFLOW_CONFIG_TASK_FILE structure
------------------------------------------------------------

-- Add new file-related columns to WORKFLOW_CONFIG_TASK_FILE table
ALTER TABLE WORKFLOW_CONFIG_TASK_FILE ADD (
    FILE_LOCATION            VARCHAR2(1000),
    FILE_TYPE_REGEX          VARCHAR2(100),
    FILE_DESCRIPTION         VARCHAR2(500),
    IS_REQUIRED             CHAR(1) DEFAULT 'N',
    FILE_STATUS              VARCHAR2(50) DEFAULT 'PENDING',
    KEEP_FILE_VERSIONS      CHAR(1) DEFAULT 'Y',
    RETAIN_FOR_CURRENT_PERIOD CHAR(1) DEFAULT 'Y',
    FILE_COMMENTARY          VARCHAR2(1000),
    UPDATED_BY               VARCHAR2(100),
    UPDATED_ON               TIMESTAMP
);

-- Rename CREATED_AT to CREATED_ON for consistency
ALTER TABLE WORKFLOW_CONFIG_TASK_FILE RENAME COLUMN CREATED_AT TO CREATED_ON;

-- Update existing records with default values
UPDATE WORKFLOW_CONFIG_TASK_FILE SET 
    IS_REQUIRED = 'N',
    FILE_STATUS = 'PENDING',
    KEEP_FILE_VERSIONS = 'Y',
    RETAIN_FOR_CURRENT_PERIOD = 'Y'
WHERE IS_REQUIRED IS NULL;

-- Update action types to match new enum values
UPDATE WORKFLOW_CONFIG_TASK_FILE SET ACTION_TYPE = 'UPLOAD' 
WHERE ACTION_TYPE = 'FILE_UPLOAD';
UPDATE WORKFLOW_CONFIG_TASK_FILE SET ACTION_TYPE = 'UPDATE' 
WHERE ACTION_TYPE = 'FILE_UPDATE';
UPDATE WORKFLOW_CONFIG_TASK_FILE SET ACTION_TYPE = 'CONSOLIDATE' 
WHERE ACTION_TYPE = 'CONSOLIDATE_FILES';

-- Remove old columns that are no longer needed
ALTER TABLE WORKFLOW_CONFIG_TASK_FILE DROP COLUMN FILE_VERSION;
ALTER TABLE WORKFLOW_CONFIG_TASK_FILE DROP COLUMN PARENT_FILE_ID;
ALTER TABLE WORKFLOW_CONFIG_TASK_FILE DROP COLUMN CONSOLIDATED_FLAG;
ALTER TABLE WORKFLOW_CONFIG_TASK_FILE DROP COLUMN DECISION_OUTCOME;

------------------------------------------------------------
-- MIGRATION 4: Update TASK_DECISION_OUTCOME structure
------------------------------------------------------------

-- Add new decision outcome columns
ALTER TABLE TASK_DECISION_OUTCOME ADD (
    NEXT_ACTION              VARCHAR2(100),
    TARGET_TASK_ID          NUMBER
);

-- Update existing records
UPDATE TASK_DECISION_OUTCOME SET 
    NEXT_ACTION = 'CONTINUE',
    TARGET_TASK_ID = NEXT_TASK_ID
WHERE NEXT_ACTION IS NULL;

-- Remove old column
ALTER TABLE TASK_DECISION_OUTCOME DROP COLUMN NEXT_TASK_ID;

------------------------------------------------------------
-- MIGRATION 5: Update WORKFLOW_INSTANCE_TASK_FILE structure
------------------------------------------------------------

-- Update action types in instance task files
UPDATE WORKFLOW_INSTANCE_TASK_FILE SET ACTION_TYPE = 'UPLOAD' 
WHERE ACTION_TYPE = 'FILE_UPLOAD';
UPDATE WORKFLOW_INSTANCE_TASK_FILE SET ACTION_TYPE = 'UPDATE' 
WHERE ACTION_TYPE = 'FILE_UPDATE';
UPDATE WORKFLOW_INSTANCE_TASK_FILE SET ACTION_TYPE = 'CONSOLIDATE' 
WHERE ACTION_TYPE = 'CONSOLIDATE_FILES';

-- Remove DECISION from action types as it's now a task type
UPDATE WORKFLOW_INSTANCE_TASK_FILE SET ACTION_TYPE = 'UPDATE' 
WHERE ACTION_TYPE = 'DECISION';

------------------------------------------------------------
-- MIGRATION 6: Create indexes for performance
------------------------------------------------------------

-- Create indexes for new columns
CREATE INDEX IDX_WORKFLOW_CONFIG_CALENDAR ON WORKFLOW_CONFIG(CALENDAR_ID);
CREATE INDEX IDX_WORKFLOW_CONFIG_TASK_PARENT ON WORKFLOW_CONFIG_TASK(PARENT_TASK_IDS);
CREATE INDEX IDX_WORKFLOW_CONFIG_TASK_PARALLEL ON WORKFLOW_CONFIG_TASK(PARALLEL_TASK_IDS);
CREATE INDEX IDX_WORKFLOW_CONFIG_TASK_FILE_TYPE ON WORKFLOW_CONFIG_TASK_FILE(FILE_TYPE_REGEX);

------------------------------------------------------------
-- MIGRATION 7: Add constraints for data integrity
------------------------------------------------------------

-- Add check constraints for new columns
ALTER TABLE WORKFLOW_CONFIG_TASK ADD CONSTRAINT CHK_TASK_STATUS 
    CHECK (TASK_STATUS IN ('PENDING','IN_PROGRESS','COMPLETED','REJECTED'));

ALTER TABLE WORKFLOW_CONFIG_TASK_FILE ADD CONSTRAINT CHK_FILE_STATUS 
    CHECK (FILE_STATUS IN ('PENDING','IN_PROGRESS','COMPLETED','REJECTED'));

ALTER TABLE WORKFLOW_CONFIG_TASK ADD CONSTRAINT CHK_TASK_PRIORITY 
    CHECK (TASK_PRIORITY IN ('LOW','MEDIUM','HIGH','CRITICAL'));

------------------------------------------------------------
-- MIGRATION 8: Update existing data for compatibility
------------------------------------------------------------

-- Set default file type regex for existing files
UPDATE WORKFLOW_CONFIG_TASK_FILE SET 
    FILE_TYPE_REGEX = '*.*'
WHERE FILE_TYPE_REGEX IS NULL;

-- Set default file location for existing files
UPDATE WORKFLOW_CONFIG_TASK_FILE SET 
    FILE_LOCATION = '/default/location/'
WHERE FILE_LOCATION IS NULL;

------------------------------------------------------------
-- MIGRATION COMPLETE
------------------------------------------------------------

-- Verify migration
SELECT 'Migration completed successfully' as status FROM DUAL;

-- Show summary of changes
SELECT 
    'WORKFLOW_CONFIG' as table_name,
    COUNT(*) as total_records,
    'Calendar mapping added' as changes
FROM WORKFLOW_CONFIG
UNION ALL
SELECT 
    'WORKFLOW_CONFIG_TASK' as table_name,
    COUNT(*) as total_records,
    'Task workflow fields added' as changes
FROM WORKFLOW_CONFIG_TASK
UNION ALL
SELECT 
    'WORKFLOW_CONFIG_TASK_FILE' as table_name,
    COUNT(*) as total_records,
    'File management fields updated' as changes
FROM WORKFLOW_CONFIG_TASK_FILE;

-- Add new consolidation and decision routing fields to WORKFLOW_CONFIG_TASK
ALTER TABLE WORKFLOW_CONFIG_TASK ADD (
    CONSOLIDATION_MODE          VARCHAR2(50) DEFAULT 'MANUAL',
    CONSOLIDATION_RULES         VARCHAR2(1000),
    FILE_SELECTION_STRATEGY     VARCHAR2(50) DEFAULT 'ALL_AVAILABLE',
    MAX_FILE_SELECTIONS         NUMBER,
    MIN_FILE_SELECTIONS         NUMBER DEFAULT 1,
    CONSOLIDATION_TEMPLATE_ID   NUMBER,
    DECISION_REQUIRES_APPROVAL  CHAR(1) DEFAULT 'Y',
    DECISION_APPROVAL_ROLE_ID   NUMBER,
    REVISION_STRATEGY           VARCHAR2(50) DEFAULT 'SINGLE_TASK',
    REVISION_TASK_MAPPING       VARCHAR2(1000)
);

-- Add new decision routing fields to TASK_DECISION_OUTCOME
ALTER TABLE TASK_DECISION_OUTCOME ADD (
    REVISION_TYPE               VARCHAR2(50) DEFAULT 'SINGLE',
    REVISION_TASK_IDS           VARCHAR2(500),
    REVISION_STRATEGY           VARCHAR2(50) DEFAULT 'REPLACE',
    REVISION_PRIORITY           NUMBER DEFAULT 1,
    REVISION_CONDITIONS         VARCHAR2(1000),
    AUTO_ESCALATE               CHAR(1) DEFAULT 'N',
    ESCALATION_ROLE_ID          NUMBER,
    IS_ACTIVE                   CHAR(1) DEFAULT 'Y',
    UPDATED_BY                  VARCHAR2(100),
    UPDATED_ON                  TIMESTAMP
);

-- Rename CREATED_AT to CREATED_ON for consistency
ALTER TABLE TASK_DECISION_OUTCOME RENAME COLUMN CREATED_AT TO CREATED_ON;
