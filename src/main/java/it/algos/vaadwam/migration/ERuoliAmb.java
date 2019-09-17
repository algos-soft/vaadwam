package it.algos.vaadwam.migration;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 05-lug-2018
 * Time: 07:14
 */
public enum ERuoliAmb {
    ROLE_prog, ROLE_custode, ROLE_admin, ROLE_milite, ROLE_ospite;


    public static ERuoliAmb get(String nome) {
        for (ERuoliAmb type : values()) {
            if (type.toString().equals(nome)) {
                return type;
            }// end of if cycle
        }// end of for cycle

        return ERuoliAmb.ROLE_ospite;
    }// end of static method

}// end of enumeration
