package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

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
     * Crea il corpo centrale della view <br>
     * Componente grafico obbligatorio <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     */
    protected void creaBody() {
        gridPlaceholder.setMargin(false);
        gridPlaceholder.setSpacing(false);
        gridPlaceholder.setPadding(false);
        FlexLayout layout = new FlexLayout();

        //--Costruisce una lista di nomi delle properties della Grid
        List<String> gridPropertyNamesList = getGridPropertyNamesList();

        gridPlaceholder.add(creaGrid(gridPropertyNamesList));

        //--Regolazioni di larghezza
        gridPlaceholder.setWidth(gridWith + "em");
        gridPlaceholder.setFlexGrow(0);
        gridPlaceholder.getElement().getStyle().set("background-color", "#ffaabb");//rosa

        //--eventuale barra di bottoni sotto la grid
        creaGridBottomLayout();
    }// end of method


    /**
     * Crea la grid <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto <br>
     */
    protected Grid creaGrid(List<String> gridPropertyNamesList) {
        if (grid == null) {
            if (entityClazz != null && AEntity.class.isAssignableFrom(entityClazz)) {
                try { // prova ad eseguire il codice
                    //--Costruisce la Grid SENZA creare automaticamente le colonne
                    //--Si possono così inserire colonne manuali prima e dopo di quelle automatiche
                    grid = new Grid(entityClazz, false);
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                    return null;
                }// fine del blocco try-catch
            } else {
                grid = new Grid();
            }// end of if/else cycle
        }// end of if cycle

        //        //--regolazioni eventuali se la Grid è paginata in fixPreferenze() della sottoclasse
//        fixGridPaginata();

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
        grid.getElement().getStyle().set("background-color", "#aabbcc");

        fixGridHeader();

        grid.addSelectionListener(new SelectionListener<Grid<AEntity>, AEntity>() {

            @Override
            public void selectionChange(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
                boolean enabled = selectionEvent != null && selectionEvent.getAllSelectedItems().size() > 0;
                sincroBottoniMenu(enabled);
            }// end of inner method
        });//end of lambda expressions and anonymous inner class

        return grid;
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
                    columnService.create(appContext, grid, entityClazz, propertyName);
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

        if (usaBottomLayout) {
            this.add(bottomPlacehorder);
        }// end of if cycle
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


    /**
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
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
