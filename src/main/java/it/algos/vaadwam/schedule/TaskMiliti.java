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

import static it.algos.vaadwam.application.WamCost.TASK_MIL;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_MILITI;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 18-lug-2018
 * Time: 09:50
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_MIL)
@Slf4j
public class TaskMiliti extends ATask {

    /**
     * Esiste il flag 'usaDaemonMiliti' per usare o meno questa Task <br>
     * Se la si usa, controlla il flag generale di debug per 'intensificare' l'import <br>
     */
    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.giornoOttavoMinuto;
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        if (pref.isBool(USA_DAEMON_MILITI)) {
            migrationService.importMiliti();

            //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
            //@TODO Prevedere un flag di preferenze per usare il log interno
            if (true) {
                System.out.println("Task di import militi: " + date.getTime(LocalDateTime.now()));
                mailService.send("Import militi", "Eseguito alle " + LocalDateTime.now().toString());
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
