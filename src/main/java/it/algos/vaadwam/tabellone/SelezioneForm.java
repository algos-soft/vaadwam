package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.ui.views.AView;
import it.algos.vaadwam.application.WamCost;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Fri, 26-Jul-2019
 * Time: 19:56
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Route(value = WamCost.TAG_SELEZIONE)
public class SelezioneForm extends AView {


    /**
     * Mantiene una property globale del tabellone <br>
     * Primo giorno selezionato <br>
     */
    private LocalDate startTabellone;

    /**
     * Mantiene una property globale del tabellone <br>
     * Ultimo giorno selezionato <br>
     */
    private LocalDate endTabellone;

    private DatePicker startDatePicker;

    private DatePicker endDatePicker;

    /**
     * Valore corrente del picker <br>
     */
    private LocalDate startPicker;

    /**
     * Valore corrente del picker <br>
     */
    private LocalDate endPicker;


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        super.setParameter(event, parameter);

        if (array.isValid(parametersMap)) {
            if (parametersMap.containsKey(KEY_MAP_GIORNO_INIZIO)) {
                startTabellone = date.localDateFromISO(parametersMap.get(KEY_MAP_GIORNO_INIZIO));
                startPicker = startTabellone;
            }// end of if cycle

            if (parametersMap.containsKey(KEY_MAP_GIORNO_FINE)) {
                endTabellone = date.localDateFromISO(parametersMap.get(KEY_MAP_GIORNO_FINE));
                endPicker = endTabellone;
            }// end of if cycle
        }// end of if cycle

        confirmButton.setEnabled(false);
    }// end of method


    /**
     * Placeholder (eventuale) per informazioni aggiuntive <br>
     * Deve essere sovrascritto <br>
     */
    @Override
    protected void creaAlertLayout() {
        Label label = new Label();
        label.setText("Tabellone dei turni");
        label.getElement().getStyle().set("font-weight", "bold");
        alertPlaceholder.add(label);
    }// end of method


    /**
     * Corpo centrale della vista <br>
     * Placeholder (obbligatorio) <br>
     */
    protected void creaBodyLayout() {
        Label message = new Label("Seleziona le date");
        message.getElement().getStyle().set("color", "#F03035");

        startDatePicker = new DatePicker();
        startDatePicker.setLabel("Giorno inziale");
        startDatePicker.setValue(startTabellone);
        endDatePicker = new DatePicker();
        endDatePicker.setLabel("Giorno finale");
        endDatePicker.setValue(endTabellone);

        startDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            endPicker = endDatePicker.getValue();
            if (selectedDate != null) {
                endDatePicker.setMin(selectedDate.plusDays(1));
                if (endPicker != null) {
                    message.setText("Da " + date.getWeekShortMese(selectedDate) + " a " + date.getWeekShortMese(endPicker));
                } else {
                    endDatePicker.setOpened(true);
                    message.setText("Seleziona il giorno finale");
                }// end of if/else cycle
            } else {
                endDatePicker.setMin(null);
                message.setText("Seleziona il giorno iniziale");
            }// end of if/else cycle
            startPicker = selectedDate;
            this.sincroConferma();
        });//end of lambda expressions and anonymous inner class

        endDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            startPicker = startDatePicker.getValue();
            if (selectedDate != null) {
                startDatePicker.setMax(selectedDate.minusDays(1));
                if (startPicker != null) {
                    message.setText("Da " + date.getWeekShortMese(startPicker) + " a " + date.getWeekShortMese(selectedDate));
                } else {
                    message.setText("Seleziona il giorno iniziale");
                }// end of if/else cycle
            } else {
                startDatePicker.setMax(null);
                if (startPicker != null) {
                    message.setText("Seleziona il giorno finale");
                } else {
                    message.setText("Nessuna data selezionata");
                }// end of if/else cycle
            }// end of if/else cycle
            endPicker = selectedDate;
            this.sincroConferma();
        });//end of lambda expressions and anonymous inner class

        bodyPlaceholder.add(startDatePicker);
        bodyPlaceholder.add(endDatePicker);
        bodyPlaceholder.add(message);
    }// end of method


    /**
     * Controlla l'abilitazione del bottone di conferma
     */
    private void sincroConferma() {
        boolean status = true;

        if (startPicker == null || endPicker == null) {
            status = false;
        }// end of if cycle

        if (startPicker != null && endPicker != null) {
            if (startPicker.equals(startTabellone) && endPicker.equals(endTabellone)) {
                status = false;
            }// end of if cycle
        }// end of if cycle

        confirmButton.setEnabled(status);
    }// end of method


    /**
     * Ritorno al tabellone coi parametri selezionati
     */
    private void routeToTabellone(LocalDate startDay, LocalDate endDay) {
        Map<String, String> mappa = new LinkedHashMap<>();
        mappa.put(KEY_MAP_GIORNO_INIZIO, date.getISO(startDay));
        mappa.put(KEY_MAP_GIORNO_FINE, date.getISO(endDay));
        final QueryParameters query = QueryParameters.simple(mappa);

        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST, query));
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Annulla
     */
    protected void back() {
        routeToTabellone(startTabellone, endTabellone);
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Conferma
     */
    protected void confirm() {
//        LocalDate startDate = startDatePicker.getValue();
//        LocalDate endDate = endDatePicker.getValue();
        routeToTabellone(startPicker, endPicker);
    }// end of method

}// end of class
