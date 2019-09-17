package it.algos.vaadwam.application;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.ui.MainLayout;

import static it.algos.vaadflow.application.FlowCost.TAG_HOME;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 8-mag-2018
 * Vista di partenza iniziale di questa applicazione <br>
 * <p>
 * Not annotated with @SpringComponent (sbagliato) <br>
 * Not annotated with @Scope (inutile) <br>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 */
@Route(value = TAG_HOME, layout = MainLayout.class)
@AIScript(sovrascrivibile = false)
public class HomeView extends VerticalLayout {


    /**
     * Icona visibile nel menu (facoltativa)
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.HOME;

    private Image immagine = new Image("frontend/images/ambulanza.jpg", "vaadin");

    /**
     * Costruttore
     */
    public HomeView() {
        removeAll();
        add(immagine);
    }// end of constructor


//    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();
        add(immagine);
    }// end of method


}// end of class