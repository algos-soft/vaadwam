package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.turno.Turno;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Componente tutto Java - singola iscrizione nel dialogo Iscrizioni dell'admin
 */
@SpringComponent
@Scope(SCOPE_PROTOTYPE)
@Slf4j
public class CompIscrizione extends Div {

    @Autowired
    private MiliteService militeService;

    @Getter
    private Iscrizione iscrizione;

    private TurnoEditPolymer turnoEditPolymer;

    private ComboBox<MiliteComboBean> combo;

    private TimePicker pickerInizio;

    private TimePicker pickerFine;

    private TextField textField;

    public CompIscrizione(Iscrizione iscrizione, TurnoEditPolymer turnoEditPolymer) {
        this.iscrizione = iscrizione;
        this.turnoEditPolymer = turnoEditPolymer;
    }

    @PostConstruct
    private void init(){
        this.setClassName("iscrizione");
        this.add(buildPrimaRiga());
        this.add(buildSecondaRiga());

        enableTimeNote(combo.getValue()!=null);

    }

    private Div buildPrimaRiga() {
        Div div = new Div();
        div.setClassName("iscrizioneRow");
        div.add(buildDivFunzione());
        div.add(buildCombo());
        return div;
    }


    private Div buildSecondaRiga() {
        Div div = new Div();
        div.setClassName("iscrizioneRow");
        div.add(buildPickerInizio());
        div.add(buildTextFieldNote());
        div.add(buildPickerFine());
        return div;
    }


    /**
     * Construisce il Div con icona e nome funzione
    */
    private Div buildDivFunzione(){
        Div div = new Div();
        div.setClassName("funzione");
        Icon icona=getFunzione().getIcona().create();
        icona.setClassName("funzioneIcona");
        div.add(icona);
        div.add(getFunzione().getSigla());
        return div;
    }


    private ComboBox<MiliteComboBean> buildCombo() {
        this.combo = new ComboBox<>();
        this.combo.setClassName("comboMilite");
        List<MiliteComboBean> militiCombo = getMilitiCombo();
        combo.setItems(militiCombo);

        // se esiste un milite in ingresso lo preseleziona nel combo
        if(getMiliteOriginale()!=null){
            MiliteComboBean comboMilite=new MiliteComboBean(getMiliteOriginale());
            if (militiCombo.contains(comboMilite)){
                combo.setValue(comboMilite);
            }
        }

        combo.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<MiliteComboBean>, MiliteComboBean>>) event -> {
            MiliteComboBean value = event.getValue();
            if(value==null){
                resetTimeNote();
            }
            enableTimeNote(value!=null);

            // inserito/modificato un milite
            if(value!=null){

                // controllo che non sia già iscritto in altra posizione
                boolean passed=true;
                for(CompIscrizione comp : turnoEditPolymer.getCompIscrizioni()){
                    if (comp!=this){
                        String idAltroMilite=comp.getIdMiliteSelezionato();
                        if(idAltroMilite!=null){
                            String idQuestoMilite=value.getIdMilite();
                            if (idQuestoMilite.equals(idAltroMilite)){
                                mostraAvvisoGiaPresente(value.getSiglaMilite());
                                combo.setValue(event.getOldValue());
                                passed=false;
                                break;
                            }
                        }
                    }
                }

                // assegno ora inizio e fine prendendole da quanto
                // mostrato nei picker del turno
                if (passed){
                    LocalTime oraInizio = turnoEditPolymer.getOraInizioPicker();
                    pickerInizio.setValue(oraInizio);
                    LocalTime oraFine = turnoEditPolymer.getOraFinePicker();
                    pickerFine.setValue(oraFine);
                }

            }

        });

        return combo;
    }

    private void mostraAvvisoGiaPresente(String nome){
        ConfirmDialog
                .createError()
                .withMessage(nome + " è già iscritto a questo turno")
                .withAbortButton(ButtonOption.caption("Chiudi"), ButtonOption.icon(VaadinIcon.CLOSE))
                .open();
    }


    private TimePicker buildPickerInizio() {
        pickerInizio = new TimePicker();
        pickerInizio.setClassName("timePicker");
        pickerInizio.setStep(Duration.ofSeconds(900));
        if (iscrizione.getInizio()!=null){
            pickerInizio.setValue(iscrizione.getInizio());
        }
        return pickerInizio;
    }

    private TextField buildTextFieldNote() {
        textField=new TextField();
        textField.setClassName("fieldNote");
        if(iscrizione.getNote()!=null){
            textField.setValue(iscrizione.getNote());
        }
        return textField;
    }

    private TimePicker buildPickerFine() {
        pickerFine = new TimePicker();
        pickerFine.setClassName("timePicker");
        pickerFine.setStep(Duration.ofSeconds(900));
        if (iscrizione.getFine()!=null){
            pickerFine.setValue(iscrizione.getFine());
        }
        return pickerFine;
    }


    private List<MiliteComboBean> getMilitiCombo(){
        List<MiliteComboBean> militiCombo=new ArrayList<>();
        List<Milite>  militi = militeService.findAllByFunzione(getFunzione());

        for(Milite milite : militi){
            MiliteComboBean mc = new MiliteComboBean(milite);
            militiCombo.add(mc);
        }
        return militiCombo;
    }


    private Funzione getFunzione(){
        return iscrizione.getFunzione();
    }


    private Milite getMiliteOriginale(){
        return iscrizione.getMilite();
    }

    String getIdMiliteSelezionato(){
        String idMilite=null;
        MiliteComboBean mc = combo.getValue();
        if (mc!=null){
            idMilite=mc.getIdMilite();
        }
        return idMilite;
    }

    LocalTime getOraInizio(){
        return pickerInizio.getValue();
    }

    LocalTime getOraFine(){
        return pickerFine.getValue();
    }

    String getNote(){
        return textField.getValue();
    }

    /**
     * Resetta orari e note
     */
    private void resetTimeNote(){
        pickerInizio.setValue(null);
        pickerFine.setValue(null);
        textField.setValue("");
    }

    private void enableTimeNote(boolean b) {
        pickerInizio.setEnabled(b);
        pickerFine.setEnabled(b);
        textField.setEnabled(b);
    }

    private Turno getTurno(){
        return turnoEditPolymer.getTurno();
    }


    public void setOraInizio(LocalTime value) {
        pickerInizio.setValue(value);
    }

    public void setOraFine(LocalTime value) {
        pickerFine.setValue(value);
    }
}

