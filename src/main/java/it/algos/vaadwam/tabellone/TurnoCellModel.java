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

    void setUsaHeaders(boolean usaHeaders);
    boolean getUsaHeaders();

    void setHeader1(String header);
    String getHeader1();

    void setHeader2(String header);
    String getHeader2();


    @Include({"coloreCella", "coloreTesto", "nomeIcona", "nomeMilite", "funzione", "nomeIconaAvviso", "coloreIconaAvviso", "coloreIcona"})
    void setRighecella(List<RigaCella> righe);

}// end of interface
