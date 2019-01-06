package Client.Views.Geometry;

import Framework.BoundingEllipse;

public class GeometryService {

    // Ignores rotation in bounding ellipse
    public static double[] getUprightPolygonGeometry(int sides, BoundingEllipse boundingEllipse) {

        double[] geometry = new double[sides * 2];
        double x = boundingEllipse.getX();
        double y = boundingEllipse.getY();
        double r1 = boundingEllipse.getR1();
        double r2 = boundingEllipse.getR2();

        for (int i = 0; i < sides; i ++) {
            double theta = (((i - 0.5) / sides) + 0.25) * Math.PI * 2; // Angle offset so flat side at bottom (upright)
            geometry[2 * i] = x + Math.cos(theta) * r1;
            geometry[2 * i + 1] = y + Math.sin(theta) * r2;
        }

        return geometry;
    }

    public static double[] getPolygonGeometry(int sides, BoundingEllipse boundingEllipse) {

        double[] geometry = getUprightPolygonGeometry(sides, boundingEllipse);
        double ox = boundingEllipse.getX();
        double oy = boundingEllipse.getY();
        double cosA = Math.cos(boundingEllipse.getRotation());
        double sinA = Math.sin(boundingEllipse.getRotation());

        for (int i = 0; i < sides; i ++) {
            double x = geometry[2 * i];
            double y = geometry[2 * i + 1];
            geometry[2 * i] = (x - ox) * cosA - (y - oy) * sinA + ox;
            geometry[2 * i + 1] = (y - oy) * cosA + (x - ox) * sinA + oy;
        }

        return geometry;
    }

    public static double[] getCursorGeometry(double size) {
        double width = 0.70711 * size;
        double height = size;
        double indentX = 0.28701 * size; // 0.75 * sin(pi/9)
        double indentY = 0.69291 * size; // 0.75 * cos(pi/9)
        return new double[] {0, 0, width, width, indentX, indentY, 0, height};
    }
}
