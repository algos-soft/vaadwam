package it.algos.vaadflow.ui;

import com.vaadin.flow.component.html.Label;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadflow.ui.list.ALayoutViewList;
import it.algos.vaadflow.ui.list.AViewList;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 14-ott-2018
 * Time: 16:28
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//@Slf4j
public abstract class ACronoViewList extends AGridViewList {

    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     */
    public ACronoViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
    }// end of Spring constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaSearch = false;
        super.usaPopupFiltro = true;
        super.usaBottoneDeleteAll = true;
        super.usaBottoneReset = true;
        super.isEntityDeveloper = true;
        super.usaBottoneNew = false;
    }// end of method


    /**
     * Placeholder (eventuale) per informazioni aggiuntive alla grid ed alla lista di elementi <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        alertPlacehorder.add(new Label("Lista visibile solo ai developer."));
        alertPlacehorder.add(new Label("Serve per costruire liste cronologiche"));
        alertPlacehorder.add(new Label("La lista viene creata in automatico allo startup del programma. Non si possono cancellare ne aggiungere elementi."));
    }// end of method

}// end of class
