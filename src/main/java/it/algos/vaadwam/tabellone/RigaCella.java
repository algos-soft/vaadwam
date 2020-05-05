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

    private String funzione;

    public RigaCella() {
    }

    public RigaCella(EAWamColore eaColore, String icona, String milite, String funzione) {
        this(eaColore, icona, milite, funzione,false);
    }


    public RigaCella(EAWamColore eaColore, String nomeIcona, String nomeMilite, String funzione, boolean aggiungiAvviso) {
        this.eaColore = eaColore;
        this.nomeIcona = nomeIcona;
        this.nomeMilite = nomeMilite;
        this.funzione=funzione;
        if (aggiungiAvviso) {
            this.nomeIconaAvviso = AVVISO;
        }
    }


    public String getColoreCella() {
        return eaColore.getEsadecimale();
    }


    public String getColoreTesto() {
        String tagContrasto=eaColore.getContrasto();
        EAColor color=EAColor.getColor(tagContrasto);
        return color.getEsadecimale();
    }


    public String getNomeIcona() {
        return nomeIcona;
    }


    public String getNomeMilite() {
        return nomeMilite;
    }

    public String getFunzione() {
        return funzione;
    }

    public String getNomeIconaAvviso() {
        return nomeIconaAvviso;
    }


    public String getColoreIconaAvviso() {
        return eaColore.getContrasto();
    }

}
