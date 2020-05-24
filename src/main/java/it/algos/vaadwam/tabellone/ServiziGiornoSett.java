package it.algos.vaadwam.tabellone;

import it.algos.vaadwam.modules.servizio.Servizio;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che descrive un giorno della settimana e relativo elenco di servizi
 */
@Data
public class ServiziGiornoSett {
    private int idxGiornoSett;
    private List<Servizio> servizi;

    public ServiziGiornoSett(int idxGiornoSett) {
        this.idxGiornoSett = idxGiornoSett;
        this.servizi = new ArrayList<>();
    }

    public void addServizio(Servizio servizio){
        this.servizi.add(servizio);
    }
}
