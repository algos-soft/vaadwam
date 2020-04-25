package it.algos.vaadflow.ui.form;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.service.IAService;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadflow.application.FlowCost.USA_DEBUG;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 10-apr-2020
 * Time: 20:06
 * Classe astratta per visualizzare il Form <br>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewForm (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewForm con la business logic principale <br>
 * - 3 APrefViewList per regolare i parametri, le preferenze ed i flags <br>
 * - 4 ALayoutViewForm per regolare il layout <br>
 * - 5 AFieldsViewForm per gestire i Fields <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 */
public abstract class ALayoutViewForm extends APrefViewForm {

    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public ALayoutViewForm(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of Vaadin/@Route constructor


    /**
     * Qui va tutta la logica grafica della view <br>
     * <p>
     * Graficamente abbiamo in tutte (di solito) le XxxViewForm: <br>
     * 1) un titolo (eventuale, presente di default) di tipo Label o HorizontalLayout <br>
     * 2) un alertPlaceholder di avviso (eventuale) con label o altro per informazioni; di norma per il developerv <br>
     * 3) un Form (obbligatorio); <br>
     * 4) un bottomPlacehorder (obbligatorio) con i bottoni di navigazione, conferma, cancella <br>
     * 5) un footer (obbligatorio) con informazioni generali <br>
     */
    protected void initView() {
        this.removeAll();

        //--Costruisce tutti i placeholder di questa view
        this.fixLayout();

        //--Regola il titolo della view <br>
        this.fixTitleLayout();

        //--Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
        this.fixAlertLayout();

        //--Form placeholder standard per i campi
        this.fixFormBody();

        //--Separatore
        this.add(new H3());

        //--Form placeholder accessorio eventuale per altri campi, resi graficamente diversi
        this.fixFormSubBody();

        //--Regola la barra dei bottoni di comando <br>
        this.fixBottomBar();
    }// end of method


    /**
     * Costruisce tutti i placeholder di questa view e li aggiunge alla view stessa <br>
     * Chiamato da AViewForm.initView() <br>
     * Può essere sovrascritto, per modificare il layout standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixLayout() {
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(true);

        super.titlePlaceholder = new Div();
        super.alertPlacehorder = new VerticalLayout();
        super.bodyPlaceHolder = new Div();
        super.bodySubPlaceHolder = new FormLayout();
        super.bottomPlacehorder = new HorizontalLayout();

        if (pref.isBool(USA_DEBUG)) {
            this.getElement().getStyle().set("background-color", EAColor.yellow.getEsadecimale());
            titlePlaceholder.getElement().getStyle().set("background-color", EAColor.lime.getEsadecimale());
            alertPlacehorder.getElement().getStyle().set("background-color", EAColor.lightgreen.getEsadecimale());
            bodyPlaceHolder.getElement().getStyle().set("background-color", EAColor.bisque.getEsadecimale());
            bodySubPlaceHolder.getElement().getStyle().set("background-color", EAColor.red.getEsadecimale());
            bottomPlacehorder.getElement().getStyle().set("background-color", EAColor.silver.getEsadecimale());
        }// end of if cycle
    }// end of method


    /**
     * Regola il titolo della view <br>
     * <p>
     * Chiamato da AViewForm.initView() <br>
     * Recupera recordName dalle @Annotation della classe Entity. Non dovrebbe mai essere vuoto. <br>
     * Costruisce il titolo con la descrizione dell'operazione (New, Edit,...) ed il recordName <br>
     * Sostituisce interamente il titlePlaceholder <br>
     */
    protected void fixTitleLayout() {
        String recordName = annotation.getRecordName(binderClass);
        String title = text.isValid(recordName) ? recordName : "Error";
        title = operationForm.getNameInTitle() + " " + title.toLowerCase();
        String titoloValido = text.isValid(titoloForm) ? titoloForm : title;

        if (usaTitoloForm) {
            if (operationForm != null) {
                titlePlaceholder.add(new H2(titoloValido));
            }// end of if cycle
        }// end of if cycleù

        this.add(titlePlaceholder);
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewForm.initView() <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixAlertLayout() {
        alertPlacehorder.removeAll();
        alertPlacehorder.setMargin(false);
        alertPlacehorder.setSpacing(false);
        alertPlacehorder.setPadding(false);
        this.add(alertPlacehorder);
    }// end of method


    /**
     * Form placeholder standard per i campi <br>
     * Chiamato da AViewForm.initView() <br>
     */
    protected void fixFormBody() {
        formLayout = new FormLayout();

        if (usaFormDueColonne) {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep(minWidthForm, 2));
        } else {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep(minWidthForm, 1));
        }// end of if/else cycle

        formLayout.addClassName("no-padding");

        bodyPlaceHolder.add(formLayout);
        bodyPlaceHolder.addClassName("has-padding");

        this.add(bodyPlaceHolder);
    }// end of method


    /**
     * Form placeholder accessorio eventuale per altri campi, resi graficamente diversi <br>
     * Chiamato da AViewForm.initView() <br>
     */
    protected void fixFormSubBody() {
    }// end of method


    /**
     * Regola la barra dei bottoni di comando <br>
     * Chiamato da AViewForm.initView() <br>
     */
    protected void fixBottomBar() {
        bottomPlacehorder.removeAll();
        bottomPlacehorder.setClassName("buttons");
        bottomPlacehorder.setPadding(false);
        bottomPlacehorder.setSpacing(true);
        bottomPlacehorder.setMargin(false);

        Label spazioVuotoEspandibile = new Label("");
        bottomPlacehorder.add(spazioVuotoEspandibile);

        fixBackButton();
        fixEditButton();
        fixSaveButton();
        fixDeleteButton();

//        cancelButton = new Button(ANNULLA);
//        annullaButton = new Button(ANNULLA);

        bottomPlacehorder.setFlexGrow(1, spazioVuotoEspandibile);
        this.add(bottomPlacehorder);
    }// end of method


    /**
     * Regola il bottone di 'ritorno' <br>
     * Chiamato da AViewForm.fixBottomBar() <br>
     * Può essere sovrascritto, per modificare titolo, icona, colore e dimensioni del bottone <br>
     */
    protected void fixBackButton() {
        if (usaBackButton) {
            backButton = new Button(backButtonText);
            backButton.addClickListener(e -> ritorno());
            backButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                backButton.addClickShortcut(Key.ARROW_LEFT);
            }// end of if cycle
            bottomPlacehorder.add(backButton);
        }// end of if cycle
    }// end of method


    /**
     * Regola il bottone di 'modifica' <br>
     * Chiamato da AViewForm.fixBottomBar() <br>
     * Può essere sovrascritto, per modificare titolo, icona, colore e dimensioni del bottone <br>
     */
    protected void fixEditButton() {
        if (usaEditButton) {
            editButton = new Button("Edit");
            editButton.addClickListener(e -> modifica());
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                editButton.addClickShortcut(Key.ENTER);
            }// end of if cycle
            bottomPlacehorder.add(editButton);
        }// end of if cycle
    }// end of method


    /**
     * Regola il bottone di 'registra' <br>
     * Chiamato da AViewForm.fixBottomBar() <br>
     * Può essere sovrascritto, per modificare titolo, icona, colore e dimensioni del bottone <br>
     */
    protected void fixSaveButton() {
        if (usaSaveButton) {
            saveButton = new Button("Save");
            saveButton.getElement().setAttribute("theme", "error");
            saveButton.addClickListener(e -> saveClicked());
            saveButton.setIcon(new Icon(VaadinIcon.DATABASE));
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                saveButton.addClickShortcut(Key.ENTER);
            }// end of if cycle
            bottomPlacehorder.add(saveButton);
        }// end of if cycle
    }// end of method


    /**
     * Regola il bottone di 'registra' <br>
     * Chiamato da AViewForm.fixBottomBar() <br>
     * Può essere sovrascritto, per modificare titolo, icona, colore e dimensioni del bottone <br>
     */
    protected void fixDeleteButton() {
        if (usaDeleteButton) {
            deleteButton = new Button("Delete");
            deleteButton.getElement().setAttribute("theme", "error");
//            deleteButton.addClickListener(e -> ritorno()); //@todo provvisorio
            deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                deleteButton.addClickShortcut(Key.ENTER);
            }// end of if cycle
            bottomPlacehorder.add(deleteButton);
        }// end of if cycle
    }// end of method

}// end of class
