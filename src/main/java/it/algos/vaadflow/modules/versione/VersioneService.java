package it.algos.vaadflow.modules.versione;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static it.algos.vaadflow.application.FlowCost.TAG_VER;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 21.19.40 <br>
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
@Qualifier(TAG_VER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class VersioneService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    private final static String TAG = ".";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private PreferenzaService pref;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private VersioneRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public VersioneService(@Qualifier(TAG_VER) MongoRepository repository) {
        super(repository);
        super.entityClass = Versione.class;
        this.repository = (VersioneRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param sigla       del progetto interessato (transient, obbligatorio, un solo carattere) <br>
     * @param titolo      della versione (obbligatorio, non unico) <br>
     * @param descrizione nome descrittivo della versione (obbligatorio, unico)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(String sigla, String titolo, String descrizione) {
        boolean creata = false;

        if (isMancaByKeyUnica(getIdKey(sigla))) {
            AEntity entity = save(newEntity(sigla, titolo, descrizione));
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
    public Versione newEntity() {
        return newEntity("", "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param sigla       del progetto interessato (transient, obbligatorio, un solo carattere) <br>
     * @param titolo      della versione (obbligatorio, non unico) <br>
     * @param descrizione della versione (obbligatoria, unica) <br>
     *                    timestamp   in cui si effettua la modifica della versione
     *                    (obbligatorio, con inserimento automatico) <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity(String sigla, String titolo, String descrizione) {
        Versione entity = Versione.builderVersione()
                .titolo(text.isValid(titolo) ? titolo : null)
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .timestamp(LocalDate.now())
                .build();
        entity.id = getIdKey(sigla);

        return entity;
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per ogni project), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property per lo specifico progetto <br>
     * Incrementa di uno il risultato <br>
     *
     * @param sigla del progetto interessato (transient, obbligatorio, un solo carattere) <br>
     */
    public String getIdKey(String sigla) {
        return getIdKey(sigla, 0);
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per ogni project), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property per lo specifico progetto <br>
     * Incrementa di uno il risultato <br>
     *
     * @param sigla     del progetto interessato (transient, obbligatorio, un solo carattere) <br>
     * @param newOrdine progressivo della versione (transient, obbligatorio) <br>
     */
    public String getIdKey(String sigla, int newOrdine) {
        String keyCode = "";
        List<Versione> lista = null;
        String idKey = "0";

        if (newOrdine == 0) {
            lista = repository.findByIdRegex(sigla);
            if (lista != null && lista.size() > 0) {
                idKey = lista.get(lista.size() - 1).getId();
                idKey = idKey.substring(1);
                idKey = idKey.startsWith(TAG) ? text.levaTesta(idKey, TAG) : idKey;
                idKey = idKey.startsWith(TAG) ? text.levaTesta(idKey, TAG) : idKey;//doppio per numeri sopra i 10 e fino a 100
            }// end of if cycle

            try { // prova ad eseguire il codice
                newOrdine = Integer.decode(idKey);
                newOrdine++;
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle


        if (newOrdine < 100) {
            if (newOrdine < 10) {
                keyCode = sigla + TAG + TAG + newOrdine;
            } else {
                keyCode = sigla + TAG + newOrdine;
            }// end of if/else cycle
        } else {
            keyCode = sigla + newOrdine;
        }// end of if/else cycle

        return keyCode;
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
        Versione entity = (Versione) super.beforeSave(entityBean, operation);

        if (text.isEmpty(entity.titolo) || text.isEmpty(entity.descrizione) || entity.timestamp == null) {
            entity = null;
        }// end of if cycle


        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param keyCode (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Versione findByKeyUnica(String keyCode) {
        Versione entity = null;
        Object optional = repository.findById(keyCode);

        if (((Optional) optional).isPresent()) {
            entity = (Versione) ((Optional) optional).get();
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param sigla     del progetto interessato (transient, obbligatorio, un solo carattere) <br>
     * @param newOrdine progressivo della versione (transient, obbligatorio) <br>
     *
     * @return istanza della Entity, null se non trovata
     */
    public Versione findByKeyUnica(String sigla, int newOrdine) {
        return findByKeyUnica(getIdKey(sigla));
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param sigla     del progetto interessato (transient, obbligatorio, un solo carattere) <br>
     * @param newOrdine progressivo della versione (transient, obbligatorio) <br>
     *
     * @return true se trovata
     */
    public boolean isMancaByKeyUnica(String sigla, int newOrdine) {
        return findByKeyUnica(getIdKey(sigla, newOrdine)) == null;
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
        return findAll(new Sort(Sort.Direction.ASC, "id"));
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param type     della preferenza
     * @param value    di default della preferenza
     */
    public void creaPref(String sigla, String codePref, String descPref, EAPrefType type, Object value) {
        pref.creaIfNotExist(codePref, descPref, type, null,value);
        this.creaIfNotExist(sigla, "Preferenze", codePref + ", di default " + value);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo string (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefTxt(String sigla, String codePref, String descPref, String value) {
        creaPref(sigla, codePref, descPref, EAPrefType.string, value);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo string (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefTxt(String sigla, String codePref, String descPref) {
        creaPrefTxt(sigla, codePref, descPref, "");
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo bool (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefBool(String sigla, String codePref, String descPref, boolean value) {
        creaPref(sigla, codePref, descPref, EAPrefType.bool, value);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo bool (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefBool(String sigla, String codePref, String descPref) {
        creaPrefBool(sigla, codePref, descPref, false);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo int (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefInt(String sigla, String codePref, String descPref, int value) {
        creaPref(sigla, codePref, descPref, EAPrefType.integer, value);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo int (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefInt(String sigla, String codePref, String descPref) {
        creaPrefInt(sigla, codePref, descPref, 0);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo date (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefDate(String sigla, String codePref, String descPref, LocalDateTime value) {
        creaPref(sigla, codePref, descPref, EAPrefType.date, value);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo date (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefDate(String sigla, String codePref, String descPref) {
        creaPrefDate(sigla, codePref, descPref, (LocalDateTime) null);
    }// end of method


}// end of class