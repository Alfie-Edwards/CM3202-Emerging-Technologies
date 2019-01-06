package Client.Models;

import Framework.BoundingEllipse;
import Framework.Colour;
import Framework.GenericEvent;
import Framework.Remote.*;
import Framework.ShapeTypes;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.function.Consumer;

public class ClientModel {

    private final Registry registry;
    private final UserServant currentUser;
    private final GenericEvent<List<Shape>> shapesChangedEvent;
    private final GenericEvent<List<User>> usersChangedEvent;
    private final GenericEvent<Shape> currentUserSelectedShapeChangedEvent;
    private ShapeList shapeList;
    private UserList userList;
    private List<Shape> shapes;
    private List<User> users;

    public ClientModel(String ipAddress, int port, UserServant currentUser) throws RemoteException, NotBoundException {
        this.currentUser = currentUser;
        shapesChangedEvent = new GenericEvent<>();
        usersChangedEvent = new GenericEvent<>();
        currentUserSelectedShapeChangedEvent = new GenericEvent<>();

        System.setProperty("java.security.policy", "file:permissions.policy");
        registry = LocateRegistry.getRegistry(ipAddress, port);
        shapeList = (ShapeList) registry.lookup("ShapeList");
        userList = (UserList) registry.lookup("UserList");

        shapeList.addListChangedListener(new CallbackListenerServant(this::onShapesChanged));
        userList.addListChangedListener(new CallbackListenerServant(this::onUsersChanged));
        currentUser.addSelectedShapeChangedCallback(new CallbackListenerServant(this::onCurrentUserSelectedShapeChanged));
        userList.registerUser(this.currentUser);
    }

    // Getters and Setters

    public UserServant getCurrentUser() {
        return currentUser;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<User> getUsers() {
        return users;
    }

    public Shape addShape(ShapeTypes type, double rotation, BoundingEllipse boundingEllipse, Colour colour) {
        try {
            return shapeList.addShape(type, rotation, boundingEllipse, colour);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeShape(int index) {
        try {
            shapeList.removeShape(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Event Listeners (Passthroughs for Callbacks)

    public void addShapesChangedListener(Consumer<List<Shape>> listener) {
        shapesChangedEvent.addListener(listener);
    }

    public void removeShapesChangedListener(Consumer<List<Shape>> listener) {
        shapesChangedEvent.removeListener(listener);
    }

    public void addUsersChangedListener(Consumer<List<User>> listener) {
        usersChangedEvent.addListener(listener);
    }

    public void removeUsersChangedListener(Consumer<List<User>> listener) {
        usersChangedEvent.removeListener(listener);
    }

    public void addCurrentUserSelectedShapeChangedListener(Consumer<Shape> listener) {
        currentUserSelectedShapeChangedEvent.addListener(listener);
    }

    public void removeCurrentUserSelectedShapeChangedListener(Consumer<Shape> listener) {
        currentUserSelectedShapeChangedEvent.removeListener(listener);
    }

    // Event Handlers

    private void onShapesChanged() {
        try {
            shapes = shapeList.getShapes();
            shapesChangedEvent.fire(shapes);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void onUsersChanged() {
        try {
            users = userList.getUsers();
            usersChangedEvent.fire(users);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void onCurrentUserSelectedShapeChanged() {
        Shape selectedShape = currentUser.getSelectedShape();
        currentUserSelectedShapeChangedEvent.fire(selectedShape);
    }

    public class CallbackListenerServant extends UnicastRemoteObject implements CallbackListener {

        private final Runnable callbackFunction;

        public CallbackListenerServant(Runnable callbackFunction) throws RemoteException {
            this.callbackFunction = callbackFunction;
        }

        public void call() throws RemoteException{
            callbackFunction.run();
        }
    }
}
