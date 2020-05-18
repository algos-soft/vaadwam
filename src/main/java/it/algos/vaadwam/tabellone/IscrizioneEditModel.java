package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface IscrizioneEditModel extends TemplateModel  {

    void setGiorno(String giorno);

    void setServizio(String servizio);

    void setOrario(String orario);

    void setUsaOrarioLabel(boolean usaOrarioLabel);

    void setUsaOrarioPicker(boolean usaOrarioPicker);

    void setIcona(String icona);

    void setFunzione(String funzione);

    void setMilite(String milite);

    void setOraInizio(String orario);

    void setOraFine(String orario);

    void setNote(String note);

    void setReadOnly(boolean readOnly);


    String getOraInizio();

    String getOraFine();

    String getNote();

    void setMinSettimaneRipeti(int min);
    int getMinSettimaneRipeti();

    void setMaxSettimaneRipeti(int max);
    int getMaxSettimaneRipeti();


}
