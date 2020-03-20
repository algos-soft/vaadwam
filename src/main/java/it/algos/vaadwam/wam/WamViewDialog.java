package it.algos.vaadwam.wam;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.AAvvisoService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.turno.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadwam.application.WamCost.TAG_CRO;
import static it.algos.vaadwam.application.WamCost.USA_FIELDS_ENABLED_IN_SHOW;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: lun, 30-lug-2018
 * Time: 15:51
 * Layer intermedio per implementare la sostituzione tra Company e Croce, valida per le classi el progetto Wam
 */
public abstract class WamViewDialog<T extends Serializable> extends AViewDialog {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected CroceService croceService;

    @Autowired
    protected AAvvisoService avvisoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected TurnoService turnoService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    protected WamService wamService;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    protected WamLogin wamLogin;


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public WamViewDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(xxxDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public WamViewDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


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
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (wamLogin.isAdminOrDev()) {
            super.usaDeleteButton = true;
        } else {
            super.usaDeleteButton = false;
        }// end of if/else cycle
    }// end of method


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
        boolean isDeveloper = wamLogin.isDeveloper();
        boolean isAdmin = wamLogin.isAdmin();
        boolean isUser = !isDeveloper && !isAdmin;

        //--solo utente
        if (isUser || isDeveloper) {
            if (alertUser != null) {
                for (Object alert : alertUser) {
                    alertPlacehorder.add(text.getLabelUser((String) alert));
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        //--solo admin
        if (isAdmin || isDeveloper) {
            if (alertAdmin != null) {
                for (Object alert : alertAdmin) {
                    alertPlacehorder.add(text.getLabelAdmin((String) alert));
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        //--solo developer
        if (isDeveloper) {
            if (wamLogin != null && wamLogin.getCroce() != null) {
                if (alertDev != null) {
                    for (Object alert : alertDev) {
                        alertPlacehorder.add(text.getLabelDev((String) alert));
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of if cycle
    }// end of method


    /**
     * Controlla l'esistenza del field company e ne regola i valori
     * Il field company esiste solo se si verificano contemporaneamente i seguenti:
     * 1) l'applicazione usa multiCompany
     * 2) la entity usa company
     * 3) siamo collegati (login) come developer
     */
    protected void fixCompanyField() {
        companyField = (AComboBox) getField("croce");
        if (companyField != null) {
            List items = croceService.findAll();
            companyField.setItems(items);
            companyField.setEnabled(false);
        }// end of if cycle
    }// end of method


    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixLayoutFinal() {
        super.fixLayoutFinal();
        LinkedHashMap<String, AbstractField> fieldMap = this.fieldMap;
        AbstractField fieldOrdine = (AbstractField) this.fieldMap.get("ordine");
        AbstractField fieldCode = (AbstractField) this.fieldMap.get("code");
        AbstractField fieldSigla = (AbstractField) this.fieldMap.get("sigla");

        if (!wamLogin.isDeveloper()) {
            if (wamLogin.isAdmin()) {
                //blocca solo il code e l'ordine
                if (fieldOrdine != null) {
                    fieldOrdine.setEnabled(false);
                }// end of if cycle
                if (fieldCode != null) {
                    fieldCode.setEnabled(false);
                }// end of if cycle
            } else {
                //blocca tutto
                if (!pref.isBool(USA_FIELDS_ENABLED_IN_SHOW)) {
                    for (Map.Entry<String, AbstractField> entry : fieldMap.entrySet()) {
                        entry.getValue().setEnabled(false);
                    }// end of for cycle
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        if (fieldSigla != null) {
            fieldSigla.addValueChangeListener(e -> syncCode());
        }// end of if cycle

    }// end of method


    protected void syncCode() {
        ATextField fieldCode = (ATextField) this.fieldMap.get("code");
        ATextField fieldSigla = (ATextField) this.fieldMap.get("sigla");
        String currentText;

        if (fieldCode == null || fieldSigla == null) {
            return;
        }// end of if cycle

        currentText = fieldSigla.getValue();
        currentText = currentText.toLowerCase();

        //developer cambia sempre mentre admin solo per un nuovo record (code ancora vuoto)
        if (login.isDeveloper() || text.isEmpty(fieldCode.getValue())) {
            fieldCode.setValue(currentText);
        }// end of if cycle

    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     * Crea i fields e visualizza il dialogo <br>
     *
     * @param entityBean        The item to edit; it may be an existing or a newly created instance
     * @param operationProposed The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param itemSaver         funzione associata al bottone 'accetta' ('registra', 'conferma')
     */
    public void openWam(final AEntity entityBean, EAOperation operationProposed, BiConsumer<T, EAOperation> itemSaver) {
        open(entityBean, operationProposed, itemSaver, null);
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
    public void openWam(final AEntity entityBean, EAOperation operationProposed, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter) {
        open(entityBean, operationProposed, itemSaver, itemDeleter, null);
    }// end of method


    /**
     * Regola in lettura l'eeventuale field company (un combo)
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readCompanyField() {
        if (companyField != null) {
            companyField.setValue(((WamEntity) getCurrentItem()).getCroce());
        }// end of if cycle
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    @Override
    protected void writeSpecificFields() {
        super.writeSpecificFields();

        ATextField fieldDesc = (ATextField) this.fieldMap.get("descrizione");
        String currentText;

        if (fieldDesc != null) {
            currentText = fieldDesc.getValue();
            currentText = text.primaMaiuscola(currentText);
            fieldDesc.setValue(currentText);
        }// end of if cycle
    }// end of method


}// end of class
