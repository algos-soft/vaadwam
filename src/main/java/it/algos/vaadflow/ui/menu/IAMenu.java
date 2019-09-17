package it.algos.vaadflow.ui.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 21-dic-2018
 * Time: 18:05
 * Interfaccia per la gestione di una barra di menu, generale per tutta l'applicazione.
 * <p>
 * La classe concreta può essere realizzata in diverse modalità,
 * che restano comunque trasparenti per gli utilizzatori dell'interfaccia
 */
public interface IAMenu {

    public AppLayout getAppLayout();

    public Component getComp();

}// end of interface
