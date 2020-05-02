package it.algos.vaadwam.dialogdemo;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

/**
 * Sample Polymer page hosting a Polymer dialog
 */
@Route(value = "dialogdemo")
@Tag("hosting-page")
@HtmlImport("src/views/dialogdemo/hosting-page.html")
@Slf4j
public class HostingPage extends PolymerTemplate<HostingPageModel>  {

    @Id
    private Button openDialog;

    @Id
    private Dialog dialog;


    public HostingPage() {

        ScrollableDialogContent content = new ScrollableDialogContent();
        dialog.add(content);

        openDialog.addClickListener(e -> {
            dialog.open();
        });
    }




}
