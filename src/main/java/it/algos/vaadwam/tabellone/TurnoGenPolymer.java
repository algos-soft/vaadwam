package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.*;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.*;

/**
 * Generatore di turni per il tabellone
 */
@Tag("turno-gen")
@HtmlImport("src/views/tabellone/turnogen-polymer.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TurnoGenPolymer extends PolymerTemplate<TurnoGenModel> implements PropertyChangeListener {

    @Id
    private Button bChiudi;

    @Id
    private Button bEsegui;

    @Id
    private DatePicker picker1;

    @Id
    private DatePicker picker2;

    @Id
    private Div radiodiv;

    private RadioButtonGroup<String> radioGroup;

    @Autowired
    private ServizioService servizioService;

    @Setter
    private CompletedListener completedListener;

    private TurnoGenWorker worker;

    private boolean working;    // acceso quando inizia l'operazione

    private static final String[] TITLES = new String[]{"L", "M", "M", "G", "V", "S", "D"};

    private static final String OPTION_GENERA = "Genera";
    private static final String OPTION_CANCELLA = "Cancella";


    @PostConstruct
    private void init() {

        picker1.setLocale(Locale.ITALY);
        picker2.setLocale(Locale.ITALY);

        populateModel();

        // bottone Chiudi
        bChiudi.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            EsitoGenerazioneTurni esito = new EsitoGenerazioneTurni(0, false, false, null, null);
            fireCompletedListener(esito);
        });

        // bottone Esegui
        bEsegui.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if(!working){

                String error = validate();
                if (error == null) {

                    Button bConferma = new Button();
                    bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event1 -> {
                        execute();
                    });

                    String word = "";
                    switch (radioGroup.getValue()) {
                        case OPTION_GENERA: {
                            word = "creazione";
                            break;
                        }
                        case OPTION_CANCELLA: {
                            word = "cancellazione";
                            break;
                        }
                    }

                    String msg = "Confermi la " + word + " dei turni selezionati?";
                    ConfirmDialog.createQuestion()
                            .withMessage(msg)
                            .withButton(new Button(), ButtonOption.caption("Annulla"), ButtonOption.closeOnClick(true))
                            .withButton(bConferma, ButtonOption.caption("Conferma"), ButtonOption.focus(), ButtonOption.closeOnClick(true))
                            .open();

                } else {    // errore di validazione dei dati
                    Notification.show(error, 3000, Notification.Position.MIDDLE);
                }

            }else{  // in esecuzione

                Button bConferma = new Button();
                bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event1 -> {
                    worker.abort();
                });

                String msg = "Interrompo l'opeazione?";
                ConfirmDialog.createQuestion()
                        .withMessage(msg)
                        .withButton(new Button(), ButtonOption.caption("Annulla"))
                        .withButton(bConferma, ButtonOption.caption("Interrompi"), ButtonOption.focus())
                        .open();

            }


        });

        setDialogTitle(OPTION_GENERA);

        radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems(OPTION_GENERA, OPTION_CANCELLA);
        radioGroup.setValue(OPTION_GENERA);
        radiodiv.add(radioGroup);

        radioGroup.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>) event -> {
            setDialogTitle(event.getValue());
        });
    }


    private void setDialogTitle(String option) {
        getModel().setTitle(option + " turni");
    }

    /**
     * Riempie il modello dati
     */
    private void populateModel() {
        List<Servizio> servizi = servizioService.findAllStandardVisibili();
        List<TurnoGenRiga> righe = new ArrayList<>();
        int rowId = 0;
        for (Servizio servizio : servizi) {
            TurnoGenRiga riga = new TurnoGenRiga(rowId);
            riga.setNomeServizio(servizio.getCode());

            List<TurnoGenFlag> flags = new ArrayList<>();
            for (int colId = 0; colId < 7; colId++) {
                TurnoGenFlag flagStatus = new TurnoGenFlag(rowId, colId);
                flags.add(flagStatus);
//                flagStatus.setOn(colId % 2 == 0);
            }
            riga.setFlags(flags);
            righe.add(riga);

            rowId++;

        }
        getModel().setRighe(righe);

        getModel().setTitoliGiorno(Arrays.asList(TITLES));

    }


    private void fireCompletedListener(EsitoGenerazioneTurni esito) {
        if (completedListener != null) {
            completedListener.onCompleted(esito);
        }
    }


    interface CompletedListener {
        void onCompleted(EsitoGenerazioneTurni esito);
    }



    public class GridRow {

        @Getter
        private Servizio servizio;

        private boolean[] flags;

        public GridRow(Servizio servizio) {
            this.servizio = servizio;
            this.flags = new boolean[7];
        }

    }


    @EventHandler
    private void clickRow(@ModelItem TurnoGenRiga riga) {
        List<TurnoGenFlag> flags = riga.getFlags();
        if (allIsOn(flags)) {
            setAllFlags(flags, false);
        } else {
            setAllFlags(flags, true);
        }

    }

    private boolean allIsOn(List<TurnoGenFlag> flags) {
        for (TurnoGenFlag flag : flags) {
            if (!flag.isOn()) {
                return false;
            }
        }
        return true;
    }

    private void setAllFlags(List<TurnoGenFlag> flags, boolean state) {
        for (int i = 0; i < flags.size(); i++) {
            flags.get(i).setOn(state);
        }
    }

    @EventHandler
    private void clickCol(@RepeatIndex int itemIndex) {

        boolean allOn = true;
        for (TurnoGenRiga riga : getModel().getRighe()) {
            List<TurnoGenFlag> flagsRiga = riga.getFlags();
            if (!flagsRiga.get(itemIndex).isOn()) {
                allOn = false;
                break;
            }
        }

        for (TurnoGenRiga riga : getModel().getRighe()) {
            List<TurnoGenFlag> flagsRiga = riga.getFlags();
            flagsRiga.get(itemIndex).setOn(!allOn);
        }

    }

    @EventHandler
    private void clickFlag(@RepeatIndex int itemIndex, @ModelItem TurnoGenRiga item) {
//        TurnoGenRiga riga = findRigaById(item.getId());
//        List<TurnoGenFlag> flags = riga.getFlags();
//        TurnoGenFlag flag = flags.get(itemIndex);
//        flag.setOn(!flag.isOn());
    }

    /**
     * Recupera una riga dal modello per id
     */
    private TurnoGenRiga findRigaById(int id) {
        TurnoGenRiga found = null;
        for (TurnoGenRiga riga : getModel().getRighe()) {
            if (riga.getId() == id) {
                found = riga;
            }
        }
        return found;
    }


    /**
     * Valida i dati del dialogo prima dell'esecuzione.
     *
     * @return null se sono validi, motivazione se non lo sono
     */
    private String validate() {

        LocalDate data1 = getDataStart();
        LocalDate data2 = getDataEnd();

        // le due date non devono essere nulle
        if (data1 == null || data2 == null) {
            return "Le date di inizio e di fine devono essere specificate.";
        }

        // le due date devono essere uguali o consecutive
        if (data2.isBefore(data1)) {
            return "Le date di inizio e di fine devono essere consecutive.";
        }

        // la prima data non deve essere precedente a oggi
        if (data1.isBefore(LocalDate.now())) {
            return "La date di inizio non può essere precedente a oggi.";
        }

        // almeno un checkbox deve essere acceso
        boolean selezionati = false;
        for (TurnoGenRiga riga : getModel().getRighe()) {
            List<TurnoGenFlag> flags = riga.getFlags();
            for (TurnoGenFlag flag : flags) {
                if (flag.isOn()) {
                    selezionati = true;
                    break;
                }
            }
        }
        if (!selezionati) {
            return "Non ci sono giorni / turni selezionati.";
        }

        return null;
    }

    private LocalDate getDataStart() {
        return picker1.getValue();
    }

    private LocalDate getDataEnd() {
        return picker2.getValue();
    }


    /**
     * Esecuzione della operazione richiesta
     */
    private void execute() {
        worker = new TurnoGenWorker(UI.getCurrent());
        worker.addPropertyChangeListener(this);
        worker.startWork();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        switch (propName) {

            case TurnoGenWorker.PROPERTY_PROGRESS: {
                break;
            }

            case TurnoGenWorker.PROPERTY_STATUS: {
                String value = (String) evt.getNewValue();
                String oldValue = (String) evt.getOldValue();
                switch (value) {
                    case TurnoGenWorker.STATUS_RUNNING:
                        if(oldValue==null){ // just started
                            working=true;
                            bEsegui.setText("Interrompi");
                        }

                    case TurnoGenWorker.STATUS_COMPLETED:

                        boolean create = radioGroup.getValue().equals(OPTION_GENERA);
                        EsitoGenerazioneTurni esito = new EsitoGenerazioneTurni(0, true, create, getDataStart(), getDataEnd());
                        completedListener.onCompleted(esito);

                        ConfirmDialog.createInfo()
                                .withMessage("Terminato.")
                                .withCloseButton()
                                .open();

                        break;

                    case TurnoGenWorker.STATUS_ABORTED:
                        working=false;
                        bEsegui.setText("Interrompi");

                        ConfirmDialog.createInfo()
                                .withMessage("Operazione interrotta.")
                                .withCloseButton()
                                .open();

                        break;
                }
            }
        }
    }


    @Data
    class EsitoGenerazioneTurni {
        private int quanti;
        private boolean aborted;
        private boolean create;
        private LocalDate dataStart;
        LocalDate dataEnd;

        /**
         * @param quanti    quanti turni sono stati generati o cancellati
         * @param aborted   se l'operazione è stata abortita
         * @param create    true se ha creato turni, false se ha cancellato turni
         * @param dataStart data del primo turno creato/cancellato
         * @param dataEnd   data dell'ultimo turno creato/cancellato
         */
        public EsitoGenerazioneTurni(int quanti, boolean aborted, boolean create, LocalDate dataStart, LocalDate dataEnd) {
            this.quanti = quanti;
            this.aborted = aborted;
            this.create = create;
            this.dataStart = dataStart;
            this.dataEnd = dataEnd;
        }
    }


}
