package org.example.projektbaeredygtig;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projektbaeredygtig.DBPackage.DBRead;

import java.io.IOException;
import java.sql.Date;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1275, 720);

        System.out.println(getClass().getResource("/styles/styles.css"));

        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());


        stage.setTitle("Green Route");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();

        System.out.println(DBRead.getMeasurement(1, Date.valueOf("2025-06-23")));
    }
}