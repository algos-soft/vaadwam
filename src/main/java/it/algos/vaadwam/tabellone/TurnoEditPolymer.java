package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.components.NoteEditor;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Editor di iscrizione riservato agli admin.
 * Un admin può creare modificare e cancellare liberamente tutte le iscrizioni
 * Un admin può iscrivere se stesso o chiunque altro
 */
@Tag("turno-dialog")
@HtmlImport("src/views/tabellone/turno-dialog.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TurnoEditPolymer extends PolymerTemplate<TurnoEditModel> {

    @Autowired
    private ApplicationContext appContext;

    /**
     * Bottone Annulla
     */
    @Id("annulla")
    private Button bAnnulla;

    /**
     * Bottone Conferma
     */
    @Id("conferma")
    private Button bConferma;

    @Id("elimina")
    private Button bElimina;


    @Autowired
    private PreferenzaService pref;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private MiliteService militeService;

    @Autowired
    private ADateService dateService;

    @Getter
    private Turno turno;

    @Autowired
    private AArrayService array;

    private ITabellone tabellone;

    private Dialog dialogo;

    private boolean abilitaCancellaTurno;

    private boolean isNuovoTurno;

    // Componenti editor di singola iscrizione inseriti nel dialogo
    @Getter
    private ListaIscrizioni compIscrizioni;

    // contiene tutto il contenuto visualizzato nel dialogo
    @Id
    private Element container;

    @Id
    private Div areaiscrizioni;

    @Id
    private TimePicker pickerInizio;

    @Id
    private TimePicker pickerFine;

    @Id
    private Div noteEditorDiv;

    /**
     * @param tabellone            il tabellone di riferimento per effettuare le callbacks
     * @param dialogo              il dialogo contenitore
     * @param turno                il turno da mostrare
     * @param abilitaCancellaTurno abilita la funzionalità di cancellazione del turno
     * @param isNuovoTurno         true se il turno passato da editare è nuovo e non ancora salvato sul db
     */
    public TurnoEditPolymer(ITabellone tabellone, Dialog dialogo, Turno turno, boolean abilitaCancellaTurno, boolean isNuovoTurno) {
        this.tabellone = tabellone;
        this.dialogo = dialogo;
        this.turno = turno;
        this.abilitaCancellaTurno = abilitaCancellaTurno;
        this.isNuovoTurno = isNuovoTurno;

        // registra il riferimento al server Java nel client JS
        // necessario perché JS possa chiamare direttamente metodi Java
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

    }


    @PostConstruct
    private void init() {

        populateModel();

        // aggiunge il componente visualizzatore/editor delle note nel suo placeholder
        NoteEditor noteEditor = new NoteEditor();
        noteEditor.setClassName("noteTurnoEditor");
        noteEditor.setNote(getModel().getNote());
        noteEditor.addNoteChangedListener(new NoteEditor.NoteChangedListener() {
            @Override
            public void onNoteChanged(String newText, String oldText) {
                getModel().setNote(newText);
            }
        });
        noteEditorDiv.add(noteEditor);

        // crea e aggiunge l'area Iscrizioni
        compIscrizioni = buildCompIscrizioni();
        for (CompIscrizione comp : compIscrizioni) {
            areaiscrizioni.add(comp);
        }

        // listeners per la modifica dell'ora inizio e fine, aggiornano la lista iscrizioni interna al dialogo
        pickerInizio.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TimePicker, LocalTime>>) event -> getCompIscrizioni().setOraInizio(event.getValue()));
        pickerFine.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TimePicker, LocalTime>>) event -> getCompIscrizioni().setOraFine(event.getValue()));

//        bConferma.addClickShortcut(Key.ENTER);    // non metterla, va in conflitto con il dialogo edit note
        bConferma.addClickListener(e -> handleConferma());

        bAnnulla.addClickShortcut(Key.ESCAPE);
        bAnnulla.addClickListener(e -> handleAnnulla());

        manageElimina();


    }


    /**
     * Regola l'altezza massima del contenitore interno dinamicamente
     */
    @ClientCallable
    public void pageReady(int w, int h) {
        Style style = container.getStyle();

        // togliamo 80 pixel empiricamente
        style.set("max-height", h - 100 + "px");

    }


    /**
     * Riempie il modello con i dati del turno
     */
    private void populateModel() {

        // data di esecuzione del turno
        String data = dateService.get(turno.getGiorno(), EATime.completa);
        getModel().setGiorno(data);

        // descrizione estesa del servizio
        Servizio servizio = turno.getServizio();
        getModel().setServizio(servizio.descrizione);

        // orario (eventuale) del turno
        fixOrario();

        // note del turno visibili solo per turni 'non standard'
        getModel().setNote(turno.getNote());
        if (servizio.isOrarioDefinito()) { // servizio con orario definito
            getModel().setNoteVisibili(false);
        } else {  // servizio con orario non definito
            getModel().setNoteVisibili(true);
        }

        getModel().setAbilitaCancellaTurno(this.abilitaCancellaTurno);

    }


    /**
     * Orario del turno
     * <p>
     * Se il servizio prevede un orario definito, usa l'orario del servizio e lo mostra disabiitato <br>
     * Altrimenti, usa l'orario del turno, abilitato<br>
     */
    private void fixOrario() {

        Servizio servizio = turno.getServizio();

        LocalTime tInizio;
        LocalTime tFine;
        boolean editabile;
        if (servizio.isOrarioDefinito()) { // servizio con orario definito
            tInizio = servizio.getInizio();
            tFine = servizio.getFine();
            editabile = false;
        } else {  // servizio con orario non definito
            tInizio = turno.getInizio();
            tFine = turno.getFine();
            editabile = true;
        }

        // evitiamo i nulli che nel TimePicker creano problemi
//        if (tInizio==null){
//            tInizio=LocalTime.MIDNIGHT;
//        }
//        if (tFine==null){
//            tFine=LocalTime.MIDNIGHT;
//        }

        getModel().setOraInizio(dateService.getOrario(tInizio));
        getModel().setOraFine(dateService.getOrario(tFine));
        getModel().setOrarioTurnoEditabile(editabile);


    }


    /**
     * Crea la lista dei componenti per editare le singole iscrizioni
     */
    private ListaIscrizioni buildCompIscrizioni() {
        ListaIscrizioni componenti = new ListaIscrizioni();

        for (Iscrizione iscrizione : turno.getIscrizioni()) {
            CompIscrizione comp = appContext.getBean(CompIscrizione.class, iscrizione, this);
            componenti.add(comp);
        }

        return componenti;
    }


    /**
     * Evento lanciato dal bottone Annulla <br>
     */
    @EventHandler
    private void handleAnnulla() {
        tabellone.annullaDialogoTurno(dialogo);
    }


    /**
     * Evento lanciato dal bottone Conferma <br>
     * <p>
     * Recupera i dati di tutte le iscrizioni presenti <br>
     * Controlla che il milite non sia già segnato nel turno <br>
     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
     * Registra le modifiche (eventuali) al turno <br>
     * Torna al tabellone <br>
     */
    @EventHandler
    private void handleConferma() {

        // clona il turno corrente per mantenere lo stato pre modifiche
        Turno oldTurno = SerializationUtils.clone(turno);

        // aggiorna l'oggetto turno come da dialogo
        syncTurno();

        // lo passa al tabellone per la registrazione
        tabellone.confermaDialogoTurno(dialogo, turno, oldTurno, isNuovoTurno);

    }


    /**
     * Sincronizza le iscrizioni dell'oggetto Turno ricevuto nel costruttore
     * in base allo stato corrente delle iscrizioni contenute nel dialogo.
     */
    private void syncTurno() {

        List<Iscrizione> iscrizioni = turno.getIscrizioni();

        for (Iscrizione iscrizione : iscrizioni) {

            CompIscrizione ci = findCompIscrizione(iscrizione);
            if (ci != null) {
                syncIscrizione(iscrizione, ci);
            } else {
                log.error("Componente iscrizione non trovato per l'iscrizione " + iscrizione.getFunzione().getCode());
            }

        }

        // ora inizio turno
        String sOra = getModel().getOraInizio();
        LocalTime time;
        if (!StringUtils.isEmpty(sOra)) {
            try {
                time = dateService.getLocalTimeHHMM(sOra);
                turno.setInizio(time);
            } catch (Exception e) {
                log.error("can't parse " + sOra + "as LocalTime", e);
            }
        } else {
            turno.setInizio(null);
        }

        // ora fine turno
        sOra = getModel().getOraFine();
        if (!StringUtils.isEmpty(sOra)) {
            try {
                time = dateService.getLocalTimeHHMM(sOra);
                turno.setFine(time);
            } catch (Exception e) {
                log.error("can't parse " + sOra + "as LocalTime", e);
            }
        } else {
            turno.setFine(null);
        }


        turno.setNote(getModel().getNote());

    }

    /**
     * Sincronizza una singola iscrizione del turno con quella del dialogo
     */
    private void syncIscrizione(Iscrizione iscrizione, CompIscrizione compIscrizione) {

        // sync milite
        String idMilite = compIscrizione.getIdMiliteSelezionato();
        if (idMilite != null) {
            Milite milite = militeService.findById(idMilite);
            iscrizione.setMilite(milite);
        } else {
            iscrizione.setMilite(null);
        }

        // sync timestamps
        iscrizione.setInizio(compIscrizione.getOraInizio());
        iscrizione.setFine(compIscrizione.getOraFine());

        // sync durata
        iscrizione.setLastModifica(LocalDateTime.now());
        iscrizioneService.setDurataMinuti(iscrizione);

        // sync note
        iscrizione.setNote(compIscrizione.getNote());

    }


    /**
     * Cerca il componente iscrizione in base all'oggetto Iscrizione
     * con cui è stato costruito
     */
    private CompIscrizione findCompIscrizione(Iscrizione iscrizione) {
        CompIscrizione compOut = null;
        for (CompIscrizione comp : compIscrizioni) {
            if (comp.getIscrizione().equals(iscrizione)) {
                compOut = comp;
                break;
            }
        }
        return compOut;
    }

    /**
     * Gestione bottone Elimina Turno
     */
    private void manageElimina() {
        bElimina.addClickListener(e -> {

            // bottone elimina del dialogo di conferma
            Button bElimina = new Button();
            bElimina.getStyle().set("background-color", "red");
            bElimina.getStyle().set("color", "white");
            bElimina.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                if (abilitaCancellaTurno) {  // controllo di sicurezza per non affidarsi solo all'invisibilità del pulsante nella GUI
                    tabellone.eliminaTurno(dialogo, turno);
                }
            });

            String warningIscritti = "";
            for (CompIscrizione comp : getCompIscrizioni()) {
                if (!StringUtils.isEmpty(comp.getIdMiliteSelezionato())) {
                    warningIscritti = "Attenzione! Ci sono dei militi già iscritti. ";
                    break;
                }
            }

            ConfirmDialog
                    .createWarning()
                    .withCaption("Conferma eliminazione turno")
                    .withMessage(warningIscritti + "Sei sicuro di voler eliminare il turno?")
                    .withAbortButton(ButtonOption.caption("Annulla"), ButtonOption.icon(VaadinIcon.CLOSE))
                    .withButton(bElimina, ButtonOption.caption("Elimina"), ButtonOption.focus(), ButtonOption.icon(VaadinIcon.TRASH))
                    .open();

        });

    }

    /**
     * Ritorna l'ora correntemente mostrata nel picker ora inizio turno
     */
    public LocalTime getOraInizioPicker() {
        LocalTime localTime = null;
        try {
            localTime = dateService.getLocalTimeHHMM(getModel().getOraInizio());
        } catch (Exception e) {
            log.error("can't parse time from picker start", e);
        }
        return localTime;
    }

    /**
     * Ritorna l'ora correntemente mostrata nel picker ora fine turno
     */
    public LocalTime getOraFinePicker() {
        LocalTime localTime = null;
        try {
            localTime = dateService.getLocalTimeHHMM(getModel().getOraFine());
        } catch (Exception e) {
            log.error("can't parse time from picker end", e);
        }
        return localTime;
    }


    /**
     * Lista di oggetti CompIscrizione con funzionalità aggiuntive
     */
    class ListaIscrizioni extends ArrayList<CompIscrizione> {

        /**
         * Assegna l'ora di inizio a tutti i camponenti interni che hanno un iscritto
         */
        public void setOraInizio(LocalTime value) {
            for (CompIscrizione comp : this) {
                if (comp.getIdMiliteSelezionato() != null) {
                    comp.setOraInizio(value);
                }
            }
        }

        /**
         * Assegna l'ora di fine a tutti i camponenti interni che hanno un iscritto
         */
        public void setOraFine(LocalTime value) {
            for (CompIscrizione comp : this) {
                if (comp.getIdMiliteSelezionato() != null) {
                    comp.setOraFine(value);
                }
            }
        }

    }

}
