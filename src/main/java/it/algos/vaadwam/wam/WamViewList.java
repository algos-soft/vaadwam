package it.algos.vaadwam.wam;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.schedule.ATask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_CRO;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: lun, 30-lug-2018
 * Time: 15:48
 */
@Slf4j
public abstract class WamViewList extends AGridViewList {

    protected Button genericFieldValue;

    protected Button deleteButton;

    protected Button importButton;

    protected String lastImport;

    protected String durataLastImport;

    protected EATempo eaTempoTypeImport;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected AMailService mailService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    protected WamService wamService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected CroceService croceService;

    /**
     * Wam-Login della sessione con i dati del Milite loggato <br>
     */
    protected WamLogin wamLogin;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param entityClazz modello-dati specifico di questo modulo
     */
    public WamViewList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze standard
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //--Crea il wam-login della sessione
        wamLogin = (WamLogin) login;

        super.usaBottoneEdit = true;
        super.usaButtonReset = false;

        if (login.isDeveloper()) {
            super.usaButtonDelete = true;
        }// end of if cycle

        if (login.isDeveloper() || login.isAdmin()) {
            super.usaButtonNew = true;
        } else {
            super.usaButtonNew = false;
        }// end of if/else cycle

        if (wamLogin.isAdminOrDev()) {
            super.isEntityModificabile = true;
        } else {
            super.isEntityModificabile = false;
        }// end of if/else cycle

        super.usaPagination = false;

        this.lastImport = VUOTA;
        this.durataLastImport = VUOTA;
        this.eaTempoTypeImport = EATempo.nessuno;
    }// end of method


    /**
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, presente di default <br>
     * - con o senza un bottone per cancellare tutta la collezione
     * - con o senza un bottone di reset per ripristinare (se previsto in automatico) la collezione
     * - con o senza gruppo di ricerca:
     * -    campo EditSearch predisposto su un unica property, oppure (in alternativa)
     * -    bottone per aprire un DialogSearch con diverse property selezionabili
     * -    bottone per annullare la ricerca e riselezionare tutta la collezione
     * - con eventuale Popup di selezione, filtro e ordinamento
     * - con o senza bottone New, con testo regolato da preferenza o da parametro <br>
     * - con eventuali altri bottoni specifici <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaTopLayout() {
        super.creaTopLayout();

        if (wamLogin.isDeveloper()) {
            importButton = new Button("Import", new Icon(VaadinIcon.ARROW_DOWN));
            importButton.getElement().setAttribute("theme", "error");
            importButton.addClassName("view-toolbar__button");
            importButton.addClickListener(e -> importa());
            topPlaceholder.add(importButton);
        }// end of if cycle
    }// end of method


    protected void delete() {
        ((WamService) service).deleteAllCroce();
        UI.getCurrent().getPage().reload();
    }// end of method


    /**
     * Importa la collezione di questa classe, riferita solo alla croce indicata <br>
     * Aggiorna comunque (anche se ha importato solo una croce) la data di import sopra la Grid <br>
     * Ricarica la intera view, per aggiornare le Label sopra la Grid <br>
     * con il solo metodo update() non venivano aggiornate <br>
     */
    protected void importa() {
        long inizio = System.currentTimeMillis();

        ((WamService) service).importa();

        log.info("Import effettuato in " + date.deltaText(inizio));
        setLastImport(inizio);
        UI.getCurrent().getPage().reload();
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo import effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo import effettuato <br>
     */
    protected void setLastImport(long inizio) {
        pref.saveValue(lastImport, LocalDateTime.now());
        pref.saveValue(durataLastImport, eaTempoTypeImport.get(inizio));
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected Label getInfoImport(ATask task, String flagDaemon, String flagLastDownload) {
        Label label = null;
        String testo = "";
        String tag = "Import automatico: ";
        String nota = task.getSchedule().getNota();

        if (login.isDeveloper()) {
            LocalDateTime lastDownload = pref.getDateTime(flagLastDownload);
            testo = tag;

            if (pref.isBool(flagDaemon)) {
                testo += nota;
            } else {
                testo += "disattivato.";
            }// end of if/else cycle

            if (lastDownload != null) {
                label = getLabelDev(testo + " Ultimo import il " + date.getTime(lastDownload));
            } else {
                if (pref.isBool(flagDaemon)) {
                    label = getLabelDev(tag + nota + " Non ancora effettuato.");
                } else {
                    label = getLabelDev(testo);
                }// end of if/else cycle
            }// end of if/else cycle

        }// end of if cycle

        return label;
    }// end of method


//    protected Button createEditButton(AEntity entityBean) {
//        if (login.isDeveloper() || login.isAdmin()) {
//            return super.createEditButton(entityBean);
//        } else {
//            Button edit = new Button("", event -> dialog.open(entityBean, EAOperation.showOnly, context));
//            edit.setIcon(new Icon("lumo", "edit"));
//            edit.addClassName("review__edit");
//            edit.getElement().setAttribute("theme", "tertiary");
//            return edit;
//        }// end of if/else cycle
//    }// end of method


//    /**
//     * Eventuali aggiustamenti finali al layout
//     * Regolazioni finali sulla grid e sulle colonne
//     * Sovrascritto
//     */
//    @Override
//    protected void fixGridLayout() {
//        super.fixLayout();
//        int keyPos = 1;
//
//        if (login.isDeveloper()) {
//            List<Grid.Column<AEntity>> colonne = grid.getColumns();
//            Grid.Column<AEntity> colonna = colonne != null ? colonne.get(keyPos) : null;
//            if (colonna != null) {
//                colonna.setWidth("9em");
//            }// end of if cycle
//        }// end of if cycle
//    }// end of method


//    /**
//     * Crea un Popup di selezione della company <br>
//     * Creato solo se developer=true e usaCompany=true <br>
//     * Può essere sovrascritto, per caricare gli items da una sottoclasse di Company <br>
//     * Invocare PRIMA il metodo della superclasse <br>
//     */
//    protected void creaCompanyFiltro() {
//        super.creaCompanyFiltro();
//        filtroCompany.setItems(croceService.findAll());
//        filtroCompany.addValueChangeListener(e -> {
//            updateFiltri();
//            updateGrid();
//        });
//    }// end of method


    /**
     * Sincronizza la company in uso. <br>
     * Chiamato dal listener di 'filtroCompany' <br>
     * <p>
     * Può essere sovrascritto, per modificare la gestione delle company <br>
     */
    protected void actionSincroCompany() {
        Croce croceSelezionata = null;

        if (filtroCompany != null) {
            croceSelezionata = (Croce) filtroCompany.getValue();
        }// end of if cycle
        wamLogin.setCroce(croceSelezionata);

        updateFiltri();
        updateGrid();
    }// end of method

//    public void updateItems() {
//        Croce croce;
//        if (filtroCompany != null) {
//            croce = (Croce) filtroCompany.getValue();
//
//            if (croce != null) {
//                items = ((WamService) service).findAllByCroce(croce);
//            } else {
//                items = ((WamService) service).findAllCroci();
//            }// end of if/else cycle
//        } else {
//            items = ((WamService) service).findAll();
//        }// end of if/else cycle
//
//    }// end of method

}// end of class
