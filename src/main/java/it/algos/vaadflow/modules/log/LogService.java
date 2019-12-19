package it.algos.vaadflow.modules.log;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EALogAction;
import it.algos.vaadflow.enumeration.EALogLivello;
import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.logtype.Logtype;
import it.algos.vaadflow.modules.logtype.LogtypeService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.service.AConsoleColorService.RESET;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 21-set-2019 6.34.44 <br>
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
@Qualifier(TAG_LOG)
@Slf4j
@AIScript(sovrascrivibile = false)
public class LogService extends AService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    private final static String SORT_FIELD = "evento";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogtypeService logtype;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private LogRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public LogService(@Qualifier(TAG_LOG) MongoRepository repository) {
        super(repository);
        super.entityClass = Log.class;
        this.repository = (LogRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity e la registra <br>
     *
     * @param descrizione (obbligatoria, non unica) <br>
     *
     * @return la entity appena creata
     */
    public Log crea(String descrizione) {
        Log entity = newEntity(descrizione);
        save(entity);
        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param livello     rilevanza del log (obbligatorio)
     * @param logType     raggruppamento logico dei log per type di eventi (obbligatorio)
     * @param descrizione (obbligatoria, non unica) <br>
     *
     * @return la entity appena creata
     */
    public Log crea(EALogLivello livello, EALogType logType, String descrizione) {
        return crea(livello, logtype.findByKeyUnica(logType.getTag()), descrizione);
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param livello     rilevanza del log (obbligatorio)
     * @param type        raggruppamento logico dei log per type di eventi (obbligatorio)
     * @param descrizione (obbligatoria, non unica) <br>
     *
     * @return la entity appena creata
     */
    public Log crea(EALogLivello livello, Logtype type, String descrizione) {
        Log entity = newEntity(livello, type, descrizione);
        save(entity);
        return entity;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Log newEntity() {
        return newEntity((EALogLivello) null, (Logtype) null, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties obbligatorie <br>
     *
     * @param descrizione (obbligatoria, non unica) <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Log newEntity(String descrizione) {
        return newEntity((EALogLivello) null, (Logtype) null, descrizione);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param livello     rilevanza del log (obbligatorio)
     * @param type        raggruppamento logico dei log per type di eventi (obbligatorio)
     * @param descrizione (obbligatoria, non unica) <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Log newEntity(EALogLivello livello, Logtype type, String descrizione) {
        return Log.builderLog()
                .livello(livello != null ? livello : EALogLivello.info)
                .type(type != null ? type : logtype.getEdit())
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .evento(LocalDateTime.now())
                .build();
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        String code = "";
        Log log = ((Log) entityBean);

        code += log.getType().code;
        code += log.getEvento().toString();

        return code;
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Log entity = (Log) super.beforeSave(entityBean, operation);

        if (entity.livello == null || entity.getType() == null || text.isEmpty(entity.descrizione)) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param indirizzo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
//    public Log findByKeyUnica(String indirizzo) {
//        return repository.findByIndirizzo(indirizzo);
//    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'code', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'descrizione', ordinate secondo questa property <br>
     * Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<? extends AEntity> findAll() {
        return repository.findAll();
    }// end of method


    public ArrayList<Log> findAllByLivello(EALogLivello livello) {
        ArrayList<Log> items = null;
        Query query = new Query();
        Sort sort = new Sort(Sort.Direction.DESC, SORT_FIELD);
        query.with(sort);
        String livelloField = "livello";

        if (livello != null) {
            switch (livello) {
                case debug:
                    query.addCriteria(Criteria.where(livelloField).is(EALogLivello.debug));
                    break;
                case info:
                    query.addCriteria(Criteria.where(livelloField).is(EALogLivello.info));
                    break;
                case warn:
                    query.addCriteria(Criteria.where(livelloField).is(EALogLivello.warn));
                    break;
                case error:
                    query.addCriteria(Criteria.where(livelloField).is(EALogLivello.error));
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return (ArrayList) mongo.mongoOp.find(query, Log.class);
    }// end of method


    /**
     * Gestisce un log di debug <br>
     *
     * @param descrizione della informazione da gestire
     */
    public void debug(String descrizione) {
        debug(descrizione, null, VUOTA);
    }// end of method


    /**
     * Gestisce un log di debug <br>
     *
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void debug(String descrizione, Class clazz, String methodName) {
        esegue(EALogLivello.debug, descrizione, clazz, methodName);
    }// fine del metodo


    /**
     * Gestisce un log di info <br>
     *
     * @param descrizione della informazione da gestire
     */
    public void info(String descrizione) {
        info(descrizione, null, VUOTA);
    }// end of method


    /**
     * Gestisce un log di info <br>
     *
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void info(String descrizione, Class clazz, String methodName) {
        esegue(EALogLivello.info, descrizione, clazz, methodName);
    }// fine del metodo


    /**
     * Gestisce un log di warning <br>
     *
     * @param descrizione della informazione da gestire
     */
    public void warn(String descrizione) {
        warn(descrizione, null, VUOTA);
    }// end of method


    /**
     * Gestisce un log di warning <br>
     *
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void warn(String descrizione, Class clazz, String methodName) {
        esegue(EALogLivello.warn, descrizione, clazz, methodName);
    }// fine del metodo


    /**
     * Gestisce un log di error <br>
     *
     * @param descrizione della informazione da gestire
     */
    public void error(String descrizione) {
        error(descrizione, null, VUOTA);
    }// end of method


    /**
     * Gestisce un log di error <br>
     *
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void error(String descrizione, Class clazz, String methodName) {
        esegue(EALogLivello.error, descrizione, clazz, methodName);
    }// fine del metodo


    /**
     * Gestisce un log, con le modalità fissate nelle preferenze <br>
     *
     * @param logLevel    del messaggio
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void esegue(EALogLivello logLevel, String descrizione, Class clazz, String methodName) {
        esegue(EALogAction.get(pref), logLevel, descrizione, clazz, methodName);
    }// fine del metodo


    /**
     * Gestisce un log <br>
     *
     * @param logAction   del messaggio
     * @param logLevel    del messaggio
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void esegue(EALogAction logAction, EALogLivello logLevel, String descrizione, Class clazz, String methodName) {
        switch (logAction) {
            case nessuno:
                break;
            case collectionMongo:
                crea(logLevel, EALogType.debug, descrizione);
                break;
            case sendMail:
                sendMail(logLevel, descrizione, clazz, methodName);
                break;
            case terminale:
                sendTerminale(logLevel, descrizione, clazz, methodName);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement
    }// fine del metodo


    //--registra un avviso
    public void importo(String descrizione) {
        crea(EALogLivello.debug, logtype.getImport(), descrizione);
    }// fine del metodo


    /**
     * Elabora il log da inviare via mail <br>
     *
     * @param logLevel    del messaggio
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void sendMail(EALogLivello logLevel, String descrizione, Class clazz, String methodName) {
        String messaggio = VUOTA;
        String adesso = date.get();
        messaggio += adesso;
        String appName = FlowVar.projectName;

        if (logLevel != null) {
            messaggio += A_CAPO;
            messaggio += text.primaMaiuscola(logLevel.name());
        }// end of if cycle

        if (text.isValid(appName)) {
            messaggio += A_CAPO;
            messaggio += "App: " + appName;
        }// end of if cycle

        if (clazz != null) {
            messaggio += A_CAPO;
            messaggio += "Class: " + clazz.getSimpleName();
        }// end of if cycle

        if (text.isValid(methodName)) {
            messaggio += A_CAPO;
            messaggio += "Method: " + methodName;
        }// end of if cycle

        if (text.isValid(logLevel)) {
            messaggio += A_CAPO;
            messaggio += "Level: " + logLevel;
        }// end of if cycle

        messaggio += A_CAPO;
        messaggio += "Message: " + descrizione;
        mail.send(appName, messaggio);
    }// fine del metodo


    /**
     * Elabora il log da presentare a terminale <br>
     *
     * @param logLevel    del messaggio
     * @param descrizione della informazione da gestire
     * @param clazz       di provenienza della richiesta
     * @param methodName  di provenienza della richiesta
     */
    public void sendTerminale(EALogLivello logLevel, String descrizione, Class clazz, String methodName) {
        String messaggio = VUOTA;
        String adesso = date.get();
        String sep = SEP;
        sep = SPAZIO;
        String appName = FlowVar.projectName;

        if (logLevel == null) {
            return;
        } else {
            messaggio += logLevel.color + logLevel.name().toUpperCase() + RESET;
        }// end of if/else cycle

        if (text.isValid(appName)) {
            messaggio += sep;
            messaggio += logLevel.color + "(App)" + RESET;
            messaggio += appName;
        }// end of if cycle

        if (clazz != null) {
            messaggio += sep;
            messaggio += logLevel.color + "(Class)" + RESET;
            messaggio += clazz.getSimpleName();
        }// end of if cycle

        if (text.isValid(methodName)) {
            messaggio += sep;
            messaggio += logLevel.color + "(Method)" + RESET;
            messaggio += methodName;
        }// end of if cycle

        messaggio += sep;
        messaggio += logLevel.color + "(Message)" + RESET;
        messaggio += descrizione;
        System.out.println(messaggio);
    }// fine del metodo


}// end of class