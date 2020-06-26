package it.algos.vaadwam.modules.statistica;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AColumnService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_STA;

/**
 * Project vaadwam <br>
 * Created by Algos
 * User: Gac
 * Fix date: 20-ott-2019 7.35.49 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * Necessario per la tipizzazione del binder <br>
 * Costruita (nella List) con appContext.getBean(StatisticaDialog.class, service, entityClazz);
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_STA)
@Slf4j
@AIScript(sovrascrivibile = true)
public class StatisticaDialog extends AViewDialog<Statistica> {

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AColumnService columnService = AColumnService.getInstance();

    private Grid<AEntity> grid;

    private Collection items;


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public StatisticaDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(StatisticaDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public StatisticaDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Regola il titolo del dialogo <br>
     * Recupera recordName dalle @Annotation della classe Entity. Non dovrebbe mai essere vuoto. <br>
     * Costruisce il titolo con la descrizione dell'operazione (New, Edit,...) ed il recordName <br>
     * Sostituisce interamente il titlePlaceholder <br>
     */
    protected void fixTitleLayout() {
        String title = "Turni del milite " + currentItem.milite.getCognome() + " " + currentItem.milite.getNome();

        titlePlaceholder.removeAll();
        titlePlaceholder.add(new H2(title));
        titlePlaceholder.add("Periodo 1° gen 2020 - 25 giu 2020");
    }// end of method


    /**
     * Crea i fields
     * <p>
     * Crea un nuovo binder (vuoto) per questo Dialog e questa Entity
     * Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
     * Costruisce una lista di nomi delle properties. Ordinata. Sovrascrivibile.
     * <p>
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge i fields al binder
     * Aggiunge i fields alla mappa fieldMap
     * <p>
     * Aggiunge eventuali fields specifici (costruiti non come standard type) al binder ed alla fieldMap
     * Aggiunge i fields della fieldMap al layout grafico
     * Aggiunge eventuali fields specifici direttamente al layout grafico (senza binder e senza fieldMap)
     * Legge la entityBean ed inserisce nella UI i valori di eventuali fields NON associati al binder
     */
    protected void creaFields(AEntity entityBean) {
        List<String> properties = annotation.getGridPropertiesName(StaTurnoIsc.class);
        grid = new Grid(StaTurnoIsc.class, false);

        //--Colonne aggiunte in automatico
        if (array.isValid(properties)) {
            for (String propertyName : properties) {
                columnService.create(grid, StaTurnoIsc.class, propertyName);
            }// end of for cycle
        }

        grid.setItems(getItems());

        grid.setWidth("100em");
        formSubLayout.setWidth("100em");
        formSubLayout.add(grid);
    }// end of method


    protected List<String> getGridPropertyNamesList() {
        List<String> properties = annotation.getGridPropertiesName(StaTurnoIsc.class);

        properties.add("funzione");
        properties.add("inizio");
        properties.add("fine");
        properties.add("durataEffettiva");
        properties.add("esisteProblema");

        return properties;
    }// end of method


    protected Collection getItems() {
        List<StaTurnoIsc> iscrizioni = new ArrayList<>();

        if (currentItem != null) {
            iscrizioni = ((Statistica) currentItem).iscrizioni;
        }

        return iscrizioni;
    }// end of method


    /**
     * Regola in lettura l'eeventuale field company (un combo)
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readCompanyField() {
        if (companyField != null) {
            companyField.setValue(((Statistica) getCurrentItem()).getCroce());
        }// end of if cycle
    }// end of method

}// end of class