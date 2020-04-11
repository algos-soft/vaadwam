package it.algos.vaadwam.tabellone;

import lombok.Data;

/**
 * Modello per un singolo elemento iscrizione nel form di edit turno
 */
@Data
public class TurnoIscrizioneModel {

    private boolean flagIscrizione;

    private String inizio;

    private String fine;

    private String note;

    private String colore;

    private String icona;

    private String funzione;

    private String milite;

    private boolean abilitata;

    private boolean abilitataPicker;

}
