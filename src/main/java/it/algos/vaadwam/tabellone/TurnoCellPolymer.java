package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
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
 * Singola cella del tabellone
 * 1.Icona
 * 2.Nome del milite
 * 3.Colore
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Tag("turno-cell")
@HtmlImport("src/views/tabellone/turnoCellPolymer.html")
public class TurnoCellPolymer extends PolymerTemplate<TurnoCellModel>   {

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
    private ServizioService servizioService;

    @Autowired
    private ADateService dateService;

    private Riga riga;

    private Turno turno;

    private LocalDate giorno;

    @Autowired
    private WamLogin wamLogin;


    private ITabellone tabellone;

    public TurnoCellPolymer() {
    }


    /**
     * Constructor.
     * @deprecated
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

        // eventuale header
        if(!riga.getServizio().isOrarioDefinito()){
            Turno turno = getTurno();
            String text1=null;
            String text2=null;
            if(turno!=null){
                String oraIni=dateService.getOrario(turno.getInizio());
                String oraFine=dateService.getOrario(turno.getFine());
                if(oraIni!=null && oraFine!=null){
                    text1 = oraIni+" - "+oraFine;
                }

                text2=turno.getNote();

            }

            getModel().setUsaHeaders(true);
            getModel().setHeader1(text1);
            getModel().setHeader2(text2);
        }

        colorazione();
    }


    private Turno getTurno(){
        Turno turno=null;
        List<Turno> turni = riga.getTurni();
        if (turni!=null){
            for(Turno t:turni){
                if(t.getGiorno().equals(giorno)){
                    turno=t;
                    break;
                }
            }
        }
        return turno;
    }


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
        }

        if (servizio != null) {
            funzioni = servizioService.getFunzioniAll(servizio);
        }

        if (funzioni != null) {
            for (Funzione funzServ : funzioni) {
                icona = "vaadin:" + funzServ.icona.name().toLowerCase();
                if (iscrizioni != null) {
                    for (Iscrizione iscr : iscrizioni) {

                        if (differenziata) {
                            eaColore = tabelloneService.getColoreIscrizione(turno, iscr);
                        }

                        if (iscr.funzione.code.equals(funzServ.code)) {
                            if (iscr.milite != null) {
                                aggiungeAvviso = aggiungeAvviso(iscr);
                                righeCella.add(new RigaCella(eaColore, icona, iscr.milite.getSigla(), iscr.funzione.code, aggiungeAvviso));
                            } else {
                                righeCella.add(new RigaCella(eaColore, icona, "", iscr.funzione.code));
                            }
                        }

                    }
                } else {
                    righeCella.add(new RigaCella(eaColore, "", "", funzServ.code));
                }
            }
        }

        getModel().setRighecella(righeCella);
    }


    private boolean aggiungeAvviso(Iscrizione iscr) {
        boolean status = false;

        if (iscr.note != null && iscr.note.length() > 0) {
            status = true;
        }
        if (iscr.inizio != null && (iscr.inizio.compareTo(turno.inizio) != 0 || turno.inizio == LocalTime.MIDNIGHT)) {
            status = true;
        }
        if (iscr.fine != null && (iscr.fine.compareTo(turno.fine) != 0 || turno.fine == LocalTime.MIDNIGHT)) {
            status = true;
        }

        return status;
    }


    /**
     * Cella interna cliccata
     */
    @EventHandler
    void handleClick(@ModelItem RigaCella item) {
        if(tabellone!=null){
            tabellone.cellClicked(turno, giorno, riga.servizio, item.getFunzione());
        }
    }






}
