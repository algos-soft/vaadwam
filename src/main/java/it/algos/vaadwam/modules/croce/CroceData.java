package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwam.application.WamCost.TAG_CRO;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: lun, 21-mag-2018
 * Time: 18:04
 */
@Slf4j
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CroceData {


    public final static String ALGOS = "algos";

    public final static String DEMO = "demo";

    public final static String TEST = "test";

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public PersonService personService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public CroceService croceService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public AddressService addressService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public AFileService fileService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public FunzioneService funzioneService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public ServizioService servizioService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public MiliteService militeService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public TurnoService turnoService;

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;


    /**
     * Il service iniettato dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia IAService
     * Spring costruisce al volo, quando serve, una implementazione di IAService (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private CroceService service;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public CroceData(@Qualifier(TAG_CRO) IAService service) {
        this.service = (CroceService) service;
    }// end of Spring constructor


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le (eventuali) istanze @Autowired <br>
     * Questo metodo viene chiamato subito dopo che il framework ha terminato l' init() implicito <br>
     * del costruttore e PRIMA di qualsiasi altro metodo <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        //        checkDemo();
    }


    /**
     * Creazione delle persone
     */
    public Person getPer(int pos) {
        return null;
    }// end of method


    /**
     * Creazione degli indirizzi
     */
    public Address getInd(int pos) {
        switch (pos) {
            case 1:
                return addressService.newEntity("Via Soderini, 55", "Milano", "20153");
            case 2:
                return addressService.newEntity("Viale dei tigli, 4", "Firenze", "64312");
            case 3:
                return addressService.newEntity("Piazza Argentina, 6", "Milano", "20103");
        } // end of switch statement
        return null;
    }// end of method


    /**
     * Ricostruisce i dati della croce demo: funzioni, servizi, militi, turni <br>
     */
    public void elabora() {
        croceService.fixCroceDemo();
        funzioneService.resetDemo();
        servizioService.resetDemo();
        militeService.resetDemo();
        turnoService.resetDemo();
    }

}// end of class
