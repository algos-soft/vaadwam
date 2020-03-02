package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.tabellone.TabelloneService;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_CRO;
import static it.algos.vaadwam.application.WamCost.USA_COLORAZIONE_DIFFERENZIATA;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 09-ago-2019
 * Time: 19:26
 * <p>
 * Java wrapper of the polymer element `edit-iscrizione`
 */
@Tag("edit-iscrizione")
@HtmlImport("src/views/iscrizioni/edit-iscrizione.html")
public class EditIscrizionePolymer extends PolymerTemplate<TemplateModel> {

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("funzione")
    public Button funzioneButton;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("milite")
    public Button militeButton;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("inizio")
    public TimePicker inizio;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("note")
    public TextField note;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("fine")
    public TimePicker fine;

    /**
     * Iscrizione corrente <br>
     */
    public Iscrizione iscrizioneEntity;

    protected boolean abilitata;

    /**
     * Turno di questa iscrizione <br>
     */
    private Turno turnoEntity;

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

    @Autowired
    private FunzioneService funzioneService;


    /**
     * Regola i dati da presentare in base al turno ed alla iscrizione selezionata <br>
     * Metodo invocato da una sottoclasse di TurnoEditIscrizioniPolymer <br>
     */
    public void inizia(Turno turno, Iscrizione iscrizione, ButtonsBar bottoniPolymer) {
        this.turnoEntity = turno;
        this.iscrizioneEntity = iscrizione;
        this.funzioneEntity = iscrizioneEntity.getFunzione();
        this.militeEntity = iscrizioneEntity.getMilite();
        this.militeLoggato = wamService.getMilite();
        this.bottoniPolymer = bottoniPolymer;
    }// end of method


    /**
     * Regola i dati da presentare in base al turno ed alla iscrizione selezionata <br>
     * Metodo invocato da una sottoclasse di TurnoEditIscrizioniPolymer <br>
     */
    public void inizia(boolean abilitata) {
        this.abilitata = abilitata;
        fixAbilitazione();
        fixColor();
        fixIcona();
        fixFunzCode();
        fixMilite();
        fixListener();
        fixInizio();
        fixNote();
        fixFine();
    }// end of method


    /**
     * Regola i dati da presentare in base al turno ed alla iscrizione selezionata <br>
     * Metodo invocato da una sottoclasse di TurnoEditIscrizioniPolymer <br>
     */
    public void inizia(Turno turno, Iscrizione iscrizione, ButtonsBar bottoniPolymer, boolean abilitata) {
        inizia(turno, iscrizione, bottoniPolymer);
        inizia(abilitata);
    }// end of method


    /**
     * Abilitazione dei due bottoni della prima riga (funzione e milite) <br>
     */
    private void fixAbilitazione() {
        boolean isMiliteLoggatoSegnatoNelTurno = isMiliteLoggatoSegnatoNelTurno(turnoEntity);

        funzioneButton.setEnabled(abilitata);
        militeButton.setEnabled(abilitata);


//        if (isMiliteLoggatoSegnatoNelTurno) {
//            if (militeEntity != null && militeEntity.getSigla().equals(militeLoggato.getSigla())) {
//                funzioneButton.setEnabled(true);
//                militeButton.setEnabled(true);
//            } else {
//                funzioneButton.setEnabled(false);
//                militeButton.setEnabled(false);
//            }// end of if/else cycle
//        } else {
//            if (militeEntity == null && militeService.isAbilitato(militeLoggato, funzioneEntity)) {
//                funzioneButton.setEnabled(true);
//                militeButton.setEnabled(true);
//            } else {
//                funzioneButton.setEnabled(false);
//                militeButton.setEnabled(false);
//            }// end of if/else cycle
//        }// end of if/else cycle
    }// end of method


    /**
     * Colore dei due bottoni della prima riga (funzione e milite) <br>
     */
    private void fixColor() {
        String colore = "";

        if (pref.isBool(USA_COLORAZIONE_DIFFERENZIATA)) {
            colore = tabelloneService.getColoreIscrizione(turnoEntity, iscrizioneEntity).getTag().toLowerCase();
        } else {
            colore = tabelloneService.getColoreTurno(turnoEntity).getTag().toLowerCase();
        }// end of if/else cycle

        if (text.isValid(colore)) {
            funzioneButton.getElement().getStyle().set("background-color", colore);
            militeButton.getElement().getStyle().set("background-color", colore);
        }// end of if cycle

    }// end of method


    /**
     * Icona della funzione di questa iscrizione <br>
     */
    private void fixIcona() {
        String tag = "vaadin";
        String iconaTxt = "";

        if (funzioneButton != null) {
            Funzione funzione = funzioneService.findById(funzioneEntity.id);
            iconaTxt = funzione.icona.name().toLowerCase();
        }// end of if cycle

        if (text.isValid(iconaTxt)) {
            funzioneButton.setIcon(new Icon(tag, iconaTxt));
            funzioneButton.setIconAfterText(true);
        }// end of if cycle

    }// end of method


    /**
     * Sigla della funzione di questa iscrizione <br>
     */
    private void fixFunzCode() {
        String funzCode = "";

        if (funzioneEntity != null) {
            funzCode = funzioneEntity.code;
        }// end of if cycle

        if (text.isValid(funzCode)) {
            funzioneButton.setText(funzCode);
        }// end of if cycle

    }// end of method


    /**
     * Milite di questa iscrizione <br>
     * NickName del Milite di questa iscrizione <br>
     */
    private void fixMilite() {
        String nickName = "";

        if (militeEntity != null) {
            nickName = militeEntity.getSigla();
        }// end of if/else cycle

        if (text.isValid(nickName)) {
            militeButton.setText(nickName);
        }// end of if cycle
    }// end of method


    /**
     * Listener dei bottoni funzione e milite per selezionare il milite <br>
     */
    private void fixListener() {
        if (militeEntity != null) {
            funzioneButton.addClickListener(e -> {
                cancellaMilite();
                syncCode();
            });//end of lambda expressions
            militeButton.addClickListener(e -> {
                cancellaMilite();
                syncCode();
            });//end of lambda expressions
        } else {
            funzioneButton.addClickListener(e -> {
                segnaMilite();
                syncCode();
            });//end of lambda expressions
            militeButton.addClickListener(e -> {
                segnaMilite();
            });//end of lambda expressions
        }// end of if/else cycle
    }// end of method


    /**
     * Orario di inizio turno specifico di questa iscrizione (indipendentemente da quello del servizio) <br>
     * Se c'è un milite segnato, mostra l'orario
     */
    private void fixInizio() {
        LocalTime time = iscrizioneEntity.inizio != null ? iscrizioneEntity.inizio : LocalTime.MIDNIGHT;

        if (time != null) {
            inizio.setValue(time);
            inizio.setStep(Duration.ofMinutes(15));
            inizio.setLocale(Locale.ITALIAN);
        }// end of if cycle

        if (inizio != null) {
            inizio.addValueChangeListener(e -> pippoz());
        }// end of if cycle

        inizio.setEnabled(abilitata && iscrizioneEntity.milite != null);
    }// end of method

    private void pippoz() {
//        Object alfa= inizio.getValue();
//        int a=87;
    }// end of method

    /**
     * Note di questa iscrizione <br>
     */
    private void fixNote() {
        String noteTxt = iscrizioneEntity.note;
        String tag = "...";

        note.setPlaceholder(tag);
        if (text.isValid(noteTxt)) {
            note.setValue(noteTxt);
        }// end of if cycle

        if (note != null) {
            note.addValueChangeListener(e -> confermaOK());
        }// end of if cycle

        note.setEnabled(abilitata && iscrizioneEntity.milite != null);
    }// end of method


    /**
     * Orario di fine turno specifico di questa iscrizione (indipendentemente da quello del servizio) <br>
     */
    private void fixFine() {
        LocalTime time = iscrizioneEntity.fine != null ? iscrizioneEntity.fine : LocalTime.MIDNIGHT;

        if (time != null) {
            fine.setValue(time);
            fine.setStep(Duration.ofMinutes(15));
            fine.setLocale(Locale.ITALIAN);
        }// end of if cycle

        if (fine != null) {
            fine.addValueChangeListener(e -> confermaOK());
        }// end of if cycle

        fine.setEnabled(abilitata && iscrizioneEntity.milite != null);
    }// end of method


    /**
     * Azione lanciata dai bottoni funzione o milite <br>
     */
    private void cancellaMilite() {
        militeEntity = null;
        militeButton.setText("");
        inizio.setValue(LocalTime.MIDNIGHT);
        note.setValue(VUOTA);
        fine.setValue(LocalTime.MIDNIGHT);
        bottoniPolymer.setConfermaEnabled(true);
    }// end of method


    /**
     * Azione lanciata dai bottoni funzione o milite <br>
     */
    private void segnaMilite() {
        militeEntity = militeLoggato;
        militeButton.setText(militeEntity.getSigla());
        inizio.setValue(turnoEntity.inizio);
        inizio.setEnabled(true);
        note.setEnabled(true);
        fine.setValue(turnoEntity.fine);
        fine.setEnabled(true);
        bottoniPolymer.setConfermaEnabled(true);
    }// end of method


    private boolean isMiliteLoggatoSegnatoNelTurno(Turno turno) {
        boolean status = false;

        if (militeLoggato != null && turno != null && array.isValid(turno.iscrizioni)) {
            for (Iscrizione iscr : turno.iscrizioni) {
                if (iscr.milite != null && iscr.milite.id.equals(militeLoggato.id)) {
                    status = true;
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Milite selezionato <br>
     * Recupera dal nickName (unico) <br>
     */
    public Milite getMilite() {
        return militeEntity;
    }// end of method


    /**
     * Milite selezionato <br>
     * Recupera dal nickName (unico) <br>
     */
    public void setMilite(Milite milite) {
        militeEntity = milite;
    }// end of method


    public Funzione getFunzioneEntity() {
        return funzioneEntity;
    }// end of method


    public void setFunzioneEntity(Funzione funzioneEntity) {
        this.funzioneEntity = funzioneEntity;
    }// end of method


    /**
     * Tempo di inizio turno <br>
     * Recupera dal TimePicker <br>
     */
    public LocalTime getInizio() {
        LocalTime time = null;
        Optional optional = inizio.getOptionalValue();
        time = (LocalTime) optional.get();

        return time;
    }// end of method


    /**
     * Note del turno <br>
     * Recupera dal TextField <br>
     */
    public String getNote() {
        return note.getValue();
    }// end of method


    /**
     * Tempo di fine turno <br>
     * Recupera dal TimePicker <br>
     */
    public LocalTime getFine() {
        LocalTime time = null;
        Optional optional = fine.getOptionalValue();
        time = (LocalTime) optional.get();

        return time;
    }// end of method


    /**
     * Controlla il bottone 'registra' <br>
     */
    public void confermaOK() {
        bottoniPolymer.setConfermaEnabled(true);
    }// end of method

    /**
     * Controlla il bottone 'registra' <br>
     */
    public void syncCode() {
        boolean status = false;

        if (inizio.getValue() == iscrizioneEntity.inizio) {
            status = true;
        }// end of if cycle

//        bottoniPolymer.setConfermaEnabled(status);
    }// end of method

}// end of class
