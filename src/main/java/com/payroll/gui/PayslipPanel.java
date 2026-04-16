package com.payroll.gui;

import com.payroll.model.PayrollRecord;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PayslipPanel extends JPanel {
    private final JTextArea payslipArea = new JTextArea();

    public PayslipPanel() {
        setLayout(new BorderLayout(10, 10));
        payslipArea.setEditable(false);
        payslipArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        add(new JScrollPane(payslipArea), BorderLayout.CENTER);
        payslipArea.setText("Generate payroll to view payslip details.");
    }

    public void showPayslip(PayrollRecord record) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        payslipArea.setText(String.format(
                "PAYSLIP\n" +
                        "----------------------------------\n" +
                        "Date: %s\n" +
                        "Employee ID: %s\n" +
                        "Employee Name: %s\n\n" +
                        "Basic Pay: %.2f\n" +
                        "HRA (40%%): %.2f\n" +
                        "DA (20%%): %.2f\n" +
                        "PF Deduction (12%%): %.2f\n" +
                        "Tax Deduction: %.2f\n" +
                        "----------------------------------\n" +
                        "Net Pay: %.2f\n",
                record.getPayrollDate().format(formatter),
                record.getEmployeeId(),
                record.getEmployeeName(),
                record.getBasicPay(),
                record.getHra(),
                record.getDa(),
                record.getPfDeduction(),
                record.getTaxDeduction(),
                record.getNetPay()
        ));
    }
}
