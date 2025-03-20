module org.example.projektbaeredygtig {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;

    opens org.example.projektbaeredygtig to javafx.fxml;
    exports org.example.projektbaeredygtig;
    exports org.example.projektbaeredygtig.DB;
    opens org.example.projektbaeredygtig.DB to javafx.fxml;
}