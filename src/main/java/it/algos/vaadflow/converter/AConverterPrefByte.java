package it.algos.vaadflow.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 27-mag-2018
 * Time: 14:11
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AConverterPrefByte implements Converter<String, byte[]> {

    private EAPrefType type;

    @Override
    public Result<byte[]> convertToModel(String stringValue, ValueContext valueContext) {
         if (type != null) {
            return Result.ok((byte[]) type.objectToBytes(stringValue));
        } else {
            return Result.ok((byte[]) null);
        }// end of if/else cycle
    }// end of method

    @Override
    public String convertToPresentation(byte[] bytes, ValueContext valueContext) {
        String stringValue = "";
        Object genericValue = null;

        if (type != null && bytes != null) {
            genericValue = type.bytesToObject(bytes);

            if (genericValue instanceof String) {
                stringValue = (String) genericValue;
            }// end of if cycle

            if (genericValue instanceof Integer) {
                stringValue = ((Integer) genericValue).toString();
            }// end of if cycle

            if (genericValue instanceof Boolean) {
                stringValue =  genericValue.toString();
            }// end of if cycle
        }// end of if cycle

        return stringValue;
    }// end of method

    public EAPrefType getType() {
        return type;
    }// end of method

    public void setType(EAPrefType type) {
        this.type = type;
    }// end of method

}// end of class
