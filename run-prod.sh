#!/bin/bash

echo "Starting DocWF Workflow Management System in Production Mode..."
echo "Using Oracle database"
echo ""

# Set the active profile to prod
export SPRING_PROFILES_ACTIVE=prod

# Set Oracle database environment variables (customize as needed)
export DB_HOST=localhost
export DB_PORT=1521
export DB_SID=XE
export DB_USERNAME=workflow_user
export DB_PASSWORD=workflow_pass
export DB_SCHEMA=WORKFLOW_USER

# Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=prod

echo ""
echo "Application stopped."
