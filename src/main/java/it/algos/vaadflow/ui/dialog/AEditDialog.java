package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.ui.fields.ATextField;
import lombok.extern.slf4j.Slf4j;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 12-ott-2018
 * Time: 22:44
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AEditDialog extends ADialog {


    private String value;

    public AEditDialog(String title, String message, String caption) {
        super.setCloseOnEsc(false);
        super.setCloseOnOutsideClick(false);
        VerticalLayout vertLayout = new VerticalLayout();
        vertLayout.setSpacing(true);
        vertLayout.setMargin(false);
        HorizontalLayout horizLayout = new HorizontalLayout();
        horizLayout.setSpacing(true);
        horizLayout.setMargin(false);


        Label labelTitle = new Label(title);
        labelTitle.getElement().setAttribute("theme", "primary");
        labelTitle.getElement().getStyle().set("color", "blue");
        labelTitle.getElement().getStyle().set("fontWeight", "bold");
        labelTitle.getElement().getStyle().set("fontSize", "larger");
        vertLayout.add(labelTitle);

        ATextField textField = new ATextField(caption);
        vertLayout.add(textField);

        Button backButton = new Button("Annulla");
        backButton.getElement().setAttribute("theme", "secondary");
        backButton.getElement().getStyle().set("margin-left", "auto");
//        backButton.getElement().getStyle().set("color", "white");
        backButton.addClickListener(event -> this.close());
        horizLayout.add(backButton);

        Button confirmButton = new Button("Conferma");
        confirmButton.getElement().setAttribute("theme", "primary");
        confirmButton.getElement().getStyle().set("margin-right", "auto");
//        confirmButton.getElement().getStyle().set("color", "white");
        confirmButton.addClickListener(event -> {
            value = confirmButton.getText();
            this.close();
        });
        horizLayout.add(confirmButton);

        vertLayout.add(horizLayout);
        this.add(vertLayout);

    }// end of constructor

    public String getValue() {
        return value;
    }// end of method

}// end of class
