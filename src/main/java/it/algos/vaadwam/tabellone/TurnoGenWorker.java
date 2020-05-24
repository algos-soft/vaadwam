package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.UI;
import it.algos.vaadwam.modules.servizio.Servizio;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Questo oggetto esegue le operazioni batch di creazione/cancellazione
 * turni in un thread separato.
 * <br>
 * Riporta lo stato di esecuzione / avanzamento ai componenti interessati che si
 * siano registrati chiamando il metodo addPropertyChangeListener.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TurnoGenWorker {

    public static final String PROPERTY_STATUS = "status";
    public static final String PROPERTY_PROGRESS = "progress";

    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_ABORTED = "aborted";

    private PropertyChangeSupport support;

    private String old_status;
    private String status;
    private float old_progress;
    private float progress; // range 0-1

    private boolean abort = false;

    private LocalDate data1;
    private LocalDate data2;
    private boolean crea;   // true=crea, false=cancella
    private List<ServiziGiornoSett> giornoServizi;


    @Getter
    private EsitoGenerazioneTurni esito;


    public TurnoGenWorker() {
    }

    /**
     * @param crea true per creare i turni, false per cancellarli
     * @data1 data inizio
     * @data2 data fine
     */
    public TurnoGenWorker(boolean crea, LocalDate data1, LocalDate data2, List<ServiziGiornoSett> giornoServizi) {
        this.crea=crea;
        this.data1=data1;
        this.data2=data2;
        this.giornoServizi=giornoServizi;

        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    public void startWork() {

        old_status = null;
        status = null;
        abort = false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        WorkerCallable task = new WorkerCallable();
        executorService.submit(task);

        // The shutdown() method doesn't cause immediate destruction of the ExecutorService.
        // It will make the ExecutorService stop accepting new tasks and shut down after all
        // running threads finish their current work.
        executorService.shutdown();

    }

    /**
     * Interrompe l'operazione nel punto in cui si trova
     */
    public void abort() {
        abort = true;
    }


    public class WorkerCallable implements Callable {

        @Override
        public Void call()  {

            old_status = status;
            status = STATUS_RUNNING;

            support.firePropertyChange(PROPERTY_STATUS, old_status, status);


            LocalDate date;
            int quantiGiorni = (int)DAYS.between(data1, data2);
            for(int i=0;i<=quantiGiorni;i++){

                date=data1.plusDays(i);

                // @todo real work here!!
                int quantiTurni;
                if(crea){
                    quantiTurni = creaTurni(date);
                }else{
                    quantiTurni = cancellaTurni(date);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                old_progress = progress;
                progress=(float)i/(float)quantiGiorni;

//                System.out.println(date+" - "+progress);

                support.firePropertyChange(PROPERTY_PROGRESS, old_progress, progress);

                if (abort) {
                    break;
                }

            }

            old_status = status;
            if (abort) {
                status = STATUS_ABORTED;
            } else {
                status = STATUS_COMPLETED;
            }

            esito = new EsitoGenerazioneTurni(0, abort, crea, null);

            support.firePropertyChange(PROPERTY_STATUS, old_status, status);

            return null;
        }

    }


    /**
     * Crea i turni per una certa data.
     * <br>
     * @param data la data
     * @return il numero di turni creati nel giorno specificato
     */
    private int creaTurni(LocalDate data){
        return 0;
    }

    /**
     * Cancella i turni per una certa data.
     * <br>
     * @param data la data
     * @return il numero di turni cancellati nel giorno specificato
     */
    private int cancellaTurni(LocalDate data){
        return 0;
    }



    /**
     * Classe che descrive il risultato dell'operazione
     */
    @Data
    class EsitoGenerazioneTurni {
        private int quanti;
        private boolean aborted;
        private boolean create;
        private List<LocalDate> giorni;

        /**
         * @param quanti    quanti turni sono stati generati (o cancellati)
         * @param aborted   se l'operazione è stata abortita
         * @param create    true se ha creato turni, false se ha cancellato turni
         * @param giorni    elenco dei giorni coinvolti nell'operazione
         */
        public EsitoGenerazioneTurni(int quanti, boolean aborted, boolean create, List<LocalDate> giorni) {
            this.quanti = quanti;
            this.aborted = aborted;
            this.create = create;
            this.giorni=giorni;
        }
    }

    /**
     * Classe che descrive un giorno della settimana e relativo elenco di servizi
     */
    @Data
    class ServiziGiornoSett {
        private int idxGiornoSett;
        private Servizio[] servizi;

        public ServiziGiornoSett(int idxGiornoSett, Servizio[] servizi) {
            this.idxGiornoSett = idxGiornoSett;
            this.servizi = servizi;
        }
    }


}
