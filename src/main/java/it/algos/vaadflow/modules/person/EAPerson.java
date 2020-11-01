package it.algos.vaadflow.modules.person;

import it.algos.vaadflow.modules.address.EAAddress;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 02-set-2018
 * Time: 15:51
 */
public enum EAPerson {
    alex("Alessandro", "Valbonesi", "345 281233", "info@algos.it", EAAddress.algos),

    gac("Guido", "Ceresa", "338 945127", "info@algos.it", EAAddress.algos),


    uno("Mario", "Rossi", "06.785556", "assurbanipal@gmail.com.it", EAAddress.uno),

    due("Aldo", "Guzzini", "338 678932", "gates@win.com", EAAddress.due),

    tre("Lucia", "Portella", "345 786631", "alfa@libero.it", EAAddress.tre);


    private String nome;

    private String cognome;

    private String telefono;

    private String mail;

    private EAAddress address;


    EAPerson(String nome, String cognome, String telefono, String mail, EAAddress address) {
        this.setNome(nome);
        this.setCognome(cognome);
        this.setTelefono(telefono);
        this.setMail(mail);
        this.setAddress(address);
    }// fine del costruttore


    public String getNome() {
        return nome;
    }// end of method

    public void setNome(String nome) {
        this.nome = nome;
    }// end of method

    public String getCognome() {
        return cognome;
    }// end of method

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }// end of method

    public String getTelefono() {
        return telefono;
    }// end of method

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }// end of method

    public String getMail() {
        return mail;
    }// end of method

    public void setMail(String mail) {
        this.mail = mail;
    }// end of method

    public EAAddress getAddress() {
        return address;
    }// end of method

    public void setAddress(EAAddress address) {
        this.address = address;
    }// end of method

}// end of enumeration class
