package it.algos.vaadflow.modules.preferenza;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.boot.ABoot;
import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.application.FlowVar.projectName;
import static it.algos.vaadflow.application.FlowVar.usaCompany;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 14-ott-2019 18.44.27 <br>
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
@Qualifier(TAG_PRE)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PreferenzaService extends AService {

    public final static List<String> LIST_PROPERTIES_MULTICOMPANY = Arrays.asList("ordine", "id", "company", "code", "type", "value", "show", "companySpecifica");

    public final static List<String> FORM_PROPERTIES_MULTICOMPANY = Arrays.asList("ordine", "id", "company", "code", "type", "value", "descrizione", "show", "companySpecifica");

    public final static List<String> PROPERTIES_NO_COMPANY = Arrays.asList("ordine", "code", "type", "value", "descrizione", "show", "companySpecifica");

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Riferimento alla sottoclasse specifica di ABoot per utilizzare il metodo sovrascritto resetPreferenze() <br>
     */
    public ABoot applicationBoot;

    //    /**
    //     * Istanza (@VaadinSessionScope) inietta da Spring ed unica nella sessione <br>
    //     */
    //    @Autowired
    //    @Qualifier(TAG_LOGIN)
    public ALogin login;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private PreferenzaRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public PreferenzaService(@Qualifier(TAG_PRE) MongoRepository repository) {
        super(repository);
        super.entityClass = Preferenza.class;
        this.repository = (PreferenzaRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(String code, String descrizione, EAPrefType type, EARole show, Object value) {
        boolean creata = false;

        if (isMancaByKeyUnica(code)) {
            AEntity entity = save(newEntity(0, code, descrizione, type, show, false, null, value));
            creata = entity != null;
        }// end of if cycle

        return creata;
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param eaPref: enumeration di dati iniziali di prova
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(IAPreferenza eaPref) {
        return creaIfNotExist(eaPref, null);
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param eaPref: enumeration di dati iniziali di prova
     * @param company (facoltativo) usata se companySpecifica=true
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(IAPreferenza eaPref, Company company) {
        boolean creata = false;
        String keyUnica;
        if (company != null) {
            keyUnica = company.code + text.primaMaiuscola(eaPref.getCode());
        } else {
            keyUnica = eaPref.getCode();
        }// end of if/else cycle

        if (findById(keyUnica) == null) {
            AEntity entity = save(newEntity(eaPref, company));
            creata = entity != null;
        }// end of if cycle

        return creata;
    }// end of method


    /**
     * Crea una entity <br>
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return true se la entity è stata creata
     */
    public boolean crea(String code, String descrizione, EAPrefType type, EARole show, Object value) {
        boolean creata;
        AEntity entity = save(newEntity(0, code, descrizione, type, show, false, null, value));
        creata = entity != null;

        return creata;
    }// end of method


    /**
     * Crea una entity <br>
     *
     * @param eaPref: enumeration di dati iniziali di prova
     *
     * @return true se la entity è stata creata
     */
    public boolean crea(IAPreferenza eaPref) {
        return crea(eaPref, null);
    }// end of method


    /**
     * Crea una entity <br>
     *
     * @param eaPref: enumeration di dati iniziali di prova
     * @param company (facoltativo) usata se companySpecifica=true
     *
     * @return true se la entity è stata creata
     */
    public boolean crea(IAPreferenza eaPref, Company company) {
        boolean creata;
        AEntity entity = save(newEntity(eaPref, company));
        creata = entity != null;

        return creata;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public AEntity newEntity() {
        return newEntity(0, "", "", null, null, false, null, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Usa una enumeration di dati iniziali di prova <br>
     *
     * @param eaPref: enumeration di dati iniziali di prova
     * @param company (facoltativo) usata se companySpecifica=true
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(IAPreferenza eaPref, Company company) {
        return newEntity(0, eaPref.getCode(), eaPref.getDesc(), eaPref.getType(), eaPref.getShow(), eaPref.isCompanySpecifica(), company, eaPref.getValue());
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine           di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param code             codice di riferimento (obbligatorio)
     * @param descrizione      (facoltativa)
     * @param type             (obbligatorio) per convertire in byte[] i valori
     * @param value            (obbligatorio) memorizza tutto in byte[]
     * @param companySpecifica (facoltativo) usa un prefisso col codice della company
     * @param company          (facoltativo) usata se companySpecifica=true
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(int ordine, String code, String descrizione, EAPrefType type, EARole show, boolean companySpecifica, Company company, Object value) {
        Preferenza entity = Preferenza.builderPreferenza().ordine(ordine != 0 ? ordine : this.getNewOrdine()).code(text.isValid(code) ? code : null).descrizione(text.isValid(descrizione) ? descrizione : null).type(type != null ? type : EAPrefType.string).show(show != null ? show : EARole.developer).value(type != null ? type.objectToBytes(value) : (byte[]) null).companySpecifica(companySpecifica).build();

        if (company != null) {
            entity.company = company;
        }// end of if cycle

        return (Preferenza) entity;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Preferenza) entityBean).getCode();
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
    public Preferenza findById(String id) {
        return (Preferenza) super.findById(id);
    }// end of method


    /**
     * Se è prevista la company obbligatoria, antepone company.code a quanto sopra (se non è vuoto)
     * Se manca la company obbligatoria, non registra
     * <p>
     * Se è prevista la company facoltativa, antepone company.code a quanto sopra (se non è vuoto)
     * Se manca la company facoltativa, registra con idKey regolata come sopra
     * <p>
     * Per codifiche diverse, sovrascrivere il metodo
     * Regola il code SOLO per le preferenze che prevedono la company specifica <br>
     * Se l'applicazione NON usaCompany, aggiunge come prefisso il nome breve dell'applicazione <br>
     * Regola il code aggiungendo come prefisso la company, se selezionata <br>
     * Se non esiste, aggiunge il nome del programma
     *
     * @param entityBean da regolare
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    public String addKeyCompany(AEntity entityBean, String keyCode) {
        String keyUnica = "";
        Company company = null;

        if (((Preferenza) entityBean).companySpecifica) {
            //--questa preferenza DEVE specificare una company nel keyID
            if (usaCompany) {
                //--l'applicazione è multiCompany
                company = ((Preferenza) entityBean).company;
                if (company != null) {
                    //--questa preferenza DOVREBBE avere una company specificata
                    keyUnica = company.code + text.primaMaiuscola(keyCode);
                } else {
                    //--se non ce l'ha, usa la sigla del programma
                    keyUnica = projectName + text.primaMaiuscola(keyCode);
                }// end of if/else cycle
            } else {
                keyUnica = keyCode;
            }// end of if/else cycle
        } else {
            keyUnica = keyCode;
        }// end of if/else cycle

        return keyUnica;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     * <p>
     * Controlla quante entities ci sono con lo stesso 'code' <br>
     * Cerca la prima usando il campo 'code' <br>
     * Se la trova, controlla se è companySpecifica=true <br>
     * Se il flag è falso, usa la preferenza trovata che dovrebbe essere unica <br>
     * Controlla che sia unica altrimenti lancia un errore <br>
     * Se il flag è vero, cerca la company corrente <br>
     * Se non la trova, utilizza il nome breve dell'applicazione <br>
     * Costruisce la chiave di ricerca per l'ID <br>
     * Cerca nel campo keyID <br>
     * Se non trova una singola entity con prefCode, controlla quante ce ne sono <br>
     * Se sono zero, c'è un errore <br>
     * Se sono più di una,
     *
     * @param prefCode (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Preferenza findByKeyUnica2(String prefCode) {
        return findByKeyUnica(prefCode, VUOTA);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     * <p>
     * Controlla quante entities ci sono con lo stesso 'code' <br>
     * Cerca la prima usando il campo 'code' <br>
     * Se la trova, controlla se è companySpecifica=true <br>
     * Se il flag è falso, usa la preferenza trovata che dovrebbe essere unica <br>
     * Controlla che sia unica altrimenti lancia un errore <br>
     * Se il flag è vero, cerca la company corrente <br>
     * Se non la trova, utilizza il nome breve dell'applicazione <br>
     * Costruisce la chiave di ricerca per l'ID <br>
     * Cerca nel campo keyID <br>
     * Se non trova una singola entity con prefCode, controlla quante ce ne sono <br>
     * Se sono zero, c'è un errore <br>
     * Se sono più di una,
     *
     * @param prefCode (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Preferenza findByKeyUnica(String prefCode, String companyPrefix) {
        Preferenza pref = null;
        String keyId = "";
        Company company = null;
        String prefix = "";
        //        int numPrefCode = 0;
        List<Preferenza> lista;

        //--Controlla quante entities ci sono con lo stesso 'code'
        lista = repository.findAllByCode(prefCode);

        //--Non ne ha trovate e lancia un errore
        if (lista.size() == 0) {
            log.error("Manca la preferenza: " + prefCode);
            return null;
        }// end of if cycle

        //--Se ce n'è una sola, usa quella
        //--Se ne trova più di una, DEVONO essere companySpecifica=true
        //--Prende la prima e costruisce la chiave di ricerca per la keyID
        if (lista.size() == 1) {
            pref = lista.get(0);
            if (text.isEmpty(pref.code)) {
                pref = null;
                log.error("Manca il codice della preferenza");
            }// end of if cycle
        } else {
            //            pref = repository.findFirstByCode(prefCode);
            if (lista != null && lista.get(0).companySpecifica) {
                if (text.isValid(companyPrefix)) {
                    prefix = text.primaMinuscola(companyPrefix);
                } else {
                    //--Se il flag è vero, cerca la company corrente
                    company = getCompany();

                    //--Se non la trova, utilizza il nome breve dell'applicazione
                    if (company != null) {
                        prefix = company.code;
                    }// end of if cycle
                }// end of if/else cycle

                //--Costruisce la chiave di ricerca per l'ID
                keyId = prefix + text.primaMaiuscola(prefCode);

                //--Cerca nel campo keyID
                pref = findById(keyId);
            } else {
                log.error("La preferenza: " + prefCode + " non è companySpecifica.");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle


        //        //--Cerca la prima usando il campo 'code'
        //        pref = repository.findFirstByCode(prefCode);

        //        if (pref != null) {
        //            //--Se la trova, controlla se è companySpecifica=true
        //            if (pref.companySpecifica) {
        //            } else {
        //                numPrefCode = repository.countByCode(prefCode);
        //
        //                //--Controlla che sia unica altrimenti lancia un errore
        //                if (true) {
        //                    //--ok
        //                } else {
        //                    //--errore
        //                }// end of if/else cycle
        //
        //
        //            }// end of if/else cycle
        //
        //
        //        } else {
        //            //-- bo?
        //        }// end of if/else cycle


        return pref;
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
        return repository.findAllByOrderByOrdine();
    }// end of method


    //    public List<Preferenza> findAllByType(EAPrefType type) {
    //        Query query = new Query();
    //        String meseField = "mese";
    //
    //        if (mese != null) {
    //            query.addCriteria(Criteria.where(meseField).is(mese));
    //        }// end of if cycle
    //
    //        return mongo.mongoOp.find(query, Preferenza.class);
    //        return repository.findAllByTypeOrderByValue(type);
    //    }// end of method


    /**
     * Metodo invocato da ABoot (o da una sua sottoclasse) <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * Creazione di una collezione - Solo se non ci sono records
     */
    @Override
    public void loadData() {
        this.reset();
    }// end of method


    /**
     * Cancella e ricrea le preferenze standard e quelle specifiche <br>
     * Invoca il metodo resetPreferenze() della sottoclasse specifica di ABoot per accedere alle preferenze specifiche <br>
     *
     * @return numero di preferenze creato
     */
    @Override
    public int reset() {
        //        int numRec = super.reset();
        int numPref = 0;
        //        int numPref = count();
        //
        //        for (EAPreferenza eaPref : EAPreferenza.values()) {
        //            numRec = creaIfNotExist(eaPref) ? numRec + 1 : numRec;
        //        }// end of for cycle
        //
        ////        if (numRec == 0) {
        ////            log.info("Algos - Data. Le preferenze sono già presenti (" + numPref + ") e non ne sono state aggiunte di nuove");
        ////        } else {
        ////            log.warn("Algos - Data. Sono state aggiunte: " + numRec + " nuove preferenze");
        ////        }// end of if/else cycle
        //
        //        return numRec;

        if (applicationBoot != null) {
            numPref = applicationBoot.resetPreferenze();
        }// end of if cycle

        return numPref;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    public List<String> getGridPropertyNamesList(AContext context) {
        return usaCompany ? LIST_PROPERTIES_MULTICOMPANY : PROPERTIES_NO_COMPANY;
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
        return usaCompany ? FORM_PROPERTIES_MULTICOMPANY : PROPERTIES_NO_COMPANY;
    }// end of method

    //    /**
    //     * Fetches the entities whose 'main text property' matches the given filter text.
    //     * <p>
    //     * The matching is case insensitive. When passed an empty filter text,
    //     * the method returns all categories. The returned list is ordered by name.
    //     * The 'main text property' is different in each entity class and chosen in the specific subclass
    //     *
    //     * @param filter the filter text
    //     *
    //     * @return the list of matching entities
    //     */
    //    @Override
    //    public List<Preferenza> findFilter(String filter) {
    //        String normalizedFilter = filter.toLowerCase();
    //        List<Preferenza> lista = findAll();
    //
    //        return lista.stream()
    //                .filter(entity -> entity.getCode().toLowerCase().contains(normalizedFilter))
    //                .collect(Collectors.toList());
    //    }// end of method


    //    /**
    //     * Opportunità di controllare (per le nuove schede) che la key unica non esista già. <br>
    //     * Invocato appena prima del save(), solo per una nuova entity <br>
    //     *
    //     * @param entityBean nuova da creare
    //     */
    //    @Override
    //    public boolean isEsisteEntityKeyUnica(AEntity entityBean) {
    //        return findByKeyUnica(((Preferenza) entityBean).getCode()) != null;
    //    }// end of method

    //    /**
    //     * Opportunità di usare una idKey specifica. <br>
    //     * Invocato appena prima del save(), solo per una nuova entity <br>
    //     *
    //     * @param entityBean da salvare
    //     */
    //    protected void creaIdKeySpecifica(AEntity entityBean) {
    //        entityBean.id = ((Preferenza) entityBean).getCode();
    //    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     */
    public int getNewOrdine() {
        int ordine = 0;

        List<Preferenza> lista = repository.findTop1AllByOrderByOrdineDesc();
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


    public Object getValue(String keyCode) {
        return getValue(keyCode, getPrefix());
    } // end of method


    public Object getValue(String keyCode, String companyPrefix) {
        Object value = null;
        Preferenza pref = findByKeyUnica(keyCode, companyPrefix);

        if (pref != null) {
            value = pref.getType().bytesToObject(pref.value);
        }// end of if cycle

        return value;
    } // end of method


    public String getStr(IAPreferenza eaPref) {
        return getStr(eaPref.getCode(), (String) eaPref.getValue(), getPrefix());
    } // end of method


    public String getStr(String keyCode) {
        return getStr(keyCode, VUOTA, getPrefix());
    } // end of method


    public String getStr(String keyCode, String defaultValue, String companyPrefix) {
        String valoreTesto = defaultValue;
        Object value = null;
        Preferenza pref = findByKeyUnica(keyCode, companyPrefix);

        if (pref != null) {
            if (pref.type == EAPrefType.enumeration) {
                valoreTesto = getEnumStr(keyCode);
            } else {
                value = getValue(keyCode);
                if (value != null && value instanceof String) {
                    valoreTesto = (String) value;
                } else {
                    log.error("Algos - Preferenze. La preferenza: " + keyCode + " è del tipo sbagliato");
                }// end of if/else cycle
            }// end of if/else cycle
        } else {
            log.warn("Algos - Preferenze. Non esiste la preferenza: " + keyCode);
        }// end of if/else cycle

        return valoreTesto;
    } // end of method


    public Boolean isBool(IAPreferenza eaPref) {
        return isBool(eaPref.getCode());
    } // end of method


    public Boolean isBool(String keyCode) {
        return isBool(keyCode, getPrefix());
    } // end of method


    public Boolean isBool(String keyCode, String companyPrefix) {
        boolean status = false;
        Object value = getValue(keyCode, companyPrefix);

        if (value != null) {
            if (value instanceof Boolean) {
                status = (boolean) value;
            } else {
                log.error("Algos - Preferenze. La preferenza: " + keyCode + " è del tipo sbagliato");
            }// end of if/else cycle
        } else {
            log.warn("Algos - Preferenze. Non esiste la preferenza: " + keyCode);
        }// end of if/else cycle

        return status;
    } // end of method


    public int getInt(IAPreferenza eaPref) {
        return getInt(eaPref.getCode(), (int) eaPref.getValue());
    } // end of method


    public int getInt(String keyCode) {
        return getInt(keyCode, 0);
    } // end of method


    public int getInt(String keyCode, int defaultValue) {
        return getInt(keyCode, defaultValue, VUOTA);
    } // end of method


    public int getInt(String keyCode, String companyPrefix) {
        return getInt(keyCode, 0, companyPrefix);
    } // end of method


    public int getInt(String keyCode, int defaultValue, String companyPrefix) {
        int valoreIntero = defaultValue;
        Object value = getValue(keyCode, companyPrefix);

        if (value != null) {
            if (value instanceof Integer) {
                valoreIntero = (Integer) value;
            } else {
                log.error("Algos - Preferenze. La preferenza: " + keyCode + " è del tipo sbagliato");
            }// end of if/else cycle
        } else {
            log.warn("Algos - Preferenze. Non esiste la preferenza: " + keyCode);
        }// end of if/else cycle

        return valoreIntero;
    } // end of method


    public long getLong(IAPreferenza eaPref) {
        return getLong(eaPref.getCode(), (long) eaPref.getValue());
    } // end of method


    public long getLong(String keyCode) {
        return getLong(keyCode, 0);
    } // end of method


    public long getLong(String keyCode, long defaultValue) {
        long valoreLungo = defaultValue;
        Object value = getValue(keyCode);

        if (value != null) {
            if (value instanceof Long) {
                valoreLungo = (Long) value;
            } else {
                log.error("Algos - Preferenze. La preferenza: " + keyCode + " è del tipo sbagliato");
            }// end of if/else cycle
        } else {
            log.warn("Algos - Preferenze. Non esiste la preferenza: " + keyCode);
        }// end of if/else cycle

        return valoreLungo;
    } // end of method


    public String getEnumStr(IAPreferenza eaPref) {
        return getEnumStr(eaPref.getCode(), (String) eaPref.getValue(), getPrefix());
    } // end of method


    public String getEnumStr(IAPreferenza eaPref, String defaultValue) {
        return getEnumStr(eaPref.getCode(), defaultValue, getPrefix());
    } // end of method


    public String getEnumStr(String keyCode) {
        return getEnumStr(keyCode, "", getPrefix());
    } // end of method


    public String getEnumStr(String keyCode, String defaultValue, String companyPrefix) {
        String valoreTesto = defaultValue;
        String rawValue = (String) getValue(keyCode, companyPrefix);

        if (text.isValid(rawValue)) {
            valoreTesto = enumService.convertToPresentation(rawValue);
        } else {
            log.warn("Algos - Preferenze. Non esiste la preferenza: " + keyCode);
        }// end of if/else cycle

        return valoreTesto;
    } // end of method


    public LocalDate getDate(String code) {
        LocalDate value = null;
        Object genericValue = getValue(code);

        if (genericValue instanceof LocalDate) {
            value = (LocalDate) genericValue;
        }// end of if cycle

        return value != null ? value : START_DATE;
    } // end of method


    public LocalDateTime getDateTime(String code) {
        LocalDateTime value = null;
        Object genericValue = getValue(code);

        if (genericValue instanceof LocalDateTime) {
            value = (LocalDateTime) genericValue;
        }// end of if cycle

        return value != null ? value : START_DATE_TIME;
    } // end of method


    public LocalTime getTime(String code) {
        LocalTime value = null;
        Object genericValue = getValue(code);

        if (genericValue instanceof LocalTime) {
            value = (LocalTime) genericValue;
        }// end of if cycle

        return value != null ? value : START_TIME;
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setValue(String keyCode, Object value) {
        return setValue(keyCode, value, VUOTA);
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setValue(String keyCode, Object value, String companyPrefix) {
        Preferenza pref = findByKeyUnica(keyCode, companyPrefix);

        if (pref != null) {
            pref.setValue(pref.getType().objectToBytes(value));
        }// end of if cycle

        return pref;
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setBool(String keyCode, boolean value) {
        return setBool(keyCode, value, VUOTA);
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setBool(String keyCode, boolean value, String companyPrefix) {
        Preferenza pref = findByKeyUnica(keyCode, companyPrefix);

        if (pref != null && pref.type == EAPrefType.bool) {
            pref = this.setValue(keyCode, value);
        }// end of if cycle

        return pref;
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setInt(String keyCode, int value) {
        return setInt(keyCode, value, VUOTA);
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setInt(String keyCode, int value, String companyPrefix) {
        Preferenza pref = findByKeyUnica(keyCode, companyPrefix);

        if (pref != null && pref.type == EAPrefType.integer) {
            pref = this.setValue(keyCode, value);
        }// end of if cycle

        return pref;
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setDate(String keyCode, LocalDateTime value) {
        return setDate(keyCode, value, VUOTA);
    } // end of method


    /**
     * Regola il valore della entity che NON viene salvata <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena regolata (non salvata)
     */
    public Preferenza setDate(String keyCode, LocalDateTime value, String companyPrefix) {
        Preferenza pref = findByKeyUnica(keyCode, companyPrefix);

        if (pref != null && pref.type == EAPrefType.localdatetime) {
            pref = this.setValue(keyCode, value);
        }// end of if cycle

        return pref;
    } // end of method


    /**
     * Regola il valore della entity e la salva <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return true se la entity è stata salvata
     */
    public boolean saveValue(String keyCode, Object value) {
        return saveValue(keyCode, value, VUOTA);
    } // end of method


    /**
     * Regola il valore della entity e la salva <br>
     *
     * @param keyCode codice di riferimento (obbligatorio)
     * @param value   (obbligatorio) memorizza tutto in byte[]
     *
     * @return true se la entity è stata salvata
     */
    public boolean saveValue(String keyCode, Object value, String companyPrefix) {
        boolean salvata = false;
        Preferenza entity = setValue(keyCode, value, companyPrefix);

        if (entity != null) {
            entity = (Preferenza) this.save(entity);
            salvata = entity != null;
        }// end of if cycle

        return salvata;
    } // end of method

    //    public  Boolean getBool(String code, Object defaultValue) {
    //        return getBool(code, CompanySessionLib.getCompany(), defaultValue);
    //    } // end of method
    //
    //    public  Boolean getBool(String code, BaseCompany company) {
    //        return getBool(code, company, "");
    //    } // end of static method
    //
    //    public  Boolean getBool(String code, BaseCompany company, Object defaultValue) {
    //        Pref pref = Pref.findByCode(code, company);
    //
    //        if (pref != null) {
    //            return (boolean) pref.getValore();
    //        }// end of if cycle
    //
    //        if (defaultValue != null && defaultValue instanceof Boolean) {
    //            return (boolean) defaultValue;
    //        }// end of if cycle
    //
    //        return false;
    //    } // end of method


    public String getPrefix() {
        String companyPrefix = login != null ? login.getCompany() != null ? login.getCompany().code : VUOTA : VUOTA;
        return companyPrefix;
    } // end of method


}// end of class