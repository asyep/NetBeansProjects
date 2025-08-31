/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Login;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author dikdn
 */
public class SignUpTest {
    
    private SignUp instance;
    
    public SignUpTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new SignUp();
    }
    
    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }

    /**
     * Test of main method, of class SignUp.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        // This is a UI initialization method, so we'll just verify it doesn't throw exceptions
        try {
            String[] args = new String[0];
            SignUp.main(args);
            // If we get here without exception, the test passes
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred during main method execution: " + e.getMessage());
        }
    }
    
    /**
     * Test password validation method
     */
    @Test
    public void testPasswordValidation() throws Exception {
        System.out.println("testPasswordValidation");
        
        // Get the private passwordRestrictions method using reflection
        Method passwordRestrictionsMethod = SignUp.class.getDeclaredMethod("passwordRestrictions", char[].class);
        passwordRestrictionsMethod.setAccessible(true);
        
        // Test with a valid password
        char[] validPassword = "Password123!".toCharArray();
        try {
            passwordRestrictionsMethod.invoke(instance, (Object) validPassword);
            // If we get here without exception, the test passes
            assertTrue(true);
        } catch (Exception e) {
            fail("Should not have thrown an exception for valid password: " + e.getMessage());
        }
    }
    
    /**
     * Test username field validation
     */
    @Test
    public void testUsernameField() throws Exception {
        System.out.println("testUsernameField");
        
        // Access the username field using reflection
        Field usernameField = SignUp.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        JTextField usernameTextField = (JTextField) usernameField.get(instance);
        
        // Test setting and getting username
        String testUsername = "testuser";
        usernameTextField.setText(testUsername);
        assertEquals("Username field should store the value correctly", testUsername, usernameTextField.getText());
    }
    
    /**
     * Test name field validation
     */
    @Test
    public void testNameField() throws Exception {
        System.out.println("testNameField");
        
        // Access the name field using reflection
        Field nameField = SignUp.class.getDeclaredField("name");
        nameField.setAccessible(true);
        JTextField nameTextField = (JTextField) nameField.get(instance);
        
        // Test setting and getting name
        String testName = "Test User";
        nameTextField.setText(testName);
        assertEquals("Name field should store the value correctly", testName, nameTextField.getText());
    }
    
    /**
     * Test password field masking
     */
    @Test
    public void testPasswordFieldMasking() throws Exception {
        System.out.println("testPasswordFieldMasking");
        
        // Access the password field using reflection
        Field passwordField = SignUp.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        JPasswordField passwordTextField = (JPasswordField) passwordField.get(instance);
        
        // Verify that echo char is set to a masking character (typically '*' or '•')
        char echoChar = passwordTextField.getEchoChar();
        // Fix the assertNotEquals call by using the correct parameter order
        assertNotEquals("Password field should mask input", (char)0, echoChar);
    }
}
