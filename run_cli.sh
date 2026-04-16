#!/bin/bash

# Quick start script for running the Payroll Management System CLI

export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.10-ms
export PATH="$JAVA_HOME/bin:$PATH"

cd /workspaces/payroll

echo "=========================================="
echo "Payroll Management System - CLI"
echo "=========================================="
echo ""
echo "Starting application..."
echo "Login with: username 'admin', password 'admin123'"
echo ""

mvn exec:java
