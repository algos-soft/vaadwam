package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIEntity;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.*;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.migration.MigrationService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.USA_DEBUG;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Fri, 28-Jun-2019
 * Time: 20:31
 */
@UIScope
@Route(value = TAG_TAB_LIST, layout = WamLayout.class)
@Qualifier(TAG_TAB_LIST)
@Slf4j
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "tabellone", menuIcon = VaadinIcon.CALENDAR, roleTypeVisibility = EARoleType.user)
public class Tabellone extends AGridViewList {


    //--property
    public final static int GIORNI_STANDARD = 7;


    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected RigaService rigaService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TabelloneService tabelloneService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private CroceService croceService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    private WamService wamService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
//    @Autowired
//    private LegendaPolymer legenda;
    // @todo Non riesco ad usarlo come SCOPE_SINGLETON e quindi lo creo con appContext.getBean(LegendaPolymer.class)

    /**
     * Mantiene una property globale del tabellone <br>
     * Primo giorno visualizzato <br>
     * Può venire modificata (da un menu/bottone) <br>
     */
    private LocalDate startDay = LocalDate.now();

    /**
     * Mantiene una property globale del tabellone <br>
     * Numero di giorni visualizzati <br>
     * Può venire modificata (da un menu/bottone) <br>
     */
    private int numDays = GIORNI_STANDARD;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    private WamLogin wamLogin;

    /**
     * Devo mantenere un valore perché il comboBox viene ricostruito ad ogni modifica della Grid <br>
     */
    private EAPeriodo currentPeriodValue = EAPeriodo.oggi;

    ;

    @Autowired
    private MiliteService militeService;

    private AComboBox comboPeriodi;


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
    public Tabellone(@Qualifier(TAG_TAB) IAService service) {
        super(service, Riga.class);
    }// end of Vaadin/@Route constructor


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        super.setParameter(event, parameter);
        String isoValue;
        LocalDate endDay;

        if (array.isValid(parametersMap)) {
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNO_INIZIO)) {
                isoValue = parametersMap.get(KEY_MAP_GIORNO_INIZIO);
                startDay = date.localDateFromISO(isoValue);
            }// end of if cycle
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNO_FINE)) {
                isoValue = parametersMap.get(KEY_MAP_GIORNO_FINE);
                endDay = date.localDateFromISO(isoValue);
                numDays = date.differenza(endDay, startDay);
                numDays++; //--gli estremi sono compresi
            }// end of if cycle
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNI_DURATA)) {
                numDays = Integer.decode(parametersMap.get(KEY_MAP_GIORNI_DURATA));
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @Override
    protected void initView() {
        super.initView();

        //--Crea il wam-login della sessione
        wamLogin = wamService.fixWamLogin();
        ALogin login = vaadinService.getLogin();
        if (login != null) {
            wamLogin = (WamLogin) login;
//            String username=login.getUtente()!=null?login.getUtente().username:"";
//            Milite milite= militeService.findByKeyUnica(username);
//            wamLogin.setMilite(milite);
//            wamLogin.setCroce((Croce)login.getCompany());
//            wamLogin.setRoleType((EARoleType) login.getRoleType());
        }// end of if cycle

        //--se il login è obbligatorio e manca, la View non funziona
        if (vaadinService.mancaLoginSeObbligatorio()) {
            return;
        }// end of if cycle

        //--provvisorio. Serve per prendere i dati da un vecchio backup mysql di 'amb'
        if (pref.isBool(USA_DEBUG)) {//@todo levare
            startDay = MigrationService.GIORNO_INIZIALE_DEBUG;
            startDay = LocalDate.now();
        }// end of if cycle
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

        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        super.searchType = EASearch.nonUsata;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottomLayout = true;
    }// end of method


//    @Override
//    public void updateView() {
//        refreshGrid();
//    }// end of method


//    /**
//     * Crea una nuova grid in base allo stato attuale
//     * e la sostituisce nella vista
//     */
//    private void refreshGrid() {
//
//        if (grid != null) {
////            gridPlaceholder.remove(grid);
//            gridPlaceholder.removeAll();
//        }
//
//        if (gridPlaceholder != null) {
//            grid = buildGrid();
//            gridPlaceholder.add(grid);
////            gridPlaceholder.add(bottomPlacehorder);
//
//            FlexLayout layout = new FlexLayout();
//            layout.setFlexGrow(1, grid);
//            this.setFlexGrow(1, layout);
//        }// end of if cycle
//    }// end of method


    /**
     * Crea la grid <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto <br>
     */
    @Override
    protected Grid creaGrid() {
        grid = new Grid(Riga.class, false);

        grid.setHeightByRows(true);
        grid.addThemeNames("no-border");
        grid.addThemeNames("no-row-borders");
        grid.addThemeNames("row-stripes");
        grid.getElement().getStyle().set("background-color", EAColor.blue.getEsadecimale());
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // costruisce le colonne
        this.setColumns(grid);

        // costruisce le righe
        this.updateGrid();

        //--legenda dei colori
        if (pref.isBool(MOSTRA_LEGENDA_TABELLONE)) {
            bottomPlacehorder.add(appContext.getBean(LegendaPolymer.class));
        }// end of if cycle

        if (pref.isBool(USA_DEBUG)) {
            grid.getElement().getStyle().set("background-color", EAColor.blue.getEsadecimale());
        }// end of if cycle

        return grid;
    }// end of method


    /**
     * Crea e aggiunge le colonne
     */
    public void setColumns(Grid grid) {

        // costruisce la colonna dei servizi
        addColumnServizio(grid);

        // costruisce le colonne dei turni
        for (int i = 0; i < numDays; i++) {
            addColumnsTurni(grid, startDay.plusDays(i));
        }

    }// end of method


//    /**
//     * Crea un Popup di selezione della company <br>
//     * Creato solo se developer=true e usaCompany=true <br>
//     */
//    protected void creaCompanyFiltro() {
//        super.creaCompanyFiltro();
//
//        filtroCompany.setItems(croceService.findAll());
//        filtroCompany.addValueChangeListener(event -> {
//            Croce croce = (Croce) event.getValue();
//            wamLogin.setCroce(croce);
//            wamLogin.setCompany(croce);
//            getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
//        });
//    }// end of method


    /**
     * Sincronizza la company in uso. <br>
     * Chiamato dal listener di 'filtroCompany' <br>
     * <p>
     * Può essere sovrascritto, per modificare la gestione delle company <br>
     */
    protected void actionSincroCompany() {
        Croce croceSelezionata = null;

        if (filtroCompany != null) {
            croceSelezionata = (Croce) filtroCompany.getValue();
        }// end of if cycle
        wamLogin.setCroce(croceSelezionata);

        updateFiltri();
        updateGrid();
    }// end of method


//    protected void updateItems() {
//    }// end of method


    /**
     * Aggiorna gli items della Grid, utilizzando i filtri. <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Sviluppato nella sottoclasse AGridViewList, oppure APaginatedGridViewList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Override
    public void updateGrid() {
        Collection items = tabelloneService.getGridRigheList(startDay, numDays);

        if (items != null) {
            grid.setItems(items);
        }
    }// end of method


//    /**
//     * Crea e aggiunge le righe
//     */
//    public void setItems(Grid grid) {
//        Collection items = tabelloneService.getGridRigheList(startDay, numDays);
//
//        if (items != null) {
//            grid.setItems(items);
//        }
//    }// end of method


    /**
     * Crea la colonna (di tipo componentProvider) per visualizzare
     * i servizi previsti per questa croce
     * <p>
     */
    private void addColumnServizio(Grid grid) {

        ValueProvider<AEntity, ServizioCellPolymer> componentProvider = new ValueProvider() {

            String currentType = "";


            @Override
            public Object apply(Object obj) {
                ServizioCellPolymer servizioCell = null;
                Servizio servizio = null;
                boolean lastInType;

                servizio = ((Riga) obj).getServizio();

                if (servizio != null) {
                    lastInType = (!servizio.getCode().equals(currentType));
                    lastInType = lastInType && !servizio.orarioDefinito;
                    servizioCell = appContext.getBean(ServizioCellPolymer.class, servizio, lastInType);
                    currentType = servizio.getCode();
                }

                return servizioCell != null ? servizioCell : new Label("Manca");
            }
        };

        Grid.Column column = grid.addComponentColumn(componentProvider);

        column.setHeader(periodoHeader());
        column.setFlexGrow(0);
        column.setWidth("12em");
        column.setSortable(false);
        column.setResizable(false);
        column.setFrozen(true);

    }


    /**
     * Crea e aggiunge la colonna dei turni per un dato giorno
     */
    private void addColumnsTurni(Grid<AEntity> grid, LocalDate day) {
        Object alfa = day;
        ValueProvider<AEntity, TurnoCellPolymer> componentProvider = new ValueProvider() {

            @Override
            public Object apply(Object obj) {
                return appContext.getBean(TurnoCellPolymer.class, (Riga) obj, day);
            }
        };//end of lambda expressions and anonymous inner class

        Grid.Column column = grid.addComponentColumn(componentProvider);

        column.setHeader(date.get(day, EATime.weekShortMese));
        column.setFlexGrow(0);
        column.setWidth("45mm");
        column.setSortable(false);
        column.setResizable(false);

    }// end of method


    /**
     * Crea l'header della colonna servizi <br>
     * Contiene un listener per modificare i giorni visualizzati nel tabellonesuperato <br>
     */
    private Component periodoHeader() {
        comboPeriodi = new AComboBox();
        comboPeriodi.setWidth("12em");
        comboPeriodi.setClearButtonVisible(false);
        comboPeriodi.setItems(EAPeriodo.values());
        comboPeriodi.setValue(currentPeriodValue);
        comboPeriodi.addValueChangeListener(event -> sincroPeriodi(event));

        return comboPeriodi;
    }// end of method


    /**
     * Modifica i giorni visualizzati nel tabellonesuperato <br>
     */
    private void sincroPeriodi(HasValue.ValueChangeEvent event) {
        EAPeriodo oldValue = (EAPeriodo) event.getOldValue();//@todo per ora non serve, ma non si sa mai...
        EAPeriodo newValue = (EAPeriodo) event.getValue();

        if (newValue != null) {

            switch (newValue) {
                case vuoto:
                    break;
                case oggi:
                    startDay = LocalDate.now();
                    currentPeriodValue = EAPeriodo.oggi;
                    break;
                case lunedi:
                    startDay = date.getFirstLunedì(LocalDate.now());
                    currentPeriodValue = EAPeriodo.lunedi;
                    break;
                case giornoPrecedente:
                    startDay = startDay.minusDays(1);
                    currentPeriodValue = EAPeriodo.vuoto;
                    break;
                case giornoSuccessivo:
                    startDay = startDay.plusDays(1);
                    currentPeriodValue = EAPeriodo.vuoto;
                    break;
                case settimanaPrecedente:
                    startDay = startDay.minusDays(GIORNI_STANDARD);
                    currentPeriodValue = EAPeriodo.vuoto;
                    break;
                case settimanaSuccessiva:
                    startDay = startDay.plusDays(GIORNI_STANDARD);
                    currentPeriodValue = EAPeriodo.vuoto;
                    break;
                case selezione:
                    apreSelezione();
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

//        currentPeriodValue = EAPeriodo.vuoto;
//        comboPeriodi.setValue(currentPeriodValue);
        numDays = GIORNI_STANDARD;

        routeToTabellone(startDay, numDays);
    }// end of method


    private void apreSelezione() {
        Map<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
        mappa.put(KEY_MAP_GIORNO_FINE, date.getISO(startDay.plusDays(numDays - 1)));
        final QueryParameters query = QueryParameters.simple(mappa);

        getUI().ifPresent(ui -> ui.navigate(TAG_SELEZIONE, query));
    }// end of method


    /**
     * Ritorno al tabellone coi parametri selezionati
     */
    private void routeToTabellone(LocalDate startDay, int numDays) {
        Map<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
        mappa.put(KEY_MAP_GIORNI_DURATA, numDays + "");
        final QueryParameters query = QueryParameters.simple(mappa);

        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST, query));
    }// end of method


    /**
     * Ritorno al tabellone coi parametri selezionati
     */
    private void routeToTabellone(LocalDate startDay, LocalDate endDay) {
        Map<String, String> mappa = new LinkedHashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
        mappa.put(KEY_MAP_GIORNO_FINE, date.getISO(endDay));
        final QueryParameters query = QueryParameters.simple(mappa);

        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST, query));
    }// end of method


//    /**
//     * Costruisce un (eventuale) layout con bottoni aggiuntivi
//     * Facoltativo (assente di default)
//     * Può essere sovrascritto, per aggiungere informazioni
//     * Invocare PRIMA il metodo della superclasse
//     */
//    @Override
//    protected void creaGridBottomLayout() {
//        super.creaGridBottomLayout();
//
//        //--legenda dei colori
//        if (pref.isBool(MOSTRA_LEGENDA_TABELLONE)) {
//            bottomPlacehorder.add(appContext.getBean(LegendaPolymer.class));
//        }// end of if cycle
//
//    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     *
     * @param entityBean
     * @param operation
     */
    @Override
    protected void save(AEntity entityBean, EAOperation operation) {
        entityBean = service.beforeSave(entityBean, operation);
        switch (operation) {
            case addNew:
                if (service.isEsisteEntityKeyUnica(entityBean)) {
                    Notification.show(entityBean + " non è stata registrata, perché esisteva già con lo stesso code ", 3000, Notification.Position.BOTTOM_START);
                } else {
                    service.save(entityBean);
                    updateGrid();
                    Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                }// end of if/else cycle
                break;
            case edit:
            case editDaLink:
                service.save(entityBean);
                Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                this.grid.getDataProvider().refreshAll();
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement
    }// end of method


//    @Override
//    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
////        super.beforeEnter(beforeEnterEvent);
//    }// end of method

}// end of class
