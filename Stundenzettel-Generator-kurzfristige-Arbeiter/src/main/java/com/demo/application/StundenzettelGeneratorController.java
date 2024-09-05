package com.demo.application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.demo.helper.Constants.*;
import static com.demo.helper.Utils.*;
import static com.demo.helper.Validation.*;

public class StundenzettelGeneratorController implements Initializable {

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

    public boolean btnExcelListeClicked = true;

    private boolean fieldsExcelListeValid;

    boolean btnEinzelerstellungClicked = false;

    private boolean fieldsEinzelerstellungValid;

    double svBrutto;

    double stundenlohn;

    //================================================== VIEW METHODS ======================================================


    @FXML
    protected void chooseInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(CHOOSER_INPUT_TITLE);
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            textfieldInputPath.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void chooseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(CHOOSER_OUTPUT_TITLE);
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            textfieldOutputPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void switchToViewExcel() {
        // Elemente der Einzelerstellung-Ansicht werden ausgeblendet
        hboxEinzelerstellung.setVisible(false);
        lblNameEmpty.setVisible(false);
        lblMitarbeiternummerEmpty.setVisible(false);
        lblFalschesFormatSvBrutto.setVisible(false);
        lblFalschesFormatAbrechnungsmonat.setVisible(false);
        // Elemente der Excel-Liste-Ansicht werden eingeblendet
        boxExcelListeInputPath.setVisible(true);
        lblValidationInputPath.setVisible(false);
        lblSchlussnachricht.setVisible(false);
        lblValidationOutputPath.setVisible(false);
        lblValidationStundenlohn.setVisible(false);
        textfieldStundenlohn.setText("");
        textfieldInputPath.setText("");
        // Boolean-Werte werden umgestellt
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

        stundenlohn = Double.parseDouble(textfieldStundenlohn.getText().replace(",", "."));

        // Prüfung Feld Output Path
        if (!isTextfieldFilled(textfieldOutputPath)) {
            setTextfieldInvalid(textfieldOutputPath, lblValidationOutputPath, VALIDATION_EMPTY_FIELD);
            fieldsExcelListeValid = false;
        } else {
            setTextfieldValid(textfieldOutputPath, lblValidationOutputPath);

            if (!isPathADirectory(textfieldOutputPath)) {
                setTextfieldInvalid(textfieldOutputPath, lblValidationOutputPath, VALIDATION_WRONG_OUTPUT_PATH);
                fieldsExcelListeValid = false;
            } else {
                setTextfieldValid(textfieldOutputPath, lblValidationOutputPath);
            }
        }

        // Prüfung Feld Stundenlohn
        if (!isTextfieldFilled(textfieldStundenlohn)) {
            setTextfieldInvalid(textfieldStundenlohn, lblValidationStundenlohn, VALIDATION_EMPTY_FIELD);
            fieldsExcelListeValid = false;
        } else {
            setTextfieldValid(textfieldStundenlohn, lblValidationStundenlohn);

            if (!isStundenlohnValid(textfieldStundenlohn)) {
                setTextfieldInvalid(textfieldStundenlohn, lblValidationStundenlohn, VALIDATION_WRONG_INPUT);
                fieldsExcelListeValid = false;
            } else {
                setTextfieldValid(textfieldStundenlohn, lblValidationStundenlohn);
            }
        }

        // Excel Liste ausgewählt
        if (btnExcelListeClicked) {
            fieldsExcelListeValid = true;
            fieldsEinzelerstellungValid = false;

            // Prüfung Feld Input Path
            if (!isTextfieldFilled(textfieldInputPath)) {
                setTextfieldInvalid(textfieldInputPath, lblValidationInputPath, VALIDATION_EMPTY_FIELD);
                fieldsExcelListeValid = false;
            } else {
                setTextfieldValid(textfieldInputPath, lblValidationInputPath);

                if (!isPathAnExcelFile(textfieldInputPath)) {
                    setTextfieldInvalid(textfieldInputPath, lblValidationInputPath, VALIDATION_WRONG_INPUT_PATH);
                    fieldsExcelListeValid = false;
                } else {
                    setTextfieldValid(textfieldInputPath, lblValidationInputPath);
                }
            }


            // Bei Klick auf Button OK: Wenn alle Felder gültig, dann weiter
            if (fieldsExcelListeValid) {
                System.out.println("INFO: Alle Felder sind gültig.");

                //if( alles andere auch passt, dann Eingabe-Excel-Datei einlesen, Ausgabe-Excel-Datei mit Stundenzettel-Vorlage erstellen und Stundenzettel-PDF generieren )
                ExcelListeReader excelListeReader = new ExcelListeReader(textfieldInputPath.getText());
                excelListeReader.getListOfAbrechnungsmonate(stundenlohn);

                setMessageSuccess(lblSchlussnachricht, VALIDATION_SUCCESS_PDF);

                // (Neuer) Stundenlohn-Wert wird in die lokale Stundenlohn-Datei gespeichert
                saveStundenlohnToDatei(textfieldStundenlohn.getText());

            } else {
                System.out.println("INFO: Nicht alle Felder sind gültig. Felder prüfen.");
                setMessageFailed(lblSchlussnachricht, VALIDATION_FAILED_PDF);
            }

        }

        // Einzelerstellung ausgewählt
        if (btnEinzelerstellungClicked) {
            fieldsEinzelerstellungValid = true;

            svBrutto = Double.parseDouble(textfieldSvBrutto.getText().replace(",", "."));

            // Prüfung Feld Abrechnungsmonat
            if (!isTextfieldFilled(textfieldAbrechnungsmonat)) {
                setTextfieldInvalid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            } else {
                setTextfieldValid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat);
                if (isValidDateFormat(textfieldAbrechnungsmonat.getText())) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
                    YearMonth abrechnungsmonat = YearMonth.parse(textfieldAbrechnungsmonat.getText(), formatter);
                    if (!(abrechnungsmonat.getYear() <= LocalDate.now().getYear())) {
                        setTextfieldInvalid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat, VALIDATION_FUTURE_DATE);
                        textfieldAbrechnungsmonat.setPromptText("Format: &quot;yyyy/MM&quot;");
                        fieldsEinzelerstellungValid = false;
                    }
                } else {
                    setTextfieldInvalid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat, VALIDATION_WRONG_DATE_FORMAT);
                    fieldsEinzelerstellungValid = false;
                }
            }
            // Prüfung Feld Mitarbeiternummer
            if (!isTextfieldFilled(textfieldMitarbeiternummer)) {
                setTextfieldInvalid(textfieldMitarbeiternummer, lblMitarbeiternummerEmpty, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            } else {
                setTextfieldValid(textfieldMitarbeiternummer, lblMitarbeiternummerEmpty);
            }

            // Prüfung Feld Name
            if (!isTextfieldFilled(textfieldName)) {
                setTextfieldInvalid(textfieldName, lblNameEmpty, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            } else {
                setTextfieldValid(textfieldName, lblNameEmpty);
            }

            // Prüfung Feld SvBrutto
            if (!isTextfieldFilled(textfieldSvBrutto)) {
                setTextfieldInvalid(textfieldSvBrutto, lblFalschesFormatSvBrutto, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            } else {
                setTextfieldValid(textfieldSvBrutto, lblFalschesFormatSvBrutto);
                try {
                    if (svBrutto < 0) {
                        setTextfieldInvalid(textfieldSvBrutto, lblFalschesFormatSvBrutto, VALIDATION_NEGATIVE_SVBRUTTO);
                    }
                } catch (NumberFormatException | NullPointerException nfe) {
                    setTextfieldInvalid(textfieldSvBrutto, lblFalschesFormatSvBrutto, VALIDATION_NO_NUMBER);
                }
            }


            //Wenn alles geklappt hat
            if (fieldsEinzelerstellungValid) {

                if (svBrutto >= stundenlohn) {

                    saveStundenlohnToDatei(textfieldStundenlohn.getText());
                    setMessageSuccess(lblSchlussnachricht, VALIDATION_SUCCESS_PDF);

                    Einzelerstellung einzelerstellung = new Einzelerstellung(textfieldAbrechnungsmonat.getText(), textfieldMitarbeiternummer.getText(), textfieldSvBrutto.getText(), textfieldName.getText());
                    einzelerstellung.writeToExcel(textfieldOutputPath.getText(), textfieldStundenlohn.getText(), checkboxErsetzen.isSelected());

                }
            } else {
                setMessageFailed(lblSchlussnachricht, VALIDATION_FAILED_PDF);
            }

        }


    }


//================================================== CLASS METHODS =====================================================

    // Alles innerhalb dieser Methode wird direkt nach Programmstart durchgeführt ("implements Initializable" notwendig)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createIfNotExistingLocalFileStundenlohn();
        loadStundenlohnIntoGuiFromDatei(textfieldStundenlohn);
        createIfNotExistingLocalFileLogo();
        createIfNotExistingLocalFileStundenzettelVorlage();
    }
}