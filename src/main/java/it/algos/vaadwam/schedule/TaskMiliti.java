package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
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
 * <p>
 * Import dei militi di tutte le croci <br>
 * Tempo stimato: pochi minuti <br>
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
        super.schedule = EASchedule.giornoDecimoMinuto;
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        long inizio = System.currentTimeMillis();

        if (pref.isBool(USA_DAEMON_MILITI)) {
            migrationService.importMiliti();

            if (pref.isBool(EAPreferenzaWam.usaMailImport)) {
                mailService.sendIP("Import militi", inizio);
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
