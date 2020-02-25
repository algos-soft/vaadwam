package it.algos.vaadflow.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 23-nov-2019
 * Time: 13:41
 */
public enum EATempo {
    nessuno(0),
    millisecondi(0),
    secondi(1000),
    minuti(1000 * 60),
    ore(1000 * 60 * 60),
    giorni(1000 * 60 * 60 * 24);

    private int delta;


    EATempo(int delta) {
        this.delta = delta;
    }// fine del costruttore


    public long getLong(long inizio) {
        return System.currentTimeMillis() - inizio;
    }// end of method


    public int get(long inizio) {
        int tempo = 0;
        long durata = getLong(inizio);

        if (durata > delta) {
            if (delta > 0) {
                tempo = (int) durata / delta;
            } else {
                tempo = (int) durata;
            }// end of if/else cycle
        }// end of if cycle

        return tempo;
    }// end of method

    public String getTxt(int durata) {
        String testo = " in ";
//        durata = (int) durata / delta;
        testo += durata;
        testo += " " + name();

        return testo;
    }// end of method

}// end of enum class
