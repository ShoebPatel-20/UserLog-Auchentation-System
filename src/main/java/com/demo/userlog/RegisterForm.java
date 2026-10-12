package com.demo.userlog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    JTextField nameField, emailField, userIdField;
    JPasswordField passField, confirmPassField;
    JButton registerBtn;
    String generatedOtp;

    public RegisterForm() {
        setTitle("Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        nameField = new JTextField();
        emailField = new JTextField();
        userIdField = new JTextField();
        passField = new JPasswordField();
        confirmPassField = new JPasswordField();
        registerBtn = new JButton("Register");

        add(new JLabel("Name")); add(nameField);
        add(new JLabel("Email")); add(emailField);
        add(new JLabel("User ID")); add(userIdField);
        add(new JLabel("Password")); add(passField);
        add(new JLabel("Confirm Password")); add(confirmPassField);
        add(new JLabel("")); add(registerBtn);

        registerBtn.addActionListener(e -> handleRegister());
        setVisible(true);
    }

    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String userid = userIdField.getText();
        String pass = new String(passField.getPassword());
        String confirm = new String(confirmPassField.getPassword());

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM \"users\" WHERE \"uid\" = ?");
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "UserID already exists.");
            } else {
                generatedOtp = String.valueOf((int)((Math.random() * 900000) + 100000)); // 6-digit OTP
                EmailSender.sendOtp(email, generatedOtp);
                new OTPVerificationForm(name, email, userid, pass, generatedOtp);
                this.dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

