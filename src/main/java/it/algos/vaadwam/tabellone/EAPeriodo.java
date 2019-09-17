package it.algos.vaadwam.tabellone;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Fri, 28-Jun-2019
 * Time: 06:13
 */
public enum EAPeriodo {
    vuoto("", "Data corrente"),
    oggi("Da oggi", "Settimana a partire da oggi"),
    lunedi("Da lunedì", "Settimana a partire da lunedì"),
    giornoPrecedente("Giorno prima", "Un giorno in meno"),
    giornoSuccessivo("Giorno dopo", "Un giorno in più"),
    settimanaPrecedente("Sett. prima", "Settimana precedente"),
    settimanaSuccessiva("Sett. dopo ", "Settimana successiva"),
    selezione("Selezione", "Selezione");

    private String tag;

    private String legenda;


    EAPeriodo(String tag, String legenda) {
        this.tag = tag;
        this.legenda = legenda;
    }// end of constructor


    public String getTag() {
        return tag;
    }// end of method


    public String getLegenda() {
        return legenda;
    }// end of method


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return tag;
    }// end of method

}// end of enum class
