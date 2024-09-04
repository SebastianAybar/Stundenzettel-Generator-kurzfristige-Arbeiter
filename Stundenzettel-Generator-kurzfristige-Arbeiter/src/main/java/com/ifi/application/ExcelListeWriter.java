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

    public void writeToExcel(List<List<MitarbeiterMonat>> jahresliste, double stundenlohn) {
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
                    String[] datum = monatsliste.get(i).getAbrechnungsmonat().split("/");
                    List<LocalDate> alleTageDesMonats = getAlleTageDesMonats(datum);
                    List<Cell> arbeitszeitenCells = new ArrayList<>();
                    int counterTage = 0;
                    List<Row> rowsToRemove = new ArrayList<>();

                    for (Row row : currentSheet) {
                        //String aktuellerMontag = "";
                        for (Cell cell : row) {
                            if (cell.getCellType() == CellType.STRING) {
                                System.out.println("Cell <" + cell.getStringCellValue() + "> is of type STRING");
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
                                    if (counterTage < alleTageDesMonats.size()) {
                                        //Wir befüllen die Spalte Datum
                                        cell.setCellValue(alleTageDesMonats.get(counterTage));
                                        //Wir befüllen die Spalte KW
                                        cell.getRow().getCell(cell.getColumnIndex() - 2).setCellValue(alleTageDesMonats.get(counterTage).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
                                        //Wir befüllen die Spalte Wochentag
                                        DateTimeFormatter deutschFormatierer = DateTimeFormatter.ofPattern("EEEE", Locale.GERMAN);
                                        String wochentag = alleTageDesMonats.get(counterTage).format(deutschFormatierer);
                                        Cell wochentagCell = cell.getRow().getCell(cell.getColumnIndex() - 1);
                                        wochentagCell.setCellValue(wochentag);
                                        //Wir markieren und leeren die Sonn- und Feiertage
                                        if (wochentag.equals("Sonntag") || isDatumEinFeiertag(alleTageDesMonats.get(counterTage), Integer.parseInt(datum[0]))) {
                                            markiereRowAlsFreienTag(workbook, row, cell);
                                        }
                                        counterTage++;
                                    } else {
                                        rowsToRemove.add(cell.getRow());
                                        break;
                                    }
                                }
                                if (cellValue.startsWith("<<Std")) {
                                    arbeitszeitenCells.add(cell);
                                }
                            } else {
                                System.out.println("Aktuelle Zelle (" + cell.getRowIndex() + ", " + cell.getColumnIndex() + ") aus der Stundenzettel-Vorlage enthält kein <tag>. Wird übersprungen.");
                            }
                        }
                    }

                    for (Row row : rowsToRemove) {
                        currentSheet.removeRow(row);
                    }

                    //Wir erstellen ein Array mit den normalverteilten Arbeitszeiten
                    double svBrutto = Double.parseDouble(monatsliste.get(i).getSvBrutto().replace(",", "."));
                    double stundensatz = svBrutto / stundenlohn;

                    double meanProportionPerEuro = 2.5 / 520;
                    double totalMean;

                    if (svBrutto <= 520) {
                        totalMean = 2.5;
                    } else {
                        totalMean = meanProportionPerEuro * svBrutto;
                    }

                    double arbeitstage = stundensatz / totalMean;
                    int gerundeteArbeitstage = (int) Math.round(arbeitstage);

                    System.out.println("stundensatz: " + stundensatz);

                    if (gerundeteArbeitstage == 0) gerundeteArbeitstage = 1;


                    double gerundeterStundensatz = stundensatz * 10;
                    gerundeterStundensatz = Math.round(gerundeterStundensatz);
                    System.out.println("gerundeter stundensatz: " + gerundeterStundensatz);
                    gerundeterStundensatz = gerundeterStundensatz / 10;
                    System.out.println("gerundeter stundensatz: " + gerundeterStundensatz);


                    // Prüfen, ob die Anzahl der Arbeitstage, die "gearbeitet wurden" auch in den Monat passen
                    // Das ist nicht der Fall, wenn bspw. der Stundenlohn im Vergleich zum svBrutto sehr niedrig ist und die Person hätte zu viele Stunden bzw. Tage arbeiten müssen, um das zu erreichen
                    if (gerundeteArbeitstage > arbeitszeitenCells.size()) {
                        displayErrorInGui("Das Gehalt übersteigt die mögliche Monatsarbeitszeit im Verhältnis zum angegebenen Stundenlohn ");
                        System.out.println("GUI ERROR sollte angezeigt werden. Check code!");
                        return;
                    }

                    //Wir erstellen ein Array mit den Arbeitszeiten
                    double[] arbeitszeiten = generateRandomNumbers(gerundeteArbeitstage, totalMean, 1);
                    double sum = 0;
                    for (double value : arbeitszeiten) {
                        sum += value;
                    }
                    for (int j = 0; j < arbeitszeiten.length; j++) {
                        arbeitszeiten[j] = arbeitszeiten[j] * (gerundeterStundensatz / sum);
                    }
                    double sumAfter = 0;
                    for (double value : arbeitszeiten) {
                        sumAfter += value;
                    }


                    String[] werktage = new String[arbeitszeitenCells.size()];
                    Random randomNumberGen = new Random();
                    int randomNumber;
                    DecimalFormat decimalFormat = new DecimalFormat("###.##");
                    int tempcounter = 0;

                    // Array mit Indices aller werktage wird geshuffelt, die ersten x tage werden nacheinander befüllt, die übrigen hinten im array sind dann die random freien tage
                    ArrayList<Integer> listOfIndices = new ArrayList<>();
                    for (int j = 0; j < werktage.length; j++) {
                        listOfIndices.add(j);
                    }
                    System.out.println(listOfIndices);
                    Collections.shuffle(listOfIndices);
                    System.out.println(listOfIndices);

                    for (int j = 0; j < arbeitszeiten.length; j++) {
                        werktage[listOfIndices.get(j)] = decimalFormat.format(arbeitszeiten[j]);
//                werktage[listOfIndices.get(i)] = String.valueOf(arbeitszeiten[i]);
                        System.out.println(Arrays.asList(werktage));
                        System.out.println(">> " + tempcounter + " <<");
                    }

                    double zaahl = 0;
                    System.out.println(Arrays.asList(werktage));
                    for (String werktag : werktage) {
                        if (werktag != null) System.out.println(zaahl += Double.parseDouble(werktag.replace(",", ".")));
                    }
                    System.out.println(zaahl);
                    System.out.println(">> " + tempcounter + " <<");


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
                                    System.out.println("Nächster Tag ist " + day);
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
//                            arbeitszeitenCells.get(k).getRow().getCell(arbeitszeitenCells.get(k).getColumnIndex() - 1).setCellValue("");
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

//                 Excel-Output-Dateien
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "\\test" + counter++ + ".xlsx")) {
                    workbook.write(fileOutputStream);
                }

                // PDF-Output-Dateien generieren
                PdfGenerator pdfGenerator = new PdfGenerator();
                pdfGenerator.createPdf(workbook, outputPath, monatsliste.get(0).getAbrechnungsmonat().replace("/", "-"));


            } catch (Exception e) {
                System.out.println("Error in writeToExcel(): " + e);
            }
        }
    }

}
