package it.algos.vaadwam.tabellone;

import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.APresenter;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwam.modules.riga.Riga;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadwam.application.WamCost.TAG_TAB;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: dom, 23-set-2018
 * Time: 10:17
 */
@SpringComponent
@UIScope
@Qualifier(TAG_TAB)
@AIScript(sovrascrivibile = false)
@Slf4j
public class TabellonePresenter extends APresenter {

    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service layer di collegamento per la Repository e la Business Logic
     */
    @Autowired
    public TabellonePresenter(@Qualifier(TAG_TAB) IAService service) {
        super(Riga.class, service);
    }// end of Spring constructor

}// end of class
