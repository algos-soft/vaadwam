package it.algos.vaadwam.tabellone;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 30-Jun-2019
 * Time: 22:04
 */
public class RigaCella {

    private String colore;

    private String icona;

    private String milite;


    public RigaCella(String colore, String icona, String milite) {
        this.colore = colore;
        this.icona = icona;
        this.milite = milite;
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

}// end of class
