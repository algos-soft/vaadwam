package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadwam.application.WamCost.TAG_TURNO_EDIT_UNO;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 10-ago-2019
 * Time: 15:55
 * <p>
 * Java wrapper of the polymer element `turno-edit-iscrizioni-una` <br>
 * <p>
 * Questa classe viene costruita tramite una chiamata del browser effettuata da @Route <br>
 * Invocata da un @EventHandler di TurnoCellPolymer.handleClick() <br>
 * È una sottoclasse di TurnoEditIscrizioniPolymer e serve unicamente per dichiarare il componente @Id("prima") <br>
 * Il componente è legato al polymer <edit-iscrizione id="prima"></edit-iscrizione> <br>
 * Non può quindi essere creato all'interno di un ciclo 'if' <br>
 */
@Route(value = TAG_TURNO_EDIT_UNO)
@Tag("turno-edit-iscrizioni-uno")
@HtmlImport("src/views/iscrizioni/turno-edit-iscrizioni-uno.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class TurnoEditIscrizioniUnoPolymer extends TurnoEditIscrizioniPolymer {


    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("prima")
    public EditIscrizionePolymer prima;


    /**
     * Metodo chiamato da @BeforeEvent alla creazione della view nel metodo setParameter();
     * Nella sottoclasse aggiunge a listaEditIscrizioni il Component specifico iniettato da @Id("xxx") <br>
     * Metodo sovrascritto. Invocare DOPO il metodo della superclasse <br>
     */
    protected void addEditIscrizionePolimer() {
        listaEditIscrizioni.add(0, prima);
        super.addEditIscrizionePolimer();
    }// end of method


//    /**
//     * Regola (nella sottoclasse) i componenti iniettati nel polymer html <br>
//     * Invocare SEMPRE anche il metodo della superclasse
//     */
//    @Override
//    protected void iniziaIscrizione() {
//        listaEditIscrizioni.add(0, prima);
//        prima.inizia(turno, turno.iscrizioni.get(0), bottoniPolymer);
//        proxyEditIscrizioni.add(0, prima);
//        super.iniziaIscrizione();
//        boolean abilitata = listaEditIscrizioniAbilitate.contains(prima) && listaEditIscrizioniAbilitate.get(listaEditIscrizioniAbilitate.indexOf(prima)).abilitata;
//        prima.inizia(abilitata);
//    }// end of method


    /**
     * Evento lanciato dal bottone Conferma della ButtonsBar <br>
     * Recupera i dati di tutte le iscrizioni presenti <br>
     * Controlla che il milite non sia già segnato nel turno <br>
     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
     * Metodo sovrascritto. Invocare DOPO il metodo della superclasse <br>
     */
    protected void conferma() {
        bind(turno, 1, prima);
        super.conferma();
    }// end of method

}// end of class
