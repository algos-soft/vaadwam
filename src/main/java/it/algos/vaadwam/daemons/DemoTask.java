package it.algos.vaadwam.daemons;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwam.modules.croce.CroceData;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 10 nov 2016.
 * <p>
 * Task eseguito periodicamente (vedi WamScheduler).
 * Cancella e ricrea la company demo per ripulirla ed ricostruire i turni prima e dopo il periodo attuale
 * Uso:
 * E' un Runnable, implementare il codice da eseguire nel metodo run()
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DemoTask implements Runnable {

//    /**
//     * Inietta da Spring come 'singleton'
//     */
//    private CroceData croceData;
//
//
//    public DemoTask(CroceData croceData) {
//        this.croceData = croceData;
//    }// end of constructor

    @Override
    public void run() {
//        croceData.creaCroci();
    }// end of method

}// end of runnable class
