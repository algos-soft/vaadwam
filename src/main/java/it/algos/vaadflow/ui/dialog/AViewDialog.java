package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadflow.ui.fields.ATextArea;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 19:10
 * <p>
 * Graficamente abbiamo in tutte (di solito) le XxxDialog:
 * 1) una titolo (obbligatorio)
 * 2) un topPlaceholder (eventuale, presente di default) di tipo HorizontalLayout
 * - con o senza campo edit search, regolato da preferenza o da parametro
 * - con o senza bottone New, regolato da preferenza o da parametro
 * - con eventuali bottoni specifici, aggiuntivi o sostitutivi
 * 3) un alertPlaceholder di avviso (eventuale) con label o altro per informazioni; di norma per il developer
 * 4) un headerGridHolder della Grid (obbligatoria) con informazioni sugli elementi della lista
 * 5) una Grid (obbligatoria); alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio)
 * 6) un bottomPlacehorder della Grid (eventuale) con informazioni sugli elementi della lista; di norma delle somme
 * 7) un bottomPlacehorder (eventuale) con bottoni aggiuntivi
 * 8) un footer (obbligatorio) con informazioni generali
 * <p>
 */
@Slf4j
public abstract class AViewDialog<T extends Serializable> extends Dialog implements IADialog {


    public final static int DURATA = 4000;

    protected final Button saveButton = new Button(REGISTRA);

    protected final Button cancelButton = new Button(ANNULLA);

    protected final Button deleteButton = new Button(DELETE);

    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final Div titlePlaceholder = new Div();

    /**
     * Corpo centrale del Form <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final FormLayout formLayout = new FormLayout();

    /**
     * Corpo centrale del Form <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final FormLayout formSubLayout = new FormLayout();

    /**
     * Barra dei bottoni di comando <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final HorizontalLayout bottomLayout = new HorizontalLayout();

    private final String confirmText = "Conferma";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public LogService logger;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public AAnnotationService annotation;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public AArrayService array;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public ADateService date;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public WamLogin wamLogin;

    /**
     * Placeholder dopo il titolo e prima del form <br>
     * Contenuto eventuale, non presente di default <br>
     * Label o altro per informazioni specifiche; di norma per il developer <br>
     */
    protected VerticalLayout alertPlacehorder;

    protected List<String> alertUser;

    protected List<String> alertAdmin;

    protected List<String> alertDev;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AReflectionService reflection;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Flag di preferenza per usare il bottone Save. Normalmente true.
     */
    protected boolean usaSaveButton;

    /**
     * Flag di preferenza per usare il bottone Cancel. Normalmente true.
     */
    protected boolean usaCancelButton;

    /**
     * Flag di preferenza per usare il bottone delete. Normalmente true.
     */
    protected boolean usaDeleteButton;

    /**
     * Flag di preferenza per le due colonne nel form. Normalmente true.
     */
    protected boolean usaFormDueColonne;

    /**
     * Flag per differenziare i dialoghi di secondo livello, aperti dai primi. Normalmente true.
     */
    protected boolean isDialogoPrimoLivello;

    protected IAService service;

    protected IAPresenter presenter;

    //--collegamento tra i fields e la entityBean
    protected Binder<T> binder;

    protected Class binderClass;

    protected LinkedHashMap<String, AbstractField> fieldMap;


    @Autowired
    protected AFieldService fieldService;

    protected T currentItem;


    protected BiConsumer<T, EAOperation> itemSaver;

    protected AComboBox companyField;


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

    protected EAOperation operation;

    private Consumer<T> itemDeleter;

    private Consumer<T> itemAnnulla;

    private Registration registrationForSave;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Unica per tutta l'applicazione. Usata come libreria. <br>
     */
    @Autowired
    private AVaadinService vaadinService;


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public AViewDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(xxxDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public AViewDialog(IAService service, Class<? extends AEntity> binderClass) {
        this(service, binderClass, true);
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(xxxDialog.class, service, entityClazz, isDialogoPrimoLivello); <br>
     *
     * @param service               business class e layer di collegamento per la Repository
     * @param binderClass           di tipo AEntity usata dal Binder dei Fields
     * @param isDialogoPrimoLivello flag per differenziare i dialoghi di secondo livello, aperti dai primi
     */
    public AViewDialog(IAService service, Class<? extends AEntity> binderClass, boolean isDialogoPrimoLivello) {
        this.service = service;
        this.binderClass = binderClass;
        this.isDialogoPrimoLivello = isDialogoPrimoLivello;
    }// end of constructor


    /**
     * Creazione iniziale (business logic) della view DOPO costruttore, init(), postConstruct() <br>
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     */
    @PostConstruct
    protected void postConstruct() {
        initView();
    }// end of method


    /**
     * Qui va la logica inizale della view <br>
     * Alcuni parametri sarannop regolabili solo dopo il metodo open(() <br>
     */
    protected void initView() {

        //--Login and context della sessione
        fixLoginContext();

        //--Preferenze specifiche di questa view
        fixPreferenze();

        //--Titolo placeholder del dialogo, regolato dopo open()
        if (pref.isBool(USA_TITOLO_FORM)) {
            this.add(creaTitleLayout());
        }// end of if cycle

        //--Costruisce gli oggetti base (placeholder) di questa view
        this.fixLayout();

        //--una o più righe di avvisi - verranno aggiunte dopo open()
        if (alertPlacehorder != null) {
            this.add(alertPlacehorder);
        }

        //--Form placeholder standard per i campi, creati dopo open()
        this.add(creaFormLayout());
        this.add(new H3());
        this.add(creaSubFormLayout());

        //--spazio per distanziare i bottoni dai campi
        this.add(new H3());

        //--Barra placeholder dei bottoni, creati adesso ma regolabili dopo open()
        this.add(creaBottomLayout());

        //--Modalità di chiusura della finestra
        fixClosing();
    }// end of method


    /**
     * Esclude la possibilità di registrare  <br>
     * Dialogo in modalità 'show' <br>
     */
    public void fixConfermaAndNotRegistrazione() {
        this.saveButton.setText(confirmText);
    }// end of method


    /**
     * Regola login and context della sessione <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixLoginContext() {
        context = vaadinService.getSessionContext();
    }// end of method


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        //--Flag di preferenza per usare il bottone Cancel. Normalmente true.
        this.usaCancelButton = pref.isBool(USA_BACK_BUTTON);

        //--Flag di preferenza per usare il bottone Save. Normalmente true.
        usaSaveButton = true;

        //--Flag di preferenza per usare il bottone Delete. Normalmente true.
        usaDeleteButton = true;

        //--Flag di preferenza per le due colonne nel form. Normalmente true.
        usaFormDueColonne = true;

        //--Flag per differenziare i dialoghi di secondo livello, aperti dai primi. Normalmente true.
        isDialogoPrimoLivello = true;


        alertUser = new ArrayList<>();
        alertAdmin = new ArrayList<>();
        alertDev = new ArrayList<>();
    }// end of method


    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    private Component creaTitleLayout() {
        return titlePlaceholder;
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questo dialogo ed inseriti in 'alertPlacehorder' <br>
     * Costruisce gli oggetti base (placeholder) di questa view <br>
     * <p>
     * Chiamato da AViewDialog.initView() <br>
     * Placeholder per  gli avvisi
     */
    protected void fixLayout() {
        alertPlacehorder = new VerticalLayout();
        alertPlacehorder.setMargin(false);
        alertPlacehorder.setSpacing(false);
        alertPlacehorder.setPadding(false);
    }// end of method


    /**
     * Body placeholder per i campi, creati dopo open()
     * Normalmente colonna doppia <br>
     */
    protected Div creaFormLayout() {
        Div div;
        if (usaFormDueColonne) {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("50em", 2));
        } else {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("50em", 1));
        }// end of if/else cycle

        formLayout.addClassName("no-padding");
        div = new Div(formLayout);
        div.addClassName("has-padding");

        return div;
    }// end of method


    /**
     * Body placeholder per un'eventuale spazio sotto il formLayout <br>
     * Colonna singola <br>
     */
    protected Component creaSubFormLayout() {
        Div div;
        formSubLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("50em", 1));

        formSubLayout.addClassName("no-padding");
        div = new Div(formSubLayout);
        div.addClassName("has-padding");

        return div;
    }// end of method


    /**
     * Barra dei bottoni
     */
    protected Component creaBottomLayout() {
        bottomLayout.setClassName("buttons");
        bottomLayout.setPadding(false);
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(false);

        Label spazioVuotoEspandibile = new Label("");
        bottomLayout.add(spazioVuotoEspandibile);

        if (usaCancelButton) {
            cancelButton.addClickListener(e -> close());
            cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
            if (pref.isBool(USA_BUTTON_SHORTCUT) && isDialogoPrimoLivello) {
                cancelButton.addClickShortcut(Key.ARROW_LEFT, KeyModifier.CONTROL);
            }// end of if cycle
            bottomLayout.add(cancelButton);
        }// end of if cycle

        if (usaSaveButton) {
            saveButton.getElement().setAttribute("theme", "primary");
            saveButton.setIcon(new Icon(VaadinIcon.DATABASE));
            //            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            //                saveButton.addClickShortcut(Key.ENTER);
            //            }// end of if cycle
            bottomLayout.add(saveButton);
        }// end of if cycle

        if (usaDeleteButton) {
            deleteButton.addClickListener(e -> deleteClicked());
            deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
            deleteButton.getElement().setAttribute("theme", "error");
            if (pref.isBool(USA_BUTTON_SHORTCUT) && isDialogoPrimoLivello) {
                deleteButton.addClickShortcut(Key.KEY_D, KeyModifier.ALT);
            }// end of if cycle
            bottomLayout.add(deleteButton);
        }// end of if cycle

        bottomLayout.setFlexGrow(1, spazioVuotoEspandibile);
        return bottomLayout;
    }// end of method


    /**
     * Modalità di chiusura della finestra <br>
     */
    private void fixClosing() {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
        addOpenedChangeListener(event -> {
            if (!isOpened()) {
                getElement().removeFromParent();
            }// end of if cycle
        });//end of lambda expressions and anonymous inner class
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     * Crea i fields e visualizza il dialogo <br>
     *
     * @param entityBean        The item to edit; it may be an existing or a newly created instance
     * @param operationProposed The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param itemSaver         funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param itemDeleter       funzione associata al bottone 'delete' (eventuale)
     */
    public void open(final AEntity entityBean, EAOperation operationProposed, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter) {
        open(entityBean, operationProposed, itemSaver, itemDeleter, null);
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     * Crea i fields e visualizza il dialogo <br>
     *
     * @param entityBean        The item to edit; it may be an existing or a newly created instance
     * @param operationProposed The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param itemSaver         funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param itemDeleter       funzione associata al bottone 'delete' (eventuale)
     * @param itemAnnulla       funzione associata al bottone 'annulla' (bottone obbligatorio, azione facoltativa)
     */
    public void open(final AEntity entityBean, EAOperation operationProposed, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter, Consumer<T> itemAnnulla) {
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;
        this.itemAnnulla = itemAnnulla;

        //--controllo iniziale di sicurezza
        if (service == null) {
            return;
        }// end of if cycle
        final EAOperation operationActive;
        this.currentItem = (T) entityBean;

        if (((AService) service).mancaCompanyNecessaria()) {
            Notification.show("Non è stata selezionata nessuna company in AViewDialog.open()", DURATA, Notification.Position.BOTTOM_START);
            return;
        }// end of if cycle

        if (currentItem == null) {
            currentItem = (T) service.newEntity();
            operationActive = EAOperation.addNew;
        } else {
            operationActive = operationProposed;
        }// end of if/else cycle
        this.operation = operationActive;

        if (currentItem == null) {
            Notification.show("Qualcosa non ha funzionato in AViewDialog.open()", DURATA, Notification.Position.BOTTOM_START);
            return;
        }// end of if cycle

        this.fixTitleLayout();

        this.fixAlertLayout();

        if (registrationForSave != null) {
            registrationForSave.remove();
        }
        registrationForSave = saveButton.addClickListener(e -> saveClicked(operationActive));

        //--Controlla la visibilità dei bottoni
        saveButton.setVisible(operation.isSaveEnabled());
        deleteButton.setVisible(operation.isDeleteEnabled());

        //--Crea i fields
        creaFields(entityBean);

        super.open();
    }// end of method


    /**
     * Regola il titolo del dialogo <br>
     * Recupera recordName dalle @Annotation della classe Entity. Non dovrebbe mai essere vuoto. <br>
     * Costruisce il titolo con la descrizione dell'operazione (New, Edit,...) ed il recordName <br>
     * Sostituisce interamente il titlePlaceholder <br>
     */
    protected void fixTitleLayout() {
        String recordName = annotation.getRecordName(binderClass);
        String title = text.isValid(recordName) ? recordName : "Error";

        titlePlaceholder.removeAll();
        titlePlaceholder.add(new H2(operation.getNameInTitle() + " " + title.toLowerCase()));
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questo dialogo ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewDialog.open() <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * DOPO invocare il metodo della superclasse <br>
     */
    protected void fixAlertLayout() {
        if (alertUser != null) {
            for (String alert : alertUser) {
                alertPlacehorder.add(text.getLabelUser(alert));
            }// end of for cycle
        }// end of if cycle

        if (alertAdmin != null) {
            for (String alert : alertAdmin) {
                alertPlacehorder.add(text.getLabelAdmin(alert));
            }// end of for cycle
        }// end of if cycle

        if (alertDev != null) {
            for (String alert : alertDev) {
                alertPlacehorder.add(text.getLabelDev(alert));
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Costruisce nell'ordine una lista di nomi di properties <br>
     * La lista viene usata per la costruzione automatica dei campi e l'inserimento nel binder <br>
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * Sovrasrivibile nella sottoclasse <br>
     * Se serve, modifica l'ordine della lista oppure esclude una property che non deve andare nel binder <br>
     */
    protected List<String> getPropertiesName() {
        return service != null ? service.getFormPropertyNamesList(context) : null;
    }// end of method


    /**
     * Crea i fields
     * <p>
     * Crea un nuovo binder (vuoto) per questo Dialog e questa Entity
     * Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
     * Costruisce una lista di nomi delle properties. Ordinata. Sovrascrivibile.
     * <p>
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge i fields al binder
     * Aggiunge i fields alla mappa fieldMap
     * <p>
     * Aggiunge eventuali fields specifici (costruiti non come standard type) al binder ed alla fieldMap
     * Aggiunge i fields della fieldMap al layout grafico
     * Aggiunge eventuali fields specifici direttamente al layout grafico (senza binder e senza fieldMap)
     * Legge la entityBean ed inserisce nella UI i valori di eventuali fields NON associati al binder
     */
    private void creaFields(AEntity entityBean) {
        List<String> propertyNamesList;

        //--Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
        fieldMap = new LinkedHashMap<>();

        //--Costruisce una lista di nomi delle properties del Form nell'ordine:
        //--1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
        //--2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
        //--3) Sovrascrive la lista nella sottoclasse specifica di xxxService
        propertyNamesList = getPropertiesName();

        //--Crea un nuovo binder (vuoto) per questo Dialog e questa entityBean (currentItem)
        binder = new Binder(binderClass);

        //--Eventuali fields specifici aggiunti PRIMA di quelli automatici
        this.creaFieldsBefore();

        //--Fields normali indicati in @AIForfm(fields =... , aggiunti in automatico
        creaFieldsBase(entityBean, propertyNamesList);

        //--Eventuali fields specifici aggiunti DOPO quelli automatici
        this.creaFieldsAfter();

        //--Eventuali regolazioni aggiuntive ai fields del binder PRIMA di associare i valori
        fixStandardAlgosFieldsAnte();

        //--Costruisce eventuali fields specifici (costruiti non come standard type)
        //--Aggiunge i fields specifici al binder (facoltativo, alcuni fields non funzionano col binder)
        //--Se i fields non sono associati al binder, DEVONO comparire in readSpecificFields()
        //--Aggiunge i fields specifici alla fieldMap (obbligatorio)
        addSpecificAlgosFields();

        //--Aggiunge ogni singolo field della fieldMap al layout grafico
        addFieldsToLayout();

        //--Associa i valori del currentItem al binder. Dal DB alla UI
        binder.readBean(currentItem);

        //--Eventuali regolazioni aggiuntive ai fields del binder DOPO aver associato i valori
        fixStandardAlgosFieldsPost();

        //--Eventuali aggiustamenti finali al layout
        //--Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
        fixLayoutFinal();

        //--Controlla l'esistenza del field company e ne regola i valori
        fixCompanyField();

        //--Regola il focus iniziale
        fixFocus();

        //--Regola in lettura eventuali valori NON associati al binder. Dal DB alla UI
        readSpecificFields();

        //--Regola in lettura l'eeventuale field company (un combo). Dal DB alla UI
        readCompanyField();

        //--aggiunge eventuali listeners ai fields (dopo aver regolato il loro valore iniziale)
        this.addListeners();
    }// end of method


    /**
     * Eventuali fields specifici aggiunti PRIMA di quelli automatici <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void creaFieldsBefore() {
    }// end of method


    /**
     * Costruisce ogni singolo field <br>
     * Fields normali indicati in @AIForfm(fields =... , aggiunti in automatico
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service <br>
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc) <br>
     * Aggiunge il field al binder, nel metodo create() del fieldService <br>
     * Aggiunge il field ad una fieldMap, per recuperare i fields dal nome <br>
     * Controlla l'esistenza tra i field di un eventuale field di tipo textArea. Se NON esiste, abilita il tasto 'return'
     */
    protected void creaFieldsBase(AEntity entityBean, List<String> propertyNamesList) {
        AbstractField propertyField = null;
        boolean esisteTextArea = false;

        for (String propertyName : propertyNamesList) {
            propertyField = fieldService.create(entityBean, appContext, binder, binderClass, propertyName);
            if (propertyField != null) {
                fieldMap.put(propertyName, propertyField);
            }// end of if cycle
            if (propertyField instanceof TextArea) {
                esisteTextArea = true;
            }// end of if cycle
        }// end of for cycle

        if (!esisteTextArea) {
            if (pref.isBool(USA_BUTTON_SHORTCUT) && isDialogoPrimoLivello) {
                saveButton.addClickShortcut(Key.ENTER);
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Eventuali fields specifici aggiunti DOPO quelli automatici <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void creaFieldsAfter() {
    }// end of method


    /**
     * Eventuali regolazioni aggiuntive ai fields del binder PRIMA di associare i valori <br>
     * Sovrascritto nella sottoclasse
     */
    protected void fixStandardAlgosFieldsAnte() {
    }// end of method


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder
     * Aggiunge i fields specifici alla fieldMap
     * Sovrascritto nella sottoclasse
     */
    protected void addSpecificAlgosFields() {
    }// end of method


    /**
     * Aggiunge ogni singolo field della fieldMap al layout grafico
     */
    protected void addFieldsToLayout() {
        getFormLayout().removeAll();
        for (String name : fieldMap.keySet()) {
            getFormLayout().add(fieldMap.get(name));
        }// end of for cycle
    }// end of method


    /**
     * Eventuali regolazioni aggiuntive ai fields del binder DOPO aver associato i valori <br>
     * Sovrascritto nella sottoclasse
     */
    protected void fixStandardAlgosFieldsPost() {
    }// end of method


    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    protected void fixLayoutFinal() {
    }// end of method


    /**
     * Controlla l'esistenza del field company e ne regola i valori
     * Il field company esiste solo se si verificano contemporaneamente i seguenti:
     * 1) l'applicazione usa multiCompany
     * 2) la entity usa company
     * 3) siamo collegati (login) come developer
     */
    protected void fixCompanyField() {
        //        companyField = (AComboBox) getField(AService.FIELD_NAME_COMPANY);
        //        if (companyField != null) {
        //            List items = companyService.findAll();
        //            companyField.setItems(items);
        //            companyField.setEnabled(false);
        //        }// end of if cycle
    }// end of method


    /**
     * Regola il focus iniziale
     * Spazzola tuti i fields e legge le @annotation per vedere se c'è un field col focus
     * Se manca, prende il primo field
     */
    private void fixFocus() {
        boolean hasFocus = false;
        AbstractField field;
        AbstractField firstField = null;

        for (String fieldName : fieldMap.keySet()) {
            if (firstField == null) {
                firstField = fieldMap.get(fieldName);
            }// end of if cycle

            hasFocus = annotation.isFocus(binderClass, fieldName);
            if (hasFocus) {
                field = fieldMap.get(fieldName);
                if (field instanceof ATextField) {
                    ((ATextField) field).focus();
                }// end of if cycle
                if (field instanceof AIntegerField) {
                    ((AIntegerField) field).focus();
                }// end of if cycle
                break;
            }// end of if cycle
        }// end of for cycle

        if (!hasFocus && firstField != null) {
            if (firstField instanceof ATextField) {
                ((ATextField) firstField).focus();
            }// end of if cycle
            if (firstField instanceof AIntegerField) {
                ((AIntegerField) firstField).focus();
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder. <br>
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readSpecificFields() {
    }// end of method


    /**
     * Regola in lettura l'eeventuale field company (un combo)
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readCompanyField() {
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
    }// end of method


    protected void focusOnPost(String currentFieldName) {
        List<String> keys = new ArrayList<>(fieldMap.keySet());
        String nameFocus = "";
        String nameTmp;
        int pos = 0;

        for (int k = 0; k < keys.size(); k++) {
            nameTmp = keys.get(k);
            if (nameTmp.equals(currentFieldName)) {
                pos = keys.indexOf(nameTmp);
                pos++;
                pos = pos < keys.size() ? pos : 0;
                nameFocus = keys.get(pos);
            }// end of if cycle
        }// end of for cycle

        AbstractField field = getField(nameFocus);
        if (field instanceof ATextField) {
            ((ATextField) getField(nameFocus)).focus();
        }// end of if cycle
        if (field instanceof ATextArea) {
            ((ATextArea) getField(nameFocus)).focus();
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge eventuali listeners ai fields che sono stati creati SENZA listeners <br>
     * <p>
     * Chiamato da AViewLDialog.creaFields()<br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void addListeners() {
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Registra
     * Inizio delle operazioni di registrazione
     */
    protected void saveClicked(EAOperation operation) {
        boolean isValid = false;
        if (currentItem != null) {
            //--trasferisce tutti i valori (se accettabili nel loro insieme) dai campi GUI al currentItem
            isValid = binder.writeBeanIfValid(currentItem);
            logger.warn("trasferisce tutti i valori (se accettabili nel loro insieme) dai campi GUI al currentItem", AViewDialog.class, "saveClicked");
        } else {
            logger.error("manca currentItem", AViewDialog.class, "saveClicked");
        }


        if (isValid) {
            writeSpecificFields();
            logger.warn("valid", AViewDialog.class, "saveClicked");
            itemSaver.accept(currentItem, operation);
            close();
        } else {
            logger.error(" non valid", AViewDialog.class, "saveClicked");
            BinderValidationStatus<T> status = binder.validate();
            Notification.show(status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.joining("; ")), 3000, Notification.Position.BOTTOM_START);
        }
    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected void deleteClicked() {
        appContext.getBean(ADeleteDialog.class, currentItem.toString()).open(this::deleteConfirmed);
    }// end of method


    private void deleteConfirmed() {
        itemDeleter.accept(currentItem);
        close();
    }// end of method


    public void close() {
        super.close();

        if (itemAnnulla != null) {
            itemAnnulla.accept(currentItem);
        }// end of if cycle
    }// end of method


    /**
     * Gets the form layout, where additional components can be added for
     * displaying or editing the item's properties.
     *
     * @return the form layout
     */
    protected final FormLayout getFormLayout() {
        return formLayout;
    }// end of method


    /**
     * Gets the binder.
     *
     * @return the binder
     */
    protected final Binder<T> getBinder() {
        return binder;
    }


    /**
     * Gets the item currently being edited.
     *
     * @return the item currently being edited
     */
    protected final T getCurrentItem() {
        return currentItem;
    }


    /**
     * Recupera il field dal nome
     */
    protected AbstractField getField(String publicFieldName) {

        if (fieldMap != null) {
            return fieldMap.get(publicFieldName);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method


    public IAPresenter getPresenter() {
        return presenter;
    }// end of method


    public void setPresenter(IAPresenter presenter) {
        this.presenter = presenter;
    }// end of method


}// end of class
