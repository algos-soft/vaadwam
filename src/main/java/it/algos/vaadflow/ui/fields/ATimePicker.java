package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 27-dic-2019
 * Time: 09:58
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ATimePicker extends CustomField<LocalTime> {

    private final TimePicker timePicker = new TimePicker();

    private final Duration STEP = Duration.ofMinutes(15);


    /**
     * Costruttore base senza parametri <br>
     */
    public ATimePicker() {
        this("Tempo");
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     */
    public ATimePicker(String caption) {
        setLabel(caption);
        add(timePicker);
        this.setStep(STEP);
    }// end of constructor


    @Override
    protected LocalTime generateModelValue() {
        return timePicker.getValue();
    }// end of method


    @Override
    protected void setPresentationValue(LocalTime newPresentationValue) {
        timePicker.setValue(newPresentationValue);
    }// end of method


    public void setStep(Duration step) {
        timePicker.setStep(step);
    }// end of method

}// end of class
