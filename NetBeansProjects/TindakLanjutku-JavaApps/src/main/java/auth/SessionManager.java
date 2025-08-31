/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

public class SessionManager {
    private static SessionManager instance;
    private int userId;
    private String username;
    private String role;
    private int idPJ; // Khusus untuk role PJ

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Method untuk memulai session
    public void startSession(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    // Tambahkan khusus untuk PJ
    public void setIdPJ(int idPJ) {
        this.idPJ = idPJ;
    }

    // Getter methods
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public int getIdPJ() { return idPJ; }

    // Method untuk logout
    public void endSession() {
        this.userId = 0;
        this.username = null;
        this.role = null;
        this.idPJ = 0;
    }

    public boolean isLoggedIn() {
        return username != null;
    }
}
