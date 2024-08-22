package com.demo.application;

import com.demo.helper.Constants;
import com.demo.helper.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class StundenzettelGeneratorController {

//================================================== VIEW VARIABLES ====================================================
    @FXML
    private VBox boxExcelListeInputPath;

    @FXML
    private VBox boxOutputPath;

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
    private Label lblAbrechnungsmonat;

    @FXML
    private Label lblValidationOutputPath;

    @FXML
    private Label lblFalschesFormatAbrechnungsmonat;

    @FXML
    private Label lblValidationStundenlohn;

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
    private Label lblValidationInputPath;

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

//================================================== CLASS VARIABLES ===================================================
    // CLASS VARIABLES
    public boolean btnExcelListeClicked = true;

    private boolean fieldsExcelListeValid;

    boolean btnEinzelerstellungClicked = false;

//================================================== VIEW METHODS ======================================================
    @FXML
    protected void chooseInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Constants.CHOOSER_INPUT_TITLE);
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            textfieldInputPath.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void chooseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(Constants.CHOOSER_OUTPUT_TITLE);
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
        boxExcelListeInputPath.setVisible(true);
        lblValidationInputPath.setVisible(false);
        lblSchlussnachricht.setVisible(false);
        lblValidationOutputPath.setVisible(false);
        lblValidationStundenlohn.setVisible(false);
        textfieldStundenlohn.setText("");
        textfieldInputPath.setText("");
        //Boolean's neu bestimmen
        btnEinzelerstellungClicked = false;
        btnExcelListeClicked = true;
    }

    @FXML
    public void switchToViewEinzelerstellung() {
        boxExcelListeInputPath.setVisible(false);

        hboxEinzelerstellung.setVisible(true);
        textfieldMitarbeiternummer.setText("");
        textfieldAbrechnungsmonat.setText("");
        textfieldName.setText("");
        textfieldSvBrutto.setText("");

        btnExcelListeClicked = false;
        btnEinzelerstellungClicked = true;
    }

    @FXML
    protected void btnConfirmClicked() {
        if (btnExcelListeClicked) {
            fieldsExcelListeValid = true;

            if (!Validation.isTextfieldFilled(textfieldInputPath)) {
                Validation.setTextfieldInvalid(textfieldInputPath, lblValidationInputPath, Constants.VALIDATION_EMPTY);
                fieldsExcelListeValid = false;
            } else {
                Validation.setTextfieldValid(textfieldInputPath, lblValidationInputPath);

                if (!Validation.isPathExcelFile(textfieldInputPath)) {
                    Validation.setTextfieldInvalid(textfieldInputPath, lblValidationInputPath, Constants.VALIDATION_WRONG_INPUT_PATH);
                    fieldsExcelListeValid = false;
                } else {
                    Validation.setTextfieldValid(textfieldInputPath, lblValidationInputPath);
                }
            }

            if (!Validation.isTextfieldFilled(textfieldOutputPath)) {
                Validation.setTextfieldInvalid(textfieldOutputPath, lblValidationOutputPath, Constants.VALIDATION_EMPTY);
                fieldsExcelListeValid = false;
            } else {
                Validation.setTextfieldValid(textfieldOutputPath, lblValidationOutputPath);
            }

            if (!Validation.isTextfieldFilled(textfieldStundenlohn)) {
                Validation.setTextfieldInvalid(textfieldStundenlohn, lblValidationStundenlohn, Constants.VALIDATION_EMPTY);
                fieldsExcelListeValid = false;
            } else {
                Validation.setTextfieldValid(textfieldStundenlohn, lblValidationStundenlohn);

                if (!Validation.isValidStundenlohn(textfieldStundenlohn)) {
                    Validation.setTextfieldInvalid(textfieldStundenlohn, lblValidationStundenlohn, Constants.VALIDATION_WRONG_INPUT);
                    fieldsExcelListeValid = false;
                } else {
                    Validation.setTextfieldValid(textfieldStundenlohn, lblValidationStundenlohn);
                }
            }

            lblSchlussnachricht.setVisible(fieldsExcelListeValid);
        }

        if (btnEinzelerstellungClicked) {

        }
    }
}