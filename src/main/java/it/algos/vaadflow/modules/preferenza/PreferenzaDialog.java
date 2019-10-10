package it.algos.vaadflow.modules.preferenza;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.company.CompanyService;
import it.algos.vaadflow.service.AEnumerationService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadflow.ui.fields.ATextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PRE;

/**
 * Project vaadflow <br>
 * Created by Algos
 * User: Gac
 * Fix date: 21-set-2019 8.28.35 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * Necessario per la tipizzazione del binder <br>
 * Costruita (nella List) con appContext.getBean(PreferenzaDialog.class, service, entityClazz);
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
@Qualifier(TAG_PRE)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PreferenzaDialog extends AViewDialog<Preferenza> {


    private final static String TIPO_FIELD_NAME = "type";

    private final static String VALUE_FIELD_NAME = "value";

    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    public CompanyService companyService;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    @Autowired
    public AEnumerationService enumService;

    private AComboBox companyField;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public PreferenzaDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(PreferenzaDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public PreferenzaDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel binder
     * Sovrascritto
     * Dopo aver creato un AField specifico, usare il metodo super.addFieldBinder() per:
     * Inizializzare AField
     */
    @Override
    protected void addSpecificAlgosFields() {
        super.addSpecificAlgosFields();

        companyField = (AComboBox) getField("company");

        if (companyField != null) {
            List items = companyService.findAll();

            if (items != null) {
                companyField.setItems(items);
            }// end of if cycle
            companyField.setEnabled(false);
        }// end of if cycle
    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Sovrascritto
     */
    @Override
    protected void readSpecificFields() {
        String enumTag = ";";
        String[] parti = null;
        super.readSpecificFields();
        if (companyField != null) {
//            companyField.setValue(((Preferenza) getCurrentItem()).getCompany());
        }// end of if cycle

        AbstractField valueField = getField(VALUE_FIELD_NAME);
        byte[] byteValue;
        Object genericValue;
        String stringValue = "";
        byteValue = getCurrentItem().getValue();
        Field reflectionJavaField = reflection.getField(Preferenza.class, "type");
        List<String> enumItems = annotation.getEnumItems(reflectionJavaField);

        if (valueField != null) {
            formLayout.remove(valueField);
            fieldMap.remove(VALUE_FIELD_NAME);
        }// end of if cycle

        EAPrefType type = getType();
        type = type != null ? type : EAPrefType.string;
        genericValue = type.bytesToObject(byteValue);
        if (genericValue instanceof String) {
            stringValue = (String) genericValue;
        }// end of if cycle

        valueField = sincro(type);
        switch (type) {
            case string:
                valueField.setValue(stringValue);
                break;
            case integer:
                valueField.setValue(genericValue.toString());
                break;
            case bool:
                valueField.setValue((boolean) genericValue);
                break;
            case date:
                if (genericValue instanceof LocalDateTime) {
                    genericValue = date.localDateTimeToLocalDate((LocalDateTime) genericValue);
                }// end of if cycle
                valueField.setValue(genericValue);
                break;
            case enumeration:
                if (text.isValid(stringValue)) {
                    ((AComboBox) valueField).setItems(enumService.getList(stringValue));
                    valueField.setValue(enumService.convertToPresentation(stringValue));
                }// end of if cycle
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        AComboBox comboType = (AComboBox) getField(TIPO_FIELD_NAME);
        comboType.setValue(type);
        if (operation == EAOperation.addNew) {
            comboType.setEnabled(true);
            comboType.addValueChangeListener(e -> sincro((EAPrefType) e.getValue()));
        } else {
            comboType.setEnabled(false);
        }// end of if/else cycle

    }// end of method


    /**
     * Cambia il valueField sincronizzandolo col comboBox
     * Senza valori, perché è attivo SOLO in modalita AddNew (new record)
     */
    protected AbstractField sincro(EAPrefType type) {
        String caption = "Valore ";
        AbstractField valueField = getField(VALUE_FIELD_NAME);
        fieldMap.remove(VALUE_FIELD_NAME);

        if (valueField != null) {
            formLayout.remove(valueField);
            valueField = null;
        }// end of if cycle

        type = type != null ? type : EAPrefType.string;
        switch (type) {
            case string:
                valueField = new ATextField(caption + "(string)");
                break;
            case integer:
                valueField = new AIntegerField(caption + "(solo numeri)");
                break;
            case bool:
                valueField = new ACheckBox(caption + "(vero/falso)");
                break;
            case date:
                valueField = new DatePicker(caption + "(giorno)");
                break;
            case enumeration:
                if (operation == EAOperation.addNew) {
                    valueField = new ATextField(caption + "(enumeration)");
                } else {
                    valueField = new AComboBox(caption + "(enumeration)");
                }// end of if/else cycle
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        if (valueField != null) {
            formLayout.add(valueField);
            fieldMap.put(VALUE_FIELD_NAME, valueField);
        }// end of if cycle

        return valueField;
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
        EAPrefType type = getType();
        AbstractField valueField = getField(VALUE_FIELD_NAME);
        byte[] byteValue;
        Object genericFieldValue = null;
        String mongoEnumValue = null;

        if (valueField != null) {
            genericFieldValue = valueField.getValue();

            switch (type) {
                case string:
                    break;
                case integer:
                    break;
                case bool:
                    break;
                case date:
                    break;
                case enumeration:
                    if (currentItem != null && text.isValid(currentItem.id)) {
                        mongoEnumValue = (String) currentItem.getType().bytesToObject(currentItem.value);
                        genericFieldValue = enumService.convertToModel(mongoEnumValue, (String) genericFieldValue);
                    } else {
                        genericFieldValue=genericFieldValue;
                    }// end of if/else cycle
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            byteValue = type.objectToBytes(genericFieldValue);
            getCurrentItem().setValue(byteValue);
        }// end of if cycle

    }// end of method


    private EAPrefType getType() {
        EAPrefType type = null;
        Preferenza preferenza = getCurrentItem();

        if (preferenza != null) {
            type = preferenza.getType();
        }// end of if cycle

        return type;
    }// end of method

}// end of class