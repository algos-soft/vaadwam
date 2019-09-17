package it.algos.vaadflow.annotation;

import it.algos.vaadflow.enumeration.EAFieldAccessibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceForm (AIForm)
 * Annotation to add some property for a single form.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIForm {


//    /**
//     * (Optional) Status (shows the ID property field) .
//     * Defaults to false.
//     */
//    boolean showsID() default false;


    /**
     * (Optional) The width of the ID field.
     * Expressed in int, to be converted in String ending with "em"
     * Defaults to "16em".
     */
    int widthIDEM() default 16;


    /**
     * (Optional) List of visible fields on Form
     * Defaults to all.
     */
    String[] fields() default "";

    /**
     * (Optional) Status (field visible and/or enabled in form) of all the fields in Form.
     * Different for New e for Edit
     * Specific for developer role
     * Defaults to allways.
     */
    EAFieldAccessibility fieldsDev() default EAFieldAccessibility.allways;

    /**
     * (Optional) Status (field visible and/or enabled in form) of all the fields in Form.
     * Different for New e for Edit
     * Specific for admin role
     * Defaults to showOnly.
     */
    EAFieldAccessibility fieldsAdmin() default EAFieldAccessibility.showOnly;

    /**
     * (Optional) Status (field visible and/or enabled in form) of all the fields in Form.
     * Different for New e for Edit
     * Specific for user role
     * Defaults to never.
     */
    EAFieldAccessibility fieldsUser() default EAFieldAccessibility.never;

    /**
     * (Optional) List of buttom on bottom
     * Specific for developer role
     * Defaults to standard.
     */
    EAFormButton buttonsDev() default EAFormButton.standard;

    /**
     * (Optional) List of buttom on bottom
     * Specific for admin role
     * Defaults to standard.
     */
    EAFormButton buttonsAdmin() default EAFormButton.standard;

    /**
     * (Optional) List of buttom on bottom
     * Specific for user role
     * Defaults to show.
     */
    EAFormButton buttonsUser() default EAFormButton.show;

}// end of interface annotation
