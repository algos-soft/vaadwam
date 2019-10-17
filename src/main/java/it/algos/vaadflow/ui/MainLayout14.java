package it.algos.vaadflow.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.ui.LoadMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AMenuService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;

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
@Theme(Lumo.class)
public class MainLayout14 extends AppLayout {

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    private AContext context;

    /**
     * Mantenuto nel 'context' <br>
     */
    private ALogin login;

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria. Disponibile subito.
     */
    private AVaadinService vaadinService = StaticContextAccessor.getBean(AVaadinService.class);

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct o comunque dopo l'init (anche implicito) del costruttore <br>
     */
    @Autowired
    private AMenuService menuService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct o comunque dopo l'init (anche implicito) del costruttore <br>
     */
    @Autowired
    private ATextService text;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct o comunque dopo l'init (anche implicito) del costruttore <br>
     */
    @Autowired
    private PreferenzaService pref;


    public MainLayout14() {
//        //@todo DA FARE cambiare immagine
//        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Algos");
//        img.setHeight("44px");
//        HorizontalLayout oriz = new HorizontalLayout();
//        oriz.setSpacing(true);
//        oriz.add(new Label("Pippoz"));
//        oriz.setAlignItems(FlexComponent.Alignment.END);
//        addToNavbar(new DrawerToggle(), img, oriz);
//        this.setDrawerOpened(false);
    }// end of constructor


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
        fixType();
    }// end of method


    /**
     * Recupera i dati della sessione
     */
    protected void fixSessione() {
        context = vaadinService.getSessionContext();
        login = context != null ? context.getLogin() : null;
    }// end of method


    /**
     * Layout base della pagina
     */
    protected void fixView() {
        //@todo DA FARE cambiare immagine
        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Algos");
        img.setHeight("44px");
        Image immagine = new Image("frontend/images/ambulanza.jpg", "Algos");
        immagine.setHeight("44px");

        String desc = login.getCompany() != null ? login.getCompany().code.toUpperCase() : text.primaMaiuscola(FlowVar.projectBanner);

        Label label = new Label(desc);
        label.getStyle().set("font-size", "x-large");
        label.getStyle().set("font-weight", "bold");
        label.getElement().getStyle().set("color", "blue");

        Label spazio = new Label();
        spazio.getElement().setProperty("innerHTML", "&nbsp;&nbsp;&nbsp;&nbsp;");

//        addToNavbar(new DrawerToggle(), img, label);
        DrawerToggle drawer = new DrawerToggle();
//        addToNavbar(drawer, label);
        addToNavbar(drawer, immagine, spazio,label);
        this.setDrawerOpened(false);
    }// end of method


    /**
     * Regola il tipo di presentazione del menu
     */
    protected void fixType() {
        String type = pref.getStr(FlowCost.USA_MENU);

        switch (type) {
            case "routers":
                addToDrawer(getRouterMenu());
                break;
            case "tabs":
                addToDrawer(getTabMenu());
                break;
            default:
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

        if (usaSecurity) {
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

        if (usaSecurity) {
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
            tabs = menuService.addMenuLogout(tabs);
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
