package it.algos.vaadflow.enumeration;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 16-nov-2019
 * Time: 17:37
 */
public enum EAEditText implements IAEnum {

    open, edit, modifica, apre, apri;

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

        for (EAEditText eaEditText : EAEditText.values()) {
            testo += eaEditText.name();
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }// end of method

}// end of enumeration
