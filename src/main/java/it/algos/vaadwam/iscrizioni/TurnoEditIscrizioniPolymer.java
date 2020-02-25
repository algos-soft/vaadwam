package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 10-ago-2019
 * Time: 15:52
 */
public abstract class TurnoEditIscrizioniPolymer extends PolymerTemplate<TemplateModel> implements HasUrlParameter<String> {

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("servizio")
    public TurnoShowServizioPolymer servizioPolymer;


    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("bottoni")
    public ButtonsBar bottoniPolymer;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ADateService dateService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ServizioService servizioService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected TurnoService turnoService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected MiliteService militeService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected IscrizioneService iscrizioneService;

    //--property bean
    protected Turno turno = null;

    protected boolean abilitata;

    /**
     * Milite loggato al momento <br>
     */
    protected Milite militeLoggato;

    /**
     * Lista delle funzioni abilitate per il milite loggato al momento <br>
     */
    protected List<String> listaIDFunzioniAbilitate;

    /**
     * Lista delle iscrizioni abilitate per il turno <br>
     */
    protected List<EditIscrizionePolymer> listaEditIscrizioniAbilitate;

    /**
     * Lista di EditIscrizionePolymer <br>
     * Proxy in modo che siano 'note' a questo livello <br>
     * Quelle originali le devo dichiarare al livello delle sottoclassi,
     * perché vengono iniettate nel polymer html con lo stesso ID
     * e quindi le devo dichiare SOLO se servono <br>
     */
    protected List<EditIscrizionePolymer> proxyEditIscrizioni;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    private WamService wamService;


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

        if (turno == null) {
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
    protected void elaboraParameter(String turnoKey) {
        if (text.isValid(turnoKey)) {
            turno = turnoService.findById(turnoKey);
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

        turno = turnoService.newEntity(giorno, servizio);

        //--elimino l'ID del turno, per poterlo distinguere da un turno esistente e già nel mongoDB
        //--l'ID viene costruioto automaticamente con newEntity() per averlo (di norma) subito disponibile
        //--ma viene in ogni caso ri-creato da beforeSave()
        turno.id = null;
    }// end of method


    /**
     * Costruisce la pagina <br>
     * Recupera il servizio <br>
     * Aggancia il listener del bottone 'conferma' della ButtonsBar <br>
     */
    protected void layoutPolymer() {
        //--informazioni sul servizio in alto
        //--regolo il componente per il turno indicato
        servizioPolymer.inizia(turno);

        proxyEditIscrizioni = new ArrayList<>();

        //--una o più iscrizioni (fino a quattro) a secondo del tipo di servizio previsto per il turno
        iniziaIscrizione();

        //--bottoni di 'annulla' e 'conferma' in basso
        //--se mancava il turno arriva un turno provvisorio in memoria (senza ID) non registrato
        //--se manca il turno viene abilitato il bottone 'conferma' per creare il nuovo turno anche vuoto
        //--l'azione di 'annulla' è standard e rimanda al tabellone (non necessita di un listener specifico da aggiunger qui)
        if (text.isValid(turno.id)) {
            bottoniPolymer.setConfermaEnabled(false);
            bottoniPolymer.setConfermaTooltips("Conferma delle modifiche effettuate.");
        } else {
            bottoniPolymer.setConfermaEnabled(true);
            bottoniPolymer.setConfermaTooltips("Creazione di un nuovo turno.");
        }// end of if/else cycle
        bottoniPolymer.addConfermaListener(e -> conferma());
    }// end of method


    /**
     * Regola (nella sottoclasse) i componenti iniettati nel polymer html <br>
     * Invocare SEMPRE anche il metodo della superclasse
     * <p>
     * Qui stabilisce le property del turno (tutte le iscrizioni)
     * <p>
     * Utente collegato
     * Funzioni per cui è abilitato
     */
    protected void iniziaIscrizione() {
        regolaAbilitazioneIscrizioni();
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
    protected void regolaAbilitazioneIscrizioni() {
        List<Iscrizione> listaIscrizioni;
        Milite militeIsc;
        boolean militeLoggatoGiaSegnato = false;
        listaEditIscrizioniAbilitate = new ArrayList<>();
//        Iscrizione iscrizioneSegnata = null;
        EditIscrizionePolymer editIscrizioneGiaSegnata = null;
        Funzione funz;

        // @todo Controlla se siamo loggati come developer, come admin o come user <br>

        //--Recupera le funzioni abilitate del milite loggato
        this.militeLoggato = wamService.getMilite();
        this.listaIDFunzioniAbilitate = militeLoggato != null ? militeService.getListaIDFunzioni(militeLoggato) : null;

        // @todo per adesso
        if (militeLoggato == null) {
            return;
        }// end of if cycle

        //--Controlla se il milite loggato è già segnato in una iscrizione. Se è così disabilita tutte le altre
        //--Ragioniamo sulle iscrizioni a video non sul DB
        for (EditIscrizionePolymer editIsc : proxyEditIscrizioni) {
            militeIsc = editIsc.getMilite();
            if (militeIsc != null && militeIsc.id.equals(militeLoggato.id)) {
                militeLoggatoGiaSegnato = true;
                editIscrizioneGiaSegnata = editIsc;
            }// end of if cycle
        }// end of for cycle

        if (militeLoggatoGiaSegnato) {
            for (EditIscrizionePolymer editIsc : proxyEditIscrizioni) {
                Object iscCorrente = editIsc.iscrizioneEntity;
                Object iscLoggata = editIscrizioneGiaSegnata.iscrizioneEntity;
                editIsc.abilitata = iscCorrente.equals(iscLoggata);
                listaEditIscrizioniAbilitate.add(editIsc);
            }// end of for cycle
        } else {
            for (EditIscrizionePolymer editIsc : proxyEditIscrizioni) {
                if (editIsc.getMilite() != null) {
                    editIsc.abilitata = false;
                } else {
                    funz = editIsc.getFunzioneEntity();
                    editIsc.abilitata = listaIDFunzioniAbilitate.contains(funz.id);
                }// end of if/else cycle
                listaEditIscrizioniAbilitate.add(editIsc);
            }// end of for cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Evento lanciato dal bottone Conferma della ButtonsBar <br>
     * Recupera i dati di tutte le iscrizioni presenti <br>
     * Controlla che il milite non sia già segnato nel turno <br>
     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
     * Metodo sovrascritto. Invocare DOPO il metodo della superclasse <br>
     */
    protected void conferma() {
        if (segnatoNelTurno()) {
            Notification.show("Non ci si può segnare due volte nello stesso turno", 3000, Notification.Position.MIDDLE);
            return;
        }// end of if cycle

        //@todo dovrebbero arrivare già regolati dal click sul nome
//        for (Iscrizione iscrizione : turno.iscrizioni) {
//            iscrizioneService.setInizio(iscrizione, turno);
//        }// end of for cycle
        //@todo dovrebbero arrivare già regolati dal click sul nome

        turnoService.save(turno);
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }// end of method


    /**
     * Regola l'iscrizione del turno coi valori del componente grafico (UI) <br>
     * Chiamato dal bottone Conferma <br>
     */
    protected void bind(Turno turno, int pos, EditIscrizionePolymer edit) {
        pos--;
        if (turno.iscrizioni.size() > pos) {
            turno.iscrizioni.get(pos).milite = edit.getMilite();
            turno.iscrizioni.get(pos).inizio = edit.getInizio();
            turno.iscrizioni.get(pos).note = edit.getNote();
            turno.iscrizioni.get(pos).fine = edit.getFine();
        }// end of if cycle
    }// end of method


    /**
     * Controlla che il milite non sia già segnato nel turno <br>
     */
    protected boolean segnatoNelTurno() {
        boolean status = false;
        List<Milite> lista = new ArrayList<>();

        for (Iscrizione iscr : turno.iscrizioni) {
            if (lista.contains(iscr.milite)) {
                status = true;
            } else {
                lista.add(iscr.milite);
            }// end of if/else cycle
        }// end of for cycle

        return false;
    }// end of method


}// end of class
