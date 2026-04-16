package com.payroll.model;

import java.time.LocalDate;

public class PayrollRecord {
    private Integer id;
    private int employeeDbId;
    private String employeeId;
    private String employeeName;
    private double basicPay;
    private double hra;
    private double da;
    private double pfDeduction;
    private double taxDeduction;
    private double netPay;
    private LocalDate payrollDate;

    public PayrollRecord() {
    }

    public PayrollRecord(Integer id, int employeeDbId, String employeeId, String employeeName, double basicPay, double hra,
                         double da, double pfDeduction, double taxDeduction, double netPay, LocalDate payrollDate) {
        this.id = id;
        this.employeeDbId = employeeDbId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.basicPay = basicPay;
        this.hra = hra;
        this.da = da;
        this.pfDeduction = pfDeduction;
        this.taxDeduction = taxDeduction;
        this.netPay = netPay;
        this.payrollDate = payrollDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEmployeeDbId() {
        return employeeDbId;
    }

    public void setEmployeeDbId(int employeeDbId) {
        this.employeeDbId = employeeDbId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getBasicPay() {
        return basicPay;
    }

    public void setBasicPay(double basicPay) {
        this.basicPay = basicPay;
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
    }

    public double getDa() {
        return da;
    }

    public void setDa(double da) {
        this.da = da;
    }

    public double getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(double pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public LocalDate getPayrollDate() {
        return payrollDate;
    }

    public void setPayrollDate(LocalDate payrollDate) {
        this.payrollDate = payrollDate;
    }
}
