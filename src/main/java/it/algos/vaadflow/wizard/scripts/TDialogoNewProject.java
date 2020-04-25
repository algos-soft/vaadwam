package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.wiz.enumeration.Chiave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

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
public class TDialogoNewProject extends TDialogo {

    private static final String SEP = "/";

    private static final String PROJECT_BASE_NAME = "it.algos.vaadflow";

    private static final String SOURCES_NAME = "wizard/sources";

    private static final String DIR_PROJECT_BASE = DIR_JAVA + "/" + PROJECT_BASE_NAME;

    private static final String DIR_SOURCES = DIR_PROJECT_BASE + SEP + SOURCES_NAME;

    @Autowired
    private AArrayService array;

    private ComboBox<String> fieldComboProgetti;

    //--regolate indipendentemente dai risultati del dialogo
    private String userDir;                 //--di sistema

    private String ideaProjectRootPath;     //--userDir meno PROJECT_BASE_NAME

    private String ideaNewProjectPath;     //--userDir meno PROJECT_BASE_NAME

    private String projectBasePath;         //--ideaProjectRootPath più PROJECT_BASE_NAME

    private String sourcePath;              //--projectBasePath più DIR_SOURCES

    private TextField fieldTextProject;



    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     */
    public TDialogoNewProject() {
        super();
        super.isNuovoProgetto = true;
    }// end of Spring constructor


    public void open(TRecipient recipient) {
        VerticalLayout layout = new VerticalLayout();
        this.recipient = recipient;
        this.setWidth("25em");
        regola();

        this.removeAll();
        super.open();

        layout.add(new Label("Creazione di un nuovo project"));
        layout.add(new Label("Devi prima creare un project idea"));
        layout.add(new Label("Di tipo 'MAVEN' senza selezionare archetype"));
        layout.add(new Label("Rimane il POM vuoto, ma verrà sovrascritto"));
        layout.add(new Label("Poi seleziona il progetto dalla lista sottostante"));
        this.add(layout);

        creaFooter();//per avere disponibili i bottoni da regolare
        this.add(creaBody());
        creaCheckBoxList();
        this.add(layoutBottoni);//aggiungre graficamente i bottoni

        addListener();
    }// end of method


    /**
     * Regolazioni iniziali indipendenti dal dialogo di input
     */
    private void regola() {
        this.userDir = System.getProperty("user.dir");
        this.ideaProjectRootPath = text.levaCodaDa(userDir, SEP);
        this.ideaNewProjectPath = text.levaCodaDa(ideaProjectRootPath, SEP);

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


    private Component creaBody() {
        forzaDirectory = new Button("Forza directory");
        forzaDirectory.addClickListener(e -> forzaProgetti());
        forzaDirectory.setWidth(NORMAL_WIDTH);
        forzaDirectory.setHeight(NORMAL_HEIGHT);
        forzaDirectory.setVisible(true);

        String label = "Progetti vuoti esistenti (nella directory IdeaProjects)";
        List<String> progetti = getProgetti(false);

        fieldComboProgetti = new ComboBox<>();
        fieldComboProgetti.setWidth("22em");
        fieldComboProgetti.setAllowCustomValue(false);
        fieldComboProgetti.setLabel(label);
        fieldComboProgetti.setItems(progetti);
        if (progetti.size() == 1) {
            fieldComboProgetti.setValue(progetti.get(0));
            confirmButton.setVisible(true);
        }// end of if cycle

        return new VerticalLayout(forzaDirectory, fieldComboProgetti);
    }// end of method


    private void forzaProgetti() {
        List<String> progetti = getProgetti(true);
        fieldComboProgetti.setItems(progetti);
        if (progetti.size() == 1) {
            fieldComboProgetti.setValue(progetti.get(0));
            confirmButton.setVisible(true);
        }// end of if cycle
    }// end of method




    private void addListener() {
        fieldComboProgetti.addValueChangeListener(event -> sincroProject(event.getValue()));//end of lambda expressions
    }// end of method


    private void sincroProject(String valueFromProject) {
        if (text.isValid(valueFromProject) && valueFromProject.length() > 2) {
            confirmButton.setVisible(true);
        } else {
            confirmButton.setVisible(false);
        }// end of if/else cycle
    }// end of method


    /**
     * Mostra i progetti 'vuoti'
     * Per essere 'vuoti' non devono avere la directory:
     * src.main.java.it.algos.vaadflow
     * <p>
     * Per essere 'vuoti' deve esserci la directory: src/main/java vuota
     */
    protected List<String> getProgetti(boolean liPrendeTutti) {
        List<String> progettiVuoti = null;
        List<String> progettiEsistenti = null;
        List<String> subMain;
        List<String> subJava;
        String tagVuoto = DIR_MAIN;
        String tagPieno = tagVuoto + "/java";
        String tagCompleto = tagPieno + "/it";
        String pahtDirectoryChiave;

        if (text.isValid(ideaNewProjectPath)) {
            progettiEsistenti = file.getSubDirectoriesName(ideaNewProjectPath);
        }// end of if cycle

        if (progettiEsistenti != null && progettiEsistenti.size() > 0) {
            progettiVuoti = new ArrayList<>();
            for (String nome : progettiEsistenti) {

                subMain = file.getSubDirectoriesName(ideaNewProjectPath + "/" + nome + tagVuoto);

                //se manca la sottodirectory src/main non se ne parla
                if (array.isValid(subMain)) {

                    //se esiste NON deve esserci il percorso src/main/java/it
                    subJava = file.getSubDirectoriesName(ideaNewProjectPath + "/" + nome + tagPieno);
                    pahtDirectoryChiave = ideaNewProjectPath + "/" + nome + tagCompleto;
                    if (array.isEmpty(subJava) && !file.isEsisteDirectory(pahtDirectoryChiave)) {
                        progettiVuoti.add(nome);
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        if (array.isEmpty(progettiVuoti) && liPrendeTutti) {
            progettiVuoti = progettiEsistenti;
        }// end of if cycle

        return progettiVuoti;
    }// end of method


    protected void setMappa() {
        if (mappaInput != null) {
            mappaInput.put(newProjectName, fieldComboProgetti.getValue());
            mappaInput.put(Chiave.targetProjectName, fieldComboProgetti.getValue());
            mappaInput.put(Chiave.flagSecurity, fieldCheckBoxSecurity.getValue());
            mappaInput.put(Chiave.flagSovrascriveFile, fieldCheckBoxSovrascriveFile.getValue());
            mappaInput.put(Chiave.flagSovrascriveDirectory, fieldCheckBoxSovrascriveDirectory.getValue());
            mappaInput.put(Chiave.flagDocumentation, fieldCheckBoxDocumentation.getValue());
            mappaInput.put(Chiave.flagLinks, fieldCheckBoxLinks.getValue());
            mappaInput.put(Chiave.flagSnippets, fieldCheckBoxSnippets.getValue());
            mappaInput.put(Chiave.flagDirectoryFlow, fieldCheckBoxFlow.getValue());
            mappaInput.put(Chiave.flagDirectoryNewProject, fieldCheckBoxNewProject.getValue());
            mappaInput.put(Chiave.flagResources, fieldCheckBoxResources.getValue());
            mappaInput.put(Chiave.flagProperties, fieldCheckBoxProperties.getValue());
            mappaInput.put(Chiave.flagRead, fieldCheckBoxRead.getValue());
            mappaInput.put(Chiave.flagGit, fieldCheckBoxGit.getValue());
            mappaInput.put(Chiave.flagPom, fieldCheckBoxPom.getValue());
            log.info("Nome nuovo progetto: " + fieldComboProgetti.getValue());
            log.info("Directory nuovo progetto: " + ideaNewProjectPath);
        }// end of if cycle
    }// end of method

}// end of class
