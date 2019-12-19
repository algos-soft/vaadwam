package it.algos.vaadflow.enumeration;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 16-nov-2019
 * Time: 17:21
 */
public enum EASearchText implements IAEnum {

    cerca, ricerca, find;


    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        String testo = VUOTA;

        for (EASearchText eaSearchText : EASearchText.values()) {
            testo += eaSearchText.name();
            testo += TRE_PUNTI;
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();
        testo += TRE_PUNTI;

        return testo;
    }// end of method

}// end of enumeration
