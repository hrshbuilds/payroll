package com.payroll.model;

public class Employee {
    private Integer id;
    private String employeeId;
    private String name;
    private String department;
    private String designation;
    private double baseSalary;

    public Employee() {
    }

    public Employee(Integer id, String employeeId, String name, String department, String designation, double baseSalary) {
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.baseSalary = baseSalary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public String toString() {
        return employeeId + " - " + name;
    }
}
