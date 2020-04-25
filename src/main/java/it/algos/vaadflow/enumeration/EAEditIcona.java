package it.algos.vaadflow.enumeration;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 07-apr-2020
 * Time: 20:49
 */
public enum EAEditIcona implements IAEnum{
    edit, search, play;

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

        for (EAEditIcona eaEditIcona : EAEditIcona.values()) {
            testo += eaEditIcona.name();
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }// end of method

}// end of enumeration
