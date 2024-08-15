package com.example.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ApplicationStart extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationStart.class.getResource("stundenzettelGenerator.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm();
        scene.getStylesheets().add(css);
        Image image = new Image("/clock.png");
        stage.getIcons().add(image);
        stage.setTitle("Stundenzettel Generator für kurzfristige Arbeiter");
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(Constants.APPLICATION_WINDOW_WIDTH);
        stage.setMinHeight(Constants.APPLICATION_WINDOW_HEIGHT);
    }

    public static void main(String[] args) {
        launch();
    }
}