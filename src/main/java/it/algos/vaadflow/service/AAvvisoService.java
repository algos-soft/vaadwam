package it.algos.vaadflow.service;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.ui.dialog.polymer.bean.DialogoUnoBeanPolymer;
import org.springframework.stereotype.Service;

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

}// end of class
