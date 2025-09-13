package com.smartagri;
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Driver not found
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/agriculture?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url, "system", "Varma25"); // change creds
    }
}