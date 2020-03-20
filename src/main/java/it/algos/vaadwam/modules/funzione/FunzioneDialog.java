package it.algos.vaadwam.modules.funzione;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AAvvisoService;
import it.algos.vaadflow.service.IAService;
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

import static it.algos.vaadwam.application.WamCost.TAG_FUN;

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
    @Autowired
    private AAvvisoService avvisoService;

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
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixLayout() {
        super.fixLayout();
        getFormLayout().add(addButtonIcona());
    }// end of method


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixStandardAlgosFields() {
        Object comboField = fieldMap.get("dipendenti");
        List funzioni = ((FunzioneService) service).findAll();

        if (comboField != null && array.isValid(funzioni)) {
            ((MultiselectComboBox) comboField).setItems(funzioni);
        }// end of if cycle

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


    private Button addButtonIcona() {
        iconButton = new Button("Icona");

        setIcona();
        iconButton.setWidth("8em");//@todo Non funziona
        iconButton.getElement().setAttribute("style", "color: verde");
        iconButton.addClickListener(e -> apreDialogo());

        if (wamLogin.isAdminOrDev()) {
            iconButton.setEnabled(true);
        } else {
            iconButton.setEnabled(false);
        }// end of if/else cycle

        return iconButton;
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
        boolean status = true;
        boolean usataNeiServizi = false;
        Funzione funzioneDaCancellare = (Funzione) currentItem;

        List<Servizio> servizi = servizioService.findAll();
        for (Servizio servizio : servizi) {
            if (servizioService.isContieneFunzione(servizio, funzioneDaCancellare)) {
                usataNeiServizi = true;
                status = false;
            }// end of if cycle
        }// end of for cycle

        if (usataNeiServizi) {
            avvisoService.warn(this.alertPlacehorder,"Questa funzione non può essere cancellata, perché usata in uno o più servizii");
//            Notification.show("Questa funzione non può essere cancellata, perché usata in uno o più servizii", 4000, Notification.Position.MIDDLE);
        }// end of if cycle

        return status;
    }// end of method

}// end of class