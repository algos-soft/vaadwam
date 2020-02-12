package it.algos.vaadwam.enumeration;


import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.enumeration.IAEnum;
import it.algos.vaadflow.modules.preferenza.IAPreferenza;
import it.algos.vaadflow.modules.role.EARole;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenzaWam implements IAPreferenza {

    //--generali
    redirectTabellone(SECURED_VIEW_REDIRECT_TABELLONE, "Reidirizza al tabellone in caso accesso non consentito ad una view", EAPrefType.bool, EARole.developer, false, true),

    //--scheduled task suddivise per croce
    usaDaemonCroceGAPS(USA_DAEMON_CROCE_GAPS, "Crono per download completo croce GAPS", EAPrefType.bool, EARole.developer, false, false),
    usaDaemonCroceCRF(USA_DAEMON_CROCE_CRF, "Crono per download completo croce CRF", EAPrefType.bool, EARole.developer, false, false),
    usaDaemonCroceCRPT(USA_DAEMON_CROCE_CRPT, "Crono per download completo croce CRPT", EAPrefType.bool, EARole.developer, false, false),
    usaDaemonCrocePAP(USA_DAEMON_CROCE_PAP, "Crono per download completo croce PAP", EAPrefType.bool, EARole.developer, false, false),

    //--scheduled task-old
    usaDaemonCroci(USA_DAEMON_CROCI, "Crono (deprecated) per download croci", EAPrefType.bool, EARole.developer, false, false),
    usaDaemonFunzioni(USA_DAEMON_FUNZIONI, "Crono (deprecated) per download funzioni", EAPrefType.bool, EARole.developer, false, true),
    usaDaemonServizi(USA_DAEMON_SERVIZI, "Crono (deprecated) per download servizi", EAPrefType.bool, EARole.developer, false, true),
    usaDaemonMiliti(USA_DAEMON_MILITI, "Crono (deprecated) per download militi", EAPrefType.bool, EARole.developer, false, true),
    usaDaemonTurni(USA_DAEMON_TURNI, "Crono (deprecated) per download turni", EAPrefType.bool, EARole.developer, false, true),

    //--cronologia degli import
    lastImportFunzioni(LAST_IMPORT_FUNZIONI, "Data ultimo import funzioni della croce", EAPrefType.localdatetime, EARole.developer, true, null),
    lastImportServizi(LAST_IMPORT_SERVIZI, "Data ultimo import servizi della croce", EAPrefType.localdatetime, EARole.developer, true, null),
    lastImportMiliti(LAST_IMPORT_MILITI, "Data ultimo import militi della croce", EAPrefType.localdatetime, EARole.developer, true, null),
    lastImportTurni(LAST_IMPORT_TURNI, "Data ultimo import turni della croce", EAPrefType.localdatetime, EARole.developer, true, null),


    durataImportFunzioni(DURATA_IMPORT_FUNZIONI, "Durata ultimo import funzioni della croce, in secondi", EAPrefType.integer, 0),
    durataImportServizi(DURATA_IMPORT_SERVIZI, "Durata ultimo import servizi della croce, in secondi", EAPrefType.integer, 0),
    durataImportMiliti(DURATA_IMPORT_MILITI, "Durata ultimo import militi della croce, in secondi", EAPrefType.integer, 0),
    durataImportTurni(DURATA_IMPORT_TURNI, "Durata ultimo import turni della croce, in secondi", EAPrefType.integer, 0),


    //--cronologia degli import-old
    lastDownloadCroci(LAST_IMPORT_CROCI, "Data ultimo import di croci", EAPrefType.localdatetime, EARole.developer, false, null),
    lastDownloadFunzioni(LAST_IMPORT_FUNZIONI, "Data ultimo import di funzioni", EAPrefType.localdatetime, EARole.developer, false, null),
    lastDownloadServizi(LAST_IMPORT_SERVIZI, "Data ultimo import di servizi", EAPrefType.localdatetime, EARole.developer, false, null),
    lastDownloadMiliti(LAST_IMPORT_MILITI, "Data ultimo import di militi", EAPrefType.localdatetime, EARole.developer, false, null),
    lastDownloadTurni(LAST_IMPORT_TURNI, "Data ultimo import di turni", EAPrefType.localdatetime, EARole.developer, false, null),

    usaMailImport(USA_MAIL_IMPORT, "Spedisce una mail ad ogni import", EAPrefType.bool, EARole.developer, true, true),
    usaColorazioneDifferenziata(USA_COLORAZIONE_DIFFERENZIATA, "Nel tabellone colori differenziati per le singole iscrizioni di un turno", EAPrefType.bool, EARole.admin, true, false),
    usaFieldsEnabledInShow(USA_FIELDS_ENABLED_IN_SHOW, "Mostra i fields abilitati anche nel Form per gli Users", EAPrefType.bool, EARole.developer, false, true),
    usaCheckFunzioniMilite(USA_CHECK_FUNZIONI_MILITE, "Mostra le funzioni abilitate di un Milite tramite checkbox", EAPrefType.bool, EARole.developer, false, false),
    //    tipoCancellazione(TIPO_CANCELLAZIONE, "Modalità di cancellazione possibili", EAPrefType.enumeration, EARole.developer, false, EACancellazione.getValuesStandard()),
    mostraOrarioServizio(MOSTRA_ORARIO_SERVIZIO, "Mostra l'orario del servizio nel dialogo di iscrizione al turno", EAPrefType.bool, EARole.admin, false, true),
    mostraLegenda(MOSTRA_LEGENDA_TABELLONE, "Mostra la legenda in calce al tabellone", EAPrefType.bool, EARole.admin, true, true),
    nuovoTurno(CREAZIONE_NUOVO_TURNO_DA_UTENTE, "L'utente può creare un nuovo turno vuoto", EAPrefType.bool, EARole.admin, true, true),
    ;


    //--codice di riferimento. Se è true il flag companySpecifica, contiene anche il code della company come prefisso.
    private String code;

    private String desc;

    private EAPrefType type;

    private Object value;

    private EARole show;


    //--usa un prefisso col codice della company
    private boolean companySpecifica;


    EAPreferenzaWam(String code, String desc, EAPrefType type, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setCompanySpecifica(true);
        this.setValue(value);
    }// fine del costruttore


    EAPreferenzaWam(String code, String desc, EAPrefType type, EARole show, boolean companySpecifica, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setShow(show);
        this.setCompanySpecifica(companySpecifica);
        this.setValue(type != EAPrefType.enumeration ? value : ((IAEnum) value).getPref());
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

} // end of enumeration
