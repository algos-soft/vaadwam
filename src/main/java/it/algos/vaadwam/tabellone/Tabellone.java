package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.claspina.confirmdialog.ButtonOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Tabellone di servizi, turni e iscrizioni
 */
//@JavaScript("frontend://js/js-comm.js")
//@JavaScript("frontend://js/tabellone.js")

//@Route(value = TAG_TAB_LIST, layout = WamLayout.class)
//@Route(value = TAG_TAB_LIST+"new", layout = AppLayout.class)
//@Route(value = TAG_TAB_LIST+"new", layout = TabelloneAppLayout.class)
@Route(value = "tabnew")
//@ParentLayout(AppLayout.class)

@Tag("tabellone-polymer")
@HtmlImport("src/views/tabellone/tabellone-polymer.html")
@Slf4j
public class Tabellone extends PolymerTemplate<TabelloneModel> implements ITabellone, HasUrlParameter<String> {

    // valore di default per il numero di giorni visualizzati nel tabellone
    public final static int NUM_GIORNI_DEFAULT = 7;

    @Autowired
    protected RigaService rigaService;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private TabelloneService tabelloneService;

    @Autowired
    private CroceService croceService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    @Qualifier(TAG_CRO)
    private WamService wamService;

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

    /**
     * Devo mantenere un valore perché il comboBox viene ricostruito ad ogni modifica della Grid <br>
     */
    private EAPeriodo currentPeriodValue = EAPeriodo.oggi;

    @Autowired
    private MiliteService militeService;

    private AComboBox comboPeriodi;

    @Autowired
    protected ApplicationContext appContext;

    private AArrayService arrayService = AArrayService.getInstance();

    private ADateService dateService = ADateService.getInstance();

    private ATextService textService = ATextService.getInstance();

    @Autowired
    protected PreferenzaService preferenzaService;

    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Mappa chiave-valore di un singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, String> parametersMap = null;

    @Id("tabellonegrid")
    private Grid grid;

    public Tabellone() {
    }

    @PostConstruct
    private void init(){

        AContext context = vaadinService.getSessionContext();
        wamLogin=(WamLogin)context.getLogin();

        // registra il riferimento al server Java nel client JS
        // UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

        grid.setHeightByRows(true);
        grid.addThemeNames("no-border");
        grid.addThemeNames("no-row-borders");
        //grid.addThemeNames("row-stripes");    // colorazione righe alterne
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        buildAllGrid();

    }


    private void buildAllGrid(){

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
    public void pageReady(){

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
                Servizio servizio = null;
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
        column.setWidth("7em");
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
                Riga riga = (Riga)obj;
                TurnoCellPolymer turnoCellPolymer =  appContext.getBean(TurnoCellPolymer.class, iTabellone, riga, day);
                return turnoCellPolymer;
            }
        };

        Grid.Column column = this.grid.addComponentColumn(componentProvider);

        column.setHeader(dateService.get(day, EATime.weekShortMese));
        column.setFlexGrow(0);
        column.setWidth("45mm");
        column.setSortable(false);
        column.setResizable(false);

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
     * @param turno, se esiste, null se non è stato ancora creato
     * @param giorno se il turno è nullo, il giorno cliccato
     * @param servizio se il turno è nullo, il servizio cliccato
     */
    @Override
    public void cellClicked(Turno turno, LocalDate giorno, Servizio servizio, String codFunzione) {

        boolean storico=giorno.isBefore(LocalDate.now());
        if(!storico){

            // crea il turno se non esiste
            if (turno==null){

                if (preferenzaService.isBool(EAPreferenzaWam.nuovoTurno) || wamLogin.isAdminOrDev()) {   // può creare turni
                    turno = turnoService.newEntity(giorno, servizio);
                }else{  // non può creare turni
                    String desc = servizio.descrizione;
                    String giornoTxt = dateService.get(giorno, EATime.weekShortMese);
                    Notification.show("Per " + giornoTxt + " non è (ancora) previsto un turno di " + desc + ". Per crearlo, devi chiedere ad un admin", 5000, Notification.Position.MIDDLE);
                    return;
                }

            }

            // switch dell'editor in funzione del tipo di editing (singolo o multiplo)
            String type="single";
            //String type="multi";
            Component editor=null;
            switch (type){

                case("single"):// modalità iscrizione singola
                    editor=editSingle(turno, codFunzione);
                    break;

                case("multi"):// modalità iscrizioni multiple
                    editor=editMulti(turno);
                    break;
            }

            // presenta il dialogo
            if (editor!=null){
                turnodialog.removeAll();
                turnodialog.add(editor);
                turnodialog.open();
            }

        }else{
            log.info("Storico");
        }

    }


    /**
     * Verifica che una iscrizione sia editabile e ritorna l'editor singolo
     */
    private Component editSingle(Turno turno, String codFunzione){

        Component editor=null;

        if(isLibera(turno, codFunzione)){   // la cella è libera
            if(isCompatibile(wamLogin.getMilite(), codFunzione)){   // il milite loggato ha questa funzione
                if(!isIscritto(turno, wamLogin.getMilite())){   // non è già iscritto a questo turno
                    Iscrizione iscrizione = getIscrizione(turno, codFunzione);
                    editor = appContext.getBean(IscrizioneEditPolymer.class,  this, turnodialog, turno, iscrizione);
                }else{   // è già iscritto a questo turno
                    String text="Sei già iscritto a questo turno";
                    notify(text);
                }
            }else{  // il milite loggato non ha questa funzione
                String descFunzione=funzioneService.findByKeyUnica(wamLogin.getCroce(), codFunzione).getDescrizione();
                String text="Non sei abilitato a iscriverti come "+descFunzione+". Per l'abilitazione rivolgiti ad un amministratore";
                notify(text);
            }
        }else{  // la cella è occupata
            Iscrizione iscrizione =getIscrizione(turno, codFunzione);
            if(iscrizione.getMilite().equals(wamLogin.getMilite())){ // l'iscritto è se stesso
                boolean inTempo=!tabelloneService.isPiuRecente(turno, wamLogin.getCroce().getGiorniCritico());
                if(inTempo){    // è in tempo
                    editor = appContext.getBean(IscrizioneEditPolymer.class,  this, turnodialog, turno, iscrizione);
                }else{  // non è più in tempo
                    notify("Il turno è bloccato e non si può più modificare, rivolgiti a un Amministratore");
                }

            }else{  // l'iscritto è un altro
                showDetails(turno, iscrizione);
            }
        }

        return editor;

    }

    /**
     * Verifica che un turno sia editabile e ritorna l'editor multiplo
     */
    private Component editMulti(Turno turno){
        Component editor;
        editor = appContext.getBean(TurnoEditPolymer.class,  this, turnodialog, turno);
        return editor;
    }


        /**
         * Determina se una iscrizione è libera o occupata
         */
    private boolean isLibera(Turno turno, String codFunzione){
        Iscrizione iscrizione = getIscrizione(turno, codFunzione);
        return iscrizione.getMilite()==null;
    }

    /**
     * Determina se un Milite ha una Funzione
     */
    private boolean isCompatibile(Milite milite, String codFunzione){
        long count = milite.getFunzioni().stream().filter(funzione -> funzione.getCode().equals(codFunzione)).count();
        return count > 0 ;
    }


    /**
     * Recupera una iscrizione da un turno dato il codice funzione
     */
    private Iscrizione getIscrizione(Turno turno, String codFunzione){
        Iscrizione iscrizione=null;
        for(Iscrizione i : turno.getIscrizioni()){
            if (i.getFunzione().code.equals(codFunzione)){
                iscrizione=i;
                break;
            }
        }
        return iscrizione;
    }

    /**
     * Determina se un Milite è iscritto a un Turno
     */
    private boolean isIscritto(Turno turno, Milite milite){
        boolean iscritto=false;
        for(Iscrizione iscrizione : turno.getIscrizioni()){
            Milite mIscritto=iscrizione.getMilite();
            if (mIscritto!=null && mIscritto.equals(milite)){
                iscritto=true;
                break;
            }
        }
        return iscritto;
    }





    /**
     * Mostra una notifica generica
     */
    private void notify(String text){
        Notification notification = new Notification(text);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }


    /*
    * Visualizza i dettagli di una iscrizione in read-only
    */
    private void showDetails(Turno turno, Iscrizione iscrizione){
        Milite mil = iscrizione.getMilite();
        String sData=dateService.get(turno.getGiorno(), EATime.completa);
        String sFunzione = iscrizione.getFunzione().getDescrizione();
        StringBuilder sb = new StringBuilder();
        sb.append(mil.getNome()+" "+mil.getCognome());
        sb.append(sData);
        sb.append(sFunzione);

        if (!iscrizione.getInizio().equals(turno.getInizio()) || !iscrizione.getFine().equals(turno.getFine())){
            String h1=dateService.getOrario(iscrizione.getInizio());
            String h2=dateService.getOrario(iscrizione.getFine());
            sb.append("Orario modificato: "+h1+"-"+h2);
        }

        if (!StringUtils.isEmpty(iscrizione.getNote())){
            sb.append(iscrizione.getNote());
        }

        Notification notification = new Notification(sb.toString());
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
        loadDataInGrid();
        grid.setDataProvider(grid.getDataProvider());   // refresh

    }


    // mostra il dettaglio della selezione periodo
    private void showDetail() {

        Map<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, dateService.getISO(startDay));
        mappa.put(KEY_MAP_GIORNO_FINE, dateService.getISO(startDay.plusDays(numDays - 1)));
//        final QueryParameters query = QueryParameters.simple(mappa);
//        getUI().ifPresent(ui -> ui.navigate(TAG_SELEZIONE, query));

        int a = 87;
        int b=a;


    }






}
