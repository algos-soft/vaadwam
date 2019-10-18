package it.algos.vaadflow.ui.list;

//import com.vaadin.flow.component.applayout.AppLayoutMenuItem;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.CompanyService;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.ASearchDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.TAG_COM;
import static it.algos.vaadflow.application.FlowCost.TAG_LOGIN;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:06
 * <p>
 * Superclasse di servizio per separare le property di AViewList in una classe 'dedicata' <br>
 * Alleggerisce la 'lettura' della sottoclasse principale <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi <br>
 */
public abstract class APropertyViewList extends VerticalLayout {


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation = AAnnotationService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AColumnService columnService = AColumnService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ADateService date = ADateService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AFieldService field = AFieldService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AReflectionService reflection = AReflectionService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    public AMongoService mongo;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UtenteService utenteService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    protected CompanyService companyService;


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     */
    protected IAPresenter presenter;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     */
    protected IADialog dialog;

    //--property
    protected ASearchDialog searchDialog;

    //--property
    protected TextField searchField;

    //--property
    protected String searchProperty;

    protected Button deleteAllButton;

    protected Button resetButton;

    protected Button clearFilterButton;

    protected Button searchButton;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Il service viene recuperato dal presenter, <br>
     * La repository è gestita direttamente dal service <br>
     */
    protected IAService service;

    /**
     * Modello-dati specifico <br>
     */
    protected Class<? extends AEntity> entityClazz;

    /**
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, presente di default <br>
     * - con o senza un bottone per cancellare tutta la collezione
     * - con o senza un bottone di reset per ripristinare (se previsto in automatico) la collezione
     * - con o senza gruppo di ricerca:
     * -    campo EditSearch predisposto su un unica property, oppure (in alternativa)
     * -    bottone per aprire un DialogSearch con diverse property selezionabili
     * -    bottone per annullare la ricerca e riselezionare tutta la collezione
     * -    eventuale Popup di selezione, filtro e ordinamento
     * - con o senza bottone New, con testo regolato da preferenza o da parametro <br>
     * - con eventuali altri bottoni specifici <br>
     */
    protected HorizontalLayout topPlaceholder;

    /**
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, non presente di default <br>
     * Label o altro per informazioni specifiche; di norma per il developer <br>
     */
    protected VerticalLayout alertPlacehorder;

    /**
     * Label (obbligatoria)  che appare nell'header della Grid. <br>
     * Informazioni sugli elementi della lista <br>
     */
    protected Label headerGridHolder;

    /**
     * Placeholder per la Grid dichiarata nella superclasse oppure <br>
     * per la PaginatedGrid che deve essere dichiarata nella sottoclasse specifica <br>
     * Esiste o una o l'altra (a seconda del flag della sottoclasse) <br>
     */
    protected VerticalLayout gridPlaceholder;

    /**
     * Griglia principale SENZA paginazione <br>
     * Alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio) <br>
     */
    protected Grid grid;

    /**
     * Placeholder SOTTO la Grid <br>
     * Eventuali bottoni aggiuntivi <br>
     */
    protected HorizontalLayout bottomPlacehorder;

    /**
     * Flag di preferenza per usare la ricerca e selezione nella barra dei menu. <br>
     * Se è true, un altro flag seleziona il textField o il textDialog <br>
     * Se è true, il bottone usaAllButton è sempre presente <br>
     * Se è true, un altro flag seleziona la presenza o meno del Popup di filtro <br>
     * Normalmente true. <br>
     */
    protected boolean usaSearch;

    /**
     * Flag di preferenza per selezionare la ricerca:
     * true per aprire un dialogo di ricerca e selezione su diverse properties <br>
     * false per presentare un textEdit predisposto per la ricerca su un unica property <br>
     * Normalmente true.
     */
    protected boolean usaSearchDialog;

    /**
     * Flag di preferenza per usare il popup di selezione, filtro e ordinamento situato nella searchBar.
     * Normalmente false <br>
     */
    protected boolean usaPopupFiltro;

    /**
     * Flag di preferenza per limitare le righe della Grid e mostrarle a gruppi (pagine). Normalmente true. <br>
     */
    protected boolean usaPagination;

//    /**
//     * Flag di preferenza per la soglia di elementi che fanno scattare la pagination. <br>
//     * Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 50 <br>
//     */
//    protected int sogliaPagination;

    /**
     * Flag per costruire una Grid normale o una PaginatedGrid. <br>
     * Viene regolato da codice. <br>
     */
    protected boolean isPaginata;

    /**
     * Flag di preferenza per usare il bottone new situato nella topLayout. Normalmente true. <br>
     */
    protected boolean usaBottoneNew;

    protected AComboBox filtroComboBox;

    protected AComboBox filtroCompany;

    protected Button newButton;

    /**
     * Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false. <br>
     */
    protected boolean usaTopAlert;

    /**
     * Flag di preferenza per la Label nell'header della Grid grid. Normalmente true. <br>
     */
    protected boolean usaHaederGrid;

    /**
     * Flag di preferenza per mostrare una caption sopra la grid. Normalmente true. <br>
     */
    @Deprecated
    protected boolean usaCaption;

    /**
     * Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true. <br>
     */
    protected boolean usaBottoneEdit;

    /**
     * Flag di preferenza per usare il placeholder di bottoni aggiuntivi sotto la Grid. Normalmente false. <br>
     */
    protected boolean usaBottomLayout;

    /**
     * Flag di preferenza per cancellare tutti gli elementi. Normalmente false. <br>
     */
    protected boolean usaBottoneDeleteAll;

    /**
     * Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false. <br>
     */
    protected boolean usaBottoneReset;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false. <br>
     */
    protected boolean isEntityDeveloper;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false. <br>
     */
    protected boolean isEntityAdmin;

    /**
     * Flag di preferenza per modificare la entity. Normalmente true. <br>
     */
    protected boolean isEntityModificabile;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false. <br>
     */
    protected boolean isEntityEmbedded;

    /**
     * Flag di preferenza se si caricano dati demo alla creazione. Resettabili. Normalmente false. <br>
     */
    protected boolean isEntityUsaDatiDemo;

    /**
     * Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true. <br>
     */
    protected boolean usaRefresh;

    /**
     * Flag di preferenza per selezionare il numero di righe visibili della Grid. <br>
     * Normalmente limit = pref.getInt(FlowCost.MAX_RIGHE_GRID). <br>
     */
    protected int limit;

    /**
     * Flag per la larghezza della Grid. Default a 100. Espressa come numero per comodità; poi viene convertita in "em". <br>
     */
    protected int gridWith;

    /**
     * Istanza (@VaadinSessionScope) inietta da Spring ed unica nella sessione <br>
     */
    @Autowired
    @Qualifier(TAG_LOGIN)
    protected ALogin login;

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

    protected int offset;

//    protected boolean isPagination;

    protected Collection items;

    protected ADeleteDialog deleteDialog;

//    protected ArrayList<AppLayoutMenuItem> specificMenuItems = new ArrayList<AppLayoutMenuItem>();

    /**
     * Flag di preferenza per usare una route view come detail della singola istanza. Normalmente true. <br>
     * In alternativa si può usare un Dialog.
     */
    protected boolean usaRouteFormView;

    /**
     * Nome della route per la location della pagina di modifica (standard) del Form <br>
     */
    protected String routeNameFormEdit;


    /**
     * Singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected String singleParameter;


    /**
     * Mappa chiave-valore di un singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, String> parametersMap = null;


    /**
     * Mappa chiave-valore di alcuni parametri (opzionali) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewList.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, List<String>> multiParametersMap = null;

}// end of class
