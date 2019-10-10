package it.algos.vaadwam.enumeration;


import it.algos.vaadflow.modules.preferenza.EAPrefType;
import it.algos.vaadflow.modules.preferenza.IAPreferenza;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenzaWam implements IAPreferenza {

    usaDaemonCroci(USA_DAEMON_CROCI, "Crono per download croci", EAPrefType.bool, true),
    usaDaemonFunzioni(USA_DAEMON_FUNZIONI, "Crono per download funzioni", EAPrefType.bool, true),
    usaDaemonServizi(USA_DAEMON_SERVIZI, "Crono per download servizi", EAPrefType.bool, true),
    usaDaemonMiliti(USA_DAEMON_MILITI, "Crono per download militi", EAPrefType.bool, false),
    usaDaemonTurni(USA_DAEMON_TURNI, "Crono per download turni", EAPrefType.bool, false),

    lastDownloadCroci(LAST_IMPORT_CROCI, "Data ultimo import di croci", EAPrefType.date, null),
    lastDownloadFunzioni(LAST_IMPORT_FUNZIONI, "Data ultimo import di funzioni", EAPrefType.date, null),
    lastDownloadServizi(LAST_IMPORT_SERVIZI, "Data ultimo import di servizi", EAPrefType.date, null),
    lastDownloadMiliti(LAST_IMPORT_MILITI, "Data ultimo import di militi", EAPrefType.date, null),
    lastDownloadTurni(LAST_IMPORT_TURNI, "Data ultimo import di turni", EAPrefType.date, null),

    usaMailImport(USA_MAIL_IMPORT, "Spedisce una mail ad ogni import", EAPrefType.bool, true),
    usaColorazioneDifferenziata(USA_COLORAZIONE_DIFFERENZIATA, "Nel tabellone colori differenziati per le singole iscrizioni di un turno", EAPrefType.bool, false),
    usaFieldsEnabledInShow(USA_FIELDS_ENABLED_IN_SHOW, "Mostra i fields abilitati anche nel Form per gli Users", EAPrefType.bool, true),
    usaCheckFunzioniMilite(USA_CHECK_FUNZIONI_MILITE, "Mostra le funzioni abilitate di un Milite tramite checkbox", EAPrefType.bool, false),
    tipoCancellazione(TIPO_CANCELLAZIONE, "Modalità di cancellazione possibili", EAPrefType.enumeration, "mai,sempre,fino,dopo;sempre"),
    mostraOrarioServizio(MOSTRA_ORARIO_SERVIZIO, "Mostra l'orario del servizio nel dialogo di iscrizione al turno", EAPrefType.bool, true),
    ;


    private String code;

    private String desc;

    private EAPrefType type;

    private Object value;


    EAPreferenzaWam(String code, String desc, EAPrefType type, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setValue(value);
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


    public Object getValue() {
        return value;
    }// end of method


    public void setValue(Object value) {
        this.value = value;
    }// end of method

} // end of enumeration
