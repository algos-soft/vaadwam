package it.algos.vaadflow.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 14-mar-2020
 * Time: 11:13
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ANullValidator implements Validator {

    private static final long serialVersionUID = 1L;

    private String message = "";


    public ANullValidator() {
    }// end of costructor


    public ANullValidator(String message) {
        this.message = message;
    }// end of costructor


    @Override
    public ValidationResult apply(Object obj, ValueContext valueContext) {
        String testo;

        if (obj != null) {
            return ValidationResult.ok();
        } else {
            if (message.equals("")) {
                return ValidationResult.error("Il contenuto di questo campo non può essere vuoto");
            } else {
                return ValidationResult.error(message);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of methodù


    /**
     * Applies this function to the given arguments.
     *
     * @param o  the first function argument
     * @param o2 the second function argument
     *
     * @return the function result
     */
    @Override
    public Object apply(Object o, Object o2) {
        return null;
    }

}// end of class
