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
    // FILE STUNDENLOHN
    public static final String FILE_STUNDENLOHN_NAME = "stundenlohn";
    public static final String FILE_STUNDENLOHN_SUFFIX = ".txt";
    public static final String PATH_FILE_STUNDENLOHN_LOCAL = HOME_DIRECTORY + File.separator + FILE_STUNDENLOHN_NAME + FILE_STUNDENLOHN_SUFFIX;
    // FILE LOGO
    public static final String FILE_LOGO_NAME = "logo_company";
    public static final String FILE_LOGO_SUFFIX = ".png";
    public static final String PATH_FILE_LOGO_JAR = "/" + FILE_LOGO_NAME + FILE_LOGO_SUFFIX;
    public static final String PATH_FILE_LOGO_LOCAL = HOME_DIRECTORY + File.separator + FILE_LOGO_NAME + FILE_LOGO_SUFFIX;
    // FILE STUNDENZETTEL_VORLAGE
    public static final String FILE_STUNDENZETTEL_VORLAGE_NAME = "Stundenzettel_Vorlage";
    public static final String FILE_STUNDENZETTEL_VORLAGE_SUFFIX = ".xlsx";
    public static final String PATH_FILE_STUNDENZETTEL_VORLAGE_JAR = "/" + FILE_STUNDENZETTEL_VORLAGE_NAME + FILE_STUNDENZETTEL_VORLAGE_SUFFIX;
    public static final String PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL = HOME_DIRECTORY + File.separator + FILE_STUNDENZETTEL_VORLAGE_NAME + FILE_STUNDENZETTEL_VORLAGE_SUFFIX;


    // OTHERS
    public static final String STUNDENLOHN_VALUE_DEFAULT = "12,00";

    // VALIDATION MESSAGES
    public static final String VALIDATION_WRONG_INPUT = "Ungültige Eingabe";
    public static final String VALIDATION_EMPTY_FIELD = "Leeres Feld";
    public static final String VALIDATION_WRONG_INPUT_PATH = "Der Pfad führt nicht zu einer gültigen Excel-Datei";
    public static final String VALIDATION_WRONG_OUTPUT_PATH = "Der Pfad ist nicht gültig";
    public static final String VALIDATION_SUCCESS_PDF = "PDF-Datei(en) wurde(n) erfolgreich generiert";
    public static final String VALIDATION_FAILED_PDF = "Fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.";
}
