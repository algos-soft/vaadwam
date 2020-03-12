package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 06-mar-2020
 * Time: 10:19
 * <p>
 * Java wrapper of the polymer element `turno-edit` <br>
 * <p>
 * Questa classe viene costruita tramite una chiamata del browser effettuata da @Route <br>
 * Invocata da un @EventHandler di TurnoCellPolymer.handleClick() <br>
 */
@Route(value = TAG_TURNO_EDIT)
@Tag("turno-edit")
@HtmlImport("src/views/tabellone/turno-edit.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class TurnoEditPolymer extends PolymerTemplate<TurnoEditModel> implements HasUrlParameter<String> {


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected TabelloneService tabelloneService;

    /**
     * Milite loggato al momento <br>
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

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private PreferenzaService pref;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TurnoService turnoService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private ServizioService servizioService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private FunzioneService funzioneService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private ADateService dateService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private MiliteService militeService;

    //--property bean
    private Turno turnoEntity = null;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private ATextService text;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AArrayService array;

    private List<TurnoIscrizione> listaIscrizioni;


    /**
     * Recupera il turno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     * oppure <br>
     * costruisce un nuovo Turno col Servizio ed il Giorno arrivati come parametri della location <br>
     *
     * @param event     con la Location, segments, target, source, ecc
     * @param parameter per recuperare l'istanza di Turno
     *                  per creare una nuova istanza di Turno dal Servizio e dal Giorno
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location;
        QueryParameters queryParameters;
        Map<String, List<String>> parametersMap;

        if (text.isValid(parameter)) {
            elaboraParameter(parameter);
        } else {
            location = event.getLocation();
            queryParameters = location.getQueryParameters();
            parametersMap = queryParameters.getParameters();
            if (parametersMap != null) {
                elaboraParameter(parametersMap);
            } else {
                System.out.println("Parametri non corretti");
            }// end of if/else cycle
        }// end of if/else cycle

        if (turnoEntity == null) {
            Notification.show("Errore: non esiste il turno indicato", 2000, Notification.Position.MIDDLE);
            return;
        }// end of if cycle

        layoutPolymer();
    }// end of method


    /**
     * Recupera il turno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param turnoKey per recuperare l'istanza di Turno
     */
    private void elaboraParameter(String turnoKey) {
        if (text.isValid(turnoKey)) {
            turnoEntity = turnoService.findById(turnoKey);
        }// end of if cycle
    }// end of method


    /**
     * Costruisce un nuovo Turno col Servizio ed il Giorno arrivati come parametri della location <br>
     *
     * @param parametersMap per costruire una nuova istanza di Turno
     */
    private void elaboraParameter(Map<String, List<String>> parametersMap) {
        List<String> listaGiorni;
        LocalDate giorno = null;
        List<String> listaServizi;
        String servizioKey = "";
        Servizio servizio = null;
        String giornoTxt = VUOTA;

        listaGiorni = parametersMap.get(KEY_MAP_GIORNO);
        if (array.isValid(listaGiorni) && listaGiorni.size() == 1) {
            giornoTxt = listaGiorni.get(0);
        }// end of if cycle
        if (text.isValid(giornoTxt)) {
            giorno = LocalDate.parse(giornoTxt);
        }// end of if cycle

        listaServizi = parametersMap.get(KEY_MAP_SERVIZIO);
        if (array.isValid(listaServizi) && listaServizi.size() == 1) {
            servizioKey = listaServizi.get(0);
        }// end of if cycle
        if (text.isValid(servizioKey)) {
            servizio = servizioService.findById(servizioKey);
        }// end of if cycle

        turnoEntity = turnoService.newEntity(giorno, servizio);

        //--elimino l'ID del turno, per poterlo distinguere da un turno esistente e già nel mongoDB
        //--l'ID viene costruioto automaticamente con newEntity() per averlo (di norma) subito disponibile
        //--ma viene in ogni caso ri-creato da beforeSave()
        turnoEntity.id = null;
    }// end of method


    /**
     * Regola i dati da presentare in base al turno selezionato <br>
     * Il turno arriva come parametro di @Route a questa classe <br>
     * Invocata da un @EventHandler di TurnoCellPolymer.handleClick() <br>
     */
    private void layoutPolymer() {
        fixGiorno();
        fixServizio();
        fixOrario();
        fixIscrizioni();
        fixAnnulla();
        fixConferma();
    }// end of method


    /**
     * Data completa (estesa) del giorno di esecuzione del turno <br>
     */
    private void fixGiorno() {
        String data;
        LocalDate giorno = null;

        if (turnoEntity != null) {
            giorno = turnoEntity.getGiorno();
        }// end of if cycle

        if (giorno != null) {
            data = dateService.get(giorno, EATime.completa);
            getModel().setGiorno(data);
        }// end of if cycle

    }// end of method


    /**
     * Descrizione estesa del servizio <br>
     */
    private void fixServizio() {
        Servizio servizio = null;

        if (turnoEntity != null) {
            servizio = turnoEntity.getServizio();
        }// end of if cycle

        if (servizio != null) {
            getModel().setServizio(servizio.descrizione);
        }// end of if cycle
    }// end of method


    /**
     * Orario (eventuale) del turno <br>
     * Se il servizio ha un orario definito, lo presenta in html come 'div' <br>
     * Se il servizio non ha un orario definito, lo presenta in html come due 'time-picker' <br>
     */
    private void fixOrario() {
        String orario = "";
        Servizio servizio = null;

        if (turnoEntity != null) {
            servizio = turnoEntity.getServizio();
        }// end of if cycle

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
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Regolazione delle iscrizioni <br>
     * Possono essere da 1 a 4 (di più non sono previste in 'turno-edit.html') <br>
     * Ogni iscrizione (su due righe) ha:
     * funzione (bottone)
     * milite (bottone)
     * inizio (picker)
     * note (text)
     * fine (picker)
     */
    private void fixIscrizioni() {
        int pos = 0;
        TurnoIscrizione turnoIscrizione;

        if (turnoEntity.getIscrizioni() != null) {
            listaIscrizioni = new ArrayList<>();
            for (Iscrizione iscr : turnoEntity.getIscrizioni()) {
                turnoIscrizione = (TurnoIscrizione) appContext.getBean(TurnoIscrizione.class, getModel(), turnoEntity, pos++);
                listaIscrizioni.add(turnoIscrizione);
            }// end of for cycle
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 0) {
            fixIscrizionePrima(listaIscrizioni.get(0));
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 1) {
            fixIscrizioneSeconda(listaIscrizioni.get(1));
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 2) {
            fixIscrizioneTerza(listaIscrizioni.get(2));
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 3) {
            fixIscrizioneQuarta(listaIscrizioni.get(3));
        }// end of if cycle

        fixAbilitazioneIscrizioni();
    }// end of method


    private void fixIscrizionePrima(TurnoIscrizione turnoIscrizione) {
        getModel().setPrimaIscrizione(true);
        getModel().setColorePrima(turnoIscrizione.coloreTxt);
        getModel().setIconaPrima(turnoIscrizione.iconaTxt);

        getModel().setMilitePrima(turnoIscrizione.militetxt);
        getModel().setFunzionePrima(turnoIscrizione.funzioneTxt);

        getModel().setInizioPrima(turnoIscrizione.inizioTxt);
        getModel().setNotePrima(turnoIscrizione.noteTxt);
        getModel().setFinePrima(turnoIscrizione.fineTxt);
    }// end of method


    private void fixIscrizioneSeconda(TurnoIscrizione turnoIscrizione) {
        getModel().setSecondaIscrizione(true);
        getModel().setColoreSeconda(turnoIscrizione.coloreTxt);
        getModel().setIconaSeconda(turnoIscrizione.iconaTxt);

        getModel().setMiliteSeconda(turnoIscrizione.militetxt);
        getModel().setFunzioneSeconda(turnoIscrizione.funzioneTxt);

        getModel().setInizioSeconda(turnoIscrizione.inizioTxt);
        getModel().setNoteSeconda(turnoIscrizione.noteTxt);
        getModel().setFineSeconda(turnoIscrizione.fineTxt);
    }// end of method


    private void fixIscrizioneTerza(TurnoIscrizione turnoIscrizione) {
        getModel().setTerzaIscrizione(true);
        getModel().setColoreTerza(turnoIscrizione.coloreTxt);
        getModel().setIconaTerza(turnoIscrizione.iconaTxt);

        getModel().setMiliteTerza(turnoIscrizione.militetxt);
        getModel().setFunzioneTerza(turnoIscrizione.funzioneTxt);

        getModel().setInizioTerza(turnoIscrizione.inizioTxt);
        getModel().setNoteTerza(turnoIscrizione.noteTxt);
        getModel().setFineTerza(turnoIscrizione.fineTxt);
    }// end of method


    private void fixIscrizioneQuarta(TurnoIscrizione turnoIscrizione) {
        getModel().setQuartaIscrizione(true);
        getModel().setColoreQuarta(turnoIscrizione.coloreTxt);
        getModel().setIconaQuarta(turnoIscrizione.iconaTxt);

        getModel().setMiliteQuarta(turnoIscrizione.militetxt);
        getModel().setFunzioneQuarta(turnoIscrizione.funzioneTxt);

        getModel().setInizioQuarta(turnoIscrizione.inizioTxt);
        getModel().setNoteQuarta(turnoIscrizione.noteTxt);
        getModel().setFineQuarta(turnoIscrizione.fineTxt);
    }// end of method


    /**
     * Regola la visibilità di tutte le iscrizioni <br>
     * Controlla se siamo loggati come developer, come admin o come user <br>
     * Recupera le funzioni abilitate del milite loggato
     * Controlla se il milite loggato è già segnato in una iscrizione. Se è così disabilita tutte le altre <br>
     * Disabilita le iscrizioni che hanno già un milite segnato <br>
     * Disabilita le iscrizioni che hanno una funzione non abilitata per il milite loggato <br>
     * Abilita le iscrizioni rimanenti <br>
     */
    protected void fixAbilitazioneIscrizioni() {
        Milite militeIsc;
        boolean militeLoggatoGiaSegnato = false;

        //--Recupera il milite loggato
        this.militeLoggato = militeService.getMilite();

        // @todo per adesso
        // @todo Controlla se siamo loggati come developer, come admin o come user <br>
        if (militeLoggato == null) {
            return;
        }// end of if cycle

        //--Se siamo nello storico, disabilita tutte le iscrizioni (developer ed amdin esclusi)
        if (tabelloneService.isStorico(turnoEntity)) {
            abilitaIscrizioni();
            return;
        }// end of if cycle

        //--Controlla se il milite loggato è già segnato in una iscrizione.
        //--Quella segnata viene abilitata. Tutte le altre disabilitate.
        if (array.isValid(listaIscrizioni)) {
            for (TurnoIscrizione turnoIscr : listaIscrizioni) {
                militeIsc = turnoIscr.iscrizione.getMilite();
                if (militeIsc != null && militeIsc.id.equals(militeLoggato.id)) {
                    militeLoggatoGiaSegnato = true;
                    turnoIscr.abilitata = true;
                    turnoIscr.abilitataPicker = true;
//                } else {
//                    turnoIscr.abilitata = false;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        //--Se il milite loggato non è segnato nel turno
        //--Abilita le iscrizioni abilitate per il milite loggato e senza un altro milite già segnato
        if (!militeLoggatoGiaSegnato) {
            abilitaOnly();
        }// end of if cycle

        abilitaIscrizioni();
    }// end of method


    private void abilitaIscrizioni() {
        if (listaIscrizioni != null && listaIscrizioni.size() > 0) {
            getModel().setAbilitataPrima(listaIscrizioni.get(0).abilitata);
            getModel().setAbilitataPickerPrima(listaIscrizioni.get(0).abilitataPicker);
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 1) {
            getModel().setAbilitataSeconda(listaIscrizioni.get(1).abilitata);
            getModel().setAbilitataPickerSeconda(listaIscrizioni.get(1).abilitataPicker);
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 2) {
            getModel().setAbilitataTerza(listaIscrizioni.get(2).abilitata);
            getModel().setAbilitataPickerTerza(listaIscrizioni.get(2).abilitataPicker);
        }// end of if cycle

        if (listaIscrizioni != null && listaIscrizioni.size() > 3) {
            getModel().setAbilitataQuarta(listaIscrizioni.get(3).abilitata);
            getModel().setAbilitataPickerQuarta(listaIscrizioni.get(3).abilitataPicker);
        }// end of if cycle
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

        if (array.isValid(listaIscrizioni)) {
            for (TurnoIscrizione turnoIscr : listaIscrizioni) {
                iscrizioneAbilitataMiliteLoggato = listaIDFunzioniAbilitate.contains(turnoIscr.funzioneEntity.id);
                iscrizioneNonSegnata = turnoIscr.militeEntity == null;
                turnoIscr.abilitata = iscrizioneAbilitataMiliteLoggato && iscrizioneNonSegnata;
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixAnnulla() {
        annulla.setText("Annulla");
        annulla.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            annulla.addClickShortcut(Key.ARROW_LEFT);
        }// end of if cycle
        annulla.addClickListener(e -> annulla());
        annulla.getElement().setAttribute("title", "Ritorno al tabellone");
    }// end of method


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
        conferma.addClickListener(e -> conferma());
        conferma.setEnabled(false);
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
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Se era segnato, viene cancellato <br>
     * Se non era segnato, lo diventa <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleClickPrima() {
        handleClick(turnoEntity.iscrizioni.get(0));
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Se era segnato, viene cancellato <br>
     * Se non era segnato, lo diventa <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleClickSeconda() {
        handleClick(turnoEntity.iscrizioni.get(1));
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Se era segnato, viene cancellato <br>
     * Se non era segnato, lo diventa <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleClickTerza() {
        handleClick(turnoEntity.iscrizioni.get(2));
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Se era segnato, viene cancellato <br>
     * Se non era segnato, lo diventa <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleClickQuarta() {
        handleClick(turnoEntity.iscrizioni.get(3));
    }// end of method


    /**
     * Se era segnato, viene cancellato <br>
     * Se non era segnato, lo diventa <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    private void handleClick(Iscrizione iscr) {
        Milite militeIsc;

        if (iscr != null) {
            militeIsc = iscr.milite;
            if (militeIsc != null && militeIsc.id.equals(militeLoggato.id)) {
                iscr.milite = null;
                handleChange(iscr, turnoEntity.inizio.toString(), VUOTA, turnoEntity.fine.toString());
            } else {
                iscr.milite = militeLoggato;
                fixIscrizioni();
            }// end of if/else cycle
        }// end of if cycle

        conferma.setEnabled(true);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleChangePrima() {
        String inizioText = getModel().getInizioPrima();
        String noteText = getModel().getNotePrima();
        String fineText = getModel().getFinePrima();

        handleChange(turnoEntity.iscrizioni.get(0), inizioText, noteText, fineText);
    }// end of method

    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleChangeSeconda() {
        String inizioText = getModel().getInizioSeconda();
        String noteText = getModel().getNoteSeconda();
        String fineText = getModel().getFineSeconda();

        handleChange(turnoEntity.iscrizioni.get(1), inizioText, noteText, fineText);
    }// end of method

    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleChangeTerza() {
        String inizioText = getModel().getInizioTerza();
        String noteText = getModel().getNoteTerza();
        String fineText = getModel().getFineTerza();

        handleChange(turnoEntity.iscrizioni.get(2), inizioText, noteText, fineText);
    }// end of method

    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleChangeQuarta() {
        String inizioText = getModel().getInizioQuarta();
        String noteText = getModel().getNoteQuarta();
        String fineText = getModel().getFineQuarta();

        handleChange(turnoEntity.iscrizioni.get(3), inizioText, noteText, fineText);
    }// end of method


    /**
     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
     */
    private void handleChange(Iscrizione iscr, String inizioText, String noteText, String fineText) {

        if (iscr != null) {
            iscr.inizio = LocalTime.parse(inizioText);
            iscr.note = noteText;
            iscr.fine = LocalTime.parse(fineText);
        }// end of if cycle

        fixIscrizioni();
        conferma.setEnabled(true);
    }// end of method


    /**
     * Evento lanciato dal bottone Annulla <br>
     * <p>
     * Torna al tabellone <br>
     */
    public void annulla() {
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }// end of method


    /**
     * Evento lanciato dal bottone Conferma <br>
     * <p>
     * Recupera i dati di tutte le iscrizioni presenti <br>
     * Controlla che il milite non sia già segnato nel turno <br>
     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
     * Registra le modifiche (eventuali) al turno <br>
     * Torna al tabellone <br>
     */
    public void conferma() {

        //@todo dovrebbero arrivare già regolati dal click sul nome
//        for (Iscrizione iscrizione : turno.iscrizioni) {
//            iscrizioneService.setInizio(iscrizione, turno);
//        }// end of for cycle
        //@todo dovrebbero arrivare già regolati dal click sul nome

        turnoService.save(turnoEntity);
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }// end of method

}// end of class
