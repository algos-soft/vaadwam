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

import static it.algos.vaadwam.application.WamCost.TASK_CRO;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_CROCI;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 12:19
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_CRO)
@Slf4j
public class TaskCroci extends ATask {


    /**
     * Esiste il flag 'usaDaemonCroci' per usare o meno questa Task <br>
     * Se la si usa, controlla il flag generale di debug per 'intensificare' l'import <br>
     */
    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.giornoQuintoMinuto;
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        if (pref.isBool(USA_DAEMON_CROCI)) {
            migrationService.importOnlyCroci();

            //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
            //@TODO Prevedere un flag di preferenze per usare il log interno
            if (true) {
                System.out.println("Task di import croci: " + date.getTime(LocalDateTime.now()));
                mailService.send("Import croci", "Eseguito alle " + LocalDateTime.now().toString());
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
