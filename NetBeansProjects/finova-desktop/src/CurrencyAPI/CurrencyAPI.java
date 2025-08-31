/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CurrencyAPI; // Your package name

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale; // Added for Locale
import org.json.JSONObject; // Ensure org.json library is in your project

/**
 *
 * @author dikdn
 */
public class CurrencyAPI {

    // Helper class to store fetched currency data
    public static class CurrencyInfo {

        private String date;     // Made private
        private double idrRate;  // Made private

        // Constructor remains package-private as it's intended for use within this package
        CurrencyInfo(String date, double idrRate) {
            this.date = date;
            this.idrRate = idrRate;
        }

        // Public getter for date
        public String getDate() {
            return date;
        }

        // Public getter for idrRate (raw double value)
        public double getIdrRate() {
            return idrRate;
        }

        /**
         * Returns the IDR rate formatted as "xxxx,xx IDR" string.
         * Ensures two decimal places with a comma as the separator.
         * Example: 16307.589 becomes "16307,59 IDR"
         * 15000.0 becomes "15000,00 IDR"
         * @return Formatted IDR rate string.
         */
        public String getFormattedIdrRate() {
            // Using Locale.GERMAN as it uses comma for decimal separator.
            // "%.2f" formats the double to 2 decimal places.
            String formattedRate = String.format(Locale.GERMAN, "%.2f", this.idrRate);
            return formattedRate + " IDR";
        }

        @Override
        public String toString() {
            return "CurrencyInfo{" +
                   "date='" + date + '\'' +
                   ", idrRate=" + idrRate +
                   ", formattedIdrRate='" + getFormattedIdrRate() + '\'' +
                   '}';
        }
    }

    /**
     * Attempts to fetch currency data from a given API URL.
     *
     * @param apiUrl The URL to fetch data from.
     * @return CurrencyInfo object if successful, null otherwise.
     */
    private static CurrencyInfo tryFetchFromUrl(String apiUrl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000); // 5 seconds connection timeout
            connection.setReadTimeout(5000);    // 5 seconds read timeout

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String date = jsonResponse.getString("date");
                JSONObject usdObject = jsonResponse.getJSONObject("usd");
                double idrRate = usdObject.getDouble("idr");

                return new CurrencyInfo(date, idrRate);
            } else {
                System.err.println("GET request failed for " + apiUrl + ". Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Exception while fetching from " + apiUrl + ": " + e.getMessage());
            // e.printStackTrace(); // Uncomment for detailed error logging
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Fetches currency data using a primary URL and a fallback URL.
     * @return CurrencyInfo object containing the fetched data, or null if fetching failed.
     */
    public static CurrencyInfo fetchCurrencyDataWithFallback() {
        String primaryApiUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/usd.min.json";
        String fallbackApiUrl = "https://latest.currency-api.pages.dev/v1/currencies/usd.min.json";

        System.out.println("Attempting to fetch from primary URL: " + primaryApiUrl);
        CurrencyInfo data = tryFetchFromUrl(primaryApiUrl);

        if (data == null) {
            System.out.println("Primary URL failed. Attempting to fetch from fallback URL: " + fallbackApiUrl);
            data = tryFetchFromUrl(fallbackApiUrl);
        }

        if (data != null) {
            System.out.println("\nSuccessfully fetched currency data!");
            System.out.println("Data fetched for date: " + data.getDate());
            System.out.println("1 USD = " + data.getFormattedIdrRate()); // Using the new formatted method
        } else {
            System.out.println("\nFailed to fetch currency data from both primary and fallback URLs.");
        }
        return data; // Return the fetched data (or null)
    }

    public static void main(String[] args) {
        CurrencyInfo info = fetchCurrencyDataWithFallback();
        if (info != null) {
            System.out.println("Main method received:");
            System.out.println("Date: " + info.getDate());
            System.out.println("Raw Rate: " + info.getIdrRate());
            System.out.println("Formatted Rate: " + info.getFormattedIdrRate()); // Using the new method
            // System.out.println("Full info object: " + info); // toString also updated
        } else {
            System.out.println("Main method: Could not retrieve currency info.");
        }
    }
}
