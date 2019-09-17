package it.algos.vaadflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 13-gen-2018
 * Time: 12:12
 * Annotation per controllare il flusso degli scripts di creazione del framework.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIScript {


    /**
     * (Optional) Status of this file.
     * Defaults to false.
     */
    boolean sovrascrivibile() default false;


}// end of interface annotation
