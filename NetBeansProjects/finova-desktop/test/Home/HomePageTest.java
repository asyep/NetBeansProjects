/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Home;

import Database.DatabaseManager;
import Database.UserSession;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

/**
 *
 * @author dikdn
 */
public class HomePageTest {

  private HomePage instance;
  private static Connection connection;

  public HomePageTest() {
  }

  @BeforeClass
  public static void setUpClass() {
    // Initialize database connection
    DatabaseManager.connect();
    connection = DatabaseManager.getConnection();

    // Set up a test user ID for testing
    UserSession.userId = 1; // Use a test user ID that exists in your database
  }

  @AfterClass
  public static void tearDownClass() {
    // Clean up database connection
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      System.err.println("Error closing connection: " + e.getMessage());
    }
  }

  @Before
  public void setUp() {
    // Create a new instance before each test
    instance = new HomePage();
  }

  @After
  public void tearDown() {
    // Clean up after each test
    if (instance != null) {
      instance.dispose();
      instance = null;
    }
  }

  /**
   * Test of updateProgressBar method, of class HomePage.
   */
  @Test
  public void testUpdateProgressBar() {
    System.out.println("updateProgressBar");

    try {
      // Setup test data in the database
      setupTestData();

      // Call the method to test
      instance.updateProgressBar();

      // Get access to the private progress bar field
      Field progressBarField = HomePage.class.getDeclaredField("jProgressBar1");
      progressBarField.setAccessible(true);
      JProgressBar progressBar = (JProgressBar) progressBarField.get(instance);

      // Get access to the private progress label field
      Field progressLabelField = HomePage.class.getDeclaredField("progressLabel");
      progressLabelField.setAccessible(true);
      JLabel progressLabel = (JLabel) progressLabelField.get(instance);

      // Verify the progress bar value is set correctly
      // The exact value will depend on your test data
      assertNotNull("Progress bar should not be null", progressBar);
      assertTrue("Progress bar value should be between 0 and 100",
          progressBar.getValue() >= 0 && progressBar.getValue() <= 100);

      // Verify the progress label text is set correctly
      assertNotNull("Progress label should not be null", progressLabel);
      assertFalse("Progress label should not be empty", progressLabel.getText().isEmpty());

      // Clean up test data
      cleanupTestData();

    } catch (Exception e) {
      fail("Exception occurred during test: " + e.getMessage());
    }
  }

  /**
   * Test of main method, of class HomePage.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    // This is a UI initialization method, so we'll just verify it doesn't throw
    // exceptions
    try {
      String[] args = new String[0];
      HomePage.main(args);
      // If we get here without exception, the test passes
      assertTrue(true);
    } catch (Exception e) {
      fail("Exception occurred during main method execution: " + e.getMessage());
    }
  }

  /**
   * Helper method to set up test data in the database
   */
  private void setupTestData() throws SQLException {
    // Insert test income data
    String incomeQuery = "INSERT INTO income (user_id, amount, income_date, income_source) VALUES (?, 1000, CURRENT_DATE(), 'Test Income')";
    try (PreparedStatement stmt = connection.prepareStatement(incomeQuery)) {
      stmt.setInt(1, UserSession.userId);
      stmt.executeUpdate();
    }

    // Insert test expense data
    String expenseQuery = "INSERT INTO expense (user_id, amount, expense_date, expense_category) VALUES (?, 500, CURRENT_DATE(), 'Test Expense')";
    try (PreparedStatement stmt = connection.prepareStatement(expenseQuery)) {
      stmt.setInt(1, UserSession.userId);
      stmt.executeUpdate();
    }

    // Insert or update target amount
    String targetQuery = "INSERT INTO target_amount (user_id, amount) VALUES (?, 2000) ON DUPLICATE KEY UPDATE amount = 2000";
    try (PreparedStatement stmt = connection.prepareStatement(targetQuery)) {
      stmt.setInt(1, UserSession.userId);
      stmt.executeUpdate();
    }
  }

  /**
   * Helper method to clean up test data from the database
   */
  private void cleanupTestData() throws SQLException {
    // Delete test income data
    String deleteIncomeQuery = "DELETE FROM income WHERE user_id = ? AND income_source = 'Test Income'";
    try (PreparedStatement stmt = connection.prepareStatement(deleteIncomeQuery)) {
      stmt.setInt(1, UserSession.userId);
      stmt.executeUpdate();
    }

    // Delete test expense data
    String deleteExpenseQuery = "DELETE FROM expense WHERE user_id = ? AND expense_category = 'Test Expense'";
    try (PreparedStatement stmt = connection.prepareStatement(deleteExpenseQuery)) {
      stmt.setInt(1, UserSession.userId);
      stmt.executeUpdate();
    }
  }
}
