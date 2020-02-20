package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.migration.MigrationService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
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
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_TUR)
@Slf4j
@AIScript(sovrascrivibile = false)
public class TurnoService extends WamService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


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
    private MigrationService migration;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private TurnoRepository repository;


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

        super.lastImport = LAST_IMPORT_TURNI;
        super.durataLastImport = DURATA_IMPORT_TURNI;
        super.eaTempoTypeImport = EATempo.secondi;
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
    public Turno newEntity(LocalDate giorno, Servizio servizio) {
        return newEntity((Croce) null, giorno, servizio, (LocalTime) null, (LocalTime) null, (List<Iscrizione>) null, "", "");
    }// end of method


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
    public Turno newEntity(
            Croce croce,
            LocalDate giorno,
            Servizio servizio,
            LocalTime inizio,
            LocalTime fine,
            List<Iscrizione> iscrizioni,
            String titoloExtra,
            String localitaExtra) {

        Turno entity = Turno.builderTurno()
                .giorno(giorno != null ? giorno : LocalDate.now())
                .servizio(servizio)
                .inizio(inizio != null ? inizio : servizio != null ? servizio.inizio : LocalTime.MIDNIGHT)
                .fine(fine != null ? fine : servizio != null ? servizio.fine : LocalTime.MIDNIGHT)
                .iscrizioni(iscrizioni != null ? iscrizioni : addIscrizioni(servizio))
                .titoloExtra(titoloExtra.equals("") ? null : titoloExtra)
                .localitaExtra(localitaExtra.equals("") ? null : localitaExtra)
                .build();

        return (Turno) addCroce(entity, croce);
    }// end of method


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

//        --property (ridondante) calcolata
//        entity.durataEffettiva = getDurata(entity);

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
        String keyID = "";
        int day = giorno.getDayOfYear();
        String codeServizio = "";

        if (servizio != null) {
            codeServizio = servizio.code;
        }// end of if cycle

        keyID += day;
        keyID += codeServizio;

        return keyID;
    }// end of method


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
    public void importa(Croce croce) {
        long inizio = System.currentTimeMillis();
        migration.importTurni(croce);
        setLastImport(croce, inizio);
    }// end of method


//    /**
//     * Importazione di dati <br>
//     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
//     *
//     * @param croce di riferimento
//     *
//     * @return true se sono stati importati correttamente
//     */
//    @Override
//    public ImportResult importa(Croce croce) {
//        boolean status;
//        super.importa();
//        status = migration.importTurni((Croce) croce);
//
////        if (status) {
//        pref.saveValue(LAST_IMPORT_TURNI, LocalDateTime.now());
////        }// end of if cycle
//
//        return null;
//    }// end of method


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
        VaadinSession vaadSession = UI.getCurrent().getSession();

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
     * Returns all instances of the selected Croce <br>
     *
     * @return lista ordinata di tutte le entities della croce
     */
    public List<Turno> findAllByYear(Croce croce, int anno) {
        LocalDate inizio = date.primoGennaio(anno);
        LocalDate fine = date.trentunDicembre(anno);

        return repository.findAllByCroceAndGiornoBetweenOrderByGiornoAsc(croce, inizio, fine);
    }// end of method


    /**
     * Returns all instances of the selected Croce <br>
     *
     * @return lista ordinata di tutte le entities della croce
     */
    public List<Turno> findAllByYearUntilNow(Croce croce, int anno) {
        LocalDate inizio = date.primoGennaio(anno);
        LocalDate fine = LocalDate.now();

        return repository.findAllByCroceAndGiornoBetweenOrderByGiornoAsc(croce, inizio, fine);
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
     * Con la funzione e senza milite
     */
    public List<Iscrizione> addIscrizioni(Servizio servizio) {
        List<Iscrizione> items = null;
        Set<Funzione> funzioni = null;
        int durata = 0;

        if (servizio != null) {
            items = new ArrayList<>();
            funzioni = servizio.getFunzioni();
            durata = servizioService.getDurata(servizio);
        }// end of if cycle

        if (funzioni != null) {
            for (Funzione funz : funzioni) {
                items.add(Iscrizione.builderIscrizione().funzione(funz).durataEffettiva(durata).build());
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
        Set<Funzione> funzioni = servizio.getFunzioni();
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
     * Turno valido se tutte le funzioni obbligatorie hanno un milite segnato <br>
     *
     * @param turno di riferimento
     *
     * @return true se valido
     */
    public boolean isValido(Turno turno) {
        boolean turnoValido = true;
        boolean funzValida = false;
        Servizio servizio = null;
        servizio = turno.getServizio();
        Set<Funzione> funzioni = servizio.getFunzioni();

        for (Funzione funz : funzioni) {
            if (funz.obbligatoria) {
                funzValida = iscrizioneService.isValida(turno, funz);
            } else {
                funzValida = true;
            }// end of if/else cycle

            turnoValido = turnoValido ? funzValida : turnoValido;
        }// end of for cycle

        return turnoValido;
    }// end of method

}// end of class