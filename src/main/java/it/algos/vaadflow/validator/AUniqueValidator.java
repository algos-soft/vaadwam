package it.algos.vaadflow.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Apr-2019
 * Time: 09:09
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AUniqueValidator extends AbstractValidator<String> {

    private final static String MESSAGE = " deve essere unico e questo valore esiste già";

    /**
     * Inietta da Spring
     */
    @Autowired
    public AMongoService mongo;

    private Class<? extends AEntity> clazz;
    private String propertyName;
    private String captionName;


    public AUniqueValidator() {
        super(MESSAGE);
    }// end of constructor


    public AUniqueValidator(Class<? extends AEntity> clazz, String propertyName, String captionName) {
        super(MESSAGE);
        this.clazz = clazz;
        this.propertyName = propertyName;
        this.captionName = captionName;
    }// end of constructor


    @Override
    public ValidationResult apply(String value, ValueContext valueContext) {

        if (mongo.isEsisteByProperty(clazz, propertyName, value)) {
            return ValidationResult.error(captionName + MESSAGE);
        } else {
            return ValidationResult.ok();
        }// end of if/else cycle

    }// end of method

}// end of class
