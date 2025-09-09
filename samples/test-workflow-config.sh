#!/bin/bash

# Test script for workflow configuration endpoints
# Make sure the application is running before executing this script

BASE_URL="http://localhost:8080"

echo "Testing Workflow Configuration Endpoints"
echo "========================================"

# Test 1: Create Workflow Configuration
echo "1. Creating workflow configuration..."
CREATE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/workflows" \
  -H "Content-Type: application/json" \
  -d @samples/workflow-config-create-request.json)

echo "Create Response: $CREATE_RESPONSE"
echo ""

# Extract workflow ID from response (assuming it returns the created workflow)
WORKFLOW_ID=$(echo $CREATE_RESPONSE | grep -o '"workflowId":[0-9]*' | cut -d':' -f2)

if [ -n "$WORKFLOW_ID" ]; then
    echo "Created workflow with ID: $WORKFLOW_ID"
    echo ""
    
    # Test 2: Get Workflow Configuration
    echo "2. Getting workflow configuration..."
    GET_RESPONSE=$(curl -s -X GET "$BASE_URL/api/workflows/$WORKFLOW_ID")
    echo "Get Response: $GET_RESPONSE"
    echo ""
    
    # Test 3: Update Workflow Configuration
    echo "3. Updating workflow configuration..."
    UPDATE_RESPONSE=$(curl -s -X PUT "$BASE_URL/api/workflows/$WORKFLOW_ID" \
      -H "Content-Type: application/json" \
      -d @samples/workflow-config-update-request.json)
    echo "Update Response: $UPDATE_RESPONSE"
    echo ""
    
    # Test 4: List All Workflows
    echo "4. Listing all workflows..."
    LIST_RESPONSE=$(curl -s -X GET "$BASE_URL/api/workflows")
    echo "List Response: $LIST_RESPONSE"
    echo ""
    
else
    echo "Failed to create workflow or extract workflow ID"
fi

echo "Test completed!"
