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
        List<Iscrizione> items;
        String widthA = "4em";
        String widthB = "6em";
        String widthC = "12em";
        grid = new Grid(Iscrizione.class);
        Servizio servizio = ((Turno) currentItem).servizio;
        items = ((Turno) currentItem).iscrizioni;
        if (items != null) {
            grid.setItems(items);
        }// end of if cycle

        for (Object column : grid.getColumns()) {
            grid.removeColumn((Grid.Column) column);
        }// end of for cycle

        grid.setWidth("200em");
        //--aggiunge una colonna semplice
        Grid.Column colonnaOrdine = grid.addColumn("funzione");
        colonnaOrdine.setHeader("Funz");
        colonnaOrdine.setId("funzione");
        colonnaOrdine.setWidth(widthB);

//        //--aggiunge una colonna semplice
//        Grid.Column colonnaSiglaFunzione = grid.addColumn("sigla");
//        colonnaSiglaFunzione.setHeader("Sigla");
//        colonnaSiglaFunzione.setId("sigla");
//        colonnaSiglaFunzione.setWidth(widthB);

        //--aggiunge una colonna semplice
        Grid.Column colonnaMilite = grid.addColumn("milite");
        colonnaMilite.setHeader("Milite");
        colonnaMilite.setId("milite");
        colonnaMilite.setWidth(widthC);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaFunzioneObbligatoria = grid.addComponentColumn(funzione -> {
//            ACheckBox box = new ACheckBox("");
//            List<Funzione> funzioniDelServizio = servizio.funzioni;
//            if (funzioniDelServizio != null) {
//                for (Funzione funzServizio : funzioniDelServizio) {
//                    if (funzServizio.code.equals(((Funzione) funzione).code)) {
//                        box.setValue(funzServizio.obbligatoria);
//                    }// end of if cycle
//                }// end of for cycle
//            }// end of if cycle
//            return box;
//        });//end of lambda expressions
//        colonnaFunzioneObbligatoria.setHeader("Must");
//        colonnaFunzioneObbligatoria.setId("must");
//        colonnaFunzioneObbligatoria.setWidth(widthA);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaMilite = grid.addComponentColumn(funzione -> {
//            Milite milite = iscrizioneService.getByTurnoAndFunzione(((Turno) currentItem), funzione).getMilite();
//            if (milite != null) {
//                return new Label(milite.toString());
//            } else {
//                return new Label("Non ancora segnato");
//            }// end of if/else cycle
//        });//end of lambda expressions
//        colonnaMilite.setHeader("Milite");
//        colonnaMilite.setId("milite");
//        colonnaMilite.setWidth(widthB);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaLastModifica = grid.addComponentColumn(funzione -> {
//            LocalDateTime time = iscrizioneService.getByTurnoAndFunzione(((Turno) currentItem), funzione).getLastModifica();
//            if (time != null) {
//                return new Label(time.toString());
//            } else {
//                return new Label("Non ancora segnato");
//            }// end of if/else cycle
//        });//end of lambda expressions
//        colonnaLastModifica.setHeader("Time");
//        colonnaLastModifica.setId("last");
//        colonnaLastModifica.setWidth(widthB);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaDurata = grid.addComponentColumn(funzione -> {
//            int durata = iscrizioneService.getByTurnoAndFunzione(((Turno) currentItem), funzione).getDurataEffettiva();
//            if (durata > 0) {
//                return new Label(durata + "");
//            } else {
//                return new Label("0");
//            }// end of if/else cycle
//        });//end of lambda expressions
//        colonnaDurata.setHeader("Ore");
//        colonnaDurata.setId("durata");
//        colonnaDurata.setWidth(widthA);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaEsisteProblema = grid.addComponentColumn(funzione -> {
//            boolean status = iscrizioneService.getByTurnoAndFunzione(((Turno) currentItem), funzione).isEsisteProblema();
//            return new ACheckBox("", status);
//        });//end of lambda expressions
//        colonnaEsisteProblema.setHeader("Prob");
//        colonnaEsisteProblema.setId("problema");
//        colonnaEsisteProblema.setWidth(widthA);


        //--aggiunge una colonna calcolata
//        Grid.Column colonnaEdit = grid.addComponentColumn(funzione -> {
//            final Iscrizione iscrizioneDellaRiga = iscrizioneService.getByTurnoAndFunzione(currentItem, funzione);
//            Button edit = new Button("Edit", event -> dialogoIscrizione.open(iscrizioneDellaRiga, EAOperation.editDaLink, context));
//            edit.setIcon(new Icon("lumo", "edit"));
//            edit.addClassName("review__edit");
//            edit.getElement().setAttribute("theme", "tertiary");
//
//            return edit;
//        });//end of lambda expressions
//        colonnaEdit.setHeader("Iscr");
//        colonnaEdit.setId("iscrizione");
//        colonnaEdit.setWidth(widthA);

        //--aggiunge una colonna semplice
        Grid.Column colonnaLast = grid.addColumn("lastModifica");
        colonnaLast.setHeader("Last");
        colonnaLast.setId("last");
        colonnaLast.setWidth(widthC);

        //--aggiunge una colonna semplice
        Grid.Column colonnaInizio = grid.addColumn("inizio");
        colonnaInizio.setHeader("Inizio");
        colonnaInizio.setId("inizio");
        colonnaInizio.setWidth(widthC);

        //--aggiunge una colonna semplice
        Grid.Column colonnaFine = grid.addColumn("fine");
        colonnaFine.setHeader("Fine");
        colonnaFine.setId("fine");
        colonnaFine.setWidth(widthC);

        //--aggiunge una colonna semplice
        Grid.Column colonnaDurata = grid.addColumn("durataEffettiva");
        colonnaDurata.setHeader("H");
        colonnaDurata.setId("durataEffettiva");
        colonnaDurata.setWidth(widthA);

        //--aggiunge una colonna semplice
        Grid.Column colonnaProblema = grid.addColumn("esisteProblema");
        colonnaProblema.setHeader("?");
        colonnaProblema.setId("esisteProblema");
        colonnaProblema.setWidth(widthA);

        //--aggiunge una colonna semplice
        Grid.Column colonnaNote = grid.addColumn("note");
        colonnaNote.setHeader("Note");
        colonnaNote.setId("note");
        colonnaNote.setWidth(widthB);

        //--aggiunge una colonna semplice
        Grid.Column colonnaMail = grid.addColumn("notificaInviata");
        colonnaMail.setHeader("Mail");
        colonnaMail.setId("notificaInviata");
        colonnaMail.setWidth(widthB);

        //--header
        fixGridHeader();

        grid.addItemDoubleClickListener(event -> apreDialogoIscrizione((ItemDoubleClickEvent) event));
        formSubLayout.add(grid);

//        final FormLayout iscrizioniLayout = new FormLayout();
//        Div div;
////        iscrizioniLayout.add(grid);
//        iscrizioniLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("50em", 1));
//
//        iscrizioniLayout.addClassName("no-padding");
//        div = new Div(iscrizioniLayout);
//        div.addClassName("has-padding");
////        add(div);
////        return div;

//        return grid;
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

        dialogo.openWam(entityBean, EAOperation.edit, this::save, this::delete);
    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void save(AEntity entityBean, EAOperation operation) {
//        if (service.save(entityBean, operation) != null) {
//            updateFiltri();
//            updateGrid();
//        }// end of if cycle
    }// end of method


    protected void delete(AEntity entityBean) {
//        service.delete(entityBean);
//        Notification.show(entityBean + " successfully deleted.", 3000, Notification.Position.BOTTOM_START);
//
//        if (usaRefresh) {
//            updateFiltri();
//            updateGrid();
//        }// end of if cycle
    }// end of method

}// end of class