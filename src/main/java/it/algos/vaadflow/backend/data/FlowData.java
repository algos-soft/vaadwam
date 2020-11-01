package it.algos.vaadflow.backend.data;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EAPreferenza;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 20-ott-2018
 * Time: 08:53
 * <p>
 * Poiché siamo in fase di boot, la sessione non esiste ancora <br>
 * Questo vuol dire che eventuali classi @VaadinSessionScope
 * NON possono essere iniettate automaticamente da Spring <br>
 * Vengono costruite con la BeanFactory <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class FlowData extends AData {




    /**
     * Inizializzazione dei dati di alcune collections standard sul DB mongo <br>
     */
    public void loadAllData() {
        roleService.loadData();
        logtypeService.loadData();
        logger.loadData();

        addressService.loadData();
        personService.loadData();
        companyService.loadData();
        if (preferenzaService.isBool(EAPreferenza.loadUtenti.getCode())) {
            utenteService.loadData();
        }// end of if cycle

        secoloService.loadData();
        meseService.loadData();
        annoService.loadData();
        giornoService.loadData();

        regioneService.loadData();
        provinciaService.loadData();
    }// end of method


}// end of class
