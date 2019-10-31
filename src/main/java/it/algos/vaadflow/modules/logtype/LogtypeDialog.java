package it.algos.vaadflow.modules.logtype;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import static it.algos.vaadflow.application.FlowCost.TAG_LOG;
import static it.algos.vaadflow.application.FlowCost.TAG_TYP;

/**
 * Project vaadflow <br>
 * Created by Algos
 * User: Gac
 * Fix date: 14-ott-2019 18.40.19 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * Necessario per la tipizzazione del binder <br>
 * Costruita (nella List) con appContext.getBean(LogtypeDialog.class, service, entityClazz);
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
@Qualifier(TAG_TYP)
@Slf4j
@AIScript(sovrascrivibile = true)
public class LogtypeDialog extends AViewDialog<Logtype> {


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public LogtypeDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(LogtypeDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public LogtypeDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor

    
}// end of class