package it.algos.vaadflow.service;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.ui.dialog.polymer.bean.DialogoUnoBeanPolymer;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.wam.WamLogin;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 19-mar-2020
 * Time: 18:00
 * <p>
 * Usata per costruire dei dialoghi di avviso <br>
 */
@Service
public class AAvvisoService extends AbstractService {

    private static String AVVISO = "Avviso";


    /**
     * @param bodyText (obbligatorio) Detail message
     */
    public void info(VerticalLayout comp, String bodyText) {
        DialogoUnoBeanPolymer dialogo = appContext.getBean(DialogoUnoBeanPolymer.class, AVVISO, bodyText, false);
        dialogo.foregroundColorHeader = EAColor.blue;
        dialogo.open();
        comp.add(dialogo);
    }// end of method


    /**
     * @param bodyText (obbligatorio) Detail message
     */
    public void warn(VerticalLayout comp, String bodyText) {
        DialogoUnoBeanPolymer dialogo = appContext.getBean(DialogoUnoBeanPolymer.class, AVVISO, bodyText, false);
        dialogo.foregroundColorHeader = EAColor.maroon;
        dialogo.open();
        comp.add(dialogo);
    }// end of method


    /**
     * @param bodyText (obbligatorio) Detail message
     */
    public void error(VerticalLayout comp, String bodyText) {
        DialogoUnoBeanPolymer dialogo = appContext.getBean(DialogoUnoBeanPolymer.class, AVVISO, bodyText, false);
        dialogo.foregroundColorHeader = EAColor.red;
        dialogo.open();
        comp.add(dialogo);
    }// end of method


    /**
     * Banner di avviso <br>
     */
    public HorizontalLayout fixBanner(AContext context) {
        HorizontalLayout layout = new HorizontalLayout();
        String banner = VUOTA;
        WamLogin wamLogin = (WamLogin) context.getLogin();
        layout.getElement().getStyle().set("background-color", EAColor.red.getEsadecimale());

        if (wamLogin != null && wamLogin.isDeveloper()) {
            banner = "developer mode";
        } else {
            if (wamLogin != null && wamLogin.getCroce() != null && wamLogin.getCroce().code.equals(CroceService.DEMO)) {
                banner = "demo";
            }
        }
        if (text.isValid(banner)) {
            layout.add(new Label(banner));
        } else {
            layout = null;
        }

        return layout;
    }// end of method

}// end of class
