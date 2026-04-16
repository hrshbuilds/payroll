package com.payroll.logic;

import com.payroll.model.Employee;
import com.payroll.model.PayrollRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayrollCalculatorTest {

    @Test
    void calculateShouldApplyAllSalaryComponents() {
        Employee employee = new Employee(1, "E001", "Alice", "Engineering", "Developer", 50000);

        PayrollRecord record = PayrollCalculator.calculate(employee, 10);

        assertEquals(50000.0, record.getBasicPay());
        assertEquals(20000.0, record.getHra());
        assertEquals(10000.0, record.getDa());
        assertEquals(6000.0, record.getPfDeduction());
        assertEquals(5000.0, record.getTaxDeduction());
        assertEquals(69000.0, record.getNetPay());
    }
}
