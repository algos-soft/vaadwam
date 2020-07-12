package it.algos.vaadwam.modules.turno;

import java.time.LocalDate;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Thu, 18-Jul-2019
 * Time: 10:08
 */
public enum EAFiltroAnno {
    corrente(0), menoUno(1), menoDue(2), menoTre(3), menoQuattro(4), menoCinque(5), menoSei(6), menoSette(7);

    public int delta;

    private String popupLabel;


    EAFiltroAnno(int delta) {
        this.delta = delta;
        fixLabel();
    }// end of constructor


    public void fixLabel() {
        if (delta == 0) {
            popupLabel = "Anno corrente";
        } else {
            popupLabel = "" + (LocalDate.now().getYear() - delta);
        }// end of if/else cycle
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
        return popupLabel;
    }// end of method

}// end of enumeration class
