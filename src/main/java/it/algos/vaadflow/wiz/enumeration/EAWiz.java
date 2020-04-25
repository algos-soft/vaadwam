package it.algos.vaadflow.wiz.enumeration;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 18-apr-2020
 * Time: 06:27
 */
public enum EAWiz {
    flagSecurity(true, "Utilizza Spring Security", true, false, false, false, false),
    flagDocumentation(true, "Directory documentazione", true, true, false, false, true),
    flagLinks(true, "Directory links a web", true, true, false, false, true),
    flagSnippets(true, "Directory snippets di aiuto", true, true, false, false, true),
    flagFlow(true, "Copia la cartella VaadFlow", true, true, false, false, true),
    flagProject(true, "Crea la cartella del nuovo progetto", true, false, false, false, false),
    flagResources(true, "Directory resources - ATTENZIONE", true, true, false, false, false),
    flagProperty(true, "File application.properties", true, true, false, false, true),
    flagRead(true, "File READ con note di testo", true, true, false, false, true),
    flagGit(true, "File GIT di esclusione", true, true, false, false, false),
    flagPom(true, "File Maven di POM.xml - ATTENZIONE", true, true, false, false, false),
    flagFile(true, "Sovrascrive il singolo FILE", false, false, false, false, false),
    flagDirectory(true, "Sovrascrive la DIRECTORY", false, false, false, false, false),
    flagBanner(true, "File banner di SpringBoot", true, true, false, false, false),
    pathUserDir(false, "Directory recuperata dal System dove gira il programma in uso", true, true, false, false, false, VUOTA),
    pathVaadFlow(false, "Directory che contiene il programma VaadFlow", true, true, false, false, false, VUOTA),
    pathIdeaProjects(false, "Directory che contiene i nuovi programmi appena creati da Idea", true, true, false, false, false, VUOTA),
    //    pathVaadFlowSources(false, "Directory dei sorgenti testuali di VaadFlow da elaborare", true, true, false, false, false, VUOTA),
    nameTargetProject(false, "Nome breve new/update project", true, true, false, false, false, VUOTA),
    pathTargetProjet(false, "Path new/update project", true, true, false, false, false, VUOTA),
    ;


    private boolean checkBox;

    private String labelBox;

    private String descrizione;

    private boolean newProject;

    private boolean updateProject;

    private boolean newPackage;

    private boolean updatePackage;

    private boolean defaultAcceso = false;

    private boolean acceso;

    private String defaultValue = VUOTA;

    private String value;


    /**
     * Costruttore per i flag con valore booleano <br>
     */
    EAWiz(boolean checkBox, String unTesto, boolean newProject, boolean updateProject, boolean newPackage, boolean updatePackage, boolean defaultAcceso) {
        this(checkBox, unTesto, newProject, updateProject, newPackage, updatePackage, defaultAcceso, VUOTA);
    }// fine del costruttore


    /**
     * Costruttore per i path con valore stringa <br>
     */
    EAWiz(boolean checkBox, String unTesto, boolean newProject, boolean updateProject, boolean newPackage, boolean updatePackage, String defaultValue) {
        this(checkBox, unTesto, newProject, updateProject, newPackage, updatePackage, false, defaultValue);
    }// fine del costruttore


    /**
     * Costruttore completo <br>
     */
    EAWiz(boolean checkBox, String unTesto, boolean newProject, boolean updateProject, boolean newPackage, boolean updatePackage, boolean defaultAcceso, String defaultValue) {
        this.checkBox = checkBox;
        if (checkBox) {
            this.labelBox = unTesto;
        } else {
            this.descrizione = unTesto;
        }// end of if/else cycle
        this.newProject = newProject;
        this.updateProject = updateProject;
        this.newPackage = newPackage;
        this.updatePackage = updatePackage;

        if (checkBox) {
            this.defaultAcceso = defaultAcceso;
            this.acceso = defaultAcceso;
        } else {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }// end of if/else cycle
    }// fine del costruttore


    public static void reset() {
        for (EAWiz eaWiz : EAWiz.values()) {
            eaWiz.setAcceso(eaWiz.defaultAcceso);
            eaWiz.setValue(eaWiz.defaultValue);
        }// end of for cycle
    }// end of method


    public boolean isCheckBox() {
        return checkBox;
    }// end of method


    public String getLabelBox() {
        return labelBox;
    }// end of method


    public String getDescrizione() {
        if (checkBox) {
            if (descrizione != null && descrizione.length() > 0) {
                return descrizione;
            } else {
                return labelBox;
            }// end of if/else cycle
        } else {
            return descrizione;
        }// end of if/else cycle
    }// end of method


    public boolean isNewProject() {
        return newProject;
    }// end of method


    public boolean isUpdateProject() {
        return updateProject;
    }// end of method


    public boolean isNewPackage() {
        return newPackage;
    }// end of method


    public boolean isUpdatePackage() {
        return updatePackage;
    }// end of method


    public boolean isAbilitato() {
        return acceso;
    }// end of method


    public boolean isAcceso() {
        return acceso;
    }// end of method


    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }// end of method


    public String getValue() {
        return value;
    }// end of method


    public void setValue(String value) {
        this.value = value;
    }// end of method


}// end of enumeration
