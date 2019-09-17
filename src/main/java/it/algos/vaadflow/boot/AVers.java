package it.algos.vaadflow.boot;

import it.algos.vaadflow.modules.preferenza.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.Preferenza;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.versione.VersioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowVar.projectName;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 14-lug-2018
 * Time: 15:08
 * <p>
 * Log delle versioni, modifiche e patch installate
 * <p>
 * Executed on container startup
 * Setup non-UI logic here
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 */
@Slf4j
public abstract class AVers {


    /**
     * Property statica per le versioni inserite da vaadinflow direttamente <br>
     */
    private final static String CODE_PROJECT = "A";

    /**
     * Property corrente per la sigla iniziale della codifica alfanumerica delle versioni <br>
     */
    protected String codeProject;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected VersioneService versioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService preferenzaService;


    /**
     * This method is called prior to the servlet context being initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE
     */
    public int inizia() {
        int k = 0;
        codeProject = CODE_PROJECT;
        boolean preferenzaCreata = false;
        Preferenza prefNew;
        String descOld;
        String descNew;

        //--prima installazione del programma
        //--non fa nulla, solo informativo
        if (installa(++k)) {
            crea("Setup", "Installazione iniziale di " + projectName);
        }// fine del blocco if

        //--crea le preferenze standard
        //--queste vengono aggiunte indipendentemente dall'ordine della enumeration
        //--vengono create se non esistono
        //--se esistono, viene aggiornata la descrizione prevista dalla enumeration
        for (EAPreferenza eaPref : EAPreferenza.values()) {
            preferenzaCreata = preferenzaService.creaIfNotExist(eaPref);
            if (preferenzaCreata) {
                versioneService.creaIfNotExist(codeProject, "Preferenze", eaPref.getDesc() + ", di default " + eaPref.getValue());
            } else {
                prefNew = (Preferenza) preferenzaService.findById(eaPref.getCode());
                descOld = eaPref.getDesc();
                descNew = prefNew.getDescrizione();
                if (!descOld.equals(descNew)) {
                    versioneService.creaIfNotExist("Z", "#" + prefNew.code, "#desc: " + descOld + " -> " + descNew);
                    prefNew.setDescrizione(descNew);
                    preferenzaService.save(prefNew);
                }// end of if cycle
            }// end of if/else cycle
            k++;
        }// end of for cycle

        return k;
    }// end of method


    /**
     * Controlla che la versione non esista già <br>
     */
    public boolean installa(int k) {
        return versioneService.isMancaByKeyUnica(codeProject, k);
    }// end of method


    /**
     * Crea una entity di versione e la registra <br>
     *
     * @param titolo della versione (obbligatorio, non unico) <br>
     * @param nome   descrittivo della versione (obbligatorio, unico) <br>
     */
    public void crea(String titolo, String nome) {
        versioneService.creaIfNotExist(codeProject, titolo, nome);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefTxt(String codePref, String descPref) {
        versioneService.creaPrefTxt(codeProject, codePref, descPref);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefTxt(String codePref, String descPref, String value) {
        versioneService.creaPrefTxt(codeProject, codePref, descPref, value);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefBool(String codePref, String descPref) {
        versioneService.creaPrefBool(codeProject, codePref, descPref);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefBool(String codePref, String descPref, boolean value) {
        versioneService.creaPrefBool(codeProject, codePref, descPref, value);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefInt(String codePref, String descPref) {
        versioneService.creaPrefInt(codeProject, codePref, descPref);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefInt(String codePref, String descPref, int value) {
        versioneService.creaPrefInt(codeProject, codePref, descPref, value);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefDate(String codePref, String descPref) {
        versioneService.creaPrefDate(codeProject, codePref, descPref);
    }// end of method


    /**
     * Crea una entity di preferenze e la regiistra <br>
     * Crea una entity di versione e la registra <br>
     *
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefDate(String codePref, String descPref, LocalDateTime value) {
        versioneService.creaPrefDate(codeProject, codePref, descPref, value);
    }// end of method

}// end of class
