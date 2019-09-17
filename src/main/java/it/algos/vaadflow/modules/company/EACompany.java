package it.algos.vaadflow.modules.company;

import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.person.EAPerson;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 02-set-2018
 * Time: 09:15
 */
public enum EACompany {

    algos("algos", "Company ALGOS di prova", EAPerson.uno, "02.453677", "gac@algos.it", EAAddress.quattro),
    demo("demo", "Company demo", EAPerson.due, "338 678932", "infostudioassociatomondadori@win.com", EAAddress.cinque),
    test("test", "Company di test", EAPerson.tre, "345 786631", "presidentepontetaro@libero.it", EAAddress.sei);


    private String code;
    private String descrizione;
    private EAPerson person;
    private String telefono;
    private String email;
    private EAAddress address;


    EACompany(String code, String descrizione, EAPerson person, String telefono, String email, EAAddress address) {
        this.setCode(code);
        this.setDescrizione(descrizione);
        this.setPerson(person);
        this.setTelefono(telefono);
        this.setEmail(email);
        this.setAddress(address);
    }// fine del costruttore



    public String getCode() {
        return code;
    }// end of method

    public void setCode(String code) {
        this.code = code;
    }// end of method

    public String getDescrizione() {
        return descrizione;
    }// end of method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }// end of method

    public EAPerson getPerson() {
        return person;
    }// end of method

    public void setPerson(EAPerson person) {
        this.person = person;
    }// end of method

    public String getTelefono() {
        return telefono;
    }// end of method

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }// end of method

    public String getEmail() {
        return email;
    }// end of method

    public void setEmail(String email) {
        this.email = email;
    }// end of method

    public EAAddress getAddress() {
        return address;
    }// end of method

    public void setAddress(EAAddress address) {
        this.address = address;
    }// end of method

}// end of enumeration class
