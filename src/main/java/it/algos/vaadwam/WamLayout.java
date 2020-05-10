package it.algos.vaadwam;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.ui.topbar.TopbarComponent;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteProfile;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mar, 22-ott-2019
 * Time: 18:45
 */

// L'annotazione @Push va applicata al top parent
// layout e vale per tutti i layout interni
@Push

public class WamLayout extends MainLayout14 {

    private static String TUTTE = "tutte le croci";

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
    protected MiliteService militeService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected CroceService croceService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_MIL)
    private WamService wamService;

    private Dialog messageDialog;


    /**
     * Se l'applicazione è multiCompany e multiUtente, li visualizzo <br>
     * Altrimenti il nome del programma <br>
     */
    protected TopbarComponent createTopBar() {
        TopbarComponent topbar = super.createTopBar();

        if (login != null && login.isDeveloper()) {
            addDeveloper(topbar);
        }// end of if cycle

        return topbar;
    }// end of method


    protected void addDeveloper(TopbarComponent topbar) {
        Tab tab = new Tab();
        tab.add(VaadinIcon.MAGIC.create(), new Label("Developer"));
        topbar.projectSubMenu.addItem(tab, menuItemClickEvent -> {
            apreDialogo();
        });
    }// end of method


    private void apreDialogo() {
        VerticalLayout layout = new VerticalLayout();
        messageDialog = new Dialog();

        messageDialog.setCloseOnEsc(true);
        messageDialog.setCloseOnOutsideClick(true);

        layout.add(new Label("Selezione di una croce"));
        layout.add(creaGruppoCroci());
        messageDialog.add(layout);
        messageDialog.open();
    }// end of method


    private RadioButtonGroup<Croce> creaGruppoCroci() {
        RadioButtonGroup<Croce> group = new RadioButtonGroup<>();
        group.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        Croce croceAll = new Croce();
        croceAll.setCode(TUTTE);

        List<Croce> croci = croceService.findAll();
        croci.add(croceAll);
        group.setItems(croci);
//        DataProvider<Croce, ?>  data=group.getDataProvider();
//        data.getId(croceAll);
        group.addValueChangeListener(event -> modificaCroce(group.getValue()));
        return group;
    }// end of method


    protected void modificaCroce(Croce croceSelezionata) {
        Croce croceNuova = null;

        if (login != null && croceSelezionata != null) {

            if (croceSelezionata.code.equals(TUTTE)) {
                croceNuova = null;
            } else {
                croceNuova = croceSelezionata;
            }// end of if/else cycle

            login.setCompany(croceNuova);
            ((WamLogin) login).setCroce(croceNuova);

            messageDialog.close();
            Notification notification = new Notification("Croce selezionata: " + croceSelezionata.code, 2000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }// end of if cycle
    }// end of method


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
