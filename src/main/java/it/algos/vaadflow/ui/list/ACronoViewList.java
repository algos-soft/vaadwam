package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.html.Label;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadflow.ui.list.ALayoutViewList;
import it.algos.vaadflow.ui.list.AViewList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 14-ott-2018
 * Time: 16:28
 */
public abstract class ACronoViewList extends AGridViewList {


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     * @param entityClazz modello-dati specifico di questo modulo
     */
    public ACronoViewList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaPopupFiltro = true;
        super.usaBottoneDeleteAll = true;
        super.usaBottoneReset = true;
        super.isEntityDeveloper = true;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = true;
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
        alertPlacehorder.add(new Label("Serve per costruire liste cronologiche. La lista viene creata in automatico allo startup del programma."));
    }// end of method

}// end of class
