package it.algos.vaadflow.modules.preferenza;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PRE;

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
@Qualifier(TAG_PRE)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PreferenzaService extends AService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


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
    public boolean creaIfNotExist(String code, String descrizione, EAPrefType type, Object value) {
        boolean creata = false;

        if (isMancaByKeyUnica(code)) {
            AEntity entity = save(newEntity(0, code, descrizione, type, value));
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
    public boolean creaIfNotExist(EAPreferenza eaPref) {
        boolean creata = false;

        if (isMancaByKeyUnica(eaPref.getCode())) {
            AEntity entity = save(newEntity(eaPref));
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
    public AEntity newEntity() {
        return newEntity(0, "", "", null, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Usa una enumeration di dati iniziali di prova <br>
     *
     * @param eaPref: enumeration di dati iniziali di prova
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(EAPreferenza eaPref) {
        return newEntity(0, eaPref.getCode(), eaPref.getDesc(), eaPref.getType(), eaPref.getValue());
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine      di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(int ordine, String code, String descrizione, EAPrefType type, Object value) {
        Preferenza entity = Preferenza.builderPreferenza()
                .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                .code(text.isValid(code) ? code : null)
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .type(type != null ? type : EAPrefType.string)
                .value(type != null ? type.objectToBytes(value) : (byte[]) null)
                .build();

        return (Preferenza) super.addCompanySeManca(entity);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Preferenza) entityBean).getCode();
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
        return super.beforeSave(entityBean, operation);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Preferenza findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method


//    /**
//     * Fetches the entities whose 'main text property' matches the given filter text.
//     * <p>
//     * Se esiste la company, filtrate secondo la company <br>
//     * The matching is case insensitive. When passed an empty filter text,
//     * the method returns all categories. The returned list is ordered by name.
//     * The 'main text property' is different in each entity class and chosen in the specific subclass
//     *
//     * @param filter the filter text
//     *
//     * @return the list of matching entities
//     */
//    @Override
//    public List<? extends AEntity> findFilter(String filter) {
//        return findAll(); //@todo PROVVISORIO
//    }
//
//
//    /**
//     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
//     *
//     * @param code di riferimento (obbligatorio)
//     *
//     * @return istanza della Entity, null se non trovata
//     */
//    public Preferenza findByKeyUnica(String code) {
//        return repository.findByCode(code);
//    }// end of method
//
//
//    /**
//     * Returns all instances of the type <br>
//     * La Entity è EACompanyRequired.nonUsata. Non usa Company. <br>
//     * Lista ordinata <br>
//     *
//     * @return lista ordinata di tutte le entities
//     */
//    @Override
//    public List<Preferenza> findAll() {
//        List<Preferenza> lista = null;
//
//        lista = repository.findAllByOrderByOrdineAsc();
//
//        return lista;
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
//        int numRec = 0;
        int numPref = count();

        for (EAPreferenza eaPref : EAPreferenza.values()) {
            numRec = creaIfNotExist(eaPref) ? numRec + 1 : numRec;
        }// end of for cycle

//        if (numRec == 0) {
//            log.info("Algos - Data. Le preferenze sono già presenti (" + numPref + ") e non ne sono state aggiunte di nuove");
//        } else {
//            log.warn("Algos - Data. Sono state aggiunte: " + numRec + " nuove preferenze");
//        }// end of if/else cycle

        return numRec;
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
        Object value = null;
        Preferenza pref = findByKeyUnica(keyCode);

        if (pref != null) {
            value = pref.getType().bytesToObject(pref.value);
        }// end of if cycle

        return value;
    } // end of method


    public LocalDateTime getDate(String code) {
        LocalDateTime value = null;
        Object genericValue = getValue(code);

        if (genericValue instanceof LocalDateTime) {
            value = (LocalDateTime) genericValue;
        }// end of if cycle

        return value;
    } // end of method


    public Boolean isBool(String keyCode) {
        boolean status = false;
        Object value = getValue(keyCode);

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


    public int getInt(String keyCode) {
        return getInt(keyCode, 0);
    } // end of method


    public int getInt(String keyCode, int defaultValue) {
        int valoreIntero = defaultValue;
        Object value = getValue(keyCode);

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


    public String getStr(String keyCode) {
        String valoreTesto = "";
        Object value = null;
        Preferenza pref = findByKeyUnica(keyCode);

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

        return valoreTesto;
    } // end of method


    public String getEnumStr(String keyCode) {
        String valoreTesto = "";
        String rawValue = (String) getValue(keyCode);

        if (text.isValid(rawValue)) {
            valoreTesto = enumService.convertToPresentation(rawValue);
        } else {
            log.warn("Algos - Preferenze. Non esiste la preferenza: " + keyCode);
        }// end of if/else cycle

        return valoreTesto;
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
        Preferenza pref = findByKeyUnica(keyCode);

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
        Preferenza pref = findByKeyUnica(keyCode);

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
        Preferenza pref = findByKeyUnica(keyCode);

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
        Preferenza pref = findByKeyUnica(keyCode);

        if (pref != null && pref.type == EAPrefType.date) {
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
        boolean salvata = false;
        Preferenza entity = setValue(keyCode, value);

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

}// end of class