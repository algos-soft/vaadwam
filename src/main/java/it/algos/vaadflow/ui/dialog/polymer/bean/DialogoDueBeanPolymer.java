package it.algos.vaadflow.ui.dialog.polymer.bean;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EAColor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 17-ago-2019
 * Time: 06:42
 */
@Tag("dialogo-due")
@HtmlImport("src/views/dialoghi/dialogo-due.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogoDueBeanPolymer extends DialogoBeanPolymer {


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoDueBeanPolymer.class, bodyText) <br>
     * @param bodyText       (obbligatorio) Detail message
     */
    public DialogoDueBeanPolymer(String bodyText) {
        super(bodyText);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoDueBeanPolymer.class, headerText, bodyText) <br>
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     */
    public DialogoDueBeanPolymer(String headerText, String bodyText) {
        super(headerText, bodyText);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoDueBeanPolymer.class, headerText, bodyText, confirmHandler) <br>
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     * @param confirmHandler (opzionale) The confirmation handler function
     */
    public DialogoDueBeanPolymer(String headerText, String bodyText, Runnable confirmHandler) {
        super(headerText, bodyText, confirmHandler);
    }// end of constructor


    /**
     * Costruttore usato da
     * dialogo=appContext.getBean(DialogoDueBeanPolymer.class, headerText, bodyText, confirmHandler, cancelHandler) <br>
     * @param headerText     (opzionale) Title message
     * @param bodyText       (obbligatorio) Detail message
     * @param confirmHandler (opzionale) The confirmation handler function
     * @param cancelHandler  (opzionale) The cancellation handler function
     */
    public DialogoDueBeanPolymer(String headerText, String bodyText, Runnable confirmHandler, Runnable cancelHandler) {
        super(headerText, bodyText, confirmHandler, cancelHandler);
    }// end of constructor


    /**
     * Preferenze standard.
     * Le preferenze vengono eventualmente sovrascritte nella sottoclasse
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

//        super.backgroundColorHeader = EAColor.red;
//        super.backgroundColorBody = EAColor.green;
//        super.backgroundColorFooter = EAColor.pink;

        this.textCancelButton = "Annulla";
        this.textConfirmButton = "Conferma";
    }// end of method


    @Override
    protected void fixHeader() {
        super.fixHeader();
        header.setText(text.primaMaiuscola(headerText));
//        header.getStyle().set("background-color", super.backgroundColorHeader.getTag());
//        header.getStyle().set("background-color", "#f0f0f0");
    }// end of method


    protected void fixFooter() {
        super.fixFooter();
        annulla.setText(textCancelButton);
        annulla.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        annulla.addClickListener(buttonClickEvent -> close());
        annulla.getStyle().set("background-color", EAColor.grigio2.getEsadecimale());
        annulla.getElement().getStyle().set("margin-left", "auto");
    }// end of method


}// end of class
