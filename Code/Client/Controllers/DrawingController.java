package Client.Controllers;

import Client.Models.ClientModel;
import Client.Models.UserServant;
import Client.Views.CanvasView;
import Framework.*;
import Framework.Remote.Shape;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.rmi.RemoteException;

public class DrawingController implements Controller {

    private static final double SQRT2 = Math.sqrt(2);

    private final CanvasEventRouter canvasEventRouter;
    private final ClientModel clientModel;
    private CanvasView canvasView;
    private double mouseX, mouseY, originX, originY;
    private boolean rotationDisabled, centerShapeAtOrigin, allowIrregularity;
    private ShapeTypes selectedShapeType;
    private Shape shapeBeingDrawn;

    public DrawingController(ClientModel clientModel, CanvasEventRouter canvasEventRouter, CanvasView canvasView) {
        this.clientModel = clientModel;
        this.canvasEventRouter = canvasEventRouter;
        this.canvasView = canvasView;

        selectedShapeType = null;
        rotationDisabled = false;
        centerShapeAtOrigin = false;
        allowIrregularity = false;
    }

    public void start() {
        canvasEventRouter.addMousePressedListener(this::onMousePressed);
        canvasEventRouter.addMouseDraggedListener(this::onMouseDragged);
        canvasEventRouter.addMouseReleasedListener(this::onMouseReleased);
        canvasEventRouter.addKeyPressedListener(this::onKeyEvent);
        canvasEventRouter.addKeyReleasedListener(this::onKeyEvent);
    }

    public void stop() {
        canvasEventRouter.removeMousePressedListener(this::onMousePressed);
        canvasEventRouter.removeMouseDraggedListener(this::onMouseDragged);
        canvasEventRouter.removeMouseReleasedListener(this::onMouseReleased);
        canvasEventRouter.removeKeyPressedListener(this::onKeyEvent);
        canvasEventRouter.removeKeyReleasedListener(this::onKeyEvent);
        selectedShapeType = null;
    }

    // Setters

    public void setSelectedShapeType(ShapeTypes shapeType) {
        selectedShapeType = shapeType;
    }

    // Events

    private void onMousePressed(MouseEvent event) {
        if (!event.isPrimaryButtonDown() || selectedShapeType == null) return;
        mouseX = event.getX() / canvasView.getWidth();
        mouseY = event.getY() / canvasView.getHeight();
        originX = mouseX;
        originY = mouseY;

        UserServant currentUser = clientModel.getCurrentUser();
        shapeBeingDrawn = clientModel.addShape(selectedShapeType, 0, generateBoundingEllipse(), currentUser.getColour());
        currentUser.setSelectedShape(shapeBeingDrawn);
    }

    private void onMouseDragged(MouseEvent event) {
        if (!event.isPrimaryButtonDown() || selectedShapeType == null) return;
        mouseX = event.getX() / canvasView.getWidth();
        mouseY = event.getY() / canvasView.getHeight();

        rotationDisabled = event.isShiftDown();
        centerShapeAtOrigin = event.isAltDown();

        updateShapeBeingDrawn();
    }

    private void onMouseReleased(MouseEvent event) {
        if (event.isPrimaryButtonDown()) return;
        shapeBeingDrawn = null;
    }

    private void onKeyEvent(KeyEvent event) {
        rotationDisabled = event.isShiftDown();
        centerShapeAtOrigin = event.isAltDown();
        allowIrregularity = event.isControlDown();

        updateShapeBeingDrawn();
    }

    private void updateShapeBeingDrawn() {
        if (shapeBeingDrawn == null) return;

        try {
            shapeBeingDrawn.setBoundingEllipse(generateBoundingEllipse());
        }
        catch (RemoteException e) {
            e.printStackTrace();
            //TODO: Proper error handling
        }
    }

    private BoundingEllipse generateBoundingEllipse() {
        double x = originX;
        double y = originY;
        double r1 = (mouseX - originX) / 2;
        double r2 = (mouseY - originY) / 2;
        double rotation = calculateRotation();

        if (rotationDisabled && !allowIrregularity && !centerShapeAtOrigin) {
            double ratio = Math.abs(r1) / Math.abs(r2);
            if (ratio > 1)
                r1 /= ratio;
            else
                r2 *= ratio;
        }
        if (centerShapeAtOrigin) {
            r1 *= 2;
            r2 *= 2;
        }
        else {
            x += r1;
            y += r2;
        }

        if (!rotationDisabled || (centerShapeAtOrigin && !allowIrregularity)) {
            r1 = Math.hypot(r1, r2);
            r2 = r1;
        }
        else {
            r1 *= SQRT2;
            r2 *= SQRT2;
        }


        return new BoundingEllipse(x, y, r1, r2, rotation);
    }

    private double calculateRotation() {
        if (rotationDisabled) return 0;
        double radians = Math.atan2(mouseX - originX, mouseY - originY);
        double degrees = radians * 180 / Math.PI;
        int sides = selectedShapeType.numSides();
        if (sides != 0)
            degrees += 180.0 / sides;
        return degrees;
    }
}
