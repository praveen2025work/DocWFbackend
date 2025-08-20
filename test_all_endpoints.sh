#!/bin/bash

# DocWF API Testing Script - Updated for New ~60 Endpoints Structure
# This script tests all major API endpoints with the new predicate-based search

echo "🚀 DocWF API Testing Script - New Structure (~60 Endpoints)"
echo "=========================================================="
echo ""

# Configuration
BASE_URL="http://localhost:8080"
API_BASE="/api"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test counter
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to run a test
run_test() {
    local test_name="$1"
    local method="$2"
    local url="$3"
    local data="$4"
    local expected_status="$5"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    echo -n "Testing: $test_name... "
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "%{http_code}" -o /tmp/response.json "$BASE_URL$url")
    elif [ "$method" = "POST" ] || [ "$method" = "PUT" ] || [ "$method" = "PATCH" ]; then
        response=$(curl -s -w "%{http_code}" -o /tmp/response.json -X "$method" -H "Content-Type: application/json" -d "$data" "$BASE_URL$url")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "%{http_code}" -o /tmp/response.json -X DELETE "$BASE_URL$url")
    fi
    
    http_code="${response: -3}"
    
    if [ "$http_code" = "$expected_status" ]; then
        echo -e "${GREEN}✓ PASS${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ FAIL (Expected: $expected_status, Got: $http_code)${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        echo "Response: $(cat /tmp/response.json)"
    fi
}

# Function to test predicate-based search
test_predicate_search() {
    local controller="$1"
    local search_params="$2"
    local test_name="$3"
    
    run_test "$test_name" "GET" "$API_BASE/$controller/search?$search_params" "" "200"
}

echo "📅 Testing Calendar Management APIs (18 endpoints)"
echo "------------------------------------------------"

# Calendar CRUD Operations
run_test "Create Calendar" "POST" "$API_BASE/calendar" '{"calendarName":"Test Calendar","description":"Test calendar for API testing","startDate":"2025-01-01","endDate":"2025-12-31","recurrence":"YEARLY","createdBy":"admin@docwf.com"}' "201"

# Get calendar ID from response
CALENDAR_ID=$(cat /tmp/response.json | grep -o '"calendarId":[0-9]*' | cut -d':' -f2)
if [ -z "$CALENDAR_ID" ]; then
    CALENDAR_ID=1
fi

run_test "Get Calendar by ID" "GET" "$API_BASE/calendar/$CALENDAR_ID" "" "200"
run_test "Update Calendar" "PUT" "$API_BASE/calendar/$CALENDAR_ID" '{"calendarName":"Updated Test Calendar","description":"Updated test calendar","startDate":"2025-01-01","endDate":"2025-12-31","recurrence":"YEARLY","updatedBy":"admin@docwf.com"}' "200"

# Calendar Predicate-Based Search
test_predicate_search "calendar" "calendarName=Test&recurrence=YEARLY&page=0&size=20" "Search Calendars with Predicates"
test_predicate_search "calendar" "recurrence=YEARLY&page=0&size=20" "Get All Calendars with Filtering"

# Calendar Days Management
run_test "Add Calendar Day" "POST" "$API_BASE/calendar/$CALENDAR_ID/days" '{"dayDate":"2025-01-01","dayType":"HOLIDAY","note":"New Year Day"}' "201"
run_test "Add Multiple Calendar Days" "POST" "$API_BASE/calendar/$CALENDAR_ID/days/batch" '[{"dayDate":"2025-01-20","dayType":"HOLIDAY","note":"MLK Day"},{"dayDate":"2025-02-17","dayType":"HOLIDAY","note":"Presidents Day"}]' "201"
run_test "Get Calendar Days" "GET" "$API_BASE/calendar/$CALENDAR_ID/days" "" "200"

# Calendar Validation & Workflow Integration
run_test "Validate Date" "GET" "$API_BASE/calendar/$CALENDAR_ID/validate-date?date=2025-01-01" "" "200"
run_test "Check Workflow Execution" "GET" "$API_BASE/calendar/$CALENDAR_ID/can-execute?date=2025-01-02" "" "200"
run_test "Get Next Valid Date" "GET" "$API_BASE/calendar/$CALENDAR_ID/next-valid-date?fromDate=2025-01-01" "" "200"
run_test "Get Previous Valid Date" "GET" "$API_BASE/calendar/$CALENDAR_ID/previous-valid-date?fromDate=2025-01-01" "" "200"

# Calendar Utility Operations
run_test "Get Calendar Days by Type" "GET" "$API_BASE/calendar/$CALENDAR_ID/days/type/HOLIDAY" "" "200"
run_test "Get Valid Dates in Range" "GET" "$API_BASE/calendar/$CALENDAR_ID/valid-dates?startDate=2025-01-01&endDate=2025-01-31" "" "200"
run_test "Get Holidays in Range" "GET" "$API_BASE/calendar/$CALENDAR_ID/holidays?startDate=2025-01-01&endDate=2025-12-31" "" "200"
run_test "Get Run Days in Range" "GET" "$API_BASE/calendar/$CALENDAR_ID/run-days?startDate=2025-01-01&endDate=2025-12-31" "" "200"

echo ""
echo "⚙️ Testing Workflow Configuration APIs (18 endpoints)"
echo "----------------------------------------------------"

# Workflow CRUD Operations
run_test "Create Workflow" "POST" "$API_BASE/workflows" '{"name":"Test Workflow","description":"Test workflow for API testing","dueInMins":1440,"escalationAfterMins":2880,"reminderBeforeDueMins":240,"minutesAfterDue":60,"isActive":"Y","createdBy":"admin@docwf.com"}' "201"

# Get workflow ID from response
WORKFLOW_ID=$(cat /tmp/response.json | grep -o '"workflowId":[0-9]*' | cut -d':' -f2)
if [ -z "$WORKFLOW_ID" ]; then
    WORKFLOW_ID=1
fi

run_test "Get Workflow by ID" "GET" "$API_BASE/workflows/$WORKFLOW_ID" "" "200"
run_test "Update Workflow" "PUT" "$API_BASE/workflows/$WORKFLOW_ID" '{"name":"Updated Test Workflow","description":"Updated test workflow","dueInMins":1440,"escalationAfterMins":2880,"reminderBeforeDueMins":240,"minutesAfterDue":60,"isActive":"Y","updatedBy":"admin@docwf.com"}' "200"

# Workflow Predicate-Based Search
test_predicate_search "workflows" "name=Test&isActive=Y&page=0&size=20" "Search Workflows with Predicates"
test_predicate_search "workflows" "isActive=Y&page=0&size=20" "Get All Workflows with Filtering"

# Workflow Task Management
run_test "Add Task to Workflow" "POST" "$API_BASE/workflows/$WORKFLOW_ID/tasks" '{"name":"Test Task","description":"Test task for API testing","sequenceOrder":1,"taskType":"REVIEW","roleId":1,"createdBy":"admin@docwf.com"}' "201"
run_test "Get Workflow Tasks" "GET" "$API_BASE/workflows/$WORKFLOW_ID/tasks" "" "200"

# Get task ID from response
TASK_ID=$(cat /tmp/response.json | grep -o '"taskId":[0-9]*' | cut -d':' -f2)
if [ -z "$TASK_ID" ]; then
    TASK_ID=1
fi

run_test "Update Workflow Task" "PUT" "$API_BASE/workflows/$WORKFLOW_ID/tasks/$TASK_ID" '{"name":"Updated Test Task","description":"Updated test task","sequenceOrder":1,"taskType":"REVIEW","roleId":1,"updatedBy":"admin@docwf.com"}' "200"

# Workflow Role Management
run_test "Assign Role to Workflow" "POST" "$API_BASE/workflows/$WORKFLOW_ID/roles" '{"roleId":1,"createdBy":"admin@docwf.com"}' "201"
run_test "Get Workflow Roles" "GET" "$API_BASE/workflows/$WORKFLOW_ID/roles" "" "200"

# Workflow Parameter Management
run_test "Add Parameter to Workflow" "POST" "$API_BASE/workflows/$WORKFLOW_ID/parameters" '{"paramKey":"TEST_PARAM","paramValue":"test_value","description":"Test parameter","createdBy":"admin@docwf.com"}' "201"
run_test "Get Workflow Parameters" "GET" "$API_BASE/workflows/$WORKFLOW_ID/parameters" "" "200"

# Get parameter ID from response
PARAM_ID=$(cat /tmp/response.json | grep -o '"paramId":[0-9]*' | cut -d':' -f2)
if [ -z "$PARAM_ID" ]; then
    PARAM_ID=1
fi

run_test "Update Workflow Parameter" "PUT" "$API_BASE/workflows/$WORKFLOW_ID/parameters/$PARAM_ID" '{"paramKey":"TEST_PARAM","paramValue":"updated_value","description":"Updated test parameter","updatedBy":"admin@docwf.com"}' "200"

# Workflow Utility Operations
run_test "Toggle Workflow Status" "PATCH" "$API_BASE/workflows/$WORKFLOW_ID/status?isActive=N" "" "200"
run_test "Reorder Tasks" "POST" "$API_BASE/workflows/$WORKFLOW_ID/tasks/reorder" "[1]" "200"

echo ""
echo "👥 Testing User Management APIs (10 endpoints)"
echo "----------------------------------------------"

# User CRUD Operations
run_test "Create User" "POST" "$API_BASE/users" '{"username":"testuser","firstName":"Test","lastName":"User","email":"testuser@company.com","isActive":"Y","createdBy":"admin@docwf.com"}' "201"

# Get user ID from response
USER_ID=$(cat /tmp/response.json | grep -o '"userId":[0-9]*' | cut -d':' -f2)
if [ -z "$USER_ID" ]; then
    USER_ID=1
fi

run_test "Get User by ID" "GET" "$API_BASE/users/$USER_ID" "" "200"
run_test "Update User" "PUT" "$API_BASE/users/$USER_ID" '{"username":"testuser","firstName":"Updated Test","lastName":"User","email":"testuser@company.com","isActive":"Y","updatedBy":"admin@docwf.com"}' "200"

# User Predicate-Based Search
test_predicate_search "users" "firstName=Test&isActive=Y&page=0&size=20" "Search Users with Predicates"
test_predicate_search "users" "isActive=Y&page=0&size=20" "Get All Users with Filtering"

# User Utility Operations
run_test "Toggle User Status" "PATCH" "$API_BASE/users/$USER_ID/status?isActive=N" "" "200"
run_test "Set User Escalation" "PATCH" "$API_BASE/users/$USER_ID/escalation?escalationToUserId=2" "" "200"
run_test "Get Escalation Hierarchy" "GET" "$API_BASE/users/$USER_ID/escalation-hierarchy" "" "200"
run_test "Check Username Availability" "GET" "$API_BASE/users/check/username/testuser" "" "200"
run_test "Check Email Availability" "GET" "$API_BASE/users/check/email/testuser@company.com" "" "200"

echo ""
echo "🔐 Testing Role Management APIs (9 endpoints)"
echo "---------------------------------------------"

# Role CRUD Operations
run_test "Create Role" "POST" "$API_BASE/roles" '{"roleName":"TEST_ROLE","isActive":"Y","createdBy":"admin@docwf.com"}' "201"

# Get role ID from response
ROLE_ID=$(cat /tmp/response.json | grep -o '"roleId":[0-9]*' | cut -d':' -f2)
if [ -z "$ROLE_ID" ]; then
    ROLE_ID=1
fi

run_test "Get Role by ID" "GET" "$API_BASE/roles/$ROLE_ID" "" "200"
run_test "Update Role" "PUT" "$API_BASE/roles/$ROLE_ID" '{"roleName":"UPDATED_TEST_ROLE","isActive":"Y","updatedBy":"admin@docwf.com"}' "200"

# Role Predicate-Based Search
test_predicate_search "roles" "roleName=TEST&isActive=Y&page=0&size=20" "Search Roles with Predicates"
test_predicate_search "roles" "isActive=Y&page=0&size=20" "Get All Roles with Filtering"

# Role Utility Operations
run_test "Toggle Role Status" "PATCH" "$API_BASE/roles/$ROLE_ID/status?isActive=N" "" "200"
run_test "Assign Role to User" "POST" "$API_BASE/roles/$ROLE_ID/assign/user/$USER_ID" "" "200"
run_test "Unassign Role from User" "DELETE" "$API_BASE/roles/$ROLE_ID/unassign/user/$USER_ID" "" "200"
run_test "Check Role Name Availability" "GET" "$API_BASE/roles/check/name/TEST_ROLE" "" "200"

echo ""
echo "🚀 Testing Workflow Execution APIs (25 endpoints)"
echo "------------------------------------------------"

# Workflow Instance Management
run_test "Start Workflow Instance" "POST" "$API_BASE/execution/workflows/$WORKFLOW_ID/start?startedByUserId=$USER_ID" "" "201"
run_test "Start Workflow with Calendar" "POST" "$API_BASE/execution/workflows/$WORKFLOW_ID/start-with-calendar?startedByUserId=$USER_ID&calendarId=$CALENDAR_ID" "" "201"

# Get instance ID from response
INSTANCE_ID=$(cat /tmp/response.json | grep -o '"instanceId":[0-9]*' | cut -d':' -f2)
if [ -z "$INSTANCE_ID" ]; then
    INSTANCE_ID=1
fi

run_test "Get Workflow Instance" "GET" "$API_BASE/execution/instances/$INSTANCE_ID" "" "200"

# Task Instance Management
run_test "Get Instance Tasks" "GET" "$API_BASE/execution/instances/$INSTANCE_ID/tasks" "" "200"

# Get instance task ID from response
INSTANCE_TASK_ID=$(cat /tmp/response.json | grep -o '"instanceTaskId":[0-9]*' | cut -d':' -f2 | head -1)
if [ -z "$INSTANCE_TASK_ID" ]; then
    INSTANCE_TASK_ID=1
fi

run_test "Assign Task to User" "POST" "$API_BASE/execution/tasks/$INSTANCE_TASK_ID/assign?userId=$USER_ID" "" "200"
run_test "Start Task" "POST" "$API_BASE/execution/tasks/$INSTANCE_TASK_ID/start" "" "200"
run_test "Complete Task" "POST" "$API_BASE/execution/tasks/$INSTANCE_TASK_ID/complete?decisionOutcome=APPROVED&completedBy=$USER_ID" "" "200"

# Workflow Execution Logic
run_test "Get Next Pending Task" "GET" "$API_BASE/execution/instances/$INSTANCE_ID/next-task" "" "200"
run_test "Execute Next Task" "POST" "$API_BASE/execution/instances/$INSTANCE_ID/execute-next" "" "200"
run_test "Get Tasks Assigned to User" "GET" "$API_BASE/execution/users/$USER_ID/tasks" "" "200"

# Calendar Integration Endpoints
run_test "Check Workflow Execution on Date" "GET" "$API_BASE/execution/workflows/$WORKFLOW_ID/calendar/$CALENDAR_ID/can-execute?date=2025-01-02" "" "200"
run_test "Get Next Valid Execution Date" "GET" "$API_BASE/execution/workflows/$WORKFLOW_ID/calendar/$CALENDAR_ID/next-valid-date?fromDate=2025-01-01" "" "200"

# Utility Endpoints
run_test "Get Overdue Tasks" "GET" "$API_BASE/execution/overdue-tasks" "" "200"
run_test "Trigger Workflow Reminders" "POST" "$API_BASE/execution/reminders/trigger" "" "200"

echo ""
echo "📁 Testing File Management APIs (8 endpoints)"
echo "---------------------------------------------"

# File Upload & Download
run_test "Upload File" "POST" "$API_BASE/files/upload" '{"instanceTaskId":1,"actionType":"UPLOAD","createdBy":"testuser"}' "201"
run_test "Get File Info" "GET" "$API_BASE/files/info/test-file.txt" "" "200"
run_test "Consolidate Files" "POST" "$API_BASE/files/consolidate" '{"instanceTaskId":1,"fileIds":"1,2,3","createdBy":"testuser"}' "201"

echo ""
echo "📊 Testing Dashboard APIs (15 endpoints)"
echo "----------------------------------------"

# Dashboard Views
run_test "Get User Dashboard" "GET" "$API_BASE/dashboard/user?userId=$USER_ID" "" "200"
run_test "Get User Tasks" "GET" "$API_BASE/dashboard/tasks?userId=$USER_ID&status=PENDING" "" "200"
run_test "Get User Workflows" "GET" "$API_BASE/dashboard/workflows?userId=$USER_ID&status=IN_PROGRESS" "" "200"
run_test "Get Process Owner Dashboard" "GET" "$API_BASE/dashboard/process-owner?processOwnerId=1" "" "200"
run_test "Get Manager Dashboard" "GET" "$API_BASE/dashboard/manager?managerId=1" "" "200"
run_test "Get Admin Dashboard" "GET" "$API_BASE/dashboard/admin?adminId=1" "" "200"

echo ""
echo "🔄 Testing Process Owner APIs (12 endpoints)"
echo "--------------------------------------------"

# Process Owner Operations
run_test "Escalate Workflow" "POST" "$API_BASE/process-owners/escalate/$INSTANCE_ID?escalatedToUserId=2&reason=Task overdue" "" "200"
run_test "Reassign Task" "POST" "$API_BASE/process-owners/tasks/$INSTANCE_TASK_ID/reassign?newUserId=2&reason=User unavailable" "" "200"
run_test "Override Task Decision" "POST" "$API_BASE/process-owners/tasks/$INSTANCE_TASK_ID/override?decision=APPROVED&reason=Manager override" "" "200"
run_test "Get Escalation Queue" "GET" "$API_BASE/process-owners/escalation-queue?processOwnerId=1" "" "200"
run_test "Get Process Owner Statistics" "GET" "$API_BASE/process-owners/stats?processOwnerId=1&startDate=2025-08-01&endDate=2025-08-20" "" "200"
run_test "Get Process Owner Team" "GET" "$API_BASE/process-owners/team?processOwnerId=1" "" "200"
run_test "Assign Workflow to Process Owner" "POST" "$API_BASE/process-owners/workflows/$WORKFLOW_ID/assign?processOwnerId=1" "" "200"

echo ""
echo "🧹 Cleanup - Deleting Test Data"
echo "--------------------------------"

# Cleanup test data
run_test "Delete Workflow Parameter" "DELETE" "$API_BASE/workflows/$WORKFLOW_ID/parameters/$PARAM_ID" "" "204"
run_test "Delete Workflow Task" "DELETE" "$API_BASE/workflows/$WORKFLOW_ID/tasks/$TASK_ID" "" "204"
run_test "Remove Role from Workflow" "DELETE" "$API_BASE/workflows/$WORKFLOW_ID/roles/1" "" "204"
run_test "Delete Calendar Day" "DELETE" "$API_BASE/calendar/days/1" "" "204"
run_test "Delete User" "DELETE" "$API_BASE/users/$USER_ID" "" "204"
run_test "Delete Role" "DELETE" "$API_BASE/roles/$ROLE_ID" "" "204"
run_test "Delete Workflow" "DELETE" "$API_BASE/workflows/$WORKFLOW_ID" "" "204"
run_test "Delete Calendar" "DELETE" "$API_BASE/calendar/$CALENDAR_ID" "" "204"

echo ""
echo "📊 Test Results Summary"
echo "======================="
echo "Total Tests: $TOTAL_TESTS"
echo -e "Passed: ${GREEN}$PASSED_TESTS${NC}"
echo -e "Failed: ${RED}$FAILED_TESTS${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "\n🎉 ${GREEN}All tests passed! Your DocWF API is working perfectly!${NC}"
    exit 0
else
    echo -e "\n❌ ${RED}Some tests failed. Please check the errors above.${NC}"
    exit 1
fi
