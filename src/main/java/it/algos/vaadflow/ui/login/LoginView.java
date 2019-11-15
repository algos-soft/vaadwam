package it.algos.vaadflow.ui.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.application.FlowVar;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 15-nov-2019
 * Time: 15:05
 */
@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    //--property che DEVE essere unica per tutto il programma
    public static final String ROUTE = "login";

    //--componente di Vaadin flow invocato dall'Annotation @Tag("sa-login-view")
    private LoginOverlay login = new LoginOverlay();

    //--componente per la gestione degli avvisi informativi e per gli errori
    private LoginI18n i18n;


    /**
     * Costruttore base senza parametri <br>
     * Non usato <br>
     * Performing the initialization in a constructor is not suggested <br>
     * as the state of the UI is not properly set up when the constructor is invoked <br>
     */
    public LoginView() {
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void postConstruct() {
        login.setAction("login");
        login.setOpened(true);
        login.setForgotPasswordButtonVisible(false);
        getElement().appendChild(login.getElement());

        creaHeader();
        creaErrorAndMessage();
    }// end of method


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // inform the user about an authentication error
        // (yes, the API for resolving query parameters is annoying...)
        if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
            login.setError(true);
        }// end of if cycle
    }// end of method


    /**
     * Crea il titolo esplicativo dell'applicazione <br>
     */
    private void creaHeader() {
        login.setTitle(FlowVar.projectName.toUpperCase());
        login.setDescription(FlowVar.projectBanner);
    }// end of method


    /**
     * Crea un'istanza per la visualizzazione di messaggi: di avviso e/o di errore <br>
     * L'oggetto LoginI18n viene creato con un messaggio di errore 'standard' e senza messaggi di avviso <br>
     */
    private void creaErrorAndMessage() {
        i18n = LoginI18n.createDefault();
        addMessage();
        fixError();
        login.setI18n(i18n);
    }// end of method


    /**
     * Aggiunge un messaggio di avviso <br>
     */
    private void addMessage() {
//        i18n.setAdditionalInformation("To close the login form submit non-empty username and password");
        i18n.setAdditionalInformation("Per chiudere la scheda di login inserisci un valido username e una password");
    }// end of method


    /**
     * Personalizza il messaggio di errore che sovrascrive quello 'standard' <br>
     */
    private void fixError() {
        LoginI18n.ErrorMessage errore = new LoginI18n.ErrorMessage();
        errore.setTitle("Riprova");
        errore.setMessage("Username o password non corretti");
        i18n.setErrorMessage(errore);
    }// end of method

}// end of class
