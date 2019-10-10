package it.algos.vaadflow.application;

import com.vaadin.flow.component.applayout.AppLayout;
//import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.ui.MainLayout;

import javax.annotation.PostConstruct;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 18-ott-2018
 * Time: 10:51
 */
@SpringComponent
@VaadinSessionScope
public class AContext {

    /**
     * Flag per controllare se l'applicazione necessita del login <br>
     * Controlla la variabile FlowVar.usaSecurity <br>
     * Potrebbe essere modificato nella singola sessione <br>
     */
    private boolean usaLogin;

    /**
     * Istanza (@VaadinSessionScope) di login <br>
     * Classe standard oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
     * Eventuale casting a carico del chiamante <br>
     */
    private ALogin login;

    /**
     * Flag per controllare se l'applicazione ha effettuato un login <br>
     * Ha senso solo se usaLogin=true <br>
     * Viene regolato nel metodo AVaadinService.fixLoginAndContext() <br>
     */
    private boolean loginValido;

    /**
     * Flag per controllare se l'applicazione è multi-company <br>
     * Controlla la variabile FlowVar.usaCompany <br>
     * Potrebbe essere modificato nella singola sessione <br>
     */
    private boolean usaCompany;

    /**
     * Istanza (@Entity) di Company <br>
     * Ha senso solo se usaCompany=true <br>
     * Classe Company oppure eventuale sottoclasse specializzata <br>
     * Eventuale casting a carico del chiamante <br>
     * Nel caso l'applicazione necessiti del login (usaLogin=true),
     * - si usa la Company di ALogin e NON questa di AContext uguale per tutta l'applicazione <br>
     * Nel caso (improbabile) l'applicazione sia multi-company (usaCompany=true) ma NON usi il login (usaLogin=false),
     * - si usa questa Company di AContext uguale per tutta l'applicazione e NON si usa la Company di ALogin <br>
     */
    private Company company;


    private MainLayout mainLayout;

    private AppLayout appLayout;

//    private AppLayoutMenu appMenu;


    /**
     * Costruttore base senza parametri <br>
     */
    public AContext() {
    } // end of constructor


    public AContext(ALogin login) {
        this(login, (Company) null);
    } // end of constructor


    public AContext(ALogin login, Company company) {
        this.login = login;
        this.company = company;
    } // end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore <br>
     * L'istanza DEVE essere creata da SpringBoot con Object algos = appContext.getBean(AlgosClass.class);  <br>
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void inizia() {
        this.setUsaLogin(FlowVar.usaSecurity);
        this.setUsaCompany(FlowVar.usaCompany);
    }// end of method


    /**
     * Flag per controllare se l'applicazione necessita del login <br>
     * Controlla la variabile FlowVar.usaSecurity <br>
     * Potrebbe essere modificato nella singola sessione <br>
     */
    public boolean isUsaLogin() {
        return usaLogin;
    }// end of method


    /**
     * Flag per controllare se l'applicazione necessita del login <br>
     * Controlla la variabile FlowVar.usaSecurity <br>
     * Potrebbe essere modificato nella singola sessione <br>
     */
    public void setUsaLogin(boolean usaLogin) {
        this.usaLogin = usaLogin;
    }// end of method


    /**
     * Istanza (@VaadinSessionScope) di login <br>
     * Classe standard oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
     * Eventuale casting a carico del chiamante <br>
     */
    public ALogin getLogin() {
        return login;
    }// end of method


    /**
     * Istanza (@VaadinSessionScope) di login <br>
     * Classe standard oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
     * Eventuale casting a carico del chiamante <br>
     */
    public void setLogin(ALogin login) {
        this.login = login;
    }// end of method


    /**
     * Flag per controllare se l'applicazione ha effettuato un login <br>
     * Ha senso solo se usaLogin=true <br>
     * Viene regolato nel metodo AVaadinService.fixLoginAndContext() <br>
     */
    public boolean isLoginValido() {
        return loginValido;
    }// end of method


    /**
     * Flag per controllare se l'applicazione ha effettuato un login <br>
     * Ha senso solo se usaLogin=true <br>
     * Viene regolato nel metodo AVaadinService.fixLoginAndContext() <br>
     */
    public void setLoginValido(boolean loginValido) {
        this.loginValido = loginValido;
    }// end of method


    /**
     * Flag per controllare se l'applicazione è multi-company <br>
     * Controlla la variabile FlowVar.usaCompany <br>
     * Potrebbe essere modificato nella singola sessione <br>
     */
    public boolean isUsaCompany() {
        return usaCompany;
    }// end of method


    /**
     * Flag per controllare se l'applicazione è multi-company <br>
     * Controlla la variabile FlowVar.usaCompany <br>
     * Potrebbe essere modificato nella singola sessione <br>
     */
    public void setUsaCompany(boolean usaCompany) {
        this.usaCompany = usaCompany;
    }// end of method


    /**
     * Istanza (@Entity) di Company <br>
     * Ha senso solo se usaCompany=true <br>
     * Classe Company oppure eventuale sottoclasse specializzata <br>
     * Eventuale casting a carico del chiamante <br>
     * Nel caso l'applicazione necessiti del login (usaLogin=true),
     * - si usa la Company di ALogin e NON questa di AContext uguale per tutta l'applicazione <br>
     * Nel caso (improbabile) l'applicazione sia multi-company (usaCompany=true) ma NON usi il login (usaLogin=false),
     * - si usa questa Company di AContext uguale per tutta l'applicazione e NON si usa la Company di ALogin <br>
     */
    public Company getCompany() {
        return company;
    }// end of method


    /**
     * Istanza (@Entity) di Company <br>
     * Ha senso solo se usaCompany=true <br>
     * Classe Company oppure eventuale sottoclasse specializzata <br>
     * Eventuale casting a carico del chiamante <br>
     * Nel caso l'applicazione necessiti del login (usaLogin=true),
     * - si usa la Company di ALogin e NON questa di AContext uguale per tutta l'applicazione <br>
     * Nel caso (improbabile) l'applicazione sia multi-company (usaCompany=true) ma NON usi il login (usaLogin=false),
     * - si usa questa Company di AContext uguale per tutta l'applicazione e NON si usa la Company di ALogin <br>
     */
    public void setCompany(Company company) {
        this.company = company;
    }// end of method


    public MainLayout getMainLayout() {
        return mainLayout;
    }// end of method


    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }// end of method


    public AppLayout getAppLayout() {
        return appLayout;
    }// end of method


    public void setAppLayout(AppLayout appLayout) {
        this.appLayout = appLayout;
    }// end of method


//    public AppLayoutMenu getAppMenu() {
//        return appMenu;
//    }// end of method
//
//
//    public void setAppMenu(AppLayoutMenu appMenu) {
//        this.appMenu = appMenu;
//    }// end of method


    /**
     * Le applicazioni che non necessitano di login è come se fossero tutti developer <br>
     */
    public boolean isDev() {
        boolean status = false;

        if (isUsaLogin()) {
            if (isLoginValido()) {
                if (login.isDeveloper()) {
                    status = true;
                }// end of if cycle
            }// end of if cycle
        } else {
            status = true;
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Le applicazioni che non necessitano di login è come se fossero tutti developer e quindi anche admin <br>
     */
    public boolean isAdmin() {
        boolean status = false;

        if (isUsaLogin()) {
            if (isLoginValido()) {
                if (login.isAdmin()) {
                    status = true;
                }// end of if cycle
            }// end of if cycle
        } else {
            status = true;
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Controlla l'esistenza del login se è obbligatorio <br>
     */
    public boolean mancaLoginSeObbligatorio() {
        boolean loginValido = false;

        if (isUsaLogin()) {
            if (getLogin() == null) {
                loginValido = false;
            } else {
                if (isLoginValido()) {
                    loginValido = true;
                } else {
                    loginValido = false;
                }// end of if/else cycle
            }// end of if/else cycle
        } else {
            loginValido = true;
        }// end of if/else cycle

        return !loginValido;
    }// end of method

}// end of class
