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
 * Time: 14:26
 * Lazy initialization method to implement Singleton pattern creates the instance in the global access method.
 * Here is the sample code for creating Singleton class with this approach.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class LazySingleton {

    /**
     * Private final property
     */
    private static final LazySingleton INSTANCE;


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
    public static LazySingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton();
        }// end of if cycle

        return INSTANCE;
    }// end of static method

}// end of singleton class
