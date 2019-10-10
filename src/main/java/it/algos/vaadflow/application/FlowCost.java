package it.algos.vaadflow.application;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLayout;
import it.algos.vaadflow.ui.MainLayout;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 13-ago-2019
 * Time: 10:50
 * <p>
 * Classe statica (astratta) per le costanti generali dell'applicazione <br>
 * Le costanti (static) sono uniche per tutta l'applicazione <br>
 * Il valore delle costanti (final) è immutabile una volta regolato qui <br>
 * Per omogeneità è meglio usare 'static final' (piuttosto che l'equivalente 'final static') <br>
 */
public abstract class FlowCost {

    public static final String DEVELOPER_COMPANY = "Algos® ";

    public static final String TAG_LOG = "log";

    public static final String TAG_LOGIN = "alogin";

    public static final String TAG_TYP = "logtype";

    public static final String TAG_UTE = "utente";

    public static final String TAG_VER = "versione";

    public static final String TAG_SEC = "secolo";

    public static final String TAG_MES = "mese";

    public static final String TAG_ANN = "anno";

    public static final String TAG_GIO = "giorno";

    public static final Locale APP_LOCALE = Locale.US;

    public static final String VUOTA = "";

    public static final String SPAZIO = " ";

    public static final String VIRGOLA = ",";

    public static final String A_CAPO = "\n";

    public static final String ASTERISCO = "*";

    public static final Class<? extends RouterLayout> LAYOUT = MainLayout.class;

    public static final String LAYOUT_NAME = "MainLayout";

    public static final String VIEWPORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes";

    public static final int FLASH = 2000;

    public static final String TAG_HOME = "home";

    public static final String TAG_WIZ = "wizard";

    public static final String TAG_DEV = "developer";

    public static final String TAG_ADD = "address";

    public static final String TAG_PER = "person";

    public static final String TAG_COM = "company";

    public static final String TAG_ROL = "role";

    public static final String TAG_PRE = "preferenza";

    public static final String PAGE_ROOT = "";

    public static final String PAGE_WIZARD = "wizard";

    public static final String PAGE_ADDRESS = "address";

    public static final String PAGE_ADDRESS_EDIT = "address/edit";

    public static final String PAGE_STOREFRONT = "storefront";

    public static final String PAGE_STOREFRONT_EDIT = "storefront/edit";

    public static final String PAGE_COMPANY = "company";

    public static final String PAGE_USERS = "users";

    public static final String PAGE_PRODUCTS = "products";

    public static final String PAGE_LOGOUT = "logout";

    public static final String PAGE_NOTFOUND = "404";

    public static final String PAGE_DEFAULT = PAGE_COMPANY;

    public static final String PAGE_ACCESS_DENIED = "access-denied";

    public static final String ICON_WIZARD = "magic";

    public static final String ICON_STOREFRONT = "edit";

    public static final String ICON_DASHBOARD = "clock";

    public static final String ICON_USERS = "user";

    public static final String ICON_PRODUCTS = "calendar";

    public static final String ICON_LOGOUT = "exit";

    public static final String TITLE_WIZARD = "Wizard";

    public static final String TITLE_STOREFRONT = "Storefront";

    public static final String TITLE_DASHBOARD = "Dashboard";

    public static final String TITLE_USERS = "Users";

    public static final String TITLE_PRODUCTS = "Products";

    public static final String TITLE_LOGOUT = "Logout";

    public static final String TITLE_NOT_FOUND = "Page was not found";

    public static final String TITLE_ACCESS_DENIED = "Access denied";

    public static final String[] ORDER_SORT_FIELDS = {"dueDate", "dueTime", "id"};

    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    public static final String FLAG_TEXT_SEARCH = "textSearch";

    public static final String FLAG_TEXT_NEW = "textNew";
    public static final String FLAG_TEXT_SHOW = "textShow";
    public static final String FLAG_TEXT_EDIT = "textEdit";

    public static final String BOT_ACCETTA = "Accetta";

    public static final String BOT_CONFERMA = "Conferma";

    public static final String BOT_ANNULLA = "Annulla";

    public static final String BOT_BACK = "Back";

    public static final String BOT_CREATE = "New";

    public static final String BOT_DELETE = "Elimina";

    public static final String BOT_EDIT = "Edit";

    public static final String BOT_SHOW = "Show";

    public static final String BOT_IMAGE = "immagine";

    public static final String BOT_IMPORT = "import";

    public static final String BOT_LINK_ACCETTA = "linkaccetta";

    public static final String BOT_LINK_REGISTRA = "linkregistra";

    public static final String BOT_REGISTRA = "registra";

    public static final String BOT_REVERT = "revert";

    public static final String BOT_SEARCH = "ricerca";

    public static final String BOT_SHOW_ALL = "tutto";

    public static final String BOT_LINK = "botlink";

    public static final String BOT_CHOOSER = "apri";

    public static final String BUTTON_NORMAL_WIDTH = "8em";

    public static final String BUTTON_ICON_WIDTH = "3em";

    public static final String PROPERTY_ID = "id";

    public static final String PROPERTY_COMPANY = "company2";

    public static final String PROPERTY_SERIAL = "serialVersionUID";

    public static final String PROPERTY_ORDINE = "ordine";

    public static final String PROPERTY_NOTE = "note";

    public static final String PROPERTY_CREAZIONE = "creazione";

    public static final String PROPERTY_MODIFICA = "modifica";

    public static final String COMPANY_CODE = "code";

    public static final String COMPANY_UNICO = "codeCompanyUnico";

    //--bottoni della scheda/form/dialog
    public static final String REGISTRA = "Save";

    public static final String ANNULLA = "Back";

    public static final String CANCELLA = "Delete";

    public static final String DELETE = "Delete";

    public static final String CONFERMA = "Conferma";

    //log type
    public static final String LOG_SETUP = "logSetup";

    public static final String LOG_NEW = "logNew";

    public static final String LOG_EDIT = "logEdit";

    public static final String LOG_DELETE = "logDelete";

    public static final String LOG_DEBUG = "logDebug";

    public static final String LOG_INFO = "logInfo";

    public static final String LOG_WARN = "logWarn";

    public static final String LOG_ERROR = "logError";

    public static final String LOG_IMPORT = "logImport";

    // generali
    public static final String USA_DEBUG = "usaDebug";

    public static final String USA_LOG_DEBUG = "usaLogDebug";

    public static final String USA_COMPANY = "usaCompany";

    public static final String USA_LOG_MAIL = "usaLogMail";

    public static final String MAIL_FROM = "mailFrom";

    public static final String MAIL_TO = "mailTo";

    public static final String MAX_RIGHE_GRID = "maxRigheGrid";
    public static final String MAX_RIGHE_GRID_CLICK = "maxRigheGridClick";

    public static final String MONGO_PAGE_LIMIT = "mongoPageLimit";

    public static final String USA_MENU = "usaMenu";

    public static final String USA_SEARCH_CASE_SENSITIVE = "usaSearchCaseSensitive";

    public static final String USA_BUTTON_SHORTCUT = "usaButtonShortcut";

    public static final String USA_GRID_HEADER_PRIMA_MAIUSCOLA = "usaGridHeaderPrimaMaiuscola";
    public static final String USA_TEXT_EDIT_BUTTON = "usaTextEditButton";
    public static final String USA_EDIT_BUTTON = "usaEditButton";

    // moduli visibili
    public static final String SHOW_COMPANY = "showCompany";

    public static final String SHOW_PREFERENZA = "showPreferenza";

    public static final String SHOW_WIZARD = "showWizard";

    public static final String SHOW_DEVELOPER = "showDeveloper";

    public static final String SHOW_ADDRESS = "showAddress";

    public static final String SHOW_PERSON = "showPerson";

    public static final String SHOW_ROLE = "showRole";

    public static final String SHOW_VERSION = "showVersion";

    public static final String SHOW_USER = "showUser";

    public static final String SHOW_LOG = "showLog";

    public static final String SHOW_LOGTYPE = "showLogType";

    public static final String SHOW_SECOLO = "showSecolo";

    public static final String SHOW_ANNO = "showAnno";

    public static final String SHOW_MESE = "showMese";

    public static final String SHOW_GIORNO = "showGiorno";

    public static final String SHOW_ACCOUNT_ON_MENU = "showAccount";

    public static final String LOAD_UTENTI = "loadUtenti";

    public static final String STOP_SAVE = "nonRegistrare";

    //--chiavi mappa costruzione giorni
    public static final String PRIMO_GIORNO_MESE = "1º";

    public static final String KEY_MAPPA_GIORNI_MESE_NUMERO = "meseNumero";

    public static final String KEY_MAPPA_GIORNI_MESE_TESTO = "meseTesto";

    public static final String KEY_MAPPA_GIORNI_NORMALE = "normale";

    public static final String KEY_MAPPA_GIORNI_BISESTILE = "bisestile";

    public static final String KEY_MAPPA_GIORNI_NOME = "nome";

    public static final String KEY_MAPPA_GIORNI_TITOLO = "titolo";

    public static final String KEY_MAPPA_GIORNI_MESE_MESE = "meseMese";

    public static final String KEY_MAPPA_HEADER = "header";

    public static final String KEY_MAPPA_BODY = "body";

    public static final String KEY_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public static final String KEY_LOGGED_USER = "loggedUser";

    public static final String KEY_UNIQUE_USER_NAME = "uniqueUserName";

    public static final String KEY_CONTEXT = "context";

    //--date
    public static final Locale LOCALE = Locale.ITALIAN;

    public static final String TAG_SEARCH = "search";

    public static final VaadinIcon VAADIN_ICON_DA_NON_USARE = VaadinIcon.VAADIN_H;

    private static final String[] esclusiAll = {PROPERTY_SERIAL, PROPERTY_CREAZIONE, PROPERTY_MODIFICA};

    public static final List<String> ESCLUSI_ALL = Arrays.asList(esclusiAll);

    private static final String[] esclusiList = {PROPERTY_ID, PROPERTY_SERIAL, PROPERTY_COMPANY, PROPERTY_NOTE, PROPERTY_CREAZIONE, PROPERTY_MODIFICA};

    public static final List<String> ESCLUSI_LIST = Arrays.asList(esclusiList);

    private static final String[] esclusiForm = {PROPERTY_ID, PROPERTY_SERIAL, PROPERTY_COMPANY, PROPERTY_CREAZIONE, PROPERTY_MODIFICA};

    public static final List<String> ESCLUSI_FORM = Arrays.asList(esclusiForm);

    private static final String[] esclusiMatrice = {PROPERTY_SERIAL, PROPERTY_ID, PROPERTY_COMPANY};

    public static final List<String> ESCLUSI = Arrays.asList(esclusiMatrice);

    private static final String[] companyMatrice = {COMPANY_CODE, COMPANY_UNICO};

    public static final List<String> COMPANY_OPTIONAL = Arrays.asList(companyMatrice);


}// end of static class