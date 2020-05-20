package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
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
import java.util.List;

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
    private Div gridholder;

    @Id
    private Button bChiudi;

    @Id
    private Button bEsegui;

    private Grid<GridRow> grid;

    @Autowired
    ServizioService servizioService;


    @Setter
    private CompletedListener completedListener;

    @PostConstruct
    private void init() {

        grid=buildGrid();
        gridholder.add(grid);

        bChiudi.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            EsitoGenerazioneTurni esito = new EsitoGenerazioneTurni(0,false, false, null, null);
            fireCompletedListener(esito);
        });

    }


    private Grid<GridRow> buildGrid(){
        Grid<GridRow> grid=new Grid<>(GridRow.class);

        List<Servizio> servizi = servizioService.findAllStandardVisibili();
        List<GridRow> rows = new ArrayList<>();
        for(Servizio servizio : servizi){
            GridRow row = new GridRow(servizio);
            rows.add(row);
        }
        grid.setItems(rows);
        grid.addColumn(GridRow::getServizio).setHeader("Servizio");
        grid.addColumn(GridRow::getLun).setHeader("Lun");
        grid.addColumn(GridRow::getMar).setHeader("Mar");
        grid.addColumn(GridRow::getMer).setHeader("Mer");
        grid.addColumn(GridRow::getGio).setHeader("Gio");
        grid.addColumn(GridRow::getVen).setHeader("Ven");
        grid.addColumn(GridRow::getSab).setHeader("Sab");
        grid.addColumn(GridRow::getDom).setHeader("Dom");


        return grid;
    }



    private void fireCompletedListener(EsitoGenerazioneTurni esito){
        if (completedListener!=null){
            completedListener.onCompleted(esito);
        }
    }

    interface CompletedListener{
        void onCompleted(EsitoGenerazioneTurni esito);
    }


    @Data
    class EsitoGenerazioneTurni{
        private int quanti;
        private boolean aborted;
        private boolean create;
        private LocalDate dataStart;
        LocalDate dataEnd;

        /**
         * @param quanti quanti turni sono stati generati o cancellati
         * @param aborted se l'operazione è stata abortita
         * @param create true se ha creato turni, false se ha cancellato turni
         * @param dataStart data del primo turno creato/cancellato
         * @param dataEnd data dell'ultimo turno creato/cancellato
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
            this.flags=new boolean[7];
        }

        public boolean getLun(){
            return flags[0];
        }
        public boolean getMar(){
            return flags[1];
        }
        public boolean getMer(){
            return flags[2];
        }
        public boolean getGio(){
            return flags[3];
        }
        public boolean getVen(){
            return flags[4];
        }
        public boolean getSab(){
            return flags[5];
        }
        public boolean getDom(){
            return flags[6];
        }

    }

}
