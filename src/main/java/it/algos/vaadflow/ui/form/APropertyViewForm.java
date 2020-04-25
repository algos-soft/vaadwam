package it.algos.vaadflow.ui.form;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 10-apr-2020
 * Time: 06:05
 * Classe astratta per visualizzare il Form <br>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewForm (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewForm con la business logic principale <br>
 * - 3 APrefViewList per regolare i parametri, le preferenze ed i flags <br>
 * - 4 ALayoutViewForm per regolare il layout <br>
 * - 5 AFieldsViewForm per gestire i Fields <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 * <p>
 * Superclasse di servizio per separare le property di AViewForm in una classe 'dedicata' <br>
 * Alleggerisce la 'lettura' della sottoclasse principale <br>
 * Le property sono regolarmente disponibili in AViewForm ed in tutte le sue sottoclassi <br>
 */
public abstract class APropertyViewForm extends VerticalLayout {

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
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected AFieldService fieldService =AFieldService.getInstance();

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    public AMongoService mongo;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
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
    public ARouteService routeService = ARouteService.getInstance();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;


    /**
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il service viene recuperato dal costruttore della classe concreta <br>
     * Nel costruttore della sottoclasse concreta (XxxForm) si usa un @Qualifier(),
     * - per avere la sottoclasse specifica (XxxService) <br>
     * Nella costruttore della sottoclasse concreta (XxxForm) si usa una costante statica (FlowCost),
     * - per scrivere sempre uguali i riferimenti <br>
     * <p>
     * Business class e layer di collegamento per la Repository <br>
     * La Repository è gestita direttamente dal service specifico <br>
     */
    protected IAService service;

    /**
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il modello-dati viene recuperato dal costruttore della classe concreta <br>
     * <p>
     * Modello-dati specifico di questo modulo <br>
     */
    protected Class<? extends AEntity> binderClass;


    //--collegamento tra i fields e la entityBean
    protected Binder binder;


    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;


    /**
     * Lista ordinata di nomi di properties <br>
     */
    protected List<String> propertyNamesList;

    /**
     * Singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewForm.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected String singleParameter;


    /**
     * Mappa chiave-valore di un singolo parametro (opzionale) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewForm.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, String> parametersMap = null;


    /**
     * Mappa chiave-valore di alcuni parametri (opzionali) in ingresso nella chiamata del browser (da @Route oppure diretta) <br>
     * Si recupera nel metodo AViewForm.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected Map<String, List<String>> multiParametersMap = null;

    /**
     * Tipologia di Form in uso <br>
     * Si recupera nel metodo AViewForm.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected EAOperation operationForm;

    /**
     * Current item <br>
     * Si recupera nel metodo AViewForm.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected AEntity entityBean;

    /**
     * Mappa ordinata di tutti i filds del form <br>
     * Serve per presentarli (ordinati) dall'alto in basso nel form <br>
     */
    protected LinkedHashMap<String, AbstractField> fieldMap;

    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected Div titlePlaceholder;

    /**
     * Label o altro per informazioni specifiche; di norma per il developer <br>
     * Placeholder (eventuale, non presente di default) <br>
     */
    protected VerticalLayout alertPlacehorder;

    /**
     * Corpo centrale del Form <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected Div bodyPlaceHolder;

    /**
     * Corpo centrale aggiuntivo del Form <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected FormLayout bodySubPlaceHolder;

    /**
     * Barra dei bottoni di comando <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected HorizontalLayout bottomPlacehorder;


    /**
     * Lista di avvisi da mostrare nel alertPlacehorder SOTTO il titolo e SOPRA il Form. <br>
     * Opzionale. Visibile a tutti. Di colore verde.
     */
    protected List<String> alertUser;

    /**
     * Lista di avvisi da mostrare nel alertPlacehorder SOTTO il titolo e SOPRA il Form. <br>
     * Opzionale. Visibile agli admin. Di colore blue.
     */
    protected List<String> alertAdmin;

    /**
     * Lista di avvisi da mostrare nel alertPlacehorder SOTTO il titolo e SOPRA il Form. <br>
     * Opzionale. Visibile ai developer. Di colore rosso.
     */
    protected List<String> alertDev;

    /**
     * Flag di preferenza per mostrare il titolo del Form. Normalmente true. <br>
     */
    protected boolean usaTitoloForm;

    /**
     * Valore titolo del Form. Regolato nella sottoclasse concreta. <br>
     */
    protected String titoloForm;

    /**
     * Flag di preferenza per realizzare il Form su due colonne. Normalmente true. <br>
     */
    protected boolean usaFormDueColonne;

    /**
     * Preferenza per la larghezza 'minima' del Form. Normalmente "50em". <br>
     */
    protected String minWidthForm;


    /**
     * Back (obbligatorio) <br>
     * Ritorna, con history.back(),  alla view precedente che ha invocato, tramite @Route, questa view <br>
     * Bottone di comando posto nella barra inferiore della scheda <br>
     */
    protected Button backButton;

    /**
     * Cancel (facoltativo) <br>
     * Cancella le modifiche effettuate <br>
     * Bottone di comando posto nella barra inferiore della scheda <br>
     */
    protected Button cancelButton;

    /**
     * Annulla (facoltativo) <br>
     * Annulla un'operazione iniziate <br>
     * Bottone di comando posto nella barra inferiore della scheda <br>
     */
    protected Button annullaButton;

    /**
     * Modifica (facoltativo) <br>
     * Apre una view in modifica <br>
     * Bottone di comando posto nella barra inferiore della scheda <br>
     */
    protected Button editButton;

    /**
     * Registra (facoltativo) <br>
     * Registra sul mongoDB i valori della UI <br>
     * Bottone di comando posto nella barra inferiore della scheda <br>
     */
    protected Button saveButton;

    /**
     * Delete (facoltativo) <br>
     * Cancella dal mongoDB la entity corrente <br>
     * Bottone di comando posto nella barra inferiore della scheda <br>
     */
    protected Button deleteButton;


    /**
     * Flag di preferenza per usare il bottone Back. Normalmente true.
     */
    protected boolean usaBackButton;

    /**
     * Flag di preferenza per usare il bottone Cancel. Normalmente false.
     */
    protected boolean usaCancelButton;

    /**
     * Flag di preferenza per usare il bottone Annulla. Normalmente false.
     */
    protected boolean usaAnnullaButton;

    /**
     * Flag di preferenza per usare il bottone Modifica. Normalmente false.
     */
    protected boolean usaEditButton;

    /**
     * Flag di preferenza per usare il bottone Registra. Normalmente true.
     */
    protected boolean usaSaveButton;

    /**
     * Flag di preferenza per usare il bottone Delete. Normalmente false.
     */
    protected boolean usaDeleteButton;


    /**
     * Flag di preferenza col testo del bottone Back. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String backButtonText;

    /**
     * Flag di preferenza col testo del bottone Cancel. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String cancelButtonText;

    /**
     * Flag di preferenza col testo del bottone Annulla. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String annullaButtonText;

    /**
     * Flag di preferenza col testo del bottone Modifica. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String editButtonText;

    /**
     * Flag di preferenza col testo del bottone Registra. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String saveButtonText;

    /**
     * Flag di preferenza col testo del bottone Delete. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String deleteButtonText;

    /**
     * Flag di preferenza per l'icona del bottone Back. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String backButtonIcon;

    /**
     * Flag di preferenza col testo del bottone Cancel. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String cancelButtonIcon;

    /**
     * Flag di preferenza col testo del bottone Annulla. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String annullaButtonIcon;

    /**
     * Flag di preferenza col testo del bottone Modifica. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String editButtonIcon;

    /**
     * Flag di preferenza col testo del bottone Registra. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String saveButtonIcon;

    /**
     * Flag di preferenza col testo del bottone Delete. Normalmente regolato in AViewForm.fixPreferenze().
     */
    protected String deleteButtonIcon;

    /**
     * Form centrale (obbligatorio) con i fields normali.
     */
    protected FormLayout formLayout;

    /**
     * Form aggiuntivo (opzionale) per altre regolazioni
     */
    protected FormLayout formSubLayout;


}// end of class
