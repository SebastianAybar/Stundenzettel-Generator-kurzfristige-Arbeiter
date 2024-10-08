package com.demo.application;

public class MitarbeiterMonat {
    private String berater;
    private String mandant;
    private String abrechnungsmonat;
    private String nachnameVorname;
    private String eintrittsdatum;
    private String austrittsdatum;
    private String personengruppenschluesselKurz;
    private String personengruppenschluesselLang;
    private String svBrutto;
    private String arbeitszeit;

    public MitarbeiterMonat() {
    }

    public MitarbeiterMonat(String berater, String mandant, String abrechnungsmonat, String nachnameVorname, String eintrittsdatum, String austrittsdatum, String personengruppenschluesselKurz, String personengruppenschluesselLang, String svBrutto, String arbeitszeit) {
        this.berater = berater;
        this.mandant = mandant;
        this.abrechnungsmonat = abrechnungsmonat;
        this.nachnameVorname = nachnameVorname;
        this.eintrittsdatum = eintrittsdatum;
        this.austrittsdatum = austrittsdatum;
        this.personengruppenschluesselKurz = personengruppenschluesselKurz;
        this.personengruppenschluesselLang = personengruppenschluesselLang;
        this.svBrutto = svBrutto;
        this.arbeitszeit = arbeitszeit;
    }

    public String getBerater() {
        return berater;
    }

    public void setBerater(String berater) {
        this.berater = berater;
    }

    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

    public String getAbrechnungsmonat() {
        return abrechnungsmonat;
    }

    public void setAbrechnungsmonat(String abrechnungsmonat) {
        this.abrechnungsmonat = abrechnungsmonat;
    }

    public String getNachnameVorname() {
        return nachnameVorname;
    }

    public void setNachnameVorname(String nachnameVorname) {
        this.nachnameVorname = nachnameVorname;
    }

    public String getEintrittsdatum() {
        return eintrittsdatum;
    }

    public void setEintrittsdatum(String eintrittsdatum) {
        this.eintrittsdatum = eintrittsdatum;
    }

    public String getAustrittsdatum() {
        return austrittsdatum;
    }

    public void setAustrittsdatum(String austrittsdatum) {
        this.austrittsdatum = austrittsdatum;
    }

    public String getPersonengruppenschluesselKurz() {
        return personengruppenschluesselKurz;
    }

    public void setPersonengruppenschluesselKurz(String personengruppenschluesselKurz) {
        this.personengruppenschluesselKurz = personengruppenschluesselKurz;
    }

    public String getPersonengruppenschluesselLang() {
        return personengruppenschluesselLang;
    }

    public void setPersonengruppenschluesselLang(String personengruppenschluesselLang) {
        this.personengruppenschluesselLang = personengruppenschluesselLang;
    }

    public String getSvBrutto() {
        return svBrutto;
    }

    public void setSvBrutto(String svBrutto) {
        this.svBrutto = svBrutto;
    }

    public String getArbeitszeit() {
        return arbeitszeit;
    }

    public void setArbeitszeit(String arbeitszeit) {
        this.arbeitszeit = arbeitszeit;
    }

    @Override
    public String toString() {
        return "Mitarbeiter: " +
                berater + ", " +
                mandant + ", " +
                abrechnungsmonat + ", " +
                nachnameVorname + ", " +
                eintrittsdatum + ", " +
                austrittsdatum + ", " +
                personengruppenschluesselKurz + ", " +
                personengruppenschluesselLang + ", " +
                svBrutto + ", " +
                arbeitszeit;
    }
}
