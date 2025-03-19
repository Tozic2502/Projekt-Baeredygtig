package org.example.projektbaeredygtig;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.Year;

public class Controller {
    @FXML ComboBox<String> ComboboxYear, TypeBox;
    @FXML ChoiceBox<String> ChoiceboxMonth, ChoiceboxWeek;
    @FXML Button ModeToggle;
    @FXML GridPane gridPane;
    @FXML Label MonthLabel;
    private boolean isAdvancedMode = false;
    private TextField TypeField = new TextField();
    private TextField yearField = new TextField();
    private TextField monthField = new TextField();
    private TextField weekField = new TextField();

    @FXML void initialize() {

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
            typeChoicetext();
        } else {
            System.out.println("Switch Mode box");
            switchToComboBoxes();
            typeChoicebox();
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
        gridPane.add(TypeField, columnIndexTypeBox, rowIndexTypeBox);
        gridPane.add(yearField, columnIndexComboboxYear, rowIndexComboboxYear);
        gridPane.add(monthField, columnIndexChoiceboxMonth, rowIndexChoiceboxMonth);
        gridPane.add(weekField, columnIndexChoiceboxWeek, rowIndexChoiceboxWeek);

    }

    private void switchToComboBoxes() {
        // Get the column and row indices, default to 0 if null
        int columnIndexTypeField = GridPane.getColumnIndex(TypeField) != null ? GridPane.getColumnIndex(TypeField) : 0;
        int rowIndexTypeField = GridPane.getRowIndex(TypeField) != null ? GridPane.getRowIndex(TypeField) : 0;

        int columnIndexYearField = GridPane.getColumnIndex(yearField) != null ? GridPane.getColumnIndex(yearField) : 0;
        int rowIndexYearField = GridPane.getRowIndex(yearField) != null ? GridPane.getRowIndex(yearField) : 0;

        int columnIndexMonthField = GridPane.getColumnIndex(monthField) != null ? GridPane.getColumnIndex(monthField) : 0;
        int rowIndexMonthField = GridPane.getRowIndex(monthField) != null ? GridPane.getRowIndex(monthField) : 0;

        int columnIndexWeekField = GridPane.getColumnIndex(weekField) != null ? GridPane.getColumnIndex(weekField) : 0;
        int rowIndexWeekField = GridPane.getRowIndex(weekField) != null ? GridPane.getRowIndex(weekField) : 0;

        // Remove the TextFields
        gridPane.getChildren().removeAll(TypeField, yearField, monthField, weekField);

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
    private void typeChoicetext() {

    }
    @FXML
    private void optimiseLabel() {

        //display label
    }
    private void calcOptimise(){
        int fullRoutekm = 80;
        int fullRoutetime = 90;

        //calc functions for label

    }
    @FXML
    private void changeBarchart(){
        //input data and change for the bar chart

    }
    @FXML
    private void changePieChart(){
        //input data and change
    }

}
