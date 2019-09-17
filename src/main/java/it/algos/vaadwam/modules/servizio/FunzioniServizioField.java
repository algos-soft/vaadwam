package it.algos.vaadwam.modules.servizio;

import com.vaadin.flow.component.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 22-set-2018
 * Time: 17:13
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@Tag("input")
public class FunzioniServizioField extends Component implements HasEnabled, HasValue, HasComponents, Focusable {


    public FunzioniServizioField() {
    }

    public void regolaValore(String value) {
        getElement().setProperty("value", value);
    }

    @Synchronize("change")
    public String getValue() {
        return getElement().getProperty("value");
    }

    @Override
    public void setValue(Object o) {

    }

    public void setValue(String value) {
        getElement().setProperty("value", value);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener valueChangeListener) {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setReadOnly(boolean b) {

    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean b) {

    }
}// end of class
