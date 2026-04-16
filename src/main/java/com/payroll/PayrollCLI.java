package com.payroll;

import com.payroll.db.DatabaseHelper;
import com.payroll.logic.PayrollManager;
import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class PayrollCLI {
    private final Scanner scanner;
    private final PayrollManager payrollManager;
    private final DatabaseHelper databaseHelper;
    private boolean isAuthenticated = false;

    public PayrollCLI(PayrollManager payrollManager, DatabaseHelper databaseHelper) {
        this.scanner = new Scanner(System.in);
        this.payrollManager = payrollManager;
        this.databaseHelper = databaseHelper;
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("  Payroll Management System - CLI");
        System.out.println("========================================\n");

        if (!login()) {
            System.out.println("Login failed. Exiting.");
            return;
        }

        mainMenu();
    }

    private boolean login() {
        System.out.println("--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if ("admin".equals(username) && "admin123".equals(password)) {
            isAuthenticated = true;
            System.out.println("\n✓ Login successful!\n");
            return true;
        } else {
            System.out.println("\n✗ Invalid login credentials.\n");
            return false;
        }
    }

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("========== MAIN MENU ==========");
            System.out.println("1. Employee Management");
            System.out.println("2. Payroll Calculation");
            System.out.println("3. View Payslips");
            System.out.println("4. Monthly Payroll Report");
            System.out.println("5. Exit");
            System.out.print("Select option (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    employeeManagementMenu();
                    break;
                case "2":
                    payrollCalculationMenu();
                    break;
                case "3":
                    viewPayslipsMenu();
                    break;
                case "4":
                    monthlyReportMenu();
                    break;
                case "5":
                    System.out.println("\nThank you for using Payroll Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("✗ Invalid option. Please try again.\n");
            }
        }
    }

    private void employeeManagementMenu() {
        boolean inEmployeeMenu = true;
        while (inEmployeeMenu) {
            System.out.println("\n========== EMPLOYEE MANAGEMENT ==========");
            System.out.println("1. View All Employees");
            System.out.println("2. Search Employees");
            System.out.println("3. Add Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select option (1-6): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllEmployees();
                    break;
                case "2":
                    searchEmployees();
                    break;
                case "3":
                    addEmployee();
                    break;
                case "4":
                    updateEmployee();
                    break;
                case "5":
                    deleteEmployee();
                    break;
                case "6":
                    inEmployeeMenu = false;
                    break;
                default:
                    System.out.println("✗ Invalid option. Please try again.");
            }
        }
    }

    private void viewAllEmployees() {
        List<Employee> employees = payrollManager.getAllEmployees();
        displayEmployeeList(employees);
    }

    private void displayEmployeeList(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNo employees found.\n");
            return;
        }

        System.out.println("\n--- Employee List ---");
        System.out.printf("%-5s %-12s %-20s %-15s %-15s %-12s%n", "ID", "Emp ID", "Name", "Department", "Designation", "Salary");
        System.out.println("-".repeat(100));

        for (Employee emp : employees) {
            System.out.printf("%-5d %-12s %-20s %-15s %-15s %-12.2f%n",
                    emp.getId(), emp.getEmployeeId(), emp.getName(),
                    emp.getDepartment(), emp.getDesignation(), emp.getBaseSalary());
        }
        System.out.println();
    }

    private void searchEmployees() {
        System.out.print("Enter search keyword (name/ID/designation): ");
        String keyword = scanner.nextLine().trim();

        List<Employee> employees = payrollManager.searchEmployees(keyword, "All");
        displayEmployeeList(employees);
    }

    private void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        System.out.print("Employee ID: ");
        String empId = scanner.nextLine().trim();

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Department: ");
        String department = scanner.nextLine().trim();

        System.out.print("Designation: ");
        String designation = scanner.nextLine().trim();

        System.out.print("Base Salary: ");
        double baseSalary;
        try {
            baseSalary = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid salary format.");
            return;
        }

        try {
            Employee employee = new Employee(null, empId, name, department, designation, baseSalary);
            payrollManager.addEmployee(employee);
            System.out.println("\n✓ Employee added successfully!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error adding employee: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        System.out.print("Enter Employee ID to update: ");
        String empId = scanner.nextLine().trim();

        List<Employee> employees = payrollManager.searchEmployees(empId, "All");
        if (employees.isEmpty()) {
            System.out.println("✗ Employee not found.");
            return;
        }

        Employee emp = employees.get(0);
        System.out.println("\nCurrent details:");
        System.out.printf("ID: %d, Name: %s, Department: %s, Designation: %s, Salary: %.2f%n",
                emp.getId(), emp.getName(), emp.getDepartment(), emp.getDesignation(), emp.getBaseSalary());

        System.out.println("\nEnter new details (or press Enter to keep current):");
        System.out.print("Name [" + emp.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) emp.setName(name);

        System.out.print("Department [" + emp.getDepartment() + "]: ");
        String department = scanner.nextLine().trim();
        if (!department.isEmpty()) emp.setDepartment(department);

        System.out.print("Designation [" + emp.getDesignation() + "]: ");
        String designation = scanner.nextLine().trim();
        if (!designation.isEmpty()) emp.setDesignation(designation);

        System.out.print("Base Salary [" + emp.getBaseSalary() + "]: ");
        String salaryStr = scanner.nextLine().trim();
        if (!salaryStr.isEmpty()) {
            try {
                emp.setBaseSalary(Double.parseDouble(salaryStr));
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid salary format.");
                return;
            }
        }

        try {
            payrollManager.updateEmployee(emp);
            System.out.println("\n✓ Employee updated successfully!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error updating employee: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        String empId = scanner.nextLine().trim();

        List<Employee> employees = payrollManager.searchEmployees(empId, "All");
        if (employees.isEmpty()) {
            System.out.println("✗ Employee not found.");
            return;
        }

        Employee emp = employees.get(0);
        System.out.printf("Are you sure you want to delete %s? (yes/no): ", emp.getName());
        String confirm = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(confirm)) {
            payrollManager.deleteEmployee(emp.getId());
            System.out.println("✓ Employee deleted successfully!\n");
        } else {
            System.out.println("Deletion cancelled.\n");
        }
    }

    private void payrollCalculationMenu() {
        System.out.println("\n========== PAYROLL CALCULATION ==========");

        List<Employee> employees = payrollManager.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found. Please add employees first.\n");
            return;
        }

        displayEmployeeList(employees);

        System.out.print("Enter Employee ID (or employee number): ");
        String empId = scanner.nextLine().trim();

        List<Employee> searchResults = payrollManager.searchEmployees(empId, "All");
        if (searchResults.isEmpty()) {
            System.out.println("✗ Employee not found.\n");
            return;
        }

        Employee employee = searchResults.get(0);

        System.out.print("Enter Tax Rate Percentage (0-50): ");
        double taxRate;
        try {
            taxRate = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid tax rate format.");
            return;
        }

        try {
            PayrollRecord record = payrollManager.createPayrollRecord(employee, taxRate);

            System.out.println("\n========== PAYROLL RECORD CREATED ==========");
            displayPayrollRecord(record);
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void displayPayrollRecord(PayrollRecord record) {
        System.out.printf("Employee ID: %s%n", record.getEmployeeId());
        System.out.printf("Employee Name: %s%n", record.getEmployeeName());
        System.out.printf("Basic Pay: %.2f%n", record.getBasicPay());
        System.out.printf("HRA (40%%): %.2f%n", record.getHra());
        System.out.printf("DA (20%%): %.2f%n", record.getDa());
        System.out.printf("PF Deduction (12%%): %.2f%n", record.getPfDeduction());
        System.out.printf("Tax Deduction: %.2f%n", record.getTaxDeduction());
        System.out.printf("Net Pay: %.2f%n", record.getNetPay());
        System.out.printf("Payroll Date: %s%n", record.getPayrollDate());
    }

    private void viewPayslipsMenu() {
        System.out.println("\n========== VIEW PAYSLIPS ==========");
        System.out.print("Enter Employee ID: ");
        String empId = scanner.nextLine().trim();

        System.out.print("Enter Month (YYYY-MM) [or press Enter for current month]: ");
        String monthStr = scanner.nextLine().trim();

        if (monthStr.isEmpty()) {
            YearMonth now = YearMonth.now();
            monthStr = now.toString();
        }

        try {
            List<PayrollRecord> records = payrollManager.getPayrollReportByMonth(monthStr);

            if (records.isEmpty()) {
                System.out.println("\nNo payroll records found for " + monthStr + "\n");
                return;
            }

            // Filter by employee ID
            PayrollRecord empRecord = records.stream()
                    .filter(r -> r.getEmployeeId().equals(empId))
                    .findFirst()
                    .orElse(null);

            if (empRecord == null) {
                System.out.println("\nNo payslip found for employee " + empId + " in month " + monthStr + "\n");
                return;
            }

            System.out.println("\n========== PAYSLIP ==========");
            displayPayrollRecord(empRecord);
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void monthlyReportMenu() {
        System.out.println("\n========== MONTHLY PAYROLL REPORT ==========");
        System.out.print("Enter Month (YYYY-MM) [or press Enter for current month]: ");
        String monthStr = scanner.nextLine().trim();

        if (monthStr.isEmpty()) {
            YearMonth now = YearMonth.now();
            monthStr = now.toString();
        }

        try {
            List<PayrollRecord> records = payrollManager.getPayrollReportByMonth(monthStr);

            if (records.isEmpty()) {
                System.out.println("\nNo payroll records found for " + monthStr + "\n");
                return;
            }

            System.out.println("\n========== PAYROLL REPORT FOR " + monthStr + " ==========");
            System.out.printf("%-12s %-20s %-12s %-10s %-10s %-10s %-10s %-10s %-12s%n",
                    "Employee ID", "Name", "Basic Pay", "HRA", "DA", "PF", "Tax", "Net Pay", "Date");
            System.out.println("-".repeat(140));

            double totalBasic = 0, totalHra = 0, totalDa = 0, totalPf = 0, totalTax = 0, totalNet = 0;

            for (PayrollRecord record : records) {
                System.out.printf("%-12s %-20s %-12.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-12s%n",
                        record.getEmployeeId(), record.getEmployeeName(), record.getBasicPay(),
                        record.getHra(), record.getDa(), record.getPfDeduction(),
                        record.getTaxDeduction(), record.getNetPay(), record.getPayrollDate());

                totalBasic += record.getBasicPay();
                totalHra += record.getHra();
                totalDa += record.getDa();
                totalPf += record.getPfDeduction();
                totalTax += record.getTaxDeduction();
                totalNet += record.getNetPay();
            }

            System.out.println("-".repeat(140));
            System.out.printf("%-12s %-20s %-12.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f%n",
                    "TOTAL", "", totalBasic, totalHra, totalDa, totalPf, totalTax, totalNet);
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
}
