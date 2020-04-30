package it.algos.vaadwam.jsknife;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Sample polymer to test JS interaction
 */
@JavaScript("frontend://js/js-comm.js")
@JavaScript("frontend://js/jsknife.js")
//@Route(value = "jsknife", layout = AppLayout.class)
@Route(value = "jsknife")
@Tag("jsknife-polymer")
@HtmlImport("src/views/js/jsknife-polymer.html")
//@Tag("simple-polymer")
//@HtmlImport("src/views/js/simple-polymer.html")

@Slf4j
public class JsKnife extends PolymerTemplate<JsKnifeModel>  {

    // links to the Java buttons
    @Id
    private Button bwide_java;
    @Id
    private Button bnarrow_java;
    @Id
    private Button btall_java;
    @Id
    private Button bshort_java;

    @Id
    private Button bcall_func_ret;

    @Id
    private Button bcall_func_ret_err;


    //@Autowired
    public JsKnife() {

        if (true){

            setId("template");

            getModel().setGreeting("Please enter your name");


            // registra il riferimento al server Java nel client JS
            // necessario perché JS possa chiamare direttamente metodi Java
            // è in un file .js separato per renderlo riutilizzabile
            UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

            // inizializza lo script specifico per questa pagina
            // (recupera i componenti nel DOM e li registra in variabili)
            UI.getCurrent().getPage().executeJs("initKnife()");


            // aggiunge i listener ai bottoni Java che invocano le funzioni JS
            bwide_java.addClickListener(e -> {
                UI.getCurrent().getPage().executeJs("wider()");
                log.info("wider() function invoked in JS");
            });
            bnarrow_java.addClickListener(e -> {
                UI.getCurrent().getPage().executeJs("narrower()");
                log.info("narrower() function invoked in JS");
            });
            btall_java.addClickListener(e -> {
                UI.getCurrent().getPage().executeJs("taller()");
                log.info("taller() function invoked in JS");
            });
            bshort_java.addClickListener(e -> {
                UI.getCurrent().getPage().executeJs("shorter()");
                log.info("shorter() function invoked in JS");
            });

            // chiamata a funzione JS con gestione del ritorno
            bcall_func_ret.addClickListener(e -> {

                int n1 = ThreadLocalRandom.current().nextInt(10, 100 + 1);
                int n2 = ThreadLocalRandom.current().nextInt(10, 100 + 1);

                // !!se si vuole ottenere il valore di ritorno bisogna sempre
                // aggiungere 'return' davanti al nome della funzione!!
                PendingJavaScriptResult pResult = UI.getCurrent().getPage().executeJs("return sum($0,$1)",n1, n2);
                SerializableConsumer resultHandler = (SerializableConsumer) objRet -> {
                    Notification.show(n1+"+"+n2+" (Java) = "+objRet+" (JS)");
                };

                // il risultato viene castato alla classe qui indicata. Se omessa usa Json.
                pResult.then(Integer.class, resultHandler);

            });


            // chiamata a funzione JS con gestione del ritorno e dell'errore
            bcall_func_ret_err.addClickListener(e -> {

                PendingJavaScriptResult pResult = UI.getCurrent().getPage().executeJs("return cookEggs()");
                SerializableConsumer resultHandler = (SerializableConsumer) objRet -> {
                    Notification.show(objRet.toString());
                };

                SerializableConsumer<String> errorHandler = (SerializableConsumer) objRet -> {
                    Notification.show("JS error says: "+objRet.toString());
                };

                // il risultato viene castato alla classe qui indicata. Se omessa usa Json.
                pResult.then(String.class, resultHandler, errorHandler);

            });

        }




    }


    /**
     * Invocato dal client JS quando la pagina è completamente caricata e visibile.
     * E' il primo momento in cui si può operare in sicurezza su tutti gli elementi del DOM.
     * Ricordati di registrare il server nel costruttore:
     * UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());
     */
    @ClientCallable
    public void pageReady(){
        // questa interazione col DOM non funzionerebbe prima di aver ricevuto la callback pageReady!!
        UI.getCurrent().getPage().executeJs("scrollTo($0,$1)", 0, 20);
    }


    @EventHandler
    private void sayHello() {
        // Called from the template click handler
        getModel().setGreeting(Optional.ofNullable(getModel().getUserInput())
                .filter(userInput -> !userInput.isEmpty())
                .map(greeting -> String.format("Hello %s!", greeting))
                .orElse("Inserisci il nome e premi il bottone"));
    }




    /**
     * Metodo invocato direttamente da JS che ritorna un valore.
     * Perché funzioni bisogna prima aver registrato il server presso il client
     * UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());
     * Il ritorno di questo metodo deve essere void.
     * Il valore eventualmente si ritorna facendo una callback a un apposito metodo JS.
     */
    @ClientCallable
    public void sum(int n1, int n2){
        int sum = n1+n2;

        // callback
        UI.getCurrent().getPage().executeJs("showResult($0,$1,$2)", n1, n2, sum);

    }


}
