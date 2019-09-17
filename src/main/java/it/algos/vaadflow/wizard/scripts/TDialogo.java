package it.algos.vaadflow.wizard.scripts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wizard.enumeration.Chiave;
import it.algos.vaadflow.wizard.enumeration.Progetto;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 03-mag-2018
 * Time: 14:07
 */
@SpringComponent
@UIScope
@Slf4j
public abstract class TDialogo extends Dialog {

    protected static final String PROJECT_BASE_NAME = "vaadflow";
    protected final static String NORMAL_WIDTH = "9em";
    protected final static String NORMAL_HEIGHT = "3em";
    protected static final String DIR_MAIN = "/src/main";
    protected static final String DIR_JAVA = DIR_MAIN + "/java/it/algos";
    protected static final String ENTITIES_NAME = "modules";
    protected TRecipient recipient;
    protected Map<Chiave, Object> mappaInput = new HashMap<>();

    protected ComboBox<Progetto> fieldComboProgetti;

    protected NativeButton confirmButton;
    protected NativeButton cancelButton;
    protected RadioButtonGroup<String> groupTitolo;
    /**
     * Service recuperato come istanza dalla classe singleton
     */
    protected ATextService text = ATextService.getInstance();
    /**
     * Service recuperato come istanza dalla classe singleton
     */
    protected AFileService file = AFileService.getInstance();


    /**
     * Costruttore
     */
    public TDialogo() {
    }// end of constructor


    @PostConstruct
    public void creaDialogo() {
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
    }// end of method


    protected Component creaFooter() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutFooter = new HorizontalLayout();
        layoutFooter.setSpacing(true);
        layoutFooter.setMargin(true);

        cancelButton = new NativeButton("Annulla", event -> {
            recipient.gotInput(null);
            this.close();
        });//end of lambda expressions
        cancelButton.setWidth(NORMAL_WIDTH);
        cancelButton.setHeight(NORMAL_HEIGHT);
        cancelButton.setVisible(true);

        confirmButton = new NativeButton("Conferma", event -> {
            chiudeDialogo();
        });//end of lambda expressions
        confirmButton.setWidth(NORMAL_WIDTH);
        confirmButton.setHeight(NORMAL_HEIGHT);
        confirmButton.setVisible(false);

        layoutFooter.add(cancelButton, confirmButton);
        layout.add(layoutFooter);
        return layout;
    }// end of method


    private void chiudeDialogo() {
        setMappa();
        recipient.gotInput(mappaInput);
        this.close();
    }// end of method


    protected void setMappa() {
    }// end of method

}// end of class
