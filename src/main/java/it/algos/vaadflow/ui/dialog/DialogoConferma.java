package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 15-ago-2019
 * Time: 18:14
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class DialogoConferma extends Dialog {


    public DialogoConferma() {
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        HorizontalLayout layout = new HorizontalLayout();

        this.setWidth("400px");
        this.setHeight("100px");
        this.add(new Label("Sei sicuro di voler procedere?"));

        layout.add(new Button("Annulla", event -> this.close()));
        layout.add(new Button("Conferma", event -> this.close()));
        this.add(layout);
    }// end of constructor

}// end of class
