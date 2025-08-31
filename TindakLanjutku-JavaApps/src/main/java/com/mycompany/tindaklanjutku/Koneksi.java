package com.mycompany.tindaklanjutku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    public static Connection konfig;
    
    public static Connection configDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/tindaklanjutku?useSSL=false&serverTimezone=UTC";
            konfig = DriverManager.getConnection(url, "root", "");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: MySQL Driver tidak ditemukan!\nPastikan file mysql-connector-java.jar ada di classpath.");
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Koneksi Database:\n" + e.getMessage());
        }
        return konfig;
    }
    
    public static void main(String[] args) throws Exception {
        Connection penghubung = configDB();
        if (penghubung != null) {
            JOptionPane.showMessageDialog(null, "Koneksi berhasil!");
            penghubung.close();
        }
    }
}