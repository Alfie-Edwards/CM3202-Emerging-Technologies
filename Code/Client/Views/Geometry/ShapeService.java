package Client.Views.Geometry;

import Framework.BoundingEllipse;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


public class ShapeService{

    public static Shape CreatePolygon(int sides, double x, double y, double radius) {
        if (sides == 0)
            return CreateCircle(x, y, radius);
        return CreatePolygon(sides, x, y, radius, radius);
    }

    public static Shape CreateCircle(double x, double y, double radius) {
        return new Circle(x, y, radius);
    }

    public static Shape CreatePolygon(int sides, double x, double y, double r1, double r2) {
        if (sides == 0)
            return CreateEllipse(x, y, r1, r2);

        BoundingEllipse boundingEllipse = new BoundingEllipse(x, y, r1, r2, 0);
        double[] geometry = GeometryService.getUprightPolygonGeometry(sides, boundingEllipse);
        return new Polygon(geometry);
    }

    public static Shape CreateEllipse(double x, double y, double r1, double r2) {
        return new Ellipse(x, y, r1, r2);
    }

    public static Shape CreateCursor(double x, double y, double size) {
        double[] geometry = GeometryService.getCursorGeometry(size);
        for (int i = 0; i < 4; i++) {
            geometry[2 * i] += x;
            geometry[2 * i + 1] += y;
        }
        return new Polygon(geometry);
    }
}
