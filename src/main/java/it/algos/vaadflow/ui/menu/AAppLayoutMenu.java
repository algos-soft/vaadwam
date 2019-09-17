package it.algos.vaadflow.ui.menu;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.ui.list.AViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 21-dic-2018
 * Time: 18:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AAppLayoutMenu extends AMenu {

    private AppLayout appLayout;

    private AppLayoutMenu appMenu;


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public AAppLayoutMenu() {
        super();
    }// end of Spring constructor


    /**
     * Costruisce il componente di questa classe concreta di menu <br>
     * Sovrascritto
     */
    @Override
    protected void inizia() {
        appLayout = new AppLayout();
        appMenu = appLayout.createMenu();
//        this.add(appLayout);
    }// end of method


//    /**
//     * Eventuali menu se collegato come sviluppatore
//     */
//    protected void creaMenuDeveloper() {
//        if (context.isDev()) {
//            addMenu(VersioneViewList.class);
//            addMenu(PreferenzaViewList.class);
//        }// end of if cycle
//    }// end of method


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

        appMenu.addMenuItem(new AppLayoutMenuItem(icon.create(), menuName, linkRoute));
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     */
    public void addMenu(Class<? extends AViewList> viewClazz) {
        String linkRoute;
        String menuName;
        VaadinIcon icon;

        menuName = annotation.getMenuName(viewClazz);
        menuName = text.primaMaiuscola(menuName);
        icon = reflection.getIconView(viewClazz);
        linkRoute = annotation.getRouteName(viewClazz);

        appMenu.addMenuItem(new AppLayoutMenuItem(icon.create(), menuName, linkRoute));
    }// end of method


    @Override
    public AppLayout getAppLayout() {
        return appLayout;
    }// end of method

}// end of class
