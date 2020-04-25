package it.algos.vaadflow.wrapper;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 06-apr-2020
 * Time: 21:37
 */
public class WrapDueStringhe {

    private String prima;

    private String seconda;


    public WrapDueStringhe(String prima, String seconda) {
        this.prima = prima;
        this.seconda = seconda;
    }// end of constructor


    public String getPrima() {
        return prima;
    }// end of method


    public void setPrima(String prima) {
        this.prima = prima;
    }// end of method


    public String getSeconda() {
        return seconda;
    }// end of method


    public void setSeconda(String seconda) {
        this.seconda = seconda;
    }// end of method

}// end of class
