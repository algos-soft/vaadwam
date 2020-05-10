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

    void setGiorno(String giorno);

    void setServizio(String servizio);

    void setOrarioTurnoEditabile(boolean editabile);

    boolean getOrarioTurnoEditabile();

    void setOraInizio(String ora);

    String getOraInizio();

    void setOraFine(String ora);

    String getOraFine();

    void setNote(String note);

    String getNote();

    boolean getNoteVisibili(boolean visibili);

    void setNoteVisibili(boolean visibili);

    boolean getAbilitaCancellaTurno();

    void setAbilitaCancellaTurno(boolean abilita);


//    void setUsaOrarioLabel(boolean usaOrarioLabel);
//
//    void setUsaOrarioPicker(boolean usaOrarioPicker);

    String getInizioExtra();

    void setInizioExtra(String inizioExtra);

    String getFineExtra();

    void setFineExtra(String fineExtra);


}

