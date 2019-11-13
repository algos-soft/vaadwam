package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.wizard.WizardView;
import it.algos.vaadflow.wizard.enumeration.Chiave;
import it.algos.vaadflow.wizard.enumeration.Progetto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vbase
 * Created by Algos
 * User: gac
 * Date: mar, 20-mar-2018
 * Time: 16:09
 * <p>
 * Dialogo per la creazione/modifica di un package (modulo)
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class TDialogoPackage extends TDialogo {


    public final static int DURATA = 2000;
    public final static boolean DEFAULT_USA_ORDINE = true;
    public final static boolean DEFAULT_USA_CODE = true;
    public final static boolean DEFAULT_USA_DESCRIZIONE = false;
    public final static boolean DEFAULT_USA_KEY_CODE_SPECIFICA = false;
    public final static boolean DEFAULT_USA_COMPANY = false;
    public final static boolean DEFAULT_USA_PAGINATED_GRID = true;
    public final static boolean DEFAULT_USA_LIST_ESTESA = true;
    public final static boolean DEFAULT_USA_SOVRASCRIVE = false;
    public final static boolean DEFAULT_USA_ALL_PACKAGE = false;
    private static final String CAPTION = WizardView.VAADFLOW + WizardView.PACKAGE;
    private static Progetto PROGETTO_STANDARD_SUGGERITO = Progetto.vaadin;
    private static String NOME_PACKAGE_STANDARD_SUGGERITO = "prova";
    //    private static String CAPTION = "Package";
    private static String RADIO_NEW = "Creazione di un nuovo package";
    private static String RADIO_UPDATE = "Modifica di un package esistente";
    private Button buttonUno;
    //    private NativeButton confirmButton;
//    private NativeButton cancelButton;
    private Dialog dialog = new Dialog();
    private boolean newPackage;
    private Progetto project;
    private String packageName;
    private List<String> packages;

    private HorizontalLayout packagePlaceHolder;
    private ComboBox<String> fieldComboPackage;
    private TextField fieldTextProject;
    private TextField fieldTextPackage;
    private TextField fieldTextEntity; // suggerito
    private TextField fieldTextTag; // suggerito
    private Checkbox fieldCheckBoxPropertyOrdine;
    private Checkbox fieldCheckBoxPropertyCode;
    private Checkbox fieldCheckBoxPropertyDescrizione;
    private Checkbox fieldCheckBoxUsaKeyIdCode;
    private Checkbox fieldCheckBoxCompany;
    private Checkbox fieldCheckBoPaginatedGrid;
    private Checkbox fieldCheckBoListEstesa;
    private Checkbox fieldCheckBoxSovrascrive;
    private Checkbox fieldCheckBoxAllPackage;

    private boolean progettoBase;

    /**
     * Costruttore
     */
    public TDialogoPackage() {
        super();
    }// end of Spring constructor


    public void open(TRecipient recipient, boolean newPackage, Progetto progetto, String packageName) {
        this.recipient = recipient;
        this.newPackage = newPackage;
        this.project = progetto != null ? progetto : PROGETTO_STANDARD_SUGGERITO;
        this.packageName = text.isValid(packageName) ? packageName : "";
        this.removeAll();
        super.open();

        String currentProject = System.getProperty("user.dir");
        currentProject = currentProject.substring(currentProject.lastIndexOf("/") + 1);
        progettoBase = currentProject.equals(PROJECT_BASE_NAME);

        this.add(creaTop());
        this.add(creaRadio());
        this.add(creaBody());
        this.add(creaFlag());
        this.add(creaFooter());

        sincroRadio(groupTitolo.getValue());
        addListeners();
        sincroPackageNew(packageName);
    }// end of method

    private void addListeners() {
        groupTitolo.addValueChangeListener(event -> sincroRadio(event.getValue()));//end of lambda expressions
        if (progettoBase) {
            fieldComboProgetti.addValueChangeListener(event -> sincroProject(event.getValue()));//end of lambda expressions
        }// end of if cycle
        fieldTextPackage.addValueChangeListener(event -> sincroPackage(event.getValue()));//end of lambda expressions
        fieldComboPackage.addValueChangeListener(event -> sincroPackage(event.getValue()));//end of lambda expressions
        fieldCheckBoxAllPackage.addValueChangeListener(event -> sincroAllPackages(event.getValue()));//end of lambda expressions
    }// end of method


    private Component creaTop() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.add(new Label(WizardView.VAADFLOW));
        layout.add(new Label(WizardView.PACKAGE));

        return layout;
    }// end of method

    private Component creaRadio() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);

        groupTitolo = new RadioButtonGroup<>();
        groupTitolo.setItems(RADIO_NEW, RADIO_UPDATE);
        groupTitolo.getElement().getStyle().set("display", "flex");
        groupTitolo.getElement().getStyle().set("flexDirection", "columnService");

        if (newPackage) {
            groupTitolo.setValue(RADIO_NEW);
        } else {
            groupTitolo.setValue(RADIO_UPDATE);
        }// end of if/else cycle

        layout.add(groupTitolo);
        return layout;
    }// end of method


    private Component creaBody() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);

        layout.add(creaProject());
        layout.add(creaPackage());
        layout.add(creaEntity());
        layout.add(creaTag());

        return layout;
    }// end of method


    private Component creaFlag() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);

        layout.add(creaOrdine());
        layout.add(creaCode());
        layout.add(creaDescrizione());
        layout.add(creaKeyIdCode());
        layout.add(creaCompany());
        layout.add(creaGrid());
        layout.add(creaList());
        layout.add(creaSovrascrive());
        layout.add(creaAllPackage());

        restartAll();
        return layout;
    }// end of method


    private Component creaProject() {
        if (progettoBase) {
            fieldComboProgetti = new ComboBox<>();
            fieldComboProgetti.setAllowCustomValue(false);
            String label = "Progetto";
            ArrayList<Progetto> progetti = new ArrayList<>();
            progetti.add(Progetto.vaadin);
            progetti.add(Progetto.test);
            fieldComboProgetti.setLabel(label);
            fieldComboProgetti.setItems(progetti);

            if (progetti.contains(project) && isProgettoEsistente()) {
                fieldComboProgetti.setValue(project);
            } else {
                fieldComboProgetti.setValue(null);
            }// end of if/else cycle

            return fieldComboProgetti;
        } else {
            String currentProject = System.getProperty("user.dir");
            currentProject = currentProject.substring(currentProject.lastIndexOf("/") + 1);
            fieldTextProject = new TextField();
            fieldTextProject.setLabel("Progetto");
            fieldTextProject.setValue(currentProject);
            fieldTextProject.setEnabled(false);
            return fieldTextProject;
        }// end of if/else cycle
    }// end of method


    private Component creaPackage() {
        packagePlaceHolder = new HorizontalLayout();
        String label = "Package";

        fieldComboPackage = new ComboBox<>();
        fieldComboPackage.setAllowCustomValue(true);
        fieldComboPackage.setLabel(label);

        fieldTextPackage = new TextField();
        fieldTextPackage.setLabel(label);
        fieldTextPackage.setEnabled(true);
        fieldTextPackage.focus();

        return packagePlaceHolder;
    }// end of method


    private Component creaEntity() {
        fieldTextEntity = new TextField();
        fieldTextEntity.setLabel("Entity");

        fieldTextEntity.setEnabled(true);
        return fieldTextEntity;
    }// end of method


    private Component creaTag() {
        fieldTextTag = new TextField();
        fieldTextTag.setLabel("Tag");

        fieldTextTag.setEnabled(true);
        return fieldTextTag;
    }// end of method


    private Component creaOrdine() {
        fieldCheckBoxPropertyOrdine = new Checkbox();
        fieldCheckBoxPropertyOrdine.setLabel("Usa la property Ordine (int)");

        return fieldCheckBoxPropertyOrdine;
    }// end of method

    private Component creaCode() {
        fieldCheckBoxPropertyCode = new Checkbox();
        fieldCheckBoxPropertyCode.setLabel("Usa la property Code (String)");

        return fieldCheckBoxPropertyCode;
    }// end of method

    private Component creaDescrizione() {
        fieldCheckBoxPropertyDescrizione = new Checkbox();
        fieldCheckBoxPropertyDescrizione.setLabel("Usa la property Descrizione (String)");

        return fieldCheckBoxPropertyDescrizione;
    }// end of method

    private Component creaKeyIdCode() {
        fieldCheckBoxUsaKeyIdCode = new Checkbox();
        fieldCheckBoxUsaKeyIdCode.setLabel("Usa la property Code (String) come keyID");

        return fieldCheckBoxUsaKeyIdCode;
    }// end of method

    private Component creaCompany() {
        fieldCheckBoxCompany = new Checkbox();
        fieldCheckBoxCompany.setLabel("Utilizza MultiCompany");

        return fieldCheckBoxCompany;
    }// end of method

    private Component creaGrid() {
        fieldCheckBoPaginatedGrid = new Checkbox();
        fieldCheckBoPaginatedGrid.setLabel("Utilizza PaginatedGrid");

        return fieldCheckBoPaginatedGrid;
    }// end of method

    private Component creaList() {
        fieldCheckBoListEstesa = new Checkbox();
        fieldCheckBoListEstesa.setLabel("List estesa");

        return fieldCheckBoListEstesa;
    }// end of method


    private Component creaSovrascrive() {
        fieldCheckBoxSovrascrive = new Checkbox();
        fieldCheckBoxSovrascrive.setLabel("Sovrascrive tutti i files esistenti del package");

        return fieldCheckBoxSovrascrive;
    }// end of method

    private Component creaAllPackage() {
        fieldCheckBoxAllPackage = new Checkbox();
        fieldCheckBoxAllPackage.setLabel("Aggiorna tutti i packages esistenti nel progetto");
        fieldCheckBoxAllPackage.setValue(DEFAULT_USA_ALL_PACKAGE);

        return fieldCheckBoxAllPackage;
    }// end of method


//    private Component creaFooter() {
//        VerticalLayout layout = new VerticalLayout();
//        HorizontalLayout layoutFooter = new HorizontalLayout();
//        layoutFooter.setSpacing(true);
//        layoutFooter.setMargin(true);
//
//        cancelButton = new NativeButton("Annulla", event -> {
//            recipient.gotInput(null);
//            dialog.close();
//        });//end of lambda expressions
//        cancelButton.setWidth(NORMAL_WIDTH);
//        cancelButton.setHeight(NORMAL_HEIGHT);
//
//        confirmButton = new NativeButton("Conferma", event -> {
//            if (fieldCheckBoxPropertyCode.getValue()) {
//                chiudeDialogo();
//            } else {
////                Notification.show("Stai creando una EntityClass SENZA la property 'code'. È possibile, ma alcune linee di codice andranno riscritte.",2000,Notification.Position.MIDDLE);
//                Notification.show("Stai tentando di creare una EntityClass SENZA la property 'code'. Non è possibile.", DURATA, Notification.Position.MIDDLE);
//            }// end of if/else cycle
//        });//end of lambda expressions
//        confirmButton.setWidth(NORMAL_WIDTH);
//        confirmButton.setHeight(NORMAL_HEIGHT);
//
//        layoutFooter.add(cancelButton, confirmButton);
//        layout.add(layoutFooter);
//        return layout;
//    }// end of method
//
//
//    private void chiudeDialogo() {
//        setMappa();
//        recipient.gotInput(mappaInput);
//        dialog.close();
//    }// end of method


    private void sincroRadio(String radioSelected) {
        if (radioSelected.equals(RADIO_NEW)) {
            newPackage = true;
            packageName = "";
        }// end of if cycle

        if (radioSelected.equals(RADIO_UPDATE)) {
            newPackage = false;
        }// end of if cycle

        if (progettoBase) {
            sincroProject(fieldComboProgetti.getValue());
        } else {
            sincroProject(fieldTextProject.getValue());
        }// end of if/else cycle
    }// end of method


    /**
     * Esce dal combo progetti ed regola il package
     */
    private void sincroProject(Progetto progettoSelezionato) {
        project = progettoSelezionato;
        sincroProject(progettoSelezionato.getNameProject());
    }// end of method


    /**
     * Esce dal combo progetti ed regola il package
     */
    private void sincroProject(String nomeProgettoSelezionato) {
        String namePackage;

        if (!isProgettoEsistente()) {
            Notification.show("Non esiste il progetto " + nomeProgettoSelezionato + ". Devi modificare il codice: wizard.enumeration.Progetto", DURATA, Notification.Position.MIDDLE);
            project = null;
            fieldTextPackage.setValue("");
            String[] vuota = {""};
            fieldComboPackage.setItems(vuota);
            fieldComboPackage.setValue("");
            return;
        }// end of if cycle

        packagePlaceHolder.removeAll();

        if (newPackage) {
            packagePlaceHolder.add(fieldTextPackage);
            fieldTextPackage.setValue("");
            sincroPackage(packageName);
        } else {
            packagePlaceHolder.add(fieldComboPackage);
            if (text.isEmpty(nomeProgettoSelezionato)) {
                invalida(true);
                if (progettoBase) {
                    fieldComboPackage.setValue(null);
                } else {
                    fieldTextProject.setValue("");
                }// end of if/else cycle
            } else {
                packages = recuperaPackageEsistenti(nomeProgettoSelezionato);
                if (packages != null && fieldComboPackage != null) {
                    fieldComboPackage.setItems(packages);
                } else {
                    Notification.show("Non esistono packages nel progetto " + nomeProgettoSelezionato + ". Puoi crearne uno nuovo", DURATA * 2, Notification.Position.MIDDLE);
                    groupTitolo.setValue(RADIO_NEW);
                }// end of if/else cycle

                namePackage = packageName;
                if (text.isValid(namePackage)) {
                    if (packages != null && packages.contains(namePackage)) {
                        fieldComboPackage.setValue(namePackage);
                    }// end of if cycle
                }// end of if cycle
            }// end of if/else cycle
            sincroPackage(fieldComboPackage.getValue() != null ? fieldComboPackage.getValue() : "");
        }// end of if/else cycle
    }// end of method


    private void sincroPackage(String valueFromPackage) {
        if (text.isValid(valueFromPackage) && valueFromPackage.length() > 2) {

            if (newPackage && isPackageEsistente()) {
                Notification.show("Esiste già questo package", DURATA, Notification.Position.MIDDLE);
                packageName = fieldTextPackage.getValue();
                groupTitolo.setValue(RADIO_UPDATE);
            } else {
                valueFromPackage = valueFromPackage.toLowerCase();
                fieldTextPackage.setValue(valueFromPackage);
                fieldTextEntity.setValue(text.primaMaiuscola(fieldTextEntity.getValue()));

                invalida(false);
                if (confirmButton != null) {
                    confirmButton.setVisible(true);
                }// end of if cycle

                fieldTextEntity.setValue(text.primaMaiuscola(valueFromPackage));
                fieldTextTag.setValue(valueFromPackage.substring(0, 3).toUpperCase());
            }// end of if/else cycle
        } else {
            invalida(true);
            Notification.show("Il nome del package deve essere di almeno 3 caratteri", DURATA, Notification.Position.MIDDLE);
            if (confirmButton != null) {
                confirmButton.setVisible(false);
            }// end of if cycle

            fieldTextEntity.setValue("");
            fieldTextTag.setValue("");
        }// end of if/else cycle


////        setMappa();
    }// end of method

    private void sincroPackageNew(String packageName) {
        fieldTextPackage.setValue(packageName);
        this.packageName = packageName;
    }// end of method

    private void sincroAllPackages(boolean value) {
        if (value) {
            resetAll();
            Notification.show("Attenzione che vengono sovrascritti TUTTI i package esistenti nel progetto", DURATA * 2, Notification.Position.MIDDLE);
        } else {
            restartAll();
            fieldComboPackage.setEnabled(true);
            confirmButton.setVisible(false);
        }// end of if/else cycle
    }// end of method

    private void resetAll() {
        fieldCheckBoxPropertyOrdine.setValue(false);
        fieldCheckBoxPropertyOrdine.setEnabled(false);
        fieldCheckBoxPropertyCode.setValue(false);
        fieldCheckBoxPropertyCode.setEnabled(false);
        fieldCheckBoxPropertyDescrizione.setValue(false);
        fieldCheckBoxPropertyDescrizione.setEnabled(false);
        fieldCheckBoxUsaKeyIdCode.setValue(false);
        fieldCheckBoxUsaKeyIdCode.setEnabled(false);
        fieldCheckBoxCompany.setValue(false);
        fieldCheckBoxCompany.setEnabled(false);
        fieldCheckBoPaginatedGrid.setValue(true);
        fieldCheckBoPaginatedGrid.setEnabled(false);
        fieldCheckBoListEstesa.setValue(true);
        fieldCheckBoListEstesa.setEnabled(false);
        fieldCheckBoxSovrascrive.setValue(false);
        fieldCheckBoxSovrascrive.setEnabled(false);

        fieldComboPackage.setValue(null);
        fieldComboPackage.setEnabled(false);

        groupTitolo.setValue(RADIO_UPDATE);
        confirmButton.setVisible(true);
    }// end of method

    private void restartAll() {
        fieldCheckBoxPropertyOrdine.setValue(DEFAULT_USA_ORDINE);
        fieldCheckBoxPropertyOrdine.setEnabled(true);
        fieldCheckBoxPropertyCode.setValue(DEFAULT_USA_CODE);
        fieldCheckBoxPropertyCode.setEnabled(true);
        fieldCheckBoxPropertyDescrizione.setValue(DEFAULT_USA_DESCRIZIONE);
        fieldCheckBoxPropertyDescrizione.setEnabled(true);
        fieldCheckBoxUsaKeyIdCode.setValue(DEFAULT_USA_KEY_CODE_SPECIFICA);
        fieldCheckBoxUsaKeyIdCode.setEnabled(true);
        fieldCheckBoxCompany.setValue(DEFAULT_USA_COMPANY);
        fieldCheckBoxCompany.setEnabled(true);
        fieldCheckBoPaginatedGrid.setValue(DEFAULT_USA_PAGINATED_GRID);
        fieldCheckBoPaginatedGrid.setEnabled(true);
        fieldCheckBoListEstesa.setValue(DEFAULT_USA_LIST_ESTESA);
        fieldCheckBoListEstesa.setEnabled(true);
        fieldCheckBoxSovrascrive.setValue(DEFAULT_USA_SOVRASCRIVE);
        fieldCheckBoxSovrascrive.setEnabled(true);
    }// end of method


    private void invalida(boolean status) {
        if (fieldComboPackage != null) {
            fieldComboPackage.setInvalid(status);
        }// end of if cycle
        if (fieldTextPackage != null) {
            fieldTextPackage.setInvalid(status);
        }// end of if cycle
        if (fieldTextEntity != null) {
            fieldTextEntity.setInvalid(status);
        }// end of if cycle
        if (fieldTextTag != null) {
            fieldTextTag.setInvalid(status);
        }// end of if cycle
    }// end of method


    private boolean isProgettoEsistente() {
        boolean esiste = false;
        String pathProject = getPathProject();

        if (file.isEsisteDirectory(pathProject)) {
            esiste = true;
        }// end of if cycle

        return esiste;
    }// end of method


    private boolean isPackageEsistente() {
        boolean esiste = false;
        String pathModules = getPathModules();
        List<String> packagesEsistenti = file.getSubdirectories(pathModules);

        if (packagesEsistenti != null && packagesEsistenti.contains(getPackage())) {
            esiste = true;
        }// end of if cycle

        return esiste;
    }// end of method


    private boolean isEntityEsistente() {
        String java = ".java";
        String entity = text.primaMaiuscola(fieldTextEntity.getValue()) + java;

        return checkBase(entity);
    }// end of method


    private boolean isTagEsistente() {
        boolean esiste = false;

        if (fieldTextTag.getValue().equals("ROL")) {
            esiste = true;
        } else {
            esiste = false;
        }// end of if/else cycle

        return esiste;
    }// end of method


    private String getPathProject() {
        String path = "";
        String sep = "/";
        String userDir = System.getProperty("user.dir");
        String projectName = "";

        if (project != null) {
            projectName = project.getNameProject();
        }// end of if cycle

        String ideaProjectRootPath = text.levaCodaDa(userDir, sep);
        path = ideaProjectRootPath + sep + projectName;

        return path;
    }// end of method


    private String getPathModules() {
        String path = "";
        String sep = "/";
        String userDir = System.getProperty("user.dir");
        Progetto project = null;
        String projectName = "";
        String moduleName = "";

        if (progettoBase) {
            project = fieldComboProgetti.getValue();
            if (project != null) {
                projectName = project.getNameProject();
                moduleName = project.getNameModule();
            }// end of if cycle
        } else {
            projectName = fieldTextProject.getValue();
            moduleName = projectName;
        }// end of if/else cycle

        String ideaProjectRootPath = text.levaCodaDa(userDir, sep);
        String projectBasePath = ideaProjectRootPath + sep + projectName;

        path += projectBasePath;
        path += DIR_JAVA;
        path += sep;
        path += moduleName;
        path += sep;
        path += ENTITIES_NAME;

        return path;
    }// end of method


    private boolean checkBase(String name) {
        boolean esiste = false;
        String path = "";
        String sep = "/";
        String pack = getPackage();
        String pathModules = getPathModules();

        path += pathModules;
        path += pack;
        path += sep;
        path += name;

        if (path.endsWith(sep)) {
            path = text.levaCodaDa(path, sep);
            esiste = file.isEsisteDirectory(path);
        } else {
            esiste = file.isEsisteFile(path);
        }// end of if/else cycle

        return esiste;
    }// end of method

    private String getPackage() {
        String pack = "";

        if (newPackage) {
            pack = fieldTextPackage.getValue();
        } else {
            pack = fieldComboPackage.getValue() != null ? fieldComboPackage.getValue().toLowerCase() : "";
        }// end of if/else cycle

        return pack;
    }// end of method


    protected void setMappa() {
        if (mappaInput != null) {
            if (progettoBase) {
                mappaInput.put(Chiave.targetProjectName, fieldComboProgetti.getValue());
            } else {
                String nameCurrentProject = fieldTextProject.getValue();
                Progetto currentProject = Progetto.getProgetto(nameCurrentProject);
                mappaInput.put(Chiave.targetProjectName, currentProject != null ? currentProject : nameCurrentProject);
            }// end of if/else cycle
            mappaInput.put(Chiave.newPackageName, getPackage());
            mappaInput.put(Chiave.newEntityName, text.primaMaiuscola(fieldTextEntity.getValue()));
            mappaInput.put(Chiave.newEntityTag, fieldTextTag.getValue());
            mappaInput.put(Chiave.flagOrdine, fieldCheckBoxPropertyOrdine.getValue());
            mappaInput.put(Chiave.flagCode, fieldCheckBoxPropertyCode.getValue());
            mappaInput.put(Chiave.flagKeyCode, fieldCheckBoxUsaKeyIdCode.getValue());
            mappaInput.put(Chiave.flagDescrizione, fieldCheckBoxPropertyDescrizione.getValue());
            mappaInput.put(Chiave.flagCompany, fieldCheckBoxCompany.getValue());
            mappaInput.put(Chiave.flagGrid, fieldCheckBoPaginatedGrid.getValue());
            mappaInput.put(Chiave.flagList, fieldCheckBoListEstesa.getValue());
            mappaInput.put(Chiave.flagSovrascrive, fieldCheckBoxSovrascrive.getValue());
            mappaInput.put(Chiave.flagUsaAllPackages, fieldCheckBoxAllPackage.getValue());
        }// end of if cycle
    }// end of method

    private List<String> recuperaPackageEsistenti(String projectName) {
        return file.getSubdirectories(getPathModules());
    }// end of method

}// end of class
