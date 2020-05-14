package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalTime;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadwam.application.WamCost.MOSTRA_ORARIO_SERVIZIO;

/**
 * Componente Editor di una singola iscrizione
 */
@Tag("iscrizione-editor")
@HtmlImport("src/views/tabellone/iscrizione-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class IscrizioneEditPolymer extends PolymerTemplate<IscrizioneEditModel> {

    @Autowired
    protected AVaadinService vaadinService;

    private Iscrizione iscrizione;

    private Turno turno;

    private ITabellone tabellone;

    private Dialog dialogo;

    private boolean readOnly;

    @Id("annulla")
    private Button bAnnulla;

    @Id("conferma")
    private Button bConferma;

    @Id("elimina")
    private Button bElimina;

    @Autowired
    private ADateService dateService;

    @Autowired
    private PreferenzaService pref;

    @Autowired
    private ServizioService servizioService;

    private WamLogin wamLogin;

    private Milite milite;


    /**
     * @param tabellone  il tabellone di riferimento per effettuare le callbacks
     * @param dialogo    il dialogo contenitore
     * @param iscrizione l'iscrizione da mostrare
     * @param readOnly   interfaccia in modalità read only
     */
    public IscrizioneEditPolymer(ITabellone tabellone, Dialog dialogo, Turno turno, Iscrizione iscrizione, boolean readOnly) {
        this.tabellone = tabellone;
        this.dialogo = dialogo;
        this.iscrizione = iscrizione;
        this.turno = turno;
        this.readOnly = readOnly;
    }


    @PostConstruct
    private void init() {

        AContext context = vaadinService.getSessionContext();
        wamLogin = (WamLogin) context.getLogin();

        // se l'iscrizione ha già un miite lo usa, se no usa l'utente loggato
        milite = iscrizione.getMilite();
        if (milite == null) {
            milite = wamLogin.getMilite();
        }

        populateModel();
        regolaBottoni();
    }


    /**
     * Riempie il modello con i dati del turno
     */
    private void populateModel() {

        getModel().setMilite(milite.getSigla());

        // data di esecuzione del turno
        String data = dateService.get(turno.getGiorno(), EATime.completa);
        getModel().setGiorno(data);

        // descrizione servizio
        Servizio servizio = turno.getServizio();
        getModel().setServizio(servizio.descrizione);


        // orario nell'header
        if (pref.isBool(MOSTRA_ORARIO_SERVIZIO)) {
            if (servizio.isOrarioDefinito()) {
                String orario = servizioService.getOrarioLungo(servizio);
                getModel().setOrario(orario);
                getModel().setUsaOrarioLabel(true);
                getModel().setUsaOrarioPicker(false);
            } else {
                //                getModel().setInizioExtra(servizio.getInizio().toString());
                //                getModel().setFineExtra(servizio.getFine().toString());
                getModel().setUsaOrarioLabel(false);
                getModel().setUsaOrarioPicker(true);
            }
        }

        // orario di inizio
        String oraInizio = dateService.getOrario(iscrizione.getInizio());
        getModel().setOraInizio(oraInizio);

        // orario di fine
        String oraFine = dateService.getOrario(iscrizione.getFine());
        getModel().setOraFine(oraFine);

        String nomeIcona = "vaadin:" + iscrizione.getFunzione().getIcona().name().toLowerCase();
        getModel().setIcona(nomeIcona);

        getModel().setFunzione(iscrizione.getFunzione().getSigla());


        getModel().setNote(iscrizione.getNote());

        getModel().setReadOnly(readOnly);

    }


    /**
     * Regola i bottoni
     */
    private void regolaBottoni() {

        // bottone Annulla
        bAnnulla.setText("Chiudi");
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            bAnnulla.addClickShortcut(Key.ESCAPE);
        }
        bAnnulla.addClickListener(e -> {
            tabellone.annullaDialogoTurno(dialogo);
        });


        // bottone Conferma
        if (iscrizione.getMilite() != null) {
            bConferma.setText("Registra");
        } else {
            bConferma.setText("Iscriviti");
        }
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            bConferma.addClickShortcut(Key.ENTER);
        }
        bConferma.addClickListener(e -> {
            if (validateInput()) {
                syncIscrizione();
                // nota: non si può registrare solo l'iscrizione perché in Mongo è interna al Turno
                tabellone.confermaDialogoTurno(dialogo, turno);
            }
        });

        // se read only questo bottone non c'è
        if (bConferma.isVisible() && readOnly) {
            bConferma.setVisible(false);
        }

        // bottone Elimina
        if (himself()) {
            bElimina.setText("Cancella iscrizione");
            bElimina.setIcon(new Icon(VaadinIcon.TRASH));
            bElimina.addClickListener(e -> {

                Button bElimina = new Button();
                bElimina.getStyle().set("background-color", "red");
                bElimina.getStyle().set("color", "white");
                bElimina.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                    if (himself()) { // controllo di sicurezza per non affidarsi solo all'invisibilità del pulsante nella GUI
                        resetIscrizione();
                        tabellone.confermaDialogoTurno(dialogo, turno);
                    }
                });

                ConfirmDialog.createWarning().withCaption("Conferma cancellazione").withMessage("Sei sicuro di voler eliminare l'iscrizione?").withAbortButton(ButtonOption.caption("Annulla"), ButtonOption.icon(VaadinIcon.CLOSE)).withButton(bElimina, ButtonOption.caption("Elimina"), ButtonOption.focus(), ButtonOption.icon(VaadinIcon.TRASH)).open();


            });
        } else {
            bElimina.setVisible(false);
        }

        // se read only questo bottone non c'è
        if (bElimina.isVisible() && readOnly) {
            bElimina.setVisible(false);
        }


    }


    private boolean himself() {
        Milite milite = wamLogin.getMilite();

        if (milite != null) {
            return iscrizione.getMilite() != null && wamLogin.getMilite().equals(iscrizione.getMilite());
        } else {
            return false;
        }
    }


    /**
     * Valida il contenuto del form e avvisa se non valido
     */
    private boolean validateInput() {
        boolean valid = true;
        String problem = "";
        LocalTime oraInizio = null;
        LocalTime oraFine = null;
        String sOra;

        if (valid) {
            try {
                sOra = getModel().getOraInizio();
                if (!StringUtils.isEmpty(sOra)) {
                    oraInizio = dateService.getLocalTimeHHMM(sOra);
                }
            } catch (Exception e) {
                problem = "Ora inizio turno non valida";
                valid = false;
            }
        }

        if (valid) {
            try {
                sOra = getModel().getOraFine();
                if (!StringUtils.isEmpty(sOra)) {
                    oraFine = dateService.getLocalTimeHHMM(sOra);
                }
            } catch (Exception e) {
                problem = "Ora fine turno non valida";
                valid = false;
            }
        }

        // no una sola valorizzata e una nulla
        if ((oraInizio != null && oraFine == null) || (oraInizio == null && oraFine != null)) {
            problem = "Ora inizio e ora fine devono avere entrambe un valore";
            valid = false;
        }

        // questo controllo per ora non lo attiviamo perché esistono
        // turni a cavallo della mezzanotte ed è quindi legittimo
        // che l'ora di fine sia anteriore all'ora di inizio.
        //        if (valid){
        //            if (oraInizio!=null && oraFine!=null){
        //                if (oraFine.isBefore(oraInizio)){
        //                    problem="L'ora di fine turno è anteriore all'ora di inizio";
        //                    valid=false;
        //                }
        //            }
        //        }

        if (!valid) {
            notify(problem);
        }

        return valid;
    }


    /**
     * Mostra una notifica generica
     */
    private void notify(String text) {
        Notification notification = new Notification(text);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }


    /**
     * Sincronizza l'oggetto Iscrizione con quanto visualizzato
     */
    private void syncIscrizione() {

        iscrizione.setMilite(milite);

        String sOra;
        LocalTime time;

        sOra = getModel().getOraInizio();

        if (!StringUtils.isEmpty(sOra)) {
            try {
                time = dateService.getLocalTimeHHMM(sOra);
                iscrizione.setInizio(time);
            } catch (Exception e) {
                log.error("can't parse " + sOra + "as LocalTime", e);
            }
        } else {
            iscrizione.setInizio(null);
        }

        sOra = getModel().getOraFine();
        if (!StringUtils.isEmpty(sOra)) {
            try {
                time = dateService.getLocalTimeHHMM(sOra);
                iscrizione.setFine(time);
            } catch (Exception e) {
                log.error("can't parse " + sOra + "as LocalTime", e);
            }
        } else {
            iscrizione.setFine(null);
        }

        iscrizione.setNote(getModel().getNote());
    }


    /**
     * Resetta l'inscrizione alle condizioni di default (vuota)
     */
    private void resetIscrizione() {
        iscrizione.setMilite(null);
        iscrizione.setInizio(turno.getInizio());
        iscrizione.setFine(turno.getFine());
        iscrizione.setNote(null);
    }


}
