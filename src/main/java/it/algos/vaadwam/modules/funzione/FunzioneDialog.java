package it.algos.vaadwam.modules.funzione;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.wam.WamViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.io.Serializable;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_FUN;
import static it.algos.vaadwam.wam.WamViewList.USER_VISIONE;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 10-ott-2019 21.14.36 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * Necessario per la tipizzazione del binder <br>
 * Costruita (nella List) con appContext.getBean(FunzioneDialog.class, service, entityClazz);
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_FUN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class FunzioneDialog extends WamViewDialog<Funzione> {


    @Autowired
    ApplicationContext appContext;

    @Autowired
    private ServizioService servizioService;

    private Button iconButton;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public FunzioneDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(FunzioneDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public FunzioneDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaFormDueColonne = false;
    }


    /**
     * Eventuali messaggi di avviso specifici di questo dialogo ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewDialog.open() <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * DOPO invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertLayout() {
        alertUser.add(USER_VISIONE);
        alertAdmin.add("La sigla appare nelle iscrizioni dei turni e può essere liberamente modificata");
        alertAdmin.add("Questa funzione può essere cancellata solo se non è usata in nessun servizio");
        alertAdmin.add("Le funzioni dipendenti,abilitate automaticamente, vengono indicate con la sigla");
        alertDev.add("Il Code è utilizzato internamente e non può essere modificato una volta creata la funzione");
        alertDev.add("Devi eventualmente cancellare prima il servizio che la usa");

        super.fixAlertLayout();
    }// end of method


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixStandardAlgosFieldsAnte() {
        String fieldName = "dipendenti";
        MultiselectComboBox<Funzione> comboFunzioni = null;
        MultiselectComboBox comboField = (MultiselectComboBox) fieldMap.get(fieldName);
        List<Funzione> funzioni = ((FunzioneService) service).findAll();
        String caption = VUOTA;

        if (comboField != null && array.isValid(funzioni)) {
            caption = annotation.getCaption(Funzione.class, fieldName);
            comboFunzioni = new MultiselectComboBox<>();
            comboFunzioni.setLabel(text.primaMaiuscola(caption));
            comboFunzioni.setItems(funzioni);
            comboFunzioni.setItemLabelGenerator(Funzione::getSigla);
            comboFunzioni.setValue(((Funzione) currentItem).dipendenti);
            fieldMap.put(fieldName, comboFunzioni);
        }// end of if cycle

    }// end of method


    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixLayoutFinal() {
        super.fixLayoutFinal();

        //--bottone icona con label/caption
        String caption = annotation.getCaption(Funzione.class, "icona");
        getFormLayout().add(text.getLabelAdmin(caption));
        getFormLayout().add(addButtonIcona());

        if (fieldMap.get("id") != null) {
            ((ATextField) fieldMap.get("id")).setEnabled(false);
        }

        if (fieldMap.get("croce") != null) {
            ((AComboBox) fieldMap.get("croce")).setEnabled(false);
        }

        if (fieldMap.get("code") != null) {
            if (wamLogin != null && wamLogin.isDeveloper()) {
                ((ATextField) fieldMap.get("code")).setEnabled(true);
            } else {
                ((ATextField) fieldMap.get("code")).setEnabled(false);
            }
        }

    }// end of method


    private Button addButtonIcona() {
        iconButton = new Button("Icona");

        setIcona();
        iconButton.setWidth("8em");//@todo Non funziona
        iconButton.getElement().setAttribute("style", "color: verde");
        iconButton.getElement().setAttribute("style", "width:8em");
        iconButton.addClickListener(e -> apreDialogo());

        if (wamLogin.isAdminOrDev()) {
            iconButton.setEnabled(true);
        } else {
            iconButton.setEnabled(false);
        }// end of if/else cycle

        return iconButton;
    }// end of method


    private void setIcona() {
        VaadinIcon vaadinIcon = ((Funzione) currentItem).getIcona();
        Icon icona;
        if (vaadinIcon != null) {
            icona = vaadinIcon.create();
        } else {
            icona = VaadinIcon.ADD_DOCK.create();
        }// end of if/else cycle
        icona.getElement().getClassList().add("verde");
        iconButton.setIcon(icona);
    }// end of method


    public void apreDialogo() {
        SelectIconDialog dialog = appContext.getBean(SelectIconDialog.class, "Icone");
        dialog.open((Funzione) currentItem, this::aggiornaIcona);
    }// end of method


    public void aggiornaIcona() {
        setIcona();
    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected void deleteClicked() {
        if (funzioneCancellabile()) {
            super.deleteClicked();
        }// end of if cycle
    }// end of method


    private boolean funzioneCancellabile() {
        boolean usataNeiServizi = false;
        Funzione funzioneDaCancellare = (Funzione) currentItem;

        List<Servizio> servizi = servizioService.findAll();
        for (Servizio servizio : servizi) {
            if (servizioService.isContieneFunzione(servizio, funzioneDaCancellare)) {
                usataNeiServizi = true;
            }// end of if cycle
        }// end of for cycle

        if (usataNeiServizi) {
            avvisoService.warn(this.alertPlacehorder, "Questa funzione non può essere cancellata, perché usata in uno o più servizi");
        }// end of if cycle

        return !usataNeiServizi;
    }// end of method

}// end of class