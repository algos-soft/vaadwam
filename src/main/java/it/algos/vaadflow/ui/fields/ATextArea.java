package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 12-mag-2018
 * Time: 16:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ATextArea extends TextArea implements IAField {

    public ATextArea() {
        this("");
    }// end of constructor

    public ATextArea(String label) {
        this(label,"");
    }// end of constructor

    public ATextArea(String label, String placeholder) {
        super(label, placeholder);
    }// end of constructor

    @Override
    public ATextArea getField() {
        return this;
    }// end of method

    @Override
    public String getValore() {
        return getValue();
    }// end of method

}// end of class
