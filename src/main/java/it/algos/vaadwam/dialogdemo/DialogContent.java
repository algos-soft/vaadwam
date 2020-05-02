package it.algos.vaadwam.dialogdemo;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;

/**
 * Polymer for the content of the dialog
 */
@Tag("dialog-content")
@HtmlImport("src/views/dialogdemo/dialog-content.html")
public class DialogContent extends PolymerTemplate<DialogContentModel>  {

    @Id
    private Element container;

    public DialogContent() {

        // registra il riferimento al server Java nel client JS
        // necessario perché JS possa chiamare direttamente metodi Java
        // è in un file .js separato per renderlo riutilizzabile
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());


//        Page page = UI.getCurrent().getPage();
//        page.addBrowserWindowResizeListener(
//                event -> {
//
//                    Style style = container.getStyle();
//                    int h = event.getHeight();
//                    //style.set("max-height", h+"px");
//                    style.set("max-height", h+"px");
//                });



    }

    /**
     * Regola l'altezza massima del contenitore interno dinamicamente
     */
    @ClientCallable
    public void pageReady(int w, int h){
        Element parent = container.getParent();
        Style style = container.getStyle();
        style.set("max-height", h-100+"px");
    }


}
