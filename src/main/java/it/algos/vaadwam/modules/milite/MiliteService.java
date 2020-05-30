package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.IUtenteService;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadwam.migration.MigrationService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.log.WamLogService;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;
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
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_MIL)
@Slf4j
@AIScript(sovrascrivibile = false)
public class MiliteService extends WamService implements IUtenteService {

    public final static String FIELD_LOCKED = "locked";

    public final static String FIELD_ROLE = "role";

    public final static String FIELD_NOME = "nome";

    public final static String FIELD_COGNOME = "cognome";

    public final static String FIELD_PASSWORD = "password";

    public final static String FIELD_USERNAME = "username";

    public final static String FIELD_ADMIN = "admin";

    public final static String FIELD_DIPENDENTE = "dipendente";

    public final static String FIELD_INFERMIERE = "infermiere";

    public final static String FIELD_CENTRALINISTA = "centralinista";

    public final static String FIELD_ATTIVO = "enabled";

    public final static String MANAGER_TABELLONE = "managerTabellone";

    public final static List<String> PROPERTIES_USER = Arrays.asList("croce", "ordine", "userName", "passwordInChiaro", "locked", "nome", "cognome", "telefono", "mail", "indirizzo", "dipendente", "infermiere", "funzioni");

    public final static List<String> PROPERTIES_ADMIN = Arrays.asList("nome", "cognome", "userName", "passwordInChiaro", "telefono", "locked", "admin", "dipendente", "infermiere");

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WamLogService wamLogger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PersonService personService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private FunzioneService funzioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private MigrationService migration;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private RoleService roleService;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private MiliteRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    public MiliteService(@Qualifier(TAG_MIL) MongoRepository repository) {
        super(repository);
        super.entityClass = Milite.class;
        this.repository = (MiliteRepository) repository;
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

        super.lastImport = LAST_IMPORT_MILITI;
        super.durataLastImport = DURATA_IMPORT_MILITI;
        super.eaTempoTypeImport = EATempo.secondi;
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param croce            di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(Croce croce, String nome, String cognome, String userName, String passwordInChiaro, Set<Role> ruoli) {

        return creaIfNotExist(croce, nome, cognome, "", userName, passwordInChiaro, ruoli, "", false, false, false, false, (Set<Funzione>) null);
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param croce            di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param telefono:        (facoltativo)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param enabled          flag enabled (facoltativo, di default true)
     * @param admin            flag amministratore (facoltativo)
     * @param dipendente       flag dipendente (facoltativo)
     * @param infermiere       flag infermiere abilitato (facoltativo)
     * @param funzioni         lista di funzioni per le quali il milite è abilitato (facoltativo)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(Croce croce, String nome, String cognome, String telefono, String userName, String passwordInChiaro, Set<Role> ruoli, String mail, boolean enabled, boolean admin, boolean dipendente, boolean infermiere, Set<Funzione> funzioni) {
        boolean creata = false;

        if (isMancaByKeyUnica(userName)) {
            AEntity entity = save(newEntity(croce, 0, nome, cognome, telefono, (Address) null, userName, passwordInChiaro, ruoli, mail, enabled, admin, dipendente, infermiere, funzioni, true));
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
    public Milite newEntity() {
        return newEntity((Croce) null, "", "", "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     *
     * @param croce            di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Milite newEntity(Croce croce, String nome, String cognome, String userName, String passwordInChiaro) {
        return newEntity(croce, 0, nome, cognome, "", (Address) null, userName, passwordInChiaro, (Set<Role>) null, "", false, false, false, false, (Set<Funzione>) null, true);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param croce            di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param ordine           di presentazione nelle liste (obbligatorio, unico nella croce,
     *                         con controllo automatico se è zero,  modificabile da developer ed admin)
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param telefono:        (facoltativo)
     * @param indirizzo:       via, nome e numero (facoltativo)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param enabled          flag enabled (facoltativo, di default true)
     * @param admin            flag amministratore (facoltativo)
     * @param dipendente       flag dipendente (facoltativo)
     * @param infermiere       flag infermiere abilitato (facoltativo)
     * @param funzioni         lista di funzioni per le quali il milite è abilitato (facoltativo)
     * @param usaSuperClasse   (transient) per utilizzare le properties di Security della superclasse Utente (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Milite newEntity(Croce croce, int ordine, String nome, String cognome, String telefono, Address indirizzo, String userName, String passwordInChiaro, Set<Role> ruoli, String mail, boolean enabled, boolean admin, boolean dipendente, boolean infermiere, Set<Funzione> funzioni, boolean usaSuperClasse) {
        Milite entity = null;
        Person entityDellaSuperClassePerson = null;

        //--controlla il flag passato come parametro e specifico di questa entity (entity embedded non usano Person)
        //--controlla il flag generale dell'applicazione
        //--se usa la security, la persona eredità tutte le property della superclasse Person
        //--prima viene creata una entity di Person, usando le regolazioni automatiche di quella superclasse.
        //--poi vengono ricopiati i valori in Milite
        //--poi vengono aggiunte le property specifiche di Milite
        //--se non usa la security, utilizza il metodo builderMilite
        if (usaSuperClasse && usaSecurity) {
            //--prima viene creata una entity di Utente, usando le regolazioni automatiche di quella superclasse.
            entityDellaSuperClassePerson = personService.newEntity(croce, nome, cognome, telefono, indirizzo, userName, passwordInChiaro, ruoli, mail, enabled, usaSuperClasse);
            entityDellaSuperClassePerson.company = croce;

            //--poi vengono ricopiati i valori in Milite
            //--casting dalla superclasse alla classe attuale
            entity = (Milite) super.cast(entityDellaSuperClassePerson, new Milite());
            entity.usaSuperClasse = true;
            entity.id = null;
        } else {
            entity = Milite.builderMilite().build();
            entity.usaSuperClasse = false;
        }// end of if/else cycle

        //--poi vengono aggiunte le property specifiche di Milite
        //--regola le property di questa classe
        //        entity.company = croce;
        entity.setCroce(croce);
        entity.setOrdine(ordine != 0 ? ordine : this.getNewOrdine(croce));
        entity.setEnabled(true);
        entity.setAdmin(admin);
        entity.setDipendente(dipendente);
        entity.setInfermiere(infermiere);
        entity.setFunzioni(funzioni);
        entity.croce = croce;

        return (Milite) addCompanySeManca(entity);
    }// end of method


    //    /**
    //     * Proviene da List e da Form (quasi sempre) <br>
    //     * Primo ingresso nel service dopo il click sul bottone <br>
    //     */
    //    public AEntity saveNotWorking(AEntity entityBean, EAOperation operation) {
    //        AEntity entitySaved = null;
    //        entitySaved = super.save(entityBean, operation);
    //
    //
    //        return entitySaved;
    //    }// end of method


    /**
     * Proviene da List e da Form (quasi sempre) <br>
     * Primo ingresso nel service dopo il click sul bottone <br>
     *
     * @param entityBean
     * @param operation
     */
    @Override
    public AEntity save(AEntity entityBean, EAOperation operation) {

        if (operation == EAOperation.addNew) {
            wamLogger.nuovoMilite(getMessageNuovo(entityBean));
        } else {
            if (isModificato(entityBean)) {
                if (operation == EAOperation.editProfile) {
                    wamLogger.modificaProfile(getMessageModifiche(entityBean));
                } else {
                    wamLogger.modificaMilite(getMessageModifiche(entityBean));
                }
            }
        }

        return super.save(entityBean, operation);
    }


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
        if (((Milite) entityBean).usaSuperClasse) {
            entityBean = personService.beforeSave(entityBean, operation);
            entityBean.id = null;
        }// end of if cycle

        Milite entity = (Milite) super.beforeSave(entityBean, operation);

        if (entity == null) {
            log.error("entity è nullo in MiliteService.beforeSave()");
            return null;
        }// end of if cycle

        if (entity.croce == null) {
            entity.croce = getCroce();
        }// end of if cycle

        if (entity.croce == null) {
            entity.id = FlowCost.STOP_SAVE;
            log.error("manca la croce in MiliteService.beforeSave()");
        }// end of if cycle

        if (text.isEmpty(entity.getUsername())) {
            entity.username = entity.nome + entity.cognome;
        }// end of if cycle

        if (operation == EAOperation.addNew) {
            if (findByKeyUnica(getPropertyUnica(entityBean)) != null) {
                log.error("esiste già un milite con questo userName in MiliteService.beforeSave()");
                return null;
            }// end of if cycle
        }// end of if cycle

        if (text.isEmpty(entity.getUsername())) {
            entity = null;
        }// end of if cycle


        if (((Milite) entityBean).admin) {
            ((Utente) entityBean).ruoli = roleService.getAdminRoles();
        } else {
            ((Utente) entityBean).ruoli = roleService.getUserRole();
        }

        //--prevedere possibilità di disabilitarlo se la croce non lo vuole
        if (true) {
            ((Milite) entityBean).nome = text.primaMaiuscola(((Milite) entityBean).nome);
            ((Milite) entityBean).cognome = text.primaMaiuscola(((Milite) entityBean).cognome);
        }

        return entity;
    }


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
        AEntity entityBean = super.save(oldBean, modifiedBean);

        if (getWamLogin() != null && getWamLogin().getMilite() != null && entityBean instanceof Milite && getWamLogin().getMilite().id.equals(entityBean.id)) {
            getWamLogin().setMilite((Milite) entityBean);
        }
        return entityBean;
    }


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
    public Milite findById(String id) {
        return (Milite) super.findById(id);
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
        return findByKeyUnica(keyUnica) == null;
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
     * @param keyCode
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    @Override
    public String addKeyCompany(AEntity entityBean, String keyCode) {
        return keyCode;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param userName di riferimento (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Milite findByKeyUnica(String userName) {
        Milite milite = null;

        try { // prova ad eseguire il codice
            milite = repository.findByUsername(userName);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            //            logger.importo("Milite - " + login.getCompany().code + "Trovati troppi militi di nome " + userName);
        }// fine del blocco try-catch

        return milite;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Milite) entityBean).getUsername();
    }// end of method


    /**
     * Importazione di dati <br>
     *
     * @return informazioni sul risultato
     */
    @Override
    public boolean importa(Croce croce) {
        boolean eseguito;

        long inizio = System.currentTimeMillis();
        eseguito = migration.importMiliti(croce);
        setLastImport(croce, inizio);

        return eseguito;
    }// end of method


    /**
     * Returns the number of entities available for the current company
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return the number of entities
     */
    public int countByCroce(Croce croce) {
        return repository.countByCroce(croce);
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Milite> findAll() {
        List<Milite> items = null;
        Croce croce = getCroce();

        if (croce != null) {
            items = findAllByCroce(croce);
        } else {
            if (getWamLogin() != null && getWamLogin().isDeveloper()) {
                items = findAllCroci();
            }// end of if cycle
        }// end of if/else cycle

        return items;
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Milite> findAllCroci() {
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
    public List<Milite> findAllByCroce(Croce croce) {
        return repository.findAllByCroceOrderByOrdineAsc(croce);
    }// end of method


    /**
     * Returns all enabled <br>
     *
     * @return lista delle entities selezionate
     */
    public List<Milite> findAllByEnabled() {
        return repository.findAllByCroceAndEnabledIsTrue(getCroce());
    }// end of method


    /**
     * Returns all instances of admins <br>
     *
     * @return lista delle entities selezionate
     */
    public List<Milite> findAllByAdmin() {
        return repository.findAllByCroceAndAdminIsTrue(getCroce());
    }// end of method


    /**
     * Returns all instances of dipendenti <br>
     *
     * @return lista delle entities selezionate
     */
    public List<Milite> findAllByDipendente() {
        return repository.findAllByCroceAndDipendenteIsTrue(getCroce());
    }// end of method


    /**
     * Returns all instances of infermieri <br>
     *
     * @return lista delle entities selezionate
     */
    public List<Milite> findAllByInfermiere() {
        return repository.findAllByCroceAndInfermiereIsTrue(getCroce());
    }// end of method


    /**
     * Returns all instances of militi senza nessuna funzione abilitata <br>
     *
     * @return lista delle entities selezionate
     */
    public List<Milite> findAllSenzaFunzioni() {
        return repository.findAllByCroceAndEnabledIsTrueAndFunzioniIsNull(getCroce());
    }// end of method


    /**
     * Returns all instances of militi con note <br>
     *
     * @return lista delle entities selezionate
     */
    public List<Milite> findAllConNote() {
        return repository.findAllByCroceAndNoteWamIsNotNull(getCroce());
    }// end of method


    /**
     * Returns all instances of admins, dipendenti ed infermieri <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata delle entities selezionate
     */
    public List<Milite> findAllByAdminOrDipOrInf() {
        List<Milite> lista = new ArrayList<>();
        List<Milite> listaAdmin = repository.findAllByCroceAndAdminIsTrue(getCroce());
        List<Milite> listaDipendenti = repository.findAllByCroceAndDipendenteIsTrue(getCroce());
        List<Milite> listaInfermieri = repository.findAllByCroceAndInfermiereIsTrue(getCroce());

        lista.addAll(listaAdmin);
        lista.addAll(listaDipendenti);
        lista.addAll(listaInfermieri);

        return lista;
    }// end of method


    //    /**
    //     * Returns instances abilitate per la funzione <br>
    //     * Lista ordinata <br>
    //     *
    //     * @param funzione che deve essere abilitata per il milite da considerare
    //     *
    //     * @return lista ordinata delle entities selezionate
    //     */
    //    public List<Milite> findAllByFunzione(Funzione funzione) {
    //        List<Milite> lista = new ArrayList<>();
    //        List<Milite> listaAll = repository.findAllByCroceOrderByOrdineAsc(getCroce());
    //        Set<Funzione> funzioniAbilitatePerIlMilite;
    //
    //        if (array.isValid(listaAll)) {
    //            for (Milite milite : listaAll) {
    //                funzioniAbilitatePerIlMilite = milite.funzioni;
    //                if (funzioniAbilitatePerIlMilite != null) {
    //                    for (Funzione funz : funzioniAbilitatePerIlMilite) {
    //                        if (funz.code.equals(funzione.code)) {
    //                            lista.add(milite);
    //                        }// end of if cycle
    //                    }// end of for cycle
    //                }// end of if cycle
    //            }// end of for cycle
    //        }// end of if cycle
    //
    //        return lista;
    //    }// end of method


    /**
     * Restituisce i militi abilitati per una data funzione
     * in ordine di cognome e nome.
     * La funzione è già specifica della croce quindi tornerà solo mititi di quella croce.
     */
    public List<Milite> findAllByFunzione(Funzione funzione) {
        Query query = new Query();
        query.addCriteria(Criteria.where("funz").elemMatch(Criteria.where("$id").is(funzione.getId())));
        query.with(new Sort(Sort.Direction.ASC, "cognome"));
        query.with(new Sort(Sort.Direction.ASC, "nome"));
        List<Milite> militi = mongoTemplate.find(query, Milite.class);
        return militi;
    }


//    /**
//     * Returns instances of the company <br>
//     * Lista ordinata <br>
//     *
//     * @param croce di appartenenza (obbligatoria)
//     *
//     * @return lista ordinata di tutte le entities
//     */
//    public List<Milite> findAllByCroceOld(Croce croce) {
//        List<Milite> listaMiliti;
//        String userName;
//        AContext context = null;
//        VaadinSession vaadSession = UI.getCurrent().getSession();
//
//        if (vaadSession != null) {
//            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
//        }// end of if cycle
//
//        if (context != null && context.getLogin().isDeveloper()) {
//            listaMiliti = repository.findAllByCroceOrderByOrdineAsc(croce);
//        } else {
//            userName = context.getLogin().getUtente().getUsername();
//            listaMiliti = repository.findAllByUsername(userName);
//
//            if (listaMiliti == null || listaMiliti.size() == 0) {
//                log.warn("Non ho trovato il milite " + userName);
//                listaMiliti = null;
//            } else {
//                if (listaMiliti.size() > 1) {
//                    log.error("Ci sono alcuni militi con lo stesso userName: " + userName);
//                    listaMiliti = null;
//                }// end of if cycle
//            }// end of if/else cycle
//        }// end of if/else cycle
//
//        return listaMiliti;
//    }// end of method


    //    /**
    //     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
    //     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
    //     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
    //     * 3) Sovrascrive la lista nella sottoclasse specifica
    //     *
    //     * @return lista di nomi di properties
    //     */
    //    public List<String> getGridPropertyNamesList() {
    //        return USA_SECURITY ? PROPERTIES_ADMIN : PROPERTIES_USER;
    //    }// end of method


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
        List<String> lista;

        if (getWamLogin() != null && getWamLogin().isAdminOrDev()) {
            lista = array.getList("ordine,nome,cognome,enabled,username,admin,infermiere,dipendente,creatoreTurni,funzioni,noteWam");
        } else {
            lista = array.getList("username,enabled,nome,cognome,admin,infermiere,dipendente,creatoreTurni,funzioni,noteWam");
        }

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
        List<String> lista;

        if (getWamLogin() != null && getWamLogin().isAdminOrDev()) {
            lista = array.getList("ordine,username,enabled,nome,cognome,admin,infermiere,dipendente,creatoreTurni,funzioni,noteWam");
        } else {
            lista = array.getList("username,enabled,nome,cognome,admin,infermiere,dipendente,creatoreTurni,funzioni,noteWam");
        }

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
        return Arrays.asList("nome", "cognome", "username");
    }// end of method


    /**
     * Deletes all entities of the collection.
     */
    @Override
    public boolean deleteAll() {
        Croce croce = getCroce();

        if (croce != null) {
            super.deleteByProperty(entityClass, "croce", croce);
        } else {
            if (getWamLogin() != null && getWamLogin().isDeveloper()) {
                mongo.drop(entityClass);
            }// end of if cycle
            super.deleteByProperty(entityClass, "croce", croce);
        }// end of if/else cycle

        return false;
    }// end of method


    /**
     * Controlla se il milite è abilitato per la funzione indicata
     *
     * @param milite   da controllare
     * @param funzione da controllare
     *
     * @return tru se è abilitato
     */
    public boolean isAbilitato(Milite milite, Funzione funzione) {
        boolean status = false;
        Set<Funzione> funzioni;

        if (milite != null && funzione != null) {
            funzioni = milite.getFunzioni();
            if (funzioni != null) {
                for (Funzione funz : milite.getFunzioni()) {
                    if (funzione.code.equals(funz.code)) {
                        status = true;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method


    @Override
    public boolean isAdmin(Utente utente) {
        return ((Milite) utente).admin;
    }// end of method


    /**
     * Restituisce una lista delle funzioni DI RIFERIMENTO e non di quelle embedded <br>
     */
    @Deprecated
    public List<Funzione> getListaFunzioniOld(Milite milite) {
        List<Funzione> lista = null;
        Funzione funz = null;

        if (milite != null) {
            if (milite.funzioni != null) {
                lista = new ArrayList<>();
                for (Funzione funzione : milite.funzioni) {
                    funz = funzioneService.findById(funzione.id);
                    lista.add(funz);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Costruisce una lista ordinata di funzioni <br>
     * Le funzioni sono memorizzate come Set <br>
     * Le funzioni non sono embedded nel milite ma sono un riferimento dinamico CON @DBRef <br>
     * Nella lista risultante, vengono ordinate secondo la property 'ordine' <br>
     *
     * @return lista ordinata di funzioni
     */
    public List<Funzione> getListaFunzioni(Milite milite) {
        List<Funzione> listaFunzioni = null;
        List<Funzione> listaAll = null;
        Croce croce = null;
        Set<Funzione> set = null;
        List<String> listaIdFunzioni = null;

        if (milite != null) {
            croce = milite.getCroce();
            set = milite.funzioni;
        }// end of if cycle

        if (croce != null) {
            listaAll = funzioneService.findAllByCroce(croce);
        }// end of if cycle

        if (listaAll != null && set != null) {
            listaIdFunzioni = funzioneService.getIdsFunzioni(set);
            listaFunzioni = new ArrayList<>();

            for (Funzione funz : listaAll) {
                if (listaIdFunzioni.contains(funz.id)) {
                    listaFunzioni.add(funz);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaFunzioni;
    }// end of method


    /**
     * Restituisce una lista delle funzioni DI RIFERIMENTO e non di quelle embedded <br>
     */
    public List<String> getListaIDFunzioni(Milite milite) {
        List<String> lista = null;

        if (milite != null) {
            if (milite.funzioni != null) {
                lista = new ArrayList<>();
                for (Funzione funz : milite.funzioni) {
                    lista.add(funz.id);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Restituisce il massimo ruolo abilitato <br>
     * <p>
     * L'ordine è:
     * developer
     * admin
     * user
     * guest
     *
     * @return ruolo massimo abilitato
     */
    public EARoleType getRoleType(Utente utente) {
        EARoleType roleType = null;
        Set<Role> ruoli = utente.ruoli;

        if (ruoli != null) {
            if (ruoli.contains(roleService.getDeveloper())) {
                return EARoleType.developer;
            } else {
                if (ruoli.contains(roleService.getAdmin())) {
                    return EARoleType.admin;
                } else {
                    if (ruoli.contains(roleService.getUser())) {
                        return EARoleType.user;
                    } else {
                        return EARoleType.guest;
                    }// end of if/else cycle
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if cycle

        return roleType;
    }// end of method


    public boolean isModificato(AEntity entityBean) {
        Milite militeOld = findByKeyUnica(((Milite) entityBean).username);
        return militeOld != null ? !militeOld.equals(entityBean) : true;
    }// end of method


    public String getMessageNuovo(AEntity entityBean) {
        String message = VUOTA;
        String sep = " -> ";
        Milite milite = (Milite) entityBean;

        message += "Milite: ";
        message += milite.id;
        message += A_CAPO;
        message += "nome: ";
        message += milite.nome;
        message += A_CAPO;

        message += "cognome: ";
        message += milite.cognome;
        message += A_CAPO;

        message += "nickname: ";
        message += milite.username;
        message += A_CAPO;

        message += "password: ";
        message += milite.password;
        message += A_CAPO;

        message += "telefono: ";
        message += milite.telefono;
        message += A_CAPO;

        message += "mail: ";
        message += milite.mail;
        message += A_CAPO;

        message += "attivo is set to ";
        message += milite.enabled ? "true" : "false";
        message += A_CAPO;

        message += "admin is set to ";
        message += milite.admin ? "true" : "false";
        message += A_CAPO;

        message += "dipendente is set to ";
        message += milite.dipendente ? "true" : "false";
        message += A_CAPO;

        message += "infermiere is set to ";
        message += milite.infermiere ? "true" : "false";
        message += A_CAPO;

        message += "creatoreTurni is set to ";
        message += milite.creatoreTurni ? "true" : "false";
        message += A_CAPO;

        message += "managerTabellone is set to ";
        message += milite.managerTabellone ? "true" : "false";
        message += A_CAPO;

        message += "funzioni are set to ";
        message += milite.funzioni;
        message += A_CAPO;

        message += "note: ";
        message += milite.noteWam;
        message += A_CAPO;

        return message.trim();
    }// end of method


    public String getMessageModifiche(AEntity entityBean) {
        String message = VUOTA;
        String sep = " -> ";
        String nullo = "null";
        Milite milite = (Milite) entityBean;
        Milite militeOld = findById(milite.id);

        if (militeOld == null) {
            logger.error("Operation errata", this.getClass(), "getMessageModifiche");
            return VUOTA;
        }

        message += "Milite: ";
        message += milite.id;
        message += A_CAPO;
        if (!milite.nome.equals(militeOld.nome)) {
            message += "nome: ";
            message += militeOld.nome;
            message += sep;
            message += milite.nome;
            message += A_CAPO;
        }
        if (!milite.cognome.equals(militeOld.cognome)) {
            message += "cognome: ";
            message += militeOld.cognome;
            message += sep;
            message += milite.cognome;
            message += A_CAPO;
        }
        if (!milite.username.equals(militeOld.username)) {
            message += "nickname: ";
            message += militeOld.username;
            message += sep;
            message += milite.username;
            message += A_CAPO;
        }
        if (!milite.password.equals(militeOld.password)) {
            message += "password: ";
            message += militeOld.password;
            message += sep;
            message += milite.password;
            message += A_CAPO;
        }
        if (text.isValid(milite.telefono) && text.isValid(militeOld.telefono)) {
            if (!milite.telefono.equals(militeOld.telefono)) {
                message += "telefono: ";
                message += militeOld.telefono;
                message += sep;
                message += milite.telefono;
                message += A_CAPO;
            }
        } else {
            if (text.isValid(milite.telefono)) {
                message += "telefono: ";
                message += nullo;
                message += sep;
                message += milite.telefono;
                message += A_CAPO;
            }
            if (text.isValid(militeOld.telefono)) {
                message += "telefono: ";
                message += militeOld.telefono;
                message += sep;
                message += nullo;
                message += A_CAPO;
            }
        }

        if (text.isValid(milite.mail) && text.isValid(militeOld.mail)) {
            if (!milite.mail.equals(militeOld.mail)) {
                message += "mail: ";
                message += militeOld.mail != null ? militeOld.mail : nullo;
                message += sep;
                message += milite.mail != null ? milite.mail : nullo;
                message += A_CAPO;
            }
        } else {
            if (text.isValid(milite.mail)) {
                message += "mail: ";
                message += nullo;
                message += sep;
                message += milite.mail;
                message += A_CAPO;
            }
            if (text.isValid(militeOld.mail)) {
                message += "mail: ";
                message += militeOld.mail;
                message += sep;
                message += nullo;
                message += A_CAPO;
            }
        }

        if (milite.enabled != militeOld.enabled) {
            message += "attivo is now ";
            message += milite.enabled ? "true" : "false";
            message += A_CAPO;
        }
        if (milite.admin != militeOld.admin) {
            message += "admin is now ";
            message += milite.admin ? "true" : "false";
            message += A_CAPO;
        }
        if (milite.dipendente != militeOld.dipendente) {
            message += "dipendente is now ";
            message += milite.dipendente ? "true" : "false";
            message += A_CAPO;
        }
        if (milite.infermiere != militeOld.infermiere) {
            message += "infermiere is now ";
            message += milite.infermiere ? "true" : "false";
            message += A_CAPO;
        }
        if (milite.creatoreTurni != militeOld.creatoreTurni) {
            message += "creatoreTurni is now ";
            message += milite.creatoreTurni ? "true" : "false";
            message += A_CAPO;
        }
        if (milite.managerTabellone != militeOld.managerTabellone) {
            message += "admin is now ";
            message += milite.managerTabellone ? "true" : "false";
            message += A_CAPO;
        }
        if (!milite.funzioni.equals(militeOld.funzioni)) {
            message += "funzioni: ";
            message += militeOld.funzioni != null ? militeOld.funzioni : nullo;
            message += sep;
            message += milite.funzioni != null ? milite.funzioni : nullo;
            message += A_CAPO;
        }

        if (text.isValid(milite.noteWam) && text.isValid(militeOld.noteWam)) {
            if (!milite.noteWam.equals(militeOld.noteWam)) {
                message += "note: ";
                message += militeOld.noteWam;
                message += sep;
                message += milite.noteWam;
                message += A_CAPO;
            }
        } else {
            if (text.isValid(milite.noteWam)) {
                message += "note: ";
                message += nullo;
                message += sep;
                message += milite.noteWam;
                message += A_CAPO;
            }
            if (text.isValid(militeOld.noteWam)) {
                message += "note: ";
                message += militeOld.noteWam;
                message += sep;
                message += nullo;
                message += A_CAPO;
            }
        }

        return message.trim();
    }// end of method

}// end of class