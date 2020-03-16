package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.enumeration.EASearch;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.wrapper.AFiltro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.USA_SEARCH_CASE_SENSITIVE;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 08:24
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
 * Sottoclasse di servizio per gestire la Grid di AViewList in una classe 'dedicata' <br>
 * Alleggerisce la 'lettura' della classe principale <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi <br>
 * Costruisce e regola la Grid <br>
 * Nelle sottoclassi concrete la Grid può essere modificata. <br>
 * <p>
 * Se si prevede che la lunghezza del DB possa superare una soglia prestabilita (regolabile in preferenza), <br>
 * occorre implementare nella sottoclasse XxxViewList 3 metodi specifici per la PaginatedGrid: <br>
 * 1) creaGridPaginata() per creare la PaginatedGrid<Xxx> della classe corretta <br>
 * 2) addColumnsGridPaginata() per creare le columns delle property richieste <br>
 * 3) fixColumn() per regolare le columns nel AColumnService <br>
 */
@Slf4j
public abstract class AGridViewList extends ALayoutViewList {


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
    public AGridViewList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


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
        if (entityClazz != null && AEntity.class.isAssignableFrom(entityClazz)) {
            //--Crea effettivamente il Component Grid
            if (isPaginata) {
                grid = creaGridComponent();
            } else {
                grid = new Grid(entityClazz, false);
            }// end of if/else cycle
        } else {
            return null;
        }// end of if/else cycle

        //--Costruisce una lista di nomi delle properties della Grid
        List<String> gridPropertyNamesList = getGridPropertyNamesList();

        //--Apre il dialog di detail
        //--Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come prima colonna
        this.addDetailDialog();

        //--Eventuali colonne specifiche aggiunte PRIMA di quelle automatiche
        this.addSpecificColumnsBefore();

        //--Colonne normali indicate in @AIList(fields =... , aggiunte in automatico
        this.addColumnsGrid(gridPropertyNamesList);

        //--Eventuali colonne specifiche aggiunte DOPO quelle automatiche
        this.addSpecificColumnsAfter();

        // Sets the max number of items to be rendered on the grid for each page
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setPageSize(limit);
        grid.setHeightByRows(true);
        grid.getElement().getStyle().set("background-color", "#aabbcc");

        fixGridHeader();

        grid.addSelectionListener(new SelectionListener<Grid<AEntity>, AEntity>() {

            @Override
            public void selectionChange(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
                boolean enabled = selectionEvent != null && selectionEvent.getAllSelectedItems().size() > 0;
                sincroBottoniMenu(enabled);
            }// end of inner method
        });//end of lambda expressions and anonymous inner class

        creaGridBottomLayout();

        return grid;
    }// end of method


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    protected Grid creaGridComponent() {
        //--Costruisce la Grid SENZA creare automaticamente le colonne
        //--Si possono così inserire colonne manuali prima e dopo quelle automatiche
        return new Grid(entityClazz, false);
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties <br>
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive il metodo getGridPropertyNamesList() nella sottoclasse specifica di xxxService <br>
     * Un eventuale modifica dell'ordine di presentazione delle colonne viene regolata nel metodo sovrascritto <br>
     */
    protected List<String> getGridPropertyNamesList() {
        List<String> gridPropertyNamesList = service != null ? service.getGridPropertyNamesList(context) : null;
        return gridPropertyNamesList;
    }// end of method


    /**
     * Eventuali colonne specifiche aggiunte PRIMA di quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsBefore() {
    }// end of method


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    protected void addColumnsGrid(List<String> gridPropertyNamesList) {
        if (grid != null) {
            if (gridPropertyNamesList != null) {
                for (String propertyName : gridPropertyNamesList) {
                    columnService.create(grid, entityClazz, propertyName, searchProperty);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Eventuali colonne specifiche aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
    }// end of method


    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi <br>
     * Facoltativo (assente di default) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaGridBottomLayout() {
        bottomPlacehorder = new HorizontalLayout();
        bottomPlacehorder.addClassName("view-toolbar");
    }// end of method


    /**
     * Apre il dialog di detail <br>
     * Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come prima colonna <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    protected void addDetailDialog() {
        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (usaBottoneEdit) {
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            Grid.Column colonna = grid.addColumn(renderer);

            if (pref.isBool(FlowCost.USA_TEXT_EDIT_BUTTON)) {
                int lar = pref.getStr(FlowCost.FLAG_TEXT_EDIT).length();
                lar += 1;
                colonna.setWidth(lar + ".5em");
            } else {
                colonna.setWidth("3em");
            }// end of if/else cycle
            colonna.setFlexGrow(0);
        } else {
            grid.setSelectionMode(Grid.SelectionMode.NONE);
            grid.addItemDoubleClickListener(event -> apreDialogo((ItemDoubleClickEvent) event));
        }// end of if/else cycle
    }// end of method


    protected void sincroBottoniMenu(boolean enabled) {
    }// end of method


    /**
     * Eventuale header text <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    protected void fixGridHeader() {
        try { // prova ad eseguire il codice
            HeaderRow topRow = grid.prependHeaderRow();
            Grid.Column[] matrix = array.getColumnArray(grid);
            HeaderRow.HeaderCell informationCell = topRow.join(matrix);
            headerGridHolder = new Label("x");
            informationCell.setComponent(headerGridHolder);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    }// end of method


    /**
     * Header text
     */
    protected String getGridHeaderText() {
        int numRecCollezione = items != null ? items.size() : 0;
        String totale = text.format(numRecCollezione);
        String testo = entityClazz != null ? entityClazz.getSimpleName() + " - " : "";

        switch (numRecCollezione) {
            case 0:
                testo += "Al momento non ci sono elementi in questa collezione";
                break;
            case 1:
                testo += "Lista con un solo elemento";
                break;
            default:
                if (isPaginata && limit < numRecCollezione) {
                    testo += "Lista di " + limit + " elementi su " + totale + " totali. ";
                } else {
                    testo += "Lista di " + totale + " elementi";
                }// end of if/else cycle
                break;
        } // end of switch statement

        return testo;
    }// end of method


    /**
     * Crea la lista dei SOLI filtri necessari alla Grid per la prima visualizzazione della view <br>
     * I filtri normali vanno in updateFiltri() <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse AGridViewList <br>
     * Chiamato SOLO alla creazione della view. Successive modifiche ai filtri sono gestite in updateFiltri() <br>
     * Può essere sovrascritto SOLO se ci sono dei filtri che devono essere attivi già alla partenza della Grid <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaFiltri() {
        filtri = new ArrayList<AFiltro>();

        if (usaFiltroCompany && filtroCompany != null && filtroCompany.getValue() != null) {
            if (filtroCompany.getValue() != null) {
                filtri.add(new AFiltro(Criteria.where(FlowVar.companyClazzName).is(filtroCompany.getValue())));
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Aggiorna la lista dei filtri della Grid. Modificati per: popup, newEntity, deleteEntity, ecc... <br>
     * Normalmente tutti i filtri  vanno qui <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse AGridViewList <br>
     * Alla prima visualizzazione della view usa SOLO creaFiltri() e non questo metodo <br>
     * Può essere sovrascritto, per costruire i filtri specifici dei combobox, popup, ecc. <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void updateFiltri() {
        this.creaFiltri();
        EAFieldType type;
        int intValue;

        //--ricerca iniziale
        if (searchType == EASearch.editField && searchField != null && text.isValid(searchProperty)) {
            type = annotation.getFormType(entityClazz, searchProperty);

            switch (type) {
                case text:
                    if (pref.isBool(USA_SEARCH_CASE_SENSITIVE)) {
                        filtri.add(new AFiltro(Criteria.where(searchProperty).regex("^" + searchField.getValue())));
                    } else {
                        filtri.add(new AFiltro(Criteria.where(searchProperty).regex("^" + searchField.getValue(), "i")));
                    }// end of if/else cycle

                    break;
                case integer:
                    try { // prova ad eseguire il codice
                        intValue = Integer.decode(searchField.getValue());
                        filtri.add(new AFiltro(Criteria.where(searchProperty).is(intValue)));
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        updateFiltriSpecifici();
    }// end of method


    /**
     * Aggiorna i filtri specifici della Grid. Modificati per: popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Può essere sovrascritto, per costruire i filtri specifici dei combobox, popup, ecc. <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void updateFiltriSpecifici() {
    }// end of method


    /**
     * Aggiorna gli items della Grid, utilizzando i filtri. <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Sviluppato nella sottoclasse AGridViewList, oppure APaginatedGridViewList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Override
    public void updateGrid() {
        Sort sort = annotation.getSort(this.getClass());
        if (array.isValid(filtri)) {
            if (sort != null) {
                items = mongo.findAllByProperty(entityClazz, filtri, sort);
            } else {
                items = mongo.findAllByProperty(entityClazz, filtri);
            }// end of if/else cycle
        } else {
            items = service != null ? service.findAll() : null;
        }// end of if/else cycle

        if (items != null) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(items);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle
        headerGridHolder.setText(getGridHeaderText());

        creaAlertLayout();
    }// end of method


}// end of class
