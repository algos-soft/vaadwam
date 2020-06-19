package it.algos.vaadwam.wam;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.Milite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

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


    public WamLogin() {
    }


    public WamLogin(Object utente, Company company, EARoleType roleType) {
        if (company instanceof Croce) {
            this.croce = (Croce) company;
        }// end of if cycle

        if (utente instanceof Milite) {
            this.milite = (Milite) utente;
            this.croce = milite.getCroce();
        }// end of if cycle

        this.roleType = roleType;

        super.utente = (Utente) utente;
    }// end of constructor


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
    }// end of method


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
