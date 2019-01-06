package Framework;

import java.io.Serializable;
import java.util.Random;

public class Colour implements Serializable{

    private double r, g, b;
    private static final Random rng = new Random();

    public Colour(double r, double g, double b) {

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public static Colour random() {
        return new Colour(rng.nextDouble(), rng.nextDouble(), rng.nextDouble());
    }
}
