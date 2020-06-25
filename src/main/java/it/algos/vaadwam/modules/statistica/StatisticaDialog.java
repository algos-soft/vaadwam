package it.algos.vaadwam.modules.statistica;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_STA;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 20-ott-2019 7.35.49 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * Necessario per la tipizzazione del binder <br>
 * Costruita (nella List) con appContext.getBean(StatisticaDialog.class, service, entityClazz);
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
@Qualifier(TAG_STA)
@Slf4j
@AIScript(sovrascrivibile = true)
public class StatisticaDialog extends AViewDialog<Statistica> {


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public StatisticaDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(StatisticaDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public StatisticaDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Costruisce ogni singolo field <br>
     * Fields normali indicati in @AIForfm(fields =... , aggiunti in automatico
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service <br>
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc) <br>
     * Aggiunge il field al binder, nel metodo create() del fieldService <br>
     * Aggiunge il field ad una fieldMap, per recuperare i fields dal nome <br>
     * Controlla l'esistenza tra i field di un eventuale field di tipo textArea. Se NON esiste, abilita il tasto 'return'
     *
     * @param entityBean
     * @param propertyNamesList
     */
    @Override
    protected void creaFieldsBase(AEntity entityBean, List<String> propertyNamesList) {
    }


    /**
     * Regola in lettura l'eeventuale field company (un combo)
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readCompanyField() {
        if (companyField != null) {
            companyField.setValue(((Statistica) getCurrentItem()).getCroce());
        }// end of if cycle
    }// end of method
}// end of class