package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadwam.modules.funzione.Funzione;
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


    @Include({"code", "descrizione"})
    void setServizio(Servizio servizio);

    @Include({"code", "nomeIcona"})
    void setFunzioni(List<Funzione> funzioni);

    void setOrario(String orario);

    void setLastInType(boolean lastInType);

    void setColore(String colore);

}// end of interface
