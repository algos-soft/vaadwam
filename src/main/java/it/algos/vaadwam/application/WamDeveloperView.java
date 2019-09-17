package it.algos.vaadwam.application;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadwam.migration.MigrationService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;

import static it.algos.vaadflow.application.FlowVar.projectName;
import static it.algos.vaadwam.application.WamCost.TAG_CRO;
import static it.algos.vaadwam.application.WamCost.TAG_WAMDEV;

/**
 * Project vaadbase
 * Created by Algos
 * User: gac
 * Date: dom, 20-mag-2018
 * Time: 17:10
 */
@UIScope
@Route(value = TAG_WAMDEV, layout = MainLayout.class)
@Qualifier(TAG_WAMDEV)
@AIView(roleTypeVisibility = EARoleType.developer)
@Slf4j
public class WamDeveloperView extends VerticalLayout {

    /**
     * Icona visibile nel menu (facoltativa)
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.TOOLS;

    /**
     * Label del menu (facoltativa)
     * Vaadin usa il 'name' della Annotation @Route per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = "developer";

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    protected WamService wamService;

    //    @Autowired
    private CroceService croceService;

    //    @Autowired
    private ALogin login;


    //    @Autowired
    private MigrationService migration;

    private Label labelUno;

    private Button buttonUno;

    private ComboBox<Croce> fieldComboCroci;

    private Button buttonCroci;

    private Button buttonFunzioni;

    private Button buttonServizi;

    private Button buttonMiliti;

    private Button buttonTurni;

    private Croce croceCorrente;


    @Autowired
    public WamDeveloperView(ALogin login, CroceService croceService, MigrationService migration) {
        this.login = login;
        this.croceService = croceService;
        this.migration = migration;
        setDeveloper();
    }// end of Spring constructor


    public void setDeveloper() {
        Label etichetta;
        etichetta = new Label("Login come developer");
        this.add(etichetta);

        ACheckBox checkBox = new ACheckBox("Developer", login.isDeveloper());
//        checkBox.addValueChangeListener(event -> login.setDeveloper(event.getValue()));//end of lambda expressions
        this.add(checkBox);
    }// end of method


    @PostConstruct
    public void inizia() {
        this.setMargin(true);
        this.setSpacing(true);
        croceCorrente = wamService.getWamLogin().getCroce();

        String currentProject = System.getProperty("user.dir");
        currentProject = currentProject.substring(currentProject.lastIndexOf("/") + 1);

        labelUno = new Label("Selezione da codice di una company in sostituzione del login");
        this.add(labelUno);

        List croci = croceService.findAll();
        String label = "Croci esistenti";
        fieldComboCroci = new ComboBox<>();
        fieldComboCroci.setRequired(true);
        fieldComboCroci.setWidth("20em");
        fieldComboCroci.setHeight("100em");
        fieldComboCroci.setAllowCustomValue(false);
        fieldComboCroci.setLabel(label);
        fieldComboCroci.setItems(croci);
        fieldComboCroci.setValue(croceCorrente);
        fieldComboCroci.addValueChangeListener(event -> sincroCompany(event.getValue()));//end of lambda expressions
        this.add(fieldComboCroci);

//        buttonCroci = new Button("Croci");
//        buttonCroci.addClickListener(event -> migration.importOnlyCroci());
//        this.add(buttonCroci);
//
//        buttonFunzioni = new Button("Funzioni");
//        buttonFunzioni.addClickListener(event -> migration.importOnlyFunzioni());
//        this.add(buttonFunzioni);
//
//        buttonServizi = new Button("Servizi");
//        buttonServizi.addClickListener(event -> migration.importOnlyServizi());
//        this.add(buttonServizi);
//
//        buttonMiliti = new Button("Militi");
//        buttonMiliti.addClickListener(event -> migration.importOnlyMiliti());
//        this.add(buttonMiliti);
//
//        buttonTurni = new Button("Turni");
//        buttonTurni.addClickListener(event -> migration.importOnlyTurni());
//        this.add(buttonTurni);

    }// end of method


    private void sincroCompany(Croce valueFromCombo) {
        if (valueFromCombo != null) {
            croceCorrente = valueFromCombo;
            wamService.getWamLogin().setCroce(croceCorrente);
            AContext context = vaadinService.getSessionContext();
            ALogin login = context.getLogin();
            login.setCompany(croceCorrente);
            FlowVar.layoutTitle = croceCorrente != null ? croceCorrente.getOrganizzazione().getDescrizione() + " - " + croceCorrente.getDescrizione() : projectName;
        }// end of if cycle
    }// end of method


}// end of class
