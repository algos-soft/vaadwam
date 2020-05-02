package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Modello dati per il dialogo edit turno
 */
public interface TurnoDialogModel extends TemplateModel {

    List<TurnoIscrizioneModel> getIscrizioni();


}

