package it.algos.vaadwam.boot;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.application.*;
import it.algos.vaadflow.backend.login.*;
import it.algos.vaadflow.boot.*;
import it.algos.vaadflow.modules.company.*;
import it.algos.vaadflow.modules.preferenza.*;
import it.algos.vaadflow.modules.role.*;
import it.algos.vaadflow.modules.utente.*;
import it.algos.vaadwam.application.*;
import it.algos.vaadwam.data.*;
import it.algos.vaadwam.enumeration.*;
import it.algos.vaadwam.migration.*;
import it.algos.vaadwam.modules.croce.*;
import it.algos.vaadwam.modules.funzione.*;
import it.algos.vaadwam.modules.log.*;
import it.algos.vaadwam.modules.milite.*;
import it.algos.vaadwam.modules.servizio.*;
import it.algos.vaadwam.modules.statistica.*;
import it.algos.vaadwam.modules.turno.*;
import it.algos.vaadwam.tabellone.*;
import it.algos.vaadwam.wam.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.time.*;
import java.util.*;


/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Estende la classe ABoot per le regolazioni iniziali di questa applicazione <br>
 * <p>
 * Running logic after the Spring context has been initialized <br>
 * Parte perché SpringBoot chiama il metodo contextInitialized() <br>
 * Invoca alcuni metodi della superclasse <br>
 * Di norma dovrebbe esserci una sola classe di questo tipo nel programma <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WamBoot extends ABoot {

    private final static String PROJECT_NAME = "WAM";

    private final static String PROJECT_BANNER = "Gestione Ambulanze";

    private final static double PROJECT_VERSION = 1.62;

    private final static LocalDate VERSION_DATE = LocalDate.of(2021, 4, 15);


    /**
     * Iniettata da Spring come 'singleton'
     */
    @Autowired
    public ImportService migration;

    /**
     * Service (@Scope = 'singleton') iniettato da Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    public CroceService croceService;

    /**
     * Service (@Scope = 'singleton') iniettato da Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    public FunzioneService funzioneService;

    /**
     * Service (@Scope = 'singleton') iniettato da Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    public ServizioService servizioService;

    /**
     * Service (@Scope = 'singleton') iniettato da Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    public MiliteService militeService;


    @Autowired
    private UtenteService utenteService;

    //    @Autowired
    private ALogin login;


    @Autowired
    private RoleService roleService;

    /**
     * Iniettata dal costruttore <br>
     */
    private WamVers wamVers;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private WamData wamData;


    /**
     * Costruttore @Autowired <br>
     *
     * @param wamVers Log delle versioni, modifiche e patch installat
     */
    @Autowired
    public WamBoot() {
        super();
        //        this.wamVers = wamVers;
    }// end of Spring constructor

    @PostConstruct
    private void init(){
        log.info("Starting "+PROJECT_NAME+" version "+PROJECT_VERSION+" of "+VERSION_DATE);
    }

    //    /**
    //     * Executed on container startup <br>
    //     * Setup non-UI logic here <br>
    //     * Viene sovrascritto in questa sottoclasse concreta che invoca il metodo super.inizia() <br>
    //     * Nella superclasse vengono effettuate delle regolazioni standard; <br>
    //     * questa sottoclasse concreta può singolarmente modificarle <br>
    //     */
    //    @Override
    //    public void contextInitialized(ServletContextEvent servletContextEvent) {
    ////        super.inizia();
    //    }// end of method


    /**
     * Inizializzazione delle versioni standard di vaadinflow <br>
     * Inizializzazione delle versioni del programma specifico <br>
     * Il metodo può essere sovrascritto per creare le preferenze specifiche dell'applicazione <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void iniziaVersioni() {
        //        wamVers.inizia();
    }// end of method


    /**
     * Riferimento alla sottoclasse specifica di ABoot per utilizzare il metodo sovrascritto resetPreferenze() <br>
     * Il metodo DEVE essere sovrascritto nella sottoclasse specifica <br>
     */
    protected void regolaRiferimenti() {
        preferenzaService.applicationBoot = this;
    }// end of method


    /**
     * Inizializzazione dei dati di alcune collection essenziali per la partenza <br>
     * Il metodo può essere sovrascritto per creare le preferenze specifiche dell'applicazione <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void iniziaDataPreliminari() {
        //--importazioni dal vecchio webambulanze
        if (croceService.isVuoto()) {
//            migration.importOnlyCroci();
            //            migration.importAll();
        }// end of if cycle
    }// end of method


    /**
     * Crea le preferenze standard <br>
     * Se non esistono, le crea <br>
     * Se esistono, NON modifica i valori esistenti <br>
     * Per un reset ai valori di default, c'è il metodo reset() chiamato da preferenzaService <br>
     * Il metodo può essere sovrascritto per creare le preferenze specifiche dell'applicazione <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    public int creaPreferenze() {
        int numPref = super.creaPreferenze();
        List<? extends Company> listaCroci = croceService.findAll();

        for (EAPreferenzaWam eaPref : EAPreferenzaWam.values()) {
            //--se è companySpecifica=true, crea una preferenza per ogni company
            if (eaPref.isCompanySpecifica()) {
                for (Company croce : listaCroci) {
                    numPref = preferenzaService.creaIfNotExist(eaPref, croce) ? numPref + 1 : numPref;
                }// end of for cycle
            } else {
                numPref = preferenzaService.creaIfNotExist(eaPref) ? numPref + 1 : numPref;
            }// end of if/else cycle
        }// end of for cycle

        return numPref;
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
    @Override
    public int resetPreferenze() {
        int numPref = super.resetPreferenze();

        List<? extends Company> listaCroci = croceService.findAll();

        for (EAPreferenzaWam eaPref : EAPreferenzaWam.values()) {
            //--se è companySpecifica=true, crea una preferenza per ogni company
            if (eaPref.isCompanySpecifica()) {
                for (Company croce : listaCroci) {
                    numPref = preferenzaService.crea(eaPref, croce) ? numPref + 1 : numPref;
                }// end of for cycle
            } else {
                numPref = preferenzaService.crea(eaPref) ? numPref + 1 : numPref;
            }// end of if/else cycle
        }// end of for cycle

        return numPref;
    }// end of method

    //    /**
    //     * Eventuali regolazione delle preferenze standard effettuata nella sottoclasse specifica <br>
    //     * Serve per modificare solo per l'applicazione specifica il valore standard della preferenza <br>
    //     * Eventuali modifiche delle preferenze specifiche (che peraltro possono essere modificate all'origine) <br>
    //     * Metodo che DEVE essere sovrascritto <br>
    //     */
    //    @Override
    //    protected void fixPreferenze() {
    ////        pref.saveValue(EAPreferenza.loadUtenti.getCode(), false);
    ////        pref.saveValue(FlowCost.USA_COMPANY, true);
    ////        pref.saveValue(FlowCost.SHOW_COMPANY, false);
    //        usaSecurity = true;
    //    }// end of method


    /**
     * Regola alcune informazioni dell'applicazione <br>
     */
    protected void regolaInfo() {
        /**
         * Controlla se l'applicazione usa il login oppure no <br>
         * Se si usa il login, occorre la classe SecurityConfiguration <br>
         * Se non si usa il login, occorre disabilitare l'Annotation @EnableWebSecurity di SecurityConfiguration <br>
         * Di defaul (per sicurezza) uguale a true <br>
         */
        FlowVar.usaSecurity = true;

        /**
         * Controlla se l'applicazione è multi-company oppure no <br>
         * Di defaul (per sicurezza) uguale a true <br>
         * Deve essere regolato in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.usaCompany = true;

        /**
         * Nome identificativo dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectName = PROJECT_NAME;

        /**
         * Descrizione completa dell'applicazione <br>
         * Usato (eventualmente) nella barra di menu in testa pagina <br>
         */
        FlowVar.projectBanner = PROJECT_BANNER;

        /**
         * Versione dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectVersion = PROJECT_VERSION;

        /**
         * Data della versione dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.versionDate = VERSION_DATE;


        /**
         * Service da usare per recuperare dal mongoDB l'utenza loggata tramite 'username' che è unico <br>
         * Di default UtenteService oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.loginServiceClazz = MiliteService.class;

        /**
         * Classe da usare per gestire le informazioni dell'utenza loggata <br>
         * Di default ALogin oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.loginClazz = WamLogin.class;

        /**
         * Service da usare per recuperare la lista delle Company (o sottoclassi) <br>
         * Di default CompanyService oppure eventuale sottoclasse specializzata per Company particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.companyServiceClazz = CroceService.class;

        /**
         * Nome da usare per recuperare la lista delle Company (o sottoclassi) <br>
         * Di default 'company' oppure eventuale sottoclasse specializzata per Company particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.companyClazzName = "croce";

        /**
         * Path per recuperare dalle risorse un'immagine da inserire nella barra di menu di MainLayout14 <br>
         * Ogni applicazione può modificarla <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.pathLogo = "frontend/images/wam.png";
    }// end of method


    /**
     * Inizializzazione dei dati di alcune collections specifiche sul DB Mongo
     */
    protected void iniziaDataProgettoSpecifico() {
        wamData.fixAllData();
        //        utenteService.deleteAll();
        //
        //        //--patch di accesso come developer
        //        utenteService.creaIfNotExist(croceService.getGAPS(), "gac", "fulvia", roleService.getRoles(EARole.developer), "gac@algos.it");
        //        utenteService.creaIfNotExist(croceService.getDEMO(), "alex", "axel01", roleService.getRoles(EARole.developer), "alex@algos.it");
        //
        //        //--patch di accesso come admin per TUTTE le croci
        //        for (String sigla : EACroce.getValues()) {
        //            utenteService.creaIfNotExist(croceService.findByKeyUnica(sigla), "admin-" + sigla, "fulvia", roleService.getRoles(EARole.admin), "gac@algos.it");
        //        }
    }// end of method


    /**
     * Aggiunge le @Route (view) specifiche di questa applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in BaseCost
     * Vengono aggiunte dopo quelle standard
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    protected void addRouteSpecifiche() {
        //--developer
        //        FlowVar.menuClazzList.add(WamDeveloperView.class);
        //        FlowVar.menuClazzList.add(ImportView.class);
        FlowVar.menuClazzList.add(TurnoList.class);
        //        FlowVar.menuClazzList.add(IscrizioneList.class);
        //        FlowVar.menuClazzList.add(RigaList.class);

        //--admin
        FlowVar.menuClazzList.add(PreferenzaList.class);

        //--utente
        FlowVar.menuClazzList.add(MiliteList.class);
        FlowVar.menuClazzList.add(Tabellone.class);
        FlowVar.menuClazzList.add(StatisticaList.class);
        FlowVar.menuClazzList.add(WamLogList.class);
        FlowVar.menuClazzList.add(FunzioneList.class);
        FlowVar.menuClazzList.add(ServizioList.class);
        FlowVar.menuClazzList.add(CroceList.class);
    }// end of method


    /**
     * Creazione iniziale di una croce demo
     * Visibile a tutti
     * <p>
     * La crea SOLO se non esiste già
     */
    //    public void creaCompanyDemo() {
    //        Croce croce = creaCroceDemo();
    //        initCroce(croce, true, true);
    //    }// end of static method
    //
    /**
     * Creazione iniziale dei dati generali per la croce demo
     * Li crea SOLO se non esistono già
     */
    //    private Croce creaCroceDemo() {
    //        Croce croce = croceService.findOrCrea(DEMO_COMPANY_CODE);
    //
    //        if (croce != null) {
    //            croceService.delete(croce);
    //        }// fine del blocco if
    //
    //        croce = croceService.newEntity(DEMO_COMPANY_CODE, "Company dimostrativa");
    ////        croce.setIndirizzo("Via Turati, 12");
    ////        croce.setAddress1("20199 Garbagnate Milanese");
    ////        croce.setContact("Mario Bianchi");
    //
    //
    //        //--flag vari
    ////        CompanyPrefs.usaGestioneCertificati.put(company, true);
    ////        CompanyPrefs.usaStatisticheSuddivise.put(company, true);
    //
    //        return croce;
    //    }// end of static method

    /**
     * Inizializza una croce appena creata, con alcuni dati di esempio
     * Visibile solo a noi (developer)
     * Crea alcune funzioni standard
     * Crea una lista di volontari di esempio
     * Crea alcuni servizi di esempio
     *
     * @param company croce di appartenenza
     */
    //    public void initCroce(Croce croce) {
    //        initCroce(croce, true, false);
    //    }// end of static method


    /**
     * Inizializza una croce appena creata, con alcuni dati di esempio
     * Visibile solo a noi (developer)
     * Crea alcune funzioni standard
     * Crea una lista di volontari di esempio
     * Crea alcuni servizi di esempio
     * Crea alcuni turni vuoti (opzionale)
     * Crea le iscrizioni per i turni creati (opzionale)
     *
     * @param company   croce di appartenenza
     * @param creaTurni flag per la creazione di turni vuoti
     * @param company   flag per la creazione delle iscrizioni per i turni
     */
    //    public void initCroce(Croce croce, boolean creaTurni, boolean creaIscrizioni) {
    //        ArrayList<Funzione> listaFunzioni;
    //        ArrayList<Servizio> listaServizi;
    //        ArrayList<Volontario> listaVolontari;
    //        ArrayList<Turno> listaTurni = null;
    //        EntityManager manager = EM.createEntityManager();
    //        manager.getTransaction().begin();

    //        creaPreferenze(company);
    //        listaFunzioni = creaFunzioni(company, manager);
    //        creaFunzioniDipendenti(company, manager);
    //        listaServizi = creaServizi(company, manager, listaFunzioni);
    //        listaVolontari = creaVolontari(company, manager, listaFunzioni);

    //        if (creaTurni) {
    //            listaTurni = creaTurniVuoti(company, manager, listaServizi);
    //        }// end of if cycle

    //        if (creaIscrizioni) {
    //            riempieTurni(company, manager, listaTurni, listaVolontari);
    //        }// end of if cycle

    //        manager.getTransaction().commit();
    //        manager.close();

    //        croce.setOrganizzazione(EAOrganizzazione.anpas);
    //        croceService.save(croce);
    //}// end of static method


    //    private User createUser(String email, String firstName, String lastName, String passwordHash, String role,
    //                            String photoUrl, boolean locked) {
    //        User user = new User();
    //        user.setEmail(email);
    //        user.setFirstName(firstName);
    //        user.setLastName(lastName);
    //        user.setPasswordHash(passwordHash);
    //        user.setRole(role);
    //        user.setPhotoUrl(photoUrl);
    //        user.setLocked(locked);
    //        return user;
    //    }

}// end of boot class