package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.footer.AFooter;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.broadcast.BroadcastMsg;
import it.algos.vaadwam.broadcast.Broadcaster;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.algos.vaadwam.application.WamCost.*;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Tabellone di servizi, turni e iscrizioni
 */
//@JavaScript("frontend://js/js-comm.js")
//@JavaScript("frontend://js/tabellone.js")

@Route(value = "", layout = WamLayout.class)
@Tag("tabellone-polymer")
@HtmlImport("src/views/tabellone/tabellone-polymer.html")
@Slf4j
@AIView(vaadflow = false, menuName = "tabellone")
@PageTitle(WamCost.BROWSER_TAB_TITLE)
public class Tabellone extends PolymerTemplate<TabelloneModel> implements ITabellone, HasUrlParameter<String> {

    // valore di default per il numero di giorni visualizzati nel tabellone
    public final static int NUM_GIORNI_DEFAULT = 7;


    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected PreferenzaService preferenzaService;

    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Mappa chiave-valore di un singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, String> parametersMap = null;

    @Value("${wam.tabellone.banner}")
    private String banner;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private TabelloneService tabelloneService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private RigaService rigaService;

    @Id
    private Dialog turnodialog;

    @Id("tabellonegrid")
    private Grid grid;

    // modello dati per la griglia
    private List<Riga> gridItems;

    @Id
    private Checkbox modoUtente;

    @Id
    private Checkbox modoCentralinista;

    @Id
    private Checkbox modoAdmin;

    @Id
    private Div legendaLayer;

    @Id
    private Button bColori;

    @Id
    private Button bAddServizio;

    @Id
    private Button bGenTurni;

    @Id
    private Div divtabellone;

    @Id
    private Div divAppFooter;


    /**
     * Primo giorno visualizzato
     */
    private LocalDate startDay = LocalDate.now();

    /**
     * Numero di giorni visualizzati
     */
    private int numDays = NUM_GIORNI_DEFAULT;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    private WamLogin wamLogin;

    @Autowired
    private AArrayService arrayService;

    @Autowired
    private ADateService dateService;

    @Autowired
    private AFooter appFooter;


    private Registration broadcasterRegistration;


    private String wCol1 = "7em";

    private String wColonne = "45mm";


    public Tabellone() {
    }


    @PostConstruct
    private void init() {

        //        log.debug("Log di level DEBUG");
        //        log.info("Log di level INFO");
        //        log.warn("Log di level WARN");
        //        log.error("Log di level ERROR");

        AContext context = vaadinService.getSessionContext();
        if (context == null) {
            return;
        }

        //        // registra il riferimento al server Java nel client JS
        //        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

        initChecks();

        getModel().setSingola(true);

        initLegendaColori();

        wamLogin = (WamLogin) context.getLogin();

        grid.setHeightByRows(true);
        grid.addThemeNames("no-border");
        grid.addThemeNames("no-row-borders");
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // quando clicco in qualsiasi punto chiudo la palette se aperta
        divtabellone.addClickListener((ComponentEventListener<ClickEvent<Div>>) divClickEvent -> legendaLayer.getStyle().set("visibility", "hidden"));

        // bottone Nuovo Servizio (solo se la croce li usa)
        bAddServizio.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> clickAddServizio());
        bAddServizio.setVisible(servizioService.usaExtra());

        // bottone Genera Turni (solo admin e developer)
        bGenTurni.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> clickGenTurni());
        bGenTurni.setVisible(isSuperUser());

        // app footer
        divAppFooter.add(appFooter);

        getModel().setBanner(banner);

        // costruisce la grid
        buildAllGrid();


    }


    private void initChecks() {
        modoUtente.setValue(true);
        modoUtente.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>) event -> {
            if (event.getValue()) {
                modoCentralinista.setValue(false);
                modoAdmin.setValue(false);
            }
        });

        modoCentralinista.setValue(false);
        modoCentralinista.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>) event -> {
            if (event.getValue()) {
                modoUtente.setValue(false);
                modoAdmin.setValue(false);
            }
        });

        modoAdmin.setValue(false);
        modoAdmin.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>) event -> {
            if (event.getValue()) {
                modoUtente.setValue(false);
                modoCentralinista.setValue(false);
            }
        });

    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(message -> ui.access(() -> {
            String code = message.getCode();

            if (code.equals("turnosaved") || code.equals("turnodeleted")) {
                LocalDate giorno = (LocalDate) message.getPayload();
                if (isInTabellone(giorno)) {
                    loadDataInGrid();
                }
            }

            if (code.equals("turnomultisave")) {
                // se ne trova anche solo uno che è nel tabellone visualizzato, aggiorna
                boolean found = false;
                List<LocalDate> giorni = (List<LocalDate>) message.getPayload();
                for (LocalDate giorno : giorni) {
                    if (isInTabellone(giorno)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    loadDataInGrid();
                }
            }

        }));
    }


    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }


    /**
     * Controlla se una data è all'interno del periodo corrente
     */
    boolean isInTabellone(LocalDate testDate) {
        LocalDate endDate = startDay.plusDays(numDays - 1);
        return !(testDate.isBefore(startDay) || testDate.isAfter(endDate));
    }

    //    /**
    //     * Controlla se un dato periodo interseca il periodo del tabellone
    //     */
    //    private boolean intersecaTabellone(LocalDate data1, LocalDate data2) {
    //        LocalDateRange rangeToCheck = LocalDateRange.ofClosed(data1, data2);
    //        LocalDateRange rangeTabellone = LocalDateRange.ofClosed(startDay, startDay.plusDays(numDays));
    //        LocalDateRange intersection = rangeToCheck.intersection(rangeTabellone);
    //        return !intersection.isEmpty();
    //    }


    private void buildAllGrid() {

        grid.removeAllColumns();

        // costruisce la colonna dei servizi
        addColumnServizi();

        // costruisce le colonne dei turni
        for (int i = 0; i < numDays; i++) {
            addColumnsTurni(startDay.plusDays(i));
        }

        loadDataInGrid();
    }


    /**
     * Invoked from the JS client when is safe to invoke JS functions operating on the DOM.
     * (The DOM is ready and the page is completely loaded).
     * For this command to work, remember to register the server in the constructor:
     * UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());
     */
    @ClientCallable
    public void pageReady() {

        //        UI.getCurrent().getPage().executeJs("setupScrollListener()");
        //
        //        // restore the previous scroll position
        //        VaadinSession session = VaadinSession.getCurrent();
        //        Object objX=session.getAttribute("tabelloneScrollX");
        //        Object objY=session.getAttribute("tabelloneScrollY");
        //        if (objX!=null && objY!=null){
        //            int x = (Integer)objX;
        //            int y = (Integer)objY;
        //            UI.getCurrent().getPage().executeJs("scrollTabelloneTo($0,$1)", x, y);
        //        }

    }

    //    @ClientCallable
    //    public void tabScrolled(int x, int y){
    //        log.info( "container scrolled: x="+x+", y="+y);
    //        // Store the current scrollscroll position in the current Context
    //        VaadinSession session = VaadinSession.getCurrent();
    //        session.setAttribute("tabelloneScrollX",x);
    //        session.setAttribute("tabelloneScrollY",y);
    //    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        //super.setParameter(event, parameter);
        String isoValue;
        LocalDate endDay;

        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> params = queryParameters.getParameters();


        if (arrayService.isValid(params)) {
            this.parametersMap = arrayService.semplificaMappa(params);
        }// end of if cycle

        if (arrayService.isValid(parametersMap)) {
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNO_INIZIO)) {
                isoValue = parametersMap.get(KEY_MAP_GIORNO_INIZIO);
                startDay = dateService.localDateFromISO(isoValue);
            }
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNO_FINE)) {
                isoValue = parametersMap.get(KEY_MAP_GIORNO_FINE);
                endDay = dateService.localDateFromISO(isoValue);
                numDays = dateService.differenza(endDay, startDay);
                numDays++; //--gli estremi sono compresi
            }
            if (parametersMap != null && parametersMap.containsKey(KEY_MAP_GIORNI_DURATA)) {
                numDays = Integer.decode(parametersMap.get(KEY_MAP_GIORNI_DURATA));
            }
        }
    }


    /**
     * Carica gli items nella Grid, utilizzando i filtri correnti
     */
    private void loadDataInGrid() {
        gridItems = tabelloneService.getGridRigheList(startDay, numDays);
        grid.setItems(gridItems);
    }


    /**
     * Crea la colonna per visualizzare i servizi previsti
     */
    private void addColumnServizi() {

        ValueProvider<AEntity, ServizioCellPolymer> componentProvider = new ValueProvider() {

            String currentType = "";


            @Override
            public Object apply(Object obj) {
                ServizioCellPolymer servizioCell = null;
                Servizio servizio;
                boolean lastInType;

                servizio = ((Riga) obj).getServizio();

                if (servizio != null) {
                    lastInType = (!servizio.getCode().equals(currentType));
                    lastInType = lastInType && servizio.extra;
                    lastInType = false; //@todo PROVVISORIO
                    servizioCell = appContext.getBean(ServizioCellPolymer.class, servizio, lastInType);
                    currentType = servizio.getCode();
                }

                return servizioCell != null ? servizioCell : new Label("Manca");
            }
        };

        Grid.Column column = grid.addComponentColumn(componentProvider);

        // provare a sostituire questo componente con un Menu
        Component component = periodoHeader();

        column.setHeader(component);
        column.setFlexGrow(0);
        column.setWidth(wCol1);
        column.setSortable(false);
        column.setResizable(false);
        column.setFrozen(true);

    }


    /**
     * Crea e aggiunge la colonna dei turni per un dato giorno
     */
    private void addColumnsTurni(LocalDate day) {

        ITabellone iTabellone = this;

        ValueProvider<AEntity, TurnoCellPolymer> componentProvider = (ValueProvider) obj -> {
            Riga riga = (Riga) obj;
            TurnoCellPolymer turnoCellPolymer = appContext.getBean(TurnoCellPolymer.class, iTabellone, riga, day);
            return turnoCellPolymer;
        };

        Grid.Column column = this.grid.addComponentColumn(componentProvider);


        String text = dateService.get(day, EATime.weekShortMese);
        Component comp = createHeaderComponent(text);
        column.setHeader(comp);

        column.setFlexGrow(0);
        column.setWidth(wColonne);
        column.setSortable(false);
        column.setResizable(false);

    }


    private Component createHeaderComponent(String text) {
        Div div = new Div();
        div.add(new Label(text));
        div.setText(text);
        div.getStyle().set("display", "flex");
        div.getStyle().set("font-size", "120%");
        div.getStyle().set("justify-content", "center");
        div.getStyle().set("align-items", "center");
        return div;
    }


    /**
     * Crea l'header della colonna servizi
     */
    private Component periodoHeader() {

        MenuBar menuBar = new MenuBar();
        menuBar.setWidth("20em");

        MenuItem periodoMenu = menuBar.addItem("Periodo");
        SubMenu periodoSubMenu = periodoMenu.getSubMenu();

        for (EAPeriodo periodo : EAPeriodo.values()) {
            periodoSubMenu.addItem(periodo.getTag(), event -> sincroPeriodi(event.getSource()));
        }


        //        Div div = new Div();
        //        div.getStyle().set("display","flex");
        //        div.getStyle().set("flex-direction","row");
        //        IronIcon icon = new IronIcon("vaadin", "date-range");
        //        icon.setSize("2em");
        //        Icon icon2 = VaadinIcon.TRASH.create();
        //        icon2.setSize("2em");
        //        div.add(icon);
        //        div.add(icon2);
        ////        div.add(menuBar);

        return menuBar;
    }


    /**
     * Modifica il periodo visualizzato nel tabellone
     */
    private void sincroPeriodi(MenuItem itemEvent) {

        String periodoTxt = itemEvent.getText();
        EAPeriodo eaPeriodo = EAPeriodo.get(periodoTxt);

        switch (eaPeriodo) {
            case oggi:
                startDay = LocalDate.now();
                buildAllGrid();
                break;
            case lunedi:
                startDay = dateService.getFirstLunedì(LocalDate.now());
                buildAllGrid();
                buildAllGrid();
                break;
            case giornoPrecedente:
                startDay = startDay.minusDays(1);
                buildAllGrid();
                break;
            case giornoSuccessivo:
                startDay = startDay.plusDays(1);
                buildAllGrid();
                break;
            case settimanaPrecedente:
                startDay = startDay.minusDays(7);
                buildAllGrid();
                break;
            case settimanaSuccessiva:
                startDay = startDay.plusDays(7);
                buildAllGrid();
                break;
            case selezione:
                selezionaPeriodoCustom();
                break;
            default:
                log.error("Switch - caso non definito");
                break;
        }


    }


    /**
     * Cella cliccata nel tabellone.
     * <p>
     *
     * @param turno,      se esiste, null se non è stato ancora creato
     * @param giorno      se il turno è nullo, il giorno cliccato
     * @param servizio    se il turno è nullo, il servizio cliccato
     * @param codFunzione la funzione relativa alla cella cliccata
     */
    @Override
    public void cellClicked(Turno turno, LocalDate giorno, Servizio servizio, String codFunzione) {

        // creazione del turno se non esiste
        boolean nuovoTurno = false;
        if (turno == null) {

            boolean creaTurno = false;
            if (servizio.isOrarioDefinito()) { //turni standard: manager li può creare sempre, gli altri utenti solo se la company lo prevede
                if (isUtenteManagerTabellone()) {
                    creaTurno = true;
                } else {
                    if (preferenzaService.isBool(EAPreferenzaWam.nuovoTurno)) {
                        creaTurno = true;
                    }
                }
            } else { //turni non-standard: li possono creare solo manager e utente abilitato
                if (isUtenteManagerTabellone() || isUtenteAbilitatoCreareTurniExtra()) {
                    creaTurno = true;
                }
            }

            // creazione effettiva del turno, o blocco e uscita con messaggio
            if (creaTurno) {
                turno = turnoService.newEntity(giorno, servizio);
                nuovoTurno = true;
            } else {
                String desc = servizio.descrizione;
                String giornoTxt = dateService.get(giorno, EATime.weekShortMese);
                Notification.show("Per " + giornoTxt + " non è (ancora) previsto un turno di " + desc + ". Per crearlo, devi chiedere ad un admin", 5000, Notification.Position.MIDDLE);
                return;
            }

        }


        // switch dell'editor in funzione del tipo di editing (singolo o multiplo)
        // se sono arrivato qui il turno esiste per forza
        Component editor = selectTurnoEditor(turno, nuovoTurno, servizio, codFunzione);

        // presenta il dialogo
        if (editor != null) {
            turnodialog.removeAll();
            turnodialog.add(editor);
            turnodialog.open();
        }

    }


    /**
     * Seleziona l'editor di turno da utilizzare
     */
    private Component selectTurnoEditor(Turno turno, boolean nuovoTurno, Servizio servizio, String codFunzione) {

        // Manager Tabellone, può fare tutto
        if (isUtenteManagerTabellone()) {
            return editMulti(turno, !nuovoTurno);
        }

        // Utente abilitato a create turni extra.
        // Se è un turno standard:
        // - edit singolo
        // Se è un turno extra:
        // - nel periodo corrente edit multiplo senza cancella
        // - nello storico edit singolo
        if (isUtenteAbilitatoCreareTurniExtra()) {
            if (servizio.isOrarioDefinito()) {    // turno standard
                return editSingle(turno, codFunzione);
            } else {  // turno extra
                if (!isTurnoStorico(turno)) { // corrente
                    return editMulti(turno, false);
                } else {  // storico
                    if (getIscrizione(turno, codFunzione).getMilite() != null) {    // c'è un iscritto
                        return editSingle(turno, codFunzione);
                    } else {  // nessun iscritto
                        return null;
                    }
                }
            }
        }

        // da qui in poi è utente normale, sempre editor di tipo singolo
        if (!isTurnoStorico(turno)) { // corrente
            return editSingle(turno, codFunzione);
        }

        // utente normale, turno storico
        if (getIscrizione(turno, codFunzione).getMilite() != null) {    // c'è un iscritto
            return editSingle(turno, codFunzione);
        } else {  // nessun iscritto
            return null;
        }

    }


    private boolean isTurnoStorico(Turno turno) {
        return turno.getGiorno().isBefore(LocalDate.now());
    }


    private boolean isUtenteAbilitatoCreareTurniExtra() {

        if (wamLogin.isDeveloper()) {
            return true;
        }

        if (wamLogin.getMilite().isCreatoreTurni()) {
            return true;
        }

        return false;

    }


    private boolean isUtenteManagerTabellone() {


        // qui non controlliamo se è Admin perché dobbiamo lasciargli la
        //possibilità di operare come user togliendosi il flag isManagerTabellone
        // ...

        if (wamLogin.isDeveloper()) {
            return true;
        }

        if (wamLogin.getMilite().isManagerTabellone()) {
            return true;
        }

        return false;

    }


    private boolean isSuperUser() {
        return (wamLogin.isDeveloper() || wamLogin.isAdmin());
    }


    /**
     * Verifica che una iscrizione sia editabile e ritorna l'editor singolo
     */
    private Component editSingle(Turno turno, String codFunzione) {

        // in modalità editor singolo lo storico è sempre read-only
        if (turno.getGiorno().isBefore(LocalDate.now())) {
            Iscrizione iscrizione = getIscrizione(turno, codFunzione);
            return creaEditorSingolo(turno, iscrizione, true); // editor read-only
        }

        Component editor = null;

        if (isLibera(turno, codFunzione)) {   // la cella è libera
            if (isCompatibile(wamLogin.getMilite(), codFunzione)) {   // il milite loggato ha questa funzione
                if (!isIscritto(turno, wamLogin.getMilite())) {   // non è già iscritto a questo turno

                    Iscrizione iscrizione = getIscrizione(turno, codFunzione);

                    // è nuova iscrizione, assegna gli orari dal turno
                    iscrizione.setInizio(turno.getInizio());
                    iscrizione.setFine(turno.getFine());

                    editor = creaEditorSingolo(turno, iscrizione, false); // editor RW

                } else {   // è già iscritto a questo turno
                    Funzione funzione = funzioneService.findByKeyUnica(wamLogin.getCroce(), codFunzione);
                    String text = "Sei già iscritto a questo turno come " + funzione.getDescrizione();
                    notify(text);
                }
            } else {  // il milite loggato non ha questa funzione
                Funzione funzione = funzioneService.findByKeyUnica(wamLogin.getCroce(), codFunzione);
                String text = "Non sei abilitato a iscriverti come " + funzione.getDescrizione();
                notify(text);
            }
        } else {  // la cella è occupata
            Iscrizione iscrizione = getIscrizione(turno, codFunzione);
            if (iscrizione.getMilite().equals(wamLogin.getMilite())) { // occupata da se stesso

                String result = tabelloneService.puoCancellareIscrizione(turno, iscrizione);
                boolean puoCancellare = StringUtils.isEmpty(result);

                if (puoCancellare) {    // può ancora modificare/cancellare
                    editor = creaEditorSingolo(turno, iscrizione, false); // editor RW
                } else {  // non può più modificare
                    editor = creaEditorSingolo(turno, iscrizione, true); // editor read-only
                }

            } else {  // occupata da un altro
                editor = creaEditorSingolo(turno, iscrizione, true); // editor read-only
            }
        }

        return editor;

    }


    private Component creaEditorSingolo(Turno turno, Iscrizione iscrizione, boolean readOnly) {
        return appContext.getBean(IscrizioneEditPolymer.class, this, turnodialog, turno, iscrizione, readOnly);
    }


    /**
     * Verifica che un turno sia editabile e ritorna l'editor multiplo
     */
    private Component editMulti(Turno turno, boolean abilitaCancellaTurno) {
        Component editor;
        editor = appContext.getBean(TurnoEditPolymer.class, this, turnodialog, turno, abilitaCancellaTurno);
        return editor;
    }


    /**
     * Determina se una iscrizione è libera o occupata
     */
    private boolean isLibera(Turno turno, String codFunzione) {
        Iscrizione iscrizione = getIscrizione(turno, codFunzione);
        return iscrizione.getMilite() == null;
    }


    /**
     * Determina se un Milite ha una Funzione
     */
    private boolean isCompatibile(Milite milite, String codFunzione) {
        long count = 0;

        if (milite.getFunzioni() != null) {
            count = milite.getFunzioni().stream().filter(funzione -> funzione.getCode().equals(codFunzione)).count();
        }

        return count > 0;
    }


    /**
     * Recupera una iscrizione da un turno dato il codice funzione
     */
    private Iscrizione getIscrizione(Turno turno, String codFunzione) {
        Iscrizione iscrizione = null;
        for (Iscrizione i : turno.getIscrizioni()) {
            if (i.getFunzione().code.equals(codFunzione)) {
                iscrizione = i;
                break;
            }
        }
        return iscrizione;
    }


    /**
     * Determina se un Milite è iscritto a un Turno
     */
    private boolean isIscritto(Turno turno, Milite milite) {
        boolean iscritto = false;
        for (Iscrizione iscrizione : turno.getIscrizioni()) {
            Milite mIscritto = iscrizione.getMilite();
            if (mIscritto != null && mIscritto.equals(milite)) {
                iscritto = true;
                break;
            }
        }
        return iscritto;
    }


    /**
     * Mostra una notifica generica
     */
    private void notify(String text) {
        Notification notification = new Notification(text);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }


    @Override
    public void annullaDialogoTurno(Dialog dialog) {
        dialog.close();
    }


    @Override
    public void confermaDialogoTurno(Dialog dialog, Turno turno) {
        dialog.close();

        //--log di conferma
        tabelloneService.fixModificaTurno(turno);

        turnoService.save(turno);

        BroadcastMsg msg = new BroadcastMsg("turnosaved", turno.getGiorno());
        Broadcaster.broadcast(msg);    // provoca l'update della GUI di questo e degli altri client

    }


    @Override
    public void confermaDialogoIscrizione(Dialog dialog, Turno turno, Iscrizione iscrizione, boolean ripeti, int numSettimane) {
        dialog.close();

        turnoService.save(turno);

        // effettuo le eventuali ripetizioni
        if (ripeti) {
            List<Turno> turniMod = ripetiTurno(turno.getServizio(), iscrizione, turno.getGiorno().plusWeeks(1), numSettimane);
            if (turniMod.size() > 0) {
                ArrayList<LocalDate> dates = new ArrayList<>();
                dates.add(turno.getGiorno());   // aggiungi anche il turno originale
                for (Turno t : turniMod) {
                    dates.add(t.getGiorno());
                }
                BroadcastMsg msg = new BroadcastMsg("turnomultisave", dates);
                Broadcaster.broadcast(msg);    // provoca l'update della GUI di questo e degli altri client
            }
        } else {
            BroadcastMsg msg = new BroadcastMsg("turnosaved", turno.getGiorno());
            Broadcaster.broadcast(msg);    // provoca l'update della GUI di questo e degli altri client
        }


    }


    @Override
    public void eliminaTurno(Dialog dialog, Turno turno) {
        dialog.close();

        //--log di cancellazione
        tabelloneService.fixCancellaTurno(turno);

        turnoService.delete(turno);

        BroadcastMsg msg = new BroadcastMsg("turnodeleted", turno.getGiorno());
        Broadcaster.broadcast(msg);    // provoca l'update della GUI di questo e degli altri client
    }


    /**
     * Ripete una iscrizione esistente nello stesso giorno per un dato numero di settimane.
     * <p>
     * Crea i turni necessari se mancanti e se l'utente ha il premesso<br>
     * Non sovrascrive iscrizioni esistenti a meno che non siano di se stesso<br>
     * Se non riesce a crere turni o iscrizioni, effettua comunque quelli dove riesce.
     *
     * @param servizio         al quale iscriversi
     * @param iscrizioneMaster da ripetere
     * @param giornoInizio     il giorno nel quale creare la prima iscrizione
     * @param numSettimane     per quante settimane ripetere l'iscrizione (prima compresa)
     *
     * @return la lista dei turni nei quali l'iscrizione è stata effettuata
     */
    private List<Turno> ripetiTurno(Servizio servizio, Iscrizione iscrizioneMaster, LocalDate giornoInizio, int numSettimane) {
        List<Turno> turniModificati = new ArrayList<>();
        LocalDate giorno = giornoInizio;
        for (int i = 0; i < numSettimane; i++) {

            // incrementa il giorno dal secondo ciclo in poi
            if (i > 0) {
                giorno = giorno.plusWeeks(1);
            }

            // recupera il turno
            try {
                List<Turno> turni = turnoService.findByDateAndServizio(giorno, servizio);
                Turno turno = null;
                if (turni.size() > 0) {
                    turno = turni.get(0); // la ripetizione vale solo su turni standard
                }

                // se il turno non esiste va creato ora
                if (turno == null) {
                    Milite milite = wamLogin.getMilite();
                    if (milite.isCreatoreTurni() || isSuperUser()) {
                        turno = turnoService.newEntity(giorno, servizio);
                    } else {
                        log.info("Creazione turno rifiutata a " + wamLogin.getMilite().getSigla() + " durante ripetizione iscrizioni per il giorno " + giorno + " perché non ha il permesso di creazione turni");
                        continue;   // skip iteration
                    }
                }

                // recupera l'iscrizione da modificare
                Iscrizione iscrizione = null;
                for (Iscrizione isc : turno.getIscrizioni()) {
                    if (isc.getFunzione().getCode().equals(iscrizioneMaster.getFunzione().getCode())) {   // attenzione a equals()! oggetti diversi!
                        iscrizione = isc;
                    }
                }

                // controlla se c'è gia un iscritto diverso da se stesso per la funzione
                if (iscrizione.getMilite() != null) {  // c'è un iscritto
                    if (!iscrizione.getMilite().equals(wamLogin.getMilite())) {   // non è lui
                        log.info("Iscrizione rifiutata a " + wamLogin.getMilite().getSigla() + " durante ripetizione iscrizione per il giorno " + giorno + " perché è gia iscritto un altro milite: " + iscrizione.getMilite().getSigla());
                        continue;   // skip iteration
                    }
                }

                // da qui in poi il turno c'è e l'iscrizione si può modificare
                iscrizione.setInizio(iscrizioneMaster.getInizio());
                iscrizione.setFine(iscrizioneMaster.getFine());
                iscrizione.setMilite(iscrizioneMaster.getMilite());
                iscrizione.setFunzione(iscrizioneMaster.getFunzione());
                iscrizione.setNote(iscrizioneMaster.getNote());

                // registra il turno e lo aggiunge all'elenco dei turni modificati
                turnoService.save(turno);
                turniModificati.add(turno);

            } catch (Exception e) {
                log.error("Errore nel recupero del turno del " + giorno + " per il servizio " + servizio.getCode(), e);
            }

        }
        return turniModificati;
    }


    /**
     * Aggiunge i colori legenda al modello
     * Registra i listener - esegue le azioni
     */
    private void initLegendaColori() {
        List<LegendaItemModel> colori = new ArrayList<>();
        ArrayList<EAWamColore> listaWrap;

        if (preferenzaService.isBool(USA_COLORAZIONE_DIFFERENZIATA)) {
            listaWrap = EAWamColore.getColorsIscrizione();
        } else {
            listaWrap = EAWamColore.getColorsTurno();
        }

        for (EAWamColore eaw : listaWrap) {
            LegendaItemModel item = new LegendaItemModel();
            item.setNome(eaw.getTitolo());
            item.setDescrizione(eaw.getLegenda());
            item.setColore(eaw.getEsadecimale());
            colori.add(item);
        }
        getModel().setColoriLegenda(colori);

        // la legenda colori si apre quando clicco l'apposito bottone e si
        // chiude quando clicco in qualsiasi parte del tabellone
        bColori.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            legendaLayer.getStyle().set("visibility", "visible");
        });


    }


    private void selezionaPeriodoCustom() {

        final ConfirmDialog dialog = ConfirmDialog.create();
        Button bConferma = new Button();
        bConferma.setEnabled(false);
        DatePicker picker1 = new DatePicker();
        DatePicker picker2 = new DatePicker();

        Div divDate = new Div();
        divDate.getElement().setAttribute("style", "display: flex; flex-direction: row");
        picker1.getElement().setAttribute("style", "width:8em");
        picker1.setPlaceholder("dal");
        picker1.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate>>) event -> {
            bConferma.setEnabled(event.getValue() != null && picker2.getValue() != null);
        });

        picker2.getElement().setAttribute("style", "width:8em; margin-left:0.5em");
        picker2.setPlaceholder("al");
        picker2.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate>>) event -> {
            bConferma.setEnabled(event.getValue() != null && picker1.getValue() != null);
        });

        divDate.add(picker1);
        divDate.add(picker2);

        bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            LocalDate data1 = picker1.getValue();
            LocalDate data2 = picker2.getValue();
            if (data1.isAfter(data2)) {
                Notification.show("Le date devono essere consecutive", 2000, Notification.Position.MIDDLE);
            } else {
                int quantiGiorni = (int) DAYS.between(data1, data2);
                final int max = 31;
                if (quantiGiorni > max) {
                    Notification.show("Il massimo periodo visualizzabile è di " + max + " giorni", 2000, Notification.Position.MIDDLE);
                } else {
                    dialog.close();
                    startDay = data1;
                    numDays = quantiGiorni + 1;
                    buildAllGrid();
                }
            }
        });

        dialog.withCaption("Periodo da visualizzare").withMessage(divDate).withButton(new Button(), ButtonOption.caption("Annulla")).withButton(bConferma, ButtonOption.caption("Conferma"), ButtonOption.closeOnClick(false));

        dialog.open();

    }


    /**
     * Click sul bottone Nuovo servizio
     */
    private void clickAddServizio() {

        // recupera la lista dei servizi aggiungibili
        List<Servizio> listaServizi = getServiziAggiungibili();


        if (listaServizi.size() == 0) { // non ci sono servizi aggiungibili

            // avvisa e ritorna
            ConfirmDialog.createWarning().withMessage("Non sono disponibili servizi da aggiungere al tabellone.").withButton(new Button(), ButtonOption.caption("Chiudi"), ButtonOption.icon(VaadinIcon.CLOSE)).open();

        } else {    // ci sono servizi aggiungibili

            if (listaServizi.size() == 1) {    // esiste un solo servizio aggiungibile

                addNuovaRiga(listaServizi.get(0)); // lo sceglie automaticamente

            } else {   // esistono più servizi candidati

                // presento un dialogo per scegliere il servizio

                Select<Servizio> select = new Select<>();
                select.setItems(listaServizi);
                select.setValue(listaServizi.get(0));

                Button bConferma = new Button();
                bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
                    addNuovaRiga(select.getValue());
                });

                ConfirmDialog.createQuestion().withCaption("Selezione servizio").withMessage(select).withButton(new Button(), ButtonOption.caption("Annulla")).withButton(bConferma, ButtonOption.caption("Conferma"), ButtonOption.focus()).open();
            }


        }
    }


    /**
     * Crea la lista di tutti i servizi aggiungibili per il popup
     */
    private List<Servizio> getServiziAggiungibili() {

        // tutti i servizi extra visibili
        List<Servizio> serviziExtra = findServiziExtraVisibili();

        // tutti i servizi non-extra e non-standard visibili
        List<Servizio> serviziAltri = findServiziNonExtraNonStandardVisibili();

        // da questi ultimi toglie quelli già a tabellone
        List<Servizio> serviziAltriCandidati = new ArrayList<>();
        for (Servizio servizio : serviziAltri) {
            if (!isPresenteInTabellone(servizio)) {
                serviziAltriCandidati.add(servizio);
            }
        }

        // compone una lista completa
        List<Servizio> listaCompleta = new ArrayList<>();
        listaCompleta.addAll(serviziExtra);
        listaCompleta.addAll(serviziAltriCandidati);

        return listaCompleta;
    }


    /**
     * Click sul bottone Genera Turni
     */
    private void clickGenTurni() {

        ConfirmDialog.setButtonAddClosePerDefault(false);
        final ConfirmDialog dialog = ConfirmDialog.create();
        TurnoGenPolymer generator = appContext.getBean(TurnoGenPolymer.class);

        generator.setCompletedListener(new TurnoGenPolymer.CompletedListener() {

            @Override
            public void onCompleted(TurnoGenWorker.EsitoGenerazioneTurni esito) {
                dialog.close();
                if (esito != null) {
                    if (esito.getQuanti() > 0) {
                        BroadcastMsg msg = new BroadcastMsg("turnomultisave", esito.getGiorni());
                        Broadcaster.broadcast(msg);
                    }
                }
            }
        });

        dialog.add(generator);
        dialog.open();

    }


    private boolean isPresenteInTabellone(Servizio servizio) {
        boolean trovato = false;
        for (Riga riga : gridItems) {
            if (riga.getServizio().equals(servizio)) {
                trovato = true;
                break;
            }
        }
        return trovato;
    }


    /**
     * Ritorna tutti i servizi extra visibili di questa Croce
     */
    private List<Servizio> findServiziExtraVisibili() {
        List<Servizio> servizi = new ArrayList<>();
        for (Servizio servizio : servizioService.findAllByCroce(wamLogin.getCroce())) {
            if (servizio.extra && servizio.isVisibile()) {
                servizi.add(servizio);
            }
        }
        return servizi;
    }


    /**
     * Ritorna tutti i servizi non-standard e non-extra visibili di questa Croce
     */
    private List<Servizio> findServiziNonExtraNonStandardVisibili() {
        List<Servizio> servizi = new ArrayList<>();
        for (Servizio servizio : servizioService.findAllByCroce(wamLogin.getCroce())) {
            if (!servizio.extra && !servizio.isOrarioDefinito() && servizio.isVisibile()) {
                servizi.add(servizio);
            }
        }
        return servizi;
    }


    private void addNuovaRiga(Servizio servizio) {
        Riga riga = rigaService.newEntity(startDay, servizio, null);
        gridItems.add(riga);
        grid.getDataProvider().refreshAll();

        //grid.getDataProvider().refreshItem(riga);
        //grid.setItems(gridItems);
        //grid.setDataProvider(grid.getDataProvider());
        // ...comunque ci si provi, la pagina scrolla sempre al top, e mi sa che c'è poco da fare...
        // https://vaadin.com/forum/thread/17634020/reloading-vaadin-grid-makes-the-page-scroll-to-top

    }


}
