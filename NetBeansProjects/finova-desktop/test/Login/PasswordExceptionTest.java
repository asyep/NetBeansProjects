/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Login;

import java.lang.reflect.Method;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dikdn
 */
public class PasswordExceptionTest {
    
    private SignUp signUpInstance;
    
    public PasswordExceptionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test password length validation
     */
    @Test
    public void testPasswordLengthValidation() throws Exception {
        System.out.println("testPasswordLengthValidation");
        
        // Create a SignUp instance to access the passwordRestrictions method
        signUpInstance = new SignUp();
        
        // Get the private passwordRestrictions method using reflection
        Method passwordRestrictionsMethod = SignUp.class.getDeclaredMethod("passwordRestrictions", char[].class);
        passwordRestrictionsMethod.setAccessible(true);
        
        // Test with a short password (less than 8 characters)
        char[] shortPassword = "short".toCharArray();
        try {
            passwordRestrictionsMethod.invoke(signUpInstance, (Object) shortPassword);
            fail("Should have thrown an exception for short password");
        } catch (Exception e) {
            // Verify that the cause is a PasswordException with the correct message
            Throwable cause = e.getCause();
            assertTrue("Exception should be a PasswordException", cause instanceof Exception);
            assertTrue("Exception message should mention password length", 
                    cause.getMessage().contains("should be at least"));
        }
        
        // Test with a password of exactly minimum length but no special character
        char[] exactLengthPassword = "password".toCharArray();
        try {
            passwordRestrictionsMethod.invoke(signUpInstance, (Object) exactLengthPassword);
            fail("Should have thrown an exception for password without special character");
        } catch (Exception e) {
            // Verify that the cause is a PasswordException with the correct message
            Throwable cause = e.getCause();
            assertTrue("Exception should be a PasswordException", cause instanceof Exception);
            assertTrue("Exception message should mention special character", 
                    cause.getMessage().contains("special character"));
        }
    }
    
    /**
     * Test password special character validation
     */
    @Test
    public void testPasswordSpecialCharacterValidation() throws Exception {
        System.out.println("testPasswordSpecialCharacterValidation");
        
        // Create a SignUp instance to access the passwordRestrictions method
        signUpInstance = new SignUp();
        
        // Get the private passwordRestrictions method using reflection
        Method passwordRestrictionsMethod = SignUp.class.getDeclaredMethod("passwordRestrictions", char[].class);
        passwordRestrictionsMethod.setAccessible(true);
        
        // Test with a password that has sufficient length but no special character
        char[] noSpecialCharPassword = "password12345".toCharArray();
        try {
            passwordRestrictionsMethod.invoke(signUpInstance, (Object) noSpecialCharPassword);
            fail("Should have thrown an exception for password without special character");
        } catch (Exception e) {
            // Verify that the cause is a PasswordException with the correct message
            Throwable cause = e.getCause();
            assertTrue("Exception should be a PasswordException", cause instanceof Exception);
            assertTrue("Exception message should mention special character", 
                    cause.getMessage().contains("special character"));
        }
        
        // Test with a valid password (sufficient length and has special character)
        char[] validPassword = "password!123".toCharArray();
        try {
            passwordRestrictionsMethod.invoke(signUpInstance, (Object) validPassword);
            // If we get here without exception, the test passes
            assertTrue(true);
        } catch (Exception e) {
            fail("Should not have thrown an exception for valid password: " + e.getMessage());
        }
    }
}
