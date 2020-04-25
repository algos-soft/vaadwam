package it.algos.vaadflow.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 17-ago-2019
 * Time: 11:26
 */
@Service
@Slf4j
public class ARouteService extends AbstractService {

    /**
     * The name of a supported <a href="../lang/package-summary.html#charenc">character encoding</a>.
     */
    private static final String CODE = "UTF-8";

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final ARouteService INSTANCE = new ARouteService();


    /**
     * Private constructor to avoid client applications to use constructor <br>
     * In alcune circostanze SpringBoot non riesce a costruire l'istanza <br>
     * Rimesso 'public' al posto del precedente 'private' <br>
     */
    public ARouteService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static ARouteService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Codifica un testo PRIMA di trasmetterlo come URL al browser <br>
     * <p>
     * Translates a string into {@code application/x-www-form-urlencoded} format using a specific encoding scheme.
     * This method uses the supplied encoding scheme to obtain the bytes for unsafe characters.
     * <p>
     * <em><strong>Note:</strong> The <a href="http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that UTF-8 should be used.
     * Not doing so may introduce incompatibilities.</em>
     *
     * @param text {@code String} to be translated.
     *
     * @return the translated {@code String}.
     *
     * @throws UnsupportedEncodingException If the named encoding is not supported
     * @see URLDecoder#decode(java.lang.String, java.lang.String)
     * @since 1.4
     */
    public String codifica(String text) {
        String textUTF8 = text;

        try { // prova ad eseguire il codice
            textUTF8 = URLEncoder.encode(text, CODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return textUTF8;
    }// end of method


    /**
     * Codifica un testo DOPO averlo ricevuto come URL dal browser <br>
     * <p>
     * Decodes a {@code application/x-www-form-urlencoded} string using a specific encoding scheme.
     * The supplied encoding is used to determine what characters are represented by any consecutive sequences of the
     * form "<i>{@code %xy}</i>".
     * <p>
     * <em><strong>Note:</strong> The <a href="http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that UTF-8 should be used.
     * Not doing so may introduce incompatibilities.</em>
     *
     * @param textUTF8 the {@code String} to decode
     *
     * @return the newly decoded {@code String}
     *
     * @throws UnsupportedEncodingException If character encoding needs to be consulted, but
     *                                      named character encoding is not supported
     * @see URLEncoder#encode(java.lang.String, java.lang.String)
     * @since 1.4
     */
    public String decodifica(String textUTF8) {
        String text = textUTF8;

        try { // prova ad eseguire il codice
            text = URLDecoder.decode(textUTF8, CODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return text;
    }// end of method


    /**
     * Costruisce una query di parametri da una mappa <br>
     * La mappa in ingresso è semplice: un valore per ogni chiave <br>
     * La classe QueryParameters è però prevista per valori multipli per ogni chiave <br>
     * Occorre quindi costruire una mappa chiave=lista di valori, con un solo valore <br>
     *
     * @param mappa di chiave=valore
     *
     * @return query da passare al Router di Vaadin
     */
    public QueryParameters getQuery(HashMap<String, String> mappa) {
        QueryParameters query = null;
        ArrayList<String> lista;
        HashMap<String, List<String>> mappaQuery = new HashMap<String, List<String>>();

        for (String key : mappa.keySet()) {
            lista = new ArrayList<>();
            lista.add(mappa.get(key));
            mappaQuery.put(key, lista);
        }// end of for cycle

        query = new QueryParameters(mappaQuery);

        return query;
    }// end of method.


    /**
     * Naviga verso l'URL indicato con la mappa di parametri <br>
     *
     * @param interfacciaUtente del chiamante
     * @param routeName         destinazione del Router
     * @param mappa             di chiave=valore
     */
    public void navigate(Optional<UI> interfacciaUtente, String routeName, HashMap<String, String> mappa) {
        QueryParameters query = getQuery(mappa);
        UI ui = null;

        if (interfacciaUtente != null) {
            ui = interfacciaUtente.get();
        }// end of if cycle

        try { // prova ad eseguire il codice
            if (ui != null) {
                ui.navigate(routeName, query);
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

    }// end of method


    /**
     * Naviga verso l'URL indicato col parametro di testo indicato <br>
     *
     * @param interfacciaUtente del chiamante
     * @param routeName         destinazione del Router
     * @param textValue         da inviare come parametro
     */
    public void navigate(Optional<UI> interfacciaUtente, String routeName, String textValue) {
        String bodyTextUTF8;
        UI ui = null;

        if (interfacciaUtente != null) {
            ui = interfacciaUtente.get();
        }// end of if cycle

        try { // prova ad eseguire il codice
            if (ui != null) {
                bodyTextUTF8 = codifica(textValue);
                ui.navigate(routeName + "/" + bodyTextUTF8);
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

    }// end of method


    /**
     * Parameters passed through the URL
     * <p>
     * This method is called by the Router, based on values extracted from the URL
     * This method will always be invoked before a navigation target is activated.
     * URL parameters can be annotated as optional using @OptionalParameter.
     * Where more parameters are needed, the URL parameter can also be annotated with @WildcardParameter.
     * The wildcard parameter will never be null.
     * It is possible to get any query parameters contained in a URL, for example ?name1=value1&name2=value2.
     * Use the getQueryParameters() method of the Location class to access query parameters.
     * You can obtain the Location class through the BeforeEvent parameter of the setParameter method.
     * A Location object represents a relative URL made up of path segments and query parameters,
     * but without the hostname, e.g. new Location("foo/bar/baz?name1=value1").
     * getQueryParameters() supports multiple values associated with the same key,
     * for example https://example.com/?one=1&two=2&one=3
     * will result in the corresponding map {"one" : [1, 3], "two": [2]}}.
     */
    public Parametro estraeParametri(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> multiParametersMap = queryParameters.getParameters();
        Parametro parametro = null;

        if (text.isValid(parameter)) {
            parametro = new Parametro(decodifica(parameter));
        }// end of if cycle

        if (array.isValid(multiParametersMap)) {
            if (array.isMappaSemplificabile(multiParametersMap)) {
                parametro = new Parametro(array.semplificaMappa(multiParametersMap));
            } else {
                parametro = new Parametro();
                parametro.setMultiParametersMap(multiParametersMap);
            }// end of if/else cycle
        }// end of if cycle

        return parametro;
    }// end of method


    public class Parametro {

        private String singleParameter;

        private Map<String, String> parametersMap;

        private Map<String, List<String>> multiParametersMap;


        public Parametro() {
        }// end of constructor


        public Parametro(String singleParameter) {
            this.singleParameter = singleParameter;
        }// end of constructor


        public Parametro(Map<String, String> parametersMap) {
            this.parametersMap = parametersMap;
        }// end of constructor


        public String getSingleParameter() {
            return singleParameter;
        }// end of method


        public Map<String, String> getParametersMap() {
            return parametersMap;
        }// end of method


        public Map<String, List<String>> getMultiParametersMap() {
            return multiParametersMap;
        }// end of method


        public void setMultiParametersMap(Map<String, List<String>> multiParametersMap) {
            this.multiParametersMap = multiParametersMap;
        }// end of method

    }// end of inner class

}// end of class
