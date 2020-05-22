package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Generatore di turni per il tabellone
 */
@Tag("turno-gen")
@HtmlImport("src/views/tabellone/turnogen-polymer.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TurnoGenPolymer extends PolymerTemplate<TurnoGenModel> {

    @Id
    private Button bChiudi;

    @Id
    private Button bEsegui;

    @Id
    private DatePicker picker1;

    @Id
    private DatePicker picker2;

    private List<TurnoGenRiga> model;

    @Autowired
    private ServizioService servizioService;

    @Setter
    private CompletedListener completedListener;

    private static final String[] TITLES = new String[]{"L", "M", "M", "G", "V", "S", "D"};

    @PostConstruct
    private void init() {

        picker1.setLocale(Locale.ITALY);
        picker2.setLocale(Locale.ITALY);

        populateModel();

        bChiudi.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            EsitoGenerazioneTurni esito = new EsitoGenerazioneTurni(0, false, false, null, null);
            fireCompletedListener(esito);
        });

    }


    /**
     * Riempie il modello dati
     */
    private void populateModel() {
        List<Servizio> servizi = servizioService.findAllStandardVisibili();
        model = new ArrayList<>();
        int id = 0;
        for (Servizio servizio : servizi) {
            id++;
            TurnoGenRiga riga = new TurnoGenRiga(id);
            riga.setNomeServizio(servizio.getCode());

            List<Boolean> flags = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                //boolean value = (i % 2 == 0);
                flags.add(false);
            }
            riga.setFlags(flags);
            model.add(riga);
        }
        getModel().setRighe(model);

        getModel().setTitoliGiorno(Arrays.asList(TITLES));

    }


    private void fireCompletedListener(EsitoGenerazioneTurni esito) {
        if (completedListener != null) {
            completedListener.onCompleted(esito);
        }
    }

    interface CompletedListener {
        void onCompleted(EsitoGenerazioneTurni esito);
    }


    @Data
    class EsitoGenerazioneTurni {
        private int quanti;
        private boolean aborted;
        private boolean create;
        private LocalDate dataStart;
        LocalDate dataEnd;

        /**
         * @param quanti    quanti turni sono stati generati o cancellati
         * @param aborted   se l'operazione è stata abortita
         * @param create    true se ha creato turni, false se ha cancellato turni
         * @param dataStart data del primo turno creato/cancellato
         * @param dataEnd   data dell'ultimo turno creato/cancellato
         */
        public EsitoGenerazioneTurni(int quanti, boolean aborted, boolean create, LocalDate dataStart, LocalDate dataEnd) {
            this.quanti = quanti;
            this.aborted = aborted;
            this.create = create;
            this.dataStart = dataStart;
            this.dataEnd = dataEnd;
        }
    }

    public class GridRow {

        @Getter
        private Servizio servizio;

        private boolean[] flags;

        public GridRow(Servizio servizio) {
            this.servizio = servizio;
            this.flags = new boolean[7];
        }

        public String getNomeServizio() {
            return servizio.getCode();
        }

        public boolean getLun() {
            return flags[0];
        }

        public boolean getMar() {
            return flags[1];
        }

        public boolean getMer() {
            return flags[2];
        }

        public boolean getGio() {
//            return flags[3];
            return true;
        }

        public boolean getVen() {
            return flags[4];
        }

        public boolean getSab() {
            return flags[5];
        }

        public boolean getDom() {
            return flags[6];
        }

        public boolean getValue(int idx) {
            return flags[idx];
        }

    }


    @EventHandler
    private void clickRow(@ModelItem TurnoGenRiga item) {
        int id = item.getId();
        TurnoGenRiga riga = findRigaById(id);
        List<Boolean> flags = riga.getFlags();
        if (allIsOn(flags)) {
            setAllFlags(flags, false);
        } else {
            setAllFlags(flags, true);
        }
        getModel().setRighe(model); // force refresh
    }

    private boolean allIsOn(List<Boolean> flags) {
        for (boolean flag : flags) {
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    private void setAllFlags(List<Boolean> flags, boolean state) {
        for (int i = 0; i < flags.size(); i++) {
            flags.set(i, state);
        }
    }

    @EventHandler
    private void clickCol(@RepeatIndex int itemIndex) {

        boolean allOn=true;
        for (TurnoGenRiga riga : model) {
            List<Boolean> flagsRiga = riga.getFlags();
            if(!flagsRiga.get(itemIndex)){
                allOn=false;
                break;
            }
        }

        for (TurnoGenRiga riga : model) {
            List<Boolean> flagsRiga = riga.getFlags();
            flagsRiga.set(itemIndex, !allOn);
        }

        getModel().setRighe(model); // force refresh

    }



    /**
     * Recupera una riga dal modello per id
     */
    private TurnoGenRiga findRigaById(int id) {
        TurnoGenRiga found = null;
        for (TurnoGenRiga riga : model) {
            if (riga.getId() == id) {
                found = riga;
            }
        }
        return found;
    }


}
