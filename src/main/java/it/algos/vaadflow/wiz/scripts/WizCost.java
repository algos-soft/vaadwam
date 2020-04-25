package it.algos.vaadflow.wiz.scripts;

import org.slf4j.Logger;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 13-apr-2020
 * Time: 05:09
 */
public class WizCost {

    //--flag per stampare info di debug
    public static final boolean FLAG_DEBUG_WIZ = true;

    public static final String PROJECT_VAADFLOW = "vaadflow/";

    public static final String NAME_VAADFLOW = "vaadflow";

    public static final String PATH_VAADFLOW_DIR_STANDARD = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow/";

    public static final String PATH_PROJECTS_DIR_STANDARD = "/Users/gac/Documents/IdeaProjects/";

    public static final String DIR_VAADFLOW = "it/algos/vaadflow/";

    public static final String NORMAL_WIDTH = "9em";

    public static final String NORMAL_HEIGHT = "3em";

    public static final String TITOLO_NUOVO_PROGETTO = "Nuovo progetto";

    public static final String TITOLO_MODIFICA_PROGETTO = "Modifica progetto esistente";

    public static final String DIR_DOC = "documentation/";

    public static final String DIR_LINKS = "links/";

    public static final String DIR_SNIPPETS = "snippets/";

    public static final String DIR_RESOURCES_NAME = "resources/";

    public static final String DIR_APPLICATION = "application/";

    public static final String DIR_MODULES = "modules/";

    public static final String APP_NAME = "application";

    public static final String FILE_READ = "README";

    public static final String FILE_COST = "Cost";

    public static final String FILE_BOOT = "Boot";

    public static final String FILE_VERS = "Vers";

    public static final String FILE_HOME = "Home";

    public static final String FILE_POM = "pom";

    public static final String FILE_BANNER = "banner";

    public static final String FILE_GIT = ".gitignore";

    public static final String FILE_PROPERTIES = "properties";

    public static final String FILE_PROPERTIES_DEST = "application.properties";

    public static final String TXT_SUFFIX = ".txt";

    public static final String XML_SUFFIX = ".xml";

    public static final String JAVA_SUFFIX = ".java";

    //--parte dal livello di root del progetto
    //--contiene java e resources di ogni progetto
    public static final String DIR_MAIN = "src/main/";

    //--parte dal livello di root del progetto
    //--contiene i moduli, di solito due (vaadFlow e vaadTest)
    public static final String DIR_ALGOS = DIR_MAIN + "java/it/algos/";

    //--parte dal livello di root del progetto
    //--contiene META_INF
    //--contiene application.properties
    //--contiene banner.txt (di solito)
    public static final String DIR_RESOURCES = DIR_MAIN + DIR_RESOURCES_NAME;

    //--parte dal livello di root del progetto
    //--contiene images/ (di solito)
    //--contiene src/ (di solito)
    //--contiene styles/ (sempre)
    public static final String DIR_FRONT_END = DIR_RESOURCES + "META_INF/resources/frontend/";

    //--parte dal livello di root del progetto
    //--valida SOLO per progetto vaadFlow
    public static final String DIR_VAADFLOW_SOURCES = DIR_ALGOS + PROJECT_VAADFLOW + "wiz/sources/";


    //--metodo statico invocato da WizDialog.regolazioniIniziali()
    public static void printInfo(Logger log) {
        if (FLAG_DEBUG_WIZ) {
            System.out.println("");
            System.out.println("********************");
            System.out.println("Costanti statiche");
            System.out.println("********************");
            System.out.println("PATH_VAADFLOW_DIR_STANDARD = " + PATH_VAADFLOW_DIR_STANDARD);
            System.out.println("PATH_PROJECTS_DIR_STANDARD = " + PATH_PROJECTS_DIR_STANDARD);
            System.out.println("DIR_MAIN = " + DIR_MAIN);
            System.out.println("DIR_ALGOS = " + DIR_ALGOS);
            System.out.println("DIR_RESOURCES = " + DIR_RESOURCES);
            System.out.println("DIR_FRONTEND = " + DIR_FRONT_END);
            System.out.println("DIR_VAADFLOW_SOURCES = " + DIR_VAADFLOW_SOURCES);
            System.out.println("");
        }// end of if cycle
    }// end of static method

}// end of class
