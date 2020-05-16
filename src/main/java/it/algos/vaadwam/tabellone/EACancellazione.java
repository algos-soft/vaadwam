package it.algos.vaadwam.tabellone;

import it.algos.vaadflow.enumeration.IAEnum;
import it.algos.vaadflow.service.ATextService;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Jul-2019
 * Time: 10:16
 */
public enum EACancellazione implements IAEnum {
    sempre, mai, tempoTrascorso, tempoMancante;

    private final static String VIR = ",";

    private final static String PV = ";";

    private static ATextService text = ATextService.getInstance();


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

        for (EACancellazione eaCanc : EACancellazione.values()) {
            testo += eaCanc.name();
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }// end of method


    public static String getValues() {
        StringBuilder buffer = new StringBuilder();

        for (EACancellazione cancellazione : EACancellazione.values()) {
            buffer.append(cancellazione.name()).append(VIR);
        }// end of for cycle

        return text.levaCoda(buffer.toString(), VIR);
    }// end of method


    public static String getValuesStandard() {
        return getValues() + PV + EACancellazione.sempre.name();
    }// end of method

}// end of enumeration class
