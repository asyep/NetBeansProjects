package admin;


public class UserSession {
    private static UserSession instance;
    private String username;
    private int userId;
    private int roleId;
    
    // Private constructor untuk mencegah instantiasi dari luar
    private UserSession() {
    }
    

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    


    public void setLoginInfo(String username, int userId, int roleId) {
        this.username = username;
        this.userId = userId;
        this.roleId = roleId;
    }
    

    public String getUsername() {
        return username;
    }
    

    public int getUserId() {
        return userId;
    }
    

    public int getRoleId() {
        return roleId;
    }
    

    public boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }
    

    public void clearLoginInfo() {
        username = null;
        userId = 0;
        roleId = 0;
    }
}