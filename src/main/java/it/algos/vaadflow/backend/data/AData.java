package it.algos.vaadflow.backend.data;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AAnnotationService;
import it.algos.vaadflow.service.AbstractService;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vbase
 * Created by Algos
 * User: gac
 * Date: lun, 19-mar-2018
 * Time: 21:10
 * <p>
 * Superclasse astratta per la costruzione inziale delle Collections <br>
 * Viene invocata PRIMA della chiamata del browser, tramite un metodo @PostConstruct della sottoclasse <br>
 * Non si possono quindi usare i service specifici dei package che sono @VaadinSessionScope <br>
 * Viceversa le repository specifiche dei package sono delle interfacce e pertanto vengono 'create' al volo <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio per le injections) <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class AData extends AbstractService {


    /**
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     * La repository è gestita direttamente dal service
     */
    protected IAService service;

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected AAnnotationService annotation = AAnnotationService.getInstance();

    /**
     * Nome della collezione su mongoDB <br>
     * Viene regolato dalla sottoclasse nel costruttore <br>
     */
    protected String collectionName;


    public AData() {
    }// end of Spring constructor

    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     */
//    public AData(MongoOperations mongo, MongoRepository repository, IAService service) {
    public AData(Class entityClazz, IAService service) {
        this.collectionName = annotation.getCollectionName(entityClazz);
        this.service = service;
    }// end of Spring constructor


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public int count() {
        return (int) service.count();
    }// end of method


    /**
     * Controlla se la collezione esiste già
     *
     * @return true se la collection è inesistente
     */
    protected boolean nessunRecordEsistente() {
        return this.count() == 0;
    }// end of method


    /**
     * Metodo invocato da ABoot (o da una sua sottoclasse) <br>
     * <p>
     * Creazione di una collezione - Solo se non ci sono records
     */
    public void loadData() {
        int numRec = this.count();

        if (numRec == 0) {
            numRec = creaAll();
            log.warn("Algos " + collectionName + "- Creazione dati iniziali loadData(): " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione " + collectionName + " è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della collezione
     */
    protected int creaAll() {
        return 0;
    }// end of method

}// end of class
