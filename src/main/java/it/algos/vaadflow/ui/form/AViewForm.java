package it.algos.vaadflow.ui.form;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.stream.Collectors;

import static it.algos.vaadflow.application.FlowCost.KEY_MAPPA_ENTITY_BEAN;
import static it.algos.vaadflow.application.FlowCost.KEY_MAPPA_FORM_TYPE;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 10-apr-2020
 * Time: 05:51
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
 * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
 * La chiamata sarà: getUI().ifPresent(ui -> ui.navigate("XxxForm", query)); con il tag della @Route specifica <br>
 * La injection viene fatta da @Route nel costruttore <br>
 * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
 * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
 * 1) viene chiamato il costruttore (da @Route) <br>
 * 2) viene eseguito l'init(); del costruttore <br>
 * 3) viene chiamato @PostConstruct da SpringBoot (con qualsiasi firma) <br>
 * 4) viene chiamato setParameter(); <br>
 * 5) viene chiamato beforeEnter(); <br>
 * <p>
 * La sottoclasse concreta viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le property di questa classe/sottoclasse vengono iniettate (@Autowired) automaticamente se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta <br>
 * 2) sono istanze di una classe SINGLETON, richiamate con AxxService.getInstance() <br>
 * 3) sono annotate @Autowired; ricorda che sono disponibili SOLO DOPO @PostConstruct <br>
 * <p>
 * Considerato che le sottoclassi concrete NON sono singleton e vengo ri-create ogni volta che dal menu (via @Router)
 * si invocano, è inutile (anche se possibile) usare un metodo @PostConstruct che è sempre un'appendice di init() del
 * costruttore.
 * Meglio spostare tutta la logica iniziale nel metodo beforeEnter() <br>
 * <p>
 * Le sottoclassi concrete NON hanno le annotation @SpringComponent, @SpringView e @Scope
 * NON annotated with @SpringComponent - Sbagliato perché va in conflitto con la @Route
 * NON annotated with @SpringView - Sbagliato perché usa la Route di VaadinFlow
 * NON annotated with @Scope - Sbagliato perché usa @UIScope
 * Annotated with @Route (obbligatorio) per la selezione della vista.
 * <p>
 * Graficamente abbiamo in tutte (di solito) le XxxViewForm:
 * 1) un titolo (eventuale, presente di default) di tipo Label o HorizontalLayout
 * 2) un alertPlaceholder di avviso (eventuale) con label o altro per informazioni; di norma per il developer
 * 3) un Form (obbligatorio);
 * 4) un bottomPlacehorder (obbligatorio) con i bottoni di navigazione, conferma, cancella
 * 5) un footer (obbligatorio) con informazioni generali
 * <p>
 * Le injections vengono fatta da SpringBoot nel metodo @PostConstruct DOPO init() automatico
 * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
 * <p>
 * Annotation @Route(value = "") per la vista iniziale - Ce ne può essere solo una per applicazione
 * ATTENZIONE: se rimangono due (o più) classi con @Route(value = ""), in fase di compilazione appare l'errore:
 * -'org.springframework.context.ApplicationContextException:
 * -Unable to start web server;
 * -nested exception is org.springframework.boot.web.server.WebServerException:
 * -Unable to start embedded Tomcat'
 * <p>
 * Usa l'interfaccia HasUrlParameter col metodo setParameter(BeforeEvent event, ...) per ricevere parametri opzionali
 * anche per chiamate che usano @Route <br>
 * Usa l'interfaccia BeforeEnterObserver col metodo beforeEnter()
 * invocato da @Route al termine dell'init() di questa classe e DOPO il metodo @PostConstruct <br>
 * <p>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public abstract class AViewForm extends APropertyViewForm implements BeforeEnterObserver, HasUrlParameter<String> {


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public AViewForm(IAService service, Class<? extends AEntity> binderClass) {
        this.service = service;
        this.binderClass = binderClass;
    }// end of Vaadin/@Route constructor


    /**
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * La injection viene fatta da @Route nel costruttore <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * 1) viene chiamato il costruttore da @Route <br>
     * 2) viene eseguito l'init() del costruttore <br>
     * 3) viene chiamato @PostConstruct da SpringBoot (con qualsiasi firma) <br>
     * 4) viene chiamato setParameter() da @Route <br>
     * 5) viene chiamato beforeEnter() da @Route <br>
     * <p>
     * Considerato che le sottoclassi concrete NON sono singleton e vengo ri-create ogni volta che dal menu (via @Router)
     * si invocano, è inutile (anche se possibile) usare un metodo @PostConstruct che è sempre un'appendice di init() del
     * costruttore.
     * Meglio spostare tutta la logica iniziale nel metodo beforeEnter() <br>
     */
    @PostConstruct
    protected void postConstruct() {
    }// end of method


    /**
     * Creazione iniziale (business logic) della view DOPO costruttore, init(), postConstruct() e setParameter() <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l'interfaccia BeforeEnterObserver implementata in AViewForm <br>
     * Chiamato DOPO @PostConstruct e DOPO setParameter() <br>
     * Può essere sovrascritto, per costruire diversamente la view <br>
     * Invocare PRIMA il metodo della superclasse <br>
     *
     * @param beforeEnterEvent con la location, ui, navigationTarget, source, ecc
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.fixLoginContext();
        this.fixPreferenze();
        this.fixProperties();
        this.fixPropertyNamesList();
        this.initView();
        this.creaAllFields();
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
     * Preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    protected void fixPreferenze() {
    }// end of method


    /**
     * Regola alcune properties (non grafiche) <br>
     */
    protected void fixProperties() {
    }// end of method


    /**
     * Costruisce una lista ordinata di nomi di properties <br>
     * <p>
     * La lista viene usata per la costruzione automatica dei campi e l'inserimento nel binder <br>
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * Sovrasrivibile nella sottoclasse <br>
     * Se serve, modifica l'ordine della lista oppure esclude una property che non deve andare nel binder <br>
     */
    protected void fixPropertyNamesList() {
        switch (operationForm) {
            case addNew:
                propertyNamesList = service != null ? service.getFormPropertyNamesListNew(context) : null;
                break;
            case edit:
            case editNoDelete:
            case editDaLink:
                propertyNamesList = service != null ? service.getFormPropertyNamesListEdit(context) : null;
                break;
            case showOnly:
                propertyNamesList = service != null ? service.getFormPropertyNamesListShow(context) : null;
                break;
            default:
                logger.warn("Switch - caso non definito");
                break;
        } // end of switch statement
    }// end of method


    /**
     * Qui va tutta la logica grafica della view <br>
     * <p>
     * Graficamente abbiamo in tutte (di solito) le XxxViewForm: <br>
     * 1) un titolo (eventuale, presente di default) di tipo Label o HorizontalLayout <br>
     * 2) un alertPlaceholder di avviso (eventuale) con label o altro per informazioni; di norma per il developerv <br>
     * 3) un Form (obbligatorio); <br>
     * 4) un bottomPlacehorder (obbligatorio) con i bottoni di navigazione, conferma, cancella <br>
     * 5) un footer (obbligatorio) con informazioni generali <br>
     */
    protected void initView() {
    }// end of method


    /**
     * Crea i fields
     * <p>
     * Crea un nuovo binder (vuoto) per questa View e questa Entity
     * Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
     * Costruisce una lista di nomi delle properties. Ordinata. Sovrascrivibile.
     * <p>
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge alla mappa (ordinata) eventuali fields specifici PRIMA di quelli automatici
     * Aggiunge i fields al binder
     * Aggiunge alla mappa (ordinata)  eventuali fields specifici DOPO quelli automatici
     * Aggiunge i fields alla mappa fieldMap
     * <p>
     * Aggiunge eventuali fields specifici (costruiti non come standard type) al binder ed alla fieldMap
     * Aggiunge i fields della fieldMap al layout grafico
     * Aggiunge eventuali fields specifici direttamente al layout grafico (senza binder e senza fieldMap)
     * Legge la entityBean ed inserisce nella UI i valori di eventuali fields NON associati al binder
     */
    protected void creaAllFields() {
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Edit
     * Apre una View in Edit mode <br>
     */
    protected void modifica() {
        HashMap mappa = new HashMap();
        mappa.put(KEY_MAPPA_FORM_TYPE, EAOperation.edit.name());
        mappa.put(KEY_MAPPA_ENTITY_BEAN, entityBean.id);
        final QueryParameters query = routeService.getQuery(mappa);
        getUI().ifPresent(ui -> ui.navigate("betaForm", query));
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder. Dalla UI al DB <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected boolean writeSpecificFields() {
        return true;
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Registra
     * Inizio delle operazioni di registrazione
     */
    protected void saveClicked() {
        boolean isValid = false;
        if (entityBean != null) {
            //--trasferisce tutti i valori (se accettabili nel loro insieme) dai campi GUI al currentItem
            isValid = binder.writeBeanIfValid(entityBean);
        }// end of if cycle

        if (isValid) {
            if (writeSpecificFields()) {
                service.save(entityBean, EAOperation.edit);
                ritorno();
            }// end of if cycle
        } else {
            BinderValidationStatus<AEntity> status = binder.validate();
            Notification.show(status.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")), 3000, Notification.Position.BOTTOM_START);
        }
    }// end of method

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


    /**
     * Torna alla view precedente <br>
     */
    protected void ritorno() {
        History history = UI.getCurrent().getPage().getHistory();
        history.back();
        if (operationForm == EAOperation.edit) {
            history.back();
        }// end of if cycle
    }// end of method

}// end of class
