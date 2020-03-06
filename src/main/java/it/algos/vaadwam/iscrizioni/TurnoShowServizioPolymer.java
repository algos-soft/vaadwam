package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static it.algos.vaadwam.application.WamCost.MOSTRA_ORARIO_SERVIZIO;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 10-ago-2019
 * Time: 16:09
 * <p>
 * Java wrapper of the polymer element `turno-show-servizio`
 * Mostra data e descrizione del servizio in alto.
 * Opzionale anche l'orario del servizio che comunque viene riportato in ogni iscrizione
 */
@Tag("turno-show-servizio")
@HtmlImport("src/views/iscrizioni/turno-show-servizio.html")
public class TurnoShowServizioPolymer extends PolymerTemplate<TurnoShowServizioPolymer.ServizioModel> {


    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ADateService dateService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ServizioService servizioService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;

    //--modello dati interno
    private ServizioModel modello = getModel();


    /**
     * Regola i dati da presentare in base al turno selezionato <br>
     * Il turno arriva come parametro di @Route alla classe TurnoEditIscrizioniPolymer <br>
     * Invocato dal metodo TurnoEditIscrizioniPolymer.setParameter() della sottoclasse <br>
     */
    public void inizia(Turno turno) {
        fixData(turno.getGiorno());
        fixOrario(turno.getServizio());
        fixServizio(turno.getServizio());
    }// end of method


    /**
     * Data completa (estesa) del giorno di esecuzione del turno <br>
     */
    private void fixData(LocalDate giorno) {
        String data = "";

        if (giorno != null) {
            data = dateService.get(giorno, EATime.completa);
            modello.setData(data);
        }// end of if cycle

    }// end of method


    /**
     * Orario (eventuale) del turno <br>
     */
    private void fixOrario(Servizio servizio) {
        String orario = "";

        if (servizio != null) {
            if (pref.isBool(MOSTRA_ORARIO_SERVIZIO)) {
                orario = servizioService.getOrario(servizio);
                modello.setOrario(orario);
            }// end of if cycle
        }// end of if cycle
        ;
        modello.setUsaOrario(servizio.isOrarioDefinito());
        modello.setNotUsaOrario(!servizio.isOrarioDefinito());
        modello.setInizioExtra(LocalTime.MIDNIGHT.toString());
        modello.setFineExtra(LocalTime.MIDNIGHT.toString());
    }// end of method

    private void handleClickInizioExtra() {
    }// end of method
    private void handleClickInizioExtra(Object alfa) {
    }// end of method

    /**
     * Desxcrizione del servizio <br>
     */
    private void fixServizio(Servizio servizio) {
        if (servizio != null) {
            modello.setServizio(servizio.descrizione);
        }// end of if cycle
    }// end of method


    /**
     * Modello dati per collegare questa classe java col polymer
     */
    public interface ServizioModel extends TemplateModel {

        void setData(String data);

        void setOrario(String orario);

        void setServizio(String servizio);

        String getInizioExtra();

        void setInizioExtra(String inizioExtra);

        String getFineExtra();

        void setFineExtra(String fineExtra);

        void setUsaOrario(boolean usaOrario);

        void setNotUsaOrario(boolean notUsaOrario);

    }// end of interface

}// end of class
