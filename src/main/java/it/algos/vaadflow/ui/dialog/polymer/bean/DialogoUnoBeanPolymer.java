package it.algos.vaadflow.ui.dialog.polymer.bean;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;


/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 16-ago-2019
 * Time: 15:09
 */
@Tag("dialogo-uno")
@HtmlImport("src/views/dialoghi/dialogo-uno.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogoUnoBeanPolymer extends DialogoBeanPolymer {


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoUnoBeanPolymer.class, bodyText) <br>
     *
     * @param bodyText (obbligatorio) Detail message
     */
    public DialogoUnoBeanPolymer(String bodyText) {
        super(bodyText);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoUnoBeanPolymer.class, headerText, bodyText) <br>
     *
     * @param headerText (opzionale) Title message
     * @param bodyText   (obbligatorio) Detail message
     */
    public DialogoUnoBeanPolymer(String headerText, String bodyText) {
        super(headerText, bodyText);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoUnoBeanPolymer.class, headerText, bodyText, confirmHandler) <br>
     *
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     * @param confirmHandler (opzionale) The confirmation handler function
     */
    public DialogoUnoBeanPolymer(String headerText, String bodyText, Runnable confirmHandler) {
        super(headerText, bodyText, confirmHandler);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoUnoBeanPolymer.class, headerText, bodyText, confirmHandler, cancelHandler) <br>
     *
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     * @param confirmHandler (opzionale) The confirmation handler function
     * @param cancelHandler  (opzionale) The cancellation handler function
     */
    public DialogoUnoBeanPolymer(String headerText, String bodyText, Runnable confirmHandler, Runnable cancelHandler) {
        super(headerText, bodyText, confirmHandler, cancelHandler);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoUnoBeanPolymer.class, headerText, bodyText) <br>
     *
     * @param headerText   (opzionale) Title message
     * @param bodyText     (obbligatorio) Detail message
     * @param iniziaSubito per rendere immediatamente visibile il dialogo, oppure per aspettare a costruirlo dopo aver regolato alcuni parametri
     */
    public DialogoUnoBeanPolymer(String headerText, String bodyText, boolean iniziaSubito) {
        super(headerText, bodyText, iniziaSubito);
    }// end of constructor


    /**
     * Preferenze standard.
     * Le preferenze vengono eventualmente sovrascritte nella sottoclasse
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.heightHeader = "2em";
        super.heightBody = "4em";
        super.heightFooter = "2em";

        this.textConfirmButton = "OK";
    }// end of method


//    /**
//     * Java event handler on the server, run asynchronously <br>
//     * <p>
//     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
//     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
//     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
//     */
//    @EventHandler
//    @Override
//    public void handleClickConferma() {
//        close();
//    }// end of method

}// end of class
