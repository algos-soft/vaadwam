package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwam.wam.WamViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 22-ott-2019
 * Time: 17:38
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_MIL)
@Slf4j
@AIScript(sovrascrivibile = false)
public class MiliteProfile extends WamViewDialog<Milite> {

    private RadioButtonGroup<String> flagIscrizioniAdmin;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public MiliteProfile() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * L'istanza DEVE essere creata con appContext.getBean(MiliteDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public MiliteProfile(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Regola il titolo del dialogo <br>
     * Recupera recordName dalle @Annotation della classe Entity. Non dovrebbe mai essere vuoto. <br>
     * Costruisce il titolo con la descrizione dell'operazione (New, Edit,...) ed il recordName <br>
     * Sostituisce interamente il titlePlaceholder <br>
     */
    @Override
    protected void fixTitleLayout() {
        titlePlaceholder.add(new H2("Profilo account"));
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
        alertAdmin.add("Per poter iscrivere tutti gli altri militi, abilita la selezione in basso");
        alertAdmin.add("Puoi sempre modificarla");
        //        alertAdmin.add("L'abilitazione vale solo per questa sessione ");

        super.fixAlertLayout();
    }


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
        return Arrays.asList("nome", "cognome", "username", "password", "telefono", "mail", "indirizzo", "noteWam");
    }// end of method


    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void fixLayoutFinal() {
        flagIscrizioniAdmin = new RadioButtonGroup<>();
        flagIscrizioniAdmin.setLabel("Come milite ti puoi iscrivere solo ai tuoi turni. Come admin puoi iscrivere tutti i militi.");
        flagIscrizioniAdmin.setItems("Milite", "Admin");

        if (((Milite) currentItem).loginComeAdmin) {
            flagIscrizioniAdmin.setValue("Admin");
        } else {
            flagIscrizioniAdmin.setValue("Milite");
        }

        getFormLayout().add(flagIscrizioniAdmin);
    }


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla UI al DB
     * Sovrascritto
     */
    @Override
    protected void writeSpecificFields() {
        String value;
        if (flagIscrizioniAdmin != null) {
            value = flagIscrizioniAdmin.getValue();
            if (text.isValid(value)) {
                switch (value) {
                    case "Milite":
                        wamLogin.setRoleType(EARoleType.user);
                        ((Milite) currentItem).loginComeAdmin = false;
                        break;
                    case "Admin":
                        wamLogin.setRoleType(EARoleType.admin);
                        ((Milite) currentItem).loginComeAdmin = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }


}// end of class
