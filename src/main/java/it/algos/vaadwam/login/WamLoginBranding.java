package it.algos.vaadwam.login;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;

/**
 * Sezione di branding della finestra di login
 */
public class WamLoginBranding extends Div {

    public WamLoginBranding() {
        super();

        getElement().setAttribute("style", "display:flex; width:100%; flex-direction:column; align-items:center");

        Image img = new Image("/frontend/images/wam.svg", "wam");
        img.setWidth("7em");
        img.setHeight("7em");

        Label label = new Label("Turni Ambulanze");
        label.getElement().setAttribute("style", "font-weight: 500");

        add(img);
        add(label);

    }
}
