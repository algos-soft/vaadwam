package it.algos.vaadwam.modules.servizio;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.ui.fields.AField;
import it.algos.vaadflow.ui.fields.IAField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 05-nov-2017
 * Time: 15:43
 */
@Slf4j
@SpringComponent
@Scope("prototype")
public class ServizioFieldFunzioni  {


//    /**
//     * Libreria di servizio. Inietta da Spring come 'singleton'
//     */
//    @Autowired
//    protected AHtmlService html;
//
//
//    /**
//     * Libreria di servizio. Inietta da Spring come 'singleton'
//     */
//    @Autowired
//    protected AColumnService column;
//
//
//    @Autowired
//    private FunzioneService funzioneService;
//
//    private Grid grid;
//
//    private List<Funzione> funzioni;
//
//
//    /**
//     * Crea (o ricrea dopo una clonazione) il componente base
//     */
//    @Override
//    public void creaContent() {
//        int width = 170;
//        grid = new Grid(Funzione.class);
//        grid.setStyleName("");
//        grid.setRowHeight(48);
//
//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaCombo = grid.addComponentColumn(funzione -> {
//            String idKey = ((Funzione) funzione).getId();
//            ComboBox combo = new ComboBox();
//            combo.setWidth("7em");
//            List<String> items = funzioneService.findAllCode();
//            combo.setItems(items);
//            combo.setValue(((Funzione) funzione).getCode());
//
//            combo.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
//                @Override
//                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
//                    codeChanged(idKey, valueChangeEvent.getOldValue(), valueChangeEvent.getValue());
//                }// end of inner method
//            });// end of anonymous inner class
//
//            return combo;
//        });//end of lambda expressions
//        colonnaCombo.setCaption("Code");
//        colonnaCombo.setId("combo");
//        colonnaCombo.setWidth(width);
//
//
//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaSigla = grid.addComponentColumn(funzione -> {
//            String sigla = ((Funzione) funzione).getSigla();
//            Label label = new Label("", ContentMode.HTML);
//            String labelTxt = "";
//            if (((Funzione) funzione).isObbligatoria()) {
//                labelTxt = html.setRossoBold(sigla);
//            } else {
//                labelTxt = html.setBluBold(sigla);
//            }// end of if/else cycle
//            label.setValue(labelTxt);
//
//            return label;
//        });//end of lambda expressions
//        colonnaSigla.setCaption("Sigla");
//        colonnaSigla.setId("sigla2");
//        colonnaSigla.setWidth(120);
//
//
//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaDescrizione = grid.addComponentColumn(funzione -> {
//            String descrizione = ((Funzione) funzione).getDescrizione();
//            Label label = new Label("", ContentMode.HTML);
//            String labelTxt = "";
//            if (((Funzione) funzione).isObbligatoria()) {
//                labelTxt = html.setRossoBold(descrizione);
//            } else {
//                labelTxt = html.setBluBold(descrizione);
//            }// end of if/else cycle
//            label.setValue(labelTxt);
//
//            return label;
//        });//end of lambda expressions
//        colonnaDescrizione.setCaption("Descrizione");
//        colonnaDescrizione.setId("descrizione2");
//        colonnaDescrizione.setWidth(350);
//
//
//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaCheck = grid.addComponentColumn(funzione -> {
//            String idKey = ((Funzione) funzione).getId();
//            CheckBox checkBox = null;
//            if (text.isEmpty(((Funzione) funzione).getId())) {
//            } else {
//                boolean obbligatoria = ((Funzione) funzione).isObbligatoria();
//                checkBox = new CheckBox("", obbligatoria);
//            }// end of if/else cycle
//
//            if (checkBox != null) {
//                checkBox.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
//                    @Override
//                    public void valueChange(HasValue.ValueChangeEvent<Boolean> valueChangeEvent) {
//                        checkChanged(idKey, valueChangeEvent.getValue());
//                    }// end of inner method
//                });// end of anonymous inner class
//            }// end of if cycle
//
//            return checkBox;
//        });//end of lambda expressions
//        colonnaCheck.setCaption("Obb.");
//        colonnaCheck.setId("obb");
//        colonnaCheck.setWidth(column.WIDTH_CHECK_BOX);
//
//        ArrayList lista = new ArrayList();
//        lista.add("combo");
//        lista.add("sigla2");
//        lista.add("descrizione2");
//        lista.add("obb");
//        grid.setColumns((String[]) lista.toArray(new String[lista.size()]));
//        float lar = grid.getWidth();
//        grid.setWidth(lar + width + column.WIDTH_CHECK_BOX+565, Unit.PIXELS);
//
//
//        grid.setStyleGenerator(new StyleGenerator() {
//            @Override
//            public String apply(Object o) {
//                return "error_row";
//            }
//        });
//    }// end of method
//
//
//    @Override
//    public void setWidth(String width) {
//        width="39em";
//        if (grid != null) {
//            grid.setWidth(width);
//        }// end of if cycle
//    }// end of method
//
//
//    @Override
//    public Component initContent() {
//        Funzione lastFunz = null;
//
//        if (entityBean != null) {
//            funzioni = ((Servizio) entityBean).getFunzioni();
//            if (grid != null && funzioni != null) {
//                lastFunz = funzioni.get(funzioni.size() - 1);
//                if (text.isValid(lastFunz.getId())) {
//                    funzioni.add(funzioneService.newEntity());
//                }// end of if cycle
//                grid.setHeightByRows(funzioni.size());
//                grid.setItems(funzioni);
//            } else {
//                funzioni = new ArrayList<>();
//                funzioni.add(funzioneService.newEntity());
//                grid.setItems(funzioni);
//                grid.setHeightByRows(1);
//            }// end of if/else cycle
//        } else {
//            if (grid != null) {
//                grid.setItems("");
//                grid.setHeightByRows(1);
//            }// end of if cycle
//        }// end of if/else cycle
//
//        return grid;
//    }// end of method
//
//    /**
//     * Recupera dalla UI il valore (eventualmente) selezionato
//     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
//     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
//     */
//    @Override
//    public Object getValue() {
//        return funzioni;
//    }// end of method
//
//
//    /**
//     * Visualizza graficamente nella UI i componenti grafici (uno o più)
//     * Riceve il valore dal DB Mongo, già col casting al typo previsto
//     */
//    @Override
//    public void doSetValue(Object value) {
//    }// end of method
//
//    /**
//     * Cambiato il code di selezione della funzione
//     */
//    private void codeChanged(String idKey, String oldCode, String newCode) {
//
//        //--combo dell'ultima riga (vuota) della Grid
//        if (text.isEmpty(oldCode) && text.isValid(newCode)) {
//            aggiungeRiga(newCode);
//            return;
//        }// end of if cycle
//
//        //--modifica una riga
//        if (text.isValid(oldCode) && text.isValid(newCode)) {
//            modificaRiga(idKey, newCode);
//            return;
//        }// end of if cycle
//
//    }// end of method
//
//    /**
//     * Cambiato il check di obbligatorietà della funzione
//     */
//    private void checkChanged(String idKey, boolean newCode) {
//        Funzione funz= getFunz(idKey);
//        funz.setObbligatoria(newCode);
//        //@todo It's a bug. Grid doesn't update itself after changes were done in underlying container nor has any reasonable method to refresh. There are several hacks around this issue i.e.
//        grid.setItems(funzioni);
//
//        publish();
//    }// end of method
//
//
//    /**
//     * Cancella l'ultima riga
//     * Aggiunge la funzione corrispondente al code ricevuto
//     * Aggiunge un'ultima riga vuota
//     */
//    private void aggiungeRiga(String newCode) {
//        Funzione funz = funzioneService.findByKeyUnica(newCode);
//
//        if (funz != null) {
//            funzioni.remove(funzioni.size() - 1);
//            funzioni.add(funz);
//            funzioni.add(funzioneService.newEntity());
//            grid.setItems(funzioni);
//            grid.setHeightByRows(funzioni.size());
//
//            publish();
//        }// end of if cycle
//
//    }// end of method
//
//
//    /**
//     * Modifica la riga
//     */
//    private void modificaRiga(String idKey, String newCode) {
//        Funzione oldFunz = null;
//        Funzione newFunz = null;
//        int oldPos = 0;
//
//        oldFunz = getFunz(idKey);
//        newFunz = funzioneService.findByKeyUnica(newCode);
//
//        if (oldFunz != null && newFunz != null) {
//            oldPos = funzioni.indexOf(oldFunz);
//            funzioni.remove(oldFunz);
//            funzioni.add(oldPos, newFunz);
//            grid.setItems(funzioni);
//            grid.setHeightByRows(funzioni.size());
//
//            publish();
//        }// end of if cycle
//
//    }// end of method
//
//    /**
//     * Recupera la funzione selezionata dalla idKey
//     */
//    private Funzione getFunz(String idKey) {
//        for (Funzione funz : funzioni) {
//            if (funz.getId().equals(idKey)) {
//                return funz;
//            }// end of if cycle
//        }// end of for cycle
//
//        return null;
//    }// end of method
//
//
//    /**
//     * Fire event
//     * source     Obbligatorio questo field
//     * target     Obbligatorio (window, dialog, presenter) a cui indirizzare l'evento
//     * entityBean Opzionale (entityBean) in elaborazione
//     */
//    @Override
//    public void publish() {
//        if (source != null) {
//            publisher.publishEvent(new AFieldEvent(EATypeField.fieldModificato, source, target, entityBean, this));
//        }// end of if cycle
//    }// end of method
//
//
//    /**
//     * Aggiunge il listener al field
//     */
//    @Override
//    protected void addListener() {
////        if (radio != null) {
////            radio.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
////                @Override
////                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
////                    publish();
////                }// end of inner method
////            });// end of anonymous inner class
////        }// end of if cycle
//    }// end of method

}// end of class

