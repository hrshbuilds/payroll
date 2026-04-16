package com.payroll.db;

import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private final String url;

    public DatabaseHelper(String dbFilePath) {
        this.url = "jdbc:sqlite:" + dbFilePath;
        initializeDatabase();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void initializeDatabase() {
        String employeeTable = """
                CREATE TABLE IF NOT EXISTS employees (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    employee_id TEXT NOT NULL UNIQUE,
                    name TEXT NOT NULL,
                    department TEXT NOT NULL,
                    designation TEXT NOT NULL,
                    base_salary REAL NOT NULL CHECK(base_salary >= 0)
                )
                """;
        String payrollTable = """
                CREATE TABLE IF NOT EXISTS payroll_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    employee_db_id INTEGER NOT NULL,
                    employee_id TEXT NOT NULL,
                    employee_name TEXT NOT NULL,
                    basic_pay REAL NOT NULL,
                    hra REAL NOT NULL,
                    da REAL NOT NULL,
                    pf_deduction REAL NOT NULL,
                    tax_deduction REAL NOT NULL,
                    net_pay REAL NOT NULL,
                    payroll_date TEXT NOT NULL,
                    FOREIGN KEY(employee_db_id) REFERENCES employees(id) ON DELETE CASCADE
                )
                """;
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            statement.execute(employeeTable);
            statement.execute(payrollTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize database", e);
        }
    }

    public int addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees(employee_id,name,department,designation,base_salary) VALUES(?,?,?,?,?)";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getEmployeeId());
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getDepartment());
            statement.setString(4, employee.getDesignation());
            statement.setDouble(5, employee.getBaseSalary());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Unable to fetch generated employee id");
        }
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET employee_id=?, name=?, department=?, designation=?, base_salary=? WHERE id=?";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.getEmployeeId());
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getDepartment());
            statement.setString(4, employee.getDesignation());
            statement.setDouble(5, employee.getBaseSalary());
            statement.setInt(6, employee.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT id,employee_id,name,department,designation,base_salary FROM employees ORDER BY name";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        }
        return employees;
    }

    public List<Employee> searchEmployees(String keyword, String departmentFilter) throws SQLException {
        String sql = """
                SELECT id,employee_id,name,department,designation,base_salary
                FROM employees
                WHERE (? = '' OR name LIKE ? OR employee_id LIKE ? OR designation LIKE ?)
                AND (? = 'All' OR department = ?)
                ORDER BY name
                """;
        List<Employee> employees = new ArrayList<>();
        String likeKeyword = "%" + keyword + "%";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, keyword);
            statement.setString(2, likeKeyword);
            statement.setString(3, likeKeyword);
            statement.setString(4, likeKeyword);
            statement.setString(5, departmentFilter);
            statement.setString(6, departmentFilter);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    public List<String> getDepartments() throws SQLException {
        String sql = "SELECT DISTINCT department FROM employees ORDER BY department";
        List<String> departments = new ArrayList<>();
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                departments.add(rs.getString(1));
            }
        }
        return departments;
    }

    public int addPayrollRecord(PayrollRecord record) throws SQLException {
        String sql = """
                INSERT INTO payroll_records(employee_db_id,employee_id,employee_name,basic_pay,hra,da,pf_deduction,tax_deduction,net_pay,payroll_date)
                VALUES(?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, record.getEmployeeDbId());
            statement.setString(2, record.getEmployeeId());
            statement.setString(3, record.getEmployeeName());
            statement.setDouble(4, record.getBasicPay());
            statement.setDouble(5, record.getHra());
            statement.setDouble(6, record.getDa());
            statement.setDouble(7, record.getPfDeduction());
            statement.setDouble(8, record.getTaxDeduction());
            statement.setDouble(9, record.getNetPay());
            statement.setString(10, record.getPayrollDate().toString());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Unable to fetch generated payroll record id");
        }
    }

    public List<PayrollRecord> getPayrollReportByMonth(String month) throws SQLException {
        String sql = """
                SELECT id,employee_db_id,employee_id,employee_name,basic_pay,hra,da,pf_deduction,tax_deduction,net_pay,payroll_date
                FROM payroll_records
                WHERE substr(payroll_date, 1, 7) = ?
                ORDER BY employee_name
                """;
        List<PayrollRecord> records = new ArrayList<>();
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, month);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    records.add(mapPayroll(rs));
                }
            }
        }
        return records;
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("designation"),
                rs.getDouble("base_salary")
        );
    }

    private PayrollRecord mapPayroll(ResultSet rs) throws SQLException {
        return new PayrollRecord(
                rs.getInt("id"),
                rs.getInt("employee_db_id"),
                rs.getString("employee_id"),
                rs.getString("employee_name"),
                rs.getDouble("basic_pay"),
                rs.getDouble("hra"),
                rs.getDouble("da"),
                rs.getDouble("pf_deduction"),
                rs.getDouble("tax_deduction"),
                rs.getDouble("net_pay"),
                LocalDate.parse(rs.getString("payroll_date"))
        );
    }
}
