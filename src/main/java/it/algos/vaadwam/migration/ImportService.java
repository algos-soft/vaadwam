package it.algos.vaadwam.migration;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.enumeration.EALogLivello;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.log.Log;
import it.algos.vaadflow.modules.logtype.LogtypeService;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.service.*;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.croce.EACroce;
import it.algos.vaadwam.modules.croce.EAOrganizzazione;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.log.WamLogService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.statistica.StatisticaService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;


/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 10-mag-2018
 * Time: 19:52
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ImportService extends AService {

    private final static String PERSISTENCE_UNIT_NAME = "Webambulanzelocal";

    public static LocalDate GIORNO_INIZIALE_DEBUG = LocalDate.of(2018, 12, 20);

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    protected WamService wamService;

    @Autowired
    private ATextService text;

    @Autowired
    private CroceAmb croceAmb;

    @Autowired
    private FunzioneAmb funzioneAmb;

    @Autowired
    private ServizioAmb servizioAmb;

    @Autowired
    private MiliteAmb militeAmb;

    @Autowired
    private UtenteAmb utenteAmb;

    @Autowired
    private TurnoAmb turnoAmb;

    @Autowired
    private UtenteRuoloAmb utenteRuoloAmb;

    @Autowired
    private MiliteFunzioneAmb militeFunzioneAmb;

    @Autowired
    private RuoloAmb ruoloAmb;

    @Autowired
    private CroceService croceService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private PersonService personService;

    @Autowired
    private AddressService indirizzoService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private MiliteService militeService;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private StatisticaService statisticaService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PreferenzaService pref;

    @Autowired
    private ADateService date;

    @Autowired
    private WamLogService logger;

    @Autowired
    private LogtypeService logtype;

    @Autowired
    private AMongoService mongo;

    private List<CroceAmb> crociOld;


    /**
     * Tutte le vecchie croci (valide)
     */
    private void setup() {
        if (array.isEmpty(crociOld)) {
            crociOld = new ArrayList<>();
            List<CroceAmb> crociAmb = croceAmb.findAll();
            String sigla;
            List<String> listaValide = EACroce.getValues();

            for (CroceAmb croceOld : crociAmb) {
                sigla = croceOld.getSigla().toLowerCase();
                if (listaValide.contains(sigla)) {
                    crociOld.add(croceOld);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle
    }// end of constructor


    //    /**
    //     * Importa tutte le companies esistenti in webAmbulanze, in fase iniziale di setup
    //     */
    //    public void importAllSetup() {
    //        setup();
    //        if (croceService.count() == 0) {
    //            importAll();
    //        }// end of if cycle
    //    }// end of method


    /**
     * Importa funzioni, servizi, militi e turni di tutte le croci <br>
     * Controlla il flag di attivazione specifico di ogni croce <br>
     */
    public void importAll() {
        Croce croceNew;

        setup();

        for (CroceAmb croceOld : crociOld) {
            croceNew = getCroce(croceOld);

            if (croceNew != null) {
                if (pref.isBool(USA_DAEMON_IMPORT, croceNew.code)) {
                    importCroce(croceNew);
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle
    }// end of method


    /**
     * Importa funzioni, servizi, militi e turni della croce <br>
     */
    public void importCroce(Croce croceNew) {
        long inizio = System.currentTimeMillis();

        if (croceNew != null) {
            this.importFunzioni(croceNew);
            this.importServizi(croceNew);
            this.importMiliti(croceNew);
            this.importTurni(croceNew);

            logger.importOld("Import della croce " + croceNew.code);
            if (pref.isBool(EAPreferenzaWam.usaMailImport)) {
                mail.sendIP("Import della croce " + croceNew.code, inizio);
            }// end of if cycle
        }
    }// end of method


    /**
     * Importa tutte le companies esistenti in webAmbulanze
     */
    public boolean importOnlyCroci() {
        boolean status = true;
        setup();

        for (CroceAmb croceOld : crociOld) {
            status = importCroce(croceOld) && status;
        }// end of for cycle

        //--Registra nelle preferenze la data dell'ultimo download effettuato
        if (status) {
            pref.saveValue(LAST_IMPORT_CROCI, LocalDateTime.now());
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Importa da webAmbulanze le funzioni di tutte le croci esistenti <br>
     */
    @Deprecated
    public boolean importFunzioni() {
        boolean status = true;
        setup();

        for (CroceAmb croceOld : crociOld) {
            if (!importFunzioni(croceOld)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        if (status) {
            pref.saveValue(LAST_IMPORT_FUNZIONI, LocalDateTime.now());
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Importa da webAmbulanze le funzioni di una sola croce <br>
     *
     * @param croceOld esistente su webAmbulanze
     */
    @Deprecated
    private boolean importFunzioni(CroceAmb croceOld) {
        return importFunzioni(croceOld, getCroce(croceOld));
    }// end of method


    /**
     * Importa da webAmbulanze le funzioni di una sola croce <br>
     *
     * @param croceNew di waadWam
     */
    public boolean importFunzioni(Croce croceNew) {
        if (croceNew != null) {
            return importFunzioni(getCroce(croceNew), croceNew);
        } else {
            return false;
        }// end of if/else cycle
    }// end of method


    /**
     * Importa da webambulanze le funzioni di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     * @param croceNew di waadwam
     */
    private boolean importFunzioni(CroceAmb croceOld, Croce croceNew) {
        boolean status = true;
        if (croceOld == null && croceNew != null) {
            System.out.println("Non posso importare le funzioni di: " + croceNew.getCode() + " perché manca la vecchia croce");
            return false;
        }// end of if cycle

        funzioneService.deleteAllCroce(croceNew);
        List<FunzioneAmb> listaFunzioniOld = funzioneAmb.findAll((int) croceOld.getId());

        try { // prova ad eseguire il codice
            if (listaFunzioniOld != null && listaFunzioniOld.size() > 0) {
                for (FunzioneAmb funzioneOld : listaFunzioniOld) {
                    if (!creaSingolaFunzione(funzioneOld, croceNew)) {
                        status = false;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

            //--le funzioni dipendenti si possono inserire solo dopo aver creato tutte le funzioni di una croce
            this.recuperaFunzioniDipendenti(listaFunzioniOld, croceNew);

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            System.out.println("Siamo in importFunzioni di: " + getCroce(croceNew).getSigla() + " con funzione");
            status = false;
        }// fine del blocco try-catch

        return status;
    }// end of method


    /**
     * Importa da webambulanze i servizi di tutte le croci esistenti <br>
     */
    public boolean importServizi() {
        boolean status = true;
        setup();

        for (CroceAmb croceOld : crociOld) {
            if (!importServizi(croceOld)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        if (status) {
            pref.saveValue(LAST_IMPORT_SERVIZI, LocalDateTime.now());
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Importa da webambulanze i servizi di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     */
    private boolean importServizi(CroceAmb croceOld) {
        return importServizi(croceOld, getCroce(croceOld));
    }// end of method


    /**
     * Importa da webambulanze i servizi di una sola croce <br>
     *
     * @param croceNew di waadwam
     */
    public boolean importServizi(Croce croceNew) {
        return importServizi(getCroce(croceNew), croceNew);
    }// end of method


    /**
     * Importa da webambulanze i servizi di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     * @param croceNew di waadwam
     */
    private boolean importServizi(CroceAmb croceOld, Croce croceNew) {
        boolean status = true;
        if (croceOld == null && croceNew != null) {
            System.out.println("Non posso importare i servizi di: " + getCroce(croceNew).getSigla() + " perché manca la vecchia croce");
            return false;
        }// end of if cycle

        servizioService.deleteAllCroce(croceNew);
        List<ServizioAmb> listaServiziOld = servizioAmb.findAll((int) croceOld.getId());

        try { // prova ad eseguire il codice
            if (listaServiziOld != null && listaServiziOld.size() > 0) {
                for (ServizioAmb servizioOld : listaServiziOld) {
                    if (!creaSingoloServizio(servizioOld, croceNew)) {
                        log.error("Pippoz");
                        System.out.println("Siamo in importServizi di: " + getCroce(croceNew).getSigla() + " e non trovo il servizio " + servizioOld.getSigla());
                        status = false;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            System.out.println("Siamo in importServizi di: " + getCroce(croceNew).getSigla());
            status = false;
        }// fine del blocco try-catch

        try { // prova ad eseguire il codice
            //--il colore per il raggruppamento si può aggiungere solo dopo aver creato tutte i servizi di una croce
            this.creaColoreGruppoServizi(croceNew);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            System.out.println("Siamo in importServizi() e creaColoreGruppoServizi()");
            status = false;
        }// fine del blocco try-catch

        return status;
    }// end of method


    /**
     * Importa da webambulanze i militi di tutte le croci esistenti <br>
     */
    public boolean importMiliti() {
        boolean status = true;
        setup();

        for (CroceAmb croceOld : crociOld) {
            if (!importMiliti(croceOld)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        if (status) {
            pref.saveValue(LAST_IMPORT_MILITI, LocalDateTime.now());
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Importa da webambulanze i militi di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     */
    private boolean importMiliti(CroceAmb croceOld) {
        return importMiliti(croceOld, getCroce(croceOld));
    }// end of method


    /**
     * Importa da webambulanze i militi di una sola croce <br>
     *
     * @param croceNew di waadwam
     */
    public boolean importMiliti(Croce croceNew) {
        return importMiliti(getCroce(croceNew), croceNew);
    }// end of method


    /**
     * Importa da webambulanze i militi di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     * @param croceNew di waadwam
     */
    private boolean importMiliti(CroceAmb croceOld, Croce croceNew) {
        boolean status = true;
        int numRec = 0;
        if (croceOld == null && croceNew != null) {
            System.out.println("Non posso importare i militi di: " + getCroce(croceNew).getSigla() + " perché manca la vecchia croce");
            return false;
        }// end of if cycle

        militeService.deleteAllCroce(croceNew);
        List<MiliteAmb> listaMilitiOld = militeAmb.findAll((int) croceOld.getId());
        List<UtenteAmb> listaUtentiOld = utenteAmb.findAll((int) croceOld.getId());
        List<UtenteRuoloAmb> listaUtentiRuoloOld = utenteRuoloAmb.findAll();
        List<RuoloAmb> listaRuoliOld = ruoloAmb.findAll();
        UtenteAmb utenteOld = null;
        RuoloAmb ruoloOld = null;
        ArrayList<UtenteAmb> listaUtentiOldAssenti = new ArrayList<>();
        ArrayList<MiliteAmb> listaMilitiOldAssenti = new ArrayList<>();
        long inizio = System.currentTimeMillis();
        long inizioPar;
        long fine;

        if (listaMilitiOld != null && listaMilitiOld.size() > 0) {
            try { // prova ad eseguire il codice
                for (MiliteAmb militeOld : listaMilitiOld) {
                    utenteOld = getUtenteAmb(listaUtentiOld, militeOld);
                    if (utenteOld != null) {
                        ruoloOld = getRuoloOld(listaUtentiRuoloOld, listaRuoliOld, utenteOld);
                        if (ruoloOld != null) {
                            inizioPar = System.currentTimeMillis();
                            if (!creaSingoloMilite(militeOld, utenteOld, ruoloOld, croceNew)) {
                                status = false;
                            }// end of if cycle
                            numRec++;
                            fine = System.currentTimeMillis();
                            //                            log.info(text.primaMaiuscola(croceNew.getCode()) + " - milite numero " + numRec + " (su " + listaUtentiOld.size() + " totali) importato in " + (fine - inizioPar) + " millisecondi");
                            //                            log.info("Singolo milite importato in " + date.deltaText(inizioPar));
                            //                            log.info("In totale " + numRec + " militi su " + listaMilitiOld.size() + "importati in " + date.deltaText(inizio));
                        } else {
                            listaUtentiOldAssenti.add(utenteOld);
                            listaMilitiOldAssenti.add(militeOld);
                        }// end of if/else cycle
                    } else {
                        if (!creaSingoloMilite(militeOld, null, null, croceNew)) {
                            status = false;
                        }// end of if cycle
                        System.out.println("Manca in " + getCroce(croceNew).getSigla() + " militeOld: " + militeOld.getCognome());
                        //@todo rimettere
                        //                        logger.debug(logtype.getImport(), "Manca utente per il milite " + militeOld.getNome() + " " + militeOld.getCognome());
                    }// end of if/else cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                System.out.println("Siamo in importMiliti di: " + getCroce(croceNew).getSigla());
            }// fine del blocco try-catch
        }// end of if cycle

        if (listaUtentiOldAssenti.size() > 0) {
            for (int k = 0; k < listaUtentiOldAssenti.size(); k++) {
                System.out.println("Manca in " + getCroce(croceNew).getSigla() + " utenteOld: " + listaUtentiOldAssenti.get(k).getNickname() + ", cioe militeOld: " + listaMilitiOldAssenti.get(k).getCognome());
            }// end of for cycle
        }// end of if cycle

        return status;
    }// end of method


    //    /**
    //     * Importa da webambulanze i turni di tutte le croci esistenti <br>
    //     */
    //    public boolean importTurni() {
    //        boolean status = true;
    //        setup();
    //
    //        for (CroceAmb croceOld : crociOld) {
    //            if (!importTurni(croceOld)) {
    //                status = false;
    //            }// end of if cycle
    //        }// end of for cycle
    //
    //        if (status) {
    //            pref.saveValue(LAST_IMPORT_TURNI, LocalDateTime.now());
    //        }// end of if cycle
    //
    //        return status;
    //    }// end of method


    /**
     * Importa da webambulanze i turni di tutte le croci esistenti <br>
     */
    public boolean importTurniAnno() {
        boolean status = true;
        setup();

        for (CroceAmb croceOld : crociOld) {
            if (!importTurni(croceOld)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        if (status) {
            pref.saveValue(LAST_IMPORT_TURNI, LocalDateTime.now());
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Importa da webambulanze i turni di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     */
    private boolean importTurni(CroceAmb croceOld) {
        return importTurni(croceOld, getCroce(croceOld));
    }// end of method


    /**
     * Importa da webambulanze i turni di una sola croce <br>
     *
     * @param croceNew di waadwam
     */
    public boolean importTurni(Croce croceNew) {
        return importTurniAnno(croceNew, date.getAnnoCorrente());
    }// end of method


    /**
     * Importa da webambulanze i turni di una sola croce <br>
     *
     * @param croceNew di waadwam
     */
    public boolean importTurniStorico(Croce croceNew) {

        for (Integer anno : ANNI) {
            importTurniAnno(croceNew, anno);
        }// end of for cycle

        return false;
    }// end of method


    /**
     * Importa da webambulanze tutti i turni di una sola croce <br>
     *
     * @param croceOld esistente su webambulanze
     * @param croceNew di waadwam
     */
    public boolean importTurni(CroceAmb croceOld, Croce croceNew) {
        boolean status = true;
        List<TurnoAmb> turniOld = null;

        turnoService.deleteAllCroce(croceNew);
        turniOld = turnoAmb.findAll((int) croceOld.getId());
        for (TurnoAmb turnoOld : turniOld) {
            status = status && creaSingoloTurno(croceNew, turnoOld);
        }// end of for cycle

        return status;
    }// end of method


    /**
     * Importa da webambulanze i turni di una sola croce per l'anno in corso <br>
     *
     * @param croceNew di waadwam
     * @param anno     da importare
     */
    public boolean importTurniAnno(Croce croceNew, int anno) {
        boolean status = true;
        CroceAmb croceOld = getCroce(croceNew);
        List<TurnoAmb> turniOld = turnoAmb.findAll((int) croceOld.getId(), anno);

        turnoService.deleteAllCroceAnno(croceNew, anno);
        for (TurnoAmb turnoOld : turniOld) {
            status = status && creaSingoloTurno(croceNew, turnoOld);
        }// end of for cycle

        return status;
    }// end of method


    /**
     * Importa da webambulanze i turni per l'anno in corso da tutte le croci <br>
     */
    public boolean importTurni() {
        boolean status = true;
        int anno = date.getAnnoCorrente();
        List<TurnoAmb> turniOld;
        Croce croceNew;

        setup();
        for (CroceAmb croceOld : crociOld) {
            croceNew = getCroce(croceOld);
            turniOld = turnoAmb.findAll((int) croceOld.getId(), anno);

            if (array.isValid(turniOld)) {
                turnoService.deleteAllCroce(croceNew);
                for (TurnoAmb turnoOld : turniOld) {
                    status = status && creaSingoloTurno(croceNew, turnoOld);
                }// end of for cycle
            }// end of if cycle
        }// end of for cycle

        if (status) {
            pref.saveValue(LAST_IMPORT_TURNI, LocalDateTime.now());
        }// end of if cycle

        return status;
    }// end of method

    //    /**
    //     * Importa da webambulanze i turni per l'anno in corso <br>
    //     */
    //    public boolean importTurni(Croce croceNew) {
    //        return false;
    //    }// end of method
    //
    //    /**
    //     * Importa da webambulanze i turni per l'anno in corso <br>
    //     */
    //    public boolean importTurni(CroceAmb croceOld) {
    //        return false;
    //    }// end of method


    /**
     * Importa da webambulanze i turni di una sola croce per un breve periodo <br>
     *
     * @param croceNew di waadwam
     */
    public boolean importTurniDebug(Croce croceNew) {
        boolean status = true;
        CroceAmb croceOld = getCroce(croceNew);
        List<TurnoAmb> turniOld = null;
        LocalDate giorno = LocalDate.now();
        int prima = 15;
        int dopo = 15;

        turniOld = turnoAmb.findAll((int) croceOld.getId(), giorno.minusDays(prima), giorno.plusDays(dopo));
        for (TurnoAmb turnoOld : turniOld) {
            status = status && creaSingoloTurno(croceNew, turnoOld);
        }// end of for cycle

        return status;
    }// end of method


    //    /**
    //     * Elabora le statistiche dei militi <br>
    //     *
    //     * @param croceNew di waadwam
    //     */
    //    public boolean elaboraStatistiche(Croce croceNew) {
    //        return statisticaService.elabora(croceNew);
    //    }// end of method

    //    /**
    //     * Importa da webambulanze i turni di una sola croce per un breve periodo <br>
    //     *
    //     * @param croceOld esistente su webambulanze
    //     */
    //    public boolean importTurniDebug(CroceAmb croceOld) {
    //        boolean status = true;
    //        Croce croceNew = getCroce(croceOld);
    //        List<TurnoAmb> turniOld = null;
    //        LocalDate giorno = LocalDate.now();
    //        int prima = 30;
    //        int dopo = 30;
    //
    //        turniOld = turnoAmb.findAll((int) croceOld.getId(), giorno.minusDays(prima), giorno.plusDays(dopo));
    //        for (TurnoAmb turnoOld : turniOld) {
    //            status = status && creaSingoloTurno(croceNew, turnoOld);
    //        }// end of for cycle
    //
    //        return status;
    //    }// end of method


    /**
     * Importa i dati di una singola company
     * <p>
     * Crea i manager specifici
     * Cerca una croce siglaCroce
     * La cancella, con tutti i dati
     * Crea una croce siglaCroce
     * Importa i dati
     * Chiude i manager specifici
     *
     * @param croceOld usata in webambulanze
     */
    public boolean importCroce(CroceAmb croceOld) {
        boolean importati = false;
        Croce croceNew;
        String codeCroceNew = getCodeNew(croceOld);
        String descrizione;
        String email;
        String telefono;
        Address indNew;
        Person presNew;
        Person contNew;
        EAOrganizzazione orgNew;
        String note = VUOTA;

        try { // prova ad eseguire il codice
            orgNew = EAOrganizzazione.get(croceOld.getOrganizzazione());
            presNew = fixPersona(croceOld.getPresidente());
            descrizione = croceOld.getDescrizione();
            contNew = fixPersona(croceOld.getRiferimento());
            telefono = croceOld.getTelefono();
            email = croceOld.getEmail();
            indNew = fixIndirizzo(croceOld.getIndirizzo());
            note = croceOld.getNote();

            croceService.creaIfNotExist(orgNew, presNew, codeCroceNew, descrizione, contNew, telefono, email, indNew);
            croceNew = croceService.findByKeyUnica(codeCroceNew);
            if (croceNew != null && text.isValid(note)) {
                croceNew.note = note.equals("") ? null : note;
                croceService.save(croceNew);
            }// end of if cycle

            importati = true;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return importati;
    }// end of method


    /**
     * Stabilisce la sigla identificativa della nuova croce
     * Lo stesso algoritmo per creare e ritrovare la nuova croce
     * Si può forzare la sigla tutta maiuscola o tutta minuscola, od altro
     */
    public String getCodeNew(CroceAmb croceOld) {
        String siglaNew = "";
        String siglaOld = croceOld.getSigla();

        if (text.isValid(siglaOld)) {
            siglaNew = siglaOld.toLowerCase();
        }// end of if cycle

        return siglaNew;
    }// end of method


    /**
     * Recupera la nuova croce dalla vecchia
     */
    private Croce getCroce(CroceAmb croceOld) {
        Croce croceNew = null;
        String siglaNew = croceOld.getSigla().toLowerCase();

        if (text.isValid(siglaNew)) {
            croceNew = croceService.findByKeyUnica(siglaNew);
        }// end of if cycle

        return croceNew;
    }// end of method


    /**
     * Recupera la vecchia croce dalla nuova
     */
    private CroceAmb getCroce(Croce croceNew) {
        CroceAmb croceOld = null;

        if (array.isEmpty(crociOld)) {
            setup();
        }// end of if cycle

        for (CroceAmb croceTmp : crociOld) {
            if (croceTmp.getSigla().toLowerCase().equals(croceNew.code.toLowerCase())) {
                croceOld = croceTmp;
            }// end of if cycle
        }// end of for cycle

        return croceOld;
    }// end of method


    /**
     * Crea la singola funzione
     * Presuppone che le funzioni dipendenti in webambulanze siano separate da virgola
     *
     * @param funzioneOld della companyOld
     */
    private boolean creaSingolaFunzione(FunzioneAmb funzioneOld, Croce croceNew) {
        String code = funzioneOld.getSigla();
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine(); //--non utilizzato
        String descrizione = funzioneOld.getDescrizione();
        VaadinIcon icona = selezionaIcona(descrizione);

        try { // prova ad eseguire il codice
            return funzioneService.creaIfNotExist(croceNew, code, sigla, descrizione, icona);
            // end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            return false;
        }// fine del blocco try-catch
    }// end of method


    /**
     * Elabora la vecchia descrizione per selezionare una icona adeguata
     *
     * @param descrizione usata in webambulanze
     *
     * @return la FontAwesome selezionata
     */
    private VaadinIcon selezionaIcona(String descrizione) {
        VaadinIcon icona = null;
        String autista = "utista";
        String medica = "edica";
        String soc = "Primo";
        String soc2 = "Soccorritore";
        String cen = "Centralino";
        String pul = "Pulizie";
        String uff = "Ufficio";

        if (descrizione.contains(autista)) {
            if (descrizione.contains(medica)) {
                icona = VaadinIcon.AMBULANCE;
            } else {
                icona = VaadinIcon.TRUCK;
            }// end of if/else cycle
        } else {
            if (descrizione.contains(soc) || descrizione.contains(soc2)) {
                if (descrizione.contains(soc)) {
                    icona = VaadinIcon.DOCTOR;
                }// end of if cycle
                if (descrizione.contains(soc2)) {
                    icona = VaadinIcon.SPECIALIST;
                }// end of if cycle
            } else {
                if (descrizione.contains(cen) || descrizione.contains(pul) || descrizione.contains(uff)) {
                    if (descrizione.contains(cen)) {
                        icona = VaadinIcon.PHONE;
                    }// end of if cycle
                    if (descrizione.contains(pul)) {
                        icona = VaadinIcon.BED;
                    }// end of if cycle
                    if (descrizione.contains(uff)) {
                        icona = VaadinIcon.OFFICE;
                    }// end of if cycle
                } else {
                    icona = VaadinIcon.USER;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return icona;
    }// end of method


    /**
     * Recupera le funzioni dipendenti solo DOPO aver costruito tutte le funzioni della croce
     */
    private void recuperaFunzioniDipendenti(List<FunzioneAmb> listaFunzioniOld, Croce croceNew) {
        String funzioniDipendentiOld = "";
        Set<Funzione> dipendenti = null;
        Funzione funzioneBase = null;
        Funzione funzioneDip = null;
        String[] parti = null;
        String tag = ",";

        if (array.isValid(listaFunzioniOld)) {
            for (FunzioneAmb funzOld : listaFunzioniOld) {
                funzioniDipendentiOld = funzOld.getFunzioni_dipendenti();

                if (text.isValid(funzioniDipendentiOld)) {
                    parti = funzioniDipendentiOld.split(tag);
                }// end of if cycle

                if (parti != null) {
                    dipendenti = new HashSet();
                    for (String code : parti) {
                        funzioneDip = funzioneService.findByKeyUnica(croceNew, code.trim());
                        if (funzioneDip != null) {
                            dipendenti.add(funzioneDip);
                        }// end of if cycle
                    }// end of for cycle
                }// end of if cycle

                if (dipendenti != null) {
                    funzioneBase = funzioneService.findByKeyUnica(croceNew, funzOld.getSigla());
                    if (funzioneBase != null) {
                        funzioneBase.dipendenti = dipendenti;
                        funzioneService.save(funzioneBase);
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

    }// end of method


    /**
     * Crea il singolo servizio
     *
     * @param servizioOld della companyOld
     */
    private boolean creaSingoloServizio(ServizioAmb servizioOld, Croce croceNew) {
        String code = servizioOld.getSigla();
        String descrizione = servizioOld.getDescrizione();
        int ordine = servizioOld.getOrdine();//--non utilizzato
        boolean orario = servizioOld.isOrario();
        int oraInizio = servizioOld.getOra_inizio();
        int minutiInizio = servizioOld.getMinuti_inizio();
        int oraFine = servizioOld.getOra_fine();
        int minutiFine = servizioOld.getMinuti_fine();

        if (oraFine == 24 && minutiFine == 0) {
            oraFine = 0;
        }// end of if cycle

        int durata = servizioOld.getDurata();
        boolean visibile = servizioOld.isVisibile();
        boolean multiplo = servizioOld.isMultiplo();
        boolean primo = servizioOld.isPrimo();//--non utilizzato
        boolean fineGiornoSuccessivo = servizioOld.isFine_giorno_successivo(); //--non utilizzato
        Set<Funzione> funzioniObbligatorie = getFunzioniObbligatorie(servizioOld, croceNew);
        Set<Funzione> funzioniFacoltative = getFunzioniFacoltative(servizioOld, croceNew);
        LocalTime inizio = LocalTime.of(oraInizio, minutiInizio);
        LocalTime fine = LocalTime.of(oraFine, minutiFine);

        if (durata != date.differenza(fine, inizio)) {
            log.error("Errore nella durata del servizio " + servizioOld.getSigla() + " di " + croceNew.code);
        }// end of if cycle

        try { // prova ad eseguire il codice
            return servizioService.creaIfNotExist(croceNew, code, descrizione, orario, inizio, fine, visibile, multiplo, funzioniObbligatorie, funzioniFacoltative);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            return false;
        }// fine del blocco try-catch


    }// end of method


    /**
     * Recupera le funzioni obbligatorie del servizio
     *
     * @param servizioOld della companyOld
     */
    private Set<Funzione> getFunzioniObbligatorie(ServizioAmb servizioOld, Croce croceNew) {
        Set<Funzione> listaFunzioni = new HashSet<>();
        Funzione funz;
        int numeroFunzioniObbligatorie = servizioOld.getFunzioni_obbligatorie();

        //--comincio dal fondo
        if (numeroFunzioniObbligatorie > 3) {
            funz = getFunzione(croceNew, servizioOld.getFunzione4_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        if (numeroFunzioniObbligatorie > 2) {
            funz = getFunzione(croceNew, servizioOld.getFunzione3_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        if (numeroFunzioniObbligatorie > 1) {
            funz = getFunzione(croceNew, servizioOld.getFunzione2_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        if (numeroFunzioniObbligatorie > 0) {
            funz = getFunzione(croceNew, servizioOld.getFunzione1_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        return listaFunzioni;
    }// end of method


    /**
     * Recupera le funzioni obbligatorie del servizio
     *
     * @param servizioOld della companyOld
     */
    private Set<Funzione> getFunzioniFacoltative(ServizioAmb servizioOld, Croce croceNew) {
        Set<Funzione> listaFunzioni = new HashSet<>();
        Funzione funz;
        int numeroFunzioniObbligatorie = servizioOld.getFunzioni_obbligatorie();

        //--comincio dall'inizio
        if (numeroFunzioniObbligatorie < 1) {
            funz = getFunzione(croceNew, servizioOld.getFunzione1_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        if (numeroFunzioniObbligatorie < 2) {
            funz = getFunzione(croceNew, servizioOld.getFunzione2_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        if (numeroFunzioniObbligatorie < 3) {
            funz = getFunzione(croceNew, servizioOld.getFunzione3_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        if (numeroFunzioniObbligatorie < 4) {
            funz = getFunzione(croceNew, servizioOld.getFunzione4_id());
            if (funz != null) {
                listaFunzioni.add(funz);
            }// end of if cycle
        }// end of if cycle

        return listaFunzioni;
    }// end of method


    private Funzione getFunzione(Croce croceNew, long idFunzione) {
        Funzione funz = null;
        FunzioneAmb funzAmb = funzioneAmb.findByID(idFunzione);

        if (funzAmb != null) {
            funz = funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla());
            if (funz == null) {
                System.out.println("Siamo in selezionaFunzioni e non trovo la funzione: " + croceNew.code + funzAmb.getSigla());
            }// end of if cycle
        }// end of if cycle

        return funz;
    }// end of method


    private void creaColoreGruppoServizi(Croce croceNew) {
        List<Servizio> servizi = servizioService.findAllByCroce(croceNew);
        LinkedHashMap<String, List<Servizio>> mappa = null;
        List<Servizio> lista;
        String sigla;
        String tag = "-";
        List<EAColor> colori = wamService.getColoriServizi();
        int k = 0;

        if (array.isValid(servizi)) {
            mappa = new LinkedHashMap<>();
            for (Servizio servizio : servizi) {
                sigla = servizio.code;

                if (sigla.contains(tag)) {
                    sigla = sigla.substring(0, sigla.indexOf(tag)).trim();
                }// end of if cycle

                if (mappa.containsKey(sigla)) {
                    lista = mappa.get(sigla);
                } else {
                    lista = new ArrayList<>();
                }// end of if/else cycle

                lista.add(servizio);
                mappa.put(sigla, lista);
            }// end of for cycle
        }// end of if cycle

        if (mappa != null && mappa.size() > 0) {
            for (List<Servizio> listaGruppo : mappa.values()) {
                for (Servizio servizio : listaGruppo) {
                    servizio.colore = colori.get(k).getTag();
                    servizioService.save(servizio);
                }// end of for cycle
                k++;
            }// end of for cycle
        }// end of if cycle

    }// end of method


    private UtenteAmb getUtenteAmb(List<UtenteAmb> listaUtentiOld, MiliteAmb militeOld) {
        UtenteAmb utenteOld = null;
        long militeID = militeOld.getId();

        if (listaUtentiOld != null && listaUtentiOld.size() > 0) {
            for (UtenteAmb utenteOldTmp : listaUtentiOld) {

                if (utenteOldTmp.getMilite_id() == militeID) {
                    utenteOld = utenteOldTmp;
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return utenteOld;
    }// end of method


    private RuoloAmb getRuoloOld(List<UtenteRuoloAmb> listaUtentiRuoloOld, List<RuoloAmb> listaRuoliOld, UtenteAmb utenteOld) {
        RuoloAmb ruoloOld = null;
        long utenteID = utenteOld.getId();
        long ruoloID = 4; //--se manca mette utente semplice

        if (listaUtentiRuoloOld != null && listaUtentiRuoloOld.size() > 0) {
            for (UtenteRuoloAmb utenteRuoloOld : listaUtentiRuoloOld) {

                if (utenteRuoloOld.getUtente_id() == utenteID) {
                    ruoloID = utenteRuoloOld.getRuolo_id();
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        if (listaRuoliOld != null && listaRuoliOld.size() > 0 && ruoloID > 0) {
            for (RuoloAmb ruoloOldTmp : listaRuoliOld) {

                if (ruoloOldTmp.getId() == ruoloID) {
                    ruoloOld = ruoloOldTmp;
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return ruoloOld;
    }// end of method


    /**
     * Crea il singolo milite
     * Non è detto che ci sia il login corretto per la company
     * Quindi non posso usare il metodo userService.findOrCrea() che usa la company del login
     * Quindi inserisco la company direttamente
     *
     * @param militeOld della companyOld
     * @param utenteOld della companyOld
     * @param ruoloOld  della companyOld
     * @param croceNew  usata in springWam
     */
    private boolean creaSingoloMilite(MiliteAmb militeOld, UtenteAmb utenteOld, RuoloAmb ruoloOld, Croce croceNew) {
        boolean status = true;
        Milite militeNew;
        boolean militeSaved;
        //        int ordine = 0;
        //        boolean locked = !militeOld.isAttivo();
        String cognome = militeOld.getCognome();
        //        long croce_id = militeOld.getCroce_id();
        //        Date data_nascita = militeOld.getData_nascita();
        boolean dipendente = militeOld.isDipendente();
        String mail = militeOld.getEmail();
        String nome = militeOld.getNome();
        //        String note = militeOld.getNote();
        //        int ore_anno = militeOld.getOre_anno();
        //        Date scadenzablsd = militeOld.getScadenzablsd();
        //        Date scadenza_non_trauma = militeOld.getScadenza_non_trauma();
        //        Date scadenza_trauma = militeOld.getScadenza_trauma();
        String telefono = militeOld.getTelefono_cellulare();
        //        String telefono_fisso = militeOld.getTelefono_fisso();
        //        int turni_anno = militeOld.getTurni_anno();
        //        int ore_extra = militeOld.getOre_extra();

        //        boolean account_expired = utenteOld.isAccount_expired();
        //        boolean account_locked = utenteOld.isAccount_locked();
        boolean enabled = utenteOld != null ? utenteOld.isEnabled() : false;
        boolean admin = utenteOld != null ? utenteRuoloAmb.isAdmin(utenteOld) : false;
        boolean infermiere = getInfermiereNew(militeOld);
        String pass = utenteOld != null ? utenteOld.getPass() : militeOld.getNome().toLowerCase() + "123";
        //        String password = utenteOld.getPassword();//--non utilizzato
        //        boolean password_expired = utenteOld.isPassword_expired();
        //        String userName = utenteOld.getUsername();//--non utilizzato
        String nickname = utenteOld != null ? utenteOld.getNickname() : militeOld.getNome().toLowerCase().substring(0, 1) + "." + militeOld.getCognome().toLowerCase();
        String nickname2 = "";
        Role ruoloNew = ruoloOld != null ? getRoleNew(ruoloOld) : roleService.getUser();
        Set<Role> ruoliNew = getRuoli(ruoloNew);

        Set<Funzione> funzioni = getFunzioniNew(croceNew, militeOld);
        String message;

        militeSaved = militeService.creaIfNotExist(croceNew, nome, cognome, telefono, nickname, pass, ruoliNew, mail, enabled, admin, dipendente, infermiere, funzioni);

        if (utenteOld == null) {
            importMilite(EALogLivello.warn, croceNew.getCode() + " - Manca utente per il milite " + militeOld.getNome() + " " + militeOld.getCognome());
        }// end of if cycle

        if (funzioni == null && enabled) {
            importMilite(EALogLivello.debug, croceNew.getCode() + " - Il milite " + militeOld.getNome() + " " + militeOld.getCognome() + " (attivo), non ha nessuna funzione abilitata.");
        }// end of if cycle

        if (!militeSaved) {
            nickname2 = nickname + "/2";
            militeSaved = militeService.creaIfNotExist(croceNew, nome, cognome, telefono, nickname2, pass, ruoliNew, mail, enabled, admin, dipendente, infermiere, funzioni);

            if (militeSaved) {
                message = "Al milite " + militeOld.getNome() + " " + militeOld.getCognome() + " è stato cambiato nickName perché ne esisteva già un altro";
                importMilite(EALogLivello.warn, message);
                militeNew = militeService.findByKeyUnica(nickname2);
                militeNew.noteWam = message;
                militeService.save(militeNew);
            } else {
                importMilite(EALogLivello.error, croceNew.getCode() + " - Il milite " + militeOld.getNome() + " " + militeOld.getCognome() + " esiste già in altra croce e non sono riuscito a cambiargli il nicName");
                status = false;
            }// end of if/else cycle

        }// end of if cycle

        return status;
    }// end of method


    private Set<Role> getRuoli(Role ruoloNew) {
        Set<Role> ruoliNew = new HashSet<>();
        Role ruoloUtente = roleService.getUser();
        ruoliNew.add(ruoloNew);

        //--aggiunge comunque (se manca) il ruolo utente
        if (!ruoliNew.contains(ruoloUtente)) {
            ruoliNew.add(ruoloUtente);
        }// end of if cycle

        return ruoliNew;
    }// end of method


    private boolean getInfermiereNew(MiliteAmb militeOld) {
        boolean isInfermiere = false;
        String tagIniUno = "I.P.";
        String tagIniDue = "I.P.-";
        String tagIniTre = "INFERMIERE";
        String cognome = militeOld.getCognome();

        if (cognome.startsWith(tagIniUno) || cognome.startsWith(tagIniDue) || cognome.startsWith(tagIniTre)) {
            isInfermiere = true;
        }// end of if cycle

        return isInfermiere;
    }// end of method


    private Role getRoleNew(RuoloAmb ruoloOld) {
        ERuoliAmb ruoloAmb = ERuoliAmb.get(ruoloOld.getAuthority());
        EARole roleNew = null;

        if (ruoloAmb != null) {
            switch (ruoloAmb) {
                case ROLE_prog:
                    roleNew = EARole.developer;
                    break;
                case ROLE_custode:
                    roleNew = EARole.admin;
                    break;
                case ROLE_admin:
                    roleNew = EARole.admin;
                    break;
                case ROLE_milite:
                    roleNew = EARole.user;
                    break;
                case ROLE_ospite:
                    roleNew = EARole.guest;
                    break;
                default:
                    roleNew = EARole.guest;
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return (Role) roleService.findById(roleNew.name());
    }// end of method


    private Set<Funzione> getFunzioniNew(Croce croceNew, MiliteAmb militeOld) {
        Set<Funzione> funzioni = null;
        List<FunzioneAmb> funzioniOld = null;

        if (militeOld != null) {
            funzioniOld = militeFunzioneAmb.findAllFunzioniByMilite(militeOld);
        }// end of if cycle

        if (funzioniOld != null && funzioniOld.size() > 0) {
            funzioni = new HashSet<>();
            for (FunzioneAmb funzAmb : funzioniOld) {
                if (militeOld.getCognome().equals("Ceresa") && (funzAmb.getSigla().equals("tut") || funzAmb.getSigla().equals("tir"))) {
                } else {
                    funzioni.add(funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla()));
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        return funzioni;
    }// end of method


    /**
     * Costruisce una nuova istanza di Persona da un vecchio campo testo
     * Presuppone che il nome preceda il cognome, separato da spazio
     */
    private Person fixPersona(String oldPersona) {
        Person persona = null;
        String spazio = " ";
        int pos = 0;
        String nome = "";
        String cognome = oldPersona != null ? oldPersona : "";

        if (text.isEmpty(oldPersona)) {
            return null;
        }// end of if cycle

        if (oldPersona.contains(spazio)) {
            pos = oldPersona.indexOf(spazio);
            nome = oldPersona.substring(0, pos);
            cognome = oldPersona.substring(pos);
        }// end of if cycle

        persona = personService.newEntity(nome, cognome, "", null, "");
        persona.id = null;
        return persona;
    }// end of constructor


    /**
     * Costruisce una nuova istanza di Address da un vecchio campo testo
     * Presuppone una formattazione precisa
     */
    private Address fixIndirizzo(String oldIndirizzo) {
        String indirizzo = oldIndirizzo;
        String localita = "";
        String cap = "";
        String spazio = " ";
        String trattino = "-";
        String parentesi = "(";
        String rimanente = "";
        int pos = 0;

        if (text.isEmpty(oldIndirizzo)) {
            return null;
        }// end of if cycle

        if (oldIndirizzo.contains(trattino)) {
            pos = oldIndirizzo.indexOf(trattino);
            pos = oldIndirizzo.indexOf(spazio, pos);
            indirizzo = oldIndirizzo.substring(0, pos - trattino.length()).trim();
            rimanente = oldIndirizzo.substring(pos).trim();
        }// end of if cycle

        if (rimanente.contains(spazio)) {
            pos = rimanente.indexOf(spazio);
            pos = rimanente.indexOf(spazio, pos);
            cap = rimanente.substring(0, pos).trim();
            localita = rimanente.substring(pos).trim();
        }// end of if cycle

        if (localita.contains(parentesi)) {
            pos = localita.indexOf(parentesi);
            localita = localita.substring(0, pos).trim();
        }// end of if cycle

        return indirizzoService.newEntity(indirizzo, localita, cap);
    }// end of constructor


    //    /**
    //     * Importa i turni esistenti in una croce di webambulanze
    //     */
    //    private boolean importTurni(CroceAmb croceOld) {
    //        return importTurni(getCroce(croceOld));
    //    }// end of constructor


    /**
     * Crea il singolo turno
     * Non è detto che ci sia il login corretto per la company
     * Quindi non posso usare il metodo userService.findOrCrea() che usa la company del login
     * Quindi inserisco la company direttamente
     *
     * @param turnoOld della companyOld
     * @param croceNew company usata in springWam
     */
    private boolean creaSingoloTurno(Croce croceNew, TurnoAmb turnoOld) {
        boolean status = true;
        Turno turnoNew = null;
        Servizio servizio = recuperaServizio(croceNew, turnoOld);
        LocalDate giornoNew = date.dateToLocalDate(turnoOld.getInizio());
        LocalTime inizioNew = getTime(turnoOld.getInizio());
        LocalTime fineNew = getTime(turnoOld.getFine());
        List<Iscrizione> iscrizioni = recuperaIscrizioni(turnoOld, servizio);
        String titoloExtra = turnoOld.getTitolo_extra();
        String localitaExtra = turnoOld.getLocalità_extra();
        String note = turnoOld.getNote();

        //--cominciamo a cercare se c'è
        turnoNew = (Turno) turnoService.findById(croceNew.code + turnoService.getPropertyUnica(giornoNew, servizio));

        if (turnoNew == null) {
            turnoNew = turnoService.newEntity(croceNew, giornoNew, servizio, inizioNew, fineNew, iscrizioni, titoloExtra, localitaExtra);
        } else {
            return status;
        }// end of if/else cycle

        //--le iscrizioni embedded vanno completate con gli orari del turno appena creato
        if (iscrizioni != null) {
            for (Iscrizione iscr : iscrizioni) {
                if (iscr.milite != null) {
                    iscr.inizio = inizioNew;
                    iscr.fine = fineNew;
                }// end of if cycle
            }// end of for cycle
        }

        turnoService.save(turnoNew);
        return status;
    }// end of method


    private LocalTime getTime(Date inizioFine) {
        LocalDateTime localDateTime = date.dateToLocalDateTimeUTC(inizioFine);
        return localDateTime.toLocalTime();
    }// end of method


    private Servizio recuperaServizio(Croce croceNew, TurnoAmb turnoOld) {
        Servizio servizioNew = null;
        ServizioAmb servizioOld = null;
        String sigla;
        long tipoturnoid;

        if (turnoOld != null) {
            tipoturnoid = turnoOld.getTipo_turno_id();
            servizioOld = servizioAmb.findByID(tipoturnoid);
            if (servizioOld != null) {
                sigla = servizioOld.getSigla();

                if (!sigla.equals("")) {
                    servizioNew = servizioService.findByKeyUnica(croceNew, sigla);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle

        if (servizioNew == null) {
            int stop = 87;
        }// end of if cycle

        return servizioNew;
    }// end of method


    private List<Iscrizione> recuperaIscrizioni(TurnoAmb turnoOld, Servizio servizio) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        Iscrizione iscrizione;
        String tag = FlowCost.ASTERISCO;

        iscrizione = recuperaIscrizione(turnoOld.getFunzione1_id(), turnoOld.getMilite_funzione1_id(), turnoOld.getOre_milite1(), turnoOld.isProblemi_funzione1(), servizio);
        if (iscrizione != null) {
            if (turnoOld.isProblemi_funzione1()) {
                iscrizione.setNote(text.isValid(turnoOld.getNote()) ? turnoOld.getNote() : tag);
            }
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione(turnoOld.getFunzione2_id(), turnoOld.getMilite_funzione2_id(), turnoOld.getOre_milite2(), turnoOld.isProblemi_funzione2(), servizio);
        if (iscrizione != null) {
            if (turnoOld.isProblemi_funzione2()) {
                iscrizione.setNote(text.isValid(turnoOld.getNote()) ? turnoOld.getNote() : tag);
            }
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione(turnoOld.getFunzione3_id(), turnoOld.getMilite_funzione3_id(), turnoOld.getOre_milite3(), turnoOld.isProblemi_funzione3(), servizio);
        if (iscrizione != null) {
            if (turnoOld.isProblemi_funzione3()) {
                iscrizione.setNote(text.isValid(turnoOld.getNote()) ? turnoOld.getNote() : tag);
            }
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione(turnoOld.getFunzione4_id(), turnoOld.getMilite_funzione4_id(), turnoOld.getOre_milite4(), turnoOld.isProblemi_funzione4(), servizio);
        if (iscrizione != null) {
            if (turnoOld.isProblemi_funzione4()) {
                iscrizione.setNote(text.isValid(turnoOld.getNote()) ? turnoOld.getNote() : tag);
            }
            iscrizioni.add(iscrizione);
        }// end of if cycle

        if (iscrizioni.size() == 0) {
            iscrizioni = null;
        }// end of if cycle

        return iscrizioni;
    }// end of method


    /**
     * Crea la singola iscrizione (embedded)
     * Non è detto che ci sia il login corretto per la company
     * Quindi non posso usare il metodo userService.findOrCrea() che usa la company del login
     * Quindi inserisco la company direttamente
     */
    private Iscrizione recuperaIscrizione(long funzioneOldID, long militeOldID, int durata, boolean esisteProblema, Servizio servizio) {
        Funzione funzioneNew = recuperaFunzione(funzioneOldID, servizio);
        Milite militeNew = recuperaMilite(militeOldID);
        LocalDateTime timestamp = LocalDateTime.now();
        String nota = "";

        if (funzioneNew == null) {
            return null;
        }// end of if cycle

        return iscrizioneService.newEntity(funzioneNew, militeNew, durata, esisteProblema);
    }// end of method


    private Funzione recuperaFunzione(long keyID, Servizio servizio) {
        Funzione funzioneNew = null;
        FunzioneAmb funzioneOld = null;
        List<Funzione> funzioniDelServizio = null;
        String siglaOld = "";
        String codeNew = "";

        if (servizio != null) {
            funzioniDelServizio = servizioService.getFunzioniAll(servizio);
        } else {
            int a = 87;
        }// end of if/else cycle

        if (keyID > 0) {
            funzioneOld = funzioneAmb.findByID(keyID);
        }// end of if cycle

        if (funzioneOld != null) {
            siglaOld = funzioneOld.getSigla();
            codeNew = siglaOld;
        }// end of if cycle

        if (text.isValid(codeNew)) {
            if (funzioniDelServizio != null) {
                for (Funzione funz : funzioniDelServizio) {
                    if (funz.getCode().equals(codeNew)) {
                        funzioneNew = funz;
                    }// end of if cycle
                }// end of for cycle
            } else {
                int a = 87;
            }// end of if/else cycle
        }// end of if cycle

        return funzioneNew;
    }// end of method


    private Milite recuperaMilite(long keyID) {
        UtenteAmb utente = null;
        String username = "";

        if (keyID > 0) {
            utente = utenteAmb.findByMilite(keyID);
        }// end of if cycle

        if (utente != null) {
            username = utente.getNickname();
        }// end of if cycle

        if (username.equals("")) {
            MiliteAmb militeOld = militeAmb.findByID(keyID);
            if (militeOld != null) {
                String nome = militeOld.getNome();
                String cognome = militeOld.getCognome();
                return militeService.findByNomeAndCognome(nome, cognome);
            }
        }

        return militeService.findById(username);
    }// end of method


    //--pulisce i logs precedenti, prima di iniziare un nuovo ciclo di import
    public void deleteImportMiliti() {
        mongo.deleteByProperty(Log.class, "type.id", WamCost.IMPORT_MILITI);
    }// fine del metodo


    //--pulisce i logs precedenti, prima di iniziare un nuovo ciclo di import
    public void deleteImportTurni() {
        mongo.deleteByProperty(Log.class, "type.id", WamCost.IMPORT_TURNI);
    }// fine del metodo


    //--registra un avviso
    public void importMilite(EALogLivello livello, String descrizione) {
        //        logger.crea(livello, (Logtype) logtype.findById(WamCost.IMPORT_MILITI), descrizione);
    }// fine del metodo


    //--registra un avviso
    public void importTurno(String descrizione) {
        //        logger.crea(EALogLivello.debug, (Logtype) logtype.findById(WamCost.IMPORT_TURNI), descrizione);
    }// fine del metodo


    /**
     * Regola il wrapper per essere sicuro che abbia entrambe le croci (vecchia e nuova)
     */
    public ImportResult fixResult(ImportResult result) {
        CroceAmb croceOld = result.getCroceOld();
        Croce croceNew = result.getCroceNew();

        if (croceOld == null && croceNew != null) {
            croceOld = this.getCroce(croceNew);
            result.setCroceOld(croceOld);
        }// end of if cycle

        if (croceNew == null && croceNew != null) {
            croceNew = this.getCroce(croceOld);
            result.setCroceNew(croceNew);
        }// end of if cycle

        return result;
    }// end of method

}// end of class
