package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadwam.application.WamCost.TAG_TURNO_EDIT_CINQUE;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: lun, 02-mar-2020
 * Time: 08:19
 * <p>
 * Java wrapper of the polymer element `turno-edit-iscrizioni-cinque` <br>
 * <p>
 * Questa classe viene costruita tramite una chiamata del browser effettuata da @Route <br>
 * Invocata da un @EventHandler di TurnoCellPolymer.handleClick() <br>
 * È una sottoclasse di TurnoEditIscrizioniPolymer e serve unicamente per dichiarare il componente @Id("quinta") <br>
 * Il componente è legato al polymer <edit-iscrizione id="quinta"></edit-iscrizione> <br>
 * Non può quindi essere creato all'interno di un ciclo 'if' <br>
 */
@Route(value = TAG_TURNO_EDIT_CINQUE)
@Tag("turno-edit-iscrizioni-cinque")
@HtmlImport("src/views/iscrizioni/turno-edit-iscrizioni-cinque.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class TurnoEditIscrizioniCinquePolymer extends TurnoEditIscrizioniQuattroPolymer {

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("quinta")
    public EditIscrizionePolymer quinta;

    /**
     * Metodo chiamato da @BeforeEvent alla creazione della view nel metodo setParameter();
     * Nella sottoclasse aggiunge a listaEditIscrizioni il Component specifico iniettato da @Id("xxx") <br>
     * Metodo sovrascritto. Invocare DOPO il metodo della superclasse <br>
     */
    protected void addEditIscrizionePolimer() {
        listaEditIscrizioni.add(0, quinta);
        super.addEditIscrizionePolimer();
    }// end of method

}// end of class
