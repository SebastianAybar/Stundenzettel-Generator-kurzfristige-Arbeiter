package com.example.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class StundenzettelGeneratorController {
    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnEinzelerstellung;

    @FXML
    private Button btnExcelList;

    @FXML
    private Button btnInputPath;

    @FXML
    private Button btnOutputPath;

    @FXML
    private CheckBox checkboxErsetzen;

    @FXML
    private HBox hboxEinzelerstellung;

    @FXML
    private HBox hboxExcelListeAnsicht;

    @FXML
    private Label lblAbrechnungsmonat;

    @FXML
    private Label lblDateiNichtAkzeptiert;

    @FXML
    private Label lblFalscherPathOutput;

    @FXML
    private Label lblFalschesFormatAbrechnungsmonat;

    @FXML
    private Label lblFalschesFormatStundenlohn;

    @FXML
    private Label lblFalschesFormatSvBrutto;

    @FXML
    private Label lblInputPath;

    @FXML
    private Label lblMitarbeiternummer;

    @FXML
    private Label lblMitarbeiternummerEmpty;

    @FXML
    private Label lblName;

    @FXML
    private Label lblNameEmpty;

    @FXML
    private Label lblOutputPath;

    @FXML
    private Label lblSchlussnachricht;

    @FXML
    private Label lblStundenlohn;

    @FXML
    private Label lblSvBrutto;

    @FXML
    private TextField textfieldAbrechnungsmonat;

    @FXML
    private TextField textfieldInputPath;

    @FXML
    private TextField textfieldMitarbeiternummer;

    @FXML
    private TextField textfieldName;

    @FXML
    private TextField textfieldOutputPath;

    @FXML
    private TextField textfieldStundenlohn;

    @FXML
    private TextField textfieldSvBrutto;


    boolean btnExcelListClicked = true;

    boolean btnEinzelerstellungClicked = false;


    @FXML
    protected void chooseInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Datei auswählen");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            textfieldInputPath.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void chooseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Ordner auswählen");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            textfieldOutputPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void switchToViewExcel() {
        //Muss aus Einzelerstellung ausgeblendet werden
        hboxEinzelerstellung.setVisible(false);
        lblNameEmpty.setVisible(false);
        lblMitarbeiternummerEmpty.setVisible(false);
        lblFalschesFormatSvBrutto.setVisible(false);
        lblFalschesFormatAbrechnungsmonat.setVisible(false);
        //Muss in ExcelList angepasst werden
        hboxExcelListeAnsicht.setVisible(true);
        lblDateiNichtAkzeptiert.setVisible(false);
        lblSchlussnachricht.setVisible(false);
        lblFalscherPathOutput.setVisible(false);
        lblFalschesFormatStundenlohn.setVisible(false);
        textfieldStundenlohn.setText("");
        textfieldInputPath.setText("");
        //Boolean's neu bestimmen
        btnEinzelerstellungClicked = false;
        btnExcelListClicked = true;
    }

    @FXML
    public void switchToViewEinzelerstellung() {
        hboxExcelListeAnsicht.setVisible(false);

        hboxEinzelerstellung.setVisible(true);
        textfieldMitarbeiternummer.setText("");
        textfieldAbrechnungsmonat.setText("");
        textfieldName.setText("");
        textfieldSvBrutto.setText("");

        btnExcelListClicked = false;
        btnEinzelerstellungClicked = true;
    }

    @FXML
    protected void btnConfirmClicked() {
        if(btnExcelListClicked){
            if(!isTextfieldFilled(textfieldInputPath, lblDateiNichtAkzeptiert, "Leeres Feld")) return;
            if(!isPathExcelFile(textfieldInputPath, lblDateiNichtAkzeptiert, "Der Pfad führt nicht zu einer gültigen Excel-Datei.")) return;

//            isTextfieldFilled(textfieldOutputPath, lblFalscherPathOutput, "Leeres Feld");
//            isTextfieldFilled(textfieldStundenlohn, lblFalschesFormatStundenlohn, "Leeres Feld");
        }

        if(btnEinzelerstellungClicked){

        }
    }

    private boolean isTextfieldFilled(TextField field, Label lblErrorMessage, String errorMessage) {
        if (!field.getText().isEmpty()) {
            lblErrorMessage.setVisible(false);
            return true;
        } else {
            lblErrorMessage.setText(errorMessage);
            lblErrorMessage.setVisible(true);
            return false;
        }
    }

    private boolean isPathExcelFile(TextField field, Label lblErrorMessage, String errorMessage) {
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