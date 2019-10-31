package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 17-ott-2019
 * Time: 16:52
 * <p>
 * Interfaccia per poter costruire una lista di icone da AFiledService <br>
 * Va implementata in una sottoclasse concreta che stabilisca quali icone presentare nella Select <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface IAIcon {

    /**
     * Returns icons.
     *
     * @return some icons
     */
    public List<VaadinIcon> findIcons();

}// end of class
