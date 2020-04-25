package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.AllowClientUpdates;
import lombok.Data;

/**
 * Modello per un singolo elemento iscrizione nel form di edit turno
 */
public class TurnoIscrizioneModel {

    private String inizio;

    private String fine;

    private String note;

    private String colore;

    private String icona;

    private String funzione;

    private String milite;

    private boolean abilitata;

    private boolean abilitataPicker;


    public String getInizio() {
        return inizio;
    }

    @AllowClientUpdates
    public void setInizio(String inizio) {
        this.inizio = inizio;
    }

    public String getFine() {
        return fine;
    }

    @AllowClientUpdates
    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getNote() {
        return note;
    }

    @AllowClientUpdates
    public void setNote(String note) {
        this.note = note;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getIcona() {
        return icona;
    }

    public void setIcona(String icona) {
        this.icona = icona;
    }

    public String getFunzione() {
        return funzione;
    }

    public void setFunzione(String funzione) {
        this.funzione = funzione;
    }

    public String getMilite() {
        return milite;
    }

    public void setMilite(String milite) {
        this.milite = milite;
    }

    public boolean isAbilitata() {
        return abilitata;
    }

    public void setAbilitata(boolean abilitata) {
        this.abilitata = abilitata;
    }

    public boolean isAbilitataPicker() {
        return abilitataPicker;
    }

    public void setAbilitataPicker(boolean abilitataPicker) {
        this.abilitataPicker = abilitataPicker;
    }
}
