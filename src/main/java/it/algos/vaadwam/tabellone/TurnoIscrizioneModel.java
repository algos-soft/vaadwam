package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.AllowClientUpdates;
import it.algos.vaadwam.modules.milite.Milite;

import java.util.List;

/**
 * Modello per un singolo elemento iscrizione nella scheda di edit turno
 */
public class TurnoIscrizioneModel {

    //--identificatore iscrizione
    private String keyTag;

    private String inizio;

    private String fine;

    private String note;

    private String colore;

    private String icona;

    private String idFunzione;

    private String funzione;

    private String idMilite;

    private String milite;

    private boolean abilitata;

    private boolean abilitataPicker;

    private List<MiliteComboBean> militi;


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

    public String getIdFunzione() {
        return idFunzione;
    }

    public void setIdFunzione(String idFunzione) {
        this.idFunzione = idFunzione;
    }

    public String getFunzione() {
        return funzione;
    }


    public void setFunzione(String funzione) {
        this.funzione = funzione;
    }

    public String getIdMilite() {
        return idMilite;
    }

    public void setIdMilite(String idMilite) {
        this.idMilite = idMilite;
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


    public String getKeyTag() {
        return keyTag;
    }


    public void setKeyTag(String keyTag) {
        this.keyTag = keyTag;
    }


    public List<MiliteComboBean> getMiliti() {
        return militi;
    }

    public void setMiliti(List<MiliteComboBean> militi) {
        this.militi = militi;
    }
}
