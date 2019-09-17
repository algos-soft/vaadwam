package it.algos.vaadwam.application;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.boot.AVers;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
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
     * L'ordine di inserimento è FONDAMENTALE
     */
    public int inizia() {
        int k = super.inizia();
        codeProject = CODE_PROJECT;

        //--prima installazione del programma
        //--non fa nulla, solo informativo
        if (installa(++k)) {
            crea("Setup", "Installazione iniziale");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_CROCI, "Crono per download croci", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_FUNZIONI, "Crono per download funzioni", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_SERVIZI, "Crono per download servizi", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_MILITI, "Crono per download militi", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_TURNI, "Crono per download turni", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_IMPORT_CROCI, "Data ultimo import di croci");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_IMPORT_FUNZIONI, "Data ultimo import di funzioni");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_IMPORT_SERVIZI, "Data ultimo import di servizi");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_IMPORT_MILITI, "Data ultimo import di militi");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_IMPORT_TURNI, "Data ultimo import di turni");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_MAIL_IMPORT, "Spedisce una mail ad ogni import", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_COLORAZIONE_DIFFERENZIATA, "Nel tabellone colori differenziati per le singole iscrizioni di un turno", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_FIELDS_ENABLED_IN_SHOW, "Mostra i fields abilitati anche nel Form per gli Users", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_CHECK_FUNZIONI_MILITE, "Mostra le funzioni abilitate di un Milite tramite checkbox", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            versioneService.creaPref(codeProject, TIPO_CANCELLAZIONE, "Modalità di cancellazione possibili", EAPrefType.enumeration, "mai,sempre,fino,dopo;sempre");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(MOSTRA_ORARIO_SERVIZIO, "Mostra l'orario del servizio nel dialogo di iscrizione al turno", true);
        }// fine del blocco if

        return k;
    }// end of method


}// end of bootstrap class