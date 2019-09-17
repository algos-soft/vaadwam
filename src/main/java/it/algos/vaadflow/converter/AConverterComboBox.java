package it.algos.vaadflow.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Fri, 19-Jul-2019
 * Time: 14:05
 */
public class AConverterComboBox implements Converter<Set, List> {


    @Override
    public Set convertToPresentation(List lista, ValueContext context) {
        // Converting to the field type should always succeed,
        // so there is no support for returning an error Result.

        Set set = new HashSet<>();
        if (lista != null) {

            for (Object funz : lista) {
                set.add(funz);
            }// end of for cycle
        }// end of if cycle

        return set;
    }// end of method


    @Override
    public Result<List> convertToModel(Set fieldValue, ValueContext context) {
        // Produces a converted value or an error
        try {
            // ok is a static helper method that creates a Result
            return Result.ok(new ArrayList(fieldValue));
        } catch (NumberFormatException e) {
            // error is a static helper method that creates a Result
            return Result.error("Please enter a valid value");
        }
    }// end of method

}// end of class
