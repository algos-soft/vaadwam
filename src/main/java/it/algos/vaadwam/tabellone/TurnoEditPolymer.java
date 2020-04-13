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
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Java wrapper of the polymer element `turno-edit`
 */
@Route(value = TAG_TURNO_EDIT)
@Tag("turno-edit")
@HtmlImport("src/views/tabellone/turno-edit.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
@Slf4j
public class TurnoEditPolymer extends PolymerTemplate<TurnoEditModel> implements HasUrlParameter<String> {


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

    private List<TurnoIscrizione> turnoIscrizioneList;


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
                log.error("Parametri non corretti");
            }
        }

        if (turnoEntity == null) {
            Notification.show("Errore: non esiste il turno indicato", 2000, Notification.Position.MIDDLE);
            return;
        }

        layoutPolymer();

    }


    /**
     * Recupera il turno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param turnoKey per recuperare l'istanza di Turno
     */
    private void elaboraParameter(String turnoKey) {
        if (text.isValid(turnoKey)) {
            turnoEntity = turnoService.findById(turnoKey);
        }
    }


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
     * Regola i dati da presentare in base al turno selezionato.
     */
    private void layoutPolymer() {

        // Data completa (estesa) del giorno di esecuzione del turno
        String data = turnoService.getGiornoTxt(turnoEntity);
        getModel().setGiorno(data);

        // Descrizione estesa del servizio
        Servizio servizio = null;
        if (turnoEntity != null) {
            servizio = turnoEntity.getServizio();
            if (servizio != null) {
                getModel().setServizio(servizio.descrizione);
            }
        }

        // Orario (eventuale) del turno
        fixOrario();

        fixIscrizioni();
        fixAnnulla();
        fixConferma();
    }


    /**
     * Orario (eventuale) del turno
     * <p>
     * Se il servizio ha un orario definito, lo presenta in html come 'div' <br>
     * Se il servizio non ha un orario definito, lo presenta in html come due 'time-picker' <br>
     */
    private void fixOrario() {
        String orario = "";
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
     * Aggiunge al modello la lista delle iscrizioni
     */
    private void fixIscrizioni() {

        List<Iscrizione> iscrizioni = turnoEntity.getIscrizioni();
        List<TurnoIscrizioneModel> turnoIscrizioni = new ArrayList<>();

        int i=0;
        if (iscrizioni != null && iscrizioni.size()>0){
            for (Iscrizione iscrizione : iscrizioni){

                TurnoIscrizione turnoIscrizione = appContext.getBean(TurnoIscrizione.class, getModel(), turnoEntity, i);

                TurnoIscrizioneModel tim = new TurnoIscrizioneModel();
                tim.setFlagIscrizione(true);
                tim.setColore(turnoIscrizione.coloreTxt);
                tim.setIcona(turnoIscrizione.iconaTxt);
                tim.setMilite(turnoIscrizione.militetxt);
                tim.setFunzione(turnoIscrizione.funzioneTxt);
                tim.setInizio(turnoIscrizione.inizioTxt);
                tim.setNote(turnoIscrizione.noteTxt);
                tim.setFine(turnoIscrizione.fineTxt);
                tim.setAbilitata(true);
                tim.setAbilitataPicker(true);

                turnoIscrizioni.add(tim);

                i++;
            }
        }

        getModel().setIscrizioni(turnoIscrizioni);

        fixAbilitazioneIscrizioni();

    }


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

        this.militeLoggato = militeService.getMilite();

        // @todo per adesso
        // @todo Controlla se siamo loggati come developer, come admin o come user <br>
        if (militeLoggato == null) {
            return;
        }

        // Se siamo nello storico, disabilita tutte le iscrizioni (developer ed amdin esclusi)
        if (tabelloneService.isStorico(turnoEntity)) {
            abilitaIscrizioni();
            return;
        }

        // Controlla se il milite loggato è già segnato in una iscrizione.
        // Quella segnata viene abilitata. Tutte le altre disabilitate.
        if (array.isValid(turnoIscrizioneList)) {
            for (TurnoIscrizione turnoIscr : turnoIscrizioneList) {
                militeIsc = turnoIscr.iscrizione.getMilite();
                if (militeIsc != null && militeIsc.id.equals(militeLoggato.id)) {
                    militeLoggatoGiaSegnato = true;
                    turnoIscr.abilitata = true;
                    turnoIscr.abilitataPicker = true;
                }
            }
        }

        // Se il milite loggato non è segnato nel turno
        // abilita le iscrizioni abilitate per il milite loggato e senza un altro milite già segnato
        if (!militeLoggatoGiaSegnato) {
            abilitaOnly();
        }

        abilitaIscrizioni();

    }


    private void abilitaIscrizioni() {


//        if (turnoIscrizioneList != null && turnoIscrizioneList.size() > 0) {
//            getModel().setAbilitataPrima(turnoIscrizioneList.get(0).abilitata);
//            getModel().setAbilitataPickerPrima(turnoIscrizioneList.get(0).abilitataPicker);
//        }// end of if cycle
//
//        if (turnoIscrizioneList != null && turnoIscrizioneList.size() > 1) {
//            getModel().setAbilitataSeconda(turnoIscrizioneList.get(1).abilitata);
//            getModel().setAbilitataPickerSeconda(turnoIscrizioneList.get(1).abilitataPicker);
//        }// end of if cycle
//
//        if (turnoIscrizioneList != null && turnoIscrizioneList.size() > 2) {
//            getModel().setAbilitataTerza(turnoIscrizioneList.get(2).abilitata);
//            getModel().setAbilitataPickerTerza(turnoIscrizioneList.get(2).abilitataPicker);
//        }// end of if cycle
//
//        if (turnoIscrizioneList != null && turnoIscrizioneList.size() > 3) {
//            getModel().setAbilitataQuarta(turnoIscrizioneList.get(3).abilitata);
//            getModel().setAbilitataPickerQuarta(turnoIscrizioneList.get(3).abilitataPicker);
//        }// end of if cycle

    }


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

        if (array.isValid(turnoIscrizioneList)) {
            for (TurnoIscrizione turnoIscr : turnoIscrizioneList) {
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
            annulla.addClickShortcut(Key.ESCAPE);
        }// end of if cycle
        annulla.addClickListener(e -> handleAnnulla());
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
        conferma.addClickListener(e -> handleConferma());
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
     * Se era segnato, viene cancellato <br>
     * Se non era segnato, lo diventa <br>
     * Riconsidera tutte le abilitazioni <br>
     * Abilita il bottone 'conferma' <br>
     */
    @EventHandler
    public void handleClickMilite() {
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
     * L'ora di inizio è stata modificata
     */
    @EventHandler
    public void handleChangeOraInizio(@ModelItem TurnoIscrizioneModel item) {
        String inizio = item.getInizio();
        String fine = item.getFine();
        int a = 87;
        int b=a;
//        String inizioText = getModel().getInizioPrima();
//        String noteText = getModel().getNotePrima();
//        String fineText = getModel().getFinePrima();
//        handleChange(turnoEntity.iscrizioni.get(0), inizioText, noteText, fineText);
    }

    /**
     * L'ora di fine è stata modificata
     */
    @EventHandler
    public void handleChangeOraFine(@ModelItem TurnoIscrizioneModel item) {
        String inizio = item.getInizio();
        String fine = item.getFine();
        int a = 87;
        int b=a;

//        String inizioText = getModel().getInizioPrima();
//        String noteText = getModel().getNotePrima();
//        String fineText = getModel().getFinePrima();
//        handleChange(turnoEntity.iscrizioni.get(0), inizioText, noteText, fineText);
    }

    /**
     * E' stato premutp il bottone Conferma
     */
    @EventHandler
    public void handleConferma() {

        int a = 87;
        int b=a;

        TurnoEditModel model = getModel();
        List<TurnoIscrizioneModel> iscrizioni = model.getIscrizioni();
        for(TurnoIscrizioneModel iscrizione : iscrizioni){
            log.info(iscrizione.getInizio()+" - "+iscrizione.getFine()+" - "+iscrizione.getNote());
        }

        // validare i dati GUI
        // se non vanno bene, spiegare il perché e non uscire dalla pagina
        // se vanno bene, creare una entity per il database e salvare sul db

        // turnoService.save(turnoEntity);
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));

    }

    /**
     * E' stato premutp il bottone Annulla
     */
    @EventHandler
    public void handleAnnulla() {
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }




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


//    /**
//     * Evento lanciato dal bottone Annulla <br>
//     * <p>
//     * Torna al tabellone <br>
//     */
//    public void annulla() {
//        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
//    }// end of method


//    /**
//     * Evento lanciato dal bottone Conferma <br>
//     * <p>
//     * Recupera i dati di tutte le iscrizioni presenti <br>
//     * Controlla che il milite non sia già segnato nel turno <br>
//     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
//     * Registra le modifiche (eventuali) al turno <br>
//     * Torna al tabellone <br>
//     */
//    public void conferma() {
//
//        //@todo dovrebbero arrivare già regolati dal click sul nome
////        for (Iscrizione iscrizione : turno.iscrizioni) {
////            iscrizioneService.setInizio(iscrizione, turno);
////        }// end of for cycle
//        //@todo dovrebbero arrivare già regolati dal click sul nome
//
//        turnoService.save(turnoEntity);
//        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
//    }// end of method

}// end of class
