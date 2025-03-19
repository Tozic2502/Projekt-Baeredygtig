package org.example.projektbaeredygtig;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.Year;

public class Controller {
    @FXML ComboBox<String> ComboboxYear, TypeBox;
    @FXML ChoiceBox ChoiceboxMonth, ChoiceboxWeek;
    @FXML Button ModeToggle;
    @FXML GridPane gridPane;
    @FXML Label MonthLabel;

    private boolean isAdvancedMode = false;

    private TextField TypeField = new TextField();
    private TextField yearField = new TextField();
    private TextField monthField = new TextField();
    private TextField weekField = new TextField();

    @FXML
    private void toggleAdvancedMode() {
        isAdvancedMode = !isAdvancedMode;

        if (isAdvancedMode) {
            switchToTextFields();
        } else {
            switchToComboBoxes();
        }
    }

    private void switchToTextFields() {
        gridPane.getChildren().removeAll(TypeBox, ComboboxYear, ChoiceboxMonth, ChoiceboxWeek);
        gridPane.add(TypeField, GridPane.getColumnIndex(TypeBox), GridPane.getRowIndex(TypeBox));
        gridPane.add(yearField, GridPane.getColumnIndex(ComboboxYear), GridPane.getRowIndex(ComboboxYear));
        gridPane.add(monthField, GridPane.getColumnIndex(ChoiceboxMonth), GridPane.getRowIndex(ChoiceboxMonth));
        gridPane.add(weekField, GridPane.getColumnIndex(ChoiceboxWeek), GridPane.getRowIndex(ChoiceboxWeek));
    }

    private void switchToComboBoxes() {
        gridPane.getChildren().removeAll(TypeField, yearField, monthField, weekField);
        gridPane.add(TypeBox, GridPane.getColumnIndex(TypeField), GridPane.getRowIndex(TypeField));
        gridPane.add(ComboboxYear, GridPane.getColumnIndex(yearField), GridPane.getRowIndex(yearField));
        gridPane.add(ChoiceboxMonth, GridPane.getColumnIndex(monthField), GridPane.getRowIndex(monthField));
        gridPane.add(ChoiceboxWeek, GridPane.getColumnIndex(weekField), GridPane.getRowIndex(weekField));
    }

    @FXML
    private void typeChoice() {
        String selectedType = TypeBox.getValue(); // Assuming TypeBox is a ComboBox or ChoiceBox

        if ("Year".equals(selectedType)) {
            ComboboxYear.setVisible(true);
            ChoiceboxMonth.setVisible(false);
            ChoiceboxWeek.setVisible(false);
        } else if ("Quarters".equals(selectedType)) {
            ComboboxYear.setVisible(true);
            ChoiceboxMonth.setVisible(true);
            ChoiceboxMonth.getItems().setAll("Q1", "Q2", "Q3", "Q4");
            MonthLabel.setText("Choose Quarter");
            ChoiceboxWeek.setVisible(false);
        } else if ("Month".equals(selectedType)) {
            ComboboxYear.setVisible(true);
            ChoiceboxMonth.setVisible(true);
            ChoiceboxMonth.getItems().setAll(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            );
            MonthLabel.setText("Choose Month");
            ChoiceboxWeek.setVisible(false);
        } else if ("Week".equals(selectedType)) {
            ComboboxYear.setVisible(true);
            ChoiceboxMonth.setVisible(true);
            ChoiceboxWeek.setVisible(true);
        }
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
