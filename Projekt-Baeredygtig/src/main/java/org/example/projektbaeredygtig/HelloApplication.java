package org.example.projektbaeredygtig;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projektbaeredygtig.DB.DB;
import org.example.projektbaeredygtig.DB.DBCreate;
import org.example.projektbaeredygtig.DB.DBRead;

import java.awt.*;
import java.io.IOException;

import static org.example.projektbaeredygtig.DB.DBCreate.createMeasurement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();


    }
}