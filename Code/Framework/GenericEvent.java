package Framework;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GenericEvent<EventArgs> {

    private final List<Consumer<EventArgs>> listeners;

    public GenericEvent() {
        listeners = new ArrayList<>();
    }

    public void addListener(Consumer<EventArgs> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<EventArgs> listener) {
        listeners.remove(listener);
    }

    public void fire(EventArgs eventArgs) {
        for (Consumer<EventArgs> listener : listeners)
            listener.accept(eventArgs);
    }
}
