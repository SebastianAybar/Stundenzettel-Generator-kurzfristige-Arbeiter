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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

import static com.demo.helper.Constants.*;
import static com.demo.helper.Utils.*;
import static com.demo.helper.Validation.*;
import static java.lang.Double.parseDouble;

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

    @FXML
    private TextField textfieldEintrittsdatum;

    @FXML
    private TextField textfieldAustrittsdatum;

    @FXML
    private Label lblEintrittsdatum;

    @FXML
    private Label lblAustrittsdatum;

    @FXML
    private Label lblValidationEintrittsdatum;

    @FXML
    private Label lblValidationAustrittsdatum;

    //================================================== CLASS VARIABLES ===================================================

    public boolean btnExcelListeClicked = true;

    private boolean fieldsExcelListeValid;

    boolean btnEinzelerstellungClicked = false;

    private boolean fieldsEinzelerstellungValid;


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
        // Textfelder müssen wieder auf valide gesetzt werden
        setTextfieldValid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat);
        setTextfieldValid(textfieldMitarbeiternummer, lblMitarbeiternummerEmpty);
        setTextfieldValid(textfieldSvBrutto, lblFalschesFormatSvBrutto);
        setTextfieldValid(textfieldName, lblNameEmpty);
        // Elemente der Einzelerstellung-Ansicht werden ausgeblendet
        hboxEinzelerstellung.setVisible(false);
        lblNameEmpty.setVisible(false);
        lblMitarbeiternummerEmpty.setVisible(false);
        lblFalschesFormatSvBrutto.setVisible(false);
        lblFalschesFormatAbrechnungsmonat.setVisible(false);
        // Elemente der Excel-Liste-Ansicht werden eingeblendet
        boxExcelListeInputPath.setVisible(true);
        lblValidationInputPath.setVisible(false);
        //lblSchlussnachricht.setVisible(false);
        //lblValidationOutputPath.setVisible(false);
        lblValidationStundenlohn.setVisible(false);
        //textfieldStundenlohn.setText("");
        textfieldInputPath.setText("");
        // Boolean-Werte werden umgestellt
        btnEinzelerstellungClicked = false;
        btnExcelListeClicked = true;
    }

    @FXML
    public void switchToViewEinzelerstellung() {
        // Textfelder müssen wieder auf valide gesetzt werden
        setTextfieldValid(textfieldInputPath, lblValidationInputPath);

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

        //stundenlohn = Double.parseDouble(textfieldStundenlohn.getText().replace(",", "."));

        // Prüfung Feld Output Path
        if (!isTextfieldFilled(textfieldOutputPath)) {
            setTextfieldInvalid(textfieldOutputPath, lblValidationOutputPath, VALIDATION_EMPTY_FIELD);
            fieldsExcelListeValid = false;
            fieldsEinzelerstellungValid = false;
        } else {
            setTextfieldValid(textfieldOutputPath, lblValidationOutputPath);

            if (!isPathADirectory(textfieldOutputPath)) {
                setTextfieldInvalid(textfieldOutputPath, lblValidationOutputPath, VALIDATION_WRONG_OUTPUT_PATH);
                fieldsExcelListeValid = false;
                fieldsEinzelerstellungValid = false;
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

            // Prüfung Feld Input Path
            if (!isTextfieldFilled(textfieldInputPath)) {
                setTextfieldInvalid(textfieldInputPath, lblValidationInputPath, VALIDATION_EMPTY_FIELD);
                fieldsExcelListeValid = false;
            } else {
                setTextfieldValid(textfieldInputPath, lblValidationInputPath);

                if (!isPathAnExcelFile(textfieldInputPath)) {
                    setTextfieldInvalid(textfieldInputPath, lblValidationInputPath, VALIDATION_WRONG_INPUT_PATH);
                    System.out.println("ERROR: " + VALIDATION_WRONG_INPUT_PATH);
                    fieldsExcelListeValid = false;
                } else {
                    setTextfieldValid(textfieldInputPath, lblValidationInputPath);
                }
            }


            // Bei Klick auf Button OK: Wenn alle Felder gültig, dann weiter
            if (fieldsExcelListeValid) {
                //System.out.println("INFO: Alle Felder sind gültig. Excel-Datei wird eingelesen...");

                //if( alles andere auch passt, dann Eingabe-Excel-Datei einlesen, Ausgabe-Excel-Datei mit Stundenzettel-Vorlage erstellen und Stundenzettel-PDF generieren )
                ExcelListeReader excelListeReader = new ExcelListeReader(textfieldInputPath.getText());
                List<List<MitarbeiterMonat>> jahresliste = excelListeReader.getListOfAbrechnungsmonate(Double.parseDouble(textfieldStundenlohn.getText().replace(",", ".")));

                excelListeReader.printJahresliste(jahresliste);
                System.out.println("INFO: Excel-Datei wurde erfolgreich eingelesen. Mitarbeiter-Objekte wurden erstellt");

                ExcelListeWriter excelListeWriter = new ExcelListeWriter(textfieldOutputPath.getText());
                excelListeWriter.writeToExcel(jahresliste, Double.parseDouble(textfieldStundenlohn.getText().replace(",", ".")));

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

            // Prüfung Feld Abrechnungsmonat
            if (!isTextfieldFilled(textfieldAbrechnungsmonat)) {
                setTextfieldInvalid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            } else {
                setTextfieldValid(textfieldAbrechnungsmonat, lblFalschesFormatAbrechnungsmonat);
                if (isValidDateFormat(textfieldAbrechnungsmonat.getText())) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
                    YearMonth abrechnungsmonat = YearMonth.parse(textfieldAbrechnungsmonat.getText(), formatter);
                    YearMonth currentMonth = YearMonth.now();
                    if (abrechnungsmonat.isAfter(currentMonth)) {
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
            double svBrutto = 0;
            String svBruttoText = textfieldSvBrutto.getText();

            if (svBruttoText == null || svBruttoText.trim().isEmpty()) {
                // Wenn das Textfeld leer ist
                setTextfieldInvalid(textfieldSvBrutto, lblFalschesFormatSvBrutto, VALIDATION_EMPTY_FIELD);
            } else if (!isParsableAsDouble(svBruttoText)) {
                // Wenn der Inhalt nicht in einen double geparst werden kann
                setTextfieldInvalid(textfieldSvBrutto, lblFalschesFormatSvBrutto, VALIDATION_NO_NUMBER);
            } else if (Double.parseDouble(svBruttoText) < 0) {
                // Wenn der geparste Wert kleiner als 0 ist
                setTextfieldInvalid(textfieldSvBrutto, lblFalschesFormatSvBrutto, VALIDATION_NEGATIVE_SVBRUTTO);
                fieldsEinzelerstellungValid = false;
            } else {
                // Wenn der Wert gültig ist
                svBrutto = Double.parseDouble(svBruttoText);
                setTextfieldValid(textfieldSvBrutto, lblFalschesFormatSvBrutto);
            }

            // Prüfung Feld Eintrittsdatum
            if (isTextfieldFilled(textfieldEintrittsdatum)) {
                if (isValidDate(textfieldEintrittsdatum.getText())) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    try {
                        LocalDate eintrittsdatum = LocalDate.parse(textfieldEintrittsdatum.getText(), formatter);
                        LocalDate currentDate = LocalDate.now();
                        if (!eintrittsdatum.isAfter(currentDate)) {
                            setTextfieldValid(textfieldEintrittsdatum, lblValidationEintrittsdatum);
                            //fieldsEinzelerstellungValid = true;
                        } else {
                            setTextfieldInvalid(textfieldEintrittsdatum, lblValidationEintrittsdatum, VALIDATION_FUTURE_DATE);
                            fieldsEinzelerstellungValid = false;
                        }
                    } catch (DateTimeParseException e) {
                        setTextfieldInvalid(textfieldEintrittsdatum, lblValidationEintrittsdatum, VALIDATION_WRONG_DATE_FORMAT);
                        fieldsEinzelerstellungValid = false;
                    }
                } else {
                    setTextfieldInvalid(textfieldEintrittsdatum, lblValidationEintrittsdatum, VALIDATION_WRONG_DATE_FORMAT);
                    fieldsEinzelerstellungValid = false;
                }
            } else {
                setTextfieldInvalid(textfieldEintrittsdatum, lblValidationEintrittsdatum, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            }


            // Prüfung Feld Austrittsdatum
            if (isTextfieldFilled(textfieldAustrittsdatum)) {
                if (isValidDate(textfieldAustrittsdatum.getText())) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    try {
                        LocalDate austrittsdatum = LocalDate.parse(textfieldAustrittsdatum.getText(), formatter);
                        LocalDate currentDate = LocalDate.now();
                        if (!austrittsdatum.isAfter(currentDate)) {
                            setTextfieldValid(textfieldAustrittsdatum, lblValidationAustrittsdatum);
                            //fieldsEinzelerstellungValid = true;
                        } else {
                            setTextfieldInvalid(textfieldAustrittsdatum, lblValidationAustrittsdatum, VALIDATION_FUTURE_DATE);
                            fieldsEinzelerstellungValid = false;
                        }
                    } catch (DateTimeParseException e) {
                        setTextfieldInvalid(textfieldAustrittsdatum, lblValidationAustrittsdatum, VALIDATION_WRONG_DATE_FORMAT);
                        fieldsEinzelerstellungValid = false;
                    }
                } else {
                    setTextfieldInvalid(textfieldAustrittsdatum, lblValidationAustrittsdatum, VALIDATION_WRONG_DATE_FORMAT);
                    fieldsEinzelerstellungValid = false;
                }
            } else {
                setTextfieldInvalid(textfieldAustrittsdatum, lblValidationAustrittsdatum, VALIDATION_EMPTY_FIELD);
                fieldsEinzelerstellungValid = false;
            }


            //Wenn alles geklappt hat
            if (fieldsEinzelerstellungValid) {

                if (svBrutto >= Double.parseDouble(textfieldStundenlohn.getText().replace(",", "."))) {

                    saveStundenlohnToDatei(textfieldStundenlohn.getText());
                    setMessageSuccess(lblSchlussnachricht, VALIDATION_SUCCESS_PDF);

                    Einzelerstellung einzelerstellung = new Einzelerstellung(textfieldAbrechnungsmonat.getText(),
                            textfieldMitarbeiternummer.getText(), svBruttoText,
                            textfieldName.getText(), textfieldEintrittsdatum.getText(),
                            textfieldAustrittsdatum.getText());

                    einzelerstellung.writeToExcel(textfieldOutputPath.getText(), textfieldStundenlohn.getText(), checkboxErsetzen.isSelected());

                }
                else {
                    setMessageFailed(lblSchlussnachricht, VALIDATION_LESS_SVBRUTTO);
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

//        textfieldInputPath.setText("C:\\Users\\MM\\Desktop\\neue_excel_kurzarbeiter\\KFB_0124.xlsx");
//        textfieldOutputPath.setText("C:\\Users\\MM\\Desktop\\neue_excel_kurzarbeiter\\generierte_Dateien");
    }
}