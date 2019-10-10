package it.algos.vaadflow.enumeration;


import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
import it.algos.vaadflow.modules.preferenza.IAPreferenza;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenza implements IAPreferenza {

    usaDebug(FlowCost.USA_DEBUG, "Flag generale di debug (ce ne possono essere di specifici, validi solo se questo è vero)", EAPrefType.bool, false),
    usaLogDebug(FlowCost.USA_LOG_DEBUG, "Uso del log di registrazione per il livello debug. Di default false.", EAPrefType.bool, false),
    usaCompany(FlowCost.USA_COMPANY, "L'applicazione è multiCompany", EAPrefType.bool, false),
    showCompany(FlowCost.SHOW_COMPANY, show(FlowCost.TAG_COM), EAPrefType.bool, true),
    showPreferenza(FlowCost.SHOW_PREFERENZA, show(FlowCost.TAG_PRE), EAPrefType.bool, true),
    showWizard(FlowCost.SHOW_WIZARD, show(FlowCost.TAG_WIZ), EAPrefType.bool, true),
    showDeveloper(FlowCost.SHOW_DEVELOPER, show(FlowCost.TAG_DEV), EAPrefType.bool, true),
    showAddress(FlowCost.SHOW_ADDRESS, show(FlowCost.TAG_ADD), EAPrefType.bool, true),
    showPerson(FlowCost.SHOW_PERSON, show(FlowCost.TAG_PER), EAPrefType.bool, true),
    showRole(FlowCost.SHOW_ROLE, show(FlowCost.TAG_ROL), EAPrefType.bool, true),
    showUser(FlowCost.SHOW_USER, show(FlowCost.TAG_UTE), EAPrefType.bool, true),
    showVersione(FlowCost.SHOW_VERSION, show(FlowCost.TAG_VER), EAPrefType.bool, true),
    showLog(FlowCost.SHOW_LOG, show(FlowCost.TAG_LOG), EAPrefType.bool, true),
    showLogType(FlowCost.SHOW_LOGTYPE, show(FlowCost.TAG_TYP), EAPrefType.bool, true),
    showSecolo(FlowCost.SHOW_SECOLO, show(FlowCost.TAG_SEC), EAPrefType.bool, false),
    showAnno(FlowCost.SHOW_ANNO, show(FlowCost.TAG_ANN), EAPrefType.bool, false),
    showMese(FlowCost.SHOW_MESE, show(FlowCost.TAG_MES), EAPrefType.bool, false),
    showGiorno(FlowCost.SHOW_GIORNO, show(FlowCost.TAG_GIO), EAPrefType.bool, false),
    loadUtenti(FlowCost.LOAD_UTENTI, "Flag per caricare gli utenti di prova allo startup del programma. Di default false.", EAPrefType.bool, false),
    usaLogMail(FlowCost.USA_LOG_MAIL, "Uso della mail spedita da un log. Di default false", EAPrefType.bool, false),
    mailFrom(FlowCost.MAIL_FROM, "Email di default da cui partono i log", EAPrefType.string, "info@algos.it"),
    mailTo(FlowCost.MAIL_TO, "Email di default a cui spedire i log di posta", EAPrefType.string, "gac@algos.it"),
    maxRigheGrid(FlowCost.MAX_RIGHE_GRID, "Numero di elementi oltre il quale scatta la pagination automatica della Grid (se attiva)", EAPrefType.integer, 15),
    maxRigheGridClick(FlowCost.MAX_RIGHE_GRID_CLICK, "Numero di elementi oltre il quale scatta la pagination automatica della Grid (se attiva) e se è abilitato il doppio click per aprire il dialogo di edit (le righe sono meno alte)", EAPrefType.integer, 20),
    mongoPageLimit(FlowCost.MONGO_PAGE_LIMIT, "Limite di elementi nelle query mongoDB", EAPrefType.integer, 50000),
    usaMenu(FlowCost.USA_MENU, "Tipo di menu in uso", EAPrefType.enumeration, "routers,tabs,buttons,popup,flowing,vaadin;routers"),
    textButtonSearch(FlowCost.FLAG_TEXT_SEARCH, "Testo del bottone Search", EAPrefType.enumeration, "cerca,ricerca,find;cerca"),
    textButtonNew(FlowCost.FLAG_TEXT_NEW, "Testo del bottone New", EAPrefType.enumeration, "new,nuovo;nuovo"),
    textButtonShow(FlowCost.FLAG_TEXT_SHOW, "Testo del bottone Show (potrebbe esserci solo l'icona)", EAPrefType.enumeration, "show,mostra,vedi;show"),
    textButtonEdit(FlowCost.FLAG_TEXT_EDIT, "Testo del bottone Edit (potrebbe esserci solo l'icona)", EAPrefType.enumeration, "open,edit,modifica,apre,apri;edit"),
    usaTextEditButton(FlowCost.USA_TEXT_EDIT_BUTTON, "Usa un testo (oltre all'icona) per il bottone di Edit che apre il dialog", EAPrefType.bool, true),
    usaEditButton(FlowCost.USA_EDIT_BUTTON, "Usa una colonna di bottoni Edit per aprire il dialogo. Se falso, usa un doppio clik nella riga", EAPrefType.bool, true),
    showAccount(FlowCost.SHOW_ACCOUNT_ON_MENU, "Mostra l'account nella barra di menu", EAPrefType.bool, true),
    usaSearchCaseSensitive(FlowCost.USA_SEARCH_CASE_SENSITIVE, "Search delle query sensibile alle maiuscole", EAPrefType.bool, false),
    usaButtonShortcut(FlowCost.USA_BUTTON_SHORTCUT, "Shortcut dei bottoni. Disabilitabile in caso di problemi col browser", EAPrefType.bool, true),
    usaGridHeaderPrimaMaiuscola(FlowCost.USA_GRID_HEADER_PRIMA_MAIUSCOLA, "Prima lettera maiuscola nell'header della Grid", EAPrefType.bool, true),
    ;


    private String code;

    private String desc;

    private EAPrefType type;

    private Object value;


    EAPreferenza(String code, String desc, EAPrefType type, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setValue(value);
    }// fine del costruttore


    public static String show(String modulo) {
        return "Flag per costruire la UI con il modulo " + modulo + " visibile nel menu";
    }// end of method


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
