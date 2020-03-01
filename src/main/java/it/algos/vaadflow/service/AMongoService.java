package it.algos.vaadflow.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.wrapper.AFiltro;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadflow.service.AService.FIELD_NAME_ORDINE;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 21-ago-2018
 * Time: 16:04
 * <p>
 * Gestione degli accessi al database MongoDB <br>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AMongoService.class); <br>
 * 2) AMongoService.getInstance(); <br>
 * 3) @Autowired private AMongoService mongoService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class AMongoService extends AbstractService {

    public final static int STANDARD_MONGO_MAX_BYTES = 33554432;

    public final static int EXPECTED_ALGOS_MAX_BYTES = 50151432;

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

//    /**
//     * Private final property
//     */
//    private static final AMongoService INSTANCE = new AMongoService();

    /**
     * Inietta da Spring
     */
//    @Autowired
    public MongoOperations mongoOp;
//    @Autowired
//    public MongoTemplate template;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AMongoService() {
    }// end of constructor


    @Autowired
    public AMongoService(MongoOperations mongoOp) {
        this.mongoOp = mongoOp;
    }// end of constructor


//    /**
//     * Gets the unique instance of this Singleton.
//     *
//     * @return the unique instance of this Singleton
//     */
//    public static AMongoService getInstance() {
//        return INSTANCE;
//    }// end of static method


//    /**
//     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
//     * Si usa un @Qualifier(), per avere la sottoclasse specifica
//     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
//     */
//    public AMongoService(MongoTemplate template, MongoOperations mongo) {
//        this.template = template;
//        this.mongo = mongo;
//    }// end of Spring constructor


//    /**
//     * Returns the number of entities available.
//     *
//     * @return the number of entities
//     */
//    public int count() {
//        return (int) repository.count();
//    }// end of method

//    //save user object into "user" collection / table
//    //class name will be used as collection name
//	mongoOperation.save(user);
//
//    //save user object into "tableA" collection
//	mongoOperation.save(user,"tableA");
//
//    //insert user object into "user" collection
//    //class name will be used as collection name
//	mongoOperation.insert(user);
//
//    //insert user object into "tableA" collection
//	mongoOperation.insert(user, "tableA");
//
//    //insert a list of user objects
//	mongoOperation.insert(listofUser);


    /**
     * Count all
     *
     * @param clazz della collezione
     *
     * @return num recs
     */
    public int count(Class<? extends AEntity> clazz) {
        return (int) mongoOp.count(new Query(), clazz);
    }// end of method


    /**
     * Find single entity
     *
     * @param clazz      della collezione
     * @param entityBean da cercare
     *
     * @return entity
     */
    public AEntity findByEntity(Class<? extends AEntity> clazz, AEntity entityBean) {
        return findById(clazz, entityBean.getId());
    }// end of method


    /**
     * Check the existence of a single entity
     *
     * @param clazz      della collezione
     * @param entityBean da cercare
     *
     * @return true if exist
     */
    public boolean isEsiste(Class<? extends AEntity> clazz, AEntity entityBean) {
        return findByEntity(clazz, entityBean) != null;
    }// end of method


    /**
     * Find single entity
     *
     * @param clazz della collezione
     * @param keyId chiave id
     *
     * @return entity
     */
    public AEntity findById(Class<? extends AEntity> clazz, String keyId) {
        AEntity entity = null;
        Object obj = null;

        if (text.isValid(keyId)) {
            obj = mongoOp.findById(keyId, clazz);
        }// end of if cycle

        if (obj != null && obj instanceof AEntity) {
            entity = (AEntity) obj;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Returns only the property of the type.
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return entity
     */
    public List<AEntity> findAllByProperty(Class<? extends AEntity> clazz, String property, Object value) {
        List<AEntity> lista = null;
        Query query = new Query();

        if (reflection.isNotEsiste(clazz, property)) {
            log.error("Algos - Manca la property " + property + " nella classe " + clazz.getSimpleName());
            return null;
        }// end of if cycle

        query.addCriteria(Criteria.where(property).is(value));
        lista = (List<AEntity>) mongoOp.find(query, clazz);

        return lista;
    }// end of method


    /**
     * Returns only the property of the type.
     *
     * @param clazz       della collezione
     * @param listaFiltri per le selezioni di filtro
     *
     * @return entity
     */
    public List<AEntity> findAllByProperty(Class<? extends AEntity> clazz, List<AFiltro> listaFiltri) {
        return findAllByProperty(clazz, listaFiltri, (Sort) null);
    }// end of method


    /**
     * Returns only the property of the type.
     *
     * @param clazz       della collezione
     * @param listaFiltri per le selezioni di filtro
     *
     * @return entity
     */
    public List<AEntity> findAllByProperty(Class<? extends AEntity> clazz, List<AFiltro> listaFiltri, Sort sortView) {
        List<AEntity> lista = null;
        String key = "_id";
        Query query = new Query();
        CriteriaDefinition criteria = null;
        Sort sort = null;

        if (listaFiltri != null && listaFiltri.size() > 0) {
            for (AFiltro filtro : listaFiltri) {
                criteria = filtro.getCriteria();
                if (criteria == null) {
                    criteria = Criteria.where(key).ne(VUOTA);
                }// end of if cycle

                if (sortView!=null) {
                    sort = sortView;
                } else {
                    if (filtro.getSort() != null) {
                        sort = filtro.getSort();
                    } else {
                        sort = new Sort(Sort.Direction.ASC, criteria.getKey());
                    }// end of if/else cycle
                }// end of if/else cycle

                query.addCriteria(criteria);
            }// end of for cycle

            if (sort != null) {
                query.with(sort);
            }// end of if cycle

            lista = (List<AEntity>) mongoOp.find(query, clazz);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Find single entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return entity
     */
    public AEntity findByProperty(Class<? extends AEntity> clazz, String property, Object value) {
        AEntity entity = null;
        List<AEntity> lista = findAllByProperty(clazz, property, value);

        if (lista != null && lista.size() == 1) {
            entity = lista.get(0);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Check the existence of a single entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return true if exist
     */
    public boolean isEsisteByProperty(Class<? extends AEntity> clazz, String property, Object value) {
        return findByProperty(clazz, property, value) != null;
    }// end of method


    /**
     * Returns only the property of the type.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @return all entities
     */
    public ArrayList findAllProperty(String property, Class<? extends AEntity> clazz) {
        ArrayList lista = null;

        Document projection = new Document(property, 1);
        Query query = new BasicQuery(new Document(), projection);
        lista = (ArrayList) mongoOp.find(query, clazz).stream().collect(Collectors.toList());

        return lista;
    }// end of method


    /**
     * Controlla l'esistenza della singola entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return true se esiste già
     */
    public boolean isEsiste(Class<? extends AEntity> clazz, String property, Object value) {
        return findByProperty(clazz, property, value) != null;
    }// end of method


    /**
     * Controlla l'esistenza della singola entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return true se manca
     */
    public boolean isManca(Class<? extends AEntity> clazz, String property, Object value) {
        return findByProperty(clazz, property, value) == null;
    }// end of method


    /**
     * Find all
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public List findAll(Class<? extends AEntity> clazz) {
        return new ArrayList(mongoOp.findAll(clazz));
    }// end of method


    /**
     * Insert single document into a collection.
     *
     * @param item  da inserire
     * @param clazz della collezione
     */
    public boolean insert(AEntity item, Class<? extends AEntity> clazz) {
        boolean status = false;
        String collectionName = annotation.getCollectionName(clazz);

        if (findByEntity(clazz, item) == null) {
            try { // prova ad eseguire il codice
                mongoOp.insert(item, collectionName);
                status = true;
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Insert single document into a collection.
     *
     * @param item           da inserire
     * @param collectionName della collezione
     */
    @Deprecated
    public boolean insert(AEntity item, String collectionName) {
        boolean status = false;

        try { // prova ad eseguire il codice
            mongoOp.insert(item, collectionName);
            status = true;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return status;
    }// end of method


    /**
     * Inserts multiple documents into a collection.
     *
     * @param lista di elementi da inserire
     * @param clazz della collezione
     */
    public void insert(List<? extends AEntity> lista, Class<? extends AEntity> clazz) {
        mongoOp.insert(lista, clazz);
    }// end of method


    /**
     * Update single document into a collection.
     *
     * @param entityBean da modificare
     * @param clazz      della collezione
     */
    public boolean update(AEntity entityBean, Class<? extends AEntity> clazz) {
        boolean status = false;
        AEntity entity = null;
        entity = findByEntity(clazz, entityBean);

        if (entity != null) {
            mongoOp.remove(entity);
        }// end of if cycle

        return insert(entityBean, clazz);
    }// end of method


    /**
     * Update multiple documents of a collection.
     * Spazzola la lista e cancella/insert ogni singola entity
     *
     * @param listaEntities di elementi da modificare
     * @param clazz         della collezione
     */
    public boolean updateBulk(List<? extends AEntity> listaEntities, Class<? extends AEntity> clazz) {
        boolean status = false;
        DeleteResult result;

        try { // prova ad eseguire il codice
            result = delete(listaEntities, clazz);
            insert(listaEntities, clazz);
            status = true;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return status;
    }// end of method


    /**
     * Delete a single entity.
     *
     * @param entityBean da cancellare
     *
     * @return lista
     */
    public DeleteResult delete(AEntity entityBean) {
        return mongoOp.remove(entityBean);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaEntities di elementi da cancellare
     * @param clazz         della collezione
     *
     * @return lista
     */
    public DeleteResult delete(List<? extends AEntity> listaEntities, Class<? extends AEntity> clazz) {
        ArrayList<String> listaId = new ArrayList<String>();

        for (AEntity entity : listaEntities) {
            if (entity != null) {
                listaId.add(entity.id);
            } else {
                log.error("Algos - Manca una entity in AMongoService.delete()");
            }// end of if/else cycle
        }// end of for cycle

        return deleteBulk(listaId, clazz);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaId di keyID da cancellare
     * @param clazz   della collezione
     *
     * @return lista
     */
    public DeleteResult deleteBulk(List<String> listaId, Class<? extends AEntity> clazz) {
        Bson condition = new Document("$in", listaId);
        Bson filter = new Document("_id", condition);
        return getCollection(clazz).deleteMany(filter);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param lista    di valori della property da cancellare
     * @param clazz    della collezione
     * @param property della Entity
     *
     * @return lista
     */
    public DeleteResult deleteBulkByProperty(List lista, Class<? extends AEntity> clazz, String property) {
        DeleteResult result;
        Bson condition = new Document("$in", lista);
        Bson filter = new Document(property, condition);
        result = getCollection(clazz).deleteMany(filter);

        return result;
    }// end of method


    /**
     * Delete a collection.
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public void drop(Class<? extends AEntity> clazz) {
        this.drop(getCollectionName(clazz));
    }// end of method


    /**
     * Delete a collection.
     *
     * @param collectionName della collezione
     *
     * @return lista
     */
    public void drop(String collectionName) {
        this.mongoOp.dropCollection(collectionName);
    }// end of method


    /**
     * Delete from a collection.
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     */
    public DeleteResult deleteByProperty(Class<? extends AEntity> clazz, String property, Object value) {
        Query searchQuery = new Query(Criteria.where(property).is(value));
        return this.mongoOp.remove(searchQuery, clazz);
    }// end of method


    /**
     * Nome (minuscolo) della collezione.
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public String getCollectionName(Class<? extends AEntity> clazz) {
        String collectionName = "";

        if (clazz != null) {
            collectionName = annotation.getCollectionName(clazz);
        }// end of if cycle

        return collectionName;
    }// end of method


    /**
     * Collezione
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public MongoCollection<Document> getCollection(Class<? extends AEntity> clazz) {
        return mongoOp.getCollection(getCollectionName(clazz));
    }// end of method


    /**
     * Collezione
     *
     * @param collectionName della collezione
     *
     * @return lista
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        return mongoOp.getCollection(collectionName);
    }// end of method


    /**
     * Recupera il valore di un parametro
     *
     * @param parameterName
     *
     * @return value of parameter
     */
    public Object getParameter(String parameterName) {
        Map<String, Object> mappa;
        MongoDatabase dbAdmin = getDBAdmin();
        Document param;

        mappa = new LinkedHashMap<>();
        mappa.put("getParameter", 1);
        mappa.put(parameterName, 1);

        param = dbAdmin.runCommand(new Document(mappa));
        return param.get(parameterName);
    }// end of method


    /**
     * Recupera il valore del parametro internalQueryExecMaxBlockingSortBytes <br>
     *
     * @return value of parameter
     */
    public int getMaxBlockingSortBytes() {
        int numBytes;
        String value = "";

        numBytes = (int) getParameter("internalQueryExecMaxBlockingSortBytes");
        value = text.format(numBytes);

        if (numBytes == AMongoService.STANDARD_MONGO_MAX_BYTES) {
            log.warn("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata col valore standard iniziale settato da mongoDB: " + value);
        } else {
            if (numBytes == AMongoService.EXPECTED_ALGOS_MAX_BYTES) {
                log.info("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata col valore richiesto da Algos: " + value);
            } else {
                log.warn("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata a cazzo: " + value);
            }// end of if/else cycle
        }// end of if/else cycle

        return numBytes;
    }// end of method


    /**
     * Regola il valore di un parametro
     *
     * @param parameterName
     * @param valueToSet    valore da regolare
     *
     * @return true se il valore è stato acquisito dal database
     */
    public boolean setParameter(String parameterName, Object valueToSet) {
        Map<String, Object> mappa;
        MongoDatabase dbAdmin = getDBAdmin();
        Document param;

        mappa = new LinkedHashMap<>();
        mappa.put("setParameter", 1);
        mappa.put(parameterName, valueToSet);

        param = dbAdmin.runCommand(new Document(mappa));
        return (double) param.get("ok") == 1;
    }// end of method


    /**
     * Regola il valore del parametro internalQueryExecMaxBlockingSortBytes
     *
     * @param maxBytes da regolare
     *
     * @return true se il valore è stato acquisito dal database
     */
    public boolean setMaxBlockingSortBytes(int maxBytes) {
        return setParameter("internalQueryExecMaxBlockingSortBytes", maxBytes);
    }// end of method


    /**
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param clazz della collezione
     */
    public int getNewOrdine(Class<? extends AEntity> clazz) {
        return getNewOrdine(clazz, FIELD_NAME_ORDINE);
    }// end of method


    /**
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param clazz        della collezione
     * @param propertyName dell'ordinamento
     */
    public int getNewOrdine(Class<? extends AEntity> clazz, String propertyName) {
        int ordine = 0;
        AEntity entityBean = null;
        Sort sort = new Sort(Sort.Direction.DESC, propertyName);
        ArrayList lista;
        Field field;
        Object value;

        if (!reflection.isEsiste(clazz, propertyName)) {
            return 0;
        }// end of if/else cycle

        Query query = new Query().with(sort).limit(1);
        lista = new ArrayList(mongoOp.find(query, clazz));

        if (array.isValid(lista) && lista.size() == 1) {
            entityBean = (AEntity) lista.get(0);
        }// end of if cycle

        if (entityBean != null) {
            field = reflection.getField(clazz, propertyName);
            try { // prova ad eseguire il codice
                value = field.get(entityBean);
                if (value instanceof Integer) {
                    ordine = (Integer) value;
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return ordine + 1;
    }// end of method


    /**
     * Restituisce una generica collection
     */
    public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        MongoCollection<Document> collection = null;
        MongoDatabase mongoDatabase = getDB(databaseName);

        if (mongoDatabase != null) {
            collection = getCollection(mongoDatabase, collectionName);
        }// end of if cycle

        return collection;
    }// end of method


    /**
     * Restituisce una generica collection
     */
    public MongoCollection<Document> getCollection(MongoDatabase mongoDatabase, String collectionName) {
        MongoCollection<Document> collection = null;

        if (mongoDatabase != null) {
            collection = mongoDatabase.getCollection(collectionName);
        }// end of if cycle

        return collection;
    }// end of method


    /**
     * Restituisce un generico database
     */
    public MongoDatabase getDB(String databaseName) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        return mongoClient.getDatabase(databaseName);
    }// end of method


    /**
     * Restituisce il database 'admin' di servizio, sempre presente in MongoDB
     */
    public MongoDatabase getDBAdmin() {
        return getDB("admin");
    }// end of method


    /**
     * Regola il valore iniziale del parametro internalQueryExecMaxBlockingSortBytes
     */
    public void fixMaxBytes() {
        this.setMaxBlockingSortBytes(EXPECTED_ALGOS_MAX_BYTES);
    }// end of method


    /**
     * Restituisce l'elenco delle collections di un database
     *
     * @param mongoDatabase da scandagliare
     */
    public List<String> listCollectionNames(MongoDatabase mongoDatabase) {
        List<String> lista = new ArrayList();
        MongoIterable<String> listaMongo = mongoDatabase.listCollectionNames();

        for (String nome : listaMongo) {
            lista.add(nome);
        }// end of for cycle

        return lista;
    }// end of method

}// end of class
