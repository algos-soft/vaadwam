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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadflow.ui.fields.ATextArea;
import it.algos.vaadflow.ui.fields.ATextField;
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
    protected final Div titleLayout = new Div();

    /**
     * Corpo centrale del Form <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final FormLayout formLayout = new FormLayout();

    /**
     * Barra dei bottoni di comando <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final HorizontalLayout bottomLayout = new HorizontalLayout();

//    /**
//     * Corpo centrale del Dialog, alternativo al Form <br>
//     * Placeholder (eventuale, presente di default) <br>
//     */
//    protected final VerticalLayout bodyLayout = new VerticalLayout();

    private final String confirmText = "Conferma";

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    public Consumer<T> itemAnnulla;

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


    protected IAService service;

    protected IAPresenter presenter;

    //--collegamento tra i fields e la entityBean
    protected Binder<T> binder;

    protected Class binderClass;

    protected LinkedHashMap<String, AbstractField> fieldMap;

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

    private String itemType;

    private Registration registrationForSave;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Unica per tutta l'applicazione. Usata come libreria. <br>
     */
    @Autowired
    private AVaadinService vaadinService;


    /**
     * Constructs a new instance.
     *
     * @param presenter per gestire la business logic del package
     */
    public AViewDialog(IAPresenter presenter) {
        this(presenter, null, null);
    }// end of constructor


    /**
     * Constructs a new instance.
     *
     * @param presenter   per gestire la business logic del package
     * @param itemSaver   funzione associata al bottone 'registra'
     * @param itemDeleter funzione associata al bottone 'annulla'
     */
    @Deprecated
    public AViewDialog(IAPresenter presenter, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter) {
        this(presenter, itemSaver, itemDeleter, null, false);
    }// end of constructor


    /**
     * Constructs a new instance.
     *
     * @param presenter               per gestire la business logic del package
     * @param itemSaver               funzione associata al bottone 'registra'
     * @param itemDeleter             funzione associata al bottone 'annulla'
     * @param itemAnnulla             funzione associata al bottone 'annulla'
     * @param confermaSenzaRegistrare cambia il testo del bottone 'Registra' in 'Conferma'
     */
    @Deprecated
    public AViewDialog(IAPresenter presenter, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter, Consumer<T> itemAnnulla, boolean confermaSenzaRegistrare) {
        this.presenter = presenter;
        this.service = presenter.getService();
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;
        this.itemAnnulla = itemAnnulla;
        this.binderClass = presenter.getEntityClazz();
        this.fieldService = presenter.getService().getFieldService();

        if (confermaSenzaRegistrare) {
            this.fixConfermaAndNotRegistrazione();
        }// end of if cycle
    }// end of constructor


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @PostConstruct
    protected void initView() {

        //--Login and context della sessione
        context = vaadinService.getSessionContext();

        //--Le preferenze standard
        fixPreferenze();

        //--Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
        fixPreferenzeSpecifiche();

        //--Titolo placeholder del dialogo, regolato dopo open()
        this.add(creaTitleLayout());

        //--Form placeholder standard per i campi, creati dopo open()
        this.add(creaFormLayout());

        //--spazio per distanziare i bottoni dai campi
        this.add(new H3());

        //--Barra placeholder dei bottoni, creati adesso ma regolabili dopo open()
        this.add(creaBottomLayout());


        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
        addOpenedChangeListener(event -> {
            if (!isOpened()) {
                getElement().removeFromParent();
            }// end of if cycle
        });//end of lambda expressions and anonymous inner class
    }// end of method


    public void fixFunzioni(BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter) {
        fixFunzioni(itemSaver, itemDeleter, null);
    }// end of method


    public void fixFunzioni(BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter, Consumer<T> itemAnnulla) {
        this.itemAnnulla = itemAnnulla;
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;
    }// end of method


    /**
     * Esclude la possibilità di registrare  <br>
     * Dialogo in modalità 'show' <br>
     */
    public void fixConfermaAndNotRegistrazione() {
        this.saveButton.setText(confirmText);
    }// end of method


    /**
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    private void fixPreferenze() {
        //--Flag di preferenza per usare il bottone Cancel. Normalmente true.
        usaCancelButton = true;

        //--Flag di preferenza per usare il bottone Save. Normalmente true.
        usaSaveButton = true;

        //--Flag di preferenza per usare il bottone Delete. Normalmente true.
        usaDeleteButton = true;

        //Flag di preferenza per le due colonne nel form. Normalmente true.
        usaFormDueColonne = true;
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void fixPreferenzeSpecifiche() {
    }// end of method


    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    private Component creaTitleLayout() {
        return titleLayout;
    }// end of method


    /**
     * Body placeholder per i campi, creati dopo open()
     */
    protected Div creaFormLayout() {
        Div div;
        if (usaFormDueColonne) {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("50em", 2));
        } else {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("50em", 1));
        }// end of if/else cycle

        formLayout.addClassName("no-padding");
        div = new Div(formLayout);
        div.addClassName("has-padding");

        return div;
    }// end of method


//    /**
//     * Corpo centrale del Dialog, alternativo al Form <br>
//     * Placeholder (eventuale, presente di default) <br>
//     */
//    private Component creaBodyLayout() {
//        return bodyLayout;
//    }// end of method


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
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                cancelButton.addClickShortcut(Key.ARROW_LEFT);
            }// end of if cycle
            bottomLayout.add(cancelButton);
        }// end of if cycle

        if (usaSaveButton) {
            saveButton.getElement().setAttribute("theme", "primary");
            saveButton.setIcon(new Icon(VaadinIcon.DATABASE));
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                saveButton.addClickShortcut(Key.ENTER);
            }// end of if cycle
            bottomLayout.add(saveButton);
        }// end of if cycle

        if (usaDeleteButton) {
            deleteButton.addClickListener(e -> deleteClicked());
            deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
            deleteButton.getElement().setAttribute("theme", "error");
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                deleteButton.addClickShortcut(Key.KEY_D, KeyModifier.ALT);
            }// end of if cycle
            bottomLayout.add(deleteButton);
        }// end of if cycle

        bottomLayout.setFlexGrow(1, spazioVuotoEspandibile);
        return bottomLayout;
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     */
    @Override
    public void open(AEntity item, EAOperation operation, AContext context) {
        open(item, operation, context, "");
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     * Riceve la entityBean <br>
     * Crea i fields <br>
     *
     * @param entityBean The item to edit; it may be an existing or a newly created instance
     * @param operation  The operation being performed on the item
     * @param context    legato alla sessione
     * @param title      of the window dialog
     */
    @Override
    public void open(AEntity entityBean, EAOperation operation, AContext context, String title) {
        //--controllo iniziale di sicurezza
        if (service == null) {
            return;
        }// end of if cycle

        if (((AService) service).mancaCompanyNecessaria()) {
            Notification.show("Non è stata selezionata nessuna company in AViewDialog.open()", DURATA, Notification.Position.BOTTOM_START);
            return;
        }// end of if cycle
        if (entityBean == null) {
            Notification.show("Qualcosa non ha funzionato in AViewDialog.open()", DURATA, Notification.Position.BOTTOM_START);
            return;
        }// end of if cycle

        this.currentItem = (T) entityBean;
        this.operation = operation;
        this.context = context;
        Object view = presenter.getView();
        if (view != null) {
            this.itemType = presenter.getView().getMenuName();
        }// end of if cycle
        this.fixTitleLayout(title);

        if (registrationForSave != null) {
            registrationForSave.remove();
        }
        registrationForSave = saveButton.addClickListener(e -> saveClicked(operation));

        //--Controlla la visibilità dei bottoni
        saveButton.setVisible(operation.isSaveEnabled());
        deleteButton.setVisible(operation.isDeleteEnabled());

        //--Crea i fields
        creaFields();

        super.open();
    }// end of method


    /**
     * Regola il titolo del dialogo <br>
     */
    protected void fixTitleLayout(String title) {
        title = text.isValid(title) ? title : text.isValid(itemType) ? itemType : "Error";
//        title = title.equals("") ? itemType : title;
        titleLayout.removeAll();
        titleLayout.add(new H2(operation.getNameInTitle() + " " + title.toLowerCase()));
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
    private void creaFields() {
        List<String> propertyNamesList;
        AbstractField propertyField = null;

        //--Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
        fieldMap = new LinkedHashMap<>();

        //--Costruisce una lista di nomi delle properties del Form nell'ordine:
        //--1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
        //--2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
        //--3) Sovrascrive la lista nella sottoclasse specifica di xxxService
        propertyNamesList = getPropertiesName();

        //--Crea un nuovo binder (vuoto) per questo Dialog e questa entityBean (currentItem)
        binder = new Binder(binderClass);

        //--Costruisce ogni singolo field
        //--Aggiunge il field al binder, nel metodo create() del fieldService
        //--Aggiunge il field ad una fieldMap, per recuperare i fields dal nome
        for (String propertyName : propertyNamesList) {
            propertyField = fieldService.create(appContext, binder, binderClass, propertyName);
            if (propertyField != null) {
                fieldMap.put(propertyName, propertyField);
            }// end of if cycle
        }// end of for cycle

        //--Eventuali regolazioni aggiuntive ai fields del binder
        fixStandardAlgosFields();

        //--Costruisce eventuali fields specifici (costruiti non come standard type)
        //--Aggiunge i fields specifici al binder (facoltativo, alcuni fields non funzionano col binder)
        //--Se i fields non sono associati al binder, DEVONO comparire in readSpecificFields()
        //--Aggiunge i fields specifici alla fieldMap (obbligatorio)
        addSpecificAlgosFields();

        //--Aggiunge ogni singolo field della fieldMap al layout grafico
        addFieldsToLayout();

        //--Associa i valori del currentItem al binder. Dal DB alla UI
        binder.readBean(currentItem);

        //--Eventuali aggiustamenti finali al layout
        //--Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
        fixLayout();

        //--Controlla l'esistenza del field company e ne regola i valori
        fixCompanyField();

        //--Regola il focus iniziale
        fixFocus();

        //--Regola in lettura eventuali valori NON associati al binder. Dal DB alla UI
        readSpecificFields();

        //--Regola in lettura l'eeventuale field company (un combo). Dal DB alla UI
        readCompanyField();
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
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    protected void fixStandardAlgosFields() {
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
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    protected void fixLayout() {
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
     * Azione proveniente dal click sul bottone Registra
     * Inizio delle operazioni di registrazione
     */
    protected void saveClicked(EAOperation operation) {
        boolean isValid = false;
        if (currentItem != null) {
            isValid = binder.writeBeanIfValid(currentItem);
        }// end of if cycle

        if (isValid) {
            writeSpecificFields();
            itemSaver.accept(currentItem, operation);
            close();
        } else {
            BinderValidationStatus<T> status = binder.validate();
            Notification.show(status.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")), 3000, Notification.Position.BOTTOM_START);
        }
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Cancella (delete)
     */
    private void deleteClicked() {
//        if (confirmDialog.getElement().getParent() == null) {
//            getUI().ifPresent(ui -> ui.add(confirmDialog));
//        }
        openConfirmDialog();
    }// end of method\


//    /**
//     * Azione proveniente dal click sul bottone Annulla
//     */
//    public void close() {
//        super.close();
////        IAView vista = presenter.getView();
////        if (vista != null) {
////            vista.updateView();
////        }// end of if cycle
////        UI.getCurrent().getPage().reload();
//    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
     * <p>
     * //     * @param title             The title text
     * //     * @param message           Detail message (optional, may be empty)
     * //     * @param additionalMessage Additional message (optional, may be empty)
     */
    protected final void openConfirmDialog() {
        String title = BOT_DELETE;
        String message = "Vuoi veramente cancellare questo elemento ?";
        String additionalMessage = "L'operazione non è reversibile";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, additionalMessage, this::deleteConfirmed);
    }// end of method


    private void deleteConfirmed() {
        itemDeleter.accept(currentItem);
        close();
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
