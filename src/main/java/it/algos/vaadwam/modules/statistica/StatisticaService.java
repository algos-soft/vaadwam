package it.algos.vaadwam.modules.statistica;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow.annotation.*;
import static it.algos.vaadflow.application.FlowCost.*;
import it.algos.vaadflow.backend.entity.*;
import it.algos.vaadflow.enumeration.*;
import it.algos.vaadflow.service.*;
import static it.algos.vaadwam.application.WamCost.*;
import it.algos.vaadwam.enumeration.*;
import it.algos.vaadwam.modules.croce.*;
import it.algos.vaadwam.modules.iscrizione.*;
import it.algos.vaadwam.modules.milite.*;
import it.algos.vaadwam.modules.servizio.*;
import it.algos.vaadwam.modules.turno.*;
import it.algos.vaadwam.wam.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.repository.*;

import java.time.*;
import java.util.*;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-ott-2019 7.35.49 <br>
 * <br>
 * Business class. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * NOT annotated with @VaadinSessionScope (sbagliato, perché SpringBoot va in loop iniziale) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_STA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class StatisticaService extends WamService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ADateService date;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public StatisticaRepository repository;

    public String usaDaemonElabora;

    public String lastElabora;

    public String durataLastElabora;

    public EATempo eaTempoTypeElabora;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ServizioService servizioService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public IscrizioneService iscrizioneService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected TurnoService turnoService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public StatisticaService(@Qualifier(TAG_STA) MongoRepository repository) {
        super(repository);
        super.entityClass = Statistica.class;
        this.repository = (StatisticaRepository) repository;
    }// end of Spring constructor


    /**
     * Le preferenze standard
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        this.usaDaemonElabora = USA_DAEMON_STATISTICHE;
        this.lastElabora = LAST_ELABORA;
        this.durataLastElabora = DURATA_ELABORA;
        this.eaTempoTypeElabora = EATempo.secondi;
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Statistica findOrCrea(Milite milite) {
        Statistica entity = findByKeyUnica(milite);

        if (entity == null) {
            entity = crea(milite);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity appena creata
     */
    public Statistica crea(Milite milite) {
        return (Statistica) save(newEntity((Croce) null, 0, 0, milite, null, 0, false, 0, 0));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Statistica newEntity() {
        return newEntity((Croce) null, 0, 0, null, null, 0, false, 0, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param ordine di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param code   codice di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Statistica newEntity(Croce croce, int anno, int ordine, Milite milite, LocalDate last, int delta, boolean valido, int turni, int ore) {
        Statistica entity = Statistica.builderStatistica()

                .ordine(ordine != 0 ? ordine : this.getNewOrdine())

                .anno(anno)

                .milite(milite)

                .last(last)

                .delta(delta)

                .valido(valido)

                .turni(turni)

                .ore(ore)

                .build();

        return (Statistica) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Se è prevista la company obbligatoria, antepone company.code a quanto sopra (se non è vuoto)
     * Se manca la company obbligatoria, non registra
     * <p>
     * Se è prevista la company facoltativa, antepone company.code a quanto sopra (se non è vuoto)
     * Se manca la company facoltativa, registra con idKey regolata come sopra
     * <p>
     * Per codifiche diverse, sovrascrivere il metodo
     *
     * @param entityBean da regolare
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    @Override
    public String addKeyCompany(AEntity entityBean, String keyCode) {
        String keyUnica = "";
        Croce croce = null;
        String companyCode = "";
        String annoTxt = VUOTA;

        if ((reflection.isEsiste(entityBean.getClass(), PROPERTY_CROCE))) {
            croce = (Croce) reflection.getPropertyValue(entityBean, PROPERTY_CROCE);
            if (croce != null) {
                companyCode = croce.getCode();
            }// end of if cycle
        }// end of if cycle

        if (text.isValid(companyCode)) {
            annoTxt = ((Statistica) entityBean).anno + VUOTA;
            keyUnica = companyCode + annoTxt + text.primaMaiuscola(keyCode);
        }
        else {
            if (annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria) {
                keyUnica = null;
            }
            else {
                keyUnica = keyCode;
            }// end of if/else cycle
        }// end of if/else cycle

        return keyUnica;
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Può essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {

        if (((Statistica) entityBean).croce == null) {
            ((Statistica) entityBean).croce = ((Statistica) entityBean).milite.croce;
        }// end of if cycle

        return super.beforeSave(entityBean, operation);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Statistica findByKeyUnica(Milite milite) {
        return repository.findByMilite(milite);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        String property = "";
        Milite milite = ((Statistica) entityBean).getMilite();

        if (milite != null) {
            property = milite.getId();
        }// end of if cycle

        return property;
    }// end of method


    /**
     * Returns all instances of the selected time of Croce <br>
     *
     * @return lista ordinata discendente di tutte le entities della croce nel periodo
     */
    public List<Statistica> findAllByYear(Croce croce, int anno) {
        List<Statistica> lista = null;
        Query query = new Query();
        Sort sort = new Sort(Sort.Direction.DESC, "giorno");

        query.addCriteria(Criteria.where("croce").is(croce));
        query.addCriteria(Criteria.where("anno").is(anno));
        query.with(sort);

        lista = mongo.mongoOp.find(query, Statistica.class);
        return lista;
    }// end of method


    /**
     * Returns the number of entities available for the current company
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return the number of entities
     */
    public int countByCroce(Croce croce) {
        return repository.countByCroce(croce);
    }// end of method


    public boolean elabora(Croce croce) {
        return elabora(croce, date.getAnnoCorrente());
    }// end of method


    public boolean elabora(Croce croce, int anno) {
        boolean status = false;
        long inizio = System.currentTimeMillis();
        List<Milite> militi = null;
        List<Turno> listaTurniCroce;
        List<Statistica> listaStatistiche;
        boolean isAnnoCorrente = true;

        if (anno == date.getAnnoCorrente()) {
            listaTurniCroce = turnoService.findAllByYearUntilNow(croce, anno);
        }
        else {
            listaTurniCroce = turnoService.findAllByYear(croce, anno);
            isAnnoCorrente = false;
        }

        listaStatistiche = findAllByYear(croce, anno);
        mongo.delete(listaStatistiche, Statistica.class);

        militi = militeService.findAllByCroce(croce);
        if (array.isValid(militi)) {
            for (Milite milite : militi) {
                elaboraSingoloMilite(croce, anno, milite, listaTurniCroce, isAnnoCorrente);
            }
            status = true;
        }

        setLastElabora(croce, inizio);
        long elapsedSec = (System.currentTimeMillis() - inizio) / 1000;
        String msg = "Elaborati i dati di " + militeService.countByCroce(croce) + " militi della croce " + croce.code + " in " + elapsedSec + "s";

        WamLogin wamLogin = getWamLogin();
        Milite milite = null;
        String ipAddr = null;
        if (wamLogin != null) {
            milite = wamLogin.getMilite();
            ipAddr = wamLogin.getAddressIP();
        }
        wamLogger.log(EAWamLogType.statistiche, msg, milite, ipAddr);

        return status;
    }


    public void elaboraSingoloMilite(Croce croce, int anno, Milite milite, List<Turno> listaTurniCroce, boolean isAnnoCorrente) {
        long inizio = System.currentTimeMillis();
        Milite militeIscritto;
        int turniMilite = 0;
        int minutiTotali = 0;
        int oreTotali = 0;
        LocalDate last = null;
        int delta = 0;
        boolean valido;
        int durata = 0;
        //        int numOreTurno = pref.getInt(NUMERO_ORE_TURNO_STANDARD, croce.code);
        int media;
        List<Iscrizione> iscrizioniTurno;
        Servizio servizio;
        boolean saltaStatistiche;
        StaTurnoIsc staTurnoIsc;
        List<StaTurnoIsc> iscrizioniMilite = new ArrayList<>();
        int ordine = 0;
        String equipaggio = VUOTA;

        for (Turno turno : listaTurniCroce) {
            iscrizioniTurno = turno.iscrizioni;
            servizio = turno.servizio;
            saltaStatistiche = servizio != null && servizio.saltaStatistiche;

            if (iscrizioniTurno != null && !saltaStatistiche) {
                for (Iscrizione iscriz : iscrizioniTurno) {
                    if (iscriz.milite != null) {
                        militeIscritto = iscriz.milite;
                        if (militeIscritto.id.equals(milite.id)) {
                            ordine++;
                            equipaggio = getEquipaggio(turno, iscriz);
                            staTurnoIsc = getStaTurnoIsc(ordine, turno, iscriz, equipaggio);
                            iscrizioniMilite.add(staTurnoIsc);
                            turniMilite++;
                            durata = staTurnoIsc.durataEffettiva;

                            if (durata == 0) {
                                logger.warn("Durata nulla per il turno di " + turno.servizio.code + " del " + date.get(turno.giorno, EATime.standard.getPattern()), this.getClass(), "elaboraSingoloMilite");
                            }
                            else {
                                if (durata < 0) {
                                    logger.error("Durata turno negativa", this.getClass(), "elaboraSingoloMilite");
                                }
                                else {
                                    minutiTotali += durata;
                                }
                            }
                            last = turno.giorno;
                            if (isAnnoCorrente) {
                                delta = date.differenza(LocalDate.now(), last);
                            }
                        }// end of if cycle
                    }
                }
            }// end of if cycle
        }// end of for cycle

        valido = checkValidita(turniMilite);
        if (pref.isBool(DISABILITA_LOGIN, croce.code)) {
            if (!milite.esentato) {
                milite.enabled = valido;
                militeService.save(milite);
            }
        }

        if (turniMilite > 0) {
            try {
                oreTotali = minutiTotali / 60;
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "elaboraSingoloMilite");
            }
            Statistica statistica = newEntity(croce, anno, 0, milite, last, delta, valido, turniMilite, oreTotali);
            media = oreTotali / turniMilite;
            statistica.media = media;
            if (iscrizioniMilite.size() > 0) {
                statistica.iscrizioni = iscrizioniMilite;
            }
            save(statistica);
        }// end of if cycle

    }// end of method


    public String getEquipaggio(Turno turno, Iscrizione esclusa) {
        String equipaggio = VUOTA;

        for (Iscrizione iscriz : turno.iscrizioni) {
            if (iscriz != null && iscriz.milite != null) {
                if (iscriz != esclusa) {
                    equipaggio += iscriz.milite;
                    equipaggio += VIRGOLA;
                }
            }
        }

        if (text.isValid(equipaggio)) {
            equipaggio = text.levaCoda(equipaggio, VIRGOLA);
        }

        return equipaggio;
    }// end of method


    public StaTurnoIsc getStaTurnoIsc(int ordine, Turno turno, Iscrizione iscriz, String equipaggio) {
        StaTurnoIsc staTurnoIsc = StaTurnoIsc.builderStatTurnoIsc()

                .ordine(ordine)

                .milite(iscriz.milite)

                .giorno(turno.giorno)

                .servizio(turno.servizio)

                .funzione(iscriz.funzione)

                .inizio(iscriz.inizio)

                .fine(iscriz.fine)

                .durataEffettiva(iscrizioneService.durataMinuti(iscriz))

                .esisteProblema(iscriz.esisteProblema)

                .titoloExtra(text.isValid(turno.titoloExtra) ? turno.titoloExtra : null)

                .localitaExtra(text.isValid(turno.localitaExtra) ? turno.localitaExtra : null)

                .equipaggio(text.isValid(equipaggio) ? equipaggio : null)

                .build();

        return staTurnoIsc;
    }// end of method


    /**
     * Controlla quanti turni sono stati fatti nell'anno/nel mese <br> <br>
     *
     * @param numTurni effettuati nel corrente anno
     *
     * @return tru se la frequenza è soddisfacente (per gli standard della singola croce)
     */
    public boolean checkValidita(int numTurni) {
        int numTurniMinimiMese = 2; //@todo da calibrare con preferenza della specifica croce
        int meseCorrente = date.getMeseCorrente();
        int turniRichiesti = numTurniMinimiMese * meseCorrente;

        return numTurni >= turniRichiesti;
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultima elaborazione effettuata <br>
     * Registra nelle preferenze la durata dell'ultima elaborazione effettuata <br>
     */
    protected void setLastElabora(Croce croce, long inizio) {
        setLastElabora(croce, inizio, lastElabora, durataLastElabora, eaTempoTypeElabora);
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo import effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo import effettuato <br>
     */
    protected void setLastElabora(Croce croce, long inizio, String lastImport, String durataLastImport, EATempo eaTempoTypeImport) {
        pref.saveValue(lastImport, LocalDateTime.now(), croce.code);
        pref.saveValue(durataLastImport, eaTempoTypeImport.get(inizio), croce.code);
    }// end of method

    //    /**
    //     * Anni di selezione per il popup in Statistiche <br>
    //     */
    //    protected List<Integer> getAnni() {
    //        List<Integer> lista = new ArrayList();
    //
    //        for (int k = 2013; k <= date.getAnnoCorrente(); k++) {
    //            lista.add(k);
    //        }
    //        return lista;
    //    }// end of method

}// end of class