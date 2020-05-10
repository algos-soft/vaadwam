package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.selection.MultiSelectionEvent;
import com.vaadin.flow.data.selection.MultiSelectionListener;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.broadcast.Broadcaster;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Tabellone di servizi, turni e iscrizioni
 */
//@JavaScript("frontend://js/js-comm.js")
//@JavaScript("frontend://js/tabellone.js")

//@Route(value = TAG_TAB_LIST, layout = WamLayout.class)
//@Route(value = TAG_TAB_LIST+"new", layout = AppLayout.class)
//@Route(value = TAG_TAB_LIST+"new", layout = TabelloneAppLayout.class)
@Route(value = "tabnew", layout = WamLayout.class)
//@ParentLayout(AppLayout.class)
//@Push @todo  LEVATO  IL  PUSH  E  RIMESSO  IL  WamLayout.class  FUNZIONA
//@Push

@Tag("tabellone-polymer")
@HtmlImport("src/views/tabellone/tabellone-polymer.html")
@Slf4j
public class Tabellone extends PolymerTemplate<TabelloneModel> implements ITabellone, HasUrlParameter<String> {

    // valore di default per il numero di giorni visualizzati nel tabellone
    public final static int NUM_GIORNI_DEFAULT = 7;

//    @Autowired
//    protected RigaService rigaService;

    @Autowired
    private TurnoService turnoService;

//    @Autowired
//    private IscrizioneService iscrizioneService;

    @Autowired
    private TabelloneService tabelloneService;

//    @Autowired
//    private CroceService croceService;

    @Autowired
    private FunzioneService funzioneService;

//    @Autowired
//    @Qualifier(TAG_CRO)
//    private WamService wamService;

    @Id
    private Dialog turnodialog;


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

//    /**
//     * Devo mantenere un valore perché il comboBox viene ricostruito ad ogni modifica della Grid <br>
//     */
//    private EAPeriodo currentPeriodValue = EAPeriodo.oggi;

//    @Autowired
//    private MiliteService militeService;

//    private AComboBox comboPeriodi;

    @Autowired
    protected ApplicationContext appContext;

    private AArrayService arrayService = AArrayService.getInstance();

    private ADateService dateService = ADateService.getInstance();

//    private ATextService textService = ATextService.getInstance();

    @Autowired
    protected PreferenzaService preferenzaService;

    @Autowired
    protected AVaadinService vaadinService;

    private Registration broadcasterRegistration;

    /**
     * Mappa chiave-valore di un singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, String> parametersMap = null;

    @Id("tabellonegrid")
    private Grid grid;

//    @Id
//    private Checkbox modoUtente;
//
//    @Id
//    private Checkbox modoCentralinista;
//
//    @Id
//    private Checkbox modoAdmin;

    @Id
    private CheckboxGroup<String> boxGroup;



    private String wCol1="7em";
    private String wColonne="45mm";


    public Tabellone() {
    }

    @PostConstruct
    private void init() {


        boxGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<CheckboxGroup<String>, Set<String>>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<CheckboxGroup<String>, Set<String>> event) {
                Set<String> setNew = event.getValue();
                Set<String> setOld  =event.getOldValue();
                int a = 87;
                int b=a;
            }
        });


//        // registra il riferimento al server Java nel client JS
//        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

        getModel().setSingola(true);

        buildColoriLegenda();

        AContext context = vaadinService.getSessionContext();
        wamLogin = (WamLogin) context.getLogin();

        grid.setHeightByRows(true);
        grid.addThemeNames("no-border");
        grid.addThemeNames("no-row-borders");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
//        grid.setVerticalScrollingEnabled(false);

//        fillHeaderModel();

        buildAllGrid();

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(newMessage -> {
            ui.access(() -> {
                if(newMessage.equals("turnosaved")){
                    loadDataInGrid();
                }
            });
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }



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
        List<Riga> gridItems = tabelloneService.getGridRigheList(startDay, numDays);
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
                    lastInType = lastInType && servizio.ripetibile;
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

        ValueProvider<AEntity, TurnoCellPolymer> componentProvider = new ValueProvider() {

            @Override
            public Object apply(Object obj) {
                Riga riga = (Riga) obj;
                TurnoCellPolymer turnoCellPolymer = appContext.getBean(TurnoCellPolymer.class, iTabellone, riga, day);
                return turnoCellPolymer;
            }
        };

        Grid.Column column = this.grid.addComponentColumn(componentProvider);


        //column.setHeader(dateService.get(day, EATime.weekShortMese));
        String text=dateService.get(day, EATime.weekShortMese);
        Component comp=createHeaderComponent(text);
        column.setHeader(comp);

        column.setFlexGrow(0);
        column.setWidth(wColonne);
        column.setSortable(false);
        column.setResizable(false);

    }

    private Component createHeaderComponent(String text){
        Div div = new Div();
        div.add(new Label(text));
        div.setText(text);
        div.getStyle().set("display","flex");
        div.getStyle().set("font-size","120%");
        div.getStyle().set("justify-content","center");
        div.getStyle().set("align-items","center");
        return div;
    }

    /**
     * Crea l'header della colonna servizi
     * Contiene un listener per modificare i giorni visualizzati nel tabellone
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
                break;
            case lunedi:
                startDay = dateService.getFirstLunedì(LocalDate.now());
                break;
            case giornoPrecedente:
                startDay = startDay.minusDays(1);
                break;
            case giornoSuccessivo:
                startDay = startDay.plusDays(1);
                break;
            case settimanaPrecedente:
                startDay = startDay.minusDays(7);
                break;
            case settimanaSuccessiva:
                startDay = startDay.plusDays(7);
                break;
            case selezione:
                showDetail();
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        }

        numDays = NUM_GIORNI_DEFAULT;

        buildAllGrid();

    }


    /**
     * Cella cliccata nel tabellone.
     * <p>
     *
     * @param turno,   se esiste, null se non è stato ancora creato
     * @param giorno   se il turno è nullo, il giorno cliccato
     * @param servizio se il turno è nullo, il servizio cliccato
     * @param codFunzione la funzione relativa alla cella cliccata
     */
    @Override
    public void cellClicked(Turno turno, LocalDate giorno, Servizio servizio, String codFunzione) {

        // crea il turno se non esiste
        if (turno == null) {

            if (preferenzaService.isBool(EAPreferenzaWam.nuovoTurno) || wamLogin.isAdminOrDev()) {   // può creare turni
                turno = turnoService.newEntity(giorno, servizio);
            } else {  // non può creare turni
                String desc = servizio.descrizione;
                String giornoTxt = dateService.get(giorno, EATime.weekShortMese);
                Notification.show("Per " + giornoTxt + " non è (ancora) previsto un turno di " + desc + ". Per crearlo, devi chiedere ad un admin", 5000, Notification.Position.MIDDLE);
                return;
            }

        }

        // switch dell'editor in funzione del tipo di editing (singolo o multiplo)
        String type="multi";
//        if (modoUtente.getValue()) {
//            type = "single";
//        } else {
//            type = "multi";
//        }
//
//        if (modoCentralinista.getValue()) {
//
//        }
//
//        if (modoAdmin.getValue()) {
//
//        }

        Component editor = null;
        switch (type) {

            case ("single"):// modalità iscrizione singola
                editor = editSingle(turno, codFunzione);
                break;

            case ("multi"):// modalità iscrizioni multiple
                editor = editMulti(turno);
                break;
        }

        // presenta il dialogo
        if (editor != null) {
            turnodialog.removeAll();
            turnodialog.add(editor);
            turnodialog.open();
        }

    }


    /**
     * Verifica che una iscrizione sia editabile e ritorna l'editor singolo
     */
    private Component editSingle(Turno turno, String codFunzione) {

        // in modalità singola lo storico è solo read-only
        if(turno.getGiorno().isBefore(LocalDate.now())){
            Iscrizione iscrizione = getIscrizione(turno, codFunzione);
            return creaEditorSingolo(turno, iscrizione, true); // editor read-only
        }

        Component editor = null;

        if (isLibera(turno, codFunzione)) {   // la cella è libera
            if (isCompatibile(wamLogin.getMilite(), codFunzione)) {   // il milite loggato ha questa funzione
                if (!isIscritto(turno, wamLogin.getMilite())) {   // non è già iscritto a questo turno
                    Iscrizione iscrizione = getIscrizione(turno, codFunzione);
                    editor=creaEditorSingolo(turno, iscrizione, false); // editor RW
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
                boolean inTempo = !tabelloneService.isPiuRecente(turno, wamLogin.getCroce().getGiorniCritico());
                if (inTempo) {    // è in tempo per modificare
                    editor=creaEditorSingolo(turno, iscrizione, false); // editor RW
                } else {  // non è più in tempo per modificare
                    editor=creaEditorSingolo(turno, iscrizione, true); // editor read-only
                }

            } else {  // occupata da un altro
                editor=creaEditorSingolo(turno, iscrizione, true); // editor read-only
            }
        }

        return editor;

    }


    private Component creaEditorSingolo(Turno turno, Iscrizione iscrizione, boolean readOnly){
        return appContext.getBean(IscrizioneEditPolymer.class, this, turnodialog, turno, iscrizione, readOnly);
    }

    /**
     * Verifica che un turno sia editabile e ritorna l'editor multiplo
     */
    private Component editMulti(Turno turno) {
        Component editor;
        editor = appContext.getBean(TurnoEditPolymer.class, this, turnodialog, turno);
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
        long count = milite.getFunzioni().stream().filter(funzione -> funzione.getCode().equals(codFunzione)).count();
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
        turnoService.save(turno);
        //loadDataInGrid();
        //grid.setDataProvider(grid.getDataProvider());   // refresh
        Broadcaster.broadcast("turnosaved");    // provoca l'update della GUI di questo e degli altri client

    }

    /**
     * Aggiunge i colori legenda al modello
     */
    private void buildColoriLegenda(){
        List<LegendaItemModel> colori=new ArrayList<>();
        for(EAWamColore eaw : EAWamColore.values()){
            LegendaItemModel item = new LegendaItemModel();
            item.setNome(eaw.getTitolo());
            item.setColore(eaw.getEsadecimale());
            colori.add(item);
        }
        getModel().setColoriLegenda(colori);
    }

    // mostra il dettaglio della selezione periodo
    private void showDetail() {

        Map<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, dateService.getISO(startDay));
        mappa.put(KEY_MAP_GIORNO_FINE, dateService.getISO(startDay.plusDays(numDays - 1)));
//        final QueryParameters query = QueryParameters.simple(mappa);
//        getUI().ifPresent(ui -> ui.navigate(TAG_SELEZIONE, query));

        int a = 87;
        int b = a;


    }


    /**
     * Metodo di test
     */
    private void fillHeaderModel() {

        getModel().setWCol1(wCol1);
        getModel().setWColonne(wColonne);

        List<HeaderCellModel> headers = getModel().getHeaders();
        HeaderCellModel hcm;

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Lunedì");
        headers.add(hcm);

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Martedì");
        headers.add(hcm);

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Mercoledì");
        headers.add(hcm);

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Giovedì");
        headers.add(hcm);

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Venerdì");
        headers.add(hcm);

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Sabato");
        headers.add(hcm);

        hcm =new HeaderCellModel();
        hcm.setTitoloColonna("Domenica");
        headers.add(hcm);

    }


}
