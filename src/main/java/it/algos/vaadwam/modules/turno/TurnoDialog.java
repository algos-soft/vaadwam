package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.ACheckBox;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.iscrizione.IscrizioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.servizio.Servizio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_ISC;
import static it.algos.vaadwam.application.WamCost.TAG_TUR;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 30-set-2018 16.22.05 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_TUR)
@Slf4j
@AIScript(sovrascrivibile = true)
public class TurnoDialog extends AViewDialog<Turno> {

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected FunzioneService funzioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected IscrizioneService iscrizioneService;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     */
    @Autowired
    @Qualifier(TAG_ISC)
    private IADialog dialogoIscrizione;

    private Grid grid;

    private List<Iscrizione> iscrizioniDelTurno;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public TurnoDialog() {
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * L'istanza DEVE essere creata con appContext.getBean(TurnoDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public TurnoDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    protected void fixLayout() {
        if (currentItem != null) {
            this.getFormLayout().add(creaGrid());
        }// end of if cycle
    }// end of method


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public Grid creaGrid() {
        List<Funzione> items;
        String widthA = "4em";
        String widthB = "6em";
        String widthC = "12em";
        grid = new Grid(Funzione.class);
        Servizio servizio = currentItem.servizio;
        iscrizioniDelTurno = currentItem.iscrizioni;
        if (servizio != null) {
            items = servizio.funzioni;
            grid.setItems(items);
        }// end of if cycle

        for (Object column : grid.getColumns()) {
            grid.removeColumn((Grid.Column) column);
        }// end of for cycle

        //--aggiunge una colonna semplice
        Grid.Column colonnaOrdine = grid.addColumn("ordine");
        colonnaOrdine.setHeader("Funz");
        colonnaOrdine.setId("ordine");
        colonnaOrdine.setWidth(widthA);

        //--aggiunge una colonna semplice
        Grid.Column colonnaSiglaFunzione = grid.addColumn("sigla");
        colonnaSiglaFunzione.setHeader("Sigla");
        colonnaSiglaFunzione.setId("sigla");
        colonnaSiglaFunzione.setWidth(widthB);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaFunzioneObbligatoria = grid.addComponentColumn(funzione -> {
            ACheckBox box = new ACheckBox("");
            List<Funzione> funzioniDelServizio = servizio.funzioni;
            if (array.isValid(funzioniDelServizio)) {
                for (Funzione funzServizio : funzioniDelServizio) {
                    if (funzServizio.code.equals(((Funzione) funzione).code)) {
                        box.setValue(funzServizio.obbligatoria);
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
            return box;
        });//end of lambda expressions
        colonnaFunzioneObbligatoria.setHeader("Must");
        colonnaFunzioneObbligatoria.setId("must");
        colonnaFunzioneObbligatoria.setWidth(widthA);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaMilite = grid.addComponentColumn(funzione -> {
            Milite milite = iscrizioneService.getByTurnoAndFunzione(currentItem, funzione).getMilite();
            if (milite != null) {
                return new Label(milite.toString());
            } else {
                return new Label("Non ancora segnato");
            }// end of if/else cycle
        });//end of lambda expressions
        colonnaMilite.setHeader("Milite");
        colonnaMilite.setId("milite");
        colonnaMilite.setWidth(widthB);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaLastModifica = grid.addComponentColumn(funzione -> {
            LocalDateTime time = iscrizioneService.getByTurnoAndFunzione(currentItem, funzione).getLastModifica();
            if (time != null) {
                return new Label(time.toString());
            } else {
                return new Label("Non ancora segnato");
            }// end of if/else cycle
        });//end of lambda expressions
        colonnaLastModifica.setHeader("Time");
        colonnaLastModifica.setId("last");
        colonnaLastModifica.setWidth(widthB);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaDurata = grid.addComponentColumn(funzione -> {
            int durata = iscrizioneService.getByTurnoAndFunzione(currentItem, funzione).getDurataEffettiva();
            if (durata > 0) {
                return new Label(durata + "");
            } else {
                return new Label("0");
            }// end of if/else cycle
        });//end of lambda expressions
        colonnaDurata.setHeader("Ore");
        colonnaDurata.setId("durata");
        colonnaDurata.setWidth(widthA);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaEsisteProblema = grid.addComponentColumn(funzione -> {
            boolean status = iscrizioneService.getByTurnoAndFunzione(currentItem, funzione).isEsisteProblema();
            return new ACheckBox("", status);
        });//end of lambda expressions
        colonnaEsisteProblema.setHeader("Prob");
        colonnaEsisteProblema.setId("problema");
        colonnaEsisteProblema.setWidth(widthA);


        //--aggiunge una colonna calcolata
//        Grid.Column colonnaEdit = grid.addComponentColumn(funzione -> {
//            final Iscrizione iscrizioneDellaRiga = iscrizioneService.getByTurnoAndFunzione(currentItem, funzione);
//            Button edit = new Button("Edit", event -> dialogoIscrizione.open(iscrizioneDellaRiga, EAOperation.editDaLink, context));
//            edit.setIcon(new Icon("lumo", "edit"));
//            edit.addClassName("review__edit");
//            edit.getElement().setAttribute("theme", "tertiary");
//
//            return edit;
//        });//end of lambda expressions
//        colonnaEdit.setHeader("Iscr");
//        colonnaEdit.setId("iscrizione");
//        colonnaEdit.setWidth(widthA);

        //--header
        fixGridHeader();

        return grid;
    }// end of method


    /**
     * Eventuale header text
     */
    private void fixGridHeader() {
        String message = "Iscrizioni previste in questo turno";
        Component comp = new HorizontalLayout();

        ((HorizontalLayout) comp).add(new Label(message));
        HeaderRow topRow = grid.prependHeaderRow();
        Grid.Column[] matrix = array.getColumnArray(grid);
        HeaderRow.HeaderCell informationCell = topRow.join(matrix);
        informationCell.setComponent(comp);
    }// end of method




}// end of class