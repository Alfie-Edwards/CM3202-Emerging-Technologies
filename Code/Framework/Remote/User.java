package Framework.Remote;

import Framework.Colour;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface User extends Remote, Serializable {

    Colour getColour() throws RemoteException;
    double getCursorX() throws RemoteException;
    double getCursorY() throws RemoteException;
    Shape getSelectedShape() throws RemoteException;
    void addSelectedShapeChangedCallback(CallbackListener listener) throws RemoteException;
    void removeSelectedShapeChangedCallback(CallbackListener listener) throws RemoteException;
}
