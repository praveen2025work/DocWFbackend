#!/bin/bash

echo "Starting DocWF Workflow Management System in Development Mode..."
echo "Using H2 in-memory database"
echo ""

# Set the active profile to dev
export SPRING_PROFILES_ACTIVE=dev

# Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo ""
echo "Application stopped."
