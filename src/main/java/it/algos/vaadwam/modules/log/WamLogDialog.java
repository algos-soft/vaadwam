package it.algos.vaadwam.modules.log;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadwam.application.WamCost.TAG_WAM_LOG;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 27-mag-2020
 * Time: 09:36
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_WAM_LOG)
@Slf4j
@AIScript(sovrascrivibile = true)
public class WamLogDialog extends AViewDialog<WamLog> {

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public WamLogDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(LogDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public WamLogDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
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
        super.fixAlertLayout();

        alertPlacehorder.add(text.getLabelAdmin("Scheda di log non modificabile"));

        if (login != null && login.isDeveloper()) {
            alertPlacehorder.add(text.getLabelDev("Scheda di log modificabile solo nella descrizione"));
            alertPlacehorder.add(text.getLabelDev("Le modifiche alle altre properties non vengono comunque registrate"));
        }
    }


    /**
     * Aggiunge ogni singolo field della fieldMap al layout grafico
     */
    @Override
    protected void addFieldsToLayout() {
        getFormLayout().removeAll();

        for (String name : fieldMap.keySet()) {
            if (name.equals("descrizione")) {
                getFormLayout().add(fieldMap.get(name), 2);
            } else {
                getFormLayout().add(fieldMap.get(name));
            }
        }// end of for cycle
    }

}
