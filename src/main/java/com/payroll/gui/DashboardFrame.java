package com.payroll.gui;

import com.payroll.db.DatabaseHelper;
import com.payroll.logic.PayrollManager;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private static final String EMPLOYEE = "Employee";
    private static final String PAYROLL = "Payroll";
    private static final String PAYSLIP = "Payslip";
    private static final String REPORTS = "Reports";

    public DashboardFrame() {
        setTitle("Payroll Management System - Dashboard");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        PayrollManager payrollManager = new PayrollManager(new DatabaseHelper("payroll.db"));

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        PayslipPanel payslipPanel = new PayslipPanel();
        ReportsPanel reportsPanel = new ReportsPanel(payrollManager);
        PayrollPanel payrollPanel = new PayrollPanel(payrollManager, payslipPanel, reportsPanel);
        EmployeeManagementPanel employeePanel = new EmployeeManagementPanel(payrollManager, payrollPanel::refreshEmployees);

        cardPanel.add(employeePanel, EMPLOYEE);
        cardPanel.add(payrollPanel, PAYROLL);
        cardPanel.add(payslipPanel, PAYSLIP);
        cardPanel.add(reportsPanel, REPORTS);

        JPanel nav = new JPanel(new GridLayout(0, 1, 6, 6));
        nav.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        nav.add(createNavButton("Employee Management", EMPLOYEE, cardLayout, cardPanel));
        nav.add(createNavButton("Payroll Calculation", PAYROLL, cardLayout, cardPanel));
        nav.add(createNavButton("Payslip View", PAYSLIP, cardLayout, cardPanel));
        nav.add(createNavButton("Monthly Reports", REPORTS, cardLayout, cardPanel));

        add(nav, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        employeePanel.refresh();
        payrollPanel.refreshEmployees();
    }

    private JButton createNavButton(String text, String cardName, CardLayout cardLayout, JPanel cardPanel) {
        JButton button = new JButton(text);
        button.addActionListener(e -> cardLayout.show(cardPanel, cardName));
        return button;
    }
}
