package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.statistica.StatisticaService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwam.application.WamCost.TASK_STATISTICA;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_STATISTICHE;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 20-feb-2020
 * Time: 09:41
 * <p>
 * Task per elaborare le statistiche di tutte le croci <br>
 * Nel metodo statisticaService.elabora() controlla ed importa solo le croci che hanno il flag attivo <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_STATISTICA)
@Slf4j
public class TaskStatistica extends ATask {

    /**
     * Service (@Scope = 'singleton') iniettato da Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    public StatisticaService statisticaService;

    /**
     * Service (@Scope = 'singleton') iniettato da Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    public CroceService croceService;


    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.oreQuattro;
    }// end of method


    /**
     * Controlla il flag 'USA_DAEMON_STATISTICHE' (specifico per croce) per usare o meno questa Task <br>
     */
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        for (Croce croce : croceService.findAll()) {
            if (pref.isBool(USA_DAEMON_STATISTICHE, croce.code)) {
                statisticaService.elabora(croce);
            }
        }
    }

}
