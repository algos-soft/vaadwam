package it.algos.vaadflow.backend.login;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static it.algos.vaadflow.application.FlowCost.TAG_LOGIN;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 10-mag-2018
 * Time: 16:23
 */
@SpringComponent
@VaadinSessionScope
@Qualifier(TAG_LOGIN)
public class ALogin {

    protected Utente utente;

    protected Company company;

    private EARoleType roleType;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private UtenteService utenteService;


    /**
     * Costruttore base senza parametri <br>
     */
    public ALogin() {
    }// end of constructor


    public ALogin(EARoleType roleType) {
        this.roleType = roleType;
    }// end of constructor


    public ALogin(Utente utente, Company company) {
        this.utente = utente;
        this.company = company;
    }


    public ALogin(Utente utente, Company company, EARoleType roleType) {
        this.utente = utente;
        this.company = company;
        this.roleType = roleType;
    }


    public Utente getUtente() {
        return utente;
    }// end of method


    public void setUtente(Utente utente) {
        this.utente = utente;
    }// end of method


    public void setUtenteAndCompany(Utente utente, Company company) {
        this.utente = utente;
        this.company = company;

        if (utente != null) {
            this.roleType = utenteService.getRole(utente);
        }// end of if cycle

    }// end of method


    public Company getCompany() {
        return company;
    }// end of method


    public void setCompany(Company company) {
        this.company = company;
    }// end of method


    public EARoleType getRoleType() {
        return roleType;
    }// end of method


    public void setRoleType(EARoleType roleType) {
        this.roleType = roleType;
    }// end of method


    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }// end of method


    public boolean isDeveloper() {
        return roleType == EARoleType.developer;
    }// end of method


    public boolean isAdmin() {
        return roleType == EARoleType.admin;
    }// end of method


    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in. False otherwise.
     */
    public boolean isUserLoggedIn() {
        Authentication authentication = getAuthentication();

        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }// end of method

}// end of class
