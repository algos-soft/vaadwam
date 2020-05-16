package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.riga.Riga;
import it.algos.vaadwam.modules.riga.RigaService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import it.algos.vaadwam.modules.turno.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.USA_COLORAZIONE_DIFFERENZIATA;

/**
 * Cella di un turno del tabellone.
 * <p>
 * Contiene le diverse cellette di iscrizione.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Tag("turno-cell")
@HtmlImport("src/views/tabellone/turnoCellPolymer.html")
public class TurnoCellPolymer extends PolymerTemplate<TurnoCellModel>   {

    public ADateService date = ADateService.getInstance();

    @Autowired
    protected PreferenzaService pref;

    @Autowired
    protected RigaService rigaService;

    @Autowired
    private TabelloneService tabelloneService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private ADateService dateService;

    private Riga riga;

    private Turno turno;

    private LocalDate giorno;

    private ITabellone tabellone;

    public TurnoCellPolymer() {
    }


    public TurnoCellPolymer(ITabellone tabellone, Riga riga, LocalDate giorno) {
        this.tabellone=tabellone;
        this.riga = riga;
        this.giorno = giorno;
    }


    @PostConstruct
    private void inizia() {

        // eventuale header
        if(!riga.getServizio().isOrarioDefinito()){
            Turno turno = getTurno();
            String text1=null;
            String text2=null;
            if(turno!=null){
                String oraIni=dateService.getOrario(turno.getInizio());
                String oraFine=dateService.getOrario(turno.getFine());
                if(oraIni!=null && oraFine!=null){
                    text1 = oraIni+" - "+oraFine;
                }

                text2=turno.getNote();

            }

            getModel().setUsaHeaders(true);
            getModel().setHeader1(text1);
            getModel().setHeader2(text2);
        }

        getModel().setRighecella(creaRigheCella());

    }


    private Turno getTurno(){
        Turno turno=null;
        List<Turno> turni = riga.getTurni();
        if (turni!=null){
            for(Turno t:turni){
                if(t.getGiorno().equals(giorno)){
                    turno=t;
                    break;
                }
            }
        }
        return turno;
    }


    /**
     * Crea le righe cella per questo turno e le aggiunge al modello
     */
    private List<RigaCella> creaRigheCella() {

        // eventuale turno (può non esserci)
        turno = rigaService.getTurno(riga, giorno);


        // recupera le eventuali iscrizioni
        List<Iscrizione> iscrizioni = null;
        if (turno != null) {
            iscrizioni = turno.getIscrizioni();
        }

        Servizio servizio = riga.servizio;
        List<Funzione> funzioni = servizioService.getFunzioniAll(servizio);

        List<RigaCella> righeCella = new ArrayList<>();

        // colore di base dell'intero turno (se turno nullo è colore "creabile")
        // per determinare il colore controlla tutte le iscrizioni per vedere se è valido
        EAWamColore coloreTurno = tabelloneService.getColoreTurno(turno);

        boolean colorazioneDifferenziata=pref.isBool(USA_COLORAZIONE_DIFFERENZIATA);

        for (Funzione funzServ : funzioni) {

            if (iscrizioni != null) {

                for (Iscrizione iscr : iscrizioni) {

                    // se usa colorazione differenziata, ricalcola la colorazione della singola iscrizione
                    EAWamColore coloreCella=coloreTurno;
                    if (colorazioneDifferenziata) {
                        coloreCella = tabelloneService.getColoreIscrizione(turno, iscr);
                    }

                    // assegna icona, testo, colore
                    if (iscr.funzione.equals(funzServ)) {
                        String icona = "vaadin:" + funzServ.icona.name().toLowerCase();
                        if (iscr.milite != null) {
                            boolean mod = haModificheManuali(iscr);
                            righeCella.add(new RigaCella(coloreCella, icona, iscr.milite.getSigla(), iscr.funzione.code, mod));
                        } else {
                            righeCella.add(new RigaCella(coloreCella, icona, "", iscr.funzione.code));
                        }
                    }

                }

            } else {    // no iscizioni, colore base del turno
                righeCella.add(new RigaCella(coloreTurno, "", "", funzServ.code));
            }

        }

        return righeCella;
    }


    private boolean haModificheManuali(Iscrizione iscr) {
        boolean status = false;

        if (iscr.note != null && iscr.note.length() > 0) {
            status = true;
        }
        if (iscr.inizio != null && (iscr.inizio.compareTo(turno.inizio) != 0 || turno.inizio == LocalTime.MIDNIGHT)) {
            status = true;
        }
        if (iscr.fine != null && (iscr.fine.compareTo(turno.fine) != 0 || turno.fine == LocalTime.MIDNIGHT)) {
            status = true;
        }

        return status;
    }


    /**
     * Celletta interna cliccata
     */
    @EventHandler
    void handleClick(@ModelItem RigaCella item) {
        if(tabellone!=null){
            tabellone.cellClicked(turno, giorno, riga.servizio, item.getFunzione());
        }
    }



}
