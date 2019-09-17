package it.algos.vaadwam.migration;

import it.algos.vaadwam.modules.croce.Croce;

import java.time.LocalDateTime;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 06-dic-2018
 * Time: 15:04
 */
public class ImportResult {


    private boolean importato;

    private LocalDateTime inizio;

    private CroceAmb croceOld;

    private Croce croceNew;

    private int numFunzioniImportate;

    private int numServiziImportati;


    public ImportResult(CroceAmb croceOld) {
        this(croceOld, (Croce) null);
    }// end of constructor


    public ImportResult(Croce croceNew) {
        this((CroceAmb) null, croceNew);
    }// end of constructor


    public ImportResult(CroceAmb croceOld, Croce croceNew) {
        this.croceOld = croceOld;
        this.croceNew = croceNew;
        this.inizio = LocalDateTime.now();
        this.importato = false;
        this.numFunzioniImportate = 0;
        this.numServiziImportati = 0;
    }// end of constructor


    public LocalDateTime getInizio() {
        return inizio;
    }// end of method


    public void setInizio(LocalDateTime inizio) {
        this.inizio = inizio;
    }// end of method


    public CroceAmb getCroceOld() {
        return croceOld;
    }


    public void setCroceOld(CroceAmb croceOld) {
        this.croceOld = croceOld;
    }


    public Croce getCroceNew() {
        return croceNew;
    }


    public void setCroceNew(Croce croceNew) {
        this.croceNew = croceNew;
    }


    public boolean isImportato() {
        return importato;
    }// end of method


    public void setImportato(boolean importato) {
        this.importato = importato;
    }// end of method


    public void addFunzione() {
        numFunzioniImportate += 1;
    }


    public void addServizio() {
        numServiziImportati += 1;
    }


    public int getNumFunzioniImportate() {
        return numFunzioniImportate;
    }


    public void setNumFunzioniImportate(int numFunzioniImportate) {
        this.numFunzioniImportate = numFunzioniImportate;
    }


    public int getNumServiziImportati() {
        return numServiziImportati;
    }


    public void setNumServiziImportati(int numServiziImportati) {
        this.numServiziImportati = numServiziImportati;
    }

}// end of class
