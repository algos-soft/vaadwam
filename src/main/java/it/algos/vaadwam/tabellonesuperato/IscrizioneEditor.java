package it.algos.vaadwam.tabellonesuperato;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.ui.components.HHMMComponent;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 17-nov-2018
 * Time: 19:55
 * <p>
 * Singola riga di TurnoEdit <br>
 * Icona della funzione <br>
 * Obbligatorietà della funzione o tramite checkbox oppure tramite colore diverso della riga <br>
 * Sigla della funzione, label fissa <br>
 * Popup dei militi abilitati per quella funzione <br>
 * Durata effettiva del servizio svolto dal singolo milite <br>
 * Note per eventuali problemi <br>
 */
@SpringComponent
@Tag(value = "xx")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class IscrizioneEditor extends HorizontalLayout {

    private final static String SEP = " . ";

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected MiliteService militeService;

    private IscrizioneGroupEditor parent;

    private AComboBox selUtenti;

    private TextField fNote;

    private HHMMComponent cTime;

    private Milite currentSelectedVolontario;


    private Funzione funzione;

    private Iscrizione iscrizioneOriginale; // l'iscrizione che appare quando viene costruito l'editor

    private Iscrizione iscrizioneCorrente;


    /**
     * @param iscrizioneOriginale l'iscrizione da editare
     * @param parent              l'editor parente per il controllo che il
     *                            volontario non sia già iscritto in altra posizione
     */
    public IscrizioneEditor(Iscrizione iscrizioneOriginale, IscrizioneGroupEditor parent) {
        this.iscrizioneOriginale = iscrizioneOriginale;
        this.funzione = iscrizioneOriginale.funzione;

        // clona l'iscrizione originale prima che venga cambiata
        if (this.iscrizioneOriginale != null) {
            try { // prova ad eseguire il codice
                iscrizioneCorrente = SerializationUtils.clone(iscrizioneOriginale);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle
        this.parent = parent;
//        setSpacing(true);

//        // crea il componente note
//        fNote = new TextField("note");
////        fNote.setValue(iscrizione.getNota());
//        fNote.setWidth("10em");

//        // crea il componente ore
//        cTime = new HHMMComponent("tempo hh:mm");
//        cTime.setHoursMinutes(iscrizione.getDurata());

        // crea il componente editor di tipo diverso a seconda
        // della modalità operativa multi-iscrizione
        Component comp;
//        if (isMultiIscrizione()) {
//            comp = creaCompPopup();
//        } else {
//            if (LibSession.isAdmin()) {
//                comp = creaCompPopup();
//            } else {
//                comp = creaCompBottoni();
//            }
//        }
//        comp = creaCompPopup();
//        setCompositionRoot(comp);

    }


    /**
     *
     */
    @PostConstruct
    private void inizia() {
        this.setMargin(true);
        this.setSpacing(true);
        this.setPadding(true);
//        selUtenti.addValueChangeListener(new Property.ValueChangeListener() {
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                // controllo che non sia già iscritto
//                Volontario sel = selUtenti.getVolontario();
//                if (sel != null) {
//                    boolean giaIscritto = IscrizioneEditor.this.parent.isIscritto(sel, IscrizioneEditor.this);
//                    if (giaIscritto) {
//                        Notification.show(sel + " è già iscritto a questo turno.", Notification.Type.ERROR_MESSAGE);
//                        if (currentSelectedVolontario == null) {
//                            selUtenti.select(null);
//                        } else {
//                            selUtenti.select(currentSelectedVolontario);
//                        }
//                    } else { //nuova iscrizione
//                        int minutiTurno = turno.getMinutiTotali();
//                        iscrizione.setMinutiEffettivi(minutiTurno);
//                        cTime.setHoursMinutes(minutiTurno);
//                        fNote.setValue("");
//                    }
//                }
//
//                syncFields();
//
//                currentSelectedVolontario = selUtenti.getVolontario()
//            }
//        });

        if (iscrizioneCorrente.getMilite() != null) {
            currentSelectedVolontario = iscrizioneCorrente.getMilite();
        }// end of if cycle

        this.addFunzione();
        this.addPopupMiliti();
        this.addDurata();
        this.addNote();

//        VerticalLayout volLayout = new VerticalLayout();
//        Label volLabel = new Label(creaTestoComponente());
//        volLayout.add(volLabel);
//        volLayout.add(selUtenti);
//
//        syncFields();
//
//        HorizontalLayout layout = new HorizontalLayout();
//        layout.setSpacing(true);
//        layout.add(volLayout);
//        layout.add(fNote);
//        layout.add(cTime);
//
//        this.add(layout);
    }


    /**
     * Icona e label con la sigla della funzione <br>
     */
    private void addFunzione() {
        Label label = null;

        if (funzione != null) {
            label = new Label(funzione.sigla);
            label.getElement().getClassList().add("bold");

            if (currentSelectedVolontario == null) {
                if (funzione.obbligatoria) {
                    label.getElement().getClassList().add("rosso");
                } else {
                    label.getElement().getClassList().add("blue");
                }// end of if/else cycle
            } else {
                label.getElement().getClassList().add("verde");
            }// end of if/else cycle
        }// end of if cycle

        if (label != null) {
            this.add(label);
        }// end of if cycle
    }// end of method


    /**
     * Popup dei militi abilitati per questa funzione <br>
     */
    private void addPopupMiliti() {
        List<Milite> items = militeService.findAllByFunzione(funzione);
        selUtenti = new AComboBox();
        selUtenti.setWidth("15em");
        selUtenti.setItems(items);
        selUtenti.addValueChangeListener(event -> sincroMilite((Milite) event.getValue()));//end of lambda expressions

        if (currentSelectedVolontario != null) {
            try { // prova ad eseguire il codice
                selUtenti.setValue(currentSelectedVolontario);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        if (array.isValid(items)) {
            this.add(new Label(SEP));
            this.add(selUtenti);
        }// end of if cycle
    }// end of method


    /**
     * Durata effettiva del servizio svolto dal singolo milite <br>
     */
    private void addDurata() {
        // crea il componente ore
        cTime = new HHMMComponent("tempo hh:mm");
        cTime.setWidth("15em");
        cTime.setHoursMinutes(iscrizioneCorrente.getDurataEffettiva());
//        this.add(cTime);
        AIntegerField fHours = new AIntegerField("Ore");
        fHours.setWidth("2.5em");
        fHours.setValue(iscrizioneCorrente.durataEffettiva + "");
        AIntegerField fMinutes = new AIntegerField();
        fMinutes.setWidth("2.5em");

        this.add(new Label(SEP));
        add(fHours);
        Label label = new Label(":");
        add(label);
        add(fMinutes);

    }// end of method


    /**
     * Note per eventuali problemi <br>
     */
    private void addNote() {
        // crea il componente note
        fNote = new TextField("note");
//        fNote.setValue(iscrizione.getNota());
        fNote.setWidth("10em");

        this.add(new Label(SEP));
        this.add(fNote);
    }// end of method


//    /**
//     * Crea un componente con bottoni che mostra il volontario iscritto o
//     * iscrive/disiscrive il volontario correntemente loggato.
//     * Solo gli utenti normali hanno questo tipo di componente, mentre gli admin
//     * hanno sempre il componente popup - qui non serve mai controllare isAdmin()
//     */
//    private Component creaCompBottoni() {
//
//        // bottone iscrizione
//        Button bMain = new Button();
//        bMain.setWidth("100%");
//        bMain.setHtmlContentAllowed(true);
//        Volontario volIscritto = iscrizione.getVolontario();
//        Funzione funz = iscrizione.getServizioFunzione().getFunzione();
//        String caption = "";
//        Volontario volLoggato = WamLogin.getLoggedVolontario();
//
//        if (volIscritto != null) {  // già iscritto
//            if (volIscritto.equals(volLoggato)) {
//                caption = "Sei già iscritto come <strong>" + funz.getSigla() + "</strong> ";
//                bMain.addStyleName("verde");
//            } else {
//                caption = "Già iscritto " + volIscritto.toString();
//                bMain.addStyleName("rosso");
//            }// end of if/else cycle
//
//            FontAwesome glyph = funz.getIcon();
//            if (glyph != null) {
//                caption = glyph.getHtml() + " " + caption;
//            }
//        } else {                    // non iscritto
//            caption = "";
//            FontAwesome glyph = funz.getIcon();
//            if (glyph != null) {
//                caption = glyph.getHtml() + " " + caption;
//            }
//            if (volLoggato != null) {
//                if (volLoggato.haFunzione(funz)) {
//                    caption += "Iscriviti come <strong>" + funz.getSigla() + "</strong>";
//                    bMain.addStyleName("verde");
//                } else {
//                    caption += "Non abilitato come <strong>" + funz.getSigla() + "</strong>";
//                    bMain.addStyleName("rosso");
//                    bMain.addStyleName("lightGrayBg");
//                }// end of if/else cycle
//            }// end of if cycle
//        }
//        bMain.setCaption(caption);
//
//
//        // click listener solo se non c'è nessuno iscritto
//        bMain.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                boolean cont = true;
//                Volontario volontario = null;
//
//                // controllo che non ci sia già un iscritto
//                if (cont) {
//                    if (volIscritto != null) {
//                        cont = false;
//                    }
//                }
//
//                // recupero il volontario loggato
//                if (cont) {
//                    volontario = WamLogin.getLoggedVolontario();
//                    if (volontario == null) {
//                        cont = false;
//                    }
//                }
//
//                // controllo che l'utente corrente abbia la funzione richiesta
//                if (cont) {
//                    if (!volontario.haFunzione(funz)) {
//                        Notification notif = new Notification("Nel tuo profilo non c'è la funzione " + funz.getSigla() + "<br>" + "Rivolgiti all'amministratore", Notification.Type.WARNING_MESSAGE);
//                        notif.setHtmlContentAllowed(true);
//                        notif.show(Page.getCurrent());
//                        cont = false;
//                    }
//                }
//
//                // controllo che non sia già iscritto in qualche altra posizione di questo turno
//                if (cont) {
//                    boolean giaIscritto = IscrizioneEditor.this.parent.isIscritto(volontario, IscrizioneEditor.this);
//                    if (giaIscritto) {
//                        Notification.show("Sei già iscritto a questo turno.", Notification.Type.ERROR_MESSAGE);
//                        cont = false;
//                    }
//                }
//
//                // se tutto ok procedo alla iscrizione
//                if (cont) {
//                    entityManager.getTransaction().begin();
//                    turno.getIscrizioni().add(iscrizione);
//                    iscrizione.setVolontario(volontario);
//                    iscrizione.setNota(fNote.getValue());
//                    iscrizione.setMinutiEffettivi(cTime.getTotalMinutes());
//                    entityManager.merge(turno);
//                    entityManager.getTransaction().commit();
//                    fireDismissListeners(new DismissEvent(bMain, true, false));
//
//                    // log iscrizione
//                    String desc = getLogIscrizione(iscrizione.getVolontario(), funz, turno);
//                    Log.info(LogType.iscrizione.getTag(), desc);
//                    WamEmailService.newIscrizione(iscrizione);
//                }
//
//            }
//        });
//
//
//        // bottone remove
//        Button bRemove = new Button();
//        bRemove.setIcon(FontAwesome.REMOVE);
//        bRemove.addStyleName("icon-red");
//        bRemove.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//
//                String err = checkIscrizioneCancellabile();
//
//                if (err.equals("")) {
//                    entityManager.getTransaction().begin();
//                    turno.getIscrizioni().remove(iscrizione);
//                    entityManager.merge(turno);
//                    entityManager.getTransaction().commit();
//                    fireDismissListeners(new DismissEvent(bRemove, true, false));
//
//                    // log cancIscrizione
//                    String desc = getLogCancellazione(iscrizione.getVolontario(), funz, turno);
//                    Log.info(LogType.cancIscrizione.getTag(), desc);
//
//                } else {
//                    Notification.show(err + "\nRivolgiti a un amministratore.", Notification.Type.ERROR_MESSAGE);
//                }
//
//            }
//
//
//        });
//
//        // bottone registra
//        Button bSave = new Button();
//        bSave.setIcon(FontAwesome.CHECK);
//        bSave.addStyleName("icon-green");
//        bSave.setVisible(volIscritto != null);
//        bSave.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                entityManager.getTransaction().begin();
//                iscrizione.setNota(fNote.getValue());
//                iscrizione.setMinutiEffettivi(cTime.getTotalMinutes());
//                entityManager.merge(iscrizione);
//                entityManager.getTransaction().commit();
//                fireDismissListeners(new DismissEvent(bRemove, true, false));
//            }
//        });
//
//        // disponibilità bottone remove:
//        // visibile solo se c'è un iscritto e se quello sono io.
//        boolean visible = false;
//        if (volIscritto != null) {
//            Volontario volLogged = WamLogin.getLoggedVolontario();
//            if (volLogged != null) {
//                if (volIscritto.equals(volLogged)) {
//                    visible = true;
//                }
//            }
//        }
//        bRemove.setVisible(visible);
//
//
//        // disponibilità bottone save:
//        // disponibile solo se c'è un iscritto e se quello sono io
//        // se il bottone non c'è ci metto una label vuota per mantenere gli allineamenti
//        Component rightComp = new Label("&nbsp;", ContentMode.HTML);
//        if (volIscritto != null) {
//            Volontario volLogged = WamLogin.getLoggedVolontario();
//            if (volLogged != null) {
//                if (volIscritto.equals(volLogged)) {
//                    rightComp = bSave;
//                }
//            }
//        }
//        rightComp.setWidth("3em");
//
//        // abilitazione note e tempo:
//        // questi campi sono sempre visibili.
//        // se c'è già un iscritto diverso da me, oppure se non sono abilitato per questa funzione, sono disabilitati
//        boolean enabled = true;
//        if (volIscritto != null) {
//            Volontario volLogged = WamLogin.getLoggedVolontario();
//            if (volLogged != null) {
//                if (!volIscritto.equals(volLogged)) {
//                    enabled = false;
//                }
//            }
//        }
//        if (enabled) {
//            Volontario volLogged = WamLogin.getLoggedVolontario();
//            if (volLogged != null) {
//                if (!volLogged.haFunzione(funz)) {
//                    enabled = false;
//                }
//            }
//        }
//        fNote.setEnabled(enabled);
//        cTime.setEnabled(enabled);
//
//
//        // layout finale
//        HorizontalLayout layout = new HorizontalLayout();
//        layout.setSpacing(true);
//        layout.setWidth("100%");
//        layout.addComponent(bRemove);
//        layout.addComponent(bMain);
//        layout.addComponent(fNote);
//        layout.addComponent(cTime);
//        layout.addComponent(rightComp);
//        layout.setExpandRatio(bMain, 1);
//
//        layout.setComponentAlignment(bRemove, Alignment.BOTTOM_CENTER);
//        layout.setComponentAlignment(bMain, Alignment.BOTTOM_CENTER);
//        layout.setComponentAlignment(fNote, Alignment.BOTTOM_LEFT);
//        layout.setComponentAlignment(cTime, Alignment.BOTTOM_LEFT);
//        layout.setComponentAlignment(rightComp, Alignment.BOTTOM_CENTER);
//
//        return layout;
//    }


//    /**
//     * Controlla se una iscrizione è cancellabile.
//     *
//     * @return stringa vuota se cancellabile, il motivo se non lo è
//     */
//    private String checkIscrizioneCancellabile() {
//        String err = "";
//
//        int mode = CompanyPrefs.modoCancellazione.getInt();
//
//        // nessun controllo
//        if (mode == Iscrizione.MODE_CANC_NONE) {
//            return "";
//        }
//
//        // controllo ore mancanti a inizio del turno
//        if (mode == Iscrizione.MODE_CANC_PRE) {
//            Turno turno = iscrizione.getTurno();
//            LocalDateTime startTurno = turno.getStartTime();
//            LocalDateTime now = LocalDateTime.now();
//            long minutesBetween = now.until(startTurno, ChronoUnit.MINUTES);
//            int hoursMax = CompanyPrefs.cancOrePrimaInizioTurno.getInt();
//            int minutesMax = hoursMax * 60;
//            // confronta con precisione minuto, avvisa con precisione ora
//            if (minutesBetween < minutesMax) {
//                err = "Mancano meno di " + hoursMax + " ore all'inizio del turno.";
//            }
//            return err;
//        }
//
//        // controllo minuti passati dal momento dell'iscrizione
//        if (mode == Iscrizione.MODE_CANC_POST) {
//            Timestamp tsCreazione = iscrizione.getTsCreazione();
//            if (tsCreazione != null) {
//                long time1 = tsCreazione.getTime();
//                long time2 = System.currentTimeMillis();
//                long msElapsed = time2 - time1;
//                int minutesElapsed = (int) (msElapsed / 1000 / 60);
//                int maxMinutes = CompanyPrefs.cancMinutiDopoIscrizione.getInt();
//                if (minutesElapsed > maxMinutes) {
//                    err = "Sono trascorsi più di " + maxMinutes + " minuti dall'iscrizione.";
//                }
//                return err;
//            }
//        }
//
//        return "";
//
//    }


    /**
     * Crea la stringa html da visualizzare nel componente
     * di selezione (usato come etichetta del popup o testo del bottone)
     */
    private String creaTestoComponente() {
        String lbltext = "";
//        Funzione funz = iscrizione.getServizioFunzione().getFunzione();
//         lbltext = funz.getSigla();
//        FontAwesome glyph = funz.getIcon();
//        if (glyph != null) {
//            lbltext = glyph.getHtml() + " " + lbltext;
//        }
        return lbltext;
    }


    private void syncFields() {
//        boolean enable = (selUtenti.() != null);
//        fNote.setVisible(enable);
//        cTime.setVisible(enable);
    }


    /**
     * Recupera il volontario correntemente selezionato nell'editor
     *
     * @return il volontario
     */
    public Milite getVolontario() {
        Milite v = null;
//        if (isMultiIscrizione()) {
//            v = selUtenti.getVolontario();
//        } else {
//            v = iscrizione.getVolontario();
//        }
        return v;
    }


    /**
     * Recupera l'iscrizione originariamente presente quando l'editor è stato presentato
     *
     * @return l'iscrizione originale
     */
    public Iscrizione getIscrizioneOriginale() {
        return iscrizioneOriginale;
    }


    public void sincroMilite(Milite milite) {
        if (iscrizioneOriginale != null) {
            iscrizioneOriginale.milite = milite;
        }// end of if cycle
    }// end of method


    /**
     * Recupera l'iscrizione aggiornata
     *
     * @return l'iscrizione aggiornata, null se nell'iscrizione
     * non è specificato il volontario (significa che nessuno è iscritto)
     */
    public Iscrizione getIscrizione() {
        if (getVolontario() != null) {
            iscrizioneCorrente.setMilite(getVolontario());
//            iscrizione.setNota(fNote.getValue());
//            iscrizione.setDurata(cTime.getTotalMinutes());
            return iscrizioneCorrente;
        } else {
            return null;
        }
    }


//    /**
//     * Combo filtrato sugli utenti che sono abilitati alla funzione corrente
//     */
//    class SelettoreUtenti extends ComboBox {
//
//        public SelettoreUtenti() {
//
//            // tutti i volontari che hanno la funzione corrente
//            Funzione funz = iscrizione.getServizioFunzione().getFunzione();
//            List volontari = CompanyQuery.getList(Volontario.class);
//            for (Object obj : volontari) {
//                Volontario v = (Volontario) obj;
//                if (v.haFunzione(funz)) {
//                    addItem(v);
//                }
//            }
//        }
//
//        public Volontario getVolontario() {
//            Volontario v = null;
//            Object obj = getValue();
//            if (obj != null && obj instanceof Volontario) {
//                v = (Volontario) obj;
//            }
//            return v;
//        }
//
//    }// end class SelettoreUtenti
//

} // end class IscrizioneEditor
