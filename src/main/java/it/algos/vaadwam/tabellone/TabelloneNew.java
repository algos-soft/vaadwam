package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ShadowRoot;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import elemental.json.JsonValue;
import it.algos.vaadflow.annotation.AIEntity;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.*;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadwam.WamLayout;
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
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Fri, 28-Jun-2019
 * Time: 20:31
 * <p>
 * La griglia è composta di oggetti 'Riga' <br>
 * La riga è composta di oggetti 'TurnoCellPolymer' <br>
 */
//@UIScope

@JavaScript("frontend://js/js-comm.js")
@JavaScript("frontend://js/tabellone.js")

//@Route(value = TAG_TAB_LIST, layout = WamLayout.class)
@Route(value = TAG_TAB_LIST+"new", layout = AppLayout.class)
//@Route(value = TAG_TAB_LIST+"new", layout = TabelloneAppLayout.class)
//@Route(value = TAG_TAB_LIST+"new")
//@ParentLayout(AppLayout.class)

@Tag("tabellone-polymer")
@HtmlImport("src/views/tabellone/tabellone-polymer.html")

//@Route(value = TAG_TAB_LIST)
//@PreserveOnRefresh

//@Qualifier(TAG_TAB_LIST+"new")
@Slf4j
//@AIEntity(company = EACompanyRequired.obbligatoria)
//@AIScript(sovrascrivibile = false)
//@AIView(vaadflow = false, menuName = "tabellone", menuIcon = VaadinIcon.CALENDAR, roleTypeVisibility = EARoleType.user)
//public class Tabellone extends AGridViewList {

public class TabelloneNew extends PolymerTemplate<TabelloneModel> implements HasUrlParameter<String>, BeforeEnterObserver, BeforeLeaveObserver, AfterNavigationObserver {


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

    @Autowired
    private MiliteService militeService;

    private AComboBox comboPeriodi;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ADateService date = ADateService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();


    /**
     * Mappa chiave-valore di un singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, String> parametersMap = null;

    @Id("tabellonegrid")
    private Grid grid;

//    @Id("host")
//    private Div host;

    //private Grid grid;


    private boolean inited;

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
    public TabelloneNew(@Qualifier(TAG_TAB) IAService service) {

        // registra il riferimento al server Java nel client JS
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

        //setSizeUndefined();

//        setId("tabelloneDIV");
//        getStyle().set("width","500px");
//        getStyle().set("height","400px");
//        getStyle().set("background-color","green");

        //grid = new Grid(Riga.class, false);
        //host.add(grid);
        //grid = getElement("tabellone-grid");
        regolaGrid();
        //grid.setId("tabelloneGrid");
        //this.add(this.grid);

        //div = new Div();
//        Label label = new Label("Ciao");
//        this.add(label);

        //super(service, Riga.class);

        //UI.getCurrent().getPage().addJavaScript("test.js");

        //getElement().executeJs("scrollContent()");
        //UI.getCurrent().getPage().executeJs("scrollContent()");

        //getElement().executeJs("greet($0, $1)", "client", getElement());


    }

    /**
     * Invoked from the JS client when is safe to invoke JS functions operating on the DOM.
     * (The DOM is ready and the page is completely loaded).
     * For this command to work, remember to register the server in the constructor:
     * UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());
     */
    @ClientCallable
    public void pageReady(){
        //PendingJavaScriptResult jsResult=UI.getCurrent().getPage().executeJs(js);
        //SerializableConsumer<JsonValue> jsonValueSerializableConsumer = ;
        //jsResult.then();
        UI.getCurrent().getPage().executeJs("setupScrollListener()");

        // restore the previous scroll position
        VaadinSession session = VaadinSession.getCurrent();
        Object objX=session.getAttribute("tabelloneScrollX");
        Object objY=session.getAttribute("tabelloneScrollY");
        if (objX!=null && objY!=null){
            int x = (Integer)objX;
            int y = (Integer)objY;
            UI.getCurrent().getPage().executeJs("scrollTo($0,$1)", x, y);
        }

        int a =87;
        int b=a;
    }

    @ClientCallable
    public void tabScrolled(int x, int y){
        log.info( "container scrolled: x="+x+", y="+y);
        // Store the current scroll position in the current Context
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("tabelloneScrollX",x);
        session.setAttribute("tabelloneScrollY",y);
    }



    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        //super.setParameter(event, parameter);
        String isoValue;
        LocalDate endDay;

        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> params = queryParameters.getParameters();


        if (array.isValid(params)) {
            this.parametersMap = array.semplificaMappa(params);
        }// end of if cycle

        if (array.isValid(parametersMap)) {
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNO_INIZIO)) {
                isoValue = parametersMap.get(KEY_MAP_GIORNO_INIZIO);
                startDay = date.localDateFromISO(isoValue);
            }
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNO_FINE)) {
                isoValue = parametersMap.get(KEY_MAP_GIORNO_FINE);
                endDay = date.localDateFromISO(isoValue);
                numDays = date.differenza(endDay, startDay);
                numDays++; //--gli estremi sono compresi
            }
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNI_DURATA)) {
                numDays = Integer.decode(parametersMap.get(KEY_MAP_GIORNI_DURATA));
            }
        }
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

//        // create the components if not already existing
//        if (this.grid==null){
//            this.grid = creaGrid();
//            this.removeAll();
//            this.add(this.grid);
//        }

        loadDataInGrid();


        //UI.getCurrent().getPage().executeJs("alert('Before enter')");



    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
    }


    //    /**
//     * Creazione dei contenuti
//     * <p>
//     * Invocata da @Route
//     */
//    @Override
//    protected void initView() {
//
//        if (!inited){
//            // crea tutta la struttura standard della pagina compresa la grid
//            //super.initView();
//
//
//
////            Label label = new Label("Ciao");
////            this.add(label);
//
//        }
//
//        this.grid=creaGrid();
//        this.removeAll();
//        this.add(grid);
//        inited=true;
//
//
//
////        // crea il wam-login della sessione
////        wamLogin = wamService.fixWamLogin();
////        ALogin login = vaadinService.getLogin();
////        if (login != null) {
////            wamLogin = (WamLogin) login;
////        }
////
////        // se il login è obbligatorio e manca, la View non funziona
////        if (vaadinService.mancaLoginSeObbligatorio()) {
////            return;
////        }
//
//        // carica i dati dal db e crea le righe della griglia
//        loadDataInGrid();
//
//        //--provvisorio. Serve per prendere i dati da un vecchio backup mysql di 'amb'
//        if (pref.isBool(USA_DEBUG)) {//@todo levare
//            startDay = MigrationService.GIORNO_INIZIALE_DEBUG;
//            startDay = LocalDate.now();
//        }
//
//    }


//    /**
//     * Le preferenze standard <br>
//     * Le preferenze specifiche della sottoclasse <br>
//     * Può essere sovrascritto, per modificare le preferenze standard <br>
//     * Invocare PRIMA il metodo della superclasse <br>
//     */
//    @Override
//    protected void fixPreferenze() {
//        super.fixPreferenze();
//
//        this.setMargin(false);
//        this.setSpacing(false);
//        this.setPadding(false);
//
//        super.searchType = EASearch.nonUsata;
//        super.usaButtonNew = false;
//        super.usaBottoneEdit = false;
//        super.usaBottomLayout = true;
//    }// end of method


    /**
     * Crea la grid <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto <br>
     */
    private void regolaGrid() {

        grid.setHeightByRows(true);
        grid.addThemeNames("no-border");
        grid.addThemeNames("no-row-borders");
        grid.addThemeNames("row-stripes");
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // aggiunge le colonne
        this.addColumns();

        // costruisce le righe
        // this.updateGrid();
        //this.loadDataInGrid();

//        if (pref.isBool(USA_DEBUG)) {
//            grid.getElement().getStyle().set("background-color", EAColor.blue.getEsadecimale());
//        }// end of if cycle

        //return grid;
    }


    /**
     * Crea e aggiunge le colonne
     */
    private void addColumns() {

//        long inizio = System.currentTimeMillis();

        // costruisce la colonna dei servizi
        addColumnServizio();

        // costruisce le colonne dei turni
        for (int i = 0; i < numDays; i++) {
            addColumnsTurni(startDay.plusDays(i));
        }

//        log.info("Costruzione tabellone in " + (System.currentTimeMillis()-inizio));

    }


//    /**
//     * Sincronizza la company in uso. <br>
//     * Chiamato dal listener di 'filtroCompany' <br>
//     * <p>
//     * Può essere sovrascritto, per modificare la gestione delle company <br>
//     */
//    protected void actionSincroCompany() {
//        Croce croceSelezionata = null;
//
//        if (filtroCompany != null) {
//            croceSelezionata = (Croce) filtroCompany.getValue();
//        }// end of if cycle
//        wamLogin.setCroce(croceSelezionata);
//
//        updateFiltri();
//        updateGrid();
//    }// end of method


//    /**
//     * Aggiorna gli items della Grid, utilizzando i filtri. <br>
//     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
//     * <p>
//     * Sviluppato nella sottoclasse AGridViewList, oppure APaginatedGridViewList <br>
//     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
//     */
//    @Override
//    public void updateGrid() {
////        Collection items = tabelloneService.getGridRigheList(startDay, numDays);
////
////        if (items != null) {
////            grid.setItems(items);
////        }// end of if cycle
//    }// end of method


    /**
     * Carica gli items nella Grid, utilizzando i filtri correnti
     */
    private void loadDataInGrid() {

//        Optional<UI> optionalUI = getUI();
//        if (optionalUI.isPresent()){
//            UI ui = optionalUI.get();
//            int a=87;
//            int b=a;
//        }

//        Optional<Component> optComponent = getParent();
//        if (optComponent.isPresent()){
//            Component comp = optComponent.get();
//            int a=87;
//            int b=a;
//        }

        Collection items = tabelloneService.getGridRigheList(startDay, numDays);
        grid.setItems(items);

    }


    /**
     * Crea la colonna (di tipo componentProvider) per visualizzare
     * i servizi previsti per questa croce
     * <p>
     */
    private void addColumnServizio() {

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
                    lastInType = lastInType && servizio.ripetibile;
                    lastInType = false; //@todo PROVVISORIO
                    servizioCell = appContext.getBean(ServizioCellPolymer.class, servizio, lastInType);
                    currentType = servizio.getCode();
                }// end of if cycle

                return servizioCell != null ? servizioCell : new Label("Manca");
            }
        };

        Grid.Column column = grid.addComponentColumn(componentProvider);

        // provare a sostituire questo componente con un Menu
        Component component = periodoHeader();

        column.setHeader(component);
        column.setFlexGrow(0);
        column.setWidth("7em");
        column.setSortable(false);
        column.setResizable(false);
        column.setFrozen(true);

    }


    /**
     * Crea e aggiunge la colonna dei turni per un dato giorno
     */
    private void addColumnsTurni(LocalDate day) {
        Object alfa = day;
        ValueProvider<AEntity, TurnoCellPolymer> componentProvider = new ValueProvider() {

            @Override
            public Object apply(Object obj) {
                return appContext.getBean(TurnoCellPolymer.class, (Riga) obj, alfa);
            }
        };

        Grid.Column column = this.grid.addComponentColumn(componentProvider);

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
        MenuBar menuBar = new MenuBar();
        menuBar.setWidth("20em");

        MenuItem periodoMenu = menuBar.addItem("Periodo");
        SubMenu periodoSubMenu = periodoMenu.getSubMenu();

        for (EAPeriodo periodo : EAPeriodo.values()) {
            periodoSubMenu.addItem(periodo.getTag(), event -> sincroPeriodi(event.getSource()));
        }// end of for cycle

        return menuBar;
    }// end of method


    /**
     * Modifica il periodo visualizzato nel tabellone
     */
    private void sincroPeriodi(MenuItem itemEvent) {

        String periodoTxt = itemEvent.getText();
        EAPeriodo eaPeriodo = EAPeriodo.get(periodoTxt);

        switch (eaPeriodo) {
            case oggi:
                startDay = LocalDate.now();
                break;
            case lunedi:
                startDay = date.getFirstLunedì(LocalDate.now());
                break;
            case giornoPrecedente:
                startDay = startDay.minusDays(1);
                break;
            case giornoSuccessivo:
                startDay = startDay.plusDays(1);
                break;
            case settimanaPrecedente:
                startDay = startDay.minusDays(GIORNI_STANDARD);
                break;
            case settimanaSuccessiva:
                startDay = startDay.plusDays(GIORNI_STANDARD);
                break;
            case selezione:
                apreSelezione();
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        numDays = GIORNI_STANDARD;

        routeToTabellone(startDay, numDays);

    }


//    /**
//     * Modifica i giorni visualizzati nel tabellonesuperato <br>
//     */
//    @Deprecated
//    private void sincroPeriodi(HasValue.ValueChangeEvent event) {
//        EAPeriodo value = (EAPeriodo) event.getValue();
//
//        if (value != null) {
//            switch (value) {
//                case oggi:
//                    startDay = LocalDate.now();
//                    break;
//                case lunedi:
//                    startDay = date.getFirstLunedì(LocalDate.now());
//                    break;
//                case giornoPrecedente:
//                    startDay = startDay.minusDays(1);
//                    break;
//                case giornoSuccessivo:
//                    startDay = startDay.plusDays(1);
//                    break;
//                case settimanaPrecedente:
//                    startDay = startDay.minusDays(GIORNI_STANDARD);
//                    break;
//                case settimanaSuccessiva:
//                    startDay = startDay.plusDays(GIORNI_STANDARD);
//                    break;
//                case selezione:
//                    apreSelezione();
//                    break;
//                default:
//                    log.warn("Switch - caso non definito");
//                    break;
//            } // end of switch statement
//        }// end of if cycle
//
//        numDays = GIORNI_STANDARD;
//
//        routeToTabellone(startDay, numDays);
//    }// end of method


    private void apreSelezione() {
        Map<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
        mappa.put(KEY_MAP_GIORNO_FINE, date.getISO(startDay.plusDays(numDays - 1)));
        final QueryParameters query = QueryParameters.simple(mappa);
        getUI().ifPresent(ui -> ui.navigate(TAG_SELEZIONE, query));
    }


    /**
     * Ritorno al tabellone coi parametri selezionati
     */
    private void routeToTabellone(LocalDate startDay, int numDays) {
        Map<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
        mappa.put(KEY_MAP_GIORNI_DURATA, numDays + "");
        final QueryParameters query = QueryParameters.simple(mappa);
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST, query));
    }


//    /**
//     * Ritorno al tabellone coi parametri selezionati
//     */
//    private void routeToTabellone(LocalDate startDay, LocalDate endDay) {
//        Map<String, String> mappa = new LinkedHashMap<>();
//        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
//        mappa.put(KEY_MAP_GIORNO_FINE, date.getISO(endDay));
//        final QueryParameters query = QueryParameters.simple(mappa);
//
//        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST, query));
//    }// end of method


//    /**
//     * Costruisce un (eventuale) layout con bottoni aggiuntivi
//     * Facoltativo (assente di default)
//     * Può essere sovrascritto, per aggiungere informazioni
//     * Invocare PRIMA il metodo della superclasse
//     */
//    @Override
//    protected void creaGridBottomLayout() {
////        super.creaGridBottomLayout();
////
////        //--legenda dei colori
////        if (pref.isBool(MOSTRA_LEGENDA_TABELLONE)) {
////            gridPlaceholder.add(appContext.getBean(LegendaPolymer.class));
////        }
//
//    }


//    /**
//     * Primo ingresso dopo il click sul bottone <br>
//     *
//     * @param entityBean
//     * @param operation
//     */
//    @Override
//    protected void save(AEntity entityBean, EAOperation operation) {
////        entityBean = service.beforeSave(entityBean, operation);
////        switch (operation) {
////            case addNew:
////                if (service.isEsisteEntityKeyUnica(entityBean)) {
////                    Notification.show(entityBean + " non è stata registrata, perché esisteva già con lo stesso code ", 3000, Notification.Position.BOTTOM_START);
////                } else {
////                    service.save(entityBean);
////                    updateGrid();
////                    Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
////                }// end of if/else cycle
////                break;
////            case edit:
////            case editDaLink:
////                service.save(entityBean);
////                Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
////                this.grid.getDataProvider().refreshAll();
////                break;
////            default:
////                log.warn("Switch - caso non definito");
////                break;
////        }
//    }


//    @Override
//    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
////        super.beforeEnter(beforeEnterEvent);
//    }// end of method


}
