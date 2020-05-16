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
import it.algos.vaadflow.service.AEnumerationService;
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

import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
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

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected AEnumerationService enumService;


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

    private LinkedHashMap<String, AbstractField> prefMap;


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
        layoutPreferenze = new VerticalLayout();
        layoutPreferenze.setMargin(false);
        layoutPreferenze.setSpacing(false);
        layoutPreferenze.setPadding(false);
        boolean isDeveloper = wamLogin.isDeveloper();
        boolean visibileAdmin = false;
        boolean visibileDev = false;
        prefMap = new LinkedHashMap<>();

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
        AbstractField field = null;

        type = eaWam.getType();
        switch (type) {
            case bool:
                field = new ACheckBox(eaWam.getDesc());
                break;
            case integer:
                layoutPreferenze.add(text.getLabelAdmin(eaWam.getDesc()));
                field = new ATextField();
                ((ATextField) field).addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                ((ATextField) field).setWidth("5em");
                break;
            case localdatetime:

                break;
            case enumeration:
                layoutPreferenze.add(text.getLabelAdmin(eaWam.getDesc()));
                field = new AComboBox();
                ((AComboBox) field).setWidth("14em");
                List items = array.getList(EACancellazione.getValues());
                if (array.isValid(items)) {
                    ((AComboBox) field).setItems(items);
                }// end of if/else cycle

                break;
            default:
                logger.warn("Switch - caso non definito: " + eaWam.name(), this.getClass(), "addSpecificAlgosFields");
                break;
        }

        prefMap.put(eaWam.getCode(), field);
        return field;
    }


    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readSpecificFields() {
        EAPrefType type;
        String key = VUOTA;
        AbstractField field = null;

        for (EAPreferenzaWam eaWam : EAPreferenzaWam.values()) {
            key = eaWam.getCode();

            if (prefMap.get(key) != null) {
                field = prefMap.get(key);
                type = eaWam.getType();
                switch (type) {
                    case bool:
                        field.setValue(pref.isBool(key));
                        break;
                    case integer:
                        field.setValue("" + pref.getInt(key));
                        break;
                    case enumeration:
                        field.setValue(pref.getEnumStr(key));
                        break;
                    default:
                        logger.warn("Switch - caso non definito", this.getClass(), "nomeDelMetodo");
                        break;
                }
            }
        }
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dallla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
        EAPrefType type;
        String key = VUOTA;
        AbstractField field = null;
        String stringEnum;
        List<String> listaEnum;

        Croce croce = (Croce) super.getCurrentItem();
        croce.setPresidente(presidenteTemporaneo);
        croce.setContatto(contattoTemporaneo);
        croce.setIndirizzo(indirizzoTemporaneo);

        for (EAPreferenzaWam eaWam : EAPreferenzaWam.values()) {
            key = eaWam.getCode();

            if (prefMap.get(key) != null) {
                field = prefMap.get(key);
                type = eaWam.getType();
                switch (type) {
                    case bool:
                        pref.saveValue(key, (Boolean) field.getValue());
                        break;
                    case integer:
                        pref.saveValue(key, Integer.parseInt((String) field.getValue()));
                        break;
                    case enumeration:
                        stringEnum = pref.getEnumStr(key);
                        listaEnum = array.getList(stringEnum);

                        //                        genericFieldValue = enumService.convertToModel(mongoEnumValue, (String) field.getValue());
                        //                        pref.setEnum(key, (Integer) field.getValue());
                        field.setValue(pref.getEnumStr(key));
                        break;
                    default:
                        logger.warn("Switch - caso non definito", this.getClass(), "nomeDelMetodo");
                        break;
                }
            }
        }
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