module com.demo.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.apache.poi.poi;
    requires de.focus_shift.jollyday.core;
    requires de.focus_shift.jollyday.jaxb;
    requires commons.math3;
    requires itextpdf;

    opens com.demo.application to javafx.fxml;
    exports com.demo.application;
    exports com.demo.helper;
    opens com.demo.helper to javafx.fxml;
}