package it.algos.vaadwam.wam;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.log.WamLogService;
import it.algos.vaadwam.modules.milite.Milite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_WAM_LOGIN;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Tue, 02-Jul-2019
 * Time: 07:27
 */
@SpringComponent
@VaadinSessionScope
@Qualifier(TAG_WAM_LOGIN)
@Slf4j
public class WamLogin extends ALogin {

    //    private Logger adminLogger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WamLogService logger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected CroceService croceService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected UtenteService utenteService;

    private Milite milite;

    private Croce croce;

    private EARoleType roleType;

    private String addressIP;

    private String browser;

    private boolean mobile;


    public WamLogin() {
    }


    public WamLogin(Object utente, Company company, EARoleType roleType) {
        if (company instanceof Croce) {
            this.croce = (Croce) company;
        }

        if (utente instanceof Milite) {
            setMilite((Milite) utente);
            this.croce = milite.getCroce();
        }

        this.roleType = roleType;

        super.utente = (Utente) utente;
    }


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Prima viene chiamato il costruttore <br>
     * Prima viene chiamato init(); <br>
     * Viene chiamato @PostConstruct (con qualsiasi firma) <br>
     * Dopo viene chiamato setParameter(); <br>
     * Dopo viene chiamato beforeEnter(); <br>
     * <p>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Creazione e posizionamento dei componenti UI <br>
     * Possono essere sovrascritti nelle sottoclassi <br>
     */
    @PostConstruct
    protected void postConstruct() {
        Company company = croceService.findByKeyUnica(croce.code);
        company.setDescrizione(croce.getOrganizzazione().getDescrizione() + " - " + croce.getDescrizione());
        super.company = company;

        this.roleType = utenteService.getRole((Utente) utente);

        this.logLogin();
    }// end of method


    public void logLogin() {
        VaadinSession vaadSession = null;
        WebBrowser webBrowser = null;
        String message = VUOTA;
        String sep = FlowCost.SEP;

        try {
            vaadSession = VaadinSession.getCurrent();
            webBrowser = vaadSession.getBrowser();
            mobile = webBrowser.isAndroid() || webBrowser.isIPhone() || webBrowser.isWindowsPhone();
            addressIP = webBrowser.getAddress();
            browser = webBrowser.getBrowserApplication();
            message = mobile ? "mobile" : "desktop";
            message += sep;
            message += browser;

            logger.login(croce, milite, addressIP, message);
        } catch (Exception unErrore) {
            log.error(unErrore.toString());
        }

        //        WamLog wamLog = newEntity(milite.croce, EAWamLogType.login, milite, message);
        //        wamLog.id = milite.croce.code + System.currentTimeMillis();
        //        mongo.update(wamLog, WamLog.class);
    }


    public Milite getMilite() {
        return milite;
    }


    public void setMilite(Milite milite) {
        this.milite = milite;
    }


    public Croce getCroce() {
        return croce;
    }// end of method


    public void setCroce(Croce croce) {
        this.croce = croce;
    }// end of method


    public EARoleType getRoleType() {
        return roleType;
    }// end of method


    public void setRoleType(EARoleType roleType) {
        this.roleType = roleType;
    }// end of method


    public boolean isDeveloper() {
        return roleType == EARoleType.developer;
    }


    /**
     * È sicuramente un admin <br>
     */
    public boolean isAdmin() {
        return roleType == EARoleType.admin;
    }


    /**
     * Non è un admin e non è neanche un developer <br>
     */
    public boolean isNotAdmin() {
        return !isAdmin() && isDeveloper();
    }// end of method


    /**
     * È un admin oppure un developer <br>
     */
    public boolean isAdminOrDev() {
        return isAdmin() || isDeveloper();
    }// end of method


    public String getAddressIP() {
        return addressIP;
    }


    public String getBrowser() {
        return browser;
    }


    public boolean isMobile() {
        return mobile;
    }

}// end of class
