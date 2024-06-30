module com.example.stundenzettelgeneratorkurzfristigearbeiter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.example.stundenzettelgeneratorkurzfristigearbeiter to javafx.fxml;
    exports com.example.stundenzettelgeneratorkurzfristigearbeiter;
}