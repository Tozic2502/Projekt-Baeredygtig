package org.example.projektbaeredygtig;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.projektbaeredygtig.DBPackage.DBConnection;
import org.example.projektbaeredygtig.DBPackage.DBRead;

import java.util.ArrayList;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Controller {
    @FXML ComboBox<String> ComboboxYear, TypeBox;
    @FXML ChoiceBox<String> ChoiceboxMonth, ChoiceboxWeek;
    @FXML Button ModeToggle;
    @FXML GridPane gridPane;
    @FXML Label MonthLabel, WeekLabel;
    @FXML BarChart BarChart;
    @FXML PieChart pieChart;

    private boolean isAdvancedMode = false;
    private TextField typeField = new TextField();
    private TextField yearField = new TextField();
    private TextField monthField = new TextField();
    private TextField weekField = new TextField();

    @FXML void initialize() {
        DBConnection.connect();
        TypeBox.getItems().addAll("Year", "Quarters", "Month", "Week");
        TypeBox.setOnAction(event -> typeChoicebox());
        ComboboxYear.getItems().setAll("2001", "2002", "2003", "2004", "2005",
                "2006", "2007", "2008", "2009", "2010");
        ChoiceboxWeek.getItems().setAll(
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31", "32", "33", "34", "35", "36", "37",
                "38", "39", "40", "41", "42", "43", "44", "45", "46",
                "47", "48", "49", "50", "51", "52"
        );
        ChoiceboxMonth.setVisible(false);
        ChoiceboxWeek.setVisible(false);

        CSVReader.ReadCSV("/C:/temp/data/dummy_data.csv/");

    }

    @FXML
    private void toggleAdvancedMode() {
        if (isAdvancedMode) {
            isAdvancedMode = false;
            switchMode();
        } else {
            isAdvancedMode = true;
            switchMode();
        }
    }

    private void switchMode() {
        if (isAdvancedMode) {
            System.out.println("Switch Mode text");
            switchToTextFields();
            MonthLabel.setText("Enter Time Period");
            WeekLabel.setText("");
            ModeToggle.setText("Simple");
        } else {
            System.out.println("Switch Mode box");
            switchToComboBoxes();
            typeChoicebox();
            MonthLabel.setText("Choose month");
            WeekLabel.setText("Choose week");
            ModeToggle.setText("Advanced");
        }
    }

    private void switchToTextFields() {
        // Get the column and row indices, default to 0 if null
        int columnIndexTypeBox = GridPane.getColumnIndex(TypeBox) != null ? GridPane.getColumnIndex(TypeBox) : 0;
        int rowIndexTypeBox = GridPane.getRowIndex(TypeBox) != null ? GridPane.getRowIndex(TypeBox) : 0;

        int columnIndexComboboxYear = GridPane.getColumnIndex(ComboboxYear) != null ? GridPane.getColumnIndex(ComboboxYear) : 0;
        int rowIndexComboboxYear = GridPane.getRowIndex(ComboboxYear) != null ? GridPane.getRowIndex(ComboboxYear) : 0;

        int columnIndexChoiceboxMonth = GridPane.getColumnIndex(ChoiceboxMonth) != null ? GridPane.getColumnIndex(ChoiceboxMonth) : 0;
        int rowIndexChoiceboxMonth = GridPane.getRowIndex(ChoiceboxMonth) != null ? GridPane.getRowIndex(ChoiceboxMonth) : 0;

        int columnIndexChoiceboxWeek = GridPane.getColumnIndex(ChoiceboxWeek) != null ? GridPane.getColumnIndex(ChoiceboxWeek) : 0;
        int rowIndexChoiceboxWeek = GridPane.getRowIndex(ChoiceboxWeek) != null ? GridPane.getRowIndex(ChoiceboxWeek) : 0;

        // Remove the ComboBoxes
        gridPane.getChildren().removeAll(TypeBox, ComboboxYear, ChoiceboxMonth, ChoiceboxWeek);

        // Add the TextFields back with the saved indices
        gridPane.add(typeField, columnIndexTypeBox, rowIndexTypeBox);
        gridPane.add(yearField, columnIndexComboboxYear, rowIndexComboboxYear);
        gridPane.add(monthField, columnIndexChoiceboxMonth, rowIndexChoiceboxMonth);
        gridPane.add(weekField, columnIndexChoiceboxWeek, rowIndexChoiceboxWeek);
        typeField.setVisible(false);
        yearField.setVisible(false);
        weekField.setVisible(false);
        monthField.textProperty().addListener((observable, oldValue, newValue) -> {
            typeChoicetext(newValue);
        });

    }

    private void switchToComboBoxes() {
        // Get the column and row indices, default to 0 if null
        int columnIndexTypeField = GridPane.getColumnIndex(typeField) != null ? GridPane.getColumnIndex(typeField) : 0;
        int rowIndexTypeField = GridPane.getRowIndex(typeField) != null ? GridPane.getRowIndex(typeField) : 0;

        int columnIndexYearField = GridPane.getColumnIndex(yearField) != null ? GridPane.getColumnIndex(yearField) : 0;
        int rowIndexYearField = GridPane.getRowIndex(yearField) != null ? GridPane.getRowIndex(yearField) : 0;

        int columnIndexMonthField = GridPane.getColumnIndex(monthField) != null ? GridPane.getColumnIndex(monthField) : 0;
        int rowIndexMonthField = GridPane.getRowIndex(monthField) != null ? GridPane.getRowIndex(monthField) : 0;

        int columnIndexWeekField = GridPane.getColumnIndex(weekField) != null ? GridPane.getColumnIndex(weekField) : 0;
        int rowIndexWeekField = GridPane.getRowIndex(weekField) != null ? GridPane.getRowIndex(weekField) : 0;

        // Remove the TextFields
        gridPane.getChildren().removeAll(typeField, yearField, monthField, weekField);

        // Add the ComboBoxes back with the saved indices
        gridPane.add(TypeBox, columnIndexTypeField, rowIndexTypeField);
        gridPane.add(ComboboxYear, columnIndexYearField, rowIndexYearField);
        gridPane.add(ChoiceboxMonth, columnIndexMonthField, rowIndexMonthField);
        gridPane.add(ChoiceboxWeek, columnIndexWeekField, rowIndexWeekField);
    }



    @FXML
    private void typeChoicebox() {
        if (ComboboxYear == null || TypeBox == null) {
            System.out.println("ERROR: ComboboxYear or TypeBox is null!");
            return;
        }

        String selectedType = TypeBox.getValue();
        ComboboxYear.setVisible(true);

        if ("Year".equals(selectedType)) {
            ChoiceboxMonth.setVisible(false);
            ChoiceboxWeek.setVisible(false);
        } else if ("Quarters".equals(selectedType)) {
            ChoiceboxMonth.setVisible(true);
            ChoiceboxMonth.getItems().setAll("Q1", "Q2", "Q3", "Q4");
            MonthLabel.setText("Choose Quarter");
            ChoiceboxWeek.setVisible(false);
        } else if ("Month".equals(selectedType)) {
            ChoiceboxMonth.setVisible(true);
            ChoiceboxMonth.getItems().setAll(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            );
            MonthLabel.setText("Choose Month");
            ChoiceboxWeek.setVisible(false);
        } else if ("Week".equals(selectedType)) {
            ChoiceboxMonth.setVisible(true);
            ChoiceboxWeek.setVisible(true);
        }
    }
    /**
     * Parses the input text to detect a year, month, quarter, or week.
     *
     * Expected input examples:
     * - "2003 month January"
     * - "2003 quarter Q1"
     * - "2003 week 12"
     * - "2003" (just a year)
     *
     * This method prints out what was detected; you can replace the
     * print statements with your own processing logic.
     */
    private void typeChoicetext(String input) {
        if (input == null || input.trim().isEmpty()) {
            return; // nothing to process
        }
        input = input.trim();

        // --- Extract Year ---
        Pattern yearPattern = Pattern.compile("\\b(\\d{4})\\b");
        Matcher yearMatcher = yearPattern.matcher(input);
        String year = null;
        if (yearMatcher.find()) {
            year = yearMatcher.group(1);
        }

        // --- Extract Quarter ---
        // Look for keywords "quarter" or a "Q" followed by a number 1-4.
        Pattern quarterPattern = Pattern.compile("(?i)\\b(?:quarter|q)\\s*([1-4])\\b");
        Matcher quarterMatcher = quarterPattern.matcher(input);
        String quarter = null;
        if (quarterMatcher.find()) {
            quarter = quarterMatcher.group(1);
        }

        // --- Extract Month ---
        // Convert the input to lowercase and check for month names.
        String[] months = {"january", "february", "march", "april", "may", "june",
                "july", "august", "september", "october", "november", "december"};
        String foundMonth = null;
        String inputLower = input.toLowerCase();
        for (String m : months) {
            if (inputLower.contains(m)) {
                foundMonth = m;
                break;
            }
        }

        // --- Extract Week ---
        // Look for "week" followed by one or two digits.
        Pattern weekPattern = Pattern.compile("(?i)\\bweek\\s*(\\d{1,2})\\b");
        Matcher weekMatcher = weekPattern.matcher(input);
        String week = null;
        if (weekMatcher.find()) {
            week = weekMatcher.group(1);
        }

        // --- Report Detected Data ---
        System.out.println("Input: " + input);
        if (year != null) {
            System.out.println("Detected Year: " + year);
        }
        if (foundMonth != null) {
            System.out.println("Detected Month: " + foundMonth);
        }
        if (quarter != null) {
            System.out.println("Detected Quarter: Q" + quarter);
        }
        if (week != null) {
            System.out.println("Detected Week: " + week);
        }
    }


    @FXML
    private void showGraphs() {
        String selectedType = TypeBox.getValue();
        String year = ComboboxYear.getValue();
        String period = null;

        if ("Year".equals(selectedType)) {
            period = year;
        } else if ("Quarters".equals(selectedType)) {
            period = ChoiceboxMonth.getValue(); // This will be Q1, Q2, etc.
        } else if ("Month".equals(selectedType)) {
            period = ChoiceboxMonth.getValue(); // This will be January, February, etc.
        } else if ("Week".equals(selectedType)) {
            period = ChoiceboxWeek.getValue(); // This will be the week number
        }

        if (period != null) {
            updatePieChart(selectedType, year, period);
        }
    }

    private void updatePieChart(String type, String year, String period) {
        // Clear existing data
        pieChart.getData().clear();

        // Fetch data based on the type and period
        List<PieChart.Data> data = fetchDataForPieChart(type, year, period);

        // Add data to the pie chart
        pieChart.getData().addAll(data);
    }

    private List<PieChart.Data> fetchDataForPieChart(String type, String year, String period) {
        List<PieChart.Data> data = new ArrayList<>();

        // Determine the date range based on the selected type and period
        List<Date> dates = getDatesForPeriod(type, year, period);

        for (Date date : dates) {
            List<Measurement> measurements = DBRead.getMeasurements(date);

            // Process measurements to create pie chart data
            data.addAll(processMeasurements(measurements));
        }

        return data;
    }

    private List<Date> getDatesForPeriod(String type, String year, String period) {
        List<Date> dates = new ArrayList<>();

        // Implement logic to determine the list of dates based on the selected period
        // For example, if the period is a month, return all dates in that month
        // This is a placeholder implementation
        if ("Year".equals(type)) {
            // Return all dates in the year
            dates.add(Date.valueOf(year + "-01-01")); // Example: Add more dates as needed
        } else if ("Quarters".equals(type)) {
            // Return all dates in the quarter
            dates.add(Date.valueOf(year + "-" + period.substring(1) + "-01")); // Example
        } else if ("Month".equals(type)) {
            // Return all dates in the month
            dates.add(Date.valueOf(year + "-" + period + "-01")); // Example
        } else if ("Week".equals(type)) {
            // Return all dates in the week
            dates.add(Date.valueOf(year + "-W" + period + "-1")); // Example: ISO week date format
        }

        return dates;
    }

    private List<PieChart.Data> processMeasurements(List<Measurement> measurements) {
        // Count occurrences of each color
        long greenCount = measurements.stream().filter(m -> m.getColor() == 0).count();
        long yellowCount = measurements.stream().filter(m -> m.getColor() == 1).count();
        long redCount = measurements.stream().filter(m -> m.getColor() == 2).count();

        List<PieChart.Data> data = new ArrayList<>();
        data.add(new PieChart.Data("Green", greenCount));
        data.add(new PieChart.Data("Yellow", yellowCount));
        data.add(new PieChart.Data("Red", redCount));

        return data;
    }

}
