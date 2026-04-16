# Payroll Management System

Java payroll management application with CLI and Swing GUI interfaces using Java 11+ and in-memory storage.

## Features

- **Authentication**: Login screen with credentials (`admin` / `admin123`)
- **Employee Management**: Add, edit, delete, search, and filter employees by department
- **Payroll Calculation**: Calculate salary with automatic components:
  - HRA = 40% of base salary
  - DA = 20% of base salary
  - PF deduction = 12% of base salary
  - Tax deduction (user-configurable percentage, 0-50%)
  - Net pay auto-calculation
- **Payslip Viewing**: View individual payslips by employee and month
- **Monthly Reports**: Generate and view payroll reports for any month
- **In-Memory Storage**: All data is stored in memory during runtime (no database files)

## Project Structure

- `com.payroll.model` - Employee and PayrollRecord data models
- `com.payroll.logic` - PayrollCalculator and PayrollManager business logic
- `com.payroll.db` - In-memory data storage
- `com.payroll.gui` - Swing desktop UI components
- `com.payroll` - CLI interface and main application entry points

## Requirements

- Java 11 or higher
- Maven 3.6+ (optional - can compile manually with `javac`)

## Compile

### Using Maven
```bash
mvn clean compile
```

### Using javac directly
```bash
javac -d target/classes $(find src/main/java -name "*.java")
```

## Run

### CLI Interface (recommended)
```bash
java -cp target/classes com.payroll.PayrollApplication
```

### Using Maven
```bash
mvn exec:java
```

### GUI Interface (Swing)
```bash
java -cp target/classes com.payroll.gui.DashboardFrame
```

## Test

```bash
mvn test
```

## Notes

- All employee and payroll data is stored in memory and will be cleared when the application exits
- The CLI interface is fully functional with customer login and complete payroll management features
- The GUI interface provides a visual alternative to the CLI
