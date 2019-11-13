package it.algos.vaadflow.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadflow.ui.list.AViewList;
import it.algos.vaadflow.ui.login.LogoutView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

import static it.algos.vaadflow.application.FlowCost.SHOW_ICONS_MENU;
import static it.algos.vaadflow.application.FlowVar.usaSecurity;
import static it.algos.vaadflow.ui.MainLayout.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 19-set-2019
 * Time: 09:27
 * Utility per la gestione dei menu <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)

@Slf4j
public class AMenuService extends AService {


    /**
     * Items di menu
     *
     * @param clazzList di classi raggiungibili da @Route
     */
    public RouterLink[] creaRoutersBase(List<Class<? extends IAView>> clazzList) {
        List<RouterLink> routers = new ArrayList<>();

        if (array.isValid(clazzList)) {
            for (Class viewClazz : clazzList) {
                routers.add(creaAlgosRouter(viewClazz));
            }// end of for cycle
        }// end of if cycle

        return routers.toArray(new RouterLink[routers.size()]);
    }// end of method


    /**
     * Eventuali item di menu, se collegato come sviluppatore
     */
    public RouterLink[] creaRoutersDeveloper(Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        return creaRoutersBase(mappaClassi.get(EARole.developer.toString()));
    }// end of method


    /**
     * Eventuali item di menu, se collegato come admin
     */
    public RouterLink[] creaRoutersAdmin(Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        return creaRoutersBase(mappaClassi.get(EARole.admin.toString()));
    }// end of method


    /**
     * Eventuali item di menu, se collegato come utente
     */
    public RouterLink[] creaRoutersUser(Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        return creaRoutersBase(mappaClassi.get(EARole.user.toString()));
    }// end of method


    /**
     * Menu logout sempre presente
     */
    public RouterLink creaMenuLogoutRouter() {
        return null;
        //appLayout.setToolbarIconButtons(new MenuItem("Logout", "exit-to-app", () -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));

    }// end of method


    /**
     * Menu logout sempre presente
     */
    public Tabs addMenuLogout(Tabs tabs) {
        Tab tab = creaMenuLogout();

        if (tabs != null && tab != null) {
            tabs.add(tab);
        }// end of if cycle

        return tabs;
    }// end of method


    /**
     * Menu logout sempre presente
     */
    public Tab creaMenuLogout() {
        return creaAlgosTab(LogoutView.class);
    }// end of method


    /**
     * Items di menu, se collegato come sviluppatore
     */
    public RouterLink[] creaRoutersNoSecurity(Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        List<RouterLink> routers = new ArrayList<>();

        for (RouterLink router : creaRoutersBase(mappaClassi.get(KEY_MAPPA_PROGETTO_BASE))) {
            routers.add(router);
        }// end of for cycle
        for (RouterLink router : creaRoutersBase(mappaClassi.get(KEY_MAPPA_CRONO))) {
            routers.add(router);
        }// end of for cycle
        for (RouterLink router : creaRoutersBase(mappaClassi.get(KEY_MAPPA_PROGETTO_SPECIFICO))) {
            routers.add(router);
        }// end of for cycle

        return routers.toArray(new RouterLink[routers.size()]);
    }// end of method


    /**
     * Items di menu
     *
     * @param clazzList di classi raggiungibili da @Route
     */
    public Tab[] creaTabsBase(List<Class<? extends IAView>> clazzList, String titoloGruppo) {
        List<Tab> tabs = new ArrayList<>();

        if (array.isValid(clazzList)) {
            if (text.isValid(titoloGruppo)) {
                tabs.add(new Tab("==" + titoloGruppo + "=="));
            }// end of if cycle

            for (Class viewClazz : clazzList) {
                tabs.add(creaAlgosTab(viewClazz));
            }// end of for cycle
        }// end of if cycle

        return tabs.toArray(new Tab[tabs.size()]);
    }// end of method


    /**
     * Eventuali item di menu, se collegato come sviluppatore
     */
    public Tabs addTabsDeveloper(Tabs tabs, Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        for (Tab tab : creaTabsBase(mappaClassi.get(EARole.developer.toString()), EARole.developer.toString())) {
            tabs.add(tab);
        }// end of for cycle

        return tabs;
    }// end of method


    /**
     * Eventuali item di menu, se collegato come admin
     */
    public Tabs addTabsAdmin(Tabs tabs, Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        for (Tab tab : creaTabsBase(mappaClassi.get(EARole.admin.toString()), EARole.admin.toString())) {
            tabs.add(tab);
        }// end of for cycle

        return tabs;
    }// end of method


    /**
     * Eventuali item di menu, se collegato come utente
     */
    public Tabs addTabsUser(Tabs tabs, Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        for (Tab tab : creaTabsBase(mappaClassi.get(EARole.user.toString()), EARole.user.toString())) {
            tabs.add(tab);
        }// end of for cycle

        return tabs;
    }// end of method


    /**
     * Eventuali item di menu, se collegato come utente
     */
    public Tabs creaTabsUser(Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        Tabs tabs = new Tabs();

        for (Tab tab : creaTabsBase(mappaClassi.get(EARole.user.toString()), "")) {
            tabs.add(tab);
        }// end of for cycle

        return tabs;
    }// end of method


    /**
     * Items di menu, se collegato come sviluppatore
     */
    public Tabs creaTabsNoSecurity(Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        Tabs tabs = new Tabs();

        for (Tab tab : creaTabsBase(mappaClassi.get(KEY_MAPPA_PROGETTO_BASE), "")) {
            tabs.add(tab);
        }// end of for cycle
        for (Tab tab : creaTabsBase(mappaClassi.get(KEY_MAPPA_CRONO), "")) {
            tabs.add(tab);
        }// end of for cycle
        for (Tab tab : creaTabsBase(mappaClassi.get(KEY_MAPPA_PROGETTO_SPECIFICO), "")) {
            tabs.add(tab);
        }// end of for cycle

        return tabs;
    }// end of method


    /**
     * Aggiunge il singolo item di menu <br>
     */
    public void addItem(Class<? extends AViewList> viewClazz) {
        String linkRoute;
        String menuName;
        VaadinIcon icon;

        menuName = annotation.getMenuName(viewClazz);
        menuName = text.primaMaiuscola(menuName);
        icon = reflection.getIconView(viewClazz);
        linkRoute = annotation.getRouteName(viewClazz);

//        appMenu.addMenuItem(new AppLayoutMenuItem(icon.create(), menuName, linkRoute));
    }// end of method


    /**
     * Aggiunge il singolo router <br>
     *
     * @param viewClazz di qualsiasi tipo Component
     *
     * @return RouterLink
     */
    public RouterLink creaRouter(Class<? extends Component> viewClazz) {
        RouterLink router = null;
        String menuName = "";
        String linkRoute;
        VaadinIcon icon;

        menuName = text.isValid(menuName) ? menuName : viewClazz.getSimpleName();
        router = new RouterLink(menuName, viewClazz);
        return router;
    }// end of method


    /**
     * Aggiunge il singolo router <br>
     *
     * @param viewClazz di qualsiasi tipo Component
     *
     * @return RouterLink
     */
    public RouterLink creaAlgosRouter(Class<? extends AViewList> viewClazz) {
        RouterLink router = null;
        String menuName = "";
        String linkRoute;
        VaadinIcon icon;

        menuName = annotation.getMenuName(viewClazz);
        menuName = text.primaMaiuscola(menuName);
        linkRoute = annotation.getRouteName(viewClazz);
        menuName = text.isValid(menuName) ? menuName : viewClazz.getSimpleName();
        router = new RouterLink(menuName, viewClazz);
        return router;
    }// end of method


    /**
     * Aggiunge il singolo tab <br>
     *
     * @param viewClazz di qualsiasi tipo Component
     *
     * @return RouterLink
     */
    public Tab creaAlgosTab(Class<? extends AViewList> viewClazz) {
        Tab tab = new Tab();
        String menuName = "";
        VaadinIcon icon;
        boolean showIcons = pref.isBool(SHOW_ICONS_MENU);

        menuName = annotation.getMenuName(viewClazz);
        menuName = text.primaMaiuscola(menuName);
        icon = annotation.getMenuIcon(viewClazz);
        menuName = text.isValid(menuName) ? menuName : viewClazz.getSimpleName();

        RouterLink link = new RouterLink(null, viewClazz);
        if (showIcons && icon != null) {
            link.add(icon.create());
        }// end of if cycle
        link.add("     ");
        link.add(menuName);
        tab.add(link);

        return tab;
    }// end of method


    /**
     * Crea una mappa di viewClazz, in funzione dell'@Annotation presente nella classe <br>
     */
    public Map<String, ArrayList<Class<? extends IAView>>> creaMappa() {
        return creaMappa(FlowVar.menuClazzList);
    }// end of method


    /**
     * Crea una mappa di viewClazz, in funzione dell'@Annotation presente nella classe <br>
     */
    public Map<String, ArrayList<Class<? extends IAView>>> creaMappa(List<Class> listaClassiMenu) {
        Map<String, ArrayList<Class<? extends IAView>>> mappa = new HashMap<>();
        ArrayList<Class<? extends IAView>> devClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> cronoClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> adminClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> userClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> progettoBaseClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> progettoSpecificoClazzList = new ArrayList<>();
        EARoleType type = null;
        List<String> cronoList = Arrays.asList(new String[]{"Secoli", "Anni", "Mesi", "Giorni"});
        String menuName;

        for (Class viewClazz : listaClassiMenu) {
            menuName = annotation.getMenuName(viewClazz);
            if (usaSecurity) {
                type = annotation.getViewRoleType(viewClazz);
                switch (type) {
                    case developer:
                        if (cronoList.contains(menuName)) {
                            cronoClazzList.add(viewClazz);
                        } else {
                            devClazzList.add(viewClazz);
                        }// end of if/else cycle
                        break;
                    case admin:
                        adminClazzList.add(viewClazz);
                        break;
                    case user:
                        userClazzList.add(viewClazz);
                        break;
                    default:
                        userClazzList.add(viewClazz);
                        log.warn("Switch - caso non definito");
                        break;
                } // end of switch statement
            } else {
                if (cronoList.contains(menuName)) {
                    cronoClazzList.add(viewClazz);
                } else {
                    if (annotation.isMenuProgettoBase(viewClazz)) {
                        progettoBaseClazzList.add(viewClazz);
                    } else {
                        progettoSpecificoClazzList.add(viewClazz);
                    }// end of if/else cycle
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of for cycle

        mappa.put(EARole.developer.toString(), devClazzList);
        mappa.put(EARole.admin.toString(), adminClazzList);
        mappa.put(KEY_MAPPA_CRONO, cronoClazzList);
        mappa.put(EARole.user.toString(), userClazzList);
        mappa.put(KEY_MAPPA_PROGETTO_BASE, progettoBaseClazzList);
        mappa.put(KEY_MAPPA_PROGETTO_SPECIFICO, progettoSpecificoClazzList);

        return mappa;
    }// end of method


//    /**
//     * Eventuali item di menu, se collegato come admin
//     */
//    public void creaMenuAdmin() {
//        if (array.isValid(adminClazzList)) {
//            for (Class viewClazz : adminClazzList) {
//                addItem(viewClazz);
//            }// end of for cycle
//        }// end of if cycle
//    }// end of method
//
//
//    /**
//     * Item di menu (normali) sempre presenti
//     */
//    public void creaMenuUser() {
//        if (array.isValid(userClazzList)) {
//            for (Class viewClazz : userClazzList) {
//                addItem(viewClazz);
//            }// end of for cycle
//        }// end of if cycle
//    }// end of method

}// end of class
