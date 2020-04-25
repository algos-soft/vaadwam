package it.algos.vaadflow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 19-nov-2019
 * Time: 20:43
 * <p>
 * Utility per la gestione di operatori e flussi matematici <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AMathService.class); <br>
 * 2) AMathService.getInstance(); <br>
 * 3) @Autowired private AMathService math; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class AMathService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final AMathService INSTANCE = new AMathService();


    /**
     * Private constructor to avoid client applications to use constructor
     */
    public AMathService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AMathService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Divisione tra due numeri double <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double divisione(double dividendo, double divisore) {
        double quoziente = dividendo / divisore;
        return quoziente;
    }// end of method


    /**
     * Divisione tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double divisione(int dividendo, int divisore) {
        return divisione((double) dividendo, (double) divisore);
    }// end of method


    /**
     * Percentuale tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double percentuale(int dividendo, int divisore) {
        return divisione((double) dividendo, (double) divisore) * 100;
    }// end of method


    /**
     * Percentuale tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo String
     */
    public String percentualeDueDecimali(int dividendo, int divisore) {
        return new DecimalFormat("0.00").format(percentuale(dividendo, divisore))+"%";
    }// end of method

}// end of class
