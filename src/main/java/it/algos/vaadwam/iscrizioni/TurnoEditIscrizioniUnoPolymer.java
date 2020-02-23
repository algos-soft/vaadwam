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
 * Java wrapper of the polymer element `turno-edit-iscrizioni-una`
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
     * Regola (nella sottoclasse) i componenti iniettati nel polymer html <br>
     * Invocare SEMPRE anche il metodo della superclasse
     */
    @Override
    protected void iniziaIscrizione() {
        super.iniziaIscrizione();
        boolean abilitata = mappaIscrizioniAbilitate.get(turno.iscrizioni.get(0));
        prima.inizia(turno, turno.iscrizioni.get(0), abilitata, bottoniPolymer);
    }// end of method


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
