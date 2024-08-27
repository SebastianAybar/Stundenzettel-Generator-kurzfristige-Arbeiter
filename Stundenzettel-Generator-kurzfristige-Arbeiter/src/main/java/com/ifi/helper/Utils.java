package com.demo.helper;

import com.demo.application.StundenzettelGeneratorController;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.demo.helper.Constants.*;

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
                System.out.println("ERROR: Lokale Stundenlohn-Datei konnte nicht erstellt werden in Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
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
            System.err.println("INFO: Lokale Stundenlohn-Datei wurde nicht gefunden in Verzeichnis: " + PATH_FILE_STUNDENLOHN_LOCAL);
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
                    System.out.println("ERROR: Logo-Datei wurde nicht gefunden in JAR-Verzeichnis: " + PATH_FILE_LOGO_JAR);
                    return;
                } else System.out.println("INFO: Logo-Datei wurde gefunden in JAR-Verzeichnis: " + PATH_FILE_LOGO_JAR);

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
                    System.out.println("ERROR: Stundenzettel-Vorlage-Datei wurde nicht gefunden in JAR-Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_JAR);
                    return;
                } else System.out.println("INFO: Stundenzettel-Vorlage-Datei wurde gefunden in JAR-Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_JAR);

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
}
