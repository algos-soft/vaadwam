package it.algos.vaadwam.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 27-mag-2020
 * Time: 10:36
 */
public enum EAWamLogType {
    nuovaIscrizione("nuovaIscrizione"),

    modificaIscrizione("modificaIscrizione"),

    cancellazioneIscrizione("cancellazioneIscrizione"),

    aggiuntaTurnoExtra("aggiuntaTurnoExtra"),

    nuovoMilite("nuovoMilite"),

    modificaMilite("modificaMilite"),

    modificaProfile("modificaProfile"),

    creazioneTurno("creazioneTurno"),

    cancellazioneTurno("cancellazioneTurno"),
    ;


    private String tag;


    EAWamLogType(String tag) {
        this.setTag(tag);
    }// fine del costruttore


    public static EAWamLogType getType(String tag) {
        EAWamLogType[] types = values();

        for (EAWamLogType type : values()) {
            if (type.getTag().equals(tag)) {
                return type;
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of static method


    public static List<String> getAll() {
        List<String> lista = new ArrayList<>();

        for (EAWamLogType type : values()) {
            lista.add(type.tag);
        }// end of for cycle

        return lista;
    }// end of static method


    private void setTag(String tag) {
        this.tag = tag;
    }// end of method


    public String getTag() {
        return tag;
    }// end of method

}// end of enumeration

