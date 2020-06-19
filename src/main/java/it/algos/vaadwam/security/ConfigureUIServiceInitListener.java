package it.algos.vaadwam.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.security.SecurityUtils;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.login.WamLoginView;
import it.algos.vaadwam.tabellone.Tabellone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 21-ago-2019
 * Time: 21:45
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;


    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }


    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            if (SecurityUtils.isUserLoggedIn()) {
                if (pref.isBool(EAPreferenzaWam.redirectTabellone.getCode())) {
                    event.rerouteTo(Tabellone.class);
                } else {
                    event.rerouteTo(WamLoginView.class);
                }// end of if/else cycle
            } else {
                event.rerouteTo(WamLoginView.class);
            }// end of if/else cycle
        }// end of if cycle
    }// end of method


}// end of class
