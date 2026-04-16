# Payroll Management System - CLI Version

This is a command-line interface (CLI) version of the Payroll Management System. All functionality has been converted from Swing GUI to interactive CLI menus.

## Prerequisites

- Java 17 or higher (tested with Java 21)
- Maven 3.9+

## How to Run

### Quick Start

```bash
cd /workspaces/payroll
mvn clean package
mvn exec:java
```

### Build Only

```bash
mvn clean package
```

This creates an executable JAR in `/target/payroll.jar`

## Features

### Authentication
- Login with admin credentials
- Default username: `admin`
- Default password: `admin123`

### Employee Management
- **View All Employees** - Display all employees in the database
- **Search Employees** - Filter by name, employee ID, or designation
- **Add Employee** - Create a new employee record
- **Update Employee** - Modify existing employee details
- **Delete Employee** - Remove an employee (with confirmation)

Fields for each employee:
- Employee ID (unique)
- Full Name
- Department
- Designation  
- Base Salary

### Payroll Calculation
- Calculate monthly payroll for an employee
- Calculate components based on base salary:
  - HRA (House Rent Allowance): 40% of base
  - DA (Dearness Allowance): 20% of base
  - PF Deduction: 12% of base
  - Tax Deduction: User-specified percentage (0-50%)
  - Net Pay: Automatically calculated
- Records are persisted in SQLite database

### View Payslips
- Retrieve and display individual employee payslips
- View specific payslip by employee ID and month
- Shows complete breakdown of salary components

### Monthly Payroll Report
- Generate comprehensive payroll report for a specific month
- Displays all employees' payroll for the requested month
- Shows totals for all salary components
- Default month is current month if not specified

## Database

The application uses SQLite for data persistence:
- Database file: `payroll.db` (created in project root on first run)
- Tables created automatically:
  - `employees` - Stores employee information
  - `payroll_records` - Stores payroll calculations

## Menu Navigation

The application uses a hierarchical menu system:

```
Main Menu
├── Employee Management
│   ├── View All Employees
│   ├── Search Employees
│   ├── Add Employee
│   ├── Update Employee
│   ├── Delete Employee
│   └── Back to Main Menu
├── Payroll Calculation
├── View Payslips
├── Monthly Payroll Report
└── Exit
```

## Example Workflow

1. Start application: `mvn exec:java`
2. Login: username `admin`, password `admin123`
3. Select "Employee Management" → "Add Employee"
4. Enter employee details (ID, name, department, designation, salary)
5. Select "Payroll Calculation" to calculate payroll
6. View reports with "Monthly Payroll Report"

## Sample Data Entry

When prompted for inputs:
- Employee ID: `EMP001`
- Name: `John Doe`
- Department: `Engineering`
- Designation: `Software Engineer`
- Base Salary: `50000`
- Tax Rate: `10` (for 10%)

## Notes

- All salary figures are in currency units (e.g., ₹ or $)
- Dates are recorded as the date of payroll generation
- Delete operations require confirmation to prevent accidental data loss
- Search is case-sensitive for partial matches
- Tax rate must be between 0 and 50 percent

## Troubleshooting

**Issue**: Application won't start
- Solution: Ensure Java 17+ is installed: `java -version`
- Solution: Run `mvn clean install` first

**Issue**: SQLite errors
- Solution: Delete `payroll.db` file and run again to reset database
- Solution: Check file permissions in project directory

**Issue**: Input not responding
- Solution: Ensure all prompts are answered with valid data
- Solution: Press Enter after each input

## Architecture

The CLI application maintains the same business logic as the GUI version:

- **Models**: `Employee`, `PayrollRecord`
- **Logic**: `PayrollCalculator`, `PayrollManager`
- **Database**: `DatabaseHelper` (SQLite)
- **CLI**: `PayrollCLI` - New interactive command-line interface

All original business logic remains unchanged; only the UI layer has been replaced.
