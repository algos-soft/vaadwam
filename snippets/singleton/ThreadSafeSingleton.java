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
 * The easier way to create a thread-safe singleton class is to make the global access method synchronized,
 * so that only one thread can execute this method at a time.
 * General implementation of this approach is like the first synchronized getInstance() method.
 * This implementation works fine and provides thread-safety
 * but it reduces the performance because of cost associated with the synchronized method,
 * although we need it only for the first few threads who might create the separate instances.
 * <p>
 * To avoid this extra overhead every time, double checked locking principle is used.
 * In this approach, the synchronized block is used inside the if condition
 * with an additional check to ensure that only one instance of singleton class is created.
 * <p>
 * The code snippet provides the double checked locking implementation in the getInstanceUsingDoubleLocking() method.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ThreadSafeSingleton {


    /**
     * Private final property
     */
    private static final ThreadSafeSingleton INSTANCE;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private ThreadSafeSingleton() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static synchronized ThreadSafeSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadSafeSingleton();
        }// end of if cycle

        return INSTANCE;
    }// end of static method


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static ThreadSafeSingleton getInstanceUsingDoubleLocking() {
        if (INSTANCE == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadSafeSingleton();
                }// end of if cycle
            }
        }// end of if cycle

        return INSTANCE;
    }// end of static method

}// end of singleton class
