package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 06-mar-2020
 * Time: 22:24
 * Modello dati per collegare questa classe java col polymer
 */
public interface TurnoEditModel extends TemplateModel {

    void setIscrizioni(List<TurnoIscrizioneModel> iscrizioni);
    List<TurnoIscrizioneModel> getIscrizioni();

    void setGiorno(String giorno);

    void setServizio(String servizio);

    void setOrario(String orario);

    void setUsaOrarioLabel(boolean usaOrarioLabel);

    void setUsaOrarioPicker(boolean usaOrarioPicker);

    void setPrimaIscrizione(boolean primaIscrizione);

    void setSecondaIscrizione(boolean secondaIscrizione);

    void setTerzaIscrizione(boolean terzaIscrizione);

    void setQuartaIscrizione(boolean terzaIscrizione);

    String getInizioExtra();

    void setInizioExtra(String inizioExtra);

    String getFineExtra();

    void setFineExtra(String fineExtra);

    String getInizioPrima();

    void setInizioPrima(String inizioPrima);

    String getInizioSeconda();

    void setInizioSeconda(String inizioSeconda);

    String getInizioTerza();

    void setInizioTerza(String inizioTerza);

    String getInizioQuarta();

    void setInizioQuarta(String inizioQuarta);

    String getFinePrima();

    void setFinePrima(String finePrima);

    String getFineSeconda();

    void setFineSeconda(String fineSeconda);

    String getFineTerza();

    void setFineTerza(String fineTerza);

    String getFineQuarta();

    void setFineQuarta(String fineQuarta);

    String getNotePrima();

    void setNotePrima(String notePrima);

    String getNoteSeconda();

    void setNoteSeconda(String noteSeconda);

    String getNoteTerza();

    void setNoteTerza(String noteTerza);

    String getNoteQuarta();

    void setNoteQuarta(String noteQuarta);

    void setColorePrima(String colorePrima);

    void setColoreSeconda(String coloreSeconda);

    void setColoreTerza(String coloreTerza);

    void setColoreQuarta(String coloreQuarta);


    void setIconaPrima(String iconaPrima);

    void setIconaSeconda(String iconaSeconda);

    void setIconaTerza(String iconaTerza);

    void setIconaQuarta(String iconaQuarta);

    void setFunzionePrima(String funzionePrima);

    void setFunzioneSeconda(String funzioneSeconda);

    void setFunzioneTerza(String funzioneTerza);

    void setFunzioneQuarta(String funzioneQuarta);

    String getMilitePrima();

    void setMilitePrima(String militePrima);

    String getMiliteSeconda();

    void setMiliteSeconda(String militeSeconda);

    String getMiliteTerza();

    void setMiliteTerza(String militeTerza);

    String getMiliteQuarta();

    void setMiliteQuarta(String militeQuarta);

    void setAbilitataPrima(boolean abilitataPrima);

    void setAbilitataPickerPrima(boolean abilitataPickerPrima);

    void setAbilitataPickerSeconda(boolean abilitataPickerSeconda);

    void setAbilitataPickerTerza(boolean abilitataPickerTerza);

    void setAbilitataPickerQuarta(boolean abilitataPickerQuarta);

    void setAbilitataSeconda(boolean abilitataSeconda);

    void setAbilitataTerza(boolean abilitataTerza);

    void setAbilitataQuarta(boolean abilitataQuarta);

}// end of interface

