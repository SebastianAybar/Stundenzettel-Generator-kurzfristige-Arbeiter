package com.demo.application;

import static com.demo.helper.Constants.*;
import static com.demo.helper.Validation.displayErrorInGui;

import com.demo.helper.Utils;
import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;


public class Einzelerstellung {

    private final String abrechnungsmonat;
    private final String mitarbeiternummer;
    private final String svBrutto;
    private final String name;
    private final String eintrittsdatum;
    private final String austrittsdatum;

    Einzelerstellung(String abrechnungsmonat, String mitarbeiternummer, String svBrutto, String name, String eintrittsdatum, String austrittsdatum) {
        this.abrechnungsmonat = abrechnungsmonat;
        this.mitarbeiternummer = mitarbeiternummer;
        this.svBrutto = svBrutto;
        this.name = name;
        this.eintrittsdatum = eintrittsdatum;
        this.austrittsdatum = austrittsdatum;
    }

    public void writeToExcel(String outputPath, String lohn, boolean isErsetzenSelected) {

        int counter = 1;

        try {
            InputStream fileStundenzettelVorlageLocal = new FileInputStream(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);

            if (fileStundenzettelVorlageLocal == null) {
                System.err.println("File not found in resources: " + fileStundenzettelVorlageLocal);
                return;
            }

            // Create the destination path in the home directory
            Path destinationPath = Paths.get(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);

            if (!Files.exists(destinationPath)) {
                // Copy the file from resources to the home directory
                Files.copy(fileStundenzettelVorlageLocal, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Stundenzettel_Vorlage wurde kopiert zu" + destinationPath);
            } else {
                System.out.println("Vorlage existiert bereits im Verzeichnis: " + PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
            }

            InputStream inputStream = new FileInputStream(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet currentSheet = workbook.getSheetAt(0);

            //Liste der Zellen in Spalte "Dezimal" in der Vorlage
            List<Cell> arbeitszeitenCells = new ArrayList<>();

            //Liste aller LocalDate's im Monat
            String[] datum = abrechnungsmonat.split("/");
            List<LocalDate> datenDesMonatsAlt = Utils.getAlleTageDesMonatsAlt(datum);
            List<LocalDate> datenDesMonats = Utils.getAlleTageDesMonats(datum, eintrittsdatum, austrittsdatum);

            //Sofern der Monat weniger als 31 Tage hat, müssen Zeilen entfernt werden
            List<Row> rowsToRemove = new ArrayList<>();

            //Wir befüllen die Felder Abrechnungsmonat, Mitarbeiter, Mitarbeiternummer und die Spalten Datum, KW und Wochentag
            int counterTage = 0;
            for (Row row : currentSheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue().trim();
                        if (cellValue.equals("<<Abrechnungsmonat>>")) {
                            cell.setCellValue(abrechnungsmonat);
                        }
                        if (cellValue.equals("<<Mitarbeiter>>")) {
                            cell.setCellValue(name);
                        }
                        if (cellValue.equals("<<Mitarbeiternummer>>")) {
                            cell.setCellValue(mitarbeiternummer);
                        }
                        if (cellValue.startsWith("<<Tag")) {
                            if (counterTage < datenDesMonatsAlt.size()) {
                                //Wir befüllen die Spalte Datum
                                cell.setCellValue(datenDesMonatsAlt.get(counterTage));
                                //Wir befüllen die Spalte KW
                                cell.getRow().getCell(cell.getColumnIndex() - 2).setCellValue(datenDesMonatsAlt.get(counterTage).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
                                //Wir befüllen die Spalte Wochentag
                                DateTimeFormatter deutschFormatierer = DateTimeFormatter.ofPattern("EEEE", Locale.GERMAN);
                                String wochentag = datenDesMonatsAlt.get(counterTage).format(deutschFormatierer);
                                Cell wochentagCell = cell.getRow().getCell(cell.getColumnIndex() - 1);
                                wochentagCell.setCellValue(wochentag);
                                //Wir markieren und leeren die Sonn- und Feiertage
                                if (wochentag.equals("Sonntag") || Utils.isDatumEinFeiertag(datenDesMonatsAlt.get(counterTage), Integer.parseInt(datum[0]))) {
                                    Utils.markiereRowAlsFreienTag(workbook, row, cell);
                                }
                                counterTage++;
                            } else {
                                rowsToRemove.add(cell.getRow());
                                break;
                            }
                        }
                        //Wir wollen die cell nur hinzufügen, wenn der wert in datenDesMonatsAlt.get(counterTage) auch in datenDesMonats enthalten ist
                        /*if (cellValue.startsWith("<<Std")) {
                            arbeitszeitenCells.add(cell);
                        }*/

                        if (cellValue.startsWith("<<Std")) {
                            if (counterTage > 0 && counterTage < datenDesMonatsAlt.size() && datenDesMonats.contains(datenDesMonatsAlt.get(counterTage - 1))) {
                                arbeitszeitenCells.add(cell);
                            } else {
                                cell.setCellValue("");
                            }
                        }


                    }
                }
            }

            for (Row row : rowsToRemove) {
                currentSheet.removeRow(row);
            }

            //Wir berechnen die Variablen totalMean, gerundeteArbeitstage und den Stundensatz für die Normalverteilung
            double svBrutto = Double.parseDouble(this.svBrutto.replace(",", "."));
            double mindestlohn = Double.parseDouble(lohn.replace(",", "."));

            double stundensatz = 0;
            int gerundeteArbeitstage = 0;
            double totalMean = 0;

            int randomArbeitstage;
            Random random = new Random();

            if (arbeitszeitenCells.size() > 5) {
                int min = arbeitszeitenCells.size() - 3;
                int max = arbeitszeitenCells.size();
                randomArbeitstage = random.nextInt(max - min) + min; //Anzahl der Arbeitstage
            } else {
                randomArbeitstage = arbeitszeitenCells.size();
            }

            double svBruttoGrenze = 8 * mindestlohn * randomArbeitstage; //Diese Variable brauchen wir um zu wissen wann wir in den 3. Case müssen

            System.out.println("-----------------");
            System.out.println("svBrutto: " + svBrutto);


            //Wenn svBrutto die Grenze übersteigt
            if (svBrutto >= svBruttoGrenze) {
                totalMean = 6.5 + (random.nextDouble() * 1.5); //Damit nicht jeder Mitarbeiter exakt 8h durchschnittliche Arbeitszeit hat
                gerundeteArbeitstage = randomArbeitstage; //Anzahl der Arbeitstage
                stundensatz = gerundeteArbeitstage * totalMean;
                double stundenlohn = svBrutto / stundensatz;
                System.out.println("gerundetete Arbeitstage: " + gerundeteArbeitstage);
                System.out.println("stundenlohn: " + stundenlohn);
                System.out.println("stundensatz: " + stundensatz);
                System.out.println("totalMean: " + totalMean);
                System.out.println("-----------------");

                if (stundenlohn > 150) {
                    gerundeteArbeitstage = arbeitszeitenCells.size();
                    totalMean = 8; //Damit nicht jeder Mitarbeiter exakt 8h durchschnittliche Arbeitszeit hat
                    stundensatz = gerundeteArbeitstage * totalMean;
                    stundenlohn = svBrutto / stundensatz;
                    if(stundenlohn > 150) {
                        DecimalFormat df = new DecimalFormat("#.00");
                        displayErrorInGui("Stundenlohn liegt bei: " + df.format(stundenlohn) + "€.\n" + "Brutto ist zu hoch für den Beschäfigungszeitraum.");
                        return;
                    }
                }
            } else { //Wenn svBrutto unter der Grenze liegt
                gerundeteArbeitstage = randomArbeitstage;
                totalMean = svBrutto / (mindestlohn * gerundeteArbeitstage);
                stundensatz = gerundeteArbeitstage * totalMean;
                while (totalMean < 2) {
                    totalMean = totalMean + 1;
                }
                gerundeteArbeitstage = (int) Math.ceil(stundensatz / totalMean);
                double stundenlohn = mindestlohn;
                System.out.println("gerundetete Arbeitstage: " + gerundeteArbeitstage);
                System.out.println("stundenlohn: " + stundenlohn);
                System.out.println("stundensatz: " + stundensatz);
                System.out.println("totalMean: " + totalMean);
                System.out.println("-----------------");
            }


            System.out.println("Alle Local Date's im Monat: " + datenDesMonatsAlt);
            System.out.println("Alle Local Date's innerhalb Beschäftigungszeitraumes: " + datenDesMonatsAlt);

            // Prüfen, ob die Anzahl der Arbeitstage, die "gearbeitet wurden" auch in den Monat passen
            // Das ist nicht der Fall, wenn bspw. der Stundenlohn im Vergleich zum svBrutto sehr niedrig ist
            // und die Person hätte zu viele Stunden bzw. Tage arbeiten müssen, um das zu erreichen.
            System.out.println("arbeitszeitenCells.size: " + arbeitszeitenCells.size());
            System.out.println("arbeitszeitenCells: " + arbeitszeitenCells);
            if (gerundeteArbeitstage > arbeitszeitenCells.size()) {
                displayErrorInGui("Das Gehalt übersteigt die mögliche Monatsarbeitszeit im Verhältnis zum angegebenen Stundenlohn ");
                return;
            }

            //Wir erstellen ein Array mit den Arbeitszeiten mit der Größe der Arbeitstage
            double[] arbeitszeiten = Utils.generateRandomNumbers(gerundeteArbeitstage, totalMean, 1);

            System.out.println("arbeitszeiten[gerundeteArbeitstage] vorher: " + Arrays.toString(arbeitszeiten));

            double sum = 0;
            for (double value : arbeitszeiten) {
                sum += value;
            }
            for (int j = 0; j < arbeitszeiten.length; j++) {
                arbeitszeiten[j] = arbeitszeiten[j] * (stundensatz / sum);
            }
            double sumAfter = 0;
            for (double value : arbeitszeiten) {
                sumAfter += value;
            }

            System.out.println("arbeitszeiten[gerundeteArbeitstage] nachher: " + Arrays.toString(arbeitszeiten));

            //Wir erstellen ein Array in der selben Größe wie arbeitszeitenCells
            String[] werktage = new String[arbeitszeitenCells.size()];

            DecimalFormat decimalFormat = new DecimalFormat("###.##");
            int tempcounter = 0;

            // Array mit Indices aller werktage wird geshuffelt, die ersten x tage werden nacheinander befüllt, die übrigen hinten im array sind dann die random freien tage
            ArrayList<Integer> listOfIndices = new ArrayList<>();
            for (int i = 0; i < werktage.length; i++) {
                listOfIndices.add(i);
            }
            //System.out.println(listOfIndices);
            Collections.shuffle(listOfIndices);
            //System.out.println(listOfIndices);

            for (int i = 0; i < arbeitszeiten.length; i++) {
//                werktage[listOfIndices.get(i)] = decimalFormat.format(arbeitszeiten[i]);
//                werktage[listOfIndices.get(i)] = String.valueOf(Math.floor(arbeitszeiten[i] * 100) / 100);
                werktage[listOfIndices.get(i)] = decimalFormat.format(Math.floor(arbeitszeiten[i] * 100) / 100);
//                System.out.println(Arrays.asList(werktage));
//                System.out.println(">> " + tempcounter + " <<");
            }

            System.out.println("Werktage: " + Arrays.asList(werktage));

            double zahl = 0;
            //System.out.println(Arrays.asList(werktage));
//            for (String werktag : werktage) {
//                if (werktag != null) System.out.println(zahl += Double.parseDouble(werktag.replace(",", ".")));
//            }
//            System.out.println(zahl);
//            System.out.println(">> " + tempcounter + " <<");
//
//            System.out.println(arbeitszeitenCells);

            try {
                //Wir befüllen die Spalten, Dezimal, Arbeitszeit Netto, Aufgezeichnet am und Arbeitszeit
                String hourMinutes;
                double insgMinuten, minuten, sekunden;
                int stunden;
                for (int i = 0; i < arbeitszeitenCells.size(); i++) {
                    hourMinutes = "";
                    if (werktage[i] != null) {
                        //Wir befüllen die Spalte "Dezimal"
                        arbeitszeitenCells.get(i).setCellValue(werktage[i]);
                        //Wir befüllen sie Spalte "Arbeitszeit Netto"
                        arbeitszeitenCells.get(i).getRow().getCell(arbeitszeitenCells.get(i).getColumnIndex() + 1).setCellValue(werktage[i]);
                        //Wir befüllen die Spalte "Aufgezeichnet am"
                        LocalDate aufgezeichnetAm = arbeitszeitenCells.get(i).getRow().getCell(arbeitszeitenCells.get(i).getColumnIndex() - 2).getLocalDateTimeCellValue().toLocalDate().plusDays(1);
                        DayOfWeek day = aufgezeichnetAm.getDayOfWeek();
                        while (day == DayOfWeek.SUNDAY || Utils.isDatumEinFeiertag(aufgezeichnetAm, Integer.parseInt(datum[0]))) {
                            //System.out.println("Nächster Tag ist " + day);
                            aufgezeichnetAm = aufgezeichnetAm.plusDays(1);
                            day = aufgezeichnetAm.getDayOfWeek();
                        }
                        arbeitszeitenCells.get(i).getRow().getCell(arbeitszeitenCells.get(i).getColumnIndex() + 2).setCellValue(aufgezeichnetAm);
                        //Wir befüllen die Spalte "Arbeitszeit"
                        String temp = werktage[i].replace(",", ".");
                        insgMinuten = Double.parseDouble(temp) * 60;
                        stunden = (int) Double.parseDouble(temp);
                        minuten = insgMinuten % 60;
                        sekunden = Double.parseDouble("0." + String.valueOf(insgMinuten).split("\\.")[1]);
                        sekunden = sekunden * 60;

                        if (stunden >= 10) hourMinutes += stunden + ":";
                        else hourMinutes += "0" + stunden + ":";
                        if (minuten >= 10) hourMinutes += String.valueOf(minuten).split("\\.")[0];
                        else hourMinutes += "0" + String.valueOf(minuten).split("\\.")[0];
                        if (sekunden >= 10) hourMinutes += ":" + (int) sekunden;
                        else hourMinutes += ":" + "0" + (int) sekunden;

                        arbeitszeitenCells.get(i).getRow().getCell(arbeitszeitenCells.get(i).getColumnIndex() - 1).setCellValue(hourMinutes);

                    } else {
                        arbeitszeitenCells.get(i).setCellValue("");
                        arbeitszeitenCells.get(i).getRow().getCell(arbeitszeitenCells.get(i).getColumnIndex() + 2).setCellValue("");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Fehler");
            }

            System.out.println("arbeitszeitenCells: " + arbeitszeitenCells);

            //Excel-Output-Dateien
//            try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "\\test" + counter++ + ".xlsx")) {
//                workbook.write(fileOutputStream);
//            }

            String fileName = abrechnungsmonat.replace("/", "-");
            String filePathWithName = outputPath + "\\" + fileName + DOCUMENT_FILE_SUFFIX;
            if (!isErsetzenSelected) {
                File pdfFile = new File(filePathWithName);
                if (pdfFile.exists()) {
                    int count = 0;

                    File newFile;
                    do {
                        count++;
                        String newFileName = outputPath + "\\" + fileName + "_" + count + DOCUMENT_FILE_SUFFIX;
                        newFile = new File(newFileName);
                    } while (newFile.exists());

                    PdfGenerator pdfGenerator = new PdfGenerator();
                    pdfGenerator.createPdf(workbook, outputPath, fileName + "_" + count);
                    //System.out.println("PDF file created: " + newFile.getAbsolutePath());
                } else {
                    PdfGenerator pdfGenerator = new PdfGenerator();
                    pdfGenerator.createPdf(workbook, outputPath, fileName);
                }
            } else {
                PdfGenerator pdfGenerator = new PdfGenerator();
                pdfGenerator.createPdf(workbook, outputPath, fileName);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
