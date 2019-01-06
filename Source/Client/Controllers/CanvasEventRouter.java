package Client.Controllers;

import Client.Views.CanvasView;
import Framework.GenericEvent;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class CanvasEventRouter implements Controller {

    private final CanvasView canvasView;
    private final GenericEvent<MouseEvent> mouseMovedEvent;
    private final GenericEvent<MouseEvent> mouseDraggedEvent;
    private final GenericEvent<MouseEvent> mousePressedEvent;
    private final GenericEvent<MouseEvent> mouseReleasedEvent;
    private final GenericEvent<MouseEvent> mouseExitedEvent;
    private final GenericEvent<KeyEvent> keyPressedEvent;
    private final GenericEvent<KeyEvent> keyReleasedEvent;

    public CanvasEventRouter(CanvasView canvasView) {

        this.canvasView = canvasView;
        mouseMovedEvent = new GenericEvent<>();
        mouseDraggedEvent = new GenericEvent<>();
        mousePressedEvent = new GenericEvent<>();
        mouseReleasedEvent = new GenericEvent<>();
        mouseExitedEvent = new GenericEvent<>();
        keyPressedEvent = new GenericEvent<>();
        keyReleasedEvent = new GenericEvent<>();
    }

    @Override
    public void start() {
        canvasView.setOnMouseMoved(this::onMouseMoved);
        canvasView.setOnMouseDragged(this::onMouseDragged);
        canvasView.setOnMousePressed(this::onMousePressed);
        canvasView.setOnMouseReleased(this::onMouseReleased);
        canvasView.setOnMouseExited(this::onMouseExited);
        canvasView.setOnKeyPressed(this::onKeyPressed);
        canvasView.setOnKeyReleased(this::onKeyReleased);
    }

    @Override
    public void stop() {
        canvasView.setOnMouseMoved(null);
        canvasView.setOnMouseDragged(null);
        canvasView.setOnMousePressed(null);
        canvasView.setOnMouseReleased(null);
        canvasView.setOnMouseExited(null);
        canvasView.setOnKeyPressed(null);
        canvasView.setOnKeyReleased(null);
    }

    // Add Listener

    public void addMouseMovedListener(Consumer<MouseEvent> listener) {
        mouseMovedEvent.addListener(listener);
    }

    public void addMouseDraggedListener(Consumer<MouseEvent> listener) {
        mouseDraggedEvent.addListener(listener);
    }

    public void addMousePressedListener(Consumer<MouseEvent> listener) {
        mousePressedEvent.addListener(listener);
    }

    public void addMouseReleasedListener(Consumer<MouseEvent> listener) {
        mouseReleasedEvent.addListener(listener);
    }

    public void addMouseExitedListener(Consumer<MouseEvent> listener) {
        mouseExitedEvent.addListener(listener);
    }

    public void addKeyPressedListener(Consumer<KeyEvent> listener) {
        keyPressedEvent.addListener(listener);
    }

    public void addKeyReleasedListener(Consumer<KeyEvent> listener) {
        keyReleasedEvent.addListener(listener);
    }

    // Remove Listener

    public void removeMouseMovedListener(Consumer<MouseEvent> listener) {
        mouseMovedEvent.removeListener(listener);
    }

    public void removeMouseDraggedListener(Consumer<MouseEvent> listener) {
        mouseDraggedEvent.removeListener(listener);
    }

    public void removeMousePressedListener(Consumer<MouseEvent> listener) {
        mousePressedEvent.removeListener(listener);
    }

    public void removeMouseReleasedListener(Consumer<MouseEvent> listener) {
        mouseReleasedEvent.removeListener(listener);
    }

    public void removeMouseExitedListener(Consumer<MouseEvent> listener) {
        mouseExitedEvent.removeListener(listener);
    }

    public void removeKeyPressedListener(Consumer<KeyEvent> listener) {
        keyPressedEvent.removeListener(listener);
    }

    public void removeKeyReleasedListener(Consumer<KeyEvent> listener) {
        keyReleasedEvent.removeListener(listener);
    }

    // Fire Event

    public void onMouseMoved(MouseEvent event) {
        mouseMovedEvent.fire(event);
    }

    public void onMouseDragged(MouseEvent event) {
        mouseDraggedEvent.fire(event);
        mouseMovedEvent.fire(event);
    }

    public void onMousePressed(MouseEvent event) {
        mousePressedEvent.fire(event);
    }

    public void onMouseReleased(MouseEvent event) {
        mouseReleasedEvent.fire(event);
    }

    public void onMouseExited(MouseEvent event) {
        mouseExitedEvent.fire(event);
    }

    public void onKeyPressed(KeyEvent event) {
        keyPressedEvent.fire(event);
    }

    public void onKeyReleased(KeyEvent event) {
        keyReleasedEvent.fire(event);
    }
}
