package it.algos.vaadwam.application;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.boot.AVers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Log delle versioni, modifiche e patch installate
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WamVers extends AVers {


    private final static String CODE_PROJECT = "W";

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private VersioneService vers;
//
//
//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private PreferenzaService pref;


    /**
     * This method is called prior to the servlet context being initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE <br>
     */
    public int inizia() {
        int k = super.inizia();
        codeProject = CODE_PROJECT;

        //--prima installazione del programma
        //--non fa nulla, solo informativo
        if (installa(++k)) {
            crea("Setup", "Installazione iniziale");
        }// fine del blocco if

        return k;
    }// end of method


}// end of bootstrap class