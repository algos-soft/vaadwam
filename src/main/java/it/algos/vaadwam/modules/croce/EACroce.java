package it.algos.vaadwam.modules.croce;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: dom, 02-set-2018
 * Time: 08:11
 */
public enum EACroce {
    crf, crpt, pap, gaps, demo;


    public static List<String> getValues() {
        List<String> lista = new ArrayList<>();

        for (EACroce croce : values()) {
            lista.add(croce.name());
        }// end of for cycle

        return lista;
    }// end of static method

}// end of Enumeration class
