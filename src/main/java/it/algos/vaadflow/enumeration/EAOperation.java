package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 03-nov-2018
 * Time: 16:22
 * The operations supported by this dialog.
 * Delete is enabled when editing an already existing item.
 */
public enum EAOperation {
    addNew("New", "add", true, false), edit("Edit", "edit", true, true), editProfile("Edit", "edit", true, false), editNoDelete("Edit", "edit", true, false), editDaLink("Edit", "edit", true, true), showOnly("Mostra", "mostra", false, false);

    private final String nameInTitle;

    private final String nameInText;

    private final boolean saveEnabled;

    private final boolean deleteEnabled;


    EAOperation(String nameInTitle, String nameInText, boolean saveEnabled, boolean deleteEnabled) {
        this.nameInTitle = nameInTitle;
        this.nameInText = nameInText;
        this.saveEnabled = saveEnabled;
        this.deleteEnabled = deleteEnabled;
    }

    public static boolean contiene(String nome) {
        boolean contiene = false;

        for (EAOperation eaOperation : EAOperation.values()) {
            if (eaOperation.name().equals(nome)) {
                contiene = true;
            }// end of if cycle
        }// end of for cycle

        return contiene;
    }// end of method

    public String getNameInTitle() {
        return nameInTitle;
    }// end of method


    public String getNameInText() {
        return nameInText;
    }// end of method


    public boolean isSaveEnabled() {
        return saveEnabled;
    }// end of method


    public boolean isDeleteEnabled() {
        return deleteEnabled;
    }// end of method

}// end of enum

