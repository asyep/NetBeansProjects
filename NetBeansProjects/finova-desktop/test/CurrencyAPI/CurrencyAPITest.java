/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package CurrencyAPI;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dikdn
 */
public class CurrencyAPITest {

  public CurrencyAPITest() {
  }

  /**
   * Test of fetchCurrencyDataWithFallback method, of class CurrencyAPI.
   */
  @Test
  public void testFetchCurrencyDataWithFallback() {
    // Given
    System.out.println("fetchCurrencyDataWithFallback");

    // When
    CurrencyAPI.CurrencyInfo result = CurrencyAPI.fetchCurrencyDataWithFallback();

    // Then
    assertNotNull("Currency info should not be null", result);
    assertNotNull("Date should not be null", result.getDate());
    assertTrue("IDR rate should be greater than 0", result.getIdrRate() > 0);
  }

  /**
   * Test of main method, of class CurrencyAPI.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    String[] args = null;
    // When
    CurrencyAPI.main(args);
    // No assertions needed as main method is just for demonstration
  }
}
