package com.example.studentfx;

import java.io.IOException;
import java.sql.*;

public class DB {

    static Connection con = null;

    // Test
    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "Alesio17!");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Test
    public static boolean connected() {
        return con != null;
    }

    // Test
    public static boolean checkIfNew(String tableName, String username) throws SQLException {
        if(connected()) {
            Statement stmt = con.createStatement();
            String query = "SELECT username FROM "+tableName+";";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                String name = rs.getString("username");
                if(name.equals(username)) {
                    return false;
                }
            }
            return true;
        }
        else {
            System.out.println("Database is not connected");
            return false;
        }
    }

    public static void getFullName() throws SQLException {
        if(connected()) {
            Statement stmt = con.createStatement();
            String query = "SELECT name, surname FROM student WHERE username = '"+Data.text+"';";
            ResultSet rs = stmt.executeQuery(query);
            String fullName = "";
            String initials = "";

            if(rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                initials += name.charAt(0) + "";
                initials += surname.charAt(0) + "";
                Data.initials = initials;
                fullName += name + " " + surname;
                Data.fullName = fullName;
            }
        }
        else {
            System.out.println("Database is not connected");
        }
    }

}
