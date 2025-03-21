package org.example.projektbaeredygtig;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.projektbaeredygtig.DBPackage.DBConnection;
import org.example.projektbaeredygtig.DBPackage.DBRead;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller {
    @FXML ComboBox<String> ComboboxYear, TypeBox;
    @FXML ChoiceBox<String> ChoiceboxMonth, ChoiceboxWeek;
    @FXML Button ModeToggle;
    @FXML GridPane gridPane;
    @FXML Label MonthLabel, WeekLabel;
    @FXML Button button;
    @FXML BarChart<String, Number> barChart;
    @FXML PieChart pieChart;
    @FXML Label totalDistanceLabel;
    @FXML Label totalTimeLabel;

    private boolean isAdvancedMode = false;
    private TextField typeField = new TextField();
    private TextField yearField = new TextField();
    private TextField monthField = new TextField();
    private TextField weekField = new TextField();
    private String filePath = "";

    @FXML void initialize() {
        DBConnection.connect();
        TypeBox.getItems().addAll("Year", "Quarters", "Month", "Week");
        TypeBox.setValue("Year"); // Set default value
        TypeBox.setOnAction(event -> typeChoicebox());
        
        // Set up bar chart initial properties
        barChart.setAnimated(false);
        barChart.setTitle("Distribution by Color");
        barChart.setCategoryGap(10);
        barChart.setBarGap(3);
        
        // Initially hide legends until data is loaded
        barChart.setLegendVisible(true);
        
        // Set axis labels
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) barChart.getYAxis();
        xAxis.setLabel("Time Period");
        yAxis.setLabel("Count");
        
        // Get available years from database
        Date minDate = DBRead.MinDate();
        Date maxDate = DBRead.MaxDate();
        
        if (minDate != null && maxDate != null) {
            int startYear = minDate.toLocalDate().getYear();
            int endYear = maxDate.toLocalDate().getYear();
            List<String> years = new ArrayList<>();
            
            for (int year = startYear; year <= endYear; year++) {
                years.add(String.valueOf(year));
            }
            
            ComboboxYear.getItems().setAll(years);
            // Select the most recent year
            ComboboxYear.setValue(String.valueOf(endYear));
        } else {
            // Fallback to current year if no database records
            ComboboxYear.getItems().setAll(String.valueOf(java.time.Year.now().getValue()));
            ComboboxYear.setValue(String.valueOf(java.time.Year.now().getValue()));
        }
        ChoiceboxMonth.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                updateWeeks(newVal.intValue() + 1); // Convert index (0-11) to month number (1-12)
            }
        });
        ChoiceboxMonth.getItems().setAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        ChoiceboxMonth.setVisible(false);
        ChoiceboxWeek.setVisible(false);
        if (!getFilePath().equals("")) {
            CSVReader.ReadCSV(getFilePath());
        }
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
        monthField.setVisible(true);
        monthField.textProperty().addListener((observable, oldValue, newValue) -> {
            typeChoicetext(newValue);
        });
        
        // Add promptText to make the field's purpose clear
        monthField.setPromptText("Enter time period (e.g., 2023 month January)");

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
            ChoiceboxMonth.getItems().setAll(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            );
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
        if (isAdvancedMode) {
            // In advanced mode, parse the text input
            String input = monthField.getText();
            if (input == null || input.trim().isEmpty()) {
                System.out.println("ERROR: No input provided in advanced mode");
                return;
            }
            typeChoicetext(input);
            // Handle the rest of the method - advanced mode implementation could be added here
            return;
        }
        
        String selectedType = TypeBox.getValue();
        String selectedYear = ComboboxYear.getValue();
        if (selectedType == null || selectedYear == null) {
            System.out.println("ERROR: Type or Year is null!");
            return;
        }

        Date startDate = null;
        Date endDate = null;
        LocalDate localStartDate = null;
        LocalDate localEndDate = null;

        switch (selectedType) {
            case "Year":
                localStartDate = LocalDate.of(Integer.parseInt(selectedYear), 1, 1);
                localEndDate = LocalDate.of(Integer.parseInt(selectedYear), 12, 31);
                startDate = Date.valueOf(localStartDate);
                endDate = Date.valueOf(localEndDate);
                break;
            case "Quarters":
                String selectedQuarter = ChoiceboxMonth.getValue();
                if (selectedQuarter == null) {
                    System.out.println("ERROR: No quarter selected");
                    return;
                }
                String year = ComboboxYear.getValue();
                switch (selectedQuarter) {
                    case "Q1":
                        localStartDate = LocalDate.of(Integer.parseInt(year), 1, 1);
                        localEndDate = LocalDate.of(Integer.parseInt(year), 3, 31);
                        startDate = Date.valueOf(localStartDate);
                        endDate = Date.valueOf(localEndDate);
                        break;
                    case "Q2":
                        localStartDate = LocalDate.of(Integer.parseInt(year), 4, 1);
                        localEndDate = LocalDate.of(Integer.parseInt(year), 6, 30);
                        startDate = Date.valueOf(localStartDate);
                        endDate = Date.valueOf(localEndDate);
                        break;
                    case "Q3":
                        localStartDate = LocalDate.of(Integer.parseInt(year), 7, 1);
                        localEndDate = LocalDate.of(Integer.parseInt(year), 9, 30);
                        startDate = Date.valueOf(localStartDate);
                        endDate = Date.valueOf(localEndDate);
                        break;
                    case "Q4":
                        localStartDate = LocalDate.of(Integer.parseInt(year), 10, 1);
                        localEndDate = LocalDate.of(Integer.parseInt(year), 12, 31);
                        startDate = Date.valueOf(localStartDate);
                        endDate = Date.valueOf(localEndDate);
                        break;
                    default:
                        System.out.println("Invalid quarter selection");
                        return;
                }
                break;
            case "Month":
                String selectedMonth = ChoiceboxMonth.getValue();
                if (selectedMonth == null) {
                    System.out.println("ERROR: No month selected");
                    return;
                }
                year = ComboboxYear.getValue();
                int month = convertMonthNameToNumber(selectedMonth);
                YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), month);
                localStartDate = yearMonth.atDay(1);
                localEndDate = yearMonth.atEndOfMonth();
                startDate = Date.valueOf(localStartDate);
                endDate = Date.valueOf(localEndDate);
                break;
            case "Week":
                String selectedWeek = ChoiceboxWeek.getValue();
                if (selectedWeek == null) {
                    System.out.println("ERROR: No week selected");
                    return;
                }
                year = ComboboxYear.getValue();
                selectedMonth = ChoiceboxMonth.getValue();
                if (selectedMonth == null) {
                    System.out.println("ERROR: No month selected for week view");
                    return;
                }
                month = convertMonthNameToNumber(selectedMonth);
                int week = Integer.parseInt(selectedWeek);

                // Determine available weeks in the selected month
                yearMonth = YearMonth.of(Integer.parseInt(year), month);
                int maxWeeks = yearMonth.atEndOfMonth().get(WeekFields.of(Locale.getDefault()).weekOfMonth());

                // Validate the selected week number
                if (week < 1 || week > maxWeeks) {
                    System.out.println("Invalid week number for the selected month.");
                    return;
                }

                // Calculate the start and end dates for the selected week
                localStartDate = yearMonth.atDay(1).with(WeekFields.of(Locale.getDefault()).weekOfMonth(), week);
                localEndDate = localStartDate.plusDays(6);

                // Ensure the last day of the week does not exceed the end of the month
                if (localEndDate.getMonthValue() != month) {
                    localEndDate = yearMonth.atEndOfMonth();
                }

                startDate = Date.valueOf(localStartDate);
                endDate = Date.valueOf(localEndDate);
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        // Retrieve color data for the pie chart
        Map<BinColor, Long> colorData = DBRead.getColorData(startDate, endDate);

        // Populate the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        // Add data for each color, even if count is 0
        for (BinColor color : BinColor.values()) {
            long count = colorData.getOrDefault(color, 0L);
            pieChartData.add(new PieChart.Data(color.name(), count));
        }
        
        pieChart.setAnimated(false);
        pieChart.getData().clear();
        pieChart.setData(pieChartData);
        
        // Apply CSS styling to pie chart slices
        pieChart.getData().forEach(data -> {
            String colorName = data.getName();
            String styleClass = "default-color" + BinColor.valueOf(colorName).ordinal();
            data.getNode().getStyleClass().add(styleClass);
        });

        // Populate the bar chart based on bin measure data
        Map<String, Map<BinColor, Long>> binMeasureData = DBRead.getBinMeasureDataForPeriod(startDate, endDate);
        
        // Debug: Print out the data keys to see what we're working with
        System.out.println("Data loaded for chart with " + binMeasureData.size() + " entries:");
        for (String key : binMeasureData.keySet()) {
            System.out.println("Key: " + key + " - Colors: " + binMeasureData.get(key).keySet());
        }
        
        if (binMeasureData.isEmpty()) {
            // Handle case when no data is available
            barChart.setAnimated(false);
            barChart.getData().clear();
            System.out.println("No bin measure data available for the selected period");
        } else {
            switch (selectedType) {
                case "Year":
                    populateMonthlyBarChart(binMeasureData, Integer.parseInt(selectedYear));
                    break;
                case "Quarters":
                    // For quarters, also show monthly breakdown
                    populateMonthlyBarChart(binMeasureData, Integer.parseInt(selectedYear));
                    break;
                case "Month":
                    String selectedMonth = ChoiceboxMonth.getValue();
                    int month = convertMonthNameToNumber(selectedMonth);
                    populateDailyBarChart(binMeasureData, Integer.parseInt(selectedYear), month);
                    break;
                default:
                    populateBarChart(binMeasureData);
                    break;
            }
        }

        // Calculate and display route data
        RouteCalc routeCalc = new RouteCalc();
        RouteCalc.AggregatedResult result = routeCalc.aggregatePeriod(localStartDate, localEndDate);

        // Format distance with 1 decimal place and add km unit
        String distanceText = String.format("%.1f km", result.getTotalDistanceSaved());
        totalDistanceLabel.setText(distanceText);

        // Format time with 1 decimal place
        String timeText;
        if (result.getTotalTimeSaved() >= 60) {
            // Convert to hours and minutes
            int hours = (int) (result.getTotalTimeSaved() / 60);
            int minutes = (int) (result.getTotalTimeSaved() % 60);
            timeText = String.format("%d h %d min", hours, minutes);
        } else {
            // Just show minutes
            timeText = String.format("%.1f min", result.getTotalTimeSaved());
        }
        totalTimeLabel.setText(timeText);
    }





    private int convertMonthNameToNumber(String monthName) {
        switch (monthName) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                throw new IllegalArgumentException("Invalid month name: " + monthName);
        }
    }


    /**
     * Populates the bar chart showing data by bins
     */
    private void populateBarChart(Map<String, Map<BinColor, Long>> data) {
        // Create series for each bin color
        XYChart.Series<String, Number> greenSeries = new XYChart.Series<>();
        greenSeries.setName("GREEN");
        XYChart.Series<String, Number> yellowSeries = new XYChart.Series<>();
        yellowSeries.setName("YELLOW");
        XYChart.Series<String, Number> redSeries = new XYChart.Series<>();
        redSeries.setName("RED");

        // Sort bin IDs to ensure consistent ordering
        List<String> sortedBinIds = new ArrayList<>(data.keySet());
        java.util.Collections.sort(sortedBinIds);

        // Add data points only for bins that have data
        for (String binId : sortedBinIds) {
            Map<BinColor, Long> colorCountMap = data.get(binId);
            
            // Check if there's any data for this bin
            long greenCount = colorCountMap.getOrDefault(BinColor.GREEN, 0L);
            long yellowCount = colorCountMap.getOrDefault(BinColor.YELLOW, 0L);
            long redCount = colorCountMap.getOrDefault(BinColor.RED, 0L);
            
            // Only add points for bins with data
            if (greenCount > 0 || yellowCount > 0 || redCount > 0) {
                greenSeries.getData().add(new XYChart.Data<>(binId, greenCount));
                yellowSeries.getData().add(new XYChart.Data<>(binId, yellowCount));
                redSeries.getData().add(new XYChart.Data<>(binId, redCount));
            }
        }

        // Update the chart
        barChart.setAnimated(false);
        barChart.getData().clear();
        
        // Add series in the correct order to match CSS styling
        barChart.getData().add(greenSeries);   // index 0 - green
        barChart.getData().add(yellowSeries);  // index 1 - yellow
        barChart.getData().add(redSeries);     // index 2 - red
        
        // Configure chart scaling based on the data
        configureBarChartScaling(greenSeries);
    }
    
    /**
     * Populates the bar chart with monthly data for a selected year
     */
    private void populateMonthlyBarChart(Map<String, Map<BinColor, Long>> data, int year) {
        // Create series for each bin color
        XYChart.Series<String, Number> greenSeries = new XYChart.Series<>();
        greenSeries.setName("GREEN");
        XYChart.Series<String, Number> yellowSeries = new XYChart.Series<>();
        yellowSeries.setName("YELLOW");
        XYChart.Series<String, Number> redSeries = new XYChart.Series<>();
        redSeries.setName("RED");
        
        // Initialize monthly data structure
        Map<Integer, Map<BinColor, Long>> monthlyData = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            monthlyData.put(month, new HashMap<>());
        }
        
        // Aggregate data by month
        for (String key : data.keySet()) {
            Map<BinColor, Long> colorCountMap = data.get(key);
            
            // Extract month from the key (binId_date)
            int month;
            try {
                // Format should be binId_YYYY-MM-DD
                String[] parts = key.split("_");
                if (parts.length >= 2) {
                    String dateStr = parts[1];
                    // Parse the date
                    java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
                    
                    // Only include data from the selected year
                    if (date.getYear() == year) {
                        month = date.getMonthValue();
                    } else {
                        // Skip data not from the selected year
                        continue;
                    }
                } else {
                    // If no date part found, try to parse from the key
                    String datePattern = ".*?(\\d{4})-(\\d{2})-(\\d{2}).*?";
                    Pattern pattern = Pattern.compile(datePattern);
                    Matcher matcher = pattern.matcher(key);
                    
                    if (matcher.find()) {
                        int keyYear = Integer.parseInt(matcher.group(1));
                        
                        // Only include data from the selected year
                        if (keyYear == year) {
                            month = Integer.parseInt(matcher.group(2));
                        } else {
                            // Skip data not from the selected year
                            continue;
                        }
                    } else {
                        // If no date found, use a default month (1)
                        month = 1;
                    }
                }
            } catch (Exception e) {
                // Default to month 1 if parsing fails
                System.out.println("Error parsing month from " + key + ": " + e.getMessage());
                month = 1;
            }
            
            // Aggregate colors by month
            Map<BinColor, Long> monthColorMap = monthlyData.get(month);
            for (BinColor color : BinColor.values()) {
                long currentCount = monthColorMap.getOrDefault(color, 0L);
                long additionalCount = colorCountMap.getOrDefault(color, 0L);
                monthColorMap.put(color, currentCount + additionalCount);
            }
        }
        
        // Add data points only for months that have data
        for (int month = 1; month <= 12; month++) {
            Map<BinColor, Long> colorCountMap = monthlyData.get(month);
            
            // Check if there's any data for this month
            long greenCount = colorCountMap.getOrDefault(BinColor.GREEN, 0L);
            long yellowCount = colorCountMap.getOrDefault(BinColor.YELLOW, 0L);
            long redCount = colorCountMap.getOrDefault(BinColor.RED, 0L);
            
            // Only add points for months with data
            if (greenCount > 0 || yellowCount > 0 || redCount > 0) {
                String monthName = java.time.Month.of(month).toString();
                // Convert to Title Case (first letter uppercase, rest lowercase)
                monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();
                
                greenSeries.getData().add(new XYChart.Data<>(monthName, greenCount));
                yellowSeries.getData().add(new XYChart.Data<>(monthName, yellowCount));
                redSeries.getData().add(new XYChart.Data<>(monthName, redCount));
            }
        }
        
        // Update the chart
        barChart.setAnimated(false);
        barChart.getData().clear();
        
        // Add series in the correct order to match CSS styling
        barChart.getData().add(greenSeries);   // index 0 - green
        barChart.getData().add(yellowSeries);  // index 1 - yellow
        barChart.getData().add(redSeries);     // index 2 - red
        
        // Configure chart scaling based on the data
        configureBarChartScaling(greenSeries);
    }
    
    /**
     * Populates the bar chart with daily data for a selected month
     */
    private void populateDailyBarChart(Map<String, Map<BinColor, Long>> data, int year, int month) {
        // Create series for each bin color
        XYChart.Series<String, Number> greenSeries = new XYChart.Series<>();
        greenSeries.setName("GREEN");
        XYChart.Series<String, Number> yellowSeries = new XYChart.Series<>();
        yellowSeries.setName("YELLOW");
        XYChart.Series<String, Number> redSeries = new XYChart.Series<>();
        redSeries.setName("RED");
        
        // Calculate days in the selected month
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        
        // Initialize daily data structure
        Map<Integer, Map<BinColor, Long>> dailyData = new HashMap<>();
        for (int day = 1; day <= daysInMonth; day++) {
            dailyData.put(day, new HashMap<>());
        }
        
        // Aggregate data by day
        for (String key : data.keySet()) {
            Map<BinColor, Long> colorCountMap = data.get(key);
            
            // Extract day from the key (binId_date)
            int day;
            try {
                // Format should be binId_YYYY-MM-DD
                String[] parts = key.split("_");
                if (parts.length >= 2) {
                    String dateStr = parts[1];
                    // Parse the date
                    java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
                    
                    // Only include data from the selected month
                    if (date.getMonthValue() == month && date.getYear() == year) {
                        day = date.getDayOfMonth();
                    } else {
                        // Skip data not from the selected month and year
                        continue;
                    }
                } else {
                    // If no date part found, try to parse from the key
                    String datePattern = ".*?(\\d{4})-(\\d{2})-(\\d{2}).*?";
                    Pattern pattern = Pattern.compile(datePattern);
                    Matcher matcher = pattern.matcher(key);
                    
                    if (matcher.find()) {
                        int keyYear = Integer.parseInt(matcher.group(1));
                        int keyMonth = Integer.parseInt(matcher.group(2));
                        
                        // Only include data from the selected month
                        if (keyMonth == month && keyYear == year) {
                            day = Integer.parseInt(matcher.group(3));
                        } else {
                            // Skip data not from the selected month and year
                            continue;
                        }
                    } else {
                        // If no date found, use a default day (1)
                        day = 1;
                    }
                }
            } catch (Exception e) {
                // Default to day 1 if parsing fails
                System.out.println("Error parsing day from " + key + ": " + e.getMessage());
                day = 1;
            }
            
            // Ensure day is within valid range
            if (day < 1 || day > daysInMonth) {
                day = 1; // Default to day 1 if out of range
            }
            
            // Aggregate colors by day
            Map<BinColor, Long> dayColorMap = dailyData.get(day);
            for (BinColor color : BinColor.values()) {
                long currentCount = dayColorMap.getOrDefault(color, 0L);
                long additionalCount = colorCountMap.getOrDefault(color, 0L);
                dayColorMap.put(color, currentCount + additionalCount);
            }
        }
        
        // Add data points only for days that have data
        for (int day = 1; day <= daysInMonth; day++) {
            Map<BinColor, Long> colorCountMap = dailyData.get(day);
            
            // Check if there's any data for this day
            long greenCount = colorCountMap.getOrDefault(BinColor.GREEN, 0L);
            long yellowCount = colorCountMap.getOrDefault(BinColor.YELLOW, 0L);
            long redCount = colorCountMap.getOrDefault(BinColor.RED, 0L);
            
            // Only add points for days with data
            if (greenCount > 0 || yellowCount > 0 || redCount > 0) {
                String dayLabel = String.valueOf(day);
                
                greenSeries.getData().add(new XYChart.Data<>(dayLabel, greenCount));
                yellowSeries.getData().add(new XYChart.Data<>(dayLabel, yellowCount));
                redSeries.getData().add(new XYChart.Data<>(dayLabel, redCount));
            }
        }
        
        // Update the chart
        barChart.setAnimated(false);
        barChart.getData().clear();
        
        // Add series in the correct order to match CSS styling
        barChart.getData().add(greenSeries);   // index 0 - green
        barChart.getData().add(yellowSeries);  // index 1 - yellow
        barChart.getData().add(redSeries);     // index 2 - red
        
        // Configure chart scaling based on the data
        configureBarChartScaling(greenSeries);
    }


    /**
     * Configures the bar chart for optimal display, scaling the x-axis appropriately
     * @param series The data series to base the scaling on
     */
    private void configureBarChartScaling(XYChart.Series<String, Number> series) {
        // Make sure axis labels are visible and chart scales properly
        barChart.getXAxis().setTickLabelRotation(45);
        barChart.setLegendVisible(true);
        
        // Adjust the CategoryAxis to better use available space
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.setGapStartAndEnd(false); // Don't add gaps at the beginning and end
        xAxis.setTickMarkVisible(true);
        xAxis.setTickLabelGap(5);
    }
    
    @FXML
    private void UploadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Get the Stage from the button
        Window stage = button.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filePath = file.getAbsolutePath();

            System.out.println("Selected File: " + filePath); // Debugging output
            CSVReader.ReadCSV(filePath);
        }
    }

    public String getFilePath() {
        return filePath; // Allow other parts of the app to get the file path
    }

    private void updateWeeks(int month) {
        int year;
        try {
            // Get selected year from ComboboxYear
            year = Integer.parseInt(ComboboxYear.getValue());
        } catch (NumberFormatException | NullPointerException e) {
            // Fallback to current year if no selection or invalid value
            year = java.time.Year.now().getValue();
        }
        YearMonth yearMonth = YearMonth.of(year, month);

        WeekFields weekFields = WeekFields.of(Locale.getDefault()); // Get local week system
        int firstWeek = yearMonth.atDay(1).get(weekFields.weekOfWeekBasedYear());
        int lastWeek = yearMonth.atEndOfMonth().get(weekFields.weekOfWeekBasedYear());

        List<String> weeks = new ArrayList<>();
        for (int i = firstWeek; i <= lastWeek; i++) {
            weeks.add(String.valueOf(i));
        }

        ChoiceboxWeek.setItems(FXCollections.observableArrayList(weeks));
    }

}
