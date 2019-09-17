package it.algos.vaadwam;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.GeneratedVaadinTextField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.ui.fields.AField;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadflow.ui.fields.IAField;
import it.algos.vaadwam.modules.croce.Croce;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.StringUtils;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 05-set-2018
 * Time: 16:49
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@Tag("mario")
public class ALinkField extends ATextField  {

    private String value;

    public ALinkField() {
        super("Forse");
    }

//    @Override
//    public AbstractField getField() {
//        return null;
//    }
//
//    @Override
//    protected void setPresentationValue(Object o) {
//        String alfa="Mariolino";
//        Label label=new Label("Doppo");
//        super.setPresentationValue(label);
//    }
//
//    @Override
//    public void setValue(Object value) {
//        Label label=new Label("Adex");
//        super.setValue(label);
//    }
//
//    @Override
//    public Object getValue() {
//        return super.getValue();
//    }


    //    @Override
//    public void setValue(String value) {
//        super.setValue(value);
//    }
//
//    @Override
//    public String getValue() {
//        return super.getValue();
//    }

//    @Override
//    public Registration addValueChangeListener(ValueChangeListener valueChangeListener) {
//        return null;
//    }


    //    @Override
//    public void setEnabled(boolean enabled) {
//
//    }

//    @Override
//    public void setValue(Object value) {
//        super.setValue(value);
//    }

//    @Override
//    public Object getValue() {
//        return super.getValue();
//    }

//    public ALinkField() {
//        this("");
//    }// end of constructor
//
//    public ALinkField(String label) {
//        super();
//    }// end of constructor
//
//    @Override
//    public void setValue(Object value) {
//        super.setValue(value == null ? "" : value);
//    }
//    @Override
//    public String getValue() {
//        String value = super.getValue();
//        return StringUtils.isEmpty(value) ? null : value;
//    }
//
////    @Override
////    public void setValue(Object value) {
////        super.setValue("mario");
////    }
////
////    @Override
////    public Object getValue() {
////        return super.getValue();
////    }
//
////    @Override
////    protected void setPresentationValue(Object o) {
////int a=87;
////    }
}// end of class
