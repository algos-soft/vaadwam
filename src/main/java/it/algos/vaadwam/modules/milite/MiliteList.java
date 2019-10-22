package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.schedule.ATask;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
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
@Route(value = TAG_MIL, layout = WamLayout.class)
@Qualifier(TAG_MIL)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName =  "militi", menuIcon = VaadinIcon.GROUP, searchProperty = "code",roleTypeVisibility = EARoleType.user)
public class MiliteList extends WamViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String IRON_ICON = "account-box";

    /**
     * Service iniettato da Spring (@Scope = 'singleton').
     */
    @Autowired
    private FunzioneService funzioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     */
    @Autowired
    @Qualifier(TASK_MIL)
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
    public MiliteList(@Qualifier(TAG_MIL) IAService service) {
        super(service, Milite.class);
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

        super.isEntityModificabile = true;
        if (wamLogin.isDeveloper() || wamLogin.isAdmin()) {
            super.usaSearch = true;
            super.usaPopupFiltro = true;
        } else {
            super.usaSearch = false;
            super.usaPopupFiltro = false;
        }// end of if/else cycle

        super.grid = new PaginatedGrid<Milite>();
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

        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();

        alertPlacehorder.add(new Label("Militi dell'associazione."));
        if (isDeveloper) {
            alertPlacehorder.add(new Label("Come developer si possono importare i militi dal vecchio programma"));
            alertPlacehorder.add(getInfoImport(task, USA_DAEMON_MILITI, LAST_IMPORT_MILITI));
        } else {
            if (isAdmin) {
                alertPlacehorder.add(new Label("Seleziona i militi attivi o lo storico o admin/dipendente/infermiere."));
                alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come admin. Gli utenti normali vedono solo il loro nome."));
                alertPlacehorder.add(new Label("Come admin si possono aggiungere e modificare i militi. Gli utenti normali possono modificare solo il proprio nome"));
                alertPlacehorder.add(new Label("I militi non si possono cancellare. Se non effettuano più turni, disabilitare il flag 'attivo' per spostarli nello 'storico'"));
            } else {
                alertPlacehorder.add(new Label("Puoi modificare alcuni dati della tua scheda, senza cancellarla."));
                alertPlacehorder.add(new Label("Le funzioni vengono modificate solo da un admin."));
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaPopupFiltro() {
        if (login.isDeveloper() || login.isAdmin()) {
            super.creaPopupFiltro();
            filtroComboBox.setWidth("14em");

            filtroComboBox.setItems(EAFiltroMilite.values());
            filtroComboBox.setValue(EAFiltroMilite.attivi);
            filtroComboBox.addValueChangeListener(e -> {
                updateItems();
                updateView();
            });
        }// end of if cycle
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties <br>
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive il metodo getGridPropertyNamesList() nella sottoclasse specifica di xxxService <br>
     * Un eventuale modifica dell'ordine di presentazione delle colonne viene regolata nel metodo sovrascritto <br>
     */
    protected List<String> getGridPropertyNamesList() {
        String tag = "funzioni";
        List<String> gridPropertyNamesList = service != null ? service.getGridPropertyNamesList(context) : null;

        if (pref.isBool(USA_CHECK_FUNZIONI_MILITE) && gridPropertyNamesList.contains(tag)) {
            gridPropertyNamesList.remove(tag);
        }// end of if cycle

        return gridPropertyNamesList;
    }// end of method


    public void updateItems() {
        EAFiltroMilite filtro = null;

        if (filtroComboBox != null) {
            filtro = (EAFiltroMilite) filtroComboBox.getValue();
            if (filtro != null) {
                switch (filtro) {
                    case attivi:
                        items = ((MiliteService) service).findAllByEnabled();
                        break;
                    case admin:
                        items = ((MiliteService) service).findAllByAdmin();
                        break;
                    case dipendenti:
                        items = ((MiliteService) service).findAllByDipendente();
                        break;
                    case infermieri:
                        items = ((MiliteService) service).findAllByInfermiere();
                        break;
                    case storico:
                        items = ((MiliteService) service).findAll();
                        break;
                    case senzaFunzioni:
                        items = ((MiliteService) service).findAllSenzaFunzioni();
                        break;
                    case conNote:
                        items = ((MiliteService) service).findAllConNote();
                        break;
                    default:
                        log.warn("Switch - caso non definito");
                        break;
                } // end of switch statement
            } else {
                items = ((MiliteService) service).findAllByEnabled();
            }// end of if/else cycle
        } else {
            Milite milite = null;
            String idKey = "";
            items = new ArrayList<Milite>();
            Utente utente = login.getUtente();
            idKey = utente.id;
            if (text.isEmpty(idKey)) {
                idKey = utente.username;
            }// end of if cycle
            if (text.isValid(idKey)) {
                milite = ((MiliteService) service).findByKeyUnica(idKey);
            }// end of if cycle
            if (milite != null) {
                items.add(milite);
            }// end of if cycle
        }// end of if/else cycle

    }// end of method


    public void updateView() {
        if (items != null) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(items);
                headerGridHolder.setText(getGridHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        creaAlertLayout();
    }// end of method


//    protected Button createEditButton(AEntity entityBean) {
//        Button edit = new Button("", event -> dialog.open(entityBean, EAOperation.edit, context));
//        edit.setIcon(new Icon("lumo", "edit"));
//        edit.addClassName("review__edit");
//        edit.getElement().setAttribute("theme", "tertiary");
//        return edit;
//    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        List<Funzione> listaFunzioniCroce = null;

        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
            listaFunzioniCroce = funzioneService.findAll();

            if (array.isValid(listaFunzioniCroce)) {
                for (Funzione funz : listaFunzioniCroce) {
                    columnFunzione(grid, funz);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnFunzione(Grid gridPaginated, Funzione funzione) {
        Grid.Column colonna = gridPaginated.addComponentColumn(milite -> {
            List<Funzione> funzioniUtenteAbilitate = ((Milite) milite).getFunzioni();
            Icon icon;

            if (array.isValid(funzioniUtenteAbilitate) && contiene(funzioniUtenteAbilitate, funzione)) {
                icon = new Icon(VaadinIcon.CHECK);
                icon.setColor("green");
            } else {
                icon = new Icon(VaadinIcon.CLOSE);
                icon.setColor("red");
            }// end of if/else cycle

            icon.setSize("1em");
            return icon;
        });//end of lambda expressions

        colonna.setId(funzione.getCode());
        colonna.setWidth("5.5em");
        colonna.setFlexGrow(0);

        Label label = new Label(funzione.getCode());
        label.getStyle().set("font-size", "12px");
        colonna.setHeader(label);
    }// end of method


    /**
     * Controlla l'esistenza della funzione tra quelle abilitate per l'utente
     */
    private boolean contiene(List<Funzione> funzioniUtenteAbilitate, Funzione funzione) {
        boolean contiene = false;

        for (Funzione funz : funzioniUtenteAbilitate) {
            if (funz.getCode().equals(funzione.getCode())) {
                contiene = true;
                break;
            }// end of if cycle
        }// end of for cycle

        return contiene;
    }// end of method


//    /**
//     * Eventuali aggiustamenti finali al layout
//     * Regolazioni finali sulla grid e sulle colonne
//     * Sovrascritto
//     */
//    @Override
//    protected void fixGridLayout() {
//        super.fixLayout();
//        grid.setWidth("100em");
//        int keyPos = 1;
//
//        if (login.isDeveloper()) {
//            List<Grid.Column<AEntity>> colonne = grid.getColumns();
//            Grid.Column<AEntity> colonna = colonne != null ? colonne.get(keyPos) : null;
//            if (colonna != null) {
//                colonna.setWidth("12em");
//            }// end of if cycle
//        }// end of if cycle
//    }// end of method


//    /**
//     * Apre il dialog di detail
//     */
//    protected void addDetailDialog() {
//        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
//        if (usaBottoneEdit) {
//            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
//            Grid.Column colonna = gridPaginated.addColumn(renderer);
//            colonna.setWidth("6em");
//            colonna.setFlexGrow(0);
//        } else {
//            EAOperation operation = isEntityModificabile ? EAOperation.edit : EAOperation.showOnly;
//            grid.addSelectionListener(evento -> apreDialogo((SingleSelectionEvent) evento, operation));
//        }// end of if/else cycle
//    }// end of method


//    /**
//     * Eventuale header text
//     */
//    protected void fixGridHeader(String messaggio) {
//        try { // prova ad eseguire il codice
//            HeaderRow topRow = grid.prependHeaderRow();
//            Grid.Column[] matrix = array.getColumnArray(grid);
//            HeaderRow.HeaderCell informationCell = topRow.join(matrix);
//            Label testo = new Label(messaggio);
//            informationCell.setComponent(testo);
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//        }// fine del blocco try-catch
//    }// end of method


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
        appContext.getBean(MiliteDialog.class, service, entityClazz).openWam(entityBean, isEntityModificabile ? EAOperation.edit : EAOperation.showOnly, this::save, this::delete);
    }// end of method

}// end of class