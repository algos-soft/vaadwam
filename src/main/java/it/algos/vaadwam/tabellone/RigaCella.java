package it.algos.vaadwam.tabellone;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 30-Jun-2019
 * Time: 22:04
 */
public class RigaCella {

    private static String AVVISO = "vaadin:alarm";

    private String colore;

    private String icona;

    private String iconaAvviso;

    private String milite;

    private boolean aggiungiAvviso;


    public RigaCella(String colore, String icona, String milite) {
        this(colore, icona, milite, false);
    }// end of constructor


    public RigaCella(String colore, String icona, String milite, boolean aggiungiAvviso) {
        this.colore = colore;
        this.icona = icona;
        this.milite = milite;
        if (aggiungiAvviso) {
            this.iconaAvviso = AVVISO;
        }// end of if cycle
    }// end of constructor


    public String getColore() {
        return colore;
    }// end of method


    public void setColore(String colore) {
        this.colore = colore;
    }// end of method


    public String getIcona() {
        return icona;
    }// end of method


    public void setIcona(String icona) {
        this.icona = icona;
    }// end of method


    public String getMilite() {
        return milite;
    }// end of method


    public void setMilite(String milite) {
        this.milite = milite;
    }// end of method


    public String getIconaAvviso() {
        return iconaAvviso;
    }// end of method


    public void setIconaAvviso(String iconaAvviso) {
        this.iconaAvviso = iconaAvviso;
    }// end of method

}// end of class
