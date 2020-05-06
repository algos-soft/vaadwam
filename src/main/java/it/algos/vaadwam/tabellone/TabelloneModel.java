package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: alex
 * Date: ven, 06-mar-2020
 * Time: 22:24
 * Modello dati per il Polymer Tabellone<br>
 */
public interface TabelloneModel extends TemplateModel {

    List<String> getHeaders();

    boolean getSingola();
    void setSingola(boolean singola);

}
