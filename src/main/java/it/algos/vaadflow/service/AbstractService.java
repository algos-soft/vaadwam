package it.algos.vaadflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 18-ott-2018
 * Time: 20:04
 * <p>
 * Superclasse astratta delle librerie xxxService. <br>
 * Serve per 'dichiarare' in un posto solo i riferimenti ad altre classi ed usarli nelle sottoclassi concrete <br>
 * I riferimenti sono 'public' per poterli usare con TestUnit <br>
 */
public abstract class AbstractService {

    @Autowired
    public ApplicationContext appContext;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ABootService boot;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AColumnService column;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ADateService date;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AFieldService field;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AMongoService mongo;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AReflectionService reflection;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AEnumerationService enumService;

    @PostConstruct
    protected void postConstruct() {
        this.annotation = AAnnotationService.getInstance();
        this.array = AArrayService.getInstance();
        this.column = AColumnService.getInstance();
        this.boot = ABootService.getInstance();
        this.date = ADateService.getInstance();
        this.field = AFieldService.getInstance();
        this.mongo = appContext.getBean(AMongoService.class);
        this.reflection = AReflectionService.getInstance();
        this.text = ATextService.getInstance();
        this.enumService = AEnumerationService.getInstance();

        fixIncrociati();
    }// end of constructor


    protected void fixIncrociati() {
        this.annotation.array = array;
        this.annotation.boot = boot;
        this.annotation.column = column;
        this.annotation.date = date;
        this.annotation.field = field;
        this.annotation.mongo = mongo;
        this.annotation.reflection = reflection;
        this.annotation.text = text;

        this.array.annotation = annotation;
        this.array.boot = boot;
        this.array.column = column;
        this.array.date = date;
        this.array.field = field;
        this.array.mongo = mongo;
        this.array.reflection = reflection;
        this.array.text = text;

        this.boot.annotation = annotation;
        this.boot.array = array;
        this.boot.column = column;
        this.boot.date = date;
        this.boot.field = field;
        this.boot.mongo = mongo;
        this.boot.reflection = reflection;
        this.boot.text = text;

        this.column.annotation = annotation;
        this.column.array = array;
        this.column.boot = boot;
        this.column.date = date;
        this.column.field = field;
        this.column.mongo = mongo;
        this.column.reflection = reflection;
        this.column.text = text;

        this.date.annotation = annotation;
        this.date.array = array;
        this.date.boot = boot;
        this.date.column = column;
        this.date.field = field;
        this.date.mongo = mongo;
        this.date.reflection = reflection;
        this.date.text = text;

        this.field.annotation = annotation;
        this.field.array = array;
        this.field.boot = boot;
        this.field.column = column;
        this.field.date = date;
        this.field.mongo = mongo;
        this.field.reflection = reflection;
        this.field.text = text;

        this.mongo.annotation = annotation;
        this.mongo.array = array;
        this.mongo.boot = boot;
        this.mongo.column = column;
        this.mongo.date = date;
        this.mongo.field = field;
        this.mongo.reflection = reflection;
        this.mongo.text = text;

        this.reflection.annotation = annotation;
        this.reflection.array = array;
        this.reflection.boot = boot;
        this.reflection.column = column;
        this.reflection.date = date;
        this.reflection.field = field;
        this.reflection.mongo = mongo;
        this.reflection.text = text;

        this.text.annotation = annotation;
        this.text.array = array;
        this.text.boot = boot;
        this.text.column = column;
        this.text.date = date;
        this.text.field = field;
        this.text.mongo = mongo;
        this.text.reflection = reflection;
    }// end of method

}// end of class
