package it.algos.vaadwam.tabellone;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.application.*;
import static it.algos.vaadflow.application.FlowCost.*;
import it.algos.vaadflow.modules.preferenza.*;
import it.algos.vaadflow.service.*;
import static it.algos.vaadwam.application.WamCost.*;
import it.algos.vaadwam.enumeration.*;
import it.algos.vaadwam.modules.croce.*;
import it.algos.vaadwam.modules.funzione.*;
import it.algos.vaadwam.modules.iscrizione.*;
import it.algos.vaadwam.modules.log.*;
import it.algos.vaadwam.modules.milite.*;
import it.algos.vaadwam.modules.riga.*;
import it.algos.vaadwam.modules.servizio.*;
import it.algos.vaadwam.modules.turno.*;
import it.algos.vaadwam.wam.*;
import static java.time.temporal.ChronoUnit.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.repository.*;

import java.time.*;
import java.util.*;

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
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WamLogService wamLogger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ADateService dateService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected RigaService rigaService;

    @Autowired
    protected ServizioService servizioService;

    @Autowired
    protected TurnoService turnoService;

    @Autowired
    protected IscrizioneService iscrizioneService;

    @Autowired
    protected CroceService croceService;

    @Autowired
    protected AVaadinService vaadinService;

    @Autowired
    protected PreferenzaService pref;

    @Autowired
    private MiliteService militeService;

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
     * @param data1        data iniziale
     * @param quantiGiorni numero di giorni da analizzare
     */
    public List<Riga> getGridRigheList(LocalDate data1, int quantiGiorni) {
        List<Riga> gridRigheList;
        Riga riga;
        LocalDate giornoFinale = data1.plusDays(quantiGiorni - 1);

        // tutti i servizi da visualizzare in tabellone:
        // - tutti i servizi standard
        // - i servizi non standard che hanno almeno un turno definito nel periodo considerato
        List<Servizio> servizi = getServiziPeriodo(data1, giornoFinale);

        gridRigheList = new ArrayList<>();
        for (Servizio servizio : servizi) {
            if (servizio.isOrarioDefinito()) {
                List<Turno> turni = turnoService.findByServizio(servizio, data1, giornoFinale);
                riga = rigaService.newEntity(data1, servizio, turni);
                gridRigheList.add(riga);
            } else {
                List<Riga> righe = creaRigheNonStandard(servizio, data1, giornoFinale);
                gridRigheList.addAll(righe);
            }
        }

        return gridRigheList;
    }


    /**
     * Crea l'elenco delle righe da aggiungere per un dato servizio non standard
     */
    private List<Riga> creaRigheNonStandard(Servizio servizio, LocalDate data1, LocalDate data2) {

        // Crea una lista di oggetti ColonnaGiorno, uno per ogni giorno.
        // Ognuno mantiene la lista dei turni di quel giorno, ordinata per ora inizio turno.
        //
        //        18  19  20  21  <- giorni
        //        [A] [C]  |  [D]
        //        [B]  |   v  [E]
        //         |   v      [F]
        //         v           |
        //                     v
        //
        List<ColonnaGiorno> colonne = new ArrayList<>();
        List<Turno> turni = turnoService.findByServizio(servizio, data1, data2);
        int numGiorni = (int) DAYS.between(data1, data2) + 1;
        int max = 0;  // la colonna con più elementi, questo sarà il numero totale di righe creato
        for (int i = 0; i < numGiorni; i++) {
            LocalDate data = data1.plusDays(i);
            List<Turno> turniGiorno = getTurniGiornoOrderByOraInizio(turni, data);
            ColonnaGiorno colonna = new ColonnaGiorno();
            colonna.addAll(turniGiorno);
            colonne.add(colonna);
            if (turniGiorno.size() > max) {
                max = turniGiorno.size();
            }
        }

        // Crea un array di oggetti ListaTurni, in numero adeguato a contenere la colonna più lunga.
        // Ogni riga contiene un oggetto ListaTurni con l'elenco dei turni di quella riga.
        // I riferimenti al giorno non servono perché sono già nel turno e sarà il tabellone a collocare
        // ogni turno nella colonna giusta.
        //
        //        [A] [C] [D] ->
        //        [B] [E] ->
        //        [F] ->
        //
        ListaTurni[] aListeTurni = new ListaTurni[max];
        for (int row = 0; row < max; row++) {
            ListaTurni listaTurni = new ListaTurni(numGiorni);
            aListeTurni[row] = listaTurni;
            for (int col = 0; col < colonne.size(); col++) {
                ColonnaGiorno colonna = colonne.get(col);
                Turno turno = colonna.get(row);
                if (turno != null) {
                    listaTurni.add(turno);
                }
            }
        }


        // Per ogni elemento dell'array aListeTurni crea una riga di tabellone
        List<Riga> righe = new ArrayList<>();
        for (ListaTurni listaTurni : aListeTurni) {
            Riga riga = rigaService.newEntity(data1, servizio, listaTurni);
            righe.add(riga);
        }

        return righe;

    }


    /**
     * Estrae da una lista di turni tutti quelli relativi al giorno dato in ordine di orario di inizio
     */
    private List<Turno> getTurniGiornoOrderByOraInizio(List<Turno> turni, LocalDate data) {
        List<Turno> turniOut = new ArrayList<>();
        for (Turno turno : turni) {
            if (turno.getGiorno().equals(data)) {
                turniOut.add(turno);
            }
        }

        // ordina per ora di inizio, e a parità di questa, ordina per data di creazione del record
        Collections.sort(turniOut, new Comparator<Turno>() {

            @Override
            public int compare(Turno t1, Turno t2) {
                if (t1.getInizio().isBefore(t2.getInizio())) {
                    return -1;
                } else {
                    if (t1.getInizio().isAfter(t2.getInizio())) {
                        return 1;
                    } else {  // stessa ora inizio
                        if (t1.getCreazione() != null && t2.getCreazione() != null) {
                            int ret = (t1.getCreazione().isBefore(t2.getCreazione()) ? -1 : 1);
                            return ret;
                        } else {
                            return 0;   // indistinguibili
                        }
                    }
                }
            }
        });

        return turniOut;
    }


    /**
     * Ritorna tutti i servizi da visualizzare in tabellone per un dato periodo.
     * <br>
     * - tutti i servizi standard (cioè a orario definito)<br>
     * - più i servizi non standard che hanno almeno un turno nel periodo considerato<br>
     * <br>
     * Prende in considerazione solo i servizi visibili.<br>
     * Ogni gruppo in ordine di servizio<br>
     */
    private List<Servizio> getServiziPeriodo(LocalDate data1, LocalDate data2) {

        // i servizi standard visibili
        List<Servizio> serviziStandardVisibili = servizioService.findAllStandardVisibili();

        // i servizi non-standard visibili che hanno almeno un turno nel periodo considerato
        List<Servizio> serviziNonStandardConTurni = new ArrayList<>();
        List<Servizio> serviziNonStandardVisibili = servizioService.findAllNonStandardVisibili();
        for (Servizio s : serviziNonStandardVisibili) {
            if (countTurni(s, data1, data2) > 0) {
                serviziNonStandardConTurni.add(s);
            }
        }

        // lista completa
        List<Servizio> servizi = new ArrayList<>();
        servizi.addAll(serviziStandardVisibili);
        servizi.addAll(serviziNonStandardConTurni);

        return servizi;

    }


    /**
     * Restituisce il numero di turni della croce corrente per un dato servizio in un dato periodo.
     */
    private long countTurni(Servizio servizio, LocalDate data1, LocalDate data2) {
        Croce croce = getWamLogin().getCroce();

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


    /**
     * Colore della iscrizione in funzione della data corrente <br>
     * I periodi di colore cambiano da Croce a Croce <br>
     */
    public EAWamColore getColoreIscrizione(Turno turno, Iscrizione iscrizione) {
        EAWamColore colore = EAWamColore.creabile;

        AContext context = vaadinService.getSessionContext();
        WamLogin wamLogin = (WamLogin) context.getLogin();
        int critico = wamLogin.getCroce().getGiorniCritico();
        int semicritico = wamLogin.getCroce().getGiorniSemicritico();

        if (iscrizione == null) {
            return colore;
        }

        if (turno.servizio.isDisponibile()) {
            return EAWamColore.disponibile;
        }

        if (iscrizioneService.isValida(iscrizione, turno.getServizio())) {
            colore = EAWamColore.normale;
        }
        else {
            if (mancaMenoDiGiorni(turno, critico)) {
                colore = EAWamColore.critico;
            }
            else {
                if (mancaMenoDiGiorni(turno, semicritico)) {
                    colore = EAWamColore.urgente;
                }
                else {
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

        if (turno.servizio.isDisponibile()) {
            return EAWamColore.disponibile;
        }

        if (turnoService.isValido(turno)) {
            colore = EAWamColore.normale;
        }
        else {
            if (mancaMenoDiGiorni(turno, critico)) {
                colore = EAWamColore.critico;
            }
            else {
                if (mancaMenoDiGiorni(turno, semicritico)) {
                    colore = EAWamColore.urgente;
                }
                else {
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
     * Turno previsto prima del numero di giorni indicati
     */
    public boolean mancaMenoDiGiorni(Turno turno, int giorni) {
        boolean status = false;
        LocalDate limite = LocalDate.now().plusDays(giorni);
        if (turno.giorno.isBefore(limite)) {
            status = true;
        }
        return status;
    }


    /**
     * Passato più di un certo numero di giorni dalla data di iscrizione
     */
    public boolean passatoPiuDiGiorni(Iscrizione iscrizione, int giorni) {
        LocalDateTime dataCreazione = iscrizione.getCreazione();
        if (dataCreazione != null) {
            LocalDate dataCreato = dataCreazione.toLocalDate();
            if (LocalDate.now().minusDays(giorni).isAfter(dataCreato)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Passato più di un certo numero di ore dalla data di iscrizione
     */
    public boolean passatoPiuDiOre(Iscrizione iscrizione, int ore) {
        LocalDateTime dataOraCreazione = iscrizione.getLastModifica();
        if (dataOraCreazione != null) {
            if (LocalDateTime.now().minusHours(ore).isAfter(dataOraCreazione)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Turno già effettuato
     */
    public boolean isStorico(Turno turno) {
        return mancaMenoDiGiorni(turno, 0);
    }// end of method


    /**
     * Determina se il milite può cancellare l'iscrizione.
     * <p>
     *
     * @param turno
     * @param iscrizione
     *
     * @return null se può cancellare, la motivazione se non può.
     */
    public String puoCancellareIscrizione(Turno turno, Iscrizione iscrizione) {

        // recupero la modalità di cancellazione
        String prefKey = pref.getEnumStr(EAPreferenzaWam.tipoCancellazione);
        EACancellazione modoCanc = null;
        if (prefKey != null) {
            modoCanc = EACancellazione.valueOf(prefKey);
        }
        if (modoCanc == null) {
            modoCanc = EACancellazione.tempoMancante;
        }

        String ret;


        switch (modoCanc) {
            case mai:
                return "cancellazione iscrizioni non abilitata";
            case sempre:
                return null;
            case tempoTrascorso:
                ret = null;
                int maxOre = pref.getInt(EAPreferenzaWam.numeroOreTrascorse);
                if (maxOre == 0) {
                    maxOre = 48;
                }
                if (passatoPiuDiOre(iscrizione, maxOre)) {
                    ret = "sono passate più di " + maxOre + " ore dall'iscrizione";
                }
                return ret;
            case tempoMancante:
                ret = null;
                int maxGiorni = pref.getInt(EAPreferenzaWam.numeroGiorniMancanti);
                if (maxGiorni == 0) {
                    maxGiorni = 2;
                }
                if (mancaMenoDiGiorni(turno, maxGiorni)) {
                    ret = "mancano meno di " + maxGiorni + " giorni alla data di esecuzione del turno";
                }
                return ret;
            default:
        }

        return null;
    }


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
        if (turnoIscrizione.militeEntity != null) {
            turnoIscrizioneModello.setIdMilite(turnoIscrizione.militeEntity.id);
        }
        turnoIscrizioneModello.setMilite(turnoIscrizione.militetxt);
        if (turnoIscrizione.funzioneEntity != null) {
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


    /**
     * Logga la variazione delle iscrizioni tra lo stato corrente e lo stato precedente di un turno.
     */
    public void logDeltaIscrizioni(Turno turno, Turno oldTurno) {

        Map<String, MiliteFunzione> mapOld=creaMap(oldTurno);
        Map<String, MiliteFunzione> mapNew=creaMap(turno);

        for(Map.Entry<String, MiliteFunzione> entry : mapOld.entrySet()){
            if(mapNew.get(entry.getKey())==null){
                logIscrizioni(oldTurno, entry.getValue().getMilite(), entry.getValue().getFunzione(), false);
            }
        }

        for(Map.Entry<String, MiliteFunzione> entry : mapNew.entrySet()){
            if(mapOld.get(entry.getKey())==null){
                logIscrizioni(turno, entry.getValue().getMilite(), entry.getValue().getFunzione(), true);
            }
        }

    }

    private void logIscrizioni(Turno turno, Milite milite, Funzione funzione, boolean action){
        Servizio s = turno.getServizio();
        String sGiorno = dateService.getDate(turno.getGiorno());
        String sFunzione=funzione.getSigla();
        String sAction;
        EAWamLogType logType;
        if(action){
            logType=EAWamLogType.nuovaIscrizione;
            sAction="iscritto al turno";
        }else {
            logType=EAWamLogType.cancellazioneIscrizione;
            sAction="cancellato dal turno";
        }
        String log = milite.getSigla()+" "+sAction+" "+s.getCode()+" del "+sGiorno+" ("+sFunzione+")";
        wamLogger.log(logType,log);

    }

    private Map<String, MiliteFunzione> creaMap(Turno turno){
        Map<String, MiliteFunzione> map=new HashMap<>();
        for(Iscrizione i : turno.getIscrizioni()){
            Milite m = i.getMilite();
            if(m!=null){
                Funzione f = i.getFunzione();
                map.put(f.getId()+"-"+m.getId(), new MiliteFunzione(m, f));
            }
        }
        return map;
    }

    @Data
    class MiliteFunzione{
        public MiliteFunzione(Milite milite, Funzione funzione) {
            this.milite = milite;
            this.funzione = funzione;
        }
        private Milite milite;
        private Funzione funzione;
    }




    /**
     * Logga una singola nuova iscrizione.
     */
    public void logIscrizione(Iscrizione iscrizione, Turno turno, boolean ripetizione) {

        Milite milite = iscrizione.getMilite();
        Servizio s = turno.getServizio();
        String sGiorno = dateService.getDate(turno.getGiorno());
        String sFunzione=iscrizione.getFunzione().getSigla();

        String log = milite.getSigla()+" iscritto al turno "+s.getCode()+" del "+sGiorno+" ("+sFunzione+")";
        if(ripetizione){
            log+=" [rip]";
        }
        wamLogger.log(EAWamLogType.nuovaIscrizione,log);

    }

    /**
     * Logga una singola nuova iscrizione.
     */
    public void logIscrizione(Iscrizione iscrizione, Turno turno) {
        logIscrizione(iscrizione, turno, false);
    }



    public void logEliminaTurno(Turno turno) {

        boolean haIscrizioni=false;
        for (Iscrizione iscr : turno.iscrizioni) {
            if (iscr.milite != null) {
                haIscrizioni = true;
                break;
            }
        }

        String message = "Cancellato turno - "+getMessaggioTurno(turno);
        if (haIscrizioni) {
            message += "militi iscritti: ";
            message += A_CAPO;
            for (Iscrizione iscr : turno.iscrizioni) {
                if (iscr.milite != null) {
                    message += "(" + iscr.funzione.sigla + ")" + " - " + iscr.milite.toString();
                    message += A_CAPO;
                }
            }
        }

        if (haIscrizioni) {
            wamLogger.log(EAWamLogType.cancellazioneTurnoPieno, message);
        } else {
            wamLogger.log(EAWamLogType.cancellazioneTurnoVuoto, message);
        }
    }


    public String getMessaggioTurno(Turno turno) {
        String message = VUOTA;
        String sep = " - ";

        message += "Turno: ";
        message += turno.id;
        message += A_CAPO;
        message += "giorno: ";
        message += turno.giorno;
        message += A_CAPO;
        message += "servizio: ";
        message += turno.servizio;
        message += A_CAPO;
        message += "orario: ";
        message += turno.inizio;
        message += sep;
        message += turno.fine;
        message += A_CAPO;

        return message;
    }


    public void logCreazioneTurno(Turno turno, boolean ripetizione){
        Servizio s = turno.getServizio();
        String sGiorno = dateService.getDate(turno.getGiorno());
        String log = "creato turno "+s.getCode()+" del "+sGiorno;
        if(ripetizione){
            log+=" [rip]";
        }
        wamLogger.log(EAWamLogType.creazioneTurno, log);
    }

    public void logCreazioneTurno(Turno turno){
        logCreazioneTurno(turno, false);
    }




    /**
     * Wrapper che mantiene una lista ordinata di turni
     */
    @Data
    class ColonnaGiorno extends ArrayList<Turno> {

        @Override
        public Turno get(int index) {
            if (index < size()) {
                return super.get(index);
            } else {
                return null;
            }
        }

    }


    class ListaTurni extends ArrayList<Turno> {

        public ListaTurni(int size) {
            super(size);
        }

    }

}// end of class
