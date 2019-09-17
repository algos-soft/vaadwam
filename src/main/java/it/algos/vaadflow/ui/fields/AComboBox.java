package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 12-mag-2018
 * Time: 20:44
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AComboBox<T> extends ComboBox implements IAField {

    public AComboBox() {
        this("");
    }// end of constructor

    public AComboBox(String label) {
        super(label);
    }// end of constructor

    public AComboBox(String label, List items) {
        super(label, items);
    }// end of constructor

    @Override
    public AComboBox getField() {
        return null;
    }// end of method

    @Override
    public Object getEmptyValue() {
        Object obj = super.getEmptyValue();
        return super.getEmptyValue();
    }// end of method

    @Override
    public Object getValue() {
        Object obj = super.getValue();
        return super.getValue();
    }// end of method

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }// end of method

    @Override
    public String getValore() {
        return getValue() != null ? getValue().toString() : "";
    }// end of method

}// end of class
