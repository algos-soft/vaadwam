package it.algos.vaadwam.modules.log;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EASearch;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadflow.wrapper.AFiltro;
import it.algos.vaadwam.enumeration.EAWamLogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadwam.application.WamCost.TAG_WAM_LOG;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 27-mag-2020
 * Time: 09:36
 */
@UIScope
@Route(value = TAG_WAM_LOG, layout = MainLayout14.class)
@Qualifier(TAG_WAM_LOG)
@Slf4j
@Secured("admin")
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = true, menuName = "wanlogs", menuIcon = VaadinIcon.ARCHIVE, searchProperty = "descrizione", roleTypeVisibility = EARoleType.admin)
public class WamLogList extends AGridViewList {

    public static final String IRON_ICON = "history";


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
    public WamLogList(@Qualifier(TAG_WAM_LOG) IAService service) {
        super(service, WamLog.class);
    }// end of Vaadin/@Route constructor


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<WamLog>();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (!FlowVar.usaSecurity || login.isDeveloper()) {
            super.usaButtonDelete = true;
        }// end of if cycle

        super.searchType = EASearch.nonUsata;
        super.usaPopupFiltro = true;
        super.usaBottoneEdit = true;
        super.usaButtonNew = false;
        super.isEntityAdmin = true;
        super.usaPagination = true;
    }// end of method


    /**
     * Placeholder (eventuale) per informazioni aggiuntive alla grid ed alla lista di elementi <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        alertPlacehorder.add(text.getLabelAdmin("Lista visibile solo perché sei collegato come admin. Gli utenti normali non la vedono."));
        alertPlacehorder.add(text.getLabelAdmin("Lista creata dal programma."));
    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaPopupFiltro() {
        super.creaPopupFiltro();

        filtroComboBox.setWidth("16em");
        filtroComboBox.setPlaceholder("Types ...");
        filtroComboBox.setItems(EAWamLogType.values());
        filtroComboBox.addValueChangeListener(e -> {
            updateFiltri();
            updateGrid();
        });
    }// end of method


    /**
     * Aggiorna i filtri specifici della Grid. Modificati per: popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Può essere sovrascritto, per costruire i filtri specifici dei combobox, popup, ecc. <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void updateFiltriSpecifici() {
        super.updateFiltriSpecifici();

        String fieldName = "type";
        String fieldSort = "evento";
        EAWamLogType type = (EAWamLogType) filtroComboBox.getValue();

        if (type != null) {
            CriteriaDefinition criteria = Criteria.where(fieldName).is(type);
            Sort sort = new Sort(Sort.Direction.DESC, fieldSort);
            AFiltro filtro = new AFiltro(criteria, sort);

            filtri.add(filtro);
        }// end of if cycle
    }// end of method


    /**
     * Apertura del dialogo per una entity esistente oppure nuova <br>
     * Sovrascritto <br>
     */
    protected void openDialog(AEntity entityBean) {
        EAOperation eaOperation;
        if (login != null && login.isDeveloper()) {
            eaOperation = EAOperation.editNoDelete;
        } else {
            eaOperation = EAOperation.showOnly;
        }

        WamLogDialog dialog = appContext.getBean(WamLogDialog.class, service, entityClazz);
        dialog.open(entityBean, eaOperation, this::save, this::delete);
    }// end of method

}// end of class