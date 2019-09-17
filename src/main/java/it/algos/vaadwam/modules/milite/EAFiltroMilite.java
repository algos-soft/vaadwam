package it.algos.vaadwam.modules.milite;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Thu, 18-Apr-2019
 * Time: 17:34
 */
public enum EAFiltroMilite {
    attivi("Attivi"),
    admin("Admin"),
    dipendenti("Dipendenti"),
    infermieri("Infermieri"),
    storico("Storico"),
    senzaFunzioni("Attivi senza funzioni"),
    conNote("Con note");

    private final String popupLabel;


    EAFiltroMilite(String popupLabel) {
        this.popupLabel = popupLabel;
    }// end of constructor


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
        return popupLabel;
    }// end of method

}// end of enumeration class
