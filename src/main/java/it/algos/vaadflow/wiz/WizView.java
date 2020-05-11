package it.algos.vaadflow.wiz;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.wiz.scripts.WizDialogNewProject;
import it.algos.vaadflow.wiz.scripts.WizDialogUpdateProject;
import it.algos.vaadflow.wiz.scripts.WizElaboraNewProject;
import it.algos.vaadflow.wiz.scripts.WizElaboraUpdateProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.File;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadflow.wiz.scripts.WizCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 13-apr-2020
 * Time: 03:02
 */
@Route(value = TAG_WIZ_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@AIView(vaadflow = true, menuIcon = VaadinIcon.MAGIC, roleTypeVisibility = EARoleType.developer)
public class WizView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private WizDialogNewProject dialogNewProject;

    @Autowired
    private WizDialogUpdateProject dialogUpdateProject;

    @Autowired
    private WizElaboraNewProject elaboraNewProject;

    @Autowired
    private WizElaboraUpdateProject elaboraUpdateProject;

    private boolean isProgettoBase = false;


    /**
     * Costruttore base senza parametri <br>
     */
    public WizView() {
    }// end of Vaadin/@Route constructor


    /**
     * Creazione iniziale (business logic) della view <br>
     * Chiamata DOPO costruttore, init(), postConstruct() e setParameter() <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l'interfaccia BeforeEnterObserver <br>
     * Chiamato DOPO @PostConstruct e DOPO setParameter() <br>
     *
     * @param beforeEnterEvent con la location, ui, navigationTarget, source, ecc
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        inizia();
    }// end of method


    /**
     * Costruzione grafica <br>
     */
    public void inizia() {
        this.setMargin(true);
        this.setPadding(true);
        this.setSpacing(false);

        titolo();
        selezioneProgettoInUso();

        if (isProgettoBase) {
            paragrafoNewProject();
        } else {
            paragrafoUpdateProject();
        }// end of if/else cycle

        terzoParagrafo();
        quartoParagrafo();
    }// end of method


    /**
     * Seleziona se parte da VaadFlow o da un altro progetto <br>
     * Comportamento diverso:
     * 1) da VaadFlow può creare nuovi progetti
     * 2) da un progetto derivato può 'aggiornare' la propria cartella VaadFlow
     */
    public void selezioneProgettoInUso() {
        String pathUserDir = System.getProperty("user.dir") + SLASH;
        File unaDirectory = new File(pathUserDir);
        String nomeDirectory = unaDirectory.getName();
        isProgettoBase = nomeDirectory.equals(NAME_VAADFLOW);
    }// end of method


    public void titolo() {
        H2 titolo = new H2("Gestione Wizard");
        titolo.getElement().getStyle().set("color", "green");
        this.add(titolo);
    }// end of method


    public void paragrafoNewProject() {
        H3 paragrafo = new H3(TITOLO_NUOVO_PROGETTO);
        paragrafo.getElement().getStyle().set("color", "blue");
        this.add(paragrafo);
        this.add(new Label("Vuoto e nella directory IdeaProjects"));

        Button bottone = new Button("New project");
        bottone.getElement().setAttribute("theme", "pimary");
        bottone.addClickListener(event -> dialogNewProject.open(this::elaboraNewProject));
        this.add(bottone);
        this.add(new H1());
    }// end of method


    private void elaboraNewProject() {
        dialogNewProject.close();
        elaboraNewProject.esegue();
    }// end of method


    public void paragrafoUpdateProject() {
        H3 paragrafo = new H3(TITOLO_MODIFICA_PROGETTO);
        paragrafo.getElement().getStyle().set("color", "blue");
        this.add(paragrafo);
        this.add(new Label("Esistente in qualsiasi directory"));

        Button bottone = new Button("Update project");
        bottone.getElement().setAttribute("theme", "pimary");
        bottone.addClickListener(event -> dialogUpdateProject.open(this::elaboraUpdateProject));
        this.add(bottone);
        this.add(new H1());
    }// end of method


    private void elaboraUpdateProject() {
        elaboraUpdateProject.esegue();
        dialogUpdateProject.close();
    }// end of method


    public void terzoParagrafo() {
        H3 paragrafo = new H3("Nuovo package di VaadFlow");
        paragrafo.getElement().getStyle().set("color", "blue");
        this.add(paragrafo);
        this.add(new H1());
    }// end of method


    public void quartoParagrafo() {
        H3 paragrafo = new H3("Nuovo package di Test");
        paragrafo.getElement().getStyle().set("color", "blue");
        this.add(paragrafo);
    }// end of method


    public void apriNuovoProgetto() {
        getUI().ifPresent(ui -> ui.navigate(TAG_WIZ_NEW_PROJECT));
    }// end of method

}// end of class
