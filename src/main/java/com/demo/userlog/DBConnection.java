package com.demo.userlog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://ep-yellow-leaf-a4ikoa1c-pooler.us-east-1.aws.neon.tech/neondb?sslmode=require";
    //"jdbc:postgresql://postgres.railway.internal:5432/railway";//ep-yellow-leaf-a4ikoa1c-pooler.us-east-1.aws.neon.tech//ep-long-pond-a452et23-pooler.us-east-1.aws.neon.tech
    private static final String USER = "neondb_owner";
    private static final String PASS = "npg_UtA3wOX2rHTP";          //  npg_jZFQwqH2U3lE

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

