package Client.Controllers;

import Client.Models.ClientModel;
import Client.Views.CanvasView;
import Client.Views.Geometry.GeometryService;
import Framework.BoundingEllipse;
import Framework.Remote.Shape;
import Framework.ShapeTypes;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import java.rmi.RemoteException;
import java.util.List;



public class SelectionController implements Controller {

    private static double KEYBOARD_NUDGE_AMOUNT = 0.01;

    ClientModel clientModel;
    private CanvasEventRouter canvasEventRouter;
    private CanvasView canvasView;
    private boolean moveMode;
    private double lastMouseX, lastMouseY;

    public SelectionController(ClientModel clientModel, CanvasEventRouter canvasEventRouter, CanvasView canvasView) {
        this.clientModel = clientModel;
        this.canvasEventRouter = canvasEventRouter;
        this.canvasView = canvasView;
        moveMode = false;
        lastMouseX = -1;
        lastMouseY = -1;
    }

    @Override
    public void start() {
        canvasEventRouter.addMousePressedListener(this::onMousePressed);
        canvasEventRouter.addMouseDraggedListener(this::onMouseDragged);
        canvasEventRouter.addKeyPressedListener(this::onKeyPressed);
    }

    @Override
    public void stop() {
        canvasEventRouter.removeMousePressedListener(this::onMousePressed);
        canvasEventRouter.removeKeyPressedListener(this::onKeyPressed);
    }

    // Setters

    public void setMoveMode(boolean moveMode) {
        this.moveMode = moveMode;
    }

    // Events

    private void onKeyPressed(KeyEvent keyEvent) {
        Shape selectedShape = clientModel.getCurrentUser().getSelectedShape();
        if (selectedShape == null) return;
        try {
            BoundingEllipse boundingEllipse = selectedShape.getBoundingEllipse();
            switch(keyEvent.getCode()) {
                case LEFT:
                    selectedShape.setBoundingEllipse(new BoundingEllipse(
                            boundingEllipse.getX() - KEYBOARD_NUDGE_AMOUNT,
                            boundingEllipse.getY(),
                            boundingEllipse.getR1(),
                            boundingEllipse.getR2(),
                            boundingEllipse.getRotation()));
                    break;
                case RIGHT:
                    selectedShape.setBoundingEllipse(new BoundingEllipse(
                            boundingEllipse.getX() + KEYBOARD_NUDGE_AMOUNT,
                            boundingEllipse.getY(),
                            boundingEllipse.getR1(),
                            boundingEllipse.getR2(),
                            boundingEllipse.getRotation()));
                    break;
                case UP:
                    selectedShape.setBoundingEllipse(new BoundingEllipse(
                            boundingEllipse.getX(),
                            boundingEllipse.getY() - KEYBOARD_NUDGE_AMOUNT,
                            boundingEllipse.getR1(),
                            boundingEllipse.getR2(),
                            boundingEllipse.getRotation()));
                    break;
                case DOWN:
                    selectedShape.setBoundingEllipse(new BoundingEllipse(
                            boundingEllipse.getX(),
                            boundingEllipse.getY() + KEYBOARD_NUDGE_AMOUNT,
                            boundingEllipse.getR1(),
                            boundingEllipse.getR2(),
                            boundingEllipse.getRotation()));
                    break;
            }
        } catch (RemoteException e) {
            clientModel.getCurrentUser().setSelectedShape(null);
        }
    }

    private void onMousePressed(MouseEvent mouseEvent) {
        double x = mouseEvent.getX() / canvasView.getWidth();
        double y = mouseEvent.getY() / canvasView.getHeight();
        Shape shapeAtPoint = getShapeAtPoint(x, y);


        switch (mouseEvent.getButton()) {
            case NONE:
                return;
            case PRIMARY:
                if (!moveMode) return;
                if (shapeAtPoint == null)
                    clientModel.getCurrentUser().setSelectedShape(null);
                clientModel.getCurrentUser().setSelectedShape(shapeAtPoint);
                lastMouseX = mouseEvent.getX() / canvasView.getWidth();
                lastMouseY = mouseEvent.getY() / canvasView.getHeight();
                return;

            case MIDDLE:
                if (shapeAtPoint == null) return;
                clientModel.removeShape(clientModel.getShapes().indexOf(shapeAtPoint));
                return;

            case SECONDARY:
                if (moveMode) {
                    lastMouseX = mouseEvent.getX() / canvasView.getWidth();
                    lastMouseY = mouseEvent.getY() / canvasView.getHeight();
                }
                else {
                    if (shapeAtPoint == null) return;
                    try {
                        shapeAtPoint.setColour(clientModel.getCurrentUser().getColour());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return;
        }
    }

    private void onMouseDragged(MouseEvent mouseEvent) {
        try {
            Shape selectedShape = clientModel.getCurrentUser().getSelectedShape();
            if (!moveMode || selectedShape == null) return;
            BoundingEllipse boundingEllipse = selectedShape.getBoundingEllipse();
            double mouseX = mouseEvent.getX() / canvasView.getWidth();
            double mouseY = mouseEvent.getY() / canvasView.getHeight();

            switch (mouseEvent.getButton()) {
                case NONE:
                    return;
                case PRIMARY:
                    double dx = mouseX - lastMouseX;
                    double dy = mouseY - lastMouseY;
                    selectedShape.setBoundingEllipse(new BoundingEllipse(
                       boundingEllipse.getX() + dx,
                       boundingEllipse.getY() + dy,
                       boundingEllipse.getR1(),
                       boundingEllipse.getR2(),
                       boundingEllipse.getRotation()
                    ));
                    lastMouseX = mouseX;
                    lastMouseY = mouseY;
                    return;
                case SECONDARY:
                    double prevAngle = Math.atan2(lastMouseX - boundingEllipse.getX(), lastMouseY - boundingEllipse.getY());
                    double newAngle = Math.atan2(mouseX - boundingEllipse.getX(), mouseY - boundingEllipse.getY());
                    double deltaAngle = (newAngle - prevAngle) * 180 / Math.PI;
                    selectedShape.setBoundingEllipse(new BoundingEllipse(
                            boundingEllipse.getX(),
                            boundingEllipse.getY(),
                            boundingEllipse.getR1(),
                            boundingEllipse.getR2(),
                            boundingEllipse.getRotation() + deltaAngle
                    ));
                    lastMouseX = mouseX;
                    lastMouseY = mouseY;
                    return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Shape Intersection
    private Shape getShapeAtPoint(double x, double y) {
        List<Shape> shapeList = clientModel.getShapes();

        if (shapeList == null) return null;

        for (int i = shapeList.size() - 1; i >= 0; i--) {
            Shape shape = shapeList.get(i);
            ShapeTypes type = null;
            BoundingEllipse boundingEllipse;
            try {
                type = shape.getType();
                boundingEllipse = shape.getBoundingEllipse();
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }

            if (type == ShapeTypes.Ellipse) {
                double ox = boundingEllipse.getX();
                double oy = boundingEllipse.getY();
                double cosA = Math.cos(-boundingEllipse.getRotation());
                double sinA = Math.sin(-boundingEllipse.getRotation());

                // Transform into space where ellipse is unit circle
                double x2 = ((x - ox) * cosA - (y - oy) * sinA) / boundingEllipse.getR1();
                double y2 = ((y - oy) * cosA + (x - ox) * sinA) / boundingEllipse.getR2();
                if (Math.hypot(x2, y2) <= 1)
                    return shape;
            }
            else if (new Polygon(GeometryService.getPolygonGeometry(type.numSides(), boundingEllipse)).contains(x, y))
                return shape;
        }
        return null;
    }
}
