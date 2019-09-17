package it.algos.vaadflow.ui.dialog.polymer.route;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
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
@Route(value = "dialogodue")
@Tag("dialogo-due")
@HtmlImport("src/views/dialoghi/dialogo-due.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogoDueRoutePolymer extends DialogoRoutePolymer {


    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
//    @Id("annulla")
    protected Button annulla = new Button();

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
//    @Id("header")
    private Span header = new Span();


    public DialogoDueRoutePolymer() {
        super();
    }// end of constructor


    public DialogoDueRoutePolymer(String bodyText) {
        super(bodyText);
    }// end of constructor

    public DialogoDueRoutePolymer(String headerText, String bodyText) {
        super(headerText, bodyText);
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
