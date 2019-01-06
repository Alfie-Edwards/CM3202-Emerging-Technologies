package Client.Views;

import Client.Views.Geometry.GeometryService;
import Framework.*;
import Framework.Remote.Shape;
import Framework.Remote.User;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.rmi.RemoteException;
import java.util.List;

public class CanvasView extends Canvas {

    private static final Color CLEAR_COLOUR = Color.WHITE;
    private static final Color SELECT_COLOUR = Color.gray(0.2, 0.8);
    private static final double CURSOR_SIZE = 32;

    GraphicsContext graphicsContext;
    private List<Shape> shapes;
    private List<User> users;
    private Shape selectedShape;

    public CanvasView() {
        super();
        this.graphicsContext = this.getGraphicsContext2D();
        setFocusTraversable(true);
    }

    // Setters

    public void updateUsers(List<User> users) {
        this.users = users;
    }

    public void updateShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public void updateSelectedShape(Shape shape) {
        this.selectedShape = shape;
    }

    // Redraw Contents

    public void redraw() {

        graphicsContext.setFill(CLEAR_COLOUR);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        try {
            if (shapes != null)
                for (Shape shape : shapes)
                    if (shape.equals(selectedShape))
                        drawSelectedShape(shape.getType(), shape.getBoundingEllipse(), shape.getColour());
                    else
                        drawShape(shape.getType(), shape.getBoundingEllipse(), shape.getColour());

            if (users != null)
                for (User user : users) {
                    double x = user.getCursorX();
                    double y = user.getCursorY();
                    if (x > 0 && y > 0)
                        drawCursor(x, y, user.getColour());
                }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Drawing Methods

    private void drawCursor(double x, double y, Colour colour) {
        graphicsContext.save();
        graphicsContext.translate(x * getWidth(), y * getHeight());
        graphicsContext.setFill(getJavaFXColour(colour));

        drawGeometry(GeometryService.getCursorGeometry(CURSOR_SIZE), 1, 1, false);

        graphicsContext.restore();
    }

    private void drawSelectedShape(ShapeTypes type, BoundingEllipse boundingEllipse, Colour colour) {
        graphicsContext.save();
        graphicsContext.setFill(getJavaFXColour(colour));
        graphicsContext.setStroke(SELECT_COLOUR);
        graphicsContext.setLineWidth(2);
        graphicsContext.setLineDashes(3, 5);

        drawShape(type, boundingEllipse, true);

        graphicsContext.restore();
    }

    private void drawShape(ShapeTypes type, BoundingEllipse boundingEllipse, Colour colour) {
        graphicsContext.save();
        graphicsContext.setFill(getJavaFXColour(colour));

        drawShape(type, boundingEllipse, false);

        graphicsContext.restore();
    }

    private void drawShape(ShapeTypes type, BoundingEllipse boundingEllipse, boolean stroke) {
        double canvasWidth = getWidth();
        double canvasHeight = getHeight();
        double x = boundingEllipse.getX() * canvasWidth;
        double y = boundingEllipse.getY() * canvasHeight;
        double r1 = boundingEllipse.getR1() * canvasWidth;
        double r2 = boundingEllipse.getR2() * canvasHeight;

        graphicsContext.translate(x, y);
        graphicsContext.rotate(-boundingEllipse.getRotation());
        graphicsContext.translate(-x, -y);

        int sides = type.numSides();
        if (sides == 0) { // Special case for circle;
            graphicsContext.fillOval(x - r1, y - r2, r1 * 2, r2 * 2);
            if (stroke)
                graphicsContext.strokeOval(x - r1, y - r2, r1 * 2, r2 * 2);
        }
        else
            drawGeometry(GeometryService.getUprightPolygonGeometry(sides, boundingEllipse), canvasWidth, canvasHeight, stroke);
    }

    // Drawing Helper Methods

    private void drawGeometry(double[] geometry, double xScale, double yScale, boolean stroke) {
        int nPoints = geometry.length / 2;
        double[] xPoints = new double[nPoints],
                 yPoints = new double[nPoints];
        for (int i = 0; i < nPoints; i ++) {
            xPoints[i] = geometry[2 * i] * xScale;
            yPoints[i] = geometry[2 * i + 1] * yScale;
        }
        graphicsContext.fillPolygon(xPoints, yPoints, nPoints);
        if (stroke)
            graphicsContext.strokePolygon(xPoints, yPoints, nPoints);
    }

    private Color getJavaFXColour(Colour colour) {
        return Color.color(colour.getR(), colour.getG(), colour.getB());
    }
}
