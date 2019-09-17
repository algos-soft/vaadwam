package it.algos.vaadflow.ui.dialog.polymer.route;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.KEY_MAPPA_BODY;
import static it.algos.vaadflow.application.FlowCost.KEY_MAPPA_HEADER;

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
public class DialogoRoutePolymer extends PolymerTemplate<DialogoRoutePolymer.DialogoModel> implements HasUrlParameter<String> {

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
     * Quasto Component viene iniettato nel file html SOLO se esiste un componente (compatibile) con lo stesso ID <br>
     * L'ID deve essere esattamente lo stesso <br>
     * La property di questo file Java invece può avere qualsiasi nome <br>
     */
    @Id("dialog")
    protected Dialog dialog;


    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
//    @Id("body")
    protected Span body = new Span();

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
//    @Id("conferma")
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


    public DialogoRoutePolymer() {
    }// end of constructor


    public DialogoRoutePolymer(String bodyText) {
        this("", bodyText);
    }// end of constructor


    public DialogoRoutePolymer(String headerText, String bodyText) {
        this.headerText = headerText;
        this.bodyText = bodyText;
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
     * Può essere costruito via broser <br>
     * Recupera i parametri ricevuti dal router di Vaadin (via broser) <br>
     *
     * @param event        con la Location, segments, target, source, ecc
     * @param bodyTextUTF8 da decodificare
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String bodyTextUTF8) {
        ARouteService.Parametro parametro = routeService.estraeParametri(event, bodyTextUTF8);
        if (text.isValid(parametro.getSingleParameter())) {
            this.bodyText = parametro.getSingleParameter();
        } else {
            if (array.isValid(parametro.getParametersMap())) {
                this.headerText = parametro.getParametersMap().get(KEY_MAPPA_HEADER);
                this.bodyText = parametro.getParametersMap().get(KEY_MAPPA_BODY);
            }// end of if cycle
        }// end of if/else cycle

        fixHeader();
        fixBody();
        fixFooter();

        layoutPolymer();
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
//        dialog.addDialogCloseActionListener(e -> close());
        dialog.open();
    }// end of method


//    private void createHeader() {
//        this.title = new Label();
//        Button close = new Button();
//        close.setIcon(VaadinIcon.CLOSE.create());
////        close.addClickListener(buttonClickEvent -> close());//@todo LEVATO
//
//        HorizontalLayout header = new HorizontalLayout();
//        header.add(this.title, close);
//        header.setFlexGrow(1, this.title);
//        header.setAlignItems(FlexComponent.Alignment.CENTER);
//        header.getStyle().set("background-color", backgroundColorHeader.getEsadecimale());
////        add(header);//@todo LEVATO
//    }// end of method


    protected void fixHeader() {
    }// end of method


    protected void fixBody() {
        body.setText(bodyText);
//        body.getStyle().set("background-color", this.backgroundColorBody.getTag());
        body.getStyle().set("background-color", backgroundColorBody.getEsadecimale());
    }// end of method


    protected void fixFooter() {
        conferma.setText(textConfirmButton);
        conferma.setIcon(new Icon(VaadinIcon.CHECK));
        conferma.getElement().getStyle().set("margin-right", "auto");
        conferma.addClickListener(buttonClickEvent -> close());
    }// end of method


//    public void confermaHandler() {
//        if (confirmHandler != null) {
//            confirmHandler.run();
//        }// end of if cycle
////        close();//@todo LEVATO
//    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     */
    @EventHandler
    public void handleClickAnnulla() {
        this.close();
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
        this.close();
    }// end of method


    protected void close() {
        History history = UI.getCurrent().getPage().getHistory();
        history.back();
    }// end of method


    private void createContent() {
        question = new Label();

        VerticalLayout content = new VerticalLayout();
        content.add(question);
        content.setPadding(false);
        content.getStyle().set("background-color", EAColor.lightskyblue.getTag());
//        add(content);//@todo LEVATO
    }// end of method


    private void createFooter() {
        Button abort = new Button("Abort");
//        abort.addClickListener(buttonClickEvent -> close());//@todo LEVATO
//        confirm = new Button("Confirm");
//        confirm.addClickListener(buttonClickEvent -> close());//@todo LEVATO

//        HorizontalLayout footer = new HorizontalLayout();
//        footer.add(abort, confirm);
//        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
//        footer.getStyle().set("background-color", EAColor.lightgray.getTag());
//        add(footer);//@todo LEVATO
    }// end of method


//    public Registration addConfermaListener(ComponentEventListener listener) {
//        return confirma.addClickListener(e -> listener.onComponentEvent(null));
//    }// end of method


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
//    @PostConstruct
    protected void inizia2() {
//        this.setCloseOnEsc(false);//@todo LEVATO
//        this.setCloseOnOutsideClick(false);//@todo LEVATO
        this.getElement().getClassList().add("confirm-dialog");

        //--Login and context della sessione
        context = vaadinService.getSessionContext();
        login = context != null ? context.getLogin() : null;

        //--preferenze standard. Possono essere modificate anche selezionando la firma di open(...)
        fixPreferenze();

        //--Titolo placeholder del dialogo
//        this.add(titleField);//@todo LEVATO

        //--Corpo centrale del Dialog
//        this.add(bodyPlaceHolder);//@todo LEVATO

        //--spazio per distanziare i bottoni sottostanti
//        this.add(new H3());//@todo LEVATO

        //--Barra placeholder dei bottoni, creati e regolati
//        this.add(bottomLayout);//@todo LEVATO

        this.creaTitleLayout();
    }// end of method


    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected void creaTitleLayout() {
        if (text.isValid(title)) {
//            titleField.setText(title);
        }// end of if cycle
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     */
    public void open() {
//        this.usaCancelButton = false;
        this.open("", "", (Runnable) null, (Runnable) null);
    }// end of method


//    /**
//     * Apre un dialogo di 'avviso' <br>
//     * Il title è già stato regolato dal costruttore <br>
//     *
//     * @param message Detail message
//     */
//    public void open(String message) {
//        this.usaCancelButton = false;
//        this.open(message, "", (Runnable) null, (Runnable) null);
//    }// end of method


//    /**
//     * Apre un dialogo di 'avviso' <br>
//     * Il title è già stato regolato dal costruttore <br>
//     *
//     * @param message           Detail message
//     * @param additionalMessage Additional message (optional, may be empty)
//     */
//    public void open(String message, String additionalMessage) {
//        this.usaCancelButton = false;
//        this.open(message, additionalMessage, (Runnable) null, (Runnable) null);
//    }// end of method


//    /**
//     * Apre un dialogo di 'avviso' <br>
//     * Il title è già stato regolato dal costruttore <br>
//     *
//     * @param message        Detail message
//     * @param confirmHandler The confirmation handler function
//     */
//    public void open(String message, Runnable confirmHandler) {
//        this.usaCancelButton = false;
//        this.open(message, "", confirmHandler, (Runnable) null);
//    }// end of method


//    /**
//     * Apre un dialogo di 'avviso' <br>
//     * Il title è già stato regolato dal costruttore <br>
//     *
//     * @param message           Detail message
//     * @param additionalMessage Additional message (optional, may be empty)
//     * @param confirmHandler    The confirmation handler function
//     */
//    public void open(String message, String additionalMessage, Runnable confirmHandler) {
//        this.usaCancelButton = false;
//        this.open(message, additionalMessage, confirmHandler, (Runnable) null);
//    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message        Detail message
     * @param confirmHandler The confirmation handler function
     * @param cancelHandler  The cancellation handler function
     */
    public void open(String message, Runnable confirmHandler, Runnable cancelHandler) {
        this.open(message, "", confirmHandler, cancelHandler);
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     * @param confirmHandler    The confirmation handler function
     * @param cancelHandler     The cancellation handler function
     */
    public void open(String message, String additionalMessage, Runnable confirmHandler, Runnable cancelHandler) {
        this.confirmHandler = confirmHandler;
        this.cancelHandler = cancelHandler;

        //--Body placeholder
        this.fixBodyLayout(message, additionalMessage);

//        //--Barra placeholder dei bottoni, creati e regolati
//        this.fixBottomLayout();

//        super.open();//@todo LEVATO
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param bodyLayout     contenuto del dialogo
     * @param confirmHandler The confirmation handler function
     * @param cancelHandler  The cancellation handler function
     */
    public void open(VerticalLayout bodyLayout, Runnable confirmHandler, Runnable cancelHandler) {
        this.confirmHandler = confirmHandler;
        this.cancelHandler = cancelHandler;

        //--Body placeholder
        this.fixBodyLayout(bodyLayout);

//        //--Barra placeholder dei bottoni, creati e regolati
//        this.fixBottomLayout();

//        super.open();//@todo LEVATO
    }// end of method


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     */
    protected void fixBodyLayout(String message, String additionalMessage) {
        bodyPlaceHolder.setPadding(false);
        bodyPlaceHolder.setSpacing(true);
        bodyPlaceHolder.setMargin(false);
        VerticalLayout bodyLayout = new VerticalLayout();
        bodyLayout.setPadding(false);
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(false);
        bodyPlaceHolder.removeAll();

        if (text.isValid(message)) {
            messageLabel.setText(message);
            bodyLayout.add(messageLabel);
        }// end of if cycle
        if (text.isValid(additionalMessage)) {
            extraMessageLabel.setText(additionalMessage);
            bodyLayout.add(extraMessageLabel);
        }// end of if cycle

        bodyPlaceHolder.add(bodyLayout);
    }// end of method


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     *
     * @param bodyLayout contenuto del dialogo
     */
    protected void fixBodyLayout(VerticalLayout bodyLayout) {
        bodyPlaceHolder.removeAll();
        bodyPlaceHolder.add(bodyLayout);
    }// end of method


//    /**
//     * Barra dei bottoni
//     */
//    protected void fixBottomLayout() {
//        bottomLayout.setClassName("buttons");
//        bottomLayout.setPadding(false);
//        bottomLayout.setSpacing(true);
//        bottomLayout.setMargin(false);
//        bottomLayout.setClassName("confirm-dialog-buttons");
//
//        Label spazioVuotoEspandibile = new Label("");
//        bottomLayout.add(spazioVuotoEspandibile);
//        bottomLayout.setFlexGrow(1, spazioVuotoEspandibile);
//
//        if (usaCancelButton) {
//            cancelButton.setText(textCancelButton);
//            cancelButton.getElement().setAttribute("theme", "primary");
//            cancelButton.addClickListener(e -> cancellaHandler());
//            cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
//            bottomLayout.add(cancelButton);
//        }// end of if cycle
//
//        if (usaConfirmButton) {
//            confirmButton.setText(textConfirmlButton);
//            if (usaCancelButton) {
//                confirmButton.getElement().setAttribute("theme", "secondary");
//            } else {
//                confirmButton.getElement().setAttribute("theme", "primary");
//            }// end of if/else cycle
//            confirmButton.addClickListener(e -> confermaHandler());
//            confirmButton.setIcon(new Icon(VaadinIcon.CHECK));
//            bottomLayout.add(confirmButton);
//        }// end of if cycle
//        bottomLayout.setAlignItems(FlexComponent.Alignment.END);
//    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     */
//    @Override//@todo LEVATO
    public void open(AEntity item, EAOperation operation, AContext context) {
    }


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     * @param title     of the window dialog
     */
//    @Override//@todo LEVATO
    public void open(AEntity item, EAOperation operation, AContext context, String title) {
    }


//    /**
//     * Azione proveniente dal click sul bottone Confirm (delete)
//     */
//    private void confirmClicked() {
//        close();
//    }// end of method


//    public void parte(String title, String message, String additionalMessage, Runnable pippo) {
//        this.pippo = pippo;
//        Button edit = new Button("Prova", event -> pippo.run());
//        this.add(edit);
//
//        super.open();
//    }// end of method


    public void setTitle(String title) {
        this.title.setText(title);
    }


    public void setQuestion(String question) {
        this.question.setText(question);
    }


//    public void addConfirmationListener(ComponentEventListener listener) {
//        confirm.addClickListener(listener);
//    }


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
