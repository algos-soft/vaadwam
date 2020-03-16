package it.algos.vaadflow.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 07-giu-2018
 * Time: 16:28
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AStringNullValidator implements Validator {
    private static final long serialVersionUID = 1L;
    private String message = "";

    public AStringNullValidator() {
    }// end of costructor

    public AStringNullValidator(String message) {
        this.message = message;
    }// end of costructor

    @Override
    public ValidationResult apply(Object obj, ValueContext valueContext) {
        String testo;

        if (obj instanceof String) {
            testo = (String) obj;
            if (testo.length() == 0) {
                if (message.equals("")) {
                    return ValidationResult.error("Il contenuto di questo campo non può essere vuoto");
                } else {
                    return ValidationResult.error(message);
                }// end of if/else cycle
            } else {
                return ValidationResult.ok();
            }// end of if/else cycle
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
