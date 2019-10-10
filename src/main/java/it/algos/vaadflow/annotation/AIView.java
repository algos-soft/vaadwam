package it.algos.vaadflow.annotation;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.modules.role.EARoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 19-dic-2017
 * Time: 11:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIView {


    /**
     * (Optional) Label del menu
     * Vaadin usa SEMPRE il 'name' della Annotation @Route per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile (con il primo carattere maiuscolo) il menuName, indicato qui
     * Di default usa il 'name' della view (@Route)
     */
    String menuName() default "";

    /**
     * (Optional) Icona visibile nel menu
     * Di default un asterisco
     */
    VaadinIcon menuIcon() default VaadinIcon.ASTERISK;

    /**
     * (Optional) Property per la ricerca tramite il searchField
     */
    String searchProperty() default "";

    /**
     * (Optional) Appartenenza al progetto Base VaadFlow
     */
    boolean vaadflow() default false;

    /**
     * (Optional) Visibilità a secondo del ruolo dell'User collegato
     * Defaults to user.
     */
    EARoleType roleTypeVisibility() default EARoleType.user;


}// end of interface annotation
