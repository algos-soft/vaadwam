package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wizard.WizardView;
import it.algos.vaadflow.wizard.enumeration.Chiave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

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
    private static final String PROJECT_BASE_NAME = "it.algos.vaadflow";
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
    }// end of Spring constructor


    public void open(TRecipient recipient) {
        VerticalLayout layout = new VerticalLayout();
        this.recipient = recipient;
        this.setWidth("25em");
        regola();

        this.removeAll();
        super.open();

        this.add(new Label("Update di un project esistente"));
        this.add(creaBody());
        this.add(creaFooter());
        if (fieldComboProgetti != null && !fieldComboProgetti.getValue().equals("")) {
            confirmButton.setVisible(true);
        }// end of if cycle

        addListener();
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


    private Component creaBody() {
        String label = "Progetti esistenti (nella directory IdeaProjects)";
        List<String> progetti = getProgetti();

        fieldComboProgetti = new ComboBox<>();
        fieldComboProgetti.setWidth("20em");
        fieldComboProgetti.setAllowCustomValue(false);
        fieldComboProgetti.setLabel(label);
        fieldComboProgetti.setItems(progetti);

        if (progetti != null && progetti.size() > 0) {
            fieldComboProgetti.setValue(progetti.get(0));
        }// end of if cycle

        return new VerticalLayout(fieldComboProgetti);
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


    protected List<String> getProgetti() {
        List<String> progettiValidi = null;
        List<String> progettiEsistenti = null;
        String tag = DIR_JAVA + "/" + PROJECT_BASE_NAME;

        if (text.isValid(ideaProjectRootPath)) {
            progettiEsistenti = file.getSubdirectories(ideaProjectRootPath);
        }// end of if cycle

        if (progettiEsistenti != null && progettiEsistenti.size() > 0) {
            progettiValidi = new ArrayList<>();
            for (String nome : progettiEsistenti) {
                if (file.isEsisteDirectory(ideaProjectRootPath + "/" + nome + tag)) {
                    if (!nome.equals(PROJECT_BASE_NAME)) {
                        progettiValidi.add(nome);
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return progettiValidi;
    }// end of method

    protected void setMappa() {
        if (mappaInput != null) {
            mappaInput.put(Chiave.newProjectName, fieldComboProgetti.getValue());
        }// end of if cycle
    }// end of method

}// end of class
