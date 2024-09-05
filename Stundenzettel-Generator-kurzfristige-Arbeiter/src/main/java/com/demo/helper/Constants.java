package com.demo.helper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

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
    public static final String VALIDATION_NO_NUMBER = "Keine Zahl";
    public static final String VALIDATION_NEGATIVE_SVBRUTTO = "Brutto kleiner als 0";
    public static final String VALIDATION_WRONG_DATE_FORMAT = "Falsches Format";
    public static final String VALIDATION_FUTURE_DATE = "Datum in der Zukunft";
    public static final String VALIDATION_WRONG_INPUT = "Ungültige Eingabe";
    public static final String VALIDATION_EMPTY_FIELD = "Leeres Feld";
    public static final String VALIDATION_WRONG_INPUT_PATH = "Der angegebene Input-Pfad führt nicht zu einer gültigen Excel-Datei";
    public static final String VALIDATION_WRONG_OUTPUT_PATH = "Der Pfad ist nicht gültig";
    public static final String VALIDATION_SUCCESS_PDF = "PDF-Datei(en) wurde(n) erfolgreich generiert";
    public static final String VALIDATION_FAILED_PDF = "Fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.";




    public static final String DOCUMENT_FILE_SUFFIX = ".pdf";
    public static final Rectangle PAGE_DIN_SIZE = PageSize.A4;
    public static final int PAGE_MARGIN_LEFT = 0;
    public static final int PAGE_MARGIN_RIGHT = 0;
    public static final int PAGE_MARGIN_TOP = 50;
    public static final int PAGE_MARGIN_BOTTOM = 20;
    // TITLE
    public static final String TITLE_NAME = "Stundenzettel";
    public static final int TITLE_FONT_SIZE = 17;
    public static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, TITLE_FONT_SIZE, Font.BOLD);
    public static final int TITLE_SPACING_BEFORE = 60;
    public static final int TITLE_SPACING_AFTER = 55;
    public static final int TITLE_INDENTATION_LEFT = 60;

    public static final int LOGO_SCALE_PERCENT = 35;
    public static final int LOGO_ABSOLUTE_POS_X = 350;
    public static final int LOGO_ABSOLUTE_POS_Y = 760;

    // MITARBEITERINFOTABELLE
    public static final int TBL_MITARBEITERINFO_COLUMNS_ANZAHL = 3;
    public static final int TBL_MITARBEITERINFO_COLUMN1_WIDTH = 13;
    public static final int TBL_MITARBEITERINFO_COLUMN2_WIDTH = 32;
    public static final int TBL_MITARBEITERINFO_COLUMN3_WIDTH = 55;
    public static final int TBL_MITARBEITERINFO_WIDTH_PERCENT = 75;
    public static final int TBL_MITARBEITERINFO_SPACING_AFTER = 30;
    public static final int TBL_MITARBEITERINFO_FONT_SIZE = 11;
    public static final Font TBL_MITARBEITERINFO_FONT = new Font(Font.FontFamily.HELVETICA, TBL_MITARBEITERINFO_FONT_SIZE);
    public static final Font TBL_MITARBEITERINFO_FETT_FONT = new Font(Font.FontFamily.HELVETICA, TBL_MITARBEITERINFO_FONT_SIZE, Font.BOLD);

    //////////////////////// STUNDENZETTELTABELLE /////////////////////////////////////

    // EXCEL INDICES
    public static final int ROWINDEX_ERSTES_DATUM = 6;
    public static final int ROWINDEX_ENDE_MITARBEITERINFO = 3;
    public static final int ROW_MITARBEITERNAME_TEXT = 0;
    public static final int COLUMN_MITARBEITERNAME_TEXT = 0;
    public static final int ROW_MITARBEITERNAME = 0;
    public static final int COLUMN_MITARBEITERNAME = 3;
    public static final int ROW_MITARBEITERNUMMER_TEXT = 2;
    public static final int COLUMN_MITARBEITERNUMMER_TEXT = 0;
    public static final int ROW_MITARBEITERNUMMER = 2;
    public static final int COLUMN_MITARBEITERNUMMER = 3;
    public static final int ROW_JAHRMONAT_TEXT = 2;
    public static final int COLUMN_JAHRMONAT_TEXT = 5;
    public static final int ROW_JAHRMONAT = 2;
    public static final int COLUMN_JAHRMONAT = 6;

    public static final int ROW_KOPFZEILE = 5;
    public static final int COLUMN_KW = 0;
    public static final int COLUMN_WOCHENTAG = 1;
    public static final int COLUMN_DATUM = 2;
    public static final int COLUMN_ARBEITSZEIT = 3;
    public static final int COLUMN_DEZIMAL = 4;
    public static final int COLUMN_ARBEITSZEITNETTO = 5;
    public static final int COLUMN_AUFGEZEICHNETAM = 6;


    // KOPFZEILE
    public static final int KOPFZEILE_FONT_SIZE = 11;
    public static final Font KOPFZEILE_FONT = new Font(Font.FontFamily.HELVETICA, KOPFZEILE_FONT_SIZE, Font.BOLD);
    public static final BaseColor KOPFZEILE_COLOR = new BaseColor(117, 186, 255);

    // INHALT (STUNDENZETTEL)
    public static final int TBL_STUNDEN_COLUMN_ANZAHL = 7;
    public static final int TBL_STUNDEN_HEADER_ANZAHL = 1;
    public static final int KW_COLUMN_WIDTH = 2;
    public static final int WOCHENTAG_COLUMN_WIDTH = 4;
    public static final int DATUM_COLUMN_WIDTH = 4;
    public static final int ARBEITSZEIT_COLUMN_WIDTH = 4;
    public static final int DEZIMAL_COLUMN_WIDTH = 3;
    public static final int ARBEITSZEITNETTO_COLUMN_WIDTH = 4;
    public static final int AUFGEZEICHNETAM_COLUMN_WIDTH = 5;
    public static final int TBL_STUNDEN_FONT_SIZE = 11;
    public static final Font TBL_STUNDEN_FONT = new Font(Font.FontFamily.HELVETICA, TBL_STUNDEN_FONT_SIZE);
    public static final Font TBL_STUNDEN_FETT_FONT = new Font(Font.FontFamily.HELVETICA, TBL_STUNDEN_FONT_SIZE, Font.BOLD);
    public static final int TBL_STUNDEN_PADDING = 3;
    public static final BaseColor FREIERTAG_COLOR = new BaseColor(192, 192, 192);
    public static final String TBL_STUNDEN_SUMMEN_FORMAT = "###.#";

    // SONSTIGE
    public static final int CELL_BORDER_TRANSPARENT = 0;
    public static final String DEFAULT_VALUE = "DEF";
    public static final String DEFAULT_STUNDENLOHN = "12";
    public static final String DATE_GERMAN_FORMAT = "dd.MM.yyyy";

}
