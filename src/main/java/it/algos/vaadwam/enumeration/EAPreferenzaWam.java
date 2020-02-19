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
    usaDaemonCroce(USA_DAEMON_CROCE, "Crono per download completo della croce specifica", EAPrefType.bool, false),

//    //--cronologia degli import
    lastImportFunzioni(LAST_IMPORT_FUNZIONI, "Data ultimo import funzioni della croce", EAPrefType.localdatetime, null),
    lastImportServizi(LAST_IMPORT_SERVIZI, "Data ultimo import servizi della croce", EAPrefType.localdatetime, null),
    lastImportMiliti(LAST_IMPORT_MILITI, "Data ultimo import militi della croce", EAPrefType.localdatetime, null),
    lastImportTurni(LAST_IMPORT_TURNI, "Data ultimo import turni della croce", EAPrefType.localdatetime, null),

    //--durata degli import
    durataImportFunzioni(DURATA_IMPORT_FUNZIONI, "Durata ultimo import funzioni della croce, in secondi", EAPrefType.integer, 0),
    durataImportServizi(DURATA_IMPORT_SERVIZI, "Durata ultimo import servizi della croce, in secondi", EAPrefType.integer, 0),
    durataImportMiliti(DURATA_IMPORT_MILITI, "Durata ultimo import militi della croce, in secondi", EAPrefType.integer, 0),
    durataImportTurni(DURATA_IMPORT_TURNI, "Durata ultimo import turni della croce, in secondi", EAPrefType.integer, 0),

    usaMailImport(USA_MAIL_IMPORT, "Spedisce una mail ad ogni import", EAPrefType.bool, EARole.developer, true, true),
    usaColorazioneDifferenziata(USA_COLORAZIONE_DIFFERENZIATA, "Nel tabellone colori differenziati per le singole iscrizioni di un turno", EAPrefType.bool, EARole.admin, true, false),
    usaFieldsEnabledInShow(USA_FIELDS_ENABLED_IN_SHOW, "Mostra i fields abilitati anche nel Form per gli Users", EAPrefType.bool, EARole.developer, false, true),
    usaCheckFunzioniMilite(USA_CHECK_FUNZIONI_MILITE, "Mostra le funzioni abilitate di un Milite tramite checkbox", EAPrefType.bool, EARole.developer, false, false),
    //    tipoCancellazione(TIPO_CANCELLAZIONE, "Modalità di cancellazione possibili", EAPrefType.enumeration, EARole.developer, false, EACancellazione.getValuesStandard()),
    mostraOrarioServizio(MOSTRA_ORARIO_SERVIZIO, "Mostra l'orario del servizio nel dialogo di iscrizione al turno", EAPrefType.bool, EARole.admin, false, true),
    mostraLegenda(MOSTRA_LEGENDA_TABELLONE, "Mostra la legenda in calce al tabellone", EAPrefType.bool, EARole.admin, true, true),
    nuovoTurno(CREAZIONE_NUOVO_TURNO_DA_UTENTE, "L'utente può creare un nuovo turno vuoto", EAPrefType.bool, EARole.admin, true, true),

    numeroOreTurnoStandard(NUMERO_ORE_TURNO_STANDARD, "Ore convenzionali per tramutare un turno in ore", EAPrefType.integer, 7),

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
        this.setShow(EARole.developer);
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
