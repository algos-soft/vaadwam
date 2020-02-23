package it.algos.vaadwam.iscrizioni;

import it.algos.vaadwam.modules.iscrizione.Iscrizione;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: dom, 23-feb-2020
 * Time: 17:37
 */
public class WrapIscrizione {

    private Iscrizione iscrizione;

    private boolean abilitata;


    public Iscrizione getIscrizione() {
        return iscrizione;
    }// end of method


    public void setIscrizione(Iscrizione iscrizione) {
        this.iscrizione = iscrizione;
    }// end of method


    public boolean isAbilitata() {
        return abilitata;
    }// end of method


    public void setAbilitata(boolean abilitata) {
        this.abilitata = abilitata;
    }// end of method

}// end of class
