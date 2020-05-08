package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import java.time.Duration;
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

    private ComboBox<MiliteComboBean> combo;

    public CompIscrizione(Iscrizione iscrizione) {
        this.iscrizione = iscrizione;
    }

    @PostConstruct
    private void init(){
        this.setClassName("iscrizione");
        this.add(buildPrimaRiga());
        this.add(buildSecondaRiga());
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

        return combo;
    }


    private TimePicker buildPickerInizio() {
        TimePicker picker = new TimePicker();
        picker.setClassName("timePicker");
        picker.setStep(Duration.ofSeconds(900));
        if (iscrizione.getInizio()!=null){
            picker.setValue(iscrizione.getInizio());
        }
        return picker;
    }

    private TextField buildTextFieldNote() {
        TextField textField=new TextField();
        textField.setClassName("fieldNote");
        if(iscrizione.getNote()!=null){
            textField.setValue(iscrizione.getNote());
        }
        return textField;
    }

    private TimePicker buildPickerFine() {
        TimePicker picker = new TimePicker();
        picker.setClassName("timePicker");
        picker.setStep(Duration.ofSeconds(900));
        if (iscrizione.getFine()!=null){
            picker.setValue(iscrizione.getFine());
        }
        return picker;
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

    public String getIdMiliteSelezionato(){
        String idMilite=null;
        MiliteComboBean mc = combo.getValue();
        if (mc!=null){
            idMilite=mc.getIdMilite();
        }
        return idMilite;
    }


}

