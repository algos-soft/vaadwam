package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 28-mag-2018
 * Time: 08:37
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ACheckBox extends Checkbox implements IAField{

    public ACheckBox(String labelText) {
        super(labelText);
    }

    public ACheckBox(boolean initialValue) {
        super(initialValue);
    }

    public ACheckBox(String labelText, boolean initialValue) {
        super(labelText, initialValue);
    }

    @Override
    public AbstractField getField() {
        return this;
    }// end of method

    @Override
    public Object getValore() {
        return null;
    }// end of method

}// end of class
