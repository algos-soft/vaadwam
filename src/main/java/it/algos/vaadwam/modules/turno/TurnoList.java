package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.component.UI;
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
import it.algos.vaadflow.enumeration.EASearch;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.wrapper.AFiltro;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.enumeration.EAWamLogType;
import it.algos.vaadwam.migration.ImportService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.log.WamLogService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDate;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VIRGOLA;
import static it.algos.vaadwam.application.WamCost.TAG_TUR;
//import static it.algos.vaadwam.application.WamCost.TASK_TUR;


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
@Route(value = TAG_TUR, layout = WamLayout.class)
@Qualifier(TAG_TUR)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "turni", menuIcon = VaadinIcon.SITEMAP, roleTypeVisibility = EARoleType.developer)
public class TurnoList extends WamViewList {


    //    /**
    //     * Icona visibile nel menu (facoltativa)
    //     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
    //     * Se manca il MENU_NAME, di default usa il 'name' della view
    //     */
    //    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String IRON_ICON = "schedule";

    protected Button importAnnoButton;

    protected Button importDebugButton;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private ImportService migration;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private IscrizioneService iscrizioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private WamLogService wamLogger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private ServizioService servizioService;

    private AComboBox filtroServizi;


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
    public TurnoList(@Qualifier(TAG_TUR) IAService service) {
        super(service, Turno.class);
    }// end of Vaadin/@Route constructor


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<Turno>();
    }// end of method


    /**
     * Le preferenze standard <br>
     * Le preferenze specifiche della sottoclasse <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.searchType = EASearch.nonUsata;

        if (wamLogin.isDeveloper()) {
            super.isEntityModificabile = true;
        } else {
            super.isEntityModificabile = false;
        }// end of if/else cycle

        if (wamLogin.isDeveloper() || login.isAdmin()) {
            super.usaPopupFiltro = true;
        } else {
            super.usaPopupFiltro = false;
        }// end of if/else cycle
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaAlertLayout() {
        fixPreferenze();

        alertUser.add("Se sei arrivato qui come milite, vuol dire che c'è stato un errore. Sei pregato di segnalarlo ad Algos®");
        alertAdmin.add("Se sei arrivato qui come admin, vuol dire che c'è stato un errore. Sei pregato di segnalarlo ad Algos®");
        alertDev.add("Turni del tabellone visibili solo al developer. Si possono creare e modificare quelli esistenti.");
        alertDev.add("La cancellazione di un singolo turno è possibile. L'operazione è irreversibile e potenzialmente pericolosa");
        alertDev.add("L'import dei turni dal vecchio programma è stato completato per tutti gli anni e la funzionalità disabilitata.");
        alertDev.add("Selezione effettuabile solo per un anno alla volta, vista la quantità dei dati.");

        super.creaAlertLayout();
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

        //--Import non più usato/usabile
        //        String tagCroce = wamLogin.getCroce().code;
        //        if (wamLogin != null && wamLogin.isDeveloper() && wamLogin.getCroce() != null) {
        //            Button importAllButton = new Button("Import storico escluso anno 2020", new Icon(VaadinIcon.ARROW_DOWN));
        //            importAllButton.getElement().setAttribute("theme", "error");
        //            importAllButton.addClassName("view-toolbar__button");
        //            importAllButton.addClickListener(e -> importStorico(wamLogin.getCroce()));
        //            topPlaceholder.add(importAllButton);
        //        }// end of if cycle
    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaPopupFiltro() {
        if (login.isDeveloper() || login.isAdmin()) {
            super.creaPopupFiltro();
            filtroComboBox.setWidth("12em");
            filtroComboBox.setHeightFull();
            filtroComboBox.setPreventInvalidInput(true);
            filtroComboBox.setAllowCustomValue(false);
            filtroComboBox.setClearButtonVisible(false);
            filtroComboBox.setItems(EAFiltroTurno.values());
            filtroComboBox.setValue(EAFiltroTurno.corrente);
            filtroComboBox.addValueChangeListener(e -> {
                updateFiltri();
                updateGrid();
            });
            topPlaceholder.add(filtroComboBox);
        }// end of if cycle

        String tagCroce = wamLogin.getCroce().code;
        if (wamLogin != null && wamLogin.isAdminOrDev() && wamLogin.getCroce() != null) {
            filtroServizi = new AComboBox();
            filtroServizi.setPlaceholder("Servizi...");
            filtroServizi.setWidth("10em");
            filtroServizi.setHeightFull();
            filtroServizi.setPreventInvalidInput(true);
            filtroServizi.setAllowCustomValue(false);
            filtroServizi.setClearButtonVisible(true);
            filtroServizi.setItems(servizioService.findAll());
            filtroServizi.addValueChangeListener(e -> {
                updateFiltri();
                updateGrid();
            });
            topPlaceholder.add(filtroServizi);
        }// end of if cycle
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        Grid.Column colonnaNote = grid.addComponentColumn(turno -> {
            boolean esisteProblema = false;
            Icon icon;
            List<Iscrizione> iscrizioni = ((Turno) turno).iscrizioni;

            if (array.isValid(iscrizioni)) {
                for (Iscrizione iscr : iscrizioni) {
                    if (iscrizioneService.aggiungeAvviso((Turno) turno, iscr)) {
                        esisteProblema = true;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

            if (esisteProblema) {
                icon = new Icon(VaadinIcon.CLOSE);
                icon.setColor("red");
            } else {
                icon = new Icon(VaadinIcon.CHECK);
                icon.setColor("green");
            }// end of if/else cycle
            icon.setSize("1em");

            return icon;
        });//end of lambda expressions

        colonnaNote.setId("noteIscrizioni");
        colonnaNote.setHeader("?");
        colonnaNote.setFlexGrow(0);

        Grid.Column colonnaMiliti = grid.addComponentColumn(turno -> {
            Label label = new Label();
            String testo = "";
            String sep = VIRGOLA + SPAZIO;
            List<Iscrizione> iscrizioni = ((Turno) turno).iscrizioni;

            if (array.isValid(iscrizioni)) {
                for (Iscrizione iscr : iscrizioni) {
                    if (iscr.milite != null) {
                        testo += iscr.milite.getUsername();
                        testo += sep;
                    }// end of if cycle
                }// end of for cycle
                testo = text.levaCoda(testo, sep);
                label.setText(testo.trim());
            }// end of if cycle

            return label;
        });//end of lambda expressions

        colonnaMiliti.setId("idMiliti");
        colonnaMiliti.setHeader("Militi segnati nel turno");
        colonnaMiliti.setFlexGrow(1);
    }// end of method


    /**
     * Importa la collezione di turni di questa croce per tutti gli anni <br>
     */
    protected void importStorico(Croce croce) {
        migration.importTurniStorico(croce);
        wamLogger.log(EAWamLogType.importOld, "Import storico dei turni di tutti gli anni escluso il 2020");
        UI.getCurrent().getPage().reload();
    }// end of method


    public void updateFiltri() {
        super.updateFiltri();

        EAFiltroTurno filtro = null;
        int annoCorrente = LocalDate.now().getYear();
        int anno = annoCorrente;
        LocalDate inizio;
        LocalDate fine;
        Sort sort = new Sort(Sort.Direction.DESC, "giorno");

        if (filtroComboBox != null && filtroComboBox.getValue() == null) {
            filtroComboBox.setValue(EAFiltroTurno.corrente);
            return;
        }// end of if cycle

        filtro = filtroComboBox != null ? (EAFiltroTurno) filtroComboBox.getValue() : null;
        if (filtro != null) {
            anno = annoCorrente - filtro.delta;
        }// end of if cycle
        inizio = date.primoGennaio(anno);
        fine = date.trentunDicembre(anno);
        filtri.add(new AFiltro(Criteria.where("giorno").gte(inizio).lte(fine), sort));

        if (filtroServizi != null) {
            Servizio servizio;
            if (filtroServizi.getValue() != null) {
                servizio = (Servizio) filtroServizi.getValue();
                filtri.add(new AFiltro(Criteria.where("servizio").is(servizio), sort));
            }
        }

    }// end of method


    public void updateItems() {
        EAFiltroTurno filtro = null;
        int annoCorrente = LocalDate.now().getYear();

        if (filtroComboBox != null) {
            filtro = (EAFiltroTurno) filtroComboBox.getValue();

            if (filtro == null || filtro == EAFiltroTurno.corrente) {
                items = ((TurnoService) service).findAllAnnoCorrente();
            } else {
                items = ((TurnoService) service).findAllByYear(annoCorrente - filtro.delta);
            }// end of if/else cycle
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
        EAOperation eaOperation = wamLogin.isDeveloper() ? EAOperation.edit : EAOperation.showOnly;
        appContext.getBean(TurnoDialog.class, service, entityClazz).openWam(entityBean, eaOperation, this::save, this::delete);
    }// end of method

}// end of class