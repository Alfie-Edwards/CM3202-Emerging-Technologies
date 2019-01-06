package Framework.Remote;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Callback {

    private final ArrayList<CallbackListener> listeners;

    public Callback() {
        listeners = new ArrayList<>();
    }

    public void addListener(CallbackListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CallbackListener listener) {
        listeners.remove(listener);
    }

    public void call() {
        int i = 0;
        while (i < listeners.size()) {
            CallbackListener listener = listeners.get(i);
            try {
                listener.call();
                i++;
            }
            catch (RemoteException e) {
                listeners.remove(i);
            }
        }
    }
}
