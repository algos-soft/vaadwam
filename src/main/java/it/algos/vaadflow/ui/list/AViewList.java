package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.footer.AFooter;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.AResetDialog;
import it.algos.vaadflow.ui.dialog.ASearchDialog;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadflow.ui.fields.ATextArea;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadflow.ui.fields.IAField;
import it.algos.vaadflow.wrapper.AFiltro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static it.algos.vaadflow.application.FlowCost.*;

//import com.vaadin.flow.component.applayout.AppLayoutMenu;
//import com.vaadin.flow.component.applayout.AppLayoutMenuItem;


/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 18:49
 * <p>
 * Classe astratta per visualizzare la Grid <br>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewList (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewList con la business logic principale <br>
 * - 3 APrefViewList per regolare le preferenze ed i flags <br>
 * - 4 ALayoutViewList per regolare il layout <br>
 * - 5 AGridViewList per gestire la Grid <br>
 * - 6 APaginatedGridViewList (opzionale) per gestire una Grid specializzata (add-on) che usa le Pagine <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 * <p>
 * La sottoclasse concreta viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le property di questa classe/sottoclasse vengono iniettate (@Autowired) automaticamente se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta <br>
 * 2) sono istanze di una classe SINGLETON, richiamate con AxxService.getInstance() <br>
 * 3) sono annotate @Autowired; sono disponibili SOLO DOPO @PostConstruct <br>
 * <p>
 * Considerato che le sottoclassi concrete NON sono singleton e vengo ri-create ogni volta che dal menu (via @Router)
 * si invocano, è inutile (anche se possibile) usare un metodo @PostConstruct che è sempre un'0appendici di init() del
 * costruttore.
 * Meglio spostare tutta la logica iniziale nel metodo beforeEnter() <br>
 * <p>
 * Le sottoclassi concrete NON hanno le annotation @SpringComponent, @SpringView e @Scope
 * NON annotated with @SpringComponent - Sbagliato perché va in conflitto con la @Route
 * NON annotated with @SpringView - Sbagliato perché usa la Route di VaadinFlow
 * NON annotated with @Scope - Usa @UIScope
 * Annotated with @Route (obbligatorio) per la selezione della vista.
 * <p>
 * Graficamente abbiamo in tutte (di solito) le XxxViewList:
 * 1) una barra di menu (obbligatorio) di tipo IAMenu
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
public abstract class AViewList extends APropertyViewList implements IAView, BeforeEnterObserver, BeforeLeaveObserver, HasUrlParameter<String> {

    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param entityClazz modello-dati specifico di questo modulo
     */
    public AViewList(IAService service, Class<? extends AEntity> entityClazz) {
        this.service = service;
        this.entityClazz = entityClazz;
    }// end of Vaadin/@Route constructor


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Prima viene chiamato il costruttore <br>
     * Prima viene chiamato init(); <br>
     * Viene chiamato @PostConstruct (con qualsiasi firma) <br>
     * Dopo viene chiamato setParameter(); <br>
     * Dopo viene chiamato beforeEnter(); <br>
     * <p>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Creazione e posizionamento dei componenti UI <br>
     * Possono essere sovrascritti nelle sottoclassi <br>
     */
    @PostConstruct
    protected void postConstruct() {
    }// end of method


    /**
     * Regola i parametri del browser per una view costruita da @Route <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l'interfaccia HasUrlParameter implementata in AViewList <br>
     * Chiamato DOPO @PostConstruct ma PRIMA di beforeEnter() <br>
     * Può essere sovrascritto, per gestire diversamente i parametri in ingresso <br>
     * Invocare PRIMA il metodo della superclasse <br>
     *
     * @param event     con la location, ui, navigationTarget, source, ecc
     * @param parameter opzionali nella chiamata del browser
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }// end of method


    /**
     * Creazione iniziale (business logic) della view DOPO costruttore, init(), postConstruct() e setParameter() <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l'interfaccia BeforeEnterObserver implementata in AViewList <br>
     * Chiamato DOPO @PostConstruct e DOPO setParameter() <br>
     * Può essere sovrascritto, per costruire diversamente la view <br>
     * Invocare PRIMA il metodo della superclasse <br>
     *
     * @param beforeEnterEvent con la location, ui, navigationTarget, source, ecc
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        initView();
    }// end of method


    /**
     * Qui va tutta la logica inizale della view <br>
     */
    protected void initView() {
        this.removeAll();
        this.setMargin(false);
        this.setSpacing(false);

        //--Login and context della sessione
        this.mongo = appContext.getBean(AMongoService.class);
        context = vaadinService.getSessionContext();
        login = context != null ? context.getLogin() : null;

        //--se il login è obbligatorio e manca, la View non funziona
        if (vaadinService.mancaLoginSeObbligatorio()) {
            return;
        }// end of if cycle

        //--Preferenze specifiche di questa view
        this.fixPreferenze();

        //--Eventuali regolazioni sulle preferenze DOPO avere invocato il metodo fixPreferenze() della sotoclasse
        this.postPreferenze();

        //--Costruisce gli oggetti base (placeholder) di questa view
        this.fixLayout();

        //--una o più righe di avvisi
        this.add(alertPlacehorder);

        //--barra/menu dei bottoni specifici del modulo
        //--crea i bottoni SENZA i listeners che vengono aggiunti dopo aver recuperato gli items
        this.creaTopLayout();
        if (topPlaceholder.getComponentCount() > 0) {
            this.add(topPlaceholder);
        }// end of if cycle

        //--seconda riga di bottoni-menu sopra la grid (normalmente non si usa)
        if (usaSecondTopPlaceholder) {
            this.add(secondTopPlaceholder);
        }// end of if cycle

        //--body con la Grid
        //--seleziona quale grid usare e la aggiunge al layout
        this.creaBody();
        if (gridPlaceholder.getComponentCount() > 0) {
            this.add(gridPlaceholder);
        }// end of if cycle
        //--aggiunge al layout una (eventuale) legenda-componente di bottoni in basso sotto la grid
        this.creaGridBottomLayout();
        if (usaBottomLayout && bottomPlacehorder.getComponentCount() > 0) {
            this.add(bottomPlacehorder);
        }// end of if cycle

        //--aggiunge il footer standard
        this.add(appContext.getBean(AFooter.class));

        this.addSpecificRoutes();

        //--Crea gli items (righe) della Grid alla prima visualizzazione della view
        this.creaFiltri();

        //--aggiunge tutti i listeners ai bottoni della barra/menu
        this.addListeners();

        updateFiltri();
        this.updateGrid();
    }// end of method


    /**
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }// end of method


    /**
     * Eventuali regolazioni sulle preferenze DOPO avere invocato il metodo fixPreferenze() della sotoclasse <br>
     * <p>
     * Chiamato da AViewList.initView() DOPO fixPreferenze() e sviluppato nella sottoclasse APrefViewList <br>
     * Non può essere sovrascritto <br>
     */
    protected void postPreferenze() {
    }// end of method


    /**
     * Costruisce gli oggetti base (placeholder) di questa view <br>
     * <p>
     * Li aggiunge alla view stessa <br>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Può essere sovrascritto, per modificare il layout standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixLayout() {
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaAlertLayout() {
    }// end of method


    /**
     * Barra dei bottoni SOPRA la Grid inseriti in 'topPlaceholder' <br>
     * <p>
     * In fixPreferenze() si regola quali bottoni mostrare. Nell'ordine: <br>
     * 1) eventuale bottone per cancellare tutta la collezione <br>
     * 2) eventuale bottone di reset per ripristinare (se previsto in automatico) la collezione <br>
     * 3) eventuale bottone New, con testo regolato da preferenza o da parametro <br>
     * 4) eventuale bottone 'Cerca...' per aprire un DialogSearch oppure un campo EditSearch per la ricerca <br>
     * 5) eventuale bottone per annullare la ricerca e riselezionare tutta la collezione <br>
     * 6) eventuale combobox di selezione della company (se applicazione multiCompany) <br>
     * 7) eventuale combobox di selezione specifico <br>
     * 8) eventuali altri bottoni specifici <br>
     * <p>
     * I bottoni vengono creati SENZA listeners che vengono regolati nel metodo addListeners() <br>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaTopLayout() {
    }// end of method


    /**
     * Crea il corpo centrale della view inserito in 'gridPlaceholder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Componente grafico obbligatorio <br>
     * Seleziona quale grid usare e la aggiunge al layout <br>
     * Eventuale barra di bottoni sotto la grid <br>
     */
    protected void creaBody() {
    }// end of method


    /**
     * Crea la grid <br>
     * <p>
     * Chiamato da ALayoutViewList.creaBody() e sviluppato nella sottoclasse AGridViewList <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono calcolati in updateFiltri() e caricati in updateGrid() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     * Se si usa una PaginatedGrid, questa DEVE essere costruita (tipizzata) nella sottoclasse specifica <br>
     */
    protected Grid creaGrid() {
        return null;
    }// end of method


    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi <br>
     * Facoltativo (assente di default) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaGridBottomLayout() {
    }// end of method


    /**
     * Navigazione verso un altra pagina
     */
    protected void routeVerso(String location, AEntity entity) {
        routeVerso(location, entity.getId());
    }// end of method


    /**
     * Navigazione verso un altra pagina
     */
    protected void routeVerso(String location, String idKey) {
        UI ui = null;
        Optional<UI> optional = getUI();

        if (optional.isPresent()) {
            ui = optional.get();
        }// end of if cycle

        if (ui != null) {
            Map<String, String> mappa = new HashMap<>();
            mappa.put("id", idKey);
            QueryParameters query = QueryParameters.simple(mappa);
            ui.navigate(location, query);
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge al menu eventuali @routes specifiche
     * Solo sovrascritto
     */
    protected void addSpecificRoutes() {
    }// end of method


    /**
     * Aggiunge al menu la @route
     */
    protected void addRoute(Class<? extends AViewList> viewClazz) {
        MainLayout mainLayout = context.getMainLayout();
//        if (specificMenuItems != null && specificMenuItems.size() > 0) {
//            specificMenuItems.add(mainLayout.addMenu(viewClazz));
//        }// end of if cycle
    }// end of method


    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
//        AppLayoutMenu appMenu = context.getAppMenu();

        if (dialog != null) {
            dialog.close();
        }// end of if cycle
        if (deleteDialog != null) {
            deleteDialog.close();
        }// end of if cycle
//        if (specificMenuItems != null && specificMenuItems.size() > 0) {
//            for (AppLayoutMenuItem menuItem : specificMenuItems) {
//                appMenu.removeMenuItem(menuItem);
//            }// end of for cycle
//        }// end of if cycle
    }// end of method


    /**
     * Crea la lista dei filtri della Grid alla prima visualizzazione della view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse AGridViewList <br>
     * Chiamato SOLO alla creazione della view. Successive modifiche ai filtri sono gestite in updateFiltri() <br>
     * Può essere sovrascritto, per modificare la selezione dei filtri <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaFiltri() {
    }// end of method


    /**
     * Aggiorna la lista dei filtri della Grid. Modificati per: popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Sviluppato nella sottoclasse AGridViewList <br>
     * Alla prima visualizzazione della view usa SOLO creaFiltri() e non questo metodo <br>
     * Può essere sovrascritto, per modificare la selezione dei filtri <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void updateFiltri() {
    }// end of method


    /**
     * Aggiunge tutti i listeners ai bottoni di 'topPlaceholder' che sono stati creati SENZA listeners <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void addListeners() {
    }// end of method


    /**
     * Sincronizza i filtri. <br>
     * Chiamato dal listener di 'clearFilterButton' <br>
     * <p>
     * Può essere sovrascritto, per modificare la gestione dei filtri <br>
     */
    protected void actionSincroSearch() {
        updateFiltri();
        updateGrid();
        if (buttonClearFilter != null) {
            buttonClearFilter.setEnabled(false);
        }// end of if cycle
    }// end of method


    /**
     * Sincronizza la company in uso. <br>
     * Chiamato dal listener di 'filtroCompany' <br>
     * <p>
     * Può essere sovrascritto, per modificare la gestione delle company <br>
     */
    protected void actionSincroCompany() {
        Company companySelezionata = null;

        if (filtroCompany != null) {
            companySelezionata = (Company) filtroCompany.getValue();
        }// end of if cycle
        login.setCompany(companySelezionata);

        updateFiltri();
        updateGrid();
    }// end of method


    /**
     * Sincronizza i filtri. <br>
     * Chiamato dal listener di 'clearFilterButton' <br>
     * <p>
     * Può essere sovrascritto, per modificare la gestione dei filtri <br>
     */
    public void actionSincroCombo() {
        updateFiltri();
        updateGrid();
    }// end of method


    /**
     * Aggiorna gli items della Grid, utilizzando i filtri. <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Sviluppato nella sottoclasse AGridViewList, oppure APaginatedGridViewList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    public void updateGrid() {
    }// end of method


    protected Button createEditButton(AEntity entityBean) {
        String label = VUOTA;
        String iconaTxt = pref.getStr(ICONA_EDIT_BUTTON);
        if (pref.isBool(FlowCost.USA_TEXT_EDIT_BUTTON)) {
            label = isEntityModificabile ? pref.getStr(FLAG_TEXT_EDIT) : pref.getStr(FLAG_TEXT_SHOW);
        }// end of if cycle

        buttonEdit = new Button(label);
        if (usaRouteFormView) {
            buttonEdit.addClickListener(event -> openForm(entityBean));
        } else {
            buttonEdit.addClickListener(event -> openDialog(entityBean));
        }// end of if/else cycle

//        buttonEdit.setIcon(new Icon("lumo", isEntityModificabile ? "edit" : "search"));
        buttonEdit.setIcon(new Icon("lumo", iconaTxt));
        buttonEdit.addClassName("review__edit");
        buttonEdit.getElement().setAttribute("theme", "tertiary");
        buttonEdit.setHeight("1em");

        return buttonEdit;
    }// end of method


    protected void apreDialogo(ItemDoubleClickEvent evento) {
        AEntity entity;
        if (evento != null) {
            if (evento.getItem().getClass().getName().equals(entityClazz.getName())) {
                entity = (AEntity) evento.getItem();
                if (usaRouteFormView && text.isValid(routeNameFormEdit)) {
                    routeVerso(routeNameFormEdit, entity);
                } else {
                    openDialog(entity);
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Creazione ed apertura del dialogo per una nuova entity <br>
     * Rimanda al metodo openDialog passandolgi un parametro nullo <br>
     */
    protected void openNew() {
        openDialog((AEntity) null);
    }// end of method


    /**
     * Creazione ed apertura del dialogo per una nuova entity oppure per una esistente <br>
     * Il dialogo è PROTOTYPE e viene creato esclusivamente da appContext.getBean(... <br>
     * Nella creazione vengono regolati il service e la entityClazz di riferimento <br>
     * Contestualmente alla creazione, il dialogo viene aperto con l'item corrente (ricevuto come parametro) <br>
     * Se entityBean è null, nella superclasse AViewDialog viene modificato il flag a EAOperation.addNew <br>
     * Si passano al dialogo anche i metodi locali (di questa classe AViewList) <br>
     * come ritorno dalle azioni save e delete al click dei rispettivi bottoni <br>
     * Il metodo DEVE essere sovrascritto <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    @Deprecated
    protected void openDialog(AEntity entityBean) {
    }// end of method


    /**
     * Creazione ed apertura di una view di tipo Form per una nuova entity oppure per una esistente <br>
     * Il metodo DEVE essere sovrascritto e chiamare super.openForm(AEntity entityBean, String formRouteName) <br>
     */
    protected void openFormNew(String formRouteName) {
        openForm(null, formRouteName, EAOperation.addNew);
    }// end of method


    /**
     * Creazione ed apertura di una view di tipo Form per una nuova entity oppure per una esistente <br>
     * Il metodo DEVE essere sovrascritto e chiamare super.openForm(AEntity entityBean, String formRouteName) <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    protected void openForm(AEntity entityBean) {
    }// end of method


    /**
     * Creazione ed apertura di una view di tipo Form per una nuova entity oppure per una esistente <br>
     * <p>
     * La view viene costruita partendo da @Route e non da SprinBoot <br>
     * Nel costruttore vengono regolati il service e la entityClazz di riferimento <br>
     * La chiamata sarà: getUI().ifPresent(ui -> ui.navigate("XxxForm", query)); con il tag della @Route specifica <br>
     * Nel parametro del metodo navigate() si specifica il tag @Route della view <br>
     * Nella mappa della query si specifica il flag EAOperation e l'ID del EntityBean <br>
     * Se entityBean è null, in AViewForm viene modificato il flag a EAOperation.addNew <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    protected void openForm(AEntity entityBean, String formRouteName) {
        openForm(entityBean, formRouteName, EAOperation.showOnly);
    }// end of method


    /**
     * Creazione ed apertura di una view di tipo Form per una nuova entity oppure per una esistente <br>
     * <p>
     * La view viene costruita partendo da @Route e non da SprinBoot <br>
     * Nel costruttore vengono regolati il service e la entityClazz di riferimento <br>
     * La chiamata sarà: getUI().ifPresent(ui -> ui.navigate("XxxForm", query)); con il tag della @Route specifica <br>
     * Nel parametro del metodo navigate() si specifica il tag @Route della view <br>
     * Nella mappa della query si specifica il flag EAOperation e l'ID del EntityBean <br>
     * Se entityBean è null, in AViewForm viene modificato il flag a EAOperation.addNew <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    protected void openForm(AEntity entityBean, String formRouteName, EAOperation operation) {
        HashMap mappa = new HashMap();
        mappa.put(KEY_MAPPA_FORM_TYPE, operation.name());
        if (entityBean!=null) {
            mappa.put(KEY_MAPPA_ENTITY_BEAN, entityBean.id);
        }// end of if cycle
        final QueryParameters query = routeService.getQuery(mappa);
        getUI().ifPresent(ui -> ui.navigate(formRouteName, query));
    }// end of method


    public void updateDopoDialog(AEntity entityBean) {
        this.updateFiltri();
        this.updateGrid();
    }// end of method


    protected void openSearch() {
        if (filtri != null) {
            filtri.clear();
        }// end of if cycle

        searchDialog = appContext.getBean(ASearchDialog.class, service);
        searchDialog.open("", "", this::updateDopoSearch, null);
    }// end of method


    public void updateDopoSearch() {
        LinkedHashMap<String, AbstractField> fieldMap = searchDialog.fieldMap;
        IAField field;
        Object fieldValue = null;

        //--ricerca della parola completa
        for (String fieldName : searchDialog.fieldMap.keySet()) {
            field = (IAField) searchDialog.fieldMap.get(fieldName);
            fieldValue = field.getValore();
            if (field instanceof ATextField || field instanceof ATextArea) {
                if (text.isValid(fieldValue)) {
                    filtri.add(new AFiltro(Criteria.where(fieldName).is(fieldValue)));
                }// end of if cycle
            }// end of if cycle
            if (field instanceof AIntegerField) {
                if ((Integer) fieldValue > 0) {
                    filtri.add(new AFiltro(Criteria.where(fieldName).is(fieldValue)));
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        if (buttonClearFilter != null) {
            buttonClearFilter.setEnabled(true);
        }// end of if cycle

        this.updateGrid();
        creaAlertLayout();
    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmDelete() {
        appContext.getBean(ADeleteDialog.class).open(this::deleteCollection);
    }// end of method


    /**
     * Opens the confirmation dialog before reset all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascrtitto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmReset() {
        appContext.getBean(AResetDialog.class).open(this::reset);
    }// end of method


    /**
     * Cancellazione effettiva (dopo dialogo di conferma) di tutte le entities della collezione. <br>
     * Rimanda al service specifico <br>
     * Azzera gli items <br>
     * Ridisegna la GUI <br>
     */
    public void deleteCollection() {
        service.deleteAll();
        updateFiltri();
        updateGrid();
    }// end of method


    /**
     * Reset effettivo (dopo dialogo di conferma) di tutte le entities della collezione. <br>
     * Rimanda al service specifico <br>
     * Azzera gli items <br>
     * Ridisegna la GUI <br>
     */
    protected void reset() {
        service.reset();
        updateFiltri();
        updateGrid();
    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void save(AEntity entityBean, EAOperation operation) {
        if (service.save(entityBean, operation) != null) {
            updateFiltri();
            updateGrid();
        }// end of if cycle
    }// end of method


    protected void delete(AEntity entityBean) {
        service.delete(entityBean);
        Notification.show(entityBean + " successfully deleted.", 3000, Notification.Position.BOTTOM_START);

        if (usaRefresh) {
            updateFiltri();
            updateGrid();
        }// end of if cycle
    }// end of method


    @Override
    public String getMenuName() {
        return annotation.getMenuName(this.getClass());
    }// end of method

}// end of class
