package com.demo.userlog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Vector;

public class SingleDayView extends JFrame {
    JComboBox<String> userDropdown;
    JTextField dateField;
    JButton loadBtn;
    JLabel userInfo, loginLogout, totalLabel;
    JTable breakTable;

    String thrs="";
    String thrswb="";

    public SingleDayView() {
        setTitle("Single Day View");
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        userDropdown = new JComboBox<>();
        dateField = new JTextField(10);
        loadBtn = new JButton("Load");

        loadUsers();

        topPanel.add(new JLabel("User:"));
        topPanel.add(userDropdown);
        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(dateField);
        topPanel.add(loadBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        userInfo = new JLabel();
        loginLogout = new JLabel();
        totalLabel = new JLabel();
        infoPanel.add(userInfo);
        infoPanel.add(loginLogout);
        infoPanel.add(totalLabel);
        add(infoPanel, BorderLayout.SOUTH);

        breakTable = new JTable();
        add(new JScrollPane(breakTable), BorderLayout.CENTER);

        loadBtn.addActionListener(e -> loadData());

        setVisible(true);
    }

    private void loadUsers() {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT uid, uname FROM users WHERE urole = 'E'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userDropdown.addItem(rs.getString("uid") + " - " + rs.getString("uname"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        String selected = (String) userDropdown.getSelectedItem();
        if (selected == null || dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a user and enter a date.");
            return;
        }

        String uid = selected.split(" - ")[0];
        String date = dateField.getText().trim();
        userInfo.setText("User: " + selected);

        try (Connection con = DBConnection.getConnection()) {
            // Login/Logout time
            PreparedStatement ps1 = con.prepareStatement("SELECT ulogin, ulogout FROM userlog WHERE uid=? AND uldate=?");
            ps1.setString(1, uid);
            ps1.setDate(2, Date.valueOf(date));
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                //double login_duration = rs1.getTime("ulogout") - rs1.getTime("ulogin");
                //String positive = String.format("%d:%02d:%02d",absSeconds / 3600,(absSeconds % 3600) / 60,absSeconds % 60);
                //thrs = DurationFormatUtils.formatDuration(millis, "**H:mm:ss**", true);
                //Duration login_duration = Duration.between(rs1.getTime("ulogin"), rs1.getTime("ulogout"));
                //long timeDiffLong = rs1.getTime("ulogout") - rs1.getTime("ulogin");
                loginLogout.setText("Login: " + rs1.getTime("ulogin") + " | Logout: " + rs1.getTime("ulogout") );
            } else {
                loginLogout.setText("No login data found.");
            }

            // Breaks table
            PreparedStatement ps2 = con.prepareStatement("SELECT bstart, bend, EXTRACT(EPOCH FROM (bend - bstart))/60 AS duration FROM breaks WHERE uid=? AND bdate=?");
            ps2.setString(1, uid);
            ps2.setDate(2, Date.valueOf(date));
            ResultSet rs2 = ps2.executeQuery();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[] {"Break Start", "Break End", "Duration (min)"});
            double total = 0;
            while (rs2.next()) {
                Time start = rs2.getTime("bstart");
                Time end = rs2.getTime("bend");
                double duration = rs2.getDouble("duration");
                total += duration;
                model.addRow(new Object[] {start, end, String.format("%.2f", duration)});
            }
            breakTable.setModel(model);
            totalLabel.setText("Total Break Time: " + String.format("%.2f mins", total));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
