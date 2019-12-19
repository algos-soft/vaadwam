package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.schedule.ATask;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.22.05 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
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
@Route(value = TAG_CRO, layout = WamLayout.class)
@Qualifier(TAG_CRO)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "croce", menuIcon = VaadinIcon.HOSPITAL, roleTypeVisibility = EARoleType.admin)
public class CroceList extends WamViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String IRON_ICON = "camera-enhance";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     */
    @Autowired
    @Qualifier(TASK_CRO)
    private ATask task;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public CroceList(@Qualifier(TAG_CRO) IAService service) {
        super(service, Croce.class);
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze standard <br>
     * Le preferenze specifiche della sottoclasse <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaButtonDelete = true;
        super.usaButtonReset = false;
        super.isEntityUsaDatiDemo = false;

        if (wamLogin.isDeveloper()) {
            super.usaButtonNew = true;
        } else {
            super.usaButtonNew = false;
        }// end of if/else cycle

    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        alertPlacehorder.removeAll();
        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();

        if (isDeveloper) {
            alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come developer. Gli admin vedono SOLO la loro Croce. Gli utenti normali non vedono nulla."));
            alertPlacehorder.add(new Label("Si possono importare le Croci dal vecchio programma"));
            alertPlacehorder.add(getInfoImport(task, USA_DAEMON_CROCI, LAST_IMPORT_CROCI));
        } else {
            if (isAdmin) {
                alertPlacehorder.add(new Label("Visibile la Croce di appartenenza solo perché sei collegato come admin. Gli utenti normali non la vedono."));
                alertPlacehorder.add(new Label("Puoi modificare le descrizioni ed i nomi delle persone. Non il code."));
            }// end of if cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid
     * - con o senza campo edit search, regolato da preferenza o da parametro
     * - con o senza bottone New, regolato da preferenza o da parametro
     * - con eventuali altri bottoni specifici
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaTopLayout() {
        super.creaTopLayout();
        ALogin login = context.getLogin();

        if (importButton != null) {
            topPlaceholder.remove(importButton);
        }// end of if cycle

        if (login.isDeveloper()) {
            importButton = new Button("Import all", new Icon(VaadinIcon.CLOSE_CIRCLE));
            importButton.getElement().setAttribute("theme", "error");
            importButton.addClassName("view-toolbar__button");
            importButton.addClickListener(e -> {
//                service.importa();
                // @todo RIMETTERE
//                updateView();
            });//end of lambda expressions and anonymous inner class
            topPlaceholder.add(importButton);
        }// end of if cycle
    }// end of method


    /**
     * Eventuale header text
     */
    protected void fixGridHeader() {
        if (login.isDeveloper()) {
            super.fixGridHeader();
        }// end of if cycle
    }// end of method


//    protected Button createEditButton(AEntity entityBean) {
//        final EAOperation operation;
//
//        if (context.getLogin().isDeveloper()) {
//            operation = EAOperation.edit;
//        } else {
//            operation = EAOperation.editNoDelete;
//        }// end of if/else cycle
//
//        Button edit = new Button("", event -> dialog.open(entityBean, operation, context));
//        edit.setIcon(new Icon("lumo", "edit"));
//        edit.addClassName("review__edit");
//        edit.getElement().setAttribute("theme", "tertiary");
//        return edit;
//    }// end of method


    public void updateItems() {
        if (wamLogin.isDeveloper()) {
            super.updateGrid();
        } else {
            if (login.isAdmin()) {
                items = Arrays.asList(wamLogin.getCroce());
            } else {
                items = null;
            }// end of if/else cycle
        }// end of if/else cycle
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
        appContext.getBean(CroceDialog.class, service, entityClazz).open(entityBean, isEntityModificabile ? EAOperation.edit : EAOperation.showOnly, this::save, this::delete);
    }// end of method

}// end of class