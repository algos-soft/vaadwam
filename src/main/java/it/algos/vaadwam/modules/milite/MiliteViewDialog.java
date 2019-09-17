package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.wam.WamViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;
import static it.algos.vaadwam.modules.milite.MiliteService.*;

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
@Qualifier(TAG_MIL)
@Slf4j
@AIScript(sovrascrivibile = false)
public class MiliteViewDialog extends WamViewDialog<Milite> {


    /**
     * Service iniettato da Spring (@Scope = 'singleton').
     */
    @Autowired
    private FunzioneService funzioneService;

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    private AArrayService array = AArrayService.getInstance();


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     */
    @Autowired
    public MiliteViewDialog(@Qualifier(TAG_MIL) IAPresenter presenter) {
        super(presenter);
    }// end of Spring constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.fixPreferenzeSpecifiche();

        if (wamLogin.isDeveloper()) {
            super.usaDeleteButton = true;
        } else {
            super.usaDeleteButton = false;
        }// end of if/else cycle
    }// end of method


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixStandardAlgosFields() {
        String tag = "funzioni";
        Object field;
        MultiselectComboBox comboField = (MultiselectComboBox) fieldMap.get(tag);

        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
            fieldMap.remove(tag);
        } else {
            List listaFunzioniCroce = funzioneService.findAll();
            if (comboField != null && array.isValid(listaFunzioniCroce)) {
                comboField.setItems(listaFunzioniCroce);
            }// end of if cycle
        }// end of if/else cycle

        if (wamLogin.isDeveloper() || wamLogin.isAdmin()) {
        } else {
            //--disabilita il campo ordine
            field = fieldMap.get("ordine");
            if (field != null) {
                ((AbstractField) field).setEnabled(false);
            }// end of if cycle

            //--disabilita il comboBox funzioni
            comboField.setEnabled(false);

            //--disabilita i campi admin, dipendente e infermiere
            disabilita(FIELD_ADMIN);
            disabilita(FIELD_DIPENDENTE);
            disabilita(FIELD_INFERMIERE);

        }// end of if/else cycle
    }// end of method


    protected void disabilita(String fieldName) {
        Object field;
        field = fieldMap.get(fieldName);

        if (field!=null) {
            ((AbstractField) field).setEnabled(false);
        }// end of if cycle

    }// end of method


    /**
     * Aggiunge ogni singolo field della fieldMap al layout grafico
     */
    protected void addFieldsToLayout() {
        List<Funzione> listaFunzioniCroce = null;
        ACheckBox field;

        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
            listaFunzioniCroce = funzioneService.findAll();

            if (array.isValid(listaFunzioniCroce)) {
                for (Funzione funz : listaFunzioniCroce) {
                    field = new ACheckBox(funz.getCode());
                    super.fieldMap.put(funz.getCode(), field);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        super.addFieldsToLayout();
    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Dal DB alla UI
     * Sovrascritto
     */
    @Override
    protected void readSpecificFields() {
        List<Funzione> listaFunzioniCroce = null;
        List<Funzione> listaFunzioniAbilitateMilite = getFunzioniMilite();
        ACheckBox checkBoxField = null;

        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
            listaFunzioniCroce = funzioneService.findAll();

            if (array.isValid(listaFunzioniCroce) && array.isValid(listaFunzioniAbilitateMilite)) {
                for (Funzione funz : listaFunzioniCroce) {
                    checkBoxField = (ACheckBox) fieldMap.get(funz.getCode());
                    if (contiene(listaFunzioniAbilitateMilite, funz)) {
                        checkBoxField.setValue(true);
                    } else {
                        checkBoxField.setValue(false);
                    }// end of if/else cycle

                    if (wamLogin.isDeveloper() || login.isAdmin()) {
                        checkBoxField.setEnabled(true);
                    } else {
                        checkBoxField.setEnabled(false);
                    }// end of if/else cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    @Override
    protected void writeSpecificFields() {
        List<Funzione> listaFunzioniCroce = null;
        ArrayList<Funzione> listaFunzioniAbilitateMilite = null;

        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
            listaFunzioniCroce = funzioneService.findAll();

            if (array.isValid(listaFunzioniCroce)) {
                listaFunzioniAbilitateMilite = new ArrayList<>();
                for (Funzione funz : listaFunzioniCroce) {
                    if (((ACheckBox) fieldMap.get(funz.getCode())).getValue()) {
                        listaFunzioniAbilitateMilite.add(funz);
                    }// end of if cycle
                }// end of for cycle
                ((Milite) currentItem).setFunzioni(listaFunzioniAbilitateMilite);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Controlla l'esistenza della funzione tra quelle abilitate per l'utente
     */
    private boolean contiene(List<Funzione> funzioniUtenteAbilitate, Funzione funzione) {
        boolean contiene = false;

        for (Funzione funz : funzioniUtenteAbilitate) {
            if (funz.getCode().equals(funzione.getCode())) {
                contiene = true;
                break;
            }// end of if cycle
        }// end of for cycle

        return contiene;
    }// end of method


    /**
     * Funzioni di questo milite
     */
    protected List<Funzione> getFunzioniMilite() {
        List<Funzione> listaFunzioniAbilitateMilite = null;
        List<Funzione> listaFunzioniCroce = null;
        ACheckBox field;

        Milite milite = null;
        if (currentItem != null) {
            milite = (Milite) currentItem;
            return milite.funzioni;
        }// end of if cycle


        listaFunzioniCroce = funzioneService.findAll();

        if (array.isValid(listaFunzioniCroce)) {
            listaFunzioniAbilitateMilite = new ArrayList<>();
            for (Funzione funz : listaFunzioniCroce) {
                listaFunzioniAbilitateMilite.add(funz);
            }// end of for cycle
        }// end of if cycle

        return listaFunzioniAbilitateMilite;
    }// end of method

}// end of class