package Framework.Remote;

import Framework.BoundingEllipse;
import Framework.Colour;
import Framework.ShapeTypes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Shape extends Remote {
    ShapeTypes getType() throws RemoteException;

    BoundingEllipse getBoundingEllipse() throws RemoteException;
    void setBoundingEllipse(BoundingEllipse boundingEllipse) throws RemoteException;

    Colour getColour() throws RemoteException;
    void setColour(Colour colour) throws RemoteException;

}