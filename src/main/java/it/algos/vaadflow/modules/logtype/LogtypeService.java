package it.algos.vaadflow.modules.logtype;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 26-ott-2018 9.59.58 <br>
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
@Qualifier(TAG_TYP)
@Slf4j
@AIScript(sovrascrivibile = false)
public class LogtypeService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public LogtypeRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public LogtypeService(@Qualifier(TAG_TYP) MongoRepository repository) {
        super(repository);
        super.entityClass = Logtype.class;
        this.repository = (LogtypeRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param code codice di riferimento (obbligatorio)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(String code) {
        boolean creata = false;

        if (isMancaByKeyUnica(code)) {
            AEntity entity = save(newEntity(0, code));
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
    public Logtype newEntity() {
        return newEntity(0, "");
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
    public Logtype newEntity(int ordine, String code) {
        return Logtype.builderLogtype()
                .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                .code(text.isValid(code) ? code : null)
                .build();
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Logtype) entityBean).getCode();
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
        Logtype entity = (Logtype) super.beforeSave(entityBean, operation);

        if (text.isEmpty(entity.code)) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Logtype findByKeyUnica(String code) {
        return repository.findByCode(code);
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

        for (EALogType type : EALogType.values()) {
            numRec = creaIfNotExist(type.getTag()) ? numRec + 1 : numRec;
        }// end of for cycle

        return numRec;
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getSetup() {
        return repository.findByCode(LOG_SETUP);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getNew() {
        return repository.findByCode(LOG_NEW);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity modificata)
     *
     * @return la entity appena trovata
     */
    public Logtype getEdit() {
        return repository.findByCode(LOG_EDIT);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity cancellata)
     *
     * @return la entity appena trovata
     */
    public Logtype getDelete() {
        return repository.findByCode(LOG_DELETE);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (import di dati)
     *
     * @return la entity appena trovata
     */
    public Logtype getImport() {
        return repository.findByCode(LOG_IMPORT);
    }// end of method

}// end of class