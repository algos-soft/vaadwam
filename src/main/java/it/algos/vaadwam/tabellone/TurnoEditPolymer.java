package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.MOSTRA_ORARIO_SERVIZIO;

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
    private ApplicationContext appContext;

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

    @Getter
    private Turno turno;

    @Autowired
    private AArrayService array;

    private ITabellone tabellone;

    private Dialog dialogo;

    // Componenti editor di singola iscrizione inseriti nel dialogo
    @Getter
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
        this.turno =turno;

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
        String data = dateService.get(turno.getGiorno(), EATime.completa);
        getModel().setGiorno(data);

        // descrizione estesa del servizio
        Servizio servizio = turno.getServizio();
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
        Servizio servizio;

        if (turno != null) {

            servizio = turno.getServizio();

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

        for(Iscrizione iscrizione : turno.getIscrizioni()){
            CompIscrizione comp = appContext.getBean(CompIscrizione.class, iscrizione, this);
            componenti.add(comp);
        }

        return componenti;
    }


    /**
     * Evento lanciato dal bottone Annulla <br>
     */
    @EventHandler
    private void handleAnnulla() {
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
    private void handleConferma() {

        // qui eventuali validazioni dei dati dialogo nel loro complesso
        // ... per ora non ce ne sono

        // aggiorna l'oggetto turno come da dialogo
        syncTurno();

        // lo passa al tabellone per la registrazione
        tabellone.confermaDialogoTurno(dialogo, turno);

    }


    /**
     * Sincronizza le iscrizioni dell'oggetto Turno ricevuto nel costruttore
     * in base allo stato corrente delle iscrizioni contenute nel dialogo.
     */
    private void syncTurno(){

        List<Iscrizione> iscrizioni= turno.getIscrizioni();

        for(Iscrizione iscrizione : iscrizioni){

            CompIscrizione ci = findCompIscrizione(iscrizione);
            if (ci!=null){
                syncIscrizione(iscrizione, ci);
            }else{
                log.error("Componente iscrizione non trovato per l'iscrizione "+iscrizione.getFunzione().getCode());
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

        // sync timestamps
        iscrizione.setInizio(compIscrizione.getOraInizio());
        iscrizione.setFine(compIscrizione.getOraFine());

        // sync note
        iscrizione.setNote(compIscrizione.getNote());

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

}
