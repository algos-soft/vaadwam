package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import lombok.extern.slf4j.Slf4j;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadflow.application.FlowCost.USA_DEBUG;
import static it.algos.vaadflow.application.FlowVar.usaCompany;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:48
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
 * Sottoclasse di servizio per regolare il layout di AViewList in una classe 'dedicata' <br>
 * Alleggerisce la 'lettura' della classe principale <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi <br>
 * Regola menu, topLayout e alertLayout <br>
 * Qui vengono regolati i layout 'standard'. <br>
 * Nelle sottoclassi concrete i layout possono essere modificati. <br>
 */
@Slf4j
public abstract class ALayoutViewList extends APrefViewList {


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
    public ALayoutViewList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Costruisce gli oggetti base (placeholder) di questa view <br>
     * <p>
     * Li aggiunge alla view stessa <br>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Può essere sovrascritto, per modificare il layout standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixLayout() {
        super.fixLayout();
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(true);

        this.alertPlacehorder = new VerticalLayout();
        this.topPlaceholder = new HorizontalLayout();
        this.secondTopPlaceholder = new HorizontalLayout();
        this.gridPlaceholder = new VerticalLayout();
        this.bottomPlacehorder = new HorizontalLayout();

        if (pref.isBool(USA_DEBUG)) {
            this.getElement().getStyle().set("background-color", EAColor.yellow.getEsadecimale());
            alertPlacehorder.getElement().getStyle().set("background-color", EAColor.lightgreen.getEsadecimale());
            topPlaceholder.getElement().getStyle().set("background-color", EAColor.lime.getEsadecimale());
            secondTopPlaceholder.getElement().getStyle().set("background-color", EAColor.bisque.getEsadecimale());
            gridPlaceholder.getElement().getStyle().set("background-color", EAColor.red.getEsadecimale());
            bottomPlacehorder.getElement().getStyle().set("background-color", EAColor.silver.getEsadecimale());
        }// end of if cycle

        gridPlaceholder.setMargin(false);
        gridPlaceholder.setSpacing(false);
        gridPlaceholder.setPadding(false);
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        alertPlacehorder.removeAll();
        alertPlacehorder.setMargin(false);
        alertPlacehorder.setSpacing(false);
        alertPlacehorder.setPadding(false);

        if (isEntityDeveloper || isEntityAdmin || isEntityEmbedded || isEntityUsaDatiDemo) {
            usaTopAlert = true;
        }// end of if cycle

        if (usaTopAlert) {
            if (usaSecurity) {
                if (isEntityDeveloper) {
                    alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come developer. Gli admin e gli utenti normali non la vedono."));
                }// end of if cycle

                if (isEntityAdmin) {
                    alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come admin. Gli utenti normali non la vedono."));
                }// end of if cycle
            }// end of if cycle

            if (isEntityEmbedded) {
                alertPlacehorder.add(new Label("Questa lista non dovrebbe mai essere usata direttamente (serve come test o per le sottoclassi specifiche)"));
                alertPlacehorder.add(new Label("L'entity è 'embedded' nelle collezioni che la usano (no @Annotation property DbRef)"));
            }// end of if cycle

            if (isEntityEmbedded || isEntityUsaDatiDemo) {
                alertPlacehorder.add(new Label("Allo startup del programma, sono stati creati alcuni elementi di prova"));
            }// end of if cycle
        }// end of if cycle

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
    @Override
    protected void creaTopLayout() {
        topPlaceholder.removeAll();
        topPlaceholder.addClassName("view-toolbar");
        String buttonTitle;
        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();

        //--il bottone associa un evento standard -> AViewList.openConfirmDelete(), che può essere sovrascritto
        if ((!FlowVar.usaSecurity && usaButtonDelete) || (isDeveloper && usaButtonDelete)) {
            buttonDelete = new Button("Delete", new Icon(VaadinIcon.CLOSE_CIRCLE));
            buttonDelete.getElement().setAttribute("theme", "error");
            buttonDelete.getElement().setAttribute("title", "Cancella tutta la collezione");
            buttonDelete.addClassName("view-toolbar__button");
            topPlaceholder.add(buttonDelete);
        }// end of if cycle


        //--il bottone associa un evento standard -> AViewList.openConfirmReset(), che rinvia al service specifico
        if ((!FlowVar.usaSecurity && usaButtonReset) || (isDeveloper && usaButtonReset)) {
            buttonReset = new Button("Reset", new Icon(VaadinIcon.CLOSE_CIRCLE));
            buttonReset.getElement().setAttribute("theme", "error");
            buttonReset.getElement().setAttribute("title", "Ripristina tutta la collezione");
            buttonReset.addClassName("view-toolbar__button");
            topPlaceholder.add(buttonReset);
        }// end of if cycle


        //--il bottone associa un evento standard -> AViewList.openNew()
        //--il bottone associa, se previsto da pref, un tasto shortcut
        if (usaButtonNew) {
            buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_NEW));
            buttonNew = new Button(buttonTitle, new Icon("lumo", "plus"));
            buttonNew.getElement().setAttribute("theme", "primary");
            buttonNew.getElement().setAttribute("title", "Crea una nuova entity");
            buttonNew.addClassName("view-toolbar__button");
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                buttonNew.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
            }// end of if cycle
            topPlaceholder.add(buttonNew);
        }// end of if cycle

        //--eventuale campo o dialogo di ricerca
        switch (searchType) {
            case nonUsata:
                break;
            case editField:
                //--campo EditSearch predisposto su un unica property
                String placeHolder = text.isValid(searchProperty) ? text.primaMaiuscola(searchProperty) + "..." : "Cerca...";
                String toolTip = "Caratteri iniziali della ricerca" + (text.isValid(searchProperty) ? " nel campo '" + searchProperty + "'" : "");
                searchField = new TextField("", placeHolder);
                searchField.setPrefixComponent(new Icon("lumo", "search"));
                searchField.getElement().setAttribute("title", toolTip);
                searchField.addClassName("view-toolbar__search-field");
                searchField.setValueChangeMode(ValueChangeMode.EAGER);

                //--bottone piccolo per pulire il campo testo di ricerca
                buttonClearFilter = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
                buttonClearFilter.setEnabled(false);
                buttonClearFilter.getElement().setAttribute("title", "Pulisce il campo di ricerca");
                topPlaceholder.add(searchField, buttonClearFilter);

                break;
            case dialog:
                //--il bottone associa un evento standard -> openConfirmDialogDelete(), che deve essere sovrascritto
                buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_SEARCH));
                buttonSearch = new Button(buttonTitle, new Icon("lumo", "search"));
                buttonSearch.getElement().setAttribute("theme", "secondary");
                buttonSearch.getElement().setAttribute("title", "Apre una finestra di dialogo");
                buttonSearch.addClassName("view-toolbar__button");

                //--bottone piccolo per ripristinare la condizione senza filtro
                buttonClearFilter = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
                buttonClearFilter.setEnabled(false);
                buttonClearFilter.getElement().setAttribute("title", "Annulla la selezione effettuata");
                topPlaceholder.add(buttonSearch, buttonClearFilter);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        //--eventuale filtro sulla company
        if (usaCompany) {
            creaCompanyFiltro();
            if (filtroCompany != null && login.isDeveloper() && mostraFiltroCompany) {
                topPlaceholder.add(filtroCompany);
            }// end of if cycle
        }// end of if cycle

        //--eventuale filtro specifico
        if (usaPopupFiltro) {
            creaPopupFiltro();
            if (filtroComboBox != null) {
                topPlaceholder.add(filtroComboBox);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge tutti i listeners ai bottoni di 'topPlaceholder' che sono stati creati SENZA listeners <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void addListeners() {
        super.addListeners();

        if (buttonDelete != null) {
            buttonDelete.addClickListener(event -> openConfirmDelete());
        }// end of if cycle

        if (buttonReset != null) {
            buttonReset.addClickListener(e -> openConfirmReset());
        }// end of if cycle

        if (buttonNew != null) {
            buttonNew.addClickListener(event -> openNew());
        }// end of if cycle

        if (searchField != null) {
            searchField.addValueChangeListener(e -> {
                updateFiltri();
                updateGrid();
                if (searchField.getValue().isEmpty()) {
                    buttonClearFilter.setEnabled(false);
                } else {
                    buttonClearFilter.setEnabled(true);
                }// end of if/else cycle
            });//end of lambda expression
            if (buttonClearFilter != null) {
                buttonClearFilter.addClickListener(e -> {
                    searchField.clear();
                    actionSincroSearch();
                });//end of lambda expressions
            }// end of if cycle
        }// end of if cycle

        if (buttonSearch != null) {
            buttonSearch.addClickListener(e -> openSearch());
            if (buttonClearFilter != null) {
                buttonClearFilter.addClickListener(e -> {
                    actionSincroSearch();
                });//end of lambda expressions
            }// end of if cycle
        }// end of if cycle

        if (filtroCompany != null) {
            filtroCompany.addValueChangeListener(e -> {
                actionSincroCompany();
            });// end of lambda expressions
        }// end of if cycle

        if (filtroComboBox != null) {
            filtroComboBox.addValueChangeListener(e -> {
                actionSincroCombo();
            });// end of lambda expressions
        }// end of if cycle

    }// end of method


    /**
     * Crea un Popup di selezione della company <br>
     * Creato solo se develeper=true e usaCompany=true <br>
     * Può essere sovrascritto, per caricare gli items da una sottoclasse di Company <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaCompanyFiltro() {
        IAService serviceCompany;
        Company companyCorrente = null;

        if (usaFiltroCompany) {
            serviceCompany = (IAService) appContext.getBean(FlowVar.companyServiceClazz);
            filtroCompany = new AComboBox();
            filtroCompany.setPlaceholder(text.primaMaiuscola(FlowVar.companyClazzName) + " ...");
            filtroCompany.setWidth("9em");
            filtroCompany.setItems(serviceCompany.findAllAll());
            if (login != null) {
                companyCorrente = login.getCompany();
                if (companyCorrente != null) {
                    filtroCompany.setValue(companyCorrente);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaPopupFiltro() {
        filtroComboBox = new AComboBox();
        filtroComboBox.setWidth("10em");
    }// end of method


    /**
     * Crea il corpo centrale della view inserito in 'gridPlaceholder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Componente grafico obbligatorio <br>
     * Seleziona quale grid usare e la aggiunge al layout <br>
     * Eventuale barra di bottoni sotto la grid <br>
     */
    @Override
    protected void creaBody() {
        gridPlaceholder.removeAll();
        gridPlaceholder.setMargin(false);
        gridPlaceholder.setSpacing(false);
        gridPlaceholder.setPadding(false);

        Grid grid = creaGrid();
        if (grid != null) {
            gridPlaceholder.add(grid);
            gridPlaceholder.setFlexGrow(0);
        }// end of if cycle
    }// end of method





}// end of class
