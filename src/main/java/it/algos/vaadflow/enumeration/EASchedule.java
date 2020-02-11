package it.algos.vaadflow.enumeration;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 17:38
 * <p>
 * Template di schedule preconfigurati, con nota esplicativa utilizzabile nelle info
 *
 * @see http://www.sauronsoftware.it/projects/cron4j/manual.php
 */
public enum EASchedule {


    /**
     * Descrizione: ogni giorno a mezzanotte <br>
     */
    giorno("0 0 * * *", "ogni giorno a mezzanotte"),
    /**
     * Descrizione: ogni giorno 1 minuto dopo mezzanotte
     */
    giornoPrimoMinuto("1 0 * * *", "ogni giorno 1 minuto dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 2 minuti dopo mezzanotte
     */
    giornoSecondoMinuto("2 0 * * *", "ogni giorno 2 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 3 minuti dopo mezzanotte
     */
    giornoTerzoMinuto("3 0 * * *", "ogni giorno 3 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 4 minuti dopo mezzanotte
     */
    giornoQuartoMinuto("4 0 * * *", "ogni giorno 4 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 5 minuti dopo mezzanotte
     */
    giornoQuintoMinuto("5 0 * * *", "ogni giorno 5 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 6 minuti dopo mezzanotte
     */
    giornoSestoMinuto("6 0 * * *", "ogni giorno 6 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno a mezzanotte
     */
    giornoSettimoMinuto("7 0 * * *", "ogni giorno 7 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 8 minuti dopo mezzanotte
     */
    giornoOttavoMinuto("8 0 * * *", "ogni giorno 8 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 9 minuti dopo mezzanotte
     */
    giornoNonoMinuto("9 0 * * *", "ogni giorno 9 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni giorno 10 minuti dopo mezzanotte
     */
    giornoDecimoMinuto("10 0 * * *", "ogni giorno 10 minuti dopo mezzanotte."),
    /**
     * Descrizione: ogni ora, al minuto 2
     */
    oraSecondoMinuto("2 * * * *", "ogni ora, al minuto 2"),
    /**
     * Descrizione: ogni ora, al minuto 3
     */
    oraTerzoMinuto("3 * * * *", "ogni ora, al minuto 3"),
    /**
     * Descrizione: ogni ora, al minuto 4
     */
    oraQuartoMinuto("4 * * * *", "ogni ora, al minuto 4"),
    /**
     * Descrizione: ogni ora, al minuto 5
     */
    oraQuintoMinuto("5 * * * *", "ogni ora, al minuto 5"),
    /**
     * Descrizione: ogni minuto
     */
    minuto("* * * * *", "ogni minuto"),
    /**
     * Descrizione: ogni minuto
     */
    minutoUno("1/3 * * * *", "ogni minuto"),
    /**
     * Descrizione: ogni minuto
     */
    minutoDue("2/3 * * * *", "ogni minuto"),
    /**
     * Descrizione: ogni minuto
     */
    minutoTre("3/3 * * * *", "ogni minuto"),
    /**
     * Descrizione: ogni settimana nella notte tra domenica e lunedi
     */
    settimanaLunedi("0 0 * * 1", "ogni settimana nella notte tra domenica e lunedi"),
    /**
     * Descrizione: il primo ed il 15 di ogni mese alle ore 4 di notte
     */
    biMensile("0 4 1,15 * *", "il primo ed il 15 di ogni mese alle ore 4 di notte"),
    /**
     * Descrizione: ogni giorno alle ore 4 di notte, escluso l'1 ed il 15
     */
    oreQuattroEscluso("0 4 2-14,16-31 * *", "ogni giorno alle ore 4 di notte, escluso l'1 ed il 15"),
    /**
     * Descrizione: ogni giorno alle ore 4 di notte, escluso l'1 ed il 15
     */
    oreQuattro("5 4 * * *", "ogni giorno alle ore 4:05 di notte."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreSei("5 6 * * *", "ogni giorno alle ore 6:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreOttoMartedi("5 8 * * 2", "ogni martedì alle ore 8:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreOttoMercoledi("5 8 * * 3", "ogni mercoledì alle ore 8:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreOttoGiovedi("5 8 * * 4", "ogni giovedì alle ore 8:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreOttoVenerdi("5 8 * * 5", "ogni venerdì alle ore 8:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreOttoSabato("5 8 * * 6", "ogni sabato alle ore 8:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    oreOttoDomenica("5 8 * * 7", "ogni domenica alle ore 8:05 del mattino."),
    /**
     * Descrizione: ogni giorno alle ore 8 del mattino
     */
    prova("5 8 * * *", "mercoledì alle 8."),
    ;

    /**
     * pattern di schedulazione di tipo 'UNIX'
     */
    private String pattern;

    /**
     * Nota esplicativa da inserire nei log
     */
    private String nota;


    /**
     * @param pattern di schedulazione di tipo 'UNIX'
     * @param nota    esplicativa da inserire nei log
     */
    EASchedule(String pattern, String nota) {
        this.setPattern(pattern);
        this.setNota(nota);
    }// fine del costruttore


    public String getPattern() {
        return pattern;
    }// end of method


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }// end of method


    public String getNota() {
        return nota;
    }// end of method


    public void setNota(String nota) {
        this.nota = nota;
    }// end of method

}// end of enum

