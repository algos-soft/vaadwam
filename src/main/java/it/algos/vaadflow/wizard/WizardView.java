package it.algos.vaadflow.wizard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.wizard.enumeration.Chiave;
import it.algos.vaadflow.wizard.enumeration.Progetto;
import it.algos.vaadflow.wizard.scripts.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.TAG_WIZ;

/**
 * Project vbase
 * Created by Algos
 * User: gac
 * Date: mar, 20-mar-2018
 * Time: 08:57
 * <p>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Invocata da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le istanza A VALLE di questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe <br>
 * 2) usano @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@UIScope
@Route(value = TAG_WIZ, layout = MainLayout.class)
@Qualifier(TAG_WIZ)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = true, menuName = TAG_WIZ, menuIcon = VaadinIcon.MAGIC, roleTypeVisibility = EARoleType.developer)
public class WizardView extends VerticalLayout {

    public static final String VAADFLOW = "Progetto base vaadflow. ";
    public static final String PACKAGE = "Creazione di un nuovo package o modifica di un package esistente";
    public final static String NORMAL_WIDTH = "9em";
    public final static String NORMAL_HEIGHT = "3em";
//    /**
//     * Icona visibile nel menu (facoltativa)
//     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
//     * Se manca il MENU_NAME, di default usa il 'name' della view
//     */
//    public static final VaadinIcon VIEW_ICON = VaadinIcon.MAGIC;
//    public static final String IRON_ICON = "build";

    private static final String PROJECT_BASE_NAME = "vaadflow";
    private static Progetto PROGETTO_STANDARD_SUGGERITO_NUOVO = Progetto.vaadin;
    private static Progetto PROGETTO_STANDARD_SUGGERITO_MODIFICA = Progetto.test;
    private static String NOME_PACKAGE_STANDARD_SUGGERITO = "prova";
    private static String PROJECT = "Creazione di un nuovo project";
    private static String LABEL_B = "Update di un project esistente";
    private static String LABEL_C = VAADFLOW + PACKAGE + ", tramite dialogo wizard";

    /**
     * Libreria di servizio. Inietta da Spring nel costruttore come 'singleton'
     */
    private ATextService text;

    private Label labelUno;
    private Label labelDue;
    private Label labelTre;
    private Label labelQuattro;
    private Button buttonProject;
    private Button buttonPackage;
    private Button buttonUno;
    private Button buttonDue;
    private Button buttonTre;
    private Button buttonQuattro;
    private NativeButton confirmButton;
    private NativeButton cancelButton;

    /**
     * Inietta da Spring nel costruttore come 'singleton'
     */
    private TDialogoPackage dialogPackage;
    @Autowired
    private TDialogoNewProject newProject;
    @Autowired
    private TDialogoUpdateProject updateProject;
    /**
     * Inietta da Spring nel costruttore come 'singleton'
     */
    private TElabora elabora;
    private TRecipient recipient;
    private ComboBox fieldComboProgetti;
    private TextField fieldTextPackage;
    private TextField fieldTextEntity; // suggerito
    private TextField fieldTextTag; // suggerito
    private Checkbox fieldCheckBoxCompany;
    private Checkbox fieldCheckBoxSovrascrive;
    private Map<Chiave, Object> mappaInput = new HashMap<>();

    @Autowired
    public WizardView(ATextService text, TDialogoPackage dialogPackage, TElabora elabora) {
        this.text = text;
        this.dialogPackage = dialogPackage;
        this.elabora = elabora;
        this.removeAll();
        checkIniziale();
    }// end of Spring constructor


//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        this.removeAll();
//        checkIniziale();
//    }// end of method

    public void checkIniziale() {
        this.setMargin(true);
        this.setSpacing(true);

        String currentProject = System.getProperty("user.dir");
        currentProject = currentProject.substring(currentProject.lastIndexOf("/") + 1);

        if (currentProject.equals(PROJECT_BASE_NAME)) {
            iniziaBase();
            this.add(creaMenuBase());
        } else {
            iniziaProject();
            this.add(updateProject());
            this.add(updatePackage());
        }// end of if/else cycle

    }// end of method


    public void iniziaBase() {
        labelUno = new Label(VAADFLOW);
        this.add(labelUno);
//        labelDue = new Label("Aggiornamento di un project esistente, tramite dialogo wizard");
//        this.add(labelDue);
//        labelTre = new Label("Creazione di un nuovo package (modulo), tramite dialogo wizard");
//        this.add(labelTre);
//        labelQuattro = new Label("Modifica di un package (modulo) esistente, tramite dialogo wizard");
//        this.add(labelQuattro);
    }// end of method

    public void iniziaProject() {
        labelUno = new Label("Update di questo project");
        this.add(labelUno);
        labelDue = new Label("Creazione di un nuovo package (modulo), tramite dialogo wizard");
        this.add(labelDue);
        labelTre = new Label("Modifica di un package (modulo) esistente, tramite dialogo wizard");
        this.add(labelTre);
    }// end of method

    private Component creaMenuBase() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        labelDue = new Label(PROJECT);
        this.add(labelDue);
        buttonUno = new Button("New project");
        buttonUno.addClickListener(event -> newProject.open(new TRecipient() {
            @Override
            public void gotInput(Map<Chiave, Object> mappaInput) {
                elaboraNewProject(mappaInput);
            }// end of inner method
        }));// end of lambda expressions and anonymous inner class
        this.add(buttonUno);

//        buttonDue = new Button("Update project");
//        buttonDue.addClickListener(event -> updateProject.open(new TRecipient() {
//            @Override
//            public void gotInput(Map<Chiave, Object> mappaInput) {
//                elaboraUpdateProject(mappaInput);
//            }// end of inner method
//        }));// end of lambda expressions and anonymous inner class
//        layout.add(buttonDue);

        labelDue = new Label(PACKAGE);
        this.add(labelDue);
        buttonDue = new Button("Nuovo package");
        buttonDue.addClickListener(event -> dialogPackage.open(new TRecipient() {
            @Override
            public void gotInput(Map<Chiave, Object> mappaInput) {
                elaboraPackage(mappaInput);
            }// end of inner method
        }, true, PROGETTO_STANDARD_SUGGERITO_NUOVO, ""));// end of lambda expressions and anonymous inner class
        layout.add(buttonDue);

        buttonTre = new Button("Modifica package");
        buttonTre.addClickListener(event -> dialogPackage.open(new TRecipient() {
            @Override
            public void gotInput(Map<Chiave, Object> mappaInput) {
                elaboraPackage(mappaInput);
            }// end of inner method
        }, false, PROGETTO_STANDARD_SUGGERITO_MODIFICA, NOME_PACKAGE_STANDARD_SUGGERITO));// end of lambda expressions and anonymous inner class
        layout.add(buttonTre);

        return layout;
    }// end of method


    private Component updateProject() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);
        Progetto currentProject = null;

        String nomeProgetto = System.getProperty("user.dir");
        nomeProgetto = nomeProgetto.substring(nomeProgetto.lastIndexOf("/") + 1);
        currentProject = Progetto.getProgetto(nomeProgetto);
        mappaInput.put(Chiave.targetProjectName, currentProject != null ? currentProject : nomeProgetto);

        buttonProject = new Button("Update this project");
        buttonProject.addClickListener(e -> {
            elaboraUpdateProject(mappaInput);
            layout.add(new Checkbox("Fatto", true));
        });//end of lambda expressions
        layout.add(buttonProject);

        this.add(layout);
        return layout;
    }// end of method

    private Component updatePackage() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        buttonPackage = new Button("Package");
        buttonPackage.addClickListener(event -> dialogPackage.open(new TRecipient() {
                    @Override
                    public void gotInput(Map<Chiave, Object> mappaInput) {
                        elaboraPackage(mappaInput);
                    }// end of inner method
                }, true, PROGETTO_STANDARD_SUGGERITO_NUOVO, NOME_PACKAGE_STANDARD_SUGGERITO)
        );// end of lambda expressions and anonymous inner class
        layout.add(buttonPackage);

        this.add(layout);
        return layout;
    }// end of method

        private void elaboraNewProject(Map<Chiave, Object> mappaInput) {
        newProject.close();

        if (mappaInput != null) {
            elabora.newProject(mappaInput);
        }// end of if cycle

    }// end of method


    private void elaboraUpdateProject(Map<Chiave, Object> mappaInput) {
        updateProject.close();

        if (mappaInput != null) {
            elabora.updateProject(mappaInput);
        }// end of if cycle

    }// end of method


    private void elaboraPackage(Map<Chiave, Object> mappaInput) {
        dialogPackage.close();

        if (mappaInput != null) {
            elabora.newPackage(mappaInput);
        }// end of if cycle

    }// end of method

}// end of class
