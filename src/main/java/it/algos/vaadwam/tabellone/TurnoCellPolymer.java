package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.wam.WamLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 05-mar-2019
 * Time: 14:48
 * <p>
 * Singola cella del tabellone <br>
 * 1.Icona
 * 2.Nome del milite
 * 3.Colore
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Tag("turno-cell")
@HtmlImport("src/views/tabellone/turnoCellPolymer.html")
public class TurnoCellPolymer extends PolymerTemplate<TurnoCellModel> {

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ADateService date = ADateService.getInstance();

    @Autowired
    protected ATextService text;

    @Autowired
    protected AArrayService array;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected RigaService rigaService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TabelloneService tabelloneService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ServizioService servizioService;

    private Riga riga;

    private Turno turno;

    private LocalDate giorno;

    @Autowired
    private WamLogin wamLogin;

    @Value("${wam.tabellone.turnoInDialogo}")
    private boolean turnoInDialogo;

    private ITabellone tabellone;

    public TurnoCellPolymer() {
    }// end of Spring constructor


    /**
     * Constructor.
     */
    public TurnoCellPolymer(Riga riga, LocalDate giorno) {
        this.riga = riga;
        this.giorno = giorno;
    }

    /**
     * Constructor.
     */
    public TurnoCellPolymer(ITabellone tabellone, Riga riga, LocalDate giorno) {
        this.tabellone=tabellone;
        this.riga = riga;
        this.giorno = giorno;
    }



    @PostConstruct
    private void inizia() {
        colorazione();
    }// end of method


    private void colorazione() {
        boolean differenziata = pref.isBool(USA_COLORAZIONE_DIFFERENZIATA);
        List<Iscrizione> iscrizioni = null;
        Servizio servizio = riga.servizio;
        List<Funzione> funzioni = null;
        String icona = "";
        List<RigaCella> righeCella = new ArrayList<>();
        EAWamColore eaColore = null;
        boolean aggiungeAvviso;

        turno = rigaService.getTurno(riga, giorno);
        eaColore = tabelloneService.getColoreTurno(turno);

        if (turno != null) {
            iscrizioni = turno.getIscrizioni();
        }// end of if cycle

        if (servizio != null) {
            funzioni = servizioService.getFunzioniAll(servizio);
        }// end of if cycle

        if (funzioni != null) {
            for (Funzione funzServ : funzioni) {
                icona = "vaadin:" + funzServ.icona.name().toLowerCase();
                if (iscrizioni != null) {
                    for (Iscrizione iscr : iscrizioni) {
                        if (differenziata) {
                            eaColore = tabelloneService.getColoreIscrizione(turno, iscr);
                        }// end of if cycle
                        if (iscr.funzione.code.equals(funzServ.code)) {
                            if (iscr.milite != null) {
                                aggiungeAvviso = aggiungeAvviso(iscr);
                                righeCella.add(new RigaCella(eaColore, icona, iscr.milite.getSigla(), aggiungeAvviso));
                            } else {
                                righeCella.add(new RigaCella(eaColore, icona, ""));
                            }// end of if/else cycle
                        }// end of if cycle
                    }// end of for cycle
                } else {
                    righeCella.add(new RigaCella(eaColore, "", ""));
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        getModel().setRighecella(righeCella);
    }// end of method


    private boolean aggiungeAvviso(Iscrizione iscr) {
        boolean status = false;

        if (iscr.note != null && iscr.note.length() > 0) {
            status = true;
        }// end of if cycle
        if (iscr.inizio != null && (iscr.inizio.compareTo(turno.inizio) != 0 || turno.inizio == LocalTime.MIDNIGHT)) {
            status = true;
        }// end of if cycle
        if (iscr.fine != null && (iscr.fine.compareTo(turno.fine) != 0 || turno.fine == LocalTime.MIDNIGHT)) {
            status = true;
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file turnoCellPolymer.html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Non occorre ricevere parametri perché questa istanza è già specifica di un particolare turno <br>
     * Viene aperta una pagina per controllare/modificare le iscrizioni del turno <br>
     * La pagina viene raggiunta con una navigazione via @Route, passando la turnoKey del turno selezionato <br>
     * La idKey del turno viene passata nell'URL <br>
     * Alla chiusura della pagina la navigazione via @Route rimanda al Tabellone <br>
     */
    @EventHandler
    void handleClick() {
        if(turnoInDialogo){ // modalità in dialogo

        }else{ // modalità in pagina separata

            if (turno != null) {
                handleClickTurnoEsistente();
            } else {
                handleClickTurnoVuoto();
            }

        }
    }



    /**
     * Non occorre ricevere parametri perché questa istanza è già specifica di un particolare turno <br>
     * Viene aperta una pagina per controllare/modificare le iscrizioni del turno <br>
     * La pagina viene raggiunta con una navigazione via @Route, passando la turnoKey del turno selezionato <br>
     * La idKey del turno viene passata nell'URL <br>
     * Alla chiusura della pagina la navigazione via @Route rimanda al Tabellone <br>
     */
    private void handleClickTurnoEsistente() {
        getUI().ifPresent(ui -> ui.navigate(TAG_TURNO_EDIT + "/" + turno));
    }// end of method


    /**
     * Non occorre ricevere parametri perché questa istanza è già specifica di un particolare turno <br>
     * Viene aperta una pagina per controllare/modificare le iscrizioni del turno <br>
     * La pagina viene raggiunta con una navigazione via @Route, passando la turnoKey=null <br>
     * La idKey del turno viene passata nell'URL <br>
     * Alla chiusura della pagina la navigazione via @Route rimanda al Tabellone <br>
     */
    private void handleClickTurnoVuoto() {
        Map<String, List<String>> mappa = null;
        List<String> lista;
        Servizio servizio = riga.servizio;

        if (pref.isBool(EAPreferenzaWam.nuovoTurno) || wamLogin.isAdminOrDev()) {
            mappa = new HashMap<String, List<String>>();

            lista = new ArrayList<>();
            lista.add(giorno.format(DateTimeFormatter.ISO_DATE));
            mappa.put(KEY_MAP_GIORNO, lista);

            lista = new ArrayList<>();
            lista.add(servizio.id);
            mappa.put(KEY_MAP_SERVIZIO, lista);

            final QueryParameters query = new QueryParameters(mappa);
            getUI().ifPresent(ui -> ui.navigate(TAG_TURNO_EDIT, query));
        } else {
            String desc = servizio.descrizione;
            String giornoTxt = date.get(giorno, EATime.weekShortMese);
            Notification.show("Per " + giornoTxt + " non è (ancora) previsto un turno di " + desc + ". Per crearlo, devi chiedere ad un admin", 5000, Notification.Position.MIDDLE);
        }// end of if/else cycle
    }// end of method


}// end of class
