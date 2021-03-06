package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ARandomService;
import it.algos.vaadwam.migration.ImportService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.service.ATextService.UGUALE;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.22.05 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_TUR)
@Slf4j
@AIScript(sovrascrivibile = false)
public class TurnoService extends WamService {


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

    public FunzioneService funzioneService;

    @Autowired
    protected ADateService date;

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
    protected ServizioService servizioService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private ImportService migration;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private TurnoRepository repository;

    private ARandomService randomService;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public TurnoService(@Qualifier(TAG_TUR) MongoRepository repository) {
        super(repository);
        super.entityClass = Turno.class;
        this.repository = (TurnoRepository) repository;
    }


    @PostConstruct
    private void init() {
        randomService = appContext.getBean(ARandomService.class, 6);
    }


    /**
     * Le preferenze standard
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.lastImport = LAST_IMPORT_TURNI;
        super.durataLastImport = DURATA_IMPORT_TURNI;
        super.eaTempoTypeImport = EATempo.secondi;
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param croce      di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param giorno     di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     * @param servizio   di riferimento (obbligatorio)
     * @param iscrizioni dei volontari a questo turno (obbligatorio per un turno valido)
     *
     * @return la entity è stata creata
     */
    public Turno creaIfNotExist(Croce croce, LocalDate giorno, Servizio servizio, List<Iscrizione> iscrizioni) {
        return creaIfNotExist(croce, giorno, servizio, iscrizioni, VUOTA, VUOTA);
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param croce         di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param giorno        di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     * @param servizio      di riferimento (obbligatorio)
     * @param iscrizioni    dei volontari a questo turno (obbligatorio per un turno valido)
     * @param titoloExtra   motivazione del turno extra (facoltativo)
     * @param localitaExtra nome evidenziato della località per turni extra (facoltativo)
     *
     * @return la entity è stata creata
     */
    public Turno creaIfNotExist(Croce croce, LocalDate giorno, Servizio servizio, List<Iscrizione> iscrizioni, String titoloExtra, String localitaExtra) {
        return save(newEntity(croce, giorno, servizio, servizio.getInizio(), servizio.getFine(), iscrizioni, titoloExtra, localitaExtra));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Turno newEntity() {
        return newEntity((LocalDate) null, (Servizio) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @param giorno   di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     * @param servizio di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Turno newEntity(Croce croce, LocalDate giorno, Servizio servizio) {
        return newEntity(croce, giorno, servizio, servizio.getInizio(), servizio.getFine(), (List<Iscrizione>) null, "", "");
    }


    public Turno newEntity(LocalDate giorno, Servizio servizio) {
        return newEntity(null, giorno, servizio);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param croce         di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param giorno        di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     * @param servizio      di riferimento (obbligatorio)
     * @param inizio        orario previsto (ore e minuti) di inizio turno (obbligatorio, suggerito da servizio)
     * @param fine          orario previsto (ore e minuti) di fine turno (obbligatorio, suggerito da servizio)
     * @param iscrizioni    dei volontari a questo turno (obbligatorio per un turno valido)
     * @param titoloExtra   motivazione del turno extra (facoltativo)
     * @param localitaExtra nome evidenziato della località per turni extra (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Turno newEntity(Croce croce, LocalDate giorno, Servizio servizio, LocalTime inizio, LocalTime fine, List<Iscrizione> iscrizioni, String titoloExtra, String localitaExtra) {

        Turno entity = Turno.builderTurno()

                .giorno(giorno != null ? giorno : LocalDate.now())

                .servizio(servizio)

                .inizio(inizio != null ? inizio : servizio != null ? servizio.inizio : LocalTime.MIDNIGHT)

                .fine(fine != null ? fine : servizio != null ? servizio.fine : LocalTime.MIDNIGHT)

                .iscrizioni(iscrizioni != null ? iscrizioni : addIscrizioni(servizio))

                .titoloExtra(titoloExtra.equals("") ? null : titoloExtra)

                .localitaExtra(localitaExtra.equals("") ? null : localitaExtra)

                .build();

        AEntity aEntity = addCroce(entity, croce);

        return (Turno) aEntity;
    }


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, EDIT)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Turno entity = (Turno) super.beforeSave(entityBean, operation);

        //--elimina informazioni inutili dalla lista (embedded) di iscrizioni
        if (array.isValid(entity.iscrizioni)) {
            for (Iscrizione iscr : entity.iscrizioni) {
                iscr.funzione.croce = null;
                iscr.funzione.dipendenti = null;
                iscr.funzione.descrizione = null;
            }// end of for cycle
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Saves a given entity.
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    @Override
    public Turno save(AEntity entityBean) {
        return (Turno) super.save(entityBean);
    }


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Turno findById(String id) {
        return (Turno) super.findById(id);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        Turno turno = (Turno) entityBean;
        LocalDate giorno = turno.getGiorno();
        Servizio servizio = turno.servizio;

        return getPropertyUnica(giorno, servizio);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    public String getPropertyUnica(LocalDate giorno, Servizio servizio) {
        String keyID = VUOTA;
        Query query = new Query();
        int anno = giorno.getYear();
        int day = giorno.getDayOfYear();
        String codeServizio = VUOTA;
        int numExtra = 0;

        if (servizio != null) {
            codeServizio = servizio.code;
            codeServizio += "-" + randomService.nextString();
        }

        keyID += anno;
        keyID += day;
        keyID += codeServizio;

        return keyID;
    }


    /**
     * Durata del servizio in minuti <br>
     */
    public int getDurata(Turno entityBean) {
        LocalTime inizio = entityBean.getInizio();
        LocalTime fine = entityBean.getFine();

        return date.durata(fine, inizio);
    }// end of method


    /**
     * Importazione di dati <br>
     *
     * @return informazioni sul risultato
     */
    @Override
    public boolean importa(Croce croce) {
        boolean eseguito;

        long inizio = System.currentTimeMillis();
        eseguito = migration.importTurni(croce);
        setLastImport(croce, inizio);

        return eseguito;
    }// end of method


    /**
     * Returns the number of entities available for the company
     *
     * @return the number of selected entities
     */
    public int countTwoWeek() {
        Croce croce = getCroce();
        int delta = 7;
        LocalDate oggi = LocalDate.now();
        LocalDate inizio = oggi.minusDays(delta + 1);
        LocalDate fine = oggi.plusDays(delta);

        return repository.countAllByCroceAndGiornoBetweenOrderByGiornoAsc(croce, inizio, fine);
    }// end of method


    /**
     * Returns the number of entities available for the current company
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return the number of entities
     */
    public int countByCroce(Croce croce) {
        return repository.countAllByCroceOrderByGiornoAsc(croce);
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Turno> findAllByCroce(Croce croce) {
        return repository.findAllByCroceOrderByGiornoAsc(croce);
    }// end of method


    /**
     * Returns instances <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Turno> findAll() {
        List<Turno> items = null;
        Croce croce = getCroce();

        if (croce != null) {
            items = findAllByCroce(croce);
        }// end of if cycle

        return items;
    }// end of method


    /**
     * Fetches the entities whose 'main text property' matches the given filter text.
     * <p>
     * Se esiste la company, filtrate secondo la company <br>
     * The matching is case insensitive. When passed an empty filter text,
     * the method returns all categories. The returned list is ordered by name.
     * The 'main text property' is different in each entity class and chosen in the specific subclass
     *
     * @param filter the filter text
     *
     * @return the list of matching entities
     */
    @Override
    public List<? extends AEntity> findFilter(String filter) {
        Croce croce = null;
        AContext context = null;
        int delta = 7;
        LocalDate oggi = LocalDate.now();
        LocalDate inizio = oggi.minusDays(delta + 1);
        LocalDate fine = oggi.plusDays(delta);
        VaadinSession vaadSession = VaadinSession.getCurrent();

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        if (context != null) {
            croce = (Croce) context.getLogin().getCompany();
        }// end of if cycle

        return repository.findAllByCroceAndGiornoBetweenOrderByGiornoAsc(croce, inizio, fine);
    }// end of method


    /**
     * Returns all instances of the selected Croce for the current year <br>
     *
     * @return lista ordinata di tutte le entities della croce
     */
    public List<Turno> findAllAnnoCorrente() {
        LocalDate inizio = date.primoGennaio();
        LocalDate fine = date.trentunDicembre();

        return repository.findAllByCroceAndGiornoBetweenOrderByGiornoAsc(getCroce(), inizio, fine);
    }// end of method


    /**
     * Returns all instances of the selected Croce <br>
     *
     * @return lista ordinata di tutte le entities della croce
     */
    public List<Turno> findAllByYear(int anno) {
        LocalDate inizio = date.primoGennaio(anno);
        LocalDate fine = date.trentunDicembre(anno);

        return repository.findAllByCroceAndGiornoBetweenOrderByGiornoAsc(getCroce(), inizio, fine);
    }// end of method


    /**
     * Returns all instances of the selected year of Croce <br>
     *
     * @return lista ordinata discendente di tutte le entities della croce
     */
    public List<Turno> findAllByYear(Croce croce, int anno) {
        LocalDate inizio = date.primoGennaio(anno);
        LocalDate fine = date.trentunDicembre(anno);

        return findAllByPeriod(croce, inizio, fine);
    }// end of method


    /**
     * Returns all instances of the selected Croce <br>
     *
     * @return lista ordinata di tutte le entities della croce
     */
    public List<Turno> findAllByYearUntilNow(Croce croce, int anno) {
        LocalDate inizio = date.primoGennaio(anno);
        LocalDate fine = LocalDate.now();

        return findAllByPeriod(croce, inizio, fine);
    }// end of method


    /**
     * Returns all instances of the selected time of Croce <br>
     *
     * @return lista ordinata discendente di tutte le entities della croce nel periodo
     */
    public List<Turno> findAllByPeriod(Croce croce, LocalDate inizio, LocalDate fine) {
        List<Turno> lista = null;
        Query query = new Query();
        Sort sort = new Sort(Sort.Direction.ASC, "giorno");

        query.addCriteria(Criteria.where("croce").is(croce));
        query.addCriteria(Criteria.where("giorno").gte(inizio).lte(fine));
        query.with(sort);

        lista = mongo.mongoOp.find(query, Turno.class);
        return lista;
    }// end of method


    /**
     * Returns instances of the current Croce <br>
     * Selected for Servizio and for starting/ending date <br>
     * Le date inizioie e fine sono comprensive degli estremi, mentre il metodo della repository no <br>
     * Estendo quindi le date a 1 giorno prima ed 1 giorno dopo <br>
     */
    public List<Turno> findByServizio(Servizio servizio, LocalDate inizioCompresoEstremo, LocalDate fineCompresoEstremo) {
        List<Turno> lista = null;
        Croce croce = getCroce();
        LocalDate inizioEsclusoEstremo = null;
        LocalDate fineEsclusoEstremo = null;

        if ((croce == null) || (servizio == null) || (inizioCompresoEstremo == null) || (fineCompresoEstremo == null)) {
            return null;
        }// end of if cycle

        inizioEsclusoEstremo = inizioCompresoEstremo.minusDays(1);
        fineEsclusoEstremo = fineCompresoEstremo.plusDays(1);

        if (inizioEsclusoEstremo != null && fineEsclusoEstremo != null) {
            lista = repository.findAllByCroceAndServizioAndGiornoBetweenOrderByGiornoAsc(croce, servizio, inizioEsclusoEstremo, fineEsclusoEstremo);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Tutti i turni della croce corrente in un dato giorno
     */
    public List<Turno> findByDate(LocalDate date) {
        return findByDate(date, date);
    }


    /**
     * Tutti i turni della croce corrente compresi in un dato periodo
     */
    public List<Turno> findByDate(LocalDate inizioCompresoEstremo, LocalDate fineCompresoEstremo) {
        List<Turno> lista = null;
        Croce croce = getCroce();
        LocalDate inizioEsclusoEstremo;
        LocalDate fineEsclusoEstremo;

        if ((croce == null) || (inizioCompresoEstremo == null) || (fineCompresoEstremo == null)) {
            return null;
        }

        inizioEsclusoEstremo = inizioCompresoEstremo.minusDays(1);
        fineEsclusoEstremo = fineCompresoEstremo.plusDays(1);

        if (inizioEsclusoEstremo != null && fineEsclusoEstremo != null) {
            long start = System.currentTimeMillis();
            lista = repository.findAllByCroceAndGiornoBetweenOrderByGiornoAsc(croce, inizioEsclusoEstremo, fineEsclusoEstremo);
            long end = System.currentTimeMillis();
            log.debug("tempo query turni by periodo: " + (end - start) + " ms");

        }

        return lista;
    }


    /**
     * Ritorna i turni della croce corrente per un dato servizio in un dato giorno.
     * <br>
     * Se si tratta di servizio standard è uno solo (salvo errori strutturali)<br>
     * Se si tratta di servizio non standard possono essere più di 1
     */
    public List<Turno> findByDateAndServizio(LocalDate giorno, Servizio servizio) {
        List<Turno> turni = repository.findAllByCroceAndServizioAndGiorno(getCroce(), servizio, giorno);
        return turni;
    }


    /**
     * Ritorna i turni della croce corrente per un dato servizio in un dato giorno e data croce.
     * <br>
     * Se si tratta di servizio standard è uno solo (salvo errori strutturali)<br>
     * Se si tratta di servizio non standard possono essere più di 1
     */
    public List<Turno> findByDateAndServizioAndCroce(LocalDate giorno, Servizio servizio, Croce croce) {
        List<Turno> turni = repository.findAllByCroceAndServizioAndGiorno(croce, servizio, giorno);
        return turni;
    }


    //    /**
    //     * Calcola il tempo di inizio del turno in base al giorno ed al tipo di servizio <br>
    //     */
    //    public LocalDateTime getInizio(LocalDate giorno, Servizio servizio) {
    //        int anno = giorno.getYear();
    //        int mese = giorno.getMonthValue();
    //        int giornoMese = giorno.getDayOfMonth();
    //        int ora = servizio.getOraInizio();
    //        int minuti = servizio.getMinutiInizio();
    //
    //        return LocalDateTime.of(anno, mese, giornoMese, ora, minuti);
    //    }// end of method


    //    /**
    //     * Calcola il tempo di fine del turno in base al giorno ed al tipo di servizio <br>
    //     */
    //    public LocalDateTime getFine(LocalDate giorno, Servizio servizio) {
    //        int anno = giorno.getYear();
    //        int mese = giorno.getMonthValue();
    //        int giornoMese = giorno.getDayOfMonth();
    //        int ora = servizio.getOraFine();
    //        int minuti = servizio.getOraInizio();
    //
    //        return LocalDateTime.of(anno, mese, giornoMese, ora, minuti);
    //    }// end of method
    //


    /**
     * Aggiunge le iscrizioni vuote ad un nuovo turno <br>
     * Con la funzione e senza milite <br>
     * Non mettiamo ne gli orari di inizio/fine ne la durata <br> (gac)
     */
    public List<Iscrizione> addIscrizioni(Servizio servizio) {
        List<Iscrizione> items = null;
        List<Funzione> funzioni = null;
        //        int durata = 0;

        if (servizio != null) {
            items = new ArrayList<>();
            funzioni = servizioService.getFunzioniAll(servizio);
            //            durata = servizioService.getDurata(servizio);
        }// end of if cycle

        if (funzioni != null) {
            for (Funzione funz : funzioni) {
                items.add(Iscrizione.builderIscrizione().funzione(funz).build());
            }// end of for cycle
        }// end of if cycle

        return items;
    }// end of method


    /**
     * Lista di iscrizioni, lunga quanto le funzioni del servizio del turno
     * Se una funzione non ha iscrizione, ne metto una vuota
     * Con la funzione e senza milite
     *
     * @param turno di riferimento
     *
     * @return lista (Iscrizione) di iscrizioni del turno
     */
    public List<Iscrizione> getIscrizioni(Turno turno) {
        List<Iscrizione> items = new ArrayList<>();
        List<Iscrizione> iscrizioniEmbeddeTurno = turno.getIscrizioni();
        Servizio servizio = null;
        servizio = turno.getServizio();
        List<Funzione> funzioni = servizioService.getFunzioniAll(servizio);
        boolean trovata;
        Funzione funzione = null;

        for (Funzione funz : funzioni) {
            trovata = false;

            if (array.isValid(iscrizioniEmbeddeTurno)) {
                for (Iscrizione iscr : iscrizioniEmbeddeTurno) {
                    funzione = iscr.getFunzione();
                    if (funzione != null) {
                        if (funzione.getCode().equals(funz.getCode())) {
                            items.add(iscr);
                            trovata = true;
                        }// end of if cycle
                    } else {
                        //                        log.warn("Iscrizione " + iscr + " del turno " + turno + " - Manca la funzione");
                    }// end of if/else cycle
                }// end of for cycle
            }// end of if cycle

            if (!trovata) {
                items.add(Iscrizione.builderIscrizione().funzione(funz).build());
            }// end of if cycle
        }// end of for cycle

        return items;
    }// end of method


    /**
     * Lista di iscrizioni, lunga quanto le funzioni del servizio <br>
     * Se una funzione non ha iscrizione, ne metto una vuota <br>
     * Con la funzione e senza milite <br>
     *
     * @param servizio di riferimento
     *
     * @return lista (Iscrizione) di iscrizioni per un turno con questo servizio
     */
    public List<Iscrizione> getIscrizioni(Servizio servizio) {
        List<Iscrizione> items = new ArrayList<>();
        List<Funzione> funzioni = servizioService.getFunzioniAll(servizio);

        if (array.isValid(funzioni)) {
            for (Funzione funzione : funzioni) {
                items.add(iscrizioneService.newEntity(servizio, funzione));
            }// end of for cycle
        }// end of if cycle

        return items;
    }// end of method


    /**
     * Restituisce la lista dei militi iscritti a un turno
     */
    public List<Milite> getMilitiIscritti(Turno turno) {
        List<Milite> militi = new ArrayList<>();
        for (Iscrizione iscrizione : turno.getIscrizioni()) {
            if (iscrizione.getMilite() != null) {
                militi.add(iscrizione.getMilite());
            }
        }
        return militi;
    }


    /**
     * Turno valido se tutte le funzioni obbligatorie hanno un milite segnato <br>
     *
     * @param turno di riferimento
     *
     * @return true se valido
     */
    public boolean isValido(Turno turno) {
        boolean turnoValido = true;
        Servizio servizio = null;
        servizio = turno.getServizio();
        List<Funzione> obbligatorie = servizioService.getObbligatorie(servizio);

        if (obbligatorie != null) {
            for (Funzione funz : obbligatorie) {
                if (!iscrizioneService.isValida(turno, funz)) {
                    turnoValido = false;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return turnoValido;
    }// end of method


    /**
     * Data completa (estesa) del giorno di esecuzione del turno <br>
     */
    public String getGiornoTxt(Turno turnoEntity) {
        String giornoTxt = VUOTA;
        LocalDate localData = null;

        if (turnoEntity != null) {
            localData = turnoEntity.giorno;
        }// end of if cycle

        if (localData != null) {
            giornoTxt = date.get(localData, EATime.completa);
        }// end of if cycle

        return giornoTxt;
    }// end of method


    /**
     * Durata di un servizio in ore <br>
     */
    public int getDurataInt(AEntity entityBean) {
        int durata = 0;

        if (entityBean != null) {
            if (((Turno) entityBean).inizio != null && ((Turno) entityBean).fine != null) {
                durata = date.differenza(((Turno) entityBean).fine, ((Turno) entityBean).inizio);
            }// end of if cycle
        }// end of if cycle

        return durata;
    }// end of method


    /**
     * Patch per regolare la durata effettiva di TUTTI i turni <br>
     * Può essere lanciato anche più volte, senza problemi <br>
     */
    public void fixDurata() {
        String message;
        Croce croce = getCroce();
        int totali = countByCroce(croce);
        int fatti = 0;
        long inizio = System.currentTimeMillis();
        List<Turno> lista;

        message = croce.code.toUpperCase() + " - Da elaborare " + text.format(totali) + " turni totali per tutti gli anni";
        log.info(message);

        for (EAFiltroAnno filtroAnno : EAFiltroAnno.values()) {
            lista = findAllByYear(croce, filtroAnno.get());
            for (Turno turno : lista) {
                fixDurataSingoloTurno(turno);
                fatti++;
            }
            message = "Elaborati " + text.format(fatti) + "/" + text.format(totali) + " turni dell'anno " + filtroAnno.get() + " in " + date.deltaText(inizio);
            log.info(message);
        }

    }// end of method


    /**
     * Patch per regolare la durata effettiva di TUTTI i turni <br>
     * Può essere lanciato anche più volte, senza problemi <br>
     */
    public void fixDurataSingoloTurno(Turno turno) {
        List<Iscrizione> lista = turno.iscrizioni;

        if (lista != null) {
            for (Iscrizione iscr : lista) {
                if (iscr.milite != null) {
                    if (iscr.inizio == null || iscr.fine == null) {
                        logger.warn("Mancano gli orari", this.getClass(), "fixDurataSingoloTurno");
                        return;
                    }
                    iscrizioneService.setDurataMinuti(iscr);
                }
            }
            save(turno);
        }
    }// end of method


    /**
     * Creazione di alcuni dati iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * La collezione (filtrata sulla croce) viene svuotata <br>
     * I dati possono essere presi da una Enumeration o creati direttamente <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse che cancella tutte le entities della croce <br>
     *
     * @return numero di elementi creati
     */
    @Override
    public int reset() {
        int numRec = super.reset();
        this.resetDemo();
        return numRec;
    }


    /**
     * Separo il reset della demo <br>
     * <p>
     * Un reset di Funzione non ha senso per le croci operative ma solo per la croce demo <br>
     * Le croci operative non hanno il bottone 'Reset' neanche per il developer e dunque non possono invocare il metodo reset <br>
     * Nel metodo reset si arriva quindi solo da dentro la croce demo;
     * si può quindi usare il metodo DeleteAll della superclasse di reset senza rischi;
     * poi si chiama questo metodo resetDemo() per la creazione dei dati <br>
     * Arrivando invece dalla TaskDemo, siamo in un thread separato e la croce non esiste <br>
     * Bypassiamo quindi reset() e chiamiamo direttamente resetDemo() in cui operiamo una
     * cancellazione selettiva della sola croce demo prima di costruire i dati <br>
     * <p>
     * Property ricavate dal CSV: servizio,iscrizioni,titoloExtra,localitaExtra <br>
     */
    public void resetDemo() {
        Croce croce = croceService.getDEMO();
        super.deleteByProperty(entityClass, "croce", croce);

        File funzioniCSV = new File("config" + File.separator + "turni");
        String path = funzioniCSV.getAbsolutePath();
        List<LinkedHashMap<String, String>> mappaCSV;
        String giornoTxt;
        LocalDate giorno = null;
        String servizioTxt;
        Servizio servizio;
        String funzioniMilitiTxt;
        List<Iscrizione> iscrizioni = null;
        String titoloExtra = VUOTA;
        String localitaExtra = VUOTA;
        Turno turno;

        mappaCSV = fileService.leggeMappaCSV(path);
        for (LinkedHashMap<String, String> riga : mappaCSV) {
            giornoTxt = riga.get("giorno");
            giorno = date.getGiornoDelta(giornoTxt);
            servizioTxt = riga.get("servizio");
            servizio = servizioService.findByKeyUnica(croce, servizioTxt);
            funzioniMilitiTxt = riga.get("iscritti");
            if (servizio != null) {
                iscrizioni = fixIscrizioni(croce, servizio, funzioniMilitiTxt);
            }
            if (iscrizioni != null) {
                try {
                    if (servizio.code.equals("extra")) {
                        creaIfNotExist(croce, giorno, servizio, iscrizioni, titoloExtra, localitaExtra);
                    } else {
                        creaIfNotExist(croce, giorno, servizio, iscrizioni);
                    }
                } catch (Exception unErrore) {
                    log.error(unErrore.getMessage());
                }
            }
        }

        loggerAdmin.reset("Turni della croce demo");
    }// end of method


    public List<Iscrizione> fixIscrizioni(Croce croce, Servizio servizio, String funzioniMilitiTxt) {
        List<Iscrizione> iscrizioni = getIscrizioni(servizio);
        Funzione funzione;
        Milite milite;
        String[] parti;
        String[] subParti;
        String funzioneTxt;
        String militeTxt;

        if (text.isEmpty(funzioniMilitiTxt)) {
            return iscrizioni;
        }

        parti = funzioniMilitiTxt.split(SLASH);
        if (parti != null && parti.length > 0) {
            for (String funzioneMilite : parti) {
                subParti = funzioneMilite.split(UGUALE);
                if (subParti != null && subParti.length == 2) {
                    funzioneTxt = subParti[0];
                    militeTxt = subParti[1];
                    funzione = funzioneService.findByKeyUnica(croce, funzioneTxt);
                    for (Iscrizione iscrizione : iscrizioni) {
                        if (iscrizione.getFunzione().code.equals(funzione.code)) {
                            milite = militeService.findByKeyUnica(militeTxt);
                            iscrizione.milite = milite;
                        }
                    }
                }
            }
        }

        return iscrizioni;
    }// end of method

}// end of class