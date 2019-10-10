package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.Serializable;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 10-dic-2018
 * Time: 17:08
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ADeleteDialog<T extends Serializable> extends ADialog {

    //--Titolo standard, eventualmente modificabile nelle sottoclassi
    private static String TITOLO = "Delete";

    private static String message = "Vuoi veramente cancellare questo elemento ?";

    private static String additionalMessage = "L'operazione non è reversibile";

    protected Button deleteButton = new Button(TITOLO);


    /**
     * Costruttore <br>
     */
    public ADeleteDialog() {
        super(TITOLO);
    }// end of constructor


    /**
     * Costruttore <br>
     */
    public ADeleteDialog(String entityName) {
        super(TITOLO + " "+entityName);
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

        cancelButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickListener(e -> cancellaHandler());
        cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            cancelButton.addClickShortcut(Key.ARROW_LEFT);
        }// end of if cycle
        bottomLayout.add(cancelButton);

        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.addClickListener(e -> confermaHandler());
        deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        bottomLayout.add(deleteButton);
    }// end of method


    /**
     * Apre il dialogo <br>
     *
     * @param deleteHandler The confirmation handler function for deleting entities
     */
    public void open(Runnable deleteHandler) {
        this.usaCancelButton = true;
        this.confirmHandler = deleteHandler;

        //--Body placeholder
        this.fixBodyLayout(message, additionalMessage);

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();
    }// end of method


}// end of class
