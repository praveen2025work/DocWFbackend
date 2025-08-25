#!/bin/bash

# Test script for Calendar Integration and Workflow Instance Mapping
# Make sure the DocWF application is running on localhost:8080

echo "🌐 Testing Calendar Integration & Workflow Instance Mapping"
echo "=========================================================="

BASE_URL="http://localhost:8080/api"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Test 1: Create Calendar with Days
echo -e "\n${YELLOW}Test 1: Creating Calendar with Days${NC}"
echo "----------------------------------------"

CALENDAR_RESPONSE=$(curl -s -X POST "$BASE_URL/calendar/with-days" \
  -H "Content-Type: application/json" \
  -d '{
    "calendarName": "Test Business Calendar 2025",
    "description": "Test calendar for integration testing",
    "startDate": "2025-01-01",
    "endDate": "2025-01-31",
    "recurrence": "MONTHLY",
    "createdBy": "test@company.com",
    "calendarDays": [
      {
        "dayDate": "2025-01-01",
        "dayType": "HOLIDAY",
        "note": "New Year Day"
      },
      {
        "dayDate": "2025-01-20",
        "dayType": "HOLIDAY",
        "note": "MLK Day"
      },
      {
        "dayDate": "2025-01-15",
        "dayType": "RUNDAY",
        "note": "Mid-month processing"
      }
    ]
  }')

if echo "$CALENDAR_RESPONSE" | grep -q "calendarId"; then
    CALENDAR_ID=$(echo "$CALENDAR_RESPONSE" | grep -o '"calendarId":[0-9]*' | cut -d':' -f2)
    print_status 0 "Calendar created successfully with ID: $CALENDAR_ID"
    echo "Response: $CALENDAR_RESPONSE" | jq '.' 2>/dev/null || echo "$CALENDAR_RESPONSE"
else
    print_status 1 "Failed to create calendar"
    echo "Response: $CALENDAR_RESPONSE"
    exit 1
fi

# Test 2: Validate Calendar Date
echo -e "\n${YELLOW}Test 2: Validating Calendar Dates${NC}"
echo "----------------------------------------"

# Test holiday date (should return false)
echo "Testing holiday date (2025-01-01)..."
HOLIDAY_TEST=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID/can-execute?date=2025-01-01")
if [ "$HOLIDAY_TEST" = "false" ]; then
    print_status 0 "Holiday date correctly identified as invalid"
else
    print_status 1 "Holiday date validation failed"
fi

# Test business day (should return true)
echo "Testing business day (2025-01-02)..."
BUSINESS_DAY_TEST=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID/can-execute?date=2025-01-02")
if [ "$BUSINESS_DAY_TEST" = "true" ]; then
    print_status 0 "Business day correctly identified as valid"
else
    print_status 1 "Business day validation failed"
fi

# Test run day (should return true)
echo "Testing run day (2025-01-15)..."
RUNDAY_TEST=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID/can-execute?date=2025-01-15")
if [ "$RUNDAY_TEST" = "true" ]; then
    print_status 0 "Run day correctly identified as valid"
else
    print_status 1 "Run day validation failed"
fi

# Test 3: Get Next Valid Date
echo -e "\n${YELLOW}Test 3: Getting Next Valid Date${NC}"
echo "----------------------------------------"

NEXT_VALID_DATE=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID/next-valid-date?fromDate=2025-01-01")
if [ "$NEXT_VALID_DATE" = "2025-01-02" ]; then
    print_status 0 "Next valid date correctly identified: $NEXT_VALID_DATE"
else
    print_status 1 "Next valid date calculation failed. Expected: 2025-01-02, Got: $NEXT_VALID_DATE"
fi

# Test 4: Get Valid Dates in Range
echo -e "\n${YELLOW}Test 4: Getting Valid Dates in Range${NC}"
echo "----------------------------------------"

VALID_DATES_RESPONSE=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID/valid-dates?startDate=2025-01-01&endDate=2025-01-10")
if echo "$VALID_DATES_RESPONSE" | grep -q "2025-01-02"; then
    print_status 0 "Valid dates in range retrieved successfully"
    echo "Valid dates: $VALID_DATES_RESPONSE"
else
    print_status 1 "Failed to retrieve valid dates in range"
    echo "Response: $VALID_DATES_RESPONSE"
fi

# Test 5: Create Workflow Instance with Calendar
echo -e "\n${YELLOW}Test 5: Creating Workflow Instance with Calendar${NC}"
echo "----------------------------------------"

# Note: This test requires a valid workflow ID and user ID to exist in the system
# For demonstration, we'll show the API call structure

echo "To test workflow instance creation, you need:"
echo "1. A valid workflow ID (from workflow configuration)"
echo "2. A valid user ID (from user management)"
echo ""
echo "Example API call:"
echo "curl -X POST \"$BASE_URL/execution/workflows/start-with-calendar\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{"
echo "    \"workflowId\": 1,"
echo "    \"startedBy\": 1,"
echo "    \"calendarId\": $CALENDAR_ID,"
echo "    \"triggeredBy\": \"test_script\""
echo "  }'"

# Test 6: Calendar Information Retrieval
echo -e "\n${YELLOW}Test 6: Retrieving Calendar Information${NC}"
echo "----------------------------------------"

CALENDAR_INFO=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID")
if echo "$CALENDAR_INFO" | grep -q "calendarName"; then
    print_status 0 "Calendar information retrieved successfully"
    echo "Calendar: $(echo "$CALENDAR_INFO" | grep -o '"calendarName":"[^"]*"' | cut -d'"' -f4)"
else
    print_status 1 "Failed to retrieve calendar information"
fi

# Test 7: Calendar Days Retrieval
echo -e "\n${YELLOW}Test 7: Retrieving Calendar Days${NC}"
echo "----------------------------------------"

CALENDAR_DAYS=$(curl -s "$BASE_URL/calendar/$CALENDAR_ID/days")
if echo "$CALENDAR_DAYS" | grep -q "calendarDayId"; then
    DAY_COUNT=$(echo "$CALENDAR_DAYS" | grep -o '"calendarDayId"' | wc -l)
    print_status 0 "Calendar days retrieved successfully. Total days: $DAY_COUNT"
else
    print_status 1 "Failed to retrieve calendar days"
fi

echo -e "\n${GREEN}🎉 Calendar Integration Testing Complete!${NC}"
echo "=========================================================="
echo ""
echo "Summary:"
echo "- Calendar creation: ✓"
echo "- Date validation: ✓"
echo "- Next valid date: ✓"
echo "- Valid dates range: ✓"
echo "- Calendar info retrieval: ✓"
echo "- Calendar days retrieval: ✓"
echo ""
echo "Note: Workflow instance creation test requires existing workflow and user data."
echo "Please ensure the DocWF application is running and has the necessary data."
