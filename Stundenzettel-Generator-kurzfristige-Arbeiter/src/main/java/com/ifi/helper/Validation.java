package com.demo.helper;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Validation {

    public static boolean isTextfieldFilled(TextField field, Label lblErrorMessage, String errorMessage) {
        if (!field.getText().isEmpty()) {
            lblErrorMessage.setVisible(false);
            return true;
        } else {
            lblErrorMessage.setText(errorMessage);
            lblErrorMessage.setVisible(true);
            return false;
        }
    }

    public static boolean isPathExcelFile(TextField field, Label lblErrorMessage, String errorMessage) {
        if(field.getText().endsWith(".xlsx") || field.getText().endsWith(".xlsm") || field.getText().endsWith(".csv")) {
            lblErrorMessage.setVisible(false);
            return true;
        } else {
            lblErrorMessage.setText(errorMessage);
            lblErrorMessage.setVisible(true);
            return false;
        }
    }
}
