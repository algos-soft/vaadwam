package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Modello dati per TurnoGenPolymer
 */
public interface TurnoGenModel extends TemplateModel {

    void setRighe(List<TurnoGenRiga> righe);
    List<TurnoGenRiga> getRighe();

    List<String> getTitoliGiorno();
    void setTitoliGiorno(List<String> titoliGiorno);

}
