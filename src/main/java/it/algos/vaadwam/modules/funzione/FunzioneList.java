package it.algos.vaadwam.modules.funzione;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwam.schedule.ATask;
import it.algos.vaadwam.wam.WamViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.22.05 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), oppure <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe, perché SpringBoot le inietta DOPO init() <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@UIScope
@Route(value = TAG_FUN, layout = MainLayout14.class)
@Qualifier(TAG_FUN)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(menuName = "funzioni", roleTypeVisibility = EARoleType.user)
public class FunzioneList extends WamViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

//    /**
//     * Label del menu (facoltativa)
//     * Vaadin usa il 'name' della Annotation @Route per identificare (internamente) e recuperare la view
//     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
//     * Se manca il MENU_NAME, di default usa il 'name' della view
//     */
//    public static final String MENU_NAME = "funzioni";

    public static final String IRON_ICON = "dashboard";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     */
    @Autowired
    @Qualifier(TASK_FUN)
    private ATask task;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public FunzioneList(@Qualifier(TAG_FUN) IAService service) {
        super(service, Funzione.class);
    }// end of Vaadin/@Route constructor


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    @RolesAllowed("developer")
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();
        String messageUno = "Quando un milite viene abilitato per una funzione, gli vengono abilitate anche le funzioni dipendenti.";
        String messageDue = "Successivamente le funzioni dipendenti possono essere singolarmente disabilitate.";

        alertPlacehorder.add(new Label("Funzioni di servizio specifiche dell'associazione. Attribuibili singolarmente ad ogni milite."));
        if (isDeveloper) {
            alertPlacehorder.add(new Label("Come developer si possono importare le funzioni dal vecchio programma"));
            alertPlacehorder.add(getInfoImport(task, USA_DAEMON_FUNZIONI, LAST_IMPORT_FUNZIONI));
            alertPlacehorder.add(new Label(messageUno));
            alertPlacehorder.add(new Label(messageDue));
        } else {
            if (isAdmin) {
                alertPlacehorder.add(new Label("Come admin si possono aggiungere, modificare e cancellare le funzioni. Gli utenti normali possono solo vederle."));
                alertPlacehorder.add(new Label(messageUno));
                alertPlacehorder.add(new Label(messageDue));
            } else {
                alertPlacehorder.add(new Label("Solo in visione. Le modifiche vengono effettuate da un admin."));
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


//    /**
//     * Crea la colonna (di tipo Component) per visualizzare le funzioni dipendenti
//     */
//    protected void addSpecificColumnsAfter() {
//        Grid.Column colonna = grid.addComponentColumn(funzione -> {
//            String valueLabel = "";
//            String tag = " - ";
//            List<Funzione> dipendenti=((Funzione)funzione).dipendenti;
//
//            if (array.isValid(dipendenti)) {
//                for (Funzione funz : dipendenti) {
//                    if (funz != null) {
//                        valueLabel += funz.getCode();
//                        valueLabel +=   tag;
//                    }// end of if cycle
//                }// end of for cycle
//                valueLabel = text.levaCoda(valueLabel, tag);
//                return new Label(valueLabel);
//            } else {
//                return new Label("");
//            }// end of if/else cycle
//        });//end of lambda expressions
//
//        colonna.setId("funzioni");
//        colonna.setHeader("Funzioni dipendenti");
//        colonna.setWidth("20em");
//        colonna.setFlexGrow(0);
//
//    }// end of method


    /**
     * Sovrascritto <br>
     */
    protected void fixInfoImport() {
        pref.saveValue(LAST_IMPORT_FUNZIONI, LocalDateTime.now());
    }// end of method

    /**
     * Apertura del dialogo per una entity esistente oppure nuova <br>
     * Sovrascritto <br>
     */
    protected void openDialog(AEntity entityBean) {
        FunzioneDialog dialog = appContext.getBean(FunzioneDialog.class, service, entityClazz);
//        dialog.open(entityBean, EAOperation.showOnly, this::save, this::delete);
    }// end of method

}// end of class