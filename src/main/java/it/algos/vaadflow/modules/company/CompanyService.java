package it.algos.vaadflow.modules.company;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.person.EAPerson;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_COM;

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
@Qualifier(TAG_COM)
@Slf4j
@AIScript(sovrascrivibile = false)
public class CompanyService extends AService {

    /**
     * Costanti usate per identificare alcune company
     */
    private static final String ALGOS = "algos";

    private static final String DEMO = "algos";

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AddressService addressService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PersonService personService;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private CompanyRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public CompanyService(@Qualifier(TAG_COM) MongoRepository repository) {
        super(repository);
        super.entityClass = Company.class;
        this.repository = (CompanyRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param eaCompany: enumeration di dati iniziali di prova
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(EACompany eaCompany) {
        boolean creata = false;

        if (isMancaByKeyUnica(eaCompany.getCode())) {
            AEntity entity = save(newEntity(eaCompany));
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
    public Company newEntity() {
        return newEntity("", "", (Person) null, "", "", (Address) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Usa una enumeration di dati iniziali di prova <br>
     *
     * @param eaCompany: enumeration di dati iniziali di prova
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Company newEntity(EACompany eaCompany) {
        String code;
        String descrizione;
        EAPerson eaPerson;
        Person contatto;
        String telefono;
        String mail;
        EAAddress eaAddress;
        Address indirizzo;

        code = eaCompany.getCode();
        descrizione = eaCompany.getDescrizione();
        eaPerson = eaCompany.getPerson();
        contatto = personService.newEntity(eaPerson);
        telefono = eaCompany.getTelefono();
        mail = eaCompany.getEmail();
        eaAddress = eaCompany.getAddress();
        indirizzo = addressService.newEntity(eaAddress);

        return newEntity(code, descrizione, contatto, telefono, mail, indirizzo);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param code        di riferimento interno (obbligatorio ed unico)
     * @param descrizione ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contatto    persona di riferimento (facoltativo)
     * @param telefono    della company (facoltativo)
     * @param mail        della company (facoltativo)
     * @param indirizzo   della company (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Company newEntity(String code, String descrizione, Person contatto, String telefono, String mail, Address indirizzo) {
        return Company.builderCompany()
                .code(text.isValid(code) ? code : null)
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .contatto(contatto)
                .telefono(text.isValid(telefono) ? telefono : null)
                .mail(text.isValid(mail) ? mail : null)
                .indirizzo(indirizzo)
                .build();
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Company) entityBean).getCode();
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
        Company entity = (Company) super.beforeSave(entityBean, operation);

        if (text.isValid(entity.descrizione)) {
            entity.descrizione = text.primaMaiuscola(entity.descrizione);
        }// end entityBean if cycle

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
    public Company findByKeyUnica(String code) {
        return repository.findByCode(code);
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
        List<AEntity> listaCompanies = null;
        ALogin login = getLogin();
        Company company = null;

        if (login.isDeveloper()) {
            return super.findAll();
        } else {
            company = getContext().getCompany();
            if (company != null) {
                listaCompanies = new ArrayList<>();
                listaCompanies.add(company);
            }// end of if cycle
            return listaCompanies;
        }// end of if/else cycle

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

        for (EACompany eaCompany : EACompany.values()) {
            numRec = creaIfNotExist(eaCompany) ? numRec + 1 : numRec;
        }// end of for cycle

        return numRec;
    }// end of method


    /**
     * Recupera dal db mongo la company (se esiste)
     */
    public Company getAlgos() {
        return repository.findByCode(ALGOS);
    }// end of method


    /**
     * Recupera dal db mongo la company (se esiste)
     */
    public Company getDemo() {
        return repository.findByCode(DEMO);
    }// end of method

}// end of class