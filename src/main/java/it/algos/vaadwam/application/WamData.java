package it.algos.vaadwam.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.modules.logtype.LogtypeService;
import it.algos.vaadwam.modules.croce.CroceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 26-set-2018
 * Time: 19:21
 * <p>
 * Poiché siamo in fase di boot, la sessione non esiste ancora <br>
 * Questo vuol dire che eventuali classi @VaadinSessionScope
 * NON possono essere iniettate automaticamente da Spring <br>
 * Vengono costruite con la BeanFactory <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WamData extends AData {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private CroceService croceService;


    /**
     * Inizializzazione dei dati di alcune collections standard sul DB mongo <br>
     */
    public void loadAllData() {
//        croceService.loadData();
    }// end of method



}// end of class
