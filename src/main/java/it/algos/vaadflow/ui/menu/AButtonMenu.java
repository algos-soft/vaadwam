package it.algos.vaadflow.ui.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.ui.LoadMode;
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
 * Time: 18:41
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public class AButtonMenu extends AMenu {

    private HorizontalLayout layout = new HorizontalLayout();


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public AButtonMenu() {
        super();
    }// end of Spring constructor


    /**
     * Costruisce il componente di questa classe concreta di menu <br>
     * Sovrascritto
     */

    @Override
    protected void inizia() {
        layout = new HorizontalLayout();
    }// end of method


    /**
     * Eventuali item di menu, se collegato come sviluppatore
     */
    protected void creaMenuDeveloper() {
        if (array.isValid(devClazzList)) {
            for (Class viewClazz : devClazzList) {
                creaItemDev(viewClazz);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Eventuali item di menu, se collegato come admin
     */
    protected void creaMenuAdmin() {
        if (array.isValid(adminClazzList)) {
            for (Class viewClazz : adminClazzList) {
                creaItemAdmin(viewClazz);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Item di menu (normali) sempre presenti
     */
    protected void creaMenuUser() {
        if (array.isValid(userClazzList)) {
            for (Class viewClazz : userClazzList) {
                creaItemUtente(viewClazz);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Crea il singolo bottone/menu <br>
     *
     * @param viewClazz
     */
    public void creaItemDev(Class<? extends AViewList> viewClazz) {
        Button button = (Button) creaItem(viewClazz);
        button.getElement().getClassList().add("rosso");
        layout.add(button);
    }// end of method


    /**
     * Crea il singolo bottone/menu <br>
     *
     * @param viewClazz
     */
    public void creaItemAdmin(Class<? extends AViewList> viewClazz) {
        Button button = (Button) creaItem(viewClazz);
        button.getElement().getClassList().add("verde");
        layout.add(button);
    }// end of method


    /**
     * Crea il singolo bottone/menu <br>
     *
     * @param viewClazz
     */
    public void creaItemUtente(Class<? extends AViewList> viewClazz) {
        Button button = (Button) creaItem(viewClazz);
        button.getElement().getClassList().add("blue");
        layout.add(button);
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     *
     * @param viewClazz
     */
    @Override
    protected Object creaItem(Class<? extends AViewList> viewClazz) {
        String menuName = annotation.getMenuName(viewClazz);
        VaadinIcon icon = reflection.getIconView(viewClazz);
        String linkRoute = annotation.getRouteName(viewClazz);

        return new Button(menuName, icon.create(), e -> goTo(linkRoute));
    }// end of method


    @Override
    public Component getComp() {
        return layout;
    }// end of method

}// end of class
