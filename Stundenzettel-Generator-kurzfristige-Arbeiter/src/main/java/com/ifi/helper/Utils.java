package com.demo.helper;

import javafx.scene.control.TextField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.demo.helper.Constants.*;

// Diese Klasse enthält "sonstige" Klassen, die nicht woanders eingeordnet werden können, aber notwendig sind
// Utils = Utility (Nützlichkeit)
public class Utils {

    public static void loadStundenlohnFromDatei(TextField field) {
        Path path = Paths.get(PATH_DATEI_STUNDENLOHN);
        String stundenlohn = DEFAULT_STUNDENLOHN;

        // Wenn stundenlohn.txt im Home-Verzeichnis nicht existiert
        if (!Files.exists(path)) {
            // Neue stundenlohn.txt im Home-Verzeichnis erstellen. Wenns nicht klappt, dann ohne Datei erstellen weiter
            System.out.println("Stundenlohn-Datei existiert nicht!");
            try {
                Files.createFile(path);
                System.out.println("Neue Datei erstellt in: " + PATH_DATEI_STUNDENLOHN);
            } catch (Exception e) {
                System.out.println("Stundenlohn-Datei konnte nicht im Verzeichnis " + PATH_DATEI_STUNDENLOHN + " erstellt werden");
            }

            try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(PATH_DATEI_STUNDENLOHN))) {
                printWriter.println(stundenlohn);
                System.out.println("Wert >" + stundenlohn + "< in die neue Datei geschrieben");
            } catch (IOException e) {
                System.out.println("In die Stundenlohn-Datei im Verzeichnis " + PATH_DATEI_STUNDENLOHN + " konnte nicht reingeschrieben werden, weil sie nicht existiert");
            }

        } else {
            System.out.println("Datei existiert bereits!");
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH_DATEI_STUNDENLOHN))) {
                stundenlohn = bufferedReader.readLine().trim();
                System.out.println("Stundenlon >" + stundenlohn + "< aus Datei gelesen");
            } catch (FileNotFoundException e) {
                System.err.println("Stundenlohn-Datei nicht gefunden in Verzeichnis: " + PATH_DATEI_STUNDENLOHN);
            } catch (IOException e) {
                System.out.println("Fehler beim Auslesen des Stundenlohns aus der Datei");
            }
        }

        field.setText(stundenlohn);
    }

    public static void saveStundenlohnToDatei(String stundenlohn) {
        Path path = Paths.get(PATH_DATEI_STUNDENLOHN);

        if (!Files.exists(path)) {
            System.out.println("Die Stundenlohn-Datei existiert nicht. Der Wert " + stundenlohn + " kann daher nicht gespeichert werden und wird verworfen");
        } else {
            System.out.println("Datei zum Speichern des Wertes gefunden!");
            try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(PATH_DATEI_STUNDENLOHN))) {
                printWriter.println(stundenlohn.trim());
                System.out.println("Wert >" + stundenlohn + "< in die existierende Stundenlohn-Datei geschrieben");
            } catch (IOException e) {
                System.out.println("In die Stundenlohn-Datei im Verzeichnis " + PATH_DATEI_STUNDENLOHN + " konnte nicht reingeschrieben werden (Error prüfen)");
            }
        }
    }
}
