package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 10-apr-2020
 * Time: 06:48
 * Enumeration of type used with AIColumn
 */
public enum EAFormType {
    newForm, editForm, showForm, searchForm;


    public static boolean contiene(String nome) {
        boolean contiene = false;

        for (EAFormType eaFormType : EAFormType.values()) {
            if (eaFormType.name().equals(nome)) {
                contiene = true;
            }// end of if cycle
        }// end of for cycle

        return contiene;
    }// end of method

}// fine della classe Enumeration
