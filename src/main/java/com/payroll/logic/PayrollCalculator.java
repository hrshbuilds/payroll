package com.payroll.logic;

import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;

import java.time.LocalDate;

public final class PayrollCalculator {
    private static final double HRA_RATE = 0.40;
    private static final double DA_RATE = 0.20;
    private static final double PF_RATE = 0.12;

    private PayrollCalculator() {
    }

    public static PayrollRecord calculate(Employee employee, double taxRatePercent) {
        double basic = employee.getBaseSalary();
        double hra = basic * HRA_RATE;
        double da = basic * DA_RATE;
        double pf = basic * PF_RATE;
        double tax = basic * (taxRatePercent / 100.0);
        double net = basic + hra + da - pf - tax;

        return new PayrollRecord(
                null,
                employee.getId(),
                employee.getEmployeeId(),
                employee.getName(),
                basic,
                round(hra),
                round(da),
                round(pf),
                round(tax),
                round(net),
                LocalDate.now()
        );
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
