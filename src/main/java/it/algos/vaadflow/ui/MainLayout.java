package it.algos.vaadflow.ui;

//import com.flowingcode.addons.applayout.AppLayout;
//import com.flowingcode.addons.applayout.menu.MenuItem;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
//import com.vaadin.flow.component.applayout.AppLayoutMenu;
//import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.AAnnotationService;
import it.algos.vaadflow.service.AReflectionService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.*;

import static it.algos.vaadflow.application.FlowVar.menuClazzList;

/**
 * Gestore dei menu. Unico nell'applicazione (almeno finche non riesco a farne girare un altro)
 * <p>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @HtmlImport (obbligatorio) per usare il CSS
 * Annotated with @Push (obbligatorio)
 * Annotated with @PageTitle (facoltativo)
 */
@SuppressWarnings("serial")
@HtmlImport(value = "styles/shared-styles.html", loadMode = LoadMode.INLINE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Push
@PageTitle(value = MainLayout.SITE_TITLE)
@Slf4j
public class MainLayout extends VerticalLayout implements RouterLayout, PageConfigurator {


    public static final String SITE_TITLE = "World Cup 2018 Stats";

    public final static String KEY_MAPPA_CRONO = "crono";

    public final static String KEY_MAPPA_PROGETTO_BASE = "progettoBase";

    public final static String KEY_MAPPA_PROGETTO_SPECIFICO = "progettoSpecifico";

    protected AppLayout appLayout;

//    protected AppLayoutMenu appMenu;

    private AAnnotationService annotation = AAnnotationService.getInstance();

    private AReflectionService reflection = AReflectionService.getInstance();

    private ATextService text = ATextService.getInstance();

    private Map<String, List<Class>> mappaClassi;

    private ALogin login;

    private Div container;

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    private AContext context;

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    private AVaadinService vaadinService = StaticContextAccessor.getBean(AVaadinService.class);


    public MainLayout() {
        setMargin(false);
        setSpacing(false);
        setPadding(false);

        //--Login and context della sessione
        context = vaadinService.getSessionContext();
        login = context != null ? context.getLogin() : null;
        if (context == null) {
            return;
        }// end of if cycle

        //--creazione iniziale del menu
        creaVaadindMenu();

        //--crea brand (sempre)
        creaBrand();

        //--crea menu dello sviluppatore (se loggato)
        creaMenuDeveloper();

        //--crea menu dell'admin (se loggato)
        creaMenuAdmin();

        //--crea menu utente normale (sempre)
        creaMenuUser();

        //--crea menu logout (sempre)
        creaMenuLogout();
    }// end of method


    /**
     * Chiamato DOPO aver termionato il costruttore
     * Registra nel context l'indirizzo a questa istanza per modifiche specifiche ai menu
     */
    @PostConstruct
    private void fixContext() {
        if (context != null) {
            context.setMainLayout(this);
            context.setAppLayout(appLayout);
//            context.setAppMenu(appMenu);
        }// end of if cycle
    }// end of method


    /**
     * Creazione iniziale del menu
     */
    protected void creaVaadindMenu() {
        appLayout = new AppLayout();
//        appMenu = appLayout.createMenu();
        mappaClassi = creaMappa(menuClazzList);
//        this.add(appLayout);
    }// end of method


    /**
     * Eventuale brand a sinistra
     */
    private void creaBrand() {
//        appLayout.setBranding(new H3("Croce Rossa Fidenza"));
    }// end of method


    /**
     * Eventuali menu se collegato come sviluppatore
     */
    private void creaMenuDeveloper() {
        if (context != null && context.isDev()) {
//            addMenu(UtenteViewList.class);
//            addMenu(VersioneViewList.class);
//            addMenu(PreferenzaViewList.class);
        }// end of if cycle
    }// end of method


    /**
     * Eventuali menu se collegato come admin
     */
    private void creaMenuAdmin() {
        if (context != null && context.isAdmin()) {
//            addMenu(LogViewList.class);
        }// end of if cycle
    }// end of method


    /**
     * Menu normali sempre presenti
     */
    private void creaMenuUser() {
        List<Class> userClazzList = null;

        if (mappaClassi.get(EARole.user.toString()) != null) {
            userClazzList = mappaClassi.get(EARole.user.toString());
        }// end of if cycle

        if (userClazzList != null && userClazzList.size() > 0) {
            for (Class viewClazz : userClazzList) {
//                addMenu(viewClazz);
            }// end of for cycle
        }// end of if cycle

    }// end of method


    /**
     * Menu logout sempre presente
     */
    protected void creaMenuLogout() {
//        appMenu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.SIGN_OUT.create(), "Logout", e -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));
    }// end of metho


    protected void logout() {
    }// end of method

//    protected void creaAllMenu() {
//        final AppLayout appLayout = new AppLayout(null, createAvatarComponent(), FlowCost.LAYOUT_TITLE);
//        ArrayList<MenuItem> listaMenu = null;
//        MenuItem menu = null;
//        Map<String, List<Class>> mappaClassi = creaMappa(FlowCost.MENU_CLAZZ_LIST);
//
//        if (mappaClassi != null && mappaClassi.size() > 0) {
//            listaMenu = new ArrayList<>();
//
//            //--crea i menu del developer, inserendoli come sub-menu
//            if (login != null && login.isDeveloper()) {
//                this.addDev(listaMenu, mappaClassi);
//            }// end of if cycle
//
//            //--crea i menu di admin, inserendoli come sub-menu
//            if (login != null && login.isAdmin()) {
//                this.addAdmin(listaMenu, mappaClassi);
//            }// end of if cycle
//
//            //--crea gli altri menu, esclusi quelli del developer e di admin che sono già inseriti
//            this.addUser(listaMenu, mappaClassi);
//
//            //--crea il logout
//            listaMenu.add(creaMenuLogout());
//
//            //--aggiunge i menu
//            appLayout.setMenuItems(listaMenu.toArray(new MenuItem[listaMenu.size()]));
//
//            //--crea la barra di bottoni, in alto a destra
//            appLayout.setToolbarIconButtons(new MenuItem("Logout", "exit-to-app", () -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));
//
//            this.add(appLayout);
//        }// end of if cycle
//    }// end of method


    private Map<String, List<Class>> creaMappa(List<Class> listaClassiMenu) {
        Map<String, List<Class>> mappa = new HashMap<>();
        ArrayList<Class> devClazzList = new ArrayList<>();
        ArrayList<Class> cronoDevClazzList = new ArrayList<>();
        ArrayList<Class> adminClazzList = new ArrayList<>();
        ArrayList<Class> utenteClazzList = new ArrayList<>();
        EARoleType type = null;
        List<String> cronoList = Arrays.asList(new String[]{"secolo", "anno", "mese", "giorno"});
        String menuName;

        for (Class viewClazz : listaClassiMenu) {
            type = annotation.getViewRoleType(viewClazz);

            switch (type) {
                case developer:
                    menuName = annotation.getMenuName(viewClazz);
                    if (cronoList.contains(menuName)) {
                        cronoDevClazzList.add(viewClazz);
                    } else {
                        devClazzList.add(viewClazz);
                    }// end of if/else cycle
                    break;
                case admin:
                    adminClazzList.add(viewClazz);
                    break;
                case user:
                    utenteClazzList.add(viewClazz);
                    break;
                default:
                    utenteClazzList.add(viewClazz);
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of for cycle

        mappa.put(EARole.developer.toString(), devClazzList);
        mappa.put(EARole.admin.toString(), adminClazzList);
        mappa.put(KEY_MAPPA_CRONO, cronoDevClazzList);
        mappa.put(EARole.user.toString(), utenteClazzList);

        return mappa;
    }// end of method


//    /**
//     * Crea i menu visibili solo per il developer <br>
//     * Li incapsula come sub-menu <br>
//     */
//    private void addDev(ArrayList<MenuItem> listaMenu, Map<String, List<Class>> mappaClassi) {
//        MenuItem developerItem;
//        MenuItem[] matrice;
//        ArrayList<MenuItem> listaSubMenuDev = new ArrayList<>();
//        MenuItem menu;
//        List<Class> devClazzList = null;
//
//        if (mappaClassi.get(EARole.developer.toString()) != null) {
//            devClazzList = mappaClassi.get(EARole.developer.toString());
//        }// end of if cycle
//
//        if (devClazzList != null && devClazzList.size() > 0) {
//            for (Class viewClazz : devClazzList) {
//                menu = creaMenu(viewClazz);
//                listaSubMenuDev.add(menu);
//            }// end of for cycle
//        }// end of if cycle
//
//        //--crea i menu del developer, inserendoli come sub-menu
//        this.addCronoDev(listaSubMenuDev, mappaClassi);
//
//        matrice = listaSubMenuDev.toArray(new MenuItem[listaSubMenuDev.size()]);
//        developerItem = new MenuItem("Developer", "build", matrice);
//        listaMenu.add(developerItem);
//    }// end of method


//    /**
//     * Crea i menu crono, visibili solo per il developer <br>
//     * Li incapsula come sub-menu <br>
//     */
//    private void addCronoDev(ArrayList<MenuItem> listaMenuDev, Map<String, List<Class>> mappaClassi) {
//        MenuItem developerItem;
//        MenuItem[] matrice;
//        ArrayList<MenuItem> listaSubMenuCronoDev = new ArrayList<>();
//        MenuItem menu;
//        List<Class> devCronoClazzList = null;
//
//        if (mappaClassi.get(KEY_MAPPA_CRONO) != null) {
//            devCronoClazzList = mappaClassi.get(KEY_MAPPA_CRONO);
//        }// end of if cycle
//
//        if (devCronoClazzList != null && devCronoClazzList.size() > 0) {
//            for (Class viewClazz : devCronoClazzList) {
//                menu = creaMenu(viewClazz);
//                listaSubMenuCronoDev.add(menu);
//            }// end of for cycle
//        }// end of if cycle
//
//        if (devCronoClazzList != null && devCronoClazzList.size() > 0) {
//            matrice = listaSubMenuCronoDev.toArray(new MenuItem[listaSubMenuCronoDev.size()]);
//            developerItem = new MenuItem("Crono", "build", matrice);
//            listaMenuDev.add(developerItem);
//        }// end of if cycle
//    }// end of method


//    /**
//     * Crea i menu visibili solo per l'admin ed per il developer <br>
//     * Li incapsula come sub-menu <br>
//     */
//    private void addAdmin(ArrayList<MenuItem> listaMenu, Map<String, List<Class>> mappaClassi) {
//        MenuItem adminItem;
//        MenuItem[] matrice;
//        ArrayList<MenuItem> listaSubMenuDev = new ArrayList<>();
//        MenuItem menu;
//        List<Class> adminClazzList = null;
//
//        if (mappaClassi.get(EARole.admin.toString()) != null) {
//            adminClazzList = mappaClassi.get(EARole.admin.toString());
//        }// end of if cycle
//
//        if (adminClazzList != null && adminClazzList.size() > 0) {
//            for (Class viewClazz : adminClazzList) {
//                menu = creaMenu(viewClazz);
//                listaSubMenuDev.add(menu);
//            }// end of for cycle
//        }// end of if cycle
//
//        matrice = listaSubMenuDev.toArray(new MenuItem[listaSubMenuDev.size()]);
//        adminItem = new MenuItem("Admin", "apps", matrice);
//        listaMenu.add(adminItem);
//    }// end of method


//    /**
//     * Crea i menu visibili a tutti <br>
//     */
//    private void addUser(ArrayList<MenuItem> listaMenu, Map<String, List<Class>> mappaClassi) {
//        MenuItem menu;
//        List<Class> userClazzList = null;
//
//        if (mappaClassi.get(EARole.user.toString()) != null) {
//            userClazzList = mappaClassi.get(EARole.user.toString());
//        }// end of if cycle
//
//        if (userClazzList != null && userClazzList.size() > 0) {
//            for (Class viewClazz : userClazzList) {
//                menu = creaMenu(viewClazz);
//                listaMenu.add(menu);
//            }// end of for cycle
//        }// end of if cycle
//    }// end of method


    /**
     * Crea l'immagine (se esiste) dell'utente collegato <br>
     */
    private Component createAvatarComponent() {
        Div container = new Div();
        H5 userName;
        container.getElement().setAttribute("style", "text-align: center;");
        Image i = new Image("/frontend/images/avatar.png", "avatar");
        i.getElement().setAttribute("style", "width: 60px; margin-top:10px");

        if (login != null && login.getUtente() != null) {
            container.add(i, new H5(login.getUtente().getUsername()));
        } else {
            container.add(i);
        }// end of if/else cycle

        return container;
    }// end of method


//    /**
//     * Crea il singolo item di menu <br>
//     */
//    public AppLayoutMenuItem addMenu(Class<? extends AViewList> viewClazz) {
//        String linkRoute;
//        String menuName;
//        VaadinIcon icon;
//
//        menuName = annotation.getMenuName(viewClazz);
//        icon = reflection.getIconView(viewClazz);
//        linkRoute = annotation.getRouteName(viewClazz);
//
//        if (text.isValid(menuName) && text.isValid(linkRoute)) {
//            return appMenu.addMenuItem(new AppLayoutMenuItem(icon.create(), menuName, linkRoute));
//        } else {
//            return null;
//        }// end of if/else cycle
//
//    }// end of method

//    /**
//     * Crea il singolo item di menu <br>
//     */
//    protected MenuItem creaMenu(Class<? extends AViewList> viewClazz) {
//        String linkRoute;
//        String menuName;
//        String icon;
//
//        linkRoute = annotation.getQualifierName(viewClazz);
//        menuName = annotation.getViewName(viewClazz);
//        menuName = text.primaMaiuscola(menuName);
//        icon = reflection.getIronIcon(viewClazz);
//
//        return new MenuItem(menuName, icon, () -> UI.getCurrent().navigate(linkRoute));
//    }// end of method


//    /**
//     * Crea (in fondo) il menu item per il logout <br>
//     */
//    protected MenuItem creaMenuLogout() {
//        MenuItem menuItem = null;
//        String menuName = "Logout";
//
//        menuItem = new MenuItem(menuName, "exit-to-app", () -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')"));
//        return menuItem;
//    }// end of method


    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }// end of method


    @Override
    public void configurePage(final InitialPageSettings settings) {
        settings.addMetaTag("viewport", "width=device-width, initial-scale=1.0");
        settings.addLink("shortcut icon", "/frontend/images/favicons/favicon-96x96.png");
        settings.addLink("manifest", "/manifest.json");
//        settings.addFavIcon("icon", "/frontend/images/favicons/favicon-96x96.png", "96x96");
        settings.addFavIcon("icon", "/frontend/images/favicon.ico", "96x96");
    }// end of method


    public AppLayout getAppLayout() {
        return appLayout;
    }// end of method


}// end of class