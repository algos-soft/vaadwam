package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 06-mar-2020
 * Time: 22:24
 * Modello dati per collegare questa classe java col polymer
 */
public interface TurnoEditModel extends TemplateModel {

    void setGiorno(String giorno);

    void setServizio(String servizio);

    void setOrario(String orario);

    void setUsaOrario(boolean usaOrario);

    void setNotUsaOrario(boolean notUsaOrario);

    String getInizioExtra();

    void setInizioExtra(String inizioExtra);

    String getFineExtra();

    void setFineExtra(String fineExtra);

}// end of interface

