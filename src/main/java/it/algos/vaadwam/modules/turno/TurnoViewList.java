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
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwam.migration.MigrationService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.schedule.ATask;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDate;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VIRGOLA;
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
@Route(value = TAG_TUR, layout = MainLayout.class)
@Qualifier(TAG_TUR)
@AIView(menuName = "turni", roleTypeVisibility = EARoleType.admin)
@Slf4j
@AIScript(sovrascrivibile = false)
public class TurnoViewList extends WamViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String IRON_ICON = "schedule";

    protected Button importAnnoButton;

    protected Button importDebugButton;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private MigrationService migration;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     */
    @Autowired
    @Qualifier(TASK_TUR)
    private ATask task;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public TurnoViewList(@Qualifier(TAG_TUR) IAPresenter presenter, @Qualifier(TAG_TUR) IADialog dialog) {
        super(presenter, dialog);
        ((TurnoViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor


    /**
     * Le preferenze standard <br>
     * Le preferenze specifiche della sottoclasse <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (wamLogin.isDeveloper() || login.isAdmin()) {
            super.usaSearch = true;
            super.usaPopupFiltro = true;
        } else {
            super.usaSearch = false;
            super.usaPopupFiltro = false;
        }// end of if/else cycle

        super.grid = new PaginatedGrid<Turno>();
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        boolean isDeveloper = login.isDeveloper();

        if (isDeveloper) {
            alertPlacehorder.add(new Label("Come developer si possono importare i Turni dal vecchio programma"));
            alertPlacehorder.add(getInfoImport(task, USA_DAEMON_TURNI, LAST_IMPORT_TURNI));
        }// end of if cycle
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

        String tagCroce = wamLogin.getCroce().code;
        if (wamLogin.isDeveloper()) {
            importButton.setText("Import all " + tagCroce);

            importAnnoButton = new Button("Import 2019 " + tagCroce, new Icon(VaadinIcon.ARROW_DOWN));
            importAnnoButton.getElement().setAttribute("theme", "error");
            importAnnoButton.addClassName("view-toolbar__button");
            importAnnoButton.addClickListener(e -> importAnno());
            topPlaceholder.add(importAnnoButton);

            importDebugButton = new Button("Import debug " + tagCroce, new Icon(VaadinIcon.ARROW_DOWN));
            importDebugButton.getElement().setAttribute("theme", "error");
            importDebugButton.addClassName("view-toolbar__button");
            importDebugButton.addClickListener(e -> importDebug());
            topPlaceholder.add(importDebugButton);
        }// end of if cycle

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

            filtroComboBox.setItems(EAFiltroTurno.values());
            filtroComboBox.setValue(EAFiltroTurno.corrente);
            filtroComboBox.addValueChangeListener(e -> {
                updateItems();
                updateView();
            });
        }// end of if cycle
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        Grid.Column colonna = grid.addComponentColumn(turno -> {
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

        colonna.setId("idMiliti");
        colonna.setHeader("militi segnati nel turno");
        colonna.setFlexGrow(1);
    }// end of method


    /**
     * Importa la collezione di turni di questa croce solo per l'anno in corso <br>
     */
    protected void importAnno() {
        if (migration.importTurniAnno(wamLogin.getCroce(), date.getAnnoCorrente())) {
            fixInfoImport();
        }// end of if cycle
        UI.getCurrent().getPage().reload();
    }// end of method


    /**
     * Importa la collezione di turni di questa croce solo per l'anno in corso <br>
     */
    protected void importDebug() {
        if (migration.importTurniDebug(wamLogin.getCroce())) {
            fixInfoImport();
        }// end of if cycle
        UI.getCurrent().getPage().reload();
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

}// end of class