package it.algos.vaadwam.tabellone;

import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.service.AVaadinService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;
import it.algos.vaadwam.modules.turno.TurnoService;
import it.algos.vaadwam.wam.WamLogin;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Autowired
    private TurnoService turnoService;

    @Autowired
    protected AVaadinService vaadinService;

    private Croce croce;


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

        croce=(Croce)vaadinService.getSessionContext().getLogin().getCompany();

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


            LocalDate giorno;
            int totTurni=0;
            List<LocalDate> dateCoinvolte=new ArrayList<>();
            int quantiGiorni = (int)DAYS.between(data1, data2);
            for(int i=0;i<=quantiGiorni;i++){

                giorno=data1.plusDays(i);

                // @todo real work here!!
                int quantiTurni;
                if(crea){
                    quantiTurni = creaTurni(giorno);
                }else{
                    quantiTurni = cancellaTurni(giorno);
                }
                if(quantiTurni>0){
                    dateCoinvolte.add(giorno);
                }
                totTurni+=quantiTurni;

                old_progress = progress;
                progress=(float)i/(float)quantiGiorni;

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

            esito = new EsitoGenerazioneTurni(totTurni, abort, crea, dateCoinvolte);

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
        int dow = getDow(data);
        List<Servizio> servizi = getServiziForDayOfWeek(dow);
        int i=0;
        for(Servizio servizio : servizi){
            // qui siamo in un thread separato e non abbiamo login, passare sempre la croce
            List<Turno> turni = turnoService.findByDateAndServizioAndCroce(data, servizio, croce);
            if(turni.isEmpty()){    // non se esiste già!
                Turno turno = turnoService.newEntity(croce, data, servizio);
                turnoService.save(turno);
                i++;
            }
        }
        return i;
    }

    /**
     * Cancella i turni per una certa data.
     * <br>
     * @param data la data
     * @return il numero di turni cancellati nel giorno specificato
     */
    private int cancellaTurni(LocalDate data){
        int dow = getDow(data);
        List<Servizio> servizi = getServiziForDayOfWeek(dow);
        int i=0;
        for(Servizio servizio : servizi){
            // qui siamo in un thread separato e non abbiamo login, passare sempre la croce
            List<Turno> turni = turnoService.findByDateAndServizioAndCroce(data, servizio, croce);
            if(!turni.isEmpty()){
                for(Turno turno : turni){  // per sicurezza ciclo anche se dovrebbe essere uno solo
                    if(turnoService.getMilitiIscritti(turno).size()==0){
                        turnoService.delete(turno);
                        i++;
                    }
                }
            }
        }
        return i;
    }

    /**
     * Ritorna l'indice del giorno di settimana per una certa data
     * <br>
     * @param data la data
     * @return l'indice del giorno di settimana (0-6)
     */
    private int getDow(LocalDate data){
        return data.getDayOfWeek().getValue()-1;
    }

    /**
     * Ritorna i servizi per un dato giorno della settimana (lun=0, dom=6)
     * <br>
     * @param idx indice del giorno della settimana (lun=0, dom=6)
     * @return i servizi del giorno
     */
    private List<Servizio> getServiziForDayOfWeek(int idx){
        List<Servizio> servizi=new ArrayList<>();
        for(ServiziGiornoSett sgs : giornoServizi){
            if(sgs.getIdxGiornoSett()==idx){
                servizi=sgs.getServizi();
                break;
            }
        }
        return servizi;
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

        public String getTestoAzione(){
            String azione;
            if(esito.isCreate()){
                azione="Creati";
            }else{
                azione="Cancellati";
            }
            return azione;
        }
    }


}
