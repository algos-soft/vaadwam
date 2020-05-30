package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.*;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.enumeration.EAWamLogType;
import it.algos.vaadwam.modules.log.WamLogService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.*;

import static it.algos.vaadwam.tabellone.TurnoGenWorker.*;
import static java.time.temporal.ChronoUnit.DAYS;

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

    @Id
    private ProgressBar progressBar;

    private RadioButtonGroup<String> radioGroup;

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private WamLogService wamLogService;

    @Autowired
    private ADateService dateService;

    @Setter
    private CompletedListener completedListener;

    private TurnoGenWorker worker;

    private boolean working;    // acceso quando inizia l'operazione

    private UI ui;

    private TurnoGenWorker.EsitoGenerazioneTurni esito;

    private static final String[] TITLES = new String[]{"L", "M", "M", "G", "V", "S", "D"};

    private static final String OPTION_GENERA = "Genera";
    private static final String OPTION_CANCELLA = "Cancella";

    private static final String SUBTITLE_CREA = null;
    private static final String SUBTITLE_CANCELLA = "Verranno cancellati solo i turni vuoti (senza nessun iscritto)";


    @PostConstruct
    private void init() {

        ui=UI.getCurrent();

        picker1.setLocale(Locale.ITALY);
        picker2.setLocale(Locale.ITALY);

        populateModel();

        // bottone Chiudi
        bChiudi.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            //EsitoGenerazioneTurni esito = new EsitoGenerazioneTurni(0, false, false, null, null);
            fireCompletedListener(esito);
        });

        // bottone Esegui
        bEsegui.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {

            if(!working){

                String error = validate();

                if (error == null) {

                    String word = "";
                    if(isCrea()){
                        word = "creazione";
                    }else{
                        word = "cancellazione";
                    }

                    Button bConferma = new Button();

                    String msg = "Confermi la " + word + " dei turni selezionati?";
                    ConfirmDialog dialog = ConfirmDialog.createQuestion()
                            .withMessage(msg)
                            .withButton(new Button(), ButtonOption.caption("Annulla"), ButtonOption.closeOnClick(true))
                            .withButton(bConferma, ButtonOption.caption("Conferma"), ButtonOption.focus(), ButtonOption.closeOnClick(true));

                    bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event1 -> {
                        dialog.close();
                        execute();
                    });

                    dialog.open();

                } else {    // errore di validazione dei dati
                    Notification.show(error, 3000, Notification.Position.MIDDLE);
                }

            }else{  // in esecuzione

                Button bConferma = new Button();
                bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event1 -> {
                    worker.abort();
                });

                String msg = "Interrompo l'operazione?";
                ConfirmDialog.createQuestion()
                        .withMessage(msg)
                        .withButton(new Button(), ButtonOption.caption("Annulla"))
                        .withButton(bConferma, ButtonOption.caption("Interrompi"), ButtonOption.focus())
                        .open();

            }


        });

        setDialogTitle(OPTION_GENERA);
        getModel().setSubtitle(SUBTITLE_CREA);

        radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems(OPTION_GENERA, OPTION_CANCELLA);
        radioGroup.setValue(OPTION_GENERA);
        radiodiv.add(radioGroup);

        radioGroup.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>) event -> {
            setDialogTitle(event.getValue());
            if(isCrea()){
                getModel().setSubtitle(SUBTITLE_CREA);
            }else{
                getModel().setSubtitle(SUBTITLE_CANCELLA);
            }
        });

        progressBar.setVisible(false);

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


    /**
     * Controla se è selezionata creazione o cancellazione
     */
    private boolean isCrea(){
        boolean crea=true;
        switch (radioGroup.getValue()) {
            case OPTION_GENERA: {
                crea=true;
                break;
            }
            case OPTION_CANCELLA: {
                crea=false;
                break;
            }
        }
        return crea;
    }

    private void fireCompletedListener(TurnoGenWorker.EsitoGenerazioneTurni esito) {
        if (completedListener != null) {
            completedListener.onCompleted(esito);
        }
    }


    interface CompletedListener {
        void onCompleted(TurnoGenWorker.EsitoGenerazioneTurni esito);
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

        // L'intervallo non deve superare 1 anno
        int daysBetween = (int)DAYS.between(data1, data2);
        if (daysBetween>365) {
            return "L'intervallo massimo è di 1 anno.";
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
        worker=appContext.getBean(TurnoGenWorker.class, isCrea(), getDataStart(), getDataEnd(), buildListaServiziGiorni());
        worker.addPropertyChangeListener(this);
        bChiudi.setEnabled(false);
        worker.startWork();

    }

    /**
     * Costruisce la lista dei giorni della settimana con relativi servizi
     * in base al set di checkboxes correntemente accesi
     */
    private List<ServiziGiornoSett> buildListaServiziGiorni(){
        List<ServiziGiornoSett> lista = new ArrayList<>();

        for(TurnoGenRiga riga : getModel().getRighe()){
            List<TurnoGenFlag> flags = riga.getFlags();
            for(int i=0; i<flags.size();i++){
                TurnoGenFlag flag = flags.get(i);
                if(flag.isOn()){
                    ServiziGiornoSett item = findOrCreate(lista, i);
                    Servizio servizio = servizioService.findByKeyUnica(riga.getNomeServizio());
                    item.addServizio(servizio);
                }
            }
        }

        return lista;
    }

    /**
     * Recupera dalla lista ServiziGiornoSett quello relativo all'indice giorno percificato.
     * Se non esiste lo crea ora e lo aggiunge alla lista.
     */
    private ServiziGiornoSett findOrCreate(List<ServiziGiornoSett> lista, int idx){

        for(ServiziGiornoSett item : lista){
            if(item.getIdxGiornoSett()==idx){
                return item;
            }
        }

        ServiziGiornoSett newItem = new ServiziGiornoSett(idx);
        lista.add(newItem);
        return newItem;

    }


    /**
     * I questo metodo viene invocato da un thread separato diverso dallo UI thread.
     * Qualsiasi azione sulla GUI che parte da qui va eseguita tramite il metodo ui.access().
     */
    @SneakyThrows
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        switch (propName) {

            case PROPERTY_PROGRESS: {
                float value = (float) evt.getNewValue();
                ui.access(() -> {
                    progressBar.setValue(value);
                });
                break;
            }

            case PROPERTY_STATUS: {
                String value = (String) evt.getNewValue();
                String oldValue = (String) evt.getOldValue();
                switch (value) {
                    case STATUS_RUNNING:

                        if(oldValue!=STATUS_RUNNING){ // just started (or restarted)

                            working=true;

                            ui.access(() -> {

                                logStart();

                                bEsegui.setText("Interrompi");
                                progressBar.setVisible(true);
                                progressBar.setValue(0);
                            });
                        }
                        break;

                    case STATUS_COMPLETED:

                        working=false;
                        esito=worker.getEsito();

                        ui.access(() -> {

                            completedListener.onCompleted(esito);

                            bEsegui.setText("Esegui");
                            bChiudi.setEnabled(true);
                            progressBar.setVisible(false);

                            ConfirmDialog.createInfo()
                                    .withMessage("Terminato. "+esito.getTestoAzione()+" "+esito.getQuanti()+" turni.")
                                    .withButton(new Button(), ButtonOption.caption("Chiudi"), ButtonOption.closeOnClick(true))
                                    .open();

                            logEnd();

                        });
                        worker.removePropertyChangeListener(this);
                        break;

                    case STATUS_ABORTED:

                        working=false;
                        esito=worker.getEsito();

                        ui.access(() -> {

                            bEsegui.setText("Esegui");
                            bChiudi.setEnabled(true);
                            progressBar.setVisible(false);

                            ConfirmDialog.createInfo()
                                    .withMessage("Operazione interrotta. "+esito.getTestoAzione()+" "+esito.getQuanti()+" turni.")
                                    .withButton(new Button(), ButtonOption.caption("Chiudi"), ButtonOption.closeOnClick(true))
                                    .open();

                            logEnd();

                        });
                        worker.removePropertyChangeListener(this);
                        break;
                }
            }
        }
    }

    /**
     * Logga l'avvio della operazione
     */
    private void logStart(){
        String dal = dateService.get(getDataStart());
        String al = dateService.get(getDataEnd());
        EAWamLogType logType;
        String op;
        if(isCrea()){
            logType=EAWamLogType.multiCreazioneTurni;
            op="creazione";
        }else{
            logType=EAWamLogType.multiCancellazioneTurni;
            op="cancellazione";
        }
        wamLogService.log(logType, "Avviata "+op+" automatica turni dal "+dal+" al "+al);

    }

    /**
     * Logga l'esito della operazione
     */
    private void logEnd(){

        String op;
        EAWamLogType logType;
        if(isCrea()){
            op="creazione";
            logType = EAWamLogType.multiCreazioneTurni;
        }else {
            op="cancellazione";
            logType = EAWamLogType.multiCancellazioneTurni;
        }

        String sEsito;
        if(esito.isAborted()){
            sEsito="interrotta";
        }else{
            sEsito="conclusa correttamente";
        }

        List<LocalDate> giorni = esito.getGiorni();
        String sPeriodo="";
        if(giorni.size()>0){
            Collections.sort(giorni);
            String sMinDate=dateService.get(giorni.get(0));
            String sMaxDate=dateService.get(giorni.get(giorni.size()-1));
            sPeriodo="dal "+sMinDate+" al "+sMaxDate;
        }


        wamLogService.log(logType, op+" automatica turni "+sEsito+" - "+esito.getTestoAzione()+" "+esito.getQuanti()+" turni "+sPeriodo);
    }

}
