package it.algos.vaadflow.modules.giorno;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.mese.Mese;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 18.39.35 <br>
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
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_GIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class GiornoService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public GiornoRepository repository;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private ADateService dateService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private MeseService meseService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public GiornoService(@Qualifier(TAG_GIO) MongoRepository repository) {
        super(repository);
        super.entityClass = Giorno.class;
        this.repository = (GiornoRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param titolo      (obbligatorio, unico)
     * @param mese        di riferimento (obbligatorio)
     * @param ordinamento (obbligatorio, unico)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(String titolo, Mese mese, int ordinamento) {
        boolean creata = false;

        if (isMancaByKeyUnica(titolo)) {
            AEntity entity = save(newEntity(titolo, mese, ordinamento));
            creata = entity != null;
        }// end of if cycle

        return creata;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Giorno newEntity() {
        return newEntity("", (Mese) null, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param titolo (obbligatorio, unico)
     * @param mese   di riferimento (obbligatorio)
     * @param ordine (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Giorno newEntity(String titolo, Mese mese, int ordine) {
        return Giorno.builderGiorno()
                .titolo(text.isValid(titolo) ? titolo : null)
                .mese(mese)
                .ordine(ordine > 0 ? ordine : getNewOrdine())
                .build();
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Giorno) entityBean).getTitolo();
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Giorno entity = (Giorno) super.beforeSave(entityBean, operation);

        if (entity.getMese() == null || entity.ordine == 0 || text.isEmpty(entity.titolo)) {
            entity = null;
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
    public Giorno findById(String id) {
        return (Giorno) super.findById(id);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titolo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Giorno findByKeyUnica(String titolo) {
        return repository.findByTitolo(titolo);
    }// end of method


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    public ArrayList<Giorno> findAll() {
        return (ArrayList) repository.findAllByOrderByOrdineAsc();
    }// end of method


    public List<Giorno> findAllByMese(Mese mese) {
        Query query = new Query();
        String meseField = "mese";

        if (mese != null) {
            query.addCriteria(Criteria.where(meseField).is(mese));
        }// end of if cycle

        return mongo.mongoOp.find(query, Giorno.class);
    }// end of method


    /**
     * Controlla l'esistenza di una Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titolo (obbligatorio, unico)
     *
     * @return true se trovata
     */
    public boolean isEsiste(String titolo) {
        return findByKeyUnica(titolo) != null;
    }// end of method


    /**
     * Creazione di alcuni dati demo iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * La collezione viene svuotata <br>
     * I dati possono essere presi da una Enumeration o creati direttamemte <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @return numero di elementi creato
     */
    @Override
    public int reset() {
        int numRec = super.reset();
        int ordine = 0;
        Giorno entity;
        List<HashMap> lista;
        String titolo;
        int bisestile;
        Mese mese;

        //costruisce i 366 records
        lista = dateService.getAllGiorni();
        for (HashMap mappaGiorno : lista) {
            titolo = (String) mappaGiorno.get(KEY_MAPPA_GIORNI_TITOLO);
            bisestile = (int) mappaGiorno.get(KEY_MAPPA_GIORNI_BISESTILE);
            mese = meseService.findByKeyUnica((String) mappaGiorno.get(KEY_MAPPA_GIORNI_MESE_TESTO));
            ordine = (int) mappaGiorno.get(KEY_MAPPA_GIORNI_NORMALE);
            numRec = creaIfNotExist(titolo, mese, ordine) ? numRec + 1 : numRec;
        }// end of for cycle

        return numRec;
    }// end of method

}// end of class