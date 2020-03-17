package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.AColumnService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.wam.WamViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_ISC;
import static it.algos.vaadwam.application.WamCost.TAG_TUR;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 30-set-2018 16.22.05 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_TUR)
@Slf4j
@AIScript(sovrascrivibile = true)
public class TurnoDialog extends WamViewDialog<Turno> {

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AColumnService columnService = AColumnService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected FunzioneService funzioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected TurnoService turnoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ServizioService servizioService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected IscrizioneService iscrizioneService;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     */
    @Autowired
    @Qualifier(TAG_ISC)
    private IADialog dialogoIscrizione;

    private Grid grid;

    private List<Iscrizione> iscrizioniDelTurno;

    private ATextField titoloExtraField;

    private ATextField localitaExtraField;

    private AComboBox servizioField;

    private Turno turnoEntity;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public TurnoDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * L'istanza DEVE essere creata con appContext.getBean(TurnoDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public TurnoDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


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
        alertDev.add("Key e Croce appaiono solo per il developer e NON sono modificabili");
        if (operation == EAOperation.addNew) {
            alertDev.add("Modificando il comboBox dei servizi, viene regolata in automatico la key");
            alertDev.add("Modificando il comboBox dei servizi, vengono predisposte le iscrizioni per le funzioni previste");
        } else {
        }// end of if/else cycle
        alertDev.add("TitoloExtra e LocalitàExtra servono solo per i servizi SENZA orario");
        alertDev.add("Il servizio è modificabile SOLO se non c'è nessun milite segnato");

        super.fixAlertLayout();
    }// end of method


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    protected void fixStandardAlgosFields() {
        titoloExtraField = (ATextField) getField("titoloExtra");
        localitaExtraField = (ATextField) getField("localitaExtra");

        titoloExtraField.setEnabled(false);
        localitaExtraField.setEnabled(false);

        servizioField = (AComboBox) getField("servizio");
        servizioField.addValueChangeListener(event -> sincroServizio((Servizio) event.getValue()));//end of lambda expressions
    }// end of method


    private void sincroServizio(Servizio servizio) {
        if (servizio.isOrarioDefinito()) {
            titoloExtraField.setEnabled(false);
            localitaExtraField.setEnabled(false);
        } else {
            titoloExtraField.setEnabled(true);
            localitaExtraField.setEnabled(true);
        }// end of if/else cycle

        regolaKey(servizio);
        creaIscrizioni(servizio);
    }// end of method


    private void regolaKey(Servizio servizio) {
        String sigla = servizio.code;
        ATextField keyField = (ATextField) getField("id");
        keyField.setValue(((Turno) currentItem).id + sigla);
    }// end of method


    private void creaIscrizioni(Servizio servizio) {
        iscrizioniDelTurno = turnoService.getIscrizioni(servizio);
        if (grid != null) {
            grid.setItems(iscrizioniDelTurno);
        }// end of if cycle
    }// end of method


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void fixLayout() {
        grid = new Grid(Iscrizione.class);

        for (Object column : grid.getColumns()) {
            grid.removeColumn((Grid.Column) column);
        }// end of for cycle

        grid.setWidth("160em");

        //--Colonne aggiunte in automatico
        for (String propertyName : getGridPropertyNamesList()) {
            columnService.create(grid, Iscrizione.class, propertyName);
        }// end of for cycle

        //--header
        fixGridHeader();

        grid.addItemDoubleClickListener(event -> apreDialogoIscrizione((ItemDoubleClickEvent) event));
        formSubLayout.add(grid);
        updateItems();
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties <br>
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive il metodo getGridPropertyNamesList() nella sottoclasse specifica di xxxService <br>
     * Un eventuale modifica dell'ordine di presentazione delle colonne viene regolata nel metodo sovrascritto <br>
     */
    protected List<String> getGridPropertyNamesList() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("funzione");
        lista.add("milite");
        lista.add("lastModifica");
        lista.add("inizio");
        lista.add("fine");
        lista.add("durataEffettiva");
        lista.add("esisteProblema");
        lista.add("note");
        lista.add("notificaInviata");

        return lista;
    }// end of method


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    protected void addColumnsGrid(List<String> gridPropertyNamesList) {
        if (grid != null) {
            if (gridPropertyNamesList != null) {
                for (String propertyName : gridPropertyNamesList) {
                    columnService.create(grid, Iscrizione.class, propertyName);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    private void updateItems() {
        List<Iscrizione> items;
        items = ((Turno) currentItem).iscrizioni;
        if (items != null) {
            grid.setItems(items);
        }// end of if cycle
    }// end of method


    /**
     * Eventuale header text
     */
    private void fixGridHeader() {
        String message = "Iscrizioni previste in questo turno";
        Component comp = new HorizontalLayout();

        ((HorizontalLayout) comp).add(new Label(message));
        HeaderRow topRow = grid.prependHeaderRow();
        Grid.Column[] matrix = array.getColumnArray(grid);
        HeaderRow.HeaderCell informationCell = topRow.join(matrix);
        informationCell.setComponent(comp);
    }// end of method


    protected void apreDialogoIscrizione(ItemDoubleClickEvent evento) {
        turnoEntity = (Turno) currentItem;
        Iscrizione entityBean = (Iscrizione) evento.getItem();
        IscrizioneTurnoDialog dialogo = appContext.getBean(IscrizioneTurnoDialog.class, service, Iscrizione.class, turnoEntity);

        dialogo.openWam(entityBean, EAOperation.editNoDelete, this::save);
    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void save(Iscrizione entityBean, EAOperation operation) {
        int a = 87;
        updateItems();
//        if (service.save(entityBean, operation) != null) {
//            updateFiltri();
//            updateGrid();
//        }// end of if cycle
    }// end of method


}// end of class