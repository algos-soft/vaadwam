package it.algos.vaadflow.boot;

import it.algos.vaadflow.backend.data.FlowData;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ABootService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 06-mag-2018
 * Time: 18:43
 * <p>
 * Running logic after the Spring context has been initialized
 * The method onApplicationEvent() will be executed nella sottoclasse before the application is up and <br>
 * <p>
 * Aggiunge tutte le @Route (views) standard e specifiche di questa applicazione <br>
 * <p>
 * Not annotated with @SpringComponent (SpringBoot crea la sottoclasse concreta) <br>
 * Not annotated with @Scope (inutile) <br>
 */
@Slf4j
public abstract class ABoot implements ServletContextListener {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ABootService boot;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AMongoService mongo;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ATextService text;


    //    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
//    @Autowired
//    private UtenteService utente;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
////    @Autowired
////    private AddressService address;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
////    @Autowired
////    private PersonService person;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
////    @Autowired
////    private CompanyService company;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
//    @Autowired
//    private LogtypeService logtype;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
//    @Autowired
//    private SecoloService secolo;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
//    @Autowired
//    private MeseService mese;
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     */
//    @Autowired
//    private AnnoService anno;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService preferenzaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private FlowData flowData;


    /**
     * Executed on container startup
     * Setup non-UI logic here
     * Utilizzato per:
     * - registrare nella xxxApp, il servlet context non appena è disponibile
     * - regolare alcuni flag dell'applicazione, uguali e validi per tutte le sessioni e tutte le request <br>
     * - lanciare gli schedulers in background <br>
     * - costruire e regolare una versione demo <br>
     * - controllare l'esistenza di utenti abilitati all'accesso <br>
     * Running logic after the Spring context has been initialized
     * Any class that use this @EventListener annotation,
     * will be executed before the application is up and its onApplicationEvent method will be called
     * <p>
     * Viene normalmente creata una sottoclasse per l'applicazione specifica:
     * - per regolare eventualmente alcuni flag in maniera non standard
     * - lanciare gli schedulers in background <br>
     * - costruire e regolare una versione demo <br>
     * - controllare l'esistenza di utenti abilitati all'accesso <br>
     * <p>
     * Stampa a video (productionMode) i valori per controllo
     * Deve essere sovrascritto dalla sottoclasse concreta che invocherà questo metodo()
     */
    protected void inizia() {
        this.iniziaDBMongo();
        this.iniziaVersioni();
        this.regolaRiferimenti();
        this.creaPreferenze();
        fixPreferenze();
        this.iniziaDataStandard();
        this.iniziaDataProgettoSpecifico();
        this.regolaInfo();
        this.addRouteStandard();
        this.addRouteSpecifiche();
    }// end of method


    /**
     * Inizializzazione di alcuni parametri del database mongoDB <br>
     */
    protected void iniziaDBMongo() {
        mongo.getMaxBlockingSortBytes();
        mongo.fixMaxBytes();
        mongo.getMaxBlockingSortBytes();
    }// end of method


    /**
     * Inizializzazione delle versioni standard di vaadinflow <br>
     * Inizializzazione delle versioni del programma specifico <br>
     */
    protected void iniziaVersioni() {
    }// end of method


    /**
     * Riferimento alla sottoclasse specifica di ABoot per utilizzare il metodo sovrascritto resetPreferenze() <br>
     * Il metodo DEVE essere sovrascritto nella sottoclasse specifica <br>
     */
    protected void regolaRiferimenti() {
    }// end of method


    /**
     * Crea le preferenze standard <br>
     * Se non esistono, le crea <br>
     * Se esistono, NON modifica i valori esistenti <br>
     * Per un reset ai valori di default, c'è il metodo reset() chiamato da preferenzaService <br>
     * Il metodo può essere sovrascritto per creare le preferenze specifiche dell'applicazione <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    public int creaPreferenze() {
        int numPref = 0;

        for (EAPreferenza eaPref : EAPreferenza.values()) {
            numPref = preferenzaService.creaIfNotExist(eaPref) ? numPref + 1 : numPref;
        }// end of for cycle

        return numPref;
    }// end of method

    /**
     * Eventuali regolazioni delle preferenze standard effettuata nella sottoclasse specifica <br>
     * Serve per modificare solo per l'applicazione specifica il valore standard della preferenza <br>
     * Eventuali modifiche delle preferenze specifiche (che peraltro possono essere modificate all'origine) <br>
     * Metodo che DEVE essere sovrascritto <br>
     */
    protected void fixPreferenze() {
    }// end of method

    /**
     * Cancella e ricrea le preferenze standard <br>
     * Metodo invocato dal metodo reset() di preferenzeService per poter usufruire della sovrascrittura
     * nella sottoclasse specifica dell'applicazione <br>
     * Il metodo può essere sovrascitto per ricreare le preferenze specifiche dell'applicazione <br>
     * Le preferenze standard sono create dalla enumeration EAPreferenza <br>
     * Le preferenze specifiche possono essere create da una Enumeration specifica, oppure singolarmente <br>
     * Invocare PRIMA il metodo della superclasse <br>
     *
     * @return numero di preferenze creato
     */
    public int resetPreferenze() {
        int numPref = 0;

        //--cancella tutte le preferenze
        preferenzaService.deleteAll();

        //--ricrea tutte le preferenze standard coi valori di default iniziali
        for (EAPreferenza eaPref : EAPreferenza.values()) {
            numPref = preferenzaService.crea(eaPref) ? numPref + 1 : numPref;
        }// end of for cycle

        return numPref;
    }// end of method


    /**
     * Regola alcune informazioni dell'applicazione
     */
    protected void regolaInfo() {
    }// end of method


    /**
     * Inizializzazione dei dati di alcune collections standard sul DB mongo <br>
     */
    private void iniziaDataStandard() {
        flowData.loadAllData();
    }// end of method


    /**
     * Inizializzazione dei dati di alcune collections specifiche sul DB mongo
     */
    protected void iniziaDataProgettoSpecifico() {
    }// end of method


    /**
     * Questa classe viene invocata PRIMA della chiamata del browser
     * Se NON usa la security, le @Route vengono create solo qui
     * Se USA la security, le @Route vengono sovrascritte all'apertura del brose nella classe AUserDetailsService
     * <p>
     * Aggiunge le @Route (view) standard
     * Nella sottoclasse concreta che invoca questo metodo, aggiunge le @Route (view) specifiche dell'applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in BaseCost
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    private void addRouteStandard() {
        boot.creaRouteStandard();
    }// end of method


    /**
     * Questa classe viene invocata PRIMA della chiamata del browser
     * Se NON usa la security, le @Route vengono create solo qui
     * Se USA la security, le @Route vengono sovrascritte all'apertura del brose nella classe AUserDetailsService
     * <p>
     * Aggiunge le @Route (view) specifiche di questa applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in BaseCost
     * Vengono aggiunte dopo quelle standard
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    protected void addRouteSpecifiche() {
    }// end of method


    /**
     * Executed on container shutdown
     * <p>
     * Clean stuff here <br>
     * Può essere sovrascritta dalla sottoclasse <br>
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo) <br>
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }// end of method


}// end of class