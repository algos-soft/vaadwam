package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadwam.modules.servizio.Servizio;

import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 05-mar-2019
 * Time: 14:52
 */
public interface ServizioCellModel extends TemplateModel {


    @Include({"code", "descrizione", "orarioDefinito"})
    void setServizio(Servizio servizio);

    void setIconeObbligatorie(List<String> iconeObbligatorie);

    void setIconeFacoltative(List<String> iconeFacoltative);

    void setOrario(String orario);

    void setLastInType(boolean lastInType);

    void setColore(String colore);

    void setGreenColor(boolean greenColor);

}// end of interface
