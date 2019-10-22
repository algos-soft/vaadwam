package it.algos.vaadwam;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteDialog;
import it.algos.vaadwam.modules.milite.MiliteProfile;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 22-ott-2019
 * Time: 18:45
 */
public class WamLayout extends MainLayout14 {

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_MIL)
    private WamService wamService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected MiliteService militeService;

    protected void profilePressed() {
        //--Crea il wam-login della sessione
        WamLogin wamLogin = wamService.fixWamLogin();
        Milite milite = wamLogin.getMilite();

        appContext.getBean(MiliteProfile.class, militeService, Milite.class).openWam(milite, EAOperation.editNoDelete, this::save, null);
    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void save(AEntity entityBean, EAOperation operation) {
        militeService.save(entityBean, EAOperation.editNoDelete);
    }// end of method

}// end of class
