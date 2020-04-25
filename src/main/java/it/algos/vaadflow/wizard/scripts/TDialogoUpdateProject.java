package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.wiz.enumeration.Chiave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.wiz.enumeration.Chiave.newProjectName;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 03-mag-2018
 * Time: 13:55
 * Dialogo per la creazione/modifica di un nuovo project
 */
@Slf4j
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TDialogoUpdateProject extends TDialogo {


    private static final String SEP = "/";

    private static final String PROJECT_BASE_NAME = "it.algos.operativi.vaadflow";

    private static final String SOURCES_NAME = "wizard/sources";

    private static final String DIR_PROJECT_BASE = DIR_JAVA + "/" + PROJECT_BASE_NAME;

    private static final String DIR_SOURCES = DIR_PROJECT_BASE + SEP + SOURCES_NAME;

    //    @Autowired
//    private ATextService text;
//    @Autowired
//    private AFileService file;
//    @Autowired
//    private AArrayService array;
    private ComboBox<String> fieldComboProgetti;

    //--regolate indipendentemente dai risultati del dialogo
    private String userDir;                 //--di sistema

    private String ideaProjectRootPath;     //--userDir meno PROJECT_BASE_NAME

    private String projectBasePath;         //--ideaProjectRootPath più PROJECT_BASE_NAME

    private String sourcePath;              //--projectBasePath più DIR_SOURCES

    private TextField fieldTextProject;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     */
    public TDialogoUpdateProject() {
        super();
        super.isNuovoProgetto = false;
    }// end of Spring constructor


    public void open(TRecipient recipient) {
        VerticalLayout layout = new VerticalLayout();
        this.recipient = recipient;
        this.setWidth("25em");
        regola();

        this.removeAll();
        super.open();

        creaFooter();//per avere disponibili i bottoni da regolare
        this.add(new Label("Update di questo project"));

        creaCheckBoxList();
        if (fieldComboProgetti != null && !fieldComboProgetti.getValue().equals("")) {
            confirmButton.setVisible(true);
        }// end of if cycle
        this.add(layoutBottoni);//aggiungre graficamente i bottoni
        confirmButton.setVisible(true);
    }// end of method


    /**
     * Regolazioni iniziali indipendenti dal dialogo di input
     */
    private void regola() {
        this.userDir = System.getProperty("user.dir");
        this.ideaProjectRootPath = text.levaCodaDa(userDir, SEP);

        this.projectBasePath = ideaProjectRootPath + SEP + PROJECT_BASE_NAME;
        this.sourcePath = projectBasePath + DIR_SOURCES;

        log.info("");
        log.info("PROJECT_BASE: " + PROJECT_BASE_NAME);
        log.info("DIR_JAVA: " + DIR_JAVA);
        log.info("DIR_PROJECT_BASE: " + DIR_PROJECT_BASE);
        log.info("DIR_SOURCES: " + DIR_SOURCES);
        log.info("userDir: " + userDir);
        log.info("ideaProjectRootPath: " + ideaProjectRootPath);
        log.info("projectBasePath: " + projectBasePath);
        log.info("sourcePath: " + sourcePath);
    }// end of method


    protected void setMappa() {
        if (mappaInput != null) {
            mappaInput.put(newProjectName, "pippoz");
            mappaInput.put(Chiave.flagSovrascriveFile, fieldCheckBoxSovrascriveFile.getValue());
            mappaInput.put(Chiave.flagSovrascriveDirectory, fieldCheckBoxSovrascriveDirectory.getValue());
            mappaInput.put(Chiave.flagDocumentation, fieldCheckBoxDocumentation.getValue());
            mappaInput.put(Chiave.flagLinks, fieldCheckBoxLinks.getValue());
            mappaInput.put(Chiave.flagSnippets, fieldCheckBoxSnippets.getValue());
            mappaInput.put(Chiave.flagDirectoryFlow, fieldCheckBoxFlow.getValue());
            mappaInput.put(Chiave.flagResources, fieldCheckBoxResources.getValue());
            mappaInput.put(Chiave.flagProperties, fieldCheckBoxProperties.getValue());
            mappaInput.put(Chiave.flagRead, fieldCheckBoxRead.getValue());
            mappaInput.put(Chiave.flagGit, fieldCheckBoxGit.getValue());
            mappaInput.put(Chiave.flagPom, fieldCheckBoxPom.getValue());
        }// end of if cycle
    }// end of method

}// end of class
