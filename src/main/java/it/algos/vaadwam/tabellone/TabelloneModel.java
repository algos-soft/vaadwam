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

    List<HeaderCellModel> getHeaders();
    void setHeaders(List<HeaderCellModel> headers);

    List<LegendaItemModel> getColoriLegenda();
    void setColoriLegenda(List<LegendaItemModel> coloriLegenda);

    boolean getSingola();
    void setSingola(boolean singola);

    String getWCol1();
    void setWCol1(String wCol1);

    String getWColonne();
    void setWColonne(String wColonne);

}
