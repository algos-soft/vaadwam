package it.algos.vaadwam.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.boot.ABoot;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.preferenza.PreferenzaList;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.migration.ImportService;
import it.algos.vaadwam.migration.ImportView;
import it.algos.vaadwam.modules.croce.CroceList;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.FunzioneList;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.IscrizioneList;
import it.algos.vaadwam.modules.log.WamLogList;
import it.algos.vaadwam.modules.milite.MiliteList;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.riga.RigaList;
import it.algos.vaadwam.modules.servizio.ServizioList;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.statistica.StatisticaList;
import it.algos.vaadwam.modules.turno.TurnoList;
import it.algos.vaadwam.tabellone.Tabellone;
import it.algos.vaadwam.wam.WamLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.util.List;


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
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WamBoot extends ABoot {

    private final static String PROJECT_NAME = "wam";

    private final static String PROJECT_BANNER = "Gestione Ambulanze";

    private final static double PROJECT_VERSION = 1.0;

    private final static LocalDate VERSION_DATE = LocalDate.of(2020, 6, 19);

    /**
     * Inietta da Spring come 'singleton'
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
            migration.importOnlyCroci();
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


        // Avvia lo schedulatore che esegue i schedule periodici sul server
//        scheduler.start();

        // Qui eventuali revisioni dei dati in funzione della versione

//        //--Esegue dei controlli iniziali per ogni Company
//        List<WamCompany> comps = WamCompany.findAll();
//        for (WamCompany company : comps) {
//            doForCompany(company);
//        }// end of for cycle


//        uno(EACompany.algos, "gac", "fulvia", EARole.developer, "gac@algos.it"),
//                due(EACompany.algos, "alex", "axel01", EARole.developer, "alex@algos.it"),
//                tre(EACompany.demo, "admin", "admin", EARole.admin, "info@algos.it"),
//                quattro((EACompany) null, "anonymous", "anonymous", EARole.user, ""),
//                cinque(EACompany.test, "Addabbo Andrea", "addabbo123", EARole.user, "");

//        //--importazioni dal vecchio webambulanze
//        if (croceService.isVuoto()) {
//            migration.importOnlyCroci();
//        }// end of if cycle

        if (funzioneService.isVuoto()) {
//            migration.importFunzioni();
        }// end of if cycle

        if (servizioService.isVuoto()) {
//            migration.importServizi();
        }// end of if cycle

        if (militeService.isVuoto()) {
//            migration.importMiliti();
        }// end of if cycle


        //--patch di accesso
        utenteService.creaIfNotExist(croceService.getGAPS(), "gac", "fulvia", roleService.getRoles(EARole.developer), "gac@algos.it");
        militeService.creaIfNotExist(croceService.getGAPS(), "gac", "gac", "Guido", "fulvia", roleService.getRoles(EARole.developer));
//        utenteService.creaIfNotExist(croceService.getGAPS(), "Guido Ceresa", "fulvia", roleService.getRoles(EARole.developer), "gac@algos.it");
//        utenteService.creaIfNotExist(croceService.getGAPS(), "Rino Olivieri", "rino123", roleService.getRoles(EARole.admin), "gac@algos.it");
//        utenteService.creaIfNotExist(croceService.getGAPS(), "Enrico Delfanti", "enrico123", roleService.getRoles(EARole.user), "gac@algos.it");
        utenteService.creaIfNotExist(croceService.getCRPT(), "alex", "axel01", roleService.getRoles(EARole.developer), "alex@algos.it");
//        utenteService.creaIfNotExist(croceService.getCRPT(), "admin", "admin", roleService.getRoles(EARole.admin), "");
//        utenteService.creaIfNotExist(croceService.getCRF(), "Addabbo Andrea", "addabbo123", roleService.getRoles(EARole.user), "");
//        utenteService.creaIfNotExist(croceService.getCRF(), "Porcari Stefano", "7777", roleService.getRoles(EARole.admin), "");
//        utenteService.creaIfNotExist(croceService.getPAP(), "Piana Silvano", "piana987", roleService.getRoles(EARole.admin), "");
//        utenteService.creaIfNotExist(croceService.getCRPT(), "Michelini Mauro", "ginevracrpt", roleService.getRoles(EARole.admin), "");

//        Utente utente = (Utente) utenteService.findById("Addabbo Andrea");
//        utente.company = croceService.getCRF();
//        utenteService.save(utente);
//        utente = (Utente) utenteService.findById("admin");
//        utente.company = croceService.getCRPT();
//        utenteService.save(utente);
//        utente = (Utente) utenteService.findById("gac");
//        utente.company = croceService.getPAP();
//        utenteService.save(utente);

//        this.wamData.loadAllData();
    }// end of method

//    private User createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return userRepository.save(createUser("admin@vaadin.com", "Göran", "Rich", passwordEncoder.encode("admin"),
//                Role.ADMIN, "https://randomuser.me/api/portraits/men/34.jpg", true));
//    }


    /**
     * Aggiunge le @Route (view) specifiche di questa applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in BaseCost
     * Vengono aggiunte dopo quelle standard
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    protected void addRouteSpecifiche() {
        //--developer
        FlowVar.menuClazzList.add(WamDeveloperView.class);
        FlowVar.menuClazzList.add(ImportView.class);
        FlowVar.menuClazzList.add(TurnoList.class);
        FlowVar.menuClazzList.add(IscrizioneList.class);
        FlowVar.menuClazzList.add(RigaList.class);

        //--admin
        FlowVar.menuClazzList.add(WamLogList.class);
        FlowVar.menuClazzList.add(PreferenzaList.class);
        FlowVar.menuClazzList.add(CroceList.class);

        //--utente
        FlowVar.menuClazzList.add(Tabellone.class);
        FlowVar.menuClazzList.add(FunzioneList.class);
        FlowVar.menuClazzList.add(ServizioList.class);
        FlowVar.menuClazzList.add(MiliteList.class);
        FlowVar.menuClazzList.add(StatisticaList.class);
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