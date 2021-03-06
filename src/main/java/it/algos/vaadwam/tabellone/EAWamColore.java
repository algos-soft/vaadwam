package it.algos.vaadwam.tabellone;

import it.algos.vaadflow.enumeration.*;

import java.util.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Fri, 28-Jun-2019
 * Time: 06:22
 */
public enum EAWamColore {

    storico(EAColor.storicoTabellone, EAColor.storicoTabelloneContrasto, "storico", "", "Turno effettuato o non creabile", ""), // blu scuro
    critico(EAColor.criticoTabellone, EAColor.criticoTabelloneContrasto, "critico", "critica", "Turno critico da assegnare subito", "Iscrizione critica da assegnare subito"), // rosso
    urgente(EAColor.urgenteTabellone, EAColor.urgenteTabelloneContrasto, "urgente", "urgente", "Turno da assegnare nei prossimi giorni", "Iscrizione da assegnare nei prossimi giorni"),  // arancione
    normale(EAColor.normaleTabellone, EAColor.normaleTabelloneContrasto, "normale", "", "Turno assegnato normale (funzioni obbligatorie coperte)", "Iscrizione assegnata"),  // verde
    previsto(EAColor.previstoTabellone, EAColor.previstoTabelloneContrasto, "previsto", "prevista", "Turno previsto e non ancora completamente assegnato", "Iscrizione prevista e non ancora assegnata"),  // blu chiaro
    creabile(EAColor.creabileTabellone, EAColor.creabileTabelloneContrasto, "creabile", "", "Turno creabile", ""),  // grigio
    disponibile(EAColor.disponibileTabellone, EAColor.disponibileTabelloneContrasto, "disponibile", "", "Solo disponibilità ad eseguire il turno", ""),  // ?
    ;


    private String tag;

    private String contrasto;

    private String titoloTurno;

    private String titoloIscrizione;

    private String legendaTurno;

    private String legendaIscrizione;


    private boolean differenziati = false;

    private String esadecimale;


    EAWamColore(EAColor color, EAColor contrasto, String titoloTurno, String titoloIscrizione, String legendaTurno, String legendaIscrizione) {
        this.tag = color.getTag();
        this.contrasto = contrasto.getTag();
        this.titoloTurno = titoloTurno;
        this.titoloIscrizione = titoloIscrizione.equals("") ? titoloTurno : titoloIscrizione;
        this.legendaTurno = legendaTurno;
        this.legendaIscrizione = legendaIscrizione.equals("") ? legendaTurno : legendaIscrizione;
        this.esadecimale = color.getEsadecimale();
    }// end of constructor


    private static ArrayList<EAWamColore> getColors(boolean differenziati) {
        ArrayList<EAWamColore> lista = new ArrayList<>();

        for (EAWamColore color : EAWamColore.values()) {
            color.differenziati = differenziati;
            lista.add(color);
        }// end of for cycle

        return lista;
    }// end of static method


    public static ArrayList<EAWamColore> getColorsTurno() {
        return getColors(false);
    }// end of static method


    public static ArrayList<EAWamColore> getColorsIscrizione() {
        return getColors(true);
    }// end of static method


    public String getTag() {
        return tag;
    }// end of method


    public String getEsadecimale() {
        return esadecimale;
    }// end of method


    public String getTitolo() {
        return differenziati ? titoloIscrizione : titoloTurno;
    }// end of method


    public String getLegenda() {
        return differenziati ? legendaIscrizione : legendaTurno;
    }// end of method


    public String getContrasto() {
        return contrasto;
    }

}
