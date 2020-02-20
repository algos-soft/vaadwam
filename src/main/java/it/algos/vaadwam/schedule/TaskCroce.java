package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static it.algos.vaadwam.application.WamCost.TASK_CROCE;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * <p>
 * Task per importare tutte le croci <br>
 * Nel metodo migrationService.importAll() controlla ed importa solo le croci che hanno il flag attivo <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_CROCE)
@Slf4j
public class TaskCroce extends ATask {


    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.giornoSecondoMinuto;
    }// end of method


    /**
     * Controlla il flag 'xxxUsaDaemonCroce' (specifico) per usare o meno questa Task <br>
     */
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        migrationService.importAll();

        //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
        //@TODO Prevedere un flag di preferenze per usare il log interno
        if (true) {
            System.out.println("Task di import croci: " + date.getTime(LocalDateTime.now()));
//            mailService.send("Import croci", "Eseguito alle " + LocalDateTime.now().toString());
        }// end of if cycle
    }// end of method

}// end of class
