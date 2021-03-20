package it.algos.vaadwam.enumeration;


import it.algos.vaadflow.enumeration.*;
import it.algos.vaadflow.modules.preferenza.*;
import it.algos.vaadflow.modules.role.*;
import static it.algos.vaadwam.application.WamCost.*;
import it.algos.vaadwam.tabellone.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenzaWam implements IAPreferenza {

    //--generali
    redirectTabellone(SECURED_VIEW_REDIRECT_TABELLONE, "Reindirizza al tabellone in caso accesso non consentito ad una view", EAPrefType.bool, EARole.developer, false, true),

    //--scheduled task suddivise per croce
    //    usaDaemonCroci(USA_DAEMON_CROCI, "Crono per download completo di tutte le croci", EAPrefType.bool, false, true),

    usaDaemonImport(USA_DAEMON_IMPORT, "Crono per importare i dati della croce specifica", EAPrefType.bool, EARole.developer, true, false, true),

    usaDaemonElabora(USA_DAEMON_STATISTICHE, "Crono per elaborare le statistiche della croce specifica", EAPrefType.bool, true, true),

    usaDaemonDemo(USA_DAEMON_DEMO, "Crono per ricostruire la croce demo leggendo i files csv", EAPrefType.bool, EARole.developer, false, false, false),

    //--cronologia degli import
    lastImportCroci(LAST_IMPORT_CROCI, "Data ultimo import di tutte le croci", EAPrefType.localdatetime, null),

    lastImportFunzioni(LAST_IMPORT_FUNZIONI, "Data ultimo import funzioni della croce", EAPrefType.localdatetime, null),

    lastImportServizi(LAST_IMPORT_SERVIZI, "Data ultimo import servizi della croce", EAPrefType.localdatetime, null),

    lastImportMiliti(LAST_IMPORT_MILITI, "Data ultimo import militi della croce", EAPrefType.localdatetime, null),

    lastImportTurni(LAST_IMPORT_TURNI, "Data ultimo import turni della croce", EAPrefType.localdatetime, null),

    lastElabora(LAST_ELABORA, "Data ultima elaborazione delle statistiche della croce", EAPrefType.localdatetime, null),

    //--durata degli import
    durataImportCroci(DURATA_IMPORT_CROCI, "Durata ultimo import di tutte le croci, in secondi", EAPrefType.integer, 0),

    durataImportFunzioni(DURATA_IMPORT_FUNZIONI, "Durata ultimo import funzioni della croce, in secondi", EAPrefType.integer, 0),

    durataImportServizi(DURATA_IMPORT_SERVIZI, "Durata ultimo import servizi della croce, in secondi", EAPrefType.integer, 0),

    durataImportMiliti(DURATA_IMPORT_MILITI, "Durata ultimo import militi della croce, in secondi", EAPrefType.integer, 0),

    durataImportTurni(DURATA_IMPORT_TURNI, "Durata ultimo import turni della croce, in secondi", EAPrefType.integer, 0),

    durataElabora(DURATA_ELABORA, "Durata ultima elaborazione delle statistiche della croce, in secondi", EAPrefType.integer, 0),

    usaMailImport(USA_MAIL_IMPORT, "Spedisce una mail ad ogni import", EAPrefType.bool, EARole.developer, true, true),

    usaFieldsEnabledInShow(USA_FIELDS_ENABLED_IN_SHOW, "Mostra i fields abilitati anche nel Form per gli Users", EAPrefType.bool, EARole.developer, false, true, true),

    usaCheckFunzioniMilite(USA_CHECK_FUNZIONI_MILITE, "Mostra le funzioni abilitate di un Milite tramite checkbox", EAPrefType.bool, EARole.developer, false, false, true),

    mostraOrarioServizio(MOSTRA_ORARIO_SERVIZIO, "Mostra l'orario del servizio nel dialogo di iscrizione al turno", EAPrefType.bool, EARole.admin, false, true, true),

    //--admin
    usaColorazioneDifferenziata(USA_COLORAZIONE_DIFFERENZIATA, "Nel tabellone, colori differenziati per le singole iscrizioni di un turno", EAPrefType.bool, EARole.admin, true, false, true),

    usaColorazioneTurni(USA_COLORAZIONE_TURNI, "Nel form di edit del turno, colori differenziati per le singole iscrizioni", EAPrefType.bool, EARole.admin, true, true, false),

    mostraLegenda(MOSTRA_LEGENDA_TABELLONE, "Mostra la legenda in calce al tabellone", EAPrefType.bool, EARole.admin, true, true, false),

    nuovoTurno(CREAZIONE_NUOVO_TURNO_DA_UTENTE, "Il milite può creare un nuovo turno vuoto", EAPrefType.bool, EARole.admin, true, false, true),

    disabilitaLogin(DISABILITA_LOGIN, "Blocco dell'accesso (login) se nelle statistiche il milite non raggiunge la frequenza di turni minima", EAPrefType.bool, EARole.admin, true, false, true),

    numeroOreTurnoStandard(NUMERO_ORE_TURNO_STANDARD, "Ore convenzionali per convertire, nelle statistiche, un turno in ore", EAPrefType.integer, EARole.admin, true, 7, true),

    tipoCancellazione(TIPO_CANCELLAZIONE, "Possibili modalità per un milite di cancellarsi da un turno già segnato", EAPrefType.enumeration, EARole.admin, true, EACancellazione.mai, true),

    numeroOreTrascorse(NUMERO_ORE_TRASCORSE, "Ore massime di tempo per poter cancellare un'iscrizione dopo averla effettuata", EAPrefType.integer, EARole.admin, true, 2, true),

    numeroGiorniMancanti(NUMERO_GIORNI_MANCANTI, "Giorni massimi mancanti all'esecuzione del turno per potersi cancellare", EAPrefType.integer, EARole.admin, true, 2, true),

    caratteriVisibili(NUMERO_CARATTERI_VISIBILI, "Numero di caratteri del nome del milite nel tabellone (0=solo cognome; -1=nome intero)", EAPrefType.integer, EARole.admin, true, true, true),

    ;


    //--codice di riferimento. Se è true il flag companySpecifica, contiene anche il code della company come prefisso.
    private String code;

    private String desc;

    private EAPrefType type;

    private Object value;

    private EARole show;

    private boolean visibileDialogo;


    //--usa un prefisso col codice della company
    private boolean companySpecifica;


    EAPreferenzaWam(String code, String desc, EAPrefType type, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setShow(EARole.developer);
        this.setCompanySpecifica(true);
        this.setValue(value);
        this.setVisibileDialogo(false);
    }// fine del costruttore


    EAPreferenzaWam(String code, String desc, EAPrefType type, Object value, boolean visibileDialogo) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setShow(EARole.developer);
        this.setCompanySpecifica(true);
        this.setValue(value);
        this.setVisibileDialogo(visibileDialogo);
    }// fine del costruttore


    EAPreferenzaWam(String code, String desc, EAPrefType type, EARole show, boolean companySpecifica, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setShow(show);
        this.setCompanySpecifica(companySpecifica);
        this.setValue(type != EAPrefType.enumeration ? value : ((IAEnum) value).getPref());
        this.setVisibileDialogo(false);
    }// fine del costruttore


    EAPreferenzaWam(String code, String desc, EAPrefType type, EARole show, boolean companySpecifica, Object value, boolean visibileDialogo) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setShow(show);
        this.setCompanySpecifica(companySpecifica);
        this.setValue(type != EAPrefType.enumeration ? value : ((IAEnum) value).getPref());
        this.setVisibileDialogo(visibileDialogo);
    }// fine del costruttore


    public String getCode() {
        return code;
    }// end of method


    public void setCode(String code) {
        this.code = code;
    }// end of method


    public String getDesc() {
        return desc;
    }// end of method


    public void setDesc(String desc) {
        this.desc = desc;
    }// end of method


    public EAPrefType getType() {
        return type;
    }// end of method


    public void setType(EAPrefType type) {
        this.type = type;
    }// end of method


    public boolean isCompanySpecifica() {
        return companySpecifica;
    }// end of method


    public void setCompanySpecifica(boolean companySpecifica) {
        this.companySpecifica = companySpecifica;
    }// end of method


    public EARole getShow() {
        return show;
    }// end of method


    public void setShow(EARole show) {
        this.show = show;
    }// end of method


    public Object getValue() {
        return value;
    }// end of method


    public void setValue(Object value) {
        this.value = value;
    }// end of method


    public boolean isVisibileDialogo() {
        return visibileDialogo;
    }


    public void setVisibileDialogo(boolean visibileDialogo) {
        this.visibileDialogo = visibileDialogo;
    }
} // end of enumeration
