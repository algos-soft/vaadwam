package it.algos.vaadflow.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.shared.ui.LoadMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AMenuService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadflow.ui.topbar.TopbarComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;

import static it.algos.vaadflow.application.FlowVar.usaCompany;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 23-ago-2019
 * Time: 23:03
 * <p>
 * You can set a logo, navigation menus, page content, and more
 * <p>
 * The component is fully responsive and the elements intuitively adjust and modify,
 * depending on the user’s screen size and device type.
 * For example, on small mobile screens, side menus collapse and open with animation,
 * and top menus are repositioned below the main content.
 */
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@BodySize
@HtmlImport(value = "styles/shared-styles.html", loadMode = LoadMode.INLINE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
@Theme(Lumo.class)
//@Theme(Material.class)
//@NoTheme
public class MainLayout14 extends AppLayout {

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

    /**
     * Mantenuto nel 'context' <br>
     */
    protected ALogin login;

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria. Disponibile subito.
     */
    protected AVaadinService vaadinService = StaticContextAccessor.getBean(AVaadinService.class);

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct o comunque dopo l'init (anche implicito) del costruttore <br>
     */
    @Autowired
    protected AMenuService menuService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct o comunque dopo l'init (anche implicito) del costruttore <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct o comunque dopo l'init (anche implicito) del costruttore <br>
     */
    @Autowired
    protected PreferenzaService pref = StaticContextAccessor.getBean(PreferenzaService.class);

    private Registration broadcasterRegistration;


    public MainLayout14() {
    }// end of constructor

//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        UI ui = attachEvent.getUI();
//        broadcasterRegistration = Broadcaster.register(message -> ui.access(() -> {
//            String code = message.getCode();
//            if (code.equals("rolechanged") ) {
//                UI currentUI = UI.getCurrent();
//                if (currentUI!=null){
//                    currentUI.getPage().reload();
//                }
//            }
//        }));
//    }

//    @Override
//    protected void onDetach(DetachEvent detachEvent) {
//        broadcasterRegistration.remove();
//        broadcasterRegistration = null;
//    }

    /**
     * Metodo invocato subito DOPO il costruttore
     * L'istanza DEVE essere creata da SpringBoot con Object algos = appContext.getBean(AlgosClass.class);  <br>
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void inizia() {
        fixSessione();
        fixView();
        //        fixType();
        addToDrawer(getTabMenu());

    }// end of method


    /**
     * Recupera i dati della sessione
     */
    protected void fixSessione() {
        context = vaadinService.getSessionContext();
        login = context != null ? context.getLogin() : null;
        if (login != null) {
            pref.login = login;
        }

    }// end of method


    /**
     * Layout base della pagina
     */
    protected void fixView() {
        TopbarComponent topbar = createTopBar();

        if (FlowVar.usaDrawer) {
            DrawerToggle drawer = new DrawerToggle();
            addToNavbar(drawer, topbar);
        } else {
            Span span = new Span();
            span.getElement().setProperty("innerHTML", "&nbsp; &nbsp; &nbsp;");
            addToNavbar(span, topbar);
        }// end of if/else cycle
        this.setDrawerOpened(false);

    }// end of method


    /**
     * Se l' applicazione è multiCompany e multiUtente, li visualizzo <br>
     * Altrimenti il nome del programma <br>
     */
    protected TopbarComponent createTopBar() {
        TopbarComponent topbar;
        String style;

        if (text.isValid(getUserName())) {
            Company company=login.getCompany();
            topbar = new TopbarComponent(login, FlowVar.pathLogo, company.getCode().toUpperCase(), "", getUserName());
        } else {
            topbar = new TopbarComponent(FlowVar.pathLogo, getDescrizione());
        }

        style = "display:inline-flex; width:100%; flex-direction:row; padding-left:0em; padding-top:0.5em; padding-bottom:0.5em; padding-right:1em; align-items:center";
        topbar.getElement().setAttribute("style", style);
        topbar.setProfileListener(() -> profilePressed());

        topbar.setLogoutListener(() -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
        });

        return topbar;
    }


    /**
     * Ha senso solo se l'applicazione è multiUtente <br>
     */
    protected void profilePressed() {
        Notification notification = new Notification("Profile pressed", 3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }// end of method


    /**
     * Se l'applicazione è multiCompany visualizzo una sigla/descrizione della company<br>
     * Altrimenti il nome del programma <br>
     */
    protected String getDescrizione() {
        String desc = "";

        if (usaCompany && login != null && login.getCompany() != null) {
            Company company = login.getCompany();
            //desc = company.code+" "+company.descrizione;
            desc = company.getCode().toUpperCase();
        } else {
            desc = text.primaMaiuscola(FlowVar.projectBanner);
        }

        return desc;
    }


    /**
     * Se l'applicazione è multiUtente, visualizzo l'utente loggato <br>
     * Altrimenti rimane vuoto (non aggiunge il componente grafico) <br>
     */
    protected String getUserName() {
        String username = "";

        if (usaCompany && login != null && login.getUtente() != null) {
            username = login.getUtente().username;
        }// end of if cycle

        return username;
    }// end of method


    /**
     * Regola il tipo di presentazione del menu
     */
    @Deprecated
    protected void fixType() {
        String type = pref.getEnumStr(EAPreferenza.usaMenu);

        switch (type) {
            case "routers":
                addToDrawer(getRouterMenu());
                break;
            case "tabs":
                addToDrawer(getTabMenu());
                break;
            default:
                log.warn("MainLayout14 - Manca la preferenza 'usaMenu'");
                break;
        } // end of switch statement

    }// end of method


    /**
     * Regola i routers
     * Può essere sovrascritto
     */
    @Deprecated
    protected VerticalLayout getRouterMenu() {
        VerticalLayout menuLayout = new VerticalLayout();
        Map<String, ArrayList<Class<? extends IAView>>> mappa = menuService.creaMappa();

        if (usaSecurity && context != null) {
            //--crea menu dello sviluppatore (se loggato)
            if (context.isDev()) {
                menuLayout.add(menuService.creaRoutersDeveloper(mappa));
            }// end of if cycle

            //--crea menu dell'admin (se loggato)
            if (context.isDev() || context.isAdmin()) {
                menuLayout.add(menuService.creaRoutersAdmin(mappa));
            }// end of if cycle

            //--crea menu utente normale (sempre)
            menuLayout.add(menuService.creaRoutersUser(mappa));

            //--crea menu logout (sempre)
            //            menuLayout.add(menuService.creaMenuLogout());
        } else {
            //--crea menu indifferenziato
            menuLayout.add(menuService.creaRoutersNoSecurity(mappa));
        }// end of if/else cycle

        return menuLayout;
    }// end of method


    /**
     * Regola i tabs
     * Può essere sovrascritto
     */
    protected Tabs getTabMenu() {
        Tabs tabs = new Tabs();
        Map<String, ArrayList<Class<? extends IAView>>> mappa = menuService.creaMappa();

        if (usaSecurity && context != null) {
            //--crea menu dello sviluppatore (se loggato)
            if (context.isDev()) {
                tabs = menuService.addTabsDeveloper(tabs, mappa);
            }// end of if cycle

            //--crea menu dell'admin (se loggato)
            if (context.isDev() || context.isAdmin()) {
                tabs = menuService.addTabsAdmin(tabs, mappa);
            }// end of if cycle

            //--crea menu utente normale (sempre)
            if (context.isDev() || context.isAdmin()) {
                tabs = menuService.addTabsUser(tabs, mappa);
            } else {
                tabs = menuService.creaTabsUser(mappa);
            }// end of if/else cycle

            //--aggiunge il menu logout (sempre se usa la security)
            //--levato Doppione del profilo in alto a destra
            //            tabs = menuService.addMenuLogout(tabs);
        } else {
            //--crea menu indifferenziato
            tabs = menuService.creaTabsNoSecurity(mappa);
        }// end of if/else cycle

        if (tabs != null) {
            tabs.setOrientation(Tabs.Orientation.VERTICAL);
        }// end of if cycle

        return tabs;
    }// end of method

}// end of class
