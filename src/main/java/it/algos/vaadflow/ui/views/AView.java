package it.algos.vaadflow.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Sat, 27-Jul-2019
 * Time: 07:10
 * <p>
 * Ordine di chiamata:
 * costruttore (eventuale)
 * initView (@PostConstruct)
 * setParameter (override di HasUrlParameter)
 * beforeEnter (override diBeforeEnterHandler)
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Viewport("width=device-width")
@Slf4j
public abstract class AView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver, BeforeLeaveObserver {


    protected final Button cancelButton = new Button(ANNULLA);

    protected final Button confirmButton = new Button(CONFERMA);


    /**
     * Placeholder di avvisi <br>
     * Contenuto eventuale <br>
     * Label o altro per informazioni specifiche <br>
     */
    protected VerticalLayout alertPlaceholder;

    /**
     * Placeholder principale della view <br>
     * Contenuto obbligatorio <br>
     */
    protected VerticalLayout bodyPlaceholder;

    /**
     * Placeholder per i bottoni di comando <br>
     * Contenuto obbligatorio <br>
     */
    protected HorizontalLayout bottomPlaceholder;

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected ATextService text = ATextService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected AArrayService array = AArrayService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected ADateService date = ADateService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da @Route <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Flag di preferenza per usare il bottone Cancel. Normalmente true.
     */
    protected boolean usaCancelButton;

    /**
     * Flag di preferenza per usare il bottone Confirm. Normalmente true.
     */
    protected boolean usaConfirmButton;

    protected String singleParameter = "";

    protected Map<String, List<String>> multiParametersMap = null;

    protected Map<String, String> parametersMap = null;


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @PostConstruct
    protected void initView() {
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        alertPlaceholder = new VerticalLayout();
        alertPlaceholder.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        bodyPlaceholder = new VerticalLayout();
        bodyPlaceholder.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        bottomPlaceholder = new HorizontalLayout();

        //--Le preferenze standard
        fixPreferenze();
    }// end of method


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse
     */
    protected void fixPreferenze() {
        //--Flag di preferenza per usare il bottone Cancel. Normalmente true.
        usaCancelButton = true;

        //--Flag di preferenza per usare il bottone Save. Normalmente true.
        usaConfirmButton = true;
    }// end of method


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> multiParametersMap = queryParameters.getParameters();

        if (text.isValid(parameter)) {
            this.singleParameter = parameter;
        }// end of if cycle

        if (array.isValid(multiParametersMap)) {
            if (array.isMappaSemplificabile(multiParametersMap)) {
                this.parametersMap = array.semplificaMappa(multiParametersMap);
            } else {
                this.multiParametersMap = multiParametersMap;
            }// end of if/else cycle
        }// end of if cycle

    }// end of method




    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //--una o più righe di avvisi
        this.creaAlertLayout();
        if (alertPlaceholder.getComponentCount() > 0) {
            this.add(alertPlaceholder);
        }// end of if cycle

        //--Corpo centrale della vista (obbligatorio)
        this.creaBodyLayout();
        if (bodyPlaceholder.getComponentCount() > 0) {
            this.add(bodyPlaceholder);
        }// end of if cycle

        //--spazio per distanziare i bottoni dai campi
        this.add(new H3());

        //--Barra placeholder dei bottoni, creati adesso ma regolabili dopo open()
        this.creaBottomLayout();
        if (bottomPlaceholder.getComponentCount() > 0) {
            this.add(bottomPlaceholder);
        }// end of if cycle
    }// end of method


    /**
     * Placeholder (eventuale) per informazioni aggiuntive <br>
     * Deve essere sovrascritto <br>
     */
    protected void creaAlertLayout() {
    }// end of method


    /**
     * Corpo centrale della vista <br>
     * Deve essere sovrascritto <br>
     */
    protected void creaBodyLayout() {
    }// end of method


    /**
     * Barra dei bottoni
     */
    protected Component creaBottomLayout() {
        bottomPlaceholder.setClassName("buttons");
        bottomPlaceholder.setPadding(false);
        bottomPlaceholder.setSpacing(true);
        bottomPlaceholder.setMargin(false);

        Label spazioVuotoEspandibile = new Label("");
        bottomPlaceholder.add(spazioVuotoEspandibile);

        if (usaCancelButton) {
            cancelButton.addClickListener(e -> back());
            cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                cancelButton.addClickShortcut(Key.ARROW_LEFT);
            }// end of if cycle
            bottomPlaceholder.add(cancelButton);
        }// end of if cycle


        if (usaConfirmButton) {
            confirmButton.addClickListener(e -> confirm());
            confirmButton.setIcon(new Icon(VaadinIcon.CHECK));
            confirmButton.getElement().setAttribute("theme", "primary");
            if (pref.isBool(USA_BUTTON_SHORTCUT)) {
                confirmButton.addClickShortcut(Key.KEY_D, KeyModifier.ALT);
            }// end of if cycle
            bottomPlaceholder.add(confirmButton);
        }// end of if cycle

        bottomPlaceholder.setFlexGrow(1, spazioVuotoEspandibile);
        return bottomPlaceholder;
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Annulla
     */
    protected void back() {
        History history = UI.getCurrent().getPage().getHistory();
        history.back();
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Conferma
     */
    protected void confirm() {
    }// end of method


    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
    }// end of method

}// end of class
