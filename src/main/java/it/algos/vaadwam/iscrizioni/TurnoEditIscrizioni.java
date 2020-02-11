package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.tabellone.TabelloneService;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 20-dic-2019
 * Time: 07:17
 */
@Route(value = TAG_TURNO_EDIT)
@Tag("turno-edit-iscrizioni")
@HtmlImport("src/views/iscrizioni/turno-edit-iscrizioni.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class TurnoEditIscrizioni extends PolymerTemplate<TurnoEditIscrizioni.ServizioModel> implements HasUrlParameter<String> {

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

//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("funzione")
//    public Button funzioneButton;
//
//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("milite")
//    public Button militeButton;
//
//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("inizio")
//    public TimePicker inizio;
//
//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("note")
//    public TextField note;
//
//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("fine")
//    public TimePicker fine;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected TurnoService turnoService;

    //--property bean
    protected Turno turno = null;

    /**
     * Turno di questa iscrizione <br>
     */
    private Turno turnoEntity;

    /**
     * Iscrizione corrente <br>
     */
    private Iscrizione iscrizioneEntity;

    /**
     * Funzione corrente <br>
     */
    private Funzione funzioneEntity;

    /**
     * Milite di questa iscrizione <br>
     * Nella UI (testo del bottone 'milite') viene mostrata la 'sigla' del Milite <br>
     */
    private Milite militeEntity;

    /**
     * Milite loggato al momento <br>
     */
    private Milite militeLoggato;

    /**
     * Bottoni 'annulla' e 'conferma' <br>
     */
    private ButtonsBar bottoniPolymer;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    private PreferenzaService pref;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AArrayService array;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private MiliteService militeService;

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
    private TabelloneService tabelloneService;

    //--modello dati interno
    private ServizioModel modello =getModel();


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
        String numGiorniDelta = "";
        LocalDate giorno = null;
        List<String> listaServizi;
        String servizioKey = "";
        Servizio servizio = null;

        listaGiorni = parametersMap.get(KEY_MAP_GIORNO);
        if (array.isValid(listaGiorni) && listaGiorni.size() == 1) {
            numGiorniDelta = listaGiorni.get(0);
        }// end of if cycle
        if (text.isValid(numGiorniDelta)) {
            giorno = dateService.getGiornoDelta(numGiorniDelta);
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
     * Regola i dati da presentare in base al turno ed alla iscrizione selezionata <br>
     * Metodo invocato da una sottoclasse di TurnoEditIscrizioniPolymer <br>
     */
//    @PostConstruct
    public void layoutPolymer() {
        this.turnoEntity = turno;
//        this.funzioneEntity = iscrizioneEntity.getFunzione();
//        this.militeEntity = iscrizioneEntity.getMilite();
//        this.militeLoggato = wamService.getMilite();
//        this.bottoniPolymer = bottoniPolymer;

        inizia(turno);
//        fixAbilitazione();
//        fixColor();
//        fixIcona();
//        fixFunzCode();
//        fixMilite();
//        fixListener();
//        fixInizio();
//        fixNote();
//        fixFine();

    }// end of method


    /**
     * Regola i dati da presentare in base al turno selezionato <br>
     * Il turno arriva come parametro di @Route alla classe TurnoEditIscrizioniPolymer <br>
     * Invocato dal metodo TurnoEditIscrizioniPolymer.setParameter() della sottoclasse <br>
     */
    public void inizia(Turno turno) {
        fixData(turno.getGiorno());
        fixOrario(turno.getServizio());
        fixServizio(turno.getServizio());
    }// end of method


    /**
     * Data completa (estesa) del giorno di esecuzione del turno <br>
     */
    private void fixData(LocalDate giorno) {
        String data = "";

        if (giorno != null) {
            data = dateService.get(giorno, EATime.completa);
            modello.setData(data);
        }// end of if cycle

    }// end of method


    /**
     * Orario (eventuale) del turno <br>
     */
    private void fixOrario(Servizio servizio) {
        String orario = "";

        if (servizio != null) {
            if (pref.isBool(MOSTRA_ORARIO_SERVIZIO)) {
                orario = servizioService.getOrario(servizio);
                modello.setOrario(orario);
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Descrizione del servizio <br>
     */
    private void fixServizio(Servizio servizio) {
        if (servizio != null) {
            modello.setServizio(servizio.descrizione);
        }// end of if cycle
    }// end of method


    /**
     * Modello dati per collegare questa classe java col polymer
     */
    public interface ServizioModel extends TemplateModel {

        void setData(String data);

        void setOrario(String orario);

        void setServizio(String servizio);

    }// end of interface

}// end of class
