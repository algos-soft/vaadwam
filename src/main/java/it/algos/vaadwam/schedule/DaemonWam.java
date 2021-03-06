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
    //    private TaskImport importa;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskStatistica statistica;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskDemo demo;


    @PostConstruct
    public void startBio() throws IllegalStateException {
        if (!isStarted()) {
            super.start();

            //--non più usato
            // schedule(importa.getSchedule().getPattern(), importa);

            schedule(statistica.getSchedule().getPattern(), statistica);
            schedule(demo.getSchedule().getPattern(), demo);

        }// fine del blocco if
    }// end of method


    @Override
    public void stop() throws IllegalStateException {
        if (isStarted()) {
            super.stop();
        }// fine del blocco if
    }// end of method

}// end of class
