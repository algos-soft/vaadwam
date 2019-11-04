package it.algos.vaadflow.modules.anno;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.secolo.Secolo;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.ui.list.AGridViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.ArrayList;
import java.util.Arrays;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 18.19.24 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * <p>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), oppure <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe, perché SpringBoot le inietta DOPO init() <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@UIScope
@Route(value = TAG_ANN, layout = MainLayout14.class)
@Qualifier(TAG_ANN)
@Slf4j
@Secured("developer")
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = true, menuName = "anni", menuIcon = VaadinIcon.CALENDAR, searchProperty = "titolo", roleTypeVisibility = EARoleType.developer)
public class AnnoList extends AGridViewList {


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected SecoloService secoloService;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa alla superclasse il service iniettato qui da Vaadin/@Route <br>
     * Passa alla superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public AnnoList(@Qualifier(TAG_ANN) IAService service) {
        super(service, Anno.class);
    }// end of Vaadin/@Route constructor


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<Anno>();
    }// end of method

    /**
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.limit = 25;
        super.usaPopupFiltro = true;
        super.usaBottoneDeleteAll = true;
        super.usaBottoneReset = true;
        super.isEntityDeveloper = true;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        alertPlacehorder.add(new Label("Sono considerati gli anni da 1.000 a.c. a 2030 d.c. Non si possono cancellare ne aggiungere elementi."));
    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaPopupFiltro() {
        super.creaPopupFiltro();

        filtroComboBox.setWidth("12em");
        filtroComboBox.setPlaceholder("Secolo ...");
        filtroComboBox.setItems(secoloService.findAll());
    }// end of method



    /**
     * Crea la lista dei filtri della Grid alla prima visualizzazione della view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse AGridViewList <br>
     * Chiamato SOLO alla creazione della view. Successive modifiche ai filtri sono gestite in updateFiltri() <br>
     * Può essere sovrascritto, per modificare la selezione dei filtri <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaFiltri() {
        super.creaFiltri();
        Secolo secolo = null;

        if (filtroComboBox != null && filtroComboBox.getValue() != null) {
            secolo = (Secolo) filtroComboBox.getValue();
            filtri.add(Criteria.where("secolo").is(secolo));
        }// end of if cycle
    }// end of method

    /**
     * Creazione ed apertura del dialogo per una nuova entity oppure per una esistente <br>
     * Il dialogo è PROTOTYPE e viene creato esclusivamente da appContext.getBean(... <br>
     * Nella creazione vengono regolati il service e la entityClazz di riferimento <br>
     * Contestualmente alla creazione, il dialogo viene aperto con l'item corrente (ricevuto come parametro) <br>
     * Se entityBean è null, nella superclasse AViewDialog viene modificato il flag a EAOperation.addNew <br>
     * Si passano al dialogo anche i metodi locali (di questa classe AViewList) <br>
     * come ritorno dalle azioni save e delete al click dei rispettivi bottoni <br>
     * Il metodo DEVE essere sovrascritto <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    @Override
    protected void openDialog(AEntity entityBean) {
        appContext.getBean(AnnoDialog.class, service, entityClazz).open(entityBean, isEntityModificabile ? EAOperation.edit : EAOperation.showOnly, this::save, this::delete);
    }// end of method

}// end of class