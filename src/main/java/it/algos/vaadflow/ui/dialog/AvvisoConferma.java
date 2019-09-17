package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 15-ago-2019
 * Time: 17:57
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AvvisoConferma extends Dialog {


    public AvvisoConferma() {
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);

        this.setWidth("400px");
        this.setHeight("100px");
        this.add(new Label("Premi il bottone per chiuderlo, dopo averlo letto"));
        this.add(new Button("Cancel", event -> this.close()));
    }// end of constructor

}// end of class
