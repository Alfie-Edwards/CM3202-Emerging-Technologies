package Server;

import Framework.Remote.Callback;
import Framework.Remote.CallbackListener;
import Framework.Remote.User;
import Framework.Remote.UserList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class UserListServant extends UnicastRemoteObject implements UserList {

    private final ArrayList<User> userList;
    private final Callback listChangedHandler;

    protected UserListServant() throws RemoteException {
        userList = new ArrayList<>();
        listChangedHandler = new Callback();
    }

    @Override
    public List<User> getUsers() {
        return userList;
    }

    @Override
    public void registerUser(User user) {
        userList.add(user);
        listChangedHandler.call();
    }

    @Override
    public void addListChangedListener(CallbackListener listener) {
        listChangedHandler.addListener(listener);
    }

    @Override
    public void removeListChangedListener(CallbackListener listener) {
        listChangedHandler.removeListener(listener);
    }
}
