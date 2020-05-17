package it.algos.vaadwam.wam;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.company.CompanyService;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.migration.MigrationService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.KEY_SECURITY_CONTEXT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: lun, 03-set-2018
 * Time: 06:54
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_CRO)
@Slf4j
public abstract class WamService extends AService {

    public final static String FIELD_NAME_CROCE = "croce";

    public String usaDaemon;

    public String lastImport;

    public String durataLastImport;

    public EATempo eaTempoTypeImport;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AMongoService mongo;

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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected CompanyService companyService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected CroceService croceService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected MigrationService migration;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected MiliteService militeService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected UtenteService utenteService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected TurnoService turnoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    //    /**
    //     * Istanza unica di una classe di servizio: <br>
    //     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
    //     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
    //     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
    //     */
    //    @Autowired
    //    @Qualifier(TAG_CRO)
    //    protected WamService wamService;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    protected WamLogin wamLogin;


    /**
     * Costruttore <br>
     *
     * @param repository per la persistenza dei dati
     */
    public WamService(MongoRepository repository) {
        super(repository);
    }// end of Spring constructor


    /**
     * Preferenze specifiche di questo service <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.usaDaemon = USA_DAEMON_CROCI;
        this.lastImport = VUOTA;
        this.durataLastImport = VUOTA;
        this.eaTempoTypeImport = EATempo.nessuno;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList(AContext context) {
        List<String> lista = super.getGridPropertyNamesList(context);

        if (context.getLogin() != null && context.getLogin().isDeveloper()) {
            lista.add(0, "id");
        }// end of if cycle

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
        ArrayList<String> lista = annotation.getFormPropertiesName(entityClass);

        if (context.getLogin().isDeveloper()) {
            lista.add(0, FIELD_NAME_ID);
            lista.add(1, FIELD_NAME_CROCE);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Se nella nuova entity manca la croce, la recupera dal login
     * Se la croce manca, lancia l'eccezione
     *
     * @param entityBean da creare
     */
    protected AEntity addCroce(AEntity entityBean) {
        return addCroce(entityBean, null);
    }// end of method


    /**
     * Se nella nuova entity manca la croce, la recupera dal wam-login
     * Se la croce manca, lancia l'eccezione
     *
     * @param entityBean da creare
     */
    protected AEntity addCroce(AEntity entityBean, Croce croce) {
        if (croce == null) {
            croce = this.getWamCroce();
        }// end of if cycle

        if (croce != null) {
            reflection.setPropertyValue(entityBean, PROPERTY_CROCE, croce);
        } else {
            log.error("Algos- Manca la croce (obbligatoria) di " + entityBean.toString() + " della classe " + entityBean.getClass().getSimpleName());
        }// end of if/else cycle

        return creaIdKeySpecifica(entityBean);
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
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    @Override
    public String addKeyCompany(AEntity entityBean, String keyCode) {
        String keyUnica = "";
        Croce croce = null;
        String companyCode = "";

        if (usaCompany()) {
            if ((reflection.isEsiste(entityBean.getClass(), PROPERTY_CROCE))) {
                croce = (Croce) reflection.getPropertyValue(entityBean, PROPERTY_CROCE);
                if (croce != null) {
                    companyCode = croce.getCode();
                }// end of if cycle
            }// end of if cycle

            //            if (text.isEmpty(companyCode)) {
            //                companyCode = getCompanyCode();
            //            }// end of if cycle

            if (text.isValid(companyCode)) {
                keyUnica = companyCode + text.primaMaiuscola(keyCode);
            } else {
                if (annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria) {
                    keyUnica = null;
                } else {
                    keyUnica = keyCode;
                }// end of if/else cycle
            }// end of if/else cycle
        } else {
            keyUnica = keyCode;
        }// end of if/else cycle

        return keyUnica;
    }// end of method


    //    /**
    //     * Recupera la sigla della company della session corrente (se esiste) <br>
    //     * Controlla che la session sia attiva <br>
    //     *
    //     * @return context della sessione
    //     */
    //    public String getCompanyCode() {
    //        String code = "";
    //        Croce croce = getCroce();
    //
    //        if (croce != null) {
    //            code = croce.getCode();
    //        }// end of if cycle
    //
    //        return code;
    //    }// end of method


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Override
    public int count() {
        int numRec = 0;
        Croce croce = getCroce();

        if (croce != null) {
            numRec = countByCroce(croce);
        } else {
            numRec = super.count();
        }// end of if/else cycle

        return numRec;
    }// end of method


    /**
     * Controlla la collezione
     *
     * @return true se non ci sono elementi
     */
    public boolean isVuoto() {
        return count() == 0;
    }// end of method


    /**
     * Returns the number of entities available for the current company
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return the number of entities
     */
    public int countByCroce(Croce croce) {
        return 0;
    }// end of method


    public Croce getCroce(AEntity entityBean) {
        Croce croce = null;
        Object obj = null;

        if (entityBean != null && reflection.isEsiste(entityClass, FIELD_NAME_CROCE)) {
            obj = reflection.getPropertyValue(entityBean, FIELD_NAME_CROCE);
        }// end of if cycle

        if (obj != null && obj instanceof Croce) {
            croce = (Croce) obj;
        }// end of if cycle

        return croce != null ? croce : getCroce();
    }// end of method


    public Croce getCroce() {
        Croce croce = null;
        WamLogin wamLogin = getWamLogin();

        if (wamLogin != null) {
            croce = wamLogin.getCroce();
        }// end of if cycle

        return croce;
    }// end of method


    public String getCompanyCode(AEntity entityBean) {
        String code = "";
        Croce croce = getCroce(entityBean);

        if (croce != null) {
            code = croce.getCode();
        }// end of if cycle

        return code;
    }// end of method


    /**
     * Returns all instances of the selected Croce <br>
     *
     * @return lista ordinata di tutte le entities della croce
     */
    @Override
    public List<? extends AEntity> findAll() {
        return findAllByCroce(getCroce());
    }// end of method


    /**
     * Returns all instances of the selected Croce <br>
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return lista ordinata di tutte le entities della croce
     */
    public List<? extends AEntity> findAllByCroce(Croce croce) {
        return null;
    }// end of method


    /**
     * Returns all instances <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<? extends AEntity> findAllCroci() {
        return null;
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico all'interno della croce), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param croce di appartenenza (obbligatoria)
     */
    public int getNewOrdine(Croce croce) {
        int ordine = 0;
        List<? extends AEntity> lista = null;
        AEntity entity = null;
        Object obj = null;

        if (croce == null) {
            croce = getCroce();
        }// end of if cycle

        if (reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
            lista = findAllByCroce(croce);
        }// end of if cycle

        if (array.isValid(lista)) {
            entity = lista.get(lista.size() - 1);
        }// end of if cycle

        if (entity != null) {
            obj = reflection.getPropertyValue(entity, FIELD_NAME_ORDINE);
        }// end of if cycle

        if (obj != null && obj instanceof Integer) {
            ordine = (int) obj;
        }// end of if cycle

        return ++ordine;
    }// end of method


    /**
     * Importazione di dati <br>
     *
     * @return true se sono stati importati correttamente
     */
    public void importa() {
        fixWamLogin();

        if (wamLogin != null && wamLogin.getCroce() != null) {
            importa(getCroce());
        }// end of if cycle
    }// end of method


    /**
     * Importazione di dati <br>
     *
     * @return true se sono stati importati correttamente
     */
    public boolean importa(Croce croce) {
        return false;
    }// end of method


    public String importAll() {
        boolean eseguito = true;

        for (Croce croce : croceService.findAll()) {
            eseguito = eseguito && importa(croce);
        }

        return eseguito ? "Fatto" : "Import non riuscito";
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo import effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo import effettuato <br>
     */
    protected void setLastImport(Croce croce, long inizio) {
        setLastImport(croce, inizio, lastImport, durataLastImport, eaTempoTypeImport);
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo import effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo import effettuato <br>
     */
    protected void setLastImport(Croce croce, long inizio, String lastImport, String durataLastImport, EATempo eaTempoTypeImport) {
        pref.saveValue(lastImport, LocalDateTime.now(), croce.code);
        pref.saveValue(durataLastImport, eaTempoTypeImport.get(inizio), croce.code);
    }// end of method


    /**
     * Deletes all entities of the collection.
     */
    @Override
    public boolean deleteAll() {
        Croce croce = getCroce();

        if (WamEntity.class.isAssignableFrom(entityClass)) {
            if (croce != null) {
                super.deleteByProperty(entityClass, "croce", croce);
            } else {
                if (wamLogin != null && wamLogin.isDeveloper()) {
                    mongo.drop(entityClass);
                }// end of if cycle
                super.deleteByProperty(entityClass, "croce", croce);
            }// end of if/else cycle
        }// end of if cycle

        return false;
    }// end of method


    /**
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public void deleteAllCroce() {
        deleteAllCroce(getCroce());
    }// end of method


    /**
     * Deletes a given entity.
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public void deleteAllCroce(Croce croce) {
        if (WamEntity.class.isAssignableFrom(entityClass) || Milite.class.isAssignableFrom(entityClass)) {
            super.deleteByProperty(entityClass, "croce", croce);
        }// end of if cycle
    }// end of method


    /**
     * Deletes a given entity.
     *
     * @param croce di appartenenza (obbligatoria)
     * @param anno  di riferimento (obbligatorio)
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public void deleteAllCroceAnno(Croce croce, int anno) {
        List<Turno> listaTurniAnno;
        int cancellati = 0;
        if (WamEntity.class.isAssignableFrom(entityClass) || Milite.class.isAssignableFrom(entityClass)) {
            if (croce != null && anno > 0) {
                listaTurniAnno = turnoService.findAllByYear(croce, anno);
                cancellati = super.delete(listaTurniAnno, entityClass);
            }// end of if cycle
        }// end of if cycle
        System.out.println(cancellati);
    }// end of method


    /**
     * Crea il wam-login <br>
     * Controlla che non esista già il wam-login nella vaadSession
     * Invocato quando la @route fa partire la AViewList. <br>
     * (non è chiaro se passa prima da MainLayout o da AViewList o da AViewDialog) <br>
     * <p>
     * Recupera l'user dall'attributo della sessione HttpSession al termine della security <br>
     * Crea il wam-login <br>
     * Inserisce il wam-login come attributo nella vaadSession <br>
     */
    public WamLogin fixWamLogin() {
        //        WamLogin wamLogin;
        ALogin login = getLogin();
        VaadinSession vaadSession;
        User springUser;
        Utente utente = null;
        String uniqueUserName = "";
        Croce croce = null;
        Croce company = null;
        Milite milite;

        if (login != null && login.getUtente() != null && text.isValid(login.getUtente().username)) {
            uniqueUserName = login.getUtente().username;
        } else {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession httpSession = attr.getRequest().getSession(true);
            SecurityContext securityContext = (SecurityContext) httpSession.getAttribute(KEY_SECURITY_CONTEXT);
            if (securityContext != null) {
                springUser = (User) securityContext.getAuthentication().getPrincipal();
                uniqueUserName = springUser.getUsername();
            } else {
                return null;
            }// end of if/else cycle


            //            springUser = (User) securityContext.getAuthentication().getPrincipal();
            //            uniqueUserName = springUser.getUsername();

            //            springUser = securityContext != null ? (User) securityContext.getAuthentication().getPrincipal() : null;
            //            uniqueUserName = springUser != null ? springUser.getUsername() : "";
        }// end of if/else cycle

        vaadSession = UI.getCurrent().getSession();
        wamLogin = (WamLogin) vaadSession.getAttribute(KEY_WAM_CONTEXT);
        if (wamLogin == null) {
            wamLogin = appContext.getBean(WamLogin.class);

            milite = militeService.findById(uniqueUserName);
            croce = milite != null ? milite.croce : null;

            //--Se entro come gac non sono registrato come Milite
            if (utente == null) {
                utente = utenteService.findByKeyUnica(uniqueUserName);
            }// end of if cycle

            croce = croce != null ? croce : utente != null ? (Croce) utente.company : null;

            wamLogin.setMilite(milite);
            wamLogin.setCroce(croce);
            wamLogin.setRoleType(milite != null ? milite.admin ? EARoleType.admin : EARoleType.user : EARoleType.guest);
            wamLogin.setUtente(milite);

            //--costruiosco una SECONDA istanza di croce per moidificare la descrizione ed inserirla come company
            //--verrà letta da MainLayout14
            company = croceService.findByKeyUnica(croce.code);
            company.setDescrizione(croce.getOrganizzazione().getDescrizione() + " - " + croce.getDescrizione());
            wamLogin.setCompany(company);
            //--backdoor
            if (utente != null && utente.isDev()) {
                wamLogin.setRoleType(EARoleType.developer);
            }// end of if cycle

            vaadSession.setAttribute(KEY_WAM_CONTEXT, wamLogin);
        }// end of if cycle

        if (login != null && login.getUtente() == null) {
            utente = new Utente();
            utente.username = uniqueUserName;
            login.setUtente(utente);
            //            login.setCompany(croce);
            login.setRoleType(wamLogin.getRoleType());
        }// end of if cycle

        return wamLogin;
    }// end of method


    /**
     * Recupera il login della session <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public WamLogin getWamLogin() {
        return vaadinService.getSessionContext() != null ? (WamLogin) vaadinService.getSessionContext().getLogin() : null;
    }// end of method


    /**
     * Recupera la croce della session <br>
     *
     * @return context della sessione
     */
    public Croce getWamCroce() {
        return getWamLogin() != null ? getWamLogin().getCroce() : null;
    }// end of method


    /**
     * Recupera il milite della session <br>
     *
     * @return context della sessione
     */
    public Milite getMilite() {
        return getWamLogin() != null ? getWamLogin().getMilite() : null;
    }// end of method


    /**
     * Alcuni colori per i servizi <br>
     * Devono essere diversi da quelli usati nella Legenda: <br>
     * lightgray <br>
     * lightsalmon <br>
     * lightpink <br>
     * lightgreen <br>
     * lightskyblue <br>
     * lightcyan <br>
     */
    public List<EAColor> getColoriServizi() {
        List<EAColor> lista = new ArrayList<>();

        lista.add(EAColor.green);
        lista.add(EAColor.blue);
        lista.add(EAColor.fuchsia);
        lista.add(EAColor.turquoise);
        lista.add(EAColor.gainsboro);
        lista.add(EAColor.gainsboro);
        lista.add(EAColor.gainsboro);

        return lista;
    }// end of method

}// end of class
