package Framework.Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackListener extends Remote {

    void call() throws RemoteException;
}
