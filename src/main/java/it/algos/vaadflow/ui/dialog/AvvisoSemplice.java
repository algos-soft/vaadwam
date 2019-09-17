package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
public class AvvisoSemplice extends Dialog {


    public AvvisoSemplice() {
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);

        this.add(new Label("Close me with the esc-key or an outside click"));
        this.setWidth("400px");
        this.setHeight("150px");
    }// end of constructor

}// end of class
