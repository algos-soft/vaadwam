package it.algos.vaadflow.modules.utente;

import it.algos.vaadflow.modules.company.EACompany;
import it.algos.vaadflow.modules.role.EARole;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 14-set-2018
 * Time: 15:39
 */
public enum EAUtente {
    gac(EACompany.algos, "gac", "fulvia", EARole.developer, "gac@algos.it"),

    alex(EACompany.algos, "alex", "axel01", EARole.developer, "alex@algos.it"),

    admin(EACompany.demo, "admin", "admin", EARole.admin, "info@algos.it"),

    demo(EACompany.demo, "demo", "demo", EARole.user, "info@algos.it"),

    quattro((EACompany) null, "anonymous", "anonymous", EARole.user, ""),

    cinque(EACompany.test, "Addabbo Andrea", "addabbo123", EARole.user, "");

    public String username;

    public String password;

    public EARole ruolo;

    public String mail;

    public EACompany company;


    EAUtente(EACompany company, String username, String password, EARole ruolo, String mail) {
        this.setCompany(company);
        this.setUsername(username);
        this.setPassword(password);
        this.setRuolo(ruolo);
        this.setMail(mail);
    }// fine del costruttore


    public EACompany getCompany() {
        return company;
    }// end of method


    public void setCompany(EACompany company) {
        this.company = company;
    }// end of method


    public String getUsername() {
        return username;
    }// end of method


    public void setUsername(String username) {
        this.username = username;
    }// end of method


    public String getPassword() {
        return password;
    }// end of method


    public void setPassword(String password) {
        this.password = password;
    }// end of method


    public EARole getRuolo() {
        return ruolo;
    }// end of method


    public void setRuolo(EARole ruolo) {
        this.ruolo = ruolo;
    }// end of method


    public String getMail() {
        return mail;
    }// end of method


    public void setMail(String mail) {
        this.mail = mail;
    }// end of method


}// end of enumeration class
