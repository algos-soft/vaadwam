package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.enumeration.EAMenu;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.menu.AButtonMenu;
import it.algos.vaadflow.ui.menu.APopupMenu;
import it.algos.vaadflow.ui.menu.IAMenu;
import lombok.extern.slf4j.Slf4j;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.application.FlowVar.usaCompany;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:48
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
     * Li aggiunge alla view stessa <br>
     * Può essere sovrascritto, per modificare il layout standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixLayout() {
        super.fixLayout();
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        this.topPlaceholder = new HorizontalLayout();
        this.alertPlacehorder = new VerticalLayout();
        this.gridPlaceholder = new VerticalLayout();

        //this.gridPlaceholder.setSizeFull();

        this.bottomPlacehorder = new HorizontalLayout();

        if (pref.isBool(USA_DEBUG)) {
            this.getElement().getStyle().set("background-color", EAColor.yellow.getEsadecimale());
            alertPlacehorder.getElement().getStyle().set("background-color", EAColor.blue.getEsadecimale());
            topPlaceholder.getElement().getStyle().set("background-color", EAColor.lime.getEsadecimale());
            gridPlaceholder.getElement().getStyle().set("background-color", EAColor.red.getEsadecimale());
            bottomPlacehorder.getElement().getStyle().set("background-color", EAColor.silver.getEsadecimale());
        }// end of if cycle

        gridPlaceholder.setMargin(false);
        gridPlaceholder.setSpacing(false);
        gridPlaceholder.setPadding(false);
    }// end of method


    /**
     * Costruisce la barra di menu e l'aggiunge alla UI <br>
     * Lo standard è 'Flowingcode'
     * Può essere sovrascritto
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaMenuLayout() {
        IAMenu menu;
        EAMenu typeMenu = EAMenu.getMenu(pref.getEnumStr(USA_MENU));

        if (typeMenu != null) {
            switch (typeMenu) {
                case buttons:
                    menu = StaticContextAccessor.getBean(AButtonMenu.class);
                    this.add(menu.getComp());
                    break;
                case popup:
                    menu = StaticContextAccessor.getBean(APopupMenu.class);
                    this.add(menu.getComp());
                    break;
                case flowing:
//                    menu = StaticContextAccessor.getBean(AFlowingcodeAppLayoutMenu.class);
//                    this.add(menu.getComp());
                    break;
                case vaadin:
//                    menu = StaticContextAccessor.getBean(AAppLayoutMenu.class);
//                    this.add(new Label("."));
//                    this.add(((AFlowingcodeAppLayoutMenu) menu).getAppLayoutFlowing());
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaAlertLayout() {
        alertPlacehorder.removeAll();
//        alertPlacehorder.addClassName("view-toolbar");
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
        topPlaceholder.removeAll();
        topPlaceholder.addClassName("view-toolbar");
        String buttonTitle;
        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();

        //--il bottone associa un evento standard -> AViewList.openConfirmDelete(), che può essere sovrascritto
        if ((!FlowVar.usaSecurity && usaBottoneDeleteAll) || (isDeveloper && usaBottoneDeleteAll)) {
            deleteAllButton = new Button("Delete all", new Icon(VaadinIcon.CLOSE_CIRCLE));
            deleteAllButton.getElement().setAttribute("theme", "error");
            deleteAllButton.addClassName("view-toolbar__button");
            deleteAllButton.addClickListener(event -> openConfirmDelete());
            topPlaceholder.add(deleteAllButton);
        }// end of if cycle

        //--il bottone associa un evento standard -> AViewList.openConfirmReset(), che rinvia al service specifico
        if ((!FlowVar.usaSecurity && usaBottoneReset) || (isDeveloper && usaBottoneReset)) {
            resetButton = new Button("Reset", new Icon(VaadinIcon.CLOSE_CIRCLE));
            resetButton.getElement().setAttribute("theme", "error");
            resetButton.addClassName("view-toolbar__button");
            resetButton.addClickListener(e -> openConfirmReset());
            topPlaceholder.add(resetButton);
        }// end of if cycle

        //--con o senza gruppo di ricerca
        if (usaSearch) {
            //--bottone per aprire un DialogSearch con diverse property selezionabili
            if (usaSearchDialog) {
                //--il bottone associa un evento standard -> openConfirmDialogDelete(), che deve essere sovrascritto
                buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_SEARCH));
                searchButton = new Button(buttonTitle, new Icon("lumo", "search"));
                searchButton.getElement().setAttribute("theme", "secondary");
                searchButton.addClassName("view-toolbar__button");
                searchButton.addClickListener(e -> openSearch());

                //--bottone piccolo per eliminare i filtri di una ricerca e restituire tutte le entities
                clearFilterButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
                clearFilterButton.addClickListener(e -> {
                    updateItems();
                    updateView();
                });

                topPlaceholder.add(searchButton, clearFilterButton);
            } else {
                //--campo EditSearch predisposto su un unica property
                searchField = new TextField("", "Search");
                searchField.setPrefixComponent(new Icon("lumo", "search"));
                searchField.addClassName("view-toolbar__search-field");
                searchField.setValueChangeMode(ValueChangeMode.EAGER);
                searchField.addValueChangeListener(e -> {
                    updateItems();
                    updateView();
                });

                //--bottone piccolo per pulire il campo testo di ricerca
                clearFilterButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
                clearFilterButton.addClickListener(e -> searchField.clear());

                topPlaceholder.add(searchField, clearFilterButton);
            }// end of if/else cycle
        }// end of if cycle

        if (usaPopupFiltro) {
            creaPopupFiltro();
            topPlaceholder.add(filtroComboBox);
        } else {
            if (usaCompany && login.isDeveloper()) {
                creaCompanyFiltro();
                topPlaceholder.add(filtroCompany);
            }// end of if cycle
        }// end of if/else cycle


        //--il bottone associa un evento standard -> AViewList.openNew()
        //--il bottone associa, se previsto da pref, un tasto shortcut
        if (usaBottoneNew) {
            buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_NEW));
            newButton = new Button(buttonTitle, new Icon("lumo", "plus"));
            newButton.getElement().setAttribute("theme", "primary");
            newButton.addClassName("view-toolbar__button");
            newButton.addClickListener(event -> openNew());
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                newButton.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
            }// end of if cycle
            topPlaceholder.add(newButton);
        }// end of if cycle
    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaPopupFiltro() {
        filtroComboBox = new AComboBox();
        filtroComboBox.setWidth("8em");
    }// end of method


    /**
     * Crea un Popup di selezione della company <br>
     * Creato solo se devleper=true e usaCompany=true <br>
     * Può essere sovrascritto, per caricare gli items da una sottoclasse di Company <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaCompanyFiltro() {
        filtroCompany = new AComboBox();
        filtroCompany.setWidth("8em");
        filtroCompany.setItems(companyService.findAll());
        filtroCompany.addValueChangeListener(e -> {
            updateItems();
            updateView();
        });
    }// end of method

}// end of class
