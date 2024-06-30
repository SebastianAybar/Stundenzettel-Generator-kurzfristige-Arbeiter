package com.example.stundenzettelgeneratorkurzfristigearbeiter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class stundenzettelGeneratorController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}