package it.algos.vaadwam.data;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.croce.EACroce;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.log.WamLogService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.modules.croce.CroceService.DEMO;

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

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private MiliteService militeService;

    @Autowired
    private RoleService roleService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WamLogService loggerAdmin;


    /**
     * Inizializzazione dei dati di alcune collections standard sul DB mongo <br>
     * Dati specifici di questa applicazione <br>
     * <p>
     * Ricrea ad ogni startup la croce demo <br>
     * Ricrea ad ogni startup gli utenti (developer ed admin) per tutte le croci <br>
     * Ricrea ad ogni startup le funzioni per la croce demo <br>
     * Ricrea ad ogni startup i servizi per la croce demo <br>
     * Ricrea ad ogni startup i militi per la croce demo <br>
     * Crea (o controlla che esistano) i turni per la croce demo <br>
     */
    public void fixAllData() {
        croceService.fixCroceDemo();
        this.fixUtenti();
        this.fixMilitiDemo();
    }


    //    /**
    //     * Ricrea ad ogni startup la croce demo <br>
    //     */
    //    private void fixCroceDemo() {
    //        Croce croce;
    //        boolean creata = false;
    //        Person presidente;
    //        Person contatto;
    //        Address indirizzo;
    //
    //        if (croceService.isEsisteByKeyUnica(DEMO)) {
    //            if (!croceService.delete(DEMO)) {
    //                logger.warn("Non sono riuscito a cancellare la croce Demo");
    //                return;
    //            }
    //        }
    //
    //        presidente = (Person) personService.save(personService.newEntity(EAPerson.alex));
    //        contatto = (Person) personService.save(personService.newEntity(EAPerson.gac));
    //        indirizzo = (Address) addressService.save(addressService.newEntity(EAAddress.algos));
    //        creata = croceService.creaIfNotExist(EAOrganizzazione.anpas, presidente, DEMO, "Croce di prova", contatto, "345 678499", "info@algos.it", indirizzo,"Croce con dati demo. Si possono liberamente modificare. Vengono ricreati ogni notte.");
    //
    //        if (!creata) {
    //            logger.warn("Non sono riuscito a creare la croce Demo");
    //            return;
    //        }
    //    }


    /**
     * Ricrea ad ogni startup gli utenti (developer ed admin) per tutte le croci <br>
     */
    public void fixUtenti() {
        Croce croceDemo = croceService.getDEMO();
        utenteService.deleteAll();

        //--patch di accesso come developer
        utenteService.creaIfNotExist(croceDemo, "gac", "fulvia", roleService.getRoles(EARole.developer), "gac@algos.it");
        utenteService.creaIfNotExist(croceDemo, "alex", "axel01", roleService.getRoles(EARole.developer), "alex@algos.it");

        //--patch di accesso come admin per TUTTE le croci, esclusa la croce DEMO
        for (String sigla : EACroce.getValues()) {
            if (!sigla.equals(DEMO)) {
                utenteService.creaIfNotExist(croceService.findByKeyUnica(sigla), "admin-" + sigla, "fulvia", roleService.getRoles(EARole.admin), "gac@algos.it");
            }
        }

        //        //--patch di accesso per la croce DEMO
        //        utenteService.creaIfNotExist(croceService.findByKeyUnica(DEMO), "admin", "admin", roleService.getRoles(EARole.admin), "info@algos.it");
        //        utenteService.creaIfNotExist(croceService.findByKeyUnica(DEMO), "demo", "demo", roleService.getRoles(EARole.user), "info@algos.it");
    }// end of method


    /**
     * Ricrea ad ogni startup i militi (admin e user) per la croce demo <br>
     */
    public void fixMilitiDemo() {
        Croce croceDemo = croceService.getDEMO();
        String demo = "demo";
        String admin = "admin";
        String mail = "info@algos.it";
        Milite milite;
        Set<Role> ruoliDemo = roleService.getUserRole();
        Set<Role> ruoliAdmin = roleService.getAdminRoles();
        Set<Funzione> funzioni = new HashSet(funzioneService.findAllByCroce(croceDemo));
        String message = "Le tue modifiche rimangono visibili a tutti per un giorno; ogni notte i dati vengono ricreati.";

        //--cancella i preesistenti records per poterli eventualmente modificare
        militeService.delete(militeService.findByKeyUnica(demo));
        militeService.delete(militeService.findByKeyUnica(admin));

        milite = militeService.creaIfNotExist(croceDemo, "Ospite", "Ospite", VUOTA, demo, demo, ruoliDemo, mail, true, false, false, false, funzioni);
        milite.noteWam = "In questa versione demo puoi provare ad iscriverti ad ogni turno, cancellarli ed esplorare i turni esistenti. " + message;
        milite.creatoreTurni = true;
        milite.managerTabellone = false;
        militeService.save(milite);

        milite = militeService.creaIfNotExist(croceDemo, "Admin", "Admin", VUOTA, admin, admin, ruoliAdmin, mail, true, true, false, false, funzioni);
        milite.noteWam = "Come admin puoi creare, modificare turni ed iscrivere gli altri utenti. Puoi vedere e modificare funzioni, servizi e turni. " + message;
        milite.creatoreTurni = true;
        milite.managerTabellone = true;
        militeService.save(milite);

        loggerAdmin.reset("Admin e user per la croce demo");
    }// end of method

}// end of class
