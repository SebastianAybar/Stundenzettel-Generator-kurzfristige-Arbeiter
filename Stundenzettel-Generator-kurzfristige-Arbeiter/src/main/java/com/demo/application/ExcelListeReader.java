package com.demo.application;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.demo.helper.Validation.isSvBruttoValid;

public class ExcelListeReader {

    private final String pathInput;
    private List<MitarbeiterMonat> listMitarbeiterMonat;
    private boolean isAllRowsRead;

    public ExcelListeReader(String pathInput) {
        this.pathInput = pathInput;
        this.listMitarbeiterMonat = new ArrayList<>();
    }

    public List<List<MitarbeiterMonat>> getListOfAbrechnungsmonate(Double stundenlohn) {
        isAllRowsRead = false;
        readAllRows(stundenlohn);

        if (isAllRowsRead) {
            System.out.println("INFO: Alle gültigen Zeilen wurden erfolgreich eingelesen");

            // Wenn in der Excel keine Personen mit Gehalt existieren, bleibt die Liste leer.
            // Es werden also keine PDF-Dateien erstellt

            orderByAbrechnungsmonat(listMitarbeiterMonat);

            List<List<MitarbeiterMonat>> jahresliste = new ArrayList<>();

            List<MitarbeiterMonat> monatsliste = new ArrayList<>();
            String monat = listMitarbeiterMonat.get(0).getAbrechnungsmonat();

            for (MitarbeiterMonat row : listMitarbeiterMonat) {
                if (row.getAbrechnungsmonat().equals(monat)) {
                    monatsliste.add(row);
                } else {
                    jahresliste.add(monatsliste);
                    monatsliste = new ArrayList<>();
                    monat = row.getAbrechnungsmonat();
                    monatsliste.add(row);

                    printMonatsliste(monatsliste);
                }
            }
            jahresliste.add(monatsliste);

            return jahresliste;
        } else {
            System.out.println("ERROR: isAllRowsRead is false");
            return new ArrayList<>();
        }
    }

    public void readAllRows(Double stundenlohn) {
        try (FileInputStream inputStream = new FileInputStream(pathInput)) {
            Workbook excelFile = WorkbookFactory.create(inputStream);
            Sheet excelSheet = excelFile.getSheetAt(0);
            System.out.println("INFO: Workbook-Objekt wurde erfolgreich erstellt aus der Datei: " + pathInput);
            listMitarbeiterMonat = new ArrayList<>();

            for (Row row : excelSheet) {
                MitarbeiterMonat mitarbeiterMonat = new MitarbeiterMonat();

                mitarbeiterMonat.setBerater(getCellAsString(row.getCell(0)));
                mitarbeiterMonat.setMandant(getCellAsString(row.getCell(1)));
                mitarbeiterMonat.setAbrechnungsmonat(getCellAsString(row.getCell(2)));
                mitarbeiterMonat.setNachnameVorname(getCellAsString(row.getCell(3)));
                mitarbeiterMonat.setEintrittsdatum(getCellAsString(row.getCell(4)));
                mitarbeiterMonat.setAustrittsdatum(getCellAsString(row.getCell(5)));
                mitarbeiterMonat.setPersonengruppenschluesselKurz(getCellAsString(row.getCell(6)));
                mitarbeiterMonat.setPersonengruppenschluesselLang(getCellAsString(row.getCell(7)));
                mitarbeiterMonat.setSvBrutto(getCellAsString(row.getCell(8)));
                mitarbeiterMonat.setArbeitszeit(getCellAsString(row.getCell(9)));

                if (!isSvBruttoValid(mitarbeiterMonat.getSvBrutto())) {
                    System.out.println("WARNING: Die aktuelle Zeile enthält keinen gültigen SvBrutto-Wert. Die Zeile wird übersprungen");
                } else {
                    if (Double.parseDouble(mitarbeiterMonat.getSvBrutto()) >= stundenlohn) {
                        listMitarbeiterMonat.add(mitarbeiterMonat);
                        System.out.println("INFO: Zeile ist gültig. Person >" + mitarbeiterMonat.getNachnameVorname() + "< wird hinzugefügt");
                    } else {
                        System.out.println("WARNING: Der SV-Brutto dieser Zeile ist nicht größer als der Stundenlohn. Person >" + mitarbeiterMonat.getNachnameVorname() + "< wird übersprungen");
                    }
                }
            }

            excelFile.close();

            isAllRowsRead = true;
        } catch (FileNotFoundException e) {
            isAllRowsRead = false;
            System.err.println("Diese Meldung sollte nie angezeigt werden, da dieser Fall bereits durch die Validierung-Methode isPathAnExcelFile() abgefangen wird");
        } catch (IndexOutOfBoundsException e) {
            isAllRowsRead = false;
            System.out.println("ERROR: Fehler outofbounds");
        } catch (IOException e) {
            isAllRowsRead = false;
            System.out.println("Error. Check code! (1): " + e);
        }
    }

    public void orderByAbrechnungsmonat(List<MitarbeiterMonat> list) {
        Comparator<MitarbeiterMonat> comparator = Comparator.comparing(MitarbeiterMonat::getAbrechnungsmonat);
        list.sort(comparator);
    }

    public static String getCellAsString(Cell cell) {
        if (cell != null) return cell.toString();
        else return "---";
    }

    public static Double getStundenlohnFromString(String stundenlohnAsString) {
        return Double.parseDouble(stundenlohnAsString.replace(",", "."));
    }

    public void printMonatsliste(List<MitarbeiterMonat> list) {
        for (MitarbeiterMonat mitarbeiterMonat : list) {
            //System.out.println(mitarbeiterMonat.toString());
        }
    }

    public void printList(List<MitarbeiterMonat> list) {
        for (MitarbeiterMonat mitarbeiterMonat : list) {
            //System.out.println(mitarbeiterMonat.toString());
        }
    }

    public void printJahresliste(List<List<MitarbeiterMonat>> lists) {
        //System.out.println("Jahresliste (alle erfassten Mitarbeiter):");
        for (List<MitarbeiterMonat> list : lists) {
            printList(list);
            //System.out.println();
        }
    }

}
