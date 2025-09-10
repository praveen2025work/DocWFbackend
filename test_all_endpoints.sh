#!/bin/bash

echo "🚀 DocWF Endpoint Validation Script"
echo "=================================="

BASE_URL="http://localhost:8080"

# Test Swagger endpoints
echo ""
echo "🔍 Testing Swagger Configuration"
echo "--------------------------------"

echo -n "Testing OpenAPI docs... "
if curl -s "$BASE_URL/api-docs" | grep -q "openapi"; then
    echo "✓"
else
    echo "✗"
fi

echo -n "Testing Swagger UI... "
if curl -s "$BASE_URL/swagger-ui.html" | grep -q "swagger"; then
    echo "✓"
else
    echo "✗"
fi

# Test core endpoints
echo ""
echo "🔧 Testing Core Endpoints"
echo "----------------------------"

echo -n "Testing GET /api/users... "
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/users")
echo "($response)"

echo -n "Testing GET /api/roles... "
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/roles")
echo "($response)"

echo -n "Testing GET /api/workflows... "
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/workflows")
echo "($response)"

# Test calendar endpoints
echo ""
echo "📅 Testing Calendar Endpoints"
echo "--------------------------------"

echo -n "Testing GET /api/calendars... "
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/calendars")
echo "($response)"

echo -n "Testing GET /api/calendar/1/can-execute... "
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/calendar/1/can-execute")
echo "($response)"

echo ""
echo "✅ Endpoint validation completed!"
echo ""
echo "📋 Summary:"
echo "- Swagger UI: http://localhost:8080/swagger-ui.html"
echo "- OpenAPI Docs: http://localhost:8080/api-docs"
