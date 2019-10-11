package it.algos.vaadwam.wam;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.milite.Milite;
import org.springframework.beans.factory.annotation.Qualifier;

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
public class WamLogin extends ALogin {

    private Milite milite;

    private Croce croce;

    private EARoleType roleType;


    public WamLogin() {
    }


    public WamLogin(Object utente, Company company, EARoleType roleType) {
        if (utente instanceof Milite) {
            this.milite = (Milite) utente;
        }// end of if cycle
        this.croce = (Croce) company;
        this.roleType = roleType;

        super.utente = (Utente) utente;
//        super.company = company;
    }// end of constructor


    public Milite getMilite() {
        return milite;
    }// end of method


    public void setMilite(Milite milite) {
        this.milite = milite;
    }// end of method


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
    }// end of method


    /**
     * È sicuramente un admin <br>
     */
    public boolean isAdmin() {
        return roleType == EARoleType.admin;
    }// end of method


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

}// end of class
