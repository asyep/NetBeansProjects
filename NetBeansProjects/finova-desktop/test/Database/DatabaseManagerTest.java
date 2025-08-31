/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dikdn
 */
public class DatabaseManagerTest {
    
    private static Connection connection;
    
    public DatabaseManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        // Initialize the database connection before running tests
        DatabaseManager.connect();
        connection = DatabaseManager.getConnection();
    }
    
    @AfterClass
    public static void tearDownClass() {
        // Clean up resources after all tests
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Test of getConnection method, of class DatabaseManager.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        Connection result = DatabaseManager.getConnection();
        assertNotNull("Connection should not be null", result);
        try {
            assertFalse("Connection should be valid", result.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    /**
     * Test of connect method, of class DatabaseManager.
     */
    @Test
    public void testConnect() {
        System.out.println("connect");
        // First close the existing connection to test reconnection
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            fail("Failed to close connection: " + e.getMessage());
        }
        
        // Now reconnect
        DatabaseManager.connect();
        connection = DatabaseManager.getConnection();
        
        // Verify the connection is valid
        assertNotNull("Connection should not be null after reconnect", connection);
        try {
            assertFalse("Connection should be valid after reconnect", connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred after reconnect: " + e.getMessage());
        }
    }

    /**
     * Test of executeQuery method, of class DatabaseManager.
     */
    @Test
    public void testExecuteQuery() {
        System.out.println("executeQuery");
        // Test with a simple query that should work on any database
        String query = "SELECT 1";
        ResultSet result = DatabaseManager.executeQuery(query);
        
        assertNotNull("ResultSet should not be null", result);
        try {
            assertTrue("ResultSet should have at least one row", result.next());
            assertEquals("Query should return 1", 1, result.getInt(1));
            result.close();
        } catch (SQLException e) {
            fail("SQLException occurred during query execution: " + e.getMessage());
        }
    }
    
    /**
     * Test executeQuery with an invalid query
     */
    @Test
    public void testExecuteInvalidQuery() {
        System.out.println("executeInvalidQuery");
        // Test with an invalid query
        String invalidQuery = "SELECT * FROM nonexistent_table";
        ResultSet result = DatabaseManager.executeQuery(invalidQuery);
        
        // The method should handle exceptions internally and return null
        assertNull("ResultSet should be null for invalid query", result);
    }
}
