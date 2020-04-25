package it.algos.vaadflow.annotation;


import it.algos.vaadflow.enumeration.EAFieldAccessibility;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.role.EARoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceField (AIField)
 * Annotation to add some property for a single field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in field only.
public @interface AIField {


    /**
     * (Optional) Classe della property.
     * Utilizzato nei Link.
     */
    Class<? extends Object> linkClazz() default Object.class;

    /**
     * (Optional) Classe della property.
     * Utilizzato nelle Enumeration.
     */
    Class<? extends Object> enumClazz() default Object.class;

    /**
     * (Optional) Classe della property.
     * Utilizzato nei Combo.
     */
    Class<? extends Object> serviceClazz() default Object.class;


    /**
     * (Optional) valori (items) della enumeration
     * Defaults to "".
     */
    String items() default "";


    /**
     * (Required) The type of the field.
     * Defaults to the text type.
     */
    EAFieldType type() default EAFieldType.text;


    /**
     * (Optional) The name of the field.
     * Defaults to the property or field name.
     */
    String name() default "";


    /**
     * (Optional) The width of the field.
     * Expressed in int, to be converted in String ending with "em"
     * Defaults to 0.
     */
    int widthEM() default 0;


    /**
     * (Optional) The number of rows of textArea field.
     * Expressed in int
     * Defaults to 3.
     */
    int numRowsTextArea() default 3;


    /**
     * (Optional) Status (field required for DB) of the the field.
     * Defaults to false.
     */
    boolean required() default false;


    /**
     * (Optional) Visibilità a secondo del ruolo dell'User collegato
     * Defaults to guest.
     */
    EARoleType roleTypeVisibility() default EARoleType.asEntity;


    /**
     * (Optional) Status (field visible and/or enabled in form) of the field.
     * Different for New e for Edit
     * Specific for developer role
     * Defaults to Form value.
     */
    EAFieldAccessibility dev() default EAFieldAccessibility.asForm;

    /**
     * (Optional) Status (field visible and/or enabled in form) of the field.
     * Different for New e for Edit
     * Specific for buttonAdmin role
     * Defaults to Form value.
     */
    EAFieldAccessibility admin() default EAFieldAccessibility.asForm;

    /**
     * (Optional) Status (field visible and/or enabled in form) of the field.
     * Different for New e for Edit
     * Specific for buttonUser role
     * Defaults to Form value.
     */
    EAFieldAccessibility user() default EAFieldAccessibility.asForm;


    /**
     * (Optional) field that get focus
     * Only one for form
     * Defaults to false.
     */
    boolean focus() default false;


    /**
     * (Optional) help text on rollover
     * Defaults to null.
     */
    String help() default "";


    /**
     * (Optional) Status (first letter capital) of the the field.
     * Defaults to false.
     */
    boolean firstCapital() default false;


    /**
     * (Optional) Status (all letters upper) of the the field.
     * Defaults to false.
     */
    boolean allUpper() default false;


    /**
     * (Optional) Status (all letters lower) of the the field.
     * Defaults to false.
     */
    boolean allLower() default false;


    /**
     * (Optional) Status (only number) of the the field.
     * Defaults to false.
     */
    boolean onlyNumber() default false;


    /**
     * (Optional) Status (only letters) of the the field.
     * Defaults to false.
     */
    boolean onlyLetter() default false;


    /**
     * (Optional) Status (allowed null selection in popup) of the the field.
     * Meaning sense only for EAFieldType.combo.
     * Defaults to false.
     */
    boolean nullSelectionAllowed() default false;


    /**
     * (Optional) Status (allowed new selection in popup) of the the field.
     * Meaning sense only for EAFieldType.combo.
     * Defaults to false.
     */
    boolean newItemsAllowed() default false;

    /**
     * (Optional) color of the component
     * Defaults to "".
     */
    String color() default "";

    /**
     * (Optional) method name for reflection
     * Defaults to findItems.
     */
    String methodName() default "findItems";

    /**
     * (Optional) property name for reflection
     * Defaults to "".
     */
    String propertyLinkata() default "";

}// end of interface annotation
