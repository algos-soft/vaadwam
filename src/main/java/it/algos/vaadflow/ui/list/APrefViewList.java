package it.algos.vaadflow.ui.list;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.service.IAService;

import static it.algos.vaadflow.application.FlowCost.USA_EDIT_BUTTON;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:19
 * <p>
 * Classe astratta per visualizzare la Grid <br>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewList (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewList con la business logic principale <br>
 * - 3 APrefViewList per regolare le preferenze ed i flags <br>
 * - 4 ALayoutViewList per regolare il layout <br>
 * - 5 AGridViewList per gestire la Grid <br>
 * - 6 APaginatedGridViewList (opzionale) per gestire una Grid specializzata (add-on) che usa le Pagine <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 * <p>
 * Sottoclasse di servizio per regolare le property di AViewList in una classe 'dedicata'. <br>
 * Alleggerisce la 'lettura' della classe principale. <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi. <br>
 * Qui vengono regolate le property 'standard'. <br>
 * Nelle sottoclassi concrete le property possono essere sovrascritte. <br>
 */
public abstract class APrefViewList extends AViewList {


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
    public APrefViewList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    protected void fixPreferenze() {

        /**
         * Flag di preferenza per usare la ricerca e selezione nella barra dei menu. <br>
         * Se è true, un altro flag seleziona il textField o il textDialog <br>
         * Se è true, il bottone usaAllButton è sempre presente <br>
         * Normalmente true. <br>
         */
        usaSearch = false;


        /**
         * Flag di preferenza per selezionare la ricerca:
         * true per aprire un dialogo di ricerca e selezione su diverse properties <br>
         * false per presentare un textEdit predisposto per la ricerca su un unica property <br>
         * Normalmente true.
         */
        usaSearchDialog = true;

        /**
         * Flag di preferenza per usare il popup di selezione, filtro e ordinamento situato nella searchBar.
         * Normalmente false <br>
         */
        usaPopupFiltro = false;


        //--Flag di preferenza per usare il bottone new situato nella barra topLayout. Normalmente true.
        usaBottoneNew = true;

        //--Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false.
        usaTopAlert = false;

        //--Flag di preferenza per la Label nell'header della Grid grid. Normalmente true.
        usaHaederGrid = true;

        //--Flag di preferenza per modificare la entity. Normalmente true.
        isEntityModificabile = true;

        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        //--Di norma fissato nelle preferenze per avere omogeneità di funzionamento del programma
        //--Può comunque essere modificato nella sottoclasse specifica
        usaBottoneEdit = pref.isBool(USA_EDIT_BUTTON);

        //--Flag di preferenza per usare il placeholder di botoni ggiuntivi sotto la Grid. Normalmente false.
        usaBottomLayout = false;

        //--Flag di preferenza per cancellare tutti gli elementi. Normalmente false.
        usaBottoneDeleteAll = false;

        //--Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false.
        usaBottoneReset = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityDeveloper = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityAdmin = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityEmbedded = false;

        //--Flag di preferenza se si caricano dati demo alla creazione. Resettabili. Normalmente false.
        isEntityUsaDatiDemo = false;

        //--Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true.
        usaRefresh = true;

        //--Flag di preferenza per la soglia di elementi che fanno scattare la pagination della Grid.
        //--Normalmente limit = pref.getInt(FlowCost.MAX_RIGHE_GRID) .
        //--Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 15
        //--Se non usa il bottone Edit: limit = pref.getInt(FlowCost.maxRigheGridClick) .
        //--Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 20
        if (pref.isBool(USA_EDIT_BUTTON)) {
            limit = pref.getInt(EAPreferenza.maxRigheGrid);
        } else {
            limit = pref.getInt(EAPreferenza.maxRigheGridClick);
        }// end of if/else cycle

        //--Flag di preferenza per usare una route view come detail della singola istanza. Normalmente true.
        //--In alternativa si può usare un Dialog.
        usaRouteFormView = false;

        //--Flag di preferenza per limitare le righe della Grid e mostrarle a gruppi (pagine). Normalmente true.
        usaPagination = true;

        //--controllo della paginazione
        if (service != null) {
            isPaginata = usaPagination && service.count() > limit;
        } else {
            isPaginata = false;
        }// end of if/else cycle

        //--Flag per la larghezza della Grid. Default a 80.
        //--Espressa come numero per comodità; poi viene convertita in "em".
        gridWith = 90;

        //--property per la ricerca con searchField.
        //--Viene letta da una @Annotation.
        //--Può essere sovrascritta in fixPreferenze() della sottoclasse specifica
        searchProperty = annotation.getSearchPropertyName(this.getClass());
    }// end of method

}// end of class
