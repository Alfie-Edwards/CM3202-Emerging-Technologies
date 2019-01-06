package Client.Models;

import Framework.Colour;
import Framework.Remote.Callback;
import Framework.Remote.CallbackListener;
import Framework.Remote.Shape;
import Framework.Remote.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServant extends UnicastRemoteObject implements User {

    private final Callback selectedShapeChangedHandler;
    private Colour colour;
    private double cursorX, cursorY;
    private Shape selectedShape;

    public UserServant(Colour colour) throws RemoteException {
        this.colour = colour;
        this.cursorX = -1;
        this.cursorY = -1;
        selectedShapeChangedHandler = new Callback();
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public double getCursorX() {
        return cursorX;
    }

    public void setCursorX(double cursorX) {
        this.cursorX = cursorX;
    }

    @Override
    public double getCursorY() {
        return cursorY;
    }

    public void setCursorY(double cursorY) {
        this.cursorY = cursorY;
    }

    @Override
    public Shape getSelectedShape() {
        return selectedShape;
    }

    @Override
    public void addSelectedShapeChangedCallback(CallbackListener listener) {
        selectedShapeChangedHandler.addListener(listener);
    }

    @Override
    public void removeSelectedShapeChangedCallback(CallbackListener listener) {
        selectedShapeChangedHandler.removeListener(listener);
    }

    public void setSelectedShape(Shape selectedShape) {
        if (this.selectedShape == selectedShape) return;
        this.selectedShape = selectedShape;
        selectedShapeChangedHandler.call();
    }
}
