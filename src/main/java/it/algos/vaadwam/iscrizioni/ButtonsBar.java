package it.algos.vaadwam.iscrizioni;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.USA_BUTTON_SHORTCUT;
import static it.algos.vaadwam.application.WamCost.TAG_TAB_LIST;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 10-ago-2019
 * Time: 08:38
 * <p>
 * Java wrapper of the polymer element `buttons-bar`
 * Mostra i due bottoni 'annulla' e 'conferma' in basso
 */
@Tag("buttons-bar")
@HtmlImport("src/views/iscrizioni/buttons-bar.html")
public class ButtonsBar extends PolymerTemplate<TemplateModel> {


    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private PreferenzaService pref;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("annulla")
    private Button annulla;

    /**
     * Component iniettato nel polymer html con lo stesso ID <br>
     */
    @Id("conferma")
    private Button conferma;


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        fixAnnulla();
        fixConferma();
    }// end of method


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixAnnulla() {
        setAnnullaText("Annulla");
        setAnnullaIcon(VaadinIcon.ARROW_LEFT);
        if (pref.isBool(USA_BUTTON_SHORTCUT)) {
            annulla.addClickShortcut(Key.ARROW_LEFT);
        }// end of if cycle
        annulla.addClickListener(e -> ritorno());
        this.setAnnullaTooltips("Ritorno al tabellone");
    }// end of method


    /**
     * Regolazioni standard di default <br>
     * Possono essere singolarmente modificate anche esternamente <br>
     */
    private void fixConferma() {
        setConfermaText("Conferma");
        setConfermaIcon(VaadinIcon.CHECK);
        setConfermaEnabled(false);
    }// end of method


    public void setAnnullaText(String annullaText) {
        annulla.setText(annullaText != null ? annullaText : "");
    }// end of method


    public void setConfermaText(String confermaText) {
        conferma.setText(confermaText != null ? confermaText : "");
    }// end of method


    public void setAnnullaIcon(VaadinIcon annullaIcon) {
        if (annullaIcon != null) {
            annulla.setIcon(new Icon(annullaIcon));
        }// end of if cycle
    }// end of method


    public void setConfermaIcon(VaadinIcon confermaIcon) {
        if (confermaIcon != null) {
            conferma.setIcon(new Icon(confermaIcon));
        }// end of if cycle
    }// end of method


    public void setAnnullaEnabled(boolean annullaEnabled) {
        annulla.setEnabled(annullaEnabled);
    }// end of method


    public void setConfermaEnabled(boolean confermaEnabled) {
        conferma.setEnabled(confermaEnabled);
    }// end of method


    public void setAnnullaTooltips(String toolTips) {
        annulla.getElement().setAttribute("title", toolTips);
    }// end of method


    public void setConfermaTooltips(String toolTips) {
        conferma.getElement().setAttribute("title", toolTips);
    }// end of method


    public Registration addAnnullalListener(ComponentEventListener<ButtonsBar.AnnullaEvent> listener) {
        return annulla.addClickListener(e -> listener.onComponentEvent(new ButtonsBar.AnnullaEvent(this, true)));
    }// end of method


    public Registration addConfermaListener(ComponentEventListener<ConfermaEvent> listener) {
        return conferma.addClickListener(e -> listener.onComponentEvent(new ButtonsBar.ConfermaEvent(this, true)));
    }// end of method


    private void ritorno() {
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }// end of method


    public static class AnnullaEvent extends ComponentEvent<ButtonsBar> {

        public AnnullaEvent(ButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }// end of constructor

    }// end of method


    public static class ConfermaEvent extends ComponentEvent<ButtonsBar> {

        public ConfermaEvent(ButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }// end of constructor

    }// end of method


}// end of class
