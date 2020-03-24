package it.algos.vaadflow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Jul-2019
 * Time: 21:35
 */
@Service
@Slf4j
public class AEnumerationService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final AEnumerationService INSTANCE = new AEnumerationService();

    private final static String TAG = ";";


//    /**
//     * Private constructor to avoid client applications to use constructor
//     */
//    private AEnumerationService() {
//    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AEnumerationService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Trasforma il contenuto del mongoDB nel valore selezionato, eliminando la lista dei valori possibili
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return valore selezionato
     */
    public String convertToPresentation(String rawValue) {
        String value = rawValue;
        String[] parti = getParti(rawValue);

        if (parti != null && parti.length == 2) {
            value = parti[1];
        }// end of if cycle

        return value;
    }// end of method


    /**
     * Restituisce le due parti del valore grezzo <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return matrice delle due parti
     */
    public String[] getParti(String rawValue) {
        String[] parti = null;

        if (rawValue.contains(TAG)) {
            parti = rawValue.split(TAG);
        }// end of if cycle

        return parti;
    }// end of method


    /**
     * Restituisce una stringa dei valori ammissibili <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return stringa dei valori ammissibili
     */
    public String getStringaValori(String rawValue) {
        String stringaValori = "";
        String[] parti = getParti(rawValue);

        if (parti != null && parti.length == 2) {
            stringaValori = parti[0];
        }// end of if cycle

        return stringaValori;
    }// end of method


    /**
     * Restituisce la lista dei valori ammissibili <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return lista dei valori ammissibili
     */
    public List<String> getList(String rawValue) {
        List<String> lista = null;
        String stringaValori = getStringaValori(rawValue);

        if (text.isValid(stringaValori)) {
            lista = array.getList(stringaValori);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Modifica il valore selezionato nella stringa del mongoDB
     *
     * @param rawValue         dei valori ammessi seguita dal valore selezionato
     * @param newSelectedValue da sostituire
     *
     * @return valore selezionato
     */
    public String convertToModel(String rawValue, String newSelectedValue) {
        String newRawValue = "";
        String stringaValori = getStringaValori(rawValue);

        if (text.isValid(stringaValori)) {
            newRawValue = stringaValori + TAG + newSelectedValue;
        }// end of if cycle

        return newRawValue;
    }// end of method


//    /**
//     * Regola in lettura eventuali valori NON associati al binder
//     * Sovrascritto
//     */
//    public void convertToModel() {
//    }// end of method

}// end of class
