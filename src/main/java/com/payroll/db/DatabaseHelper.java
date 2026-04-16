package com.payroll.db;

import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;

import java.util.*;
import java.util.stream.Collectors;

public class DatabaseHelper {
    private final List<Employee> employees = new ArrayList<>();
    private final List<PayrollRecord> payrollRecords = new ArrayList<>();
    private int employeeIdCounter = 1;
    private int payrollIdCounter = 1;

    public DatabaseHelper(String dbFilePath) {
        // In-memory storage, no database file needed
    }

    public void initializeDatabase() {
        // No initialization needed for in-memory storage
    }

    public int addEmployee(Employee employee) {
        employee.setId(employeeIdCounter++);
        employees.add(employee);
        return employee.getId();
    }

    public boolean updateEmployee(Employee employee) {
        int index = employees.stream()
                .map(Employee::getId)
                .collect(Collectors.toList())
                .indexOf(employee.getId());
        if (index >= 0) {
            employees.set(index, employee);
            return true;
        }
        return false;
    }

    public boolean deleteEmployee(int id) {
        return employees.removeIf(e -> e.getId() == id);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public List<Employee> searchEmployees(String keyword, String departmentFilter) {
        return employees.stream()
                .filter(e -> keyword.isEmpty() || 
                    e.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    e.getEmployeeId().toLowerCase().contains(keyword.toLowerCase()) ||
                    e.getDesignation().toLowerCase().contains(keyword.toLowerCase()))
                .filter(e -> "All".equals(departmentFilter) || e.getDepartment().equals(departmentFilter))
                .sorted(Comparator.comparing(Employee::getName))
                .collect(Collectors.toList());
    }

    public List<String> getDepartments() {
        return employees.stream()
                .map(Employee::getDepartment)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public int addPayrollRecord(PayrollRecord record) {
        record.setId(payrollIdCounter++);
        payrollRecords.add(record);
        return record.getId();
    }

    public List<PayrollRecord> getPayrollReportByMonth(String month) {
        return payrollRecords.stream()
                .filter(r -> r.getPayrollDate().toString().startsWith(month))
                .sorted(Comparator.comparing(PayrollRecord::getEmployeeName))
                .collect(Collectors.toList());
    }
}
