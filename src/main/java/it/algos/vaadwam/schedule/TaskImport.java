package it.algos.vaadwam.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwam.enumeration.EAPreferenzaWam;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwam.application.WamCost.TASK_FUN;
import static it.algos.vaadwam.application.WamCost.USA_DAEMON_IMPORT;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 12-mag-2020
 * Time: 18:07
 * Import di tutte le croci <br>
 * Tempo stimato: alcuni minuti <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_FUN)
public class TaskImport extends ATask {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public FunzioneService funzioneService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ServizioService servizioService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MiliteService militeService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TurnoService turnoService;


    /**
     * Esiste il flag 'usaDaemon' per usare o meno questa Task <br>
     * Se la si usa, controlla il flag generale di debug per 'intensificare' l'import <br>
     */
    @PostConstruct
    public void inizia() {
        super.schedule = EASchedule.oreDue;
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        long inizio = System.currentTimeMillis();

        if (pref.isBool(USA_DAEMON_IMPORT)) {
            funzioneService.importAll();
            servizioService.importAll();
            militeService.importAll();
            turnoService.importAll();

            if (pref.isBool(EAPreferenzaWam.usaMailImport)) {
                mailService.sendIP("Import", inizio);
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
