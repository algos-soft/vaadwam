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

    void setUsaOrarioLabel(boolean usaOrarioLabel);

    void setUsaOrarioPicker(boolean usaOrarioPicker);

    void setPrimaIscrizione(boolean primaIscrizione);

    String getInizioExtra();

    void setInizioExtra(String inizioExtra);

    String getFineExtra();

    void setFineExtra(String fineExtra);

    String getInizioPrima();

    void setInizioPrima(String inizioPrima);

    String getFinePrima();

    void setFinePrima(String finePrima);

    String getNotePrima();

    void setNotePrima(String notePrima);

    String getIconaPrima();

    void setIconaPrima(String iconaPrima);

    String getMilitePrima();

    void setMilitePrima(String militePrima);

}// end of interface

