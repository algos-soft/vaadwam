package it.algos.vaadflow.wizard.enumeration;

import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 08-mar-2018
 * Time: 08:05
 */
@Slf4j
public enum Task {


    entity("Entity", "Entity", ""),
    list("ViewList", "ViewList", "ViewList"),
    form("ViewDialog", "ViewDialog", "ViewDialog"),
    presenter("Presenter", "Presenter", "Presenter"),
    repository("Repository", "Repository", "Repository"),
    service("Service", "Service", "Service");


    private String sourceSenzaCompany;
    private String sourceConCompany;
    private String destinationSuffix;


    Task(String sourceSenzaCompany, String sourceConCompany, String destinationSuffix) {
        this.sourceSenzaCompany = sourceSenzaCompany;
        this.sourceConCompany = sourceConCompany;
        this.destinationSuffix = destinationSuffix;
    }// fine del costruttore


    public String getSourceName(boolean flagCompany) {
        String txt = ".txt";

        if (flagCompany) {
            return sourceConCompany + txt;
        } else {
            return sourceSenzaCompany + txt;
        }// end of if/else cycle
    }// end of method


    public String getJavaClassName() {
        String java = ".java";
        return destinationSuffix + java;
    }// end of method


}// end of enumeration
