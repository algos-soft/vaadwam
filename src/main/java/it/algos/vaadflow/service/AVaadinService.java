package it.algos.vaadflow.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.IUtenteService;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.milite.Milite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.application.FlowCost.KEY_SECURITY_CONTEXT;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 31-ott-2018
 * Time: 09:16
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AVaadinService.class); <br>
 * 2) AVaadinService.getInstance(); <br>
 * 3) @Autowired private AVaadinService vaadinService; <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AVaadinService {


    /**
     * Private final property
     */
    private static final AVaadinService INSTANCE = new AVaadinService();

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public PreferenzaService pref;

    @Autowired
    ApplicationContext appContext;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO il costruttore <br>
     */
    @Autowired
    private UtenteService utenteService;


    /**
     * Private constructor to avoid client applications to use constructor <br>
     * In alcune circostanze SpringBoot non riesce a costruire l'istanza <br>
     * Rimesso 'public' al posto del precedente 'private' <br>
     */
    public AVaadinService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AVaadinService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Restituisce il context unico della sessione <br>
     * Se non esiste lo crea <br>
     * Invocato quando la @route fa partire la AViewList. <br>
     * (non è chiaro se passa prima da MainLayout o da AViewList o da AViewDialog) <br>
     * <p>
     * Recupera l'user dall'attributo della sessione HttpSession al termine della security <br>
     * Usa il service od una sua sottoclasse specifica dell'applicazione per creare l'utente loggato <br>
     * Usa ALogin od una sua sottoclasse specifica dell'applicazione per memorizzare l'utente loggato <br>
     * Crea il context <br>
     * Crea il login <br>
     * Inserisce il context come attributo nella vaadSession <br>
     */
    public AContext getSessionContext() {
        AContext context;
        String uniqueUsername;
        ALogin login;
        IUtenteService uService;
        Utente utente;
        EARoleType roleType = null;

        VaadinSession vaadSession = VaadinSession.getCurrent();
        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
            uniqueUsername = getLoggedUsername();
        } else {
            return null;
        }

        if (context == null) {
            if (usaSecurity) {
                uService = (IUtenteService) appContext.getBean(FlowVar.loginServiceClazz);
                utente = uService.findByKeyUnica(uniqueUsername);

                //--accesso diretto per developer ed altri registrati come utenti e non come sottoclasse di utenti
                if (utente == null) {
                    utente = this.utenteService.findByKeyUnica(uniqueUsername);
                    roleType = EARoleType.developer;


                    // crea un milite fittizio che non è presente sul db e non logga nel log dell'admin
                    Milite milite = new Milite();
                    milite.setFantasma(true);
                    milite.setId(utente.getUsername());
                    milite.setCognome(utente.getUsername());
                    milite.setAdmin(true);
                    milite.setCroce((Croce)utente.getCompany());
                    milite.setCreatoreTurni(true);
                    milite.setManagerTabellone(true);
                    milite.setUsername(utente.getUsername());
                    utente=milite;

                }

                if (utente != null) {
                    if (roleType == null) {
                        roleType = uService.getRoleType(utente);
                    }
                    login = (ALogin) appContext.getBean(FlowVar.loginClazz, utente, utente.getCompany(), roleType);
                    context = appContext.getBean(AContext.class, login);
                    context.setUsaLogin(true);
                    context.setLoginValido(true);
                }

            } else {
                login = (ALogin) appContext.getBean(FlowVar.loginClazz, EARoleType.developer);
                context = appContext.getBean(AContext.class, login);
                context.setUsaLogin(false);
                context.setLoginValido(true);
            }

            if (context != null) {
                vaadSession.setAttribute(KEY_CONTEXT, context);
            }
        }

        return context;
    }


    public ALogin getLogin() {
        return getSessionContext() != null ? getSessionContext().getLogin() : null;
    }// end of  method


    public String getLoggedUsernameOld() {
        String uniqueUsername = "";
        User springUser;

        try { // prova ad eseguire il codice

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession httpSession = attr.getRequest().getSession(true);
            SecurityContext securityContext = (SecurityContext) httpSession.getAttribute(KEY_SECURITY_CONTEXT);

            if (securityContext != null) {
                springUser = (User) securityContext.getAuthentication().getPrincipal();
                uniqueUsername = springUser.getUsername();
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return uniqueUsername;
    }// end of  method


    /**
     * Ritorna lo username dell'utente loggato
     */
    public String  getLoggedUsername() {
        VaadinSession vs = VaadinSession.getCurrent();
        WrappedSession ws = vs.getSession();
        SecurityContext sc = (SecurityContext)ws.getAttribute(KEY_SECURITY_CONTEXT);
        User user = (User)sc.getAuthentication().getPrincipal();
        return user.getUsername();
    }



    /**
     * Controlla l'esistenza del login se è obbligatorio <br>
     */
    public boolean mancaLoginSeObbligatorio() {
        return getSessionContext() != null ? getSessionContext().mancaLoginSeObbligatorio() : true;
    }// end of method

}// end of class
