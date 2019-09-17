package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.IADialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.vaadin.klaudeta.PaginatedGrid;

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
     * Costruttore <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    public AGridViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
    }// end of Spring constructor


    /**
     * Crea il corpo centrale della view <br>
     * Componente grafico obbligatorio <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     */
    protected void creaGrid() {
        gridPlaceholder.setMargin(false);
        gridPlaceholder.setSpacing(false);
        gridPlaceholder.setPadding(false);

        List<String> gridPropertyNamesList = null;
        FlexLayout layout = new FlexLayout();

        //--Costruisce una lista di nomi delle properties della Grid
        gridPropertyNamesList = getGridPropertyNamesList();

        //--regolazioni eventuali se la Grid è paginata in fixPreferenze() della sottoclasse
        fixGridPaginata();

        if (grid == null) {
            isPaginata = false;
            if (entityClazz != null && AEntity.class.isAssignableFrom(entityClazz)) {
                try { // prova ad eseguire il codice
                    //--Costruisce la Grid SENZA creare automaticamente le colonne
                    //--Si possono così inserire colonne manuali prima e dopo di quelle automatiche
                    grid = new Grid(entityClazz, false);
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                    return;
                }// fine del blocco try-catch
            } else {
                grid = new Grid();
            }// end of if/else cycle
        }// end of if cycle

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
        grid.setWidth(gridWith + "em");
        gridPlaceholder.add(grid);

        //--Regolazioni di larghezza
        gridPlaceholder.setWidth(gridWith + "em");
        gridPlaceholder.setFlexGrow(0);
//        gridPlaceholder.setWidth(gridWith + "em");

//        gridPlaceholder.setFlexGrow(1, grid); //@todo Non sembra che funzioni
        gridPlaceholder.getElement().getStyle().set("background-color", "#ffaabb");//rosa
        grid.getElement().getStyle().set("background-color", "#aabbcc");

        grid.addSelectionListener(new SelectionListener<Grid<AEntity>, AEntity>() {

            @Override
            public void selectionChange(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
                boolean enabled = selectionEvent != null && selectionEvent.getAllSelectedItems().size() > 0;
                sincroBottoniMenu(enabled);
            }// end of inner method
        });//end of lambda expressions and anonymous inner class

        fixGridHeader();

        //--eventuale barra di bottoni sotto la grid
        creaGridBottomLayout();
    }// end of method


    /**
     * Regola la GridPaginata <br>
     * DEVE essere creata in fixPreferenze() della sottoclasse con la PaginatedGrid specifica della Collection <br>
     * Può essere sovrascritto <br>
     */
    protected void fixGridPaginata() {
        if (grid != null) {
            // Sets how many pages should be visible on the pagination before and/or after the current selected page
            ((PaginatedGrid) grid).setPaginatorSize(1);
        }// end of if cycle
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
     */
    protected void addColumnsGrid(List<String> gridPropertyNamesList) {
        if (gridPropertyNamesList != null) {
            for (String propertyName : gridPropertyNamesList) {
                columnService.create(appContext, grid, entityClazz, propertyName);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Eventuali colonne specifiche aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
    }// end of method


    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi
     * Facoltativo (assente di default)
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaGridBottomLayout() {
        bottomPlacehorder = new HorizontalLayout();
        bottomPlacehorder.addClassName("view-toolbar");

        if (usaBottomLayout) {
            this.add(bottomPlacehorder);
        }// end of if cycle
    }// end of method


    /**
     * Apre il dialog di detail <br>
     * Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come prima colonna <br>
     */
    protected void addDetailDialog() {
        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (usaBottoneEdit) {
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            Grid.Column colonna = grid.addColumn(renderer);
            colonna.setWidth("3em");
            colonna.setFlexGrow(0);
        } else {
            EAOperation operation = isEntityModificabile ? EAOperation.edit : EAOperation.showOnly;
            grid.addSelectionListener(evento -> apreDialogo((SingleSelectionEvent) evento, operation));
        }// end of if/else cycle
    }// end of method


    protected void sincroBottoniMenu(boolean enabled) {
    }// end of method


    /**
     * Eventuale header text
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
        int numRecCollezione = items.size();
//        int numRecCollezione = service.count();
        String filtro = text.format(items.size());
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


    protected void updateItems() {
        List<AEntity> lista = null;
        ArrayList<CriteriaDefinition> listaCriteriaDefinitionRegex = new ArrayList();

        if (usaSearch) {
            if (!usaSearchDialog && searchField != null && text.isEmpty(searchField.getValue())) {
                items = service != null ? service.findAll() : null;
            } else {
                if (searchField != null) {
                    if (pref.isBool(USA_SEARCH_CASE_SENSITIVE)) {
                        listaCriteriaDefinitionRegex.add(Criteria.where(searchProperty).regex("^" + searchField.getValue()));
                    } else {
                        listaCriteriaDefinitionRegex.add(Criteria.where(searchProperty).regex("^" + searchField.getValue(), "i"));
                    }// end of if/else cycle
                    lista = mongo.findAllByProperty(entityClazz, listaCriteriaDefinitionRegex.stream().toArray(CriteriaDefinition[]::new));
                } else {
                    items = service != null ? service.findAll() : null;
                }// end of if/else cycle

                if (array.isValid(lista)) {
                    items = lista;
                }// end of if cycle
            }// end of if/else cycle
        } else {
            items = service != null ? service.findAll() : null;
        }// end of if/else cycle
    }// end of method


    public void updateView() {
        if (items != null) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(items);
                headerGridHolder.setText(getGridHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        creaAlertLayout();
    }// end of method


}// end of class
