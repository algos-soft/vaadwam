package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.*;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.modules.funzione.Funzione;
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
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;
import static it.algos.vaadwam.application.WamCost.USA_COLORAZIONE_DIFFERENZIATA;

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
public class TurnoEditPolymer extends PolymerTemplate<TurnoEditModel>  {

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected TabelloneService tabelloneService;

    /**
     * Milite attualmente loggato nella sessione
     */
    protected Milite militeLoggato;

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

    @Autowired
    private PreferenzaService pref;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private MiliteService militeService;

    @Autowired
    private ADateService dateService;

    private Turno turnoEntity;

    @Autowired
    private AArrayService array;

    private ITabellone tabellone;

    private Dialog dialogo;

    // Componenti editor di singola iscrizione inseriti nel dialogo
    private List<CompIscrizione> compIscrizioni;

    // contiene tutto il contenuto visualizzato nel dialogo
    @Id
    private Element container;

    @Id
    private Div areaiscrizioni;

    /**
     * @param tabellone il tabellone di riferimento per effettuare le callbacks
     * @param dialogo il dialogo contenitore
     * @param turno il turno da mostrare
     */
    public TurnoEditPolymer(ITabellone tabellone, Dialog dialogo, Turno turno) {
        this.tabellone=tabellone;
        this.dialogo=dialogo;
        this.turnoEntity=turno;

        // registra il riferimento al server Java nel client JS
        // necessario perché JS possa chiamare direttamente metodi Java
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

    }


    @PostConstruct
    private void init(){

        populateModel();

        // crea e aggiunge l'area Iscrizioni
        compIscrizioni=buildCompIscrizioni();
        for (CompIscrizione comp : compIscrizioni){
            areaiscrizioni.add(comp);
        }


        bConferma.addClickShortcut(Key.ENTER);
        bConferma.addClickListener(e -> handleConferma());

        bAnnulla.addClickShortcut(Key.ESCAPE);
        bAnnulla.addClickListener(e -> handleAnnulla());




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




    /**
     * Riempie il modello con i dati del turno
     */
    private void populateModel() {

        // data di esecuzione del turno
        String data = dateService.get(turnoEntity.getGiorno(), EATime.completa);
        getModel().setGiorno(data);

        // descrizione estesa del servizio
        Servizio servizio = turnoEntity.getServizio();
        getModel().setServizio(servizio.descrizione);

        // orario (eventuale) del turno
        fixOrario();


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


    }


    /**
     * Crea la lista dei componenti per editare le singole iscrizioni
     */
    private List<CompIscrizione> buildCompIscrizioni() {
        List<CompIscrizione> componenti = new ArrayList<>();

        for(Iscrizione iscrizione : turnoEntity.getIscrizioni()){
            CompIscrizione comp = appContext.getBean(CompIscrizione.class, iscrizione);
            componenti.add(comp);
        }

        return componenti;
    }



//    /**
//     * Calcola la chiave per una iscrizione del turno.
//     * Usata per riconciliare le iscrizioni del turno con le iscrizioni del dialogo.
//     *
//     * @param iscrizione l'iscrizione del turno
//     * @return la chiave per l'iscrizione del dialogo
//     */
//    private String getKeyIscrizione(Iscrizione iscrizione){
//        return turnoEntity.getId()+"-"+iscrizione.getFunzione().getId();
//    }



//    /**
//     * Colore dei due bottoni della prima riga (funzione e milite) di ogni iscrizione <br>
//     */
//    private String getColore(Iscrizione iscrizione) {
//        String colore = "";
//
//        if (pref.isBool(USA_COLORAZIONE_TURNI)) {
//            if (pref.isBool(USA_COLORAZIONE_DIFFERENZIATA)) {
//                colore = tabelloneService.getColoreIscrizione(turnoEntity, iscrizione).getTag().toLowerCase();
//            } else {
//                colore = tabelloneService.getColoreTurno(turnoEntity).getTag().toLowerCase();
//            }// end of if/else cycle
//        } else {
//            colore = VUOTA;
//        }// end of if/else cycle
//
//        return colore;
//    }

//    /**
//     * Ritorna una stringa per il picker rappresentante una LocalTime.
//     * @param lTime la LocalTime da convertire
//     * @param defaultLtime la LocalTime da utilizzare nel caso lTime sia null
//     * @return la stringa rappresentante la LocalTime
//     */
//    private String getPickerTimeString(LocalTime lTime, LocalTime defaultLtime){
//        String timeTxt;
//        LocalTime usedTime;
//        if (lTime!=null){
//            usedTime=lTime;
//        }else{
//            usedTime=defaultLtime;
//        }
//        timeTxt=usedTime.toString();
//        return timeTxt;
//    }



//    /**
//     * Regola abilita/disabilita di tutte le iscrizioni <br>
//     * Chiamata ad ogni modifica dei dati del Client <br>
//     * <p>
//     * Controlla se siamo loggati come developer, come admin o come user <br>
//     * Recupera le funzioni abilitate del milite loggato <br>
//     * <p>
//     * L'iscrizione per cui non si hanno i permessi è disabilitata. <br>
//     * Eventualmente differenziare col colore di scadenza del turno. <br>
//     * <p>
//     * L'iscrizione per cui si hanno i permessi è abilitata se vuota.
//     * Eventualmente differenziare col colore di scadenza del turno.
//     * Se è già segnato un milite (non quello loggato) è disabilitata.
//     * Se è già segnato il milite loggato è abilitata per permettere la cancellazione (entro il tempo previsto).
//     * <p>
//     * Controlla se il milite loggato è già segnato in una iscrizione. Se è così disabilita tutte le altre <br>
//     * Disabilita le iscrizioni che hanno già un milite segnato <br>
//     * Disabilita le iscrizioni che hanno una funzione non abilitata per il milite loggato <br>
//     * Abilita le iscrizioni rimanenti <br>
//     */
//    protected void regolaIscrizioni() {
//
//        boolean militeLoggatoGiaSegnato = false;
//
//        this.militeLoggato = militeService.getMilite();
//
//        // @todo per adesso
//        // @todo Controlla se siamo loggati come developer, come admin o come user <br>
//        if (militeLoggato == null) {
//            return;
//        }
//
//        //--Se siamo nello storico, disabilita tutte le iscrizioni (developer ed amdin esclusi)
//        // @todo per adesso
//        // @todo dovrà permettere al developer e all'admin di entrare <br>
//        if (tabelloneService.isStorico(turnoEntity)) {
//            disabilitaAll();
//            return;
//        }
//
//        //--Controlla se il milite loggato è già segnato in una iscrizione.
//        //--Quella segnata viene abilitata. Tutte le altre disabilitate.
//        List<TurnoIscrizioneModel> listaIscrizioni = getModel().getIscrizioni();
//        for(TurnoIscrizioneModel iscrizione : listaIscrizioni){
//            String idMiliteIscritto = iscrizione.getIdMilite();
//            if (!StringUtils.isEmpty(idMiliteIscritto) && idMiliteIscritto.equals(militeLoggato.id)){
//                militeLoggatoGiaSegnato = true;
//                iscrizione.setAbilitata(true);
//                iscrizione.setAbilitataPicker(true);
//            }else{
//                iscrizione.setAbilitata(false);
//                iscrizione.setAbilitataPicker(false);
//            }
//
//        }
//
//
//
////        if (array.isValid(listaTurnoIscrizioni)) {
////            for (TurnoIscrizione turnoIscr : listaTurnoIscrizioni) {
////                militeIsc = turnoIscr.militeEntity;
////                if (militeIsc != null && militeIsc.id.equals(militeLoggato.id)) {
////                    militeLoggatoGiaSegnato = true;
////                    turnoIscr.abilitata = true;
////                    turnoIscr.abilitataPicker = true;
////                } else {
////                    turnoIscr.abilitata = false;
////                    turnoIscr.abilitataPicker = false;
////                }// end of if/else cycle
////            }// end of for cycle
////        }// end of if cycle
//
//        // Se il milite loggato non è segnato nel turno
//        // abilita le iscrizioni abilitate per il milite loggato e senza un altro milite già segnato
//        if (!militeLoggatoGiaSegnato) {
//            abilitaOnly();
//        }// end of if cycle
//
//        //--sincronizza il modello
//        //List<TurnoIscrizioneModel> listaTurnoIscrizioniModello = getModel().getIscrizioni();
////        List<TurnoIscrizioneModel> listaTurnoIscrizioniModello = tabelloneService.getTurnoIscrizioniModello(listaTurnoIscrizioni);
////        getModel().setIscrizioni(listaTurnoIscrizioniModello);
//
//    }


//    /**
//     * Se siamo nello storico, disabilita tutte le iscrizioni (developer ed amdin esclusi)
//     */
//    private void disabilitaAll() {
//
//        List<TurnoIscrizioneModel> listaTurnoIscrizioniModello=getModel().getIscrizioni();
//
//        if (array.isValid(listaTurnoIscrizioniModello)) {
//            for (TurnoIscrizioneModel turnoModello : listaTurnoIscrizioniModello) {
//                turnoModello.setAbilitata(false);
//                turnoModello.setAbilitataPicker(false);
//            }
//        }
//
//        getModel().setIscrizioni(listaTurnoIscrizioniModello);
//    }// end of method


//    /**
//     * Se il milite loggato non è segnato nel turno
//     * Recupera le funzioni abilitate del milite loggato
//     * Abilita le iscrizioni abilitate per il milite loggato e senza un altro milite già segnato
//     */
//    private void abilitaOnly() {
//        List<String> listaIDFunzioniAbilitate;
//        listaIDFunzioniAbilitate = militeLoggato != null ? militeService.getListaIDFunzioni(militeLoggato) : null;
//        boolean iscrizioneAbilitataMiliteLoggato;
//        boolean iscrizioneNonSegnata;
//
//        for(TurnoIscrizioneModel iscrizione : getModel().getIscrizioni()){
//            iscrizioneAbilitataMiliteLoggato = listaIDFunzioniAbilitate.contains(iscrizione.getIdFunzione());
//            iscrizioneNonSegnata = iscrizione.getIdMilite() == null;
//            iscrizione.setAbilitata(iscrizioneAbilitataMiliteLoggato && iscrizioneNonSegnata);
//        }
//
////        if (array.isValid(listaTurnoIscrizioni)) {
////            for (TurnoIscrizione turnoIscr : listaTurnoIscrizioni) {
////                iscrizioneAbilitataMiliteLoggato = listaIDFunzioniAbilitate.contains(turnoIscr.funzioneEntity.id);
////                iscrizioneNonSegnata = turnoIscr.militeEntity == null;
////                turnoIscr.abilitata = iscrizioneAbilitataMiliteLoggato && iscrizioneNonSegnata;
////            }// end of for cycle
////        }// end of if cycle
//
//    }


    /**
     * Regolazioni standard di default del bottone 'Annulla' <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixAnnulla() {
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            bAnnulla.addClickShortcut(Key.ESCAPE);
        }
        bAnnulla.addClickListener(e -> handleAnnulla());
    }


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixConferma() {
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            bConferma.addClickShortcut(Key.ENTER);
        }
        bConferma.addClickListener(e -> handleConferma());
    }


//    /**
//     * Cliccato sul milite di una iscrizione.
//     *
//     * Se il milite era segnato, viene cancellato <br>
//     * Se l'iscrizione era vuota, viene segnato il milite attualmente loggato <br>
//     * Riconsidera tutte le abilitazioni <br>
//     * Abilita il bottone 'conferma' <br>
//     */
//    @EventHandler
//    public void handleClickMilite(@ModelItem TurnoIscrizioneModel item) {
//
//        for(TurnoIscrizioneModel iscrizione : getModel().getIscrizioni()){
//            if (iscrizione.getKeyTag().equals(item.getKeyTag())) {
//                if (iscrizione.getIdMilite() == null) {
//                    iscrizione.setIdMilite(militeLoggato.id);
//                    iscrizione.setMilite(militeLoggato.getSigla());
//                } else {
//                    iscrizione.setIdMilite(null);
//                    iscrizione.setMilite(null);
//                }
//            }
//        }
//
////        if (array.isValid(listaTurnoIscrizioni)) {
////            for (TurnoIscrizione turnoIsc : listaTurnoIscrizioni) {
////                if (turnoIsc.keyTag.equals(item.getKeyTag())) {
////                    if (turnoIsc.militeEntity == null) {
////                        turnoIsc.militeEntity = militeLoggato;
////                        turnoIsc.militetxt = militeLoggato.username;
////                    } else {
////                        turnoIsc.militeEntity = null;
////                        turnoIsc.militetxt = VUOTA;
////                    }// end of if/else cycle
////                }// end of if cycle
////            }// end of for cycle
////        }// end of if cycle
//
//        regolaIscrizioni();
//        bConferma.setEnabled(true);
//    }// end of method


//    /**
//     * Modificate le note di una iscrizione
//     */
//    @EventHandler
//    public void handleChangeOraInizio(@ModelItem TurnoIscrizioneModel item) {
//        handleChange(item);
//    }


//    /**
//     * Modificata l'ora di fine del turno di una iscrizione
//     */
//    @EventHandler
//    public void handleChangeNote(@ModelItem TurnoIscrizioneModel item) {
//        handleChange(item);
//    }


//    /**
//     * Modificata l'ora di fine del turno di una iscrizione
//     */
//    @EventHandler
//    public void handleChangeOraFine(@ModelItem TurnoIscrizioneModel item) {
//        handleChange(item);
//    }


//    /**
//     * Java event handler on the server, run asynchronously <br>
//     * <p>
//     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
//     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
//     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
//     * <p>
//     * Presente solo se il servizio è senza orario fisso <br>
//     */
//    @EventHandler
//    public void handleChangeInizioExtra() {
//        String inizioText = getModel().getInizioExtra();
//        turnoEntity.inizio = LocalTime.parse(inizioText);
//        bConferma.setEnabled(true);
//    }// end of method


//    /**
//     * Java event handler on the server, run asynchronously <br>
//     * <p>
//     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
//     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
//     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
//     * <p>
//     * Presente solo se il servizio è senza orario fisso <br>
//     */
//    @EventHandler
//    public void handleChangeFineExtra() {
//        String fineText = getModel().getFineExtra();
//        turnoEntity.fine = LocalTime.parse(fineText);
//        bConferma.setEnabled(true);
//    }// end of method


    /**
     * Evento lanciato dal bottone Annulla <br>
     */
    @EventHandler
    public void handleAnnulla() {
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
    public void handleConferma() {

        // validare i dati GUI
        // se non vanno bene, spiegare il perché e non uscire dalla pagina
        // se vanno bene, creare una entity per il database e salvare sul db

//        if (array.isValid(listaTurnoIscrizioni)) {
//            for (TurnoIscrizione turnoIscr : listaTurnoIscrizioni) {
//                turnoIscr.iscrizioneEntity.milite = turnoIscr.militeEntity;
//            }
//        }

        syncTurno();

        tabellone.confermaDialogoTurno(dialogo, turnoEntity);

    }


    /**
     * Sincronizza le iscrizioni dell'oggetto Turno ricevuto nel costruttore
     * in base allo stato corrente delle iscrizioni contenute nel dialogo.
     */
    private void syncTurno(){

        List<Iscrizione> iscrizioni=turnoEntity.getIscrizioni();

        for(Iscrizione iscrizione : iscrizioni){

            CompIscrizione ci = findCompIscrizione(iscrizione);
            if (ci!=null){
                syncIscrizione(iscrizione, ci);
            }else{
                log.error("Componente iscrizione non trovato per l'iscrizione "+iscrizione.getFunzione().getCode());
            }


            // recupera la corrispondente iscrizione del dialogo
//            TurnoIscrizioneModel iscrizioneModello=getIscrizioneModello(iscrizioneTurno);
            TurnoIscrizioneModel iscrizioneModello=null;

            // aggiorna l'iscrizione del modello in base a quella del dialogo
            if(iscrizioneModello!=null){

                String idMilite=iscrizioneModello.getIdMilite();
                if (idMilite!=null){
                    Milite milite = militeService.findById(idMilite);
                    iscrizione.setMilite(milite);
                }else{
                    iscrizione.setMilite(null);
                }

                LocalTime lTime;

                lTime=LocalTime.parse(iscrizioneModello.getInizio());
                iscrizione.setInizio(lTime);

                lTime=LocalTime.parse(iscrizioneModello.getFine());
                iscrizione.setFine(lTime);

            }
        }

    }

    /**
     * Sincronizza una singola iscrizione del turno con quella del dialogo
     *
     */
    private void syncIscrizione(Iscrizione iscrizione, CompIscrizione compIscrizione){

        // sync milite
        String idMilite=compIscrizione.getIdMiliteSelezionato();
        if (idMilite!=null){
            Milite milite = militeService.findById(idMilite);
            iscrizione.setMilite(milite);
        }else{
            iscrizione.setMilite(null);
        }

    }


    /**
     * Cerca il componente iscrizione in base all'oggetto Iscrizione
     * con cui è stato costruito
     */
    private CompIscrizione findCompIscrizione(Iscrizione iscrizione){
        CompIscrizione compOut=null;
        for(CompIscrizione comp : compIscrizioni){
            if (comp.getIscrizione().equals(iscrizione)){
                compOut=comp;
                break;
            }
        }
        return compOut;
    }


//    /**
//     * Ritorna l'iscrizione del modello corrispondente a una data Iscrizione turno.
//     * <p>
//     * @param iscrTurno l'iscrizione turno
//     * @return l'iscrizione modello, o null se non esiste
//     */
//    private TurnoIscrizioneModel getIscrizioneModello(Iscrizione iscrTurno) {
//        TurnoIscrizioneModel iscrFound=null;
//        String key = getKeyIscrizione(iscrTurno);
//        for(TurnoIscrizioneModel iscrModello : getModel().getIscrizioni()){
//            if(iscrModello.getKeyTag().equals(key)){
//                iscrFound=iscrModello;
//                break;
//            }
//        }
//        return iscrFound;
//    }


//    /**
//     * Recupera i dati (della seconda riga) dalla GUI ed abilita il bottone 'conferma' <br>
//     */
//    private void handleChange(TurnoIscrizioneModel item) {
////        Iscrizione iscr = null;
////
////        if (item != null) {
////            iscr = getIscrizione(item);
////            iscr.inizio = LocalTime.parse(item.getInizio());
////            iscr.note = item.getNote();
////            iscr.fine = LocalTime.parse(item.getFine());
////        }// end of if cycle
////
////        fixIscrizioni();
////        conferma.setEnabled(true);
//    }// end of method


//    /**
//     * Recupera il turnoIscrizione selezionato dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
//     */
//    private TurnoIscrizione getTurnoIscrizione(TurnoIscrizioneModel item) {
//        TurnoIscrizione turnoIscrizione = null;
//
//        if (array.isValid(listaTurnoIscrizioni)) {
//            for (TurnoIscrizione turnoIsc : listaTurnoIscrizioni) {
//                if (turnoIsc.keyTag.equals(item.getKeyTag())) {
//                    turnoIscrizione = turnoIsc;
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return turnoIscrizione;
//    }// end of method


//    /**
//     * Recupera l'iscrizione selezionata dal ciclo <dom-repeat items="[[iscrizioni]]"> del Client <br>
//     */
//    private Iscrizione getIscrizione(TurnoIscrizioneModel item) {
//        Iscrizione iscrizione = null;
//        TurnoIscrizione turnoIscrizione = getTurnoIscrizione(item);
//
//        if (turnoIscrizione != null) {
//            iscrizione = turnoIscrizione.iscrizioneEntity;
//        }// end of if cycle
//
//        return iscrizione;
//    }

//    Component findComponentWithId(HasComponents root, String id) {
//        for(Component child : root) {
//            if(id.equals(child.getId())) {
//                // found it!
//                return child;
//            } else if(child instanceof HasComponents) {
//                // recursively go through all children that themselves have children
//                return findComponentWithId((HasComponents) child, id);
//            }
//        }
//        // none was found
//        return null;
//    }


    @EventHandler
    void fireSomeButtonClick(@RepeatIndex int itemIndex) {
        //getModel().getFuels().get(itemIndex) ...
    }

}
