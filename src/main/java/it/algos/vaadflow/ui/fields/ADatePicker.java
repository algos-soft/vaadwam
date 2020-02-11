package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 13-lug-2018
 * Time: 18:10
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ADatePicker extends CustomField<LocalDate> {

    private final DatePicker datePicker = new DatePicker();


    /**
     * Costruttore base senza parametri <br>
     */
    public ADatePicker() {
        this("Data");
    }// end of constructor



    /**
     * Costruttore con parametri <br>
     */
    public ADatePicker(String caption) {
        setLabel(caption);
        add(datePicker);
    }// end of constructor


    @Override
    protected LocalDate generateModelValue() {
        return datePicker.getValue();
    }// end of method


    @Override
    protected void setPresentationValue(LocalDate newPresentationValue) {
        datePicker.setValue(newPresentationValue);
    }// end of method


}// end of class
