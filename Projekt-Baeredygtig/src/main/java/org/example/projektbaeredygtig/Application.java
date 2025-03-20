package org.example.projektbaeredygtig;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

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
        launch();
        // Create an instance of RouteCalc
        RouteCalc calc = new RouteCalc();

        // Add some dummy records:
        // For example, assume:
        // - Record 1: March 15, 2025 using "Drejby til Sønderby" (index 1)
        // - Record 2: March 16, 2025 using "Frem og tilbage Broager" (index 0)
        // - Record 3: March 17, 2025 using "Drejby til Østerby og Østerby til Sønderby" (index 2)
        // - Record 4: March 18, 2025 using "Drejby til Sønderkobbel Strand Camping" (index 3)
        calc.addRouteRecord(LocalDate.of(2025, 3, 15), 1);
        calc.addRouteRecord(LocalDate.of(2025, 3, 16), 0);
        calc.addRouteRecord(LocalDate.of(2025, 3, 17), 2);
        calc.addRouteRecord(LocalDate.of(2025, 3, 18), 3);

        // Print all records to see individual calculations
        System.out.println("Daily Route Records:");
        calc.printAllRecords();

        // Aggregate for a period, e.g., from March 15, 2025 to March 18, 2025
        RouteCalc.AggregatedResult agg = calc.aggregatePeriod(LocalDate.of(2025, 3, 15),
                LocalDate.of(2025, 3, 18));
        System.out.println("\nAggregated Results (March 15, 2025 - March 18, 2025):");
        System.out.printf("Total Driven Distance: %.1f km%n", agg.getTotalDrivenDistance());
        System.out.printf("Total Driven Time: %.1f min%n", agg.getTotalDrivenTime());
        System.out.printf("Total Distance Saved: %.1f km%n", agg.getTotalDistanceSaved());
        System.out.printf("Total Time Saved: %.1f min%n", agg.getTotalTimeSaved());
        System.out.println("Days Driven: " + agg.getDaysDriven());
    }

}