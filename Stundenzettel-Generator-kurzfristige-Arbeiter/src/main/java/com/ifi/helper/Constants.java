package com.demo.helper;

import java.io.File;

public class Constants {

    // APPLICATION
    public static final int APPLICATION_WINDOW_WIDTH = 710;
    public static final int APPLICATION_WINDOW_HEIGHT = 460;

    // FILE/DIRECTORY CHOOSER
    public static final String CHOOSER_INPUT_TITLE = "Datei auswählen";
    public static final String CHOOSER_OUTPUT_TITLE = "Verzeichnis auswählen";

    // PATHS
    public static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final String DATEI_STUNDENLOHN_NAME = "stundenlohn";
    public static final String DATEI_STUNDENLOHN_SUFFIX = ".txt";
    public static final String PATH_DATEI_STUNDENLOHN = HOME_DIRECTORY + File.separator + DATEI_STUNDENLOHN_NAME + DATEI_STUNDENLOHN_SUFFIX;

    // SONSTIGE
    public static final String DEFAULT_STUNDENLOHN = "12";

    // VALIDATION MESSAGES
    public static final String VALIDATION_NO_NUMBER = "Keine Zahl";
    public static final String VALIDATION_NEGATIVE_SVBRUTTO = "Brutto kleiner als 0";
    public static final String VALIDATION_WRONG_DATE_FORMAT = "Falsches Format";
    public static final String VALIDATION_FUTURE_DATE = "Datum in der Zukunft";
    public static final String VALIDATION_WRONG_INPUT = "Ungültige Eingabe";
    public static final String VALIDATION_EMPTY = "Leeres Feld";
    public static final String VALIDATION_WRONG_INPUT_PATH = "Der Pfad führt nicht zu einer gültigen Excel-Datei";
    public static final String VALIDATION_WRONG_OUTPUT_PATH = "Der Pfad führt nicht zu einem gültigen Verzeichnis";
    public static final String VALIDATION_SUCCESS_PDF = "PDF-Datei(en) wurde(n) erfolgreich generiert";
    public static final String VALIDATION_FAILED_PDF = "Fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben";
}
