package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sat, 29-Jun-2019
 * Time: 11:13
 */
public interface LegendaModel extends TemplateModel {

    @Include({"titolo", "tag", "esadecimale", "legenda"})
    public void setColori(List<EAWamColore> colori);

}// end of interface
