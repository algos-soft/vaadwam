package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 06-mar-2020
 * Time: 22:24
 * Modello dati per collegare TurnoEditPolymer.java col polymer turno-edit.html <br>
 */
public interface TurnoEditModel extends TemplateModel {

    List<TurnoIscrizioneModel> getIscrizioni();

    void setIscrizioni(List<TurnoIscrizioneModel> iscrizioni);

    void setGiorno(String giorno);

    void setServizio(String servizio);

    void setOrario(String orario);

    void setUsaOrarioLabel(boolean usaOrarioLabel);

    void setUsaOrarioPicker(boolean usaOrarioPicker);

    String getInizioExtra();

    void setInizioExtra(String inizioExtra);

    String getFineExtra();

    void setFineExtra(String fineExtra);

}// end of interface

