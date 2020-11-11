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

    public final static String TAG_WAM_LOG = "wamLog";

    public final static String TAG_STA = "statistica";

    public final static String TAG_RIG = "riga";

    public final static String TAG_TUR = "turno";

    public final static String KEY_WAM_CONTEXT = "wamContext";

    public final static String TAG_TURNO_EDIT = "turnoEdit";

    public final static String TAG_TURNO_EDIT_OLD = "turnoEditOld";

    public final static String TAG_TURNO_EDIT_UNO = TAG_TURNO_EDIT + "uno";

    public final static String TAG_TURNO_EDIT_DUE = TAG_TURNO_EDIT + "due";

    public final static String TAG_TURNO_EDIT_TRE = TAG_TURNO_EDIT + "tre";

    public final static String TAG_TURNO_EDIT_QUATTRO = TAG_TURNO_EDIT + "quattro";

    public final static String TAG_TURNO_EDIT_CINQUE = TAG_TURNO_EDIT + "cinque";

    public final static String TAG_SELEZIONE = "selezione";

    public final static String TAG_WAM_LOGIN = "wamLogin";

    public final static String TAG_TUR_EDIT = "turnoEdit";

    public final static String TAG_TUR_EDIT_VIEW = "turnoEditView";

    public final static String TAG_MIL = "milite";

    public final static String TAG_ISC = "iscrizione";

    public final static String TAG_ISC_TUR = "iscrizioneTurno";

    public final static String TAG_SER = "servizio";

    public final static String TAG_CRO = "croce";

    public final static String TAG_FUN = "funzione";

    public final static String TAG_IMP = "import";

    public final static String TAG_TAB = "tabelloneSuperato";

    public final static String TAG_TAB_LIST = "";

    public final static String TAG_TAB_GRID = "nonFunziona";

    public final static String TAG_WAM_DEV = "wamDeveloper";

    //--daemons
    //    public final static String USA_DAEMON_IMPORT_GAPS = "usaDaemonCroceGaps";
    //
    //    public final static String USA_DAEMON_IMPORT_CRF = "usaDaemonCroceCrf";
    //
    //    public final static String USA_DAEMON_IMPORT_CRPT = "usaDaemonCroceCrpt";
    //
    //    public final static String USA_DAEMON_IMPORT_PAP = "usaDaemonCrocePap";

    //    public final static String USA_DAEMON_CROCI = "usaDaemonCroci";

    public final static String USA_DAEMON_IMPORT = "usaDaemonImport";

    public final static String USA_DAEMON_STATISTICHE = "usaDaemonStatistiche";


    //    public final static String USA_DAEMON_FUNZIONI = "usaDaemonFunzioni";
    //
    //    public final static String USA_DAEMON_SERVIZI = "usaDaemonServizi";
    //
    //    public final static String USA_DAEMON_MILITI = "usaDaemonMiliti";
    //
    //    public final static String USA_DAEMON_TURNI = "usaDaemonTurni";

    public final static String LAST_IMPORT_CROCI = "lastImportCroci";

    public final static String LAST_IMPORT_FUNZIONI = "lastImportFunzioni";

    public final static String LAST_ELABORA = "lastElabora";

    public final static String LAST_IMPORT_SERVIZI = "lastImportServizi";

    public final static String LAST_IMPORT_MILITI = "lastImportMiliti";

    public final static String LAST_IMPORT_TURNI = "lastImportTurni";

    public final static String DURATA_IMPORT_CROCI = "durataImportCroci";

    public final static String DURATA_IMPORT_FUNZIONI = "durataImportFunzioni";

    public final static String DURATA_IMPORT_SERVIZI = "durataImportServizi";

    public final static String DURATA_IMPORT_MILITI = "durataImportMiliti";

    public final static String DURATA_IMPORT_TURNI = "durataImportTurni";

    public final static String DURATA_ELABORA = "durataElabora";

    //flag
    public final static String NUMERO_ORE_TURNO_STANDARD = "numeroOreTurno";

    public final static String NUMERO_ORE_TRASCORSE = "numeroOreTrascorse";

    public final static String NUMERO_GIORNI_MANCANTI = "numeroGiorniMancanti";

    public final static String DISABILITA_LOGIN = "disabilita login";


    public final static String TASK_CRO = "taskCroci";

    public final static String TASK_CROCE = "taskCroce";

    public final static String TASK_STATISTICA = "taskStatistica";

    public final static String TASK_IMPORT = "taskImport";

    //    public final static String TASK_SER = "taskServizi";
    //
    //    public final static String TASK_MIL = "taskMiliti";
    //
    //    public final static String TASK_TUR = "taskTurni";

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

    public final static String COGNOME_PRIMA_DEL_NOME = "cognomePrimaDelNome";

    public final static String NUMERO_CARATTERI_VISIBILI = "caratteriVisibili";

    //--mappa @Route
    public final static String KEY_MAP_GIORNO = "giorno";

    public final static String KEY_MAP_SERVIZIO = "servizio";

    public final static String KEY_MAP_GIORNO_INIZIO = "giornoInizio";

    public final static String KEY_MAP_GIORNO_FINE = "giornoFine";

    public final static String KEY_MAP_GIORNI_DURATA = "giorniDurata";

    public final static List<Integer> ANNI = new ArrayList<Integer>(Arrays.asList(2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020));

    public final static String LUMO_PRIMARY_COLOR = "#1676F3";  // non riesco a reciperarlo dal context allora visto che so qual è lo ridefinisco qui

    public final static String BROWSER_TAB_TITLE = "WAM";

}// end of static class