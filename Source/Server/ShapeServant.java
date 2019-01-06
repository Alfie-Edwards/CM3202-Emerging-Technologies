package Server;

import Framework.BoundingEllipse;
import Framework.Colour;
import Framework.Remote.Shape;
import Framework.ShapeTypes;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;


class ShapeServant extends UnicastRemoteObject implements Shape {
    private ShapeTypes type;
    private double rotation;
    private BoundingEllipse boundingEllipse;
    private Colour colour;

    public ShapeServant(ShapeTypes type, double rotation, BoundingEllipse boundingEllipse, Colour colour) throws RemoteException {
        this.type = type;
        this.rotation = rotation;
        this.boundingEllipse = boundingEllipse;
        this.colour = colour;
    }

    @Override
    public ShapeTypes getType() {
        return type;
    }

    @Override
    public BoundingEllipse getBoundingEllipse() {
        return boundingEllipse;
    }

    @Override
    public void setBoundingEllipse(BoundingEllipse boundingEllipse) {
        this.boundingEllipse = boundingEllipse;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public void setColour(Colour colour) {
        this.colour = colour;
    }
}