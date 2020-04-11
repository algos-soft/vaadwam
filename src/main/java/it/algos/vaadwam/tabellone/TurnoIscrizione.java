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

    public Iscrizione iscrizione;

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

    private int pos;

    private TurnoEditModel modello;

    private Turno turnoEntity;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro del costruttore usato <br>
     */
    public TurnoIscrizione() {
    }// end of constructor


    public TurnoIscrizione(TurnoEditModel modello, Turno turnoEntity, int pos) {
        this.modello = modello;
        this.turnoEntity = turnoEntity;
        this.pos = pos;
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
        if (pos == 0) {
            fixIscrizionePrima();
        }// end of if cycle

        if (pos == 1) {
            fixIscrizioneSeconda();
        }// end of if cycle

        if (pos == 2) {
            fixIscrizioneTerza();
        }// end of if cycle

        if (pos == 3) {
            fixIscrizioneQuarta();
        }// end of if cycle
    }// end of method


    private void fixIscrizioneBase(Iscrizione iscrizione) {
        LocalTime inizioTime = iscrizione != null ? iscrizione.inizio : null;
        LocalTime fineTime = iscrizione != null ? iscrizione.fine : null;
        Servizio servizio = turnoEntity.getServizio();

        abilitata = false;
        abilitataPicker = false;

        funzioneEntity = iscrizione != null ? iscrizione.funzione : null;
        militeEntity = iscrizione != null ? iscrizione.milite : null;

        coloreTxt = fixColor(iscrizione);
        iconaTxt = fixIcona(iscrizione);

        funzioneTxt = funzioneEntity != null ? funzioneEntity.code : VUOTA;
        militetxt = militeEntity != null ? militeEntity.username : VUOTA;

        inizioTxt = inizioTime != null ? inizioTime.toString() : servizio != null ? servizio.getInizio().toString() : LocalTime.MIDNIGHT.toString();
        noteTxt = iscrizione != null ? iscrizione.note : VUOTA;
        fineTxt = fineTime != null ? fineTime.toString() : servizio != null ? servizio.getFine().toString() : LocalTime.MIDNIGHT.toString();
    }// end of method


    public void fixIscrizionePrima() {
        iscrizione = turnoEntity.iscrizioni.get(0);
        fixIscrizioneBase(iscrizione);

        modello.setColorePrima(coloreTxt);
        modello.setIconaPrima(iconaTxt);

        modello.setFunzionePrima(funzioneTxt);
        modello.setMilitePrima(militetxt);

        modello.setInizioPrima(inizioTxt);
        modello.setNotePrima(VUOTA);
        modello.setFinePrima(fineTxt);
    }// end of method


    private void fixIscrizioneSeconda() {
        iscrizione = turnoEntity.iscrizioni.get(1);
        fixIscrizioneBase(iscrizione);

        modello.setColoreSeconda(coloreTxt);
        modello.setIconaSeconda(iconaTxt);

        modello.setFunzioneSeconda(funzioneTxt);
        modello.setMiliteSeconda(militetxt);

        modello.setInizioSeconda(inizioTxt);
        modello.setNoteSeconda(noteTxt);
        modello.setFineSeconda(fineTxt);
    }// end of method


    private void fixIscrizioneTerza() {
        iscrizione = turnoEntity.iscrizioni.get(2);
        fixIscrizioneBase(iscrizione);

        modello.setColoreTerza(coloreTxt);
        modello.setIconaTerza(iconaTxt);

        modello.setFunzioneTerza(funzioneTxt);
        modello.setMiliteTerza(militetxt);

        modello.setInizioTerza(inizioTxt);
        modello.setNoteTerza(noteTxt);
        modello.setFineTerza(fineTxt);
    }// end of method


    private void fixIscrizioneQuarta() {
        iscrizione = turnoEntity.iscrizioni.get(3);
        fixIscrizioneBase(iscrizione);

        modello.setColoreQuarta(coloreTxt);
        modello.setIconaQuarta(iconaTxt);

        modello.setFunzioneQuarta(funzioneTxt);
        modello.setMiliteQuarta(militetxt);

        modello.setInizioQuarta(inizioTxt);
        modello.setNoteQuarta(noteTxt);
        modello.setFineQuarta(fineTxt);
    }// end of method


    /**
     * Colore dei due bottoni della prima riga (funzione e milite) di ogni iscrizione <br>
     */
    private String fixColor(Iscrizione iscrizioneEntity) {
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
    private String fixIcona(Iscrizione iscrizioneEntity) {
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
