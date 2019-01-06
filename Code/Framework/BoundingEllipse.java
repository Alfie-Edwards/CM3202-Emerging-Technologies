package Framework;

import java.io.Serializable;

public class BoundingEllipse implements Serializable {
    private final double x, y, r1, r2, rotation;

    public BoundingEllipse(double x, double y, double r1, double r2, double rotation) {
        this.x = x;
        this.y = y;
        this.r1 = Math.abs(r1);
        this.r2 = Math.abs(r2);
        this.rotation = rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR1() {
        return r1;
    }

    public double getR2() {
        return r2;
    }

    public double getRotation() {
        return rotation;
    }
}
