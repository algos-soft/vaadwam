package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 27-dic-2019
 * Time: 08:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ADateTimePicker extends CustomField<LocalDateTime> {

    private final DatePicker datePicker = new DatePicker();

    private final TimePicker timePicker = new TimePicker();

    private final Duration STEP = Duration.ofMinutes(15);


    /**
     * Costruttore base senza parametri <br>
     */
    public ADateTimePicker() {
        this("Data e tempo");
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     */
    public ADateTimePicker(String caption) {
        setLabel(caption);
        add(datePicker, timePicker);
        this.setStep(STEP);
    }// end of constructor


    @Override
    protected LocalDateTime generateModelValue() {
        final LocalDate date = datePicker.getValue();
        final LocalTime time = timePicker.getValue();

        return date != null && time != null ? LocalDateTime.of(date, time) : null;
    }// end of method


    @Override
    protected void setPresentationValue(LocalDateTime newPresentationValue) {
        datePicker.setValue(newPresentationValue != null ? newPresentationValue.toLocalDate() : null);
        timePicker.setValue(newPresentationValue != null ? newPresentationValue.toLocalTime() : null);
    }// end of method


    public void setStep(Duration step) {
        timePicker.setStep(step);
    }// end of method

}// end of class
