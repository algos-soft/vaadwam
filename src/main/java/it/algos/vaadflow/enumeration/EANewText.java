package it.algos.vaadflow.enumeration;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 16-nov-2019
 * Time: 17:33
 */
public enum EANewText implements IAEnum {

    nuovoInglese("new"),
    nuovoItaliano("nuovo");

    private final String tag;


    EANewText(String tag) {
        this.tag = tag;
    }// end of constructor


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

        for (EANewText eaNewText : EANewText.values()) {
            testo += eaNewText.tag;
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += tag;

        return testo;
    }// end of method

}// end of enumeration
