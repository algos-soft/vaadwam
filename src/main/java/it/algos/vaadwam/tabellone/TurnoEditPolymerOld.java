package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.application.WamCost;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.EATypeIscrizione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.tabellone.TurnoEditPolymerOld.TurnoEditModel;
import it.algos.vaadwam.wam.WamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Mar-2019
 * Time: 11:04
 * <p>
 * Polymer per visualizzare/editare un singolo turno in dettaglio
 * DEVE richiamare un file html (possibilmente con lo stesso nome, più html)
 * DEVE usare un modello dati per 'sincronizzare' i valori delle property (possibilmente con lo stesso nome, più model)
 * <p>
 * L'istanza NON viene costruita da SpringBoot alla partenza del programma (SCOPE_SINGLETON)
 * L'istanza NON viene costruita con appContext.getBean(TurnoEditPolymer.class) (SCOPE_PROTOTYPE)
 * L'istanza viene costruita da Flow con la chiamata 'handleClick' da TurnoCellPolymer
 * - getUI().ifPresent(ui -> ui.navigate(TAG_TURNO_EDIT + "/" + turno.id));
 * <p>
 * Eventuali injection sono disponibili DOPO setParameter()
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Route(value = WamCost.TAG_TURNO_EDIT+"oldissimo")
@Viewport("width=device-width")
@Tag("turno-edit")
@HtmlImport("src/views/tabellone/turnoEditPolymer.html")
public class TurnoEditPolymerOld extends PolymerTemplate<TurnoEditModel> implements HasUrlParameter<String>, BeforeEnterObserver {

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected TurnoService turnoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ServizioService servizioService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected MiliteService militeService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ADateService dateService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_CRO)
    private WamService wamService;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TabelloneService tabelloneService;

//    private String turnoKey;

    private Turno turno = null;

    private Servizio servizio = null;

    private TurnoEditModel model;

    private boolean registraAbilitato = false;


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // implementation omitted
        Object record = null;

    }


    /**
     * Recupera il turno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     * oppure <br>
     * costruisce un nuovo Turno col Servizio ed il Giorno arrivati come parametri della location <br>
     *
     * @param event     con la Location, segments, target, source, ecc
     * @param parameter per recuperare l'istanza di Turno
     *                  per creare una nuova istanza di Turno dal Servizio e dal Giorno
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location;
        QueryParameters queryParameters;
        Map<String, List<String>> parametersMap;

        if (text.isValid(parameter)) {
            inizia(parameter);
        } else {
            location = event.getLocation();
            queryParameters = location.getQueryParameters();
            parametersMap = queryParameters.getParameters();
            if (parametersMap != null) {
                inizia(parametersMap);
            } else {
                System.out.println("Parametri non corretti");
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Costruisce la pagina <br>
     * Recupera il turno arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param turnoKey per recuperare l'istanza di Turno
     */
    private void inizia(String turnoKey) {

        if (text.isValid(turnoKey)) {
            turno = turnoService.findById(turnoKey);
        }// end of if cycle

        if (turno == null) {
            Notification.show("Errore", 2000, Notification.Position.MIDDLE);
            return;
        }// end of if cycle

        this.inizia();
    }// end of method


    /**
     * Costruisce la pagina <br>
     * costruisce un nuovo Turno col Servizio ed il Giorno arrivati come parametri della location <br>
     *
     * @param parametersMap per costruire una nuova istanza di Turno
     */
    private void inizia(Map<String, List<String>> parametersMap) {
        List<String> listaGiorni;
        String numGiorniDelta = "";
        LocalDate giorno = null;
        List<String> listaServizi;
        String servizioKey = "";
        Servizio servizio = null;

        listaGiorni = parametersMap.get(KEY_MAP_GIORNO);
        if (array.isValid(listaGiorni) && listaGiorni.size() == 1) {
            numGiorniDelta = listaGiorni.get(0);
        }// end of if cycle
        if (text.isValid(numGiorniDelta)) {
            giorno = dateService.getGiornoDelta(numGiorniDelta);
        }// end of if cycle

        listaServizi = parametersMap.get(KEY_MAP_SERVIZIO);
        if (array.isValid(listaServizi) && listaServizi.size() == 1) {
            servizioKey = listaServizi.get(0);
        }// end of if cycle
        if (text.isValid(servizioKey)) {
            servizio = servizioService.findById(servizioKey);
        }// end of if cycle
        turno = turnoService.newEntity(giorno, servizio);

        this.inizia();
    }// end of method


    /**
     * Costruisce la pagina <br>
     */
    private void inizia() {
        servizio = turno.servizio;

        // creiamo il modello della vista (TurnoEditModel) dal turno
        model = getModel();

        fixData(turno.getGiorno());
        fixOrario(turno.getServizio());
        fixServizio(turno.getServizio());
        fixIscrizioni();

        model.setRegistraAbilitato(registraAbilitato);
    }// end of method


    private void fixData(LocalDate giorno) {
        String data = "";

        if (giorno != null) {
            data = dateService.get(giorno, EATime.completa);
            model.setData(data);
        }// end of if cycle

    }// end of method


    private void fixOrario(Servizio servizio) {
        String orario = "";

        if (servizio != null) {
//            orario = servizioService.getOre(servizio);
            model.setOrario(orario);
        }// end of if cycle

    }// end of method


    private void fixServizio(Servizio servizio) {
        if (servizio != null) {
            model.setServizio(servizio.descrizione);
        }// end of if cycle
    }// end of method


    /**
     * Abilita/disabilita ogni iscrizione a seconda dei permessi dell'utente collegato.
     * <p>
     * Quella per cui non ha i permessi è disabilitata.
     * Eventualmente differenziare col colore di scadenza del turno.
     * <p>
     * Quella di cui ha i permessi è abilitata se vuota.
     * Eventualmente differenziare col colore di scadenza del turno.
     * Se è già segnato un milite (non quello loggato) è disabilitata.
     * Se è già segnato il milite loggato è abilitata per permettere la cancellazione (entro il tempo previsto).
     */
    private void fixIscrizioni() {
        List<IscrizioneModel> iscrizioni = new ArrayList<>();
        EATypeIscrizione type = EATypeIscrizione.libero;
        Milite militeLoggato = wamService.getMilite();
        Milite militeIscrizione = null;
        Funzione funzione = null;
        String icona;
        String nickName;
        String note;
        String inizio;
        String fine;
        IscrizioneModel modello = null;
        boolean nomeDisabilitato = true;
        boolean noteOrariDisabilitati = true;
        String funzCode = "";
        String colore = "";
        boolean isMiliteLoggatoSegnatoNelTurno = isMiliteLoggatoSegnatoNelTurno();

        if (turno != null && turno.iscrizioni != null) {
            for (Iscrizione iscr : turno.iscrizioni) {
                icona = "";
                nickName = "";
                note = "";
                inizio = LocalTime.MIDNIGHT.toString();
                fine = LocalTime.MIDNIGHT.toString();

                militeIscrizione = iscr.getMilite();
                if (militeIscrizione != null) {
                    nickName = militeIscrizione.getSigla();
                    inizio = iscr.inizio != null ? iscr.inizio.toString() : "";
                    fine = iscr.fine != null ? iscr.fine.toString() : "";
                    note = militeIscrizione.getNote();
                }// end of if/else cycle

                funzione = iscr.getFunzione();
                if (funzione != null) {
                    icona = "vaadin:" + funzione.icona.name().toLowerCase();
                    funzCode = funzione.code;
                }// end of if cycle

                if (isMiliteLoggatoSegnatoNelTurno) {
                    if (militeIscrizione != null && militeIscrizione.getSigla().equals(militeLoggato.getSigla())) {
                        nomeDisabilitato = false;
                        noteOrariDisabilitati = false;
                    } else {
                        nomeDisabilitato = true;
                        noteOrariDisabilitati = true;
                    }// end of if/else cycle
                } else {
                    if (text.isEmpty(nickName) && militeService.isAbilitato(militeLoggato, funzione)) {
                        nomeDisabilitato = false;
                    } else {
                        nomeDisabilitato = true;
                    }// end of if/else cycle
                }// end of if/else cycle

                if (pref.isBool(USA_COLORAZIONE_DIFFERENZIATA)) {
                    colore = tabelloneService.getColoreIscrizione(turno, iscr).getTag().toLowerCase();
                } else {
                    colore = tabelloneService.getColoreTurno(turno).getTag().toLowerCase();
                }// end of if/else cycle

                modello = new IscrizioneModel(icona, funzCode, nickName, note, inizio, fine, nomeDisabilitato, noteOrariDisabilitati, colore, funzCode);
                iscrizioni.add(modello);
            }// end of for cycle
        }// end of if cycle

        model.setIscrizioni(iscrizioni);
    }// end of method


    private boolean isMiliteLoggatoSegnatoNelTurno() {
        boolean status = false;
        Milite militeLoggato = wamService.getMilite();

        if (militeLoggato != null && turno != null && array.isValid(turno.iscrizioni)) {
            for (Iscrizione iscr : turno.iscrizioni) {
                if (iscr.milite != null && iscr.milite.id.equals(militeLoggato.id)) {
                    status = true;
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno script con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Il click NON può arrivare da una iscrizione per cui l'utente loggato non è abilitato <br>
     * Il click NON può arrivare da una iscrizione già segnata per un altro milite <br>
     * Se l'iscrizione è vuota, segno il nome del milite loggato <br>
     * Se il milite loggato era già segnato, lo avviso prima della cancellazione <br>
     */
    @EventHandler
    void handleClickMilite(@ModelItem IscrizioneModel iscrizione) {
        String nome = iscrizione.getNome();
//        boolean abilitato = iscrizione.isAbilitato();
        String nomeClient = iscrizione.getNome();
        Milite militeLoggato = wamService.getMilite();
        registraAbilitato = false;

        if (militeLoggato == null) {
            return;
        }// end of if cycle

        if (text.isEmpty(nomeClient)) {
            iscrizione.setNome(militeLoggato.username);
            iscrizione.setNoteOrariDisabilitati(false);
            iscrizione.setInizio(servizio.inizio.toString());
            iscrizione.setFine(servizio.fine.toString());
            registraAbilitato = true;
        } else {
            if (nomeClient.equals(militeLoggato.getSigla())) {
                if (controlloCancellazione()) {
                    iscrizione.setNome("");
                    iscrizione.setInizio(LocalTime.MIDNIGHT.toString());
                    iscrizione.setFine(LocalTime.MIDNIGHT.toString());
                    iscrizione.setNoteOrariDisabilitati(true);
                    registraAbilitato = true;
                }// end of if cycle
            } else {
                Notification.show("Qualcosa non ha funzionato in TurnoEditPolymer.handleClickMilite()", 3000, Notification.Position.MIDDLE);
            }// end of if/else cycle
        }// end of if/else cycle


//        List<IscrizioneModel> iscrizioni = getModel().getIscrizioni();
//        for (IscrizioneModel i : iscrizioni) {
//            //i.setAbilitato(false);
//        }

        model = getModel();
//        iscrizione.setDisabilitata(true);
        model.setRegistraAbilitato(registraAbilitato);
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Alla chiusura della pagina la navigazione via Route rimanda al Tabellone <br>
     */
    @EventHandler
    void handleClickInizio(@ModelItem IscrizioneModel iscrizione) {
        String inizioTxt = "";
        String fineTxt;
        LocalTime inizioTime;
        LocalTime fineTime;

        List<IscrizioneModel> lista = getModel().getIscrizioni();
        Object a = iscrizione.getInizio();
        Object b = iscrizione.getNome();
        Object c = iscrizione.getFunzCode();
        Object d = iscrizione.getColore();
        Object e = iscrizione.getFunzione();
        inizioTime = LocalTime.parse(inizioTxt);
        turno.iscrizioni.get(1).inizio = inizioTime;
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Alla chiusura della pagina la navigazione via Route rimanda al Tabellone <br>
     */
    @EventHandler
    void handleClickFine(@ModelItem IscrizioneModel iscrizione) {
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Alla chiusura della pagina la navigazione via Route rimanda al Tabellone <br>
     */
    @EventHandler
    void handleClickBack() {
        getUI().ifPresent(ui -> ui.navigate(TAG_TAB_LIST));
    }// end of method


    /**
     * Java event handler on the server, run asynchronously <br>
     * <p>
     * Evento ricevuto dal file html collegato e che 'gira' sul Client <br>
     * Il collegamento tra il Client sul browser e queste API del Server viene gestito da Flow <br>
     * Uno scritp con lo stesso nome viene (eventualmente) eseguito in maniera sincrona sul Client <br>
     * <p>
     * Alla chiusura della pagina la navigazione via Route rimanda al Tabellone <br>
     */
    @EventHandler
    void handleClickRegistra() {
        update();
        if (registra()) {
            handleClickBack();
        }// end of if cycle
    }// end of method


    private boolean registra() {
        boolean modificato = false;
        boolean doppiaIscrizione = false;
        Milite militeOld = null;
        Milite militeNew = null;
        String nomeMiliteOld;
        String nomeMiliteNew = "";
        Iscrizione iscrizioneMongo;
        IscrizioneModel iscrizioneModello;
        List<Iscrizione> listaMongo = turno.iscrizioni;
        List<IscrizioneModel> listaModello = model.getIscrizioni();
        List<String> listaControllo;
        String nome;
//        IscrizioneModel modello = null;
//        String inizioTxt;
//        String fineTxt;
//        LocalTime inizioTime;
//        LocalTime fineTime;

        //--controllo doppia iscrizione
        //--messo per sicurezza - la doppia iscrizione dovrebbe essera già stata intercettata prima
        listaControllo = new ArrayList<>();
        for (IscrizioneModel iscr : listaModello) {
            nome = iscr.getNome();
            if (text.isValid(nome)) {
                if (listaControllo.contains(nome)) {
                    doppiaIscrizione = true;
                } else {
                    listaControllo.add(nome);
                }// end of if/else cycle
            }// end of if cycle
        }// end of for cycle
        if (doppiaIscrizione) {
            Notification.show("Non puoi segnarti due volte nello stesso turno", 3000, Notification.Position.MIDDLE);
            fixIscrizioni();
            model.setRegistraAbilitato(false);
            return false;
        }// end of if cycle
        //--fine controllo doppia iscrizione

        for (int k = 0; k < listaMongo.size(); k++) {
            militeOld = listaMongo.get(k).getMilite();
            nomeMiliteOld = "";
            if (militeOld != null) {
                nomeMiliteOld = militeOld.getSigla();
            }// end of if cycle
            nomeMiliteNew = listaModello.get(k).getNome();

            if (!nomeMiliteNew.equals(nomeMiliteOld)) {
                militeNew = militeService.findByKeyUnica(nomeMiliteNew);
                turno.iscrizioni.get(k).milite = militeNew;
                modificato = true;
            }// end of if cycle

            //--corrispondenza tra le iscrizioni del turno e quelle del modello
            //--l'ordine di presentazione NON è garantito e quindi devo cercare la corrispondenza con un metodo ad hoc
//            modello = getModelloCorrente(listaModello, listaMongo.get(k));
//            inizioTxt = modello.getInizio();
//            Object a = modello.getInizio();
//            Object b = modello.getNome();
//            Object c = modello.getFunzCode();
//            Object d = modello.getColore();
//            Object e = modello.getFunzione();
//            inizioTime = LocalTime.parse(inizioTxt);
//            turno.iscrizioni.get(k).inizio = inizioTime;
//            fineTxt = modello.getFine();
//            fineTime = LocalTime.parse(fineTxt);
//            turno.iscrizioni.get(k).fine = fineTime;
            int x = 87;
        }// end of for cycle

        if (modificato) {
            turnoService.save(turno);
            Notification.show("La modifica al turno è stata registrata", 2000, Notification.Position.MIDDLE);
        } else {
            if (turnoService.isEsisteEntityKeyUnica(turno)) {
                Notification.show("Il turno non è stato modificato", 2000, Notification.Position.MIDDLE);
            } else {
                turnoService.save(turno);
                Notification.show("È stato creato un nuovo turno", 2000, Notification.Position.MIDDLE);
            }// end of if/else cycle
        }// end of if/else cycle

        return true;
    }// end of method


    public void update() {
        List<IscrizioneModel> lista = getModel().getIscrizioni();
        for (IscrizioneModel model : lista) {
            Object a = model.getInizio();
            Object b = model.getNome();
            Object c = model.getFunzCode();
            Object d = model.getColore();
            Object e = model.getFunzione();
        }// end of for cycle
    }


    private IscrizioneModel getModelloCorrente(List<IscrizioneModel> listaModello, Iscrizione iscriz) {
        IscrizioneModel modello = null;

        if (array.isValid(listaModello)) {
            for (IscrizioneModel mod : listaModello) {
                if (mod.getFunzCode().equals(iscriz.funzione.code)) {
                    modello = mod;
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return modello;
    }// end of method


    /**
     * La cancellazione per l'utente può essere ammessa oppure no. Ovviamente gli admin possono sempre cancellare. <br>
     * La cancellazione può essere ammessa sempre oppure solo entro un determinato tempo <br>
     * <br>
     */
    private boolean controlloCancellazione() {
        boolean status = true;
        String type = pref.getEnumStr(TIPO_CANCELLAZIONE);

        // cancellazione ammessa per questa croce
        switch (type) {
            case "sempre"://ammessa sempre
                status = true;
                break;
            case "mai"://non ammessa mai
                if (wamService.getWamLogin().isAdminOrDev()) { // developer o admin
                    status = true;
                } else {
                    //--inserire avviso di impossibilità e contattare admin
                    Notification.show("Dopo che ti sei segnato in un turno, non puoi più cancellarti. Contatta un admin per eliminare l'iscrizione.", 4000, Notification.Position.MIDDLE);
                    status = false;
                }// end of if/else cycle
                break;
            case "fino":// ammessa solo per un determinato tempo
                if (true) { // controllo tempo mancante
                    status = true;
                } else {
                    //--inserire avviso di troppo poco tempo mancante e contattare admin
                    status = false;
                }// end of if/else cycle
                break;
            case "dopo":// ammessa solo per un determinato tempo
                if (true) { // controllo tempo trascorso
                    status = true;
                } else {
                    //--inserire avviso di troppo tempo trascorso e contattare admin
                    status = false;
                }// end of if/else cycle
                break;
            default:
                break;
        } // end of switch statement

        return status;
    }// end of method


    /**
     * Modello dati per TurnoEditPolymer
     */
    public interface TurnoEditModel extends TemplateModel {

        String getData();

        void setData(String data);

        String getOrario();

        void setOrario(String orario);

        String getServizio();

        void setServizio(String servizio);

        List<IscrizioneModel> getIscrizioni();

        void setIscrizioni(List<IscrizioneModel> iscrizioni);

        boolean isRegistraAbilitato();

        void setRegistraAbilitato(boolean disabilitato);


    }// end of interface


}// end of class
