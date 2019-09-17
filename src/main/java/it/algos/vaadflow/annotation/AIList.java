package it.algos.vaadflow.annotation;


import it.algos.vaadflow.enumeration.EAListButton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceList (AIList)
 * Annotation to add some property for a single collection or list.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIList {



    /**
     * (Optional) The width of the ID columnService.
     * Defaults to 270
     */
    int widthID() default 290;


    /**
     * (Optional) List of visible fields on Form
     * Defaults to all.
     */
    String[] fields() default "";


    /**
     * (Optional) List of buttom on bottom
     * Specific for developer role
     * Defaults to standard.
     */
    EAListButton dev() default EAListButton.standard;

    /**
     * (Optional) List of buttom on bottom
     * Specific for buttonAdmin role
     * Defaults to standard.
     */
    EAListButton admin() default EAListButton.noSearch;

    /**
     * (Optional) List of buttom on bottom
     * Specific for buttonUser role
     * Defaults to standard.
     */
    EAListButton user() default EAListButton.edit;


}// end of interface annotation
