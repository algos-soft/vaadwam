package it.algos.vaadflow.ui.dialog.polymer.route;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 16-ago-2019
 * Time: 07:15
 * <p>
 * Java wrapper of the polymer element `dialogo-zero`
 */
@Route(value = "dialogozero")
@Tag("dialogo-zero")
@HtmlImport("src/views/dialoghi/dialogo-zero.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogoZeroRoutePolymer extends DialogoRoutePolymer {


}// end of class
