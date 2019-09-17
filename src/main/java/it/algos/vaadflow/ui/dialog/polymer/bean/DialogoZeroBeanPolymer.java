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
 * Time: 07:15
 * <p>
 * Java wrapper of the polymer element `dialogo-zero`
 */
@Tag("dialogo-zero")
@HtmlImport("src/views/dialoghi/dialogo-zero.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogoZeroBeanPolymer extends DialogoBeanPolymer {



    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoZeroBeanPolymer.class, bodyText) <br>
     * @param bodyText       (obbligatorio) Detail message
     */
    public DialogoZeroBeanPolymer(String bodyText) {
        super(bodyText);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoZeroBeanPolymer.class, headerText, bodyText) <br>
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     */
    public DialogoZeroBeanPolymer(String headerText, String bodyText) {
        super(headerText, bodyText);
    }// end of constructor

}// end of class
