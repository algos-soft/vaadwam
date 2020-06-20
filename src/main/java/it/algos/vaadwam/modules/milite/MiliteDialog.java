package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.wam.WamViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.algos.vaadflow.application.FlowCost.PUNTO;
import static it.algos.vaadwam.application.WamCost.TAG_MIL;
import static it.algos.vaadwam.modules.milite.MiliteList.*;
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
public class MiliteDialog extends WamViewDialog<Milite> {


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
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public MiliteDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * L'istanza DEVE essere creata con appContext.getBean(MiliteDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public MiliteDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificare informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        usaFormDueColonne = false;
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
        alertUser.add(NOME);
        alertUser.add(CANCELLARE);
        alertUser.add(ADMIN);
        alertUser.add(NICKNAME);

        alertAdmin.add(STORICO);
        alertDev.add(STORICO);

        super.fixAlertLayout();
    }// end of method


    /**
     * Costruisce nell'ordine una lista di nomi di properties <br>
     * La lista viene usata per la costruzione automatica dei campi e l'inserimento nel binder <br>
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * Sovrascrivibile nella sottoclasse <br>
     * Se serve, modifica l'ordine della lista oppure esclude una property che non deve andare nel binder <br>
     */
    @Override
    protected List<String> getPropertiesName() {
        List<String> lista;

        if (wamLogin != null && wamLogin.isAdminOrDev()) {
            //            if (wamLogin.getMilite() != null && wamLogin.getMilite().id.equals(((Milite) currentItem).id)) {
            if (((Milite) currentItem).admin) {
                lista = array.getList("ordine,nome,cognome,username,password,telefono,mail,enabled,admin,infermiere,dipendente,creatoreTurni,managerTabellone,funzioni,noteWam");
            } else {
                lista = array.getList("ordine,nome,cognome,username,password,telefono,mail,enabled,admin,infermiere,dipendente,creatoreTurni,disabIscr,funzioni,noteWam");
            }
        } else {
            lista = array.getList("ordine,nome,cognome,username,password,telefono,mail,enabled,admin,infermiere,dipendente,creatoreTurni,funzioni,noteWam");
        }

        return lista;
    }


    /**
     * Eventuali specifiche regolazioni aggiuntive ai fields del binder
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixStandardAlgosFieldsAnte() {
        List<String> lista;
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

        if (wamLogin != null && wamLogin.isAdminOrDev()) {
            if (wamLogin.isAdmin() && ((Milite) currentItem).admin && !wamLogin.getMilite().id.equals(((Milite) currentItem).id)) {
                lista = array.getList("ordine,username,enabled,nome,cognome,admin,infermiere,dipendente,creatoreTurni,managerTabellone,funzioni,noteWam");
                for (String fieldName : lista) {
                    disabilita(fieldName);
                }
                abilita(FIELD_ADMIN);
                abilita(FIELD_ATTIVO);
            }
        } else {
            //--disabilita il campo ordine
            field = fieldMap.get("ordine");
            if (field != null) {
                ((AbstractField) field).setEnabled(false);
            }// end of if cycle

            //--disabilita il comboBox funzioni
            if (comboField != null) {
                comboField.setEnabled(false);
            }// end of if cycle

            //--disabilita i campi admin, dipendente e infermiere e centralinista
            disabilita(FIELD_ATTIVO);
            disabilita(FIELD_ADMIN);
            disabilita(FIELD_DIPENDENTE);
            disabilita(FIELD_INFERMIERE);
            disabilita(FIELD_CENTRALINISTA);
        }// end of if/else cycle

        // capitalizzazione nome
        getField(FIELD_NOME).addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> {
            if (event.isFromClient()){
                getField(FIELD_NOME).setValue(StringUtils.capitalize(event.getValue()));
            }
        });

        // capitalizzazione cognome
        getField(FIELD_COGNOME).addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> {
            if (event.isFromClient()){
                getField(FIELD_COGNOME).setValue(StringUtils.capitalize(event.getValue()));
            }
        });

        //--suggerimenti per il nickname
        if (getField(FIELD_NOME) != null && getField(FIELD_COGNOME) != null) {
            ((ATextField) getField(FIELD_NOME)).addFocusListener(event -> sincro());
            ((ATextField) getField(FIELD_COGNOME)).addFocusListener(event -> sincro());
            ((ATextField) getField(FIELD_PASSWORD)).addFocusListener(event -> sincro());
        }

    }// end of method


    protected void sincro() {
        String alfa3 = (String) getField(FIELD_COGNOME).getValue();
        String nomeText;
        String cognomeTxt;
        String sep = PUNTO;
        String tag = "123";

        //        if (getField(FIELD_NOME) != null && getField(FIELD_COGNOME) != null && getField(FIELD_USERNAME) != null && getField(FIELD_PASSWORD) != null) {
        //            nomeText = text.isValid((String) getField(FIELD_NOME).getValue()) ? (String) getField(FIELD_NOME).getValue() : VUOTA;
        //            cognomeTxt = text.isValid((String) getField(FIELD_COGNOME).getValue()) ? (String) getField(FIELD_COGNOME).getValue() : VUOTA;
        //            getField(FIELD_USERNAME).setValue(nomeText.substring(0, 1).toLowerCase() + sep + cognomeTxt.toLowerCase());
        //            getField(FIELD_PASSWORD).setValue(nomeText.toLowerCase() + tag);
        //            getField(FIELD_NOME).setValue(text.primaMaiuscola(nomeText));
        //            getField(FIELD_COGNOME).setValue(text.primaMaiuscola(cognomeTxt));
        //        }
    }// end of method


    protected void abilita(String fieldName) {
        Object field;
        field = fieldMap.get(fieldName);

        if (field != null) {
            ((AbstractField) field).setEnabled(true);
        }// end of if cycle

    }// end of method


    protected void disabilita(String fieldName) {
        Object field;
        field = fieldMap.get(fieldName);

        if (field != null) {
            ((AbstractField) field).setEnabled(false);
        }// end of if cycle

    }// end of method


    //    /**
    //     * Costruisce eventuali fields specifici (costruiti non come standard type)
    //     * Aggiunge i fields specifici al binder
    //     * Aggiunge i fields specifici alla fieldMap
    //     * Sovrascritto nella sottoclasse
    //     */
    //    @Override
    //    protected void addSpecificAlgosFields() {
    //        String message = VUOTA;
    //        ACheckBox fieldLoggato;
    //        Field reflectionJavaField;
    //        String publicFieldName = "managerTabellone";
    //
    //        super.addSpecificAlgosFields();
    //
    //        if (wamLogin.getMilite() != null && wamLogin.getMilite().id.equals(((Milite) currentItem).id)) {
    //            reflectionJavaField = reflection.getField(binderClass, publicFieldName);
    //            message = annotation.getFormFieldName(reflectionJavaField);
    //            fieldLoggato = new ACheckBox(message);
    //
    //            if (fieldLoggato != null) {
    //                if (binder != null) {
    //                    binder.forField(fieldLoggato).bind(MANAGER_TABELLONE);
    //                }// end of if cycle
    //
    //                if (fieldMap != null) {
    //                    fieldMap.put(MANAGER_TABELLONE, fieldLoggato);
    //                }// end of if cycle
    //            }
    //        }
    //    }


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


    //    /**
    //     * Regola in lettura eventuali valori NON associati al binder
    //     * Dal DB alla UI
    //     * Sovrascritto
    //     */
    //    @Override
    //    protected void readSpecificFields() {
    //        List<Funzione> listaFunzioniCroce = null;
    //        List<Funzione> listaFunzioniAbilitateMilite = getFunzioniMilite();
    //        ACheckBox checkBoxField = null;
    //
    //        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
    //            listaFunzioniCroce = funzioneService.findAll();
    //
    //            if (array.isValid(listaFunzioniCroce) && array.isValid(listaFunzioniAbilitateMilite)) {
    //                for (Funzione funz : listaFunzioniCroce) {
    //                    checkBoxField = (ACheckBox) fieldMap.get(funz.getCode());
    //                    if (contiene(listaFunzioniAbilitateMilite, funz)) {
    //                        checkBoxField.setValue(true);
    //                    } else {
    //                        checkBoxField.setValue(false);
    //                    }// end of if/else cycle
    //
    //                    if (wamLogin.isDeveloper() || login.isAdmin()) {
    //                        checkBoxField.setEnabled(true);
    //                    } else {
    //                        checkBoxField.setEnabled(false);
    //                    }// end of if/else cycle
    //                }// end of for cycle
    //            }// end of if cycle
    //        }// end of if cycle
    //    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    @Override
    protected void writeSpecificFields() {
        List<Funzione> listaFunzioniCroce = null;
        Set<Funzione> listaFunzioniAbilitateMilite = null;

        if (pref.isBool(WamCost.USA_CHECK_FUNZIONI_MILITE)) {
            listaFunzioniCroce = funzioneService.findAll();

            if (array.isValid(listaFunzioniCroce)) {
                listaFunzioniAbilitateMilite = new HashSet<>();
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


    //    /**
    //     * Funzioni di questo milite
    //     */
    //    protected List<Funzione> getFunzioniMilite() {
    //        List<Funzione> listaFunzioniAbilitateMilite = null;
    //        List<Funzione> listaFunzioniCroce = null;
    //        ACheckBox field;
    //
    //        Milite milite = null;
    //        if (currentItem != null) {
    //            milite = (Milite) currentItem;
    //            return milite.funzioni;
    //        }// end of if cycle
    //
    //
    //        listaFunzioniCroce = funzioneService.findAll();
    //
    //        if (array.isValid(listaFunzioniCroce)) {
    //            listaFunzioniAbilitateMilite = new ArrayList<>();
    //            for (Funzione funz : listaFunzioniCroce) {
    //                listaFunzioniAbilitateMilite.add(funz);
    //            }// end of for cycle
    //        }// end of if cycle
    //
    //        return listaFunzioniAbilitateMilite;
    //    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected void deleteClicked() {
        if (militeCancellabile()) {
            super.deleteClicked();
        }// end of if cycle
    }// end of method


    private boolean militeCancellabile() {
        boolean usatoNeiTurni = false;
        Milite militeDaCancellare = (Milite) currentItem;
        List<Iscrizione> iscrizioni;

        List<Turno> turni = turnoService.findAllAnnoCorrente();
        for (Turno turno : turni) {
            iscrizioni = turno.iscrizioni;
            if (iscrizioni != null) {
                for (Iscrizione iscr : iscrizioni) {
                    if (iscr != null && iscr.milite != null && iscr.milite.equals(militeDaCancellare)) {
                        usatoNeiTurni = true;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of for cycle

        if (usatoNeiTurni) {
            avvisoService.warn(this.alertPlacehorder, "Questo milite non può essere cancellato, perché segnato in uno o più turni");
        }// end of if cycle

        return !usatoNeiTurni;
    }// end of method

}// end of class