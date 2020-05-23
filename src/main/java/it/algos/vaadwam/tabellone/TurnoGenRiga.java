package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.AllowClientUpdates;
import lombok.Data;

import java.util.List;

/**
 * Singola riga con nome servizio e valori booleani dei checkboxes nel polymer TurnoGen
 */
public class TurnoGenRiga {

    public TurnoGenRiga() {
    }

    public TurnoGenRiga(int id) {
        this.id = id;
    }

    private int id;
    private String nomeServizio;
    private List<TurnoGenFlag> flags;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeServizio() {
        return nomeServizio;
    }

    public void setNomeServizio(String nomeServizio) {
        this.nomeServizio = nomeServizio;
    }

    public List<TurnoGenFlag> getFlags() {
        return flags;
    }

    public void setFlags(List<TurnoGenFlag> flags) {
        this.flags = flags;
    }
}
