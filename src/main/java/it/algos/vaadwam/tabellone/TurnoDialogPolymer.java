package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Java wrapper of the polymer element `turno-edit`
 */
@Tag("turno-dialog")
@HtmlImport("src/views/tabellone/turno-dialog.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TurnoDialogPolymer extends PolymerTemplate<TurnoDialogModel>  {

    public TurnoDialogPolymer() {
    }

}
