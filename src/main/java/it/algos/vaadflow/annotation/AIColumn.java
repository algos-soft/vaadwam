package it.algos.vaadflow.annotation;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.enumeration.EAFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static it.algos.vaadflow.application.FlowCost.VAADIN_ICON_DA_NON_USARE;

/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceColumn (AIColumn)
 * Annotation to add some property for a single columnService of the Grid.
 * Alcune property sono in comune con AIField
 * Se nell'annotation AIColumn manca una property ,
 * si prende il valore della corrispondente property di AIField
 * (se esiste, altrimenti il valore di default)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in field only.
public @interface AIColumn {


    /**
     * (Required) The type of the field.
     * Se manca (valore di default), prende quello indicato in AIField
     * Se manca anche in AIField, prende il valore di default di AIField
     */
    EAFieldType type() default EAFieldType.ugualeAlForm;


    /**
     * (Optional) The name of the column header.
     * Defaults to the property or field name.
     */
    String name() default "";


    /**
     * (Optional) The width of the column.
     * Expressed in int, to be converted in String ending with "em"
     * Defaults to 0.
     */
    int widthEM() default 0;


    /**
     * (Optional) color of the component
     * Defaults to "".
     */
    String color() default "blue";


    /**
     * (Optional) column that expand the maximum
     * Only one for list
     * Defaults to false.
     */
    boolean flexGrow() default false;

    /**
     * (Optional) column sortable
     * Defaults to false.
     */
    boolean sortable() default false;

    /**
     * (Optional) icon
     * Defaults to false.
     */
    VaadinIcon headerIcon() default VaadinIcon.YOUTUBE;

    /**
     * (Optional) icon
     * Defaults to 15.
     */
    int headerIconSizePX() default 20;

    /**
     * (Optional) icon
     * Defaults to blue.
     */
    String headerIconColor() default "blue";


    /**
     * (Optional) method name for reflection
     * Defaults to blue.
     */
    String methodName() default "";

}// end of interface annotation
