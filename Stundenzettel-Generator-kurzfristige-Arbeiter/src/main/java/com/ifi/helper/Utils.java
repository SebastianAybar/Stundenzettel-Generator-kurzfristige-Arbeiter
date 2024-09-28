package com.demo.helper;

import com.demo.application.StundenzettelGeneratorController;
import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import javafx.scene.control.TextField;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.demo.helper.Constants.*;
import static java.util.Locale.GERMANY;

// Diese Klasse enthält "sonstige" Klassen, die nicht woanders eingeordnet werden können, aber notwendig sind
// Utils = Utility (Nützlichkeit)
public class Utils {

    public static void createIfNotExistingLocalFileStundenlohn() {
        // Wenn stundenlohn.txt-Datei nicht existiert, wird sie im Home-Verzeichnis erstellt.
        // Wenn es nicht klappt, dann ohne Dateierstellung weiter
        Path pathLocalFileStundenlohn = Path.of(PATH_FILE_STUNDENLOHN_LOCAL);
        if (!Files.exists(pathLocalFileStundenlohn)) {
            System.out.println("INFO: Lokale Stundenlohn-Datei existiert nicht im Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
            System.out.println("INFO: Lokale Stundenlohn-Datei wird erstellt im Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
            try {
                Files.createFile(pathLocalFileStundenlohn);
                System.out.println("INFO: Neue lokale Stundenlohn-Datei wurde erstellt in das lokale Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
            } catch (Exception e) {
                System.out.println("ERROR: Lokale Stundenlohn-Datei konnte nicht erstellt werden im Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
            }
        } else {
            System.out.println("INFO: Lokale Stundenlohn-Datei existiert bereits");
        }
    }

    public static void loadStundenlohnIntoGuiFromDatei(TextField field) {
        String stundenlohn = STUNDENLOHN_VALUE_DEFAULT;

        try (BufferedReader localStundenlohnFileReader = new BufferedReader(new FileReader(PATH_FILE_STUNDENLOHN_LOCAL))) {
            stundenlohn = localStundenlohnFileReader.readLine().trim();
            System.out.println("INFO: Stundenlohn >" + stundenlohn + "< wurde aus der lokalen Stundenlohn-Datei gelesen");
        } catch (FileNotFoundException e) {
            System.err.println("INFO: Lokale Stundenlohn-Datei wurde nicht gefunden im Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
        } catch (IOException e) {
            System.out.println("ERROR: Fehler beim Auslesen des Stundenlohns aus der lokalen Stundenlohn-Datei");
        } catch (NullPointerException e) {
            System.out.println("INFO: Lokale Stundenlohn-Datei wurde gefunden, ist aber leer. Default-Wert >" + STUNDENLOHN_VALUE_DEFAULT + "< wird übernommen");
            stundenlohn = STUNDENLOHN_VALUE_DEFAULT;
        }

        field.setText(stundenlohn);
    }

    public static void saveStundenlohnToDatei(String stundenlohn) {
        Path pathLocalFileStundenlohn = Paths.get(PATH_FILE_STUNDENLOHN_LOCAL);

        if (!Files.exists(pathLocalFileStundenlohn)) {
            System.out.println("INFO: Die lokale Stundenlohn-Datei existiert nicht. Der Wert >" + stundenlohn + "< kann daher nicht gespeichert werden und wird verworfen");
        } else {
            System.out.println("INFO: Der aktuelle Stundenlohn >" + stundenlohn + "< wird in der lokalen Stundenlohn-Datei gespeichert");
            try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(PATH_FILE_STUNDENLOHN_LOCAL))) {
                printWriter.println(stundenlohn.trim());
                System.out.println("INFO: Der aktuelle Stundenlohn >" + stundenlohn + "< wurde in der lokalen Stundenlohn-Datei gespeichert");
            } catch (IOException e) {
                System.out.println("ERROR: In die Stundenlohn-Datei im Verzeichnis " + PATH_FILE_STUNDENLOHN_LOCAL + " konnte nicht reingeschrieben werden (check code)");
            }
        }
    }

    public static void createIfNotExistingLocalFileLogo() {
        // Unternehmenslogo erstellen
        // Create the destination path in the home directory
        Path pathLocalFileLogo = Path.of(PATH_FILE_LOGO_LOCAL);

        if (!Files.exists(pathLocalFileLogo)) {
            try {
                InputStream fileLogoFromJAR = StundenzettelGeneratorController.class.getResourceAsStream(PATH_FILE_LOGO_JAR);

                if (fileLogoFromJAR == null) {
                    System.out.println("ERROR: Logo-Datei wurde nicht gefunden im JAR-Verzeichnis: " + PATH_FILE_LOGO_JAR);
                    return;
                } else System.out.println("INFO: Logo-Datei wurde gefunden im JAR-Verzeichnis: " + PATH_FILE_LOGO_JAR);

                // Copy the file from resources to the home directory
                Files.copy(fileLogoFromJAR, pathLocalFileLogo, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("INFO: Logo-Datei wurde kopiert in das lokale Verzeichnis: " + PATH_FILE_LOGO_LOCAL);
            } catch (Exception e) {
                System.out.println("ERROR: Logo-Datei problem (check code)");
            }
        } else {
            System.out.println("INFO: Logo-Datei existiert bereits im Verzeichnis: " + PATH_FILE_LOGO_LOCAL);
        }

    }

    public static void createIfNotExistingLocalFileStundenzettelVorlage() {
        // Stundenzettel-Vorlage erstellen
        // Create the destination path in the home directory
        Path pathLocalFileStundenzettelVorlage = Path.of(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);

        if (!Files.exists(pathLocalFileStundenzettelVorlage)) {
            try {
                InputStream fileStundenzettelVorlageFromJAR = StundenzettelGeneratorController.class.getResourceAsStream(PATH_FILE_STUNDENZETTEL_VORLAGE_JAR);

                if (fileStundenzettelVorlageFromJAR == null) {
                    System.out.println("ERROR: Stundenzettel-Vorlage-Datei wurde nicht gefunden im JAR-Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_JAR);
                    return;
                } else System.out.println("INFO: Stundenzettel-Vorlage-Datei wurde gefunden im JAR-Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_JAR);

                // Copy the file from resources to the home directory
                Files.copy(fileStundenzettelVorlageFromJAR, pathLocalFileStundenzettelVorlage, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("INFO: Stundenzettel-Vorlage-Datei wurde kopiert in das lokale Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
            } catch (Exception e) {
                System.out.println("ERROR: Stundenzettel-Vorlage-Datei problem (check code)");
            }
        } else {
            System.out.println("INFO: Stundenzettel-Vorlage-Datei existiert bereits im Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
        }

    }


    // Gibt eine Liste mit allen Tagen des bestimmten Monats zurück
    public static List<LocalDate> getAlleTageDesMonatsAlt(String[] datum) {
        YearMonth jahrMonat = YearMonth.of(Integer.parseInt(datum[0]), Integer.parseInt(datum[1]));
        int anzahlTageImMonat = jahrMonat.lengthOfMonth();
        LocalDate ersterTag = jahrMonat.atDay(1);
        List<LocalDate> datenDesMonats = new ArrayList<>();
        for (int j = 0; j < anzahlTageImMonat; j++) {
            LocalDate aktuellesDatum = ersterTag.plusDays(j);
            datenDesMonats.add(aktuellesDatum);
        }
        return datenDesMonats;
    }

    // Gibt eine Liste mit allen Tagen des bestimmten Monats zurück
    public static List<LocalDate> getAlleTageDesMonats(String[] datum, String eintrittsdatum, String austrittsdatum) {

        YearMonth jahrMonat = YearMonth.of(Integer.parseInt(datum[0]), Integer.parseInt(datum[1]));
        int anzahlTageImMonat = jahrMonat.lengthOfMonth();
        LocalDate ersterTag = jahrMonat.atDay(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate entryDate = LocalDate.parse(eintrittsdatum, formatter);
        LocalDate exitDate = LocalDate.parse(austrittsdatum, formatter);

        List<LocalDate> datenDesMonats = new ArrayList<>();

        for (int j = 0; j < anzahlTageImMonat; j++) {
            LocalDate aktuellesDatum = ersterTag.plusDays(j);
            // Prüfen, ob aktuelles Datum zwischen Eintritts- und Austrittsdatum liegt
            if ((aktuellesDatum.isEqual(entryDate) || aktuellesDatum.isAfter(entryDate)) &&
                    (aktuellesDatum.isEqual(exitDate) || aktuellesDatum.isBefore(exitDate))) {
                datenDesMonats.add(aktuellesDatum);
            }
        }
        //System.out.println("Alle LocalDate des Monats: " + datenDesMonats);
        return datenDesMonats;
    }
    // Prüft, ob das angegebene Datum ein Feiertag ist (mithilfe der Library <jollyday>)
    public static boolean isDatumEinFeiertag(LocalDate datum, int jahr) {
        final String BUNDESLAND = "he";
        final HolidayManager feiertageManager = HolidayManager.getInstance(ManagerParameters.create(GERMANY));
        final Set<Holiday> feiertage = feiertageManager.getHolidays(jahr, BUNDESLAND);
        for (Holiday feiertag : feiertage) {
            if (feiertag.getDate().toString().equals(datum.toString())) {
                return true;
            }
        }
        return false;
    }

    // Row als freien Tag (Sonn- und Feiertage) markieren (-2, -1, +2, +3, +4, +5 um KW, Wochentag, etc. anzusprechen)
    public static void markiereRowAlsFreienTag(Workbook workbook, Row row, Cell cell) {
        // Zellen leeren
        Cell kwCell = cell.getRow().getCell(cell.getColumnIndex() - 2);

        Cell wochentagCell = cell.getRow().getCell(cell.getColumnIndex() - 1);

        Cell arbeitszeitCell = cell.getRow().getCell(cell.getColumnIndex() + 1);
        arbeitszeitCell.setCellValue("");

        Cell dezimalCell = cell.getRow().getCell(cell.getColumnIndex() + 2);
        dezimalCell.setCellValue("");

        Cell arbeitszeitNettoCell = cell.getRow().getCell(cell.getColumnIndex() + 3);
        arbeitszeitNettoCell.removeFormula();
        arbeitszeitNettoCell.setCellValue("");

        Cell aufgezeichnetAmCell = cell.getRow().getCell(cell.getColumnIndex() + 4);
        aufgezeichnetAmCell.setCellValue("");

        // Zellen färben
        CellStyle originalStyle = cell.getCellStyle();
        CellStyle freierTagStyle = workbook.createCellStyle();
        CellStyle freierTagStyleFuerDatum = workbook.createCellStyle();
        freierTagStyle.cloneStyleFrom(originalStyle);

        freierTagStyle.setFillPattern(FillPatternType.BRICKS);
        freierTagStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        freierTagStyle.setBorderBottom(BorderStyle.THIN);
        freierTagStyle.setBorderLeft(BorderStyle.THIN);
        freierTagStyle.setBorderRight(BorderStyle.THIN);
        freierTagStyle.setBorderTop(BorderStyle.THIN);

        freierTagStyleFuerDatum.cloneStyleFrom(freierTagStyle);

        freierTagStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("General"));

        // Feld: KW
        kwCell.setCellStyle(freierTagStyle);

        // Feld: Wochentag
        wochentagCell.setCellStyle(freierTagStyle);

        // Feld: Datum
        cell.setCellStyle(freierTagStyleFuerDatum);

        // Feld: Arbeitszeit
        arbeitszeitCell.setCellStyle(freierTagStyle);

        // Feld: Dezimal
        dezimalCell.setCellStyle(freierTagStyle);

        // Feld: Arbeitszeit netto
        arbeitszeitNettoCell.setCellStyle(freierTagStyle);

        // Feld: Aufgezeichnet am
        aufgezeichnetAmCell.setCellStyle(freierTagStyle);
    }

    public static double[] generateRandomNumbers(int numArbeitstage, double mean, double sd) {
        double[] result = new double[numArbeitstage];
        NormalDistribution normalDistribution = new NormalDistribution(mean, sd);

        double randomValue;
        for (int i = 0; i < numArbeitstage; i++) {
            do {
                randomValue = normalDistribution.sample();
            } while (randomValue < 0.25);
            DecimalFormat decimalFormat = new DecimalFormat("###.#");
            result[i] = Double.parseDouble(decimalFormat.format(randomValue).replace(",", "."));
        }
        return result;
    }

}
