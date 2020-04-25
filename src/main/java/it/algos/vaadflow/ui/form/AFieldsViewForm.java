package it.algos.vaadflow.ui.form;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.textfield.TextArea;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.IAService;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 10-apr-2020
 * Time: 20:13
 * Classe astratta per visualizzare il Form <br>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewForm (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewForm con la business logic principale <br>
 * - 3 APrefViewList per regolare i parametri, le preferenze ed i flags <br>
 * - 4 ALayoutViewForm per regolare il layout <br>
 * - 5 AFieldsViewForm per gestire i Fields <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 */
public class AFieldsViewForm extends ALayoutViewForm {


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public AFieldsViewForm(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of Vaadin/@Route constructor


    /**
     * Crea i fields
     * <p>
     * Crea un nuovo binder (vuoto) per questa View e questa Entity
     * Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
     * Costruisce una lista di nomi delle properties. Ordinata. Sovrascrivibile.
     * <p>
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge alla mappa (ordinata) eventuali fields specifici PRIMA di quelli automatici
     * Aggiunge i fields al binder
     * Aggiunge alla mappa (ordinata)  eventuali fields specifici DOPO quelli automatici
     * Aggiunge i fields alla mappa fieldMap
     * <p>
     * Aggiunge eventuali fields specifici (costruiti non come standard type) al binder ed alla fieldMap
     * Aggiunge i fields della fieldMap al layout grafico
     * Aggiunge eventuali fields specifici direttamente al layout grafico (senza binder e senza fieldMap)
     * Legge la entityBean ed inserisce nella UI i valori di eventuali fields NON associati al binder
     */
    @Override
    protected void creaAllFields() {

        //--Aggiunge alla mappa (ordinata) eventuali fields specifici PRIMA di quelli automatici
        this.creaFieldsBefore();

        //--Fields normali indicati in @AIForfm(fields =... , aggiunti in automatico
        this.creaFieldsBase();

        //--Aggiunge alla mappa (ordinata) eventuali fields specifici DOPO quelli automatici
        this.creaFieldsAfter();


        //--Costruisce eventuali fields specifici (costruiti non come standard type)
        //--Aggiunge i fields specifici al binder (facoltativo, alcuni fields non funzionano col binder)
        //--Se i fields non sono associati al binder, DEVONO comparire in readSpecificFields()
        //--Aggiunge i fields specifici alla fieldMap (obbligatorio)
        addSpecificAlgosFields();

        //--Aggiunge ogni singolo field della fieldMap al layout grafico
        addFieldsToLayout();

        //--Eventuali regolazioni aggiuntive ai fields del binder PRIMA di associare i valori
        this.fixStandardAlgosFieldsBefore();

        //--Associa i valori del currentItem al binder. Dal DB alla UI
        binder.readBean(entityBean);

        //--Eventuali regolazioni aggiuntive ai fields del binder DOPO aver associato i valori
        fixStandardAlgosFieldsAfter();

        //--Eventuali aggiustamenti finali al layout
        //--Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
        fixLayoutFinal();

        //--Controlla l'esistenza del field company e ne regola i valori
        fixCompanyField();

        //--Regola il focus iniziale
        fixFocus();

        //--Regola in lettura eventuali valori NON associati al binder. Dal DB alla UI
        readSpecificFields();

        //--Regola in lettura l'eeventuale field company (un combo). Dal DB alla UI
        readCompanyField();

        //--aggiunge eventuali listeners ai fields (dopo aver regolato il loro valore iniziale)
        this.addListeners();
    }// end of method


    /**
     * Aggiunge alla mappa (ordinata) eventuali fields specifici PRIMA di quelli automatici <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void creaFieldsBefore() {
    }// end of method


    /**
     * Costruisce ogni singolo field <br>
     * Fields normali indicati in @AIForfm(fields =... , aggiunti in automatico
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service <br>
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc) <br>
     * Aggiunge il field al binder, nel metodo create() del fieldService <br>
     * Aggiunge il field ad una fieldMap, per recuperare i fields dal nome <br>
     * Controlla l'esistenza tra i field di un eventuale field di tipo textArea. Se NON esiste, abilita il tasto 'return'
     */
    protected void creaFieldsBase() {
        AbstractField propertyField = null;
        boolean esisteTextArea = false;

        if (propertyNamesList != null) {
            for (String propertyName : propertyNamesList) {
                propertyField = fieldService.create(entityBean, appContext, binder, binderClass, propertyName);
                if (propertyField != null) {
                    fieldMap.put(propertyName, propertyField);
                }// end of if cycle
                if (propertyField instanceof TextArea) {
                    esisteTextArea = true;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle


//        if (!esisteTextArea) {
//            if (pref.isBool(USA_BUTTON_SHORTCUT) && isDialogoPrimoLivello) {
//                saveButton.addClickShortcut(Key.ENTER);
//            }// end of if cycle
//        }// end of if cycle

    }// end of method


    /**
     * Aggiunge alla mappa (ordinata) eventuali fields specifici DOPO quelli automatici <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void creaFieldsAfter() {
    }// end of method


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder (facoltativo, alcuni fields non funzionano col binder)
     * Se i fields non sono associati al binder, DEVONO comparire in readSpecificFields()
     * Aggiunge i fields specifici alla fieldMap (obbligatorio)
     * Sovrascritto nella sottoclasse <br>
     */
    protected void addSpecificAlgosFields() {
    }// end of method


    /**
     * Aggiunge ogni singolo field della fieldMap (ordinata) al layout grafico <br>
     */
    protected void addFieldsToLayout() {
        formLayout.removeAll();
        for (String nameField : fieldMap.keySet()) {
            if (operationForm == EAOperation.showOnly) {
                fieldMap.get(nameField).setReadOnly(true);
            }// end of if cycle
            formLayout.add(fieldMap.get(nameField));
        }// end of for cycle
    }// end of method


    /**
     * Eventuali regolazioni aggiuntive ai fields del binder PRIMA di associare i valori <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void fixStandardAlgosFieldsBefore() {
    }// end of method


    /**
     * Eventuali regolazioni aggiuntive ai fields del binder DOPO aver associato i valori <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void fixStandardAlgosFieldsAfter() {
    }// end of method


    /**
     * Eventuali aggiustamenti finali al layout <br>
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap) <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void fixLayoutFinal() {
    }// end of method


    /**
     * Controlla l'esistenza del field company e ne regola i valori <br>
     */
    protected void fixCompanyField() {
    }// end of method


    /**
     * Regola il focus iniziale <br>
     */
    protected void fixFocus() {
    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder. Dal DB alla UI <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void readSpecificFields() {
    }// end of method


    /**
     * Regola in lettura l'eeventuale field company (un combo). Dal DB alla UI <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void readCompanyField() {
    }// end of method


    /**
     * gAgiunge eventuali listeners ai fields (dopo aver regolato il loro valore iniziale) <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected void addListeners() {
    }// end of method


}// end of class
