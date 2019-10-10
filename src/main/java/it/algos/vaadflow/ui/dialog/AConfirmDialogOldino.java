package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 17-gen-2019
 * Time: 22:33
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AConfirmDialogOldino extends ADialog {

    //--Titolo standard, eventualmente modificabile nelle sottoclassi
    private static String TITOLO = "Conferma";

    protected Button deleteButton = new Button(TITOLO);


    /**
     * Costruttore <br>
     */
    public AConfirmDialogOldino() {
        super(TITOLO);
    }// end of constructor


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


    /**
     * Barra dei bottoni
     */
    protected void fixBottomLayout() {
        bottomLayout.setClassName("buttons");
        bottomLayout.setPadding(false);
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(false);
        bottomLayout.setClassName("confirm-dialog-buttons");

        Label spazioVuotoEspandibile = new Label("");
        bottomLayout.add(spazioVuotoEspandibile);
        bottomLayout.setFlexGrow(1, spazioVuotoEspandibile);

        cancelButton.getElement().setAttribute("theme", "secondary");
        cancelButton.addClickListener(e -> cancellaHandler());
        cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        bottomLayout.add(cancelButton);

        confirmButton.setText(textConfirmlButton);
        confirmButton.getElement().setAttribute("theme", "primary");
        confirmButton.addClickListener(e -> confermaHandler());
        confirmButton.setIcon(new Icon(VaadinIcon.CHECK));
        bottomLayout.add(confirmButton);
    }// end of method

}// end of class
