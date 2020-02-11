package it.algos.vaadflow.enumeration;

import it.algos.vaadflow.modules.preferenza.PreferenzaService;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.service.AConsoleColorService.*;

/**
 * Created by gac on 22 ago 2015.
 */
public enum EALogLivello implements IAEnum {

    debug(GREEN), info(BLUE), warn(PURPLE), error(RED);

    public String color;


    EALogLivello(String color) {
        this.color = color;
    }// fine del costruttore

    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        StringBuilder testo = new StringBuilder(VUOTA);

        for (EALogLivello eaLogLevl : EALogLivello.values()) {
            testo.append(eaLogLevl.name());
            testo.append(VIRGOLA);
        }// end of for cycle

        testo = new StringBuilder(testo.substring(0, testo.length() - 1));
        testo.append(PUNTO_VIRGOLA);
        testo.append(this.name());

        return testo.toString();
    }// end of method

    /**
     * Azione memorizzata nelle preferenze <br>
     *
     * @return azione
     */
    public static EALogLivello get(PreferenzaService pref) {
        EALogLivello eaLogLevl = null;
        String livelloStr = VUOTA;

        if (pref != null) {
            livelloStr = pref.getStr(EAPreferenza.logLevelCorrente);
        }// end of if cycle

        if (livelloStr.length() > 0) {
            eaLogLevl = EALogLivello.valueOf(livelloStr);
        }// end of if cycle

        return eaLogLevl;
    }// end of method

} // fine della Enumeration
