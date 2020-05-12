package it.algos.vaadwam.migration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadwam.application.WamCost.TAG_IMP;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 16-mag-2018
 * Time: 12:06
 * Annotated with @Route (obbligatorio)
 * Annotated with @Theme (facoltativo)
 */
@UIScope
@Route(value = TAG_IMP, layout = MainLayout.class)
@Qualifier(TAG_IMP)
@AIView(roleTypeVisibility = EARoleType.developer)
@Slf4j
public class ImportView extends VerticalLayout {

    /**
     * Icona visibile nel menu (facoltativa)
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.INSERT;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public FunzioneService funzioneService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ServizioService servizioService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MiliteService militeService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TurnoService turnoService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public CroceService croceService;

    private Label labelUno;

    private Label labelDue;

    private Label labelTre;

    private Label labelQuattro;

    private HorizontalLayout layoutCroci;

    private HorizontalLayout layoutFunzioni;

    private HorizontalLayout layoutServizi;

    private HorizontalLayout layoutMiliti;

    private HorizontalLayout layoutTurni;

    private HorizontalLayout layoutAll;

    private Button buttonCroci;

    private Button buttonFunzioni;

    private Button buttonServizi;

    private Button buttonMiliti;

    private Button buttonTurni;

    private Button buttonAll;

    /**
     * Libreria di servizio. Inietta da Spring nel costruttore come 'singleton'
     */
    @Autowired
    private MigrationService migration;


    public ImportView() {
        this.removeAll();
        checkIniziale();
    }// end of Spring constructor


    public void checkIniziale() {
        this.setMargin(true);
        this.setSpacing(true);

        labelUno = new Label("Import dei dati dal vecchio webambulanze");
        this.add(labelUno);

        //        this.crociOnly();
        this.funzioniOnly();
        this.serviziOnly();
        this.militiOnly();
        this.turniOnly();
        //        this.all();
    }// end of method


    private void crociOnly() {
        layoutCroci = new HorizontalLayout();
        layoutCroci.setMargin(false);
        layoutCroci.setSpacing(true);

        buttonCroci = new Button("Croci only");
        buttonCroci.addClickListener(e -> {
            if (migration.importOnlyCroci()) {
                layoutCroci.add(new Checkbox("Fatto", true));
            } else {
                layoutCroci.add(new Checkbox("Import non riuscito", true));
            }// end of if/else cycle
        });//end of lambda expressions
        layoutCroci.add(buttonCroci);

        this.add(layoutCroci);
    }// end of method


    private void funzioniOnly() {
        layoutFunzioni = new HorizontalLayout();
        layoutFunzioni.setMargin(false);
        layoutFunzioni.setSpacing(true);

        buttonFunzioni = new Button("Funzioni only");
        buttonFunzioni.addClickListener(e -> layoutFunzioni.add(new Checkbox(funzioneService.importAll(), true)));//end of lambda expressions
        layoutFunzioni.add(buttonFunzioni);

        this.add(layoutFunzioni);
    }// end of method


    private void serviziOnly() {
        layoutServizi = new HorizontalLayout();
        layoutServizi.setMargin(false);
        layoutServizi.setSpacing(true);

        buttonServizi = new Button("Servizi only");
        buttonServizi.addClickListener(e -> layoutServizi.add(new Checkbox(servizioService.importAll(), true)));//end of lambda expressions
        layoutServizi.add(buttonServizi);

        this.add(layoutServizi);
    }// end of method


    private void militiOnly() {
        layoutMiliti = new HorizontalLayout();
        layoutMiliti.setMargin(false);
        layoutMiliti.setSpacing(true);

        buttonMiliti = new Button("Militi only");
        buttonMiliti.addClickListener(e -> layoutMiliti.add(new Checkbox(militeService.importAll(), true)));//end of lambda expressions
        layoutMiliti.add(buttonMiliti);

        this.add(layoutMiliti);
    }// end of method


    private void turniOnly() {
        layoutTurni = new HorizontalLayout();
        layoutTurni.setMargin(false);
        layoutTurni.setSpacing(true);

        buttonTurni = new Button("Turni only");
        buttonTurni.addClickListener(e -> layoutTurni.add(new Checkbox(turnoService.importAll(), true)));//end of lambda expressions
        layoutTurni.add(buttonTurni);

        this.add(layoutTurni);
    }// end of method


    private void all() {
        layoutAll = new HorizontalLayout();
        layoutAll.setMargin(false);
        layoutAll.setSpacing(true);

        buttonAll = new Button("All");
        buttonAll.addClickListener(e -> {
            serviziOnly();
            funzioniOnly();
            militiOnly();
            turniOnly();
            layoutAll.add(new Checkbox("Fatto", true));
        });//end of lambda expressions
        layoutAll.add(buttonAll);

        this.add(layoutAll);
    }// end of method


}// end of class
