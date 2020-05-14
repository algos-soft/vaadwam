package it.algos.vaadwam.broadcast;

import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<BroadcastMsg>> listeners = new LinkedList<>();

    public static synchronized Registration register(Consumer<BroadcastMsg> listener) {
        listeners.add(listener);

        return () -> {
            synchronized (Broadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void  broadcast(BroadcastMsg message) {
        for (Consumer<BroadcastMsg> listener : listeners) {
            executor.execute(() -> listener.accept(message));
        }
    }
}
