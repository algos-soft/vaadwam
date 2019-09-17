package it.algos.vaadflow.modules.address;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 02-set-2018
 * Time: 18:38
 * Indirizzi di prova <br>
 */
public enum EAAddress {
    uno("via delle Rose, 17", "Mantova", "32010"),
    due("via Ruffini, 4 ", "Milano", "20103"),
    tre("corso Garibaldi, 58", "Palazzolo sull'Oglio", "25914"),
    quattro("piazza Murat, 1", "Bari", "99121"),
    cinque("largo Appennini, 6", "Avellino", "45100"),
    sei("via del Pratello, 2", "Bologna", "31232");


    private String indirizzo;
    private String localita;
    private String cap;


    EAAddress(String indirizzo, String localita, String cap) {
        this.setIndirizzo(indirizzo);
        this.setLocalita(localita);
        this.setCap(cap);
    }// fine del costruttore


    public String getIndirizzo() {
        return indirizzo;
    }// end of method

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }// end of method

    public String getLocalita() {
        return localita;
    }// end of method

    public void setLocalita(String localita) {
        this.localita = localita;
    }// end of method

    public String getCap() {
        return cap;
    }// end of method

    public void setCap(String cap) {
        this.cap = cap;
    }// end of method

}// end of enumeration class
