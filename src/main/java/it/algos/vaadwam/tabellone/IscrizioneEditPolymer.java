package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

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
public class IscrizioneEditPolymer extends PolymerTemplate<IscrizioneEditModel>  {

    private Iscrizione iscrizione;

    private Turno turno;

    private ITabellone tabellone;

    private Dialog dialogo;

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

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    protected AVaadinService vaadinService;

    private WamLogin wamLogin;


    /**
     * @param tabellone il tabellone di riferimento per effettuare le callbacks
     * @param dialogo il dialogo contenitore
     * @param iscrizione l'iscrizione da mostrare
     */
    public IscrizioneEditPolymer(ITabellone tabellone, Dialog dialogo, Turno turno, Iscrizione iscrizione) {
        this.tabellone=tabellone;
        this.dialogo=dialogo;
        this.iscrizione=iscrizione;
        this.turno=turno;
    }

    @PostConstruct
    private void init(){

        AContext context = vaadinService.getSessionContext();
        wamLogin=(WamLogin)context.getLogin();

        populateModel();
        regolaBottoni();
    }

    /**
     * Riempie il modello con i dati del turno
     */
    private void populateModel() {

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

        // orario fine
        String oraInizio = dateService.getOrario(iscrizione.getInizio());
        getModel().setOraInizio(oraInizio);

        // orario di fine
        String oraFine = dateService.getOrario(iscrizione.getFine());
        getModel().setOraFine(oraFine);

        String nomeIcona = "vaadin:" + iscrizione.getFunzione().getIcona().name().toLowerCase();
        getModel().setIcona(nomeIcona);

        getModel().setFunzione(iscrizione.getFunzione().getSigla());

        Milite milite = wamLogin.getMilite();
        getModel().setMilite(milite.getSigla());



        //@todo buttare
//        TurnoIscrizioneModel iscrizioneModello=new TurnoIscrizioneModel();
//
//        String key = getKeyIscrizione(iscrizione);
//        iscrizioneModello.setKeyTag(key);
//
//        iscrizioneModello.setColore(getColore(iscrizione));
//
//        Funzione funzione = iscrizione.getFunzione();
//        String nomeIcona = "vaadin:" + funzione.icona.name().toLowerCase();
//        iscrizioneModello.setIcona(nomeIcona);
//
//        if (iscrizione.getMilite()!=null){
//            iscrizioneModello.setIdMilite(iscrizione.getMilite().id);
//            iscrizioneModello.setMilite(iscrizione.getMilite().getSigla());
//        }
//
//        iscrizioneModello.setIdFunzione(iscrizione.getFunzione().id);
//        iscrizioneModello.setFunzione(iscrizione.getFunzione().getSigla());
//
//        Servizio servizio = turnoEntity.getServizio();
//        String sTime;
//
//        sTime=getPickerTimeString(iscrizione.getInizio(), servizio.getInizio());
//        iscrizioneModello.setInizio(sTime);
//
//        sTime=getPickerTimeString(iscrizione.getFine(), servizio.getFine());
//        iscrizioneModello.setFine(sTime);
//
//        iscrizioneModello.setNote(iscrizione.getNote());
//
//        iscrizioniModello.add(iscrizioneModello);



    }


    /**
     * Regola i bottoni
     */
    private void regolaBottoni() {

        bAnnulla.setText("Annulla");
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            bAnnulla.addClickShortcut(Key.ESCAPE);
        }
        bAnnulla.addClickListener(e -> {tabellone.annullaDialogoTurno(dialogo);});

        if(iscrizione.getMilite()!=null){
            bConferma.setText("Registra");
        }else{
            bConferma.setText("Iscriviti");
        }
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            bConferma.addClickShortcut(Key.ENTER);
        }
        bConferma.addClickListener(e -> {tabellone.confermaDialogoTurno(dialogo, turno);});

        if(iscrizione.getMilite()!=null){
            bElimina.setText("Cancella iscrizione");
            bElimina.setIcon(new Icon(VaadinIcon.TRASH));
            bElimina.addClickListener(e -> {tabellone.eliminaIscrizione(dialogo, turno, iscrizione);});
        }else{
            bElimina.setVisible(false);
        }

    }



}
