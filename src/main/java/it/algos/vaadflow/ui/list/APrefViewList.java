package it.algos.vaadflow.ui.list;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.enumeration.EASearch;
import it.algos.vaadflow.service.IAService;

import static it.algos.vaadflow.application.FlowCost.USA_EDIT_BUTTON;
import static it.algos.vaadflow.application.FlowVar.usaCompany;
import static it.algos.vaadflow.service.AService.FIELD_NAME_CODE;
import static it.algos.vaadflow.service.AService.FIELD_NAME_DESCRIZIONE;

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
 * 1) Viene invocato il metodo fixPreferenze() della sottoclasse
 * 2) Come prima cosa invoca il metodo della superclasse (questa)
 * 3) Torna nella sottoclasse per le regolazioni/modifiche delle preferenze
 * 4) Torna nel metodo AViewList.initView()
 * 5) Esegue eventuali regolazioni sulle preferenze nel metodo postPreferenze di questa classe
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
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        /**
         * Flag di preferenza per usare il popup di selezione, filtro e ordinamento situato nella searchBar.
         * Normalmente false <br>
         */
        usaPopupFiltro = false;

        //--Flag di preferenza per usare il bottone new situato nella barra topLayout. Normalmente true.
        usaButtonNew = true;

        //--Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false.
        usaTopAlert = false;

        //--Flag di preferenza per usare la seconda riga di menu sopra la Grid. Normalmente false. <br>
        usaSecondTopPlaceholder = false;

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
        usaButtonDelete = false;

        //--Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false.
        usaButtonReset = false;

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
        //--Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 20
        //--Se non usa il bottone Edit: limit = pref.getInt(FlowCost.maxRigheGridClick) .
        //--Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 25
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


        //--Flag per la larghezza della Grid. Default a 80.
        //--Espressa come numero per comodità; poi viene convertita in "em".
        gridWith = 90;


        /**
         * Flag di preferenza per selezionare la ricerca testuale:
         * 1) nessuna
         * 2) campo editText di selezione per una property specificata in searchProperty
         * 3) dialogo di selezione
         */
        searchType = EASearch.editField;


        //--property per la ricerca con searchField.
        //--Ha senso solo se searchType=EASearch.editField
        //--Viene letta da una @Annotation. Di default 'code' o 'descrizione'
        //--Può essere sovrascritta in fixPreferenze() della sottoclasse specifica
        searchProperty = this.getSearchPropertyName();


        //--il flag viene provvisoriamente impostato con la preferenza generale del programma
        //--può essere sovrascritto
        //--nel metodo postPreferenze() vengono comunque controllati i 'permessi'
        usaFiltroCompany = usaCompany && annotation.getCompanyRequired(entityClazz) != EACompanyRequired.nonUsata;

        //--flag di preferenza per mostrare il popup di filtro. Normalmente false. <br>
        mostraFiltroCompany = false;

    }// end of method


    /**
     * Eventuali regolazioni sulle preferenze DOPO avere invocato il metodo fixPreferenze() della sotoclasse <br>
     * <p>
     * Chiamato da AViewList.initView() DOPO fixPreferenze() e sviluppato nella sottoclasse APrefViewList <br>
     * Non può essere sovrascritto <br>
     */
    @Override
    protected void postPreferenze() {
        //--controllo della paginazione
        if (service != null) {
            isPaginata = usaPagination && service.count() > limit;
        } else {
            isPaginata = false;
        }// end of if/else cycle

        //--controlla alcune condizioni indispensabili
        if (usaFiltroCompany) {
            if (usaCompany) {
            } else {
                usaFiltroCompany = false;
            }// end of if/else cycle
        }// end of if cycle
    }// end of method


    /**
     * Restituisce il nome della property per le ricerche con searchField <br>
     * Se non la trova, prova a vedere se esiste la property 'code' <br>
     * Se non la trova, prova a vedere se esiste la property 'descrizione' <br>
     *
     * @return the name of the property
     */
    private String getSearchPropertyName() {
        String searchProperty = annotation.getSearchPropertyName(this.getClass());

        if (text.isEmpty(searchProperty)) {
            if (reflection.isEsiste(entityClazz, FIELD_NAME_CODE)) {
                searchProperty = FIELD_NAME_CODE;
            } else {
                if (reflection.isEsiste(entityClazz, FIELD_NAME_DESCRIZIONE)) {
                    searchProperty = FIELD_NAME_DESCRIZIONE;
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        return searchProperty;
    }// end of method

}// end of class
