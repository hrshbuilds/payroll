# Payroll Management System

Java Swing payroll management application using Java 17, Maven, and SQLite.

## Features

- Login screen (`admin` / `admin123`)
- Dashboard navigation for:
  - Employee management (add/edit/delete/search/filter)
  - Payroll calculation
  - Payslip viewing
  - Monthly payroll reporting
- Salary calculation with:
  - HRA = 40% of base
  - DA = 20% of base
  - PF deduction = 12% of base
  - Tax deduction (user-entered percentage)
  - Net pay auto-calculation
- SQLite persistence through `DatabaseHelper`

## Project Structure

- `com.payroll.model` - Employee and PayrollRecord models
- `com.payroll.logic` - PayrollCalculator and PayrollManager
- `com.payroll.db` - SQLite CRUD operations
- `com.payroll.gui` - Swing UI screens and panels

## Run

```bash
mvn clean test
mvn exec:java
```

The SQLite database file is created as `payroll.db` in the project root when the app starts.
