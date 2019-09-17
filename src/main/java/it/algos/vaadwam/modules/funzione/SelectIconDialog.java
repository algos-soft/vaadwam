package it.algos.vaadwam.modules.funzione;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.ui.dialog.ADialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 30-nov-2018
 * Time: 10:05
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class SelectIconDialog extends ADialog {


    private Funzione funzione;

    private VaadinIcon iconaOriginale;

    private VaadinIcon iconaCorrente;

    private VaadinIcon[] icons = {
            VaadinIcon.AMBULANCE,
            VaadinIcon.HEART,
            VaadinIcon.MEDAL,
            VaadinIcon.STETHOSCOPE,
            VaadinIcon.USER,
            VaadinIcon.USER_STAR,
            VaadinIcon.TRUCK,
            VaadinIcon.MALE,
            VaadinIcon.FEMALE,
            VaadinIcon.PHONE,
            VaadinIcon.DOCTOR,
            VaadinIcon.SPECIALIST,
            VaadinIcon.BED,
            VaadinIcon.OFFICE,
            VaadinIcon.BRIEFCASE,
            VaadinIcon.STAR};


    /**
     * Costruttore @Autowired <br>
     */
    public SelectIconDialog() {
        this("");
    }// end of constructor


    /**
     * Costruttore @Autowired <br>
     */
    public SelectIconDialog(String title) {
        super(title);
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        super.inizia();
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param funzione corrente
     */
    public void open(Funzione funzione,Runnable  confirmHandler) {
        this.funzione = funzione;
        this.iconaOriginale = funzione.icona;
        this.iconaCorrente = iconaOriginale;
        super.open(creaLayout(), confirmHandler, (Runnable) null);
    }// end of method


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     */
    protected VerticalLayout creaLayout() {
        VerticalLayout layout = new VerticalLayout();
        Button button;
        layout.setMargin(false);

        for (VaadinIcon vaadinIcon : icons) {
            Icon icona = vaadinIcon.create();
            button = new Button(icona, event -> sincro(event));
            button.setId(vaadinIcon.name());
            button.setWidth("3em");
            layout.add(button);
            if (vaadinIcon.equals(iconaCorrente)) {
                button.getElement().getClassList().add("rosso");
                button.setText("Selezionata");
                button.setWidth("10em");
            } else {
                button.getElement().getClassList().add("verde");
            }// end of if/else cycle
        }// end of for cycle

        return layout;
    }// end of method


    private void sincro(ClickEvent event) {
        Button button = (Button) event.getSource();
        Optional optional = button.getId();
        String idIconName = (String) optional.get();
        iconaCorrente = VaadinIcon.valueOf(idIconName);

        super.fixBodyLayout(creaLayout());
    }// end of method


//    @Override
//    public void cancellaHandler() {
//        super.cancellaHandler();
//    }// end of method


    @Override
    public void confermaHandler() {
        funzione.icona = iconaCorrente;
        super.confermaHandler();
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     */
    @Override
    public void open(AEntity item, EAOperation operation, AContext context) {
        super.open();
    }// end of method


}// end of class
