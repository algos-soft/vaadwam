package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.footer.AFooter;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.AResetDialog;
import it.algos.vaadflow.ui.dialog.ASearchDialog;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadflow.ui.fields.ATextArea;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadflow.ui.fields.IAField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import javax.annotation.PostConstruct;
import java.util.*;

import static it.algos.vaadflow.application.FlowCost.FLAG_TEXT_EDIT;
import static it.algos.vaadflow.application.FlowCost.FLAG_TEXT_SHOW;

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
 * La classe viene divisa verticalmente in alcune classi, per 'leggerla' meglio (era troppo grossa) <br>
 * - 1 superclasse (APropertyViewList) <br>
 * - 3 sottoclassi (AGridViewList, ALayoutViewList e APrefViewList) <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 * <p>
 * La sottoclasse concreta viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le property di questa classe/sottoclasse vengono iniettate (@Autowired) automaticamente se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta <br>
 * 2) sono istanze di una classe SINGLETON, richiamate con AxxService.getInstance() <br>
 * 3) sono annotate @Autowired; sono disponibile SOLO DOPO @PostConstruct <br>
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
 * Annotation @Route(value = "") per la vista iniziale - Ce ne pouò essere solo una per applicazione
 * ATTENZIONE: se rimangono due (o più) classi con @Route(value = ""), in fase di compilazione appare l'errore:
 * -'org.springframework.context.ApplicationContextException:
 * -Unable to start web server;
 * -nested exception is org.springframework.boot.web.server.WebServerException:
 * -Unable to start embedded Tomcat'
 * <p>
 * Non usa l'interfaccia HasUrlParameter col metodo setParameter(BeforeEvent event, ...)
 * che serve per chiamate che NON usano @Route
 * Usa l'interfaccia BeforeEnterObserver col metodo beforeEnter()
 * invocato da @Route al termine dell'init() di questa classe e DOPO il metodo @PostConstruct
 * <p>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public abstract class AViewList extends APropertyViewList implements IAView, BeforeEnterObserver, BeforeLeaveObserver {

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
     * Questo metodo viene chiamato per primo subito dopo il costruttore <br>
     * Dopo viene chiamato beforeEnter(); <br>
     * <p>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Creazione e posizionamento dei componenti UI <br>
     * Possono essere sovrascritti nelle sottoclassi <br>
     */
    @PostConstruct
    protected void initView() {
        this.setMargin(false);
        this.setSpacing(false);
        this.removeAll();

        //--Login and context della sessione
        this.mongo = appContext.getBean(AMongoService.class);
        context = vaadinService.getSessionContext();
        login = context != null ? context.getLogin() : null;

        //--se il login è obbligatorio e manca, la View non funziona
        if (vaadinService.mancaLoginSeObbligatorio()) {
            return;
        }// end of if cycle

        //--Le preferenze standard e specifiche
        this.fixPreferenze();

        //--Placeholder
        this.fixLayout();

        //--menu generale dell'applicazione
        this.creaMenuLayout();

        //--una o più righe di avvisi
        this.creaAlertLayout();
        if (alertPlacehorder.getComponentCount() > 0) {
            this.add(alertPlacehorder);
        }// end of if cycle

        //--barra/menu dei bottoni specifici del modulo
        this.creaTopLayout();
        if (topPlaceholder.getComponentCount() > 0) {
            this.add(topPlaceholder);
        }// end of if cycle

        //--body con la Grid
        //--seleziona quale grid usare e la aggiunge al layout
        this.creaBody();
        this.add(gridPlaceholder);

        //--aggiunge il footer standard
        this.add(appContext.getBean(AFooter.class));
    }// end of method


    /**
     * Metodo chiamato da com.vaadin.flow.router.Router verso questa view tramite l'interfaccia BeforeEnterObserver <br>
     * Chiamato DOPO @PostConstruct <br>
     *
     * @param beforeEnterEvent con la location, ui, navigationTarget, source, ecc
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //--se il login è obbligatorio e manca, la View non funziona
        if (vaadinService.mancaLoginSeObbligatorio()) {
            return;
        }// end of if cycle
        this.addSpecificRoutes();
        this.updateItems();
        this.updateView();
    }// end of method


    /**
     * Le preferenze standard <br>
     * Le preferenze specifiche della sottoclasse <br>
     * Gestito nella classe specializzata APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }// end of method


    /**
     * Costruisce gli oggetti base (placeholder) di questa view <br>
     * Li aggiunge alla view stessa <br>
     * Gestito nella classe specializzata ALayoutViewList <br>
     * Può essere sovrascritto, per modificare il layout standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixLayout() {
    }// end of method


    /**
     * Costruisce la barra di menu e l'aggiunge alla UI <br>
     * Lo standard è 'Flowingcode'
     * Può essere sovrascritto
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaMenuLayout() {
    }// end of method


    /**
     * Placeholder (eventuale) per informazioni aggiuntive alla grid ed alla lista di elementi <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaAlertLayout() {
    }// end of method


    /**
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, presente di default <br>
     * - con o senza un bottone per cancellare tutta la collezione
     * - con o senza un bottone di reset per ripristinare (se previsto in automatico) la collezione
     * - con o senza gruppo di ricerca:
     * -    campo EditSearch predisposto su un unica property, oppure (in alternativa)
     * -    bottone per aprire un DialogSearch con diverse property selezionabili
     * -    bottone per annullare la ricerca e riselezionare tutta la collezione
     * - con eventuale Popup di selezione, filtro e ordinamento
     * - con o senza bottone New, con testo regolato da preferenza o da parametro <br>
     * - con eventuali altri bottoni specifici <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaTopLayout() {
    }// end of method


    /**
     * Crea il corpo centrale della view <br>
     * Componente grafico obbligatorio <br>
     * Seleziona quale grid usare e la aggiunge al layout <br>
     * Eventuale barra di bottoni sotto la grid <br>
     */
    protected void creaBody() {
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


    protected void updateItems() {
    }// end of method


    public void updateView() {
    }// end of method


    protected Button createEditButton(AEntity entityBean) {
        Button edit = new Button(isEntityModificabile ? pref.getStr(FLAG_TEXT_EDIT) : pref.getStr(FLAG_TEXT_SHOW), event -> openDialog(entityBean));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
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
    protected void openDialog(AEntity entityBean) {
    }// end of method


    public void updateDopoDialog(AEntity entityBean) {
        this.updateItems();
        this.updateView();
    }// end of method

    //@todo da rendere getBean il dialogo
    protected void openSearch() {
        searchDialog = appContext.getBean(ASearchDialog.class, service);
        searchDialog.open("", "", this::updateDopoSearch, null);
    }// end of method


    public void updateDopoSearch() {
        LinkedHashMap<String, AbstractField> fieldMap = searchDialog.fieldMap;
        List<AEntity> lista;
        IAField field;
        Object fieldValue = null;
        ArrayList<CriteriaDefinition> listaCriteriaDefinitionRegex = new ArrayList();

        for (String fieldName : searchDialog.fieldMap.keySet()) {
            field = (IAField) searchDialog.fieldMap.get(fieldName);
            fieldValue = field.getValore();
            if (field instanceof ATextField || field instanceof ATextArea) {
                if (text.isValid(fieldValue)) {
                    listaCriteriaDefinitionRegex.add(Criteria.where(fieldName).regex("^" + fieldValue));
                }// end of if cycle
            }// end of if cycle
            if (field instanceof AIntegerField) {
                if ((Integer) fieldValue > 0) {
                    listaCriteriaDefinitionRegex.add(Criteria.where(fieldName).regex("^" + fieldValue));
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle
        lista = mongo.findAllByProperty(entityClazz, listaCriteriaDefinitionRegex.stream().toArray(CriteriaDefinition[]::new));

        if (array.isValid(lista)) {
            items = lista;
        }// end of if cycle

        this.updateView();

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
        updateItems();
        updateView();
    }// end of method


    /**
     * Reset effettivo (dopo dialogo di conferma) di tutte le entities della collezione. <br>
     * Rimanda al service specifico <br>
     * Azzera gli items <br>
     * Ridisegna la GUI <br>
     */
    protected void reset() {
        service.reset();
        updateItems();
        updateView();
    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void save(AEntity entityBean, EAOperation operation) {
        if (service.save(entityBean, operation) != null) {
            updateItems();
            updateView();
        }// end of if cycle
    }// end of method


    protected void delete(AEntity entityBean) {
        service.delete(entityBean);
        Notification.show(entityBean + " successfully deleted.", 3000, Notification.Position.BOTTOM_START);

        if (usaRefresh) {
            updateItems();
            updateView();
        }// end of if cycle
    }// end of method


    @Override
    public String getMenuName() {
        return annotation.getMenuName(this.getClass());
    }// end of method

}// end of class
