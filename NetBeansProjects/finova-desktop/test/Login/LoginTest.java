/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Login;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dikdn
 */
public class LoginTest {
    
    private static Connection conn;
    private Login instance;
    
    public LoginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/finova_desktop", "root", "");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    @Before
    public void setUp() {
        instance = new Login();
    }
    
    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }

    /**
     * Test of main method, of class Login.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        // This is a UI initialization method, so we'll just verify it doesn't throw exceptions
        try {
            String[] args = new String[0];
            Login.main(args);
            // If we get here without exception, the test passes
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred during main method execution: " + e.getMessage());
        }
    }
    
    /**
     * Test login button action performed method
     */
    @Test
    public void testLoginButtonAction() throws Exception {
        System.out.println("testLoginButtonAction");
        
        // Get the jButton1ActionPerformed method using reflection
        Method loginButtonMethod = Login.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
        loginButtonMethod.setAccessible(true);
        
        // Verify the method exists
        assertNotNull("Login button action method should exist", loginButtonMethod);
        
        // Note: We can't fully test the login process here because it involves database queries and UI updates
        // A more comprehensive test would require mocking the database and UI components
        // This test simply verifies that the method exists and can be accessed
    }
    
    /**
     * Test username field
     */
    @Test
    public void testUsernameField() throws Exception {
        System.out.println("testUsernameField");
        
        // Access the username field using reflection
        Field usernameField = Login.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        JTextField usernameTextField = (JTextField) usernameField.get(instance);
        
        // Test setting and getting username
        String testUsername = "testuser";
        usernameTextField.setText(testUsername);
        assertEquals("Username field should store the value correctly", testUsername, usernameTextField.getText());
    }
    
    /**
     * Test password field masking
     */
    @Test
    public void testPasswordFieldMasking() throws Exception {
        System.out.println("testPasswordFieldMasking");
        
        // Access the password field using reflection
        Field passwordField = Login.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        JPasswordField passwordTextField = (JPasswordField) passwordField.get(instance);
        
        // Verify that echo char is set to a masking character (typically '*' or '•')
        char echoChar = passwordTextField.getEchoChar();
        assertNotEquals("Password field should mask input", (char)0, echoChar);
    }
}
