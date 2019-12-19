package it.algos.vaadflow.enumeration;

import it.algos.vaadflow.modules.preferenza.PreferenzaService;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 15-nov-2019
 * Time: 17:40
 */
public enum EALogAction implements IAEnum {

    nessuno, terminale, collectionMongo, sendMail;


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

        for (EALogAction eaLogAction : EALogAction.values()) {
            testo.append(eaLogAction.name());
            testo.append(VIRGOLA);
        }// end of for cycle

        testo = new StringBuilder(testo.substring(0, testo.length() - 1));
        testo.append(PUNTO_VIRGOLA);
        testo.append(name());

        return testo.toString();
    }// end of method


    /**
     * Azione memorizzata nelle preferenze <br>
     *
     * @return azione
     */
    public static EALogAction get(PreferenzaService pref) {
        EALogAction eaLogAction = null;
        String actionStr = VUOTA;

        if (pref != null) {
            actionStr = pref.getStr(EAPreferenza.logAction);
        }// end of if cycle

        if (actionStr.length() > 0) {
            eaLogAction = EALogAction.valueOf(actionStr);
        }// end of if cycle

        return eaLogAction;
    }// end of method

}// end of enumeration
