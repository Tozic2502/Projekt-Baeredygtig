package org.example.projektbaeredygtig;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1275, 720);

        System.out.println(getClass().getResource("/styles/styles.css"));

        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());


        stage.setTitle("Green Route");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

    }
}