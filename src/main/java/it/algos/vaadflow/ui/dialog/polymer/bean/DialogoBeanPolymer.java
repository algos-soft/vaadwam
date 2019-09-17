package it.algos.vaadflow.ui.dialog.polymer.bean;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 12-ott-2018
 * Time: 20:30
 * <p>
 * Dialogo costruito 'al volo' <br>
 * Costruttori ridotti al minimo; con e senza titolo (non obbligatorio) <br>
 * I parametri (variabili) vengono passati nel metodo open() <br>
 * Se si costruisce l'istanza con StaticContextAccessor.getBean(ADialog.class), non si possono passare i Consumer <br>
 * diventa quindi indispensabile usare un metodo successivo 'open()' per questi parametri <br>
 * <p>
 * A confirmation dialog is a simple dialog that asks the user to confirm an action.
 * They are commonly used in situations where triggering an action prematurely would lead to diminished user experience or execute an irreversible application functionality.
 * Typical examples are actions that result in additional work for the end user when triggered by accident like logging out of the application or storing wrong/incomplete user data.
 * To avoid annoying the user, confirmation dialogs should not be overused.
 * Actions that are easily reversible should therefore not require confirmation dialogs.
 * An excellent example of this would be actions that trigger navigation events.
 * <p>
 * Confirmation dialogs usually consist of the same three parts as most other dialogs.
 * A header section, a content, and a footer section.
 * The header contains the title of the dialog and most often a button to close the dialog.
 * The content section typically contains the confirmation question.
 * This can be a simple "Are you sure?" or provide more specifics about the action in question.
 * This section sometimes also contains a summary of the data related to the action.
 * The footer part comprises two buttons, one to confirm and one to cancel the operation.
 * This is an example of what a confirmation dialog might look like:
 * <p>
 * Due to the simplicity of confirmation dialogs, they are highly reusable and should be implemented as such.
 * Only three things change for different confirmation dialogs:
 * 1- The title in the header section
 * 2- The confirmation question in the content section
 * 3- The action that gets triggered once the confirm button in the footer section is pressed.
 * <p>
 * The default constructor shown here creates the three different sections of the dialog and introduces some basic styling for the dialog itself.
 * In this case, this is just setting the width and the height of the dialog.
 * The title, confirmation question and confirm button are class variables which values/actions can be set either by using a second constructor or by setting them directly using public setter methods.
 * <p>
 * In this example, the header is a horizontal layout which contains the title label as well as a close button.
 * To close the dialog simply calls the close() method.
 * This helper method also introduces some styling to the section like setting the background color and then adds the container component to the dialog.
 * The content and footer sections are created the same way.
 * <p>
 * The confirm button also closes the dialog since confirming and action also resolves the confirmation process and the dialog is no longer needed.
 * <p>
 * To attach the dialog to the UI and show it to the user simply calls the open() method of the dialog.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class DialogoBeanPolymer extends PolymerTemplate<DialogoBeanPolymer.DialogoModel> {

    protected final H2 titleField = new H2();

    protected final Div messageLabel = new Div();

    protected final Div extraMessageLabel = new Div();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public ATextService text = ATextService.getInstance();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public AArrayService array = AArrayService.getInstance();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public ADialogoService dialogoService = ADialogoService.getInstance();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public ARouteService routeService = ARouteService.getInstance();

    public Runnable cancelHandler;

    public Runnable confirmHandler;

    /**
     * Flag di preferenza per usare la sezione header. Normalmente true.
     */
    public boolean usaHeader;

    /**
     * Flag di preferenza per usare la sezione body. Normalmente true.
     */
    public boolean usaBody;

    /**
     * Flag di preferenza per usare la sezione footer. Normalmente true.
     */
    public boolean usaFooter;

    /**
     * Flag di preferenza per il testo del bottone Cancel. Normalmente 'Annulla'.
     */
    protected String textCancelButton;

    /**
     * Flag di preferenza per il testo del bottone Confirm. Normalmente 'Conferma'.
     */
    protected String textConfirmButton;

    /**
     * Corpo centrale del Dialog <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected VerticalLayout bodyPlaceHolder = new VerticalLayout();

    /**
     * Barra dei bottoni di comando <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected HorizontalLayout bottomLayout = new HorizontalLayout();

//    protected String message;
//
//    protected String additionalMessage;


    /**
     * Flag di preferenza per il colore di sfondo dell'header. Normalmente EAColor.lightgray.
     */
    protected EAColor backgroundColorHeader;

    /**
     * Flag di preferenza per il colore di sfondo dell'header. Normalmente EAColor.lightgray.
     */
    protected EAColor backgroundColorBody;

    /**
     * Flag di preferenza per il colore di sfondo del footer. Normalmente lightslategray.
     */
    protected EAColor backgroundColorFooter;


    /**
     * Flag di preferenza per la larghezza del dialogo. Normalmente 18em.
     */
    protected String width;

    /**
     * Flag di preferenza per l'altezza di header. Normalmente 8em.
     */
    protected String heightHeader;

    /**
     * Flag di preferenza per l'altezza di body. Normalmente 8em.
     */
    protected String heightBody;

    /**
     * Flag di preferenza per l'altezza di footer. Normalmente 8em.
     */
    protected String heightFooter;


    /**
     * Titolo del dialogo <br>
     */
    protected Label title;

    protected Button cancelButton = new Button(textCancelButton);

    protected Button confirmButton = new Button(textConfirmButton);

    /**
     * Service (@Scope = 'singleton') iniettato dal costruttore @Autowired di Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    protected AVaadinService vaadinService;

    protected ALogin login;

    /**
     * Recuperato dalla sessione, quando Spring costruisce un'istanza di questa classe. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

    protected String headerText;

    protected String bodyText;

    protected String iconaAnnulla;

    protected String iconaConferma;


    /**
     * Questo Component NON viene costruito qui ma viene iniettato da Vaadin <br>
     * Questo Component viene iniettato nel file html SOLO se esiste un componente (compatibile) con lo stesso ID <br>
     * L'ID deve essere esattamente lo stesso <br>
     * La property di questo file Java invece può avere qualsiasi nome <br>
     */
    @Id("dialog")
    protected Dialog dialog;

    /**
     * Component che DEVE essere costruito qui perché altrimenti: <br>
     * Corresponding element was found in a sub template, for which injection is not supported  <br>
     */
    protected Span header = new Span();


    /**
     * Component che DEVE essere costruito qui perché altrimenti: <br>
     * Corresponding element was found in a sub template, for which injection is not supported  <br>
     */
    protected Span body = new Span();

    /**
     * Component che DEVE essere costruito qui perché altrimenti: <br>
     * Corresponding element was found in a sub template, for which injection is not supported  <br>
     */
    protected Button annulla = new Button();

    /**
     * Component che DEVE essere costruito qui perché altrimenti: <br>
     * Corresponding element was found in a sub template, for which injection is not supported  <br>
     */
    protected Button conferma = new Button();


//    public DialogoPolymer(String title, String content, ComponentEventListener listener) {
//        this();
//        setTitle(title);
//        setQuestion(content);
//        addConfirmationListener(listener);
//    }// end of constructor
//
//
//    public DialogoPolymer() {
//        createHeader();
//        createContent();
//        createFooter();
//    }// end of constructor

    private Label question;


    /**
     * Costruttore usato dalla sottoclasse concreta
     * dialogo=appContext.getBean(DialogoXxxBeanPolymer.class, bodyText) <br>
     *
     * @param bodyText (obbligatorio) Detail message
     */
    public DialogoBeanPolymer(String bodyText) {
        this("", bodyText);
    }// end of constructor


    /**
     * Costruttore usato dalla sottoclasse concreta
     * dialogo=appContext.getBean(DialogoXxxBeanPolymer.class, headerText, bodyText) <br>
     *
     * @param headerText (opzionale) Title message
     * @param bodyText   (obbligatorio) Detail message
     */
    public DialogoBeanPolymer(String headerText, String bodyText) {
        this(headerText, bodyText, null, null);
    }// end of constructor


    /**
     * Costruttore usato dalla sottoclasse concreta
     * dialogo=appContext.getBean(DialogoXxxBeanPolymer.class, headerText, bodyText, confirmHandler) <br>
     *
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     * @param confirmHandler (opzionale) The confirmation handler function
     */
    public DialogoBeanPolymer(String headerText, String bodyText, Runnable confirmHandler) {
        this(headerText, bodyText, confirmHandler, null);
    }// end of constructor


    /**
     * Costruttore usato dalla sottoclasse concreta
     * dialogo=appContext.getBean(DialogoXxxBeanPolymer.class, headerText, bodyText, confirmHandler, cancelHandler) <br>
     *
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     * @param confirmHandler (opzionale) The confirmation handler function
     * @param cancelHandler  (opzionale) The cancellation handler function
     */
    public DialogoBeanPolymer(String headerText, String bodyText, Runnable confirmHandler, Runnable cancelHandler) {
        this.headerText = headerText;
        this.bodyText = bodyText;
        this.confirmHandler = confirmHandler;
        this.cancelHandler = cancelHandler;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * L'istanza DEVE essere creata da SpringBoot con Object algos = appContext.getBean(AlgosClass.class);  <br>
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void inizia() {
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        fixPreferenze();
        fixHeader();
        fixBody();
        fixFooter();

        layoutPolymer();
    }// end of method


    /**
     * Preferenze standard.
     * Le preferenze vengono eventualmente sovrascritte nella sottoclasse
     * Invocare PRIMA il metodo della superclasse
     */
    protected void fixPreferenze() {
        this.usaHeader = true;
        this.usaBody = true;
        this.usaFooter = true;

        this.backgroundColorHeader = EAColor.white;
        this.backgroundColorBody = EAColor.white;
        this.backgroundColorFooter = EAColor.white;

        this.width = "30em";
        this.heightHeader = "3em";
        this.heightBody = "6em";
        this.heightFooter = "4em";

        this.textCancelButton = "Annulla";
        this.textConfirmButton = "Conferma";

        this.iconaAnnulla = "icons:close";
        this.iconaConferma = "icons:check";
    }// end of method


    /**
     * Costruisce la pagina <br>
     */
    protected void layoutPolymer() {
        getModel().setBackgroundColorHeader(backgroundColorHeader.getEsadecimale());
        getModel().setBackgroundColorBody(backgroundColorBody.getEsadecimale());
        getModel().setBackgroundColorFooter(backgroundColorFooter.getEsadecimale());
        getModel().setWidth(width);
        getModel().setHeightHeader(heightHeader);
        getModel().setHeightBody(heightBody);
        getModel().setHeightFooter(heightFooter);
        getModel().setHeaderText(headerText);
        getModel().setBodyText(bodyText);
        getModel().setTextCancelButton(textCancelButton);
        getModel().setTextConfirmButton(textConfirmButton);
        getModel().setIconaAnnulla(iconaAnnulla);
        getModel().setIconaConferma(iconaConferma);
        dialog.addDialogCloseActionListener(e -> close());
        dialog.open();
    }// end of method


    protected void fixHeader() {
    }// end of method


    protected void fixBody() {
        body.setText(bodyText);
        body.getStyle().set("background-color", backgroundColorBody.getEsadecimale());
    }// end of method


    protected void fixFooter() {
        conferma.setText(textConfirmButton);
        conferma.setIcon(new Icon(VaadinIcon.CHECK));
        conferma.getElement().getStyle().set("margin-right", "auto");
        conferma.addClickListener(buttonClickEvent -> close());
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     */
    @EventHandler
    public void handleClickAnnulla() {
        if (cancelHandler != null) {
            cancelHandler.run();
        }// end of if cycle
        dialog.close();
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     */
    @EventHandler
    public void handleClickConferma() {
        if (confirmHandler != null) {
            confirmHandler.run();
        }// end of if cycle
        dialog.close();
    }// end of method


    protected void close() {
        dialog.close();
    }// end of method


    /**
     * Modello dati per collegare questa classe java col polymer
     */
    public interface DialogoModel extends TemplateModel {

        void setIconaAnnulla(String iconaAnnulla);

        void setIconaConferma(String iconaConferma);

        void setHeaderText(String headerText);

        void setBodyText(String bodyText);

        void setWidth(String width);

        void setHeightHeader(String heightHeader);

        void setHeightBody(String heightBody);

        void setHeightFooter(String heightFooter);

        void setBackgroundColorHeader(String backgroundColorHeader);

        void setBackgroundColorBody(String backgroundColorBody);

        void setBackgroundColorFooter(String backgroundColorFooter);

        void setTextCancelButton(String textCancelButton);

        void setTextConfirmButton(String textConfirmButton);

    }// end of interface

}// end of class
