package com.demo.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Validation {

    public static boolean errorDisplayed = false;

    // Allgemeine Validierungen

    public static boolean isTextfieldFilled(TextField field) {
        return !field.getText().isEmpty();
    }

    public static boolean isPathADirectory(TextField field) {
        File directory = new File(field.getText());

        return directory.exists() && directory.isDirectory();
    }

    public static boolean isStundenlohnValid(TextField field) {
        try {
            double stundenlohn = Double.parseDouble(field.getText().replace(",", "."));

            return stundenlohn >= 0;

        } catch (NumberFormatException e) {
            System.out.println("Aus dem Stundenlohn-Feld konnte kein gültiger Double entnommen werden");
            return false;
        } catch (NullPointerException e) {
            System.out.println("Stundenlohn-Feld ist NULL");
            return false;
        }
    }

    public static void setTextfieldValid(TextField field, Label lblErrorMessage) {
        Border borderValid = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, null, new BorderWidths(1)));
        field.setBorder(borderValid);
        lblErrorMessage.setVisible(false);
    }

    public static void setTextfieldInvalid(TextField field, Label lblErrorMessage, String errorMessage) {
        Border borderValid = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));
        field.setBorder(borderValid);
        lblErrorMessage.setText(errorMessage);
        lblErrorMessage.setVisible(true);
    }

    public static void setMessageSuccess(Label lblSuccessMessage, String successMessage) {
        lblSuccessMessage.setText(successMessage);
        lblSuccessMessage.setTextFill(Color.valueOf("00c300"));
        lblSuccessMessage.setVisible(true);
    }

    public static void setMessageFailed(Label lblFailedMessage, String failedMessage) {
        lblFailedMessage.setText(failedMessage);
        lblFailedMessage.setTextFill(Color.RED);
        lblFailedMessage.setVisible(true);
    }

    public static void displayErrorInGui(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        errorDisplayed = true;
    }

    // Validierungen für Excel-Liste

    public static boolean isPathAnExcelFile(TextField field) {
        if(field.getText().endsWith(".xlsx") || field.getText().endsWith(".xlsm") || field.getText().endsWith(".xls")
                || field.getText().endsWith(".csv")) {
            String pathInput = field.getText();
            try {
                FileInputStream fileInputExcel = new FileInputStream(pathInput);
                return true;
            } catch (FileNotFoundException e) {
                return false;
            }
        } else return false;
    }

    public static boolean isSvBruttoValid(String svBrutto) {
        try {
            Double.parseDouble(svBrutto);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }


    // Validierungen für Einzelerstellung

    public static boolean isValidDateFormat(String text) {
        return text.matches("\\d{4}/\\d{2}");
    }


}
