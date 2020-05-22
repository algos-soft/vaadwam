package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.ui.LoadMode;
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

    private Grid grid;

    @Autowired
    private ServizioService servizioService;

    @Setter
    private CompletedListener completedListener;

    private static final String[] TITLES = new String[]{"L","M","M","G","V","S","D"};

    @PostConstruct
    private void init() {

        picker1.setLocale(Locale.ITALY);
        picker2.setLocale(Locale.ITALY);

        populateModel();

//        buildGrid();
//        gridholder.add(grid);

        bChiudi.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            EsitoGenerazioneTurni esito = new EsitoGenerazioneTurni(0, false, false, null, null);
            fireCompletedListener(esito);
        });

    }


    private void buildGrid() {
        grid = new Grid<>(GridRow.class);

        List<Servizio> servizi = servizioService.findAllStandardVisibili();
        List<GridRow> rows = new ArrayList<>();
        for (Servizio servizio : servizi) {
            GridRow row = new GridRow(servizio);
            rows.add(row);
        }
        grid.setItems(rows);
        grid.removeAllColumns();

        addColumnServizio();
        for(int i=0;i<7;i++){
            addColumnGiorno(i);
        }
//        Grid.Column<String> col = grid.addColumn(GridRow::getNomeServizio);
//        col.setHeader("Servizio");
//        grid.addColumn(GridRow::getLun).setHeader("Lun");
//        grid.addColumn(GridRow::getMar).setHeader("Mar");
//        grid.addColumn(GridRow::getMer).setHeader("Mer");
//        grid.addColumn(GridRow::getGio).setHeader("Gio");
//        grid.addColumn(GridRow::getVen).setHeader("Ven");
//        grid.addColumn(GridRow::getSab).setHeader("Sab");
//        grid.addColumn(GridRow::getDom).setHeader("Dom");

    }


    /**
     * Aggiunge la colonna per visualizzare i servizi previsti
     */
    private void addColumnServizio() {

        ValueProvider<Servizio, Label> componentProvider = new ValueProvider() {

            @Override
            public Label apply(Object obj) {
                Servizio servizio = ((GridRow)obj).getServizio();
                Label label = new Label(servizio.getCode());
                return label;
            }
        };

        Grid.Column<Label> column = grid.addComponentColumn(componentProvider);

        column.setHeader("Servizio");
        column.setFlexGrow(0);
        column.setWidth("10em");
        column.setSortable(false);
        column.setResizable(false);
        column.setFrozen(true);

    }


    /**
     * Aggiunge la colonna per visualizzare un giorno
     */
    private void addColumnGiorno(int idx) {

        ValueProvider<GridRow, Checkbox> componentProvider = new ValueProvider() {

            @Override
            public Checkbox apply(Object obj) {
                GridRow row = ((GridRow)obj);
                boolean flag = row.getValue(idx);
                Checkbox cb = new Checkbox();
                cb.setLabel("");
                //cb.getStyle().set("width","1.5em");
                cb.setValue(flag);
                return cb;
            }
        };

        Grid.Column<Checkbox> column = grid.addComponentColumn(componentProvider);

//        column.setAutoWidth(false);
//        column.getElement().getStyle().set("margin","0");
//        column.getElement().getStyle().set("padding","0");

        String title = TITLES[idx];

        Div div = new Div();
        div.setText(title);

//        Label titleLabel=new Label(title);
//        titleLabel.getStyle().set("overflow","hidden");
//        titleLabel.getStyle().set("text-overflow", "clip");
        column.setHeader(div);
        column.setFlexGrow(0);
        column.setWidth("6em");
        column.setSortable(false);
        column.setResizable(false);
        column.setFrozen(true);

    }


    /**
     * Riempie il modello dati
     */
    private void populateModel(){
        List<Servizio> servizi = servizioService.findAllStandardVisibili();
        List<TurnoGenRiga> righe = new ArrayList<>();
        for (Servizio servizio : servizi) {
            TurnoGenRiga riga = new TurnoGenRiga();
            riga.setNomeServizio(servizio.getCode());

            List<Boolean>flags = new ArrayList<>();
            for(int i=0; i<7;i++){
                flags.add(false);
            }
            riga.setFlags(flags);
            righe.add(riga);
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

        public boolean getValue(int idx){
            return flags[idx];
        }

    }

}
