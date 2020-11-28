package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.person.EAPerson;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
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
@Qualifier(TAG_CRO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class CroceService extends WamService {


    public final static String CRF = "crf";

    public final static String CRPT = "crpt";

    public final static String PAP = "pap";

    public final static String GAPS = "gaps";

    public final static String DEMO = "demo";


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private CroceRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public CroceService(@Qualifier(TAG_CRO) MongoRepository repository) {
        super(repository);
        super.entityClass = Croce.class;
        this.repository = (CroceRepository) repository;
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

        super.usaDaemon = VUOTA;
        super.lastImport = LAST_IMPORT_CROCI;
        super.durataLastImport = DURATA_IMPORT_CROCI;
        super.eaTempoTypeImport = EATempo.secondi;
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param organizzazione di appartenenza (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contatto       persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param mail           della company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     * @param note           della company (facoltativo)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(EAOrganizzazione organizzazione, Person presidente, String code, String descrizione, Person contatto, String telefono, String mail, Address indirizzo, String note) {
        boolean creata = false;

        if (isMancaByKeyUnica(code)) {
            AEntity entity = save(newEntity(organizzazione, presidente, code, descrizione, contatto, telefono, mail, indirizzo, note));
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
    public Croce newEntity() {
        return newEntity((EAOrganizzazione) null, (Person) null, "", "", (Person) null, "", "", (Address) null, VUOTA);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param organizzazione di appartenenza (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contatto       persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param mail           della company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     * @param note           della company (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Croce newEntity(EAOrganizzazione organizzazione, Person presidente, String code, String descrizione, Person contatto, String telefono, String mail, Address indirizzo, String note) {
        Croce entity;
        Company entityDellaSuperClasseCompany = null;

        entity = findByKeyUnica(code);
        if (entity != null) {
            return findByKeyUnica(code);
        }// end of if cycle

        //--prima viene creata una entity di Company, usando le regolazioni automatiche di quella superclasse.
        entityDellaSuperClasseCompany = companyService.newEntity(code, descrizione, contatto, telefono, mail, indirizzo);

        //--poi vengono ricopiati i valori in Croce
        //--casting dalla superclasse alla classe attuale
        entity = (Croce) super.cast(entityDellaSuperClasseCompany, new Croce());

        //--poi vengono aggiunte le property specifiche di Croce
        //--regola le property di questa classe
        entity.setOrganizzazione(organizzazione != null ? organizzazione : EAOrganizzazione.anpas);
        entity.setPresidente(presidente);

        entity.note = note;

        return entity;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Croce) entityBean).getCode();
    }// end of method


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
        entityBean = companyService.beforeSave(entityBean, operation);
        Croce entity = (Croce) super.beforeSave(entityBean, operation);

        if (text.isEmpty(entity.code)) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Croce findByKeyUnica(String code) {
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
    public List<Croce> findAll() {
        List<Croce> lista = null;
        Croce croce;
        AContext context = null;

        VaadinSession vaadSession = VaadinSession.getCurrent();

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        if (context != null) {
            if (context.getLogin().isDeveloper()) {
                lista = repository.findAllByOrderByCodeAsc();
            } else {
                if (context.getLogin().isAdmin()) {
                    croce = ((WamLogin) context.getLogin()).getCroce();
                    if (croce != null) {
                        lista = new ArrayList<>();
                        lista.add(croce);
                    }// end of if cycle
                }// end of if cycle
            }// end of if/else cycle
        } else {
            lista = repository.findAllByOrderByCodeAsc();
        }// end of if/else cycle

        return lista;
    }// end of method


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Croce> findAllAll() {
        return repository.findAllByOrderByCodeAsc();
    }// end of method


    /**
     * Returns the number of entities available for the current company
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return the number of entities
     */
    public int countByCroce(Croce croce) {
        Long num = repository.count();
        return num.intValue();
    }// end of method


    /**
     * Importazione di dati <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @return true se sono stati importati correttamente
     */
    @Override
    public void importa() {
        long inizio = System.currentTimeMillis();
        migration.importOnlyCroci();
        setLastImport(getCroce(), inizio);
    }// end of method


    /**
     * Creazione di alcuni dati iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * La collezione (filtrata sulla croce) viene svuotata <br>
     * I dati possono essere presi da una Enumeration o creati direttamente <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse che cancella tutta la Collection <br>
     *
     * @return numero di elementi creati
     */
    @Override
    public int reset() {
        this.fixCroceDemo();
        return 0;
    }// end of method


    /**
     * Ricrea ad ogni startup la croce demo e SOLO quella <br>
     */
    public void fixCroceDemo() {
        boolean creata;
        Person presidente;
        Person contatto;
        Address indirizzo;

        if (croceService.isEsisteByKeyUnica(DEMO)) {
            if (!croceService.delete(DEMO)) {
                logger.warn("Non sono riuscito a cancellare la croce Demo");
                return;
            }
        }

        presidente = (Person) personService.save(personService.newEntity(EAPerson.alex));
        contatto = (Person) personService.save(personService.newEntity(EAPerson.gac));
        indirizzo = (Address) addressService.save(addressService.newEntity(EAAddress.algos));
        creata = croceService.creaIfNotExist(EAOrganizzazione.anpas, presidente, DEMO, "Croce di prova", contatto, "345 678499", "info@algos.it", indirizzo, "Croce con dati demo. Si possono liberamente modificare. Vengono ricreati ogni notte.");

        if (!creata) {
            logger.warn("Non sono riuscito a creare la croce Demo");
            return;
        }

        loggerAdmin.reset("Croce demo");
    }


    /**
     * Recupera dal db mongo la croce (se esiste)
     */
    public Croce getCRF() {
        return findByKeyUnica(CRF);
    }// end of method


    /**
     * Recupera dal db mongo la croce (se esiste)
     */
    public Croce getCRPT() {
        return findByKeyUnica(CRPT);
    }// end of method


    /**
     * Recupera dal db mongo la croce (se esiste)
     */
    public Croce getPAP() {
        return findByKeyUnica(PAP);
    }// end of method


    /**
     * Recupera dal db mongo la croce (se esiste)
     */
    public Croce getGAPS() {
        return findByKeyUnica(GAPS);
    }// end of method


    /**
     * Recupera dal db mongo la croce (se esiste)
     */
    public Croce getDEMO() {
        return findByKeyUnica(DEMO);
    }// end of method

}// end of class