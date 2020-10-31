package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.person.EAPerson;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
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
        checkDemo();
    }


    /**
     * Controlla che esista la croce demo <br>
     * Se manca, la crea <br>
     */
    public void checkDemo() {
        Person person;
        Address indirizzo;

        this.creaFunzioni();

        if (service.getDEMO() == null) {
            person = (Person) personService.save(personService.newEntity(EAPerson.gac));
            indirizzo = (Address) addressService.save(addressService.newEntity(EAAddress.algos));
            service.creaIfNotExist(EAOrganizzazione.anpas, person, DEMO, "Associazione di prova", person, "345 678", VUOTA, indirizzo);
        }
    }// end of method

    //    /**
    //     * Creazione di una collezione
    //     */
    //    public void findOrCrea() {
    //        int numRec = 0;
    //
    //        creaCroci();
    //        numRec = service.count();
    //        log.warn("Algos - Creazione dati iniziali: " + numRec + " croci");
    //    }// end of method


    //    /**
    //     * Creazione delle croci
    //     * Solo se non esistono
    //     */
    //    public void creaCroci() {
    ////        service.crea(EAOrganizzazione.anpas, getPer(1), ALGOS, "Algos s.r.l.", getPer(2), "335 475612", "", getInd(1));
    ////        service.crea(EAOrganizzazione.cri, getPer(3), DEMO, "Company di prova", getPer(4), "45 9981333", "", getInd(2));
    ////        service.crea(EAOrganizzazione.csv, getPer(5), TEST, "Altra company", getPer(6), "241 1274355", "", getInd(3));
    //    }// end of method


    /**
     * Creazione delle persone
     */
    public Person getPer(int pos) {
        //        switch (pos) {
        //            case 1:
        //                return personService.newEntity( "Mario",  "Rossi");
        //            case 2:
        //                return personService.newEntity( "Giovanna",  "Tessitori");
        //            case 3:
        //                return personService.newEntity( "Andrea",  "Romagnoli");
        //            case 4:
        //                return personService.newEntity( "Marco",  "Beretta");
        //            case 5:
        //                return personService.newEntity( "Silvana",  "Piccolomini");
        //            case 6:
        //                return personService.newEntity( "Flavia",  "Brigante");
        //        } // end of switch statement
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


    public void creaFunzioni() {
        File regioniCSV = new File("config" + File.separator + "funzioni");
        String path = regioniCSV.getAbsolutePath();
        List<LinkedHashMap<String, String>> mappaCSV;
        String croceTxt = VUOTA;
        Croce croce = null;
        String code = VUOTA;
        String sigla = VUOTA;
        String descrizione = VUOTA;
        String iconaTxt = VUOTA;
        VaadinIcon icona = null;

        mappaCSV = fileService.leggeMappaCSV(path);
        for (LinkedHashMap<String, String> riga : mappaCSV) {
            croceTxt = riga.get("croce");
            croce = text.isValid(croceTxt) ? croceService.findByKeyUnica(croceTxt) : null;
            code = riga.get("code");
            sigla = riga.get("sigla");
            descrizione = riga.get("descrizione");
            iconaTxt = riga.get("icona");
            icona = text.isValid(iconaTxt) ? VaadinIcon.valueOf(iconaTxt) : null;
        }

        try {
            funzioneService.creaIfNotExist(croce, code, sigla, descrizione, icona);
        } catch (Exception unErrore) {
        }
    }// end of method

}// end of class
