package it.algos.vaadwam.tabellone;

import it.algos.vaadflow.service.ATextService;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Jul-2019
 * Time: 10:16
 */
public enum EACancellazione {
    sempre, mai, tempoTrascorso, tempoMancante;

    private final static String VIR = ",";

    private final static String PV = ";";

    private static ATextService text = ATextService.getInstance();


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
