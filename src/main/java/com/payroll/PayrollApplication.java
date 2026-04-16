package com.payroll;

import com.payroll.db.DatabaseHelper;
import com.payroll.logic.PayrollManager;

public class PayrollApplication {
    public static void main(String[] args) {
        String dbPath = "payroll.db";
        DatabaseHelper databaseHelper = new DatabaseHelper(dbPath);
        PayrollManager payrollManager = new PayrollManager(databaseHelper);
        
        PayrollCLI cli = new PayrollCLI(payrollManager, databaseHelper);
        cli.start();
    }
}
