package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

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

    @Autowired
    private ADateService dateService;

    @Autowired
    private PreferenzaService pref;

    @Autowired
    private ServizioService servizioService;

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
        populateModel();
        regolaBottoni();
    }

    /**
     * Riempie il modello con i dati del turno
     */
    private void populateModel() {

        // data di esecuzione del turno nell'header
        String data = dateService.get(turno.getGiorno(), EATime.completa);
        getModel().setGiorno(data);

        // descrizione servizio nell'header
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


        //@todo da fare qui
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
     * Regola i bottoni Conferma e Annulla
     */
    private void regolaBottoni() {

//        //--Regolazioni standard di default del bottone 'Annulla'
//        fixAnnulla();
//
//        //--Regolazioni standard di default del bottone 'Conferma'
//        fixConferma();

    }



}
