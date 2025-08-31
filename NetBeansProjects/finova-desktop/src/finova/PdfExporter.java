package finova; // Or your relevant package

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays; // Import Arrays
import java.util.List;   // Import List
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;

import Database.DatabaseManager;
import Database.UserSession;
import Chart.IncomeExpenseChart; // Assuming IncomeExpenseChart is in this package

public class PdfExporter {

    private static final PDType1Font FONT_BOLD = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final PDType1Font FONT_REGULAR = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final float FONT_SIZE_TITLE = 18;
    private static final float FONT_SIZE_HEADING = 14;
    private static final float FONT_SIZE_NORMAL = 10;
    private static final float FONT_SIZE_SMALL = 8;
    private static final float LEADING = 1.5f * FONT_SIZE_NORMAL;
    private static final float MARGIN = 50;
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String LOGO_PATH = "c:/Dev/finova-desktop/src/Icon/finova-logo.svg";

    public void exportToPdf(String filePath) {
        try (PDDocument document = new PDDocument()) {
            // Page 1: Chart
            addChartPage(document);

            // Page 2: Financial Summary and Transactions
            addFinancialSummaryAndTransactionsPage(document);

            // Page 3: Budget Table
            addBudgetTablePage(document);

            // Page 4: Accounts Table
            addAccountsTablePage(document);

            document.save(new File(filePath));
            System.out.println("PDF exported successfully to " + filePath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately (e.g., show an error dialog to the user)
        }
    }

    private void addChartPage(PDDocument document) throws IOException, SQLException {
        // [MODIFIED] Create a landscape A4 page
        PDRectangle landscapeA4 = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
        PDPage page = new PDPage(landscapeA4);
        document.addPage(page);

        Map<String, Integer> monthlyIncomeMap = IncomeExpenseChart.getMonthlyIncome();
        Map<String, Integer> monthlyExpensesMap = IncomeExpenseChart.getMonthlyExpenses();

        CategoryChart chart = new org.knowm.xchart.CategoryChartBuilder()
               .width(1000)  
                .height(600)
                .title("Monthly Income vs. Expenses")
                .xAxisTitle("Month")
                .yAxisTitle("Amount")
                .build();
        chart.getStyler().setLegendVisible(true);

        // [MODIFIED] Prepare series data in chronological order
        List<String> xAxisCategories = Arrays.asList(IncomeExpenseChart.MONTHS); // Use chronological months
        List<Integer> incomeValues = new ArrayList<>();
        List<Integer> expenseValues = new ArrayList<>();

        for (String month : xAxisCategories) {
            incomeValues.add(monthlyIncomeMap.getOrDefault(month, 0));
            expenseValues.add(monthlyExpensesMap.getOrDefault(month, 0));
        }

        chart.addSeries("Income", xAxisCategories, incomeValues);
        chart.addSeries("Expenses", xAxisCategories, expenseValues);


        BufferedImage chartImage = BitmapEncoder.getBufferedImage(chart);
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, chartImage);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Scale image to fit landscape page, maintaining aspect ratio
            float pageUsableWidth = page.getMediaBox().getWidth() - 2 * MARGIN;
            float pageUsableHeight = page.getMediaBox().getHeight() - 2 * MARGIN;

            float imageOriginalWidth = chartImage.getWidth();
            float imageOriginalHeight = chartImage.getHeight();

            float scale = Math.min(pageUsableWidth / imageOriginalWidth, pageUsableHeight / imageOriginalHeight);
            scale = Math.min(scale, 1.0f); // Don't scale up beyond original size

            float scaledImageWidth = imageOriginalWidth * scale;
            float scaledImageHeight = imageOriginalHeight * scale;

            float x = (page.getMediaBox().getWidth() - scaledImageWidth) / 2;
            float y = (page.getMediaBox().getHeight() - scaledImageHeight) / 2;

            contentStream.drawImage(pdImage, x, y, scaledImageWidth, scaledImageHeight);
            
            // Add footer with timestamp and logo
            addFooter(document, page, contentStream);
        }
    }

    private void addFinancialSummaryAndTransactionsPage(PDDocument document) throws IOException, SQLException {
        PDPage page = new PDPage(PDRectangle.A4); // Portrait for this page
        document.addPage(page);
        DatabaseManager.connect();
        PDPageContentStream contentStream = null;

        try {
            contentStream = new PDPageContentStream(document, page);

            float yStart = page.getMediaBox().getHeight() - MARGIN;
            float currentY = yStart;

            // Title
            contentStream.beginText();
            contentStream.setFont(FONT_BOLD, FONT_SIZE_TITLE);
            contentStream.newLineAtOffset(MARGIN, currentY);
            contentStream.showText("Financial Summary & Transactions");
            contentStream.endText();
            currentY -= FONT_SIZE_TITLE + 20;

            String balanceQuery = "SELECT COALESCE(SUM(balance), 0) AS total_balance FROM account WHERE user_id = ?";
            String liabilitiesQuery = "SELECT COALESCE(SUM(liabilities), 0) AS total_liabilities FROM account WHERE user_id = ?";
            String incomeQuery = "SELECT COALESCE(SUM(amount), 0) AS total_income FROM income WHERE user_id = ? AND MONTH(income_date) = MONTH(NOW()) AND YEAR(income_date) = YEAR(NOW())";
            String expenseQuery = "SELECT COALESCE(SUM(amount), 0) AS total_expense FROM expense WHERE user_id = ? AND MONTH(expense_date) = MONTH(NOW()) AND YEAR(expense_date) = YEAR(NOW())";

            double totalBalance = fetchData(balanceQuery);
            double totalLiabilities = fetchData(liabilitiesQuery);
            double totalIncome = fetchData(incomeQuery);
            double totalExpense = fetchData(expenseQuery);

            contentStream.beginText();
            contentStream.setFont(FONT_REGULAR, FONT_SIZE_NORMAL);
            contentStream.setLeading(LEADING);
            contentStream.newLineAtOffset(MARGIN, currentY);
            contentStream.showText("Current Balance: " + CURRENCY_FORMAT.format(totalBalance));
            contentStream.newLine();
            contentStream.showText("Liabilities: " + CURRENCY_FORMAT.format(totalLiabilities));
            contentStream.newLine();
            contentStream.showText("This Month's Income: " + CURRENCY_FORMAT.format(totalIncome));
            contentStream.newLine();
            contentStream.showText("This Month's Expense: " + CURRENCY_FORMAT.format(totalExpense));
            contentStream.endText();
            currentY -= (4 * LEADING) + 20;

            contentStream.beginText();
            contentStream.setFont(FONT_BOLD, FONT_SIZE_HEADING);
            contentStream.newLineAtOffset(MARGIN, currentY);
            contentStream.showText("Recent Transactions");
            contentStream.endText();
            currentY -= FONT_SIZE_HEADING + 10;

            String[] headers = {"ID", "Account", "Type", "Amount", "Statement", "Date"};
            float[] columnWidths = {30, 100, 70, 80, 150, 80};
            drawTableHeaders(contentStream, MARGIN, currentY, headers, columnWidths);
            currentY -= LEADING;

            String query = "SELECT t.transaction_id, a.account_type, t.type, t.amount, t.statement, t.time "
                    + "FROM transaction t "
                    + "INNER JOIN account a ON t.account_id = a.account_id "
                    + "WHERE a.user_id = ? ORDER BY t.time DESC LIMIT 10";
            try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
                pstmt.setInt(1, UserSession.userId);
                ResultSet rs = pstmt.executeQuery();
                // Font will be set in drawTableRow based on column
                while (rs.next()) {
                    if (currentY < MARGIN + LEADING) {
                        if (contentStream != null) contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        currentY = yStart;
                        drawTableHeaders(contentStream, MARGIN, currentY, headers, columnWidths);
                        currentY -= LEADING;
                        // Font will be reset in drawTableRow
                    }
                    String[] rowData = {
                            rs.getString("transaction_id"),
                            rs.getString("account_type"),
                            rs.getString("type"),
                            CURRENCY_FORMAT.format(rs.getDouble("amount")),
                            rs.getString("statement"),
                            rs.getString("time")
                    };
                drawTableRow(contentStream, MARGIN, currentY, rowData, columnWidths, headers); // Pass headers for context
                currentY -= LEADING;
            }
        }
        
        // Add footer with timestamp and logo
        if (contentStream != null) {
            addFooter(document, page, contentStream);
        }
    } finally {
        if (contentStream != null) {
            contentStream.close();
        }
    }
    }

    private void addBudgetTablePage(PDDocument document) throws IOException, SQLException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        DatabaseManager.connect();
        PDPageContentStream contentStream = null;

        try {
            contentStream = new PDPageContentStream(document, page);
            float yStart = page.getMediaBox().getHeight() - MARGIN;
            float currentY = yStart;

            contentStream.beginText();
            contentStream.setFont(FONT_BOLD, FONT_SIZE_TITLE);
            contentStream.newLineAtOffset(MARGIN, currentY);
            contentStream.showText("Budget Allocations");
            contentStream.endText();
            currentY -= FONT_SIZE_TITLE + 20;

            String[] headers = {"Expense Category", "Allocated Amount"};
            float[] columnWidths = {250, 150};
            drawTableHeaders(contentStream, MARGIN, currentY, headers, columnWidths);
            currentY -= LEADING;

            String query = "SELECT expense_category, amount FROM budget WHERE user_id = ? ORDER BY expense_category";
            try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
                pstmt.setInt(1, UserSession.userId);
                ResultSet rs = pstmt.executeQuery();
                // Font will be set in drawTableRow
                while (rs.next()) {
                    if (currentY < MARGIN + LEADING) {
                        if (contentStream != null) contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        currentY = yStart;
                        drawTableHeaders(contentStream, MARGIN, currentY, headers, columnWidths);
                        currentY -= LEADING;
                        // Font will be reset in drawTableRow
                    }
                    String[] rowData = {
                            rs.getString("expense_category"),
                            CURRENCY_FORMAT.format(rs.getDouble("amount"))
                    };
                    drawTableRow(contentStream, MARGIN, currentY, rowData, columnWidths, headers); // Pass headers
                    currentY -= LEADING;
                }
            }
            
            // Add footer with timestamp and logo
            if (contentStream != null) {
                addFooter(document, page, contentStream);
            }
        } finally {
            if (contentStream != null) {
                contentStream.close();
            }
        }
    }

    private void addAccountsTablePage(PDDocument document) throws IOException, SQLException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        DatabaseManager.connect();
        PDPageContentStream contentStream = null;

        try {
            contentStream = new PDPageContentStream(document, page);
            float yStart = page.getMediaBox().getHeight() - MARGIN;
            float currentY = yStart;

            contentStream.beginText();
            contentStream.setFont(FONT_BOLD, FONT_SIZE_TITLE);
            contentStream.newLineAtOffset(MARGIN, currentY);
            contentStream.showText("Account Overview");
            contentStream.endText();
            currentY -= FONT_SIZE_TITLE + 20;

            String[] headers = {"Account Name", "Current Balance", "Total Expenses"};
            float[] columnWidths = {200, 150, 150};
            drawTableHeaders(contentStream, MARGIN, currentY, headers, columnWidths);
            currentY -= LEADING;

            String query = "SELECT account_type, balance, liabilities FROM account WHERE user_id = ?";
            try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
                pstmt.setInt(1, UserSession.userId);
                ResultSet rs = pstmt.executeQuery();
                // Font will be set in drawTableRow
                while (rs.next()) {
                    if (currentY < MARGIN + LEADING) {
                        if (contentStream != null) contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        currentY = yStart;
                        drawTableHeaders(contentStream, MARGIN, currentY, headers, columnWidths);
                        currentY -= LEADING;
                        // Font will be reset in drawTableRow
                    }
                    String[] rowData = {
                            rs.getString("account_type"),
                            CURRENCY_FORMAT.format(rs.getDouble("balance")),
                            CURRENCY_FORMAT.format(rs.getDouble("liabilities"))
                    };
                    drawTableRow(contentStream, MARGIN, currentY, rowData, columnWidths, headers); // Pass headers
                    currentY -= LEADING;
                }
            }
            
            // Add footer with timestamp and logo
            if (contentStream != null) {
                addFooter(document, page, contentStream);
            }
        } finally {
            if (contentStream != null) {
                contentStream.close();
            }
        }
    }

    private double fetchData(String query) throws SQLException {
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, UserSession.userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    private void drawTableHeaders(PDPageContentStream contentStream, float x, float y, String[] headers, float[] columnWidths) throws IOException {
        contentStream.setFont(FONT_BOLD, FONT_SIZE_NORMAL);
        float currentX = x;
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(currentX, y);
            contentStream.showText(headers[i]);
            contentStream.endText();
            currentX += columnWidths[i];
        }
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(x, y - 3);
        contentStream.lineTo(x + sum(columnWidths), y - 3);
        contentStream.stroke();
    }

    // [MODIFIED] drawTableRow to accept headers array for contextual font sizing
    private void drawTableRow(PDPageContentStream contentStream, float x, float y, String[] rowData, float[] columnWidths, String[] tableHeaders) throws IOException {
        float currentX = x;
        for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(currentX, y);
            String text = rowData[i] != null ? rowData[i] : "";

            PDType1Font currentFont = FONT_REGULAR;
            float currentFontSize = FONT_SIZE_NORMAL;

            // Example: Different font size for "ID" and "Date" columns in Transactions table
            if (Arrays.equals(tableHeaders, new String[]{"ID", "Account", "Type", "Amount", "Statement", "Date"})) {
                if (i == 0 || i == 5) { // Index for ID and Date
                    currentFontSize = FONT_SIZE_SMALL;
                }
            }
            // Add other conditions for other tables if needed

            contentStream.setFont(currentFont, currentFontSize);

            // Basic Truncation (improved slightly)
            float textWidth = currentFont.getStringWidth(text) / 1000 * currentFontSize;
            if (textWidth > columnWidths[i] - 5) { // Give a little padding
                // Estimate how many characters can fit
                int charsToFit = (int) ((columnWidths[i] - 5) / (textWidth / text.length() + 1e-5)); // +1e-5 to avoid div by zero
                if (text.length() > charsToFit && charsToFit > 3) {
                    text = text.substring(0, charsToFit - 3) + "...";
                } else if (text.length() > 3 && charsToFit <=3) { // If calc is too small but text is still long
                     text = text.substring(0, Math.min(text.length(), 3)) + "...";
                }
            }

            contentStream.showText(text);
            contentStream.endText();
            currentX += columnWidths[i];
        }
    }

    // headersMatch method is removed as it's replaced by directly passing headers to drawTableRow
    // private boolean headersMatch(float[] currentLayout, float[] targetLayout) { ... }

    private float sum(float[] array) {
        float total = 0;
        for (float val : array) {
            total += val;
        }
        return total;
    }

    /**
     * Adds a footer with timestamp to the page
     */
    private void addFooter(PDDocument document, PDPage page, PDPageContentStream contentStream) throws IOException {
        String timestamp = "Generated on: " + DATE_FORMAT.format(new Date());
        
        // Calculate footer position
        float footerY = 30; // 30 points from bottom
        float pageWidth = page.getMediaBox().getWidth();
        
        // Add timestamp on the left
        contentStream.beginText();
        contentStream.setFont(FONT_REGULAR, FONT_SIZE_SMALL);
        contentStream.newLineAtOffset(MARGIN, footerY);
        contentStream.showText(timestamp);
        contentStream.endText();
        
        // Add logo on the right
        addLogoToFooter(document, page, contentStream, pageWidth - MARGIN - 100, footerY);
    }

    /**
     * Adds the Finova logo to the footer
     */
    private void addLogoToFooter(PDDocument document, PDPage page, PDPageContentStream contentStream, float x, float y) throws IOException {
        try {
            // Read SVG file and convert to image
            PDImageXObject logoImage = createImageFromSVG(document);
            if (logoImage != null) {
                // Scale logo to appropriate size for footer
                float logoWidth = 32;
                float logoHeight = 32;
                contentStream.drawImage(logoImage, x, y, logoWidth, logoHeight);
            } else {
                // Fallback: Add text "FINOVA" if logo can't be loaded
                contentStream.beginText();
                contentStream.setFont(FONT_BOLD, FONT_SIZE_SMALL);
                contentStream.newLineAtOffset(x, y + 5);
                contentStream.showText("FINOVA");
                contentStream.endText();
            }
        } catch (Exception e) {
            // Fallback: Add text "FINOVA" if any error occurs
            contentStream.beginText();
            contentStream.setFont(FONT_BOLD, FONT_SIZE_SMALL);
            contentStream.newLineAtOffset(x, y + 5);
            contentStream.showText("FINOVA");
            contentStream.endText();
        }
    }

    /**
     * Creates a PDImageXObject from PNG logo file
     */
    private PDImageXObject createImageFromSVG(PDDocument document) {
        try {
            // Load the PNG logo file
            String logoPath = "c:/Dev/finova-desktop/src/Icon/finova-logo.png";
            File logoFile = new File(logoPath);
            
            if (logoFile.exists()) {
                BufferedImage logoImage = javax.imageio.ImageIO.read(logoFile);
                return LosslessFactory.createFromImage(document, logoImage);
            } else {
                System.err.println("Logo file not found at: " + logoPath);
                return createFallbackLogo(document);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo image: " + e.getMessage());
            return createFallbackLogo(document);
        }
    }
    
    /**
     * Creates a fallback logo image with "FINOVA" text
     */
    private PDImageXObject createFallbackLogo(PDDocument document) {
        try {
            // Create a simple placeholder image with "FINOVA" text
            BufferedImage logoImage = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = logoImage.createGraphics();
            
            // Set background to white
            g2d.setColor(java.awt.Color.WHITE);
            g2d.fillRect(0, 0, 200, 50);
            
            // Set text color and font
            g2d.setColor(new java.awt.Color(67, 97, 123)); // #43617B from the original SVG
            g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            
            // Draw "FINOVA" text
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth("FINOVA");
            int x = (200 - textWidth) / 2;
            int y = (50 + fm.getAscent()) / 2;
            g2d.drawString("FINOVA", x, y);
            
            g2d.dispose();
            
            return LosslessFactory.createFromImage(document, logoImage);
        } catch (Exception e) {
            System.err.println("Error creating fallback logo: " + e.getMessage());
            return null;
        }
    }
}