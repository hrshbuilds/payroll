package com.payroll.gui;

import com.payroll.logic.PayrollManager;
import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PayrollPanel extends JPanel {
    private final PayrollManager payrollManager;
    private final JComboBox<Employee> employeeComboBox = new JComboBox<>();
    private final JTextField taxField = new JTextField("10", 8);
    private final JLabel resultLabel = new JLabel("Generate payroll to calculate salary components.");
    private final PayslipPanel payslipPanel;
    private final ReportsPanel reportsPanel;

    public PayrollPanel(PayrollManager payrollManager, PayslipPanel payslipPanel, ReportsPanel reportsPanel) {
        this.payrollManager = payrollManager;
        this.payslipPanel = payslipPanel;
        this.reportsPanel = reportsPanel;

        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("Employee:"));
        form.add(employeeComboBox);
        form.add(new JLabel("Tax Deduction (%):"));
        form.add(taxField);

        JButton calculateButton = new JButton("Calculate & Save Payroll");
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(calculateButton, BorderLayout.WEST);
        bottom.add(resultLabel, BorderLayout.CENTER);

        add(form, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);

        calculateButton.addActionListener(e -> calculatePayroll());
    }

    public void refreshEmployees() {
        employeeComboBox.removeAllItems();
        try {
            List<Employee> employees = payrollManager.getAllEmployees();
            for (Employee employee : employees) {
                employeeComboBox.addItem(employee);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load employees: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculatePayroll() {
        Employee employee = (Employee) employeeComboBox.getSelectedItem();
        try {
            double taxRate = Double.parseDouble(taxField.getText().trim());
            PayrollRecord record = payrollManager.createPayrollRecord(employee, taxRate);
            resultLabel.setText(String.format("Net pay for %s: %.2f", record.getEmployeeName(), record.getNetPay()));
            payslipPanel.showPayslip(record);
            reportsPanel.refresh();
            JOptionPane.showMessageDialog(this, "Payroll generated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tax deduction must be numeric", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save payroll: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
