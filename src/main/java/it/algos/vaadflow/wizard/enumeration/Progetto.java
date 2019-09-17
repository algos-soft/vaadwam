package it.algos.vaadflow.wizard.enumeration;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 08-mar-2018
 * Time: 08:23
 */
@Slf4j
public enum Progetto {

    vaadin("vaadflow", "vaadflow", "vaadtest", "Flow"),
    test("vaadflow", "vaadtest", "vaadtest", "Test"),
    bio("vaadwiki", "vaadwiki", "vaadwiki", "Wiki"),
    wam("vaadwam", "vaadwam", "vaadwam", "Wam"),
    ;

    private String nameProject;
    private String nameModule;
    private String nameLayout;
    private String nameShort;


    Progetto(String nameProject, String nameModule, String nameLayout, String nameShort) {
        this.setNameProject(nameProject);
        this.setNameModule(nameModule);
        this.setNameLayout(nameLayout);
        this.setNameShort(nameShort);
    }// fine del costruttore

    public static List<String> getNames() {
        List<String> nomi = new ArrayList<>();

        for (Progetto progetto : Progetto.values()) {
            nomi.add(progetto.nameProject);
        }// end of for cycle

        return nomi;
    }// end of method

    public static Progetto getProgetto(String nameProject) {
        for (Progetto progetto : Progetto.values()) {
            if (progetto.getNameProject().equals(nameProject)) {
                return progetto;
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of static method

    public String getNameProject() {
        return nameProject;
    }// end of method

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }// end of method

    public String getNameModule() {
        return nameModule;
    }// end of method

    public void setNameModule(String nameModule) {
        this.nameModule = nameModule;
    }// end of method

    public String getNameLayout() {
        return nameLayout;
    }// end of method

    public void setNameLayout(String nameLayout) {
        this.nameLayout = nameLayout;
    }// end of method

    public String getNameShort() {
        return nameShort;
    }// end of method

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }// end of method

}// end of enumeration class
