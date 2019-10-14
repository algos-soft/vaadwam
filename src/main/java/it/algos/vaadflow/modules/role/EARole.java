package it.algos.vaadflow.modules.role;

import it.algos.vaadflow.service.ATextService;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 22-mar-2018
 * Time: 21:09
 */
public enum EARole {
    developer, admin, user, guest;

//    /**
//     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
//     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
//     */
//    public ATextService text = ATextService.getInstance();
//
//    /**
//     * Returns the name of this enum constant, as contained in the
//     * declaration.  This method may be overridden, though it typically
//     * isn't necessary or desirable.  An enum type should override this
//     * method when a more "programmer-friendly" string form exists.
//     *
//     * @return the name of this enum constant
//     */
//    public final String[] security() {
//        String tab = "ROLE_";
//        String[] securityName = {tab + text.primaMaiuscola(super.toString())};
//
//        return securityName;
//    }// end of method

}// end of enumeration class
