package it.algos.vaadflow.backend.data;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.company.CompanyService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.logtype.LogtypeService;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.modules.preferenza.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.modules.utente.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private RoleService roleService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private UtenteService utenteService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private LogtypeService logtypeService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private AddressService addressService;

    /**
     * Istanza @VaadinSessionScope inietta da BeanFactory <br>
     */
    @Autowired
    private PersonService personService;

    /**
     * Istanza @VaadinSessionScope inietta da BeanFactory <br>
     */
    @Autowired
    private CompanyService companyService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private MeseService meseService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private SecoloService secoloService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private AnnoService annoService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private GiornoService giornoService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private PreferenzaService preferenzaService;


    /**
     * Inizializzazione dei dati di alcune collections standard sul DB mongo <br>
     */
    public void loadAllData() {
        roleService.loadData();
        logtypeService.loadData();

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
    }// end of method


}// end of class
