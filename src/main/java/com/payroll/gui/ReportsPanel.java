package com.payroll.gui;

import com.payroll.logic.PayrollManager;
import com.payroll.model.PayrollRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportsPanel extends JPanel {
    private final PayrollManager payrollManager;
    private final JTextField monthField = new JTextField(10);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Emp ID", "Name", "Basic", "HRA", "DA", "PF", "Tax", "Net", "Date"}, 0
    );

    public ReportsPanel(PayrollManager payrollManager) {
        this.payrollManager = payrollManager;
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        JButton loadButton = new JButton("Load Monthly Report");
        top.add(new JLabel("Month (YYYY-MM):"));
        top.add(monthField);
        top.add(loadButton);

        JTable table = new JTable(tableModel);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadButton.addActionListener(e -> refresh());
    }

    public void refresh() {
        tableModel.setRowCount(0);
        try {
            List<PayrollRecord> records = payrollManager.getPayrollReportByMonth(monthField.getText().trim());
            for (PayrollRecord record : records) {
                tableModel.addRow(new Object[]{
                        record.getEmployeeId(), record.getEmployeeName(),
                        record.getBasicPay(), record.getHra(), record.getDa(),
                        record.getPfDeduction(), record.getTaxDeduction(),
                        record.getNetPay(), record.getPayrollDate()
                });
            }
            JOptionPane.showMessageDialog(this, "Loaded " + records.size() + " payroll record(s)", "Report", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
