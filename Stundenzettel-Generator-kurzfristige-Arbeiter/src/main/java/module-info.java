module com.example.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.example.application to javafx.fxml;
    exports com.example.application;
}