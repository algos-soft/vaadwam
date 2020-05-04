package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Componente Editor di una singola iscrizione
 */
@Tag("iscrizione-editor")
@HtmlImport("src/views/tabellone/iscrizione-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class IscrizioneEditPolymer extends PolymerTemplate<IscrizioneEditModel>  {

    private Turno turno;

    private ITabellone tabellone;

    private Dialog dialogo;

    /**
     * @param tabellone il tabellone di riferimento per effettuare le callbacks
     * @param dialogo il dialogo contenitore
     * @param turno il turno da mostrare
     */
    public IscrizioneEditPolymer(ITabellone tabellone, Dialog dialogo, Turno turno) {
        this.tabellone=tabellone;
        this.dialogo=dialogo;
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

//        // data di esecuzione del turno
//        String data = dateService.get(turnoEntity.getGiorno(), EATime.completa);
//        getModel().setGiorno(data);
//
//        //--Descrizione estesa del servizio
//        Servizio servizio = turnoEntity.getServizio();
//        getModel().setServizio(servizio.descrizione);
//
//        //--Orario (eventuale) del turno
//        fixOrario();
//
//        //--Regolazione delle iscrizioni
//        fixIscrizioni();

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
