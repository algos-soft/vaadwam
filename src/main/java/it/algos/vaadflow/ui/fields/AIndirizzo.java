package it.algos.vaadflow.ui.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 03-ott-2019
 * Time: 16:28
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AIndirizzo extends CustomField<Address> {

    private final ATextField indirizzo = new ATextField();

    private final ATextField localita = new ATextField();

    private final ATextField cap = new ATextField();

    @Autowired
    private AddressService service;

    private String keyId;

//    private Address currentItem;


    public AIndirizzo() {
        this("");
    }// end of constructor


    public AIndirizzo(String keyId) {
        this.keyId = keyId;
//        add(datePicker, timePicker);
    }// end of constructor


    @PostConstruct
    public void inizia() {
//        currentItem = service.findByKeyUnica(keyId);

        add(indirizzo, localita, cap);

//        if (currentItem != null) {
//            indirizzo.setValue(currentItem.indirizzo);
//            localita.setValue(currentItem.localita);
//            cap.setValue(currentItem.cap);
//        }// end of if cycle
    }// end of constructor


    @Override
    protected Address generateModelValue() {
//        final LocalDate date = datePicker.getValue();
//        final LocalTime time = timePicker.getValue();
//        return date != null && time != null ? LocalDateTime.of(date, time) : null;
        return null;
    }// end of method


    @Override
    protected void setPresentationValue(Address currentItem) {
//        currentItem = service.findByKeyUnica(keyId);

        if (currentItem != null) {
            indirizzo.setValue(currentItem.indirizzo);
            localita.setValue(currentItem.localita);
            cap.setValue(currentItem.cap);
        }// end of if cycle

//        datePicker.setValue(newPresentationValue
//                != null ?
//                newPresentationValue.toLocalDate() :
//                null);
//        timePicker.setValue(newPresentationValue
//                != null ?
//                newPresentationValue.toLocalTime() :
//                null);
    }// end of method

//    @Override
//    protected String generateModelValue() {
//        return null;
//    }// end of method
//
//
//    @Override
//    protected void setPresentationValue(String idKey) {
//
//    }// end of method

}// end of class
