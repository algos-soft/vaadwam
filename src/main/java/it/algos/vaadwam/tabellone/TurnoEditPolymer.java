package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.iscrizioni.ButtonsBar;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.time.LocalTime;
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
 */
@Route(value = TAG_TURNO_EDIT)
@Tag("turno-edit")
@HtmlImport("src/views/tabellone/turno-edit.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class TurnoEditPolymer extends PolymerTemplate<TurnoEditModel> implements HasUrlParameter<String> {

//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("giorno")
//    public Span giorno;
//
//    /**
//     * Component iniettato nel polymer html con lo stesso ID <br>
//     */
//    @Id("servizio")
//    public Span servizio;

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
    protected TurnoService turnoService;

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
    protected ADateService dateService;

    //--property bean
    protected Turno turno = null;

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
     * Regola i dati da presentare in base al turno selezionato <br>
     * Il turno arriva come parametro di @Route alla classe TurnoEditIscrizioniPolymer <br>
     * Invocato dal metodo TurnoEditIscrizioniPolymer.setParameter() della sottoclasse <br>
     */
    public void layoutPolymer() {
        fixGiorno();
        fixServizio();
        fixOrario();
        fixAnnulla();
        fixConferma();
    }// end of method


    /**
     * Data completa (estesa) del giorno di esecuzione del turno <br>
     */
    private void fixGiorno() {
        String data;
        LocalDate giorno = null;

        if (turno != null) {
            giorno = turno.getGiorno();
        }// end of if cycle

        if (giorno != null) {
            data = dateService.get(giorno, EATime.completa);
            getModel().setGiorno(data);
        }// end of if cycle

    }// end of method


    /**
     * Descrizione del servizio <br>
     */
    private void fixServizio() {
        Servizio servizio = null;

        if (turno != null) {
            servizio = turno.getServizio();
        }// end of if cycle

        if (servizio != null) {
            getModel().setServizio(servizio.descrizione);
        }// end of if cycle
    }// end of method

    /**
     * Orario (eventuale) del turno <br>
     */
    private void fixOrario() {
        String orario = "";
        Servizio servizio = null;

        if (turno != null) {
            servizio = turno.getServizio();
        }// end of if cycle

        if (servizio != null) {
            if (pref.isBool(MOSTRA_ORARIO_SERVIZIO)) {
                orario = servizioService.getOrario(servizio);
                getModel().setOrario(orario);
            }// end of if cycle
        }// end of if cycle

        getModel().setUsaOrario(servizio != null && servizio.isOrarioDefinito());
        getModel().setNotUsaOrario(servizio != null && !servizio.isOrarioDefinito());
        getModel().setInizioExtra(LocalTime.MIDNIGHT.toString());
        getModel().setFineExtra(LocalTime.MIDNIGHT.toString());
    }// end of method


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixAnnulla() {
        setAnnullaText("Annulla");
        setAnnullaIcon(VaadinIcon.ARROW_LEFT);
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            annulla.addClickShortcut(Key.ARROW_LEFT);
        }// end of if cycle
        annulla.addClickListener(e -> ritorno());
        this.setAnnullaTooltips("Ritorno al tabellone");
    }// end of method


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixConferma() {
        setConfermaText("Conferma");
        setConfermaIcon(VaadinIcon.CHECK);
        setConfermaEnabled(false);
    }// end of method


    public void setAnnullaText(String annullaText) {
        annulla.setText(annullaText != null ? annullaText : "");
    }// end of method


    public void setConfermaText(String confermaText) {
        conferma.setText(confermaText != null ? confermaText : "");
    }// end of method


    public void setAnnullaIcon(VaadinIcon annullaIcon) {
        if (annullaIcon != null) {
            annulla.setIcon(new Icon(annullaIcon));
        }// end of if cycle
    }// end of method


    public void setConfermaIcon(VaadinIcon confermaIcon) {
        if (confermaIcon != null) {
            conferma.setIcon(new Icon(confermaIcon));
        }// end of if cycle
    }// end of method


    public void setAnnullaEnabled(boolean annullaEnabled) {
        annulla.setEnabled(annullaEnabled);
    }// end of method


    public void setConfermaEnabled(boolean confermaEnabled) {
        conferma.setEnabled(confermaEnabled);
    }// end of method


    public void setAnnullaTooltips(String toolTips) {
        annulla.getElement().setAttribute("title", toolTips);
    }// end of method


    public void setConfermaTooltips(String toolTips) {
        conferma.getElement().setAttribute("title", toolTips);
    }// end of method


    public Registration addAnnullalListener(ComponentEventListener<TurnoEditPolymer.AnnullaEvent> listener) {
        return annulla.addClickListener(e -> listener.onComponentEvent(new TurnoEditPolymer.AnnullaEvent(this, true)));
    }// end of method


    public Registration addConfermaListener(ComponentEventListener<TurnoEditPolymer.ConfermaEvent> listener) {
        return conferma.addClickListener(e -> listener.onComponentEvent(new TurnoEditPolymer.ConfermaEvent(this, true)));
    }// end of method

    private void ritorno() {
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }// end of method


    public static class AnnullaEvent extends ComponentEvent<TurnoEditPolymer> {
        public AnnullaEvent(TurnoEditPolymer source, boolean fromClient) {
            super(source, fromClient);
        }// end of constructor
    }// end of method


    public static class ConfermaEvent extends ComponentEvent<TurnoEditPolymer> {
        public ConfermaEvent(TurnoEditPolymer source, boolean fromClient) {
            super(source, fromClient);
        }// end of constructor
    }// end of method

}// end of class
