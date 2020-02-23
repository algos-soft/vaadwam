package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadwam.application.WamCost.TAG_TURNO_EDIT_TRE;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 10-ago-2019
 * Time: 15:55
 * <p>
 * Java wrapper of the polymer element `turno-edit-iscrizioni-tre`
 */
@Route(value = TAG_TURNO_EDIT_TRE)
@Tag("turno-edit-iscrizioni-tre")
@HtmlImport("src/views/iscrizioni/turno-edit-iscrizioni-tre.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class TurnoEditIscrizioniTrePolymer extends TurnoEditIscrizioniDuePolymer {


    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("terza")
    public EditIscrizionePolymer terza;


    /**
     * Regola (nella sottoclasse) i componenti iniettati nel polymer html <br>
     * Invocare SEMPRE anche il metodo della superclasse
     */
    @Override
    protected void iniziaIscrizione() {
        super.iniziaIscrizione();
        boolean abilitata = mappaIscrizioniAbilitate.get(turno.iscrizioni.get(2));
        terza.inizia(turno, turno.iscrizioni.get(2), abilitata, bottoniPolymer);
    }// end of method


    /**
     * Evento lanciato dal bottone Conferma della ButtonsBar <br>
     * Recupera i dati di tutte le iscrizioni presenti <br>
     * Controlla che il milite non sia già segnato nel turno <br>
     * Controlla che il milite non sia già segnato in un altro turno della stessa giornata <br>
     * Metodo sovrascritto. Invocare DOPO il metodo della superclasse <br>
     */
    @Override
    protected void conferma() {
        bind(turno, 3, terza);
        super.conferma();
    }// end of method

}// end of class
