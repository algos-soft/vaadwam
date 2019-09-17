package it.algos.vaadflow.menu;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 19-ago-2018
 * Time: 14:25
 * <p>
 * Static block initialization implementation is similar to eager initialization,
 * except that instance of class is created in the static block that provides option for exception handling.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class StaticBlockSingleton {


    /**
     * Private final property
     */
    private static final StaticBlockSingleton INSTANCE;


    /**
     * Static block initialization for exception handling
     */
    static {
        try { // prova ad eseguire il codice
            INSTANCE = new StaticBlockSingleton();
        } catch (Exception unErrore) { // intercetta l'errore
            throw new RuntimeException("Exception occured in creating singleton instance");
        }// fine del blocco try-catch
    }// end of static block


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private EagerSingleton() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static StaticBlockSingleton getInstance() {
        return INSTANCE;
    }// end of static method

}// end of singleton class
