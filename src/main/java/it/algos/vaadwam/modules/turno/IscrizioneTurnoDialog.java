package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadflow.ui.fields.ATextArea;
import it.algos.vaadflow.ui.fields.ATimePicker;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.wam.WamViewDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_ISC_TUR;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: dom, 15-mar-2020
 * Time: 17:26
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
@Qualifier(TAG_ISC_TUR)
@AIScript(sovrascrivibile = false)
public class IscrizioneTurnoDialog extends WamViewDialog<Iscrizione> {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ADateService dateService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TurnoService turnoService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private IscrizioneService iscrizioneService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ServizioService servizioService;


    private Turno turnoEntity;

    private Servizio servizioEntity;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public IscrizioneTurnoDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * L'istanza DEVE essere creata con appContext.getBean(IscrizioneDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public IscrizioneTurnoDialog(IAService service, Class<? extends AEntity> binderClass, Turno turnoEntity) {
        super(service, binderClass);
        this.turnoEntity = turnoEntity;
        this.servizioEntity = turnoEntity.servizio;
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
    }// end of method


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
        String giorno = null;
        String funzione = null;
        String orario = null;

        if (turnoEntity != null) {
            giorno = turnoService.getGiornoTxt(turnoEntity);
        }// end of if cycle

        if (((Iscrizione) currentItem).funzione != null) {
            funzione = ((Iscrizione) currentItem).funzione.descrizione;
        }// end of if cycle

        if (turnoEntity != null && servizioEntity != null) {
            orario = servizioService.getOrarioLungo(servizioEntity);
        }// end of if cycle

        alertAdmin.add("Turno: " + (giorno != null ? giorno : VUOTA));
        alertAdmin.add("Funzione: " + (funzione != null ? funzione : VUOTA));
        alertAdmin.add("Orario previsto: " + (orario != null ? orario : VUOTA));

        super.fixAlertLayout();
    }// end of method


    /**
     * Costruisce nell'ordine una lista di nomi di properties <br>
     * La lista viene usata per la costruzione automatica dei campi e l'inserimento nel binder <br>
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * Sovrasrivibile nella sottoclasse <br>
     * Se serve, modifica l'ordine della lista oppure esclude una property che non deve andare nel binder <br>
     */
    protected List<String> getPropertiesName() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("milite");
        lista.add("inizio");
        lista.add("fine");
        lista.add("note");
        lista.add("durataEffettiva");
        lista.add("esisteProblema");

        return lista;
    }// end of method


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    protected void fixStandardAlgosFields() {
        ATextArea noteField = (ATextArea) getField("note");
        noteField.setLabel("Segnalazione per eventuali problemi di orario");
    }// end of method


    /**
     * Aggiunge eventuali listeners ai fields che sono stati creati SENZA listeners <br>
     * <p>
     * Chiamato da AViewLDialog.creaFields()<br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void addListeners() {
        ATimePicker inizioField = (ATimePicker) getField("inizio");
        ATimePicker fineField = (ATimePicker) getField("fine");
        ATextArea noteField = (ATextArea) getField("note");

        inizioField.addValueChangeListener(event -> sincroOrarioNote());//end of lambda expressions
        fineField.addValueChangeListener(event -> sincroOrarioNote());//end of lambda expressions
        noteField.addValueChangeListener(event -> sincroOrarioNote());//end of lambda expressions
    }// end of method


    /**
     * Calcola quanto tempo passa da 'inizio' a 'fine' dell'iscrizione e lo inserisce in 'durataField' <br>
     * 1) Controlla che 'inizio' dell'iscrizione coincida con 'inizio' del servizio <br>
     * 2) Controlla che 'fine' dell'iscrizione coincida con 'fine' del servizio <br>
     * 3) Controlla che 'note' sia vuoto <br>
     * Se tutte e 3 le condizioni sono soddisfatte, pone a 'false'  il field 'esisteProblema' <br>
     */
    private void sincroOrarioNote() {
        ATimePicker inizioField = (ATimePicker) getField("inizio");
        ATimePicker fineField = (ATimePicker) getField("fine");
        ATextArea noteField = (ATextArea) getField("note");
        IntegerField durataField = (IntegerField) getField("durataEffettiva");
        ACheckBox problemaField = (ACheckBox) getField("esisteProblema");
        LocalTime inizio = (LocalTime) inizioField.getValue();
        LocalTime fine = (LocalTime) fineField.getValue();
        String noteTxt = noteField.getValore();
        boolean esisteProblema = false;

        int durata = date.differenza(fine, inizio);
        durataField.setValue(durata);

        if (!inizio.equals(servizioEntity.inizio)) {
            esisteProblema = true;
        }// end of if cycle

        if (!fine.equals(servizioEntity.fine)) {
            esisteProblema = true;
        }// end of if cycle

        if (text.isValid(noteTxt)) {
            esisteProblema = true;
        }// end of if cycle

        problemaField.setValue(esisteProblema);
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    @Override
    protected void writeSpecificFields() {
        String currentText = VUOTA;
        super.writeSpecificFields();

        ATextArea noteField = (ATextArea) getField("note");
        if (noteField != null) {
            currentText = noteField.getValue();
            ((Iscrizione) currentItem).note = currentText;
        }// end of if cycle

        iscrizioneService.setDurata((Iscrizione) currentItem);

    }// end of method

}// end of class
