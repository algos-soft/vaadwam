package it.algos.vaadwam.tabellone;

import it.algos.vaadflow.enumeration.EAColor;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 30-Jun-2019
 * Time: 22:04
 */
public class RigaCella {

    private static String AVVISO = "vaadin:alarm";

    private EAWamColore eaColore;

    private String nomeIcona;

    private String nomeIconaAvviso = VUOTA;

    private String nomeMilite;


    public RigaCella(EAWamColore eaColore, String icona, String milite) {
        this(eaColore, icona, milite, false);
    }// end of constructor


    public RigaCella(EAWamColore eaColore, String nomeIcona, String nomeMilite, boolean aggiungiAvviso) {
        this.eaColore = eaColore;
        this.nomeIcona = nomeIcona;
        this.nomeMilite = nomeMilite;
        if (aggiungiAvviso) {
            this.nomeIconaAvviso = AVVISO;
        }// end of if cycle
    }// end of constructor


    public String getColoreCella() {
        return eaColore.getEsadecimale();
    }// end of method


    public String getColoreTesto() {
        //return nomeIconaAvviso.equals(VUOTA) ? EAColor.black.getEsadecimale() : eaColore.getContrasto();
        // alex 5 apr, parliamone
        return EAColor.black.getEsadecimale();
    }// end of method


    public String getNomeIcona() {
        return nomeIcona;
    }// end of method


    public String getNomeMilite() {
        return nomeMilite;
    }// end of method


    public String getNomeIconaAvviso() {
        return nomeIconaAvviso;
    }// end of method


    public String getColoreIconaAvviso() {
        return eaColore.getContrasto();
    }// end of method

}// end of class
