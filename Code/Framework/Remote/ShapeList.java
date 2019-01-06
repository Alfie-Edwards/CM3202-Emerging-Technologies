package Framework.Remote;

import Framework.BoundingEllipse;
import Framework.Colour;
import Framework.ShapeTypes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ShapeList extends Remote {
    List<Shape> getShapes() throws RemoteException;
    Shape addShape(ShapeTypes type, double rotation, BoundingEllipse boundingEllipse, Colour colour) throws RemoteException;
    void removeShape(int index) throws RemoteException;
    void addListChangedListener(CallbackListener listener) throws RemoteException;
    void removeListChangedListener(CallbackListener listener) throws RemoteException;
}
