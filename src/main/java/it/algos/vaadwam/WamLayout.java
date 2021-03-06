package it.algos.vaadwam;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.server.VaadinSession;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.ui.topbar.TopbarComponent;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceList;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.FunzioneList;
import it.algos.vaadwam.modules.log.WamLogList;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteList;
import it.algos.vaadwam.modules.milite.MiliteProfile;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.ServizioList;
import it.algos.vaadwam.modules.statistica.StatisticaList;
import it.algos.vaadwam.topbar.WamTopbar;
import it.algos.vaadwam.wam.WamLogin;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;
import static it.algos.vaadwam.modules.croce.CroceService.DEMO;

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

    //    @Override
    //    public void configurePage(InitialPageSettings settings) {
    //        settings.addInlineFromFile(InitialPageSettings.Position.PREPEND,
    //                "inline.js", InitialPageSettings.WrapMode.JAVASCRIPT);
    //
    //        settings.addMetaTag("og:title", "The Rock");
    //        settings.addMetaTag("og:type", "video.movie");
    //        settings.addMetaTag("og:url",
    //                "http://www.imdb.com/title/tt0117500/");
    //        settings.addMetaTag("og:image",
    //                "http://ia.media-imdb.com/images/rock.jpg");
    //
    //        settings.addLink("shortcut icon", "images/favicon.ico");
    //        settings.addFavIcon("icon", "images/medal.ico", "192x192");
    //    }


    /**
     * Recupera i dati della sessione
     */
    @Override
    protected void fixSessione() {
        super.fixSessione();

        if (login != null && login instanceof WamLogin) {
            if (((WamLogin) login).isAdminOrDev()) {
                FlowVar.usaDrawer = true;
            } else {
                FlowVar.usaDrawer = false;
            }
        }
    }


    /**
     * Se l'applicazione è multiCompany e multiUtente, li visualizzo <br>
     * Altrimenti il nome del programma <br>
     */
    protected TopbarComponent createTopBar() {
        TopbarComponent topbar;
        String style;

        if (text.isValid(getUserName())) {
            Company company = login.getCompany();
            topbar = (TopbarComponent) new WamTopbar(login, FlowVar.pathLogo, company.getCode().toUpperCase(), "", getUserName());
        } else {
            topbar = (TopbarComponent) new WamTopbar(FlowVar.pathLogo, getDescrizione());
        }

        style = "display:inline-flex; width:100%; flex-direction:row; padding-left:0em; padding-top:0.5em; padding-bottom:0.5em; padding-right:1em; align-items:center";
        topbar.getElement().setAttribute("style", style);
        topbar.setProfileListener(() -> profilePressed());

        topbar.setLogoutListener(() -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
        });

        if (login != null && login.isDeveloper()) {
            addDeveloper(topbar);
        }

        return topbar;
    }


    protected void addDeveloper(TopbarComponent topbar) {
        Tab tab = new Tab();
        tab.add(VaadinIcon.MAGIC.create(), new Label("Developer"));
        topbar.projectSubMenu.addItem(tab, menuItemClickEvent -> {
            apreDialogo();
        });
    }


    /**
     * Regola i tabs
     * Può essere sovrascritto
     */
    @Override
    protected Tabs getTabMenu() {
        Tabs tabs = new Tabs();

        if (context != null && context.isAdmin()) {
            tabs.add(menuService.creaAlgosTabTabellone());
            tabs.add(menuService.creaAlgosTab(MiliteList.class));
            if (!((WamLogin) context.getLogin()).getCroce().code.equals(DEMO)) {
                tabs.add(menuService.creaAlgosTab(StatisticaList.class));
                tabs.add(menuService.creaAlgosTab(WamLogList.class));
            }
            tabs.add(menuService.creaAlgosTab(ServizioList.class));
            tabs.add(menuService.creaAlgosTab(FunzioneList.class));
            tabs.add(menuService.creaAlgosTab(CroceList.class));

            tabs.setOrientation(Tabs.Orientation.VERTICAL);
            return tabs;
        } else {
            return super.getTabMenu();
        }
    }


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


    /**
     * Occorre sincronizzare i dati <br>
     * La entityBean di milite contenuta nel wamLogin è quella caricata al login del programma <br>
     * Nel frattempo le property del milite potrebbero essere state cambiate nel form MiliteDialog <br>
     * Ricarico quindi dal database la versione aggiornata dei dati <br>
     */
    protected void profilePressed() {
        //--Crea il wam-login della sessione
        wamService.fixWamLogin();
        Milite milite = wamService.getWamLogin().getMilite();

        if (milite != null) {
            MiliteProfile profile = appContext.getBean(MiliteProfile.class, militeService, Milite.class);
            profile.openWam(milite, EAOperation.editProfile, this::saveMilite, null);
        }

    }


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void saveMilite(AEntity entityBean, EAOperation operation) {
        Milite milite = (Milite) entityBean;
        if (!milite.isFantasma()) {
            militeService.save(entityBean, operation);
        }

    }

}
