package it.algos.vaadwam.tabellone;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoRepository;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_TAB;
import static it.algos.vaadwam.application.WamCost.TAG_TUR;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: dom, 23-set-2018
 * Time: 10:22
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_TAB)
@Slf4j
@AIScript(sovrascrivibile = false)
public class TabelloneService extends AService {

    public static final String OGGI = "Oggi";

    public static final String LUNEDI = "Da lunedì";

    public static final String PRECEDENTE = "Precedente";

    public static final String SUCCESSIVO = "Successivo";

    public static final String SELEZIONE = "Selezione";

    public static final String SEP = " / ";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected RigaService rigaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
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
    protected TurnoService turnoService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected IscrizioneService iscrizioneService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected CroceService croceService;

    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    public TabelloneService(@Qualifier(TAG_TUR) MongoRepository repository) {
        super(repository);
        super.entityClass = Turno.class;
        this.repository = (TurnoRepository) repository;
    }// end of Spring constructor


    /**
     * Costruisce una lista di giorni da visualizzare nella grid <br>
     *
     * @param giornoIniziale     del tabellonesuperato
     * @param giorniVisualizzati nel tabellonesuperato
     */
    public List<LocalDate> getGriDaysList(LocalDate giornoIniziale, int giorniVisualizzati) {
        List<LocalDate> gridDaysList = null;

        if (giornoIniziale != null && giorniVisualizzati > 0) {
            gridDaysList = new ArrayList<>();
            for (int k = 0; k < giorniVisualizzati; k++) {
                gridDaysList.add(giornoIniziale.plusDays(k));
            }// end of for cycle
        }// end of if cycle

        return gridDaysList;
    }// end of method


    /**
     * Costruisce una lista di righe da visualizzare nella grid. Una per ogni servizio visibile <br>
     *
     * @param giornoIniziale     del tabellonesuperato
     * @param giorniVisualizzati nel tabellonesuperato
     */
    public List<Riga> getGridRigheList(LocalDate giornoIniziale, int giorniVisualizzati) {
        List<Riga> gridRigheList = null;
        Riga riga;
        LocalDate giornoFinale = giornoIniziale.plusDays(giorniVisualizzati - 1);
        List<Servizio> servizi = servizioService.findAllVisibili();
        List<Turno> turni;

        gridRigheList = new ArrayList<>();

        for (Servizio servizio : servizi) {
            turni = turnoService.findByServizio(servizio, giornoIniziale, giornoFinale);
            riga = rigaService.newEntity(giornoIniziale, servizio, turni);
            gridRigheList.add(riga);
        }// end of for cycle

        return gridRigheList;
    }// end of method


//    /**
//     * Costruisce una lista di selezione dei giorni da visualizzare
//     *
//     * @param giornoIniziale del tabellonesuperato
//     */
//    public List<String> getPeriodi(LocalDate giornoIniziale) {
//        List<String> lista = new ArrayList<>();
//
//        lista.add(getPresente(giornoIniziale));
//        lista.add(getIndietro(giornoIniziale));
//        lista.add(getAvanti(giornoIniziale));
//        lista.add(OGGI);
//        lista.add(LUNEDI);
//        lista.add(PRECEDENTE);
//        lista.add(SUCCESSIVO);
//        lista.add(SELEZIONE);
//
//        return lista;
//    }// end of method


//    /**
//     * Costruisce la stringa del periodo attuale
//     *
//     * @param giornoIniziale del tabellonesuperato
//     */
//    public String getPresente(LocalDate giornoIniziale) {
//        String label = "";
//
//        if (giornoIniziale != null) {
//            label = date.get(giornoIniziale, EATime.meseCorrente);
//        }// end of if cycle
//
//        return label;
//    }// end of method


//    /**
//     * Costruisce la stringa del periodo precedente
//     *
//     * @param giornoIniziale del tabellonesuperato
//     */
//    public String getIndietro(LocalDate giornoIniziale) {
//        String label = "";
//        LocalDate inizio = giornoIniziale.minusDays(GridTabellone.GIORNI_STANDARD);
//        LocalDate fine = giornoIniziale.minusDays(1);
//
//        if (giornoIniziale != null) {
//            label = date.get(inizio, EATime.meseLong) + SEP + date.get(fine, EATime.meseLong);
//        }// end of if cycle
//
//        return label;
//    }// end of method


//    /**
//     * Costruisce la stringa del periodo successivo
//     *
//     * @param giornoIniziale del tabellonesuperato
//     */
//    public String getAvanti(LocalDate giornoIniziale) {
//        String label = "";
//        LocalDate inizio = giornoIniziale.plusDays(GridTabellone.GIORNI_STANDARD);
//        LocalDate fine = giornoIniziale.plusDays(GridTabellone.GIORNI_STANDARD + GridTabellone.GIORNI_STANDARD - 1);
//
//        if (giornoIniziale != null) {
//            label = date.get(inizio, EATime.meseLong) + SEP + date.get(fine, EATime.meseLong);
//        }// end of if cycle
//
//        return label;
//    }// end of method


    /**
     * Colore della iscrizione in funzione della data corrente <br>
     * I periodi di colore cambiano da Croce a Croce <br>
     */
    public EAWamColore getColoreIscrizione(Turno turno,Iscrizione iscrizione) {
        EAWamColore colore = EAWamColore.creabile;
        int critico = 2; //@todo valori diversi per ogni croce. Leggere da preferenze
        int semicritico = 4;//@todo valori diversi per ogni croce. Leggere da preferenze

        if (iscrizione == null) {
            return colore;
        }// end of if/else cycle

        if (iscrizioneService.isValida(iscrizione)) {
            colore = EAWamColore.normale;
        } else {
            if (isPiuRecente(turno, critico)) {
                colore = EAWamColore.critico;
            } else {
                if (isPiuRecente(turno, semicritico)) {
                    colore = EAWamColore.urgente;
                } else {
                    colore = EAWamColore.previsto;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        if (isStorico(turno)) {
            colore = EAWamColore.storico;
        }// end of if cycle

        return colore;
    }// end of method


    /**
     * Colore del turno in funzione della data corrente <br>
     * I periodi di colore cambiano da Croce a Croce <br>
     */
    public EAWamColore getColoreTurno(Turno turno) {
        EAWamColore colore = EAWamColore.creabile;
        int critico = 2; //@todo valori diversi per ogni croce. Leggere da preferenze
        int semicritico = 4;//@todo valori diversi per ogni croce. Leggere da preferenze

        if (turno == null) {
            return colore;
        }// end of if/else cycle


        if (turnoService.isValido(turno)) {
            colore = EAWamColore.normale;
        } else {
            if (isPiuRecente(turno, critico)) {
                colore = EAWamColore.critico;
            } else {
                if (isPiuRecente(turno, semicritico)) {
                    colore = EAWamColore.urgente;
                } else {
                    colore = EAWamColore.previsto;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        if (isStorico(turno)) {
            colore = EAWamColore.storico;
        }// end of if cycle

        return colore;
    }// end of method



    /**
     * Turno previsto prima dei giorni indicati
     */
    public boolean isPiuRecente(Turno turno, int giorni) {
        boolean status = false;
        LocalDate limite = LocalDate.now().plusDays(giorni);

        if (turno.giorno.isBefore(limite)) {
            status = true;
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Turno già effettuato
     */
    public boolean isStorico(Turno turno) {
        return isPiuRecente(turno, 0);
    }// end of method

}// end of class
