package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;

import static it.algos.vaadflow.application.FlowCost.USA_DEBUG;
import static it.algos.vaadwam.application.WamCost.TAG_CRO;

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
@PageTitle(WamCost.BROWSER_TAB_TITLE)
public class CroceList extends WamViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String IRON_ICON = "camera-enhance";

    public static String NOMI = "Puoi modificare le descrizioni ed i nomi delle persone. Non il code.";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public CroceData croceData;

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

        super.usaButtonDelete = false;
        super.usaButtonReset = true;
        super.isEntityUsaDatiDemo = false;

        if (wamLogin.isDeveloper()) {
            super.usaButtonNew = true;
        }
        else {
            super.usaButtonNew = false;
        }// end of if/else cycle

        super.soloVisioneUser = false;
        super.soloVisioneAdmin = false;
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        fixPreferenze();

        alertAdmin.add("Visibile la Croce di appartenenza solo perché sei collegato come admin. Gli utenti normali non la vedono.");
        alertAdmin.add(NOMI);

        alertDev.add("Lista visibile solo perché sei collegato come developer. Gli admin vedono SOLO la loro Croce. Gli utenti normali non vedono nulla.");
        alertDev.add("La lista delle croci prevede un bottone 'Import' da usare SOLO sul computer di casa ed in modalità debug (per sicurezza).");
        alertDev.add("Nella fase di import la singola croce operativa viene creata SOLO se mancante. Per modificarla, occorre prima cancellarla.");
        alertDev.add("La lista delle croci prevede un bottone 'Reset' per ricreare la sola croce Demo (che viene comunque ricreata ad ogni riavvio).");

        super.creaAlertLayout();
    }// end of method

    /**
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, presente di default <br>
     * - con o senza un bottone per cancellare tutta la collezione
     * - con o senza un bottone di reset per ripristinare (se previsto in automatico) la collezione
     * - con o senza gruppo di ricerca:
     * -    campo EditSearch predisposto su un unica property, oppure (in alternativa)
     * -    bottone per aprire un DialogSearch con diverse property selezionabili
     * -    bottone per annullare la ricerca e riselezionare tutta la collezione
     * - con eventuale Popup di selezione, filtro e ordinamento
     * - con o senza bottone New, con testo regolato da preferenza o da parametro <br>
     * - con eventuali altri bottoni specifici <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaTopLayout() {
        super.creaTopLayout();

        //--Elaborazione utilizzabile SOLO dal developer
        //--Elaborazione utilizzabile SOLO in modalità debug (così me ne accorgo)
        //--Elaborazione utilizzabile SOLO per la croce DEMO
        if (wamLogin != null && wamLogin.isDeveloper()) {
            if (usaImportButton && pref.isBool(USA_DEBUG)) {
                if (wamLogin.getCroce() != null && wamLogin.getCroce().code.equals(CroceService.DEMO)) {
                    Button elaboraButton = new Button("CSV demo", new Icon(VaadinIcon.REFRESH));
                    elaboraButton.getElement().setAttribute("theme", "error");
                    elaboraButton.addClassName("view-toolbar__button");
                    elaboraButton.addClickListener(e -> elabora());
                    topPlaceholder.add(elaboraButton);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle
    }

    /**
     * Opens the confirmation dialog before reset all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected void openConfirmReset() {
        reset();
    }// end of method

    //    /**
    //     * Placeholder (eventuale, presente di default) SOPRA la Grid
    //     * - con o senza campo edit search, regolato da preferenza o da parametro
    //     * - con o senza bottone New, regolato da preferenza o da parametro
    //     * - con eventuali altri bottoni specifici
    //     * Può essere sovrascritto, per aggiungere informazioni
    //     * Invocare PRIMA il metodo della superclasse
    //     */
    //    @Override
    //    protected void creaTopLayout() {
    //        super.creaTopLayout();
    //        ALogin login = context.getLogin();
    //
    //        if (importButton != null) {
    //            topPlaceholder.remove(importButton);
    //        }// end of if cycle
    //
    //        if (login.isDeveloper()) {
    //            importButton = new Button("Import", new Icon(VaadinIcon.CLOSE_CIRCLE));
    //            importButton.getElement().setAttribute("theme", "error");
    //            importButton.addClassName("view-toolbar__button");
    //            importButton.addClickListener(e -> {
    //                //                service.importa();
    //                // @todo RIMETTERE
    //                //                updateView();
    //            });//end of lambda expressions and anonymous inner class
    //            topPlaceholder.add(importButton);
    //        }// end of if cycle
    //    }// end of method
    //
    //
    //    /**
    //     * Eventuale header text
    //     */
    //    protected void fixGridHeader() {
    //        //        if (login.isDeveloper()) {
    //        super.fixGridHeader();
    //        //        }// end of if cycle
    //    }// end of method

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
        }
        else {
            if (login.isAdmin()) {
                items = Arrays.asList(wamLogin.getCroce());
            }
            else {
                items = null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

    public void elabora() {
        croceData.elabora();
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
        EAOperation operation = wamLogin.isAdmin() ? EAOperation.editNoDelete : EAOperation.edit;
        appContext.getBean(CroceDialog.class, service, entityClazz).open(entityBean, operation, this::save, this::delete);
    }// end of method

}// end of class