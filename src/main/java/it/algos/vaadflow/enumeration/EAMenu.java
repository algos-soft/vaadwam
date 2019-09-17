package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-dic-2018
 * Time: 09:23
 */
public enum EAMenu {
    buttons, popup, flowing, vaadin;


    public static EAMenu getMenu(String nameMenu) {
        EAMenu menu = null;

        for (EAMenu menuTmp : EAMenu.values()) {
            if (menuTmp.toString().equals(nameMenu)) {
                menu = menuTmp;
            }// fine del blocco if
        }// end of for cycle

        return menu;
    }// end of static method

}// end of class
