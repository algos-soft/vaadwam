package it.algos.vaadflow.menu;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 19-ago-2018
 * Time: 14:24
 *
 * In eager initialization, the instance of Singleton Class is created at the time of class loading,
 * this is the easiest method to create a singleton class
 * but it has a drawback that instance is created even though client application might not be using it.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class EagerSingleton {


    /**
     * Private final property
     */
    private static final EagerSingleton INSTANCE = new EagerSingleton();


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
    public static EagerSingleton getInstance() {
        return INSTANCE;
    }// end of static method

}// end of singleton class
