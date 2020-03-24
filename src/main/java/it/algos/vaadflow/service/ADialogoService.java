package it.algos.vaadflow.service;

import com.vaadin.flow.component.UI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

import static it.algos.vaadflow.application.FlowCost.KEY_MAPPA_BODY;
import static it.algos.vaadflow.application.FlowCost.KEY_MAPPA_HEADER;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 16-ago-2019
 * Time: 22:36
 *
 * Usata per aprire il dialogo in una nuova finestra-indirizzo url, usando @route e Router <br>
 * In realtà non molto utile. Non è possibile (a meno di complessi metodi) ritornare un valore <br>
 */
@Service
@Slf4j
public class ADialogoService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Private final property
     */
    private static final ADialogoService INSTANCE = new ADialogoService();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public ARouteService routeService = ARouteService.getInstance();


//    /**
//     * Private constructor to avoid client applications to use constructor
//     */
//    private ADialogoService() {
//    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static ADialogoService getInstance() {
        return INSTANCE;
    }// end of static method


    public void dialogoZero(Optional<UI> interfacciaUtente, String bodyText) {
        String routeNameTag = "dialogozero";
        routeService.navigate(interfacciaUtente, routeNameTag, bodyText);
    }// end of method


    public void dialogoUno(Optional<UI> interfacciaUtente, String bodyText) {
        dialogoUno(interfacciaUtente, "", bodyText);
    }// end of method


    public void dialogoUno(Optional<UI> interfacciaUtente, String headerText, String bodyText) {
        String routeNameTag = "dialogouno";
        dialogo(interfacciaUtente, headerText, bodyText,routeNameTag);
    }// end of method


    public void dialogoDue(Optional<UI> interfacciaUtente, String bodyText) {
        dialogoDue(interfacciaUtente, "", bodyText);
    }// end of method


    public void dialogoDue(Optional<UI> interfacciaUtente, String headerText, String bodyText) {
        String routeNameTag = "dialogodue";
        dialogo(interfacciaUtente, headerText, bodyText,routeNameTag);
    }// end of method


    public void dialogo(Optional<UI> interfacciaUtente, String headerText, String bodyText, String routeNameTag) {
        if (text.isValid(headerText)) {
            routeService.navigate(interfacciaUtente, routeNameTag, getMappa(headerText, bodyText));
        } else {
            routeService.navigate(interfacciaUtente, routeNameTag, bodyText);
        }// end of if/else cycle
    }// end of method


    public HashMap<String, String> getMappa(String headerText, String bodyText) {
        HashMap<String, String> mappa = new HashMap<>();
        mappa.put(KEY_MAPPA_HEADER, headerText);
        mappa.put(KEY_MAPPA_BODY, bodyText);
        return mappa;
    }// end of method

}// end of class
