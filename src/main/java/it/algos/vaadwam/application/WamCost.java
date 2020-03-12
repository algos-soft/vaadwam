package it.algos.vaadwam.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Completa la classe BaseCost con le costanti statiche specifiche di questa applicazione <br>
 * <p>
 * Not annotated with @SpringComponent (inutile) <br>
 * Not annotated with @Scope (inutile) <br>
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 * CRPT - Michelini Mauro: ginevracrpt
 * CRF - Porcari Stefano: 7777
 * PAP - Piana Silvano: piana987
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovrascrivibile = false)
public class WamCost {

    public final static String TAG_STA = "statistica";

    public final static String TAG_RIG = "riga";

    public final static String TAG_TUR = "turno";

    public final static String KEY_WAM_CONTEXT = "wamcontext";

    public final static String TAG_TURNO_EDIT = "turnoedit";

    public final static String TAG_TURNO_EDIT_OLD = "turnoeditold";

    public final static String TAG_TURNO_EDIT_UNO = TAG_TURNO_EDIT + "uno";

    public final static String TAG_TURNO_EDIT_DUE = TAG_TURNO_EDIT + "due";

    public final static String TAG_TURNO_EDIT_TRE = TAG_TURNO_EDIT + "tre";

    public final static String TAG_TURNO_EDIT_QUATTRO = TAG_TURNO_EDIT + "quattro";

    public final static String TAG_TURNO_EDIT_CINQUE = TAG_TURNO_EDIT + "cinque";

    public final static String TAG_SELEZIONE = "selezione";

    public final static String TAG_WAM_LOGIN = "wamlogin";

    public final static String TAG_TUR_EDIT = "turnoEdit";

    public final static String TAG_TUR_EDIT_VIEW = "turnoEditView";

    public final static String TAG_MIL = "milite";

    public final static String TAG_ISC = "iscrizione";

    public final static String TAG_SER = "servizio";

    public final static String TAG_CRO = "croce";

    public final static String TAG_FUN = "funzione";

    public final static String TAG_IMP = "import";

    public final static String TAG_TAB = "tabellonesuperato";

    public final static String TAG_TAB_LIST = "";

    public final static String TAG_TAB_GRID = "nonFunziona";

    public final static String TAG_WAMDEV = "wamdeveloper";

    //--daemons
    public final static String USA_DAEMON_CROCE_GAPS = "usaDaemonCroceGaps";

    public final static String USA_DAEMON_CROCE_CRF = "usaDaemonCroceCrf";

    public final static String USA_DAEMON_CROCE_CRPT = "usaDaemonCroceCrpt";

    public final static String USA_DAEMON_CROCE_PAP = "usaDaemonCrocePap";

    public final static String USA_DAEMON_CROCE = "usaDaemonCroce";

    public final static String USA_DAEMON_ELABORA = "usaDaemonElabora";

    public final static String USA_DAEMON_CROCI = "usaDaemonCroci";

    public final static String USA_DAEMON_FUNZIONI = "usaDaemonFunzioni";

    public final static String USA_DAEMON_SERVIZI = "usaDaemonServizi";

    public final static String USA_DAEMON_MILITI = "usaDaemonMiliti";

    public final static String USA_DAEMON_TURNI = "usaDaemonTurni";


    public final static String LAST_IMPORT_FUNZIONI = "lastImportFunzioni";

    public final static String LAST_ELABORA = "lastElabora";


    public final static String LAST_IMPORT_SERVIZI = "lastImportServizi";


    public final static String LAST_IMPORT_MILITI = "lastImportMiliti";


    public final static String LAST_IMPORT_TURNI = "lastImportTurni";


    public final static String DURATA_IMPORT_FUNZIONI = "durataImportFunzioni";


    public final static String DURATA_IMPORT_SERVIZI = "durataImportServizi";


    public final static String DURATA_IMPORT_MILITI = "durataImportMiliti";


    public final static String DURATA_IMPORT_TURNI = "durataImportTurni";

    public final static String DURATA_ELABORA = "durataElabora";


    public final static String LAST_IMPORT_CROCI = "lastImportCroci";

    public final static String NUMERO_ORE_TURNO_STANDARD = "numeroOreTurno";


    public final static String TASK_CRO = "taskCroci";

    public final static String TASK_CROCE = "taskCroce";

    public final static String TASK_STATISTICA = "taskStatistica";

    public final static String TASK_FUN = "taskFunzioni";

    public final static String TASK_SER = "taskServizi";

    public final static String TASK_MIL = "taskMiliti";

    public final static String TASK_TUR = "taskTurni";

    //--tag per i logs
    public final static String IMPORT_MILITI = "ImportMilite";

    public final static String IMPORT_TURNI = "ImportTurno";

    //--properties
    public final static String PROPERTY_ID = "id";

    public final static String PROPERTY_CODE = "code";

    public final static String PROPERTY_CROCE = "croce";

    //--preferenze
    public static final String SECURED_VIEW_REDIRECT_TABELLONE = "redirectTabellone";

    public final static String USA_MAIL_IMPORT = "usaMailImport";

    public final static String USA_COLORAZIONE_TURNI = "usaColorazioneTurni";

    public final static String USA_COLORAZIONE_DIFFERENZIATA = "usaColorazioneDifferenziata";

    public final static String USA_FIELDS_ENABLED_IN_SHOW = "usaFieldsEnabledInShow";

    public final static String USA_CHECK_FUNZIONI_MILITE = "usaCheckFunzioniMilite";

    public final static String TIPO_CANCELLAZIONE = "tipoCancellazione";

    public final static String MOSTRA_ORARIO_SERVIZIO = "mostraOrarioServizio";

    public final static String MOSTRA_LEGENDA_TABELLONE = "mostraLegendaTabellone";

    public final static String CREAZIONE_NUOVO_TURNO_DA_UTENTE = "creazioneNuovoTurnoDaUtente";

    //--mappa @Route
    public final static String KEY_MAP_GIORNO = "giorno";

    public final static String KEY_MAP_SERVIZIO = "servizio";

    public final static String KEY_MAP_GIORNO_INIZIO = "giornoInizio";

    public final static String KEY_MAP_GIORNO_FINE = "giornoFine";

    public final static String KEY_MAP_GIORNI_DURATA = "giorniDurata";

    public final static List<Integer> ANNI = new ArrayList<Integer>(Arrays.asList(2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020));

}// end of static class