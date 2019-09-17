package it.algos.vaadflow.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:32
 * <p>
 * Superclasse astratta delle classi che gestiscono la business logic del package <br>
 * Implementa l'interfaccia IAPresenter <br>
 * <p>
 * <p>
 * La sottoclasse concreta viene costruita partendo dalla catena @Autowired di SpringBoot <br>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) vengono dichiarate prima del costruttore <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@SpringComponent
@UIScope
@Slf4j
public abstract class APresenter implements IAPresenter {


    /**
     * Istanza di classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), iniettata da Spring <br>
     * Service usato come libreria <br>
     */
    @Autowired
    public AArrayService array;


    /**
     * Istanza di classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), iniettata da Spring <br>
     * Service usato come libreria <br>
     */
    @Autowired
    public AReflectionService reflection;


    /**
     * Istanza di classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), iniettata da Spring <br>
     * Service usato come libreria <br>
     */
    @Autowired
    public AAnnotationService annotation;


    /**
     * Istanza di classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), iniettata da Spring <br>
     * Service usato come libreria <br>
     */
    @Autowired
    public ATextService text;


//    @Autowired
//    AUIParams params;


//    @Autowired
//    private LogService logger;


    /**
     * Il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
     */
    protected Class<? extends AEntity> entityClazz;


    /**
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     * La repository è gestita direttamente dal service
     */
    protected IAService service;


    /**
     */
    protected IAView view;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param entityClazz iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     * @param service     iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public APresenter(Class<? extends AEntity> entityClazz, IAService service) {
        ((AService) service).entityClass = entityClazz;
        this.entityClazz = entityClazz;
        this.service = service;
    }// end of Spring constructor


//    /**
//     * Metodo invocato da un evento, in alternativa al comando del menu
//     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
//     */
//    public void fireList() {
//        fireList(this);
//    }// end of method


//    /**
//     * Metodo invocato da un evento
//     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
//     */
//    public void fireList(IAPresenter source) {
//        list.getList().source = source != null ? source : this;
//
//        Class clazz = list.getViewComponent().getClass();
//        params.getNavigator().navigateTo(annotation.getViewName(clazz));
//    }// end of method


    /**
     * Gestione di una Lista visualizzata con una Grid
     * Metodo invocato da:
     * 1) una view quando diventa attiva
     * 2) un Evento (azione) che necessita di aggiornare e ripresentare la Lista;
     * tipo dopo un delete, dopo un nuovo record, dopo la edit di un record
     * <p>
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Recupera dal service le colonne da mostrare nella grid
     * Recupera dal service gli items (records) della collection, da mostrare nella grid
     * Recupera dal service i bottoni comando da mostrare nella toolbar del footer (sotto la Grid)
     * Passa il controllo alla view con i dati necessari
     */
    public void setList() {
        List items = null;
        List<Field> columns = null;

//        columns = service.getListFields();
//        if (array.isEmpty(columns)) {
//            log.warn("Algos - Columns. La grid: " + entityClass + " non ha colonne visibili");
//        }// end of if cycle
//
//        items = service.findAll();
//        if (array.isEmpty(items)) {
//            log.info("Algos - Items. La grid: " + entityClass + " non ha nessuna scheda");
//        }// end of if cycle
//
//        typeButtons = service.getListTypeButtons();
//
//        list.start(entityClass, columns, items, typeButtons);
    }// end of method


//    /**
//     * Modificata la selezione della Grid
//     * Controlla nella Grid quanti sono i records selezionati
//     * Abilita e disabilita i bottoni Modifica ed Elimina della List
//     * Se abilitato, inietta nel bottone Edit l'entityBean selezionato
//     */
//    @Override
//    public void selectionChanged() {
//        boolean unaSolaRigaSelezionata = false;
//        int numRigheSelezionate = 0;
//        AButton buttonEdit = null;
//        AButton buttonDelete = null;
//        AEntity entityBean = null;
//
//        if (list == null) {
//            log.warn("List null in APresenter.selectionChanged()");
//            return;
//        }// end of if cycle
//        log.warn("List null in APresenter.selectionChanged()");
//
//        //--recupera le informazioni dalla Grid
//        unaSolaRigaSelezionata = list.isUnaSolaRigaSelezionata();
//        entityBean = list.getGrid().getEntityBean();
//
//        //--il bottone Edit viene abilitato se c'è UNA SOLA riga selezionata
//        if (list.getButton(EATypeButton.edit) != null) {
//            buttonEdit = list.getButton(EATypeButton.edit);
//            buttonEdit.setEnabled(unaSolaRigaSelezionata);
//            buttonEdit.setEntityBean(unaSolaRigaSelezionata ? entityBean : null);
//        }// end of if cycle
//
//        //--il bottone Delete viene abilitato in funzione della modalità di selezione adottata
//        if (list.getButton(EATypeButton.delete) != null) {
//            buttonDelete = list.getButton(EATypeButton.delete);
//            buttonDelete.setEnabled(unaSolaRigaSelezionata);
//            buttonDelete.setEntityBean(unaSolaRigaSelezionata ? entityBean : null);
//        }// end of if cycle
//
//        //@todo RIMETTERE
////        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
////            numRigheSelezionate = view.numRigheSelezionate();
////            if (numRigheSelezionate >= 1) {
////                view.enableButtonList(AButtonType.delete, true);
////            } else {
////                view.enableButtonList(AButtonType.delete, false);
////            }// end of if/else cycle
////        } else {
////            view.enableButtonList(AButtonType.delete, unaSolaRigaSelezionata);
////        }// end of if/else cycle
//
//    }// end of method


    /**
     * Creazione o modifica di un singolo record (entityBean)
     * If entityBean=null, create a new item and edit it in a form
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Passa il controllo alla view con i dati necessari
     *
     * @param entityBean istanza da elaborare
     */
    public void editBean(AEntity entityBean) {
        int numRigheSelezionate = 0;
    }// end of method


//    /**
//     * Revert (ripristina) button pressed in form
//     * Rimane nella view (Form) SENZA registrare e ripristinando i valori iniziali del record (entityBean)
//     */
//    public void revert() {
//        form.revert();
//        this.abilitaBottoniForm(false);
//    }// end of method


//    /**
//     * Cancellazione di un singolo record (entityBean)
//     * If entityBean=null, recupera dal Form il record selezionato (se riesce)
//     * Usa il service per cancellare il record selezionato
//     * Rimane nella view (List) e la ricostruisce, ricaricando i dati aggiornati dal service
//     *
//     * @param entityBean istanza da cancellare
//     */
//    public void delete(AEntity entityBean) {
//        service.delete(entityBean);
//        setList();
//    }// end of method


//    /**
//     * Modificato il contenuto di un Field
//     * Abilita e disabilita i bottoni Revert e Registra/Accetta del Form
//     */
//    @Override
//    public void valueChanged() {
//        this.abilitaBottoniForm(true);
//    }// end of method


//    /**
//     * Evento 'save' (registra) button pressed in form
//     * Esegue il 'commit' nel Form, trasferendo i valori dai campi alla entityBean
//     * Esegue, nel Form, eventuale validazione e trasformazione dei dati
//     * Registra le modifiche nel DB, tramite il service
//     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
//     */
//    @Override
//    public boolean registra() {
//        return this.registra(this);
//    }// end of method


//    /**
//     * Evento 'save' (registra) button pressed in form
//     * Esegue il 'commit' nel Form, trasferendo i valori dai campi alla entityBean
//     * Esegue, nel Form, eventuale validazione e trasformazione dei dati
//     * Registra le modifiche nel DB, tramite il service
//     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
//     */
//    public boolean registra(IAPresenter target) {
//        boolean status = false;
//        AEntity oldBean = getBean();
//        AEntity modifiedBean = form.commit();
//
//        try { // prova ad eseguire il codice
//            status = service.save(oldBean, modifiedBean) != null;
////            if (service.save(oldBean, modifiedBean) != null) {
////                fireList(target);
////            }// end of if cycle
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//            Notification.show("Nuova scheda", NullCompanyException.MESSAGE, Notification.Type.ERROR_MESSAGE);
//        }// fine del blocco try-catch
//
//        return status;
//    }// end of method


//    public AEntity getBean() {
//        AEntity oldBean = null;
//        String oldId = form.getBean().getId();
//        if (text.isValid(oldId)) {
//            oldBean = (AEntity) service.find(oldId);
//        }// end of if cycle
//        return oldBean;
//    }// end of method


//    /**
//     * Regola lo stato dei bottoni del Form
//     *
//     * @param enabled abilitati o disabilitati
//     */
//    protected void abilitaBottoniForm(boolean enabled) {
//        if (form.getButton(EATypeButton.revert) != null) {
//            form.getButton(EATypeButton.revert).setEnabled(enabled);
//        }// end of if cycle
//
//        if (form.getButton(EATypeButton.registra) != null) {
//            form.getButton(EATypeButton.registra).setEnabled(enabled);
//        }// end of if cycle
//
//        if (form.getButton(EATypeButton.accetta) != null) {
//            form.getButton(EATypeButton.accetta).setEnabled(enabled);
//        }// end of if cycle
//
//        if (form.getButton(EATypeButton.conferma) != null) {
//            form.getButton(EATypeButton.conferma).setEnabled(enabled);
//        }// end of if cycle
//    }// end of method


    public Class<? extends AEntity> getEntityClazz() {
        return entityClazz;
    }// end of method

    public IAService getService() {
        return service;
    }// end of method

    @Override
    public IAView getView() {
        return view;
    }// end of method

    @Override
    public void setView(IAView view) {
        this.view = view;
    }// end of method

}// end of class
