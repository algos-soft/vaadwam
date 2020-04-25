package it.algos.vaadflow.wiz.scripts;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wiz.enumeration.EAToken;
import it.algos.vaadflow.wiz.enumeration.EAWiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.SLASH;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadflow.wiz.scripts.WizCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 13-apr-2020
 * Time: 05:17
 * <p>
 * Classe astratta per alcuni dialoghi di regolazione dei parametri per il Wizard <br>
 */
@Slf4j
public abstract class WizDialog extends Dialog {

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile al termine del costruttore di questa classe <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile al termine del costruttore di questa classe <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile al termine del costruttore di questa classe <br>
     */
    @Autowired
    protected AFileService file;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile al termine del costruttore di questa classe <br>
     */
    @Autowired
    protected LogService logger;

    protected WizRecipient wizRecipient;

    protected LinkedHashMap<String, Checkbox> mappaCheckbox;

    protected boolean isNuovoProgetto;

    protected Button confirmButton;

    protected Button cancelButton;

    protected Button buttonForzaDirectory;

    protected VerticalLayout layoutLegenda;

    protected VerticalLayout layoutTitolo;

    protected VerticalLayout layoutCheckBox;

    protected VerticalLayout layoutBottoni;

    protected H3 titoloCorrente;

    protected ComboBox<File> fieldComboProgetti;

    //--regolata iniziale indipendentemente dai risultati del dialogo
    //--tutte le property il cui nome inizia con 'path' finiscono con SLASH
    //--directory recuperata dal System dove gira il programma in uso
    protected String pathUserDir;

    //--regolata iniziale indipendentemente dai risultati del dialogo
    //--tutte le property il cui nome inizia con 'path' finiscono con SLASH
    //--dipende solo da dove si trova attualmente il progetto base VaadFlow
    //--posso spostarlo (è successo) senza che cambi nulla
    //--directory che contiene il programma VaadFlow
    //--dovrebbe essere PATH_VAAD_FLOW_DIR_STANDARD
    //--posso spostarlo (è successo) senza che cambi nulla
    protected String pathVaadFlow;

    //--regolata iniziale indipendentemente dai risultati del dialogo
    //--tutte le property il cui nome inizia con 'path' finiscono con SLASH
    //--directory che contiene i nuovi programmi appena creati da Idea
    //--dovrebbe essere PATH_PROJECTS_DIR_STANDARD
    //--posso spostarla (è successo) senza che cambi nulla
    protected String pathIdeaProjects;

    //--regolata iniziale indipendentemente dai risultati del dialogo
    //--tutte le property il cui nome inizia con 'path' finiscono con SLASH
    //--dipende solo da dove si trova attualmente il progetto base VaadFlow
    //--posso spostarlo (è successo) senza che cambi nulla
    //--directory dei sorgenti testuali di VaadFlow (da elaborare)
    //--pathVaadFlow più DIR_SOURCES
    protected String pathSources;

    //--regolata da questo dialogo
    //--può essere un new project oppure un update di un progetto esistente
    protected String nameTargetProject = VUOTA;

    //--regolata da questo dialogo
    //--può essere il path completo di un new project oppure di un update esistente
    //--tutte le property il cui nome inizia con 'path' finiscono con SLASH
    protected String pathTargetProjet = VUOTA;


    /**
     * Regolazioni grafiche
     */
    protected void inizia() {
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);
        this.removeAll();

        //--regolazione di property varie
        this.regolaVariabili();

        //--info di avvisi
        this.creaLegenda();

        //--creazione bottoni di comando
        this.creaBottoni();

        //--solo per newProject
        this.spazzolaDirectory();

        //--checkbox di spunta
        mappaCheckbox = new LinkedHashMap<>();
        creaCheckBoxMap();
        this.addCheckBoxMap();

        //--aggiunge bottoni al layout
        this.addBottoni();

        //--superclasse
        super.open();
    }// end of method


    /**
     * Regolazioni iniziali indipendenti dal dialogo di input
     */
    protected void regolaVariabili() {
        EAWiz.reset();

        if (FLAG_DEBUG_WIZ) {
            WizCost.printInfo(log);
        }// end of if cycle

        this.pathUserDir = System.getProperty("user.dir") + SLASH;
        this.pathVaadFlow = PATH_VAADFLOW_DIR_STANDARD;
        if (isNuovoProgetto && !pathVaadFlow.equals(pathUserDir)) {
            logger.error("Attenzione. La directory di VaadFlow è cambiata", WizDialog.class, "regolazioniIniziali");
        }// end of if/else cycle

        //valido SOLO per new project
        if (isNuovoProgetto) {
            this.pathIdeaProjects = file.levaDirectoryFinale(pathVaadFlow);
            this.pathIdeaProjects = file.levaDirectoryFinale(pathIdeaProjects);
            if (!pathIdeaProjects.equals(PATH_PROJECTS_DIR_STANDARD)) {
                logger.error("Attenzione. La directory dei Projects è cambiata", WizDialog.class, "regolazioniIniziali");
            }// end of if cycle
        } else {
            File unaDirectory = new File(pathUserDir);
            this.nameTargetProject = unaDirectory.getName();
            this.pathTargetProjet = pathUserDir;
        }// end of if/else cycle

        this.pathSources = pathVaadFlow + DIR_VAADFLOW_SOURCES;

        if (FLAG_DEBUG_WIZ) {
            System.out.println("********************");
            System.out.println("Ingresso del dialogo");
            System.out.println("********************");
            System.out.println("Directory di esecuzione: pathUserDir=" + pathUserDir);
            System.out.println("Directory VaadFlow: pathVaadFlow=" + pathVaadFlow);
            if (isNuovoProgetto) {
                System.out.println("Directory dei nuovi progetti: pathIdeaProjects=" + pathIdeaProjects);
            }// end of if cycle
            System.out.println("Sorgenti VaadFlow: pathSources=" + pathSources);
            System.out.println("");
        }// end of if cycle
    }// end of method


    /**
     * Legenda iniziale <br>
     * Viene sovrascritta nella sottoclasse che deve einvocare PRIMA questo metodo <br>
     */
    protected void creaLegenda() {
        if (isNuovoProgetto) {
            this.titoloCorrente = new H3(TITOLO_NUOVO_PROGETTO);
        } else {
            this.titoloCorrente = new H3(text.primaMaiuscola(nameTargetProject));
        }// end of if/else cycle

        layoutTitolo = new VerticalLayout();
        layoutTitolo.setMargin(false);
        layoutTitolo.setSpacing(false);
        layoutTitolo.setPadding(true);
        titoloCorrente.getElement().getStyle().set("color", "blue");
        layoutTitolo.add(titoloCorrente);

        layoutLegenda = new VerticalLayout();
        layoutLegenda.setMargin(false);
        layoutLegenda.setSpacing(false);
        layoutLegenda.setPadding(true);
        layoutLegenda.getElement().getStyle().set("color", "green");
        this.setWidth("25em");

        this.add(layoutTitolo);
        this.add(layoutLegenda);
    }// end of method


    protected void creaBottoni() {
        cancelButton = new Button("Annulla", event -> {
            esceDalDialogo(false);
        });//end of lambda expressions
        cancelButton.setIcon(VaadinIcon.ARROW_LEFT.create());
        cancelButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickShortcut(Key.ARROW_LEFT);
        cancelButton.setWidth(NORMAL_WIDTH);
        cancelButton.setHeight(NORMAL_HEIGHT);
        cancelButton.setVisible(true);

        confirmButton = new Button("Conferma", event -> {
            esceDalDialogo(true);
        });//end of lambda expressions
        confirmButton.setIcon(VaadinIcon.EDIT.create());
        confirmButton.getElement().setAttribute("theme", "error");
        confirmButton.setWidth(NORMAL_WIDTH);
        confirmButton.setHeight(NORMAL_HEIGHT);
        confirmButton.setVisible(true);
        confirmButton.setEnabled(!isNuovoProgetto);
    }// end of method


    /**
     * Spazzola la directory 'ideaProjects' <br>
     * Recupera i possibili progetti 'vuoti' <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void spazzolaDirectory() {
    }// end of method


    /**
     * Crea i checkbox di controllo <br>
     * Spazzola (nella sottoclasse) la Enumeration per aggiungere solo i checkbox adeguati: <br>
     * newProject
     * updateProject
     * newPackage
     * updatePackage
     * Spazzola la Enumeration e regola a 'true' i chekbox secondo il flag 'isAcceso' <br>
     * DEVE essere sovrascritto nella sottoclasse <br>
     */
    protected void creaCheckBoxMap() {
    }// end of method


    /**
     * Aggiounge al layout i checkbox di controllo <br>
     */
    private void addCheckBoxMap() {
        layoutCheckBox = new VerticalLayout();
        layoutCheckBox.setMargin(false);
        layoutCheckBox.setSpacing(false);
        layoutCheckBox.setPadding(true);

        for (String key : mappaCheckbox.keySet()) {
            layoutCheckBox.add(mappaCheckbox.get(key));
        }// end of for cycle

        this.add(layoutCheckBox);
    }// end of method


    protected void addBottoni() {
        layoutBottoni = new VerticalLayout();
        HorizontalLayout layoutFooter = new HorizontalLayout();
        layoutFooter.setSpacing(true);
        layoutFooter.setMargin(true);

        layoutFooter.add(cancelButton, confirmButton);
        layoutBottoni.add(layoutFooter);
        this.add(layoutBottoni);
    }// end of method


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola tutti i valori della Enumeration EAWiz che saranno usati da WizElaboraNewProject e WizElaboraUpdateProject <br>
     * Regola alcuni valori della Enumeration EAToken che saranno usati da WizElaboraNewProject e WizElaboraUpdateProject <br>
     */
    protected void regolazioniFinali() {
        if (isNuovoProgetto) {
            if (fieldComboProgetti != null && fieldComboProgetti.getValue() != null) {
                nameTargetProject = fieldComboProgetti.getValue().getName();
                pathTargetProjet = fieldComboProgetti.getValue().getAbsolutePath() + SLASH;
            }// end of if cycle
        }// end of if cycle

        regolaEAWiz();
        regolaEAToken();
    }// end of method


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola tutti i valori della Enumeration EAWiz che saranno usati da WizElaboraNewProject e WizElaboraUpdateProject <br>
     */
    protected void regolaEAWiz() {
        EAWiz.pathUserDir.setValue(pathUserDir);
        EAWiz.pathVaadFlow.setValue(pathVaadFlow);
        EAWiz.pathIdeaProjects.setValue(pathIdeaProjects);
        EAWiz.nameTargetProject.setValue(nameTargetProject);
        EAWiz.pathTargetProjet.setValue(pathTargetProjet);

        for (EAWiz flag : EAWiz.values()) {
            if (mappaCheckbox.get(flag.name()) != null) {
                flag.setAcceso(mappaCheckbox.get(flag.name()).getValue());
            }// end of if cycle
        }// end of for cycle

        //--visualizzazione di controllo
        if (FLAG_DEBUG_WIZ) {
            System.out.println("********************");
            System.out.println("Uscita dal dialogo - EAWiz");
            System.out.println("********************");
            for (EAWiz flag : EAWiz.values()) {
                if (flag.isCheckBox()) {
                    System.out.println("EAWiz." + flag.name() + " \"" + flag.getLabelBox() + "\" = " + flag.isAbilitato());
                } else {
                    System.out.println("EAWiz." + flag.name() + " \"" + flag.getDescrizione() + "\" = " + flag.getValue());
                }// end of if/else cycle
            }// end of for cycle
            System.out.println("");
        }// end of if cycle
    }// end of method


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola alcuni valori della Enumeration EAToken che saranno usati da WizElaboraNewProject e WizElaboraUpdateProject <br>
     */
    protected void regolaEAToken() {
        EAToken.reset();
        EAToken.nameTargetProject.setValue(nameTargetProject);
        EAToken.pathTargetProjet.setValue(pathTargetProjet);

        EAToken.projectNameUpper.setValue(nameTargetProject.toUpperCase());
        EAToken.moduleNameMinuscolo.setValue(nameTargetProject.toLowerCase());
        EAToken.moduleNameMaiuscolo.setValue(text.primaMaiuscola(nameTargetProject));
        EAToken.first.setValue(text.isValid(nameTargetProject) ? nameTargetProject.substring(0, 1).toUpperCase() : VUOTA);

        //--visualizzazione di controllo
        if (FLAG_DEBUG_WIZ) {
            System.out.println("********************");
            System.out.println("Uscita dal dialogo - EAToken");
            System.out.println("********************");
            for (EAToken token : EAToken.values()) {
                if (token.isUsaValue()) {
                    System.out.println("EAToken." + token.name() + "  - " + token.getTokenTag() + " = " + token.getValue());
                }// end of if cycle
            }// end of for cycle
            System.out.println("");
        }// end of if cycle
    }// end of method


    /**
     * Esce dal dialogo con due possibilità (a seconda del flag)
     * 1) annulla
     * 2) esegue
     */
    private void esceDalDialogo(boolean esegue) {
        regolazioniFinali();
        this.close();
        if (esegue) {
            wizRecipient.esegue();
        }// end of if cycle
    }// end of method

}// end of class
