package org.example.projektbaeredygtig;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.Year;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private void onAdvancedModeClick() {
        isAdvancedMode = !isAdvancedMode;

        ComboBoxYear.setEditable(isAdvancedMode);
        ComboBoxMonth.setEditable(isAdvancedMode);
        ComboBoxWeek.setEditable(isAdvancedMode);
    }

    @FXML
    private void typeLabel() {
        if (Year) {
            ComboBoxYear.setVisible(true);
            ComboBoxMonth.setVisible(false);
            ComboBoxWeek.setVisible(false);
        } else if (Quarters) {
            ComboboxYear.setVisible(true);
            ComboBoxMonth.setVisible(true);
            ComboBoxMonth.getItems().setAll("Q1", "Q2", "Q3", "Q4");
            chooseLabel.setText("Choose Quarter");
            ComboboxWeek.setVisible(false);
        } else if (Month) {
            ComboboxYear.setVisible(true);
            ComboBoxMonth.setVisible(true);
            chooseLabel.setText("Choose Month");
        } else if (Week) {
            ComboboxYear.setVisible(true);
            ComboBoxMonth.setVisible(true);
            ComboBoxWeek.setVisible(true);

        }
    }
    @FXML
    private void optimiseLabel() {

        //display label
    }
    private void calcOptimise(){
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
