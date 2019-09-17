package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 11-mag-2018
 * Time: 16:10
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AField<C extends AbstractField<C, T>, T> extends AbstractField<C, T> implements IAField {

    public AField(T defaultValue) {
        super(defaultValue);
    }// end of Spring constructor

    public AField(Element element, T defaultValue) {
        super(element, defaultValue);
    }// end of Spring constructor

    @Override
    protected void setPresentationValue(Object o) {
    }// end of method

}// end of class
