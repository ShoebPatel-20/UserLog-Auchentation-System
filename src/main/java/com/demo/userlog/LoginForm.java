package com.demo.userlog;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField userIdField;
    JPasswordField passwordField;
    JButton loginBtn;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userIdField = new JTextField();
        passwordField = new JPasswordField();
        loginBtn = new JButton("Login");

        add(new JLabel("User ID")); add(userIdField);
        add(new JLabel("Password")); add(passwordField);
        add(new JLabel("")); add(loginBtn);

        loginBtn.addActionListener(e -> login());
        setVisible(true);
    }

    private void login() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE uid = ? AND upass = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userIdField.getText());
            ps.setString(2, new String(passwordField.getPassword()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                String role = rs.getString("urole");
                if(role.equals("A")){
                    new AdminHomeForm();
                }else {
                    new HomeForm(userIdField.getText());
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
