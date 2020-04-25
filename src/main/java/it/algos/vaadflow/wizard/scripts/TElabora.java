package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wiz.enumeration.Chiave;
import it.algos.vaadflow.wiz.enumeration.EAToken;
import it.algos.vaadflow.wiz.enumeration.Progetto;
import it.algos.vaadflow.wiz.enumeration.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 06-mar-2018
 * Time: 08:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class TElabora {

    public static final String PROPERTY_ORDINE_NAME = "Ordine";

    private static final String A_CAPO = "\n";

    private static final String TAB = "\t";

    private static final String SEP = "/";

    private static final String JAVA_SUFFIX = ".java";

    private static final String SOURCE_SUFFIX = ".txt";

    private static final String COST_SUFFIX = "Cost";

    private static final String BOOT_SUFFIX = "Boot";

    private static final String VERS_SUFFIX = "Vers";

    private static final String TAG = "TAG_";

    private static final String IMPORT = "import it.algos.";

    private static final String DIR_MAIN = "/src/main";

    private static final String DIR_JAVA = DIR_MAIN + "/java/it/algos";

    private static final String PREFIX_NAME = "vaad";

    private static final String PROJECT_BASE_NAME = "vaadflow";

    private static final String DIR_PROJECT_BASE = DIR_JAVA + "/" + PROJECT_BASE_NAME;

    private static final String SOURCES_NAME = "wizard/sources";

    private static final String APP_NAME = "application";

    private static final String SECURITY_NAME = "security";

    private static final String RESOURCES_NAME = "/resources";

    private static final String META_NAME = "/META-INF";

    private static final String UI_NAME = "ui";

    private static final String ENTITIES_NAME = "modules";

    private static final String LAYOUT_NAME = "MainLayout";

    private static final String LIB_NAME = "lib";

    private static final String DIR_FLOW_BOOT = "service";

    private static final String FLOW_BOOT_NAME = "ABootService";

    private static final String DIR_SOURCES = DIR_PROJECT_BASE + SEP + SOURCES_NAME;

    private static final String SUPERCLASS_ENTITY = "AEntity";

    private static final String SUPERCLASS_ENTITY_COMPANY = "ACEntity";

    private static final String SUPERCLASS_GRID = "AGridViewList";

    private static final String SUPERCLASS_PAGINATED_GRID = "APaginatedGridViewList";

    private static final String PROPERTY = "Property";

    private static final String METHOD = "Method";

    private static final String READ = "README";

    private static final String PROPERTY_COMPANY_NAME = "Company";

    private static final String PROPERTY_CODE_NAME = "Code";

    private static final String PROPERTY_DESCRIZIONE_NAME = "Descrizione";

    private static final String PROPERTY_ORDINE_SOURCE_NAME = PROPERTY + PROPERTY_ORDINE_NAME + SOURCE_SUFFIX;

    private static final String PROPERTY_CODE_SOURCE_NAME = PROPERTY + PROPERTY_CODE_NAME + SOURCE_SUFFIX;

    private static final String PROPERTY_DESCRIZIONE_SOURCE_NAME = PROPERTY + PROPERTY_DESCRIZIONE_NAME + SOURCE_SUFFIX;

    private static final String METHOD_FIND = METHOD + "Find" + SOURCE_SUFFIX;

    private static final String METHOD_NEW_ORDINE = METHOD + "NewOrdine" + SOURCE_SUFFIX;

    private static final String METHOD_NEW_ORDINE_COMPANY = METHOD + "NewOrdineCompany" + SOURCE_SUFFIX;

    private static final String METHOD_ID_KEY_SPECIFICA = METHOD + "IdKeySpecifica" + SOURCE_SUFFIX;

    private static final String METHOD_READ_COMPANY = METHOD + "ReadCompany" + SOURCE_SUFFIX;

    private static final String METHOD_CREA_GRID = METHOD + "CreaGrid" + SOURCE_SUFFIX;

    private static final String METHOD_POST_CONSTRUCT = METHOD + "PostConstruct" + SOURCE_SUFFIX;

    private static final String METHOD_SET_PARAMETER = METHOD + "SetParameter" + SOURCE_SUFFIX;

    private static final String METHOD_BEFORE_ENTER = METHOD + "BeforeEnter" + SOURCE_SUFFIX;

    private static final String METHOD_FIX_PREFERENZE = METHOD + "FixPreferenze" + SOURCE_SUFFIX;

    private static final String METHOD_FIX_LAYOUT = METHOD + "FixLayout" + SOURCE_SUFFIX;

    private static final String METHOD_CREA_ALERT_LAYOUT = METHOD + "CreaAlertLayout" + SOURCE_SUFFIX;

    private static final String METHOD_CREA_TOP_LAYOUT = METHOD + "CreaTopLayout" + SOURCE_SUFFIX;

    private static final String METHOD_CREA_POPUP_FILTRO = METHOD + "CreaPopupFiltro" + SOURCE_SUFFIX;

    private static final String METHOD_CREA_FILTRI = METHOD + "CreaFiltri" + SOURCE_SUFFIX;

    private static final String METHOD_UPDATE_FILTRI = METHOD + "UpdateFiltri" + SOURCE_SUFFIX;

    private static final String METHOD_ADD_LISTENERS = METHOD + "AddListeners" + SOURCE_SUFFIX;

    private static final String VIEW_SUFFIX = "List";

    private static final String POM = "pom";

    //    private static final String COST_NAME = "ProjectCost";
    private static final String HOME_NAME = "HomeView";

    private static final String DETAILS_NAME = "AUserDetailsService";

    private static final String CONFIGURATION_NAME = "SecurityConfiguration";

    private static final String DIR_DOC = "documentation";

    private static final String DIR_LINKS = "links";

    private static final String DIR_SNIPPETS = "snippets";

    private static final String GIT = ".gitignore";

    private static final String END = "}// end of method";

    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AFileService file;

    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;

    public String newEntityName;           //--dal dialogo di input

    public boolean flagOrdine;             //--dal dialogo di input

    public boolean flagCode;               //--dal dialogo di input

    public boolean flagDescrizione;        //--dal dialogo di input

    public boolean flagKeyCode;            //--dal dialogo di input

    public boolean flagCompany;            //--dal dialogo di input

    public boolean flagSecurity;            //--dal dialogo di input

    public boolean flagSovrascriveFile;        //--dal dialogo di input

    public boolean flagSovrascriveDirectory;        //--dal dialogo di input

    public boolean flagDocumentation;            //--dal dialogo di input

    public boolean flagLinks;            //--dal dialogo di input

    public boolean flagSnippets;            //--dal dialogo di input

    public boolean flagDirectoryFlow;            //--dal dialogo di input

    public boolean flagDirectoryNewProject;            //--dal dialogo di input

    public boolean flagResources;            //--dal dialogo di input

    public boolean flagProperties;            //--dal dialogo di input

    public boolean flagRead;            //--dal dialogo di input

    public boolean flagGit;            //--dal dialogo di input

    public boolean flagPom;            //--dal dialogo di input

    public boolean flagGrid;            //--dal dialogo di input

    public boolean flagList;            //--dal dialogo di input

    protected boolean isNuovoProgetto;

    //--regolate indipendentemente dai risultati del dialogo
    private String userDir;                 //--di sistema

    private String ideaProjectRootPath;     //--userDir meno PROJECT_BASE_NAME

    private String projectBasePath;         //--ideaProjectRootPath più PROJECT_BASE_NAME

    private String sourcePath;              //--projectBasePath più DIR_SOURCES

    //--risultati del dialogo
    private String targetProjectName;       //--dal dialogo di input

    private String targetModuleName;        //--dal dialogo di input

    private String targetLayoutName;        //--dal dialogo di input

    private String newProjectName;          //--dal dialogo di input

    private String newPackageName;          //--dal dialogo di input

    private String newEntityTag;            //--dal dialogo di input

    private boolean flagAllPackages;        //--dal dialogo di input

    //--regolate elaborando i risultati del dialogo
    private String projectPath;         //--ideaProjectRootPath più targetProjectName (usato come radice per pom.xml e README.text)

    private String targetModuleCapitalName;   //--targetModuleName con la prima maiuscola

    private String projectJavaPath;     //--projectPath più DIR_JAVA più targetProjectName

    private String applicationPath;     //--projectJavaPath più APP_NAME

    private String costPath;          //--applicationPath più LAYOUT_SUFFIX più JAVA_SUFFIX

    private String bootPath;          //--applicationPath più LAYOUT_SUFFIX più JAVA_SUFFIX

    private String versPath;          //--applicationPath più LAYOUT_SUFFIX più JAVA_SUFFIX

    private String flowBootPath;          //--applicationPath più LAYOUT_SUFFIX più JAVA_SUFFIX

    private String uiPath;              //--projectJavaPath più UI_NAME

    private String entityPath;          //--projectJavaPath più newPackageName

    private String nameShort;

    private String nameClassCost;

    private String nameClassBoot;

    private String nameClassVers;

    private String firstCharProject;

    private String packagePath;         //--entityPath più newPackageName

    private String nameCost;        //--NAME_COST (springvaadin) o NAME_APP_COST (altri progetti)

    private String dirCost;         //--DIR_LIB (springvaadin) o DIR_APP (altri progetti)

    private String pathFileCost;    //--pathModulo più lib (springvaadin) o application (altri progetti)

    private String qualifier;       //--NAME_COST (springvaadin) o NAME_APP_COST (altri progetti) più TAG più tagBreveTreChar

    private String qualifierView;   //--NAME_COST (springvaadin) o NAME_APP_COST (altri progetti) più VIEW più tagBreveTreChar

    private String queryText;

    private String layoutPath;

    private String propertiesText;

    private String parametersEntityText;

    private String parametersDocText;

    private String parametersText;

    private String methodFindText;

    private String methodNewEntityText;

    private String methodNewOrdineText;

    private String methodIdKeySpecificaText;

    private String methodKeyUnicaText;

    private String methodEstendeText;

    private String methodUsaCompanyText;

    private String methodAddCompanyText;

    private String methodBuilderText;

    private String methodReadCompanyText;

    private String superClassEntity;

    private String importCost;

    private String gridSuperclass;


    public TElabora() {
    }// end of constructor


    /**
     * Creazione di un nuovo project
     */
    public void newProject(Map<Chiave, Object> mappaInput) {
        isNuovoProgetto = true;
        this.regolazioni(mappaInput);
        this.copiaDirectoryBase();
        this.regolaDirectorySpecifica();
    }// end of method


    /**
     * Update di un project esistente
     */
    public void updateProject(Map<Chiave, Object> mappaInput) {
        isNuovoProgetto = false;
        this.regolazioni(mappaInput);
        this.copiaDirectoryBase();
        this.regolaDirectorySpecifica();
    }// end of method


    /**
     * Regolazioni iniziali indipendenti dal dialogo di input
     * Regolazioni iniziali con i valori del dialogo di input
     */
    public void regolazioni(Map<Chiave, Object> mappaInput) {
        this.regola();
        this.regolaProgetto(mappaInput);
    }// end of method


    /**
     * Directory it.algos.vaadflow
     */
    public void copiaDirectoryBase() {
        if (flagDirectoryFlow) {
            copiaDirectoriesBaseFlow();
        }// end of if cycle
    }// end of method


    /**
     * Directory specifica
     */
    public void regolaDirectorySpecifica() {
        if (flagDirectoryNewProject) {
            this.copiaBase();
            this.copiaExtra();
        }// end of if cycle
    }// end of method


    /**
     * Directory specifica
     */
    public void copiaBase() {
        this.creaProjectModule();
        this.creaApplicationMain();
        this.creaApplicationDirectory();
        this.creaApplicationFolderContent();
        this.creaSecurityDirectory();
        this.creaSecurityFolderContent();
        this.creaModulesDirectory();
    }// end of method


    private void copiaExtra() {
        this.regolaDocumentation();
        this.regolaLinks();
        this.regolaSnippets();

        this.regolaRead();
        this.regolaGit();
        this.regolaProperties();
        this.regolaPom();
        this.copiaMetaInf();

//        this.copiaResources();
    }// end of method


    /**
     * Creazione completa del package
     * Crea una directory
     * Crea i files previsti nella enumeration
     */
    public void newPackage(Map<Chiave, Object> mappaInput) {
        this.regolazioni(mappaInput);

        if (mappaInput.containsKey(Chiave.flagUsaAllPackages)) {
            this.flagAllPackages = (boolean) mappaInput.get(Chiave.flagUsaAllPackages);
        } else {
            this.flagAllPackages = false;
        }// end of if/else cycle

        if (flagAllPackages) {
            allPackages(mappaInput);
        } else {
            singoloPackage(mappaInput);
        }// end of if/else cycle
    }// end of method


    public void allPackages(Map<Chiave, Object> mappaInput) {
        List<String> packages = recuperaPackagesEsistenti();
        String name;
        String tag;

        for (String pack : packages) {
            mappaInput.put(Chiave.newPackageName, pack);

            name = text.primaMaiuscola(pack);
            mappaInput.put(Chiave.newEntityName, name);

            tag = pack.substring(0, 3).toUpperCase();
            mappaInput.put(Chiave.newEntityTag, tag);

            singoloPackage(mappaInput);
        }// end of for cycle

    }// end of method


    public void singoloPackage(Map<Chiave, Object> mappaInput) {
        this.regolaTag(mappaInput);
        if (text.isEmpty(newEntityName)) {
            return;
        }// end of if cycle
        this.creaDirectory();
        this.creaTasks(mappaInput);
        this.addPackageMenu();
        this.addTagCostanti();
    }// end of method


    /**
     * Regolazioni iniziali indipendenti dal dialogo di input
     */
    private void regola() {
        this.userDir = System.getProperty("user.dir");
        this.ideaProjectRootPath = text.levaCodaDa(userDir, SEP);

        if (isNuovoProgetto) {
            this.projectBasePath = ideaProjectRootPath + SEP + PROJECT_BASE_NAME;
        } else {
            this.projectBasePath = ideaProjectRootPath + SEP + "operativi" + SEP + PROJECT_BASE_NAME;
        }// end of if/else cycle

        this.sourcePath = projectBasePath + DIR_SOURCES;
        this.ideaProjectRootPath = text.levaCodaDa(ideaProjectRootPath, SEP);
    }// end of method


    /**
     * Regolazioni iniziali con i valori del dialogo di input
     */
    private void regolaProgetto(Map<Chiave, Object> mappaInput) {
        Object projectValue;
        Progetto progetto;
        String projectName;

        if (mappaInput.containsKey(Chiave.newProjectName) && mappaInput.get(Chiave.newProjectName) != null) {
            this.newProjectName = (String) mappaInput.get(Chiave.newProjectName);
        } else {
            this.newProjectName = targetProjectName;
        }// end of if/else cycle
        if (mappaInput.containsKey(Chiave.flagSecurity)) {
            this.flagSecurity = (boolean) mappaInput.get(Chiave.flagSecurity);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagSovrascriveFile)) {
            this.flagSovrascriveFile = (boolean) mappaInput.get(Chiave.flagSovrascriveFile);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagSovrascriveDirectory)) {
            this.flagSovrascriveDirectory = (boolean) mappaInput.get(Chiave.flagSovrascriveDirectory);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagDocumentation)) {
            this.flagDocumentation = (boolean) mappaInput.get(Chiave.flagDocumentation);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagLinks)) {
            this.flagLinks = (boolean) mappaInput.get(Chiave.flagLinks);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagSnippets)) {
            this.flagSnippets = (boolean) mappaInput.get(Chiave.flagSnippets);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagDirectoryFlow)) {
            this.flagDirectoryFlow = (boolean) mappaInput.get(Chiave.flagDirectoryFlow);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagDirectoryNewProject)) {
            this.flagDirectoryNewProject = (boolean) mappaInput.get(Chiave.flagDirectoryNewProject);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagResources)) {
            this.flagResources = (boolean) mappaInput.get(Chiave.flagResources);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagProperties)) {
            this.flagProperties = (boolean) mappaInput.get(Chiave.flagProperties);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagRead)) {
            this.flagRead = (boolean) mappaInput.get(Chiave.flagRead);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagGit)) {
            this.flagGit = (boolean) mappaInput.get(Chiave.flagGit);
        }// end of if cycle
        if (mappaInput.containsKey(Chiave.flagPom)) {
            this.flagPom = (boolean) mappaInput.get(Chiave.flagPom);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.targetProjectName) && mappaInput.get(Chiave.targetProjectName) != null) {
            projectValue = mappaInput.get(Chiave.targetProjectName);
            if (projectValue instanceof Progetto) {
                progetto = (Progetto) projectValue;
                this.targetProjectName = progetto.getNameProject().toLowerCase();
                this.targetModuleName = progetto.getNameModule().toLowerCase();
                this.targetLayoutName = progetto.getNameLayout().toLowerCase();
                this.nameShort = progetto.getNameShort();
            } else {
                if (projectValue instanceof String) {
                    projectName = (String) projectValue;
                    this.targetProjectName = projectName.toLowerCase();
                    this.targetModuleName = projectName.toLowerCase();
                    this.targetLayoutName = projectName.toLowerCase();
                    this.nameShort = text.primaMaiuscola(projectName);
                }// end of if cycle
            }// end of if/else cycle
            this.targetModuleCapitalName = text.primaMaiuscola(targetModuleName);
            this.projectPath = ideaProjectRootPath + SEP + targetProjectName;
            this.projectJavaPath = projectPath + DIR_JAVA + SEP + targetModuleName;
            this.applicationPath = projectJavaPath + SEP + APP_NAME;
            this.uiPath = projectJavaPath + SEP + UI_NAME;
            this.entityPath = projectJavaPath + SEP + ENTITIES_NAME;
            this.flowBootPath = projectJavaPath + SEP + DIR_FLOW_BOOT + SEP + FLOW_BOOT_NAME + JAVA_SUFFIX;

            this.nameClassCost = nameShort + COST_SUFFIX;
            this.nameClassBoot = nameShort + BOOT_SUFFIX;
            this.nameClassVers = nameShort + VERS_SUFFIX;
            this.costPath = projectJavaPath + SEP + APP_NAME + SEP + nameClassCost + JAVA_SUFFIX;
            this.bootPath = projectJavaPath + SEP + APP_NAME + SEP + nameClassBoot + JAVA_SUFFIX;
            this.versPath = projectJavaPath + SEP + APP_NAME + SEP + nameClassVers + JAVA_SUFFIX;

            this.layoutPath = projectPath + DIR_JAVA + SEP + targetLayoutName + SEP + LAYOUT_NAME + JAVA_SUFFIX;
//            if (mappaInput.containsKey(Chiave.newProjectName) && mappaInput.get(Chiave.newProjectName) != null) {
//                this.newProjectName = (String) mappaInput.get(Chiave.newProjectName);
//            } else {
//                this.newProjectName = targetProjectName;
//            }// end of if/else cycle
//            if (mappaInput.containsKey(Chiave.flagSecurity)) {
//                this.flagSecurity = (boolean) mappaInput.get(Chiave.flagSecurity);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagSovrascriveFile)) {
//                this.flagSovrascriveFile = (boolean) mappaInput.get(Chiave.flagSovrascriveFile);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagSovrascriveDirectory)) {
//                this.flagSovrascriveDirectory = (boolean) mappaInput.get(Chiave.flagSovrascriveDirectory);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagDocumentation)) {
//                this.flagDocumentation = (boolean) mappaInput.get(Chiave.flagDocumentation);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagLinks)) {
//                this.flagLinks = (boolean) mappaInput.get(Chiave.flagLinks);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagSnippets)) {
//                this.flagSnippets = (boolean) mappaInput.get(Chiave.flagSnippets);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagDirectoryFlow)) {
//                this.flagDirectoryFlow = (boolean) mappaInput.get(Chiave.flagDirectoryFlow);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagDirectoryNewProject)) {
//                this.flagDirectoryNewProject = (boolean) mappaInput.get(Chiave.flagDirectoryNewProject);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagResources)) {
//                this.flagResources = (boolean) mappaInput.get(Chiave.flagResources);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagProperties)) {
//                this.flagProperties = (boolean) mappaInput.get(Chiave.flagProperties);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagRead)) {
//                this.flagRead = (boolean) mappaInput.get(Chiave.flagRead);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagGit)) {
//                this.flagGit = (boolean) mappaInput.get(Chiave.flagGit);
//            }// end of if cycle
//            if (mappaInput.containsKey(Chiave.flagPom)) {
//                this.flagPom = (boolean) mappaInput.get(Chiave.flagPom);
//            }// end of if cycle
            this.firstCharProject = nameShort.substring(0, 1);
        } else {
            if (mappaInput.containsKey(Chiave.newProjectName) && mappaInput.get(Chiave.newProjectName) != null) {
                this.newProjectName = (String) mappaInput.get(Chiave.newProjectName);
                this.projectPath = ideaProjectRootPath + SEP + newProjectName;
                this.projectJavaPath = projectPath + DIR_JAVA + SEP + newProjectName;
                this.applicationPath = projectJavaPath + SEP + APP_NAME;
                this.uiPath = projectJavaPath + SEP + UI_NAME;
                this.entityPath = projectJavaPath + SEP + ENTITIES_NAME;
                this.flowBootPath = projectJavaPath + SEP + DIR_FLOW_BOOT + SEP + FLOW_BOOT_NAME + JAVA_SUFFIX;
            } else {
                this.newProjectName = targetProjectName;
                this.projectPath = ideaProjectRootPath + SEP + newProjectName;
                this.projectJavaPath = projectPath + DIR_JAVA + SEP + newProjectName;
                this.applicationPath = projectJavaPath + SEP + APP_NAME;
                this.uiPath = projectJavaPath + SEP + UI_NAME;
                this.entityPath = projectJavaPath + SEP + ENTITIES_NAME;
                this.flowBootPath = projectJavaPath + SEP + DIR_FLOW_BOOT + SEP + FLOW_BOOT_NAME + JAVA_SUFFIX;
            }// end of if/else cycle
        }// end of if/else cycle

        if (mappaInput.containsKey(Chiave.newEntityTag) && text.isValid(mappaInput.get(Chiave.newEntityTag))) {
            this.newEntityTag = (String) mappaInput.get(Chiave.newEntityTag);
            this.firstCharProject = newEntityTag.substring(0, 1);
        }// end of if cycle

        log.info("Conferma nome nuovo progetto: " + newProjectName);
        log.info("Conferma directory nuovo progetto: " + projectPath);
    }// end of method


    private void regolaTag(Map<Chiave, Object> mappaInput) {
        Progetto progetto;

        if (mappaInput.containsKey(Chiave.newEntityName)) {
            this.newEntityName = (String) mappaInput.get(Chiave.newEntityName);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.newPackageName)) {
            this.newPackageName = (String) mappaInput.get(Chiave.newPackageName);
            this.packagePath = entityPath + SEP + newPackageName;
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.newEntityTag)) {
            this.newEntityTag = (String) mappaInput.get(Chiave.newEntityTag);
            this.qualifier = TAG + newEntityTag;
            this.firstCharProject = newEntityTag.substring(0, 1);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagOrdine)) {
            this.flagOrdine = (boolean) mappaInput.get(Chiave.flagOrdine);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagCode)) {
            this.flagCode = (boolean) mappaInput.get(Chiave.flagCode);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagDescrizione)) {
            this.flagDescrizione = (boolean) mappaInput.get(Chiave.flagDescrizione);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagKeyCode)) {
            this.flagKeyCode = (boolean) mappaInput.get(Chiave.flagKeyCode);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagCompany)) {
            this.flagCompany = (boolean) mappaInput.get(Chiave.flagCompany);
            if (flagCompany) {
                superClassEntity = SUPERCLASS_ENTITY_COMPANY;
            } else {
                superClassEntity = SUPERCLASS_ENTITY;
            }// end of if/else cycle
        }// end of if cycle


        if (mappaInput.containsKey(Chiave.flagGrid)) {
            this.flagGrid = (boolean) mappaInput.get(Chiave.flagGrid);
            if (flagGrid) {
                gridSuperclass = SUPERCLASS_PAGINATED_GRID;
            } else {
                gridSuperclass = SUPERCLASS_GRID;
            }// end of if/else cycle
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagList)) {
            this.flagList = (boolean) mappaInput.get(Chiave.flagList);
            if (flagList) {
//                gridSuperclass = SUPERCLASS_PAGINATED_GRID;//@todo DODODODO
            } else {
//                gridSuperclass = SUPERCLASS_GRID;//@todo DODODODO
            }// end of if/else cycle
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagSovrascriveFile)) {
            this.flagSovrascriveFile = (boolean) mappaInput.get(Chiave.flagSovrascriveFile);
        }// end of if cycle

        if (mappaInput.containsKey(Chiave.flagUsaAllPackages)) {
            this.flagAllPackages = (boolean) mappaInput.get(Chiave.flagUsaAllPackages);
        }// end of if cycle

    }// end of method


    private void creaDirectory() {
        if (text.isValid(newPackageName)) {
            if (!file.isEsisteDirectory(entityPath)) {
                log.warn("Non esisteva la directory " + ENTITIES_NAME + " nel progetto " + targetProjectName + " e l'ho creata");
                file.creaDirectory(entityPath);
            }// end of if cycle

            file.creaDirectory(packagePath);
        }// end of if cycle
    }// end of method


    private void creaTasks(Map<Chiave, Object> mappaInput) {
        for (Task task : Task.values()) {
            creaTask(task, mappaInput);
        }// end of for cycle
    }// end of method


    private void creaTask(Task task, Map<Chiave, Object> mappaInput) {
        String nomeFileTextSorgente = "";
        String sourceTemplatesText = "";
        String newTaskText = "";

        if (text.isEmpty(newEntityName)) {
            return;
        }// end of if cycle

        nomeFileTextSorgente = task.getSourceName(flagCompany);
        sourceTemplatesText = leggeFile(nomeFileTextSorgente);
        newTaskText = replaceText(sourceTemplatesText);

        this.checkAndWriteFileTask(task, newTaskText);
    }// end of method


    private String leggeFile(String nomeFileTextSorgente) {
        String suffix = ".txt";

        if (!nomeFileTextSorgente.endsWith(suffix)) {
            nomeFileTextSorgente += suffix;
        }// end of if cycle

        return file.leggeFile(sourcePath + SEP + nomeFileTextSorgente);
    }// end of method


    private void checkAndWriteFileTask(Task task, String newTaskText) {
        String fileNameJava = "";
        String pathFileJava;
        String oldFileText = "";

        fileNameJava = newEntityName + task.getJavaClassName();
        pathFileJava = packagePath + SEP + fileNameJava;

        if (flagSovrascriveFile) {
            file.sovraScriveFile(pathFileJava, newTaskText);
            System.out.println(fileNameJava + " esisteva già ed è stato modificato");
        } else {
            oldFileText = file.leggeFile(pathFileJava);
            if (text.isValid(oldFileText)) {
                if (checkFile(oldFileText)) {
                    file.sovraScriveFile(pathFileJava, newTaskText);
                    System.out.println(fileNameJava + " esisteva già ed è stato modificato");
                } else {
                    writeDocOnly(pathFileJava, oldFileText, newTaskText);
                    System.out.println(fileNameJava + " esisteva già ed è stato modificato SOLO nella documentazione");
                }// end of if/else cycle
            } else {
                file.scriveNewFile(pathFileJava, newTaskText);
                System.out.println(fileNameJava + " non esisteva ed è stato creato");
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Sostituisce SOLO la documentazione in testa al file
     * Sostituisce fino al tag @AIScript. Se manca il tag, sovrascrive tutto.
     */
    private void writeDocOnly(String pathFileJava, String oldFileText, String newTaskText) {
        String tagIni = "/**";
        String tagEndPre = "*/";
        String tagEnd = "@AIScript";
        int oldPosIni = oldFileText.indexOf(tagIni);
        int oldPosEnd = oldFileText.indexOf(tagEndPre, oldPosIni);
        oldPosEnd = oldFileText.indexOf(tagEnd, oldPosEnd);
        int newPosIni = newTaskText.indexOf(tagIni);
        int newPosEnd = newTaskText.indexOf(tagEndPre, newPosIni);
        newPosEnd = newTaskText.indexOf(tagEnd, newPosEnd);
        String oldDoc = oldFileText.substring(oldPosIni, oldPosEnd);
        String newDoc = newTaskText.substring(newPosIni, newPosEnd);
        String newFileText = oldFileText.replace(oldDoc, newDoc);

        file.sovraScriveFile(pathFileJava, newFileText);
    }// end of method


    private void checkAndWriteFile(String pathNewFile, String sourceText) {
        String fileNameJava = "";
        String oldText = "";

        if (flagSovrascriveFile) {
            file.scriveNewFile(pathNewFile, sourceText);
            System.out.println(fileNameJava + " esisteva già ed è stato modificato");
        } else {
            oldText = file.leggeFile(pathNewFile);
            if (text.isValid(oldText)) {
                if (checkFile(oldText)) {
                    file.scriveNewFile(pathNewFile, sourceText);
                    System.out.println(fileNameJava + " esisteva già ed è stato modificato");
                } else {
                    System.out.println(fileNameJava + " esisteva già e NON è stato modificato");
                }// end of if/else cycle
            } else {
                file.scriveNewFile(pathNewFile, sourceText);
                System.out.println(fileNameJava + " non esisteva ed è stato creato");
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    private boolean checkFile(String oldFileText) {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("@AIScript(sovrascrivibile = false)");
        tags.add("@AIScript(sovrascrivibile=false)");
        tags.add("@AIScript(sovrascrivibile= false)");
        tags.add("@AIScript(sovrascrivibile =false)");

        return text.nonContiene(oldFileText, tags);
    }// end of method


    private String replaceText(String sourceText) {
        Map<EAToken, String> mappa = new HashMap<>();

        mappa.put(EAToken.nameTargetProject, targetProjectName);
        mappa.put(EAToken.moduleNameMinuscolo, targetModuleName);
        mappa.put(EAToken.moduleNameMaiuscolo, targetModuleCapitalName);
        mappa.put(EAToken.first, firstCharProject);
        mappa.put(EAToken.packageName, newPackageName);
        mappa.put(EAToken.projectCost, nameShort + COST_SUFFIX);
        mappa.put(EAToken.user, "Gac");
        mappa.put(EAToken.today, LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        mappa.put(EAToken.qualifier, qualifier != null ? qualifier : "");
        mappa.put(EAToken.tagView, "");
        mappa.put(EAToken.entity, newEntityName);
        mappa.put(EAToken.estendeEntity, creaEstendeEntity());
        mappa.put(EAToken.superClassEntity, superClassEntity);
        mappa.put(EAToken.usaCompany, creaUsaCompany());
        mappa.put(EAToken.usaSecurity, creaUsaCompany());
        mappa.put(EAToken.readCompany, creaReadCompany());
        mappa.put(EAToken.grid, gridSuperclass);
        mappa.put(EAToken.creaGrid, creaGrid());
        mappa.put(EAToken.postConstruct, postConstruct());
        mappa.put(EAToken.setParameter, setParameter());
        mappa.put(EAToken.beforeEnter, beforeEnter());
        mappa.put(EAToken.fixPreferenze, fixPreferenze());
        mappa.put(EAToken.fixLayout, fixLayout());
        mappa.put(EAToken.creaAlertLayout, creaAlertLayout());
        mappa.put(EAToken.creaTopLayout, creaTopLayout());
        mappa.put(EAToken.creaPopupFiltro, creaPopupFiltro());
        mappa.put(EAToken.creaFiltri, creaFiltri());
        mappa.put(EAToken.updateFiltri, updateFiltri());
        mappa.put(EAToken.addListeners, addListeners());
        mappa.put(EAToken.methodFind, creaFind());
        mappa.put(EAToken.parametersDoc, creaParametersDoc());
        mappa.put(EAToken.keyUnica, creaKeyUnica());
        mappa.put(EAToken.builder, creaBuilder());
        mappa.put(EAToken.methodNewOrdine, creaNewOrdine());
        mappa.put(EAToken.methodIdKeySpecifica, creaIdKeySpecifica());
        mappa.put(EAToken.parameters, creaParameters());
        mappa.put(EAToken.parametersNewEntity, creaParametersNewEntity());
        mappa.put(EAToken.query, creaQuery());
        mappa.put(EAToken.findAll, creaFindAll());
        mappa.put(EAToken.properties, creaProperties());
        mappa.put(EAToken.propertyOrdine, creaPropertyOrdine());
        mappa.put(EAToken.propertyCode, creaPropertyCode());
        mappa.put(EAToken.propertyDescrizione, creaPropertyDescrizione());
        mappa.put(EAToken.toString, creaToString());

        return EAToken.replaceAll(sourceText, mappa);
    }// end of method


    private String creaFind() {
        methodFindText = "";

        if (flagCode) {
            methodFindText += leggeFile(METHOD_FIND);
            methodFindText = EAToken.replace(EAToken.entity, methodFindText, newEntityName);
            methodFindText = EAToken.replace(EAToken.parameters, methodFindText, creaParameters());
            methodFindText = EAToken.replace(EAToken.parametersFind, methodFindText, creaParametersFind());
        }// end of if cycle

        return methodFindText;
    }// end of method


    private String creaNewOrdine() {
        methodNewOrdineText = "";
        String nomeFileSorgente = flagCompany ? METHOD_NEW_ORDINE_COMPANY : METHOD_NEW_ORDINE;

        if (flagOrdine) {
            methodNewOrdineText += leggeFile(nomeFileSorgente);
            methodNewOrdineText = EAToken.replace(EAToken.entity, methodNewOrdineText, newEntityName);
        }// end of if cycle

        return methodNewOrdineText;
    }// end of method


    private String creaIdKeySpecifica() {
        methodIdKeySpecificaText = "";

        if (flagCode && flagKeyCode) {
            methodIdKeySpecificaText += leggeFile(METHOD_ID_KEY_SPECIFICA);
            methodIdKeySpecificaText = EAToken.replace(EAToken.entity, methodIdKeySpecificaText, newEntityName);
        }// end of if cycle

        return methodIdKeySpecificaText;
    }// end of method


    private String creaReadCompany() {
        methodReadCompanyText = "";

        if (flagCompany) {
            methodReadCompanyText += leggeFile(METHOD_READ_COMPANY);
            methodReadCompanyText = EAToken.replace(EAToken.entity, methodReadCompanyText, newEntityName);
        }// end of if cycle

        return methodReadCompanyText;
    }// end of method


    private String creaGrid() {
        String testo = "";

        if (flagGrid) {
            testo += leggeFile(METHOD_CREA_GRID);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String postConstruct() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_POST_CONSTRUCT);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String setParameter() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_SET_PARAMETER);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String beforeEnter() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_BEFORE_ENTER);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String fixPreferenze() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_FIX_PREFERENZE);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String fixLayout() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_FIX_LAYOUT);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String creaAlertLayout() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_CREA_ALERT_LAYOUT);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String creaTopLayout() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_CREA_TOP_LAYOUT);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String creaPopupFiltro() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_CREA_POPUP_FILTRO);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String creaFiltri() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_CREA_FILTRI);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String updateFiltri() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_UPDATE_FILTRI);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    private String addListeners() {
        String testo = "";

        if (flagList) {
            testo += leggeFile(METHOD_ADD_LISTENERS);
            testo = EAToken.replace(EAToken.entity, testo, newEntityName);
        }// end of if cycle

        return testo;
    }// end of method


    public String creaQuery() {
        queryText = "";
        String aCapo = "\n\n\t";
        String parVuoto = "();";
        String parCompany = "(Company company);";
        String companyText = flagCompany ? "ByCompany" : "";
        String companyBy = flagCompany ? "" : "By";
        String entity = "public " + newEntityName + " findBy";
        String entityCompany = entity + "CompanyAnd";
        String listBase = "public List<" + newEntityName + "> findAll" + companyText;
        String listOrdine = listBase + companyBy + "OrderByOrdineAsc";

        if (flagCompany) {
            if (flagCode) {
                queryText += entityCompany + PROPERTY_CODE_NAME + "(Company company, String " + PROPERTY_CODE_NAME.toLowerCase() + ");";
                queryText += aCapo;
            }// end of if cycle
            if (flagOrdine) {
                queryText += listOrdine + parCompany;
                queryText += aCapo;
            } else {
                queryText += listBase + parCompany;
                queryText += aCapo;
            }// end of if/else cycle
        } else {
            if (flagCode) {
                queryText += entity + PROPERTY_CODE_NAME + "(String " + PROPERTY_CODE_NAME.toLowerCase() + ");";
                queryText += aCapo;
            }// end of if cycle
            if (flagOrdine) {
                queryText += listOrdine + parVuoto;
                queryText += aCapo;
            } else {
//                queryText += listBase + parVuoto;
            }// end of if/else cycle
        }// end of if/else cycle

        queryText = text.levaCoda(queryText, aCapo);
        return queryText;
    }// end of method


    public String creaFindAll() {
        String findAll = "";

        if (flagOrdine) {
            if (flagCompany) {
                findAll = "findAllByCompanyOrderBy" + PROPERTY_ORDINE_NAME + "Asc(company)";
            } else {
                findAll = "findAllByOrderBy" + PROPERTY_ORDINE_NAME + "Asc()";
            }// end of if/else cycle
        } else {
            if (flagCompany) {
                findAll = "findAllByCompany(company)";
            } else {
                findAll = "findAll()";
            }// end of if/else cycle
        }// end of if/else cycle

        return findAll;
    }// end of method


    private String creaProperties() {
        propertiesText = "";
        String apici = "\"";
        String virgola = ", ";

        if (flagCompany) {
            propertiesText += apici + PROPERTY_COMPANY_NAME.toLowerCase() + apici + virgola;
        }// end of if cycle
        if (flagOrdine) {
            propertiesText += apici + PROPERTY_ORDINE_NAME.toLowerCase() + apici + virgola;
        }// end of if cycle
        if (flagCode) {
            propertiesText += apici + PROPERTY_CODE_NAME.toLowerCase() + apici + virgola;
        }// end of if cycle
        if (flagDescrizione) {
            propertiesText += apici + PROPERTY_DESCRIZIONE_NAME.toLowerCase() + apici + virgola;
        }// end of if cycle

        propertiesText = text.levaCoda(propertiesText, virgola);
        return propertiesText;
    }// end of method


    private String creaParametersFind() {
        parametersEntityText = "";
        String tagCompany = "(Company) null";
        String tagOrdine = "0";
        String tagDescrizione = "\"\"";
        String virgola = ", ";

        if (flagCompany) {
            parametersEntityText += tagCompany + virgola;
        }// end of if cycle
        if (flagOrdine) {
            parametersEntityText += tagOrdine + virgola;
        }// end of if cycle
        if (flagCode) {
            parametersEntityText += PROPERTY_CODE_NAME.toLowerCase() + virgola;
        }// end of if cycle
        if (flagDescrizione) {
            parametersEntityText += tagDescrizione + virgola;
        }// end of if cycle

        parametersEntityText = text.levaCoda(parametersEntityText, virgola);
        return parametersEntityText;
    }// end of method


    private String creaParametersDoc() {
        parametersDocText = "";
        String inizio = "\n\t* @param ";
        String virgola = ", ";

        if (flagOrdine) {
            parametersDocText += inizio + "ordine      di presentazione (obbligatorio con inserimento automatico se è zero)";
        }// end of if cycle
        if (flagCode) {
            parametersDocText += inizio + "code        codice di riferimento (obbligatorio)";
        }// end of if cycle
        if (flagDescrizione) {
            parametersDocText += inizio + "descrizione (facoltativa, non unica)";
        }// end of if cycle

        parametersDocText = text.levaCoda(parametersDocText, virgola);
        return parametersDocText;
    }// end of method


    private String creaParametersNewEntity() {
        String testo = "";
        String tagCompany = "(Company) null";
        String tagNumerico = "0";
        String tagTesto = "\"\"";
        String virgola = ", ";

        if (flagCompany) {
            testo += tagCompany + virgola;
        }// end of if cycle
        if (flagOrdine) {
            testo += tagNumerico + virgola;
        }// end of if cycle
        if (flagCode) {
            testo += tagTesto + virgola;
        }// end of if cycle
        if (flagDescrizione) {
            testo += tagTesto + virgola;
        }// end of if cycle

        testo = text.levaCoda(testo, virgola);
        return testo;
    }// end of method


    private String creaParameters() {
        parametersText = "";
        String stringa = "String";
        String intero = "int";
        String spazio = " ";
        String virgola = ", ";

        if (flagCompany) {
            parametersText += "Company" + spazio + PROPERTY_COMPANY_NAME.toLowerCase() + virgola;
        }// end of if cycle
        if (flagOrdine) {
            parametersText += intero + spazio + PROPERTY_ORDINE_NAME.toLowerCase() + virgola;
        }// end of if cycle
        if (flagCode) {
            parametersText += stringa + spazio + PROPERTY_CODE_NAME.toLowerCase() + virgola;
        }// end of if cycle
        if (flagDescrizione) {
            parametersText += stringa + spazio + PROPERTY_DESCRIZIONE_NAME.toLowerCase() + virgola;
        }// end of if cycle

        parametersText = text.levaCoda(parametersText, virgola);
        return parametersText;
    }// end of method


    private String creaKeyUnica() {
        methodKeyUnicaText = "";
        String stringa = "String";
        String intero = "int";
        String tab = "\t";
        String aCapo = "\n" + tab + tab;

        if (flagCode) {
            methodKeyUnicaText += "entity = findByKeyUnica(code);";
            methodKeyUnicaText += aCapo + "if (entity != null) {";
            methodKeyUnicaText += aCapo + tab + "return findByKeyUnica(code);";
            methodKeyUnicaText += aCapo + "}// end of if cycle";
            methodKeyUnicaText += aCapo;
        }// end of if cycle

        return methodKeyUnicaText;
    }// end of method


    private String creaEstendeEntity() {
        methodEstendeText = "";

        if (flagCompany) {
            methodEstendeText = "Estende la entity astratta ACEntity che contiene il riferimento alla property Company";
        } else {
            methodEstendeText = "Estende la entity astratta AEntity che contiene la key property ObjectId";
        }// end of if/else cycle

        return methodEstendeText;
    }// end of method


    private String creaUsaCompany() {
        methodUsaCompanyText = "";

        if (flagCompany) {
            methodUsaCompanyText = "EACompanyRequired.obbligatoria";
        } else {
            methodUsaCompanyText = "EACompanyRequired.nonUsata";
        }// end of if/else cycle

        return methodUsaCompanyText;
    }// end of method


    private String creaUsaSecurity() {
        methodUsaCompanyText = "";

        if (flagCompany) {
            methodUsaCompanyText = "EACompanyRequired.obbligatoria";
        } else {
            methodUsaCompanyText = "EACompanyRequired.nonUsata";
        }// end of if/else cycle

        return methodUsaCompanyText;
    }// end of method


    private String creaBuilder() {
        methodBuilderText = "";
        String tab4 = "\t\t\t\t";
        String aCapo = "\n" + tab4;
        String companyText = flagCompany ? "company" : "";

        if (flagOrdine) {
            methodBuilderText += aCapo + ".ordine(ordine != 0 ? ordine : this.getNewOrdine(" + companyText + "))";
        }// end of if cycle
        if (flagCode) {
            methodBuilderText += aCapo + ".code(text.isValid(code) ? code : null)";
        }// end of if cycle
        if (flagDescrizione) {
            methodBuilderText += aCapo + ".descrizione(text.isValid(descrizione) ? descrizione : null)";
        }// end of if cycle
        methodBuilderText += aCapo + ".build()";

        return methodBuilderText;
    }// end of method


    private String creaPropertyOrdine() {
        String entityPropertyText = "";

        if (flagOrdine) {
            entityPropertyText = leggeFile(PROPERTY_ORDINE_SOURCE_NAME);
            if (text.isValid(entityPropertyText)) {
                entityPropertyText = A_CAPO + TAB + entityPropertyText;
            }// end of if cycle
        }// end of if cycle

        return entityPropertyText;
    }// end of method


    private String creaPropertyCode() {
        String entityPropertyText = "";

        if (flagCode) {
            entityPropertyText = leggeFile(PROPERTY_CODE_SOURCE_NAME);
            if (text.isValid(entityPropertyText)) {
                entityPropertyText = A_CAPO + TAB + entityPropertyText;
            }// end of if cycle
        }// end of if cycle

        return entityPropertyText;
    }// end of method


    private String creaPropertyDescrizione() {
        String entityPropertyText = "";

        if (flagDescrizione) {
            entityPropertyText = leggeFile(PROPERTY_DESCRIZIONE_SOURCE_NAME);
            if (text.isValid(entityPropertyText)) {
                entityPropertyText = A_CAPO + TAB + entityPropertyText;
            }// end of if cycle
        }// end of if cycle

        return entityPropertyText;
    }// end of method


    private String creaToString() {
        String entityPropertyText = "return super.toString();";

        if (flagCode) {
            entityPropertyText = "return code;";
        } else {
            if (flagDescrizione) {
                entityPropertyText = "return descrizione;";
            }// end of if/else cycle
        }// end of if/else cycle

        return entityPropertyText;
    }// end of method


    private void addPackageMenu() {
        String max = text.primaMaiuscola(newPackageName);
        String aCapoImport = "\n";
        String aCapoPre = "\t";
        String aCapoPost = "\n\t";
        String tagRoute = "FlowVar.menuClazzList.add(" + max + VIEW_SUFFIX + ".class);" + aCapoPost;
        String tagImport = "import it.algos." + targetModuleName + ".modules." + newPackageName + "." + max + VIEW_SUFFIX + ";";

        if (targetModuleName.equals(PROJECT_BASE_NAME)) {
            addRouteFlow(tagImport, aCapoPre, aCapoImport, tagRoute);
        } else {
            addRouteProject(tagImport, aCapoPre, aCapoImport, tagRoute);
        }// end of if/else cycle
    }// end of method


    private void addRouteFlow(String tagImport, String aCapo, String aCapoImport, String tagRoute) {
        String refMethod = "public void creaRouteStandardDeveloper() {";
        String refMethodEnd = END;
        String refImport = "import it.algos.vaadflow.application.FlowCost;";
        String textBootClass = file.leggeFile(flowBootPath);
        String textCorpoMetodo = "";
        int posIni = 0;
        int posEnd = 0;

        if (textBootClass.contains(refMethod)) {
            if (textBootClass.contains(tagRoute)) {
                System.out.println("Nella classe ABootService esiste già la route per il package " + newPackageName);
            } else {
                //--sostituisce import
                textBootClass = text.sostituisce(textBootClass, refImport, refImport + aCapoImport + tagImport);

                //--sostituisce riga di aggiunta a Array di menu
                posIni = textBootClass.indexOf(refMethod);
                posEnd = textBootClass.indexOf(refMethodEnd, posIni + refMethod.length());
                textCorpoMetodo = textBootClass.substring(posIni, posEnd);

                textBootClass = text.sostituisce(textBootClass, textCorpoMetodo, textCorpoMetodo + aCapo + tagRoute);
                file.sovraScriveFile(flowBootPath, textBootClass);

                System.out.println("Il package " + text.primaMaiuscola(newPackageName) + " è stato aggiunto a " + nameClassBoot);
            }// end of if/else cycle
        } else {
            System.out.println("Nella classe " + nameClassBoot + " manca il metodo addRouteSpecifiche()");
        }// end of if/else cycle
    }// end of method


    private void addRouteProject(String tagImport, String aCapo, String aCapoImport, String tagRoute) {
        String refMethod = "protected void addRouteSpecifiche() {";
        String refMethodEnd = END;
        String refImport = "import it.algos.vaadflow.boot.ABoot;";
        String textBootClass = file.leggeFile(bootPath);
        String textCorpoMetodo = "";
        int posIni = 0;
        int posEnd = 0;
        String textNew = "";

        if (textBootClass.contains(refMethod)) {
            if (textBootClass.contains(tagRoute)) {
                System.out.println("Nella classe " + nameClassBoot + " esiste già la route per il package " + newPackageName);
            } else {
                //--sostituisce import
                textBootClass = text.sostituisce(textBootClass, refImport, refImport + aCapoImport + tagImport);

                //--sostituisce riga di aggiunta a Array di menu
                posIni = textBootClass.indexOf(refMethod);
                posEnd = textBootClass.indexOf(refMethodEnd, posIni + refMethod.length());
                textCorpoMetodo = textBootClass.substring(posIni, posEnd);
                textNew = textCorpoMetodo + aCapo + tagRoute;

                textBootClass = text.sostituisce(textBootClass, textCorpoMetodo, textNew);
                file.sovraScriveFile(bootPath, textBootClass);

                System.out.println("Il package " + text.primaMaiuscola(newPackageName) + " è stato aggiunto a " + nameClassBoot);
            }// end of if/else cycle
        } else {
            System.out.println("Nella classe " + nameClassBoot + " manca il metodo addRouteSpecifiche()");
        }// end of if/else cycle
    }// end of method


//    private void addImportPackage() {
//        String aCapoImport = "\n";
//        String viewImport = "import it.algos.vaadflow.boot.ABoot;";
//        String textBootClass = file.leggeFile(bootPath);
//        String max = text.primaMaiuscola(newPackageName);
//        String tagImport = "import it.algos." + targetModuleName + ".modules." + newPackageName + "." + max + VIEW_SUFFIX + ";";
//
//        if (textBootClass.contains(viewImport)) {
//            System.out.println("Nella classe " + nameClassBoot + " esiste già l'import per il package " + newPackageName);
//        } else {
//            textBootClass = text.sostituisce(textBootClass, viewImport, viewImport + aCapoImport + tagImport);
//            file.scriveFile(bootPath, textBootClass, true);
//
//            System.out.println("L'import del package " + newPackageName + " è stato inserito negli import iniziali di " + nameClassBoot);
//        }// end of if/else cycle
//    }// end of method


    //    private boolean isEsisteMetodo(String fileNameUIClass, String textUIClass, String tagMethod) {
//        boolean esiste = false;
//
//        if (textUIClass.contains(tagMethod)) {
//            esiste = true;
//        } else {
//            System.out.println("Nella classe iniziale " + fileNameUIClass + " manca il metodo " + tagMethod);
//        }// end of if/else cycle
//
//        return esiste;
//    }// end of method


    private void addTagCostanti() {
        String textCostClass;
        String path = applicationPath + SEP + nameClassCost + JAVA_SUFFIX;
        String tagOld = "public class " + nameClassCost + " {";
        String tagRif = "public final static String " + qualifier + " = \"" + newPackageName + "\";";
        String tagNew = tagOld + A_CAPO + TAB + tagRif;

        textCostClass = file.leggeFile(path);
        if (!textCostClass.contains(tagRif)) {
            textCostClass = text.sostituisce(textCostClass, tagOld, tagNew);
            file.scriveNewFile(path, textCostClass);
        }// end of if cycle
    }// end of method


    private void copiaDirectoriesBaseFlow() {
        boolean progettoCancellato = false;
        String tag = DIR_JAVA + "/" + PROJECT_BASE_NAME;
        String srcPath = projectBasePath;
        String destPath = ideaProjectRootPath + "/" + newProjectName;

        if (text.isValid(newProjectName)) {
            progettoCancellato = file.deleteDirectory(destPath + tag);
        }// end of if cycle

        if (progettoCancellato || !file.isEsisteDirectory(destPath + tag)) {
            file.copyDirectoryAddingOnly(srcPath + tag, destPath + tag);
        }// end of if cycle
    }// end of method


    /**
     * Cartella di documentazione (in formati vari)
     */
    private void regolaDocumentation() {
        fixCartellaExtra(flagDocumentation, DIR_DOC);
    }// end of method


    /**
     * Cartella di LINKS utili in text
     */
    private void regolaLinks() {
        fixCartellaExtra(flagLinks, DIR_LINKS);
    }// end of method


    /**
     * Cartella di snippets utili in text
     */
    private void regolaSnippets() {
        fixCartellaExtra(flagSnippets, DIR_SNIPPETS);
    }// end of method


    /**
     * File README di text
     */
    private void regolaRead() {
        String fileName = "/" + READ + SOURCE_SUFFIX;
        String srcPath = sourcePath + fileName;
        String destPath = projectPath + fileName;

        if (flagRead) {
            if (flagSovrascriveFile || !file.isEsisteFile(destPath)) {
                file.copyFile(srcPath, destPath);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * File di esclusioni GIT di text
     */
    private void regolaGit() {
        String srcPath = projectBasePath + "/" + GIT;
        String destPath = projectPath + "/" + GIT;

        if (flagGit) {
            if (flagSovrascriveFile || !file.isEsisteFile(destPath)) {
                file.copyFile(srcPath, destPath);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * File  application.properties
     */
    private void regolaProperties() {
        String sourceText = leggeFile("properties");
        String destPath = projectPath + DIR_MAIN + RESOURCES_NAME + "/application.properties";
        sourceText = EAToken.replace(EAToken.moduleNameMinuscolo, sourceText, newProjectName);

        if (flagProperties) {
            checkAndWriteFile(destPath, sourceText);
        }// end of if cycle
    }// end of method


    /**
     * File MAVEN di script
     */
    private void regolaPom() {
        String sourceText = leggeFile(POM);
        String destPath = ideaProjectRootPath + "/" + newProjectName + "/" + POM + ".xml";
        sourceText = EAToken.replace(EAToken.moduleNameMinuscolo, sourceText, newProjectName);

        if (flagPom) {
            checkAndWriteFile(destPath, sourceText);
        }// end of if cycle
    }// end of method


    /**
     * Cartella di resources META-INF
     */
    private void copiaMetaInf() {
        String srcPath = projectBasePath + DIR_MAIN + RESOURCES_NAME + META_NAME;
        String destPath = projectPath + DIR_MAIN + RESOURCES_NAME + META_NAME;
        boolean dirCancellata = false;

        if (flagResources) {
            if (text.isValid(newProjectName)) {
                dirCancellata = file.deleteDirectory(destPath);
            }// end of if cycle

            if (dirCancellata || !file.isEsisteDirectory(destPath)) {
                file.copyDirectoryAddingOnly(srcPath, destPath);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Sovrascrive o aggiunge a seconda del flag
     */
    private void fixCartellaExtra(boolean esegue, String dirName) {
        boolean dirCancellata = false;
        String sep = "/";
        String srcPath = projectBasePath + sep + dirName;
        String destPath = projectPath + sep + dirName;

        if (!esegue) {
            return;
        }// end of if cycle

        if (flagSovrascriveDirectory) {
            dirCancellata = file.deleteDirectory(destPath);
            if (dirCancellata || !file.isEsisteDirectory(destPath)) {
                file.copyDirectoryAddingOnly(srcPath, destPath);
            }// end of if cycle
        } else {
            List<String> lista = file.getFiles(srcPath);
            for (String nomeFile : lista) {
                if (!file.isEsisteFile(destPath + sep + nomeFile) || flagSovrascriveFile) {
                    file.copyFile(srcPath + sep + nomeFile, destPath + sep + nomeFile);
                }// end of if cycle
            }// end of for cycle
        }// end of if/else cycle


//        if (overwrite) {
//            if (text.isValid(newProjectName)) {
//                dirCancellata = file.deleteDirectory(destPath);
//            }// end of if cycle
//
//            if (dirCancellata || !file.isEsisteDirectory(destPath)) {
//                file.copyDirectory(srcPath, destPath);
//            }// end of if cycle
//        } else {
//        }// end of if/else cycle

    }// end of method


    private void creaProjectModule() {
        file.creaDirectory(projectJavaPath);
    }// end of method


    private void creaApplicationMain() {
        String mainApp = nameShort + text.primaMaiuscola(APP_NAME);
        mainApp = text.levaTesta(mainApp, PREFIX_NAME);
        mainApp = text.primaMaiuscola(mainApp);
        String destPath = projectJavaPath + "/" + mainApp + JAVA_SUFFIX;
        String testoApp = leggeFile(APP_NAME + SOURCE_SUFFIX);

        testoApp = EAToken.replace(EAToken.moduleNameMinuscolo, testoApp, newProjectName);
        testoApp = EAToken.replace(EAToken.moduleNameMaiuscolo, testoApp, text.primaMaiuscola(nameShort));

        if (flagSecurity) {
            testoApp = EAToken.replace(EAToken.usaSecurity, testoApp, VUOTA);
        } else {
            testoApp = EAToken.replace(EAToken.usaSecurity, testoApp, ", exclude = {SecurityAutoConfiguration.class}");
        }// end of if/else cycle

        checkAndWrite(destPath, testoApp);
    }// end of method


    private void creaApplicationDirectory() {
        file.creaDirectory(projectJavaPath + "/" + APP_NAME);
    }// end of method


    private void creaSecurityDirectory() {
        if (flagSecurity) {
            file.creaDirectory(projectJavaPath + "/" + SECURITY_NAME);
        }// end of if cycle
    }// end of method


    private void creaModulesDirectory() {
        file.creaDirectory(projectJavaPath + "/" + ENTITIES_NAME);
    }// end of method


    private void creaApplicationFolderContent() {
        creaHome();
        creaCost();
        creaBoot();
        creaVers();
    }// end of method


    private void creaHome() {
        String destPath = projectJavaPath + "/" + APP_NAME + "/" + HOME_NAME + JAVA_SUFFIX;
        String testoHome = leggeFile(HOME_NAME + SOURCE_SUFFIX);

        testoHome = EAToken.replace(EAToken.moduleNameMinuscolo, testoHome, newProjectName);
        checkAndWrite(destPath, testoHome);
    }// end of method


    private void creaCost() {
        String testoCost = leggeFile(COST_SUFFIX + SOURCE_SUFFIX);

        testoCost = EAToken.replace(EAToken.moduleNameMinuscolo, testoCost, newProjectName);
        testoCost = EAToken.replace(EAToken.moduleNameMaiuscolo, testoCost, text.primaMaiuscola(nameShort));
        checkAndWrite(costPath, testoCost);
    }// end of method


    private void creaBoot() {
        String testoBoot = leggeFile(BOOT_SUFFIX + SOURCE_SUFFIX);

        testoBoot = EAToken.replace(EAToken.moduleNameMinuscolo, testoBoot, newProjectName);
        testoBoot = EAToken.replace(EAToken.moduleNameMaiuscolo, testoBoot, text.primaMaiuscola(nameShort));
        checkAndWrite(bootPath, testoBoot);
    }// end of method


    private void creaVers() {
        String testoVers = leggeFile(VERS_SUFFIX + SOURCE_SUFFIX);

        testoVers = EAToken.replace(EAToken.moduleNameMinuscolo, testoVers, newProjectName);
        testoVers = EAToken.replace(EAToken.moduleNameMaiuscolo, testoVers, text.primaMaiuscola(nameShort));
        testoVers = EAToken.replace(EAToken.first, testoVers, firstCharProject);
        checkAndWrite(versPath, testoVers);
    }// end of method


    private void creaSecurityFolderContent() {
        if (flagSecurity) {
            creaConfiguration();
            creaDetails();
        }// end of if cycle
    }// end of method


    private void creaConfiguration() {
        String destPath = projectJavaPath + "/" + SECURITY_NAME + "/" + CONFIGURATION_NAME + JAVA_SUFFIX;
        String testoHome = leggeFile(CONFIGURATION_NAME + SOURCE_SUFFIX);

        testoHome = EAToken.replace(EAToken.moduleNameMinuscolo, testoHome, newProjectName);
        checkAndWrite(destPath, testoHome);
    }// end of method


    private void creaDetails() {
        String destPath = projectJavaPath + "/" + SECURITY_NAME + "/" + DETAILS_NAME + JAVA_SUFFIX;
        String testoHome = leggeFile(DETAILS_NAME + SOURCE_SUFFIX);

        testoHome = EAToken.replace(EAToken.moduleNameMinuscolo, testoHome, newProjectName);
        checkAndWrite(destPath, testoHome);
    }// end of method


    private void checkAndWrite(String destPath, String newText) {
        String oldFileText = file.leggeFile(destPath);
        if (checkFile(oldFileText)) {
            file.scriveNewFile(destPath, newText);
        }// end of if cycle
    }// end of method


    private List<String> recuperaPackagesEsistenti() {
        return file.getSubDirectoriesName(entityPath);
    }// end of method

//    private void addTagCost(String tag, String value) {
//        String aCapo = "\n\t";
//        String tagFind = "public abstract class " + nameCost + " {";
//        String tagStaticFind = "";
//        String tagStaticReplace = "";
//        String textCostClass = file.leggeFile(pathFileCost);
//        int posIni = 0;
//
//        tagStaticFind = "public final static String " + tag;
//        tagStaticReplace = tagStaticFind + " = \"" + value + "\";";
//
//        if (textCostClass.contains(tagStaticFind)) {
//        } else {
//            posIni = textCostClass.indexOf(tagFind);
//            posIni = posIni + tagFind.length();
//            tagStaticReplace = aCapo + tagStaticReplace;
//
//            textCostClass = text.inserisce(textCostClass, tagStaticReplace, posIni);
//            file.scriveFile(pathFileCost, textCostClass, true);
//
//            System.out.println("La costante statica TAG_" + tag + " è stata inserita nel file " + nameCost);
//        }// end of if/else cycle
//    }// end of method


}// end of class
