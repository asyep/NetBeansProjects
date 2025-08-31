/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import com.mycompany.tindaklanjutku.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    
    public static boolean authenticate(String username, String password) throws SQLException {
        Connection conn = Koneksi.configDB();
        String sql = "SELECT id_usr, namaUsr, pw, role FROM user WHERE namaUsr = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("pw");
                    
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        // Start session
                        SessionManager session = SessionManager.getInstance();
                        session.startSession(
                            rs.getInt("id_usr"),
                            username,
                            rs.getString("role")
                        );
                        
                        // Jika role PJ, ambil id_pj
                        if ("pj".equalsIgnoreCase(rs.getString("role"))) {
                            int idPJ = getIdPJByUserId(rs.getInt("id_usr"));
                            session.setIdPJ(idPJ);
                        }
                        
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static int getIdPJByUserId(int userId) throws SQLException {
        Connection conn = Koneksi.configDB();
        String sql = "SELECT id_pj FROM penanggung_jawab WHERE id_user = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_pj");
            }
        }
        return 0; // atau lempar exception jika diperlukan
    }
}