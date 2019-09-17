package it.algos.vaadwam.modules.iscrizione;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadwam.modules.funzione.Funzione;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_ISC;

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
@Qualifier(TAG_ISC)
@Slf4j
@AIScript(sovrascrivibile = true)
public class IscrizioneViewDialog extends AViewDialog<Iscrizione> {


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     */
    @Autowired
    public IscrizioneViewDialog(@Qualifier(TAG_ISC) IAPresenter presenter) {
        super(presenter);
    }// end of Spring constructor


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder
     * Aggiunge i fields specifici alla fieldMap
     * Sovrascritto nella sottoclasse
     */
    @Override
    protected void addSpecificAlgosFields() {
        AComboBox comboFunzioni = null;
        AComboBox comboMiliti = null;
        List<Funzione> funzioniDelServizio = null;
//        Iscrizione iscrizione = currentItem;
//        Turno turno=null;
//        Servizio servizio=null;
        Funzione funzione = null;

        if (operation == EAOperation.editDaLink) {
//            turno = (Turno) operation.getSorgente();
//            if (turno!=null) {
//                servizio=turno.servizio;
//            }// end of if cycle
//            if (servizio!=null) {
//                funzioniDelServizio=servizio.funzioni;
//            }// end of if cycle


            comboFunzioni = (AComboBox) getField("funzione");

            funzioniDelServizio = new ArrayList<>();
            funzione = currentItem.funzione;

            if (funzione != null) {
                funzioniDelServizio.add(funzione);
            } else {
                log.error("Qualcosa non ha funzionato");
            }// end of if/else cycle

            if (comboFunzioni != null && funzioniDelServizio.size() == 1) {
                comboFunzioni.setItems(funzioniDelServizio);
            }// end of if cycle

            comboMiliti = (AComboBox) getField("milite");
        }// end of if cycle

    }// end of method


}// end of class