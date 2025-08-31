package Chart;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays; // Import Arrays
import java.util.List;  // Import List
import java.util.Map;
import java.util.TreeMap; // Import TreeMap to sort the data
import Database.DatabaseManager;
import Database.UserSession;
import javax.swing.JFrame;
import org.knowm.xchart.SwingWrapper;

public class IncomeExpenseChart {

    // Array of month names in chronological order - MAKE THIS PUBLIC
    public static final String[] MONTHS = { // [MODIFIED]
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
            "November", "December"
    };

    public static Map<String, Integer> getMonthlyIncome() throws SQLException {
        Map<String, Integer> monthlyIncome = new TreeMap<>(); // TreeMap will sort alphabetically initially
        // To ensure all months are present for data retrieval, we can pre-populate or use the MONTHS array later for ordering
        for (String month : MONTHS) {
            monthlyIncome.put(month, 0); // Pre-populate with 0
        }

        DatabaseManager.connect();
        String query = "SELECT monthname(income_date) AS month, SUM(amount) AS income " +
                "FROM income " +
                "WHERE user_id = ? " +
                "GROUP BY monthname(income_date)"; // Group by monthname to match keys
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, UserSession.userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String month = rs.getString("month");
                int income = rs.getInt("income");
                if (month != null) { // Ensure month is not null
                    monthlyIncome.put(month, income); // Override pre-populated 0 if data exists
                }
            }
        }
        return monthlyIncome; // Will contain all 12 months, data sorted alphabetically by month name
    }

    public static Map<String, Integer> getMonthlyExpenses() throws SQLException {
        Map<String, Integer> monthlyExpenses = new TreeMap<>();
        for (String month : MONTHS) {
            monthlyExpenses.put(month, 0); // Pre-populate with 0
        }
        DatabaseManager.connect();
        String query = "SELECT monthname(expense_date) AS month, SUM(amount) AS expense " +
                "FROM expense " +
                "WHERE user_id = ? " +
                "GROUP BY monthname(expense_date)"; // Group by monthname
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, UserSession.userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String month = rs.getString("month");
                int expense = rs.getInt("expense");
                 if (month != null) { // Ensure month is not null
                    monthlyExpenses.put(month, expense);
                }
            }
        }
        return monthlyExpenses;
    }

    // generateChart method in IncomeExpenseChart.java is primarily for Swing display.
    // The PDF exporter will create its own chart instance using the data.
    public static void generateChart(Map<String, Integer> monthlyIncome, Map<String, Integer> monthlyExpenses) {
        Thread chartThread = new Thread(() -> {
            CategoryChart chart = new CategoryChartBuilder()
                    .width(1000)
                    .height(504)
                    .title("Monthly Income vs. Expenses")
                    .xAxisTitle("Month")
                    .yAxisTitle("Amount")
                    .build();

            // Prepare chronologically ordered data for the chart
            List<String> KATEGORIXAxis = Arrays.asList(MONTHS);
            List<Integer> nilaiIncome = new ArrayList<>();
            List<Integer> nilaiExpense = new ArrayList<>();

            for (String month : KATEGORIXAxis) {
                nilaiIncome.add(monthlyIncome.getOrDefault(month, 0));
                nilaiExpense.add(monthlyExpenses.getOrDefault(month, 0));
            }

            chart.addSeries("Income", KATEGORIXAxis, nilaiIncome);
            chart.addSeries("Expenses", KATEGORIXAxis, nilaiExpense);

            SwingWrapper<CategoryChart> wrapper = new SwingWrapper<>(chart);
            JFrame frame = wrapper.displayChart();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });
        chartThread.start();
    }
}