package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwam.application.WamCost.TASK_TUR;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_TURNI;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 18-lug-2018
 * Time: 09:50
 * <p>
 * Import dei turni dell'anno corrente per tutte le croci <br>
 * Tempo stimato: pochi minuti <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_TUR)
@Slf4j
public class TaskTurni extends ATask {

    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.oreQuattro;
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        long inizio = System.currentTimeMillis();

        if (pref.isBool(USA_DAEMON_TURNI)) {
            migrationService.importTurniAnno();

            if (pref.isBool(EAPreferenzaWam.usaMailImport)) {
                mailService.sendIP("Import turni anno corrente", inizio);
            }// end of if cycle

        }// end of if cycle
    }// end of method

}// end o class
