package it.algos.vaadflow.service;

import com.mongodb.client.result.DeleteResult;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.data.FlowData;
import it.algos.vaadflow.backend.entity.ACEntity;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.ui.dialog.AViewDialog.DURATA;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:36
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class AService extends AbstractService implements IAService {


    public final static String FIELD_NAME_ID = "id";

    public final static String FIELD_NAME_ORDINE = "ordine";

    public final static String FIELD_NAME_CODE = "code";

    public final static String FIELD_NAME_DESCRIZIONE = "descrizione";

    public final static String FIELD_NAME_COMPANY = "company";

    private final static int SIZE = 500;

    //--il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
    public Class<? extends AEntity> entityClass;

    /**
     * Inietta da Spring
     */
    @Autowired
    public AMongoService mongo;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected FlowData flow;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;

    //--la repository dei dati viene iniettata dal costruttore della sottoclasse concreta
    protected MongoRepository repository;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;


    /**
     * Default constructor
     */
    public AService() {
    }// end of constructor


    /**
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public AService(MongoRepository repository) {
        this.repository = repository;
    }// end of Spring constructor


    @Override
    public AAnnotationService getAnnotationService() {
        return annotation;
    }// end of method


    //    @Override
    public AFieldService getFieldService() {
        return field;
    }// end of method


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Override
    public int count() {
        return mongo.count(entityClass);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param keyUnica (obbligatoria, unica)
     *
     * @return istanza della Entity, null se non trovata
     */
    protected AEntity findByKeyUnica(String keyUnica) {
        return null;
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
    public AEntity findById(String id) {
        AEntity entityBean = null;
        Object genericObj = null;
        Optional optional = repository.findById(id.trim());

        if (optional.isPresent()) {
            genericObj = optional.get();
            if (genericObj instanceof AEntity) {
                entityBean = (AEntity) genericObj;
            }// end of if cycle
        }// end of if cycle

        return entityBean;
    }// end of method


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
        List<? extends AEntity> lista = null;
        String sortName = "";
        Sort sort;

        if (entityClass == null) {
            return null;
        }// end of if cycle

        if (reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
            sortName = FIELD_NAME_ORDINE;
        } else {
            if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                sortName = FIELD_NAME_CODE;
            } else {
                if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                    sortName = FIELD_NAME_DESCRIZIONE;
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        if (text.isValid(sortName)) {
            sort = new Sort(Sort.Direction.ASC, sortName);
            lista = findAll(sort);
        } else {
            lista = findAll((Sort) null);
        }// end of if/else cycle

        return lista;
    }// end of method

    @Override
    public List<? extends AEntity> findAllAll() {
        return null;
    }// end of method

    @Override
    public List<? extends AEntity> findAllByCompany(Company company) {
        List<? extends AEntity> lista = null;
        String sortName = "";
        Sort sort;

        if (entityClass == null) {
            return null;
        }// end of if cycle
        if (!ACEntity.class.isAssignableFrom(entityClass)) {
            return null;
        }// end of if cycle

        if (reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
            sortName = FIELD_NAME_ORDINE;
        } else {
            if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                sortName = FIELD_NAME_CODE;
            } else {
                if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                    sortName = FIELD_NAME_DESCRIZIONE;
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

//        if (text.isValid(sortName)) {
//            sort = new Sort(Sort.Direction.ASC, sortName);
//            lista = new ArrayList<>(repository.findAll(sort));
//        } else {
//            lista = new ArrayList<>(repository.findAll());
//        }// end of if/else cycle

        lista = mongo.findAllByProperty(entityClass, "company",company);



        return lista;
    }// end of method


    /**
     * Returns only entities of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     * <p>
     * Methods of this library return Iterable<T>, while the rest of my code expects Collection<T>
     * L'annotation standard di JPA prevede un ritorno di tipo Iterable, mentre noi usiamo List
     * Eseguo qui la conversione, che rimane trasparente al resto del programma
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     *
     * @return all entities
     */
    @Override
    public List<? extends AEntity> findAll(int offset, int size) {
        return findAll(offset, size, (Sort) null);
    }// end of method


    /**
     * Returns only entities of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     * @param sort   ordinamento degli elementi
     *
     * @return all entities
     */
    @Override
    public ArrayList<? extends AEntity> findAll(int offset, int size, Sort sort) {
        Pageable page;

        if (sort != null) {
            page = PageRequest.of(offset, size, sort);
        } else {
            page = PageRequest.of(offset, size);
        }// end of if/else cycle

        return new ArrayList(repository.findAll(page).getContent());
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
        return mongo.findAllProperty(property, clazz);
    }// end of method


    /**
     * Returns ids list of all entities of the type.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @return all entities
     */
    public ArrayList<String> findAllIds() {
        ArrayList<String> lista = null;
        int cicli = array.numCicli(count(), SIZE);
        Sort sort = new Sort(Sort.Direction.ASC, "_id");

        for (int k = 0; k < cicli; k++) {
            lista = array.somma(lista, findAllIds(k, SIZE, sort));
        }// end of for cycle

        return lista;
    }// end of method


    /**
     * Returns ids list of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     * @param sort   ordinamento degli elementi
     *
     * @return all entities
     */
    public ArrayList<String> findAllIds(int offset, int size, Sort sort) {
        ArrayList<String> lista = null;
        ArrayList<? extends AEntity> listaEntities = findAll(offset, size, sort);

        if (array.isValid(listaEntities)) {
            lista = new ArrayList<>();
            for (AEntity entity : listaEntities) {
                lista.add(entity.id);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Ordinate secondo l'ordinamento previsto
     *
     * @param sort ordinamento previsto
     *
     * @return all ordered entities
     */
    protected ArrayList<? extends AEntity> findAll(Sort sort) {
        ArrayList<? extends AEntity> lista = null;

        try { // prova ad eseguire il codice
            if (sort != null) {
                lista = new ArrayList<>(repository.findAll(sort));
            } else {
                lista = new ArrayList<>(repository.findAll());
            }// end of if/else cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString() + " in AService.findAll(Sort sort)");
        }// fine del blocco try-catch

        return lista;
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
    @Deprecated
    public List<? extends AEntity> findFilter(String filter) {
        List<? extends AEntity> lista = null;
        String normalizedFilter = filter.toLowerCase();
        boolean entityUsaCompanyObbligatoria = annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria;
        boolean entityUsaCompanyFacoltativa = annotation.getCompanyRequired(entityClass) == EACompanyRequired.facoltativa;
        boolean mancaCompany = mancaCompanyNecessaria();

        if (mancaCompany) {
            return lista;
        }// end of if cycle

        if (entityUsaCompanyObbligatoria || entityUsaCompanyFacoltativa) {
            if (entityUsaCompanyObbligatoria) {
                lista = findAll();
                lista = lista.stream()
                        .filter(entity -> {
                            if (isEsisteEntityKeyUnica(entity)) {
                                return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
                            } else {
                                if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                                    return ((String) reflection.getPropertyValue(entity, FIELD_NAME_CODE)).startsWith(normalizedFilter);
                                } else {
                                    if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_DESCRIZIONE)).startsWith(normalizedFilter);
                                    } else {
                                        return true;
                                    }// end of if/else cycle
                                }// end of if/else cycle
                            }// end of if/else cycle
                        })
                        .collect(Collectors.toList());
            } else {
                lista = findAll();
                if (lista != null) {
                    lista = lista.stream()
                            .filter(entity -> {
                                boolean status = true;
                                return status;
                            })
                            .filter(entity -> {
                                if (isEsisteEntityKeyUnica(entity)) {
                                    return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
                                } else {
                                    if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_CODE)).startsWith(normalizedFilter);
                                    } else {
                                        if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                                            return ((String) reflection.getPropertyValue(entity, FIELD_NAME_DESCRIZIONE)).startsWith(normalizedFilter);
                                        } else {
                                            return true;
                                        }// end of if/else cycle
                                    }// end of if/else cycle
                                }// end of if/else cycle
                            })
                            .collect(Collectors.toList());
                }// end of if cycle
            }// end of if/else cycle
        } else {
            lista = findAll();
            if (lista != null) {
                lista = lista.stream()
                        .filter(entity -> {
                            if (isEsisteEntityKeyUnica(entity)) {
                                return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
                            } else {
                                if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                                    if (reflection.getPropertyValue(entity, FIELD_NAME_CODE) == null) {
                                        return true;
                                    } else {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_CODE)).startsWith(normalizedFilter);
                                    }// end of if/else cycle
                                } else {
                                    if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_DESCRIZIONE)).startsWith(normalizedFilter);
                                    } else {
                                        return true;
                                    }// end of if/else cycle
                                }// end of if/else cycle
                            }// end of if/else cycle
                        })
                        .collect(Collectors.toList());
            }// end of if cycle
        }// end of if/else cycle

        return lista;
    }// end of method


    private Predicate<? extends AEntity> getPredicate(String normalizedFilter) {
        return entity -> getKeyUnica(entity).toLowerCase().contains(normalizedFilter);
    }


    public boolean mancaCompanyNecessaria() {
        boolean status = false;
        boolean entityUsaCompany = annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria;

        return status;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     * todo ancora da sviluppare
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList(AContext context) {
        List<String> lista = annotation.getGridPropertiesName(entityClass);

        if (lista.contains(FIELD_NAME_COMPANY) && !context.getLogin().isDeveloper()) {
            lista.remove(FIELD_NAME_COMPANY);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * todo ancora da sviluppare
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getFormPropertyNamesList(AContext context) {
        ArrayList<String> lista = annotation.getFormPropertiesName(entityClass);

        if (lista.contains(FIELD_NAME_COMPANY) && !context.getLogin().isDeveloper()) {
            lista.remove(FIELD_NAME_COMPANY);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Search nell'ordine:
     * 1) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getSearchPropertyNamesList(AContext context) {
        return getFormPropertyNamesList(context);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public AEntity newEntity() {
        return null;
    }// end of method


    /**
     * Se la nuova entity usa la company, la recupera dal login
     * Se la campany manca, lancia l'eccezione
     *
     * @param entityBean da creare
     */
    protected AEntity addCompanySeManca(AEntity entityBean) {
        EACompanyRequired tableCompanyRequired;
        Company company = null;

        //--se la EntityClass non estende ACEntity, non deve fare nulla
        if ((entityBean instanceof ACEntity)) {
            company = ((ACEntity) entityBean).company;
        } else {
            return entityBean;
        }// end of if/else cycle

        if (company == null) {
            company = this.getCompany();
        }// end of if cycle

        //--controlla l'obbligatorietà della Company
        tableCompanyRequired = annotation.getCompanyRequired(entityBean.getClass());
        switch (tableCompanyRequired) {
            case nonUsata:
                log.error("C'è una discrepanza tra 'extends ACEntity' della classe " + entityBean.getClass().getSimpleName() + " e l'annotation @AIEntity della classe stessa");
                break;
            case facoltativa:
                if (company != null) {
                    ((ACEntity) entityBean).company = company;
                } else {
                    log.warn("Algos- Nuova scheda senza company (facoltativa) di " + entityBean.toString() + " della classe " + entityBean.getClass().getSimpleName());
                }// end of if/else cycle
                break;
            case obbligatoria:
                if (company != null) {
                    ((ACEntity) entityBean).company = company;
                } else {
                    log.error("Algos- Manca la company (obbligatoria) di " + entityBean.toString() + " della classe " + entityBean.getClass().getSimpleName());
                }// end of if/else cycle
                break;
            default:
                break;
        } // end of switch statement

        return entityBean;
    }// end of method


    public boolean usaCompany() {
        boolean status = false;
        EACompanyRequired tableCompanyRequired = null;

        //--se l'applicazione non è multiCompany, non deve far nulla
        if (FlowVar.usaCompany) {
            //--se la EntityClass non estende ACCompany, non deve fare nulla
            tableCompanyRequired = annotation.getCompanyRequired(entityClass);
            status = tableCompanyRequired == EACompanyRequired.obbligatoria || tableCompanyRequired == EACompanyRequired.facoltativa;
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     * <p>
     * Controlla se l'applicazione usa le company2 - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company2: può essere
     * a)EACompanyRequired.nonUsata
     * b)EACompanyRequired.facoltativa
     * c)EACompanyRequired.obbligatoria
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    @Override
    public AEntity save(AEntity entityBean) {
        AEntity entityValida = beforeSave(entityBean, EAOperation.edit);

        if (entityValida != null) {
            entityValida = save(null, entityValida);
        } else {
            if (UI.getCurrent() != null) {
                Notification.show("La scheda non è completa", DURATA, Notification.Position.BOTTOM_START);
            }// end of if cycle
            log.error("Algos - La scheda " + entityBean.toString() + " di " + entityBean.getClass().getSimpleName() + " non è completa");
        }// end of if/else cycle

        return entityValida;
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
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        return creaIdKeySpecifica(entityBean);
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param oldBean      previus state
     * @param modifiedBean to be saved
     *
     * @return the saved entity
     */
    @Override
    public AEntity save(AEntity oldBean, AEntity modifiedBean) {
        AEntity savedBean = null;
        Object obj = null;

        //--opportunità di usare una idKey specifica
        if (text.isEmpty(modifiedBean.id)) {
            creaIdKeySpecifica(modifiedBean);
        }// end of if cycle

        //--controlla l'integrità dei dati, elaborati dalle sottoclassi nei metodi beforeSave()
        if (modifiedBean.id == null || !modifiedBean.id.equals(FlowCost.STOP_SAVE)) {
            try { // prova ad eseguire il codice
                obj = repository.save(modifiedBean);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                //Notification.show("La scheda non è stata registrata", DURATA, Notification.Position.BOTTOM_START);
            }// fine del blocco try-catch
        }// end of if cycle

        if (obj != null && obj instanceof AEntity) {
            savedBean = (AEntity) obj;
        }// end of if cycle

        return savedBean;
    }// end of method


    /**
     * Proviene da Lista (quasi sempre)
     * Primo ingresso dopo il click sul bottone <br>
     */
    public AEntity save(AEntity entityBean, EAOperation operation) {
        AEntity entitySaved = null;
        entityBean = this.beforeSave(entityBean, operation);
        switch (operation) {
            case addNew:
                if (this.isEsisteEntityKeyUnica(entityBean)) {
                    try { // prova ad eseguire il codice
                        Notification.show(entityBean + " non è stata registrata, perché esisteva già con lo stesso code ", 3000, Notification.Position.BOTTOM_START);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.info(unErrore.toString());
                    }// fine del blocco try-catch
                } else {
                    entitySaved = this.save(entityBean);
                    try { // prova ad eseguire il codice
                        Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.info(unErrore.toString());
                    }// fine del blocco try-catch
                }// end of if/else cycle
                break;
            case edit:
            case editDaLink:
                entitySaved = this.save(entityBean);
                Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        return entitySaved;
    }// end of method


    /*
     * Opportunità di usare una idKey specifica. <br>
     * Invocato appena prima del save(), solo per una nuova entity (vuol dire che arriva con entityBean.id=null) <br>
     * Se idKey è vuota, uso la standard random mongo (lasciando entityBean.id = null) <br>
     *
     * @param entityBean da salvare
     */
    public AEntity creaIdKeySpecifica(AEntity entityBean) {
        String idKey = "";

        if (text.isEmpty(entityBean.id)) {
            idKey = getKeyUnica(entityBean);
            if (text.isValid(idKey)) {
                entityBean.id = idKey;
            } else {
                entityBean.id = null;
            }// end of if/else cycle
        }// end of if cycle

        return entityBean;
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che la key unica non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param newEntityBean nuova da creare
     */
    public boolean isEsisteEntityKeyUnica(AEntity newEntityBean) {
        return isEsisteByKeyUnica(getKeyUnica(newEntityBean));
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che una entity con la keyUnica indicata non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param keyUnica di riferimento (obbligatoria ed unica)
     *
     * @return true se la entity con la keyUnica indicata esiste
     */
    public boolean isEsisteByKeyUnica(String keyUnica) {
        if (text.isValid(keyUnica)) {
            return findById(keyUnica) != null;
        } else {
            return false;
        }// end of if/else cycle
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che una entity con la keyUnica indicata non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param keyUnica di riferimento (obbligatoria ed unica)
     *
     * @return true se la entity con la keyUnica indicata non esiste
     */
    public boolean isMancaByKeyUnica(String keyUnica) {
        return !isEsisteByKeyUnica(keyUnica);
    }// end of method


    /**
     * Formula univoca per costruire una (eventuale) idKey
     * Può essere costruita liberamente
     * La utilizza sia in scrittura che in lettura
     * Se usa la company, questa deve esserci (eventualmente vuota)
     * <p>
     * Se esiste la property 'code', usa questa property come idKey <br>
     * Altrimenti, se esiste la property 'ordine', usa questa property come idKey <br>
     * Altrimenti, se esiste un metodo sovrascritto nella sottoclasse concreta, utilizza quello <br>
     * Altrimenti, restituisce un valore vuoto <br>
     *
     * @param entityBean da regolare
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    public String getKeyUnica(AEntity entityBean) {
        String keyUnica = "";
        String keyCode = getPropertyUnica(entityBean);

        if (text.isEmpty(keyCode)) {
            if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                keyCode = (String) reflection.getPropertyValue(entityBean, FIELD_NAME_CODE);
            } else {
                if (reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
                    keyCode = reflection.getPropertyValue(entityBean, FIELD_NAME_ORDINE).toString();
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        if (text.isEmpty(keyCode)) {
            return keyCode;
        }// end of if cycle

        keyUnica = addKeyCompany(entityBean, keyCode);
        return keyUnica;
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
    public String addKeyCompany(AEntity entityBean, String keyCode) {
        String keyUnica = "";
        Company company = null;
        String companyCode = "";

        if (usaCompany()) {
            if (entityBean instanceof ACEntity) {
                company = ((ACEntity) entityBean).company;
                if (company != null) {
                    companyCode = company.getCode();
                }// end of if cycle
            }// end of if cycle

            if (text.isEmpty(companyCode)) {
                companyCode = getCompanyCode();
            }// end of if cycle

            if (text.isValid(companyCode)) {
                keyUnica = companyCode + text.primaMaiuscola(keyCode);
            } else {
                if (annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria) {
                    keyUnica = null;
                } else {
                    keyUnica = keyCode;
                }// end of if/else cycle
            }// end of if/else cycle
        } else {
            keyUnica = keyCode;
        }// end of if/else cycle

        return keyUnica;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return "";
    }// end of method


    public String getCompanyCode(AEntity entityBean) {
        String code = "";
        Company company = getCompany(entityBean);

        if (company != null) {
            code = company.getCode();
        }// end of if cycle

        return code;
    }// end of method


    public Company getCompany(AEntity entityBean) {
        Company company = null;

        if (entityBean instanceof ACEntity) {
        } else {
            company = getCompany();
        }// end of if/else cycle

        return company;
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     */
    public int getNewOrdine() {
        return mongo.getNewOrdine(entityClass);
    }// end of method


    /**
     * Instances of the current company <br>
     * Lista ordinata <br>
     * Può essere sovrascritta nella sottoclasse <br>
     * Adatta SOLO per collections non troppo lunghe <br>
     * Per colelctions con centinaia o migliaia di entities, usare una chiamata nella repository specifica <br>
     *
     * @return lista ordinata delle entities della company corrente
     */
    private ArrayList<? extends AEntity> findAllByCompany(Sort sort) {
        ArrayList<AEntity> listByCompany = null;
        ArrayList<? extends AEntity> listAllEntities = null;
        Company company = null;
        ACEntity companyEntity;

        if (company != null) {
            listAllEntities = findAll(sort);
            if (array.isValid(listAllEntities)) {
                listByCompany = new ArrayList<>();
                for (AEntity entity : listAllEntities) {
                    if (entity instanceof ACEntity) {
                        companyEntity = (ACEntity) entity;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        return listByCompany;
    }// end of method


    /**
     * Deletes a given entity.
     *
     * @param entityBean must not be null
     *
     * @return true, se la entity è stata effettivamente cancellata
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public boolean delete(AEntity entityBean) {
        boolean status = false;
        DeleteResult result = null;

        if (entityBean != null) {
            result = mongo.delete(entityBean);
        }// end of if cycle

        if (result != null && result.getDeletedCount() == 1) {
            status = true;
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Delete from a collection.
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     */
    public DeleteResult deleteByProperty(Class<? extends AEntity> clazz, String property, Object value) {
        return mongo.deleteByProperty(clazz, property, value);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaEntities di elementi da cancellare
     * @param clazz         della collezione
     *
     * @return numero di elementi cancellati
     */
    public int delete(List<? extends AEntity> listaEntities, Class<? extends AEntity> clazz) {
        List<String> listaId = new ArrayList<String>();

        for (AEntity entity : listaEntities) {
            listaId.add(entity.id);
        }// end of for cycle

        return deleteBulk(listaId, clazz);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaId di ObjectId da cancellare
     * @param clazz   della collezione
     *
     * @return numero di elementi cancellati
     */
    public int deleteBulk(List<String> listaId, Class<? extends AEntity> clazz) {
        int cancellati = 0;
        DeleteResult result = mongo.deleteBulk(listaId, clazz);

        if (result != null) {
            cancellati = (int) result.getDeletedCount();
        }// end of if cycle

        return cancellati;
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
        return mongo.deleteBulkByProperty(lista, clazz, property);
    }// end of method


    /**
     * Deletes all entities of the collection.
     */
    @Override
    public boolean deleteAll() {
        mongo.drop(entityClass);
        return count() == 0;
    }// end of method


    /**
     * Metodo invocato da ABoot (o da una sua sottoclasse) <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * Creazione di una collezione - Solo se non ci sono records
     */
    @Override
    public void loadData() {
        int numRec = this.count();
        String collectionName = annotation.getCollectionName(entityClass);

        if (numRec == 0) {
            numRec = reset();
            log.warn("Algos - Data. La collezione " + collectionName + " è stata creata: " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione " + collectionName + " è già presente: " + numRec + " schede");
        }// end of if/else cycle

    }// end of method


    /**
     * Creazione di alcuni dati iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * I dati possono essere presi da una Enumeration o creati direttamemte <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse che cancella tutta la Collection <br>
     *
     * @return numero di elementi creati
     */
    @Override
    public int reset() {
        this.deleteAll();
        return 0;
    }// end of method


    /**
     * Recupera il context della session <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public AContext getContext() {
        AContext context = null;
        UI ui;
        VaadinSession vaadSession = null;

        ui = UI.getCurrent();
        if (ui != null) {
            vaadSession = ui.getSession();
        }// end of if cycle

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        return context;
    }// end of method


    /**
     * Recupera il login della session corrente <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public ALogin getLogin() {
        ALogin login = null;
        AContext context = getContext();

        if (context != null) {
            login = context.getLogin();
        }// end of if cycle

        return login;
    }// end of method


    /**
     * Recupera la company della session corrente <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public Company getCompany() {
        Company company = null;
        AContext context = getContext();
        ALogin login;

        if (context != null) {
            login = context.getLogin();
            if (login != null) {
                company = login.getCompany();
            }// end of if cycle
        }// end of if cycle

        return company;
    }// end of method


    /**
     * Recupera la sigla della company della session corrente (se esiste) <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public String getCompanyCode() {
        String code = "";
        Company company = getCompany();

        if (company != null) {
            code = company.getCode();
        }// end of if cycle

        return code;
    }// end of method


    /**
     * Casting da una superclasse ad una sottoclasse <br>
     * Viene creata una nuova istanza VUOTA della sottoclasse <br>
     * Tutti i valori delle property della superclasse vengono ricopiati nella sottoclasse <br>
     * Le rimanenti property della sottoclasse rimangono vuote e verranno regolate successivamente <br>
     */
    public AEntity cast(AEntity entitySopra, AEntity entitySotto) {
        Object value;
        Field fieldSotto;
        List<Field> lista = reflection.getAllFields(entitySopra.getClass());

        try { // prova ad eseguire il codice
            for (Field fieldSopra : lista) {
                value = reflection.getPropertyValue(entitySopra, fieldSopra.getName());
                fieldSotto = reflection.getField(entitySotto.getClass(), fieldSopra.getName());
                fieldSotto.set(entitySotto, value);
            }// end of for cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entitySotto;
    }// end of method

}// end of class
