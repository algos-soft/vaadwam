package it.algos.vaadflow.modules.person;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressPresenter;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.AddressViewDialog;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.ATextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.function.Consumer;

import static it.algos.vaadflow.application.FlowCost.FLASH;
import static it.algos.vaadflow.application.FlowCost.TAG_PER;

/**
 * Project vaadflow <br>
 * Created by Algos
 * User: Gac
 * Fix date: 26-ott-2018 9.59.58 <br>
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
@Qualifier(TAG_PER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PersonViewDialog extends AViewDialog<Person> {


    private final static String INDIRIZZO = "indirizzo";

    private AddressPresenter addressPresenter;

    private AddressService addressService;

    private AddressViewDialog addressDialog;

    private Address indirizzoTemporaneo;

    private ATextField indirizzoField;

    private ATextField mailField;

    private Consumer<Person> itemAnnulla;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     */
    @Autowired
    public PersonViewDialog(@Qualifier(TAG_PER) IAPresenter presenter) {
        super(presenter);
    }// end of constructor


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder
     * Aggiunge i fields specifici alla fieldMap
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void addSpecificAlgosFields() {
        addressPresenter = StaticContextAccessor.getBean(AddressPresenter.class);
        addressService = (AddressService) addressPresenter.getService();

        addressDialog = StaticContextAccessor.getBean(AddressViewDialog.class);
        addressDialog.setPresenter(addressPresenter);
        addressDialog.fixFunzioni(this::saveUpdate, this::deleteUpdate, this::annullaUpdate);
        addressDialog.fixConfermaAndNotRegistrazione();

        indirizzoField = (ATextField) getField(INDIRIZZO);
        if (indirizzoField != null) {
            indirizzoField.addFocusListener(e -> addressDialog.open(getIndirizzo(), EAOperation.edit, context));
        }// end of if cycle
    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readSpecificFields() {
        indirizzoTemporaneo = getIndirizzoCorrente();
        indirizzoField.setValue(indirizzoTemporaneo != null ? indirizzoTemporaneo.toString() : "");
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dallla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
        Person persona = super.getCurrentItem();
        persona.setIndirizzo(indirizzoTemporaneo);
//        service.save(persona);
    }// end of method


    private void saveUpdate(Address entityBean, EAOperation operation) {
        indirizzoTemporaneo = entityBean;
        indirizzoField.setValue(entityBean.toString());
        focusOnPost(INDIRIZZO);
        Notification.show("La modifica di indirizzo è stata confermata ma devi registrare questa persona per renderla definitiva", FLASH, Notification.Position.BOTTOM_START);
    }// end of method


    private void deleteUpdate(Address entityBean) {
        indirizzoTemporaneo = null;
        indirizzoField.setValue("");
        focusOnPost(INDIRIZZO);
    }// end of method


    protected void annullaUpdate(Address entityBean) {
        cancelButton.focus();
    }// end of method


    private Address getIndirizzoCorrente() {
        Address indirizzo = null;
        Person persona = getCurrentItem();

        if (persona != null) {
            indirizzo = persona.getIndirizzo();
        }// end of if cycle

        return indirizzo;
    }// end of method


    private Address getIndirizzo() {
        Address indirizzo = getIndirizzoCorrente();

        if (indirizzo == null) {
            indirizzo = addressService.newEntity();
        }// end of if cycle

        return indirizzo;
    }// end of method


    public void close() {
        super.close();
        if (itemAnnulla != null) {
            itemAnnulla.accept(null);
        }// end of if cycle
    }// end of method

}// end of class