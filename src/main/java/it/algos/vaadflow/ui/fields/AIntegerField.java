package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ATextService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 11-mag-2018
 * Time: 16:28
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AIntegerField extends TextField implements IAField {


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();


    public AIntegerField() {
        this("");
    }// end of constructor


    public AIntegerField(String label) {
        this(label, "");
    }// end of constructor


    public AIntegerField(String label, String placeholder) {
        super(label, placeholder);
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    public void qualsiasiFirma() {
        this.setPattern("[0-9]*");
        this.setPreventInvalidInput(true);
        this.setSuffixComponent(new Span("€"));
    }// end of method


    @Override
    public AIntegerField getField() {
        return this;
    }// end of method


    @Override
    public Integer getValore() {
        String textValue = getValue();

        if (text.isValid(textValue)) {
            return Integer.decode(textValue);
        } else {
            return new Integer(0);
        }// end of if/else cycle

    }// end of method

}// end of class
