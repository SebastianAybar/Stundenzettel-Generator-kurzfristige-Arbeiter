package com.demo.application;


import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static com.demo.helper.Constants.PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL;

public class Einzelerstellung {

    private final String abrechnungsmonat;
    private final String mitarbeiternummer;
    private final String svBrutto;
    private final String name;

    Einzelerstellung(String abrechnungsmonat, String mitarbeiternummer, String svBrutto, String name) {
        this.abrechnungsmonat = abrechnungsmonat;
        this.mitarbeiternummer = mitarbeiternummer;
        this.svBrutto = svBrutto;
        this.name = name;
    }

    public void writeToExcel(String outputPath, String lohn, boolean isErsetzenSelected) {
        try {
            String resourceFilePath = "/Stundenzettel_Vorlage.xlsx";
            InputStream resourceStream = Einzelerstellung.class.getResourceAsStream(resourceFilePath);

            if (resourceStream == null) {
                System.err.println("File not found in resources: " + resourceFilePath);
                return;
            }

            // Create the destination path in the home directory

            Path destinationPath = Paths.get(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);

            if (!Files.exists(destinationPath)) {
                // Copy the file from resources to the home directory
                Files.copy(resourceStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Stundenzettel_Vorlage wurde kopiert zu" + destinationPath);
            } else {
                System.out.println("Vorlage existiert bereits im Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
            }

            InputStream inputStream = new FileInputStream(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet currentSheet = workbook.getSheetAt(0);

            List<Cell> arbeitszeitenCells = new ArrayList<>();
            String[] datum = abrechnungsmonat.split("/");
            List<LocalDate> datenDesMonats = getDatenDesMonats(datum);
            List<Row> rowsToRemove = new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private List<LocalDate> getDatenDesMonats(String[] datum) {
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
}
