package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadbase
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 11:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DaemonWam extends Scheduler {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private TaskCroci croci;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskCroce croce;


//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private TaskFunzioni funzioni;


//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private TaskServizi servizi;


//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private TaskMiliti militi;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskStatistica statistica;


    @PostConstruct
    public void startBio() throws IllegalStateException {
        if (!isStarted()) {
            super.start();

            // schedule(croci.getSchedule().getPattern(), croci);
            schedule(croce.getSchedule().getPattern(), croce);
            schedule(statistica.getSchedule().getPattern(), statistica);

        }// fine del blocco if
    }// end of method


    @Override
    public void stop() throws IllegalStateException {
        if (isStarted()) {
            super.stop();
        }// fine del blocco if
    }// end of method

}// end of class
