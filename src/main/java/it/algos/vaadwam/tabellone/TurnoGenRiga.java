package it.algos.vaadwam.tabellone;

import it.algos.vaadflow.enumeration.EAColor;
import lombok.Data;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Singola riga con nome servizio e valori booleani dei checkboxes nel polymer TurnoGen
 */
@Data
public class TurnoGenRiga {

    public TurnoGenRiga() {
    }

    public TurnoGenRiga(int id) {
        this.id = id;
    }

    private int id;
    private String nomeServizio;
    private List<Boolean> flags;
}
