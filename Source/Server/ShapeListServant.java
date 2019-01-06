package Server;
import Framework.*;
import Framework.Remote.Callback;
import Framework.Remote.CallbackListener;
import Framework.Remote.Shape;
import Framework.Remote.ShapeList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

class ShapeListServant extends UnicastRemoteObject implements ShapeList {

    private final ArrayList<Shape> shapeList;
    private final Callback listChangedHandler;

    public ShapeListServant() throws RemoteException {
        super();
        this.shapeList = new ArrayList<>();
        listChangedHandler = new Callback();
    }

    // Persistence
    public ShapeListServant(ArrayList<Shape> initialShapeList) throws RemoteException {
        super();
        this.shapeList = initialShapeList;
        listChangedHandler = new Callback();
    }

    @Override
    public List<Shape> getShapes() {
        return shapeList;
    }

    @Override
    public Shape addShape(ShapeTypes type, double rotation, BoundingEllipse boundingEllipse, Colour colour) throws RemoteException {
        ShapeServant shape = new ShapeServant(type, rotation, boundingEllipse, colour);
        shapeList.add(shape);
        listChangedHandler.call();
        return shape;
    }

    @Override
    public void removeShape(int index) throws RemoteException {
        shapeList.remove(index);
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
