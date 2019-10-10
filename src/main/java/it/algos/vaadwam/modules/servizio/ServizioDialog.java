package it.algos.vaadwam.modules.servizio;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAColor;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadwam.application.WamCost.TAG_SER;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 30-set-2018 16.22.05 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_SER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class ServizioDialog extends AViewDialog<Servizio> {

    private final static String FUNZIONI = "funzioni";

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected FunzioneService funzioneService;

    private Grid grid;

    private Button addButton;

    private Button deleteButton;

    private ComboBox<String> comboColorField = null;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    protected WamLogin wamLogin;

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public ServizioDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(ServizioDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public ServizioDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor

    /**
     * Regola login and context della sessione <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixLoginContext() {
        super.fixLoginContext();

        AContext context = null;
        VaadinSession vaadSession = UI.getCurrent().getSession();

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        if (context != null && context.getLogin() != null) {
            wamLogin = (WamLogin) context.getLogin();
        }// end of if cycle
    }// end of method

    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (wamLogin.isAdminOrDev()) {
            super.usaDeleteButton = true;
        } else {
            super.usaDeleteButton = false;
        }// end of if/else cycle
    }// end of method


    /**
     * Body placeholder per i campi, creati dopo open()
     */
    protected Div creaFormLayout2() {
        Div div;
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("50em", 1));
        formLayout.addClassName("no-padding");
        div = new Div(formLayout);
        div.addClassName("has-padding");

        return div;
    }// end of method


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder
     * Aggiunge i fields specifici alla fieldMap
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void addSpecificAlgosFields() {
        super.addSpecificAlgosFields();

        comboColorField = new ComboBox("Color");
        comboColorField.setWidth("4em");
        ArrayList<String> items = new ArrayList();
        for (EAColor color : EAColor.values()) {
            items.add(color.name());
        }// end of for cycle

        comboColorField.setItems(items);
        if (comboColorField != null) {
            fieldMap.put("colorfield", comboColorField);
        }// end of if cycle
    }// end of method


    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    protected void fixLayout() {
        if (currentItem != null) {
            this.getFormLayout().add(creaGrid(((Servizio) currentItem).funzioni));
        }// end of if cycle
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
        String colorValue = "";

        if (comboColorField != null) {
            colorValue = comboColorField.getValue();
            ((Servizio) currentItem).colore=colorValue;
        }// end of if cycle
    }// end of method


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public Grid creaGrid(List funzioniDiQuestoServizio) {
        String widthA = "4em";
        String widthB = "6em";
        String widthC = "12em";
        grid = new Grid(Funzione.class);
        if (array.isValid(funzioniDiQuestoServizio)) {
            grid.setItems(funzioniDiQuestoServizio);
        }// end of if cycle

        for (Object column : grid.getColumns()) {
            grid.removeColumn((Grid.Column) column);
        }// end of for cycle

        //--aggiunge popup di selezione
        Grid.Column colonnaCombo = grid.addComponentColumn(funzione -> {
            List<String> items = null;
            String idKey = ((Funzione) funzione).getId();
            ComboBox combo = new ComboBox();
            combo.setWidth(widthC);

            if (text.isValid(idKey)) {
                items = funzioneService.findAllCode();
                combo.setItems(items);
                combo.setValue(((Funzione) funzione).getCode());
                combo.setEnabled(false);
            } else {
                items = findCodiciFunzioniRimanentiNonAncoraUsate();
                combo.setItems(items);
            }// end of if/else cycle


            combo.addValueChangeListener(new HasValue.ValueChangeListener<HasValue.ValueChangeEvent<String>>() {

                @Override
                public void valueChanged(HasValue.ValueChangeEvent<String> stringValueChangeEvent) {
                    fixNewItem(stringValueChangeEvent.getValue());
                }// end of method
            });// end of anonymous inner class
            return combo;
        });//end of lambda expressions
        colonnaCombo.setHeader("Code");
        colonnaCombo.setId("combo");
        colonnaCombo.setWidth(widthB);


        //--aggiunge la icona
        Grid.Column colonnaIcona = grid.addComponentColumn(funzione -> {
            Icon icon = null;
            VaadinIcon vaadinIcon;

            if (funzione != null) {
                vaadinIcon = ((Funzione) funzione).getIcona();
                if (vaadinIcon != null) {
                    icon = vaadinIcon.create();
                }// end of if cycle
            }// end of if cycle
            if (icon != null) {
                if (((Funzione) funzione).obbligatoria) {
                    icon.getElement().getClassList().add("rosso");
                } else {
                    icon.getElement().getClassList().add("blue");
                }// end of if/else cycle
                return icon;
            } else {
                return new Label("");
            }// end of if/else cycle
        });//end of lambda expressions
        colonnaIcona.setHeader("Icona");
        colonnaIcona.setId("icona");
        colonnaCombo.setWidth(widthA);


        //--aggiunge la sigla
        Grid.Column colonnaSigla = grid.addComponentColumn(funzione -> {
            Label label = new Label("");
            String sigla = "";

            if (funzione != null) {
                sigla = ((Funzione) funzione).getSigla();
                label.setText(sigla);
            }// end of if cycle

            if (((Funzione) funzione).obbligatoria) {
                label.getElement().getClassList().add("rosso");
            } else {
                label.getElement().getClassList().add("blue");
            }// end of if/else cycle

            return label;
        });//end of lambda expressions
        colonnaSigla.setHeader("Sigla");
        colonnaSigla.setId("sigla");
        colonnaCombo.setWidth(widthA);


        //--aggiunge la descrizione
        Grid.Column colonnaDescrizione = grid.addComponentColumn(funzione -> {
            Label label = new Label("");
            String descrizione = "";

            if (funzione != null) {
                descrizione = ((Funzione) funzione).getDescrizione();
                label.setText(descrizione);
            }// end of if cycle

            if (((Funzione) funzione).obbligatoria) {
                label.getElement().getClassList().add("rosso");
            } else {
                label.getElement().getClassList().add("blue");
            }// end of if/else cycle

            return label;
        });//end of lambda expressions
        colonnaDescrizione.setHeader("Descrizione");
        colonnaDescrizione.setId("descrizione");
        colonnaCombo.setWidth(widthC);


        //--aggiunge l'obbligatorietà
        Grid.Column colonnaCheck = grid.addComponentColumn(funzione -> {
            ACheckBox checkBox = new ACheckBox("");
            String idKey = "";

            if (funzione != null) {
                idKey = ((Funzione) funzione).getId();
                if (text.isValid(((Funzione) funzione).getId())) {
                    boolean obbligatoria = ((Funzione) funzione).isObbligatoria();
                    checkBox.setValue(obbligatoria);
                }// end of if cycle
            }// end of if cycle


            if (checkBox != null) {
//                checkBox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent>() {
//
//                    @Override
//                    public void valueChanged(AbstractField.ComponentValueChangeEvent componentValueChangeEvent) {
//
//                    }// end of method
//                });// end of anonymous inner class
            }// end of if cycle

            return checkBox;
        });//end of lambda expressions
        colonnaCheck.setHeader("Obb.");
        colonnaCheck.setId("obb");
        colonnaCheck.setWidth(widthA);

        ArrayList lista = new ArrayList();
        lista.add("combo");
        lista.add("sigla2");
        lista.add("descrizione2");
        lista.add("obb");
//        grid.setColumns((String[]) lista.toArray(new String[lista.size()]));
//        float lar = grid.getWidth();
//        grid.setWidth(lar + width + column.WIDTH_CHECK_BOX + 565, Unit.PIXELS);


//        grid.setStyleGenerator(new StyleGenerator() {

//            @Override
//            public String apply(Object o) {
//                return "error_row";
//            }
//        });

        //--header
        fixGridHeader();

        //--listener
        grid.addSelectionListener(new SelectionListener() {

            @Override
            public void selectionChange(SelectionEvent selectionEvent) {
                deleteButton.setEnabled(grid.getSelectedItems().size() > 0);
            }// end of method
        });// end of anonymous inner class

        return grid;
    }// end of method


    /**
     * Eventuale header text
     */
    private void fixGridHeader() {
        String message = "Funzioni previste per questo tipo di servizio";
        Component comp = new HorizontalLayout();

        addButton = new Button("Add", event -> addFunzione());
        addButton.setIcon(new Icon("lumo", "plus"));
        addButton.addClassName("review__edit");
        addButton.getElement().setAttribute("theme", "secondary");

        deleteButton = new Button("Delete", event -> deleteFunzione());
        deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        deleteButton.addClassName("review__edit");
        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.setEnabled(false);

        ((HorizontalLayout) comp).add(new Label(message));
        ((HorizontalLayout) comp).add(addButton);
        ((HorizontalLayout) comp).add(deleteButton);
        HeaderRow topRow = grid.prependHeaderRow();
        Grid.Column[] matrix = array.getColumnArray(grid);
        HeaderRow.HeaderCell informationCell = topRow.join(matrix);
        informationCell.setComponent(comp);
    }// end of method


    /**
     * Selezione le funzioni ancora possibili per questo servizio <br>
     * Tutte quelle della croce meno quelle già previste <br>
     * Evita che ci possano essere duplicati <br>
     */
    private ArrayList<String> findCodiciFunzioniRimanentiNonAncoraUsate() {
        ArrayList<String> funzioniRimanenti = new ArrayList<String>();
        List<String> allFunzioni = funzioneService.findAllCode();
        List funzioniAttualmenteVisualizzate = findCodeVisualizzate();

        for (String funzCode : allFunzioni) {
            if (funzioniAttualmenteVisualizzate.contains(funzCode)) {
            } else {
                funzioniRimanenti.add(funzCode);
            }// end of if/else cycle
        }// end of for cycle

        return funzioniRimanenti;
    }// end of method


    /**
     * Aggiunge una nuova funzione al servizio
     */
    private void addFunzione() {
        ArrayList funzioniAttualmenteVisualizzate = new ArrayList(((Servizio) currentItem).funzioni);
        Funzione newFunzioneVuotaDaInserire = funzioneService.newEntity();
        funzioniAttualmenteVisualizzate.add(newFunzioneVuotaDaInserire);
        ((Servizio) currentItem).funzioni = funzioniAttualmenteVisualizzate;
        grid.setItems(funzioniAttualmenteVisualizzate);

        sincroAddButton();
    }// end of method


    /**
     * Regola la funzione appena aggiunta <br>
     * Si suppone che sia l'ultima e che i valori 'id', 'code' e 'sigla' siano vuoti <br>
     */
    private void fixNewItem(String code) {
        Funzione newFunzioneDaRegolare = ((Servizio) currentItem).funzioni.get(((Servizio) currentItem).funzioni.size() - 1);
        Funzione funzioneDellaCroce = funzioneService.findByKeyUnica(code);

        if (funzioneDellaCroce != null) {
            newFunzioneDaRegolare.id = funzioneDellaCroce.id;
            newFunzioneDaRegolare.code = funzioneDellaCroce.code;
            newFunzioneDaRegolare.sigla = funzioneDellaCroce.sigla;
            newFunzioneDaRegolare.descrizione = funzioneDellaCroce.descrizione;
            newFunzioneDaRegolare.icona = funzioneDellaCroce.icona;
            grid.setItems(((Servizio) currentItem).funzioni);
        }// end of if cycle
    }// end of method


    /**
     * Elimina la funzione selezionata
     */
    private void deleteFunzione() {
        ArrayList funzioniAttualmenteVisualizzate = new ArrayList(((Servizio) currentItem).funzioni);

        if (grid.getSelectedItems().size() == 1) {
            Funzione funz = (Funzione) grid.getSelectedItems().toArray()[0];
            if (funzioniAttualmenteVisualizzate != null && funz != null && funzioniAttualmenteVisualizzate.contains(funz)) {
                funzioniAttualmenteVisualizzate.remove(funz);
                ((Servizio) currentItem).funzioni = funzioniAttualmenteVisualizzate;
                grid.setItems(funzioniAttualmenteVisualizzate);
            }// end of if cycle
        }// end of if cycle

        sincroAddButton();
    }// end of method


    /**
     * Elenco dei codici delle funzioni già visualizzate <br>
     */
    private ArrayList<String> findCodeVisualizzate() {
        ArrayList<String> codeRimanenti = new ArrayList<String>();
        List<Funzione> funzioniAttualmenteVisualizzate = new ArrayList(((Servizio) currentItem).funzioni);

        for (Funzione funz : funzioniAttualmenteVisualizzate) {
            codeRimanenti.add(funz.code);
        }// end of for cycle

        return codeRimanenti;
    }// end of method


    /**
     * Elenco dei codici delle funzioni già visualizzate <br>
     */
    private void sincroAddButton() {
        ArrayList<String> codeRimanenti = new ArrayList<String>();
        List<Funzione> funzioniAttualmenteVisualizzate = new ArrayList(((Servizio) currentItem).funzioni);

        for (Funzione funz : funzioniAttualmenteVisualizzate) {
            codeRimanenti.add(funz.code);
        }// end of for cycle
        addButton.setEnabled(((Servizio) currentItem).funzioni.size() < funzioneService.count());
    }// end of method


}// end of class