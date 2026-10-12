package com.demo.userlog;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Date;
import java.util.UUID;

public class OTPVerificationForm extends JFrame {
    JTextField otpField;
    JButton verifyBtn;
    String name, email, userid, pass, generatedOtp;

    public OTPVerificationForm(String name, String email, String userid, String pass, String otp) {
        this.name = name;
        this.email = email;
        this.userid = userid;
        this.pass = pass;
        this.generatedOtp = otp;

        setTitle("Enter OTP");
        setSize(300, 150);
        setLayout(new GridLayout(2, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        otpField = new JTextField();
        verifyBtn = new JButton("Verify OTP");

        add(new JLabel("Enter OTP:")); add(otpField);
        add(new JLabel("")); add(verifyBtn);

        verifyBtn.addActionListener(e -> verifyOtp());
        setVisible(true);
    }

    private void verifyOtp() {
        if (otpField.getText().equals(generatedOtp)) {
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users (uid, uname, umail, upass, urole, uregdate) VALUES (?, ?, ?, ?, 'E',?)");
                ps.setString(1, userid);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setString(4, pass);
                ps.setDate(5, new Date(new java.util.Date().getTime()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful.");
                new LoginForm();
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid OTP.");
        }
    }

    public Date getdate(){
        java.util.Date utilDate = new java.util.Date();
        Date current = new Date(utilDate.getTime());
        //(java.util.Date()).getTime()
        return current;
    }

    public int getId(){
        //String uuid = UUID.randomUUID()
        return 3;//(int) Math.random()*100;
    }
}

