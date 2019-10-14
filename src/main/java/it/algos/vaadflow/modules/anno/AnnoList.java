package it.algos.vaadflow.modules.anno;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.modules.secolo.Secolo;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.list.ACronoViewList;
import it.algos.vaadflow.ui.MainLayout14;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 18.19.24 <br>
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
 */
@UIScope
@Route(value = TAG_ANN, layout = MainLayout14.class)
@Qualifier(TAG_ANN)
@Slf4j
@Secured("developer")
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = true, menuName = "anni", searchProperty = "secolo", roleTypeVisibility = EARoleType.developer)
public class AnnoList extends ACronoViewList {


    public static final String IRON_ICON = "today";


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected SecoloService secoloService;


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
    public AnnoList(@Qualifier(TAG_ANN) IAService service) {
        super(service, Anno.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaBottoneEdit = false;
        super.grid = new PaginatedGrid<Anno>();
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
        alertPlacehorder.add(new Label("Sono considerati gli anni da 1.000 a.c. a 2030 d.c. Non si possono cancellare ne aggiungere elementi."));
    }// end of method

    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaPopupFiltro() {
        super.creaPopupFiltro();

        filtroComboBox.setWidth("12em");
        filtroComboBox.setItems(secoloService.findAll());
        filtroComboBox.addValueChangeListener(e -> {
            updateItems();
            updateView();
        });
    }// end of method


    public void updateItems() {
        Secolo secolo = (Secolo) filtroComboBox.getValue();
        items = ((AnnoService) service).findAllBySecolo(secolo);
    }// end of method


    /**
     * Apertura del dialogo per una entity esistente oppure nuova <br>
     * Sovrascritto <br>
     */
    protected void openDialog(AEntity entityBean) {
        AnnoDialog dialog = appContext.getBean(AnnoDialog.class, service, entityClazz);
        dialog.open(entityBean, EAOperation.showOnly, this::save, this::delete);
    }// end of method

}// end of class