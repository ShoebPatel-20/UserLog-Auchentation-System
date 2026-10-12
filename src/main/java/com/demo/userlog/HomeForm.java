package com.demo.userlog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.sql.*;
import java.time.LocalDateTime;

public class HomeForm extends JFrame {
    JButton loginBtn, breakStartBtn, breakEndBtn, logoutBtn;
    String userId;
    int uid;
    int currentBreakId = -1;
    int currentLoginId = -1;

    public HomeForm(String userId) {
        this.userId = userId;
        setTitle("E-Home");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        loginBtn = new JButton("Login");
        breakStartBtn = new JButton("Break Start");
        breakEndBtn = new JButton("Break End");
        logoutBtn = new JButton("Logout");

        add(loginBtn);
        add(breakStartBtn);
        add(breakEndBtn);
        add(logoutBtn);

        breakStartBtn.setEnabled(false);
        breakEndBtn.setEnabled(false);
        logoutBtn.setEnabled(false);

        loginBtn.addActionListener(e -> handleLogin());
        breakStartBtn.addActionListener(e -> startBreak());
        breakEndBtn.addActionListener(e -> endBreak());
        logoutBtn.addActionListener(e -> handleLogout());

        setVisible(true);
    }

    private void handleLogin() {

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"userlog\" (uid, ulogin, uldate) VALUES (?, ?,?) RETURNING ulsn");
            ps.setString(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentLoginId = rs.getInt("ulsn");
                loginBtn.setEnabled(false);
                breakStartBtn.setEnabled(true);
                logoutBtn.setEnabled(true);
                JOptionPane.showMessageDialog(this, "You are now logged in.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startBreak() {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"breaks\" (uid, bdate, bstart) VALUES (?, ?,?) RETURNING bsn");
            ps.setString(1, userId);
            ps.setDate(2, new Date(new java.util.Date().getTime()));
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentBreakId = rs.getInt("bsn");
                breakStartBtn.setEnabled(false);
                breakEndBtn.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Break started.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void endBreak() {
        if (currentBreakId == -1) {
            JOptionPane.showMessageDialog(this, "No break started.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE \"breaks\" SET \"bend\" = ? WHERE \"bsn\" = ?");
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, currentBreakId);
            ps.executeUpdate();
            breakEndBtn.setEnabled(false);
            breakStartBtn.setEnabled(true);
            currentBreakId = -1;
            JOptionPane.showMessageDialog(this, "Break ended.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleLogout() {


        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE \"userlog\" SET \"ulogout\" = ? WHERE \"ulsn\" = ?");
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, currentLoginId);
            ps.executeUpdate();
                loginBtn.setEnabled(true);
                breakStartBtn.setEnabled(false);
                breakEndBtn.setEnabled(false);
                logoutBtn.setEnabled(false);
                currentBreakId = -1;
                currentLoginId = -1;
            JOptionPane.showMessageDialog(this, "You have logged out.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getBid(){
        int Bid = 0;
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM \"breaks\" ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Bid = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Bid+1;
    }
}
