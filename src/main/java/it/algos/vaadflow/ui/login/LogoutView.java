package it.algos.vaadflow.ui.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.ui.list.ALayoutViewList;

import static it.algos.vaadflow.application.FlowCost.TAG_LOGOUT;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 12-ott-2019
 * Time: 06:45
 */
@UIScope
@Route(value = TAG_LOGOUT)
@AIView(vaadflow = true, menuName = "logout", menuIcon = VaadinIcon.ESC)
public class LogoutView extends ALayoutViewList implements BeforeEnterObserver {

    public LogoutView() {
        super(null, null);
    }// end of constructor


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
    }// end of method

}// end of class
