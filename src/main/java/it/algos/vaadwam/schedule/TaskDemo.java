package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwam.modules.croce.CroceData;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwam.application.WamCost.TASK_DEMO;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_DEMO;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 11-nov-2020
 * Time: 18:26
 * <p>
 * Task per elaborare la ricostruzione della croce demo <br>
 * Nel metodo croceData.elabora() ricostruisce i dati: funzioni, servizi, militi, turni <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_DEMO)
public class TaskDemo extends ATask {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public CroceData croceData;


    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.oreDue;
    }


    /**
     * Controlla il flag 'USA_DAEMON_DEMO' per usare o meno questa Task <br>
     */
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        if (pref.isBool(USA_DAEMON_DEMO)) {
            croceData.elabora();
        }
    }

}
