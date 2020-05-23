package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.UI;
import lombok.Data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.concurrent.*;

public class TurnoGenWorker {

    public static final String PROPERTY_STATUS = "status";
    public static final String PROPERTY_PROGRESS = "progress";

    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_ABORTED = "aborted";

    private PropertyChangeSupport support;

    private String old_status;
    private String status;
    private final UI ui;


    private boolean abort = false;


    public TurnoGenWorker(UI ui) {
        support = new PropertyChangeSupport(this);
        this.ui = ui;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void startWork() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        WorkerCallable task = new WorkerCallable();
        Future<Integer> future = executorService.submit(task);
        try {
            Integer i = future.get().intValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        ui.access(() -> {
//            try {
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        });



    }

    public void abort() {
        abort = true;
    }


    public class WorkerCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {

            old_status = status;
            status = STATUS_RUNNING;
            ui.access(() -> support.firePropertyChange(PROPERTY_STATUS, old_status, status));

            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(i);
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
            ui.access(() -> support.firePropertyChange(PROPERTY_STATUS, old_status, status));

            return 9;
        }

    }

}
