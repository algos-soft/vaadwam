package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Java wrapper of the polymer element `turno-edit`
 */
@Tag("turno-dialog")
@HtmlImport("src/views/tabellone/turno-dialog.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TurnoEditPolymerNew extends PolymerTemplate<TurnoEditModel>  {


    @Autowired
    protected TabelloneService tabelloneService;

    /**
     * Milite loggato al momento
     */
    protected Milite militeLoggato;

    @Autowired
    ApplicationContext appContext;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("annulla")
    private Button annulla;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("conferma")
    private Button conferma;

    @Autowired
    private PreferenzaService pref;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ADateService dateService;

    @Autowired
    private MiliteService militeService;

    private Turno turnoEntity = null;

    @Autowired
    private ATextService text;

    @Autowired
    private AArrayService array;

    //--wrapper per avere disponibili contemporaneamente Milite e milite.username
    private List<TurnoIscrizione> listaTurnoIscrizioni;

    //--modello dati per il collegamento TurnoEditPolymer con turno-edit.html
    //--serve per tutte le property ESCLUSI i Button 'annulla' e 'conferma'
    //--che sono oggetti creati in questa classe Java col loro @ID di collegamento
    private List<TurnoIscrizioneModel> listaTurnoIscrizioniModello;

    private ITabellone tabellone;

    private Dialog dialogo;

    private boolean nuovo;

    @Id
    private Element container;


    public TurnoEditPolymerNew() {
    }


    /**
     * @param tabellone il tabellone di riferimento per effettuare le callbacks
     * @param dialogo il dialogo contenitore
     * @param turno il turno da mostrare
     * @param nuovoTurno se si tratta di nuovo turno
     */
    public TurnoEditPolymerNew(ITabellone tabellone, Dialog dialogo, Turno turno, boolean nuovoTurno) {
        this.tabellone=tabellone;
        this.dialogo=dialogo;
        this.turnoEntity=turno;
        this.nuovo =nuovoTurno;

        // registra il riferimento al server Java nel client JS
        // necessario perché JS possa chiamare direttamente metodi Java
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

    }

    /**
     * Regola l'altezza massima del contenitore interno dinamicamente
     */
    @ClientCallable
    public void pageReady(int w, int h){
        Style style = container.getStyle();

        // togliamo 80 pixel empiricamente
        style.set("max-height", h-100+"px");

    }




    @PostConstruct
    private void init(){
        layoutPolymer();
    }



    /**
     * Regola i dati da presentare in base al turno selezionato
     */
    private void layoutPolymer() {

        //--Data completa (estesa) del giorno di esecuzione del turno
        String data = turnoService.getGiornoTxt(turnoEntity);
        getModel().setGiorno(data);

        //--Descrizione estesa del servizio
        Servizio servizio = turnoEntity.getServizio();
        getModel().setServizio(servizio.descrizione);

        //--Orario (eventuale) del turno
        fixOrario();

        //--Regolazione delle iscrizioni
        fixIscrizioni();

        //--Regolazioni standard di default del bottone 'Annulla'
        fixAnnulla();

        //--Regolazioni standard di default del bottone 'Conferma'
        fixConferma();

    }




    /**
     * Orario (eventuale) del turno
     * <p>
     * Se il servizio ha un orario definito, lo presenta in html come 'div' <br>
     * Se il servizio non ha un orario definito, lo presenta in html come due 'time-picker' <br>
     * Regola il valore del modello-dati di questo componente <br>
     */
    private void fixOrario() {
        String orario;
        Servizio servizio = null;

        if (turnoEntity != null) {
            servizio = turnoEntity.getServizio();
        }

        if (servizio != null) {
            if (pref.isBool(MOSTRA_ORARIO_SERVIZIO)) {
                if (servizio.isOrarioDefinito()) {
                    orario = servizioService.getOrarioLungo(servizio);
                    getModel().setOrario(orario);
                    getModel().setUsaOrarioLabel(true);
                    getModel().setUsaOrarioPicker(false);
                } else {
                    getModel().setInizioExtra(servizio.getInizio().toString());
                    getModel().setFineExtra(servizio.getFine().toString());
                    getModel().setUsaOrarioLabel(false);
                    getModel().setUsaOrarioPicker(true);
                }
            }
        }

    }


    /**
     * Aggiunge al modello (e quindi alla pagina turno-edit.html) la lista delle iscrizioni <br>
     * <p>
     * Qui la lista viene costruita con i valori di turno ed iscrizione provenienti dal mongoDB <br>
     * Successivamente i valori vengono regolati anche in maniera dinamica <br>
     * Ci possono essere n iscrizioni, senza limiti (anche se probabilmente non oltre le 4) <br>
     * <p>
     * Ogni iscrizione (su due righe) ha:
     * Nella prima riga un bottone con icona e sigla della funzione
     * Nella prima riga un bottone col nome del milite
     * Nella seconda riga un timePicker per l'inizio del servizio per il milite specifico
     * Nella seconda riga un textEdit per eventuali note del milite specifico
     * Nella seconda riga un timePicker per la fine del servizio per il milite specifico
     */
    private void fixIscrizioni() {
        //--wrapper disponibile per elaborazioni
        listaTurnoIscrizioni = tabelloneService.getTurnoIscrizioni(turnoEntity);

        //--modello dati per il collegamento TurnoEditPolymer con turno-edit.html
        //--serve per tutte le property ESCLUSI i Button 'annulla' e 'conferma'
        //--che sono oggetti creati in questa classe Java col loro @ID di collegamento
        listaTurnoIscrizioniModello = tabelloneService.getTurnoIscrizioniModello(listaTurnoIscrizioni);

        //--prepara il modello dati per la vista
        getModel().setIscrizioni(listaTurnoIscrizioniModello);

        //--regolazione iniziale, successivamente richiamata ad ogni modifica dei dati del Client
        regolaIscrizioni();
    }


    /**
     * Regola abilita/disabilita di tutte le iscrizioni <br>
     * Chiamata ad ogni modifica dei dati del Client <br>
     * <p>
     * Controlla se siamo loggati come developer, come admin o come user <br>
     * Recupera le funzioni abilitate del milite loggato <br>
     * <p>
     * L'iscrizione per cui non si hanno i permessi è disabilitata. <br>
     * Eventualmente differenziare col colore di scadenza del turno. <br>
     * <p>
     * L'iscrizione per cui si hanno i permessi è abilitata se vuota.
     * Eventualmente differenziare col colore di scadenza del turno.
     * Se è già segnato un milite (non quello loggato) è disabilitata.
     * Se è già segnato il milite loggato è abilitata per permettere la cancellazione (entro il tempo previsto).
     * <p>
     * Controlla se il milite loggato è già segnato in una iscrizione. Se è così disabilita tutte le altre <br>
     * Disabilita le iscrizioni che hanno già un milite segnato <br>
     * Disabilita le iscrizioni che hanno una funzione non abilitata per il milite loggato <br>
     * Abilita le iscrizioni rimanenti <br>
     */
    protected void regolaIscrizioni() {
        Milite militeIsc;
        boolean militeLoggatoGiaSegnato = false;

        this.militeLoggato = militeService.getMilite();

        // @todo per adesso
        // @todo Controlla se siamo loggati come developer, come admin o come user <br>
        if (militeLoggato == null) {
            return;
        }// end of if cycle

        //--Se siamo nello storico, disabilita tutte le iscrizioni (developer ed amdin esclusi)
        // @todo per adesso
        // @todo dovrà permettere al developer e all'admin di entrare <br>
        if (tabelloneService.isStorico(turnoEntity)) {
            disabilitaAll();
            return;
        }// end of if cycle

        //--Controlla se il milite loggato è già segnato in una iscrizione.
        //--Quella segnata viene abilitata. Tutte le altre disabilitate.
        if (array.isValid(listaTurnoIscrizioni)) {
            for (TurnoIscrizione turnoIscr : listaTurnoIscrizioni) {
                militeIsc = turnoIscr.militeEntity;
                if (militeIsc != null && militeIsc.id.equals(militeLoggato.id)) {
                    militeLoggatoGiaSegnato = true;
                    turnoIscr.abilitata = true;
                    turnoIscr.abilitataPicker = true;
                } else {
                    turnoIscr.abilitata = false;
                    turnoIscr.abilitataPicker = false;
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        // Se il milite loggato non è segnato nel turno
        // abilita le iscrizioni abilitate per il milite loggato e senza un altro milite già segnato
        if (!militeLoggatoGiaSegnato) {
            abilitaOnly();
        }// end of if cycle

        //--sincronizza il modello
        listaTurnoIscrizioniModello = tabelloneService.getTurnoIscrizioniModello(listaTurnoIscrizioni);
        getModel().setIscrizioni(listaTurnoIscrizioniModello);

    }// end of method


    /**
     * Se siamo nello storico, disabilita tutte le iscrizioni (developer ed amdin esclusi)
     */
    private void disabilitaAll() {
        if (array.isValid(listaTurnoIscrizioniModello)) {
            for (TurnoIscrizioneModel turnoModello : listaTurnoIscrizioniModello) {
                turnoModello.setAbilitata(false);
                turnoModello.setAbilitataPicker(false);
            }// end of for cycle
        }// end of if cycle

        getModel().setIscrizioni(listaTurnoIscrizioniModello);
    }// end of method


    /**
     * Se il milite loggato non è segnato nel turno
     * Recupera le funzioni abilitate del milite loggato
     * Abilita le iscrizioni abilitate per il milite loggato e senza un altro milite già segnato
     */
    private void abilitaOnly() {
        List<String> listaIDFunzioniAbilitate;
        listaIDFunzioniAbilitate = militeLoggato != null ? militeService.getListaIDFunzioni(militeLoggato) : null;
        boolean iscrizioneAbilitataMiliteLoggato;
        boolean iscrizioneNonSegnata;

        if (array.isValid(listaTurnoIscrizioni)) {
            for (TurnoIscrizione turnoIscr : listaTurnoIscrizioni) {
                iscrizioneAbilitataMiliteLoggato = listaIDFunzioniAbilitate.contains(turnoIscr.funzioneEntity.id);
                iscrizioneNonSegnata = turnoIscr.militeEntity == null;
                turnoIscr.abilitata = iscrizioneAbilitataMiliteLoggato && iscrizioneNonSegnata;
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Regolazioni standard di default del bottone 'Annulla' <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixAnnulla() {
        annulla.setText("Annulla");
        annulla.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            annulla.addClickShortcut(Key.ESCAPE);
        }// end of if cycle
        annulla.addClickListener(e -> handleAnnulla());
        annulla.getElement().setAttribute("title", "Ritorno al tabellone");
    }


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixConferma() {
        conferma.setText("Conferma");
        conferma.setIcon(new Icon(VaadinIcon.CHECK));
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            conferma.addClickShortcut(Key.ENTER);
        }// end of if cycle
        conferma.addClickListener(e -> handleConferma());
        conferma.setEnabled(false);
    }


    /**
     * Java event handler on the server, run asynchronously <br>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Proviene dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
     * <p>
     * Se il milite era segnato, viene cancellato <br>
     * Se non c'erano militi segnati, viene segnato il milite loggato al momento <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleClickMilite(@ModelItem TurnoIscrizioneModel item) {
        if (array.isValid(listaTurnoIscrizioni)) {
            for (TurnoIscrizione turnoIsc : listaTurnoIscrizioni) {
                if (turnoIsc.keyTag.equals(item.getKeyTag())) {
                    if (turnoIsc.militeEntity == null) {
                        turnoIsc.militeEntity = militeLoggato;
                        turnoIsc.militetxt = militeLoggato.username;
                    } else {
                        turnoIsc.militeEntity = null;
                        turnoIsc.militetxt = VUOTA;
                    }// end of if/else cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        regolaIscrizioni();
        conferma.setEnabled(true);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Proviene dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
     * <p>
     * Modificata l'ora di inizio del turno per il milite selezionato  <br>
     */
    @EventHandler
    public void handleChangeOraInizio(@ModelItem TurnoIscrizioneModel item) {
        handleChange(item);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Proviene dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
     * <p>
     * Modificata il campo note per il milite selezionato  <br>
     */
    @EventHandler
    public void handleChangeNote(@ModelItem TurnoIscrizioneModel item) {
        handleChange(item);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Proviene dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
     * <p>
     * Modificata l'ora di fine turno per il milite selezionato  <br>
     */
    @EventHandler
    public void handleChangeOraFine(@ModelItem TurnoIscrizioneModel item) {
        handleChange(item);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Presente solo se il servizio è senza orario fisso <br>
     */
    @EventHandler
    public void handleChangeInizioExtra() {
        String inizioText = getModel().getInizioExtra();

        turnoEntity.inizio = LocalTime.parse(inizioText);
        conferma.setEnabled(true);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Presente solo se il servizio è senza orario fisso <br>
     */
    @EventHandler
    public void handleChangeFineExtra() {
        String fineText = getModel().getFineExtra();

        turnoEntity.fine = LocalTime.parse(fineText);
        conferma.setEnabled(true);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * Evento lanciato dal bottone Annulla <br>
     */
    @EventHandler
    public void handleAnnulla() {
        if (dialogo!=null){
            tabellone.annullaDialogoTurno(dialogo);
        }else{
            getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
        }
    }


    /**
     * Java event handler on the server, run asynchronously <br>
     * Evento lanciato dal bottone Conferma <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Recupera i dati di tutte le iscrizioni presenti <br>
     * Controlla che il milite non sia già segnato nel turno <br>
     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
     * Registra le modifiche (eventuali) al turno <br>
     * Torna al tabellone <br>
     */
    @EventHandler
    public void handleConferma() {

        // validare i dati GUI
        // se non vanno bene, spiegare il perché e non uscire dalla pagina
        // se vanno bene, creare una entity per il database e salvare sul db
        if (array.isValid(listaTurnoIscrizioni)) {
            for (TurnoIscrizione turnoIscr : listaTurnoIscrizioni) {
                turnoIscr.iscrizioneEntity.milite = turnoIscr.militeEntity;
            }
        }

        tabellone.confermaDialogoTurno(dialogo, turnoEntity, nuovo);

    }


    /**
     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
     */
    private void handleChange(TurnoIscrizioneModel item) {
        Iscrizione iscr = null;

        if (item != null) {
            iscr = getIscrizione(item);
            iscr.inizio = LocalTime.parse(item.getInizio());
            iscr.note = item.getNote();
            iscr.fine = LocalTime.parse(item.getFine());
        }// end of if cycle

        fixIscrizioni();
        conferma.setEnabled(true);
    }// end of method


    /**
     * Recupera il turnoIscrizione selezionato dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
     */
    private TurnoIscrizione getTurnoIscrizione(TurnoIscrizioneModel item) {
        TurnoIscrizione turnoIscrizione = null;

        if (array.isValid(listaTurnoIscrizioni)) {
            for (TurnoIscrizione turnoIsc : listaTurnoIscrizioni) {
                if (turnoIsc.keyTag.equals(item.getKeyTag())) {
                    turnoIscrizione = turnoIsc;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return turnoIscrizione;
    }// end of method


    /**
     * Recupera l'iscrizione selezionata dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
     */
    private Iscrizione getIscrizione(TurnoIscrizioneModel item) {
        Iscrizione iscrizione = null;
        TurnoIscrizione turnoIscrizione = getTurnoIscrizione(item);

        if (turnoIscrizione != null) {
            iscrizione = turnoIscrizione.iscrizioneEntity;
        }// end of if cycle

        return iscrizione;
    }

}
