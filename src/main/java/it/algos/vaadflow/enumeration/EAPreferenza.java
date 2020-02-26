package it.algos.vaadflow.enumeration;


import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.preferenza.IAPreferenza;
import it.algos.vaadflow.modules.role.EARole;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenza implements IAPreferenza {

    usaDebug(FlowCost.USA_DEBUG, "Flag generale di debug (ce ne possono essere di specifici, validi solo se questo è vero)", EAPrefType.bool, EARole.developer, true, false),
    usaLogDebug(FlowCost.USA_LOG_DEBUG, "Uso del log di registrazione per il livello debug. Di default false.", EAPrefType.bool, EARole.developer, false, false),
    logLevelCorrente(FlowCost.LOG_LEVEL_CORRENTE, "Livello corrente di log. Di default info.", EAPrefType.enumeration, EARole.developer, false, EALogLivello.info),
    loadUtenti(FlowCost.LOAD_UTENTI, "Flag per caricare gli utenti di prova allo startup del programma. Di default false.", EAPrefType.bool, EARole.developer, false, false),

    //--visibilità standard dei moduli
    showCompany(FlowCost.SHOW_COMPANY, showMenu(FlowCost.TAG_COM), EAPrefType.bool, EARole.developer, false, false),
    showPreferenza(FlowCost.SHOW_PREFERENZA, showMenu(FlowCost.TAG_PRE), EAPrefType.bool, EARole.developer, false, true),
    showWizard(FlowCost.SHOW_WIZARD, showMenu(FlowCost.TAG_WIZ), EAPrefType.bool, EARole.developer, false, true),
    //    showDeveloper(FlowCost.SHOW_DEVELOPER, showMenu(FlowCost.TAG_DEV), EAPrefType.bool, EARole.developer, false, false),
    showAddress(FlowCost.SHOW_ADDRESS, showMenu(FlowCost.TAG_ADD), EAPrefType.bool, EARole.developer, false, true),
    showPerson(FlowCost.SHOW_PERSON, showMenu(FlowCost.TAG_PER), EAPrefType.bool, EARole.developer, false, true),
    showRole(FlowCost.SHOW_ROLE, showMenu(FlowCost.TAG_ROL), EAPrefType.bool, EARole.developer, false, true),
    showUser(FlowCost.SHOW_USER, showMenu(FlowCost.TAG_UTE), EAPrefType.bool, EARole.developer, false, true),
    showVersione(FlowCost.SHOW_VERSION, showMenu(FlowCost.TAG_VER), EAPrefType.bool, EARole.developer, false, true),
    showLog(FlowCost.SHOW_LOG, showMenu(FlowCost.TAG_LOG), EAPrefType.bool, EARole.developer, false, true),
    showLogType(FlowCost.SHOW_LOGTYPE, showMenu(FlowCost.TAG_TYP), EAPrefType.bool, EARole.developer, false, true),
    showSecolo(FlowCost.SHOW_SECOLO, showMenu(FlowCost.TAG_SEC), EAPrefType.bool, EARole.developer, false, false),
    showAnno(FlowCost.SHOW_ANNO, showMenu(FlowCost.TAG_ANN), EAPrefType.bool, EARole.developer, false, false),
    showMese(FlowCost.SHOW_MESE, showMenu(FlowCost.TAG_MES), EAPrefType.bool, EARole.developer, false, false),
    showGiorno(FlowCost.SHOW_GIORNO, showMenu(FlowCost.TAG_GIO), EAPrefType.bool, EARole.developer, false, false),

    //--menu
    usaMenu(FlowCost.USA_MENU, "Tipo di menu in uso", EAPrefType.enumeration, EARole.developer, false, EAMenu.tabs),
    showAccount(FlowCost.SHOW_ACCOUNT_ON_MENU, "Mostra l'account nella barra di menu", EAPrefType.bool, EARole.developer, false, true),
    showIcons(FlowCost.SHOW_ICONS_MENU, "Mostra le icone nella barra di menu", EAPrefType.bool, EARole.developer, false, true),

    //--mail
    usaLogMail(FlowCost.USA_LOG_MAIL, "Uso della mail spedita da un log. Di default false", EAPrefType.bool, EARole.developer, false, false),
    mailFrom(FlowCost.MAIL_FROM, "Email di default da cui partono i log", EAPrefType.email, EARole.developer, false, "info@algos.it"),
    mailTo(FlowCost.MAIL_TO, "Email di default a cui spedire i log di posta", EAPrefType.email, EARole.admin, false, "gac@algos.it"),

    //--.grid
    maxRigheGrid(FlowCost.MAX_RIGHE_GRID, "Numero di elementi oltre il quale scatta la pagination automatica della Grid (se attiva)", EAPrefType.integer, EARole.developer, true, 20),
    maxRigheGridClick(FlowCost.MAX_RIGHE_GRID_CLICK, "Numero di elementi oltre il quale scatta la pagination automatica della Grid (se attiva) e se è abilitato il doppio click per aprire il dialogo di edit (le righe sono meno alte)", EAPrefType.integer, EARole.developer, false, 25),
    mongoPageLimit(FlowCost.MONGO_PAGE_LIMIT, "Limite di elementi nelle query mongoDB", EAPrefType.integer, EARole.developer, false, 50000),
    usaSearchCaseSensitive(FlowCost.USA_SEARCH_CASE_SENSITIVE, "Search delle query sensibile alle maiuscole", EAPrefType.bool, EARole.developer, false, false),
    usaButtonShortcut(FlowCost.USA_BUTTON_SHORTCUT, "Shortcut dei bottoni. Disabilitabile in caso di problemi col browser", EAPrefType.bool, EARole.developer, true, true),
    usaGridHeaderPrimaMaiuscola(FlowCost.USA_GRID_HEADER_PRIMA_MAIUSCOLA, "Prima lettera maiuscola nell'header della Grid", EAPrefType.bool, EARole.developer, true, true),

    textButtonSearch(FlowCost.FLAG_TEXT_SEARCH, "Testo del bottone Search", EAPrefType.enumeration, EARole.developer, true, EASearchText.cerca),
    textButtonNew(FlowCost.FLAG_TEXT_NEW, "Testo del bottone New", EAPrefType.enumeration, EARole.developer, true, EANewText.nuovoItaliano),
    textButtonShow(FlowCost.FLAG_TEXT_SHOW, "Testo del bottone Show (potrebbe esserci solo l'icona)", EAPrefType.enumeration, EARole.developer, true, EAShowText.show),
    textButtonEdit(FlowCost.FLAG_TEXT_EDIT, "Testo del bottone Edit (potrebbe esserci solo l'icona)", EAPrefType.enumeration, EARole.developer, true, EAEditText.edit),
    usaTextEditButton(FlowCost.USA_TEXT_EDIT_BUTTON, "Usa un testo (oltre all'icona) per il bottone di Edit che apre il dialog", EAPrefType.bool, EARole.developer, true, true),
    usaEditButton(FlowCost.USA_EDIT_BUTTON, "Usa una colonna di bottoni Edit per aprire il dialogo. Se falso, usa un doppio clik nella riga", EAPrefType.bool, EARole.developer, false, true),
    logAction(FlowCost.FLAG_LOG_ACTION, "Azione di log", EAPrefType.enumeration, EARole.developer, false, EALogAction.terminale),
    ;


    //--codice di riferimento. Se è true il flag companySpecifica, contiene anche il code della company come prefisso.
    private String code;

    private String desc;

    private EAPrefType type;

    private Object value;

    //--chi la può vedere
    private EARole show;

    //--usa un prefisso col codice della company
    private boolean companySpecifica;


    EAPreferenza(String code, String desc, EAPrefType type, EARole show, boolean companySpecifica, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setShow(show);
        this.setCompanySpecifica(companySpecifica);
        this.setValue(type != EAPrefType.enumeration ? value : ((IAEnum) value).getPref());
    }// fine del costruttore


    public static String showMenu(String modulo) {
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
