package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.AllowClientUpdates;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Modello dati per TurnoGenPolymer
 */
public interface TurnoGenModel extends TemplateModel {

    void setTitle(String title);
    String getTitle();

    void setSubtitle(String subtitle);
    String getSubtitle();

    void setRighe(List<TurnoGenRiga> righe);
    List<TurnoGenRiga> getRighe();

    List<String> getTitoliGiorno();
    void setTitoliGiorno(List<String> titoliGiorno);

}
