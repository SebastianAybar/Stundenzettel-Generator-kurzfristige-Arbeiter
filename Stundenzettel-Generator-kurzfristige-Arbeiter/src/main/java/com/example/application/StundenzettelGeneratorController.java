package com.example.application;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.lang.reflect.Field;

public class StundenzettelGeneratorController {
    @FXML
    private Label lblStundenlohn;
    @FXML
    private Label lblDatei;
    @FXML
    private TextField textFieldStundenlohn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button excelList;
    @FXML
    private Button einzelerstellung;
    @FXML
    private Button btnChooseInputFile;
    @FXML
    private Button btnChooseOutputFile;
    @FXML
    private Label inputFile;
    @FXML
    private Label outputFile;
    @FXML
    private TextField inputPathTextField;
    @FXML
    private TextField outputPathTextField;
    @FXML
    private CheckBox replaceFile;
    @FXML
    private Separator separator1;
    @FXML
    private Separator separator2;
    @FXML
    private Button calculate;
    @FXML
    private FontAwesomeIconView icnChooseInputFile;
    @FXML
    private FontAwesomeIconView icnChooseOutputFile;
    @FXML
    private HBox hboxExcelListeAnsicht;
    @FXML
    private HBox hboxEinzelerstellung;
    @FXML
    private TextField textFieldSvBrutto;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldMitarbeiternummer;
    @FXML
    private TextField textFieldAbrechnungsmonat;
    @FXML
    private Label lblAbrechnungsmonat;
    @FXML
    private Label lblMitarbeiternummer;
    @FXML
    private Label lblName;
    @FXML
    private Label lblSvBrutto;
    @FXML
    private Label lblFalschesFormatAbrechnungsmonat;
    @FXML
    private Label lblFalschesFormatSvBrutto;
    @FXML
    private Label lblMitarbeiternummerEmpty;
    @FXML
    private Label lblNameEmpty;
    @FXML
    private Label lblFalschesFormatStundenlohn;
    @FXML
    private Label lblDateiNichtAkzeptiert;
    @FXML
    private Label lblFalscherPathOutput;
    @FXML
    private Label lblSchlussnachricht;
    @FXML
    private CheckBox checkboxErsetzen;

    Boolean excelListClicked = true;
    Boolean einzelerstellungClicked = false;

    @FXML
    protected void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Datei auswählen");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            inputPathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Ordner auswählen");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            outputPathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void switchViewExcel() {
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
        textFieldStundenlohn.setText("");
        inputPathTextField.setText("");
        //Boolean's neu bestimmen
        einzelerstellungClicked = false;
        excelListClicked = true;
    }

    @FXML
    public void switchViewEinzelerstellung() {
        hboxExcelListeAnsicht.setVisible(false);

        hboxEinzelerstellung.setVisible(true);
        textFieldMitarbeiternummer.setText("");
        textFieldAbrechnungsmonat.setText("");
        textFieldName.setText("");
        textFieldSvBrutto.setText("");

        excelListClicked = false;
        einzelerstellungClicked = true;
    }

    @FXML
    protected void btnOkClicked() {
        if(excelListClicked){
            if(!isTextfieldFilled(inputPathTextField, lblDateiNichtAkzeptiert, "Leeres Feld")) return;
            if(!isPathExcelFile(inputPathTextField, lblDateiNichtAkzeptiert, "Der Pfad führt nicht zu einer gültigen Excel-Datei.")) return;

            isTextfieldFilled(outputPathTextField, lblFalscherPathOutput, "Leeres Feld");
            isTextfieldFilled(textFieldStundenlohn, lblFalschesFormatStundenlohn, "Leeres Feld");
        }

        if(einzelerstellungClicked){

        }
    }

    private boolean isTextfieldFilled(TextField field, Label lblErrorMessage, String errorMessage) {
        if (field.getText().isEmpty()) {
            lblErrorMessage.setText(errorMessage);
            lblErrorMessage.setVisible(true);
            return false;
        } else {
            lblErrorMessage.setVisible(false);
            return true;
        }
    }

    private boolean isPathExcelFile(TextField field, Label lblErrorMessage, String errorMessage) {
        if(field.getText().endsWith(".xlsx") || field.getText().endsWith(".xlsm") || field.getText().endsWith(".csv")) {
            lblErrorMessage.setText("Ungültige Eingabe");
            lblErrorMessage.setVisible(false);
            return true;
        } else {
            lblErrorMessage.setText(errorMessage);
            lblErrorMessage.setVisible(true);
            return false;
        }
    }
}