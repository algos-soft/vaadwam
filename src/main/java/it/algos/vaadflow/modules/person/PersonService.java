package it.algos.vaadflow.modules.person;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.preferenza.EAPreferenza;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PER;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;

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


/**
 * La newEntity() usa il metodo newEntity() della superclasse per usare 'builderUtente' <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_PER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PersonService extends AService {

    public final static List<String> PROPERTIES_SECURED =
            Arrays.asList("userName", "passwordInChiaro", "locked", "nome", "cognome", "telefono", "mail", "indirizzo");

    public final static List<String> PROPERTIES_NOT_SECURED =
            Arrays.asList("nome", "cognome", "telefono",  "indirizzo");

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected UtenteService utenteService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AddressService addressService;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private PersonRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public PersonService(@Qualifier(TAG_PER) MongoRepository repository) {
        super(repository);
        super.entityClass = Person.class;
        this.repository = (PersonRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param eaPerson: enumeration di dati iniziali di prova
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(EAPerson eaPerson) {
        boolean creata = false;

        if (true) {//@todo da inventare
            AEntity entity = save(newEntity(eaPerson));
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
    public Person newEntity() {
        return newEntity((Company) null, "", "", "", (Address) null, "", "", (List<Role>) null, "", false, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Non usa le properties di security della superclasse Utente
     * Usato come record 'embedded' in altre classi (Company,...)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntityConSuperclasse() {
        return newEntity((Company) null, "", "", "", (Address) null, "", "", (List<Role>) null, "", false, true);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Usa una enumeration di dati iniziali di prova <br>
     *
     * @param eaPerson: enumeration di dati iniziali di prova
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(EAPerson eaPerson) {
        String nome;
        String cognome;
        String telefono;
        EAAddress address;
        Address indirizzo;
        String mail;

        nome = eaPerson.getNome();
        cognome = eaPerson.getCognome();
        telefono = eaPerson.getTelefono();
        address = eaPerson.getAddress();
        indirizzo = addressService.newEntity(address);
        mail = eaPerson.getMail();

        return newEntity(nome, cognome, telefono, indirizzo, mail);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     *
     * @param nome:      (obbligatorio, non unico)
     * @param cognome:   (obbligatorio, non unico)
     * @param telefono:  (facoltativo)
     * @param indirizzo: via, nome e numero (facoltativo)
     * @param mail       posta elettronica (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(String nome, String cognome, String telefono, Address indirizzo, String mail) {
        return newEntity((Company) null, nome, cognome, telefono, indirizzo, "", "", (List<Role>) null, "", false, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param company          di appartenenza (facoltativa)
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param telefono:        (facoltativo)
     * @param indirizzo:       via, nome e numero (facoltativo)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param enabled          flag enabled (facoltativo, di default true)
     * @param usaSuperClasse   (transient) per utilizzare le properties di Security della superclasse Utente (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(
            Company company,
            String nome,
            String cognome,
            String telefono,
            Address indirizzo,
            String userName,
            String passwordInChiaro,
            List<Role> ruoli,
            String mail,
            boolean enabled,
            boolean usaSuperClasse) {
        Person entity = null;
        Utente entityDellaSuperClasseUtente = null;

        //--controlla il flag passato come parametro e specifico di questa entity (entity embedded non usano Utente)
        //--controlla il flag generale dell'applicazione
        //--se usa la security, la persona eredità tutte le property della superclasse Utente
        //--prima viene creata una entity di Utente, usando le regolazioni automatiche di quella superclasse.
        //--poi vengono ricopiati i valori in Persona
        //--poi vengono aggiunte le property specifiche di Persona
        //--se non usa la security, utilizza il metodo builderPerson
        if (usaSuperClasse && usaSecurity) {
            //--prima viene creata una entity di Utente, usando le regolazioni automatiche di quella superclasse.
            entityDellaSuperClasseUtente = utenteService.newEntity(company, userName, passwordInChiaro, ruoli, mail, enabled);

            //--poi vengono ricopiati i valori in Persona
            //--casting dalla superclasse alla classe attuale
            entity = (Person) super.cast(entityDellaSuperClasseUtente, new Person());
            entity.usaSuperClasse = true;
        } else {
            entity = Person.builderPerson().build();
            entity.usaSuperClasse = false;
        }// end of if/else cycle

        //--poi vengono aggiunte le property specifiche di Persona
        //--regola le property di questa classe
        entity.setNome(text.isValid(nome) ? nome : null);
        entity.setCognome(text.isValid(cognome) ? cognome : null);
        entity.setTelefono(text.isValid(telefono) ? telefono : null);
        entity.setIndirizzo(indirizzo);

        return (Person) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        Person persona = (Person) entityBean;
        String nome = persona.nome != null ? persona.nome : "";
        String cognome = persona.cognome != null ? persona.cognome : "";

        return usaSecurity ? utenteService.getPropertyUnica(entityBean) : nome + cognome;
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
        if (((Person) entityBean).usaSuperClasse) {
            entityBean = utenteService.beforeSave(entityBean, operation);
        }// end of if cycle
        Person entity = (Person) super.beforeSave(entityBean, operation);

        if (entity == null) {
            log.error("entity è nullo in PersonService.beforeSave()");
            return null;
        }// end of if cycle

        if (text.isValid(entity.nome)) {
            entity.nome = text.primaMaiuscola(entity.nome);
        }// end of if cycle

        if (text.isValid(entity.cognome)) {
            entity.cognome = text.primaMaiuscola(entity.cognome);
        }// end of if cycle

        if (text.isEmpty(entity.nome) && text.isEmpty(entity.cognome)) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titolo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Person findByKeyUnica(String titolo) {
        return null;
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

        for (EAPerson eaPerson : EAPerson.values()) {
            numRec = creaIfNotExist(eaPerson) ? numRec + 1 : numRec;
        }// end of for cycle

        return numRec;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList(AContext context) {
        return usaSecurity ? PROPERTIES_SECURED : PROPERTIES_NOT_SECURED;
    }// end of method

//
//    /**
//     * Costruisce una lista di nomi delle properties del Form nell'ordine:
//     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
//     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
//     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
//     *
//     * @return lista di nomi di properties
//     */
//    @Override
//    public List<String> getFormPropertyNamesList( AContext context) {
//        return usaSuperClasse ? PROPERTIES_SECURED : PROPERTIES_NOT_SECURED;
//    }// end of method
//
}// end of class