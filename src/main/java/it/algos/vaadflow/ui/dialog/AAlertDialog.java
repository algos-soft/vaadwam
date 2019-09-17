package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 12-ott-2018
 * Time: 20:32
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AAlertDialog extends ADialog {


    /**
     * Metodo invocato subito DOPO il costruttore.
     * DEVE essere inserito nella sottoclasse e invocare (eventualmente) un metodo della superclasse.
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizializzazione() {
        super.inizia();
    }// end of method

    public AAlertDialog(String title, String message) {
        VerticalLayout vertLayout = new VerticalLayout();
        vertLayout.setSpacing(false);
        vertLayout.setMargin(false);
        HorizontalLayout horizLayout = new HorizontalLayout();
        horizLayout.setSpacing(true);
        horizLayout.setMargin(true);

        Label labelTitle = new Label(title);
        labelTitle.getElement().setAttribute("theme", "primary");
        labelTitle.getElement().getStyle().set("color", "blue");
        labelTitle.getElement().getStyle().set("fontWeight", "bold");
        labelTitle.getElement().getStyle().set("fontSize", "larger");

        vertLayout.add(labelTitle);

        Label labelMessage = new Label(message);
//        labelMessage.getElement().getStyle().set("color", "blue");
        vertLayout.add(labelMessage);

        Button confirmButton = new Button("OK");
        confirmButton.getElement().setAttribute("theme", "primary");
        confirmButton.getElement().getStyle().set("margin-right", "auto");
        confirmButton.getElement().getStyle().set("color", "white");
        confirmButton.addClickListener(event -> this.close());
        horizLayout.add(confirmButton);
        vertLayout.add(horizLayout);
        this.add(vertLayout);

    }// end of constructor

}// end of class
