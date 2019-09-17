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
 * Time: 14:28
 * Prior to Java 5, java memory model had a lot of issues and above approaches used to fail in certain scenarios
 * where too many threads try to get the instance of the Singleton class simultaneously.
 * So Bill Pugh came up with a different approach to create the Singleton class using a inner static helper class.
 * The Bill Pugh Singleton implementation goes like this;
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class HelperSingleton {


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private HelperSingleton() {
    }// end of constructor


    private static class HelperSingleton{
        private static final HelperSingleton INSTANCE = new HelperSingleton();
    }// end of static class


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static HelperSingleton getInstance(){
        return HelperSingleton.INSTANCE;
    }// end of static method

}// end of singleton class
