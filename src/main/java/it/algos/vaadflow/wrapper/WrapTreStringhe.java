package it.algos.vaadflow.wrapper;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 06-apr-2020
 * Time: 21:37
 */
public class WrapTreStringhe {

    private String prima;

    private String seconda;

    private String terza;


    public WrapTreStringhe(String prima, String seconda, String terza) {
        this.prima = prima;
        this.seconda = seconda;
        this.terza = terza;
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


    public String getTerza() {
        return terza;
    }// end of method


    public void setTerza(String terza) {
        this.terza = terza;
    }// end of method

}// end of class
