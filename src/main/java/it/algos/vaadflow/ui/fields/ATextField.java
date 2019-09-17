package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 11-mag-2018
 * Time: 16:20
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ATextField extends TextField implements IAField {

    public ATextField() {
        this("");
    }// end of constructor

    public ATextField(String label) {
        this(label, "");
    }// end of constructor

    public ATextField(String label, String placeholder) {
        super(label, placeholder);
    }// end of constructor

    @Override
    public ATextField getField() {
        return this;
    }// end of method

    @Override
    public String getValue() {
        return super.getValue().equals("") ? null : super.getValue();
    }// end of method

    @Override
    public String getValore() {
        return getValue();
    }// end of method

}// end of class
