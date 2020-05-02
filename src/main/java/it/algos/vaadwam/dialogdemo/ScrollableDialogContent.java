package it.algos.vaadwam.dialogdemo;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;

/**
 * Polymer for the content of the scrollable dialog
 */
@Tag("dialog-content")
@HtmlImport("src/views/dialogdemo/dialog-content.html")
public class ScrollableDialogContent extends PolymerTemplate<DialogContentModel>  {

    @Id
    private Element container;

    public ScrollableDialogContent() {

        // registra il riferimento al server Java nel client JS
        // necessario perché JS possa chiamare direttamente metodi Java
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

    }


    /**
     * Regola l'altezza massima del contenitore interno dinamicamente
     */
    @ClientCallable
    public void pageReady(int w, int h){
        Style style = container.getStyle();

        // togliamo 80 pixel empiricamente
        style.set("max-height", h-80+"px");

    }


}
