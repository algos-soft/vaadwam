package it.algos.vaadwam.modules.statistica;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwam.WamLayout;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.schedule.ATask;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.haijian.Exporter;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.START_DATE_TIME;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_STA;
import static it.algos.vaadwam.application.WamCost.TASK_STATISTICA;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-ott-2019 7.35.49 <br>
 * <p>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * <p>
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
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) e viene richiamate
 * con AxxService.getInstance() <br>
 * 3) sono annotate @Autowired; sono disponibili SOLO DOPO @PostConstruct <br>
 * <p>
 * Considerato che le sottoclassi concrete NON sono singleton e vengo ri-create ogni volta che dal menu (via @Router)
 * si invocano, è inutile (anche se possibile) usare un metodo @PostConstruct che è sempre un'0appendici di init() del
 * costruttore.
 * Meglio spostare tutta la logica iniziale nel metodo beforeEnter() <br>
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
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @Secured (facoltativo) per l'accesso con security a seconda del ruolo dell'utente loggato <br>
 * - 'developer' o 'admin' o 'user' <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 * Annotated with @AIView (facoltativo Algos) per il menu-name, l'icona-menu, la property-search e la visibilità <br>
 * Se serve una Grid paginata estende APaginatedGridViewList altrimenti AGridViewList <br>
 * Se si usa APaginatedGridViewList è obbligatorio creare la PaginatedGrid
 * 'tipizzata' con la entityClazz (Collection) specifica nel metodo creaGridPaginata <br>
 */
@UIScope
@Route(value = TAG_STA, layout = WamLayout.class)
@Qualifier(TAG_STA)
@Slf4j
//@Secured("admin")
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "statistiche", menuIcon = VaadinIcon.RECORDS, sortProperty = "ordine", roleTypeVisibility = EARoleType.admin)
@PageTitle(WamCost.BROWSER_TAB_TITLE)
public class StatisticaList extends WamViewList {


    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     */
    @Autowired
    protected MiliteService militeService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     */
    @Autowired
    protected TurnoService turnoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     */
    @Autowired
    @Qualifier(TASK_STATISTICA)
    private ATask taskElabora;

    private StatisticaService service;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa alla superclasse il service iniettato qui da Vaadin/@Route <br>
     * Passa alla superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public StatisticaList(@Qualifier(TAG_STA) IAService service) {
        super(service, Statistica.class);
    }// end of Vaadin/@Route constructor


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<Statistica>();
    }// end of method


    /**
     * Preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        this.service = (StatisticaService) super.service;

        //        if (wamLogin.isAdmin()) {
        //            super.usaButtonDelete = true;
        //        }// end of if cycle

        super.usaButtonNew = false;
        super.usaBottoneEdit = true;
        super.isEntityModificabile = false;

        super.soloVisioneUser = false;
        super.soloVisioneAdmin = false;
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        fixPreferenze();

        alertAdmin.add("Statistiche dei turni effettuati da ogni milite dal 1° gennaio dell'anno alla data odierna.");
        alertAdmin.add("Solo in visione. Vengono generate in automatico ogni notte. Come admin si possono ri-elaborare in ogni momento.");
        alertAdmin.add("Nome del milite, data dell'ultimo turno effettuato, giorni trascorsi dall'ultimo turno, validità della frequenza (sulla base di 2 turni/mese),");
        alertAdmin.add("numero totale dei turni effettuati dall'inizio dell'anno, ore totali effettuate dall'inizio dell'anno, media (arrotondata) di ore per turno");

        super.creaAlertLayout();
        alertPlacehorder.add(getInfoElabora());
    }// end of method


    /**
     * Costruisce le info di elaborazione <br>
     * Può essere attivo lo scheduler della croce <br>
     * Tre possibilità:
     * Non previsto
     * Disattivato
     * Scheduled alle....
     */
    protected Label getInfoElabora() {
        Label label = null;
        String testo = "";
        String tag = "Elaborazione automatica ";
        boolean elaborazioneAutomatica = pref.isBool(service.usaDaemonElabora);
        String nota = taskElabora != null ? taskElabora.getSchedule().getNota() : VUOTA;
        int durata = text.isValid(service.durataLastElabora) ? pref.getInt(service.durataLastElabora) : 0;

        if (wamLogin.isAdminOrDev()) {
            LocalDateTime lastImport = text.isValid(service.lastElabora) ? pref.getDateTime(service.lastElabora) : START_DATE_TIME;
            testo = tag;

            if (elaborazioneAutomatica) {
                testo += nota;
            } else {
                testo += "disattivata.";
            }// end of if/else cycle

            if (lastImport != null) {
                label = text.getLabelDev(testo + " Ultima elaborazione il " + date.getTime(lastImport) + " in " + date.toTextSecondi(durata));
            } else {
                label = text.getLabelDev(tag + nota + " Non ancora effettuata.");
            }// end of if/else cycle

        }// end of if cycle

        return label;
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
    @Override
    protected void creaTopLayout() {
        super.creaTopLayout();

        if (topPlaceholder != null && importButton != null) {
            topPlaceholder.remove(importButton);
        }// end of if cycleì

        if (wamLogin.isAdminOrDev()) {
            Button elaboraButton = new Button("Elabora", new Icon(VaadinIcon.REFRESH));
            elaboraButton.getElement().setAttribute("theme", "primary");
            elaboraButton.getElement().setAttribute("title", "Elaborazione immediata");
            elaboraButton.addClassName("view-toolbar__button");
            elaboraButton.addClickListener(e -> elabora());
            topPlaceholder.add(elaboraButton);
        }// end of if cycle

        if (wamLogin.isAdminOrDev()) {
            Button exportButton = new Button("Excel", new Icon(VaadinIcon.DOWNLOAD_ALT));
            exportButton.getElement().setAttribute("theme", "error");
            exportButton.getElement().setAttribute("title", "Foglio di excel");
            exportButton.addClassName("view-toolbar__button");
            exportButton.addClickListener(e -> exportExcel());
            topPlaceholder.add(exportButton);
        }// end of if cycle

        //        if (wamLogin.isAdminOrDev()) {
        //            Button exportButton = new Button("CSV", new Icon(VaadinIcon.DOWNLOAD_ALT));
        //            exportButton.getElement().setAttribute("theme", "error");
        //            exportButton.getElement().setAttribute("title", "Foglio di excel");
        //            exportButton.addClassName("view-toolbar__button");
        //            exportButton.addClickListener(e -> exportCSV());
        //            topPlaceholder.add(exportButton);
        //        }// end of if cycle

    }// end of method


    /**
     * Elabora (nel service) le statistiche <br>
     * Se developer=true or admin=true, elabora SOLO la propria croce <br>
     * Se user=true, non vede questa lista <br>
     */
    public void elabora() {
        if (wamLogin.isAdminOrDev()) {
            service.elabora(wamLogin.getCroce());
        }// end of if cycle
        updateGrid();
    }// end of method


    /**
     * Crea un Popup di selezione della company <br>eeeeeeee4
     * Creato solo se developer=true e usaCompany=true <br>
     */
    @Override
    protected void creaCompanyFiltro() {
        if (login.isDeveloper()) {
            super.creaCompanyFiltro();
        }// end of if cycle
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
    @Override
    protected void openDialog(AEntity entityBean) {
        appContext.getBean(StatisticaDialog.class, service, entityClazz).open(entityBean, isEntityModificabile ? EAOperation.edit : EAOperation.showOnly, this::save, this::delete);
    }// end of method


    protected void exportExcel() {
        Grid<Statistica> grid = new Grid(Statistica.class, false);
        grid.setColumns("milite", "last", "delta", "valido", "turni", "ore", "media");
        grid.setItems(items);

        //        StreamResource stream=new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid));
        Anchor esporta = new Anchor(new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid)), "Download As Excel");
        esporta.getElement().setAttribute("Export", true);
        esporta.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        topPlaceholder.add(esporta);
        int a = 87;
    }// end of method


    protected void exportCSV() {
        Grid<Statistica> grid = new Grid(Statistica.class);
        grid.setItems(items);

        //        StreamResource stream=new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid));
        Anchor esporta = new Anchor(new StreamResource("my-file.csv", Exporter.exportAsCSV(grid)), "Download As CSV");
        esporta.getElement().setAttribute("Export", true);
        esporta.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        topPlaceholder.add(esporta);
        int a = 87;
    }// end of method

}// end of class