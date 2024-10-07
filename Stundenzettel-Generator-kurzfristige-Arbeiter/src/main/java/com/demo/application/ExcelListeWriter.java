package com.demo.application;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

import static com.demo.helper.Constants.PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL;
import static com.demo.helper.Utils.*;
import static com.demo.helper.Validation.displayErrorInGui;

public class ExcelListeWriter {

    private final String outputPath;

    public ExcelListeWriter(String outputPath) {
        this.outputPath = outputPath;
    }

    public void writeToExcel(List<List<MitarbeiterMonat>> jahresliste, String lohn) {
        int counter = 1;

        for (List<MitarbeiterMonat> monatsliste : jahresliste) {

            Workbook workbook = null;

            try {

                InputStream fileStundenzettelVorlageLocal = new FileInputStream(PATH_FILE_STUNDENZETTEL_VORLAGE_LOCAL);
                workbook = WorkbookFactory.create(fileStundenzettelVorlageLocal);
                Sheet currentSheet = workbook.getSheetAt(0);

                for (int i = 0; i < monatsliste.size(); i++) {

                    workbook.cloneSheet(0);
                    currentSheet = workbook.getSheetAt(i + 1);

                    //Liste der Zellen in Spalte "Dezimal" in der Vorlage
                    List<Cell> arbeitszeitenCells = new ArrayList<>();

                    //Liste aller LocalDate's im Monat
                    String[] datum = monatsliste.get(i).getAbrechnungsmonat().split("/");
                    List<LocalDate> alleTageDesMonatsAlt = getAlleTageDesMonatsAlt(datum);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MMM-uuuu");
                    LocalDate eintrittsdatum = LocalDate.parse(monatsliste.get(i).getEintrittsdatum(), formatter2);
                    LocalDate austrittsdatum = LocalDate.parse(monatsliste.get(i).getAustrittsdatum(), formatter2);
                    List<LocalDate> alleTageDesMonats = getAlleTageDesMonats(datum, eintrittsdatum.format(formatter), austrittsdatum.format(formatter));


                    int counterTage = 0;

                    //Sofern der Monat weniger als 31 Tage hat, müssen Zeilen entfernt werden
                    List<Row> rowsToRemove = new ArrayList<>();

                    for (Row row : currentSheet) {
//                        String aktuellerMontag = "";
                        for (Cell cell : row) {
                            if (cell.getCellType() == CellType.STRING) {
//                                System.out.println("Cell <" + cell.getStringCellValue() + "> is of type STRING");
                                String cellValue = cell.getStringCellValue().trim();
                                if (cellValue.equals("<<Abrechnungsmonat>>")) {
                                    cell.setCellValue(monatsliste.get(i).getAbrechnungsmonat());
                                }
                                if (cellValue.equals("<<Mitarbeiter>>")) {
                                    cell.setCellValue(monatsliste.get(i).getNachnameVorname());
                                }
                                if (cellValue.equals("<<Mitarbeiternummer>>")) {
                                    cell.setCellValue(monatsliste.get(i).getMandant());
                                }
                                if (cellValue.startsWith("<<Tag")) {
                                    if (counterTage < alleTageDesMonatsAlt.size()) {
//                                        Wir befüllen die Spalte Datum
                                        cell.setCellValue(alleTageDesMonatsAlt.get(counterTage));
//                                        Wir befüllen die Spalte KW
                                        cell.getRow().getCell(cell.getColumnIndex() - 2).setCellValue(alleTageDesMonatsAlt.get(counterTage).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
//                                        Wir befüllen die Spalte Wochentag
                                        DateTimeFormatter deutschFormatierer = DateTimeFormatter.ofPattern("EEEE", Locale.GERMAN);
                                        String wochentag = alleTageDesMonatsAlt.get(counterTage).format(deutschFormatierer);
                                        Cell wochentagCell = cell.getRow().getCell(cell.getColumnIndex() - 1);
                                        wochentagCell.setCellValue(wochentag);
//                                        Wir markieren und leeren die Sonn- und Feiertage
                                        if (wochentag.equals("Sonntag") || isDatumEinFeiertag(alleTageDesMonatsAlt.get(counterTage), Integer.parseInt(datum[0]))) {
                                            markiereRowAlsFreienTag(workbook, row, cell);
                                        }
                                        counterTage++;
                                    } else {
                                        rowsToRemove.add(cell.getRow());
                                        break;
                                    }
                                }
                                if (cellValue.startsWith("<<Std")) {
                                    if (counterTage > 0 && counterTage < alleTageDesMonatsAlt.size() && alleTageDesMonats.contains(alleTageDesMonatsAlt.get(counterTage - 1))) {
                                        arbeitszeitenCells.add(cell);
                                    } else {
                                        cell.setCellValue("");
                                    }
                                }
                            } else {
                                //System.out.println("Aktuelle Zelle (" + cell.getRowIndex() + ", " + cell.getColumnIndex() + ") aus der Stundenzettel-Vorlage enthält kein <tag>. Wird übersprungen.");
                            }
                        }
                    }

                    for (Row row : rowsToRemove) {
                        currentSheet.removeRow(row);
                    }



                    //Wir berechnen die Variablen totalMean, gerundeteArbeitstage und den Stundensatz für die Normalverteilung
                    double svBrutto = Double.parseDouble(monatsliste.get(i).getSvBrutto().replace(",", "."));
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


                    System.out.println("Alle Local Date's im Monat: " + alleTageDesMonatsAlt);
                    System.out.println("Alle Local Date's innerhalb Beschäftigungszeitraumes: " + alleTageDesMonats);



//                     Prüfen, ob die Anzahl der Arbeitstage, die "gearbeitet wurden" auch in den Monat passen
//                     Das ist nicht der Fall, wenn bspw. der Stundenlohn im Vergleich zum svBrutto sehr niedrig ist und die Person hätte zu viele Stunden bzw. Tage arbeiten müssen, um das zu erreichen
                    if (gerundeteArbeitstage > arbeitszeitenCells.size()) {
                        displayErrorInGui("Das Gehalt übersteigt die mögliche Monatsarbeitszeit im Verhältnis zum angegebenen Stundenlohn ");
                        //System.out.println("GUI ERROR sollte angezeigt werden. Check code!");
                        return;
                    }

//                    Wir erstellen ein Array mit den Arbeitszeiten
                    double[] arbeitszeiten = generateRandomNumbers(gerundeteArbeitstage, totalMean, 1);
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


                    String[] werktage = new String[arbeitszeitenCells.size()];
                    DecimalFormat decimalFormat = new DecimalFormat("###.##");
                    int tempcounter = 0;

//                     Array mit Indices aller werktage wird geshuffelt, die ersten x tage werden nacheinander befüllt, die übrigen hinten im array sind dann die random freien tage
                    ArrayList<Integer> listOfIndices = new ArrayList<>();
                    for (int j = 0; j < werktage.length; j++) {
                        listOfIndices.add(j);
                    }
//                    System.out.println(listOfIndices);
                    Collections.shuffle(listOfIndices);
//                    System.out.println(listOfIndices);

                    for (int j = 0; j < arbeitszeiten.length; j++) {
                        werktage[listOfIndices.get(j)] = decimalFormat.format(arbeitszeiten[j]);
                        //System.out.println(Arrays.asList(werktage));
                        //System.out.println(">> " + tempcounter + " <<");
                    }

                    double zahl = 0;
                    //System.out.println(Arrays.asList(werktage));
                    /*for (String werktag : werktage) {
                        if (werktag != null) System.out.println(zahl += Double.parseDouble(werktag.replace(",", ".")));
                    }*/
                    //System.out.println(zahl);
                    //System.out.println(">> " + tempcounter + " <<");


                    try {
                        //Wir befüllen die Spalten, Dezimal, Arbeitszeit Netto, Aufgezeichnet am, und Arbeitszeit
                        String hourMinutes;
                        double insgMinuten, minuten, sekunden;
                        int stunden;

                        for (int k = 0; k < arbeitszeitenCells.size(); k++) {
                            hourMinutes = "";
                            if (werktage[k] != null) {
                                //Wir befüllen die Spalte "Dezimal"
                                arbeitszeitenCells.get(k).setCellValue(werktage[k]);
                                //Wir befüllen sie Spalte "Arbeitszeit Netto"
                                arbeitszeitenCells.get(k).getRow().getCell(arbeitszeitenCells.get(k).getColumnIndex() + 1).setCellValue(werktage[k]);
                                //Wir befüllen die Spalte "Aufgezeichnet am"
                                LocalDate aufgezeichnetAm = arbeitszeitenCells.get(k).getRow().getCell(arbeitszeitenCells.get(k).getColumnIndex() - 2).getLocalDateTimeCellValue().toLocalDate().plusDays(1);
                                DayOfWeek day = aufgezeichnetAm.getDayOfWeek();
                                while (day == DayOfWeek.SUNDAY || isDatumEinFeiertag(aufgezeichnetAm, Integer.parseInt(datum[0]))) {
                                    //System.out.println("Nächster Tag ist " + day);
                                    aufgezeichnetAm = aufgezeichnetAm.plusDays(1);
                                    day = aufgezeichnetAm.getDayOfWeek();
                                }
                                arbeitszeitenCells.get(k).getRow().getCell(arbeitszeitenCells.get(k).getColumnIndex() + 2).setCellValue(aufgezeichnetAm);
                                //Wir befüllen die Spalte "Arbeitszeit"
                                String temp = werktage[k].replace(",", ".");
                                insgMinuten = Double.parseDouble(temp) * 60;
                                stunden = (int) Double.parseDouble(temp);
                                minuten = insgMinuten % 60;
                                sekunden = Double.parseDouble("0." + String.valueOf(insgMinuten).split("\\.")[1]);
                                sekunden = sekunden * 60;

                                hourMinutes = hourMinutes + "0" + stunden + ":";
                                if (minuten >= 10) hourMinutes += String.valueOf(minuten).split("\\.")[0];
                                else hourMinutes += "0" + String.valueOf(minuten).split("\\.")[0];
                                if (sekunden >= 10) hourMinutes += ":" + (int) sekunden;
                                else hourMinutes += ":" + "0" + (int) sekunden;

                                arbeitszeitenCells.get(k).getRow().getCell(arbeitszeitenCells.get(k).getColumnIndex() - 1).setCellValue(hourMinutes);
                            } else {
                                arbeitszeitenCells.get(k).setCellValue("");
                                arbeitszeitenCells.get(k).getRow().getCell(arbeitszeitenCells.get(k).getColumnIndex() + 2).setCellValue("");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error");
                    }

                    //Excel Formeln werden nach dem Füllen der Felder noch einmal ausgeführt (z.B. für KW)
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    evaluator.evaluateAll();

                }

                workbook.removeSheetAt(0);

                //Excel-Output-Dateien
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "\\test" + counter++ + ".xlsx")) {
                    workbook.write(fileOutputStream);
                }

                // PDF-Output-Dateien generieren
                PdfGenerator pdfGenerator = new PdfGenerator();
                pdfGenerator.createPdf(workbook, outputPath, monatsliste.get(0).getAbrechnungsmonat().replace("/", "-"));


            } catch (Exception e) {
                System.out.println("Error in writeToExcel(): " + e);
                e.printStackTrace();
            }
        }
    }

}
