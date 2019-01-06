package Framework.Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserList extends Remote {
    List<User> getUsers() throws RemoteException;
    void registerUser(User user) throws RemoteException;
    void addListChangedListener(CallbackListener listener) throws RemoteException;
    void removeListChangedListener(CallbackListener listener) throws RemoteException;
}
