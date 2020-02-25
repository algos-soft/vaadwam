package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.statistica.StatisticaService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static it.algos.vaadwam.application.WamCost.TASK_CROCE;
import static it.algos.vaadwam.application.WamCost.TASK_STATISTICA;

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

    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.oreQuattro;
    }// end of method


    /**
     * Controlla il flag 'xxxUsaDaemonElabora' (specifico) per usare o meno questa Task <br>
     */
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        statisticaService.elabora();

        if (true) {
//            System.out.println("Task di elaborazione statistiche: " + date.getTime(LocalDateTime.now()));
//            mailService.send("Import croci", "Eseguito alle " + LocalDateTime.now().toString());
        }// end of if cycle
    }// end of method

}// end of class
