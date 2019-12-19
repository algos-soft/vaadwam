package it.algos.vaadflow.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EAPrefType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 26-mag-2018
 * Time: 14:16
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AConverterIntegerByte implements Converter<String, byte[]> {


    @Override
    public Result<byte[]> convertToModel(String s, ValueContext valueContext) {
        return Result.ok((byte[]) EAPrefType.integer.objectToBytes(s));
    }// end of method

    @Override
    public String convertToPresentation(byte[] bytes, ValueContext valueContext) {
        if (bytes != null) {
            return (String) EAPrefType.integer.bytesToObject(bytes);
        } else {
            return "Vuoto";
        }// end of if/else cycle
    }// end of method

}// end of class
