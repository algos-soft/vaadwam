package it.algos.vaadflow.enumeration;

import static it.algos.vaadflow.service.AConsoleColorService.*;

/**
 * Created by gac on 22 ago 2015.
 */
public enum EALogLivello {

    debug(GREEN), info(BLUE), warn(PURPLE), error(RED);

    public String color;


    EALogLivello(String color) {
        this.color = color;
    }// fine del costruttore


} // fine della Enumeration
