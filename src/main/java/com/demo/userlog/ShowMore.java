package com.demo.userlog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ShowMore extends JFrame {
    JComboBox<String> userDropdown;
    JTextField startdatef,enddatef;
    JButton loadBtn;
    JLabel datel, loginl, logoutl, loginhoursl, breakdurationl;
    JTable logintable;

    public ShowMore (){
        setTitle("User Login Details");
        setSize(800, 500);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        userDropdown = new JComboBox<>();
        startdatef = new JTextField(5);
        enddatef = new JTextField(5);
        loadBtn = new JButton("Load");

        loadUsers();

        topPanel.add(new JLabel("User:"));
        topPanel.add(userDropdown);
        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(startdatef);
        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(enddatef);
        topPanel.add(loadBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        datel = new JLabel();
        loginl = new JLabel();
        logoutl = new JLabel();
        loginhoursl = new JLabel();
        breakdurationl = new JLabel();
        infoPanel.add(datel);
        infoPanel.add(loginl);
        infoPanel.add(logoutl);
        infoPanel.add(loginhoursl);
        infoPanel.add(breakdurationl);
        add(infoPanel, BorderLayout.SOUTH);

        logintable = new JTable();
        add(new JScrollPane(logintable), BorderLayout.CENTER);

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
        if (selected == null || startdatef.getText().trim().isEmpty() || enddatef.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a user and enter a date.");
            return;
        }


        String uid = selected.split(" - ")[0];
        String startdatestr = startdatef.getText().trim();
        String enddatestr = enddatef.getText().trim();
        //userInfo.setText("User: " + selected);

        try (Connection con = DBConnection.getConnection()) {
            // Login/Logout time
            String query = "SELECT uldate, ul.ulogin as login, ul.ulogout as logout, " +
                    "COALESCE(EXTRACT(EPOCH FROM (ul.ulogout - ul.ulogin)) / 60, 0) AS login_minutes, " +
                    "COALESCE(SUM(EXTRACT(EPOCH FROM (b.bend - b.bstart)) / 60), 0) AS break_minutes " +
                    "FROM userlog ul " +
                    "LEFT JOIN breaks b ON b.uid = ul.uid AND b.bdate = ul.uldate " +
                    "WHERE ul.uid = ?  AND ul.uldate between ? AND ? " +
                    "GROUP BY ul.uldate, ul.ulogin, ul.ulogout";
            PreparedStatement ps1 = con.prepareStatement(query);
            ps1.setString(1, uid);
            ps1.setDate(2, Date.valueOf(startdatestr));
            ps1.setDate(3, Date.valueOf(enddatestr));
            ResultSet rs1 = ps1.executeQuery();


            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[] {"Date", "Login", "Logout" , "Login Hours (min)", "Total Break (min)"});
            //double total = 0;
            while (rs1.next()) {
                Date date = rs1.getDate("uldate");
                Time logint = rs1.getTime("login");
                Time logoutt = rs1.getTime("logout");
                double logind = rs1.getDouble("login_minutes");
                double breakd = rs1.getDouble("break_minutes");
                //total += duration;
                model.addRow(new Object[] {date, logint, logoutt, String.format("%.2f", logind), String.format("%.2f", breakd)});
            }
            logintable.setModel(model);
            //totalLabel.setText("Total Break Time: " + String.format("%.2f mins", total));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
