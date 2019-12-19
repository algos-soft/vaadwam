package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Mar-2019
 * Time: 11:29
 * <p>
 * Modello per TurnoCellPolymer
 */
public interface TurnoCellModel extends TemplateModel {

    @Include({"colore", "icona", "milite", "iconaAvviso"})
    public void setRighecella(List<RigaCella> righe);

}// end of interface
