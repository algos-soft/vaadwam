package it.algos.vaadwam.modules.servizio;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.ui.fields.AField;
import it.algos.vaadflow.ui.fields.IAField;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 02-giu-2018
 * Time: 06:58
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ServizioFunzioniField extends Composite<Div> implements IAField {

    private Label label;
    private Grid grid;
    private Servizio servizio;
    private FunzioneService funzioneService;
    private Dialog dialog;
    private ComboBox<Funzione> comboFunzioni;
    private Button butAggiungi;

    public ServizioFunzioniField() {
        inizia();
    }// end of constructor

    @Override
    public AbstractField getField() {
        return null;
    }

    public void inizia() {
        getContent().add(crealabel());
        getContent().add(creaGrid());
    }// end of method


    private Label crealabel() {
        return new Label("Funzioni espletate in questo servizio");
    }// end of method


    private Grid creaGrid() {
        Grid.Column colonna = null;
        Button plus;
        grid = new Grid(Funzione.class);

        for (Object column : grid.getColumns()) {
            if (column instanceof Grid.Column) {
                grid.removeColumn((Grid.Column) column);
            }// end of if cycle
        }// end of for cycle

        grid.addColumn("ordine")
                .setHeader("#")
                .setFlexGrow(0)
                .setWidth("1.6em");

        grid.addColumn("code")
                .setHeader("Code")
                .setFlexGrow(0)
                .setWidth("5em");

//        grid.addColumn("sigla")
//                .setHeader("Sigla")
//                .setFlexGrow(0)
//                .setWidth("5em");

        grid.addColumn("descrizione")
                .setHeader("Descrizione")
                .setFlexGrow(0)
                .setWidth("15em");

        colonna = grid.addComponentColumn(funzione -> {
            Button edit = new Button("", event -> deleteFunz((Funzione) funzione));
            edit.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
//            edit.addClassName("review__edit");
//            edit.getElement().setAttribute("theme", "danger");
            return edit;

        });
        plus = new Button("", event -> addFunz());
        plus.setIcon(new Icon(VaadinIcon.PLUS));
//        plus.setIcon(new Icon("lumo", "plus"));
//        plus.addClassName("review__edit");
//        plus.getElement().setAttribute("theme", "primary");
        colonna.setHeader(plus);


        return grid;
    }// end of method


    public void addFunz() {
        dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout bottomBar = new HorizontalLayout();

        List items = funzioneService.findAll();
        if (items == null) {
            Notification.show("Non sei loggato");
            return;
        }// end of if cycle
        comboFunzioni = new ComboBox("Funzioni", items);
        comboFunzioni.addValueChangeListener(event -> sincro(event.getValue()));
        dialog.add(comboFunzioni);

        butAggiungi = new Button("Aggiungi", event -> aggiungi());
        butAggiungi.getElement().setAttribute("theme", "primary");
        butAggiungi.setEnabled(false);
        Button butAnnulla = new Button("Annulla", event -> chiudi());
        bottomBar.add(butAggiungi, butAnnulla);
        dialog.add(bottomBar);
        dialog.open();
    }// end of method


    public void deleteFunz(Funzione entityBean) {
        List<Funzione> items = null;
        ArrayList<Funzione> lista = null;

        if (entityBean != null) {
            items = servizio.getFunzioni();
        }// end of if cycle

        if (items != null && items.contains(entityBean)) {
            lista = new ArrayList<>();
            for (Funzione funz : items) {
                if (funz != entityBean) {
                    lista.add(funz);
                }// end of if cycle
            }// end of for cycle
            servizio.setFunzioni(lista);
            setItems(servizio);
        }// end of if cycle
    }// end of method


    public void setServizio(FunzioneService funzioneService, Servizio entityBean) {
        this.funzioneService = funzioneService;
        this.servizio = entityBean;
        setItems(servizio);
    }// end of method


    private void setItems(Servizio entityBean) {
        List<Funzione> items = entityBean.getFunzioni();
        if (items != null && items.size() > 0) {
            grid.setItems(items);
        }// end of if cycle
    }// end of method


    private void sincro(Funzione funzioneSelezionata) {
        if (butAggiungi != null) {
            butAggiungi.setEnabled(funzioneSelezionata != null);
        }// end of if cycle
    }// end of method

    private void aggiungi() {
        Funzione funzioneSelezionata = null;
        if (comboFunzioni != null && butAggiungi != null) {
            funzioneSelezionata = comboFunzioni.getValue();
        }// end of if cycle

        if (funzioneSelezionata != null) {
            addFunz(funzioneSelezionata);
        }// end of if cycle

    }// end of method

    public void addFunz(Funzione newFunz) {
        List<Funzione> items = null;
        ArrayList<Funzione> lista = new ArrayList<>();

        if (newFunz != null) {
            items = servizio.getFunzioni();
        }// end of if cycle

        if (items != null) {
            for (Funzione funz : items) {
                lista.add(funz);
            }// end of for cycle
        }// end of if cycle

        lista.add(newFunz);
        servizio.setFunzioni(lista);
        setItems(servizio);
        chiudi();
    }// end of method


    private void chiudi() {
        dialog.close();
    }// end of method


    @Override
    public Object getValore() {
        return null;
    }

}// end of class
