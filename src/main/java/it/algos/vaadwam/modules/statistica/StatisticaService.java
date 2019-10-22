package it.algos.vaadwam.modules.statistica;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_STA;

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
@Service
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
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public StatisticaRepository repository;

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
        return (Statistica) save(newEntity((Croce) null, 0, milite, null, 0, false, 0, 0));
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
        return newEntity((Croce) null, 0, null, null, 0, false, 0, 0);
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
    public Statistica newEntity(Croce croce, int ordine, Milite milite, LocalDate last, int delta, boolean valido, int turni, int ore) {
        Statistica entity = null;

        entity = Statistica.builderStatistica()
                .ordine(ordine != 0 ? ordine : this.getNewOrdine())
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
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Statistica> findAllCroci() {
        return repository.findAll();
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Statistica> findAllByCroce(Croce croce) {
        return repository.findAllByCroceOrderByOrdineAsc(croce);
    }// end of method


    public void elabora() {
        super.fixWamLogin();
        if (wamLogin.isDeveloper()) {
            for (Croce croce : croceService.findAll()) {
                elabora(croce);
            }// end of for cycle
        } else {
            elabora(wamLogin.getCroce());
        }// end of if/else cycle
    }// end of method


    public void elabora(Croce croce) {
        List<Milite> militi;
        deleteAllCroce(croce);

        militi = militeService.findAllByCroce(croce);
        if (array.isValid(militi)) {
            for (Milite milite : militi) {
                elaboraSingoloMilite(croce, milite);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    public void elaboraSingoloMilite(Croce croce, Milite milite) {
        List<Turno> listaTurniCroce = turnoService.findAllByYear(2019);
        Milite militeIscritto;
        Turno turno;
        List<Turno> listaTurniMilite = new ArrayList<>();
        int turni = 0;
        int oreTotali = 0;
        LocalDate last = null;
        int delta = 0;
        boolean valido;

        for (int j = 0; j < listaTurniCroce.size(); j++) {
            turno = listaTurniCroce.get(j);
            for (int k = 0; k < turno.iscrizioni.size(); k++) {
                if (turno.iscrizioni != null && turno.iscrizioni.get(k) != null && turno.iscrizioni.get(k).milite != null) {
                    militeIscritto = turno.iscrizioni.get(k).milite;
                    if (militeIscritto.id.equals(milite.id)) {
                        listaTurniMilite.add(turno);
                        oreTotali += listaTurniMilite.size() * 7;//@todo no buono
                        last = turno.giorno;
                        delta = date.differenza(LocalDate.now(), last);
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of for cycle

        turni = listaTurniMilite.size();
        valido = checkValidita(turni);

        if (turni > 0) {
            Statistica statistica = newEntity(croce, 0, milite, last, delta, valido, turni, oreTotali);
            save(statistica);
        }// end of if cycle
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

        return numTurni > turniRichiesti;
    }// end of method

}// end of class