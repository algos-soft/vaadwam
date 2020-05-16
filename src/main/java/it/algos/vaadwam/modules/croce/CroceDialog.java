package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.tabellone.EACancellazione;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadwam.application.WamCost.TAG_CRO;
import static it.algos.vaadwam.modules.croce.CroceList.NOMI;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 30-set-2018 16.22.05 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_CRO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class CroceDialog extends AViewDialog<Croce> {

    private final static int DURATION = 4000;

    public static String CONTATTO = "contatto";

    public static String INDIRIZZO = "indirizzo";

    protected static String ORGANIZZAZIONE = "organizzazione";

    protected static String PRESIDENTE = "presidente";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

    protected PersonService personService;


    protected Person presidenteTemporaneo;

    protected ATextField presidenteField;


    protected Person contattoTemporaneo;

    protected ATextField contattoField;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    protected WamLogin wamLogin;

    private AComboBox organizzazioneField;

    private AddressService indirizzoService;

    private Address indirizzoTemporaneo;

    private ATextField indirizzoField;

    private VerticalLayout layoutPreferenze = new VerticalLayout();

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public CroceDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * L'istanza DEVE essere creata con appContext.getBean(CroceDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public CroceDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Eventuali messaggi di avviso specifici di questo dialogo ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewDialog.open() <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * DOPO invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertLayout() {
        alertAdmin.add(NOMI);

        super.fixAlertLayout();
    }// end of method


    /**
     * Regola login and context della sessione <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixLoginContext() {
        super.fixLoginContext();

        AContext context = null;
        VaadinSession vaadSession = UI.getCurrent().getSession();

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        if (context != null && context.getLogin() != null) {
            wamLogin = (WamLogin) context.getLogin();
        }// end of if cycle
    }// end of method


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixStandardAlgosFieldsAnte() {
        if (wamLogin.isAdmin()) {
            Object codeField = fieldMap.get("code");
            if (codeField != null) {
                ((AbstractField) codeField).setEnabled(false);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder
     * Aggiunge i fields specifici alla fieldMap
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void addSpecificAlgosFields() {
        EAPrefType type;
        AbstractField propertyField = null;
        layoutPreferenze = new VerticalLayout();
        layoutPreferenze.setMargin(false);
        layoutPreferenze.setSpacing(false);
        layoutPreferenze.setPadding(false);
        boolean isDeveloper = wamLogin.isDeveloper();
        boolean visibileAdmin = false;
        boolean visibileDev = false;

        if (isDeveloper) {
            layoutPreferenze.add(text.getLabelAdmin("Preferenze modificabili (admin)"));
        } else {
            layoutPreferenze.add(text.getLabelAdmin("Preferenze modificabili"));
        }

        for (EAPreferenzaWam eaWam : EAPreferenzaWam.values()) {
            visibileAdmin = eaWam.getShow() == EARole.admin;
            if (eaWam.isCompanySpecifica() && eaWam.isVisibileDialogo() && visibileAdmin) {
                layoutPreferenze.add(fixField(eaWam));
            }
        }
        formSubLayout.add(layoutPreferenze);

        if (isDeveloper) {
            layoutPreferenze.add(text.getLabelDev("Preferenze modificabili (dev)"));

            for (EAPreferenzaWam eaWam : EAPreferenzaWam.values()) {
                visibileDev = eaWam.getShow() == EARole.developer;
                if (eaWam.isCompanySpecifica() && eaWam.isVisibileDialogo() && visibileDev) {
                    layoutPreferenze.add(fixField(eaWam));
                }
            }
            formSubLayout.add(layoutPreferenze);
        }
    }


    protected AbstractField fixField(EAPreferenzaWam eaWam) {
        EAPrefType type;
        AbstractField propertyField = null;

        type = eaWam.getType();
        switch (type) {
            case bool:
                propertyField = new ACheckBox(eaWam.getDesc());
                break;
            case integer:
                layoutPreferenze.add(text.getLabelAdmin(eaWam.getDesc()));
                propertyField = new ATextField();
                ((ATextField) propertyField).addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                ((ATextField) propertyField).setWidth("5em");
                break;
            case localdatetime:

                break;
            case enumeration:
                layoutPreferenze.add(text.getLabelAdmin(eaWam.getDesc()));
                propertyField = new AComboBox();
                ((AComboBox) propertyField).setWidth("14em");
                List items = array.getList(EACancellazione.getValues());
                if (array.isValid(items)) {
                    ((AComboBox) propertyField).setItems(items);
                }// end of if/else cycle

                break;
            default:
                logger.warn("Switch - caso non definito: " + eaWam.name(), this.getClass(), "addSpecificAlgosFields");
                break;
        }

        return propertyField;
    }


    //    /**
    //     * Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel binder
    //     * Sovrascritto
    //     * Dopo aver creato un AField specifico, usare il metodo super.addFieldBinder() per:
    //     * Inizializzare AField
    //     */
    //    @Override
    //    protected void addSpecificAlgosFields() {
    //        personPresenter = StaticContextAccessor.getBean(PersonPresenter.class);
    //        personService = (PersonService) personPresenter.getService();
    //        indirizzoPresenter = StaticContextAccessor.getBean(AddressPresenter.class);
    //        indirizzoService = (AddressService) indirizzoPresenter.getService();
    //
    //        presidenteDialog = StaticContextAccessor.getBean(PersonViewDialog.class);
    //        presidenteDialog.setPresenter(personPresenter);
    //        presidenteDialog.fixFunzioni(this::saveUpdatePre, this::deleteUpdatePre, this::annullaPre);
    //        presidenteDialog.fixConfermaAndNotRegistrazione();
    //        presidenteField = (ATextField) getField(PRESIDENTE);
    //        if (presidenteField != null) {
    //            presidenteField.addFocusListener(e -> presidenteDialog.open(getPresidente(), EAOperation.edit, null, PRESIDENTE));
    //        }// end of if cycle
    //
    //        contattoDialog = StaticContextAccessor.getBean(PersonViewDialog.class);
    //        contattoDialog.setPresenter(personPresenter);
    //        contattoDialog.fixFunzioni(this::saveUpdateCon, this::deleteUpdateCon, this::annullaCon);
    //        contattoDialog.fixConfermaAndNotRegistrazione();
    //        contattoField = (ATextField) getField(CONTATTO);
    //        if (contattoField != null) {
    //            contattoField.addFocusListener(e -> contattoDialog.open(getContatto(), EAOperation.edit, null, CONTATTO));
    //        }// end of if cycle
    //
    //        indirizzoDialog = StaticContextAccessor.getBean(AddressViewDialog.class);
    //        indirizzoDialog.setPresenter(indirizzoPresenter);
    //        indirizzoDialog.fixFunzioni(this::saveUpdateInd, this::deleteUpdateInd, this::annullaInd);
    //        indirizzoDialog.fixConfermaAndNotRegistrazione();
    //        indirizzoField = (ATextField) getField(INDIRIZZO);
    //        if (indirizzoField != null) {
    //            indirizzoField.addFocusListener(e -> indirizzoDialog.open(getIndirizzo(), EAOperation.edit, context));
    //        }// end of if cycle
    //    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readSpecificFields() {
        //        presidenteTemporaneo = getPresidenteCorrente();
        //        presidenteField.setValue(presidenteTemporaneo != null ? presidenteTemporaneo.toString() : "");
        //        contattoTemporaneo = getContattoCorrente();
        //        contattoField.setValue(contattoTemporaneo != null ? contattoTemporaneo.toString() : "");
        //
        //        indirizzoTemporaneo = getIndirizzoCorrente();
        //        indirizzoField.setValue(indirizzoTemporaneo != null ? indirizzoTemporaneo.toString() : "");
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dallla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
        Croce croce = (Croce) super.getCurrentItem();
        croce.setPresidente(presidenteTemporaneo);
        croce.setContatto(contattoTemporaneo);
        croce.setIndirizzo(indirizzoTemporaneo);
    }// end of method


    protected void saveUpdatePre(Person entityBean, EAOperation operation) {
        Croce croce = (Croce) super.getCurrentItem();
        entityBean = (Person) personService.beforeSave(entityBean, operation);
        presidenteTemporaneo = entityBean;

        croce.setPresidente(presidenteTemporaneo);
        presidenteField.setValue(entityBean.toString());

        focusOnPost(PRESIDENTE);
        Notification.show("La modifica di persona è stata confermata ma devi registrare questa croce per renderla definitiva", DURATION, Notification.Position.BOTTOM_START);
    }// end of method


    protected void saveUpdateCon(Person entityBean, EAOperation operation) {
        Croce croce = (Croce) super.getCurrentItem();
        entityBean = (Person) personService.beforeSave(entityBean, operation);
        contattoTemporaneo = entityBean;

        croce.setContatto(contattoTemporaneo);
        contattoField.setValue(entityBean.toString());

        focusOnPost(CONTATTO);
        Notification.show("La modifica di persona è stata confermata ma devi registrare questa croce per renderla definitiva", DURATION, Notification.Position.BOTTOM_START);
    }// end of method


    protected void saveUpdateInd(Address entityBean, EAOperation operation) {
        indirizzoTemporaneo = entityBean;
        indirizzoField.setValue(entityBean.toString());
        focusOnPost(INDIRIZZO);
        Notification.show("La modifica di indirizzo è stata confermata ma devi registrare questa croce per renderla definitiva", DURATION, Notification.Position.BOTTOM_START);
    }// end of method


    protected void deleteUpdatePre(Person entityBean) {
        presidenteTemporaneo = null;
        presidenteField.setValue("");
        focusOnPost(PRESIDENTE);
        Notification.show("La cancellazione di persona è stata confermata ma devi registrare questa croce per renderla definitiva", DURATION, Notification.Position.BOTTOM_START);
    }// end of method


    protected void deleteUpdateCon(Person entityBean) {
        contattoTemporaneo = null;
        contattoField.setValue("");
        focusOnPost(INDIRIZZO);
        Notification.show("La cancellazione di indirizzo è stata confermata ma devi registrare questa croce per renderla definitiva", DURATION, Notification.Position.BOTTOM_START);
    }// end of method


    protected void deleteUpdateInd(Address entityBean) {
        indirizzoTemporaneo = null;
        indirizzoField.setValue("");
        focusOnPost(INDIRIZZO);
        Notification.show("La cancellazione di indirizzo è stata confermata ma devi registrare questa croce per renderla definitiva", DURATION, Notification.Position.BOTTOM_START);
    }// end of method


    protected void annullaPre(Person entityBean) {
        focusOnPost(PRESIDENTE);
    }// end of method


    protected void annullaCon(Person entityBean) {
        focusOnPost(CONTATTO);
    }// end of method


    protected void annullaInd(Address entityBean) {
        focusOnPost(INDIRIZZO);
    }// end of method


    private Person getPresidenteCorrente() {
        Person persona = null;
        Croce croce = (Croce) super.getCurrentItem();

        if (croce != null) {
            persona = croce.getPresidente();
        }// end of if cycle

        return persona;
    }// end of method


    private Person getContattoCorrente() {
        Person persona = null;
        Croce croce = (Croce) super.getCurrentItem();

        if (croce != null) {
            persona = croce.getContatto();
        }// end of if cycle

        return persona;
    }// end of method


    private Address getIndirizzoCorrente() {
        Address indirizzo = null;
        Croce croce = (Croce) super.getCurrentItem();

        if (croce != null) {
            indirizzo = croce.getIndirizzo();
        }// end of if cycle

        return indirizzo;
    }// end of method


    private Person getPresidente() {
        Person persona = getPresidenteCorrente();

        if (persona == null) {
            persona = personService.newEntity();
        }// end of if cycle

        return persona;
    }// end of method


    private Person getContatto() {
        Person persona = getContattoCorrente();

        if (persona == null) {
            persona = personService.newEntity();
        }// end of if cycle

        return persona;
    }// end of method


    private Address getIndirizzo() {
        Address indirizzo = getIndirizzoCorrente();

        if (indirizzo == null) {
            indirizzo = (Address) indirizzoService.newEntity();
        }// end of if cycle

        return indirizzo;
    }// end of method


}// end of class