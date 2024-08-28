package com.demo.application;


import java.io.InputStream;

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
        String resourceFilePath = "/Stundenzettel_Vorlage.xlsx";

    }

}
