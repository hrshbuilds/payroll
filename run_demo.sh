#!/bin/bash

# Demo script to show CLI functionality
export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.10-ms
export PATH="$JAVA_HOME/bin:$PATH"

cd /workspaces/payroll

echo "================================================"
echo "Payroll Management System - CLI Demo"
echo "================================================"
echo ""
echo "Building project..."
mvn clean package -q

echo ""
echo "Running application with demo commands..."
echo ""

# Create input file - login + add employee + view all + exit
cat > demo_input.txt << 'EOF'
admin
admin123
1
3
EMP001
John Doe
Engineering
Software Engineer
50000
6
2
2
3
55000
EMP002
Jane Smith
HR
HR Manager
45000
6
3

6
5
EOF

# Run the application with the demo input
mvn exec:java -q < demo_input.txt

# Clean up
rm -f demo_input.txt
