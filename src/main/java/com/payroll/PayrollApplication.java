package com.payroll;

import com.payroll.gui.LoginFrame;

import javax.swing.*;

public class PayrollApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
