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
import java.net.InetAddress;
import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwam.application.WamCost.TASK_FUN;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_FUNZIONI;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 18-lug-2018
 * Time: 09:49
 * <p>
 * Import delle funzioni di tutte le croci <br>
 * Tempo stimato: pochi secondi <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_FUN)
@Slf4j
public class TaskFunzioni extends ATask {


    /**
     * Esiste il flag 'usaDaemonFunzioni' per usare o meno questa Task <br>
     * Se la si usa, controlla il flag generale di debug per 'intensificare' l'import <br>
     */
    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.giornoSestoMinuto;
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        long inizio = System.currentTimeMillis();

        if (pref.isBool(USA_DAEMON_FUNZIONI)) {
            migrationService.importFunzioni();

            if (pref.isBool(EAPreferenzaWam.usaMailImport)) {
                mailService.sendIP("Import funzioni", inizio);
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
