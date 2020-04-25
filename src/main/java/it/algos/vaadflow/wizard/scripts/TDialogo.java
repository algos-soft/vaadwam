package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wiz.enumeration.Chiave;
import it.algos.vaadflow.wiz.enumeration.Progetto;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 03-mag-2018
 * Time: 14:07
 */
@SpringComponent
@UIScope
@Slf4j
public abstract class TDialogo extends Dialog {

    protected static final String PROJECT_BASE_NAME = "vaadflow";

    protected final static String NORMAL_WIDTH = "9em";

    protected final static String NORMAL_HEIGHT = "3em";

    protected static final String DIR_MAIN = "/src/main";

    protected static final String DIR_JAVA = DIR_MAIN + "/java/it/algos";

    protected static final String ENTITIES_NAME = "modules";

    protected TRecipient recipient;

    protected Map<Chiave, Object> mappaInput = new HashMap<>();

    protected ComboBox<Progetto> fieldComboProgetti;

    protected VerticalLayout layoutBottoni;

    protected Button confirmButton;

    protected Button cancelButton;

    protected Button forzaDirectory;

    protected RadioButtonGroup<String> groupTitolo;

    /**
     * Service recuperato come istanza dalla classe singleton
     */
    protected ATextService text = ATextService.getInstance();

    /**
     * Service recuperato come istanza dalla classe singleton
     */
    protected AFileService file = AFileService.getInstance();

    protected Checkbox fieldCheckBoxSecurity;

    protected Checkbox fieldCheckBoxSovrascriveFile;

    protected Checkbox fieldCheckBoxSovrascriveDirectory;

    protected Checkbox fieldCheckBoxDocumentation;

    protected Checkbox fieldCheckBoxLinks;

    protected Checkbox fieldCheckBoxSnippets;

    protected Checkbox fieldCheckBoxFlow;

    protected Checkbox fieldCheckBoxNewProject;

    protected Checkbox fieldCheckBoxResources;

    protected Checkbox fieldCheckBoxProperties;

    protected Checkbox fieldCheckBoxRead;

    protected Checkbox fieldCheckBoxGit;

    protected Checkbox fieldCheckBoxPom;

    protected boolean isNuovoProgetto;


    /**
     * Costruttore
     */
    public TDialogo() {
    }// end of constructor


    @PostConstruct
    public void creaDialogo() {
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
    }// end of method


    protected void creaCheckBoxList() {
        if (isNuovoProgetto) {
            this.add(creaSecurity());
        }// end of if cycle
        this.add(creaSovrascriveFile());
        this.add(creaSovrascriveDirectory());
        this.add(creaDocumentation());
        this.add(creaLinks());
        this.add(creaSnippets());
        this.add(creaFlow());
        if (isNuovoProgetto) {
            this.add(creaDirectoryNewProject());
        }// end of if cycle
        this.add(creaResources());
        this.add(creaProperties());
        this.add(creaRead());
        this.add(creaGit());
        this.add(creaPom());
    }// end of method


    private Component creaSecurity() {
        fieldCheckBoxSecurity = new Checkbox();
        fieldCheckBoxSecurity.setLabel("Utilizza Spring Security");
        fieldCheckBoxSecurity.setValue(false);
        return fieldCheckBoxSecurity;
    }// end of method


    private Component creaSovrascriveFile() {
        fieldCheckBoxSovrascriveFile = new Checkbox();
        fieldCheckBoxSovrascriveFile.setLabel("Sovrascrive il singolo FILE");
        fieldCheckBoxSovrascriveFile.setValue(false);
        return fieldCheckBoxSovrascriveFile;
    }// end of method


    private Component creaSovrascriveDirectory() {
        fieldCheckBoxSovrascriveDirectory = new Checkbox();
        fieldCheckBoxSovrascriveDirectory.setLabel("Sovrascrive la DIRECTORY");
        fieldCheckBoxSovrascriveDirectory.setValue(false);
        return fieldCheckBoxSovrascriveDirectory;
    }// end of method


    private Component creaDocumentation() {
        fieldCheckBoxDocumentation = new Checkbox();
        fieldCheckBoxDocumentation.setLabel("Directory documentazione");
        fieldCheckBoxDocumentation.setValue(true);
        return fieldCheckBoxDocumentation;
    }// end of method


    private Component creaLinks() {
        fieldCheckBoxLinks = new Checkbox();
        fieldCheckBoxLinks.setLabel("Directory links a web");
        fieldCheckBoxLinks.setValue(true);
        return fieldCheckBoxLinks;
    }// end of method


    private Component creaSnippets() {
        fieldCheckBoxSnippets = new Checkbox();
        fieldCheckBoxSnippets.setLabel("Directory snippets di aiuto");
        fieldCheckBoxSnippets.setValue(true);
        return fieldCheckBoxSnippets;
    }// end of method


    private Component creaFlow() {
        fieldCheckBoxFlow = new Checkbox();
        fieldCheckBoxFlow.setLabel("Copia la cartella VaadFlow");
        fieldCheckBoxFlow.setValue(true);
        return fieldCheckBoxFlow;
    }// end of method


    private Component creaDirectoryNewProject() {
        fieldCheckBoxNewProject = new Checkbox();
        fieldCheckBoxNewProject.setLabel("Crea la cartella del nuovo progetto");
        fieldCheckBoxNewProject.setValue(true);
        return fieldCheckBoxNewProject;
    }// end of method


    private Component creaPom() {
        fieldCheckBoxPom = new Checkbox();
        fieldCheckBoxPom.setLabel("File Maven di POM.xml");
        fieldCheckBoxPom.setValue(isNuovoProgetto);
        return fieldCheckBoxPom;
    }// end of method


    private Component creaResources() {
        fieldCheckBoxResources = new Checkbox();
        fieldCheckBoxResources.setLabel("Directory resources - ATTENZIONE");
        fieldCheckBoxResources.setValue(false);
        return fieldCheckBoxResources;
    }// end of method


    private Component creaProperties() {
        fieldCheckBoxProperties = new Checkbox();
        fieldCheckBoxProperties.setLabel("File application.properties");
        fieldCheckBoxProperties.setValue(true);
        return fieldCheckBoxProperties;
    }// end of method


    private Component creaRead() {
        fieldCheckBoxRead = new Checkbox();
        fieldCheckBoxRead.setLabel("File READ con note di testo");
        fieldCheckBoxRead.setValue(true);
        return fieldCheckBoxRead;
    }// end of method


    private Component creaGit() {
        fieldCheckBoxGit = new Checkbox();
        fieldCheckBoxGit.setLabel("File GIT di esclusione");
        fieldCheckBoxGit.setValue(true);
        return fieldCheckBoxGit;
    }// end of method


    protected void creaFooter() {
        layoutBottoni = new VerticalLayout();
        HorizontalLayout layoutFooter = new HorizontalLayout();
        layoutFooter.setSpacing(true);
        layoutFooter.setMargin(true);

        cancelButton = new Button("Annulla", event -> {
            recipient.gotInput(null);
            this.close();
        });//end of lambda expressions
        cancelButton.setWidth(NORMAL_WIDTH);
        cancelButton.setHeight(NORMAL_HEIGHT);
        cancelButton.setVisible(true);

        confirmButton = new Button("Conferma", event -> {
            chiudeDialogo();
        });//end of lambda expressions
        confirmButton.setWidth(NORMAL_WIDTH);
        confirmButton.setHeight(NORMAL_HEIGHT);
        confirmButton.setVisible(false);

        layoutFooter.add(cancelButton, confirmButton);
        layoutBottoni.add(layoutFooter);
    }// end of method


    private void chiudeDialogo() {
        setMappa();
        recipient.gotInput(mappaInput);
        this.close();
    }// end of method


    protected void setMappa() {
    }// end of method

}// end of class
