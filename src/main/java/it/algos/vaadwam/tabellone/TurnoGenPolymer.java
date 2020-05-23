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

    //private List<TurnoGenRiga> model;

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

        bEsegui.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            TurnoGenModel turnoGenModel=getModel();
            List<TurnoGenRiga> righe = turnoGenModel.getRighe();

            for(TurnoGenRiga riga : righe){
                List<TurnoGenFlag> flags = riga.getFlags();
                for(TurnoGenFlag flag : flags){
                    boolean flagValue = flag.isOn();
                    log.info("row: "+flag.getRow()+" col: "+flag.getColumn()+" value "+flag.isOn());
                }
            }

//            TurnoGenRiga riga = righe.get(0);
//            List<TurnoGenFlag> flags = riga.getFlags();
//            for(TurnoGenFlag flag : flags){
//                boolean flagValue = flag.isOn();
//                int a = 87;
//                int b= a;
//            }
//            int a = 87;
//            int b= a;

        });



    }


    /**
     * Riempie il modello dati
     */
    private void populateModel() {
        List<Servizio> servizi = servizioService.findAllStandardVisibili();
        List<TurnoGenRiga> righe = new ArrayList<>();
        int rowId = 0;
        for (Servizio servizio : servizi) {
            TurnoGenRiga riga = new TurnoGenRiga(rowId);
            riga.setNomeServizio(servizio.getCode());

            List<TurnoGenFlag> flags = new ArrayList<>();
            for (int colId = 0; colId < 7; colId++) {
                TurnoGenFlag flagStatus=new TurnoGenFlag(rowId, colId);
                flags.add(flagStatus);
//                flagStatus.setOn(colId % 2 == 0);
            }
            riga.setFlags(flags);
            righe.add(riga);

            rowId++;

        }
        getModel().setRighe(righe);

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

    }


    @EventHandler
    private void clickRow(@ModelItem TurnoGenRiga riga) {
        List<TurnoGenFlag> flags = riga.getFlags();
        if (allIsOn(flags)) {
            setAllFlags(flags, false);
        } else {
            setAllFlags(flags, true);
        }

    }

    private boolean allIsOn(List<TurnoGenFlag> flags) {
        for (TurnoGenFlag flag : flags) {
            if (!flag.isOn()) {
                return false;
            }
        }
        return true;
    }

    private void setAllFlags(List<TurnoGenFlag> flags, boolean state) {
        for (int i = 0; i < flags.size(); i++) {
            flags.get(i).setOn(state);
        }
    }

    @EventHandler
    private void clickCol(@RepeatIndex int itemIndex) {

        boolean allOn=true;
        for (TurnoGenRiga riga : getModel().getRighe()) {
            List<TurnoGenFlag> flagsRiga = riga.getFlags();
            if(!flagsRiga.get(itemIndex).isOn()){
                allOn=false;
                break;
            }
        }

        for (TurnoGenRiga riga : getModel().getRighe()) {
            List<TurnoGenFlag> flagsRiga = riga.getFlags();
            flagsRiga.get(itemIndex).setOn(!allOn);
        }

    }

    @EventHandler
    private void clickFlag(@RepeatIndex int itemIndex, @ModelItem TurnoGenRiga item) {
//        TurnoGenRiga riga = findRigaById(item.getId());
//        List<TurnoGenFlag> flags = riga.getFlags();
//        TurnoGenFlag flag = flags.get(itemIndex);
//        flag.setOn(!flag.isOn());
    }

    /**
     * Recupera una riga dal modello per id
     */
    private TurnoGenRiga findRigaById(int id) {
        TurnoGenRiga found = null;
        for (TurnoGenRiga riga : getModel().getRighe()) {
            if (riga.getId() == id) {
                found = riga;
            }
        }
        return found;
    }



//    private void invertFlag(int row, int col){
//        TurnoGenRiga riga = findRigaById(row);
//        TurnoGenFlag flag = riga.getFlags().get(col);
//        flag.setOn(!flag.isOn());
//    }


}
