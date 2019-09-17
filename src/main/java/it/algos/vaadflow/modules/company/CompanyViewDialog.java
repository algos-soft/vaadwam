package it.algos.vaadflow.modules.company;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressPresenter;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.AddressViewDialog;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonPresenter;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.modules.person.PersonViewDialog;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.ATextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_COM;

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
@Qualifier(TAG_COM)
@Slf4j
@AIScript(sovrascrivibile = false)
public class CompanyViewDialog extends AViewDialog<Company> {

    public static String CONTATTO = "contatto";
    public static String INDIRIZZO = "indirizzo";

    protected PersonPresenter contattoPresenter;
    protected PersonService contattoService;
    protected PersonViewDialog contattoDialog;
    protected Person contattoTemporaneo;
    protected ATextField contattoField;

    private AddressPresenter indirizzoPresenter;
    private AddressService indirizzoService;
    private AddressViewDialog indirizzoDialog;
    private Address indirizzoTemporaneo;
    private ATextField indirizzoField;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     */
    @Autowired
    public CompanyViewDialog(@Qualifier(TAG_COM) IAPresenter presenter) {
        super(presenter);
    }// end of constructor


    /**
     * Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel binder
     * Sovrascritto
     * Dopo aver creato un AField specifico, usare il metodo super.addFieldBinder() per:
     * Inizializzare AField
     */
    @Override
    protected void addSpecificAlgosFields() {
        contattoPresenter = StaticContextAccessor.getBean(PersonPresenter.class);
        contattoService = (PersonService) contattoPresenter.getService();

        contattoDialog = StaticContextAccessor.getBean(PersonViewDialog.class);
        contattoDialog.setPresenter(contattoPresenter);
        contattoDialog.fixFunzioni(this::saveUpdateCon, this::deleteUpdateCon, this::annullaCon);
        contattoDialog.fixConfermaAndNotRegistrazione();
        contattoField = (ATextField) getField(CONTATTO);
        if (contattoField != null) {
            contattoField.addFocusListener(e -> contattoDialog.open(getContatto(), EAOperation.edit, null, CONTATTO));//todo sistemare il null
        }// end of if cycle

        indirizzoPresenter = StaticContextAccessor.getBean(AddressPresenter.class);
        indirizzoService = (AddressService) indirizzoPresenter.getService();

        indirizzoDialog = StaticContextAccessor.getBean(AddressViewDialog.class);
        indirizzoDialog.setPresenter(indirizzoPresenter);
        indirizzoDialog.fixFunzioni(this::saveUpdateInd, this::deleteUpdateInd, this::annullaInd);
        indirizzoDialog.fixConfermaAndNotRegistrazione();
        indirizzoField = (ATextField) getField(INDIRIZZO);
        if (indirizzoField != null) {
            indirizzoField.addFocusListener(e -> indirizzoDialog.open(getIndirizzo(), EAOperation.edit, null, INDIRIZZO));//todo sistemare il null
        }// end of if cycle
    }// end of method

    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readSpecificFields() {
        contattoTemporaneo = getContattoCorrente();
        contattoField.setValue(contattoTemporaneo != null ? contattoTemporaneo.toString() : "");

        indirizzoTemporaneo = getIndirizzoCorrente();
        indirizzoField.setValue(indirizzoTemporaneo != null ? indirizzoTemporaneo.toString() : "");
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dallla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields2() {
        Company company = super.getCurrentItem();
        company.setContatto(contattoTemporaneo);
        company.setIndirizzo(indirizzoTemporaneo);
    }// end of method


    protected void saveUpdateCon(Person entityBean, EAOperation operation) {
        Company company = super.getCurrentItem();
        entityBean = (Person) contattoService.beforeSave(entityBean, operation);
        contattoTemporaneo = entityBean;

        company.setContatto(contattoTemporaneo);
        contattoField.setValue(entityBean.toString());

        focusOnPost(CONTATTO);
        Notification.show("La modifica di persona è stata confermata ma devi registrare questa company per renderla definitiva", 3000, Notification.Position.BOTTOM_START);
    }// end of method


    protected void saveUpdateInd(Address entityBean, EAOperation operation) {
        Company company = super.getCurrentItem();
        entityBean = (Address) indirizzoService.beforeSave(entityBean, operation);
        indirizzoTemporaneo = entityBean;

        company.setIndirizzo(indirizzoTemporaneo);
        indirizzoField.setValue(entityBean.toString());

        focusOnPost(INDIRIZZO);
        Notification.show("La modifica di indirizzo è stata confermata ma devi registrare questa company per renderla definitiva", 3000, Notification.Position.BOTTOM_START);
    }// end of method


    protected void deleteUpdateCon(Person entityBean) {
        contattoTemporaneo = null;
        contattoField.setValue("");
        focusOnPost(CONTATTO);
        Notification.show("La cancellazione di persona è stata confermata ma devi registrare questa company per renderla definitiva", 3000, Notification.Position.BOTTOM_START);
    }// end of method


    protected void deleteUpdateInd(Address entityBean) {
        indirizzoTemporaneo = null;
        indirizzoField.setValue("");
        focusOnPost(INDIRIZZO);
        Notification.show("La cancellazione di indirizzo è stata confermata ma devi registrare questa company per renderla definitiva", 3000, Notification.Position.BOTTOM_START);
    }// end of method


    protected void annullaCon(Person entityBean) {
        focusOnPost(CONTATTO);
    }// end of method


    protected void annullaInd(Address entityBean) {
        focusOnPost(INDIRIZZO);
    }// end of method


    private Address getIndirizzoCorrente() {
        Address indirizzo = null;
        Company company = getCurrentItem();

        if (company != null) {
            indirizzo = company.getIndirizzo();
        }// end of if cycle

        return indirizzo;
    }// end of method


    private Address getIndirizzo() {
        Address indirizzo = getIndirizzoCorrente();

        if (indirizzo == null) {
            indirizzo = indirizzoService.newEntity();
        }// end of if cycle

        return indirizzo;
    }// end of method


    private Person getContattoCorrente() {
        Person persona = null;
        Company company = getCurrentItem();

        if (company != null) {
            persona = company.getContatto();
        }// end of if cycle

        return persona;
    }// end of method


    private Person getContatto() {
        Person persona = getContattoCorrente();

        if (persona == null) {
            persona = contattoService.newEntity();
        }// end of if cycle

        return persona;
    }// end of method


}// end of class