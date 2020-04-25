package it.algos.vaadflow.ui.form;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.IAService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 10-apr-2020
 * Time: 19:52
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
public abstract class APrefViewForm extends AViewForm {

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
    public APrefViewForm(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of Vaadin/@Route constructor


    /**
     * Regola i parametri provenienti dal browser per una view costruita da @Route <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l'interfaccia HasUrlParameter implementata in AViewForm <br>
     * Chiamato DOPO @PostConstruct ma PRIMA di beforeEnter() <br>
     * <p>
     * Dal browser arrivano come parametri:
     * 1) typo di form da utilizzare: New, Edit, Show (obbligatorio) <br>
     * 2) entityBean specifico (obbligatorio se Edit o Show) sotto forma di ID univoco della entityClazz specifica <br>
     * 3) link di ritorno (facoltativi) <br>
     * Può essere sovrascritto, per gestire diversamente i parametri in ingresso <br>
     * Invocare PRIMA il metodo della superclasse <br>
     *
     * @param event     con la location, ui, navigationTarget, source, ecc
     * @param parameter opzionali nella chiamata del browser
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> multiParametersMap = queryParameters.getParameters();

        if (text.isValid(parameter)) {
            this.singleParameter = parameter;
        }// end of if cycle

        if (array.isValid(multiParametersMap)) {
            if (array.isMappaSemplificabile(multiParametersMap)) {
                this.parametersMap = array.semplificaMappa(multiParametersMap);
            } else {
                this.multiParametersMap = multiParametersMap;
            }// end of if/else cycle
        }// end of if cycle

        fixParameters();
    }// end of method


    /**
     * Elabora i parametri ricevuti <br>
     * <p>
     * Dal browser arrivano come parametri:
     * 1) typo di form da utilizzare: New, Edit, Show (obbligatorio) <br>
     * 2) entityBean specifico (obbligatorio se Edit o Show) sotto forma di ID univoco della entityClazz specifica <br>
     * 3) link di ritorno (facoltativi) <br>
     */
    protected void fixParameters() {
        String operationFormTxt = VUOTA;
        String entityBeanKey = VUOTA;

        //--formType
        if (parametersMap != null) {
            if (parametersMap.containsKey(KEY_MAPPA_FORM_TYPE)) {
                operationFormTxt = parametersMap.get(KEY_MAPPA_FORM_TYPE);
                if (text.isValid(operationFormTxt)) {
                    if (EAOperation.contiene(operationFormTxt)) {
                        operationForm = EAOperation.valueOf(operationFormTxt);
                        logger.info("Per ora tutto bene. Il formOperation è arrivato del tipo: " + operationFormTxt, getClass(), "fixPreferenze");
                    } else {
                        logger.error("Il valore " + operationFormTxt + " del parametro formOperation non è tra quelli previsti", getClass(), "fixPreferenze");
                        ritorno();
                        return;
                    }// end of if/else cycle
                } else {
                    logger.error("Il parametro formOperation è arrivato ma è vuoto", getClass(), "fixPreferenze");
                    ritorno();
                    return;
                }// end of if/else cycle
            } else {
                logger.error("La mappa parametersMap non contiene formOperation", getClass(), "fixPreferenze");
                ritorno();
                return;
            }// end of if/else cycle
        } else {
            logger.error("Manca parametersMap", getClass(), "fixPreferenze");
            ritorno();
            return;
        }// end of if/else cycle

        //--entityBean
        //--parametersMap è sicuramente arrivata, se non sarebbe uscito prima
        if (parametersMap.containsKey(KEY_MAPPA_ENTITY_BEAN)) {
            entityBeanKey = parametersMap.get(KEY_MAPPA_ENTITY_BEAN);
            if (text.isValid(entityBeanKey)) {
                if (isValidEntityBeanKey(entityBeanKey)) {
                    logger.info("Per ora tutto bene. Il parametro entityBeanKey è arrivato col valore " + entityBeanKey + " ed ha recuperato la entityBean corrispondente", getClass(), "fixPreferenze");
                } else {
                    logger.error("Il valore " + entityBeanKey + " del parametro entityBeanKey non corrisponde a nessuna entityBean del mongoDB " + binderClass.getSimpleName(), getClass(), "fixPreferenze");
                    ritorno();
                    return;
                }// end of if/else cycle
            } else {
                logger.error("Il parametro entityBeanKey è arrivato ma è vuoto", getClass(), "fixPreferenze");
                ritorno();
                return;
            }// end of if/else cycle
        } else {
            if (operationFormTxt.equals(EAOperation.addNew.name())) {
                logger.info("Per ora tutto bene. Manca l'entityBeanKey ma non serviva.", getClass(), "fixPreferenze");
            } else {
                logger.error("La mappa parametersMap non contiene entityBeanKey", getClass(), "fixPreferenze");
                ritorno();
                return;
            }// end of if/else cycle
        }// end of if/else cycle

        if (entityBean == null) {
            entityBean = service.newEntity();
        }// end of if cycle
    }// end of method


    /**
     * Controlla la validità del parametro entityBeanKey in ingresso <br>
     * Regola la property interna entityBean <br>
     */
    protected boolean isValidEntityBeanKey(String entityBeanKey) {
        if (text.isValid(entityBeanKey)) {
            entityBean = service.findById(entityBeanKey);
        }// end of if cycle

        return entityBean != null;
    }// end of method


    /**
     * Preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.usaTitoloForm = true;
        super.titoloForm = VUOTA;
        super.usaFormDueColonne = true;

        super.usaBackButton = true;
        super.usaCancelButton = false;
        super.usaAnnullaButton = false;
        super.usaEditButton = operationForm == EAOperation.showOnly;
        super.minWidthForm = "50em";

        if (operationForm == EAOperation.addNew || operationForm == EAOperation.edit || operationForm == EAOperation.editNoDelete) {
            super.usaSaveButton = true;
        } else {
            super.usaSaveButton = false;
        }// end of if/else cycle
        super.usaDeleteButton = false;

        super.backButtonText = BOT_BACK;
        super.cancelButtonText = "Pippoz";
        super.annullaButtonText = "Pippoz";
        super.saveButtonText = "Pippoz";
        super.deleteButtonText = "Pippoz";

        super.backButtonIcon = "Pippoz";
        super.cancelButtonIcon = "Pippoz";
        super.annullaButtonIcon = "Pippoz";
        super.saveButtonIcon = "Pippoz";
        super.deleteButtonIcon = "Pippoz";
    }// end of method


    /**
     * Regola alcune properties (non grafiche) <br>
     */
    @Override
    protected void fixProperties() {
        //--Crea una mappa fieldMap (vuota e ordinata), per recuperare i fields dal nome
        fieldMap = new LinkedHashMap<>();

        //--Crea un nuovo binder (vuoto) per questo view e questa entityBean (currentItem)
        binder = new Binder(binderClass);
    }// end of method


}// end of class
