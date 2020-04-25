package it.algos.vaadwam.tabellone;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalTime;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.USA_COLORAZIONE_DIFFERENZIATA;
import static it.algos.vaadwam.application.WamCost.USA_COLORAZIONE_TURNI;

/**
 * QUESTA CLASSE VA ELIMINATA, E' SOSTITUITA DA TurnoIscrzioneModel - Alex 12 apr 2020
 */


/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 10-mar-2020
 * Time: 09:06
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TurnoIscrizione {

    public Iscrizione iscrizioneEntity;

    public boolean abilitata;

    public boolean abilitataPicker;

    public String iconaTxt;

    public String funzioneTxt;

    public String militetxt;

    public String inizioTxt;

    public String noteTxt;

    public String fineTxt;

    public String coloreTxt;

    public Funzione funzioneEntity;

    public Milite militeEntity;

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
    private TabelloneService tabelloneService;

//    private int pos;

//    private TurnoEditModel modello;

    private Turno turnoEntity;



    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro del costruttore usato <br>
     */
    public TurnoIscrizione() {
    }// end of constructor


    public TurnoIscrizione(Turno turnoEntity, Iscrizione iscrizioneEntity) {
        this.turnoEntity = turnoEntity;
        this.iscrizioneEntity = iscrizioneEntity;
    }// end of constructor


    public TurnoIscrizione(TurnoEditModel modello, Turno turnoEntity, int pos) {
//        this.modello = modello;
        this.turnoEntity = turnoEntity;
//        this.pos = pos;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * L'istanza DEVE essere creata da SpringBoot con Object algos = appContext.getBean(AlgosClass.class);  <br>
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        LocalTime inizioTime = iscrizioneEntity != null ? iscrizioneEntity.inizio : null;
        LocalTime fineTime = iscrizioneEntity != null ? iscrizioneEntity.fine : null;
        Servizio servizio = turnoEntity.getServizio();

        abilitata = false;
        abilitataPicker = false;

        funzioneEntity = iscrizioneEntity != null ? iscrizioneEntity.funzione : null;
        militeEntity = iscrizioneEntity != null ? iscrizioneEntity.milite : null;

        coloreTxt = fixColor();
        iconaTxt = fixIcona();

        funzioneTxt = funzioneEntity != null ? funzioneEntity.code : VUOTA;
        militetxt = militeEntity != null ? militeEntity.username : VUOTA;

        inizioTxt = inizioTime != null ? inizioTime.toString() : servizio != null ? servizio.getInizio().toString() : LocalTime.MIDNIGHT.toString();
        noteTxt = iscrizioneEntity != null ? iscrizioneEntity.note : VUOTA;
        fineTxt = fineTime != null ? fineTime.toString() : servizio != null ? servizio.getFine().toString() : LocalTime.MIDNIGHT.toString();
    }// end of method


    /**
     * Colore dei due bottoni della prima riga (funzione e milite) di ogni iscrizione <br>
     */
    private String fixColor() {
        String colore = "";

        if (pref.isBool(USA_COLORAZIONE_TURNI)) {
            if (pref.isBool(USA_COLORAZIONE_DIFFERENZIATA)) {
                colore = tabelloneService.getColoreIscrizione(turnoEntity, iscrizioneEntity).getTag().toLowerCase();
            } else {
                colore = tabelloneService.getColoreTurno(turnoEntity).getTag().toLowerCase();
            }// end of if/else cycle
        } else {
            colore = VUOTA;
        }// end of if/else cycle

        return colore;
    }// end of method


    /**
     * Icona della funzione di questa iscrizione <br>
     */
    private String fixIcona() {
        String iconaTxt = "";
        String tag = "vaadin:";
        Funzione funzione;

        if (iscrizioneEntity != null) {
            funzione = iscrizioneEntity.funzione;
            iconaTxt = tag + funzione.icona.name().toLowerCase();
        }// end of if cycle

        return iconaTxt;
    }// end of method


}// end of class
