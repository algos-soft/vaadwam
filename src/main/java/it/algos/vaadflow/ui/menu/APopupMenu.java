package it.algos.vaadflow.ui.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.ui.LoadMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.list.AViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-dic-2018
 * Time: 19:54
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public class APopupMenu extends AMenu {

    private HorizontalLayout layout = new HorizontalLayout();


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public APopupMenu() {
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
        AComboBox<AViewList> combo;
        ArrayList<String> namesList = null;
        String devName = "Developer";

        if (array.isValid(devClazzList)) {
            combo = new AComboBox();
            combo.getElement().getClassList().add("rosso");
            namesList = getNamesList(devClazzList);
            namesList.add(0, devName);
            combo.setItems(namesList);
            combo.setValue(devName);
            combo.addValueChangeListener(e -> sincro(e.getValue()));
            layout.add(combo);
        }// end of if cycle
    }// end of method


    /**
     * Eventuali item di menu, se collegato come admin
     */
    protected void creaMenuAdmin() {
        AComboBox<AViewList> combo;
        ArrayList<String> namesList = null;
        String adminName = "Admin";

        if (array.isValid(adminClazzList)) {
            combo = new AComboBox();
            combo.getElement().getClassList().add("verde");
            namesList = getNamesList(adminClazzList);
            namesList.add(0, adminName);
            combo.setItems(namesList);
            combo.setValue(adminName);
            combo.addValueChangeListener(e -> sincro(e.getValue()));
            layout.add(combo);
        }// end of if cycle
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

        return new Button(menuName, icon != null ? icon.create() : null, e -> goTo(linkRoute));
    }// end of method


    public void sincro(Object event) {
        Class<? extends AViewList> viewClazz = null;

        if (event instanceof String) {
            viewClazz = getViewClazz((String) event);
        }// end of if cycle

        if (viewClazz != null) {
            goTo(annotation.getRouteName(viewClazz));
        }// end of if cycle

    }// end of method


    /**
     * Crea il singolo item di menu <br>
     *
     * @param viewClazz
     */
    @Override
    protected void addItem(Class<? extends AViewList> viewClazz) {
        Button button = (Button) creaItem(viewClazz);
        layout.add(button);
    }// end of method


    @Override
    public Component getComp() {
        return layout;
    }// end of method

}// end of class
