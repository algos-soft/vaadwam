package it.algos.vaadwam.testjs;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.tabellone.*;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.*;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Sample polymer to test JS interaction
 */
@JavaScript("frontend://js/js-comm.js")
@JavaScript("frontend://js/testjs.js")
//@Route(value = "testjs", layout = AppLayout.class)
@Route(value = "testjs")
@Tag("testjs-polymer")
@HtmlImport("src/views/js/testjs-polymer.html")
@Slf4j
public class TestJS extends PolymerTemplate<TestJSModel>  {

//    @Autowired
//    protected ApplicationContext appContext;


//    @Id("tabellonegrid")
//    private Grid grid;

    // links to the Java buttons
    @Id
    private Button bwide_java;
    @Id
    private Button bnarrow_java;
    @Id
    private Button btall_java;
    @Id
    private Button bshort_java;



    @Autowired
    public TestJS() {

        setId("template");

        getModel().setGreeting("Please enter your name");


        // registra il riferimento al server Java nel client JS
        UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());

        // inizializza lo script specifico per questa pagina
        UI.getCurrent().getPage().executeJs("initTestJS()");

        // aggiunge i listener ai bottoni Java che invocano le funzioni JS
        bwide_java.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("wider()");
            log.info("wider() function invoked in JS");
        });
        bnarrow_java.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("narrower()");
            log.info("narrower() function invoked in JS");
        });
        btall_java.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("taller()");
            log.info("taller() function invoked in JS");
        });
        bshort_java.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("shorter()");
            log.info("shorter() function invoked in JS");
        });





//        VerticalLayout comp = new VerticalLayout();
//        comp.setId("comp1");
//        comp.getStyle().set("width","250px");
//        comp.getStyle().set("height","200px");
//        comp.getStyle().set("background-color","yellow");
//        comp.getStyle().set("overflow-y","scroll");
//        comp.getStyle().set("overflow-x","scroll");

        //this.add(comp);

    }


    @EventHandler
    private void sayHello() {
        // Called from the template click handler
        getModel().setGreeting(Optional.ofNullable(getModel().getUserInput())
                .filter(userInput -> !userInput.isEmpty())
                .map(greeting -> String.format("Hello %s!", greeting))
                .orElse("Please enter your name"));
    }


    /**
     * Invoked from the JS client when is safe to invoke JS functions operating on the DOM.
     * (The DOM is ready and the page is completely loaded).
     * For this command to work, remember to register the server in the constructor:
     * UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());
     */
    @ClientCallable
    public void pageReady(){
        //getElement().executeJs("scrollTo(0, 200)");
    }



//    @Override
//    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
//    }
//
//    @Override
//    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
//    }
//
//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//    }
//
//    @Override
//    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
//    }



}
