package com.demo.userlog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class AdminHomeForm extends JFrame {
    JTable table;
    JButton singleDayBtn, showMoreBtn;

    public AdminHomeForm() {
        setTitle("Admin Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        table = new JTable();
        loadTodayData();

        JPanel btnPanel = new JPanel();
        singleDayBtn = new JButton("Single Day");
        showMoreBtn = new JButton("Show More");
        btnPanel.add(singleDayBtn);
        btnPanel.add(showMoreBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        singleDayBtn.addActionListener(e -> new SingleDayView());
        showMoreBtn.addActionListener(e -> new ShowMore());

        setVisible(true);
    }

    private void loadTodayData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {"User Name", "Date", "Login Time", "Total Break Duration"});
        table.setModel(model);

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT u.uname, uldate, ul.ulogin, " +
                    "COALESCE(SUM(EXTRACT(EPOCH FROM (b.bend - b.bstart)) / 60), 0) AS break_minutes " +
                    "FROM userlog ul " +
                    "JOIN users u ON u.uid = ul.uid " +
                    "LEFT JOIN breaks b ON b.uid = ul.uid AND b.bdate = ul.uldate " +
                    "WHERE u.urole = 'E' AND ul.uldate = ? GROUP BY u.uname, ul.uldate, ul.ulogin";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setDate(1, new Date(new java.util.Date().getTime()));        //Date.valueOf(LocalDate.now())
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("uname");
                Date date = rs.getDate("uldate");
                Time login = rs.getTime("ulogin");
                double breakMin = rs.getDouble("break_minutes");
                model.addRow(new Object[] { name, date, login, String.format("%.2f mins", breakMin) });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
