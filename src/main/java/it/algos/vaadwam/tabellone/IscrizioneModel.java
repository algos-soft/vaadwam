package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.AllowClientUpdates;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

/**
 * Created by avalbonesi on 16/05/19.
 * <p>
 * Modello dati per una singola riga di iscrizione
 * nel form di editing di un turno
 */
@Data
@AllArgsConstructor
public class IscrizioneModel {

    private String icona;

    private String funzione;

    private String nome;

    private String note;

//    private String ore;
//
//    private String minuti;

    private String inizio;

    private String fine;

    private boolean nomeDisabilitato;
    private boolean noteOrariDisabilitati;

    private String colore;

    private String funzCode;


    /**
     * Costruttore semplice senza parametri. <br>
     * Indispensabile anche se non viene utilizzato
     * (anche solo per compilazione in sviluppo) <br>
     */
    public IscrizioneModel() {
    }// fine del metodo costruttore semplice

}// end of class
