package Chart;

import java.util.Map;
import java.util.TreeMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Database.UserSession;

public class IncomeExpenseChartTest {

  @Before
  public void setUp() {
    // Ensure we have a valid user session for testing
    UserSession.userId = 1; // Use a test user ID
  }

  @Test
  public void testGetMonthlyIncome() throws Exception {
    // When
    Map<String, Integer> result = IncomeExpenseChart.getMonthlyIncome();

    // Then
    assertNotNull("Monthly income map should not be null", result);
    assertEquals("Should contain all 12 months", 12, result.size());
    assertTrue("Should contain January", result.containsKey("January"));
    assertTrue("Should contain December", result.containsKey("December"));

    // Verify the map is properly initialized
    for (String month : IncomeExpenseChart.MONTHS) {
      assertTrue("Should contain " + month, result.containsKey(month));
      assertNotNull("Value for " + month + " should not be null");
    }
  }

  @Test
  public void testGetMonthlyExpenses() throws Exception {
    // When
    Map<String, Integer> result = IncomeExpenseChart.getMonthlyExpenses();

    // Then
    assertNotNull("Monthly expenses map should not be null", result);
    assertEquals("Should contain all 12 months", 12, result.size());
    assertTrue("Should contain January", result.containsKey("January"));
    assertTrue("Should contain December", result.containsKey("December"));

    // Verify the map is properly initialized
    for (String month : IncomeExpenseChart.MONTHS) {
      assertTrue("Should contain " + month, result.containsKey(month));
      assertNotNull("Value for " + month + " should not be null", result.get(month));
    }
  }

  @Test
  public void testGenerateChart() {
    // Given
    Map<String, Integer> monthlyIncome = new TreeMap<>();
    Map<String, Integer> monthlyExpenses = new TreeMap<>();

    // Populate with test data
    for (String month : IncomeExpenseChart.MONTHS) {
      monthlyIncome.put(month, 1000);
      monthlyExpenses.put(month, 500);
    }

    IncomeExpenseChart.generateChart(monthlyIncome, monthlyExpenses);
  }
}
