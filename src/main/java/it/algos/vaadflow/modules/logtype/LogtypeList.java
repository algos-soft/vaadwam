package it.algos.vaadflow.modules.logtype;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadflow.ui.MainLayout14;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadflow.application.FlowCost.TAG_TYP;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 21-set-2019 7.43.41 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * <p>
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
 * Annotated with @AIView (facoltativo Algos) per regolare alcune property associate a questa classe <br>
 */
@UIScope
@Route(value = TAG_TYP, layout = MainLayout14.class)
@Qualifier(TAG_TYP)
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = true, menuName = "logTypes", menuIcon = VaadinIcon.ASTERISK, searchProperty = "code")
public class LogtypeList extends AGridViewList {



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
    public LogtypeList(@Qualifier(TAG_TYP) IAService service) {
        super(service, Logtype.class);
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.isEntityDeveloper = true;
        super.usaBottoneDeleteAll = true;
        super.usaBottoneReset = true;
        super.usaSearchDialog = false;
        super.isEntityUsaDatiDemo = true;
    }// end of method


    /**
     * Placeholder (eventuale) per informazioni aggiuntive alla grid ed alla lista di elementi <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        alertPlacehorder.add(new Label("Serve per aggiungere altri eventuali 'type' specifici per i logs dell'applicazione"));
    }// end of method


    /**
     * Apertura del dialogo per una entity esistente oppure nuova <br>
     * Il dialogo è PROTOTYPE e viene creato esclusivamente da appContext.getBean(... <br>
     * Sovrascritto <br>
     */
    protected void openDialog(AEntity entityBean) {
        LogtypeDialog dialog = appContext.getBean(LogtypeDialog.class, service, entityClazz);
        dialog.open(entityBean, EAOperation.edit, this::save, this::delete);
    }// end of method

}// end of class