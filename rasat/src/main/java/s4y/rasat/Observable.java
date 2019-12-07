package s4y.rasat;

import java.util.HashSet;
import java.util.Set;

public class Observable<T> {
    final Set<Handler<T>> handlers = new HashSet<>();
    T value;

    public Observable(T value) {
        this.value = value;
    }

    public Observable() {
        this.value = null;
    }

    public void remove(Handler<T> handler) {
        handlers.remove(handler);
    }

    public Disposable<T> subscribe(Handler<T> handler) {
        handlers.add(handler);
        return new Disposable<>(handler,this);
    }

    public T value() {
        return value;
    }
}
