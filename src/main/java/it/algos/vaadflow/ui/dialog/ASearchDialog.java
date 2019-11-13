package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AAnnotationService;
import it.algos.vaadflow.service.AFieldService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 02-gen-2019
 * Time: 15:19
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ASearchDialog extends ADialog {


    //--Titolo standard, eventualmente modificabile nelle sottoclassi
    private static String TITOLO = "Cerca";

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AFieldService fieldService = AFieldService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation = AAnnotationService.getInstance();

    public LinkedHashMap<String, AbstractField> fieldMap;

    public IAService service;

    protected AEntity entityClass;


    /**
     * Costruttore <br>
     */
    public ASearchDialog() {
        super(TITOLO);
    }// end of constructor


    /**
     * Costruttore <br>
     */
    public ASearchDialog(IAService service) {
        super(TITOLO);
        this.service = service;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizializzazione() {
        super.inizia();
    }// end of method


    /**
     * Preferenze standard.
     * Possono essere modificate anche selezionando la firma di open(...)
     * Le preferenze vengono eventualmente sovrascritte nella sottoclasse
     * Invocare PRIMA il metodo della superclasse
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.textCancelButton = "Annulla";
        super.textConfirmlButton = "Ricerca";
    }// end of method


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     */
    protected void fixBodyLayout(String message, String additionalMessage) {
        super.fixBodyLayout(message, additionalMessage);
        creaFields();
    }// end of method


    /**
     * Barra dei bottoni
     */
    @Override
    protected void fixBottomLayout() {
        super.fixBottomLayout();
        cancelButton.getElement().setAttribute("theme", "secondary");
        confirmButton.getElement().setAttribute("theme", "primary");
    }// end of method


    /**
     * Aggiunge i campi di ricerca <br>
     * Sovrascrivibile <br>
     */
    protected void creaFields() {
        List<String> propertyNamesList;
        AbstractField propertyField = null;
        String fieldKeyMongo = "";

        //--Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
        fieldMap = new LinkedHashMap<>();

        //--Costruisce una lista di nomi delle properties del Form nell'ordine:
        //--1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
        //--2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
        //--3) Sovrascrive la lista nella sottoclasse specifica di xxxService
        propertyNamesList = getPropertiesName();

        //--Costruisce ogni singolo field
        //--Aggiunge il field al binder, nel metodo create() del fieldService
        //--Aggiunge il field ad una fieldMap, per recuperare i fields dal nome
        if (propertyNamesList != null) {
            for (String propertyName : propertyNamesList) {
                propertyField = fieldService.create(null, null, ((AService) service).entityClass, propertyName);
                if (propertyField != null) {
                    fieldKeyMongo = annotation.getFieldKeyMongo(((AService) service).entityClass, propertyName);
                    fieldMap.put(fieldKeyMongo, propertyField);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        addFieldsToLayout();
    }// end of method


    /**
     * Aggiunge ogni singolo field della fieldMap al layout grafico
     */
    protected void addFieldsToLayout() {
        bodyPlaceHolder.removeAll();
        for (String name : fieldMap.keySet()) {
            bodyPlaceHolder.add(fieldMap.get(name));
        }// end of for cycle
    }// end of method


    /**
     * Costruisce nell'ordine una lista di nomi di properties <br>
     * La lista viene usata per la costruzione automatica dei campi e l'inserimento nel binder <br>
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * Sovrasrivibile nella sottoclasse <br>
     * Se serve, modifica l'ordine della lista oppure esclude una property che non deve andare nel binder <br>
     */
    protected List<String> getPropertiesName() {
        return service != null ? service.getSearchPropertyNamesList(context) : null;
    }// end of method


    public void confermaHandler() {
        if (confirmHandler != null) {
            confirmHandler.run();
        }// end of if cycle
        close();
    }// end of method

}// end of class
