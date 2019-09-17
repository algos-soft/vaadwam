package it.algos.vaadflow.ui.menu;

import com.flowingcode.addons.applayout.AppLayout;
import com.flowingcode.addons.applayout.menu.MenuItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.shared.ui.LoadMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.modules.preferenza.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.ui.list.AViewList;
import it.algos.vaadflow.ui.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Map;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-dic-2018
 * Time: 08:59
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public class AFlowingcodeAppLayoutMenu extends AMenu {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;


    private AppLayout appLayout;

    private ArrayList<MenuItem> menuButtons;

    private ArrayList<MenuItem> toolBarButtons;


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public AFlowingcodeAppLayoutMenu() {
        super();
    }// end of Spring constructor


    /**
     * Costruisce il componente di questa classe concreta di menu <br>
     * Sovrascritto
     */
    @Override
    protected void inizia() {
        menuButtons = new ArrayList<>();
        toolBarButtons = new ArrayList<MenuItem>();
        appLayout = new AppLayout(null, createAvatarComponent(), FlowVar.layoutTitle);
    }// end of method


    /**
     * Eventuali item di menu, se collegato come sviluppatore
     */
    @Override
    protected void creaMenuDeveloper() {
        MenuItem developerItem;
        MenuItem[] matrice;
        MenuItem menu;
        ArrayList<MenuItem> listaSubMenuDev = new ArrayList<>();

        if (array.isValid(devClazzList)) {
            for (Class viewClazz : devClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuDev.add(menu);
            }// end of for cycle
        }// end of if cycle

        //--crea i menu del developer, inserendoli come sub-menu
        this.addCronoDev(listaSubMenuDev, mappaClassi);

        matrice = listaSubMenuDev.toArray(new MenuItem[listaSubMenuDev.size()]);
        developerItem = new MenuItem("Developer", "build", matrice);
        menuButtons.add(developerItem);
    }// end of method


    /**
     * Eventuali item di menu, se collegato come admin
     */
    protected void creaMenuAdmin() {
        MenuItem adminItem;
        MenuItem[] matrice;
        MenuItem menu;
        ArrayList<MenuItem> listaSubMenuAdmin = new ArrayList<>();

        if (array.isValid(adminClazzList)) {
            for (Class viewClazz : adminClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuAdmin.add(menu);
            }// end of for cycle
        }// end of if cycle

        matrice = listaSubMenuAdmin.toArray(new MenuItem[listaSubMenuAdmin.size()]);
        adminItem = new MenuItem("Admin", "build", matrice);
        menuButtons.add(adminItem);
    }// end of method


    /**
     * Crea i menu crono, visibili solo per il developer <br>
     * Li incapsula come sub-menu <br>
     */
    private void addCronoDev(ArrayList<MenuItem> listaMenuDev, Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        MenuItem developerItem;
        MenuItem[] matrice;
        ArrayList<MenuItem> listaSubMenuCronoDev = new ArrayList<>();
        MenuItem menu;

        if (array.isValid(cronoClazzList)) {
            for (Class viewClazz : cronoClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuCronoDev.add(menu);
            }// end of for cycle
        }// end of if cycle

        if (array.isValid(cronoClazzList)) {
            matrice = listaSubMenuCronoDev.toArray(new MenuItem[listaSubMenuCronoDev.size()]);
            developerItem = new MenuItem("Crono", "build", matrice);
            listaMenuDev.add(developerItem);
        }// end of if cycle
    }// end of method

    /**
     * Item di menu sempre presenti
     */
    protected void creaMenuNoSecurity() {
        MenuItem cronoItem;
        ArrayList<MenuItem> listaSubMenuCrono = new ArrayList<>();
        MenuItem menu;

        if (array.isValid(progettoBaseClazzList)) {
            for (Class viewClazz : progettoBaseClazzList) {
                menuButtons.add(creaItem(viewClazz));
            }// end of for cycle
        }// end of if cycle

        if (array.isValid(cronoClazzList)) {
            MenuItem[] matrice;

            for (Class viewClazz : cronoClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuCrono.add(menu);
            }// end of for cycle

            matrice = listaSubMenuCrono.toArray(new MenuItem[listaSubMenuCrono.size()]);
            cronoItem = new MenuItem("Crono", "build", matrice);
            menuButtons.add(cronoItem);
        }// end of if cycle

        if (array.isValid(progettoSpecificoClazzList)) {
            for (Class viewClazz : progettoSpecificoClazzList) {
                menuButtons.add(creaItem(viewClazz));
            }// end of for cycle
        }// end of if cycle
    }// end of method

    /**
     * Item di menu logout sempre presente
     * Sia nel menu laterale sia nella toolBar superiore
     */
    protected void creaMenuLogout() {
        MenuItem item = new MenuItem("Logout", "exit-to-app", () -> goTo("logout"));
        menuButtons.add(item);

        if (pref.isBool(EAPreferenza.showAccount.getCode())) {
            toolBarButtons.add(new MenuItem("Account", "vaadin:user", () -> apriAccount()));
        }// end of if cycle

        toolBarButtons.add(item);
        appLayout.setToolbarIconButtons(getMatrice(toolBarButtons));
    }// end of method


    public MenuItem[] getMatrice(ArrayList<MenuItem> toolBarButtons) {
        MenuItem[] menuItemMatrice;

        if (array.isValid(toolBarButtons)) {
            menuItemMatrice = new MenuItem[toolBarButtons.size()];

            for (int k = 0; k < toolBarButtons.size(); k++) {
                menuItemMatrice[k] = toolBarButtons.get(k);
            }// end of for cycle
        } else {
            menuItemMatrice = new MenuItem[0];
        }// end of if/else cycle

        return menuItemMatrice;
    }// end of method


    protected void apriAccount() {
        Notification.show("Modifica account");
    }// end of method


    /**
     * Regolazioni finali
     */
    @Override
    protected void fixFinali() {
        appLayout.setMenuItems(menuButtons.toArray(new MenuItem[menuButtons.size()]));
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     */
    @Override
    protected MenuItem creaItem(Class<? extends AViewList> viewClazz) {
        String menuName = annotation.getMenuName(viewClazz);
        String icon = reflection.getIronIcon(viewClazz);
        String linkRoute = annotation.getRouteName(viewClazz);

        return new MenuItem(menuName, icon, () -> goTo(linkRoute));
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     *
     * @param viewClazz
     */
    @Override
    protected void addItem(Class<? extends AViewList> viewClazz) {
        MenuItem item = creaItem(viewClazz);
        menuButtons.add(item);
    }// end of method


    /**
     * Crea l'immagine (se esiste) dell'utente collegato <br>
     */
    private Component createAvatarComponent() {
        Div container = new Div();
        H5 userName;
        container.getElement().setAttribute("style", "text-align: center;");
        Image i = new Image("/frontend/images/avatar.png", "avatar");
        i.getElement().setAttribute("style", "width: 60px; margin-top:10px");

        //@todo per adesso non lo uso
//        if (login != null && login.getUtente() != null) {
//            container.add(i, new H5(login.getUtente().getUsername()));
//        } else {
//            container.add(i);
//        }// end of if/else cycle

        return container;
    }// end of method


    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }// end of method


    @Override
    public Component getComp() {
        return appLayout;
    }// end of method


    public AppLayout getAppLayoutFlowing() {
        return appLayout;
    }// end of method

}// end of class
