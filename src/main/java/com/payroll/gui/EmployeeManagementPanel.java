package com.payroll.gui;

import com.payroll.logic.PayrollManager;
import com.payroll.model.Employee;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeManagementPanel extends JPanel {
    private final PayrollManager payrollManager;
    private final Runnable onEmployeesChanged;

    private final JTextField employeeIdField = new JTextField(12);
    private final JTextField nameField = new JTextField(16);
    private final JTextField departmentField = new JTextField(16);
    private final JTextField designationField = new JTextField(16);
    private final JTextField baseSalaryField = new JTextField(12);
    private final JTextField searchField = new JTextField(16);
    private final JComboBox<String> departmentFilter = new JComboBox<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"DB ID", "Employee ID", "Name", "Department", "Designation", "Base Salary"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);
    private Integer selectedDbId;

    public EmployeeManagementPanel(PayrollManager payrollManager, Runnable onEmployeesChanged) {
        this.payrollManager = payrollManager;
        this.onEmployeesChanged = onEmployeesChanged;
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 4, 10, 10));
        form.add(new JLabel("Employee ID"));
        form.add(employeeIdField);
        form.add(new JLabel("Name"));
        form.add(nameField);
        form.add(new JLabel("Department"));
        form.add(departmentField);
        form.add(new JLabel("Designation"));
        form.add(designationField);
        form.add(new JLabel("Base Salary"));
        form.add(baseSalaryField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(addButton);
        actions.add(updateButton);
        actions.add(deleteButton);
        actions.add(clearButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        departmentFilter.addItem("All");
        JButton searchButton = new JButton("Search/Filter");
        searchPanel.add(new JLabel("Search"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Department"));
        searchPanel.add(departmentFilter);
        searchPanel.add(searchButton);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(form, BorderLayout.NORTH);
        top.add(actions, BorderLayout.CENTER);
        top.add(searchPanel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> addEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> loadEmployees(searchField.getText().trim()));
        table.getSelectionModel().addListSelectionListener(this::onRowSelected);
    }

    public void refresh() {
        loadDepartmentFilter();
        loadEmployees("");
    }

    private void addEmployee() {
        try {
            Employee employee = buildEmployeeFromForm(null);
            payrollManager.addEmployee(employee);
            JOptionPane.showMessageDialog(this, "Employee added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAfterMutation();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee() {
        try {
            if (selectedDbId == null) {
                throw new IllegalArgumentException("Please select an employee to edit");
            }
            Employee employee = buildEmployeeFromForm(selectedDbId);
            payrollManager.updateEmployee(employee);
            JOptionPane.showMessageDialog(this, "Employee updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAfterMutation();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        try {
            if (selectedDbId == null) {
                throw new IllegalArgumentException("Please select an employee to delete");
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete selected employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            payrollManager.deleteEmployee(selectedDbId);
            JOptionPane.showMessageDialog(this, "Employee deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAfterMutation();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAfterMutation() {
        clearForm();
        refresh();
        onEmployeesChanged.run();
    }

    private Employee buildEmployeeFromForm(Integer dbId) {
        String employeeId = employeeIdField.getText().trim();
        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String designation = designationField.getText().trim();
        String salaryText = baseSalaryField.getText().trim();

        if (employeeId.isBlank() || name.isBlank() || department.isBlank() || designation.isBlank() || salaryText.isBlank()) {
            throw new IllegalArgumentException("All employee fields are required");
        }

        double baseSalary;
        try {
            baseSalary = Double.parseDouble(salaryText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Base salary must be numeric");
        }

        if (baseSalary <= 0) {
            throw new IllegalArgumentException("Base salary must be greater than zero");
        }

        return new Employee(dbId, employeeId, name, department, designation, baseSalary);
    }

    private void loadEmployees(String keyword) {
        tableModel.setRowCount(0);
        String department = (String) departmentFilter.getSelectedItem();
        List<Employee> employees = payrollManager.searchEmployees(keyword, department);
        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getId(),
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getDepartment(),
                    employee.getDesignation(),
                    employee.getBaseSalary()
            });
        }
    }

    private void loadDepartmentFilter() {
        String previous = (String) departmentFilter.getSelectedItem();
        departmentFilter.removeAllItems();
        departmentFilter.addItem("All");
        for (String department : payrollManager.getDepartments()) {
            departmentFilter.addItem(department);
        }
        if (previous != null) {
            departmentFilter.setSelectedItem(previous);
        }
    }

    private void onRowSelected(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }
        selectedDbId = (Integer) tableModel.getValueAt(row, 0);
        employeeIdField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        nameField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        departmentField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        designationField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        baseSalaryField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
    }

    private void clearForm() {
        selectedDbId = null;
        employeeIdField.setText("");
        nameField.setText("");
        departmentField.setText("");
        designationField.setText("");
        baseSalaryField.setText("");
        table.clearSelection();
    }
}
