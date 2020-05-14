package it.algos.vaadwam.tabellone;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoRepository;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.annotation.PostConstruct;
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

    @Autowired
    private MiliteService militeService;

    @Autowired
    protected AVaadinService vaadinService;

//    /**
//     * Wam-Login della sessione con i dati del Milite loggato <br>
//     */
//    private WamLogin wamLogin;

    @Autowired
    private MongoTemplate mongoTemplate;


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


//    @PostConstruct
//    private void init(){
//        AContext context = vaadinService.getSessionContext();
//        vaadinService.getSessionContext();
//        if (context!=null){
//            wamLogin = (WamLogin) context.getLogin();
//        }
//    }

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
     * Costruisce la lista delle righe da visualizzare nella grid per un dato periodo.
     *
     * @param data1  data iniziale
     * @param quantiGiorni numero di giorni da analizzare
     */
    public List<Riga> getGridRigheList(LocalDate data1, int quantiGiorni) {
        List<Riga> gridRigheList;
        Riga riga;
        LocalDate giornoFinale = data1.plusDays(quantiGiorni - 1);

        // tutti i servizi da visualizzare in tabellone:
        // - tutti i servizi visibili
        // - i servizi (non visibili o extra) che hanno almeno un turno definito nel periodo considerato
        List<Servizio> servizi = getServiziPeriodo(data1, giornoFinale);

        List<Turno> turni;

        gridRigheList = new ArrayList<>();
        for (Servizio servizio : servizi) {
            turni = turnoService.findByServizio(servizio, data1, giornoFinale);
            riga = rigaService.newEntity(data1, servizio, turni);
            gridRigheList.add(riga);
        }

        return gridRigheList;
    }


    /**
     * Ritorna tutti i servizi da visualizzare in tabellone per un dato periodo.
     * <br>
     * - tutti i servizi standard (cioè a orario definito) che sono visibili<br>
     * - più i servizi standard che sono invisibili ma hanno almeno un turno nel periodo considerato<br>
     * - più i servizi extra (visibili o invisibili) che hanno almeno un turno nel periodo considerato<br>
     * <br>
     * Il tutto ordinato come segue:<br>
     * - prima i servizi standard visibili, in ordine di servizio<br>
     * - poi i servizi standard invisibili<br>
     * - poi i servizi extra<br>
     * (ogni sottogruppo in ordine di servizio.)<br>
     */
    private List<Servizio> getServiziPeriodo(LocalDate data1, LocalDate data2){

        // i servizi standard che sono visibili
        //long start0=System.currentTimeMillis();

        //long start=System.currentTimeMillis();
        List<Servizio> serviziStandardVisibili = servizioService.findAllStandardVisibili();
//        long end=System.currentTimeMillis();
//        log.info("tempo serviziStandardVisibili: "+(end-start)+" ms");

        // i servizi standard che sono invisibili ma hanno almeno un turno nel periodo considerato
        List<Servizio> serviziStandardInvisibiliConTurni=new ArrayList<>();
        List<Servizio> serviziStandardInvisibili = servizioService.findAllStandardInvisibili();
        //start=System.currentTimeMillis();
        for(Servizio s : serviziStandardInvisibili){
            if (countTurni(s, data1, data2)>0){
                serviziStandardInvisibiliConTurni.add(s);
            }
        }
        //end=System.currentTimeMillis();
        //log.info("tempo serviziStandardInvisibiliConTurni ciclofor: "+(end-start)+" ms");


        // i servizi extra (visibili o invisibili) che hanno almeno un turno nel periodo considerato
        List<Servizio> serviziExtraConTurni=new ArrayList<>();
        List<Servizio> serviziExtra = servizioService.findAllExtra();
//        start=System.currentTimeMillis();
        for(Servizio s : serviziExtra){
            if (countTurni(s, data1, data2)>0){
                serviziExtraConTurni.add(s);
            }
        }
//        end=System.currentTimeMillis();
//        log.info("tempo serviziExtraConTurni ciclofor: "+(end-start)+" ms");


//        end=System.currentTimeMillis();
//        log.info("tempo totale: "+(end-start0)+" ms");

        // lista completa
        List<Servizio> servizi = new ArrayList<>();
        servizi.addAll(serviziStandardVisibili);
        servizi.addAll(serviziStandardInvisibiliConTurni);
        servizi.addAll(serviziExtraConTurni);

        return  servizi;

    }




    /**
     * Restituisce il numero di turni della croce corrente per un dato servizio in un dato periodo.
     */
    private long countTurni(Servizio servizio, LocalDate data1, LocalDate data2) {
        Croce croce=getWamLogin().getCroce();

        Query query = new Query();
        query.addCriteria(Criteria.where("croce").is(croce));
        query.addCriteria(Criteria.where("giorno").gte(data1).andOperator(Criteria.where("giorno").lte(data2)));
        query.addCriteria(Criteria.where("serv.$id").is(servizio.getId()));

//        long start=System.currentTimeMillis();
        long count = mongoTemplate.count(query, Turno.class);
//        long end=System.currentTimeMillis();
//        log.info("tempo count: "+(end-start)+" ms");

        return count;

    }


    /**
     * Recupera il login della session <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public WamLogin getWamLogin() {
        return vaadinService.getSessionContext() != null ? (WamLogin) vaadinService.getSessionContext().getLogin() : null;
    }



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
    public EAWamColore getColoreIscrizione(Turno turno, Iscrizione iscrizione) {
        EAWamColore colore = EAWamColore.creabile;

        AContext context = vaadinService.getSessionContext();
        WamLogin wamLogin=(WamLogin)context.getLogin();
        int critico=wamLogin.getCroce().getGiorniCritico();
        int semicritico=wamLogin.getCroce().getGiorniSemicritico();

        if (iscrizione == null) {
            return colore;
        }

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


    /**
     * Costruisce una lista wrapper di iscrizioni del turno <br>
     * Serve per regolare in maniera sincrona tutte le iscrizioni <br>
     */
    public List<TurnoIscrizione> getTurnoIscrizioni(Turno turnoEntity) {
        List<TurnoIscrizione> listaTurnoIscrizioni = new ArrayList<>();
        TurnoIscrizione turnoIscrizione = null;
        List<Iscrizione> listaIscrizioniDelTurno = turnoEntity.getIscrizioni();

        if (array.isValid(listaIscrizioniDelTurno)) {
            for (Iscrizione iscrizioneEntity : listaIscrizioniDelTurno) {
                turnoIscrizione = appContext.getBean(TurnoIscrizione.class, turnoEntity, iscrizioneEntity);
                listaTurnoIscrizioni.add(turnoIscrizione);
            }// end of for cycle
        }// end of if cycle

        return listaTurnoIscrizioni;
    }// end of method


    /**
     * Costruisce una lista modello dati per il collegamento TurnoEditPolymer con turno-edit.html <br>
     * Serve per tutte le property ESCLUSI i Button 'annulla' e 'conferma' <br>
     */
    public List<TurnoIscrizioneModel> getTurnoIscrizioniModello(List<TurnoIscrizione> listaTurnoIscrizioni) {
        List<TurnoIscrizioneModel> listaTurnoIscrizioniModello = new ArrayList<>();
        TurnoIscrizioneModel turnoIscrizioneModello;

        if (array.isValid(listaTurnoIscrizioni)) {
            for (TurnoIscrizione turnoIscrizione : listaTurnoIscrizioni) {
                turnoIscrizioneModello = creaItemIscrizioneModello(turnoIscrizione);
                listaTurnoIscrizioniModello.add(turnoIscrizioneModello);
            }// end of for cycle
        }// end of if cycle

        return listaTurnoIscrizioniModello;
    }// end of method


    /**
     * Costruisce un singolo item del modello dati per il collegamento TurnoEditPolymer con turno-edit.html <br>
     */
    @Deprecated
    public TurnoIscrizioneModel creaItemIscrizioneModello(TurnoIscrizione turnoIscrizione) {
        TurnoIscrizioneModel turnoIscrizioneModello = new TurnoIscrizioneModel();

        turnoIscrizioneModello.setKeyTag(turnoIscrizione.keyTag);
        turnoIscrizioneModello.setColore(turnoIscrizione.coloreTxt);
        turnoIscrizioneModello.setIcona(turnoIscrizione.iconaTxt);
        if(turnoIscrizione.militeEntity!=null){
            turnoIscrizioneModello.setIdMilite(turnoIscrizione.militeEntity.id);
        }
        turnoIscrizioneModello.setMilite(turnoIscrizione.militetxt);
        if (turnoIscrizione.funzioneEntity!=null){
            turnoIscrizioneModello.setIdFunzione(turnoIscrizione.funzioneEntity.id);
        }
        turnoIscrizioneModello.setFunzione(turnoIscrizione.funzioneTxt);
        turnoIscrizioneModello.setInizio(turnoIscrizione.inizioTxt);
        turnoIscrizioneModello.setNote(turnoIscrizione.noteTxt);
        turnoIscrizioneModello.setFine(turnoIscrizione.fineTxt);
        turnoIscrizioneModello.setAbilitata(turnoIscrizione.abilitata);
        turnoIscrizioneModello.setAbilitataPicker(turnoIscrizione.abilitataPicker);

        return turnoIscrizioneModello;
    }// end of method


}// end of class
