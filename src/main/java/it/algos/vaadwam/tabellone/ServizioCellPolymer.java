package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 05-mar-2019
 * Time: 14:48
 * <p>
 * Colonna di sinistra del tabellone <br>
 * Contiene:
 * 1.Orario
 * 2.Nome della servizio
 * 3.Icona della funzione
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Tag("cell-servizio")
@HtmlImport("src/views/tabellone/servizioCellPolymer.html")
public class ServizioCellPolymer extends PolymerTemplate<ServizioCellModel> {

    @Autowired
    protected ATextService text;

    @Autowired
    protected AArrayService array;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ServizioService servizioService;

    private Servizio servizio;

    private boolean lastInType;


    /**
     *
     */
    public ServizioCellPolymer() {
    }// end of constructor


    /**
     * Creates the hello world template.
     *
     * @param lastInType true if this cell is the last in a sequence of cells of the same type.
     */
    public ServizioCellPolymer(Servizio servizio, boolean lastInType) {
        this.servizio = servizio;
        this.lastInType = lastInType;
    }// end of constructor


    /**
     * Chiamato DOPO aver termionato il costruttore
     * Registra nel context l'indirizzo a questa istanza per modifiche specifiche ai menu
     */
    @PostConstruct
    private void inizia() {
        String colore = "";
        String orario = "";

        if (servizio != null) {
            if (servizioService != null) {
                colore = servizio.colore;
                orario = servizioService.getOrarioBreve(servizio);
            }// end of if cycle
        }// end of if cycle

        if (text.isValid(colore)) {
            setColore(colore);
        }// end of if cycle

        if (text.isValid(orario)) {
            setOrario(orario);
        }// end of if cycle

        if (servizio != null) {
            setServizio(servizio);
        }// end of if cycle

        if (servizioService.getFunzioniAll(servizio).size() > 0) {
            setIcone(servizio);
        }// end of if cycle

        this.setLastInType();
    }// end of method


    public void setServizio(Servizio servizio) {
        getModel().setServizio(servizio);
    }// end of method


    public void setColore(String colore) {
        getModel().setColore(colore);
    }// end of method


    public void setIcone(Servizio servizio) {
        List<String> listaIconeObbligatorie = null;
        List<String> listaIconeFacoltative = null;

        if (servizio != null) {
            if (servizio.obbligatorie != null && servizio.obbligatorie.size() > 0) {
                listaIconeObbligatorie = new ArrayList<>();
                for (Funzione funzione : servizioService.getObbligatorie(servizio)) {
                    if (funzione.icona != null) {
                        listaIconeObbligatorie.add(funzione.icona.name().toLowerCase());
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

            if (servizio.facoltative != null && servizio.facoltative.size() > 0) {
                listaIconeFacoltative = new ArrayList<>();
                for (Funzione funzione : servizioService.getFacoltative(servizio)) {
                    if (funzione.icona != null) {
                        listaIconeFacoltative.add(funzione.icona.name().toLowerCase());
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

            getModel().setIconeObbligatorie(listaIconeObbligatorie);
            getModel().setIconeFacoltative(listaIconeFacoltative);

        }// end of if cycle
    }// end of method


    public void setOrario(String orario) {
        getModel().setOrario(orario);
    }// end of method


    public void setLastInType() {
        getModel().setLastInType(lastInType);
    }// end of method


}// end of class
