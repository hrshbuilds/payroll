package com.payroll.logic;

import com.payroll.db.DatabaseHelper;
import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;

import java.sql.SQLException;
import java.util.List;

public class PayrollManager {
    private final DatabaseHelper databaseHelper;

    public PayrollManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Employee addEmployee(Employee employee) throws SQLException {
        validateEmployee(employee, false);
        int id = databaseHelper.addEmployee(employee);
        employee.setId(id);
        return employee;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        validateEmployee(employee, true);
        databaseHelper.updateEmployee(employee);
    }

    public void deleteEmployee(int employeeDbId) throws SQLException {
        databaseHelper.deleteEmployee(employeeDbId);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return databaseHelper.getAllEmployees();
    }

    public List<Employee> searchEmployees(String keyword, String department) throws SQLException {
        String safeKeyword = keyword == null ? "" : keyword.trim();
        String safeDepartment = (department == null || department.isBlank()) ? "All" : department;
        return databaseHelper.searchEmployees(safeKeyword, safeDepartment);
    }

    public List<String> getDepartments() throws SQLException {
        return databaseHelper.getDepartments();
    }

    public PayrollRecord createPayrollRecord(Employee employee, double taxRatePercent) throws SQLException {
        if (employee == null || employee.getId() == null) {
            throw new IllegalArgumentException("Please select a valid employee");
        }
        if (taxRatePercent < 0 || taxRatePercent > 50) {
            throw new IllegalArgumentException("Tax rate must be between 0 and 50");
        }
        PayrollRecord record = PayrollCalculator.calculate(employee, taxRatePercent);
        int id = databaseHelper.addPayrollRecord(record);
        record.setId(id);
        return record;
    }

    public List<PayrollRecord> getPayrollReportByMonth(String month) throws SQLException {
        if (month == null || !month.matches("\\d{4}-\\d{2}")) {
            throw new IllegalArgumentException("Month must be in YYYY-MM format");
        }
        return databaseHelper.getPayrollReportByMonth(month);
    }

    private void validateEmployee(Employee employee, boolean updateOperation) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee is required");
        }
        if (updateOperation && employee.getId() == null) {
            throw new IllegalArgumentException("Employee id is required for update");
        }
        if (isBlank(employee.getEmployeeId()) || isBlank(employee.getName()) || isBlank(employee.getDepartment()) || isBlank(employee.getDesignation())) {
            throw new IllegalArgumentException("All employee fields are required");
        }
        if (employee.getBaseSalary() <= 0) {
            throw new IllegalArgumentException("Base salary must be greater than zero");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
